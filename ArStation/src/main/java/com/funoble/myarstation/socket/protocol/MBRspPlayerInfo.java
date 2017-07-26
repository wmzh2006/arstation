package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBRspPlayerInfo implements MsgPara {
	public short		iResult;
	public String		iMsg;						//提示信息
	public int			iSeatID;					//座位ID
	public int			iUserId;					//用户uid
	public String		iNick;						//用户昵称
	public int			iVipLevel;					//Vip级别	
	public int			iGameLevel;					//级别
	public int			iGameExp;					//当前级别的经验
	public int			iCurrentDrunk;				//当前醉酒度
	public int			iMaxDrunk;					//最大醉酒度
	public int			iKillsCount;				//胜
	public int			iBeKillsCount;				//负
	public int			iModelID;					//角色模型ID（为-1时，表示没选择人物）
	public int			iSex;						//性别（为-1时，表示没选择人物）
	public int     		iWinning;					//胜率
	public String		iTitle;						//人物称号
	public int			iGb;						//游戏币
	public int			iReward;					//奖


	
	public MBRspPlayerInfo() {
		this.iResult = -1;
		this.iMsg = "";
		this.iSeatID = -1;
		this.iUserId = 0;
		this.iNick = "";
		this.iVipLevel = 0;
		this.iGameLevel = 0;
		this.iGameExp = 0;
		this.iCurrentDrunk = 0;
		this.iMaxDrunk = 0;
		this.iKillsCount = 0;
		this.iBeKillsCount = 0;
		this.iModelID = -1;
		this.iSex = -1;
		this.iWinning = 0;
		this.iTitle = "";
		this.iGb = 0;
		this.iReward = 0;
	}


	@Override
	public int Encode(byte[] aOutBuffer, short aOutLength) {
		if(aOutBuffer == null) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.wrap(aOutBuffer);
		short sTempLength = 2;
		sTempLength += aOutLength;
		Temp.position(aOutLength);
		Temp.putShort(iResult);
		Temp.putShort((short) iMsg.length());
		Temp.put(iMsg.getBytes());
		Temp.putInt(iUserId);
		Temp.putShort((short) iNick.length());
		Temp.put(iNick.getBytes());
		Temp.putInt(iVipLevel);
		Temp.putInt(iGameLevel);
		Temp.putInt(iGameExp);
		Temp.putInt(iCurrentDrunk);
		Temp.putInt(iMaxDrunk);
		Temp.putInt(iKillsCount);
		Temp.putInt(iBeKillsCount);
		Temp.putInt(iModelID);
		Temp.putInt(iSex);
		Temp.putInt(iWinning);
		Temp.putShort((short) iTitle.getBytes().length);
		if(iTitle.length() > 0) {
			Temp.put(iTitle.getBytes());
		}
		sTempLength = (short) Temp.position();
		if(sTempLength > aOutLength) {
			return -1;
		}
		Temp.putInt(iGb);
		Temp.putInt(iReward);
		sTempLength -= aOutLength;
		Temp.putShort(aOutLength, sTempLength);
		return Temp.position();
	}

	@Override
	public int Decode(byte[] aInBuffer, short aInLenght) {
		if(aInBuffer == null || aInLenght < 0 ) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.allocate(aInBuffer.length + 2) ;
		Temp.put(aInBuffer);
		Temp.position(aInLenght);
		short shLeftLength = (short) aInBuffer.length;
		short sTempLength = 0;

		sTempLength = Temp.getShort();
		if( sTempLength > shLeftLength) {
			return -1;
		}
		iResult = Temp.getShort();
		int iMsgLen = Temp.getShort();
		if(iMsgLen > 0) {
			byte[] msg = new byte[iMsgLen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iSeatID = Temp.getInt();
		iUserId = Temp.getInt();
		int nickLen = Temp.getShort();
		if(nickLen > 0) {
			byte[] nick = new byte[nickLen];
			Temp.get(nick);
			iNick = new String(nick);
			nick = null;
		}
		iVipLevel 		= Temp.getInt();
		iGameLevel 		= Temp.getInt();
		iGameExp 		= Temp.getInt();
		iCurrentDrunk 		= Temp.getInt();
		iMaxDrunk 		= Temp.getInt();
		iKillsCount 	= Temp.getInt();
		iBeKillsCount 	= Temp.getInt();
		iModelID 		= Temp.getInt();
		iSex 			= Temp.getInt();
		iWinning 		= Temp.getInt();
		int titlelen	= Temp.getShort();
		if(titlelen > 0) {
			byte[] title = new byte[titlelen];
			Temp.get(title);
			iTitle = new String(title);
			title = null;
		}
		iGb = Temp.getInt();
		iReward = Temp.getInt();
		return Temp.position();
	}


	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspPlayerInfo [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iSeatID=" + iSeatID + ", iUserId=" + iUserId + ", iNick="
				+ iNick + ", iVipLevel=" + iVipLevel + ", iGameLevel="
				+ iGameLevel + ", iGameExp=" + iGameExp + ", iMaxDrunk="
				+ iMaxDrunk + ", iKillsCount=" + iKillsCount
				+ ", iBeKillsCount=" + iBeKillsCount + ", iModelID=" + iModelID
				+ ", iSex=" + iSex + ", iWinning=" + iWinning + ", iTitle="
				+ iTitle + ", iGb=" + iGb + ", iReward=" + iReward + "]";
	}

}
