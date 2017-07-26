/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqUpLoadPic.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-2 下午04:39:15
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.Arrays;


public class MBReqUpLoadPic implements MsgPara {
	public int		iFileSize;					//文件的总大小
	public int		iCurrentSize;				//当前数据的大小
	public byte[]	iBuf;						//数据缓冲区

	/**
	 * construct
	 */
	public MBReqUpLoadPic() {
		iFileSize = 0;
		iCurrentSize = 0;
		iBuf = new byte[iCurrentSize];
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
		Temp.putInt(iFileSize);
		Temp.putInt(iCurrentSize);
		if(iCurrentSize > BufferSize.MAX_UPLOAD_BLOCK_LEN) {
			return -2;
		}
		if(iCurrentSize > 0) {
			Temp.put(iBuf);
		}
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
		iFileSize = Temp.getInt();
		iCurrentSize = Temp.getInt();
		iBuf = new byte[iCurrentSize];
		Temp.get(iBuf);
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBReqUpLoadPic [iFileSize=" + iFileSize + ", iCurrentSize="
				+ iCurrentSize + ", iBuf=" + Arrays.toString(iBuf) + "]";
	}

}
