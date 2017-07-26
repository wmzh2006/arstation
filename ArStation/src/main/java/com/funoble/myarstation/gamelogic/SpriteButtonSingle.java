package com.funoble.myarstation.gamelogic;

import com.funoble.myarstation.gamebase.Sprite;

/**
 *  SpriteButtonSingle 【单选框】，按一次设置为选择，以后再按也不会更改状态，只能通过setSelected()强制设置
 * 
 */
public class SpriteButtonSingle extends SpriteButton {
	
	private boolean ibSelected;//是否被选择
	
	public SpriteButtonSingle(Sprite aSprite) {
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
			iActionID = ESPRITE_BUTTON_ACTION_PRESSED;
		}
		else {
			iActionID = ESPRITE_BUTTON_ACTION_NORMAL;
		}
	}

	@Override
	public void pressed() {
	}
	@Override
	public void released() {
		ibSelected = true;
		iActionID = ESPRITE_BUTTON_ACTION_PRESSED;
	}
}
