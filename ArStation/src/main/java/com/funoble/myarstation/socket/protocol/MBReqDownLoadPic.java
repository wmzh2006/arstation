/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqDownLoadPic.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 下午03:55:08
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBReqDownLoadPic implements MsgPara {
	public String   iFileName;			//文件名
	public int		iStartIndex;		//开始的索引

	/**
	 * construct
	 */
	public MBReqDownLoadPic() {
		iFileName = "";
		iStartIndex = -1;
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
		Temp.putShort((short) iFileName.getBytes().length);
		if(iFileName.length() > 0) {
			Temp.put(iFileName.getBytes());
		}
		Temp.putInt(iStartIndex);
		shLength = Temp.position();
		shLength -= aOutLength;
		if(shLength < 0) {
			return -1;
		}
		Temp.putShort(aOutLength, (short) shLength);
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
		int shlength = 0;
		shlength = Temp.getShort();
		if(shlength > aInBuffer.length) {
			return -1;
		}
		int len = Temp.getShort();
		if(len > 0) {
			byte[] name = new byte[len];
			Temp.get(name);
			iFileName = new String(name);
			name = null;
		}
		iStartIndex = Temp.getInt();
		shlength = Temp.position();
		return shlength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBReqDownLoadPic [iFileName=" + iFileName + ", iStartIndex="
				+ iStartIndex + "]";
	}
	
}
