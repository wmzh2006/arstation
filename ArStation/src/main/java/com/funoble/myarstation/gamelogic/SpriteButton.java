package com.funoble.myarstation.gamelogic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.funoble.myarstation.gamebase.ActionFrame;
import com.funoble.myarstation.gamebase.GameButtonHandler;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamebase.SpriteAction;
import com.funoble.myarstation.utils.ActivityUtil;

/*
============================================================================
Name		: spritebutton.h
Author	  : zero
Version	 : 1.0
Copyright   : 凤智科技
Description : CSpriteButton 精灵按钮
============================================================================
*/

public class SpriteButton {
	public static final int ESPRITE_BUTTON_ACTION_PRESSED = 0;//按下时的动作ID
	public static final int ESPRITE_BUTTON_ACTION_NORMAL = 1;//通常时的动作ID
	public static final int ESPRITE_BUTTON_ACTION_SHOW = 2;	//出场
	public static final int ESPRITE_BUTTON_ACTION_MSG = 3;	//有消息
	public static final int ESPRITE_BUTTON_ACTION_UNABLE = 4;	//失效
	public static final int ESPRITE_BUTTON_RECT_EDIT = 0;//精灵的编辑层矩形
	public static final int ESPRITE_BUTTON_RECT_SPRITE = 1;//整个精灵的矩形
	
	protected GameButtonHandler iHandler = null;
	protected Sprite	iSprite;//精灵
	protected int iActionID;// 动作ID
	protected int iFrameID;// 帧ID
	protected int iFrameDelay;// 帧停留时间
	protected int iEventID;//事件ID
	protected int iX;//X坐标
	protected int iY;//Y坐标
	protected Rect iRect;//逻辑矩形
	protected Rect iRectNull;//
	protected boolean ibVision;//是否可视
	protected boolean ibAutoDisappear; //自动消失
	
	public SpriteButton(Sprite aSprite) {
		iSprite = aSprite;
		iActionID = ESPRITE_BUTTON_ACTION_SHOW;
		iFrameID = 0;
		iFrameDelay = 0;
		iEventID = 0;
		iX = 0;
		iY = 0;
		iRect = new Rect();
		iRectNull = new Rect();
		iRectNull.set(-10000,-10000,-10000,-10000);
		ibVision = true;
		ibAutoDisappear = false;
		setLogicRect();
	}
	
	public void releaseResource() {
		iSprite = null;
		iRect = null;
	}
	
	public void setHandler(GameButtonHandler aGameButtonHandler) {
		iHandler = aGameButtonHandler;
	}
	
	public int getX() {
		return iX;
	}
	
	public int getY() {
		return iY;
	}
	
	public Rect getRect() {
		if(iActionID == ESPRITE_BUTTON_ACTION_NORMAL
				|| iActionID == ESPRITE_BUTTON_ACTION_MSG) {
			return iRect;
		}
		return iRectNull;
	}
	
	public int getEventID() {
		return iEventID;
	}
	
	public void setEventID(int aEventID) {
		iEventID = aEventID;
	}
	
	public boolean isVision() {
		return ibVision;
	}
	
	public void setVision(boolean aVision){
		ibVision = aVision;
	}
	
	public void perform() {
		if (iSprite == null || !ibVision) {
			return;
		}
		if (iActionID < 0 || iActionID >= iSprite.actionList.length) {
			return;
		}
		//动画逻辑
		SpriteAction tempAction = (SpriteAction) iSprite.actionList[iActionID];
		//test-------------
		if(iFrameID >= tempAction.actionList.length) {
			iFrameID = tempAction.actionList.length - 1;
		}
		//test-------------
		iFrameDelay ++;
		if(iFrameDelay >= tempAction.timeDelay[iFrameID]) {
			iFrameDelay = 0;
			iFrameID ++;
			if(iFrameID >= tempAction.actionList.length) {
				if(iActionID == ESPRITE_BUTTON_ACTION_NORMAL) {
					if(ibAutoDisappear) {
						iFrameID = 0;
//						iActionID = ESPRITE_BUTTON_ACTION_UNABLE;
						ibVision = false;
					}
					else {
						iFrameID = 0;
					}
				}
				else if(iActionID == ESPRITE_BUTTON_ACTION_PRESSED) {
					iFrameID = 0;
					iActionID = ESPRITE_BUTTON_ACTION_NORMAL;
					if(iHandler != null) {
						iHandler.ItemAction(iEventID);
					}
				}
				else if(iActionID == ESPRITE_BUTTON_ACTION_SHOW) {
					iFrameID = 0;
					iActionID = ESPRITE_BUTTON_ACTION_NORMAL;
				}
				else {
					iFrameID = 0;
				}
			}
		}
	}
	
	public void paint(Canvas aOffscreen) {
		if (iSprite == null) {
			return;
		}
		if(!ibVision) {
			return;
		}
		iSprite.paintAction(aOffscreen, iX, iY, iActionID, iFrameID);
	}
	
	public void paint(Canvas aOffscreen, int aCameraX, int aCameraY) {
		if (iSprite == null) {
			return;
		}
		if(!ibVision) {
			return;
		}
		iSprite.paintAction(aOffscreen, iX-aCameraX, iY-aCameraY, iActionID, iFrameID);
	}
	
	private void setLogicRect() {
		if (iSprite == null) {
			return;
		}
		//拿第一个动作的第一个帧
		if(iSprite.actionList.length <= 1) {
			return;
		}
		ActionFrame pFrame = (ActionFrame) iSprite.actionList[ESPRITE_BUTTON_ACTION_NORMAL].actionList[0];
		int[][] pRect = pFrame.getLogicRect();
		int pRectCount = pFrame.getRectCount();
		for (int i=0; i< pRectCount; i++) {
			if (pRect[i][4] == ESPRITE_BUTTON_RECT_SPRITE) {
//				iRect.SetRect(TPoint(pRect[i][0], pRect[i][1]), TSize(pRect[i][2], pRect[i][3]));
				int left = pRect[i][0];
				int top = pRect[i][1];
				int right = left + pRect[i][2];
				int bottom = top + pRect[i][3];
				iRect.set(left, top, right, bottom);
			}
		}
	}
	
	public void setPosition(int aX, int aY) {
		iX = aX;
		iY = aY;
	}
	
	public void pressed() {
		if((iActionID == ESPRITE_BUTTON_ACTION_NORMAL
				|| iActionID == ESPRITE_BUTTON_ACTION_MSG) && ibVision) {
			iActionID = ESPRITE_BUTTON_ACTION_PRESSED;
			iFrameID = 0;
			iFrameDelay = 0;
		}
	}
	
	public void released() {
//		iActionID = ESPRITE_BUTTON_ACTION_NORMAL;
//		iFrameID = 0;
//		iFrameDelay = 0;
	}
	
	public void setActionID(int aActionID) {
		iActionID = aActionID;
		iFrameDelay = 0;
		iFrameID = 0;
	}
	
	public int getActionID() {
		return iActionID;
	}
	
	public void setAutoDisappear(boolean aAuto) {
		ibAutoDisappear = aAuto;
	}
}
