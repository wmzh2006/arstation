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
import java.util.LinkedList;
import java.util.Queue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.communication.RestClient;
import com.funoble.myarstation.communication.RestMsg;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.imagemanager.ImageSaveManager;
import com.funoble.myarstation.socket.protocol.ProtocolType.HTTPERROR;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.DataPath;
import com.funoble.myarstation.utils.ImageUtil;
import com.funoble.myarstation.utils.SettingManager;
import com.funoble.myarstation.view.MessageEventID;


public class HttpDownloadManager implements Handler.Callback {
	private final String MAINURL = "http://www.funnoble.com:801/";
	private final String PAKURL = MAINURL+"pak/";
	
	private static String TAG = "Download";
	private static final int DOWANLOAD_TYPE_PIC = 0;
	private static final int DOWANLOAD_TYPE_ICON = 1;
	private static final int DOWANLOAD_TYPE_PAK = 2;
	private Queue<TaskItem> queue;
    private final String pathBase = "/lyingdice/.cache/.res/";
    
	private File sdCard = android.os.Environment.getExternalStorageDirectory();
	private String path = sdCard.getPath()+pathBase;
	private Handler iMainHandler;
	private boolean ibsDownload;
	private Handler iHandler;
	private Handler iTimeHandler;
	private TaskItem  currentTask;
	private ImageSaveManager ImageSave;
	
	Runnable runnable=new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//要做的事情
			//Tools.debug("准备下载...");
			startNextTask();
			//
			//Tools.debug("停止计时器");
			iTimeHandler.removeCallbacks(runnable);
		}
	};
	
	private int[] iStoreIcon = {
			R.drawable.new_shop_car00,
			R.drawable.new_shop_car01,
			R.drawable.new_shop_car02,
			R.drawable.new_shop_car03,
			R.drawable.new_shop_car04,
			R.drawable.new_shop_car05,
			R.drawable.new_shop_car06,
			R.drawable.new_shop_car07,
			R.drawable.new_shop_car08,
			R.drawable.new_shop_car09,
			R.drawable.new_shop_car10,
			R.drawable.new_shop_car11
	};
	
	public HttpDownloadManager(Handler aHandler) {
		queue = new LinkedList<TaskItem>();
		iMainHandler = aHandler;
		iHandler = new Handler(this);
		iTimeHandler = new Handler();
		ibsDownload = false;
		ImageSave = new ImageSaveManager();
	}
	
	public boolean addTask(String aFileName, int type) {
		//
		if(aFileName == null || aFileName.contains("default") || aFileName.length() <= 0) {
			return false;
		}
		//
		if(currentTask != null && currentTask.fileName.equals(aFileName)) {
			return false;
		}
		//
		if(contains(aFileName)) {
			return false;
		}
		//是否有sdcard
		if (!android.os.Environment.getExternalStorageState().equals(  
			    	android.os.Environment.MEDIA_MOUNTED)) {  
			return false;  
		}
//		Tools.debug("加入下载任务：" + aFileName);
		return queue.add(new TaskItem(aFileName, type));
	}
	
	public void removeTask() {
		while(queue.size() > 0) {
			queue.remove();
		}
	}
	
	public void StopTask() {
//		Tools.debug("停止下载任务");
		if(ibsDownload) {
			//停止下载计时器
			iTimeHandler.removeCallbacks(runnable);
			ibsDownload = false;
		}
	}
	
	public boolean removeTaskByFileName(String FileName) {
		TaskItem e = findTaskByName(FileName);
		 if( e != null) {
			 return queue.remove(e);
		 }
		 return false;
	}
	
	private boolean contains(String e) {
        Object [] column = queue.toArray();  
        for(int i = 0 ; i < column.length; i++){  
            if(((TaskItem)column[i]).fileName.equals(e)){  
                return true ;  
            }  
        }  
        return false;      
	}
	
	private TaskItem findTaskByName(String aFileName) {
		TaskItem [] column = (TaskItem[]) queue.toArray();
		if(column == null) {
			return null;
		}
		 for(int i = 0; i < column.length; i++) {
			 if(column[i].fileName.equals(aFileName)) {
				 return column[i];
			 }
		 }
		 return null;
	}
	
	private TaskItem getTask() {
		return queue.poll();
	}
	
	public boolean startTask() {
		if(!ibsDownload) {// && nextTask()!= null) {
			ibsDownload = true;
//			Tools.debug("启动下载定时器...");
			//启动下载定时器
			iTimeHandler.postDelayed(runnable, 500);
			return true;
		}
		return false;
	}
//		if(!ibsDownload && nextTask()!= null) {
//			ibsDownload = true;
//			if(currentTask.type == DOWANLOAD_TYPE_PIC) {
//				sendGet();
//			}
//			else if(currentTask.type == DOWANLOAD_TYPE_PAK) {
//				 if(ActivityUtil.isWifi()) {
//					 sendGetPak();
//				 }
//				 else {
//					 startNextTask();
//				 }
//			}
//			else {
//				sendGetGoodsIcon();
//			}
//			return true;
//		}
//		return false;
//	}
	
	private boolean startNextTask() {
		if(nextTask()!= null) {
			if(currentTask.type == DOWANLOAD_TYPE_PIC) {
				//Tools.debug("startNextTask::图片下载任务  188");
				sendGet();
			}
			else if(currentTask.type == DOWANLOAD_TYPE_PAK) {
				 if(ActivityUtil.isWifi()) {
					 sendGetPak();
				 }
				 else {
					 startNextTask();
				 }
			}
			else {
				sendGetGoodsIcon();
			}
			return true;
		}
		else {
			//Tools.debug("startNextTask::没有下载任务了！ 204");
			ibsDownload = false;
			currentTask = null;
			Message msg = new Message();
			msg.what = MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_PIC;
			iMainHandler.sendMessage(msg);
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
//		Tools.debug("清空下载任务");
		//停止下载计时器
		iTimeHandler.removeCallbacks(runnable);
		//
		currentTask = null;
		ibsDownload = false;
		removeTask();
	}
	
	public int getTaskSize() {
		return queue.size();
	}

	
	private boolean sdcardUsableness() {
    	if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
    		return true;
    	}
    	return false;
	}
	
	private TaskItem nextTask() {
		return currentTask = getTask();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case HTTPERROR.MSG_HTTP_ERROR:
//			System.out.println("下载失败 未知错误" + "size = " + getTaskSize());
			removeTask();
			StopTask();
			break;
		case HTTPERROR.MSG_PARSE_HTTP_ERRO:
//			System.out.println("下载失败 " + "size = " + getTaskSize());
			if(getTaskSize() > 0) {
				//启动下载定时器
				iTimeHandler.postDelayed(runnable, 1000);
				//startNextTask();
			}
			else {
				StopTask();
			}
			break;
		case HTTPERROR.MSG_GET_IMAGE:
//			Tools.debug(HTTPERROR.MSG_GET_IMAGE + " 下载完成 " + "size = " + getTaskSize());
			//
			noticeSgPicCpl();
			//启动定时器
			iTimeHandler.postDelayed(runnable, 1000);//每两秒执行一次runnable.
			break;
		case HTTPERROR.MSG_GET_GOODSICON:
//			Tools.debug(HTTPERROR.MSG_GET_GOODSICON + " 下载完成 " + "size = " + getTaskSize());
//			try {
				//noticeSgPicCpl();
				//启动定时器
				iTimeHandler.postDelayed(runnable, 1000);//每两秒执行一次runnable.
//			} catch (OutOfMemoryError e) {
//				e.printStackTrace();
//			}
			break;
		case HTTPERROR.MSG_GET_PAK:
			Tools.println(HTTPERROR.MSG_GET_PAK + " 下载完成 " + "size = " + getTaskSize());
//			try {
				//启动定时器
				iTimeHandler.postDelayed(runnable, 1000);//每两秒执行一次runnable.
//			} catch (OutOfMemoryError e) {
//				e.printStackTrace();
//			}
			break;
		case HTTPERROR.MSG_POST_FILE:
			Tools.println(HTTPERROR.MSG_POST_FILE + " 上传完成 ");
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
		FileInputStream fis = null;
		BitmapFactory.Options opt = new BitmapFactory.Options();  
		opt.inPreferredConfig = Bitmap.Config.RGB_565;   
		opt.inPurgeable = true;  
		opt.inInputShareable = true;  
		try {
			img = ImageSave.loadRoundedCornerBitmap(aFileName);
		}
		catch(Exception e) {
		}
		finally {
			if(img == null) {
//				if(aFileName.contains("_h")) {
//					opt.inSampleSize = 4;//图片宽高都为原来的二分之一，即图片为原来的四分之一
//				}
				InputStream is = MyArStation.getInstance().getApplicationContext().getResources().openRawResource(defaultRes(aFileName));  
				img = BitmapFactory.decodeStream(is,null,opt);  
				img = ImageUtil.getRoundedCornerBitmap(img, 7.0f);
				if(SettingManager.getInstance().isWifiDownload()) {
					if(ActivityUtil.isWifi()) {
						if(addTask(aFileName, 0)) {
//							Tools.debug("getImage::调用下载函数 340");
							startTask();
						}
					}
				}
				else {
					if(addTask(aFileName, 0)) {
//						Tools.debug("getImage::调用下载函数 347");
						startTask();
					}
				}
			}
			if(fis != null) {
				try {
					fis.close();
				}
				catch(Exception e){
					
				}
			}
		}
		return img;
	}
	
	public Bitmap getGoodsIcon(int resID) {
		String aFileName = resID + ".png";
		Bitmap img = null;
		if(aFileName == null || aFileName.length() <= 0) {
			return img;
		}
		FileInputStream fis = null;
		BitmapFactory.Options opt = new BitmapFactory.Options();  
		opt.inPreferredConfig = Bitmap.Config.RGB_565;   
		opt.inPurgeable = true;  
		opt.inInputShareable = true;  
		try {
			img = ImageSave.loadBitmap(aFileName, path);
		}
		catch(Exception e) {
		}
		finally {
			if(img == null) {
				opt.inSampleSize = 1;
				int id = defaultGoodsRes(resID);
				if(R.drawable.mypicdef == id) {
					opt.inSampleSize = 4;//图片宽高都为原来的二分之一，即图片为原来的四分之一
					InputStream is = MyArStation.getInstance().getApplicationContext().getResources().openRawResource(id);  
					img = BitmapFactory.decodeStream(is,null,opt);  
					if(SettingManager.getInstance().isWifiDownload()) {
						if(ActivityUtil.isWifi()) {
							if(addTask(aFileName, DOWANLOAD_TYPE_ICON)) {
								startTask();
							}
						}
					}
					else {
						if(addTask(aFileName, DOWANLOAD_TYPE_ICON)) {
							startTask();
						}
					}
				}
				else {
					InputStream is = MyArStation.getInstance().getApplicationContext().getResources().openRawResource(id);  
					img = BitmapFactory.decodeStream(is,null,opt);  
				}
			}
			if(fis != null) {
				try {
					fis.close();
				}
				catch(Exception e){
					
				}
			}
		}
		return img;
	}
	
	public Bitmap getGoodsIcon(String resName) {
		String aFileName = resName;
		Bitmap img = null;
		if(aFileName == null || aFileName.length() <= 0) {
			return img;
		}
		FileInputStream fis = null;
		BitmapFactory.Options opt = new BitmapFactory.Options();  
		opt.inPreferredConfig = Bitmap.Config.RGB_565;   
		opt.inPurgeable = true;  
		opt.inInputShareable = true;  
		try {
			img = ImageSave.loadBitmap(aFileName, path);
		}
		catch(Exception e) {
		}
		finally {
			if(img == null) {
				InputStream is = MyArStation.getInstance().getApplicationContext().getResources().openRawResource(R.drawable.gift);  
				img = BitmapFactory.decodeStream(is,null,opt);  
				img = ImageUtil.getRoundedCornerBitmap(img, 7.0f);
				if(SettingManager.getInstance().isWifiDownload()) {
					if(ActivityUtil.isWifi()) {
						if(addTask(aFileName, DOWANLOAD_TYPE_ICON)) {
							//Tools.debug("getGoodsIcon::调用下载函数 447");
							startTask();
						}
					}
				}
				else {
					if(addTask(aFileName, DOWANLOAD_TYPE_ICON)) {
						//Tools.debug("getGoodsIcon::调用下载函数 453");
						startTask();
					}
				}
			}
			if(fis != null) {
				try {
					fis.close();
				}
				catch(Exception e){
					
				}
			}
		}
		return img;
	}
	
	public Project getProject(String pakName) {
		Project project = null;
		project = Project.loadProject(DataPath.pakPath+pakName, false, false);
		if(project == null) {
			//下载
			if(ActivityUtil.isWifi()) {
				if(addTask(pakName, DOWANLOAD_TYPE_PAK)) {
					startTask();
				}
			}
		}
		return project;
	}
	
	public Bitmap getAdImage(String aFileName) {
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
				img = ImageSave.loadBitmap(aFileName);
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
					if(SettingManager.getInstance().isWifiDownload()) {
						if(ActivityUtil.isWifi()) {
							if(addTask(aFileName, DOWANLOAD_TYPE_PIC)) {
								startTask();
							}
						}
					}
					else {
						if(addTask(aFileName, DOWANLOAD_TYPE_PIC)) {
							startTask();
						}
					}
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
	
	private void noticeSgPicCpl() {
		Message message = new Message();
		message.what = MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_SINGLE_PIC;
		message.obj = currentTask != null ? currentTask.fileName : " ";
		iMainHandler.sendMessage(message);
	}
	
	private int defaultRes(String imageName) {
		int resID;
		if(imageName.contains("default0")) {
			resID = R.drawable.default0;
		}
		else if(imageName.contains("default1")) {
			resID = R.drawable.default1;
		}
		else if(imageName.contains("default2")) {
			resID = R.drawable.default2;
		}
		else if(imageName.contains("default3")) {
			resID = R.drawable.default3;
		}
		else if(imageName.contains("default4")) {
			resID = R.drawable.default4;
		}
		else if(imageName.contains("defaultbig0")) {
			resID = R.drawable.defaultbig0;
		}
		else if(imageName.contains("defaultbig1")) {
			resID = R.drawable.defaultbig1;
		}
		else if(imageName.contains("defaultbig2")) {
			resID = R.drawable.defaultbig2;
		}
		else if(imageName.contains("defaultbig3")) {
			resID = R.drawable.defaultbig3;
		}
		else if(imageName.contains("defaultbig4")) {
			resID = R.drawable.defaultbig4;
		}
		else if(imageName.contains("defaultx")) {
			resID = R.drawable.defaultx;
		}
		else {
			resID = R.drawable.mypicdef;
		}
		return resID;
	}
	
	private int defaultGoodsRes(int id) {
		Tools.debug("card = "+id);
		int resID = R.drawable.mypicdef;
		if(id < iStoreIcon.length && id >= 0) {
			resID = iStoreIcon[id];
		}
		return resID;
	}
	/**
	 * 发送GET客户端请求，该方法一般情况只在该协议层使用，当然也可以被外界调用
	 * 
	 * @param url请求的地址
	 * @param values客户端发送数据
	 * @param action响应消息类型
	 */
	private void sendGet() {
		if(currentTask == null) {
			return;
		}
//		Tools.debug("开始下载...");
		RestMsg restMsg = null;
		String realHost = MAINURL + currentTask.fileName;
		restMsg = new RestMsg(HTTPERROR.MSG_GET_IMAGE, realHost);
		RestClient.addTask(RestClient.TaskMehod.GET, restMsg, iHandler);
	}
	
	private void sendGetPak() {
		if(currentTask == null) {
			return;
		}
		RestMsg restMsg = null;
		String realHost = PAKURL + currentTask.fileName;
		restMsg = new RestMsg(HTTPERROR.MSG_GET_PAK, realHost);
//		System.out.println(restMsg.toString());
		RestClient.addTask(RestClient.TaskMehod.GET, restMsg, iHandler);
	}
	
	private void sendGetGoodsIcon() {
		if(currentTask == null) {
			return;
		}
		RestMsg restMsg = null;
		String realHost = MAINURL + currentTask.fileName;
		restMsg = new RestMsg(HTTPERROR.MSG_GET_GOODSICON, realHost);
//		System.out.println(restMsg.toString());
		RestClient.addTask(RestClient.TaskMehod.GET, restMsg, iHandler);
	}
	
	public void saveImage(byte[] bmp, String fileName) {
		if (bmp != null) {
			try {
				ImageSave.saveImage2SDCard(bmp, fileName);
			}
			catch (OutOfMemoryError e) {
				// 再试一次
				try {
					ImageSave.saveImage2SDCard(bmp,fileName);
				}
				catch (Exception e2) {
				}
			}
			catch (Exception e) {
				
			}
		}
	}
	
	public void saveImage(byte[] bmp, String fileName, String path) {
		if (bmp != null) {
			try {
				ImageSave.saveImage2SDCard(bmp, fileName, path, 5);
			}
			catch (OutOfMemoryError e) {
				// 再试一次
				try {
					ImageSave.saveImage2SDCard(bmp,fileName, path, 5);
				}
				catch (Exception e2) {
				}
			}
			catch (Exception e) {
				
			}
		}
	}
	
	public void savePak(byte[] bmp, String fileName, String path) {
		if (bmp != null) {
			try {
				ImageSave.savePak2SDCard(bmp, fileName, path, 5);
			}
			catch (OutOfMemoryError e) {
				// 再试一次
				try {
					ImageSave.savePak2SDCard(bmp,fileName, path, 5);
				}
				catch (Exception e2) {
				}
			}
			catch (Exception e) {
				
			}
		}
	}
	
	public class TaskItem {
		public String fileName;
		public int type = 0;//不普通
		/**
		 * construct
		 * @param fileName
		 * @param type
		 */
		public TaskItem(String fileName, int type) {
			this.fileName = fileName;
			this.type = type;
		}
	}
	
	public void PostFile(File file) {
		sendPostFile(file);
	}
	
	private void sendPostFile(File file) {
		if(file == null) return;
		RestMsg restMsg = null;
		String realHost = "http://www.funoble.com/crashrep/upload.php";
		restMsg = new RestMsg(HTTPERROR.MSG_POST_FILE, realHost, file);
//		System.out.println(restMsg.toString());
		RestClient.addTask(RestClient.TaskMehod.POST, restMsg, iHandler);
	}
}
