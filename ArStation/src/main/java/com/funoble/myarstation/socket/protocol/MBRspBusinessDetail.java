/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspBusinessDetail.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-2 下午04:39:41
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.funoble.myarstation.common.Tools;


public class MBRspBusinessDetail implements MsgPara {
	public short	nResult;					//结果
	public String    iMsg;						//消息
	public int		iTotalMoney;				//总购买额
	public int		iTotalCount;				//总数量
	public int      iStartIndex;				//开始的索引号
	public int		iCount;						//数量
	public ArrayList<Integer>		iGoodsID;	//商品ID
	public ArrayList<Integer>		iGoodsIcon;//商品图标
	public ArrayList<Integer>		iGoodsPrice;	//商品价格
	public ArrayList<String>		iGoodsName;	//物品名
	public ArrayList<Integer>		iBuyGoodsTime;//购买物品的时间
	public ArrayList<String>	    iGoodsInfo;	//物品功能的介绍

	/**
	 * construct
	 */
	public MBRspBusinessDetail() {
		nResult = -1;
		iMsg = "";
		iTotalMoney =  0;
		iTotalCount = 0;
		iStartIndex = 0;
		iCount = 0;
		iGoodsID = new ArrayList<Integer>();
		iGoodsIcon = new ArrayList<Integer>();
		iGoodsPrice = new ArrayList<Integer>();
		iGoodsName = new ArrayList<String>();
		iBuyGoodsTime = new ArrayList<Integer>();
		iGoodsInfo = new ArrayList<String>();
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
		Temp.putShort(nResult);
		int msglen = iMsg.getBytes().length;
		Temp.putShort((short) msglen);
		if(msglen > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iTotalMoney);
		Temp.putInt(iTotalCount);
		Temp.putInt(iStartIndex);
		Temp.putInt(iCount);
		for(int i = 0; i < iCount; i++) {
			Temp.putInt(iGoodsID.get(i));
			Temp.putInt(iGoodsIcon.get(i));
			Temp.putInt(iGoodsPrice.get(i));
			int goodsNameLen = iGoodsName.get(i).getBytes().length;
			Temp.putShort((short) goodsNameLen);
			if(goodsNameLen > 0) {
				Temp.put(iGoodsName.get(i).getBytes());
			}
			Temp.putInt(iBuyGoodsTime.get(i));
			int goodsInfoLength = iGoodsInfo.get(i).getBytes().length;
			if(goodsInfoLength > 0) {
				Temp.put(iGoodsInfo.get(i).getBytes());
			}
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
		Tools.debug(toString());
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
		iTotalMoney = Temp.getInt();
		iTotalCount = Temp.getInt();
		iStartIndex = Temp.getInt();
		iCount = Temp.getInt();
		for(int i = 0; i < iCount; i++) {
			iGoodsID.add(Temp.getInt());
			iGoodsIcon.add(Temp.getInt());
			iGoodsPrice.add(Temp.getInt());
			int goodsNameLen = Temp.getShort();
			if(goodsNameLen > 0) {
				byte[] goodsName = new byte[goodsNameLen];
				Temp.get(goodsName);
				iGoodsName.add(new String(goodsName));
				goodsName = null;
			}
			iBuyGoodsTime.add(Temp.getInt());
			int goodsInfolen = Temp.getShort();
			if(goodsInfolen > 0) {
				byte[] goodsInfo = new byte[goodsInfolen];
				Temp.get(goodsInfo);
				iGoodsInfo.add(new String(goodsInfo));
				goodsInfo = null;
			}
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspBusinessDetail [nResult=" + nResult + ", iMsg=" + iMsg
				+ ", iTotalMoney=" + iTotalMoney + ", iTotalCount="
				+ iTotalCount + ", iStartIndex=" + iStartIndex + ", iCount="
				+ iCount + ", iGoodsID=" + iGoodsID + ", iGoodsIcon="
				+ iGoodsIcon + ", iGoodsPrice=" + iGoodsPrice + ", iGoodsName="
				+ iGoodsName + ", iBuyGoodsTime=" + iBuyGoodsTime
				+ ", iGoodsInfo=" + iGoodsInfo + "]";
	}

}
