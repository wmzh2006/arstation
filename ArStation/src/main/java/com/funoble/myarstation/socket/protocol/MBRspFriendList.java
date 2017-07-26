/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspFriendList.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 上午10:32:37
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.Vector;


public class MBRspFriendList implements MsgPara {

	public short 			nResult; 				//是否成功, 0 --- 表示成功
	public String			iMsg; 					//信息
	public int				iFriendTotalCount;		//好友总数
	public int				iFriendOLCount;			//上线好友总数
	public int				iStartIndex;			//下一次开始的索引号
	public int				iCount;					//这个包的个数
	public Vector<Integer>	iFriendUserID;			//好友UID
	public Vector<String>	iFriendNick;			//好友的昵称
	public Vector<String>	iFriendPicName;			//好友图片
	public Vector<Short>	iOnLine;				//是否在线		0 --- 不在线  1 --- 在线在大厅  2 --- 游戏中
	public Vector<String> 	iTitle;					//人物称号
	public Vector<Short>	iUpdate; 				//是否有更新 0 --- 没有   1 --- 有

	/**
	 * construct
	 */
	public MBRspFriendList() {
		nResult = -1;
		iMsg = "";
		iFriendTotalCount = 0;
		iFriendOLCount = 0;
		iStartIndex = 0;
		iCount = 0;
		iFriendUserID = new Vector<Integer>();
		iFriendNick = new Vector<String>();
		iFriendPicName = new Vector<String>();
		iOnLine = new Vector<Short>();
		iTitle = new Vector<String>();
		iUpdate = new Vector<Short>();;
	}

	/* 
	 * @see com.funoble.lyingdice.socket.protocol.MsgPara#Encode(byte[], short)
	 */
	@Override
	public int Encode(byte[] aOutBuffer, short aOutLength) {
		if(aOutBuffer == null || aOutLength < 0) {
			return -1;
		}
		ByteBuffer Temp  = ByteBuffer.wrap(aOutBuffer);
		int shLenght = 2;
		shLenght += aOutLength;
		Temp.position(aOutLength);
		Temp.putShort(nResult);
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iFriendTotalCount);
		Temp.putInt(iFriendOLCount);
		Temp.putInt(iStartIndex);
		Temp.putInt(iCount);
		for(int i = 0; i < iCount; i++) {
			Temp.putInt(iFriendUserID.get(i));
			String friendNick = iFriendNick.get(i);
			Temp.putShort((short) friendNick.getBytes().length);
			if(friendNick.length() > 0) {
				Temp.put(friendNick.getBytes());
			}
			String FriendPicName = iFriendPicName.get(i);
			Temp.putShort((short) FriendPicName.getBytes().length);
			if(FriendPicName.length() > 0) {
				Temp.put(FriendPicName.getBytes());
			}
			Temp.putShort(iOnLine.get(i));
			String titlename = iTitle.get(i);
			Temp.putShort((short) titlename.getBytes().length);
			if(titlename.length() > 0) {
				Temp.put(titlename.getBytes());
			}
			Temp.putShort(iUpdate.get(i));
		}
		shLenght = Temp.position();
		shLenght -= shLenght;
		Temp.putShort(aOutLength, (short) shLenght);
		return Temp.position();
	}

	/* 
	 * @see com.funoble.lyingdice.socket.protocol.MsgPara#Decode(byte[], short)
	 */
	@Override
	public int Decode(byte[] aInBuffer, short aInLength) {
		if(aInBuffer == null) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.allocate(aInBuffer.length + 2);
		Temp.put(aInBuffer);
		Temp.position(aInLength);
		int shLenght = 0;
		shLenght = Temp.getShort();
		if(shLenght > aInBuffer.length) {
			return -2;
		}
		nResult = Temp.getShort();
		int msglen = Temp.getShort();
		if(msglen > 0) {
			byte[] msg = new byte[msglen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iFriendTotalCount = Temp.getInt();
		iFriendOLCount = Temp.getInt();
		iStartIndex = Temp.getInt();
		iCount = Temp.getInt();
		for(int i = 0; i < iCount; i++) {
			iFriendUserID.add(Temp.getInt());
			int friendnicklen = Temp.getShort();
			if(friendnicklen > 0) {
				byte[] friendnick = new byte[friendnicklen];
				Temp.get(friendnick);
				iFriendNick.add(new String(friendnick));
				friendnick = null;
			}
			int FriendPicNameLen = Temp.getShort();
			if(FriendPicNameLen > 0) {
				byte[] FriendPicName = new byte[FriendPicNameLen];
				Temp.get(FriendPicName);
				iFriendPicName.add(new String(FriendPicName));
				FriendPicName = null;
			}
			iOnLine.add(Temp.getShort());
			int titlelen = Temp.getShort();
			if(titlelen > 0) {
				byte[] title = new byte[titlelen];
				Temp.get(title);
				iTitle.add(new String(title));
				title = null;
			}
			iUpdate.add(Temp.getShort());
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspFriendList [nResult=" + nResult + ", iMsg=" + iMsg
				+ ", iFriendTotalCount=" + iFriendTotalCount
				+ ", iFriendOLCount=" + iFriendOLCount + ", iStartIndex="
				+ iStartIndex + ", iCount=" + iCount + ", iFriendUserID="
				+ iFriendUserID + ", iFriendNick=" + iFriendNick
				+ ", iFriendPicName=" + iFriendPicName + ", iOnLine=" + iOnLine
				+ ", iTitle=" + iTitle + ", iUpdate=" + iUpdate + "]";
	}
}
