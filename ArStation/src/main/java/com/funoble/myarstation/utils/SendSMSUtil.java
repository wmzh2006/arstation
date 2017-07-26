/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: SendSMSUtil.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2013-1-8 下午06:18:02
 *******************************************************************************/
package com.funoble.myarstation.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;


public class SendSMSUtil {
	private final String TAG = "SendSMSUtil";
	private String SENT_SMS_ACTION = "SENT_SMS_ACTION";
//	private SMSSendReceiver  receiver = new SMSSendReceiver();
	
	public SendSMSUtil() {
//		register();
	}
	
	/**
	 * 发送短信
	 * @param smsBody
	 */
	
	public void sendSMS(String smsBody) {
		Uri smsToUri = Uri.parse("smsto:");
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", smsBody);
		MyArStation.mGameMain.startActivityForResult(intent, 300);
	}

	public void register(){
		Tools.debug("TAG register");
//		GameMain.mGameMain.registerReceiver(receiver, new IntentFilter(SENT_SMS_ACTION));
	}
	
	public void unregisterReceiver () {
		Tools.debug("TAG unregisterReceiver");
//		GameMain.mGameMain.unregisterReceiver(receiver);
	}
}
