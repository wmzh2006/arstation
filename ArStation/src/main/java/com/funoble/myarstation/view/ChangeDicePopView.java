/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: ChangeDicePopView.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014年3月27日 下午11:37:53
 *******************************************************************************/
package com.funoble.myarstation.view;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Message;
import android.view.KeyEvent;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.screen.BasePopScreen;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.TPoint;


public class ChangeDicePopView extends BasePopScreen {

	private int selectDice = 0;
	private SpriteButton iCommitSpriteButton;
	
	public ChangeDicePopView() {
		init();
	}
	
	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#loadPak()
	 */
	@Override
	protected void loadPak() {
		iUIPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"selectdice_map.pak", false);
		iSceneOperate = iUIPak.getScene(0);
		if(iSceneOperate != null) {
			iSceneOperate.setViewWindow(0, 0, 800, 480);
			iSceneOperate.initDrawList();
			iSceneOperate.initNinePatchList(null);
			ViewRect = iSceneOperate.getLayoutMap(Scene.RectangleLayout).getNineRect(0);
			if(ViewRect != null) {
				iSoffsetX = ActivityUtil.SCREEN_WIDTH - ViewRect.left;
				iSoX = iSoffsetX;
				iSoSpeed = (iSoX / 6);
				iSceneOperate.iCameraX = -iSoX;
			}
			//
			Vector<?> pSpriteList = null;
			Map pMap = iSceneOperate.getLayoutMap(Scene.SpriteLayout);
			pSpriteList = pMap.getSpriteList();
			Sprite pSprite;
			for (int i=0; i<pSpriteList.size(); i++) {
				pSprite = (Sprite)pSpriteList.elementAt(i);
				int pX = pSprite.getX();
				int pY = pSprite.getY();
				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_CHANGEDICE + pSprite.getIndex();
				TPoint pPoint = new TPoint();
				pPoint.iX = pX;
				pPoint.iY = pY;
				//精灵按钮
				SpriteButton pSpriteButton;
				pSpriteButton = new SpriteButton(pSprite);
				pSpriteButton.setEventID(pEventID);
				pSpriteButton.setPosition(pX, pY);
				pSpriteButton.setHandler(this);
				if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CHANGEDICE_COMMIT) {
					iCommitSpriteButton = pSpriteButton;
				}
				iSpriteButtonList.add(pSpriteButton);
			}
		}
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#init()
	 */
	@Override
	public void init() {
		super.init();
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#show()
	 */
	@Override
	public void show() {
		isVision = true;
		selectDice = MyArStation.iPlayer.iDiceID / 10;
		Tools.println("selectDice " + selectDice);
		setDiceState(selectDice);
		iState = STATE_ENTERING;
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#dimiss()
	 */
	@Override
	public void dimiss() {
		iState = STATE_EXITING;
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#releaseResource()
	 */
	@Override
	public void releaseResource() {
		super.releaseResource();
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#paintScreen(android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	public void paintScreen(Canvas g, Paint paint) {
		if(!isVision) return;
		g.drawRect(rect, iPaint);
		if(iSceneOperate != null) {
			iSceneOperate.paint(g, 0, 0);
			for(int i = 0; i < iSpriteButtonList.size(); i++) {
				iSpriteButtonList.get(i).paint(g, iSceneOperate.iCameraX, 0);
			}
		}
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(!isVision) return false;
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			dimiss();
			return true;
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#pointerPressed(int, int)
	 */
	@Override
	public boolean pointerPressed(int aX, int aY) {
		if(!isVision) return false;
//		if(iState != STATE_ENTERED) return false;
		boolean result = false;
		Rect pRect = new Rect();
		SpriteButton pSB;
		if (iCommitSpriteButton != null) {
			pSB = iCommitSpriteButton;
			if (pSB.isVision()) {
				int pX = pSB.getX();
				int pY = pSB.getY();
				Rect pLogicRect = pSB.getRect();
				pRect.set(pX + pLogicRect.left, pY + pLogicRect.top, pX
						+ pLogicRect.right, pY + pLogicRect.bottom);
				if (pRect.contains(aX, aY)) {
					MediaManager.getMediaManagerInstance(
							MediaManager.OTHER_SOUND_ID).playerSound(
									SoundsResID.bn_pressed, 0);
					pSB.pressed();
					result = true;
				}
			}
		}
		if(!result) {
			if(null != ViewRect && ViewRect.contains(aX, aY)) {
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					pSB = ((SpriteButton)iSpriteButtonList.get(i));
					if(!pSB.isVision()) {
						continue;
					}
					int pX = pSB.getX();
					int pY = pSB.getY();
					Rect pLogicRect = pSB.getRect();
					pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
							pX+pLogicRect.right, pY+pLogicRect.bottom);
					if (pRect.contains(aX, aY)) {
						MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
						pSB.pressed();
						return true;
					}
				}
				return true;
			}
			else {
				dimiss();
				return true;
			}
		}
		return result;
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#OnProcessCmd(java.lang.Object, int, int)
	 */
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#ItemAction(int)
	 */
	@Override
	public boolean ItemAction(int aEventID) {
		switch (aEventID) {
		case GameEventID.ESPRITEBUTTON_EVENT_CHANGEDICE_WHITE:
			selectDice = 0;
			setDiceState(selectDice);
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_CHANGEDICE_PURPLE:
			selectDice = 1;
			setDiceState(selectDice);
			
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_CHANGEDICE_BLUE:
			selectDice = 2;
			setDiceState(selectDice);
			
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_CHANGEDICE_RED:
			selectDice = 3;
			setDiceState(selectDice);
			
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_CHANGEDICE_YELLOW:
			selectDice = 4;
			setDiceState(selectDice);
			
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_CHANGEDICE_COMMIT:
			if(selectDice != MyArStation.iPlayer.iDiceID) {
				MyArStation.iGameProtocol.RequestChangeDiceType(selectDice);
			}
			dimiss();
			return true;

		default:
			return true;
		}
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#performL()
	 */
	@Override
	public void performL() {
		if(!isVision) return;
		switch (iState) {
		case STATE_LOADING:
			if(iSceneLoading != null) {
				iSceneLoading.handle();
			}
			break;
		case STATE_ENTERING:
			iSoX -= iSoSpeed;
			if(iSoX <= 0) {
				iSoX = 0;
				iState = STATE_ENTERED;
			}
			break;
		case STATE_ENTERED:
			iSoX = 0;
			break;
		case STATE_EXITING:
			if(iSoX < iSoffsetX) {
				iSoX += iSoSpeed;
			}
			else {
				iState = STATE_EXITED;
			}
			break;
		case STATE_EXITED:
			iSoX = iSoffsetX;
			iState = STATE_INVALID;
			isVision =false;
			break;

		default:
			break;
		}
		performButton();
	}

	private void performButton() {
		if(iSceneOperate != null) {
			iSceneOperate.handle();
			iSceneOperate.iCameraX = -iSoX;
			for(int i = 0; i < iSpriteButtonList.size(); i++) {
				iSpriteButtonList.get(i).perform();
			}
		}
	}
	
	private void setDiceState(int id) {
		if(iSpriteButtonList == null) throw new NullPointerException();
		for(int i = 0; i < iSpriteButtonList.size(); i++) {
			SpriteButton spriteButton = iSpriteButtonList.get(i);
			if(spriteButton.getEventID() - GameEventID.ESPRITEBUTTON_EVENT_CHANGEDICE == id) {
				spriteButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_UNABLE);
			}
			else {
				spriteButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
			}
		}
	}
}
