/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: GiveGiftData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014年4月18日 下午7:13:32
 *******************************************************************************/
package com.funoble.myarstation.store.data;


import java.sql.Date;
import java.text.SimpleDateFormat;

import com.funoble.myarstation.common.Tools;


public class GiveGiftData {
	public int id;
	public int index;//临时索引
	public String nick;
	public long time;
	public int cham;
	
	
	/**
	 * construct
	 * @param id
	 * @param index
	 * @param nick
	 * @param time
	 * @param cham
	 */
	public GiveGiftData(int id, int index, String nick, long time, int cham) {
		this.id = id;
		this.index = index;
		this.nick = nick;
		this.time = time;
		this.cham = cham;
	}
	
	public String toString() {
		long _timestamp = time * 1000;
		Date now = new Date(_timestamp);
		SimpleDateFormat temp=new SimpleDateFormat("MM月dd日");
		String str = temp.format(now);
		return str + ",您收到了【"+nick+"】赠送的礼物，魅力值增加"+cham+"点";
	}

	/**
	 * construct
	 */
	public GiveGiftData() {
		this.id = -1;
		this.index = -1;
		this.nick = "";
		this.time = 0;
		this.cham = 0;
	}
}
