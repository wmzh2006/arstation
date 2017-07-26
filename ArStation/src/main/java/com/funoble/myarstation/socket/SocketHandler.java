/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: SocketExHandler.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-4-27 下午02:33:34
 *******************************************************************************/
package com.funoble.myarstation.socket;


/**
 * 异步Socket Server Interface
 * 使用方法：
 * 1.定义类 MySocketServerEx 实现SocketExHandler接口,实现 OnReceive OnListen  OnClose OnAccept  事件
 * 2.在类中实现start方法 MySocketEx = new ServerSocketEx(this,ip,port)
 * 3.在类中实现stop方法 delete MySocketEx
 * 4.当开始监听时会触发OnListen事件
 * 5.当SOCKET关闭时会触发OnClose事件
 * 6.当有客户端SOCKET要建立连接时会触发OnAccept事件
 */

public interface SocketHandler {
	//当读写数据出错出发
	public void OnReportError(Object socket, int aErrorCode);
	//当客户端sock数据到达时触发
	public void OnReceive(Object socket,byte buf[],int nLen);
	//当客户端sock连接建立成功时触发
	public void OnConnect(Object socket);
	//当sock关闭时触发
	public void OnClose(Object socket);
	//超时
	public void OnTimeOut(Object socket);
}
