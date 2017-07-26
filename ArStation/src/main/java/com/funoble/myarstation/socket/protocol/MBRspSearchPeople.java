/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspAddFriend.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-24 上午11:14:10
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.Vector;


public class MBRspSearchPeople implements MsgPara {
	public short    iResult;						//结果
	public String	iMsg;							//消息
	public int		iCount;							//这个包的个数
	public Vector<Integer>			iUserID;			//好友UID
	public Vector<String>			iNick;	//好友的昵称
	public Vector<String>			iPicName;	//好友图片
	
	/**
	 * construct
	 */
	public MBRspSearchPeople() {
		iResult = -1;
		iMsg = "";
		iCount = 0;
		iUserID = new Vector<Integer>();
		iNick = new Vector<String>();
		iPicName = new Vector<String>();
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
		for(int i = 0; i < iCount; i++) {
			Temp.putInt(iUserID.get(i));
			Temp.putShort((short) iNick.get(i).getBytes().length);
			Temp.put(iNick.get(i).getBytes());
			Temp.putShort((short) iPicName.get(i).length());
			Temp.put(iPicName.get(i).getBytes());
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
			iUserID.add(Temp.getInt());
			int namelen = Temp.getShort();
			if(namelen > 0) {
				byte[] name = new byte[namelen];
				Temp.get(name);
				iNick.add(new String(name));
				name = null;
			}
			int piclen = Temp.getShort();
			if(piclen > 0) {
				byte[] pic = new byte[piclen];
				Temp.get(pic);
				iPicName.add(new String(pic));
				pic = null;
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
		return "MBRspSearchPeople [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iCount=" + iCount + ", iUserID=" + iUserID + ", iNick="
				+ iNick + ", iPicName=" + iPicName + "]";
	}

}
