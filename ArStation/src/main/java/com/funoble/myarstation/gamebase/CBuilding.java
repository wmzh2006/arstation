package com.funoble.myarstation.gamebase;

import android.graphics.Canvas;

public class CBuilding extends BaseAction {
	
	//资源
	private BuildRes iBuildRes = null;
	//动画
	private Sprite iSprite = null;
	//动作ID
	private int iActionID = 0;
	//帧ID
	private int iFrameID = 0;		
	//帧停留时间
	private byte iFrameDelay = 0;
	//是否循环
	private boolean iRepeat = true;
	
	
	/*
	 * 构造函数
	 */
	CBuilding(Sprite aSprite, BuildRes aBuildRes) {
		iSprite = aSprite;
		iBuildRes = aBuildRes;
		iIndex = iSprite.iIndex;
	}
	
	/**
	 * 显示
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paint(Canvas g, int x, int y) {
		if(iBuildRes == null || iSprite == null) {
			return;
		}
		if(iBuildRes.iFlip) {
			iSprite.paintActionMirror(g, x, y, iActionID, iFrameID);
		}
		else {
			iSprite.paintAction(g, x, y, iActionID, iFrameID);
		}
	}
	
	/*
	 * 逻辑
	 */
	public void handle() {
		if(iSprite.actionList == null || iSprite.actionList.length <= 0) {
			return;
		}
		SpriteAction tempAction = (SpriteAction) iSprite.actionList[iActionID];
		iFrameDelay ++;
		if(tempAction.timeDelay.length > 0 && iFrameDelay >= tempAction.timeDelay[iFrameID]) {
			iFrameDelay = 0;
			iFrameID ++;
			if(iFrameID >= tempAction.actionList.length) {
				if(iRepeat) {
					iFrameID = 0;
				}
				else {
					iFrameID --;
				}
			}
		}
	}
	
	/*
	 * 设置是否重复播放
	 */
	public void setRepeat(boolean aRepeat) {
		iRepeat = aRepeat;
	}
	
	/*
	 * 设置是否重复播放
	 */
	public void setActionID(int aActionID) {
		if(iSprite.actionList.length > aActionID && aActionID >= 0) {
			iActionID = aActionID;
			iFrameID = 0;		
			iFrameDelay = 0;
		}
	}
	
	/*
	 * 获取建筑的动画
	 */
	public Sprite getSprite() {
		return iSprite;
	}
	
	public int getActionID() {
		return iActionID;
	}
}
