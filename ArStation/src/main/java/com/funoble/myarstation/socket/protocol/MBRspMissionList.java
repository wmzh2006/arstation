/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspDing.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-24 上午11:14:10
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.Vector;


public class MBRspMissionList implements MsgPara {
	public short    			iResult;						//结果
	public String				iMsg;							//消息
	public int					iType;		// 0 --- 每日   1 --- 酒吧
	public int					iCount;		//个数
	public Vector<Integer>		iMissionIDs;
	public Vector<String>		stMissionNames;	//任务名称
	public Vector<String>	    stMissionDess;	//任务描述
	public Vector<String>	    szLittlePics;	//图片名称
	public Vector<Integer>	    iButtonStatus;  //任务按钮状态 0 --- 不显示   1 --- 显示

	/**
	 * construct
	 */
	public MBRspMissionList() {
		iResult = -1;
		iMsg = "";
		iMissionIDs = new Vector<Integer>();
		stMissionNames = new Vector<String>();
		stMissionDess = new Vector<String>();
		szLittlePics = new Vector<String>();
		iButtonStatus = new Vector<Integer>();
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
		Temp.putShort(iResult);
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iType);
		Temp.putInt(iCount);
		for(int i = 0; i < iCount; i++) {
			Temp.putInt(iMissionIDs.get(i));
			Temp.putShort((short) stMissionNames.get(i).getBytes().length);
			if((short) stMissionNames.get(i).getBytes().length > 0)
			Temp.put(stMissionNames.get(i).getBytes());
			Temp.putShort((short) stMissionDess.get(i).getBytes().length);
			if((short) stMissionDess.get(i).getBytes().length > 0)
			Temp.put(stMissionDess.get(i).getBytes());
			Temp.putShort((short) szLittlePics.get(i).getBytes().length);
			if((short) szLittlePics.get(i).getBytes().length > 0)
			Temp.put(szLittlePics.get(i).getBytes());
			Temp.putInt(iButtonStatus.get(i));
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
		iResult = Temp.getShort();
		int msglen = Temp.getShort();
		if(msglen > 0) {
			byte[] msg = new byte[msglen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iType = Temp.getInt();
		iCount = Temp.getInt();
		for(int i = 0; i < iCount; i++) {
			iMissionIDs.add(Temp.getInt());
			int len = Temp.getShort();
			if(len > 0) {
				byte[] data = new byte[len];
				Temp.get(data);
				stMissionNames.add(new String(data));
			}
			else {
				stMissionNames.add(new String());
			}
			len = Temp.getShort();
			if(len > 0) {
				byte[] data = new byte[len];
				Temp.get(data);
				stMissionDess.add(new String(data));
			}
			else {
				stMissionDess.add(new String());
			}
			len = Temp.getShort();
			if(len > 0) {
				byte[] data = new byte[len];
				Temp.get(data);
				szLittlePics.add(new String(data));
			}
			else {
				szLittlePics.add(new String(""));
			}
			iButtonStatus.add(Temp.getInt());
		}
		shLength = Temp.position();
		return shLength;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspMissionList [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iType=" + iType + ", iCount=" + iCount + ", iMissionIDs="
				+ iMissionIDs + ", stMissionNames=" + stMissionNames
				+ ", stMissionDess=" + stMissionDess + ", szLittlePics="
				+ szLittlePics + ", iButtonStatus=" + iButtonStatus + "]";
	}
}
