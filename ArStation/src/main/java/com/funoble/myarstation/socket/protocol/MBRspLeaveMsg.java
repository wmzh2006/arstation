/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqLeaveMsg.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-23 上午10:33:34
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBRspLeaveMsg implements MsgPara {
	public short	iResult; 	//用户ID
	public String   iMsg; 		//消息
	public int		iLeaveMsgID;	 //留言的ID									
	public int		iUID;			 //留言者的UID
	public String	iNick;
	public String	iPic;
	public int		iTime;			 //留言时间
	public String   iText;			 //内容
	/**
	 * construct
	 */
	public MBRspLeaveMsg() {
		iResult = -1;
		iMsg = "";
		iLeaveMsgID = 0;
		iUID = 0;
		iNick = "";
		iPic = "";
		iTime = 0;
		iText = "";
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
		int shLength =  2;
		shLength += aOutLength;
		Temp.position(shLength);
		Temp.putShort(iResult);
		if(iMsg.length() > BufferSize.MAX_MSG_LEN) {
			return -1;
		}
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iLeaveMsgID);
		Temp.putInt(iUID);
		Temp.putShort((short) iNick.getBytes().length);
		if(iNick.length() > 0) {
			Temp.put(iNick.getBytes());
		}
		Temp.putShort((short) iPic.getBytes().length);
		if(iPic.length() > 0) {
			Temp.put(iPic.getBytes());
		}
		Temp.putInt(iTime);
		Temp.putShort((short) iText.getBytes().length);
		if(iText.length() > 0) {
			Temp.put(iText.getBytes());
		}
		shLength = Temp.position();
		shLength -= aOutLength;
		Temp.putShort(aOutLength, aOutLength);
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
		short len = Temp.getShort();
		if(len > 0) {
			byte[] msg = new byte[len];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iLeaveMsgID = Temp.getInt();
		iUID = Temp.getInt();
		short iNickLen = Temp.getShort();
		if(iNickLen > 0) {
			byte[] nick = new byte[iNickLen];
			Temp.get(nick);
			iNick = new String(nick);
			nick = null;
		}
		short iPicLen = Temp.getShort();
		if(iPicLen > 0) {
			byte[] pic = new byte[iPicLen];
			Temp.get(pic);
			iPic = new String(pic);
			pic = null;
		}
		iTime = Temp.getInt();
		short textlen = Temp.getShort();
		if(textlen > 0) {
			byte[] text = new byte[textlen];
			Temp.get(text);
			iText = new String(text);
			text = null;
		}
		shLength = Temp.position();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspLeaveMsg [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iLeaveMsgID=" + iLeaveMsgID + ", iUID=" + iUID
				+ ", iNick=" + iNick + ", iPic=" + iPic + ", iTime=" + iTime
				+ ", iText=" + iText + "]";
	}

}
