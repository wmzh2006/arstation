/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqLoginRoom.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 下午03:55:08
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBReqLoginVipRoom implements MsgPara {

	public int		iRoomID;					//房间ID
	public int		iAction;		//动作		0 --- 订房     1 --- 进入
	public int		iPara;			//底注倍数
	public String	iUserPwd;		//用户密码


	/**
	 * construct
	 */
	public MBReqLoginVipRoom() {
		iRoomID = 0;
		iAction = 0;
		iPara = 0;
		iUserPwd = "";
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
		int shLength = 2;
		shLength += aOutLength;
		Temp.position(shLength);
		Temp.putInt(iRoomID);
		Temp.putInt(iAction);
		Temp.putInt(iPara);
		Temp.putShort((short) iUserPwd.getBytes().length);
		if(iUserPwd.length() > 0) {
			Temp.put(iUserPwd.getBytes());
		}
		shLength = Temp.position();
		shLength -= aOutLength;
		if(shLength < 0) {
			return -1;
		}
		Temp.putShort(aOutLength, (short) shLength);
		return Temp.position();
	}

	/* 
	 * @see com.funoble.lyingdice.socket.protocol.MsgPara#Decode(byte[], short)
	 */
	@Override
	public int Decode(byte[] aInBuffer, short aInLength) {
		if(aInBuffer == null) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.allocate(aInBuffer.length + 2);
		Temp.put(aInBuffer);
		Temp.position(aInLength);
		int shlength = 0;
		shlength = Temp.getShort();
		if(shlength > aInBuffer.length) {
			return -1;
		}
		iRoomID = Temp.getInt();
		iAction = Temp.getInt();
		iPara = Temp.getInt();
		int len = Temp.getShort();
		if(len > 0) {
			byte[] data = new byte[len];
			Temp.get(data);
			iUserPwd = new String(data);
			data = null;
		}
		shlength = Temp.position();
		return shlength;
	}

}
