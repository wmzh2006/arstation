/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspVIPinfo.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-2 下午12:07:31
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;

public class MBNotifyActivityStart implements MsgPara {
	public String   iMsg;					//消息
	
	public int		iActivityType;			//活动类型
	public int		iActivityStatus;		////活动状态  //活动状态  0 --- 显示报名中    1 --- 显示即将开始    2 --- 显示正在进行   3 --- 显示已结束   4 --- 显示查看结果  5 --- 报了名，但没开始
	public int		iActivityID;			//活动ID
	public int     	iActivityRoomID;		//活动房间ID
	public int 		iRoomType;				//房间类型
	/**
	 * construct
	 */
	public MBNotifyActivityStart() {
		iMsg = "";
		iActivityType = 0;
		iActivityStatus = 0;
		iActivityID = 0;
		iActivityRoomID = 0;
		iRoomType = 5;
	}

	/* 
	 * @see com.funoble.lyingdice.socket.protocol.MsgPara#Encode(byte[], short)
	 */
	@Override
	public int Encode(byte[] aOutBuffer, short aOutLength) {
		if(aOutBuffer == null || aOutLength <= 0) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.wrap(aOutBuffer);
		int shLength = 2;
		shLength += aOutLength;
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iActivityType);
		Temp.putInt(iActivityStatus);
		Temp.putInt(iActivityID);
		Temp.putInt(iActivityRoomID);
		Temp.putInt(iRoomType);
		shLength = Temp.position();
		shLength -= aOutLength;
		Temp.putShort(aOutLength, (short) shLength);
		return Temp.position();
	}

	/* 
	 * @see com.funoble.lyingdice.socket.protocol.MsgPara#Decode(byte[], short)
	 */
	@Override
	public int Decode(byte[] aInBuffer, short aInLength) {
		if(aInBuffer == null || aInLength <= 0) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.allocate(aInBuffer.length + 2);
		Temp.put(aInBuffer);
		Temp.position(aInLength);
		int shLength = 0;
		shLength = Temp.getShort();
		if(shLength > aInBuffer.length - aInLength) {
			return -1;
		}
		int msglen = Temp.getShort();
		if(msglen > 0) {
			byte[] msg = new byte[msglen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iActivityType = Temp.getInt();
		iActivityStatus = Temp.getInt();
		iActivityID = Temp.getInt();
		iActivityRoomID = Temp.getInt();
		iRoomType = Temp.getInt();
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBNotifyActivityStart [iMsg=" + iMsg + ", iActivityType="
				+ iActivityType + ", iActivityStatus=" + iActivityStatus
				+ ", iActivityID=" + iActivityID + ", iActivityRoomID="
				+ iActivityRoomID + "]";
	}

}
