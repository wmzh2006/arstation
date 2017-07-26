/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: RankData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-7-7 下午03:54:54
 *******************************************************************************/
package com.funoble.myarstation.data.cach;

public class RankData {
	public int  		iRankUserID; //排行榜用户的UID
	public String		iRankNick;//排行榜用户的昵称
	public String	    iRankPicName; //排行榜用户的图片
	public String		iRankValue; //根据排行榜的类型来变， 排行榜是财富 --- 金币   排行榜是级别 --- 级别   排行榜是胜利场数 --- 胜利场数
	public short		iOnLine; //是否在线
	public short		iFriend; //是否为好友
	public int			iRank;//排名
	public int 			iSelfID;//自己id
	public int 			iIconID;//图标
	public RankData() {
		this.iRankUserID = 0;
		this.iRankNick = "";
		this.iRankPicName = "";
		this.iRankValue = "";
		this.iOnLine = 0;
		this.iFriend = 0;
		this.iRank = 0;
		this.iSelfID = 0;
		this.iIconID = 0;
	}

	/**
	 * construct
	 * @param iRankUserID
	 * @param iRankNick
	 * @param iRankPicName
	 * @param iRankValue
	 * @param iOnLine
	 * @param iFriend
	 */
	public RankData(int iRank, int iRankUserID, int iSelfID, String iRankNick, String iRankPicName,
			String iRankValue, short iOnLine, short iFriend, int iIconID) {
		super();
		this.iRank = iRank;
		this.iRankUserID = iRankUserID;
		this.iRankNick = iRankNick;
		this.iRankPicName = iRankPicName;
		this.iRankValue = iRankValue;
		this.iOnLine = iOnLine;
		this.iFriend = iFriend;
		this.iSelfID = iSelfID;
		this.iIconID = iIconID;
	}
	
}
