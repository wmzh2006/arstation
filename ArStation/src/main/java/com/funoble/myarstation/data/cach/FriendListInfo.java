/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: FriendInfo.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-7-10 下午10:30:59
 *******************************************************************************/
package com.funoble.myarstation.data.cach;

public class FriendListInfo {

//	public boolean  ibImageInit;			//图片是否初始化
	public int		iFriendUserID;			//好友UID
	public String	iFriendNick;			//好友的昵称
	public String	iFriendPicName;			//好友图片
	public String	iFriendLevel;			//好友等级
	public int		iFriendStatus;			//好友状态
	public int 		iUpdate;				//更新状态0-没有1-有
	/**
	 * construct
	 */
	public FriendListInfo(int aFriendUserID, String aFriendNick, String aFriendPicName, String aFriendLevel,
			int	aFriendStatus, int aUpdate) {
		iFriendUserID = aFriendUserID;
		iFriendNick = aFriendNick;
		iFriendPicName = aFriendPicName;
		iFriendLevel = aFriendLevel;
		iFriendStatus = aFriendStatus;
		iUpdate = aUpdate;
//		ibImageInit = false;
	}

}
