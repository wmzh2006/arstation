/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: Person.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2013-3-4 下午06:25:00
 *******************************************************************************/
package com.funoble.myarstation.phrase;


public class Phrase {
	private String word;
	private Boolean edit;
	
	public Phrase() {
		
	}
	/**
	 * construct
	 * @param id
	 * @param word
	 */
	public Phrase(String word, Boolean edit) {
		this.word = word;
		this.edit = edit;
	}
	/**
	 * return word : return the property word.
	 */
	public String getWord() {
		return word;
	}
	/**
	 * param word : set the property word.
	 */
	public void setWord(String word) {
		this.word = word.trim();
	}
	/**
	 * return edit : return the property edit.
	 */
	public Boolean getEdit() {
		return edit;
	}
	/**
	 * param edit : set the property edit.
	 */
	public void setEdit(Boolean edit) {
		this.edit = edit;
	}
	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Phrase [word=" + word + ", edit=" + edit + "]";
	}
}
