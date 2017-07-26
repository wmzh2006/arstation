/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspBusiness.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-30 下午06:57:32
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBRspBusiness implements MsgPara {
	public short	nResult;			//结果
	public String   iMsg;				//消息
	public int     	iGoodsID;			//商品ID
	public int		iCount;				//此商品的交易个数
	public int		iMoney;				//交易的凤智币
	public int		iGb;				//交易的游戏币
	/**
	 * construct
	 */
	public MBRspBusiness() {
		nResult = -1;
		iMsg = "";
		iGoodsID = 0;
		iCount = 0;
		iMoney = 0;
		iGb = 0;
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
		int shLegth = 2;
		shLegth += aOutLength;
		Temp.position(shLegth);
		Temp.putShort(nResult);
		int msglen = iMsg.getBytes().length;
		Temp.putShort((short) msglen);
		if(msglen > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iGoodsID);
		Temp.putInt(iCount);
		Temp.putInt(iMoney);
		Temp.putInt(iGb);
		shLegth = Temp.position();
		shLegth -= aOutLength;
		Temp.putShort(aOutLength, (short) shLegth);
		return Temp.position();
	}

	/* 
	 * @see com.funoble.lyingdice.socket.protocol.MsgPara#Decode(byte[], short)
	 */
	@Override
	public int Decode(byte[] aInBuffer, short aInLength) {
		if(aInBuffer == null || aInLength < 0) {
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
		nResult = Temp.getShort();
		int msglen = Temp.getShort();
		if(msglen > 0) {
			byte[] msg = new byte[msglen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iGoodsID = Temp.getInt();
		iCount = Temp.getInt();
		iMoney = Temp.getInt();
		iGb = Temp.getInt();
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspBusiness [nResult=" + nResult + ", iMsg=" + iMsg
				+ ", iGoodsID=" + iGoodsID + ", iCount=" + iCount + ", iMoney="
				+ iMoney + ", iGb=" + iGb + "]";
	}

}
