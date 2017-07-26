/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: AnimFormatData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014年4月2日 下午10:40:33
 *******************************************************************************/
package com.funoble.myarstation.store.data;

import com.funoble.myarstation.common.Tools;


public class AnimFormatData {
	public String		szPak;			//Pak名称
	public int		nStartType;			//开始动画位置类型(0 --- 位置  1 --- 坐标)
	public int		nX1;				//起始X坐标	(位置：0 ~ 5 表示玩家座位， 6 ~ 14 表示屏幕的左上，上，右上，左，中，右，左下，下，右下)
	public int		nY1;				//起始Y坐标
	public int		nEndType;			//结束动画位置类型(0 --- 位置  1 --- 坐标）
	public int		nX2;				//结束X坐标	
	public int		nY2;				//结束Y坐标
	public int		nStartID;			//开始动画ID
	public int		nStartMotion;		//开始动画动作
	public int		nEndID;				//结束动画ID
	public int		nEndMotion;			//结束动画动作
	
	
	/**
	 * construct
	 * @param szPak
	 * @param nStartType
	 * @param nX1
	 * @param nY1
	 * @param nEndType
	 * @param nX2
	 * @param nY2
	 * @param nStartID
	 * @param nStartMotion
	 * @param nEndID
	 * @param nEndMotion
	 */
	public void SetAnimFormatData(String szPak, int nStartType,
			int nX1, int nY1, int nEndType, int nX2, int nY2,
			int nStartID, int nStartMotion, int nEndID, int nEndMotion) {
		this.szPak = szPak;
		this.nStartType = nStartType;
		this.nX1 = nX1;
		this.nY1 = nY1;
		this.nEndType = nEndType;
		this.nX2 = nX2;
		this.nY2 = nY2;
		this.nStartID = nStartID;
		this.nStartMotion = nStartMotion;
		this.nEndID = nEndID;
		this.nEndMotion = nEndMotion;
	}
	/**
	 * @param szPak
	 * @param nStartType
	 * @param nX1
	 * @param nY1
	 * @param nEndType
	 * @param nX2
	 * @param nY2
	 * @param nStartID
	 * @param nStartMotion
	 * @param nEndID
	 * @param nEndMotion
	 */
	public AnimFormatData(String formatString) {
		if(formatString == null) {
			SetAnimFormatData("", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		}
		else {
			String [] dataStrings = formatString.split("\\|");
			if(dataStrings != null && dataStrings.length == 11) {
				SetAnimFormatData(
						dataStrings[0],
						Short.valueOf(dataStrings[1]),
						Short.valueOf(dataStrings[2]),
						Short.valueOf(dataStrings[3]),
						Short.valueOf(dataStrings[4]),
						Short.valueOf(dataStrings[5]),
						Short.valueOf(dataStrings[6]),
						Short.valueOf(dataStrings[7]),
						Short.valueOf(dataStrings[8]),
						Short.valueOf(dataStrings[9]),
						Short.valueOf(dataStrings[10])
						);
				//Tools.debug("AnimFormatData::AnimFormatData() toString=" + toString());
			}
			else {
				SetAnimFormatData("", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			}
		}
	}
	/**
	 * construct
	 * @param szPak
	 * @param nStartType
	 * @param nX1
	 * @param nY1
	 * @param nEndType
	 * @param nX2
	 * @param nY2
	 * @param nStartID
	 * @param nStartMotion
	 * @param nEndID
	 * @param nEndMotion
	 */
	public AnimFormatData() {
		SetAnimFormatData("", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AnimFormatData [szPak=" + szPak + ", nStartType=" + nStartType
				+ ", nX1=" + nX1 + ", nY1=" + nY1 + ", nEndType=" + nEndType
				+ ", nX2=" + nX2 + ", nY2=" + nY2 + ", nStartID=" + nStartID
				+ ", nStartMotion=" + nStartMotion + ", nEndID=" + nEndID
				+ ", nEndMotion=" + nEndMotion + "]";
	}
	
}


