/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: SpriteButtonGift.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014年4月17日 下午6:47:44
 *******************************************************************************/
package com.funoble.myarstation.gamelogic;

import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamebase.SpriteAction;
import com.funoble.myarstation.store.data.GiveGiftData;
import com.funoble.myarstation.utils.ActivityUtil;


public class SpriteButtonGift extends SpriteButton {
	private OnGiftSelectListen listen;
	public GiveGiftData data;
	public boolean isDrop = false;
	public int iDropStep = 1;
	public int iEndY = 0;
	
	/**
	 * construct
	 * @param aSprite
	 */
	public SpriteButtonGift(Sprite aSprite) {
		super(aSprite);
	}

	public interface OnGiftSelectListen {
		public void OnGiftSelect(int aEventID, GiveGiftData data);
	}
	
	public void setOnGiftSelect(OnGiftSelectListen l) {
		listen = l;
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
		if(isDrop) {
			if(iY <= iEndY) {
				iY += iDropStep;
				if(iY >= iEndY) {
					iY = iEndY;
					isDrop = false;
				}
			}
		}
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
					if(listen != null) {
						listen.OnGiftSelect(iEventID, data);
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
	
	public void setDropStep(int stary, int endy) {
		int temp = (int) ((endy - stary) / 10 * ActivityUtil.ZOOM_X);
		iDropStep = temp <= 0 ? 1 : temp;
	}
	
}
