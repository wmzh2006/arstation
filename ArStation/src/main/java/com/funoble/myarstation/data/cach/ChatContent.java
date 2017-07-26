/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: ChatContent.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-11-12 下午04:54:09
 *******************************************************************************/
package com.funoble.myarstation.data.cach;

import android.R.integer;



public class ChatContent {
	public String name;
	public String time;
	public String content;
	public String index;
	public boolean edit = true;
	public boolean ibUserWords = false;
	public boolean ibSystemMessage = false;
	
	public ChatContent() {
		name = "";
		time = "";
		content = "";
		index = "";
		edit = true;
	}
	/**
	 * construct
	 * @param name
	 * @param content
	 */
	public ChatContent(String name, String content) {
		super();
		this.name = name;
		this.content = content;
		this.index = "";
	}
	
	/**
	 * construct
	 * @param name
	 * @param content
	 */
	public ChatContent(String name, String content, String index) {
		super();
		this.name = name;
		this.content = content;
		this.index = index;
	}
	
	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ChatContent [name=" + name + ", time=" + time + ", content="
				+ content + ", index=" + index + ", ibUserWords=" + ibUserWords
				+ ", ibSystemMessage=" + ibSystemMessage + "]";
	}
}