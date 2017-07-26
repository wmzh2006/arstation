/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: URLAvailability.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-20 下午01:00:30
 *******************************************************************************/
package com.funoble.myarstation.utils;

import java.net.HttpURLConnection;
import java.net.URL;

/**
* 文件名称为：URLAvailability.java
* 文件功能简述： 描述一个URL地址是否有效
*/
public class URLAvailability {
	private static URL url;
	private static HttpURLConnection con;
	private static int state = -1;
	
	/**
	   * 功能：检测当前URL是否可连接或是否有效,
	   * 描述：最多连接网络 5 次, 如果 5 次都不成功，视为该地址不可用
	   * @param urlStr 指定URL网络地址
	   * @return URL
	   */
	public synchronized URL isConnect(String urlStr) {
	   int counts = 0;
	   if (urlStr == null || urlStr.length() <= 0) {                       
	    return null;                 
	   }
	   while (counts < 5) {
	    try {
	     url = new URL(urlStr);
	     con = (HttpURLConnection) url.openConnection();
	     state = con.getResponseCode();
	     System.out.println(counts +"= "+state);
	     if (state == 200) {
	      System.out.println("URL可用！");
	     }
	     break;
	    }catch (Exception ex) {
	     counts++; 
	     System.out.println("URL不可用，连接第 "+counts+" 次");
	     urlStr = null;
	     continue;
	    }
	   }
	   return url;
	}
}
