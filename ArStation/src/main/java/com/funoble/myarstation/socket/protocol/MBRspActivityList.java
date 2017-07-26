/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspRank.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-7-7 下午12:38:51
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.Vector;


public class MBRspActivityList implements MsgPara {
	public short			nResult; //结果
	public String 			iMsg; //消息

	public int 				iActivityType; //活动类型  0 --- 日      1 --- 周      2 --- 月     3 --- 争霸赛
	public int    			iTotalCount; //活动的总个数
	public int     			iStartIndex; //下一次开始的索引号
	public int 				iCount; //这个包的条数
	public Vector<Integer>  iID; //活动ID
	public Vector<String> 	iName;//活动的名称
	public Vector<String>   iStartTime; //活动的开始时间
	public Vector<String> 	iEndTime; //活动的结束时间
	public Vector<Integer>	iPeopleCount; //报名人数
	public Vector<String> 	iStateText;//活动的状态描述
	public Vector<Integer>  iState;//活动状态  0 --- 显示报名中    1 --- 显示即将开始    2 --- 显示正在进行   3 --- 显示已结束
	public Vector<Integer> 	iFB; //报名需要的凤智币
	public Vector<Integer>	iGB; //报名需要的金币
	public Vector<Integer> 	iLevel; //报名需要的级别

	public MBRspActivityList() {
		this.nResult = -1;
		this.iMsg = "";
		this.iActivityType = 0;
		this.iTotalCount = 0;
		this.iStartIndex = 0;
		this.iCount = 0;
		this.iID = new Vector<Integer>();
		this.iName = new Vector<String>();
		this.iStartTime = new Vector<String>();
		this.iEndTime = new Vector<String>();
		this.iPeopleCount = new Vector<Integer>();
		this.iStateText = new Vector<String>();
		this.iState = new Vector<Integer>();
		this.iFB = new Vector<Integer>();
		this.iGB = new Vector<Integer>();
		this.iLevel = new Vector<Integer>();
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
		Temp.putShort(nResult);
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iActivityType);
		Temp.putInt(iTotalCount);
		Temp.putInt(iStartIndex);
		Temp.putInt(iCount);
		for(int i = 0; i < iCount; i++) {
			Temp.putInt(iID.get(i));
			Temp.putShort((short) iName.get(i).getBytes().length);
			if(iName.get(i).length() > 0) {
				Temp.put(iName.get(i).getBytes());
			}
			Temp.putShort((short) iStartTime.get(i).getBytes().length);
			if(iStartTime.get(i).length() > 0) {
				Temp.put(iStartTime.get(i).getBytes());
			}
			Temp.putShort((short) iEndTime.get(i).getBytes().length);
			if(iEndTime.get(i).length() > 0) {
				Temp.put(iEndTime.get(i).getBytes());
			}
			Temp.putInt(iPeopleCount.get(i));
			Temp.putShort((short) iStateText.get(i).getBytes().length);
			if(iStateText.get(i).length() > 0) {
				Temp.put(iStateText.get(i).getBytes());
			}
			Temp.putInt(iState.get(i));
			Temp.putInt(iFB.get(i));
			Temp.putInt(iGB.get(i));
			Temp.putInt(iLevel.get(i));
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
		iActivityType = Temp.getInt();
		iTotalCount = Temp.getInt();
		iStartIndex = Temp.getInt();
		iCount = Temp.getInt();
		for(int i = 0; i < iCount; i++) {
			iID.add(Temp.getInt());
			int ranknicklen = Temp.getShort();
			if(ranknicklen > 0) {
				byte[] ranknick = new byte[ranknicklen];
				Temp.get(ranknick);
				iName.add(new String(ranknick));
				ranknick = null;
			}
			int rankpicnamelen = Temp.getShort();
			if(rankpicnamelen > 0) {
				byte[] rankpicname= new byte[rankpicnamelen];
				Temp.get(rankpicname);
				iStartTime.add(new String(rankpicname));
				rankpicname = null;
			}
			int rankvaluelen = Temp.getShort();
			if(rankvaluelen > 0) {
				byte[] rankvalue= new byte[rankvaluelen];
				Temp.get(rankvalue);
				iEndTime.add(new String(rankvalue));
				rankvalue = null;
			}
			iPeopleCount.add(Temp.getInt());
			int statusTextlen = Temp.getShort();
			if(statusTextlen > 0) {
				byte[] statusText = new byte[statusTextlen];
				Temp.get(statusText);
				iStateText.add(new String(statusText));
				statusText = null;
			}
			iState.add(Temp.getInt());
			iFB.add(Temp.getInt());
			iGB.add(Temp.getInt());
			iLevel.add(Temp.getInt());
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspActivityList [nResult=" + nResult + ", iMsg=" + iMsg
				+ ", iActivityType=" + iActivityType + ", iTotalCount="
				+ iTotalCount + ", iStartIndex=" + iStartIndex + ", iCount="
				+ iCount + ", iID=" + iID + ", iName=" + iName
				+ ", iStartTime=" + iStartTime + ", iEndTime=" + iEndTime
				+ ", iPeopleCount=" + iPeopleCount + ", iStateText="
				+ iStateText + ", iState=" + iState + ", iFB=" + iFB + ", iGB="
				+ iGB + ", iLevel=" + iLevel + "]";
	}

}
