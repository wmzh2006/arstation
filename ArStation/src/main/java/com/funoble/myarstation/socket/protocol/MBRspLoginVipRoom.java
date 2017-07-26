/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspLeaveRoom.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-24 上午11:14:10
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;

import com.funoble.myarstation.data.cach.RoomData;


public class MBRspLoginVipRoom implements MsgPara {
	public short    iResult;						//结果
	public String	iMsg;							//消息
	public int		iRoomID;						//目标所在的房间ID
	public int		iTableID;						//目标所在的桌子ID
	public int		iClientRoomID;					//客户端房间ID
	public int 		iState;							//房间状态
	public int		iSecond;						//还剩多少秒到钟
	public int 		iType;							//房间类型
	/**
	 * construct
	 */
	public MBRspLoginVipRoom() {
		iResult = -1;
		iMsg = "";
		iRoomID = 0;
		iTableID = 0;
		iClientRoomID = 0;
		iState = 0;
		iSecond = 0;
		iType = RoomData.TYPE_CLASSICAL;
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
		Temp.position(shLength);
		Temp.putShort(iResult);
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iRoomID);
		Temp.putInt(iTableID);
		Temp.putInt(iClientRoomID);
		Temp.putInt(iState);
		Temp.putInt(iSecond);
		Temp.putInt(iType);
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
		iResult = Temp.getShort();
		int msglen = Temp.getShort();
		if(msglen > 0) {
			byte[] msg = new byte[msglen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iRoomID = Temp.getInt();
		iTableID = Temp.getInt();
		iClientRoomID = Temp.getInt();
		iState = Temp.getInt();
		iSecond = Temp.getInt();
		iType = Temp.getInt();
		shLength = Temp.position();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspLoginVipRoom [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iRoomID=" + iRoomID + ", iTableID=" + iTableID
				+ ", iClientRoomID=" + iClientRoomID + ", iState=" + iState
				+ ", iSecond=" + iSecond + ", iType" + iType +"]";
	}
}
