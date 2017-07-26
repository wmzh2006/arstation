/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: Goods.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-29 下午04:44:25
 *******************************************************************************/
package com.funoble.myarstation.store.data;


public class Goods {
	public String 		iName;
	public int 			iPrice;
	public String 		iDesc;
	public String 		iUseTime;
	public String		iOverplusTime;
	public String		iBuyTime;
	public int			iGoodsID;
	public int			iType;
	public int			iIconID;
	
	/**
	 * construct
	 * @param iName
	 * @param iPrice
	 * @param iDesc
	 * @param iIconID
	 * @param iGoodsID
	 * @param iType 0-GB,1-RMB
	 */
	public Goods(String iName, int iPrice, String iDesc, int iGoodsID, int iIconID, int iType) {
		this.iName = iName;
		this.iPrice = iPrice;
		this.iDesc = iDesc;
		this.iBuyTime = "";
		this.iUseTime = "";
		this.iOverplusTime = "";
		this.iType = iType;
		this.iIconID = iIconID;
		this.iGoodsID = iGoodsID;
	}

	
	
	/**
	 * construct
	 * @param iName
	 * @param iPrice
	 * @param iDesc
	 * @param iUseTime
	 * @param iOverplusTime
	 * @param iBuyTime
	 * @param iIconID
	 * @param iType 0-GB,1-RMB
	 */
	public Goods(String iName, int iPrice, String iDesc, String iUseTime,
			String iOverplusTime, String iBuyTime, int iIconID, int iType) {
		this.iName = iName;
		this.iPrice = iPrice;
		this.iDesc = iDesc;
		this.iUseTime = iUseTime;
		this.iOverplusTime = iOverplusTime;
		this.iBuyTime = iBuyTime;
		this.iType = iType;
		this.iIconID = iIconID;
	}

	/**
	 * construct
	 * @param iName
	 * @param iDesc
	 * @param iUseTime
	 * @param iOverplusTime
	 * @param iIconID
	 */
	public Goods(String iName, String iDesc, String iUseTime,
			String iOverplusTime, int iIconID) {
		this.iName = iName;
		this.iDesc = iDesc;
		this.iUseTime = iUseTime;
		this.iOverplusTime = iOverplusTime;
		this.iIconID = iIconID;
	}

	
	/**
	 * construct
	 * @param iName
	 * @param iPrice
	 * @param iDesc
	 * @param iBuyTime
	 * @param iIconID
	 * @param iType 0-GB,1-RMB
	 */
	public Goods(String iName, int iPrice, String iDesc, String iBuyTime, int iIconID, int iType) {
		this.iName = iName;
		this.iPrice = iPrice;
		this.iDesc = iDesc;
		this.iBuyTime = iBuyTime;
		this.iType = iType;
		this.iIconID = iIconID;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Goods [iName=" + iName + ", iPrice=" + iPrice + ", iDesc="
				+ iDesc + ", iUseTime=" + iUseTime + ", iOverplusTime="
				+ iOverplusTime + ", iBuyTime=" + iBuyTime + ", iGoodsID="
				+ iGoodsID + ", iType=" + iType + ", iIconID" + iIconID + "]";
	}
	
}
