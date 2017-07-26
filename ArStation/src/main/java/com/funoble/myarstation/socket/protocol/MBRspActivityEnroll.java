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
import java.util.Vector;

public class MBRspActivityEnroll implements MsgPara {
	public short	nResult;				//结果
	public String   iMsg;					//消息
	
	public int		iActivityType;			//活动类型
	public int		iActivityID;			//活动ID
	public int		iPeopleCount;			//已报名人数
	public int     	iActivityRoomID;		//活动房间ID
	public String	iStateText;				//活动的状态描述
	public int		iActivityStatus;		//活动状态  0 --- 不显示按钮  1 --- 显示报名    2 --- 显示参加    3 --- 显示结果
	public int 		iRoomType;
	/**
	 * construct
	 */
	public MBRspActivityEnroll() {
		nResult = -1;
		iMsg = "";
		iActivityType = 0;
		iActivityStatus = 0;
		iActivityID = 0;
		iPeopleCount = 0;
		iActivityRoomID = 0;
		iStateText = "";
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
		Temp.putShort(nResult);
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iActivityType);
		Temp.putInt(iActivityID);
		Temp.putInt(iPeopleCount);
		Temp.putInt(iActivityRoomID);
		Temp.putShort((short) iStateText.getBytes().length);
		if(iStateText.length() > 0) {
			Temp.put(iStateText.getBytes());
		}
		Temp.putInt(iActivityStatus);
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
		nResult = Temp.getShort();
		int msglen = Temp.getShort();
		if(msglen > 0) {
			byte[] msg = new byte[msglen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iActivityType = Temp.getInt();
		iActivityID = Temp.getInt();
		iPeopleCount = Temp.getInt();
		iActivityRoomID = Temp.getInt();
		int statustextlen = Temp.getShort();
		if(statustextlen > 0) {
			byte[] statustext = new byte[statustextlen];
			Temp.get(statustext);
			iStateText = new String(statustext);
			statustext = null;
		}
		iActivityStatus = Temp.getInt();
		iRoomType = Temp.getInt();
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspActivityEnroll [nResult=" + nResult + ", iMsg=" + iMsg
				+ ", iActivityType=" + iActivityType + ", iActivityID="
				+ iActivityID + ", iPeopleCount=" + iPeopleCount
				+ ", iActivityRoomID=" + iActivityRoomID + ", iStateText="
				+ iStateText + ", iActivityStatus=" + iActivityStatus + "]";
	}
	
}
