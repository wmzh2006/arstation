/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBNotifyRecharge.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-24 上午11:14:10
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBNotifyRecharge implements MsgPara {
	public short    iResult;						//结果
	public String	iMsg;							//消息
	public int 		iUserID; 						//用户ID
	public int 		iMoney; 						//当前凤智币


	/**
	 * construct
	 */
	public MBNotifyRecharge() {
		iResult = -1;
		iMsg = "";
		iUserID = 0;
		iMoney = 0;
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
		Temp.putInt(iUserID);
		Temp.putInt(iMoney);
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
		iUserID = Temp.getInt();
		iMoney = Temp.getInt();
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBNotifyRecharge [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iUserID=" + iUserID + ", iMoney=" + iMoney + "]";
	}
}
