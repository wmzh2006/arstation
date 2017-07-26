/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: ShortcutUtil.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-17 上午02:23:40
 *******************************************************************************/
package com.funoble.myarstation.utils;

import com.funoble.myarstation.game.R;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;


public class ShortcutUtil {

	/**
	 * 创建快捷方式
	 * 
	 * @param context
	 */
	public static void createShortcut(Context context, Class<?> target) {
		// <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
		Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.drawable.icon));
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
		intent.putExtra("duplicate", false);
		Intent sIntent = new Intent(Intent.ACTION_MAIN);
		sIntent.addCategory(Intent.CATEGORY_LAUNCHER);// 加入action,和category之后，程序卸载的时候才会主动将该快捷方式也卸载
		sIntent.setClass(context, target);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, sIntent);
		context.sendBroadcast(intent);
	}

	/**
	 * 判断桌面上是否有的快捷方式
	 * 
	 * @return
	 */
	public static boolean hasShortcut(Context context) {
		String url = "";
		System.out.println(getSystemVersion());
		if(getSystemVersion() < 8){
			url = "content://com.android.launcher.settings/favorites?notify=true";
		}else{
			url = "content://com.android.launcher2.settings/favorites?notify=true";
		}
		final ContentResolver cr = context.getContentResolver();
		final Uri CONTENT_URI = Uri.parse(url);
		Cursor c = cr.query(CONTENT_URI, new String[] { "intent" }, null, null, null);
		if (c == null) {
			// 注: 2.1update和2.2版本的真机上测试无法访问com.android.launcher.settings，2.1update1的模拟器上可以
			// ERROR/ActivityThread(1136): Failed to find provider info for com.android.launcher.settings
			return false;
		}
		while (c.moveToNext()) {
			String intentstring = c.getString(c.getColumnIndex("intent"));
			if (intentstring == null) {
				continue;
			}
			String componentString = getComponentString(intentstring);
			if (componentString.startsWith(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	private static String getComponentString(String intentInfo) {
		// intent info 的格式:intent=#Intent;action=android.intent.action.MAIN;category=android.intent.category.LAUNCHER;launchFlags=0x10200000;component=com.allstar.tanzhi/.activities.StartActivity;end
		int start = intentInfo.indexOf("component") + 9 + 1;
		int end = intentInfo.indexOf(";", start);
		return intentInfo.substring(start, end);
	}
	
	private static int getSystemVersion(){
		return android.os.Build.VERSION.SDK_INT;
	}
}