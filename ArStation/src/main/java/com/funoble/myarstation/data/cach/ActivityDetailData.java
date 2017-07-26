/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: ActivityDetailData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-9-10 下午12:10:30
 *******************************************************************************/
package com.funoble.myarstation.data.cach;

import java.util.ArrayList;
import java.util.List;

import com.funoble.myarstation.store.data.StockData;


public class ActivityDetailData {
	public int				activityID;
	public String			content;
	public List<StockData>	list;
	/**
	 * construct
	 * @param activityID
	 * @param content
	 * @param list
	 */
	public ActivityDetailData(int activityID, String content,
			List<StockData> list) {
		this.activityID = activityID;
		this.content = content;
		this.list = new ArrayList<StockData>();
		this.list.addAll(list);
	}
	
	public ActivityDetailData() {
		this.activityID = 0;
		this.content = "";
		this.list = new ArrayList<StockData>();
	}
}
