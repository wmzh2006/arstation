package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBRspPlayerInfoFour extends MBRspPlayerInfoThree implements MsgPara {
	public String iHonor;
	
	public MBRspPlayerInfoFour() {
		super();
		iHonor = "";
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
		Temp.putInt(iSupportCount);
		Temp.putInt(iRankTotalCount);
		Temp.putInt(iAction);
		Temp.putInt(iSecond);
		Temp.putShort(nOnLine);
		Temp.putShort(nDiceResID);
		Temp.putShort(nHasCard);
		Temp.putShort(nHasLicense);
		Temp.putInt(iFriendExp);
		Temp.putInt(iNextFriendExp);
		Temp.putShort((short) iFriendTitle.getBytes().length);
		if(iFriendTitle.length() > 0) {
			Temp.put(iFriendTitle.getBytes());
		}
		Temp.putInt(iNextExp);
		Temp.putShort(iZhaiSkill);
		Temp.putShort(iCarID);
		Temp.putShort(iHouseID);
		Temp.putShort(iPetID);
		Temp.putShort((short) iHonor.getBytes().length);
		if(iHonor.length() > 0) {
			Temp.put(iHonor.getBytes());
		}
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
		iCurrentDrunk 	= Temp.getInt();
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
		
		iSupportCount = Temp.getInt();
		iRankTotalCount = Temp.getInt();
		iAction = Temp.getInt();
		iSecond = Temp.getInt();
		nOnLine = Temp.getShort();
		nDiceResID = Temp.getShort();
		nHasCard = Temp.getShort();
		nHasLicense = Temp.getShort();
		iFriendExp = Temp.getInt();
		iNextFriendExp = Temp.getInt();
		int shareTextNameLEN = Temp.getShort();
		if(shareTextNameLEN > 0) {
			byte[] shareTextName = new byte[shareTextNameLEN];
			Temp.get(shareTextName);
			iFriendTitle = new String(shareTextName);
			shareTextName = null;
		}
		iNextExp = Temp.getInt();
		iZhaiSkill = Temp.getShort();
		iCarID = Temp.getShort();
		iHouseID = Temp.getShort();
		iPetID = Temp.getShort();
		int len = Temp.getShort();
		if(len > 0) {
			byte[] data = new byte[len];
			Temp.get(data);
			iHonor = new String(data);
			data = null;
		}
		return Temp.position();
	}


	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspPlayerInfoFour [iHonor=" + iHonor + "]";
	}


}
