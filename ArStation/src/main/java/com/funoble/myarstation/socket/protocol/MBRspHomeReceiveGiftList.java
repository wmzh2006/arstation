/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspKick.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 上午10:32:37
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;


public class MBRspHomeReceiveGiftList implements MsgPara {

	public short nResult; //是否成功, 0 --- 表示成功
	public String iMsg; //信息
	public int	 iCount;	 
	public Vector<Short>	IIDs;	//礼品的ID
	public Vector<Short>	iCharm;			//增加的魅力值
	public Vector<String>   iNick;
	public Vector<Integer>	iTime;	//赠礼时间

	/**
	 * construct
	 */
	public MBRspHomeReceiveGiftList() {
		nResult = -1;
		iMsg = "";
		iCount = 0;
		IIDs = new Vector<Short>();
		iCharm = new Vector<Short>();
		iNick = new Vector<String>();
		iTime = new Vector<Integer>();
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
		Temp.putShort(nResult);
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iCount);
		for(int i = 0; i < iCount; i++) {
			Temp.putShort(IIDs.get(i));
			Temp.putShort(iCharm.get(i));
			Temp.putShort((short) iNick.get(i).length());
			if(iNick.get(i).length() > 0) {
				Temp.put(iNick.get(i).getBytes());
			}
			Temp.putInt(iTime.get(i));
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
		nResult = Temp.getShort();
		int msglen = Temp.getShort();
		if(msglen > 0) {
			byte[] msg = new byte[msglen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iCount = Temp.getInt();
		for(int i = 0; i < iCount; i++) {
			IIDs.add(Temp.getShort());
			iCharm.add(Temp.getShort());
			int len = Temp.getShort();
			if(len > 0) {
				byte[] data = new byte[len];
				Temp.get(data);
				iNick.add(new String(data));
				data = null;
			}
			else {
				iNick.add(new String(""));
			}
			iTime.add(Temp.getInt());
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspHomeReceiveGiftList [nResult=" + nResult + ", iMsg="
				+ iMsg + ", iCount=" + iCount + ", IIDs=" + IIDs + ", iCharm="
				+ iCharm + ", iNick=" + iNick + ", iTime=" + iTime + "]";
	}

}
