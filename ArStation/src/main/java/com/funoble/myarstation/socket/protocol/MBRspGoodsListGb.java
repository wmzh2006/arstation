/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBRspGoodsList.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-28 下午12:22:13
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import android.provider.ContactsContract.CommonDataKinds.Im;

import com.funoble.myarstation.utils.TPoint;


public class MBRspGoodsListGb implements MsgPara {
	public short					nResult;			//结果
	public String  					iMsg;				//消息
	
	public int    					iGbGoodsTotalCount;//RMB商品的总个数
	public int    				 	iStartIndex;		//开始的索引号
	public int						iGbGoodsCount;		//GB商品的个数
	public ArrayList<Integer>		iGbGoodsID;			//GB商品ID
	public ArrayList<String> 		iGbGoodsName;		//GB商品的名称
	public ArrayList<Integer>		iGbGoodsIcon;		//GB商品的图标
	public ArrayList<Integer>		iGbGoodsPrice;		//GB商品的价格
	public ArrayList<String> 		iGbGoodsDetail;		//GB商品详情

	/**
	 * construct
	 */
	public MBRspGoodsListGb() {
		nResult = -1;
		iMsg = "";
		iGbGoodsTotalCount = 0;
		iStartIndex = 0;
		iGbGoodsCount = 0;
		iGbGoodsID = new ArrayList<Integer>();
		iGbGoodsName = new ArrayList<String>();
		iGbGoodsIcon = new ArrayList<Integer>();
		iGbGoodsPrice = new ArrayList<Integer>();
		iGbGoodsDetail = new ArrayList<String>();
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
		Temp.putShort((short) iMsg.getBytes().length);
		if(iMsg.length() > 0) {
			Temp.put(iMsg.getBytes());
		}
		Temp.putInt(iGbGoodsTotalCount);
		Temp.putInt(iStartIndex);
		Temp.putInt(iGbGoodsCount);
		for(int i = 0; i < iGbGoodsCount; i++) {
			Temp.putInt(iGbGoodsID.get(i));
			String gbGoodsName = iGbGoodsName.get(i);
			if(gbGoodsName != null) {
				int gbGoodsNameLen = gbGoodsName.getBytes().length;
				Temp.putShort((short) gbGoodsNameLen);
				if(gbGoodsNameLen > 0) {
					Temp.put(gbGoodsName.getBytes());
				}
			}
			Temp.putInt(iGbGoodsIcon.get(i));
			Temp.putInt(iGbGoodsPrice.get(i));
			String gbGoodsDetail = iGbGoodsDetail.get(i);
			if(gbGoodsDetail != null) {
				int gbGoodsDetailLen = gbGoodsDetail.getBytes().length;
				Temp.putShort((short) gbGoodsDetailLen);
				if(gbGoodsDetailLen > 0) {
					Temp.put(gbGoodsDetail.getBytes());
				}
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
		shLength =Temp.getShort();
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
		iGbGoodsTotalCount = Temp.getInt();
		iStartIndex = Temp.getInt();
		iGbGoodsCount = Temp.getInt();
		for(int i = 0; i < iGbGoodsCount; i++) {
			iGbGoodsID.add(Temp.getInt());
			int gbGoodsNameLen = Temp.getShort();
			if(gbGoodsNameLen > 0) {
				byte[] gbGoodsName = new byte[gbGoodsNameLen];
				Temp.get(gbGoodsName);
				iGbGoodsName.add(new String(gbGoodsName));
				gbGoodsName = null;
			}
			iGbGoodsIcon.add(Temp.getInt());
			iGbGoodsPrice.add(Temp.getInt());
			int DetialLen = Temp.getShort();
			if(DetialLen > 0) {
				byte[] detail = new byte[DetialLen];
				Temp.get(detail);
				iGbGoodsDetail.add(new String(detail));
				detail = null;
			}
		}
		return Temp.position();
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspGoodsListGb [nResult=" + nResult + ", iMsg=" + iMsg
				+ ", iGbGoodsTotalCount=" + iGbGoodsTotalCount
				+ ", iStartIndex=" + iStartIndex + ", iGbGoodsCount="
				+ iGbGoodsCount + ", iGbGoodsID=" + iGbGoodsID
				+ ", iGbGoodsName=" + iGbGoodsName + ", iGbGoodsIcon="
				+ iGbGoodsIcon + ", iGbGoodsPrice=" + iGbGoodsPrice
				+ ", iGbGoodsDetail=" + iGbGoodsDetail + "]";
	}
}
