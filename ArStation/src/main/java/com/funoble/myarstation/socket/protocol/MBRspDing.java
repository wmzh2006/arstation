/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspDing.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-24 上午11:14:10
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBRspDing implements MsgPara {
	public short    iResult;						//结果
	public String	iMsg;							//消息
	public int		iDesUserID;						//目标玩家的UID
	public int		iSupportCount;					//支持次数
	public int		iAddIntimate;		//增加的亲密度
	public int		iAddGB;				//增
	/**
	 * construct
	 */
	public MBRspDing() {
		iResult = -1;
		iMsg = "";
		iDesUserID = 0;
		iSupportCount = 0;
		iAddIntimate = 0;
		iAddGB = 0;
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
		Temp.putShort(iResult);
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iDesUserID);
		Temp.putInt(iSupportCount);
		Temp.putInt(iAddIntimate);
		Temp.putInt(iAddGB);
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
		iResult = Temp.getShort();
		int msglen = Temp.getShort();
		if(msglen > 0) {
			byte[] msg = new byte[msglen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iDesUserID = Temp.getInt();
		iSupportCount = Temp.getInt();
		iAddIntimate = Temp.getInt();
		iAddGB = Temp.getInt();
		shLength = Temp.position();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspDing [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iDesUserID=" + iDesUserID + ", iSupportCount="
				+ iSupportCount + ", iAddIntimate=" + iAddIntimate
				+ ", iAddGB=" + iAddGB + "]";
	}
}
