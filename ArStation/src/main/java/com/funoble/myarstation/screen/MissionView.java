package com.funoble.myarstation.screen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.funoble.myarstation.adapter.MissionAdapter;
import com.funoble.myarstation.adapter.MissionItem;
import com.funoble.myarstation.adapter.MissionAdapter.OnSubmitMissionListener;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.socket.protocol.MBRspMissionList;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.TPoint;
import com.funoble.myarstation.view.GameEventID;
import com.funoble.myarstation.view.MessageEventID;

/**
 * <p>
 * 
 * </p>
 */
public class MissionView extends BasePopScreen implements
		android.view.View.OnClickListener,
		OnTouchListener,
		OnSubmitMissionListener {
	private final int EVENTID_INIT = 6;
	private final int EVENTID_CLOSE = 7;
	private final int EVENTID_SHOW = 8;
	private final int EVENTID_HIDE = 9;
	private final int EVENTID_SHOWMSG = 10;
	
	private final int EVENTID_RSP_MISSION = 200;
	
	private Project iUiProject = null;
	private Scene  iDailyScene = null;
	private Scene  iBarScene = null;
	private ArrayList<SpriteButton> iDailyButtons = new ArrayList<SpriteButton>();
	private ArrayList<SpriteButton> iBarDailyButtons = new ArrayList<SpriteButton>();
	
	private final int MISSION_DAILY = 0;//通用任务
	private final int MISSION_BAR = 1;//酒吧任务
	private int iCurrentMissionPage = MISSION_DAILY; //
	private RelativeLayout mLayout;
	private RelativeLayout relativeLayout;
	private ListView iDailyMissionListView;
	private MissionAdapter iDailyAdapter;
	private ArrayList<MissionItem> iDailyMissionItems;
	
	private ListView iBarDailyMissionListView;
	private MissionAdapter iBarDailyAdapter;
	private ArrayList<MissionItem> iBarDailyMissionItems;
	
	private Context iContext;
	
	private int x = (int) (88 * ActivityUtil.ZOOM_X);
	private int y = (int) (121 * ActivityUtil.ZOOM_Y);
	private int width = (int) (616 * ActivityUtil.ZOOM_X);
	private int height = (int) (303 * ActivityUtil.ZOOM_Y);
	
	private MyHandler iHandler = new MyHandler();
	
	public  class MyHandler extends Handler {

		/* 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EVENTID_INIT:
				initSystemCom();
				break;
			case STATE_ENTERING:
				setSize(x+iSoX, y, width, height);
				break;
			case STATE_ENTERED:
				iSoX = 0;
				setSize(x+iSoX, y, width, height);
				break;
			case STATE_EXITING:
				mLayout.setVisibility(View.GONE);
				setSize(x+iSoX, y, width, height);
				break;
			case STATE_EXITED:
				setSize(x+iSoX, y, width, height);
				messageEvent(EVENTID_HIDE);
				break;
			case EVENTID_CLOSE:
//				isVision = false;
				mLayout.setVisibility(View.GONE);
//				mLayout.setFocusable(false);
				iState = STATE_EXITING;
				break;
			case EVENTID_HIDE:
				isVision = false;
				mLayout.setVisibility(View.GONE);
				mLayout.setFocusable(false);
				break;
			case EVENTID_SHOW:
				isVision = true;
				iSoX = iSoffsetX;
				iDailyScene.iCameraX = -iSoX;
				iBarScene.iCameraX = -iSoX;
				switchTab(MISSION_DAILY);
				MyArStation.mGameMain.mainLayout.bringChildToFront(mLayout);
				mLayout.setVisibility(View.VISIBLE);
				setSize(x+iSoX, y, width, height);
//				if(iDailyMissionItems != null && iDailyMissionItems.size() == 0 && iCurrentMissionPage == MISSION_DAILY) {
					requestMissionList(iCurrentMissionPage);
//				}
//				else if(iBarDailyMissionItems != null && iBarDailyMissionItems.size() == 0 && iCurrentMissionPage == MISSION_BAR) {
//					requestMissionList(iCurrentMissionPage);
//				}
				iState = STATE_ENTERING;
				break;
			case GameEventID.ESPRITEBUTTON_EVENT_DAILYMISSION:
				switchTab(MISSION_DAILY);
				if(iDailyMissionItems.size() == 0) {
					requestMissionList(iCurrentMissionPage);
				}
				break;
			case GameEventID.ESPRITEBUTTON_EVENT_BARMISSION:
				switchTab(MISSION_BAR);
				if(iBarDailyMissionItems.size() == 0) {
					requestMissionList(iCurrentMissionPage);
				}
				break;
			case EVENTID_RSP_MISSION:
				MBRspMissionList rspMissionList = (MBRspMissionList) msg.obj;
				if(rspMissionList.iMsg.length() > 0) {
					Tools.showSimpleToast(iContext, rspMissionList.iMsg);
				}
				int count = rspMissionList.iCount;
				int type = rspMissionList.iType;
				iDailyMissionItems.clear();
				if(type == MISSION_DAILY) {
					for(int i = 0; i < count; i++) {
						MissionItem item = new MissionItem(
								rspMissionList.iMissionIDs.get(i), 
								rspMissionList.iButtonStatus.get(i),
								rspMissionList.szLittlePics.get(i),
								rspMissionList.stMissionNames.get(i), 
								rspMissionList.stMissionDess.get(i));
						iDailyMissionItems.add(item);
					}
					iDailyAdapter.notifyDataSetChanged();
				}
				else {
					iBarDailyMissionItems.clear();
					for(int i = 0; i < count; i++) {
						MissionItem item = new MissionItem(
								rspMissionList.iMissionIDs.get(i), 
								rspMissionList.iButtonStatus.get(i),
								rspMissionList.szLittlePics.get(i),
								rspMissionList.stMissionNames.get(i), 
								rspMissionList.stMissionDess.get(i));
						iBarDailyMissionItems.add(item);
					}
					iBarDailyAdapter.notifyDataSetChanged();
				}
				break;
			case EVENTID_SHOWMSG:
				Tools.showSimpleToast(iContext, (String) msg.obj);
				break;
			default:
				break;
			}
		}
		
	}
	
	private void SendMessage(int EventId) {
		Message message = new Message();
		message.what = EventId;
		iHandler.sendMessage(message);
	}
	
	private void SendMessage(int EventId, Object object) {
		Message message = new Message();
		message.what = EventId;
		message.obj = object;
		iHandler.sendMessage(message);
	}
	/**
	 * @param context
	 */
	public MissionView(Context context) {
		iContext = context;
		init();
		SendMessage(EVENTID_INIT);
//		switchTab(currentPageIndex);
	}
	
	
	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#init()
	 */
	@Override
	public void init() {
		super.init();
	}

	/**
	 * @param context
	 */
	private void initSystemCom() {
		mLayout = (RelativeLayout) LayoutInflater.from(iContext).inflate(
				R.layout.new_mission, null);
		mLayout.setVisibility(View.GONE);
		MyArStation.mGameMain.mainLayout.addView(mLayout);
		relativeLayout =  (RelativeLayout) mLayout.findViewById(R.id.llmission);
		iDailyMissionListView = (ListView) mLayout.findViewById(R.id.dailyMission);
		iDailyMissionListView.setVisibility(View.GONE);
		iDailyMissionListView.setBackgroundColor(Color.TRANSPARENT);
		iBarDailyMissionListView = (ListView) mLayout.findViewById(R.id.barMission);
		iBarDailyMissionListView.setVisibility(View.GONE);
		iBarDailyMissionListView.setBackgroundColor(Color.TRANSPARENT);
		
		iDailyMissionItems = new ArrayList<MissionItem>();
//		for(int i = 0; i < 10; i++) {
//			MissionItem item = new MissionItem(5, 1, "", "每日任务test(完成)"+i , "奖励反劈卡");
//			iDailyMissionItems.add(item);
//		}
		iDailyAdapter = new MissionAdapter(iContext, iDailyMissionItems);
		iDailyAdapter.setOnSubmitListen(this);
		iDailyMissionListView.setAdapter(iDailyAdapter);
		
		iBarDailyMissionItems = new ArrayList<MissionItem>();
//		for(int i = 0; i < 10; i++) {
//			MissionItem item = new MissionItem(0, 0, "", "酒吧任务test"+i , "奖励反劈卡");
//			iBarDailyMissionItems.add(item);
//		}
		iBarDailyAdapter = new MissionAdapter(iContext, iBarDailyMissionItems);
		iBarDailyAdapter.setOnSubmitListen(this);
		iBarDailyMissionListView.setAdapter(iBarDailyAdapter);
		setSize(x+iSoX, y, width, height);
	}
	/**
	 * 
	 */
	private void setSize(int x, int y, int w, int h) {
//		if(true)return;
		LayoutParams lParams = (LayoutParams) iDailyMissionListView.getLayoutParams();
		lParams.leftMargin = x;
		lParams.topMargin = y;
		lParams.width = w;
		lParams.height = h;
		iDailyMissionListView.setLayoutParams(lParams);
		iDailyMissionListView.invalidate();
		iBarDailyMissionListView.setLayoutParams(lParams);
		iBarDailyMissionListView.invalidate();
	}
	
	public void dismiss() {
		SendMessage(EVENTID_CLOSE);
	}
	/* 
	 * @see android.app.Dialog#show()
	 */
	public void show() {
		SendMessage(EVENTID_SHOW);
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#messageEvent(int, java.lang.Object)
	 */
	@Override
	protected void messageEvent(int aEventID, Object aMsg) {
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#messageEvent(int)
	 */
	@Override
	protected void messageEvent(int aEventID) {
		SendMessage(aEventID);
	}
	
	private void switchTab(int current) {
		iCurrentMissionPage = current;
		iDailyMissionListView.setVisibility(iCurrentMissionPage == MISSION_DAILY ? View.VISIBLE: View.GONE);
		iBarDailyMissionListView.setVisibility(iCurrentMissionPage == MISSION_DAILY ? View.GONE: View.VISIBLE);
	}

	/* 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(mLayout.getVisibility() == View.VISIBLE) return true;
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#loadPak()
	 */
	@Override
	protected void loadPak() {
		iUiProject = CPakManager.loadBulletinPak();
		iDailyScene = iUiProject.getScene(2);
		if(iDailyScene != null) {
			iDailyScene.setViewWindow(0, 0, 800, 480);
			iDailyScene.initDrawList();
			iDailyScene.initNinePatchList(null);
			ViewRect = iDailyScene.getLayoutMap(Scene.RectangleLayout).getNineRect(0);
			if(ViewRect != null) {
				iSoffsetX = ActivityUtil.SCREEN_WIDTH - ViewRect.left;
				iSoX = iSoffsetX;
				iSoSpeed = (iSoX / 6);
				iDailyScene.iCameraX = -iSoX;
			}
			//
			Vector<?> pSpriteList = null;
			Map pMap = iDailyScene.getLayoutMap(Scene.SpriteLayout);
			pSpriteList = pMap.getSpriteList();
			Sprite pSprite;
			for (int i=0; i<pSpriteList.size(); i++) {
				pSprite = (Sprite)pSpriteList.elementAt(i);
				int pX = pSprite.getX();
				int pY = pSprite.getY();
				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_BULLETIN + pSprite.getIndex();
				TPoint pPoint = new TPoint();
				pPoint.iX = pX;
				pPoint.iY = pY;
				//精灵按钮
				SpriteButton pSpriteButton;
				pSpriteButton = new SpriteButton(pSprite);
				pSpriteButton.setEventID(pEventID);
				pSpriteButton.setPosition(pX, pY);
				pSpriteButton.setHandler(this);
				iDailyButtons.add(pSpriteButton);
			}
		}
		
		iBarScene = iUiProject.getScene(3);
		if(iBarScene != null) {
			iBarScene.setViewWindow(0, 0, 800, 480);
			iBarScene.initDrawList();
			iBarScene.initNinePatchList(null);
			ViewRect = iDailyScene.getLayoutMap(Scene.RectangleLayout).getNineRect(0);
			if(ViewRect != null) {
				iSoffsetX = ActivityUtil.SCREEN_WIDTH - ViewRect.left;
				iSoX = iSoffsetX;
				iSoSpeed = (iSoX / 6);
				iBarScene.iCameraX = -iSoX;
			}
			Map pMap = iBarScene.getLayoutMap(Scene.SpriteLayout);
			Vector<?> pSpriteList = pMap.getSpriteList();
			Sprite pSprite;
			for (int i=0; i<pSpriteList.size(); i++) {
				pSprite = (Sprite)pSpriteList.elementAt(i);
				int pX = pSprite.getX();
				int pY = pSprite.getY();
				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_BULLETIN + pSprite.getIndex();
				TPoint pPoint = new TPoint();
				pPoint.iX = pX;
				pPoint.iY = pY;
				//精灵按钮
				SpriteButton pSpriteButton;
				pSpriteButton = new SpriteButton(pSprite);
				pSpriteButton.setEventID(pEventID);
				pSpriteButton.setPosition(pX, pY);
				pSpriteButton.setHandler(this);
				iBarDailyButtons.add(pSpriteButton);
			}
		}
		
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#releaseResource()
	 */
	@Override
	public void releaseResource() {
		super.releaseResource();
		iUiProject = null;
		iSpriteButtonList = null;
		iDailyButtons = null;
		iBarDailyButtons = null;
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#paintScreen(android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	public void paintScreen(Canvas g, Paint paint) {
		super.paintScreen(g, paint);
		if(!isVision) return;
		g.drawRect(rect, iPaint);
		if(iCurrentMissionPage == MISSION_DAILY) {
			if(iDailyScene != null) {
				iDailyScene.paint(g, iDailyScene.iCameraX, 0);
				if(iDailyButtons != null) {
					for(int i = 0; i < iDailyButtons.size(); i++) {
						iDailyButtons.get(i).paint(g, iDailyScene.iCameraX, 0);
					}
				}
			}
		}
		else {
			if(iBarScene != null) {
				iBarScene.paint(g, iBarScene.iCameraX, 0);
				if(iBarDailyButtons != null) {
					for(int i = 0; i < iBarDailyButtons.size(); i++) {
						iBarDailyButtons.get(i).paint(g, iBarScene.iCameraX, 0);
					}
				}
			}
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
				messageEvent(STATE_ENTERING);
			}
			break;
		case STATE_ENTERED:
			iSoX = 0;
			messageEvent(STATE_ENTERED);
			break;
		case STATE_EXITING:
			if(iSoX < iSoffsetX) {
				iSoX += iSoSpeed;
				messageEvent(STATE_EXITING);
			}
			else {
				iState = STATE_EXITED;
			}
			break;
		case STATE_EXITED:
			iSoX = iSoffsetX;
			iState = STATE_INVALID;
			messageEvent(STATE_EXITED);
			isVision =false;
			break;

		default:
			break;
		}
		performButton();
	}
	/**
	 * 
	 */
	private void performButton() {
		if(iDailyScene != null) {
			iDailyScene.handle();
			iDailyScene.iCameraX = -iSoX;
			if(iDailyButtons != null) {
				for(int i = 0; i < iDailyButtons.size(); i++) {
					iDailyButtons.get(i).perform();
				}
			}
		}
		if(iBarScene != null) {
			iBarScene.handle();
			iBarScene.iCameraX = -iSoX;
			if(iBarDailyButtons != null) {
				for(int i = 0; i < iBarDailyButtons.size(); i++) {
					iBarDailyButtons.get(i).perform();
				}
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
		Rect pRect = new Rect();
		SpriteButton pSB;
		if(null != ViewRect && ViewRect.contains(aX, aY)) {
			for(int i=0; i<iDailyButtons.size(); i++) {
				pSB = ((SpriteButton)iDailyButtons.get(i));
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
			for(int i=0; i<iBarDailyButtons.size(); i++) {
				pSB = ((SpriteButton)iBarDailyButtons.get(i));
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
			dismiss();
			return true;
		}
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_PIC:
			if(iDailyAdapter != null && iBarDailyAdapter != null) {
				iDailyAdapter.notifyDataSetChanged();
				iBarDailyAdapter.notifyDataSetChanged();
			}
			break;

		default:
			break;
		}
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#OnProcessCmd(java.lang.Object, int, int)
	 */
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
		MobileMsg pMobileMsg = (MobileMsg) socket;
		switch (aMobileType) {
		case IM.IM_RESPONSE:
		{
			switch (aMsgID) {
			case RspMsg.MSG_RSP_MISSION_LIST:
				MBRspMissionList rspMissionList = (MBRspMissionList)pMobileMsg.m_pMsgPara;
				if(rspMissionList == null) {
					return;
				}
				Tools.println(rspMissionList.toString());
				SendMessage(EVENTID_RSP_MISSION, rspMissionList);
				break;
			}
		}
		}
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#ItemAction(int)
	 */
	@Override
	public boolean ItemAction(int aEventID) {
		if(!isVision || !ibTouch) return false;
		switch (aEventID) {
		case GameEventID.ESPRITEBUTTON_EVENT_BARMISSION:
			if(iCurrentMissionPage != MISSION_BAR) {
				messageEvent(GameEventID.ESPRITEBUTTON_EVENT_BARMISSION);
			}
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_DAILYMISSION:
			if(iCurrentMissionPage != MISSION_DAILY) {
				messageEvent(GameEventID.ESPRITEBUTTON_EVENT_DAILYMISSION);
			}
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_BULLETIN_CLOSE:
			dismiss();
			return true;
		default:
			return false;
		}
	}
	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#dimiss()
	 */
	@Override
	public void dimiss() {
		dismiss();
	}
	
	private void requestMissionList(int type) {
		MyArStation.iGameProtocol.RequestMissionList(type);
	}

	/* 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
	}

	/* 
	 * @see com.funoble.lyingdice.adapter.MissionAdapter.OnSubmitMissionListener#onSubmitMission(com.funoble.lyingdice.adapter.MissionItem)
	 */
	@Override
	public void onSubmitMission(MissionItem item) {
		if(item == null) return;
//		SendMessage(EVENTID_SHOWMSG,  item.toString());
		MyArStation.iGameProtocol.RequestAward(item.missionId);
		if(item.missionType == MISSION_DAILY) {
			iDailyMissionItems.remove(item);
			iDailyAdapter.notifyDataSetChanged();
		}
		else {
			iBarDailyMissionItems.remove(item);
			iBarDailyAdapter.notifyDataSetChanged();
		}
//		dismiss();
	}
}
