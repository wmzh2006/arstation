/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqLoginRoom.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 下午03:55:08
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBReqLoginRoomB implements MsgPara {

	public int		iRoomID;			//房间ID，当房间ID为-1时，表示自动匹配
	public int		iTableID;			//座子ID

	/**
	 * construct
	 */
	public MBReqLoginRoomB() {
		iRoomID = -1;
		iTableID = -1;
	}

	/* 
	 * @see com.funoble.lyingdice.socket.protocol.MsgPara#Encode(byte[], short)
	 */
	@Override
	public int Encode(byte[] aOutBuffer, short aOutLength) {
		if(aOutBuffer == null || aOutLength < 0) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.wrap(aOutBuffer);
		int shLength = 2;
		shLength += aOutLength;
		Temp.position(shLength);
		Temp.putInt(iRoomID);
		Temp.putInt(iTableID);
		shLength = Temp.position();
		shLength -= aOutLength;
		if(shLength < 0) {
			return -1;
		}
		Temp.putShort(aOutLength, (short) shLength);
		return Temp.position();
	}

	/* 
	 * @see com.funoble.lyingdice.socket.protocol.MsgPara#Decode(byte[], short)
	 */
	@Override
	public int Decode(byte[] aInBuffer, short aInLength) {
		if(aInBuffer == null) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.allocate(aInBuffer.length + 2);
		Temp.put(aInBuffer);
		Temp.position(aInLength);
		int shlength = 0;
		shlength = Temp.getShort();
		if(shlength > aInBuffer.length) {
			return -1;
		}
		iRoomID = Temp.getInt();
		iTableID = Temp.getInt();
		shlength = Temp.position();
		return shlength;
	}

}
