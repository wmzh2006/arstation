/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: DownloadManager.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-7-19 下午06:26:46
 *******************************************************************************/
package com.funoble.myarstation.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.socket.DownloadProtocol;
import com.funoble.myarstation.socket.DownloadSocketHandler;
import com.funoble.myarstation.socket.protocol.MBRspDownLoadPic;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.SocketErrorType;
import com.funoble.myarstation.socket.protocol.ProtocolType.Url;
import com.funoble.myarstation.utils.ImageUtil;
import com.funoble.myarstation.view.MessageEventID;


public class DownloadManager implements DownloadSocketHandler, Runnable, Handler.Callback {
	private static String TAG = "Download";
	private Queue<DownloadData> queue;
	private DownloadProtocol downloadProtocol;
	private Thread th;
    private static final String pathDownlaod = "/lyingdice/cache/img/";
	private File sdCard = android.os.Environment.getExternalStorageDirectory();
	private String path = sdCard.getPath()+pathDownlaod;
	private String tempPath = path+"temp.data";
	private DownloadData currentTask;
	private Handler iMainHandler;
	private boolean ibsDownload;
	private Handler iHandler;
	private int iMaxReconnect = 10;
	private int iReconnectCount = 0;
	
	private final  int  REDOWNLOAD = 1;
	
	public DownloadManager(Handler aHandler) {
		queue = new LinkedList<DownloadData>();
		downloadProtocol = new DownloadProtocol(this);
		iMainHandler = aHandler;
		iHandler = new Handler(this);
		ibsDownload = false;
	}
	
	public boolean addTask(String aFileName) {
		if(aFileName == null || aFileName.contains("default") || aFileName.length() <= 0) {
			return false;
		}
		DownloadData data = new DownloadData();
		data.iFileName = aFileName;
		if(currentTask != null && currentTask.iFileName.equals(aFileName)) {
			return false;
		}
		return addTask(data);
	}
	
	public boolean addTask(DownloadData data) {
		if(contains(data) || data.iFileName == null || data.iFileName.length() <= 0) {
			return false;
		}
		return queue.add(data);
	}
	
	public void removeTask() {
		while(queue.size() > 0) {
			queue.remove();
		}
	}
	
	public void StopTask() {
		ibsDownload = false;
	}
	
	public boolean removeTaskByFileName(String FileName) {
		DownloadData e = findTaskByName(FileName);
		 if( e != null) {
			 return queue.remove(e);
		 }
		 return false;
	}
	
	private boolean contains(DownloadData e) {
        Object [] column = queue.toArray();  
        for(int i = 0 ; i < column.length; i++){  
            if(((DownloadData)column[i]).equals((DownloadData)e)){  
                return true ;  
            }  
        }  
        return false;      
	}
	
	private DownloadData findTaskByName(String aFileName) {
		DownloadData [] column = (DownloadData[]) queue.toArray();
		 for(int i = 0; i < column.length; i++) {
			 if(column[i].iFileName.equals(aFileName)) {
				 return column[i];
			 }
		 }
		 return null;
	}
	
	private DownloadData getTask() {
		return queue.poll();
	}
	
	public synchronized boolean startTask() {
		if(!ibsDownload && nextTask()!= null) {
			ibsDownload = true;
			iReconnectCount = 0;
			initSaveFile();
			keepUp();
			return true;
		}
		return false;
	}
	
	public boolean isDownLoad() {
		return ibsDownload;
	}
	
	/**
	 * 删除所有任务包括当前任务
	 */
	public void cleanTask() {
		currentTask = null;
		ibsDownload = false;
		iReconnectCount = 0;
		downloadProtocol.stop();
		removeTask();
	}
	
	private boolean startNextTask() {
		if(nextTask()!= null) {
			iReconnectCount = 0;
			initSaveFile();
			keepUp();
			return true;
		}
		else {
			iReconnectCount = 0;
			downloadProtocol.stop();
			ibsDownload = false;
			currentTask = null;
			Message msg = new Message();
			msg.what = MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_PIC;
			iMainHandler.sendMessage(msg);
		}
		return false;
	}
	
	private void keepUp() {
		if(ibsDownload) {
			th = new Thread(this);
			th.start();
		}
	}
	
	public int getTaskSize() {
		return queue.size();
	}

	@Override
	public void OnDlReportError(Object socket, int aErrorCode) {
//		Log.d("Download","OnDlReportError..........");
		if(aErrorCode == SocketErrorType.Error_TimeOut) {
			reconect();
		}
	}

	@Override
	public void OnDlProcessCmd(Object socket, int aMobileType, int aMsgID) {
		MobileMsg pMobileMsg = (MobileMsg) socket;
		switch (aMobileType) {
		case IM.IM_RESPONSE:
		{
			switch (aMsgID) {
			case RspMsg.MSG_RSP_DOWNLOAD_PIC:
				MBRspDownLoadPic rspDownLoadPic = (MBRspDownLoadPic)pMobileMsg.m_pMsgPara;
				if(rspDownLoadPic == null) {
					return;
				}
//				Log.d("Download",rspDownLoadPic.toString());
				if(rspDownLoadPic.iResult == ResultCode.SUCCESS) {
					sendMessage(RspMsg.MSG_RSP_DOWNLOAD_PIC, rspDownLoadPic);
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspUpLoadPic, rspUpdUpLoadPic);
				}
				else {
//					messageEvent(rspUpdUpLoadPic.nResult, rspUpdUpLoadPic.iMsg);
				}
				break;
			default:
				break;
			}
		}
		break;
		
		default:
			break;
		}
	}
	
	@Override
	public void OnDlConnect(Object socket) {
	}

	@Override
	public void OnDlClose(Object socket) {
//		Log.d("Download","OnDlClose..........");
		if(getTaskSize() > 0) {
			reconect();
		}
	}

	@Override
	public void run() {
//		while(ibDowning) {
			if(!downloadProtocol.isConnect()) {
				if(downloadProtocol.create(Url.ip, Url.port)) {
					downloadProtocol.requestDownloadPic(currentTask.iFileName, currentTask.iStartIndex);
//					Log.d("Download"," creator start download pic..... iReconnectCount = " + iReconnectCount);
				}
			}
			else {
//				Log.d("Download","start download pic");
				downloadProtocol.requestDownloadPic(currentTask.iFileName, currentTask.iStartIndex);
			}
//		}
	}
	
	public boolean saveData(DownloadData date) {
		if(date == null && !sdcardUsableness()) {
			return false;
		}
//		Log.d("Download","DownLoad saveData...........");
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(tempPath, "rwd");
			int length = date.iCurrentSize;
			randomAccessFile.seek(date.iStartIndex - length);
			// 将要下载的文件写到保存在保存路径下的文件中
			randomAccessFile.write(date.data, 0, length);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				randomAccessFile.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	private boolean sdcardUsableness() {
    	if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
    		return true;
    	}
    	return false;
	}
	
	private DownloadData nextTask() {
		return currentTask = getTask();
	}
	
	private boolean reName() {
		File file = new File(tempPath);
		if(!file.exists() && currentTask == null){
			return false;
		}  
		boolean bool = file.renameTo(new File(path + currentTask.iFileName));
//		Log.i("renamebool","rename"+bool);
		return bool;
	}
	
	private String getUserIDbyFileName() {
		String userID = "";
		if(currentTask == null) {
			return "";
		}
		int index = currentTask.iFileName.indexOf("_");
		if(index != -1) {
			userID = currentTask.iFileName.substring(0, index);
		}
		return userID;
	}
	
	private void deleteOldImage() {
		File file = new File(path);
		if(!file.exists() && currentTask == null){
			return;
		} 
		else if(file.isDirectory()){
			File[] files = file.listFiles();
			for(int i = 0; i < files.length; i++) {
				String filename = files[i].getName();
				if(filename.contains(getUserIDbyFileName()) &&
						isHeadImage(currentTask.iFileName) && 
						isHeadImage(filename)) {
					files[i].delete();
				}
				else if(filename.contains(getUserIDbyFileName()) && 
						isBigImage(currentTask.iFileName) &&
						isBigImage(filename)) {
					files[i].delete();
				}
			}
		}
	}
	
	private void initSaveFile() {
		try {
			if(sdcardUsableness() && currentTask != null) {
				File file = new File(path);
				if (!file.exists()) {
//					file.getParentFile().mkdirs();
//					file.createNewFile();
					file.mkdirs();
				}
				else if(file.exists() && file.isFile()){
					file.delete();
					file.mkdirs();
				}
				else {
                    File[] files = file.listFiles();
                    if(files != null && files.length > 200) {//最多存储200张图片
                        for(int i = 0, len = files.length; i < len; i++)
                        {
                            files[i].delete();
                        }
                    }
                }
				File imgFile = new File(tempPath);
//				Log.d("Download","download init()..........." + currentTask.toString());
				// 本地访问文件
				RandomAccessFile accessFile = new RandomAccessFile(imgFile, "rwd");
				accessFile.setLength(currentTask.iFileTotalSize);
				accessFile.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case ResultCode.ERR_DOWNLOAD_NOT_FOUND:
			startNextTask();
			break;
		case RspMsg.MSG_RSP_DOWNLOAD_PIC:
			MBRspDownLoadPic rspDownLoadPic = (MBRspDownLoadPic)msg.obj;
			if(currentTask == null) {
				break;
			}
			currentTask.iFileName = rspDownLoadPic.iFileName;
			currentTask.iStartIndex = rspDownLoadPic.iStartIndex;
			currentTask.iFileTotalSize = rspDownLoadPic.iTotalSize;
			currentTask.iCurrentSize = rspDownLoadPic.iSize;
			currentTask.data = rspDownLoadPic.iData;
			saveData(currentTask);
//			Log.d("Download",currentTask.toString());
			if(!currentTask.isComplete()) {
				keepUp();
			}
			else {
//				Log.d("Download","next task-------------------------------!" + getTaskSize() +"");
				deleteOldImage();
				if(reName()) {
					noticeSgPicCpl();
				}
				startNextTask();
			}
			break;
		case REDOWNLOAD:
			downloadProtocol.stop();
			keepUp();
			break;
		default:
			break;
		}
		return false;
	}
	
	private void sendMessage(int msgID, Object msg) {
		Message message = new Message();
		message.what = msgID;
		message.obj = msg;
		iHandler.sendMessage(message);
	}
	
	public Bitmap getImage(String aFileName) {
		Bitmap img = null;
		if(aFileName == null || aFileName.length() <= 0) {
			return img;
		}
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			FileInputStream fis = null;
			BitmapFactory.Options opt = new BitmapFactory.Options();  
			 opt.inPreferredConfig = Bitmap.Config.RGB_565;   
			 opt.inPurgeable = true;  
			 opt.inInputShareable = true;  
			try {
				File file = new File(path + aFileName);  
				fis = new FileInputStream(file);
				img = BitmapFactory.decodeStream(fis, null, opt);
				img = ImageUtil.getRoundedCornerBitmap(img, 7.0f);
			}
			catch(Exception e) {
			}
			finally {
				if(img == null) {
					if(aFileName.contains("_h")) {
						opt.inSampleSize = 4;//图片宽高都为原来的二分之一，即图片为原来的四分之一
					}
					InputStream is = MyArStation.getInstance().getApplicationContext().getResources().openRawResource(defaultRes(aFileName));  
					img = BitmapFactory.decodeStream(is,null,opt);  
					img = ImageUtil.getRoundedCornerBitmap(img, 7.0f);
					addTask(aFileName);
					startTask();
				}
				if(fis != null) {
					try {
						fis.close();
					}
					catch(Exception e){
						
					}
				}
			}
		}
		return img;
	}
	
	public Bitmap getImageNoDefault(String aFileName) {
		Bitmap img = null;
		if(aFileName == null || aFileName.length() <= 0) {
			return img;
		}
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			FileInputStream fis = null;
			try {
//				Log.d(TAG, "getImage getTaskSize = "+getTaskSize());
				File file = new File(path + aFileName);  
				fis = new FileInputStream(file);
				img = BitmapFactory.decodeStream(fis);
				img = ImageUtil.getRoundedCornerBitmap(img, 7.0f);
			}
			catch(Exception e) {
			}
			finally {
				if(img == null) {
//					img = BitmapFactory.decodeResource(GameMain.mGameMain.getResources(), R.drawable.mypicdef);
					addTask(aFileName);
//					Log.d(TAG, "getTaskSize = "+getTaskSize());
					startTask();
				}
				if(fis != null) { 
					try {
						fis.close();
					}
					catch(Exception e){
						
					}
				}
			}
		}
		return img;
	}
	
	private void reconect() {
		if( iReconnectCount < iMaxReconnect) {
			iReconnectCount++;
//			Log.d(TAG, "reconect getTaskSize = " +getTaskSize() + "iReconnectCount"+ iReconnectCount);
			sendMessage(REDOWNLOAD, "");
		}
		else {
//			Log.d(TAG, "reconect getTaskSize = " +getTaskSize() + "iReconnectCount"+ iReconnectCount);
			startNextTask();
		}
	}
	
	private void noticeSgPicCpl() {
		Message message = new Message();
		message.what = MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_SINGLE_PIC;
		message.obj = currentTask.iFileName != null ? currentTask.iFileName : "";
		iMainHandler.sendMessage(message);
	}
	
	private boolean isHeadImage(String aFile) {
		if(aFile != null) {
			return aFile.contains("h.jpg");
		}
		return false;
	}
	
	private boolean isBigImage(String aFile) {
		if(aFile != null) {
			return !aFile.contains("h.jpg");
		}
		return false;
	}
	
	private int defaultRes(String imageName) {
		int resID;
		if(imageName.contains("default0")) {
			resID = R.drawable.default0;
		}
		else if(imageName.contains("default1")) {
			resID = R.drawable.default3;
		}
		else if(imageName.contains("default2")) {
			resID = R.drawable.default0;
		}
		else if(imageName.contains("default3")) {
			resID = R.drawable.default3;
		}
		else if(imageName.contains("defaultbig0")) {
			resID = R.drawable.defaultbig0;
		}
		else if(imageName.contains("defaultbig1")) {
			resID = R.drawable.defaultbig0;
		}
		else if(imageName.contains("defaultbig2")) {
			resID = R.drawable.defaultbig3;
		}
		else if(imageName.contains("defaultbig3")) {
			resID = R.drawable.defaultbig3;
		}
		else {
			resID = R.drawable.mypicdef;
		}
		return resID;
	}
}
