/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqLogin.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-17 上午10:42:36
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;



public class MBReqLogin implements MsgPara {
	
	public String iUserName;
	public String iUserPswd;
	public int iVersion; //客户端当前版本号

	public MBReqLogin() {
		iUserName = "";
		iUserPswd = "";
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
		if(iUserName.length() > BufferSize.MAX_USERID_LEN || iUserPswd.length() > BufferSize.MAX_PWD_LEN) {
			return -1;
		}
		Temp.putShort((short) iUserName.length());
		Temp.put(iUserName.getBytes());
		
		Temp.putShort((short) iUserPswd.length());
		Temp.put(iUserPswd.getBytes());
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
		int userNameLen = Temp.getShort();
		if(userNameLen > BufferSize.MAX_USERID_LEN) {
			return -1;
		}
		if(userNameLen > 0) {
			byte[] userName = new byte[userNameLen];
			Temp.get(userName);
			iUserName = new String(userName);
		}
		
		int userPswLen = Temp.getShort();
		if(userPswLen > BufferSize.MAX_PWD_LEN) {
			return -1;
		}
		if(userPswLen > 0) {
			byte[] userPsw = new byte[userPswLen];
			Temp.get(userPsw);
			iUserPswd = new String(userPsw);
		}
		
		iVersion = Temp.getInt();
		sTempLength = (short) Temp.position();
		return sTempLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBReqLogin [iUserName=" + iUserName + ", iUserPswd="
				+ iUserPswd + "]";
	}

}
