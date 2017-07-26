/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: GiftData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014年4月13日 下午2:48:59
 *******************************************************************************/
package com.funoble.myarstation.store.data;


public class GiftData {
	public short	iID;				//礼品的ID
	public short	iMoney;				//需要的凤币
	public int	iGb;				//需要的金币
	public short	iIntimate;			//增加的亲密度
	public String	iNames;
	public String   iPrice;
	/**
	 * construct
	 * @param iID
	 * @param iMoney
	 * @param iGb
	 * @param iIntimate
	 * @param iNames
	 */
	public GiftData(short iID, short iMoney, int iGb, short iIntimate,
			String iNames) {
		this.iID = iID;
		this.iMoney = iMoney;
		this.iGb = iGb;
		this.iIntimate = iIntimate;
		this.iNames = iNames;
		this.iPrice = "";
	}
	
	public GiftData() {
		this.iID = -1;
		this.iMoney = 0;
		this.iGb = 0;
		this.iIntimate = 0;
		this.iNames = "";
		this.iPrice = "";
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GiftData [iID=" + iID + ", iMoney=" + iMoney + ", iGb=" + iGb
				+ ", iIntimate=" + iIntimate + ", iNames=" + iNames
				+ ", iPrice=" + iPrice + "]";
	}

}
