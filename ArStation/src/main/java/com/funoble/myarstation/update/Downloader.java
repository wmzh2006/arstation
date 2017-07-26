package com.funoble.myarstation.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.socket.protocol.MBRspUpdate;
import com.funoble.myarstation.view.MessageEventID;

public class Downloader {
	 /**
     * 存放的sd卡路径
     */
    private static final String pathDownlaod = "/lyingdice/download/";
	private String localfile;// 保存路径
	private Handler mHandler;// 消息处理器
	private Dao dao;// 工具类
	private int fileSize;// 所要下载的文件的大小
	private List<DownloadInfo> infos;// 存放下载信息类的集合
	private static final int INIT = 1;// 定义三种下载的状态：初始化状态，正在下载状态，暂停状态
	private static final int DOWNLOADING = 2;
	private static final int PAUSE = 3;
	private int state = INIT;
	private int version;
	private File sdCard = android.os.Environment.getExternalStorageDirectory();
	private String tempName = sdCard.getPath()+pathDownlaod + "temp.data";
	
	public Downloader(int version, String localfile,
			Context context, Handler mHandler, int fileSize) {
		this.version = version;
		this.localfile = sdCard.getPath()+pathDownlaod+localfile;
		this.mHandler = mHandler;
		this.fileSize = fileSize;
		dao = new Dao(context);
	}

	/**
	 * 判断是否正在下载
	 */
	public boolean isdownloading() {
		return state == DOWNLOADING;
	}

	/**
	 * 得到downloader里的信息 首先进行判断是否是第一次下载，如果是第一次就要进行初始化，并将下载器的信息保存到数据库中
	 * 如果不是第一次下载，那就要从数据库中读出之前下载的信息（起始位置，结束为止，文件大小等），并将下载信息返回给下载器
	 */
	public LoadInfo getDownloaderInfors() {
		if (isFirst(version) || !TempFileExists()) {
			delete(version);
			init();
			int range = fileSize;
			infos = new ArrayList<DownloadInfo>();
			for (int i = 0; i < 1 - 1; i++) {
				DownloadInfo info = new DownloadInfo(version, i * range, (i + 1)
						* range - 1, 0, 0);
				infos.add(info);
			}
			DownloadInfo info = new DownloadInfo(version,
					(1 - 1) * range, fileSize - 1, 0, 0);
			infos.add(info);
			// 保存infos中的数据到数据库
			dao.saveInfos(infos);
			// 创建一个LoadInfo对象记载下载器的具体信息
			LoadInfo loadInfo = new LoadInfo(fileSize, 0, version);
			return loadInfo;
		} else {
			// 得到数据库中已有的urlstr的下载器的具体信息
			infos = dao.getInfos(version);
			Log.v("TAG", "not isFirst size=" + infos.size());
			int size = 0;
			int compeleteSize = 0;
			for (DownloadInfo info : infos) {
				compeleteSize += info.compeleteSize;
				size += info.endPos - info.startPos + 1;
			}
			return new LoadInfo(size, compeleteSize, version);
		}
	}

	/**
      */
	private void init() {
		try {
			if(sdcardUsableness()) {
				File file = new File(tempName);
				if (!file.exists()) {
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				Tools.debug("download init()...........");
				// 本地访问文件
				RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
				accessFile.setLength(fileSize);
				accessFile.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean TempFileExists() {
		if(sdcardUsableness()) {
			File file = new File(tempName);
			if (!file.exists()) {
				return false;
			}
			return true;
		}
		return false;
	}
	/**
	 * 判断是否是第一次 下载
	 */
	private boolean isFirst(Integer version) {
		return dao.isHasInfors(version);
	}

	public boolean freshDate(MBRspUpdate date) {
		if(date == null && !sdcardUsableness()) {
			return false;
		}
		Tools.debug("freshDate...........");
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(tempName, "rwd");
			int length = date.iSize;
			randomAccessFile.seek(date.iStartIndex - length);
			// 将要下载的文件写到保存在保存路径下的文件中
			randomAccessFile.write(date.iData, 0, length);
			// 更新数据库中的下载信息
			dao.updataInfos(version, date.iStartIndex);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				randomAccessFile.close();
				dao.closeDb();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	// 删除数据库中urlstr对应的下载器信息
	public void delete(int version) {
		dao.delete(version);
	}
	//标志下载完
	public void updataCompelete(int version, int compelete) {
		dao.updataCompelete(version, compelete);
	}
	
	// 设置暂停
	public void pause() {
		state = PAUSE;
	}

	// 重置下载状态
	public void reset() {
		state = INIT;
	}
	
	public boolean reName() {
		File file = new File(tempName);
		if(!file.exists()){
			return false;
		}  
		boolean bool = file.renameTo(new File(localfile));
		Log.i("renamebool","rename"+bool);
		return bool;
	}
	
	private boolean sdcardUsableness() {
    	if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
    		return true;
    	}
    	return false;
	}
	
    public PackageInfo getUninatllApkInfo(Context context, String archiveFilePath) {  
    	File apkFile = new File(archiveFilePath);  
    	if (!apkFile.exists() || !archiveFilePath.toLowerCase().endsWith(".apk")) {  
	    	System.out.println("file path is not correct");  
	    	return null;  
    	 }  
        try {
        	PackageManager pm = context.getPackageManager();  
        	PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);  
        	if (info != null) {  
        		if(info.versionCode != version) {
        			System.out.println("info.versionCode = " +info.versionCode + "version = " + version);
        			apkFile.delete();
        			return null;
        		}
        	}  
        	return info;
		} catch (Exception e) {
		}
		return null;
    }  
	
	public boolean installApk() {
		File file = new File(sdCard.getPath()+"/lyingdice/download/lyingdice.apk");
//		if(!file.exists() && !isCompelete(version)) {
//			return false;
//		}
		if(null == getUninatllApkInfo(MyArStation.mGameMain, sdCard.getPath()+"/lyingdice/download/lyingdice.apk")) {
			return false;
		}
//		if(!file.exists()) {
//			return false;
//		}
		// TODO Auto-generated method stub
		Log.e("OpenFile", file.getName());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
		"application/vnd.android.package-archive");
		MyArStation.mGameMain.startActivity(intent);
		MyArStation.mGameMain.finish();
		return true;
	}
	
	public boolean isCompelete(Integer version) {
		return dao.isCompelete(version);
	}
	
	public static boolean removeFile() {
		boolean result = false;
		File file = new File(android.os.Environment.getExternalStorageDirectory() + "/lyingdice/download/");
		if(!file.exists()) {
			return result;
		}
		File[] f = file.listFiles();
		for(File tem : f) {
			if(tem.toString().endsWith(".apk")) {
				Tools.debug(tem.toString());
				result = tem.delete();
			}
		}
		return result;
	}
}
