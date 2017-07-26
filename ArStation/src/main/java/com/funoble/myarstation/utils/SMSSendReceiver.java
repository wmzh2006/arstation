/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: SMSSendReceiver.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2013-1-8 下午06:46:16
 *******************************************************************************/
package com.funoble.myarstation.utils;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;


public class SMSSendReceiver extends BroadcastReceiver {

	/* 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Tools.debug("TAG onReceive");
		switch (getResultCode()) {
		case Activity.RESULT_OK:
			Tools.debug("TAG 短信发送成功");
			Toast.makeText(context,
					"短信发送成功", Toast.LENGTH_SHORT)
					.show();
			break;
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			break;
		case SmsManager.RESULT_ERROR_RADIO_OFF:
			break;
		case SmsManager.RESULT_ERROR_NULL_PDU:
			break;
		}
		MyArStation.mGameMain.sendSMS.unregisterReceiver();
	}

}
