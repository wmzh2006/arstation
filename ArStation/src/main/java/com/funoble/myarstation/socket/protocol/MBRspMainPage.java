package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBRspMainPage implements MsgPara {
	public short		iResult;
	public String		iMsg;						//提示信息
	public int			iUserId;					//用户uid
	public String		iNick;						//用户昵称
	public int			iMoney;						//凤智币
	public int			iGb;						//游戏币
	public int			iVipLevel;					//Vip级别	
	public int			iVipTime;					//Vip到期时间
	public int			iGameLevel;					//级别
	public int			iGameExp;					//当前级别的经验
	public int			iCurrentDrunk;				//当前醉酒度
	public int			iMaxDrunk;					//支持数（原来是最大醉酒度，一直没有用)
	public int			iKillsCount;				//灌倒多少人
	public int			iBeKillsCount;				//被灌倒多少次
	public int			iModelID;					//角色模型ID（为-1时，表示没选择人物）
	public int			iSex;						//性别（为-1时，表示没选择人物）
	public int			iOnLineCount;				//同时在线人数
	public int     		iWinning;					//胜率
	public String		iTitle;						//人物称号
	public int			iReward;					//奖
	public int			iNextExp;					//升级经验
	public String		iText; 						//数据
	public String    	iLittlePicName;				//头像图片
	public String		iBigPicName;				//大图片
	public short		iZhaiSkill;					//直接喊斋技能 0 --- 没学会    1 --- 学会

	
	public MBRspMainPage() {
		this.iResult = -1;
		this.iMsg = "";
		this.iUserId = 0;
		this.iNick = "";
		this.iMoney = 0;
		this.iGb = 0;
		this.iVipLevel = 0;
		this.iVipTime = 0;
		this.iGameLevel = 0;
		this.iGameExp = 0;
		this.iCurrentDrunk = 0;
		this.iMaxDrunk = 0;
		this.iKillsCount = 0;
		this.iBeKillsCount = 0;
		this.iModelID = -1;
		this.iSex = -1;
		this.iOnLineCount = 0;
		this.iWinning = 0;
		this.iTitle = "";
		this.iReward = 0;
		this.iNextExp = 0;
		this.iText = "";
		this.iLittlePicName = "";
		this.iBigPicName = "";
		this.iZhaiSkill = 0;
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
		Temp.putInt(iMoney);
		Temp.putInt(iGb);
		Temp.putInt(iVipLevel);
		Temp.putInt(iVipTime);
		Temp.putInt(iGameLevel);
		Temp.putInt(iGameExp);
		Temp.putInt(iCurrentDrunk);
		Temp.putInt(iMaxDrunk);
		Temp.putInt(iKillsCount);
		Temp.putInt(iBeKillsCount);
		Temp.putInt(iModelID);
		Temp.putInt(iSex);
		Temp.putInt(iOnLineCount);
		Temp.putInt(iWinning);
		Temp.putShort((short) iTitle.getBytes().length);
		if(iTitle.length() > 0) {
			Temp.put(iTitle.getBytes());
		}
		Temp.putInt(iReward);
		Temp.putInt(iNextExp);
		Temp.putShort((short) iText.getBytes().length);
		if(iText.length() > 0) {
			Temp.put(iText.getBytes());
		}
		Temp.putShort((short) iLittlePicName.getBytes().length);
		if(iLittlePicName.length() > 0) {
			Temp.put(iLittlePicName.getBytes());
		}
		Temp.putShort((short) iBigPicName.getBytes().length);
		if(iBigPicName.length() > 0) {
			Temp.put(iBigPicName.getBytes());
		}
		Temp.putShort(iZhaiSkill);
		sTempLength = (short) Temp.position();
		if(sTempLength > aOutLength) {
			return -1;
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
		int nickLen = Temp.getShort();
		if(nickLen > 0) {
			byte[] nick = new byte[nickLen];
			Temp.get(nick);
			iNick = new String(nick);
			nick = null;
		}
		iMoney 			= Temp.getInt();
		iGb 			= Temp.getInt();
		iVipLevel 		= Temp.getInt();
		iVipTime 		= Temp.getInt();
		iGameLevel 		= Temp.getInt();
		iGameExp 		= Temp.getInt();
		iCurrentDrunk 	= Temp.getInt();
		iMaxDrunk 		= Temp.getInt();
		iKillsCount 	= Temp.getInt();
		iBeKillsCount 	= Temp.getInt();
		iModelID 		= Temp.getInt();
		iSex 			= Temp.getInt();
		iOnLineCount 	= Temp.getInt();
		iWinning 		= Temp.getInt();
		int titlelen	= Temp.getShort();
		if(titlelen > 0) {
			byte[] title = new byte[titlelen];
			Temp.get(title);
			iTitle = new String(title);
			title = null;
		}
		iReward 		= Temp.getInt();
		iNextExp 		= Temp.getInt();
		int textlen = Temp.getShort();
		if(textlen > 0) {
			byte[] text = new byte[textlen];
			Temp.get(text);
			iText = new String(text);
			text = null;
		}
		int litlePiclen = Temp.getShort();
		if(litlePiclen > 0) {
			byte[] text = new byte[litlePiclen];
			Temp.get(text);
			iLittlePicName = new String(text);
			text = null;
		}
		int bigPiclen = Temp.getShort();
		if(bigPiclen > 0) {
			byte[] text = new byte[bigPiclen];
			Temp.get(text);
			iBigPicName = new String(text);
			text = null;
		}
		iZhaiSkill = Temp.getShort();
		sTempLength = (short) Temp.position();
		if(sTempLength > shLeftLength) {
			return -1;
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspMainPage [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iUserId=" + iUserId + ", iNick=" + iNick + ", iMoney="
				+ iMoney + ", iGb=" + iGb + ", iVipLevel=" + iVipLevel
				+ ", iVipTime=" + iVipTime + ", iGameLevel=" + iGameLevel
				+ ", iGameExp=" + iGameExp + ", iCurrentDrunk=" + iCurrentDrunk
				+ ", iMaxDrunk=" + iMaxDrunk + ", iKillsCount=" + iKillsCount
				+ ", iBeKillsCount=" + iBeKillsCount + ", iModelID=" + iModelID
				+ ", iSex=" + iSex + ", iOnLineCount=" + iOnLineCount
				+ ", iWinning=" + iWinning + ", iTitle=" + iTitle
				+ ", iReward=" + iReward + ", iNextExp=" + iNextExp
				+ ", iText=" + iText + ", iLittlePicName=" + iLittlePicName
				+ ", iBigPicName=" + iBigPicName + "]";
	}

}
