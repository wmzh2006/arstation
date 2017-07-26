/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqChat.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 上午10:19:17
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Vector;


public class MBReqUseItem implements MsgPara {
	public int		iItemID;					//道具ID
	public int		iDesUserCount;				//作用于多少个玩家身上
	public Vector<Integer>		iDesSeatIDs;	//目标玩家的UID


	/**
	 * construct
	 */
	public MBReqUseItem() {
		iItemID = 0;
		iDesUserCount = 0;
		iDesSeatIDs = new Vector<Integer>();
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
		Temp.putInt(iItemID);
		Temp.putInt(iDesUserCount);
		if(iDesUserCount > 6) {
			return -1;
		}
		for(int i = 0; i < iDesUserCount; i++) {
			Temp.putInt(iDesSeatIDs.get(i));
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
		iItemID = Temp.getInt();
		iDesUserCount = Temp.getInt();
		for(int i = 0; i < iDesUserCount; i++) {
			iDesSeatIDs.add(Temp.getInt());
		}
		shLenght = Temp.position();
		return shLenght;
	}

}
