/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqRank.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-2 上午11:59:57
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBReqRank implements MsgPara {
	public int		iRankType;					//排行榜类型
	public int		iStartIndex;				//开始索引号

	/**
	 * construct
	 */
	public MBReqRank() {
		iRankType = 0;
		iStartIndex = 0;
	}

	/* 
	 * @see com.funoble.lyingdice.socket.protocol.MsgPara#Encode(byte[], short)
	 */
	@Override
	public int Encode(byte[] aOutBuffer, short aOutLength) {
		if(aOutBuffer == null || aOutLength <= 0) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.wrap(aOutBuffer);
		int shLength = 2;
		shLength += aOutLength;
		Temp.position(shLength);
		Temp.putInt(iRankType);
		Temp.putInt(iStartIndex);
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
		if(aInBuffer == null || aInLength <= 0) {
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
		iRankType = Temp.getInt();
		iStartIndex = Temp.getInt();
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBReqRank [iRankType=" + iRankType + ", iStartIndex=" + iStartIndex
				+ "]";
	}

}
