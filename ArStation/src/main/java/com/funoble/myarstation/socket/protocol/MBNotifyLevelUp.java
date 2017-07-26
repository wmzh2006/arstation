/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBNotifyShowReady.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-21 下午02:39:22
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;

import android.R.integer;


public class MBNotifyLevelUp implements MsgPara {
	public int				iUserID;		//升级者的UID
	public int				iSeatID;		//升级者的座位ID
	public int				iGameLevel;		//级别
	public int				iGameExp;		//当前级别的经验
	public int				iNextExp;		//升级经验
	public String			iTitle;			//人物称号
	public short			nDiceID;		//骰子资源ID


	/**
	 * construct
	 */
	public MBNotifyLevelUp() {
		iUserID = 0;		
		iSeatID = 0;		
		iGameLevel = 0;		
		iGameExp = 0;		
		iNextExp = 0;		
		iTitle = "";		
		nDiceID = 0;
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
		Temp.putInt(iUserID);
		Temp.putInt(iSeatID);
		Temp.putInt(iGameLevel);
		Temp.putInt(iGameExp);
		Temp.putInt(iNextExp);
		Temp.putShort((short) iTitle.getBytes().length);
		if(iTitle.length() > 0) {
			Temp.put(iTitle.getBytes());
		}
		Temp.putShort(nDiceID);
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
		iUserID = Temp.getInt();
		iSeatID = Temp.getInt();
		iGameLevel = Temp.getInt();
		iGameExp = Temp.getInt();
		iNextExp = Temp.getInt();
		int len = Temp.getShort();
		if(len > 0) {
			byte []title = new byte[len];
			Temp.get(title);
			iTitle = new String(title);
			title = null;
		}
		nDiceID = Temp.getShort();
		shLength = Temp.position();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBNotifyLevelUp [iUserID=" + iUserID + ", iSeatID=" + iSeatID
				+ ", iGameLevel=" + iGameLevel + ", iGameExp=" + iGameExp
				+ ", iNextExp=" + iNextExp + ", iTitle=" + iTitle + "]";
	}
}
