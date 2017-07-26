/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: Sound.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-14 下午09:05:32
 *******************************************************************************/
package com.funoble.myarstation.sound;

import java.util.HashMap;


public class CallPointdHashMap {
	public HashMap<Integer, Integer>  iCount;
	public HashMap<Integer, Integer>  iPointCount;
	
	public CallPointdHashMap(HashMap<Integer, Integer> aCount, HashMap<Integer, Integer> aPointCount) {
		iCount = aCount;
		iPointCount = aPointCount;
	}
	
	private int getCount(int aKey) {
		Integer count = iCount.get(aKey);
		if(count == null) {
			return 0;
		}
		return count;
	}
	
	private int getPointCount(int aKey) {
		Integer pointCOunt = iPointCount.get(aKey);
		if(pointCOunt == null) {
			return 0;
		}
		return pointCOunt;
	}
	
	public CallPoint getCallContent(int aCountKey, int aPointCountKey) {
		if(getPointCount(aPointCountKey) == 0 || getCount(aCountKey) == 0) {
			return null;
		}
		return new CallPoint(getCount(aCountKey), getPointCount(aPointCountKey));
	}
}
