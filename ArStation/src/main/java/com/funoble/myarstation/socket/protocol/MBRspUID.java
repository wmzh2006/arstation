/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspAccount.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 上午10:32:37
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBRspUID implements MsgPara {
	public String iDesUserName; //目标玩家帐号
	public int		iDesUserID;	//目标玩家的UID

	/**
	 * construct
	 */
	public MBRspUID() {
		iDesUserName = "";
		iDesUserID = 0;
	}

	/* 
	 * @see com.funoble.lyingdice.socket.protocol.MsgPara#Encode(byte[], short)
	 */
	@Override
	public int Encode(byte[] aOutBuffer, short aOutLength) {
		if(aOutBuffer == null || aOutLength < 0) {
			return -1;
		}
		ByteBuffer Temp  = ByteBuffer.wrap(aOutBuffer);
		int shLenght = 2;
		shLenght += aOutLength;
		Temp.position(aOutLength);
		Temp.putShort((short) iDesUserName.getBytes().length);
		if(iDesUserName.length() > 0) {
			Temp.put(iDesUserName.getBytes());
		}
		Temp.putInt(iDesUserID);
		shLenght = Temp.position();
		shLenght -= shLenght;
		Temp.putShort(aOutLength, (short) shLenght);
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
		int shLenght = 0;
		shLenght = Temp.getShort();
		if(shLenght > aInBuffer.length) {
			return -2;
		}
		int AccountLen = Temp.getShort();
		if(AccountLen > 0) {
			byte[] msg = new byte[AccountLen];
			Temp.get(msg);
			iDesUserName = new String(msg);
			msg = null;
		}
		iDesUserID = Temp.getInt();
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspUID [iDesUserName=" + iDesUserName + ", iDesUserID="
				+ iDesUserID + "]";
	}

}
