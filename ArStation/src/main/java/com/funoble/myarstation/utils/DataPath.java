/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: DataPath.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014年3月20日 下午7:59:50
 *******************************************************************************/
package com.funoble.myarstation.utils;

public class DataPath {
	 public static final String pathBase = android.os.Environment.getExternalStorageDirectory()+"/lyingdice/.cache/";
	 public static final String pakPath= pathBase+"."+ActivityUtil.PATH_SCREEN_WVGA800;
}
