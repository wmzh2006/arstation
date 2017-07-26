/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: Heart.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-20 下午07:18:22
 *******************************************************************************/
package com.funoble.myarstation.data.cach;

import java.util.Timer;
import java.util.TimerTask;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.socket.protocol.ProtocolType.SocketErrorType;


public class Heart {
	private Timer					iSendTimer;			//发送计时器
	private TimerTask 				mTimerTask;
	private long					time;
	private static Heart 			iHeart = null;
	
	public static Heart getInstance() {
		if(iHeart == null) {
			iHeart = new Heart();
		}
		return iHeart;
	}
	
	
    /**
	 * construct
	 */
	public Heart() {
		iSendTimer = null;
		mTimerTask = null;
		time = 0;
	}

	/**
     * @param aDelay second
     * @param period second
     */
    public void startTimer(long aDelaySecond, long periodSecond){  
    	stopTimer();
        if (iSendTimer == null) {  
            iSendTimer = new Timer();  
        }  
        if (mTimerTask == null) {  
            mTimerTask = new TimerTask() {  
                @Override  
                public void run() {  
                	if(MyArStation.iGameProtocol.isConnect()){
                		MyArStation.iGameProtocol.requsetHeart(System.currentTimeMillis());
                	}
                }  
            };  
        }  
  
        if(iSendTimer != null && mTimerTask != null ) {
        	time = System.currentTimeMillis();
        	Tools.debug("start time = " + time); 
        	iSendTimer.scheduleAtFixedRate(mTimerTask, aDelaySecond * 1000, periodSecond * 1000);  
        }
  
    }  
  
    private void stopTimer() {  
    	Tools.debug("stopTimer time over = " + (System.currentTimeMillis() - time ));
    	time = System.currentTimeMillis();
        if (iSendTimer != null) {  
            iSendTimer.cancel();  
            iSendTimer = null;  
        }  
        if (mTimerTask != null) {  
            mTimerTask.cancel();  
            mTimerTask = null;  
        }     
    }  
}
