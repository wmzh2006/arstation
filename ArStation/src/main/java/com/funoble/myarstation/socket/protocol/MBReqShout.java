/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqShout.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-23 上午10:33:34
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBReqShout implements MsgPara {
	public int		iUserID;					//用户ID
	public int		iDice;						//多少点
	public int		iCount;						//多少个点
	public short	nShoutOne;					//是否喊了斋 0 --- 没有   1 --- 斋

	/**
	 * construct
	 */
	public MBReqShout() {
		iUserID = -1;
		iDice = 0;
		iCount = 0;
		nShoutOne = 0;
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
		int shLength =  2;
		shLength += aOutLength;
		Temp.position(shLength);
		Temp.putInt(iUserID);
		Temp.putInt(iDice);
		Temp.putInt(iCount);
		Temp.putShort(nShoutOne);
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
		iUserID = Temp.getInt();
		iDice = Temp.getInt();
		iCount = Temp.getInt();
		nShoutOne = Temp.getShort();
		shLength = Temp.position();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBReqShout [iUserID=" + iUserID + ", iDice=" + iDice
				+ ", iCount=" + iCount + "]";
	}

}
