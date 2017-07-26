/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: TimerCtl.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-4-27 下午02:53:42
 *******************************************************************************/
package com.funoble.myarstation.socket;

import java.util.Vector;


/**
 * Timer Component
 *
 * Note:
 *  - The successful operation of this timer requires clients to execute simple, short
 *    code snippets when called back by the engine.  Otherwise the queue's delivery
 *    mechanism will be held up
 *
 * Further work:
 *  - When Thread.Interrupt is implemented we can switch from the busy wait model to
 *    the calculated wait model.  Without the interrupt the thread waits for the
 *    calculated interval before waking up.  This is a problem if another shorter
 *    request arrives.  For now we'll assume the minimum resolution of the timer is
 *    100ms.
 *
 * @version 1.0, 2 October 1995
 *
 */

public class TimerCtl {
	static TimerTasks timerTasks;
  
	public TimerCtl() {
		
	}
  
	/*
	 * Start a timer running
	 * */
	public static void startTimer(TimerClient client, int eventId, long delay, boolean repeat) {
		  // create the timer if necessary
		System.out.println("TimerCtl::startTimer() 1");
		if (timerTasks == null) {
			System.out.println("TimerCtl::startTimer() 2");
		  timerTasks = new TimerTasks();
		  timerTasks.start();
		}
		System.out.println("TimerCtl::startTimer() 3 eventId=" + eventId
				+ " delay=" + delay
				+ " rpeat=" + repeat);
	  
	  //Diagnostic.out.println("TIMER: startTimer"+eventId);
	  
	  // add the new task to the queue
		  timerTasks.add(client, eventId, delay, repeat);
		  System.out.println("TimerCtl::startTimer() 4");
	  }
	  
	  /*
	   * Stop a timer
	   */
	  public static void stopTimer(TimerClient client, int eventId) {
		  //Diagnostic.out.println("TIMER: stopTimer"+eventId);
			  if(timerTasks != null)
				  timerTasks.end(client, eventId);
		  }
	}
	
	class TimerTasks extends Thread {
		Vector tasks = new Vector();
		boolean suspended = false;
		boolean sleeping = false;
		
	/**
	 * Thread task runner
	 */
	public void run() {
		// Loop forever
	while (true) {
		long sleepTime = 0;
		
		// Ensure that the tasks class is protected
	synchronized (tasks) {
		//Diagnostic.out.println("TIMER: Tick");
	
	// Scan the job list for any jobs which may fire.
	// Mark one-shot jobs for deletion
	// Calculate the maximum time we can sleep for
	sleepTime = scan();
	
	// Delete DeletePending jobs.  DeletePending jobs result from one-shots which have
	// been sent, and repeat jobs which have been cancelled.  Jobs may have been
	// cancelled during the Scan process.
		purge();
	}
	
	// Suspend timer if necessary
	if (tasks.size() == 0) {
		//Diagnostic.out.println("TIMER: Suspend");
		try {
			synchronized(this) {
				suspended = true;
				wait();
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("TimerCtl::run()");
		}
	}
	else {
		//Diagnostic.out.println("TIMER: Suggested Sleeping for "+sleepTime);
	if (sleepTime >= 0) {
		try {
			sleeping = true;
			sleep(sleepTime);
			sleeping = false;
		}
		catch (InterruptedException i) {
			//Diagnostic.out.println("TIMER: Caught me napping");
					}
				}
			}
		}
	}
	
	/**
	 * Add a new task
	 */
	public void add(TimerClient client, int eventId, long delay, boolean repeat) {
		TimerTask t = new TimerTask(client, eventId, delay, repeat);
		
		synchronized (tasks) {
			tasks.addElement((Object)t);
		}
	
		if (suspended) {
			synchronized(this) {
				notify();
				//Diagnostic.out.println("TIMER: Resume");
				suspended = false;
			}
		}
	}
	
	/**
	 * Find the job and mark it for deletion
	 */
	public void end(TimerClient client, int eventId) {
		synchronized (tasks) {
			for (int i = 0; i < tasks.size(); i++) {
				TimerTask t = (TimerTask)tasks.elementAt(i);
				
				//if (!t.deletePending && t.client == client && t.eventId == eventId)
	if (t.deletePending == false && t.client == client && t.eventId == eventId) {
		// JPBS - if we don't reset 'repeat', deletePending will be set again
					t.repeat = false;
					t.deletePending = true;
					break;
				}
			}
		}
	}
	
	/**
	 * Clear out all the dead wood
	 */
	void purge() {
		for (int i = 0; i < tasks.size(); i++) {
			TimerTask t = (TimerTask)tasks.elementAt(i);
			
			if (t.deletePending) {
				//Diagnostic.out.println("TIMER: purged");
				
				tasks.removeElementAt(i);
				i--;
			}
		}
	}
	
	long scan() {
		// The value added to the current time determines the MAX time until
	// the next scan
	// This is 100 now since thread.interrupt() is not implemented
	long nextTime = System.currentTimeMillis() + 100;
	
	for (int i = 0; i < tasks.size(); i++) {
		TimerTask t = (TimerTask)tasks.elementAt(i);
		
		// if not already deletePending, test (and possibly send the event)
	// as a result, the job may be flagged for deletion.
	// May also be a non-repeating job and so require self deletion
	if (!t.deletePending)
		t.test();
	
	// if the task didn't get deleted - see what it contributes to the time
	if (!t.deletePending)
		nextTime = Math.min(nextTime, t.timeNext);
	
	//Diagnostic.out.println("TIMER: Scanning "+t.eventId+" "+(t.deletePending == true ? "DEL" : ""));
			}
			
			return nextTime - System.currentTimeMillis();
		}
	}
	
	class TimerTask
	{
		TimerClient client;
		int         eventId;
		
		long        timePrev;
		long        timeDelay;
		long        timeNext;
		
		boolean repeat;
		boolean deletePending;
		
		public TimerTask(TimerClient client, int eventId, long timeDelay, boolean repeat) {
			this.client = client;
			this.eventId = eventId;
			this.timeDelay = timeDelay;
			this.repeat = repeat;
			
			// schedule the next click - now + delay
	timeNext = System.currentTimeMillis() + timeDelay;
	deletePending = false;
	
	//Diagnostic.out.println("TIMER: Adding New Task");
	}
	
	public void test() {
		if (System.currentTimeMillis() >= timeNext) {
			//Diagnostic.out.println("TIMER: fire");
	
	// Fire the event
	client.timerEvent(eventId);
	
	// Update the next time
			timeNext = System.currentTimeMillis() + timeDelay;
			
			deletePending = !repeat;
		}
	}
}
