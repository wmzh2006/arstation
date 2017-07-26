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
public class MBRspActivityDetail implements MsgPara {
	public short	nResult;				//结果
	public String   iMsg;					//消息
	
	public int		iActivityID;			//活动id
	public int		iAction;				//请求动作
	public String	iContent;				//活动说明
	public int		iCount;					//获奖人数
	public Vector<String>	iActivityRank;	//活动名次
	public Vector<String>	iActivityReward;//活动相应名次奖品
	
	/**
	 * construct
	 */
	public MBRspActivityDetail() {
		nResult = -1;
		iMsg = "";
		iContent = "";
		iCount = 0;
		iActivityRank = new Vector<String>();
		iActivityReward = new Vector<String>();
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
		Temp.putInt(iActivityID);
		Temp.putInt(iAction);
		Temp.putShort((short) iContent.getBytes().length);
		if(iContent.length() > 0) {
			Temp.put(iContent.getBytes());
		}
		Temp.putInt(iCount);
		for(int i = 0; i < iCount; i++) {
			Temp.putShort((short) iActivityRank.get(i).getBytes().length);
			if(iActivityRank.get(i).length() > 0) {
				Temp.put(iActivityRank.get(i).getBytes());
			}
			Temp.putShort((short)iActivityReward.get(i).getBytes().length);
			if(iActivityReward.get(i).length() > 0) {
				Temp.put(iActivityReward.get(i).getBytes());
			}
		}
		Temp.putInt(iCount);
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
		iActivityID = Temp.getInt();
		iAction = Temp.getInt();
		int contentlen = Temp.getShort();
		if(contentlen > 0) {
			byte[] content = new byte[contentlen];
			Temp.get(content);
			iContent = new String(content);
			content = null;
		}
		iCount = Temp.getInt();
		for(int i = 0; i < iCount; i++) {
			int ranklen = Temp.getShort();
			if(ranklen > 0) {
				byte[] rank = new byte[ranklen];
				Temp.get(rank);
				iActivityRank.add(new String(rank));
				rank = null;
			}
			int rewardlen = Temp.getShort();
			if(rewardlen > 0) {
				byte[] reward = new byte[rewardlen];
				Temp.get(reward);
				iActivityReward.add(new String(reward));
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
		return "MBRspActivityDetail [nResult=" + nResult + ", iMsg=" + iMsg
				+ ", iActivityID=" + iActivityID + ", iAction=" + iAction
				+ ", iContent=" + iContent + ", iCount=" + iCount
				+ ", iActivityRank=" + iActivityRank + ", iActivityReward="
				+ iActivityReward + "]";
	}
}
