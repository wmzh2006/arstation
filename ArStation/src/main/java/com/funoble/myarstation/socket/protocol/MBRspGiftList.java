/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspGiftList.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-24 上午11:14:10
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


public class MBRspGiftList implements MsgPara {
	public short    iResult;						//结果
	public String	iMsg;							//消息
	public int		iCount;						
	public List<String> iGiftMsgs;
	/**
	 * construct
	 */
	public MBRspGiftList() {
		iResult = -1;
		iMsg = "";
		iCount = 0;
		iGiftMsgs = new ArrayList<String>();
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
		Temp.putInt(iCount);
		for(int i = 0; i < iCount; i++) {
			Temp.putShort((short) iGiftMsgs.get(i).getBytes().length);
			Temp.put(iGiftMsgs.get(i).getBytes());
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
		iResult = Temp.getShort();
		int msglen = Temp.getShort();
		if(msglen > 0) {
			byte[] msg = new byte[msglen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iCount = Temp.getInt();
		for(int i = 0; i < iCount; i++) {
			int len = Temp.getShort();
			if(len > 0) {
				byte[] gifmsg = new byte[len];
				Temp.get(gifmsg);
				iGiftMsgs.add(new String(gifmsg));
				gifmsg = null;
				
			}
		}
		shLength = Temp.position();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspGiftList [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iCount=" + iCount + ", iGiftMsgs=" + iGiftMsgs + "]";
	}
}
