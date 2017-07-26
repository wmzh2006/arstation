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


public class MBRspRank implements MsgPara {
	public short		nResult; //结果
	public String 		iMsg; //消息

	public int 			iUserID; //玩家的UID
	public String		iNick; //玩家的昵称
	public int 			iWealths; //财富榜排名
	public int 			iLevel; //级别榜排名
	public int 			iVictory; // 场数排名
	public int 			iRankType; //排行榜类型，0 --- 财富    1 --- 级别     2 --- 胜利场数

	public String 		iValue; //根据排行榜的类型来变， 排行榜是财富 --- 金币   排行榜是级别 --- 级别   排行榜是胜利场数 --- 胜利场数
	public int    		iRankTotalCount; //排行榜的总条数
	public int     		iStartIndex; //下一次开始的索引号
	public int 			iCount; //这个包的条数

	public Vector<Integer>  iRankUserID; //排行榜用户的UID
	public Vector<String> 	iRankNick;//排行榜用户的昵称
	public Vector<String>   iRankPicName; //排行榜用户的图片
	public Vector<String> 	iRankValue; //根据排行榜的类型来变， 排行榜是财富 --- 金币   排行榜是级别 --- 级别   排行榜是胜利场数 --- 胜利场数
	public Vector<Short>	iOnLine; //是否在线
	public Vector<Short>  iFriend; //是否为好友

	public MBRspRank() {
		this.nResult = -1;
		this.iMsg = "";
		this.iUserID = 0;
		this.iNick = "";
		this.iWealths = 0;
		this.iLevel = 0;
		this.iVictory = 0;
		this.iRankType = 0;
		this.iValue = "";
		this.iRankTotalCount = 0;
		this.iStartIndex = 0;
		this.iCount = 0;
		this.iRankUserID = new Vector<Integer>();
		this.iRankNick = new Vector<String>();
		this.iRankPicName = new Vector<String>();
		this.iRankValue = new Vector<String>();
		this.iOnLine = new Vector<Short>();
		this.iFriend = new Vector<Short>();
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
		Temp.putInt(iUserID);
		Temp.putShort((short) iNick.getBytes().length);
		if(iNick.length() > 0) {
			Temp.put(iNick.getBytes());
		}
		Temp.putInt(iWealths);
		Temp.putInt(iLevel);
		Temp.putInt(iVictory);
		Temp.putInt(iRankType);
		Temp.putShort((short) iValue.getBytes().length);
		if(iValue.length() > 0) {
			Temp.put(iValue.getBytes());
		}
		Temp.putInt(iRankTotalCount);
		Temp.putInt(iStartIndex);
		Temp.putInt(iCount);
		for(int i = 0; i < iCount; i++) {
			Temp.putInt(iRankUserID.get(i));
			Temp.putShort((short) iRankNick.get(i).getBytes().length);
			if(iRankNick.get(i).length() > 0) {
				Temp.put(iRankNick.get(i).getBytes());
			}
			Temp.putShort((short) iRankPicName.get(i).getBytes().length);
			if(iRankPicName.get(i).length() > 0) {
				Temp.put(iRankPicName.get(i).getBytes());
			}
			Temp.putShort((short) iRankValue.get(i).getBytes().length);
			if(iRankValue.get(i).length() > 0) {
				Temp.put(iRankValue.get(i).getBytes());
			}
			Temp.putShort(iOnLine.get(i));
			Temp.putShort(iFriend.get(i));
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
		int nicklen = Temp.getShort();
		if(nicklen > 0) {
			byte[] nick = new byte[nicklen];
			Temp.get(nick);
			iNick = new String(nick);
			nick = null;
		}
		iWealths = Temp.getInt();
		iLevel = Temp.getInt();
		iVictory = Temp.getInt();
		iRankType = Temp.getInt();
		int valuelen = Temp.getShort();
		if(valuelen > 0) {
			byte[] value = new byte[valuelen];
			Temp.get(value);
			iValue = new String(value);
			value = null;
		}
		iRankTotalCount = Temp.getInt();
		iStartIndex = Temp.getInt();
		iCount = Temp.getInt();
		for(int i = 0; i < iCount; i++) {
			iRankUserID.add(Temp.getInt());
			int ranknicklen = Temp.getShort();
			if(ranknicklen > 0) {
				byte[] ranknick = new byte[ranknicklen];
				Temp.get(ranknick);
				iRankNick.add(new String(ranknick));
				ranknick = null;
			}
			int rankpicnamelen = Temp.getShort();
			if(rankpicnamelen > 0) {
				byte[] rankpicname= new byte[rankpicnamelen];
				Temp.get(rankpicname);
				iRankPicName.add(new String(rankpicname));
				rankpicname = null;
			}
			int rankvaluelen = Temp.getShort();
			if(rankvaluelen > 0) {
				byte[] rankvalue= new byte[rankvaluelen];
				Temp.get(rankvalue);
				iRankValue.add(new String(rankvalue));
				rankvalue = null;
			}
			iOnLine.add(Temp.getShort());
			iFriend.add(Temp.getShort());
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspRank [nResult=" + nResult + ", iMsg=" + iMsg
				+ ", iUserID=" + iUserID + ", iNick=" + iNick + ", iWealths="
				+ iWealths + ", iLevel=" + iLevel + ", iVictory=" + iVictory
				+ ", iRankType=" + iRankType + ", iValue=" + iValue
				+ ", iRankTotalCount=" + iRankTotalCount + ", iStartIndex="
				+ iStartIndex + ", iCount=" + iCount + ", iRankUserID="
				+ iRankUserID + ", iRankNick=" + iRankNick + ", iRankPicName="
				+ iRankPicName + ", iRankValue=" + iRankValue + ", iOnLine="
				+ iOnLine + ", iFriend=" + iFriend + "]";
	}
}
