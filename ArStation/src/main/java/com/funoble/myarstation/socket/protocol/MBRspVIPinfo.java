/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspVIPinfo.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-2 下午12:07:31
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class MBRspVIPinfo implements MsgPara {
	public short	nResult;				//结果
	public String   iMsg;					//消息
	public int		iVipLevel;				//Vip级别	
	public int		iBuyVipTime;			//购买Vip的时间
	public int		iEndVipTime;			//Vip到期时间
	public int		iLastDay;				//Vip剩余的天数
	public int		iNextLevelDay;			//升级需要的天数
	public int		iCurrentDay;			//当前天数
	public String   iVipInfo;				//当前级别VIP功能的介绍
	public String   iNextVipInfo;		    //下一级别VIP功能的介绍

	public int		iVipGoodsID;			//VIP商品ID
	public String	iVipGoodsName;			//VIP商品的名称
	public int		iVipGoodsIcon;			//VIP商品的图标
	public int		iVipGoodsPrice;			//VIP商品的价格
	public String	iVipGoodsDetail;		//VIP商品详情
	
	/**
	 * construct
	 */
	public MBRspVIPinfo() {
		nResult = -1;
		iMsg = "";
		iVipLevel = 0;
		iBuyVipTime = 0;
		iBuyVipTime = 0;
		iEndVipTime = 0;
		iLastDay = 0;
		iNextLevelDay = 0;
		iCurrentDay = 0;
		iVipInfo = "";
		iNextVipInfo = "";
		iVipGoodsID = 0;
		iVipGoodsName = "";
		iVipGoodsIcon = 0;
		iVipGoodsPrice = 0;
		iVipGoodsDetail = "";
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
		Temp.putShort(nResult);
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iVipLevel);
		Temp.putInt(iBuyVipTime);
		Temp.putInt(iEndVipTime);
		Temp.putInt(iLastDay);
		Temp.putInt(iNextLevelDay);
		Temp.putInt(iCurrentDay);
		Temp.putShort((short) iVipInfo.getBytes().length);
		if(iVipInfo.length() > 0) {
			Temp.put(iVipInfo.getBytes());
		}
		Temp.putShort((short) iNextVipInfo.getBytes().length);
		if(iNextVipInfo.length() > 0) {
			Temp.put(iNextVipInfo.getBytes());
		}
		Temp.putInt(iVipGoodsID);
		Temp.putShort((short) iVipGoodsName.getBytes().length);
		if(iVipGoodsName.length() > 0) {
			Temp.put(iVipGoodsName.getBytes());
		}
		Temp.putInt(iVipGoodsIcon);
		Temp.putInt(iVipGoodsPrice);
		Temp.putShort((short) iVipGoodsDetail.getBytes().length);
		if(iVipGoodsDetail.length() > 0) {
			Temp.put(iVipGoodsDetail.getBytes());
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
		nResult = Temp.getShort();
		int msglen = Temp.getShort();
		if(msglen > 0) {
			byte[] msg = new byte[msglen];
			Temp.get(msg);
			iMsg = new String(msg);
			msg = null;
		}
		iVipLevel = Temp.getInt();
		iBuyVipTime = Temp.getInt();
		iEndVipTime = Temp.getInt();
		iLastDay = Temp.getInt();
		iNextLevelDay = Temp.getInt();
		iCurrentDay = Temp.getInt();
		int vipinfolen = Temp.getShort();
		if(vipinfolen > 0) {
			byte[] vipinfo = new byte[vipinfolen];
			Temp.get(vipinfo);
			iVipInfo = new String(vipinfo);
			vipinfo = null;
		}
		int nextvipinfolen = Temp.getShort();
		if(nextvipinfolen > 0) {
			byte[] nextvipinfo = new byte[nextvipinfolen];
			Temp.get(nextvipinfo);
			iNextVipInfo = new String(nextvipinfo);
			nextvipinfo = null;
		}
		iVipGoodsID = Temp.getInt();
		int vipGoodsNameLen = Temp.getShort();
		if(vipGoodsNameLen > 0) {
			byte[] vipGoodsName = new byte[vipGoodsNameLen];
			Temp.get(vipGoodsName);
			iVipGoodsName = new String(vipGoodsName);
			vipGoodsName = null;
		}
		iVipGoodsIcon = Temp.getInt();
		iVipGoodsPrice = Temp.getInt();
		int vipGoodsDetailLen = Temp.getShort();
		if(vipGoodsDetailLen > 0) {
			byte[] vipGoodsDetail = new byte[vipGoodsDetailLen];
			Temp.get(vipGoodsDetail);
			iVipGoodsDetail = new String(vipGoodsDetail);
			vipGoodsDetail = null;
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspVIPinfo [nResult=" + nResult + ", iMsg=" + iMsg
				+ ", iVipLevel=" + iVipLevel + ", iBuyVipTime=" + iBuyVipTime
				+ ", iEndVipTime=" + iEndVipTime + ", iLastDay=" + iLastDay
				+ ", iNextLevelDay=" + iNextLevelDay + ", iCurrentDay="
				+ iCurrentDay + ", iVipInfo=" + iVipInfo + ", iNextVipInfo="
				+ iNextVipInfo + ", iVipGoodsID=" + iVipGoodsID
				+ ", iVipGoodsName=" + iVipGoodsName + ", iVipGoodsIcon="
				+ iVipGoodsIcon + ", iVipGoodsPrice=" + iVipGoodsPrice
				+ ", iVipGoodsDetail=" + iVipGoodsDetail + "]";
	}

}
