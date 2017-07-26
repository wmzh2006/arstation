package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBRspThirdLogin extends MBRspLogin {
	public String		iUserName;					//用户名称
	public String		iUserPwd;					//用户密码
	public int			iChannelID;	//渠道号 // 1 --- 腾讯    2 --- 新浪   3 --- 人人网		4 --- OPPO

	public MBRspThirdLogin() {
		super();
		this.iUserName = "";
		this.iUserPwd = "";
		this.iChannelID = 0;
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
		Temp.putShort(iNeedUpdate);
		Temp.putInt(iServerVersion);
		Temp.putInt(iTotalSize);
		Temp.putShort((short) iUrl.getBytes().length);
		if(iUrl.length() > 0) {
			Temp.put(iUrl.getBytes());
		}
		Temp.putShort((short) iUserName.getBytes().length);
		if(iUserName.length() > 0) {
			Temp.put(iUserName.getBytes());
		}
		Temp.putShort((short) iUserPwd.getBytes().length);
		if(iUserPwd.length() > 0) {
			Temp.put(iUserPwd.getBytes());
		}
		Temp.putInt(iChannelID);
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
		iNeedUpdate = Temp.getShort();
		iServerVersion = Temp.getInt();
		iTotalSize = Temp.getInt();
		int urllen = Temp.getShort();
		if(urllen > 0) {
			byte[] url = new byte[urllen];
			Temp.get(url);
			iUrl = new String(url);
			url = null;
		}
		int userNameLen = Temp.getShort();
		if(userNameLen > 0) {
			byte[] userName = new byte[userNameLen];
			Temp.get(userName);
			iUserName = new String(userName);
			userName = null;
		}
		int userPwdLen = Temp.getShort();
		if(userPwdLen > 0) {
			byte[] userPwd = new byte[userPwdLen];
			Temp.get(userPwd);
			iUserPwd = new String(userPwd);
			userPwd = null;
		}
		iChannelID = Temp.getShort();
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
		return "MBRspThirdLogin [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iUserId=" + iUserId + ", iNeedUpdate=" + iNeedUpdate
				+ ", iServerVersion=" + iServerVersion + ", iTotalSize="
				+ iTotalSize + ", iUrl=" + iUrl + ", iUserName=" + iUserName
				+ ", iUserPwd=" + iUserPwd + ", iChannelID=" + iChannelID + "]";
	}

}
