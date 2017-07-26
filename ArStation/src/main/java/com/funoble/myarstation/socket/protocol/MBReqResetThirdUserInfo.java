/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqRegister.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-17 下午06:24:11
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBReqResetThirdUserInfo implements MsgPara {
	public String iUserID;
	public short  iPwdQFlag;		//提示问题标识,不能是0，以1开始，客户端自定义
	public String iPwdQ;
	public String iUserPswd;

	/**
	 * construct
	 */
	public MBReqResetThirdUserInfo() {
		iUserID = "";
		iPwdQFlag = 1;
		iPwdQ = "";
		iUserPswd = "";
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
		short shLenght = 2;
		shLenght += aOutLength;
		Temp.position(shLenght);
		if(iUserID.length() > BufferSize.MAX_USERID_LEN || iUserPswd.length() > BufferSize.MAX_PWD_LEN) {
			return -1;
		}
		Temp.putShort((short) iUserID.length());
		Temp.put(iUserID.getBytes());
		Temp.putShort(iPwdQFlag);
		if(iPwdQ.length() <= 0 || iPwdQ.length() > 32) {
			return -1;
		}
		Temp.putShort((short) iPwdQ.getBytes().length);
		if(iPwdQ.length() > 0) {
			Temp.put(iPwdQ.getBytes());
		}
		Temp.putShort((short) iUserPswd.length());
		Temp.put(iUserPswd.getBytes());
		shLenght = (short) ((short) Temp.position() - aOutLength);
		
		Temp.putShort(aOutLength, shLenght);
		return Temp.position();
	}

	/* 
	 * @see com.funoble.lyingdice.socket.protocol.MsgPara#Decode(byte[], short)
	 */
	@Override
	public int Decode(byte[] aIntBuffer, short aInLength) {
		if(aIntBuffer == null || aInLength < 0) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.allocate(aIntBuffer.length + 2) ;
		Temp.put(aIntBuffer);
		Temp.position(aInLength);
		short shLeftLength = 0;
		short sTempLength = 0;
		sTempLength = Temp.getShort();
		if(shLeftLength > aIntBuffer.length ) {
			return -1;
		}
		int userNameLen = Temp.getShort();
		if(userNameLen > BufferSize.MAX_USERID_LEN) {
			return -1;
		}
		if(userNameLen > 0) {
			byte[] userName = new byte[userNameLen];
			Temp.get(userName);
			iUserID = new String(userName);
			userName = null;
		}
		
		iPwdQFlag = Temp.getShort();
		int len = Temp.getShort();
		if(len > 0) {
			byte[] data = new byte[len];
			Temp.get(data);
			iPwdQ = new String(data);
			data = null;
		}
		int userPswLen = Temp.getShort();
		if(userPswLen > BufferSize.MAX_PWD_LEN) {
			return -1;
		}
		if(userPswLen > 0) {
			byte[] userPsw = new byte[userPswLen];
			Temp.get(userPsw);
			iUserPswd = new String(userPsw);
			userPsw = null;
		}
		sTempLength = (short) Temp.position();
		return sTempLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBReqResetThirdUserInfo [iUserID=" + iUserID + ", iUserPswd=" + iUserPswd
				+ "]";
	}

}
