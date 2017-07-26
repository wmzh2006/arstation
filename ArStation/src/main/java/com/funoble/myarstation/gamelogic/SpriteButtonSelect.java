package com.funoble.myarstation.gamelogic;

import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamebase.SpriteAction;

/**
 *  SpriteButtonSelect 【选择框】，按一次设置为选择，再按一次设置为未选
 * 
 */
public class SpriteButtonSelect extends SpriteButton {
	
	private boolean ibSelected;//是否被选择
		
	public SpriteButtonSelect(Sprite aSprite) {
		super(aSprite);
		ibSelected = false;
	}
	
	public boolean getSelected() {
		return ibSelected;
	}
	//设置为状态
	public void setSelected(boolean abSelected) {
		ibSelected = abSelected;
		if (ibSelected) {
			iActionID = ESPRITE_BUTTON_ACTION_MSG;
		}
		else {
			iActionID = ESPRITE_BUTTON_ACTION_NORMAL;
		}
	}

	@Override
	public void pressed() {
		super.pressed();
		ibSelected = !ibSelected;
	}
	@Override
	public void released() {
	}

	/* 
	 * @see com.funoble.lyingdice.gamelogic.SpriteButton#perform()
	 */
	@Override
	public void perform() {
		if (iSprite == null) {
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
					if (ibSelected) {
						iActionID = ESPRITE_BUTTON_ACTION_MSG;
					}
					else {
						iActionID = ESPRITE_BUTTON_ACTION_NORMAL;
					}
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
}
