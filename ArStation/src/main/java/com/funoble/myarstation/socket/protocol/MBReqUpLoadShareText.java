/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqUpLoadShareText.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 上午10:19:17
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBReqUpLoadShareText implements MsgPara {

	public String iContent;//消息

	/**
	 * construct
	 */
	public MBReqUpLoadShareText() {
		iContent = ""; 
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
		if(iContent.length() > BufferSize.MAX_SHARE_TEXT_LEN) {
			return -1;
		}
		Temp.putShort((short) iContent.getBytes().length);
		if( iContent.length() > 0) {
			Temp.put(iContent.getBytes());
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
		int contentlen = Temp.getShort();
		if(contentlen > 0) {
			byte[] content = new byte[contentlen];
			Temp.get(content);
			iContent = new String(content);
			content = null;
		}
		shLenght = Temp.position();
		return shLenght;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBReqUpLoadShareText [iContent=" + iContent + "]";
	}
}
