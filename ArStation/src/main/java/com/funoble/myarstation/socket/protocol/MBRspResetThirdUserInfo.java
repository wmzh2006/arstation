/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspRegister.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-17 下午06:24:49
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBRspResetThirdUserInfo implements MsgPara {
	public short	nResult;					//是否成功, 0 --- 表示成功
	public String	iUserID;		
	public String 	iMsg;			//信息

	public MBRspResetThirdUserInfo() {
		nResult = -1;
		iUserID = "";
		iMsg = "";
	}
	
	/* 
	 * @see com.funoble.lyingdice.socket.protocol.MsgPara#Encode(byte[], short)
	 */
	@Override
	public int Encode(byte[] aOutBuffer, short aOutLength) {
		if(aOutBuffer == null) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.wrap(aOutBuffer);
		short shLenght = 2;
		shLenght += aOutLength;
		Temp.position(shLenght);
		Temp.putShort(nResult);
		if(iUserID.length() > BufferSize.MAX_USERID_LEN) {
			return -1;
		}
		Temp.putShort((short) iUserID.length());
		Temp.put(iUserID.getBytes());
		Temp.putShort((short) iMsg.length());
		Temp.put(iMsg.getBytes());
		shLenght = (short) (Temp.position() - aOutLength);
		Temp.putShort(aOutLength, shLenght);
		return Temp.position();
	}

	/* 
	 * @see com.funoble.lyingdice.socket.protocol.MsgPara#Decode(byte[], short)
	 */
	@Override
	public int Decode(byte[] aInBuffer, short aInLength) {
		if(aInBuffer == null || aInLength < 0) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.allocate(aInBuffer.length + 2);
		Temp.put(aInBuffer);
		Temp.position(aInLength);
		short shLength = 0;
		shLength = Temp.getShort();
		if(shLength > aInBuffer.length) {
			return -2;
		}
		nResult = Temp.getShort();
		int useIDlen = Temp.getShort();
		if(useIDlen > 0) {
			byte[] useID = new byte[useIDlen];
			Temp.get(useID);
			iUserID = new String(useID);
			useID = null;
		}
		int msgLen = Temp.getShort();
		if(msgLen > 0) {
			byte[] msg = new byte[msgLen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		shLength = (short) Temp.position();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspResetThirdUserInfo [nResult=" + nResult + ", iMsg=" + iMsg
				+ ", iUserID=" + iUserID + "]";
	}

}
