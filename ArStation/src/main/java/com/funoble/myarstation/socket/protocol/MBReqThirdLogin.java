/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqRank.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-2 上午11:59:57
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBReqThirdLogin implements MsgPara {

	public String 	iUserName;
	public int		iChannelID;	//渠道号
	public int		iVersion;	//客户端当前版本号

	/**
	 * construct
	 */
	public MBReqThirdLogin() {
		iUserName = "";
		iChannelID = 0;
		iVersion = 0;
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
		Temp.putShort((short) iUserName.getBytes().length);
		if(iUserName.length() > 0) {
			Temp.put(iUserName.getBytes());
		}
		Temp.putInt(iChannelID);
		Temp.putInt(iVersion);
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
		int len = Temp.getShort();
		if(len > 0) {
			byte[] userid = new byte[len];
			Temp.get(userid);
			iUserName = new String(userid);
			userid = null;
		}
		iChannelID = Temp.getInt();
		iVersion = Temp.getInt();
		return Temp.position();
	}


}
