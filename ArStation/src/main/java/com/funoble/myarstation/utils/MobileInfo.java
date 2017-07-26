/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MobileInfo.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014-1-7 下午08:51:25
 *******************************************************************************/
package com.funoble.myarstation.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.text.format.Formatter;

public class MobileInfo {
	public static String getCpuInfo()// 获取cpu型号
	{
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cupinfo = { "", "" };
		String[] arrayOfString;
		try {
			FileReader reader = new FileReader(str1);
			BufferedReader buffer = new BufferedReader(reader, 8192);
			str2 = buffer.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cupinfo[0] = cupinfo[0] + arrayOfString[i] + "";
			}
			reader.close();
			buffer.close();
		} catch (Exception e) {
		}
		return cupinfo[0];
	}

	public static String getCpuName() {
		return null;
	}

	public static String getMaxCpuFreq() {// 获取cpu最大频率
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat",
					"/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream is = process.getInputStream();
			byte[] te = new byte[24];
			while (is.read(te) != -1) {
				result += new String(te);
			}
			float core = Float.valueOf(result);
			core = core / 1000;
			result = (int) core + " Mhz";
			is.close();
		} catch (Exception e) {
			result = "N/A";
		}
		return result.trim();

	}

	public static int getMaxCpuFreqInt() {// 获取cpu最大频率
		String result = "800";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat",
					"/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream is = process.getInputStream();
			byte[] te = new byte[24];
			while (is.read(te) != -1) {
				result = new String(te);
			}
			float core = Float.valueOf(result);
			core = core / 1000;
			result = (int) core + "";
			is.close();
		} catch (Exception e) {
			result = "800";
		}
		return Integer.valueOf(result);

	}

	public static String getMinCpuFreq() {// 获取cpu最小频率
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat",
					"/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream is = process.getInputStream();
			byte[] te = new byte[24];
			while (is.read(te) != -1) {
				result += new String(te);
			}
			float core = Float.valueOf(result);
			System.out.println("------>coure" + core);
			core = core / 1000;
			result = (int) core + " Mhz";
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}

	private static int getNumCores() {// 获取cpu核心数
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}

		}
		try {
			File dir = new File("/sys/devices/system/cpu/");
			File[] files = dir.listFiles(new CpuFilter());
			return files.length;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	public static String getNumCoure()// 转换核心数
	{
		String result = "";
		if (getNumCores() == 1) {
			result = "单核";
		} else if (getNumCores() == 2) {
			result = "双核";
		} else if (getNumCores() == 4) {
			result = "四核";
		} else {
			result = "你手机为劣质手机,无法检测!";
		}
		return result;
	}

	public static String getMobileInfo(Context context) // 获取手机RAM内存和RAM剩余内存
	{

		Method _readProclines = null;
		String str3 = "";
		String str2 = "";
		Class procClass = null;
		try {
			procClass = Class.forName("android.os.Process");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Class parameterTypes[] = new Class[] { String.class, String[].class,
				long[].class };
		try {
			_readProclines = procClass.getMethod("readProcLines",
					parameterTypes);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		Object arglist[] = new Object[3];
		final String[] mMemInfoFileds = new String[] { "MemTotal:", "Cached:" };
		long[] mMemInfoSize = new long[mMemInfoFileds.length];
		mMemInfoSize[0] = 30;
		mMemInfoSize[1] = -30;
		arglist[0] = new String("/proc/meminfo");
		arglist[1] = mMemInfoFileds;
		arglist[2] = mMemInfoSize;
		if (_readProclines != null) {
			try {
				_readProclines.invoke(null, arglist);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < mMemInfoSize.length; i++) {
				str3 += String.valueOf(mMemInfoSize[0]);
				str2 += String.valueOf(mMemInfoSize[1]);
				System.out.println("------------->" + mMemInfoFileds[i] + ":"
						+ mMemInfoSize[i] / 1024);
			}
			long size = Long.valueOf(str3) / 1024;
			str3 = Formatter.formatFileSize(context, size);

			long size1 = Long.valueOf(str2) / 1024;
			str2 = Formatter.formatFileSize(context, size1);

			str3 = str3 + "(剩余" + getAvailMemory(context) + ")";
			System.out.println("str2---------------------->" + str2);
		}
		return str3;
	}

	private static String getAvailMemory(Context context) {// 获取android
															// RAM当前可用内存大小
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
	}

	public static float getAvailMemoryInt(Context context) {// 获取android
		// RAM当前可用内存大小
		// mi.availMem; 当前系统的可用内存
		float result = 1.0f;
		try {
			ActivityManager am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			MemoryInfo mi = new MemoryInfo();
			am.getMemoryInfo(mi);
			result = mi.availMem / (1024*1024);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static int getAppLimitMemory(Context context) {
		int memory = 1;
		try {
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			memory = activityManager.getMemoryClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return memory;
	}
}
