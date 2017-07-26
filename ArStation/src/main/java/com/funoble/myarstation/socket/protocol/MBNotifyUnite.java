/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBNotifyUnite.java 
 * Description://同(解)盟通知
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-21 下午02:39:22
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBNotifyUnite implements MsgPara {
	public int		iType;				//类型 0 --- 结盟    1 --- 解散
	public int		iSeatAUID;			//A座位的UID
	public int		iSeatBUID;			//B座位的UID
	public int		iSeatAID;			//A座位ID
	public int		iSeatBID;			//B座位ID
	public int		iGroupID;			//组队ID 0 --- 红色   1 --- 绿色    2 ---- 蓝色    3 --- 紫
	public String   iMsg;
	/**
	 * construct
	 */
	public MBNotifyUnite() {
		iType = 0;
		iSeatAUID = 0;
		iSeatBUID = 0;
		iSeatAID = 0;
		iSeatBID = 0;
		iGroupID = 0;
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
		int shLength = 0;
		shLength += aOutLength;
		Temp.position(shLength);
		Temp.putInt(iType);
		Temp.putInt(iSeatAUID);
		Temp.putInt(iSeatBUID);
		Temp.putInt(iSeatAID);
		Temp.putInt(iSeatBID);
		Temp.putInt(iGroupID);
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
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
		if(aInBuffer == null) {
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
		iType = Temp.getInt();
		iSeatAUID = Temp.getInt();
		iSeatBUID = Temp.getInt();
		iSeatAID = Temp.getInt();
		iSeatBID = Temp.getInt();
		iGroupID = Temp.getInt();
		int len = Temp.getShort();
		if(len > 0) {
			byte [] data = new byte[len];
			Temp.get(data);
			iMsg = new String(data);
			data = null;
		}
		shLength = Temp.position();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBNotifyUnite [iType=" + iType + ", iSeatAUID=" + iSeatAUID
				+ ", iSeatBUID=" + iSeatBUID + ", iSeatAID=" + iSeatAID
				+ ", iSeatBID=" + iSeatBID + ", iGroupID=" + iGroupID
				+ ", iMsg=" + iMsg + "]";
	}

}
