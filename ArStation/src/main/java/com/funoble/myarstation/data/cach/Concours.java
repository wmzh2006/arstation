/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: Concours.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-11-15 下午07:25:33
 *******************************************************************************/
package com.funoble.myarstation.data.cach;


public class Concours {
	public String   iMsg;					//活动说明
	public int		iActivityType;			//活动类型
	public int		iActivityStatus;		////活动状态  //活动状态  0 --- 显示报名中    1 --- 显示即将开始    2 --- 显示正在进行   3 --- 显示已结束   4 --- 显示查看结果  5 --- 报了名，但没开始
	public int		iActivityID;			//活动ID
	public int     	iActivityRoomID;		//活动房间ID
	public int 		iRoomType = 5; 				//房间类型
	public Concours() {
		
	}

	/**
	 * construct
	 * @param iActivityType
	 * @param iActivityStatus
	 * @param iActivityID
	 * @param iActivityRoomID
	 */
	public Concours(String aMsg, int aActivityType, int aActivityStatus,
			int aActivityID, int aActivityRoomID, int aRoomType) {
		this.iMsg = aMsg;
		this.iActivityType = aActivityType;
		this.iActivityStatus = aActivityStatus;
		this.iActivityID = aActivityID;
		this.iActivityRoomID = aActivityRoomID;
		this.iRoomType = aRoomType;
	}
}
