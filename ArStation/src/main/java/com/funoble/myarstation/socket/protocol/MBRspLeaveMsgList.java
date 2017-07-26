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
import java.util.Vector;


public class MBRspLeaveMsgList implements MsgPara {
	public short			iResult; 	//用户ID
	public String   		iMsg; 		//消息
	public int      		iStartIndex;	 //开始的索引号
	public int	    		iAllMsgCount;	 //所有留言的个数
	public int	    		iMsgCount;	 //留言的个数
	public Vector<Integer>	iLeaveMsgID;	 //留言的ID									
	public Vector<Integer>	iUID;			 //留言者的UID
	public Vector<String>	iNick;
	public Vector<String>	iPic;
	public Vector<Integer>	iTime;			 //留言时间
	public Vector<String>   iText;			 //内容
	/**
	 * construct
	 */
	public MBRspLeaveMsgList() {
		iResult = -1;
		iMsg = "";
		iStartIndex = 0;
		iAllMsgCount = 0;
		iAllMsgCount = 0;
		iMsgCount = 0;
		iLeaveMsgID = new Vector<Integer>();
		iUID = new Vector<Integer>();
		iNick = new Vector<String>();
		iPic = new Vector<String>();
		iTime = new Vector<Integer>();
		iText = new Vector<String>();
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
		Temp.putInt(iStartIndex);
		Temp.putInt(iAllMsgCount);
		Temp.putInt(iMsgCount);
		for(int i = 0; i < iMsgCount; i++) {
			Temp.putInt(iLeaveMsgID.get(i));
			Temp.putInt(iUID.get(i));
			Temp.putShort((short) iNick.get(i).getBytes().length);
			if(iNick.get(i).length() > 0) {
				Temp.put(iNick.get(i).getBytes());
			}
			Temp.putShort((short) iPic.get(i).getBytes().length);
			if(iPic.get(i).length() > 0) {
				Temp.put(iPic.get(i).getBytes());
			}
			Temp.putInt(iTime.get(i));
			Temp.putShort((short) iText.get(i).getBytes().length);
			if(iText.get(i).length() > 0) {
				Temp.put(iText.get(i).getBytes());
			}
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
		iStartIndex = Temp.getInt();
		iAllMsgCount = Temp.getInt();
		iMsgCount = Temp.getInt();
		for(int i = 0; i < iMsgCount; i++) {
			iLeaveMsgID.add(Temp.getInt());
			iUID.add(Temp.getInt());
			short iNickLen = Temp.getShort();
			if(iNickLen > 0) {
				byte[] nick = new byte[iNickLen];
				Temp.get(nick);
				iNick.add(new String(nick));
				nick = null;
			}
			short iPicLen = Temp.getShort();
			if(iPicLen > 0) {
				byte[] pic = new byte[iPicLen];
				Temp.get(pic);
				iPic.add(new String(pic));
				pic = null;
			}
			iTime.add(Temp.getInt());
			short textlen = Temp.getShort();
			if(textlen > 0) {
				byte[] text = new byte[textlen];
				Temp.get(text);
				iText.add(new String(text));
				text = null;
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
		return "MBRspLeaveMsgList [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iStartIndex=" + iStartIndex + ", iAllMsgCount="
				+ iAllMsgCount + ", iMsgCount=" + iMsgCount + ", iLeaveMsgID="
				+ iLeaveMsgID + ", iUID=" + iUID + ", iNick=" + iNick
				+ ", iPic=" + iPic + ", iTime=" + iTime + ", iText=" + iText
				+ "]";
	}
}
