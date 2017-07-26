/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: RoomListData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-7-19 下午03:00:03
 *******************************************************************************/
package com.funoble.myarstation.data.cach;

import java.util.ArrayList;


public class RoomListData {
	public int iRoomID;
	public int iTableID;
	public String iRoomName;
	public String iRoomOnlineNum;
	public String iRoomLimit;
	public String iRoomLimitNum;
	public String iRoomBet;
	public String iRoomBetNum;
	public int    iMinGB;
	public int    iMaxGB;
	public String iIcon;
	public int    iState;
	public ArrayList<String> ivipbet = new ArrayList<String>();;
	public String iVipBookPrice;
	public String iTime;
	public int iRoomType;
	/**
	 * construct
	 * @param iRoomID
	 * @param iTableID
	 * @param iRoomName
	 * @param iRoomOnlineNum
	 * @param iRoomLimit
	 * @param iRoomLimitNum
	 * @param iRoomBet
	 * @param iRoomBetNum
	 */
	public RoomListData(int iRoomID, int iTableID, String iRoomName,
			String iRoomOnlineNum, String iRoomLimit, String iRoomLimitNum,
			String iRoomBet, String iRoomBetNum, int iMinGB, int iMaxGB) {
		this.iRoomID = iRoomID;
		this.iTableID = iTableID;
		this.iRoomName = iRoomName;
		this.iRoomOnlineNum = iRoomOnlineNum;
		this.iRoomLimit = iRoomLimit;
		this.iRoomLimitNum = iRoomLimitNum;
		this.iRoomBet = iRoomBet;
		this.iRoomBetNum = iRoomBetNum;
		this.iMinGB = iMinGB;
		this.iMaxGB = iMaxGB;
	}
	
	public RoomListData(int iRoomID, int iTableID, String iRoomName,
			String iRoomLimitNum, String iRoomBetNum, int iMinGB, int iMaxGB) {
		this.iRoomID = iRoomID;
		this.iTableID = iTableID;
		this.iRoomName = iRoomName;
		this.iRoomLimitNum = iRoomLimitNum;
		this.iRoomBetNum = iRoomBetNum;
		this.iMinGB = iMinGB;
		this.iMaxGB = iMaxGB;
	}
	
	public RoomListData(int iRoomID, int iTableID, String iRoomName,
			String iRoomLimitNum, String iRoomBetNum, int iMinGB, 
			int iMaxGB, String iIcon, int aType) {
		this.iRoomID = iRoomID;
		this.iTableID = iTableID;
		this.iRoomName = iRoomName;
		this.iRoomLimitNum = iRoomLimitNum;
		this.iRoomBetNum = iRoomBetNum;
		this.iMinGB = iMinGB;
		this.iMaxGB = iMaxGB;
		this.iIcon = iIcon;
		this.iRoomType = aType;
	}
	
	public RoomListData(int iRoomID, int iTableID, String iRoomName,
			int iState) {
		this.iRoomID = iRoomID;
		this.iTableID = iTableID;
		this.iRoomName = iRoomName;
		this.iState = iState;
	}
	
	public RoomListData(int iRoomID, int iTableID, String iRoomName,
			int iState, String iIcon, String betA, 
			String betB, String betC,
			String iVipBookPrice, String iTime, int aType) {
		this.iRoomID = iRoomID;
		this.iTableID = iTableID;
		this.iRoomName = iRoomName;
		this.iState = iState;
		this.iIcon = iIcon;
		ivipbet.add(betA);
		ivipbet.add(betB);
		ivipbet.add(betC);
		this.iVipBookPrice = iVipBookPrice;
		this.iTime = iTime;
		this.iRoomType = aType;
	}
}
