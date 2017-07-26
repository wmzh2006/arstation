/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: GiftUtil.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014年4月17日 下午4:16:20
 *******************************************************************************/
package com.funoble.myarstation.utils;


public class GiftUtil {
	public static final int GIFTID_1 = 29;
	public static final int GIFTID_2 = GIFTID_1 + 1;
	public static final int GIFTID_3 = GIFTID_1 + 2;
	public static final int GIFTID_4 = GIFTID_1 + 3;
	public static final int GIFTID_5 = GIFTID_1 + 4;
	public static final int GIFTID_6 = GIFTID_1 + 5;
	public static final int GIFTID_7 = GIFTID_1 + 6;
	public static final int GIFTID_8 = GIFTID_1 + 7;
	public static final int GIFTID_9 = GIFTID_1 + 8;
	
	public static float giftPaintLeft = 20 * ActivityUtil.ZOOM_X;
	public static float giftPaintTop = 319 * ActivityUtil.ZOOM_Y;
	public static float giftPaintRight = 356 * ActivityUtil.ZOOM_X;
	public static float giftPaintBottom = 368 * ActivityUtil.ZOOM_Y;
	
	public static float giftSpaceX = 50 * ActivityUtil.ZOOM_X;
	public static float giftSpaceY = 50 * ActivityUtil.ZOOM_Y;
	public static int giftMaxRow = (int) (giftPaintRight / giftSpaceX);
	public static int giftMaxCol = (int) ((giftPaintBottom - giftPaintTop) / giftSpaceY);
	
}

