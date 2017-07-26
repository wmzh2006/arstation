/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: CacheMsgHead.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-15 下午12:00:13
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MobileMsgHead {
	public short m_sMsgID;
	public byte  m_cMsgType;
	public int	 m_iMsgSeq;
	/**
	 * construct
	 */
	public MobileMsgHead() {
		m_iMsgSeq = -1;
		m_sMsgID = -1;
		m_cMsgType = (byte) -1;
	}

	public int Encode(byte[] aOutBuffer, short aOutLenght) {
		if(aOutBuffer == null) {
			return -1;
		}
		ByteBuffer pTem = ByteBuffer.wrap(aOutBuffer);
		pTem.position(aOutLenght);
		pTem.putShort(m_sMsgID);
		pTem.put(m_cMsgType);
		pTem.putInt(m_iMsgSeq);
		if(pTem.position() <= 0) {
			return -1;
		}
		aOutLenght = (short) pTem.position();
		return aOutLenght;
	}
	
	public int Decode(byte[] aInBffer, short aInLength) {
		if(aInBffer == null || aInLength <= 0 || aInLength > aInBffer.length) {
			return -1;
		}
		short shLeftLength;
		ByteBuffer pTem = ByteBuffer.allocate(aInBffer.length + 2);
		pTem.put(aInBffer);
		pTem.position(aInLength);
		m_sMsgID = pTem.getShort();
		m_cMsgType = pTem.get();
		m_iMsgSeq = pTem.getInt();
		shLeftLength = (short) pTem.position();
//		if(aInBffer.length - shLeftLength < 0) {
//			return -1;
//		}
		return shLeftLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MobileMsgHead [m_sMsgID=" + m_sMsgID + ", m_cMsgType="
				+ m_cMsgType + ", m_iMsgSeq=" + m_iMsgSeq + "]";
	}
	
}
