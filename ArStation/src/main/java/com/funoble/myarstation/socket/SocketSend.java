/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: SocketSend.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2013-9-29 下午08:28:40
 *******************************************************************************/
package com.funoble.myarstation.socket;


public interface SocketSend {
	boolean OnThreadSend(byte[] data, int len);
}
