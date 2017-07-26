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


public class MBRspShakaFriend implements MsgPara {
	public short    iResult;						//结果
	public String	iMsg;							//消息
	public int		iDesUserID;						//目标玩家的UID
	public int		iAction;						//0 --- 显示“追踪”   1 --- 显示“摇醒TA”  2 --- 显示“还剩多久才可以摇” 3 --- 什么都不显示
	public int 		iMinute; //还剩多少分钟可以摇
	public int 		iSecond; //还剩多少秒钟可以摇
	public short 	nOnLine; //是否在线 0 --- 不在线  1 --- 在线在大厅  2 --- 游戏中
	public short 	nDrunk; //醉酒度

	/**
	 * construct
	 */
	public MBRspShakaFriend() {
		iResult = -1;
		iMsg = "";
		iDesUserID = 0;
		iAction = 0;
		iMinute = 0;
		iSecond = 0;
		nOnLine = 0;
		nDrunk = 0;
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
		Temp.putInt(iAction);
		Temp.putInt(iMinute);
		Temp.putInt(iSecond);
		Temp.putShort(nOnLine);
		Temp.putShort(nDrunk);
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
		iAction = Temp.getInt();
		iMinute = Temp.getInt();
		iSecond = Temp.getInt();
		nOnLine = Temp.getShort();
		nDrunk = Temp.getShort();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspShakaFriend [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iDesUserID=" + iDesUserID + ", iAction=" + iAction
				+ ", iMinute=" + iMinute + ", iSecond=" + iSecond
				+ ", nOnLine=" + nOnLine + ", nDrunk=" + nDrunk + "]";
	}

}
