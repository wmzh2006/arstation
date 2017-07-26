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


public class MBNotifyChangeStatus implements MsgPara {
	public int		iSeatID;	//座位ID (0 ~ 5 表示座位ID，其它表示房间)
	public int		iType;		//类型
	public int		iValue;		//数据
	public String	szStrs;	//图片名称
	
	/**
	 * construct
	 */
	public MBNotifyChangeStatus() {
		iSeatID = 0;
		iType = 0;
		iValue = 0;
		szStrs = "";
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
		Temp.putInt(iSeatID);
		Temp.putInt(iType);
		Temp.putInt(iValue);
		Temp.putShort((short) szStrs.getBytes().length);
		if(szStrs.length() > 0) {
			Temp.put(szStrs.getBytes());
		}
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
		iSeatID = Temp.getInt();
		iType = Temp.getInt();
		iValue = Temp.getInt();
		int len = Temp.getShort();
		if(len > 0) {
			byte[] data = new byte[len];
			Temp.get(data);
			szStrs = new String(data);
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBNotifyChangeStatus [iSeatID=" + iSeatID + ", iType=" + iType
				+ ", iValue=" + iValue + ", szStrs=" + szStrs + "]";
	}
}
