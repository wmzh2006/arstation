/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspLoginRoom.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 下午04:05:47
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Vector;

import com.funoble.myarstation.utils.TPoint;

import android.provider.UserDictionary.Words;


public class MBRspLoginRoom implements MsgPara {
	public short			iResult;		//登录房间是否成功
	public String  			iMsg;			//消息
	public String 			iWineName; 		//酒的名称
	public int				iPrice;			//单价
	public int				iWineCount;		//酒的数量
	public int				iRoomID;		//房间ID
	public int				iTableID;		//桌子ID
	public int				iSelfSeatID;	//自己的座位ID
	public int				iPlayerCount;	//玩家个数
	public Vector<Integer>	iSeatIDs;		//各个玩家的座位id
	public Vector<String>	iUserNicks;		//各个玩家的名字
	public Vector<Integer>  iModelIDs;  	//各个玩家的模型id
	public Vector<Integer>	iUserStates;	//各个玩家的状态					
	public Vector<Integer>  iCurrentDrunks;	//各个玩家的醉酒度
	public Vector<Integer>  iMaxDrunks;		//各个玩家的最大醉酒度（酒量）
	public Vector<Integer>	iUserIDs;		//各个玩家的UserID
	public Vector<String>	iLittlePic;		//头像图片名称
	public Vector<Integer>	iDicePakID;		//玩家骰子的PakID
	public int				iLogicType;		//游戏逻辑类型
	public int				iRewardGB;		//灌倒人的金币
	public String			iRewardName;	//奖励的名字
	public String			iRoomName;	  //房间名字

	/**
	 * construct
	 */
	public MBRspLoginRoom() {
		iResult = -1;
		iMsg = "";
		iWineName = "";
		iPrice = 0;
		iWineCount = 0;
		iRoomID = -1;
		iTableID = -1;
		iSelfSeatID = -1;
		iPlayerCount = 0;
		iSeatIDs = new Vector<Integer>();
		iUserNicks = new Vector<String>();
		iModelIDs = new Vector<Integer>();
		iUserStates = new Vector<Integer>();
		iCurrentDrunks = new Vector<Integer>();
		iMaxDrunks = new Vector<Integer>();
		iUserIDs = new Vector<Integer>();
		iLittlePic = new Vector<String>();
		iLogicType = -1;
		iRewardGB = 0;
		iRewardName = "";
		iRoomName = "";
		iDicePakID = new Vector<Integer>();
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
		int shLength = 2;
		shLength += aOutLength;
		Temp.position(shLength);
		Temp.putShort(iResult);
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putShort((short) iWineName.getBytes().length);
		if(iWineName.length() > 0) {
			Temp.put(iWineName.getBytes());
		}
		Temp.putInt(iPrice);
		Temp.putInt(iWineCount);
		Temp.putInt(iRoomID);
		Temp.putInt(iTableID);
		Temp.putInt(iSelfSeatID);
		Temp.putInt(iPlayerCount);
		if(iPlayerCount > 0) {
			Iterator<Integer> iter = iSeatIDs.iterator();
			while(iter.hasNext()) {
				Temp.putInt(iter.next());
			}
			Iterator<String> striter = iUserNicks.iterator();
			while(striter.hasNext()) {
				String strTemp = striter.next();
				short len = (short) strTemp.getBytes().length;
				Temp.putShort(len);
				if(len > 0) {
					Temp.put(strTemp.getBytes());
				}
			}
			
			iter = iModelIDs.iterator();
			while(iter.hasNext()) {
				Temp.putInt(iter.next());
			}
			
			iter = iUserStates.iterator();
			while(iter.hasNext()) {
				Temp.putInt(iter.next());
			}
			
			iter = iCurrentDrunks.iterator();
			while(iter.hasNext()) {
				Temp.putInt(iter.next());
			}
			
			iter = iMaxDrunks.iterator();
			while(iter.hasNext()) {
				Temp.putInt(iter.next());
			}
			iter = iUserIDs.iterator();
			while(iter.hasNext()) {
				Temp.putInt(iter.next());
			}
			striter = iLittlePic.iterator();
			while(striter.hasNext()) {
				String strTemp = striter.next();
				short len = (short) strTemp.getBytes().length;
				Temp.putShort(len);
				if(len > 0) {
					Temp.put(strTemp.getBytes());
				}
			}
			iter = iDicePakID.iterator();
			while(iter.hasNext()) {
				Temp.putInt(iter.next());
			}
		}
		Temp.putInt(iLogicType);
		Temp.putInt(iRewardGB);
		Temp.putShort((short) iRewardName.getBytes().length);
		if(iRewardName.length() > 0) {
			Temp.put(iRewardName.getBytes());
		}
		Temp.putShort((short) iRoomName.getBytes().length);
		if(iRoomName.length() > 0) {
			Temp.put(iRoomName.getBytes());
		}
		shLength = Temp.position();
		shLength -= aOutLength;
		Temp.putShort((short) shLength);
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
		short shLength = 0;
		shLength = Temp.getShort();
		if(shLength > aInBuffer.length || shLength <= 0) {
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
		int winenamelen = Temp.getShort();
		if(winenamelen > 0) {
			byte[] winename = new byte[winenamelen];
			Temp.get(winename);
			iWineName = new String(winename);
			winename = null;
		}
		iPrice = Temp.getInt();
		iWineCount = Temp.getInt();
		iRoomID = Temp.getInt();
		iTableID = Temp.getInt();
		iSelfSeatID = Temp.getInt();
		iPlayerCount = Temp.getInt();
		for(int i = 0; i < iPlayerCount; i++) {
			int seatID = Temp.getInt();
			iSeatIDs.add(seatID);
			
			short userNicklen = Temp.getShort();
			if(userNicklen > 0) {
				byte[] userNick = new byte[userNicklen];
				Temp.get(userNick);
				iUserNicks.add(new String(userNick));
				userNick = null;
			}
			int modelID = Temp.getInt();
			iModelIDs.add(modelID);
			
			int userState = Temp.getInt();
			iUserStates.add(userState);
			
			int currentDrunk = Temp.getInt();
			iCurrentDrunks.add(currentDrunk);
			
			int MaxDrunk = Temp.getInt();
			iMaxDrunks.add(MaxDrunk);
			
			int UserIDs = Temp.getInt();
			iUserIDs.add(UserIDs);
			
			short litlepiclen = Temp.getShort();
			if(litlepiclen > 0) {
				byte[] pic = new byte[litlepiclen];
				Temp.get(pic);
				iLittlePic.add(new String(pic));
				pic = null;
			}
			int DicePakIDs = Temp.getInt();
			iDicePakID.add(DicePakIDs);
		}
		iLogicType = Temp.getInt();
		iRewardGB = Temp.getInt();
		int RewardNameLen = Temp.getShort();
		if(RewardNameLen > 0) {
			byte[] RewardName = new byte[RewardNameLen];
			Temp.get(RewardName);
			iRewardName = new String(RewardName);
			RewardName = null;
		}
		int RoomNameLen = Temp.getShort();
		if(RoomNameLen > 0) {
			byte[] RoomName = new byte[RoomNameLen];
			Temp.get(RoomName);
			iRoomName = new String(RoomName);
			RoomName = null;
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspLoginRoom [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iWineName=" + iWineName + ", iPrice=" + iPrice
				+ ", iWineCount=" + iWineCount + ", iRoomID=" + iRoomID
				+ ", iTableID=" + iTableID + ", iSelfSeatID=" + iSelfSeatID
				+ ", iPlayerCount=" + iPlayerCount + ", iSeatIDs=" + iSeatIDs
				+ ", iUserNicks=" + iUserNicks + ", iModelIDs=" + iModelIDs
				+ ", iUserStates=" + iUserStates + ", iCurrentDrunks="
				+ iCurrentDrunks + ", iMaxDrunks=" + iMaxDrunks + ", iUserIDs="
				+ iUserIDs + ", iLittlePic=" + iLittlePic + ", iDicePakID="
				+ iDicePakID + ", iLogicType=" + iLogicType + ", iRewardGB="
				+ iRewardGB + ", iRewardName=" + iRewardName + ", iRoomName="
				+ iRoomName + "]";
	}
}
