/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: CopyFileFromAssets.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2013-3-4 下午04:10:34
 *******************************************************************************/
package com.funoble.myarstation.phrase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;


public class CopyFileFromAssets {
	/**
	 * 
	 * @param myContext
	 * @param ASSETS_NAME 要复制的文件名
	 * @param savePath 要保存的路径
	 * @param saveName 复制后的文件名
	 * @param force 强制复制
	 *  testCopy(Context context)是一个测试例子。
	 */
	
	public static void copy(Context myContext, String ASSETS_NAME,
			String savePath, String saveName, boolean force) {
		String filename = savePath + "/" + saveName;

		File dir = new File(savePath);
		// 如果目录不中存在，创建这个目录
		if (!dir.exists())
			dir.mkdir();
		try {
			if (force || !(new File(filename)).exists()) {
				InputStream is = myContext.getResources().getAssets()
						.open(ASSETS_NAME);
				FileOutputStream fos = new FileOutputStream(filename);
				byte[] buffer = new byte[7168];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
