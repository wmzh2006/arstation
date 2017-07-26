/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: QuickSelectRoomScreen.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014年3月18日 下午9:24:39
 *******************************************************************************/
package com.funoble.myarstation.screen;

import java.util.Vector;

import android.R.integer;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.GameButtonHandler;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.TPoint;
import com.funoble.myarstation.view.GameEventID;
import com.funoble.myarstation.view.GameView;


public abstract class BasePopScreen extends GameView implements GameButtonHandler{

	protected final int STATE_INVALID = 0;
	protected final int STATE_LOADING = 1;
	protected final int STATE_ENTERING = 2;
	protected final int STATE_ENTERED = 3;
	protected final int STATE_EXITING = 4;
	protected final int STATE_EXITED = 5;
	
	protected int iState = STATE_INVALID;
	protected Project iUIPak = null;
	protected Scene iSceneLoading = null;
	protected Scene iSceneOperate = null;
	protected Vector<SpriteButton> iSpriteButtonList = new Vector<SpriteButton>();//精灵按钮 列表
	protected int iSoX = 0;
	protected int iSoSpeed = 0;
	protected int iSoffsetX = 0;
	protected int iSoY= 0;
	protected int iSoSpeedY = 0;
	protected int iSoffsetY = 0;
	protected Paint iPaint = null;
	protected Rect rect = null;
	protected boolean isVision = false;
	protected Rect ViewRect;
	protected int iViewID = 0;
	
	protected boolean ibTouch = true;
	private OnAnimateListen animateListen;
	private int animateType = 0;
	
	/* 
	 * @see com.funoble.lyingdice.view.GameView#init()
	 */
	@Override
	public void init() {
		iPaint = new Paint();
		iPaint.setColor(Color.parseColor("#86222222"));
		rect = new Rect(0, 0, ActivityUtil.SCREEN_WIDTH, ActivityUtil.SCREEN_WIDTH);
		ViewRect = new Rect(0, 0, 0, 0);
		loadPak();
	}
	
	public void setSceenLoading(Scene scene) {
		iSceneLoading = scene;
	}
	
	protected void setVision(boolean vision) {
		isVision = vision;
	}
	
	public void show()  {
		if(iState == STATE_LOADING || iState == STATE_ENTERING) return;
		iState = STATE_ENTERED;
		isVision = true;
	}
	
	public void dimiss(){
		iState = STATE_EXITING;
	}
	
	public void setTouch(boolean touch) {
		ibTouch = touch;
	}
	
	public int getViewID() {
		return iViewID;
	}
	
	public void setViewSize(Rect size) {
		if(size == null) return;
		ViewRect.set(size);
	}
	
	public void setAnimateType(int type) {
		if(type > 1 || type < 0) {
			type = 0;
		}
		animateType = type;
	}
	
	/* 
	 * @see com.funoble.lyingdice.view.GameView#releaseResource()
	 */
	@Override
	public void releaseResource() {
		iSpriteButtonList = null;
	}

	public boolean vision() {
		return isVision;
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#paintScreen(android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	public void paintScreen(Canvas g, Paint paint) {
		if(!isVision) return;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#performL()
	 */
	@Override
	public void performL() {
		if(!isVision) return;
	}

	private void spriteButtonPerform() {
		if(iSceneOperate != null) {
			iSceneOperate.handle();
			iSceneOperate.iCameraX = -(int)iSoX;
			
			//
			for(int i=0; i<iSpriteButtonList.size(); i++) {
				((SpriteButton)iSpriteButtonList.elementAt(i)).perform();
			}
		}
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(!isVision) return true;
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onDown(android.view.MotionEvent)
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onLongPress(android.view.MotionEvent)
	 */
	@Override
	public boolean onLongPress(MotionEvent e) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onSingleTapUp(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#pointerPressed(int, int)
	 */
	@Override
	public boolean pointerPressed(int aX, int aY) {
		if(!isVision || !ibTouch) return false;
		if(iState != STATE_ENTERED) return true;
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#pointerReleased(int, int)
	 */
	@Override
	public boolean pointerReleased(int aX, int aY) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#OnProcessCmd(java.lang.Object, int, int)
	 */
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
	}
	
	protected abstract void loadPak();

	/* 
	 * @see com.funoble.lyingdice.gamebase.GameButtonHandler#ItemAction(int)
	 */
	@Override
	public boolean ItemAction(int aEventID) {
		if(!isVision || !ibTouch) return false;
		return false;
	}
	
	
	protected interface OnAnimateListen {
		void onEntering();
		void onEntered();
		void onExiting();
		void onExited();
	}
}
