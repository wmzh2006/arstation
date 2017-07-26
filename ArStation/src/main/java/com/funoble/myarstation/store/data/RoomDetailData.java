/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: RoomData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014年3月19日 上午1:40:52
 *******************************************************************************/
package com.funoble.myarstation.store.data;

public class RoomDetailData {
	public int 		iRoomID; // 房间ID
	public int 		iTableID; // 桌子ID
	public String 	iRoomName; // 房间名字
	public String 	iRoomLimit; // 房间限制
	public String 	iRoomBet; // 房间赌注
	public int	 	iMinGB; // 进入房间的最新GB
	public int 		iMaxGB; // 进入房间的最大GB
	public String 	iIconName; // 图片名称
	public int 		iType; // 逻辑类型
	public int 		iGB; // 自己当前的金币
	public String 	iPicName; // 图片名称
	public String 	iUrl; // 网址
	
	public RoomDetailData() {
		this.iRoomID = 0;
		this.iTableID = 0;
		this.iRoomName = "";
		this.iRoomLimit = "";
		this.iRoomBet = "";
		this.iMinGB = 0;
		this.iMaxGB = 0;
		this.iIconName = "";
		this.iType = 0;
		this.iGB = 0;
		this.iPicName = "";
		this.iUrl = "";
	}
	/**
	 * construct
	 * @param iRoomID
	 * @param iTableID
	 * @param iRoomName
	 * @param iRoomLimit
	 * @param iRoomBet
	 * @param iMinGB
	 * @param iMaxGB
	 * @param iIconName
	 * @param iType
	 * @param iGB
	 * @param iPicName
	 * @param iUrl
	 */
	public RoomDetailData(int iRoomID, int iTableID, String iRoomName,
			String iRoomLimit, String iRoomBet, int iMinGB, int iMaxGB,
			String iIconName, int iType, int iGB, String iPicName, String iUrl) {
		this.iRoomID = iRoomID;
		this.iTableID = iTableID;
		this.iRoomName = iRoomName;
		this.iRoomLimit = iRoomLimit;
		this.iRoomBet = iRoomBet;
		this.iMinGB = iMinGB;
		this.iMaxGB = iMaxGB;
		this.iIconName = iIconName;
		this.iType = iType;
		this.iGB = iGB;
		this.iPicName = iPicName;
		this.iUrl = iUrl;
	}
}
