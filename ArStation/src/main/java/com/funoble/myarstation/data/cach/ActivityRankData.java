/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: ActivityRankData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-12-5 下午06:29:02
 *******************************************************************************/
package com.funoble.myarstation.data.cach;

public class ActivityRankData {
	public long		iRankUserID;	//排行榜用户的UID
	public String	iRankNick;		//排行榜用户的昵称
	public String	iRankValue;		//场数
	
	
	/**
	 * construct
	 * @param iUserID
	 * @param iActivityID
	 * @param iActivityPeopleCount
	 * @param iRanking
	 * @param iCount
	 * @param iRankUserID
	 * @param iRankNick
	 * @param iRankValue
	 */
	public ActivityRankData(long iRankUserID, String iRankNick, String iRankValue) {
		long userid = iRankUserID;
		if(userid < 0) {
			userid += 4294967296L;
		}
		this.iRankUserID = userid;
		this.iRankNick = iRankNick;
		this.iRankValue = iRankValue;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[id=" + iRankUserID + ", 昵称="
				+ iRankNick + ", 胜场=" + iRankValue + "]\n";
	}

	/**
	 * construct
	 */
	public ActivityRankData() {
		super();
	}
	
}

