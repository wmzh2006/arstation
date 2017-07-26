package com.funoble.myarstation.crash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import com.funoble.myarstation.download.HttpDownloadManager;
import com.funoble.myarstation.game.MyArStation;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 * 
 * @author way
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	private static final String TAG = "CrashHandler";
//	private final String Path = android.os.Environment.getExternalStorageDirectory() + "/lyingdice/.cache/.crash/";
	private Thread.UncaughtExceptionHandler mDefaultHandler;// 系统默认的UncaughtException处理类
	private static CrashHandler INSTANCE = new CrashHandler();// CrashHandler实例
	private Context mContext;// 程序的Context对象
	private Map<String, String> info = new HashMap<String, String>();// 用来存储设备信息和异常信息
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");// 用于格式化日期,作为日志文件名的一部分
	/** 错误报告文件的扩展名 */   
    private static final String CRASH_REPORTER_EXTENSION = ".cr";   
	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {

	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
		Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器
	}

	/**
	 * 当UncaughtException发生时会转入该重写的方法来处理
	 */
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果自定义的没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);// 如果处理了，让程序继续运行3秒再退出，保证文件保存并上传到服务器
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 *            异常信息
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	public boolean handleException(Throwable ex) {
		if (ex == null)
			return false;
		new Thread() {
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出",  Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();
		// 收集设备参数信息
//		collectDeviceInfo(mContext);
		// 保存日志文件
//		saveCrashInfo2File(ex);
		//发送错误报告到服务器   
//        sendCrashReportsToServer(mContext);   
		return true;
	}
	
	/**  
	 * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告  
	 */   
	public void sendPreviousReportsToServer() {   
		sendCrashReportsToServer(mContext);   
	}   
	
	/**  
	 * 把错误报告发送给服务器,包含新产生的和以前没发送的.  
	 * @param ctx  
	 */   
	private void sendCrashReportsToServer(Context ctx) {   
//		String[] crFiles = getCrashReportFiles(ctx);   
//		if (crFiles != null && crFiles.length > 0) {   
//			TreeSet<String> sortedFiles = new TreeSet<String>();   
//			sortedFiles.addAll(Arrays.asList(crFiles));   
//			for (String fileName : sortedFiles) {   
//				File cr = new File(Path, fileName);   
//				postReport(cr);   
//				cr.delete();// 删除已发送的报告   
//			}   
//		}   
	}   
	
	private void postReport(File file) {   
		if(MyArStation.iHttpDownloadManager != null) {
			MyArStation.iHttpDownloadManager.PostFile(file);
		}
	}   
	
//	
	/**  
	 * 获取错误报告文件名  
	 * @param ctx  
	 * @return  
	 */   
	private String[] getCrashReportFiles(Context ctx) {  
//		File filesDir = null;
//		FilenameFilter filter = null;
//		if (Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED)) {
//			filesDir = new File(Path);
//			if(filesDir != null && filesDir.exists()) {
//				if(filesDir.isDirectory()) {
//					filter = new FilenameFilter() {   
//						public boolean accept(File dir, String name) {   
//							return name.endsWith(CRASH_REPORTER_EXTENSION);   
//						}   
//					};   
//				}
//			}
//		}
//		return (filesDir != null && filter != null) ? filesDir.list(filter) : null;
		return null;
	}   

	/**
	 * 收集设备参数信息
	 * 
	 * @param context
	 */
	public void collectDeviceInfo(Context context) {
		try {
			PackageManager pm = context.getPackageManager();// 获得包管理器
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				info.put("versionName", versionName);
				info.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		Field[] fields = Build.class.getDeclaredFields();// 反射机制
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				info.put(field.getName(), field.get("").toString());
				Log.d(TAG, field.getName() + ":" + field.get(""));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private String saveCrashInfo2File(Throwable ex) {
//		StringBuffer sb = new StringBuffer();
//		for (Map.Entry<String, String> entry : info.entrySet()) {
//			String key = entry.getKey();
//			String value = entry.getValue();
//			sb.append(key + "=" + value + "\r\n");
//		}
//		Writer writer = new StringWriter();
//		PrintWriter pw = new PrintWriter(writer);
//		ex.printStackTrace(pw);
//		Throwable cause = ex.getCause();
//		//循环着把所有的异常信息写入writer中
//		while (cause != null) {
//			cause.printStackTrace(pw);
//			cause = cause.getCause();
//		}
//		pw.close();//记得关闭
//		String result = writer.toString();
//		sb.append(result);
//		//保存文件
//		long timetamp = System.currentTimeMillis();
//		String time = format.format(new Date());
//		String fileName = "crash-" + time + "-" + timetamp + CRASH_REPORTER_EXTENSION;
//		if (Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED)) {
//			try {
//				File dir = new File(Path);
//				if (!dir.exists())
//					dir.mkdir();
//				File file = new File(dir, fileName);
//				FileOutputStream fos = new FileOutputStream(file);
//				fos.write(sb.toString().getBytes());
//				fos.flush();
//				fos.close();
//				return fileName;
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		return null;
	}
}
