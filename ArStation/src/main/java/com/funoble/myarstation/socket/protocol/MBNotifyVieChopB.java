/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBNotifyReady.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-21 下午02:39:22
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;

import android.R.integer;


public class MBNotifyVieChopB implements MsgPara {
	public int		iVieChopSeatID;				//抢劈者的座位ID
	public int		iDesSeatID;					//被劈的座位ID
	public int		iType;						//0 --- 被档	大于或等于1 --- 中招
	public int		iNextDesSeatID;				//反弹到的目标座位ID
	public int		iNextDesType;				//0 --- 被档	大于或等于1 --- 中招

	/**
	 * construct
	 */
	public MBNotifyVieChopB() {
		iVieChopSeatID = 0;
		iDesSeatID = 0;
		iType = 0;
		iNextDesSeatID = 0;
		iNextDesType = 0;
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
		int shLength = 0;
		shLength += aOutLength;
		Temp.position(shLength);
		Temp.putInt(iVieChopSeatID);
		Temp.putInt(iDesSeatID);
		Temp.putInt(iType);
		Temp.putInt(iNextDesSeatID);
		Temp.putInt(iNextDesType);
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
		if(aInBuffer == null) {
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
		iVieChopSeatID = Temp.getInt();
		iDesSeatID = Temp.getInt();
		iType = Temp.getInt();
		iNextDesSeatID = Temp.getInt();
		iNextDesType = Temp.getInt();
		shLength = Temp.position();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBNotifyVieChopB [iVieChopSeatID=" + iVieChopSeatID + " iDesSeatID=" + iDesSeatID 
					+ " iType=" + iType 
					+ " iNextDesSeatID=" + iNextDesSeatID 
					+ " iNextDesType=" + iNextDesType  + "]";
	}

}
