package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;

public interface MsgPara {
	/**
	 * @param aOutBuffer
	 * @param aOutLength 已写数据长度
	 * @return
	 */
	public abstract int Encode(byte[] aOutBuffer, short aOutLength);
	/**
	 * @param aInBuffer
	 * @param aInLength 已读数据长度
	 * @return
	 */
	public abstract int Decode(byte[] aInBuffer, short aInLength);
}
