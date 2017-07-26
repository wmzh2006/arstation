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


public class MBNotifyActivityInfo implements MsgPara {
	public int		iActivityCount;			//待报名的活动个数
	public int     	iHasActivity;			//今天是否有活动 0 --- 没有    1 --- 有			
	
	/**
	 * construct
	 */
	public MBNotifyActivityInfo() {
		iActivityCount = 0;
		iHasActivity = 0;
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
		Temp.putInt(iActivityCount);
		Temp.putInt(iHasActivity);
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
		iActivityCount = Temp.getInt();
		iHasActivity = Temp.getInt();
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBNotifyActivityInfo [iActivityCount=" + iActivityCount + ", iHasActivity="
				+ iHasActivity + "]";
	}

}
