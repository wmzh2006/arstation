package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBRspVisitorLogin implements MsgPara {
	public short		iResult;
	public String		iMsg;						//提示信息
	public int			iUserId;					//用户uid
	public short		iNeedUpdate;				//是否需要更新--大于0更新
	public int			iServerVersion;				//当前最新版本号
	public int			iTotalSize;					//包的总大小
	public String 		iUrl;						 //客户端apkUrl
	
	public MBRspVisitorLogin() {
		this.iResult = -1;
		this.iMsg = "";
		this.iUserId = 0;
		this.iNeedUpdate = 0;
		this.iServerVersion = 0;
		this.iTotalSize = 0;
		this.iUrl = "";
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
		return "MBRspVisitorLogin [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iUserId=" + iUserId + ", iNeedUpdate=" + iNeedUpdate
				+ ", iServerVersion=" + iServerVersion + ", iTotalSize="
				+ iTotalSize + ", iUrl=" + iUrl + "]";
	}
}
