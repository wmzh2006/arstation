/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBNotifyStart.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-22 下午12:28:42
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.Vector;


public class MBNotifyStartUnite implements MsgPara {
	public	Vector<Integer>	iDice;				//骰子的点数
	public	int		iShouterSeatID;				//喊点者的座位ID
	public 	int		iPlayerCount;				//玩家的个数		
	public	Vector<Integer>	iUniteDice;			//骰子的点数
	/**
	 * construct
	 */
	public MBNotifyStartUnite() {
		iDice = new Vector<Integer>();
		iShouterSeatID = -1;
		iPlayerCount = 2;
		iUniteDice = new Vector<Integer>();
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
		for(int i = 0; i < BufferSize.MAX_DICE_COUNT; i++) {
			Temp.putInt(iDice.get(i));
		}
		Temp.putInt(iShouterSeatID);
		Temp.putInt(iPlayerCount);
		for(int i = 0; i < BufferSize.MAX_DICE_COUNT; i++) {
			Temp.putInt(iUniteDice.get(i));
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
		for(int i = 0; i < BufferSize.MAX_DICE_COUNT; i++) {
			iDice.add(Temp.getInt());
		}
		iShouterSeatID = Temp.getInt();
		iPlayerCount = Temp.getInt();
		for(int i = 0; i < BufferSize.MAX_DICE_COUNT; i++) {
			iUniteDice.add(Temp.getInt());
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBNotifyStartUnite [iDice=" + iDice + ", iShouterSeatID="
				+ iShouterSeatID + ", iPlayerCount=" + iPlayerCount
				+ ", iUniteDice=" + iUniteDice + "]";
	}
}
