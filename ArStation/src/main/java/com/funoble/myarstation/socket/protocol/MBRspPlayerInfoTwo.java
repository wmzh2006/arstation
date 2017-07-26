package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBRspPlayerInfoTwo implements MsgPara {
	public short		iResult;
	public String		iMsg;						//提示信息
	public int			iUserId;					//用户uid
	public int			iSeatID;					//座位ID
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
	public int			iMoney;						//凤智币
	public int			iWealthRank;				//财富排名
	public int			iLevelRank;					//等级排名
	public int			iVictoryRank;				//胜场排名
	public String	    iLittlePicName;				//头像图片
	public String   	iBigPicName;				//大图片
	public String		iShareTextName;				//分享文本
	public int			iSupportCount;				//支持次数
	public int			iRankTotalCount;			//排行榜总数


	
	public MBRspPlayerInfoTwo() {
		this.iResult = -1;
		this.iMsg = "";
		this.iSeatID = -1;
		this.iUserId = -1;
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
		this.iMoney = 0;
		this.iWealthRank = 0;
		this.iLevelRank = 0;
		this.iVictoryRank = 0;
		this.iLittlePicName = "";
		this.iBigPicName = "";
		this.iShareTextName = "";
		this.iSupportCount = 0;
		this.iRankTotalCount = 0;
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
		Temp.putInt(iSeatID);
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
		Temp.putInt(iMoney);
		Temp.putInt(iWealthRank);
		Temp.putInt(iLevelRank);
		Temp.putInt(iVictoryRank);
		Temp.putShort((short) iLittlePicName.getBytes().length);
		if(iLittlePicName.length() > 0) {
			Temp.put(iLittlePicName.getBytes());
		}
		Temp.putShort((short) iBigPicName.getBytes().length);
		if(iBigPicName.length() > 0) {
			Temp.put(iBigPicName.getBytes());
		}
		Temp.putShort((short) iShareTextName.getBytes().length);
		if(iShareTextName.length() > 0) {
			Temp.put(iShareTextName.getBytes());
		}
		Temp.putInt(iSupportCount);
		Temp.putInt(iRankTotalCount);
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
		iUserId = Temp.getInt();
		iSeatID = Temp.getInt();
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
		iMoney = Temp.getInt();
		iWealthRank = Temp.getInt();
		iLevelRank = Temp.getInt();
		iVictoryRank = Temp.getInt();
		int littlepicnamelen = Temp.getShort();
		if(littlepicnamelen > 0) {
			byte[] littlepicname = new byte[littlepicnamelen];
			Temp.get(littlepicname);
			iLittlePicName = new String(littlepicname);
			littlepicname = null;
		}
		int bigpicnamelen = Temp.getShort();
		if(bigpicnamelen > 0) {
			byte[] bigpicname = new byte[bigpicnamelen];
			Temp.get(bigpicname);
			iBigPicName = new String(bigpicname);
			bigpicname = null;
		}
		int shareTextNameLEN = Temp.getShort();
		if(shareTextNameLEN > 0) {
			byte[] shareTextName = new byte[shareTextNameLEN];
			Temp.get(shareTextName);
			iShareTextName = new String(shareTextName);
			shareTextName = null;
		}
		iSupportCount = Temp.getInt();
		iRankTotalCount = Temp.getInt();
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspPlayerInfoTwo [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iUserId=" + iUserId + ", iSeatID=" + iSeatID + ", iNick="
				+ iNick + ", iVipLevel=" + iVipLevel + ", iGameLevel="
				+ iGameLevel + ", iGameExp=" + iGameExp + ", iCurrentDrunk="
				+ iCurrentDrunk + ", iMaxDrunk=" + iMaxDrunk + ", iKillsCount="
				+ iKillsCount + ", iBeKillsCount=" + iBeKillsCount
				+ ", iModelID=" + iModelID + ", iSex=" + iSex + ", iWinning="
				+ iWinning + ", iTitle=" + iTitle + ", iGb=" + iGb
				+ ", iReward=" + iReward + ", iMoney=" + iMoney
				+ ", iWealthRank=" + iWealthRank + ", iLevelRank=" + iLevelRank
				+ ", iVictoryRank=" + iVictoryRank + ", iLittlePicName="
				+ iLittlePicName + ", iBigPicName=" + iBigPicName
				+ ", iShareTextName=" + iShareTextName + ", iSupportCount="
				+ iSupportCount + ", iRankTotalCount=" + iRankTotalCount + "]";
	}
	
}
