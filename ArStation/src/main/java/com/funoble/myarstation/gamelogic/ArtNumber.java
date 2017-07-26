package com.funoble.myarstation.gamelogic;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.funoble.myarstation.gamebase.ActionFrame;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamebase.SpriteAction;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.view.GameEventID;

public class ArtNumber {
	private int[] iX_Offset_ONE_ONE = {9,7};//1位数 个位 数字 X偏移量，0index为800屏，1index为480屏
	private int[] iY_Offset = {7,6};//
	private int[] iX_Offset_ONE = {17,12};//2位数 个位 数字 X偏移量，0index为800屏，1index为480屏
	private int[] iX_Offset_TEN = {2,3};//2位数 十位 数字 X偏移量，0index为800屏，1index为480屏
	
	private Project iResProject;//pak
	private Sprite iSprite;//Sprite 个位
	private int iActionID;//动作ID
	private int iFrameID;//帧ID
	private int iFrameDelay;//帧停留时间
	private Sprite iSpriteTen;//Sprite 十位
	private int iActionIDTen;//动作ID
	private int iFrameIDTen;//帧ID
	private int iFrameDelayTen;//帧停留时间
	private int iX;//底板坐标
	private int iY;
	private int iValue;//数量值
	private Rect iRect;//逻辑矩形
	private boolean ibKey;//是否关键
//	private Rect iDrawRect;//若是关键时，要画的框
	private boolean ibDraw;//是否画
	
	public ArtNumber(Project aPak) {
		init(aPak);
	}
	
	//释放资源
	public void releaseResource() {
		iResProject = null;
		iRect = null;
//		iDrawRect = null;
	}
	
	//初始化
	private void init(Project aPak) {
		iResProject = aPak;
		iSprite = null;
		iActionID = 0;
		iFrameID = 0;
		iFrameDelay = 0;
		iSpriteTen = null;
		iActionIDTen = 0;
		iFrameIDTen = 0;
		iFrameDelayTen = 0;
		iX = 50;
		iY = 50;
		loadProjectL();
		iRect = new Rect();
//		iDrawRect = new Rect();
		setLogicRect();
		ibKey = false;
		ibDraw = true;
	}
	
	private void setLogicRect() {
//		if (iSprite == null) {
//			return;
//		}
//		//拿第一个动作的第一个帧
//		ActionFrame pFrame = (ActionFrame) iSprite.actionList[0].actionList[0];
//		int[][] pRect = pFrame.getLogicRect();
//		int pRectCount = pFrame.getRectCount();
//		for (int i=0; i< pRectCount; i++) {
//			if (pRect[i][4] == 1) {
//				int left = pRect[i][0];
//				int top = pRect[i][1];
//				int right = left + pRect[i][2];
//				int bottom = top + pRect[i][3];
//				iRect.set(left, top, right, bottom);
//			}
//		}
	}
	
	public void setPosition(int aX, int aY) {
		iX = aX;
		iY = aY;
//		iDrawRect.set(iX+iRect.left, iY+iRect.top, iX+iRect.right, iY+iRect.bottom);
	}
	
	private void loadProjectL() {
		if (iResProject != null) {
			iSprite = iResProject.getSprite(0);
			iSpriteTen = iResProject.getSprite(0);
		}
	}
	
	public void setPak(Project aPak) {
		iResProject = aPak;
		if (iResProject != null) {
			iSprite = iResProject.getSprite(0);
			iActionID = 0;
			iFrameID = 0;
			iFrameDelay = 0;
		}
	}
	
	public void performL() {
		spritePerformL();
	}
	
	public void paint(Canvas g) {
		if (!ibDraw) {
			return;
		}
		if (iValue >= 10) {
			if (iSprite != null) {
				iSprite.paintAction(g,
						(int) (iX+iX_Offset_ONE[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X),
						(int) (iY+iY_Offset[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y),
						iActionID, iFrameID);
			}
			if (iSpriteTen != null) {
				iSpriteTen.paintAction(g,
						(int) (iX+iX_Offset_TEN[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X),
						(int) (iY+iY_Offset[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y),
						iActionIDTen, iFrameIDTen);
			}
		}
		else {
			if (iSprite != null) {
				iSprite.paintAction(g,
						(int) (iX+iX_Offset_ONE_ONE[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X),
						(int) (iY+iY_Offset[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y),
						iActionID, iFrameID);
			}
		}
//		if (ibKey) {
//			g.drawRect(iDrawRect, ActivityUtil.mDicePaint);
//			g.drawRect(iDrawRect, ActivityUtil.mDicePaint);
//			g.drawRect(iDrawRect, ActivityUtil.mDicePaint);
//			g.drawRect(iDrawRect, ActivityUtil.mDicePaint);
//			g.drawRect(iDrawRect, ActivityUtil.mDicePaint);
//		}
	}
	
	private void spritePerformL() {
		if (iSprite == null) {
			return;
		}
		SpriteAction tempAction = (SpriteAction) iSprite.actionList[iActionID];
		iFrameDelay ++;
		if(iFrameDelay >= tempAction.timeDelay[iFrameID]) {
			iFrameDelay = 0;
			iFrameID ++;
			if(iFrameID >= tempAction.actionList.length) {
				if (ibKey) {
					iFrameID --;
				}
				else {
					iFrameID = 0;
				}
			}
		}
	}
	
	//设置数量值
	public void setValue(int aValue) {
		iValue = aValue;
		int pTen = 0;//十位
		int pOne = 0;//个位
		if (iValue < 0) {
			iValue = 0;
		}
		else {
			pTen = iValue/10;
			pOne = iValue%10;
		}
		if (iResProject != null) {
			iSpriteTen = iResProject.getSprite(pTen);
			iSprite = iResProject.getSprite(pOne);
		}
	}
	
	public void setDraw(boolean abDraw) {
		ibDraw = abDraw;
	}
	
}
