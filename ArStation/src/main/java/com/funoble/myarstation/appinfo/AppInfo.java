/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: AppInfo.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2013-9-17 下午06:26:25
 *******************************************************************************/
package com.funoble.myarstation.appinfo;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class AppInfo {
	public String appName = "";
	public String packageName = "";
	public String versionName = "";
	public int versionCode = 0;
	public Drawable appIcon = null;

	public static List<AppInfo> getAppInfos(Context context) {
		List<AppInfo> appInfo = new ArrayList<AppInfo>();
		List<PackageInfo> packages = context.getPackageManager()
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo packageInfo : packages) {
			AppInfo tmpInfo = new AppInfo();
			tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
					context.getPackageManager()).toString();
			tmpInfo.packageName = packageInfo.packageName;
			tmpInfo.versionName = packageInfo.versionName;
			tmpInfo.versionCode = packageInfo.versionCode;
			tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(context
					.getPackageManager());
			// Only display the non-system app info
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					appInfo.add(tmpInfo);
				}
			}
		return appInfo;
	}

	public static AppInfo getAppInfo(Context context, String packageName) {
		if(packageName == null || packageName.equals("")) {
			return null;
		}
		PackageManager pm = context.getPackageManager();
		AppInfo appInfo = null;
		PackageInfo pi = null;
		try {
			pi = pm.getPackageInfo(packageName, 0);
			if(pi != null) {
				appInfo = new AppInfo();
				appInfo.packageName = pi.packageName;
				appInfo.versionName = pi.versionName;
				appInfo.versionCode = pi.versionCode;
				appInfo.appIcon = pi.applicationInfo.loadIcon(context
						.getPackageManager());
			}
		} catch (NameNotFoundException e) {
		}
		return appInfo;
	}
	
	public static Intent openApp(Context context, String packageName) {
		Intent intent = null;
		PackageManager pm = context.getPackageManager();
		PackageInfo pi = null;
		try {
			pi = pm.getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(pi.packageName);
		
		pm = context.getPackageManager();
		List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);

		ResolveInfo ri = apps.iterator().next();
		if (ri != null) {
			String pkName = ri.activityInfo.packageName;
			String className = ri.activityInfo.name;

			intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			ComponentName cn = new ComponentName(pkName, className);

			intent.setComponent(cn);
		}
		return intent;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppInfo [appName=" + appName + ", packageName=" + packageName
				+ ", versionName=" + versionName + ", versionCode="
				+ versionCode + ", appIcon=" + appIcon + "]";
	}
}
