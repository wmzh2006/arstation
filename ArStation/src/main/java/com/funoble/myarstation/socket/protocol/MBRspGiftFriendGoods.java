/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspVIPinfo.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-2 下午12:07:31
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBRspGiftFriendGoods implements MsgPara {
	public short	nResult;				//结果
	public String   iMsg;					//消息
	public int		iMoney;	 //当前剩下的凤币
	public int	 	iAddFriendExp;	 //增加的友好度
	public int	 	iFriendExp;	 //当前级别的友好度
	public int	 	iNextFriendExp;	 //下一级别的友好度
	public String	iFriendTitle;	//友好度级别称号
	/**
	 * construct
	 */
	public MBRspGiftFriendGoods() {
		nResult = -1;
		iMsg = "";
		iMoney = 0;
		iAddFriendExp = 0;
		iFriendExp = 0;
		iNextFriendExp = 0;
		iFriendTitle = "";
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
		Temp.putShort(nResult);
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iMoney);
		Temp.putInt(iAddFriendExp);
		Temp.putInt(iFriendExp);
		Temp.putInt(iNextFriendExp);
		Temp.putShort((short) iFriendTitle.getBytes().length);
		if(iFriendTitle.length() > 0) {
			Temp.put(iFriendTitle.getBytes());
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
		nResult = Temp.getShort();
		int msglen = Temp.getShort();
		if(msglen > 0) {
			byte[] msg = new byte[msglen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iMoney = Temp.getInt();
		iAddFriendExp = Temp.getInt();
		iFriendExp = Temp.getInt();
		iNextFriendExp = Temp.getInt();
		int friendlen = Temp.getShort();
		if(friendlen > 0) {
			byte[] title = new byte[friendlen];
			Temp.get(title);
			iFriendTitle = new String(title);
			title = null;
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspGiftFriendGoods [nResult=" + nResult + ", iMsg=" + iMsg
				+ ", iMoney=" + iMoney + ", iAddFriendExp=" + iAddFriendExp
				+ ", iFriendExp=" + iFriendExp + ", iNextFriendExp="
				+ iNextFriendExp + ", iFriendTitle=" + iFriendTitle + "]";
	}
}
