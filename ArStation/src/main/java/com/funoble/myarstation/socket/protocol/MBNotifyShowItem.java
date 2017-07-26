/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBNotifyChat.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 上午10:19:17
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.Vector;

import android.webkit.WebIconDatabase.IconListener;


public class MBNotifyShowItem implements MsgPara {
	public int					iItemTypeCount;//道具有多少种类
	public Vector<Integer>		iItemIDs;	//道具的ID
	public Vector<Short>		nCanUses;	//是否可以使用 0 --- 不可以， 1 --- 可以
	public Vector<Integer>		iItemCounts;//道具有多少个


	/**
	 * construct
	 */
	public MBNotifyShowItem() {
		iItemTypeCount = 0;
		iItemIDs = new Vector<Integer>();
		nCanUses = new Vector<Short>();
		iItemCounts = new Vector<Integer>();
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
		Temp.putInt(iItemTypeCount);
		for(int i = 0; i < iItemTypeCount; i++) {
			Temp.putInt(iItemIDs.get(i));
			Temp.putShort(nCanUses.get(i));
			Temp.putInt(iItemCounts.get(i));
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
		iItemTypeCount = Temp.getInt();
		for(int i = 0; i < iItemTypeCount; i++) {
			iItemIDs.add(Temp.getInt());
			nCanUses.add(Temp.getShort());
			iItemCounts.add(Temp.getInt());
		}
		shLenght = Temp.position();
		return shLenght;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBNotifyShowItem [iItemTypeCount=" + iItemTypeCount
				+ ", iItemIDs=" + iItemIDs + ", nCanUses=" + nCanUses
				+ ", iItemCounts=" + iItemCounts + "]";
	}

}
