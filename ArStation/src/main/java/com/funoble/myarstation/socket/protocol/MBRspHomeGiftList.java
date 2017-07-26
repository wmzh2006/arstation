/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspAccount.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 上午10:32:37
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.Vector;

public class MBRspHomeGiftList implements MsgPara {
	public short	iResult;
	public String	iMsg;			//提示信息
	public int		iCount;		//个数
	public Vector<Short>	iIDs;				//礼品的ID
	public Vector<Short>	iMoneys;				//需要的凤币
	public Vector<Integer>	iGbs;				//需要的金币
	public Vector<Short>	iIntimates;			//增加的亲密度
	public Vector<String>	iNames;

	/**
	 * construct
	 */
	public MBRspHomeGiftList() {
		iResult = -1;
		iMsg = "";
		iCount = 0;
		iIDs = new Vector<Short>();
		iMoneys = new Vector<Short>();
		iGbs = new Vector<Integer>();
		iIntimates = new Vector<Short>();
		iNames = new Vector<String>();
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
		//
		Temp.putShort(iResult);
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iCount);
		for(int i = 0; i < iCount; i++) {
			Temp.putShort(iIDs.get(i));
			Temp.putShort(iMoneys.get(i));
			Temp.putInt(iGbs.get(i));
			Temp.putShort(iIntimates.get(i));
			Temp.putShort((short) iNames.get(i).getBytes().length);
			if(iNames.get(i).length() > 0) {
				Temp.put(iNames.get(i).getBytes());
			}
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
		//
		iResult = Temp.getShort();
		int len = Temp.getShort();
		if(len > 0) {
			byte[] data = new byte[len];
			Temp.get(data);
			iMsg = new String(data);
		}
		iCount = Temp.getInt();
		for (int i = 0; i < iCount; i++) {
			iIDs.add(Temp.getShort());
			iMoneys.add(Temp.getShort());
			iGbs.add(Temp.getInt());
			iIntimates.add(Temp.getShort());
			len = Temp.getShort();
			if(len > 0) {
				byte[] data = new byte[len];
				Temp.get(data);
				iNames.add(new String(data));
				data = null;
			}
			else {
				iNames.add(new String(""));
			}
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspHomeGiftList [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iCount=" + iCount + ", iIDs=" + iIDs + ", iMoneys="
				+ iMoneys + ", iGbs=" + iGbs + ", iIntimates=" + iIntimates
				+ ", iNames=" + iNames + "]";
	}

}
