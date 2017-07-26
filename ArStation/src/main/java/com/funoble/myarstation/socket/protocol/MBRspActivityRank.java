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
import java.util.Vector;

//MSG_REQ_ACTIVITY_DETAIL 1089 //活动详情应答
public class MBRspActivityRank implements MsgPara {
	public short	nResult;				//结果
	public String   iMsg;					//消息
	
	public int		iUserID;				//玩家的UID
	public int		iActivityID;			//活动id
	public int		iActivityPeopleCount;	//参加活动的人数
	public int		iRanking;				//名次
	public int		iCount;					//获奖人数
	public Vector<Integer>	iRankUserID;	//排行榜用户的UID
	public Vector<String>	iRankNick;		//排行榜用户的昵称
	public Vector<String>	iRankValue;		//场数
	
	/**
	 * construct
	 */
	public MBRspActivityRank() {
		nResult = -1;
		iMsg = "";
		iUserID = 0;
		iActivityID = 0;
		iActivityPeopleCount = 0;
		iRanking = 0;
		iCount = 0;
		iRankUserID = new Vector<Integer>();
		iRankNick = new Vector<String>();
		iRankValue = new Vector<String>();
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
		Temp.putInt(iUserID);
		Temp.putInt(iActivityID);
		Temp.putInt(iActivityPeopleCount);
		Temp.putInt(iRanking);
		Temp.putInt(iCount);
		for(int i = 0; i < iCount; i++) {
			Temp.putInt(iRankUserID.get(i));
			Temp.putShort((short) iRankNick.get(i).getBytes().length);
			if(iRankNick.get(i).length() > 0) {
				Temp.put(iRankNick.get(i).getBytes());
			}
			Temp.putShort((short)iRankValue.get(i).getBytes().length);
			if(iRankValue.get(i).length() > 0) {
				Temp.put(iRankValue.get(i).getBytes());
			}
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
		iUserID = Temp.getInt();
		iActivityID = Temp.getInt();
		iActivityPeopleCount = Temp.getInt();
		iRanking = Temp.getInt();
		iCount = Temp.getInt();
		for(int i = 0; i < iCount; i++) {
			iRankUserID.add(Temp.getInt());
			int ranklen = Temp.getShort();
			if(ranklen > 0) {
				byte[] rank = new byte[ranklen];
				Temp.get(rank);
				iRankNick.add(new String(rank));
				rank = null;
			}
			int rewardlen = Temp.getShort();
			if(rewardlen > 0) {
				byte[] reward = new byte[rewardlen];
				Temp.get(reward);
				iRankValue.add(new String(reward));
				reward = null;
			}
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspActivityRank [nResult=" + nResult + ", iMsg=" + iMsg
				+ ", iUserID=" + iUserID + ", iActivityID=" + iActivityID
				+ ", iActivityPeopleCount=" + iActivityPeopleCount
				+ ", iRanking=" + iRanking + ", iCount=" + iCount
				+ ", iRankUserID=" + iRankUserID + ", iRankNick=" + iRankNick
				+ ", iRankValue=" + iRankValue + "]";
	}

}
