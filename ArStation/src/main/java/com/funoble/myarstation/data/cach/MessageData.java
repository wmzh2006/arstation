/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MessageData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-11-22 上午11:14:18
 *******************************************************************************/
package com.funoble.myarstation.data.cach;

import android.R.integer;


public class MessageData {
	public String iLittleHead;
	public String iName;
	public String iMsg;
	public int iLeaveMsgID;
	public int uid;
	public String iTime;
	public boolean self;
	public short iType;//类型 0 --- 普通    1 --- 私信  2 --- 邀请好友  3 --- 系统消息
	/**
	 * construct
	 * @param iLittleHead
	 * @param iName
	 * @param iMsg
	 */
	public MessageData(MessageData msg) {
		reset(msg);
	}
	
	/**
	 * construct
	 */
	public MessageData() {
		reset();
	}
	
	public void reset() {
		reset(null, null, null, 0, 0, null, false, (short) 0);
	}
	
	public void reset(MessageData msg) {
		reset(msg.iLittleHead, msg.iName, msg.iMsg, msg.uid, msg.iLeaveMsgID, msg.iTime, msg.self, (short) 0);
	}
	
	public void reset(String iLittleHead, String iName, String iMsg, int uid, int iLeaveMsgID, String iTime, boolean self, short type) {
		this.iLittleHead = iLittleHead;
		this.iName = iName;
		this.iMsg = iMsg;
		this.uid = uid;
		this.iLeaveMsgID = iLeaveMsgID;
		this.iTime = iTime;
		this.self = self;
		this.iType = type;
	}

	/* 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return (o != null && ((MessageData)o).iLeaveMsgID == this.iLeaveMsgID) ? true : false;
	}
	
	
}
