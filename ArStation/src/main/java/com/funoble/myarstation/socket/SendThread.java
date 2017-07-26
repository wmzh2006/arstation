/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: SendThread.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2013-9-29 下午05:53:45
 *******************************************************************************/
package com.funoble.myarstation.socket;

import java.util.LinkedList;
import java.util.Queue;

public class SendThread extends Thread {
	private boolean isSleep;
	private boolean isRunning;
	private Queue<byte[]> queue;
	private SocketSend send;
	/**
	 * construct
	 * @param isSleep
	 * @param isRunning
	 * @param queue
	 */
	public SendThread(SocketSend send) {
		super("SendThread");
		this.isSleep = true;
		this.isRunning = false;
		this.queue = new LinkedList<byte[]>();
		this.send = send;
	}
	
	public Boolean SendOrder(byte[] Order){
		synchronized (queue) {
			queue.offer(Order);
		}
		return true;
	}
	/* 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		byte[] data;
		 while(isRunning) {
			 if(isSleep) {
				 try {
					 Thread.sleep(10);
//					 Tools.debug("Thread.sleep(1)");
				 } catch (InterruptedException e) {
					 e.printStackTrace();
					 
				 } 
			 }
			 else {
				 synchronized (queue) {
					if ((data = queue.poll()) != null) {
						if (send != null)
							send.OnThreadSend(data, data.length);
						try {
							if(queue.size() == 0) {
								Thread.sleep(100);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();

						}
					}
				} 
			 }
//			 Tools.debug(this.getName() + " runnng..");
		 }
	}
	
	public void Sleep(boolean sleep) {
		synchronized (this) {
			isSleep = sleep;
		}
	}

	/* 
	 * @see java.lang.Thread#start()
	 */
	@Override
	public synchronized void start() {
		isRunning = true;
		super.start();
	}
		public void Stop() {
		synchronized (this) {
			isSleep = true;
		}
	}
	
	public void purse() {
		synchronized (queue) {
			queue.clear();
		}
	}
}
