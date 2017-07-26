/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MBReqLeaveMsg.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-23 上午10:33:34
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;



public class MBRspAgreeFriend extends MBRspLeaveMsgB {

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBRspAgreeFriend [iResult=" + iResult + ", iMsg=" + iMsg
				+ ", iLeaveMsgID=" + iLeaveMsgID + ", iUID=" + iUID
				+ ", iNick=" + iNick + ", iPic=" + iPic + ", iTime=" + iTime
				+ ", iText=" + iText + ", iSecret=" + iType + "]";
	}
	
}
