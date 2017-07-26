/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: MyGallery.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-6 下午04:18:25
 *******************************************************************************/
package com.funoble.myarstation.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.view.MessageEventID;


public class MyGallery extends Gallery {

	float scale = MyArStation.getInstance().getResources().getDisplayMetrics().density;
	int FLINGTHRESHOLD = (int) (20.0f * scale + 0.5f); 
	int SPEED = 100;
	private int sign = 0;
	public AlertDialog dialog;
	/**
	 * construct
	 * @param context
	 */
	public MyGallery(Context context) {
		super(context);

	}

	/**
	 * construct
	 * @param context
	 * @param attrs
	 */
	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	/**
	 * construct
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MyGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	/* 
	 * @see android.widget.Gallery#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
//		if (velocityX>FLINGTHRESHOLD) { 
//			return super.onFling(e1, e2, SPEED, velocityY); 
//		} else if (velocityX<-FLINGTHRESHOLD) { 
//			return super.onFling(e1, e2, -SPEED, velocityY); 
//		} else { 
//			return super.onFling(e1, e2, velocityX, velocityY); 
//		}	
		int kEvent;
        if(isScrollingLeft(e1, e2)){
            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
            sign = 0;
        } else{
            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
            if (getLastVisiblePosition() + 1 == getCount()) {
                if(sign != 0){
                	showConfirmBuy();
                }
                sign = 1;
            }
        }
        onKeyDown(kEvent, null);
        return true;   
	}
	
	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {  
        return e2.getX() > e1.getX();  
    } 
	
	private void showConfirmBuy() {
		dialog = new AlertDialog.Builder(MyArStation.getInstance()).setMessage(
				MyArStation.getInstance().getString(R.string.Tutorial_Conntent)).setCancelable(false)
				.setPositiveButton(R.string.Tutorial_Start, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) {
						messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
						MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					}
				}).setNegativeButton(R.string.Tutorial_Agian, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					}
				}).create();
		dialog.show();
	}
	
	private void messageEvent(int aEventID) {
		Message tMes = new Message();
		tMes.what = aEventID;
		MyArStation.getInstance().iHandler.sendMessage(tMes);
	}
}
