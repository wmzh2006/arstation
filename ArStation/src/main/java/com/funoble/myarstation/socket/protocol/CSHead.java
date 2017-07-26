package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;

public class CSHead {
	public short 	iVer;					// 协议版本号
	public short 	iPlatformID;			// 客户端平台ID
	public short 	iMobileType;			// 手机型号
	public short 	iUIDLen;				// 用户ID长度
	public String 	iUID;					// 用户ID
	
	
	public CSHead() {
		this.iVer = 0x00;
		this.iPlatformID = 0;
		this.iMobileType = 0;
		this.iUIDLen = 0;
		this.iUID = null;
	}

	public int Encode(byte[] aOutBuffer, short aOutLenght) {
		if(aOutBuffer == null) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.wrap(aOutBuffer);
		short tempLenght = 0;
		
		Temp.position(aOutLenght);
		Temp.putShort(iVer);
		Temp.putShort(iPlatformID);
		Temp.putShort(iMobileType);
		Temp.putShort((short) iUID.length());
		if(iUID.length() > 0) {
			Temp.put(iUID.getBytes());
		}
		tempLenght = (short) Temp.position();
		return tempLenght;
	}
	
	public int Decode(byte[] aInBuffer, short aInLenght) {
		if(aInBuffer == null || aInLenght <= 0) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.allocate(aInBuffer.length + 2);
		Temp.put(aInBuffer);
		Temp.position(aInLenght);
		short shLenght = 0;
		iVer = Temp.getShort();
		iPlatformID = Temp.getShort();
		iMobileType = Temp.getShort();
		iUIDLen = Temp.getShort();
		if(iUIDLen > 0) {
			byte[] temp = new byte[iUIDLen];
			Temp.get(temp);
			iUID = new String(temp);
			temp = null;
		}
		shLenght = (short) Temp.position();
		return shLenght;
	}
}
