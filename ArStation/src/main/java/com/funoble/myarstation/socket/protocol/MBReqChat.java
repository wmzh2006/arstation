/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqChat.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 上午10:19:17
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBReqChat implements MsgPara {

	public String iMsg;//消息

	/**
	 * construct
	 */
	public MBReqChat() {
		iMsg = ""; 
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
		int shLenght = 2;
		shLenght += aOutLength;
		Temp.position(shLenght);
		if(iMsg.length() > BufferSize.MAX_CHATMSG_LEN) {
			return -1;
		}
		Temp.putShort((short) iMsg.getBytes().length);
		if( iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		shLenght = Temp.position();
		shLenght -= aOutLength;
		Temp.putShort(aOutLength, (short) shLenght);
		if(shLenght > aOutBuffer.length) {
			return -1;
		}
		return Temp.position();
	}

	/* 
	 * @see com.funoble.lyingdice.socket.protocol.MsgPara#Decode(byte[], short)
	 */
	@Override
	public int Decode(byte[] aInBuffer, short aInLength) {
		if(aInBuffer == null || aInLength > aInBuffer.length) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.allocate(aInBuffer.length + 2);
		Temp.put(aInBuffer);
		Temp.position(aInLength);
		int shLenght = 0;
		shLenght = Temp.getShort();
		if(shLenght > aInBuffer.length) {
			return -1;
		}
		int msglen = Temp.getShort();
		if(msglen > BufferSize.MAX_CHATMSG_LEN) {
			return -1;
		}
		if(msglen > 0) {
			byte[] msg = new byte[msglen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		shLenght = Temp.position();
		return shLenght;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBReqChat [iMsg=" + iMsg + "]";
	}
}
