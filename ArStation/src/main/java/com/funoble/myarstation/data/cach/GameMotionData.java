/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: GameMotionData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 zero  2012-9-15 下午12:22:30
 *******************************************************************************/
package com.funoble.myarstation.data.cach;

public class GameMotionData {
	public String	iRank;
	public String	iName;
	public String	iPoint;
	/**
	 * construct
	 * @param aRank
	 * @param aName
	 * @param list
	 */
	public GameMotionData(String aRank, String aName, String aPoint) {
		this.iRank = aRank;
		this.iName = aName;
		this.iPoint = aPoint;
	}
	
	public GameMotionData() {
		this.iRank = "";
		this.iName = "";
		this.iPoint = "";
	}
}
