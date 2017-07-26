package com.funoble.myarstation.utils;

import android.util.Log;

public final class LogUtil {
	
	/*Log.v的调试颜色为黑色 的，任何消息都会输出;
	Log.d的输出颜色是蓝色的 ，仅输出debug，但他会输出上层的信息，过滤通过DDMS的Logcat标签来选择。
	Log.i的输出为绿色 ，一般提示性的消息information，它不会输出Log.v和Log.d的信息，但会显示i、w和e的信息。
	Log.w的意思为橙色 ，需要我们注意优化Android代码，同时选择它后还会输出Log.e的信息。
	Log.e为红色 ，这些错误就需要我们认真的分析，查看栈的信息了。*/ 
	
	private static final String VERBOSE_TAG = "VERBOSE";
	private static final String DEBUG_TAG = "DEBUG";
	private static final String INFO_TAG = "INFO";
	private static final String WARN_TAG = "WARN";
	private static final String ERROR_TAG = "ERROR";

	public static void Log_debug(String strMessage){
		Log.d(DEBUG_TAG, strMessage);
	}
}
