/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBNotifyLoginRoom.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 下午06:52:44
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBNotifyLoginRoom implements MsgPara {
	public int		iSeatID;		//用户ID
	public String	iUserName;		//玩家的名字
	public int     	iModelID;  		//玩家的模型id
	public int		iUserState;		//玩家的状态					
	public int     	iCurrentDrunk;	//玩家的醉酒度
	public int     	iMaxDrunk;		//玩家的最大醉酒度（酒量）		
	public int      iUserID;		//玩家的UserID
	public String	iLittlePic;		//玩家的头像
	public int		iDicePakID;						//玩家骰子的PakID
	/**
	 * construct
	 */
	public MBNotifyLoginRoom() {
		iSeatID = -1;
		iUserName = "";
		iModelID = 0;
		iUserState = 0;
		iCurrentDrunk = 0;
		iMaxDrunk = 0;
		iUserID = 0;
		iLittlePic = "";
		iDicePakID = 0;
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
		Temp.putInt(iSeatID);
		Temp.putShort((short) iUserName.getBytes().length);
		if(iUserName.length() > 0) {
			Temp.put(iUserName.getBytes());
		}
		Temp.putInt(iModelID);
		Temp.putInt(iUserState);
		Temp.putInt(iCurrentDrunk);
		Temp.putInt(iMaxDrunk);
		Temp.putInt(iUserID);
		Temp.putShort((short) iLittlePic.getBytes().length);
		if(iLittlePic.length() > 0)	{
			Temp.put(iLittlePic.getBytes());
		}
		Temp.putInt(iDicePakID);
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
		iSeatID = Temp.getInt();
		int usernamelen = Temp.getShort();
		if(usernamelen > 0) {
			byte[] username = new byte[usernamelen];
			Temp.get(username);
			iUserName = new String(username);
			username = null;
		}
		iModelID = Temp.getInt();
		iUserState = Temp.getInt();
		iCurrentDrunk = Temp.getInt();
		iMaxDrunk = Temp.getInt();
		iUserID = Temp.getInt();
		int len = Temp.getShort();
		if(len > 0) {
			byte[] pic = new byte[len];
			Temp.get(pic);
			iLittlePic = new String(pic);
			pic = null;
		}
		iDicePakID = Temp.getInt();
		shLength = Temp.position();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBNotifyLoginRoom [iSeatID=" + iSeatID + ", iUserName="
				+ iUserName + ", iModelID=" + iModelID + ", iUserState="
				+ iUserState + ", iCurrentDrunk=" + iCurrentDrunk
				+ ", iMaxDrunk=" + iMaxDrunk + ", iUserID=" + iUserID
				+ ", iLittlePic=" + iLittlePic + ", iDicePakID=" + iDicePakID
				+ "]";
	}

}
