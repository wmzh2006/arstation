/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: GiftData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014年3月24日 下午6:09:26
 *******************************************************************************/
package com.funoble.myarstation.data.cach;


public class GiftData {
	public int iGiftID;
	public String iGiftName;
	public String iGiftDes;
	public int iGiftPrice;
	/**
	 * construct
	 * @param iGiftID
	 * @param iGiftName
	 * @param iGiftDes
	 * @param iGiftPrice
	 */
	public GiftData(int iGiftID, String iGiftName, String iGiftDes,
			int iGiftPrice) {
		super();
		this.iGiftID = iGiftID;
		this.iGiftName = iGiftName;
		this.iGiftDes = iGiftDes;
		this.iGiftPrice = iGiftPrice;
	}
}
