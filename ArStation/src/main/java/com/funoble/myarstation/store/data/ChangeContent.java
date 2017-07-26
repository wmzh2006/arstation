/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: ChangeContent.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-31 下午10:35:12
 *******************************************************************************/
package com.funoble.myarstation.store.data;


public class ChangeContent {
	private String iProductPrice;
	private String iProductName;
	private String iProductDes;
	/**
	 * construct
	 */
	public ChangeContent() {
		iProductPrice = "";
		iProductName = "funoble";
		iProductDes = "";
	}
	/**
	 * return iProductPrice : return the property iProductPrice.
	 */
	public String getProductPrice() {
		return iProductPrice;
	}
	/**
	 * param iProductPrice : set the property iProductPrice.
	 */
	public void setProductPrice(String iProductPrice) {
		this.iProductPrice = iProductPrice;
	}
	/**
	 * return iProductName : return the property iProductName.
	 */
	public String getProductName() {
		return iProductName;
	}
	/**
	 * param iProductName : set the property iProductName.
	 */
	public void setProductName(String iProductName) {
		this.iProductName = iProductName;
	}
	/**
	 * return iProductDes : return the property iProductDes.
	 */
	public String getProductDes() {
		return iProductDes;
	}
	/**
	 * param iProductDes : set the property iProductDes.
	 */
	public void setProductDes(String iProductDes) {
		this.iProductDes = iProductDes;
	}

	
}
