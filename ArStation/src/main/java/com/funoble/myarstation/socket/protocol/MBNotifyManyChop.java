/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBNotifyShout.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-23 上午10:55:06
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBNotifyManyChop implements MsgPara {
	public int		iUserSeatID;			//使用者座位ID
	public int     	iDesSeatID;				//目标座位ID
	
	/**
	 * construct
	 */
	public MBNotifyManyChop() {
		iUserSeatID = -1;
		iDesSeatID = -1;
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
		Temp.putInt(iUserSeatID);
		Temp.putInt(iDesSeatID);
		shLength = Temp.position();
		shLength -= aOutLength;
		Temp.putShort(aOutLength, aOutLength);
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
		iUserSeatID = Temp.getInt();
		iDesSeatID = Temp.getInt();
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBNotifyManyChop [iUserSeatID=" + iUserSeatID + ", iDesSeatID="
				+ iDesSeatID + "]";
	}

}
