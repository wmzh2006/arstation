/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqLeaveMsg.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-23 上午10:33:34
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBReqLeaveMsgB implements MsgPara {
	public int		iUserID; //用户ID
	public String   iMsg;	//留言内容
	public short	iSecret;//是否私信 0--普通、1--私信
	public short	iReply;	//是否回复0--没回复、1--回复

	/**
	 * construct
	 */
	public MBReqLeaveMsgB() {
		iUserID = -1;
		iMsg = "";
		iSecret = 0;
		iReply = 0;
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
		if(iMsg.length() > BufferSize.MAX_MSG_LEN) {
			return -1;
		}
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putShort(iSecret);
		Temp.putShort(iReply);
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
		iUserID = Temp.getInt();
		short len = Temp.getShort();
		if(len > 0) {
			byte[] msg = new byte[len];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iSecret = Temp.getShort();
		iReply = Temp.getShort();
		shLength = Temp.position();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBReqLeaveMsgB [iUserID=" + iUserID + ", iMsg=" + iMsg
				+ ", iSecret=" + iSecret + ", iReply=" + iReply + "]";
	}

}
