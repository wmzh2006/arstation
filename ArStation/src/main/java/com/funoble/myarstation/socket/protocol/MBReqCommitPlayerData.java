/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqCommitPlayerData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 上午10:19:17
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBReqCommitPlayerData implements MsgPara {
	public String	iUserName;	//openid
	public String	iNick;	//昵称
	public String	iPicUrl; //相片URL
	public short	iSex; //性别
	public int		iMoney; //元宝
	public String	iExInfo; //扩展信息

	/**
	 * construct
	 */
	public MBReqCommitPlayerData() {
		iUserName = "";	//openid
		iNick = "";	//昵称
		iPicUrl = ""; //相片URL
		iSex = 0; //性别
		iMoney = 0; //元宝
		iExInfo = ""; //扩展信息
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
		Temp.putShort((short) iUserName.getBytes().length);
		if( iUserName.length() > 0) {
			Temp.put(iUserName.getBytes());
		}
		Temp.putShort((short) iNick.getBytes().length);
		if( iNick.length() > 0) {
			Temp.put(iNick.getBytes());
		}
		Temp.putShort((short) iPicUrl.getBytes().length);
		if( iPicUrl.length() > 0) {
			Temp.put(iPicUrl.getBytes());
		}
		Temp.putShort(iSex);
		Temp.putInt(iMoney);
		Temp.putShort((short) iExInfo.getBytes().length);
		if( iExInfo.length() > 0) {
			Temp.put(iExInfo.getBytes());
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
		int len = Temp.getShort();
		if(len > 0) {
			byte[] content = new byte[len];
			Temp.get(content);
			iUserName = new String(content);
			content = null;
		}
		len = Temp.getShort();
		if(len > 0) {
			byte[] content = new byte[len];
			Temp.get(content);
			iUserName = new String(content);
			content = null;
		}
		len = Temp.getShort();
		if(len > 0) {
			byte[] content = new byte[len];
			Temp.get(content);
			iNick = new String(content);
			content = null;
		}
		len = Temp.getShort();
		if(len > 0) {
			byte[] content = new byte[len];
			Temp.get(content);
			iPicUrl = new String(content);
			content = null;
		}
		iSex = Temp.getShort();
		iMoney = Temp.getInt();
		len = Temp.getShort();
		if(len > 0) {
			byte[] content = new byte[len];
			Temp.get(content);
			iExInfo = new String(content);
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
		return "MBReqCommitPlayerData [iUserName=" + iUserName + ", iNick="
				+ iNick + ", iPicUrl=" + iPicUrl + ", iSex=" + iSex
				+ ", iMoney=" + iMoney + ", iExInfo=" + iExInfo + "]";
	}
}
