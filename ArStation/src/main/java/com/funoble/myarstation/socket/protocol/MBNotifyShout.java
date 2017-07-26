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


public class MBNotifyShout implements MsgPara {
	public int		iShouterSeatID;				//喊点者的座位ID
	public int     	iNextSeatID;				//下一个喊点者的座位ID
	public int		iDice;						//多少点
	public int     	iCount;			
	public short	nShoutOne;					//是否喊了斋 0 --- 没有   1 --- 斋
	
	/**
	 * construct
	 */
	public MBNotifyShout() {
		iShouterSeatID = -1;
		iNextSeatID = -1;
		iDice = -1;
		iCount = -1;
		nShoutOne = 0;
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
		Temp.putInt(iShouterSeatID);
		Temp.putInt(iNextSeatID);
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
		iShouterSeatID = Temp.getInt();
		iNextSeatID = Temp.getInt();
		iDice = Temp.getInt();
		iCount = Temp.getInt();
		nShoutOne = Temp.getShort();
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBNotifyShout [iShouterSeatID=" + iShouterSeatID
				+ ", iNextSeatID=" + iNextSeatID + ", iDice=" + iDice
				+ ", iCount=" + iCount + "]";
	}

}
