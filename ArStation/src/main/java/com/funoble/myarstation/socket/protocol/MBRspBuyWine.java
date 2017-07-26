/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspBuyWine.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-24 上午11:14:10
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBRspBuyWine implements MsgPara {
	public short    iResult;						//结果
	public String	iMsg;							//消息
	public int		iCostGb;						//使用了多少金币
	public int		iCurrentGb;						//解酒请求
	public int		iSeatID; 						//买酒的座位ID
	public String 	iWineName; 						//酒的名称
	public int		iPrice;						//单价
	public int		iWineCount;					//酒的数量

	/**
	 * construct
	 */
	public MBRspBuyWine() {
		iResult = -1;
		iMsg = "";
		iCostGb = 0;
		iCurrentGb = 0;
		iSeatID = 0;
		iWineName = "";
		iPrice = 0;
		iWineCount = 0;
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
		Temp.putInt(iCostGb);
		Temp.putInt(iCurrentGb);
		Temp.putInt(iSeatID);
		Temp.putShort((short) iWineName.getBytes().length);
		if(iWineName.length() > 0) {
			Temp.put(iWineName.getBytes());
		}
		Temp.putInt(iPrice);
		Temp.putInt(iWineCount);
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
		iCostGb = Temp.getInt();
		iCurrentGb = Temp.getInt();
		iSeatID = Temp.getInt();
		int len = Temp.getShort();
		if(len > 0) {
			byte[] wineName = new byte[len];
			Temp.get(wineName);
			iWineName = new String(wineName);
			wineName = null;
		}
		iPrice = Temp.getInt();
		iWineCount = Temp.getInt();
		shLength = Temp.position();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspBuyWine [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iCostGb=" + iCostGb + ", iCurrentGb=" + iCurrentGb
				+ ", iSeatID=" + iSeatID + ", iWineName=" + iWineName
				+ ", iPrice=" + iPrice + ", iWineCount=" + iWineCount + "]";
	}
}
