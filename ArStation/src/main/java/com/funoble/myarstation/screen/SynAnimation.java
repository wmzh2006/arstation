/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: SynAnimation.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014年4月3日 上午9:17:32
 *******************************************************************************/
package com.funoble.myarstation.screen;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.CGamePlayer;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.store.data.AnimFormatData;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.view.GameView;


public class SynAnimation extends GameView {

	private final int MAX_ANIM_SPRITE = 8;	//动态动画的个数
	
	private Vector<Integer> iAnimEndMotionList = null;//动态动画的动作
	private Vector<Sprite> iAnimSpriteList = null; //动态动画
	private Sprite[] iAnimSprite = null;
	//动态动画
	private float[] iAnimX;
	private float[] iAnimY;
	private int[] iAnimStartX;
	private int[] iAnimStartY;
	private int[] iAnimEndX;
	private int[] iAnimEndY;
	private float[] iAnimLineK;
	private float[] iAnimSpeed;
	private int[] iAnimLifeTick;	//飞心的生命周期
	private int[] iStartAnimID;
	private int[] iStartMotion;
	private int[] iEndAnimID;
	private int[] iEndMotion;
	private String[] iAnimName;		
	private SynAnimationListen listen = null;;
	/* 
	 * @see com.funoble.lyingdice.view.GameView#init()
	 */
	@Override
	public void init() {
		iAnimEndMotionList = new Vector<Integer>();//动态动画的动作
		iAnimSpriteList = new Vector<Sprite>(); //动态动画
		iAnimSprite = new Sprite[MAX_ANIM_SPRITE];
		for(int i=0; i<MAX_ANIM_SPRITE; i++) {
			iAnimSprite[i] = new Sprite();
		}
		//动态动画
		iAnimX = new float[MAX_ANIM_SPRITE];
		iAnimY = new float[MAX_ANIM_SPRITE];
		iAnimStartX = new int[MAX_ANIM_SPRITE];
		iAnimStartY = new int[MAX_ANIM_SPRITE];
		iAnimEndX = new int[MAX_ANIM_SPRITE];
		iAnimEndY = new int[MAX_ANIM_SPRITE];
		iAnimLineK = new float[MAX_ANIM_SPRITE];
		iAnimSpeed = new float[MAX_ANIM_SPRITE];
		iAnimLifeTick = new int[MAX_ANIM_SPRITE];	//飞心的生命周期
		iStartAnimID = new int[MAX_ANIM_SPRITE];
		iStartMotion = new int[MAX_ANIM_SPRITE];
		iEndAnimID = new int[MAX_ANIM_SPRITE];
		iEndMotion = new int[MAX_ANIM_SPRITE];
		iAnimName = new String[MAX_ANIM_SPRITE];	
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#releaseResource()
	 */
	@Override
	public void releaseResource() {
		//动态动画
		iAnimX = null;
		iAnimY = null;
		iAnimStartX = null;
		iAnimStartY = null;
		iAnimEndX = null;
		iAnimEndY = null;
		iAnimLineK = null;
		iAnimSpeed = null;
		iAnimLifeTick = null;
		iStartAnimID = null;
		iStartMotion = null;
		iEndAnimID = null;
		iEndMotion = null;
		iAnimName = null;
		
		iAnimEndMotionList = null;//动态动画的动作
		iAnimSpriteList = null; //动态动画
		iAnimSprite = null;
		listen = null;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#paintScreen(android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	public void paintScreen(Canvas g, Paint paint) {
		//动态动画
		for(int i=0; i<iAnimSpriteList.size(); i++) {
			Sprite tempSprite = iAnimSpriteList.get(i);
			int tEndMotion = iAnimEndMotionList.get(i);
			tempSprite.paintStatic(g, tEndMotion, tempSprite.getX(), tempSprite.getY());
		}
		for(int i=0; i<MAX_ANIM_SPRITE; i++) {
			if(iAnimLifeTick[i] <= 0) {
				continue;
			}
			//
			iAnimSprite[i].paintAction(g, iStartMotion[i], (int)iAnimX[i], (int)iAnimY[i]);//iStartMotion[i],
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#performL()
	 */
	@Override
	public void performL() {
		for(int i=0; i<iAnimSpriteList.size(); i++) {
			Sprite pSprite = iAnimSpriteList.get(i);
			int tEndMotion = iAnimEndMotionList.get(i);
			if(pSprite.performAction(tEndMotion) == true) {
				pSprite = null;
				iAnimSpriteList.remove(i);
				iAnimEndMotionList.remove(i);
				if(listen != null) listen.onSynAnimationEnd();
				i --;
			}
		}
		
		//动态动画
		for(int i=0; i<MAX_ANIM_SPRITE; i++) {
			if(iAnimLifeTick[i] <= 0) {
				continue;
			}
			iAnimLifeTick[i] --;
			if(iAnimStartX[i] == iAnimEndX[i]) {
				iAnimY[i] += iAnimSpeed[i];
			}
			else if(iAnimStartY[i] == iAnimEndY[i]) {
				iAnimX[i] += iAnimSpeed[i];
			}
			else {
				iAnimX[i] += iAnimSpeed[i];
				iAnimY[i] = (int)((iAnimX[i]-iAnimStartX[i]) * iAnimLineK[i]) + iAnimStartY[i];
			}
			//触发第二个动画
			if(iAnimLifeTick[i] == 0) {	
				Project tPj = CPakManager.loadSynProject(iAnimName[i]);
				if(tPj != null) {
					Sprite pSprite = new Sprite();
					pSprite.copyFrom(tPj.getSprite(iEndAnimID[i]));
					pSprite.setPosition((short)iAnimX[i], (short)iAnimY[i]);
					pSprite.resetFrameID();
					iAnimSpriteList.addElement(pSprite);
					iAnimEndMotionList.addElement(iEndMotion[i]);
//					Tools.debug("SynAnimation::performL() iEndMotion[" + i 
//							+ "]=" + iEndMotion[i]);
				}
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
	
	public void remove() { 
		//动态动画
		for(int i=0; i<iAnimEndMotionList.size(); i++) {
			iAnimEndMotionList.remove(i);
		}
		for(int i=0; i<iAnimSpriteList.size(); i++) {
			Sprite pSprite = iAnimSpriteList.get(i);
			pSprite = null;
			iAnimSpriteList.remove(i);
		}
	}
	
	public void setAnimationData(String aAnimation, CGamePlayer[] iPlayerList, int[] iChangedSeatID) {
		setAnimationData(new AnimFormatData(aAnimation), iPlayerList, iChangedSeatID);
	}
	
	public void setAnimationData(AnimFormatData aAnimFormatData, CGamePlayer[] iPlayerList, int[] iChangedSeatID) {
		if(aAnimFormatData == null) return;
		AnimFormatData animFormatData = aAnimFormatData;
		//动态动画 寻找控制器
		int tempIndex = -1;
		for(int i=0; i<MAX_ANIM_SPRITE; i++) {
			if(iAnimLifeTick[i] <= 0) {
				tempIndex = i;
				break;
			}
		}
		if(tempIndex < 0) {
			return;
		}
		//动态动画 设置动作
		iStartAnimID[tempIndex] = animFormatData.nStartID;
		iStartMotion[tempIndex] = animFormatData.nStartMotion;
		iEndAnimID[tempIndex] = animFormatData.nEndID;
		iEndMotion[tempIndex] = animFormatData.nEndMotion;
//		Tools.debug("SynAnimation::setAnimationData() Index=" + tempIndex
//				+ " nStartMotion=" + iStartMotion[tempIndex]
//				+ " nEndMotion=" + iEndMotion[tempIndex]);
		//动态动画 寻找Pak
		iAnimName[tempIndex] = animFormatData.szPak;
		Project tPj = CPakManager.loadSynProject(animFormatData.szPak);
		if(tPj == null) {
			return;
		}
		Sprite sprite = tPj.getSprite(animFormatData.nStartID);
		iAnimSprite[tempIndex].copyFrom(sprite);
		//设置生命周期
		iAnimLifeTick[tempIndex] = 18;
		//计算起始坐标
		if(animFormatData.nStartType == 0 && iPlayerList != null && iChangedSeatID != null) { //位置模式
			int tSeatID = animFormatData.nX1;
			if(tSeatID < 0 || tSeatID >= 6) {
				if(tSeatID < 0) {
					tSeatID = 6;
				}
				if(tSeatID > 14) {
					tSeatID = 14;
				}
				//6 ~ 14  左上 ， 上，  右上 ，左，中，右，左下，下，右下
				iAnimX[tempIndex] = iAnimStartX[tempIndex] = ActivityUtil.SCREEN_POS_X[tSeatID-6];
				iAnimY[tempIndex] = iAnimStartY[tempIndex] = ActivityUtil.SCREEN_POS_Y[tSeatID-6];
			}
			else {
				//0 ~ 5  玩家的座位ID
				int bmpH = 80;
				if(iPlayerList[tSeatID].iBmp != null) {
					bmpH = iPlayerList[tSeatID].iBmp.getHeight();
				}
				iAnimX[tempIndex] = iAnimStartX[tempIndex] = iPlayerList[iChangedSeatID[tSeatID]].iX;
				iAnimY[tempIndex] = iAnimStartY[tempIndex] = iPlayerList[iChangedSeatID[tSeatID]].iY - (bmpH>>1);
			}
		}
		else { //坐标模式
			iAnimX[tempIndex] = iAnimStartX[tempIndex] = (int)(animFormatData.nX1 * ActivityUtil.ZOOM_X);
			iAnimY[tempIndex] = iAnimStartY[tempIndex] = (int)(animFormatData.nY1 * ActivityUtil.ZOOM_X);
		}
		//计算结束坐标
		if(animFormatData.nEndType == 0  && iPlayerList != null && iChangedSeatID != null) { 
			int tDesSeatID = animFormatData.nX2;
			if(tDesSeatID < 0 || tDesSeatID >= 6) {
				//6 ~ 14  左上 ， 上，  右上 ，左，中，右，左下，下，右下
				iAnimEndX[tempIndex] = ActivityUtil.SCREEN_POS_X[tDesSeatID-6];
				iAnimEndY[tempIndex] = ActivityUtil.SCREEN_POS_Y[tDesSeatID-6];
			}
			else {
				int bmpH = 80;
				if(iPlayerList[tDesSeatID].iBmp != null) {
					bmpH = iPlayerList[tDesSeatID].iBmp.getHeight();
				}
				iAnimEndX[tempIndex] = iPlayerList[iChangedSeatID[tDesSeatID]].iX; 
				iAnimEndY[tempIndex] = iPlayerList[iChangedSeatID[tDesSeatID]].iY - (bmpH>>1);
			}
		}
		else {	//坐标模式
			iAnimEndX[tempIndex] = (int)(animFormatData.nX2 * ActivityUtil.ZOOM_X);
			iAnimEndY[tempIndex] = (int)(animFormatData.nY2 * ActivityUtil.ZOOM_Y);
		}
		//计算速度
		if(iAnimEndX[tempIndex]-iAnimStartX[tempIndex] != 0) {
			iAnimLineK[tempIndex] = ((float)(iAnimEndY[tempIndex]-iAnimStartY[tempIndex]))/((float)(iAnimEndX[tempIndex]-iAnimStartX[tempIndex]));
			iAnimSpeed[tempIndex] = ((float)(iAnimEndX[tempIndex] - iAnimStartX[tempIndex])) / (float)18;
		}
		else {
			iAnimSpeed[tempIndex] = ((float)(iAnimEndY[tempIndex]-iAnimStartY[tempIndex])) / (float)18;
		}
	}
	
	public void setAnimationData(String aAnimFormatData) {
		setAnimationData(new AnimFormatData(aAnimFormatData), null, null);
	}
	
	public void setAnimationData(AnimFormatData aAnimFormatData) {
		setAnimationData(aAnimFormatData, null, null);
	}
	
	public void setOnSynAnimationListen(SynAnimationListen l) {
		listen = l;
	}
	
	public interface SynAnimationListen {
		public void onSynAnimationStart();
		public void onSynAnimationPlaying();
		public void onSynAnimationEnd();
	}
}
