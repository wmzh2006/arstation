/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspUpdate.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 上午10:32:37
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.funoble.myarstation.common.Tools;


public class MBRspUpdate implements MsgPara {

	public short 	iResult; //是否成功, 0 --- 表示成功
	public String 	iMsg; //信息
	public int		iServerVersion;				//服务端版本
	public int		iTotalSize;					//总大小
	public int		iStartIndex;				//开始的索引
	public int		iSize;						//这次发送给客户端的内容的大小
	public byte[] 	iData; 						//数据

	/**
	 * construct
	 */
	public MBRspUpdate() {
		iResult = -1;
		iMsg = "";
		iServerVersion = 0;
		iTotalSize = 0;
		iStartIndex = 0;
		iSize = 0;
		iData = new byte[0];
	}

	/* 
	 * @see com.funoble.lyingdice.socket.protocol.MsgPara#Encode(byte[], short)
	 */
	@Override
	public int Encode(byte[] aOutBuffer, short aOutLength) {
		if(aOutBuffer == null || aOutLength < 0) {
			return -1;
		}
		ByteBuffer Temp  = ByteBuffer.wrap(aOutBuffer);
		int shLenght = 2;
		shLenght += aOutLength;
		Temp.position(aOutLength);
		Temp.putShort(iResult);
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iServerVersion);
		Temp.putInt(iTotalSize);
		Temp.putInt(iStartIndex);
		Temp.putInt(iSize);
		if(iSize > 0) {
			Temp.put(iData);
		}
		shLenght = Temp.position();
		shLenght -= shLenght;
		Temp.putShort(aOutLength, (short) shLenght);
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
		int shLenght = 0;
		shLenght = Temp.getShort();
		if(shLenght > aInBuffer.length) {
			return -2;
		}
		iResult = Temp.getShort();
		int msglen = Temp.getShort();
		if(msglen > 0) {
			byte[] msg = new byte[msglen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iServerVersion = Temp.getInt();
		iTotalSize = Temp.getInt();
		iStartIndex = Temp.getInt();
		iSize = Temp.getInt();
		Tools.debug("iSize" + iSize);
		if(iSize > 0) {
			iData = null;
			iData = new byte[iSize];
			Temp.get(iData);
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspUpdate [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iServerVersion=" + iServerVersion + ", iTotalSize="
				+ iTotalSize + ", iStartIndex=" + iStartIndex + ", iSize="
				+ iSize + ", iData=" + Arrays.toString(iData) + "]";
	}
}
