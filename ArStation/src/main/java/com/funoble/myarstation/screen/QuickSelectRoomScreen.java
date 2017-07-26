/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: QuickSelectRoomScreen.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014年3月18日 下午9:24:39
 *******************************************************************************/
package com.funoble.myarstation.screen;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.RoomData;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.GameButtonHandler;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.screen.BasePopScreen.OnAnimateListen;
import com.funoble.myarstation.socket.protocol.MBRspRoomListC;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.store.data.RoomDetailData;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.TPoint;
import com.funoble.myarstation.view.GameEventID;
import com.funoble.myarstation.view.GameView;


public class QuickSelectRoomScreen extends GameView implements GameButtonHandler{

	private final int STATE_INVALID = 0;
	private final int STATE_LOADING = 1;
	private final int STATE_ENTERING = 2;
	private final int STATE_ENTERED = 3;
	private final int STATE_EXITING = 4;
	private final int STATE_EXITED = 5;
	
	private int iState = STATE_INVALID;
	private Project iUIPak = null;
	private Scene iSceneLoading = null;
	private Scene iSceneOperate = null;
	private Vector<SpriteButton> iSpriteButtonList = new Vector<SpriteButton>();//精灵按钮 列表
	private int iSoX = 0;
	private int iSoSpeed = 0;
	private int iSoffsetX = 0;
	private Paint iPaint = new Paint();
	private Rect rect = new Rect();
	private boolean isVision = false;
	private Rect ViewRect;
	private int iSceneId = 0;
	private int[] iRoomIds = {
			0,     1,     2,
			3,     4,     5, 
			60
	};//KTV 酒吧  酒店  公海  皇家  大排档 情侣酒吧
	
	private OnSelectRoomListen listen;
	private OnAnimateListen animateListen;
	
	private RoomData selecteRoomData;
	private boolean ibDim = true;
	private boolean ibPop = true;
	
	public QuickSelectRoomScreen() {
		iSceneId = 0;
	}
	
	public QuickSelectRoomScreen(int sceneId) {
		if(sceneId <0 || sceneId > 1) {
			sceneId = 0;
		}
		iSceneId = sceneId;
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#init()
	 */
	@Override
	public void init() {
		iPaint.setColor(Color.parseColor("#86222222"));
		rect.set(0, 0, ActivityUtil.SCREEN_WIDTH, ActivityUtil.SCREEN_WIDTH);
		iSoX *= (float)ActivityUtil.SCREEN_WIDTH / (float)ActivityUtil.SCREEN_WIDTH_STANDARD;
		loadPak();
//		GameMain.iGameProtocol.requestRoomListC(0);
	}
	
	public void setSceenLoading(Scene scene) {
		iSceneLoading = scene;
	}
	
	private void setVision(boolean vision) {
		isVision = vision;
	}
	
	public void setDim(boolean aDim) {
		ibDim = aDim;
	}
	
	public void setPop(boolean aPop) {
		ibPop = aPop;
	}
	
	public void show()  {
		if(iState == STATE_LOADING || iState == STATE_ENTERING) return;
		isVision = true;
		if(MyArStation.iPlayer.iRoomInfo.size() <= 0) {
			MyArStation.iGameProtocol.requestRoomListC(0);
			iState = STATE_LOADING;
		}
		else {
			initButton();
			iState = STATE_ENTERING;
		}
	}

	/**
	 * 
	 */
	private void initButton() {
		Iterator<Integer> iterator = MyArStation.iPlayer.iRoomInfo.keySet().iterator();
		while (iterator.hasNext()) {
			Integer key = iterator.next();
			RoomDetailData val = MyArStation.iPlayer.iRoomInfo.get(key);
			switch (key) {
			case 0://ktv
				buttonAction(val, GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_KTV);
				break;
			case 1://酒吧
				buttonAction(val, GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_BAR);
				break;
			case 2://酒店
				buttonAction(val, GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_HOTEL);
				break;
			case 3://公海
				buttonAction(val, GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_SEA);
				break;
			case 4://皇家
				buttonAction(val, GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_ROYAL);
				break;
			case 5://大排档
				buttonAction(val, GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_STALL);
				break;
			case 60://情侣
				buttonAction(val, GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_LOVER);
				break;

			default:
				break;
			}
		}
	}

	/**
	 * @param val
	 */
	private void buttonAction(RoomDetailData val, int buttonId) {
		Iterator<SpriteButton> iterator = iSpriteButtonList.iterator();
		while (iterator.hasNext()) {
			SpriteButton button = (SpriteButton) iterator.next();
			if(button.getEventID() == buttonId) {
				if(MyArStation.iPlayer.iGb >= val.iMinGB && MyArStation.iPlayer.iGb <= val.iMaxGB) {
					if(button != null)button.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				}
				else {
					if(button != null)button.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_UNABLE);
				}
				break;
			}
		}
	}
	
	public void dimiss(){
		iState = STATE_EXITING;
	}
	
	public void setOnSelectRoom(OnSelectRoomListen l) {
		listen = l;
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#releaseResource()
	 */
	@Override
	public void releaseResource() {
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
		if(iState == STATE_LOADING) {
			if(iSceneLoading != null) {
				iSceneLoading.paint(g, 0, 0);
//				g.drawText("正在读取数据", 278*ActivityUtil.ZOOM_X, 288*ActivityUtil.ZOOM_Y, ActivityUtil.mLoading);
			}
		}
		else {
			// 以绘制透明渐变的矩形View
			if(ibDim) {
				g.drawRect(rect, iPaint);
			}
			
			if(iSceneOperate != null) {
				iSceneOperate.paint(g, 0, 0);
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).paint(g, iSceneOperate.iCameraX, iSceneOperate.iCameraY);
				}
			}
		}
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
				if(animateListen != null) animateListen.onEntering();
			}
			break;
		case STATE_ENTERED:
			if(animateListen != null) animateListen.onEntered();
			break;
		case STATE_EXITING:
			if(iSoX < iSoffsetX) {
				iSoX += iSoSpeed;
				if(animateListen != null) animateListen.onExiting();
			}
			else {
				iState = STATE_EXITED;
			}
			break;
		case STATE_EXITED:
			isVision = false;
			iState = STATE_INVALID;
			if(animateListen != null) animateListen.onExited();
			if(listen != null && selecteRoomData != null) listen.OnSelectRoom(selecteRoomData);
			selecteRoomData = null;
			break;
		default:
			break;
		}
		spriteButtonPerform();
		
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
		if(!isVision) return false;
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(ibPop) {
				dimiss();
				return true;
			}
		}
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
		if(!isVision) return false;
		if(iState != STATE_ENTERED) return true;
		Rect pRect = new Rect();
		SpriteButton pSB;
		if(ViewRect.contains(aX, aY)) {
			for(int i=0; i<iSpriteButtonList.size(); i++) {
				pSB = ((SpriteButton)iSpriteButtonList.elementAt(i));
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
					//System.out.println("ProgressScreen pointerPressed  pSB.pressed() ");
					return true;
				}
			}
			return true;
		}
		else {
			if(ibPop) {
				dimiss();
				return true;
			}
		}
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

		MobileMsg pMobileMsg = (MobileMsg) socket;
		switch (aMobileType) {
		case IM.IM_RESPONSE:
		{
			switch (aMsgID) {
			case RspMsg.MSG_RSP_ROOM_LIST_C:
				try {
					MBRspRoomListC rspRoomListc = (MBRspRoomListC)pMobileMsg.m_pMsgPara;
					if(rspRoomListc == null) {
						break;
					}
					Tools.debug(rspRoomListc.toString());
					MyArStation.iPlayer.iRoomInfo = new HashMap<Integer, RoomDetailData>();
					for(int i = 0; i < rspRoomListc.iCount; i++) {
						RoomDetailData data = new RoomDetailData(rspRoomListc.iRoomID.get(i), 
								rspRoomListc.iTableID.get(i),
								rspRoomListc.iRoomName.get(i), 
								rspRoomListc.iRoomLimit.get(i), 
								rspRoomListc.iRoomBet.get(i), 
								rspRoomListc.iMinGB.get(i),
								rspRoomListc.iMaxGB.get(i), 
								rspRoomListc.iIconName.get(i), 
								rspRoomListc.iType.get(i), 
								rspRoomListc.iGB,
								rspRoomListc.iPicName,
								rspRoomListc.iUrl);
						MyArStation.iPlayer.iRoomInfo.put(rspRoomListc.iRoomID.get(i), data);
					}
					initButton();
					iState = STATE_ENTERING;
				} catch (Exception e) {
					e.printStackTrace();
				}
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
	
	private void loadPak() {
		iUIPak = CPakManager.loadSelectRoomPak();//Project.loadProject(ActivityUtil.PATH_SCREEN+"gameshome_map.pak", false);
		iSceneOperate = null;
		iSceneOperate = iUIPak.getScene(iSceneId);
		if(iSceneOperate != null) {
			iSceneOperate.setViewWindow(0, 0, 800, 480);
			iSceneOperate.initDrawList();
			iSceneOperate.initNinePatchList(null);
			ViewRect = iSceneOperate.getLayoutMap(Scene.RectangleLayout).getNineRect(0);
			iSoffsetX = (int)ViewRect.width();
			iSoX = iSoffsetX;
			iSoSpeed = (iSoX / 6);
			iSceneOperate.iCameraX = -iSoX;
			//
			Map pMap = iSceneOperate.getLayoutMap(Scene.SpriteLayout);
			Vector<?> pSpriteList = pMap.getSpriteList();
			Sprite pSprite;
			iSpriteButtonList.removeAllElements();
			for (int i=0; i<pSpriteList.size(); i++) {
				pSprite = (Sprite)pSpriteList.elementAt(i);
				int pX = pSprite.getX();
				int pY = pSprite.getY();
				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_OFFSET + pSprite.getIndex();
				TPoint pPoint = new TPoint();
				pPoint.iX = pX;
				pPoint.iY = pY;
				//精灵按钮
				SpriteButton pSpriteButton;
				pSpriteButton = new SpriteButton(pSprite);
				pSpriteButton.setEventID(pEventID);
				pSpriteButton.setPosition(pX, pY);
				pSpriteButton.setHandler(this);
				iSpriteButtonList.addElement(pSpriteButton);
			}
		}
	}

	/* 
	 * @see com.funoble.lyingdice.gamebase.GameButtonHandler#ItemAction(int)
	 */
	@Override
	public boolean ItemAction(int aEventID) {
		if(!isVision) return false;
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.windowgo, 0);
		switch (aEventID) {
		case GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_STALL:	
			RoomDetailData data = MyArStation.iPlayer.iRoomInfo.get(iRoomIds[5]);
			selecteRoomData = new RoomData(data.iRoomID, data.iTableID, data.iType);
			iState = STATE_EXITING;
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_KTV: 
			data = MyArStation.iPlayer.iRoomInfo.get(iRoomIds[0]);
			selecteRoomData = new RoomData(data.iRoomID, data.iTableID, data.iType);
			iState = STATE_EXITING;
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_BAR: 
			data = MyArStation.iPlayer.iRoomInfo.get(iRoomIds[1]);
			selecteRoomData = new RoomData(data.iRoomID, data.iTableID, data.iType);
			iState = STATE_EXITING;
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_HOTEL: 
			data = MyArStation.iPlayer.iRoomInfo.get(iRoomIds[2]);
			selecteRoomData = new RoomData(data.iRoomID, data.iTableID, data.iType);
			iState = STATE_EXITING;
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_SEA: 
			data = MyArStation.iPlayer.iRoomInfo.get(iRoomIds[3]);
			selecteRoomData = new RoomData(data.iRoomID, data.iTableID, data.iType);
			iState = STATE_EXITING;
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_ROYAL: 
			data = MyArStation.iPlayer.iRoomInfo.get(iRoomIds[4]);
			selecteRoomData = new RoomData(data.iRoomID, data.iTableID, data.iType);
			iState = STATE_EXITING;
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_LOVER:
			data = MyArStation.iPlayer.iRoomInfo.get(iRoomIds[6]);
			selecteRoomData = new RoomData(data.iRoomID, data.iTableID, data.iType);
			iState = STATE_EXITING;
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_VIP:
//			data = iRoomInfo.get(iRoomIds[6]);
//			roomData = new RoomData(data.iRoomID, data.iTableID, data.iType);
//			if(listen != null) listen.OnSelectRoom(roomData);
//			data = null;
			iState = STATE_EXITING;
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_QUICKCHOOSEROOM_COLSE://关闭
			dimiss();
			return true;
		}
		return false;
	}
	
	public void setOnAnimateListen(OnAnimateListen l) {
		animateListen = l;
	}
	
	public interface OnSelectRoomListen {
		public void OnSelectRoom(RoomData data);
	}
	
}
