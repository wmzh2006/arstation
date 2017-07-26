/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: ActivityData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-9-8 下午05:45:53
 *******************************************************************************/
package com.funoble.myarstation.store.data;


public class ActivityData {
	public int 		acitivityID;
	public String 	activityName;
	public String 	activityTime;
	public int	  	activitynumber;
	public int 		activityStatus;
	public String 	activityStatusText;
	public int 		aFB;
	public int 		aGB;
	public int 		aLevel;
	
	public ActivityData() {
		super();
		this.acitivityID = 0;
		this.activityName = "";
		this.activityTime = "";
		this.activityStatusText = "";
		this.activitynumber = 0;
		this.activityStatus = 0;
		this.aFB = 0;
		this.aGB = 0;
		this.aLevel = 0;
	}
	/**
	 * construct
	 * @param acitivityID
	 * @param activityName
	 * @param activityTime
	 * @param number
	 * @param activityStatus
	 */
	public ActivityData(int acitivityID, String activityName,
			String activityTime, int number, int activityStatus,
			int aFB, int aGB, int aLevel, String aActivityStatusText) {
		super();
		this.acitivityID = acitivityID;
		this.activityName = activityName;
		this.activityTime = activityTime;
		this.activitynumber = number;
		this.activityStatus = activityStatus;
		this.aFB = aFB;
		this.aGB = aGB;
		this.aLevel = aLevel;
		this.activityStatusText = aActivityStatusText;
	}
}
