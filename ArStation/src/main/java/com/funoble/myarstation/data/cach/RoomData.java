/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: RoomData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2013-9-13 下午02:47:02
 *******************************************************************************/
package com.funoble.myarstation.data.cach;


public class RoomData {
	public static final int TYPE_CLASSICAL = 5;//普通房
	public static final int TYPE_UNITE = 6;//同盟房
	
	public int roomID;
	public int tableID;
	public int roomType;
	/**
	 * construct
	 * @param roomID
	 * @param tableID
	 * @param roomType
	 */
	public RoomData() {
		this.roomID = -1;
		this.tableID = -1;
		this.roomType = TYPE_CLASSICAL;
	}
	/**
	 * construct
	 * @param roomID
	 * @param tableID
	 * @param roomType
	 */
	public RoomData(int roomID, int tableID, int roomType) {
		this.roomID = roomID;
		this.tableID = tableID;
		this.roomType = roomType;
	}
	
	public RoomData(RoomData data) {
		this.roomID = data.roomID;
		this.tableID = data.tableID;
		this.roomType = data.roomType;
	}
	
}
