/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: LocalService.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-12-22 下午10:34:13
 *******************************************************************************/
package com.funoble.myarstation.game;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


public class LocalService extends Service {
	 private NotificationManager mNM;
	 private final IBinder mBinder = new LocalBinder();
	 
	 public class LocalBinder extends Binder {
		 LocalService getService() {
			 return LocalService.this;
		 }
	 }
	/* 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	/* 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/* 
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	/* 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		showNotification();
	}

	private void showNotification() {
		Notification notification = new Notification(0,
				"my_service_name",
				System.currentTimeMillis());
		PendingIntent p_intent = PendingIntent.getActivity(this, 0,
				new Intent(this, MyArStation.class), 0);
//		notification.setLatestEventInfo(this, "", "", p_intent);
		notification.flags = notification.flags | 
		Notification.FLAG_ONGOING_EVENT;
		startForeground(0x1982, notification);   // notification ID: 0x1982, you can name it as
	}
}
