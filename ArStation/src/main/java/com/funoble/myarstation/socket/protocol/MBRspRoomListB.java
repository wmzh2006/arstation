/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspRoomList.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-20 上午10:32:37
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.Vector;


public class MBRspRoomListB implements MsgPara {

	public short 			nResult; //是否成功, 0 --- 表示成功
	public String 			iMsg; //信息
	public int				iCount;					//个数
	public Vector<Integer>	iRoomID;	//房间ID
	public Vector<Integer>	iTableID;  //桌子ID
	public Vector<String>	iRoomName;	//房间名字
	public Vector<String>	iRoomLimit; //房间限制
	public Vector<String>   iRoomBet;	//房间赌注
	public Vector<Integer>	iMinGB;		//进入房间的最新GB
	public Vector<Integer>	iMaxGB;		//进入房间的最大GB
	public int 				iGB; 		//自己当前的金币
	public String			iPicName;	 //图片名称
	public String			iUrl;	 //网址


	/**
	 * construct
	 */
	public MBRspRoomListB() {
		nResult = -1;
		iMsg = "";
		iRoomID = new Vector<Integer>();
		iTableID = new Vector<Integer>();
		iRoomName = new Vector<String>();
		iRoomLimit = new Vector<String>();
		iRoomBet = new Vector<String>();
		iMinGB = new Vector<Integer>();
		iMaxGB = new Vector<Integer>();
		iGB = 0;
		iPicName  = "";
		iUrl  = "";
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
			Temp.putInt(iRoomID.get(i));
			Temp.putInt(iTableID.get(i));
			Temp.putShort((short) iRoomName.get(i).getBytes().length);
			if(iRoomName.get(i).length() > 0) {
				Temp.put(iRoomName.get(i).getBytes());
			}
			Temp.putShort((short) iRoomLimit.get(i).getBytes().length);
			if(iRoomLimit.get(i).length() > 0) {
				Temp.put(iRoomLimit.get(i).getBytes());
			}
			Temp.putShort((short) iRoomBet.get(i).getBytes().length);
			if(iRoomBet.get(i).length() > 0) {
				Temp.put(iRoomBet.get(i).getBytes());
			}
			Temp.putInt(iMinGB.get(i));
			Temp.putInt(iMaxGB.get(i));
		}
		Temp.putInt(iGB);
		Temp.putShort((short) iPicName.getBytes().length);
		Temp.put(iPicName.getBytes());
		Temp.putShort((short) iUrl.getBytes().length);
		Temp.put(iUrl.getBytes());
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
			iRoomID.add(Temp.getInt());
			iTableID.add(Temp.getInt());
			int len = Temp.getShort();
			if(len > 0) {
				byte[] roomname = new byte[len];
				Temp.get(roomname);
				iRoomName.add(new String(roomname));
				roomname = null;
			}
			len = Temp.getShort();
			if(len > 0) {
				byte[] rooLimit = new byte[len];
				Temp.get(rooLimit);
				iRoomLimit.add(new String(rooLimit));
				rooLimit = null;
			}
			len = Temp.getShort();
			if(len > 0) {
				byte[] roombet = new byte[len];
				Temp.get(roombet);
				iRoomBet.add(new String(roombet));
				roombet = null;
			}
			iMinGB.add(Temp.getInt());
			iMaxGB.add(Temp.getInt());
		}
		iGB = Temp.getInt();
		short picnamelen = Temp.getShort();
		if(picnamelen > 0) {
			byte[] data = new byte[picnamelen];
			Temp.get(data);
			iPicName = new String(data);
			data = null;
		}
		short urllen = Temp.getShort();
		if(urllen > 0) {
			byte[] data = new byte[urllen];
			Temp.get(data);
			iUrl = new String(data);
			data = null;
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspRoomListB [nResult=" + nResult + ", iMsg=" + iMsg
				+ ", iCount=" + iCount + ", iRoomID=" + iRoomID + ", iTableID="
				+ iTableID + ", iRoomName=" + iRoomName + ", iRoomLimit="
				+ iRoomLimit + ", iRoomBet=" + iRoomBet + ", iMinGB=" + iMinGB
				+ ", iMaxGB=" + iMaxGB + ", iGB=" + iGB + ", iPicName="
				+ iPicName + ", iUrl=" + iUrl + "]";
	}
}
