/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqDing.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-24 上午11:05:42
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBReqSetPwdQuestion implements MsgPara {
	public short  iPwdQFlag;		//提示问题标识,不能是0，以1开始，客户端自定义
	public String iPwdQ;

	/**
	 * construct
	 */
	public MBReqSetPwdQuestion() {
		iPwdQFlag = 1;
		iPwdQ = "";
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
		Temp.putShort(iPwdQFlag);
		if(iPwdQ.length() <= 0 || iPwdQ.length() > 32) {
			return -1;
		}
		Temp.putShort((short) iPwdQ.getBytes().length);
		if(iPwdQ.length() > 0) {
			Temp.put(iPwdQ.getBytes());
		}
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
		iPwdQFlag = Temp.getShort();
		int len = Temp.getShort();
		if(len > 0) {
			byte[] data = new byte[len];
			Temp.get(data);
			iPwdQ = new String(data);
			data = null;
		}
		return Temp.position();
	}

}
