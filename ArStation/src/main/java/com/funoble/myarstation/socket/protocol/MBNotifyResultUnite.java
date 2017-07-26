/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBNotifyResultUnite.java 
 * Description://结果消息UNITE
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-23 下午05:16:28
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.Arrays;


public class MBNotifyResultUnite implements MsgPara {
	public short			nResult;					//结果
	public String			iMsg;						//消息
	public int				iPlayerCount;				//此桌子玩家的个数
	public int[]			iSeatIDs;					//这几个玩家分别的座位号
	public int[][]			iDices;						//各个玩家投资的点数
	public int[]			iScores;					//获得的积分
	public int[]			iGBs;						//获得的GB
	public int[]			iVIPScores;					//VIP额外获得的积分
	public int[]			iFriendScores;				//亲密度额外获得的积分

	public int				iChopDice;					//被劈的骰子
	public int				iDiceCount;					//被劈的骰子总共有几个
	public int				iWinnerSeatID;				//者座位ID
	public int				iWinnderGetGB;				//获得的游戏币
	public int				iWinnerKillsCount;			//灌倒的人数
	public int				iFailSeatID;				//失败者座位ID
	public int				iFailCurrentDrunk;			//失败者喝酒后的醉酒度
	public String			iDrunkMsg;					//灌倒人后的信息
	public int				iWinnerUniteSeatID;			//者盟友座位ID
	public int				iFailUniteSeatID;			//失败者盟友座位ID
	public int				iFailUniteCurrentDrunk;		//失败者喝酒后的醉酒度
	public int				iFailUniteCleanDrunkPrice;	//失败者盟友醒酒需要的金币


	/**
	 * construct
	 */
	public MBNotifyResultUnite() {
		iPlayerCount = 0;
		iMsg = "";
		iSeatIDs = new int[BufferSize.MAX_TABLE_PLAYER_COUNT];
		iDices = new int[BufferSize.MAX_TABLE_PLAYER_COUNT][BufferSize.MAX_DICE_COUNT];
		iScores = new int[BufferSize.MAX_TABLE_PLAYER_COUNT];
		iGBs = new int[BufferSize.MAX_TABLE_PLAYER_COUNT];
		iVIPScores = new int[BufferSize.MAX_TABLE_PLAYER_COUNT];
		iFriendScores = new int[BufferSize.MAX_TABLE_PLAYER_COUNT];
		iChopDice = 0;
		iDiceCount = 0;
		iWinnerSeatID = 0;
		iWinnderGetGB = 0;
		iWinnerKillsCount = 0;
		iFailSeatID = 0;
		iFailCurrentDrunk = 0;
		iDrunkMsg = "";
		iWinnerUniteSeatID = 0;
		iFailUniteSeatID = 0;
		iFailUniteCurrentDrunk = 0;
		iFailUniteCleanDrunkPrice = 0;
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
		Temp.putInt(iPlayerCount);
		for(int i = 0; i < iPlayerCount; i++) {
			Temp.putInt(iSeatIDs[i]);
			for(int j = 0; j < BufferSize.MAX_DICE_COUNT; j++) {
				Temp.putInt(iDices[i][j]);
			}
			Temp.putInt(iScores[i]);
			Temp.putInt(iGBs[i]);
			Temp.putInt(iVIPScores[i]);
			Temp.putInt(iFriendScores[i]);
		}
		Temp.putInt(iChopDice);
		Temp.putInt(iDiceCount);
		Temp.putInt(iWinnerSeatID);
		Temp.putInt(iWinnderGetGB);
		Temp.putInt(iWinnerKillsCount);
		Temp.putInt(iFailSeatID);
		Temp.putInt(iFailCurrentDrunk);
		Temp.putShort((short) iDrunkMsg.getBytes().length);
		if(iDrunkMsg.length() > 0) {
			Temp.put(iDrunkMsg.getBytes());
		}
		Temp.putInt(iWinnerUniteSeatID);
		Temp.putInt(iFailUniteSeatID);
		Temp.putInt(iFailUniteCurrentDrunk);
		Temp.putInt(iFailUniteCleanDrunkPrice);
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
		int msgLen = Temp.getShort();
		if(msgLen > 0) {
			byte[] msg = new byte[msgLen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iPlayerCount = Temp.getInt();
		if(iPlayerCount > BufferSize.MAX_TABLE_PLAYER_COUNT) {
			return -1;
		}
		for(int i = 0; i < iPlayerCount; i++) {
			iSeatIDs[i] = Temp.getInt();
			for(int j = 0; j < BufferSize.MAX_DICE_COUNT; j++) {
				iDices[i][j] = Temp.getInt();
			}
			iScores[i] = Temp.getInt();
			iGBs[i] = Temp.getInt();
			iVIPScores[i] = Temp.getInt();
			iFriendScores[i] = Temp.getInt();
		}
		iChopDice = Temp.getInt();
		iDiceCount = Temp.getInt();
		iWinnerSeatID = Temp.getInt();
		iWinnderGetGB = Temp.getInt();
		iWinnerKillsCount = Temp.getInt();
		iFailSeatID = Temp.getInt();
		iFailCurrentDrunk = Temp.getInt();
		int drunkLen = Temp.getShort();
		if(drunkLen > 0) {
			byte[] drunk = new byte[drunkLen];
			Temp.get(drunk);
			iDrunkMsg = new String(drunk);
			drunk = null;
		}
		iWinnerUniteSeatID = Temp.getInt();
		iFailUniteSeatID = Temp.getInt();
		iFailUniteCurrentDrunk = Temp.getInt();
		iFailUniteCleanDrunkPrice = Temp.getInt();
		shLength = Temp.position();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBNotifyResultUnite [nResult=" + nResult + ", iMsg=" + iMsg
				+ ", iPlayerCount=" + iPlayerCount + ", iSeatIDs="
				+ Arrays.toString(iSeatIDs) + ", iDices="
				+ Arrays.toString(iDices) + ", iScores="
				+ Arrays.toString(iScores) + ", iGBs=" + Arrays.toString(iGBs)
				+ ", iVIPScores=" + Arrays.toString(iVIPScores)
				+ ", iFriendScores=" + Arrays.toString(iFriendScores)
				+ ", iChopDice=" + iChopDice + ", iDiceCount=" + iDiceCount
				+ ", iWinnerSeatID=" + iWinnerSeatID + ", iWinnderGetGB="
				+ iWinnderGetGB + ", iWinnerKillsCount=" + iWinnerKillsCount
				+ ", iFailSeatID=" + iFailSeatID + ", iFailCurrentDrunk="
				+ iFailCurrentDrunk + ", iDrunkMsg=" + iDrunkMsg
				+ ", iWinnerUniteSeatID=" + iWinnerUniteSeatID
				+ ", iFailUniteSeatID=" + iFailUniteSeatID
				+ ", iFailUniteCurrentDrunk=" + iFailUniteCurrentDrunk
				+ ", iFailUniteCleanDrunkPrice=" + iFailUniteCleanDrunkPrice
				+ "]";
	}

}
