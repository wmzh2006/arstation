/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspLeaveRoom.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-24 上午11:14:10
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBRspLeaveRoom implements MsgPara {
	public short    iResult;						//结果
	public String	iMsg;							//消息
	public String	iWineName;						//退给系统的酒的名字
	public int		iPrice;							//退给系统的酒的单价
	public int		iWineCount;						//退给系统的酒的数量	
	public int		iGB;							//玩家目前的总金币

	/**
	 * construct
	 */
	public MBRspLeaveRoom() {
		iResult = -1;
		iMsg = "";
		iWineName = "";
		iPrice = 0;
		iWineCount = 0;
		iGB = 0;
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
		Temp.putShort((short) iWineName.getBytes().length);
		if(iWineName.length() > 0) {
			Temp.put(iWineName.getBytes());
		}
		Temp.putInt(iPrice);
		Temp.putInt(iWineCount);
		Temp.putInt(iGB);
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
		int wineNamelen = Temp.getShort();
		if(wineNamelen > 0) {
			byte[] winename = new byte[wineNamelen];
			Temp.get(winename);
			iWineName = new String(winename);
			winename = null;
		}
		iPrice = Temp.getInt();
		iWineCount = Temp.getInt();
		iGB = Temp.getInt();
		shLength = Temp.position();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspLeaveRoom [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iWineName=" + iWineName + ", iPrice=" + iPrice
				+ ", iWineCount=" + iWineCount + ", iGB=" + iGB + "]";
	}
}
