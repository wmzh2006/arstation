/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqDing.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-24 上午11:05:42
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBNotifyPlayAnimationTwo implements MsgPara {
	//pak名称|起始位置类型(0---位置 1---坐标)|位置【X坐标】|0【Y坐标】
			//|结束位置类型(0---位置 1---坐标)|位置【X坐标】|0【Y坐标】|开始动画ID|开始动画动作|结束动画ID|结束动画动作
	public String szAnimDes; //
	/**
	 * construct
	 */
	public MBNotifyPlayAnimationTwo() {
		szAnimDes = "";
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
		Temp.putShort((short) szAnimDes.getBytes().length);
		if(szAnimDes.length() > 0) {
			Temp.put(szAnimDes.getBytes());
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
		int len = Temp.getShort();
		if(len > 0 ) {
			byte[] data = new byte[len];
			Temp.get(data);
			szAnimDes = new String(data);
			data = null;
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBNotifyPlayAnimationTwo [szAnimDes=" + szAnimDes + "]";
	}

}
