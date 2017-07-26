/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqVisitorLogin.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-17 上午10:42:36
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;



public class MBReqVisitorLogin implements MsgPara {
	
	public int iVersion; //客户端当前版本号

	public MBReqVisitorLogin() {
		iVersion = 0;
	}
	
	/* 
	 * @see com.funoble.lyingdice.protocol.MsgPara#Encode(byte[], short)
	 */
	@Override
	public int Encode(byte[] aOutBuffer, short aOutLength) {
		if(aOutBuffer == null) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.wrap(aOutBuffer);
		short sTempLength = aOutLength;
		sTempLength += 2;
		Temp.position(sTempLength);
		Temp.putInt(iVersion);
		sTempLength = (short) (Temp.position() - aOutLength);
		Temp.putShort(aOutLength, sTempLength);
		
		return  Temp.position();
	}

	/* 
	 * @see com.funoble.lyingdice.protocol.MsgPara#Decode(byte[], short)
	 */
	@Override
	public int Decode(byte[] aInBuffer, short aInLength) {
		if(aInBuffer == null || aInLength < 0 ) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.allocate(aInBuffer.length + 2) ;
		Temp.put(aInBuffer);
		Temp.position(aInLength);
		short shLeftLength = 0;
		short sTempLength = 0;
		sTempLength = Temp.getShort();
		if(shLeftLength > aInBuffer.length ) {
			return -1;
		}
		iVersion = Temp.getInt();
		sTempLength = (short) Temp.position();
		return sTempLength;
	}

}
