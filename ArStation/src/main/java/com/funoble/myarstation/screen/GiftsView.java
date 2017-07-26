/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: InfoView.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014年3月22日 下午9:38:46
 *******************************************************************************/
package com.funoble.myarstation.screen;

import java.util.Vector;

import android.R.integer;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.funoble.myarstation.game.CPlayer;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.store.data.GiftData;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.TPoint;
import com.funoble.myarstation.view.GameEventID;


public class GiftsView extends BasePopScreen {

	private final int ROWMAX = 3;
	private final int COLMAX = 3;
	
	private Scene iSceneDetail = null;
	private Vector<SpriteButton> iSpritebuttonListDetail = new Vector<SpriteButton>();//精灵按钮 列表
	private CPlayer iPlayer;
	private OnClickGiftsListen listen;
	private GiftData iGiftData = null;
	private Vector<com.funoble.myarstation.store.data.GiftData> iGiftDatas;
	
	private int iRow1X = (int) (170 * ActivityUtil.ZOOM_X);
	private int iCol1Y = (int) (167 * ActivityUtil.ZOOM_Y);
	private int iRow2X = (int) (385 * ActivityUtil.ZOOM_X);
	private int iCol2Y = (int) (296 * ActivityUtil.ZOOM_Y);
	private int iRow3X = (int) (611 * ActivityUtil.ZOOM_X);
	private int iCol3Y = (int) (412 * ActivityUtil.ZOOM_Y);
	
	private Vector<Point> iPos = new Vector<Point>();
	
	private Paint iPaintWhite = null;
	private Paint iPaintRed = null;
	
	public GiftsView() { 
	}
	
	/* 
	 * @see com.funoble.lyingdice.view.GameView#init()
	 */
	@Override
	public void init() {
		super.init();
		iPaintWhite = new Paint();
		iPaintWhite.setColor(Color.WHITE);
		iPaintWhite.setTextSize(ActivityUtil.TEXTSIZE_NORMAL);
		iPaintWhite.setTextAlign(Align.LEFT);
		iPaintWhite.setAntiAlias(true);
		iPaintRed = new Paint();
		iPaintRed.setColor(Color.RED);
		iPaintRed.setTextSize(ActivityUtil.TEXTSIZE_NORMAL);
		iPaintRed.setTextAlign(Align.LEFT);
		iPaintRed.setAntiAlias(true);
		iPos.add(new Point(iRow1X, iCol1Y));
		iPos.add(new Point(iRow2X, iCol1Y));
		iPos.add(new Point(iRow3X, iCol1Y));
		iPos.add(new Point(iRow1X, iCol2Y));
		iPos.add(new Point(iRow2X, iCol2Y));
		iPos.add(new Point(iRow3X, iCol2Y));
		iPos.add(new Point(iRow1X, iCol3Y));
		iPos.add(new Point(iRow2X, iCol3Y));
		iPos.add(new Point(iRow3X, iCol3Y));
	}
	
	public void setOnClickGiftListen(OnClickGiftsListen l) {
		listen = l;
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#releaseResource()
	 */
	@Override
	public void releaseResource() {
		super.releaseResource();
		iSceneDetail = null;
		iSceneLoading = null;
		iUIPak = null;
	}

	
	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#show()
	 */
	@Override
	public void show() {
		if(iState == STATE_LOADING || iState == STATE_ENTERING) return;
		iState = STATE_ENTERING;
		isVision = true;
	}

	public void dimiss() {
		iState = STATE_EXITING;
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#paintScreen(android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	public void paintScreen(Canvas g, Paint paint) {
		if(!isVision) return;
		if(iState == STATE_LOADING) {
			if(iSceneLoading != null) {
				iSceneLoading.paint(g, 0, 0);
//				g.drawText("正在读取数据", 278*ActivityUtil.ZOOM_X, 288*ActivityUtil.ZOOM_Y, ActivityUtil.mLoading);
			}
		}
		else {
			// 以绘制透明渐变的矩形View
			g.drawRect(rect, iPaint);
			if(iSceneDetail != null) {
				iSceneDetail.paint(g, iSceneDetail.iCameraX, iSceneDetail.iCameraY);
				for(int i=0; i<iSpritebuttonListDetail.size(); i++) {
					((SpriteButton)iSpritebuttonListDetail.elementAt(i)).paint(g, iSceneDetail.iCameraX, iSceneDetail.iCameraY);
				}
				//称号
				String price = "未定";
//				//column0
//				g.drawText(price, iRow1X + iSoX, iCol1Y, iPaintWhite);
//				g.drawText(price, iRow2X + iSoX, iCol1Y, iPaintWhite);
//				g.drawText(price, iRow3X + iSoX, iCol1Y, iPaintWhite);
//				
//				//column1
//				g.drawText(price, iRow1X + iSoX, iCol2Y, iPaintWhite);
//				g.drawText(price, iRow2X + iSoX, iCol2Y, iPaintWhite);
//				g.drawText(price, iRow3X + iSoX, iCol2Y, iPaintWhite);
//				
//				//column2
//				g.drawText(price, iRow1X + iSoX, iCol3Y, iPaintWhite);
//				g.drawText(price, iRow2X + iSoX, iCol3Y, iPaintWhite);
//				g.drawText(price, iRow3X + iSoX, iCol3Y, iPaintWhite);
				float textSize = iPaintWhite.getTextSize();
				for(int col = 0; col < COLMAX; col++) {
					for(int row = 0; row < ROWMAX; row++) {
						int pos = col + row * ROWMAX;
						GiftData temp = getPrice(pos);
						int rowX = iPos.get(pos).x;
						int colY = iPos.get(pos).y;
						g.drawText(""+temp.iNames, rowX + iSoX, colY - textSize * 2, iPaintWhite);
						g.drawText("亲密:"+temp.iIntimate, rowX + iSoX, colY - textSize, iPaintWhite);
						g.drawText(temp.iPrice, rowX + iSoX, colY, iPaintWhite);
					}
				}
			}
		}
	}

	/**
	 * @param pos
	 * @return
	 */
	private GiftData getPrice(int pos) {
		GiftData tempData = new GiftData();
		if(iGiftDatas != null) {
			if(pos < iGiftDatas.size()) {
				tempData = iGiftDatas.get(pos);
				if(tempData != null) {
					tempData.iPrice = tempData.iMoney > 0 ? tempData.iMoney+"" : tempData.iGb + "";
				}
			}
		}
		return tempData;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#performL()
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
			if(listen != null && iGiftData != null) listen.OnClickGifts(iGiftData);
			iState = STATE_INVALID;
			isVision =false;
			iGiftData = null;
			break;

		default:
			break;
		}
		spriteButtonPerform();
		
	}

	private void spriteButtonPerform() {
		if(iSceneDetail != null) {
			iSceneDetail.handle();
			iSceneDetail.iCameraX = -iSoX;
			//
			for(int i=0; i<iSpritebuttonListDetail.size(); i++) {
				((SpriteButton)iSpritebuttonListDetail.elementAt(i)).perform();
			}
		}
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!isVision) return false;
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onKeyDown(int, android.view.KeyEvent)
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
	 * @see com.funoble.lyingdice.view.GameView#onDown(android.view.MotionEvent)
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		super.onDown(e);
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onLongPress(android.view.MotionEvent)
	 */
	@Override
	public boolean onLongPress(MotionEvent e) {
		super.onLongPress(e);
		if(!isVision) return false;
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onSingleTapUp(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		super.onSingleTapUp(e);
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#pointerPressed(int, int)
	 */
	@Override
	public boolean pointerPressed(int aX, int aY) {
		if(!isVision) return false;
		if(iState != STATE_ENTERED) return true;
		Rect pRect = new Rect();
		SpriteButton pSB;
		if(null != ViewRect && ViewRect.contains(aX, aY)) {
			for(int i=0; i<iSpritebuttonListDetail.size(); i++) {
				pSB = ((SpriteButton)iSpritebuttonListDetail.elementAt(i));
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
		}
		else {
			dimiss();
		}
		return true;
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
		MobileMsg pMobileMsg = (MobileMsg) socket;
		switch (aMobileType) {
		case IM.IM_RESPONSE:
		{
			switch (aMsgID) {
			case RspMsg.MSG_RSP_ROOM_LIST_C:
				break;
			default:
				break;
			}
		}
		break;
		default:
			break;
		}
	}
	
	protected void loadPak() {
		//详细资料界面
		iUIPak = CPakManager.loadMyHomePak();//Project.loadProject(ActivityUtil.PATH_SCREEN+"gameshome_map.pak", false);
		iSceneDetail = iUIPak.getScene(5);
		if(iSceneDetail != null) {
			iSceneDetail.setViewWindow(0, 0, 800, 480);
			iSceneDetail.initDrawList();
			iSceneDetail.initNinePatchList(null);
			ViewRect = iSceneDetail.getLayoutMap(Scene.RectangleLayout).getNineRect(0);
			if(ViewRect != null) {
				iSoffsetX = ActivityUtil.SCREEN_WIDTH - ViewRect.left;
				iSoX = iSoffsetX;
				iSoSpeed = (iSoX / 6);
				iSceneDetail.iCameraX = -iSoX;
//			iSoffsetY = ActivityUtil.SCREEN_HEIGHT - ViewRect.top;
//			iSoY = iSoffsetY;
//			iSoSpeedY = (iSoY / 6);
//			iSceneDetail.iCameraY = -iSoY;
			}
			//
			Vector<?> pSpriteList = null;
			Map pMap = iSceneDetail.getLayoutMap(Scene.SpriteLayout);
			pSpriteList = pMap.getSpriteList();
			Sprite pSprite;
			iSpritebuttonListDetail.removeAllElements();
			for (int i=0; i<pSpriteList.size(); i++) {
				pSprite = (Sprite)pSpriteList.elementAt(i);
				int pX = pSprite.getX();
				int pY = pSprite.getY();
				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_GIFTS_OFFSET + pSprite.getIndex();
				TPoint pPoint = new TPoint();
				pPoint.iX = pX;
				pPoint.iY = pY;
				//精灵按钮
				SpriteButton pSpriteButton;
				pSpriteButton = new SpriteButton(pSprite);
				pSpriteButton.setEventID(pEventID);
				pSpriteButton.setPosition(pX, pY);
				pSpriteButton.setHandler(this);
				iSpritebuttonListDetail.addElement(pSpriteButton);
			}
		}
	}

	/* 
	 * @see com.funoble.lyingdice.gamebase.GameButtonHandler#ItemAction(int)
	 */
	@Override
	public boolean ItemAction(int aEventID) {
		if(!isVision || !ibTouch) return false;
		switch (aEventID) {
		case GameEventID.ESPRITEBUTTON_EVENT_DETAILINFO_CLOSE:
			dimiss();
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_GIFTS_CLOSE:
			dimiss();
			break;
//		case GameEventID.ESPRITEBUTTON_EVENT_GIFTS_CHOCOLATE:
//			dimiss();
//			break;
//		case GameEventID.ESPRITEBUTTON_EVENT_GIFTS_PEER:
//			dimiss();
//			break;
//		case GameEventID.ESPRITEBUTTON_EVENT_GIFTS_BEAR:
//			dimiss();
//			break;
//		case GameEventID.ESPRITEBUTTON_EVENT_GIFTS_HEART:
//			dimiss();
//			break;
//		case GameEventID.ESPRITEBUTTON_EVENT_GIFTS_BOX:
//			dimiss();
//			break;
//		case GameEventID.ESPRITEBUTTON_EVENT_GIFTS_FLOWER:
//			dimiss();
//			break;
//		case GameEventID.ESPRITEBUTTON_EVENT_GIFTS_BALLOON:
//			dimiss();
//			break;
//		case GameEventID.ESPRITEBUTTON_EVENT_GIFTS_UNDERWEAR:
//			dimiss();
//			break;
//		case GameEventID.ESPRITEBUTTON_EVENT_GIFTS_VIP:
//			dimiss();
//			break;

		default:
			if(GameEventID.ESPRITEBUTTON_EVENT_GIFTS_CHOCOLATE <= aEventID && 
					aEventID <= GameEventID.ESPRITEBUTTON_EVENT_GIFTS_VIP) {
				int index = aEventID - GameEventID.ESPRITEBUTTON_EVENT_GIFTS_CHOCOLATE;
				if(iGiftDatas != null && index >= 0 && index < 9) {
					iGiftData = iGiftDatas.get(index);
					dimiss();
				}
			}
			break;
		}
		return false;
	}
	
	public interface OnClickGiftsListen {
		public void OnClickGifts(GiftData aGiftData);
	}
	
	public void setGiftData(Vector<com.funoble.myarstation.store.data.GiftData> aDatas) {
		iGiftDatas = aDatas;
	}
}
