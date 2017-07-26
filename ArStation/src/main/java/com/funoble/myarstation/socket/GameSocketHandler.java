/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: DemoAppSocketHandler.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-4-27 下午03:00:13
 *******************************************************************************/
package com.funoble.myarstation.socket;

import java.util.Vector;



public interface GameSocketHandler {
	//当读写数据出错出发
	public void OnReportError(Object socket, int aErrorCode);
	//当客户端sock有数据到达时触发
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID);
	//当客户端sock连接建立成功时触发
	public void OnConnect(Object socket);
	//当sock关闭时触发
	public void OnClose(Object socket);
}
