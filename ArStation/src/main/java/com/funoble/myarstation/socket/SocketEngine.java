package com.funoble.myarstation.socket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import android.text.format.Time;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.common.Util;


/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: SocketEx.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-4-27 下午02:36:38
 *******************************************************************************/

public class SocketEngine implements Runnable {
	public static final boolean isdebug = false;//调试

	/**
	 * 以下实现不要改动!!!!
	 */
	private Thread NotifyThread=null;
	private boolean IsRunning = false;
	private Socket thisSocket = null;
	private static final int BUFLEN = 4097;
	private	byte buf[] = new byte[BUFLEN];
	private	BufferedInputStream biStream = null;
	private	SocketHandler seh=null;
	
	/**
	 *构造函数.
	 */
	public SocketEngine(SocketHandler seh,Socket ClientSocket){
		this.seh = seh;thisSocket = ClientSocket;	
		InitNotify();
	}
	
	public SocketEngine(SocketHandler seh,String host,int port) throws IOException {
		this.seh = seh;thisSocket = new Socket(host,port);
		InitNotify();
	}
	
	public SocketEngine(SocketHandler seh, InetAddress address, int port ) throws IOException {
		this.seh = seh;thisSocket = new Socket(address, port);
		InitNotify();
	}
	
	public SocketEngine(SocketHandler seh, String host, int port, InetAddress localAddr, int localPort ) throws IOException {
		this.seh = seh;
		thisSocket = new Socket(host,port,localAddr,localPort );
		InitNotify();
	}
	
	public SocketEngine(SocketHandler seh, InetAddress address, int port, InetAddress localAddr, int localPort ) throws IOException {
		this.seh = seh;thisSocket = new Socket(address, port, localAddr,localPort );
		InitNotify();
	}
	
	/**
	 * 实现Socket的可见方法.
	 */
	public synchronized void close() throws IOException	{
		IsRunning = false;
		Tools.debug("thisSocket" + thisSocket);
		if(thisSocket != null) {
			Tools.debug("thisSocket.close()");
//			thisSocket.shutdownInput();
//			thisSocket.shutdownOutput();
			thisSocket.close();
		}
	}
	
	public InetAddress getInetAddress() {
		return thisSocket.getInetAddress();
	}
	
	public InputStream getInputStream() throws IOException{
		return thisSocket.getInputStream(); 
	}
	
	public InetAddress getLocalAddress() {	
		return thisSocket.getLocalAddress();
	}
	
	public int getLocalPort() {	
		return thisSocket.getLocalPort(); 
	}
	
	public OutputStream getOutputStream() throws IOException{
		return thisSocket.getOutputStream();
	}
	
	public int getPort() {	
		return thisSocket.getPort(); 
	}
	
	public int getSoLinger() throws SocketException{	
		return thisSocket.getSoLinger();
	}
	
	public synchronized int getSoTimeout() throws SocketException {	
		return thisSocket.getSoTimeout();
	}
	
	public boolean getTcpNoDelay() throws SocketException {	
		return thisSocket.getTcpNoDelay(); 
	}
	
	public void setSoLinger( boolean on, int val ) throws SocketException {	
		thisSocket.setSoLinger(on,val); 
	}
	
	public synchronized void setSoTimeout( int timeout ) throws SocketException {
		thisSocket.setSoTimeout( timeout ); 
	}
	
	public void setTcpNoDelay( boolean on ) throws SocketException {
		thisSocket.setTcpNoDelay(on); 
	}
	
	public String toString() {	
		return thisSocket.toString(); 
	}
	
	public boolean isClosed() {
		return thisSocket.isClosed();
	}
	
	public boolean isConnected() {
		return thisSocket.isConnected();
	}
	/**
	 * 获取Socket
	 */
	public Socket GetSocket() {
		return thisSocket;
	}
	
	/**
	 * 初始化异步Socket
	 */
	private void ShowMsg(String Msg)
	{
		if(isdebug)
			System.out.println(Msg);
	}
	
	private void InitNotify()
	{
		if(NotifyThread != null) return ;
		try{
			biStream = new BufferedInputStream(getInputStream());
			thisSocket.setSoTimeout(0);
  		}catch(IOException e){
			ShowMsg("SocketEx InitNotify() IOException.");
		}
		IsRunning = true;
		NotifyThread = new Thread(this,"SocketEx_NoitfyThread");
		NotifyThread.setDaemon(true);
		NotifyThread.start();
//		if(seh !=null)
//			seh.OnConnect(this);
		DoConnect();
	}
	
	/**
	 * 关闭Socket
	 */
	private void Close()
	{
		try{
			close();
		}catch(Exception eclose){
			ShowMsg("SocketEx Close() Exception." + eclose.toString());
		}
	}
	
	protected void finalize() throws Throwable
	{
		Close();
		super.finalize();
	}
	
	/**
	 * Thread 运行
	 */
	public void run()
	{
		//最大多少秒，连接上收不到数据提示用户，重新登录
		int maxIdleTimeInSeconds = 60*1000;
		//最大多少秒，连接上收不到数据提示用户，选择重连
		int hint2TimeInSeconds = 60 * 1000;
		//多长时间没有收到任何数据，提示用户
		int hintTimeInSeconds = 30 * 1000;
		long lastHintUserTime;
		long lastReciveDataTime;
		long now;
		lastReciveDataTime = System.currentTimeMillis();
		lastHintUserTime = lastReciveDataTime;
		while(IsRunning){
			if(this.isClosed() == true && this.isConnected() == false) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				continue;
			}
			try{
				//三秒超时
				setSoTimeout(5 * 1000);
				if(getInputStream().read(buf,0,1) <= 0)//试读一个字节
				{
					DoClose();
					return ;
				}
				if(!DoReceive(getInputStream().available()))
				{
					return ;
				}
				else {
					lastReciveDataTime = System.currentTimeMillis();
				}

			}
			catch (SocketTimeoutException e) {
				if (!isClosed() && isConnected()) {
					now = System.currentTimeMillis();
					if(now - lastReciveDataTime > maxIdleTimeInSeconds && 
							now - lastHintUserTime > hintTimeInSeconds) {
						//网络异常
						DoClose();
						return;
					}
					else if(now - lastReciveDataTime > hint2TimeInSeconds &&
							now - lastHintUserTime > hintTimeInSeconds) {
						Tools.debug("SocketEngine::run():OnTimeOut");
						//超时
						if(seh != null) {
							seh.OnTimeOut(this);
						}
					}
					else if(now - lastReciveDataTime > hintTimeInSeconds && 
							now - lastHintUserTime > hintTimeInSeconds) {
					}
					else {
						lastHintUserTime = now;
						lastReciveDataTime = now;
					} 
				}
				else {
					Close();
				}
			}
			catch(Exception e){
//				e.printStackTrace();
				ShowMsg("SocketEx run() Exception.");
				DoClose();
				return ;
			}
			try{
				Thread.sleep(200); //
			}catch(InterruptedException e){
				ShowMsg("SocketEx run() InterruptedException.");
				DoClose();
				return ;
			}
		}
	}

	/**
	 * 当有数据到达时的回调方法.
	 */
	private boolean DoReceive(int nCanReadCount)
	{
		try{
			int len = 0,nCurrReadCount=0,nStart=1;
		//	do{
//				for(int i=nStart;i< BUFLEN;i++) {
//					buf[i]=0;
//				}
//				if(nCanReadCount == 0)
//				{
//					Tools.debug("[buf[i]=0] = " + Util.bytesToHexString(buf));
//					if(seh !=null)
//						seh.OnReceive(this,buf,nStart);
//					return true;
//				}
//				if(nCanReadCount >(BUFLEN-2))
//				{
//					nCurrReadCount = BUFLEN-2;
//				}
//				else
//				{
//					nCurrReadCount = nCanReadCount;
//				}
				byte data[] = new byte[nCanReadCount + 1];
				data[0] = buf[0];
				len = biStream.read(data,1,nCanReadCount);
				Tools.debug("SocketEngine::DoReceive()::data len = " + data.length + " biStream.read = " + Util.bytesToHexString(data));
				
				if(len == 0)
				{
					DoClose();
					return false;
				}
//				nCanReadCount -= len;
//				buf[len+nStart] = 0;
				//解码
				if(seh !=null) {
					seh.OnReceive(this,data,nCanReadCount+1);
				}
	//			nStart = 0;
	//		}while(nCanReadCount >0);
	    }catch(Exception excpt){
	    	excpt.printStackTrace();
			ShowMsg("SocketEx DoReceive() Exception." + excpt.toString());
			DoClose();
			return false;
        }
		return true;
	}

	/**
	 * 当Socket建立连接时的回调方法.
	 */
	private void DoConnect()
	{
		Tools.debug("SocketEngine::DoConnect()");
		if(seh !=null)
			seh.OnConnect(this);
	}
	
	/**
	 * 当Socket关闭时的回调方法.
	 */
	private void DoClose() {
		try{
			Tools.debug("SocketEngine::DoClose()");
			  if(IsRunning) {
			    Close();
			    if(seh !=null)
			      seh.OnClose(this);
			    IsRunning = false;
			  }
			}catch(Exception e){
				e.printStackTrace();
				ShowMsg("SocketEx DoClose() Exception.");
		}
	}
}
