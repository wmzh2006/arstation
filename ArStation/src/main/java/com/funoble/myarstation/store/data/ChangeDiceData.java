/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: ChangeDiceData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2013-1-7 下午06:51:04
 *******************************************************************************/
package com.funoble.myarstation.store.data;

import android.graphics.Bitmap;


public class ChangeDiceData {
	public int diceType;
	public int[] dicesResID;
	
	/**
	 * construct
	 */
	public ChangeDiceData(int diceType, int[] dicesResID) {
		this.diceType = diceType;
		this.dicesResID = dicesResID;
	}
}
