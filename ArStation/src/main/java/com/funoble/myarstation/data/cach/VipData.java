/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: VipData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2013-7-26 下午05:22:18
 *******************************************************************************/
package com.funoble.myarstation.data.cach;



public class VipData {
	public int ID;
	public int roomID;
	public int tableID;
	public String roomLimit;
	public int status;
	
	/**
	 * construct
	 * @param roomID
	 * @param tableID
	 * @param roomLimit
	 */
	public VipData() {
		this.ID = 0;
		this.roomID = 0;
		this.tableID = 0;
		this.roomLimit = "";
		status = 0;
		
	}


	/**
	 * construct
	 * @param iD
	 * @param roomID
	 * @param tableID
	 * @param roomLimit
	 */
	public VipData(int iD, int roomID, int tableID, String roomLimit, int status) {
		super();
		ID = iD;
		this.roomID = roomID;
		this.tableID = tableID;
		this.roomLimit = roomLimit;
		this.status = status;
	}
}