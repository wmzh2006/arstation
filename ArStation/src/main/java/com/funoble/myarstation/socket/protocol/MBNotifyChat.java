/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBNotifyChat.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 上午10:19:17
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBNotifyChat implements MsgPara {

	public short 		nResult; //结果
	public String 		iMsg;//消息
	public int 			iSeatID;//座位ID

	/**
	 * construct
	 */
	public MBNotifyChat() {
		nResult = 0;
		iMsg = ""; 
		iSeatID = -1;
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
		Temp.putShort(nResult);
		if(iMsg.length() > BufferSize.MAX_CHATMSG_LEN) {
			return -1;
		}
		Temp.putShort((short) iMsg.getBytes().length);
		if( iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iSeatID);
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
		nResult = Temp.getShort();
		int msgLen = Temp.getShort();
		if(msgLen > BufferSize.MAX_CHATMSG_LEN) {
			return -1;
		}
		if(msgLen > 0) {
			byte[] msg = new byte[msgLen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iSeatID = Temp.getInt();
		shLenght = Temp.position();
		return shLenght;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBNotifyChat [nResult=" + nResult + ", iMsg=" + iMsg
				+ ", iSeatID=" + iSeatID + "]";
	}
}
