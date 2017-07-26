/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: ManagerPopView.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014年3月22日 下午8:49:26
 *******************************************************************************/
package com.funoble.myarstation.game;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.funoble.myarstation.screen.BasePopScreen;
import com.funoble.myarstation.view.GameView;


public class ManagerPopView extends GameView {

	ArrayList<BasePopScreen> popScreens;
	/* 
	 * @see com.funoble.lyingdice.view.GameView#init()
	 */
	@Override
	public void init() {
		popScreens = new ArrayList<BasePopScreen>();
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#releaseResource()
	 */
	@Override
	public void releaseResource() {
		for(int i = 0; i < popScreens.size(); i++) {
			popScreens.get(i).releaseResource();
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#paintScreen(android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	public void paintScreen(Canvas g, Paint paint) {
		for(int i = 0; i < popScreens.size(); i++) {
			popScreens.get(i).paintScreen(g, paint);;
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#performL()
	 */
	@Override
	public void performL() {
		for(int i = 0; i < popScreens.size(); i++) {
			popScreens.get(i).performL();
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		for(int i = 0; i < popScreens.size(); i++) {
			if(popScreens.get(i).onTouchEvent(event)) {
				return true;
			}
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		for(int i = 0; i < popScreens.size(); i++) {
			if(popScreens.get(i).onKeyDown(keyCode, event)) {
				return true;
			}
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onDown(android.view.MotionEvent)
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		for(int i = 0; i < popScreens.size(); i++) {
			if(popScreens.get(i).onDown(e)) {
				return true;
			}
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onLongPress(android.view.MotionEvent)
	 */
	@Override
	public boolean onLongPress(MotionEvent e) {
		for(int i = 0; i < popScreens.size(); i++) {
			if(popScreens.get(i).onLongPress(e)) {
				return true;
			}
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onSingleTapUp(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		for(int i = 0; i < popScreens.size(); i++) {
			if(popScreens.get(i).onSingleTapUp(e)) {
				return true;
			}
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#pointerPressed(int, int)
	 */
	@Override
	public boolean pointerPressed(int aX, int aY) {
		for(int i = 0; i < popScreens.size(); i++) {
			if(popScreens.get(i).pointerPressed(aX, aY)) {
				return true;
			}
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#pointerReleased(int, int)
	 */
	@Override
	public boolean pointerReleased(int aX, int aY) {
		for(int i = 0; i < popScreens.size(); i++) {
			if(popScreens.get(i).pointerPressed(aX, aY)) {
				return true;
			}
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		for(int i = 0; i < popScreens.size(); i++) {
			if(popScreens.get(i).onFling(e1, e2, velocityX, velocityY)) {
				return true;
			}
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		for(int i = 0; i < popScreens.size(); i++) {
			popScreens.get(i).handleMessage(msg);
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#OnProcessCmd(java.lang.Object, int, int)
	 */
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
		for(int i = 0; i < popScreens.size(); i++) {
			popScreens.get(i).OnProcessCmd(socket, aMobileType, aMsgID);
		}
	}
	
	public void showView(BasePopScreen aGameView) {
		for(int i = 0; i < popScreens.size(); i++) {
			BasePopScreen tempview = popScreens.get(i);
		}
	}
}
