package com.funoble.myarstation.screen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.funoble.myarstation.common.MD5;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.common.Util;
import com.funoble.myarstation.common.AccountStore.AccountInfo;
import com.funoble.myarstation.download.HttpDownloadManager;
import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.GameButtonHandler;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.gamelogic.SpriteButtonSelect;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.socket.GameProtocol;
import com.funoble.myarstation.socket.protocol.MBRspLogin;
import com.funoble.myarstation.socket.protocol.MBRspResetThirdUserInfo;
import com.funoble.myarstation.socket.protocol.MBRspThirdLogin;
import com.funoble.myarstation.socket.protocol.MBRspUpdate;
import com.funoble.myarstation.socket.protocol.MBRspVisitorLogin;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.SocketErrorType;
import com.funoble.myarstation.socket.protocol.ProtocolType.Url;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.update.DownloadProgressListener;
import com.funoble.myarstation.update.Downloader;
import com.funoble.myarstation.update.FileDownloader;
import com.funoble.myarstation.update.LoadInfo;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.TPoint;
import com.funoble.myarstation.utils.TextUtil;
import com.funoble.myarstation.utils.URLAvailability;
import com.funoble.myarstation.view.GameEventID;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.MessageEventID;
import com.funoble.myarstation.view.MyProgressBar;
import com.funoble.myarstation.view.ReRegistDialog;
import com.funoble.myarstation.view.ReRegistDialog.Reregist;

public class LoginScreen extends GameView implements GameButtonHandler, TextWatcher, Reregist {
	private final String filePath = Environment.getExternalStorageDirectory().getPath()+File.separator+"/lyingdice/download/";
	private GameCanvas iGameCanvas = null;
	private Project iUIPak = null;
	private Scene iScene = null;
//	private Scene iScene2 = null;
	private Scene iSceneLoading = null;
	private Sprite iHartSprite = null;	//飞心动画
	private Vector<SpriteButton> iSpriteButtonList = new Vector<SpriteButton>();//精灵按钮 列表
	MobileMsg iCacheMsg;
	byte[] data;
	private SharedPreferences iSP;
	private EditText iETID;//账号输入框
	private EditText iETPSW;//密码输入框
	private Paint iLoadingPaint;//
	private Context iContext = MyArStation.mGameMain;
	private MyProgressBar bar;
	private Downloader downloader;
	public  Dialog updateDialog;
	private MBRspLogin iRspLogin;
	private int iServerVersion = 0;
	private int iGameState = 0; //0 --- 出场    1 --- 正常    2 --- 正在登录    3 --- 登录成功
	private int iGameTick = 0;
	private int	iLoginDelay = 0; //登录状态的延迟	
	private RelativeLayout absoluteLayout = null;
	private boolean iHasReconnected = true;	//是否已经进行重连
	private boolean iInitOK = false; //是否允许显示系统控件
	private boolean iAutoLogin = true;
	private String	iIPString = Url.ip;
	
	private final String	FAKE_PWD	= "••••••";
	private SpriteButtonSelect iSavePWD = null; //喊斋按钮
	private ReRegistDialog  registDialog;//第三方帐户需要重置用户信息，转成公版用户
	private boolean ibShowDialog = false;	//是否显示对话框
	
	
	//背景动画
	private final int MAX_CIRCEL_COUNT = 12;
	private int iX[] = null;
	private int iY[] = null;
	private int iSpeedX[] = null;
	private int iSpeedY[] = null;
	private int iRadius[] = null;
	private int iSthID[] = null;
	
	private boolean isNewVisitor = false;
	
	private int iChannelID = 131;   //
	
	private boolean isTest = true;
	
	//
	public LoginScreen() {
	}
	
	public LoginScreen(boolean aAutoLogin) {
		iAutoLogin = aAutoLogin;
	}

	@Override
	public void init() {
		Tools.debug("LoginScreen::init()::start");
		//
		iGameCanvas = GameCanvas.getInstance();
		//载入背景图
		MyArStation.iImageManager.loadLoginBack();
		//
		Tools.debug("LoginScreen::init() 1");
		//
		downloader = null;
		mViewId = 0;
		iLoadingPaint = new Paint();
		iLoadingPaint.setColor(Color.BLACK);
		iSP = MyArStation.getInstance().getPreferences(Activity.MODE_PRIVATE);
		Tools.debug("LoginScreen::init()::start init loadUIPak()");
		loadUIPak();
		Tools.debug("LoginScreen::init()::end init loadUIPak()");
		if(MyArStation.iGameProtocol != null) {
			MyArStation.iGameProtocol.stop();
		}
		iCacheMsg = new MobileMsg();
		if(MyArStation.iHttpDownloadManager != null) {
			MyArStation.iHttpDownloadManager.cleanTask();
		}	
		//初始化游戏数据
		MyArStation.iPlayer.ibInit = false;
		MyArStation.iPlayer.reset();
		MyArStation.msgManager.setClean(true);
		MyArStation.msgManager.stopMsgTask();
		if(MyArStation.eventSelectDialog != null) {
			MyArStation.eventSelectDialog.dismiss();
		}
		//
		Tools.debug("LoginScreen::init() 2");
		//是否有帐号
		isNewVisitor = isNewVisitor();
		//
		Tools.debug("LoginScreen::init() 3");
		//
		iX = new int[MAX_CIRCEL_COUNT];
		iY = new int[MAX_CIRCEL_COUNT];
		iSpeedX = new int[MAX_CIRCEL_COUNT];
		iSpeedY = new int[MAX_CIRCEL_COUNT];
		iRadius = new int[MAX_CIRCEL_COUNT];
		iSthID = new int[MAX_CIRCEL_COUNT];
		for(int i=0; i<MAX_CIRCEL_COUNT; i++) {
			iX[i] = (int)(ActivityUtil.SCREEN_WIDTH * Math.random());
			iY[i] = (int)(ActivityUtil.SCREEN_HEIGHT + Math.random() * 30);
			iSthID[i] = 1;
			int temp = (int)(Math.random() * 10);
			if(temp >= 8) {
				iSpeedX[i] = -1;
				iSpeedY[i] = -1;
			}
			else if(temp >= 5) {
				iSpeedX[i] = 1;
				iSpeedY[i] = -1;
			}
			else if(temp >= 3) {
				iSpeedX[i] = 0;
				iSpeedY[i] = -2;
			}
			else {
				iSpeedX[i] = 0;
				iSpeedY[i] = -1;
			}
			iRadius[i] = (int)(8 * ActivityUtil.DENSITY_ZOOM_Y * Math.random() + 8);
			iSthID[i] = 0;
		}
		//
		iInitOK = true;
		//载入数据,并自动登陆
		messageEvent(MessageEventID.EMESSAGE_EVENT_LOGIN_LOADDATA);
		//
		//MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusic(SoundsResID.bg1, true);
		//
		Tools.debug("LoginScreen::init() end");
	}
	
	@Override
	public void releaseResource() {
		downloader = null;
		iX = null;
		iY = null;
		iSpeedX = null;
		iSpeedY = null;
		iRadius = null;
		iHartSprite = null;
		iSceneLoading = null;
		iScene = null;
//		iScene2 = null;
		iUIPak = null;
//		iSthID = null;
		iSavePWD = null;
		if(registDialog != null) {
			registDialog.dismiss();
			registDialog = null;
		}
//		GameMain.iImageManager.iBackLoginBmp = null;
	}

	@Override
	public void paintScreen(Canvas g, Paint paint) {
		g.drawBitmap(MyArStation.iImageManager.iBackLoginBmp,
				0,
				0,
				null);
		if(!iInitOK) {
			return;
		}
		//登录状态
		if(iGameState == 2) {
			if(iSceneLoading != null) {
				iSceneLoading.paint(g, 0, 0);
			}
//			g.drawText("正在登录...", 278*ActivityUtil.ZOOM_X, 288*ActivityUtil.ZOOM_Y, ActivityUtil.mLoading);
		}
		//登录成功状态
		else if(iGameState == 3) {
			if(iSceneLoading != null) {
				iSceneLoading.paint(g, 0, 0);
			}
//			g.drawText("登录成功", 278*ActivityUtil.ZOOM_X, 288*ActivityUtil.ZOOM_Y, ActivityUtil.mLoading);			
		}
		//正常状态
		else {
			if(iScene != null) {
				for(int i=0; i<MAX_CIRCEL_COUNT; i++) {
					g.drawCircle(iX[i]+iRadius[i], iY[i]+iRadius[i], iRadius[i], ActivityUtil.mAlphaRect);
					if(iSthID[i] > 0) {
						iHartSprite.paint(g, iX[i]+iRadius[i], iY[i]+iRadius[i]);
					}
				}
				//
				iScene.paint(g, 0, 0);
			}
			for(int i=0; i<iSpriteButtonList.size(); i++) {
				((SpriteButton)iSpriteButtonList.elementAt(i)).paint(g);
			}
			if(iSavePWD != null) {
				iSavePWD.paint(g);
			}
		}
	}

	@Override
	public void performL() {
//		if(isReload) {
//			isReload = false;
//			iScene2 = null;
//			if(iSpriteButtonList != null) iSpriteButtonList.clear();
//			loadUIPak();
//		}
		//进场状态
		if(iGameState == 0) {
			iGameTick ++;
			if(iGameTick > 1) {
				iGameTick = 0;
				//正常状态
				iGameState = 1;
				//显示系统控件
				messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM);
			}
			if(iScene != null) {
				iScene.handle();
			}
			for(int i=0; i<iSpriteButtonList.size(); i++) {
				((SpriteButton)iSpriteButtonList.elementAt(i)).perform();
			}
			if(iSavePWD != null) {
				iSavePWD.perform();
			}
		}
		//正常状态
		else if(iGameState == 1) {
			if(iScene != null) {
				iScene.handle();
			}
			for(int i=0; i<iSpriteButtonList.size(); i++) {
				((SpriteButton)iSpriteButtonList.elementAt(i)).perform();
			}
			if(iSavePWD != null) {
				iSavePWD.perform();
			}
			for(int i=0; i<MAX_CIRCEL_COUNT; i++) {
				if(iSpeedX == null) {
					break;
				}
				iX[i] += iSpeedX[i];
				iY[i] += iSpeedY[i];
				if(iY[i] <= -80) {
					iX[i] = (int)(ActivityUtil.SCREEN_WIDTH * Math.random());
					iY[i] = (int)(ActivityUtil.SCREEN_HEIGHT + Math.random() * 100);
					iRadius[i] = (int)(20 * ActivityUtil.DENSITY_ZOOM_Y * Math.random() + 8);
					if(iRadius[i] < 10) {
						iRadius[i] = 10;
					}
					iSthID[i] = 1;
					int temp = (int)(Math.random() * 10);
					if(temp >= 8) {
						iSpeedX[i] = -1;
						iSpeedY[i] = -1;
					}
					else if(temp >= 6) {
						iSpeedX[i] = 1;
						iSpeedY[i] = -1;
					}
					else if(temp >= 4) {
						iSpeedX[i] = 0;
						iSpeedY[i] = -2;
					}
					else if(temp == 2) {
						iSpeedX[i] = 0;
						iSpeedY[i] = -2;
						iSthID[i] = 1;
						iRadius[i] = (int)(16 * ActivityUtil.DENSITY_ZOOM_Y);
					}
					else if(temp == 1) {
						iSpeedX[i] = 0;
						iSpeedY[i] = -1;
						iSthID[i] = 1;
						iRadius[i] = (int)(16 * ActivityUtil.DENSITY_ZOOM_Y);
					}
					else {
						iSpeedX[i] = 0;
						iSpeedY[i] = -1;
					}
				}
			}
		}
		//登录状态
		else if(iGameState > 1) {
			if(iSceneLoading != null) {
				iSceneLoading.handle();
			}
			//如果不在等待输入帐号信息状态
			if(!ibShowDialog) {
				//10秒钟内强行切换状态
				iLoginDelay ++;
				if(iLoginDelay > 160) {
					Tools.debug("LoginScreen::performL()::Login Time Out!");
					//
					MyArStation.iGameProtocol.stop();
					//
					iLoginDelay = 0;
					//将状态改成登正常
					iGameState = 1;
					//强行改成老用户
					isNewVisitor = false;
					//显示系统控件
					messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_REBUILD_SYSCOM);
				}
			}
			else {
				iLoginDelay = 0;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
//		Tools.debug("ProgressScreen onDown event.getAction() = " +e.getAction());
		boolean result= false;
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		result = pointerPressed(aX, aY);
		//test---
//		if(result == false) {
//			iLightX = aX;
//			iLightY = aY;
//			iLightState = 0;
//		}
		//test---
		return result;
	}

	@Override
	public boolean onLongPress(MotionEvent e) {
//		Tools.debug("ProgressScreen onLongPress event.getAction() = " +e.getAction());
		boolean result= false;
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		result = pointerReleased(aX, aY);
		return result;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
//		Tools.debug("ProgressScreen onSingleTapUp event.getAction() = " +e.getAction());
		boolean result= false;
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		result = pointerReleased(aX, aY);
		return result;
	}

	@Override
	public boolean pointerPressed(int aX, int aY) {
		Tools.debug("LoginScreen:: pointerPressed");
		if(iGameState != 1) {
			return true;
		}
		boolean result= false;
		Rect pRect = new Rect();
		SpriteButton pSB;
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
				pSB.pressed();
//				Tools.debug("ProgressScreen pointerPressed  pSB.pressed() EventID=" + pSB.getEventID());
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
				result = true;
				break;
			}
		}
		if(iSavePWD != null && iSavePWD.isVision()) {
			int pX = iSavePWD.getX();
			int pY = iSavePWD.getY();
			Rect pLogicRect = iSavePWD.getRect();
			pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
					pX+pLogicRect.right, pY+pLogicRect.bottom);
			if (pRect.contains(aX, aY)) {
				iSavePWD.pressed();
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
				result = true;
			}
		}
		return result;
	}

	@Override
	public boolean pointerReleased(int aX, int aY) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	//按下按钮
	public boolean ItemAction(int aEventID) {
		switch (aEventID) {
		case GameEventID.ESPRITEBUTTON_EVENT_LOGIN_LOGIN:
			{
				iHasReconnected = false;
				//登录状态
				iGameState = 2;
				iLoginDelay = 0;
				//隐藏系统控件
				messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_HIDE_SYSCOM);
				//if(isNewVisitor) {
				//	requestThreeLogin(Util.getUUID(GameMain.mGameMain.getApplicationContext()), 0);
				//}
				//else {
					requestLogin();
				//}
				return true;
			}
		case GameEventID.ESPRITEBUTTON_EVENT_LOGIN_REGISTER:
			{
//				Tools.debug("START ESPRITEBUTTON_EVENT_LOGIN_REGISTER");
//				MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//				Tools.debug("hideSysCom");
				//注册时，不需要重连
				iHasReconnected = true;
				//隐藏系统控件
				messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_HIDE_SYSCOM);
				//切换GameCanvas
//				messageEvent(MessageEventID.EMESSAGE_EVENT_TO_REGISTER);
				GameCanvas.getInstance().changeView(new RegisterScreen());
				return true;
			}
		case GameEventID.ESPRITEBUTTON_EVENT_LOGIN_GUEST://游客
			{
				//登录状态
				//iGameState = 2;
				//iLoginDelay = 0;
				//隐藏系统控件
				//messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_HIDE_SYSCOM);
				//
				//iReconnectType = 1;
				//发送登录请求
				//requestVisitorLogin();
				return true;
			}
		case GameEventID.ESPRITEBUTTON_EVENT_LOGIN_AUTO_LOGIN:
			{
				return true;
			}
		}
		return false;
	}
	
	private void loadUIPak() {
		iSceneLoading = LoadLoading();
		iUIPak = CPakManager.loadUIPak();
		if (iUIPak != null) {
//			if(!isNewVisitor) {
				iScene = iUIPak.getScene(1);
				if(iScene != null) {
					iScene.setViewWindow(0, 0, 800, 480);
					iScene.initDrawList();
					iScene.initNinePatchList(new int[] {R.drawable.btmck, R.drawable.new_text_bg, R.drawable.new_text_bg});
					Map pMap = iScene.getLayoutMap(Scene.SpriteLayout);
					Vector<?> pSpriteList = pMap.getSpriteList();
					Sprite pSprite;
					for (int i=0; i<pSpriteList.size(); i++) {
						pSprite = (Sprite)pSpriteList.elementAt(i);
						int pX = pSprite.getX();
						int pY = pSprite.getY();
						int pEventID = GameEventID.ESPRITEBUTTON_EVENT_LOGIN_OFFSET + pSprite.getIndex();
						TPoint pPoint = new TPoint();
						pPoint.iX = pX;
						pPoint.iY = pY;
						//精灵按钮
						if(pSprite.getIndex() == 74) {
							iSavePWD = new SpriteButtonSelect(pSprite);
							iSavePWD.setEventID(GameEventID.ESPRITEBUTTON_EVENT_LOGIN_AUTO_LOGIN);
							iSavePWD.setPosition(pX, pY);
							iSavePWD.setHandler(this);
							if(MyArStation.mCurrAccountInfo != null) {
								iSavePWD.setSelected(MyArStation.mCurrAccountInfo.isSavedPwd());
							}
						}
						else {
							SpriteButton pSpriteButton;
							pSpriteButton = new SpriteButton(pSprite);
							pSpriteButton.setEventID(pEventID);
							pSpriteButton.setPosition(pX, pY);
							pSpriteButton.setHandler(this);
							iSpriteButtonList.addElement(pSpriteButton);
						}
					}
				}
//			}
//			else {
//				iScene2 = iUIPak.getScene(24);
//				if(iScene2 != null) {
//					iScene2.setViewWindow(0, 0, 800, 480);
//					iScene2.initDrawList();
//					iScene.initNinePatchList(new int[] {R.drawable.btmck, R.drawable.new_text_bg, R.drawable.new_text_bg});
//					Map pMap = iScene2.getLayoutMap(Scene.SpriteLayout);
//					Vector<?> pSpriteList = pMap.getSpriteList();
//					Sprite pSprite;
//					for (int i=0; i<pSpriteList.size(); i++) {
//						pSprite = (Sprite)pSpriteList.elementAt(i);
//						int pX = pSprite.getX();
//						int pY = pSprite.getY();
//						int pEventID = GameEventID.ESPRITEBUTTON_EVENT_LOGIN_OFFSET + pSprite.getIndex();
//						TPoint pPoint = new TPoint();
//						pPoint.iX = pX;
//						pPoint.iY = pY;
//						//精灵按钮
//						if(pSprite.getIndex() == 74) {
//						}
//						else {
//							SpriteButton pSpriteButton;
//							pSpriteButton = new SpriteButton(pSprite);
//							pSpriteButton.setEventID(pEventID);
//							pSpriteButton.setPosition(pX, pY);
//							pSpriteButton.setHandler(this);
//							iSpriteButtonList.addElement(pSpriteButton);
//						}
//					}
//				}
//			}
			
			iHartSprite = iUIPak.getSprite(62);	//飞心动画
		}
	}

	//显示系统组件
	private void displaySysCom() {
		if(iETID != null && iETPSW != null) {
			return;
		}
		float pxETIDX = 318; //800*480的像素值
		float pxETIDY = 248;
		float pxETPSWY = 300;
		float pxETIDH = 34;
		float pxETIDW = 242;
		
		//转成对应屏幕分辨率的值
		pxETIDX *= ActivityUtil.ZOOM_X; 
		pxETIDY *= ActivityUtil.ZOOM_Y;
		pxETPSWY *= ActivityUtil.ZOOM_Y;
		pxETIDW *= ActivityUtil.ZOOM_X; 
		pxETIDH *= ActivityUtil.ZOOM_Y;
		if(iETID == null) {
			iETID = (EditText) absoluteLayout.findViewById(R.id.editTextID);
			iETID.addTextChangedListener(this);
			if (iETID != null) {
				iETID.setVisibility(View.VISIBLE);
				LayoutParams lp = (LayoutParams) iETID.getLayoutParams();
				lp.leftMargin = (int) pxETIDX;//(int) (lp.x*ActivityUtil.DENSITY_ZOOM_X);
				lp.topMargin = (int) pxETIDY;//(int) (lp.y*ActivityUtil.DENSITY_ZOOM_Y);
				lp.width = (int) pxETIDW;
				lp.height = (int) pxETIDH;
				iETID.setLayoutParams(lp);
			}
		}
		if(iETPSW == null) {
			iETPSW = (EditText) absoluteLayout.findViewById(R.id.editTextPSW);
			if (iETPSW != null) {
				iETPSW.setVisibility(View.VISIBLE);
				LayoutParams lp = (LayoutParams) iETPSW.getLayoutParams();
				lp.leftMargin = (int) pxETIDX;//(int) (lp.x*ActivityUtil.DENSITY_ZOOM_X);
				lp.topMargin = (int) pxETPSWY;//(int) (lp.y*ActivityUtil.DENSITY_ZOOM_Y);
				lp.width = (int) pxETIDW;
				lp.height = (int) pxETIDH;
				iETPSW.setLayoutParams(lp);
			}
		}
	}
	
	//隐藏系统组件
	private void hideSysCom() {
		if (iETID != null) {
			iETID.setVisibility(View.INVISIBLE);
		}
		if (iETPSW != null) {
			iETPSW.setVisibility(View.INVISIBLE);
		}
	}
	
	//显示控件
	private void showSysCom() {
		if (iETID != null) {
			iETID.setVisibility(View.VISIBLE);
		}
		if (iETPSW != null) {
			iETPSW.setVisibility(View.VISIBLE);
		}
	}
	
	//测试模式登录
	private void loadTestData() {
		Tools.debug("LoginScreen::loadTestData()::Start");
		iHasReconnected = false;
	    //登录状态
		iGameState = 2;
		iLoginDelay = 0;
		//隐藏系统控件
		hideSysCom();
		Tools.debug("LoginScreen::loadTestData()::1");
		requestLoginTest();
		
		
//		String tIDStr = iSP.getString(MyArStation.mGameMain.getString(R.string.Key_SaveData_Login_ID), null);
//		String tPSWStr = iSP.getString(MyArStation.mGameMain.getString(R.string.Key_SaveData_Login_Password), null);
//		Tools.debug("LoginScreen::loadTestData()::2");
//		if(tIDStr != null && tPSWStr != null) {
//			MyArStation.mCurrAccountInfo.setName(tIDStr);
//			MyArStation.mCurrAccountInfo.setPwdLength((byte) (tPSWStr.length()));
//			byte[] md5One = MD5.toMD5(tPSWStr); // 2次md5加密
//			MyArStation.mCurrAccountInfo.setInputPwd(MD5.toMD5(md5One));
//			MyArStation.mCurrAccountInfo.setInputPwdOne(md5One);
//			MyArStation.mCurrAccountInfo.setSucceedPwd(md5One);
//			MyArStation.mAccountStore.pushAndUpdate(MyArStation.mCurrAccountInfo);
//			MyArStation.mAccountStore.commit();
//		}
//		Tools.debug("LoginScreen::loadTestData()::3");
//		if(iETID != null) {
//			iETID.setText(MyArStation.mCurrAccountInfo.getName());
//		}
//		byte[] bytes =  MyArStation.mCurrAccountInfo.getSucceedPwd();
//		if(iETPSW != null) {
//			iETPSW.setText(bytes != null && bytes.length > 0 ? FAKE_PWD : "");
//		}
//		Tools.debug("LoginScreen::loadTestData()::4");
//		if(iETID != null && iETPSW != null) {
//			if(TextUtils.isEmpty(MyArStation.mCurrAccountInfo.getName()) && 
//					!TextUtils.isEmpty(MyArStation.mCurrIMEIAccountInfo.getName())) {
//				iETID.setText(MyArStation.mCurrIMEIAccountInfo.getName());
//				bytes =  MyArStation.mCurrIMEIAccountInfo.getSucceedPwd();
//				iETPSW.setText(bytes != null && bytes.length > 0 ? FAKE_PWD : "");
//			}
//		}
//		Tools.debug("LoginScreen::loadTestData()::5");
////		if(!iAutoLogin) {
////			return;
////		}
//		Tools.debug("LoginScreen::loadData()::6");
//		iHasReconnected = false;
//		//登录状态
//		iGameState = 2;
//		iLoginDelay = 0;
//		//隐藏系统控件
//		hideSysCom();
//		Tools.debug("LoginScreen::loadTestData()::7");
////		//已有账号
//		if (!TextUtils.isEmpty(MyArStation.mCurrAccountInfo.getName())) {
//			Tools.debug("LoginScreen::loadTestData()::8");
//			if((bytes != null && bytes.length > 0)) {
//				Tools.debug("LoginScreen::loadTestData()::9");
//				requestLoginTest();
//				Tools.debug("LoginScreen::loadTestData()::10");
//			}
//			Tools.debug("LoginScreen::loadTestData()::11");
//		}
		Tools.debug("LoginScreen::loadTestData()::End");
	}
	
	
	//载入账号密码
	private void loadData() {
		Tools.debug("LoginScreen::loadData()::1");
		String tIDStr = iSP.getString(MyArStation.mGameMain.getString(R.string.Key_SaveData_Login_ID), null);
		String tPSWStr = iSP.getString(MyArStation.mGameMain.getString(R.string.Key_SaveData_Login_Password), null);
		Tools.debug("LoginScreen::loadData()::2");
		if(tIDStr != null && tPSWStr != null) {
			MyArStation.mCurrAccountInfo.setName(tIDStr);
			MyArStation.mCurrAccountInfo.setPwdLength((byte) (tPSWStr.length()));
			byte[] md5One = MD5.toMD5(tPSWStr); // 2次md5加密
			MyArStation.mCurrAccountInfo.setInputPwd(MD5.toMD5(md5One));
			MyArStation.mCurrAccountInfo.setInputPwdOne(md5One);
			MyArStation.mCurrAccountInfo.setSucceedPwd(md5One);
			MyArStation.mAccountStore.pushAndUpdate(MyArStation.mCurrAccountInfo);
			MyArStation.mAccountStore.commit();
		}
		Tools.debug("LoginScreen::loadData()::3");
		if(iETID != null) {
			iETID.setText(MyArStation.mCurrAccountInfo.getName());
		}
		byte[] bytes =  MyArStation.mCurrAccountInfo.getSucceedPwd();
		if(iETPSW != null) {
			iETPSW.setText(bytes != null && bytes.length > 0 ? FAKE_PWD : "");
		}
		Tools.debug("LoginScreen::loadData()::4");
		if(iETID != null && iETPSW != null) {
			if(TextUtils.isEmpty(MyArStation.mCurrAccountInfo.getName()) && 
					!TextUtils.isEmpty(MyArStation.mCurrIMEIAccountInfo.getName())) {
				iETID.setText(MyArStation.mCurrIMEIAccountInfo.getName());
				bytes =  MyArStation.mCurrIMEIAccountInfo.getSucceedPwd();
				iETPSW.setText(bytes != null && bytes.length > 0 ? FAKE_PWD : "");
			}
		}
		Tools.debug("LoginScreen::loadData()::5");
		if(!iAutoLogin) {
			return;
		}
		Tools.debug("LoginScreen::loadData()::6");
		iHasReconnected = false;
		//登录状态
		iGameState = 2;
		iLoginDelay = 0;
		//隐藏系统控件
		hideSysCom();
		Tools.debug("LoginScreen::loadData()::7");
//		//已有账号
		if (!TextUtils.isEmpty(MyArStation.mCurrAccountInfo.getName())) {
			Tools.debug("LoginScreen::loadData()::8");
			if((bytes != null && bytes.length > 0)) {
				Tools.debug("LoginScreen::loadData()::9");
				requestLogin();
				Tools.debug("LoginScreen::loadData()::10");
			}
			Tools.debug("LoginScreen::loadData()::11");
		}
		else {//自动注册
			Tools.debug("LoginScreen::loadData()::12");
//			ItemAction(GameEventID.ESPRITEBUTTON_EVENT_LOGIN_REGISTER);
			if(!TextUtils.isEmpty(MyArStation.mCurrIMEIAccountInfo.getName())) {
				Tools.debug("LoginScreen::loadData()::13");
				if(iETID != null) {
					iETID.setText(MyArStation.mCurrIMEIAccountInfo.getName());
				}
				Tools.debug("LoginScreen::loadData()::14");
				bytes =  MyArStation.mCurrIMEIAccountInfo.getSucceedPwd();
				if(iETPSW != null) {
					iETPSW.setText(bytes != null && bytes.length > 0 ? FAKE_PWD : "");
				}
				Tools.debug("LoginScreen::loadData()::15");
				boolean bResult = requestThreeLogin(MyArStation.mCurrIMEIAccountInfo.getName(), iChannelID);
				Tools.debug("LoginScreen::loadData()::16:Result=" + bResult);
			}
			else {
				Tools.debug("LoginScreen::loadData()::17");
				boolean bResult = requestThreeLogin(Util.getUUID(MyArStation.mGameMain.getApplicationContext()), iChannelID);
				Tools.debug("LoginScreen::loadData()::18:Result=" + bResult);
			}
		}
		Tools.debug("LoginScreen::loadData()::end");
	}
	
	//保存账号密码
	private void saveData() {
		Tools.debug("LoginScreen::saveData() 1");
		SharedPreferences.Editor tSPE = iSP.edit();
		tSPE.clear();
		tSPE.commit();
		MyArStation.mAccountStore.pushAndUpdate(MyArStation.mCurrAccountInfo);
		MyArStation.mAccountStore.commit();
		Tools.debug("LoginScreen::saveData() " + MyArStation.mCurrAccountInfo.toString());
	}

	private void saveThirdData(String userName, String userPwd) {
		Tools.debug("LoginScreen::saveThirdData() 1");
		MyArStation.mCurrIMEIAccountInfo.setSucceedPwd(userPwd.getBytes());
		MyArStation.mCurrIMEIAccountInfo.setName(userName.trim());
		MyArStation.mIMEIStore.pushAndUpdate(MyArStation.mCurrIMEIAccountInfo);
		MyArStation.mIMEIStore.commit();
		Tools.debug("LoginScreen::saveThirdData() " + MyArStation.mCurrIMEIAccountInfo.toString());
	}
	
	private boolean requestLoginTest() {
		Tools.debug("LoginScreen::requestLoginTest() Start");
		//
		String uin = "Test123";
		String psw = "12345678";
		MyArStation.mCurrAccountInfo.setName(uin);
		Tools.debug("LoginScreen::requestLoginTest() 1");
		MyArStation.mCurrAccountInfo.setPwdLength((byte) (psw.length()));
		byte[] md5One = MD5.toMD5(psw); // 2次md5加密
		MyArStation.mCurrAccountInfo.setInputPwd(MD5.toMD5(md5One));
		MyArStation.mCurrAccountInfo.setInputPwdOne(md5One);
		MyArStation.mCurrAccountInfo.setSucceedPwd(md5One);
		//发登录成功的消息
		messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_VISITORLOGIN);
		//
		Tools.debug("LoginScreen::requestLoginTest() End");
		return true;
	}
	
	private boolean requestLogin() {
		//如果没有初始化文本框，则使用文件的帐号登录
		if(iETID == null || iETPSW == null) {
			Tools.debug("LoginScreen::requestLogin() 1");
			Tools.debug("LoginScreen::requestLogin() ip=" + iIPString + " port=" + Url.port);
			if(MyArStation.iGameProtocol.create(iIPString, Url.port)) {
				Tools.debug("LoginScreen::requestLogin() 2");
				//
				MyArStation.iGameProtocol.releaseMsgScBuffer();
				//
				String userID = MyArStation.mCurrAccountInfo.getName();
				//
				String MD5Str = "";
				byte[] md5 = MyArStation.mCurrAccountInfo.getSucceedPwd();
				if(md5 != null && (md5.length > 0 && md5.length <= 16)) {
					MD5Str = MD5.MD5ByteToStr(MyArStation.mCurrAccountInfo.getSucceedPwd());
				}
				Tools.debug("LoginScreen::requestLogin() 3");
				//
				if(userID.length() <= 0 || MD5Str.length() <= 0) {
					Tools.debug("LoginScreen::requestLogin() 4");
					messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM);
					return false;
				}
				Tools.debug("LoginScreen::requestLogin() 5");
				//
				MyArStation.iGameProtocol.RequestLoginL(userID, MD5Str, getVersionFromPackage());
				return true;
			}
			else {
				Tools.debug("LoginScreen::requestLogin() 6");
				messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_DISCREATE, iContext.getString(R.string.Connect_Network_Failed));
				return false;
			}
		}
		Tools.debug("LoginScreen::requestLogin() 7");
		
		// 登录
		String uin = iETID.getText().toString();
		String psw = iETPSW.getText().toString();
		if(TextUtils.isEmpty(uin) == false && uin.equals(MyArStation.mCurrIMEIAccountInfo.getName())) {
			Tools.debug("LoginScreen::requestLogin() 8");
			requestThreeLogin(uin, iChannelID);
			return true;
		}
		Tools.debug("LoginScreen::requestLogin() 9");
		// 如果是新账号则取消登录成功号码
		if (MyArStation.mAccountStore.search(uin) < 0) {
			Tools.debug("LoginScreen::requestLogin() 10");
			MyArStation.mCurrAccountInfo.setSucceedPwd(new byte[0]);
		}
		MyArStation.mCurrAccountInfo.setName(uin);
		String MD5Str = "";
		if (!FAKE_PWD.equals(psw)) {
			Tools.debug("LoginScreen::requestLogin() 11");
			MyArStation.mCurrAccountInfo.setPwdLength((byte) (psw.length()));
			byte[] md5One = MD5.toMD5(psw); // 2次md5加密
			MyArStation.mCurrAccountInfo.setInputPwd(MD5.toMD5(md5One));
			MyArStation.mCurrAccountInfo.setInputPwdOne(md5One);
			MyArStation.mCurrAccountInfo.setSucceedPwd(md5One);
			MD5 MD5 = new MD5();
			MD5Str = MD5.getMD5ofStr(iETPSW.getText().toString());
		}
		else {
			Tools.debug("LoginScreen::requestLogin() 12");
			byte[] md5 = MyArStation.mCurrAccountInfo.getSucceedPwd();
			if(md5 != null && (md5.length > 0 && md5.length <= 16)) {
				MD5Str = MD5.MD5ByteToStr(md5);
			}
		}
		
		Tools.debug(MD5Str);
		if(!iSavePWD.getSelected()) {
			Tools.debug("LoginScreen::requestLogin() 13");
			MyArStation.mCurrAccountInfo.setSucceedPwd(new byte[0]);
		}
		MyArStation.mCurrAccountInfo.setSavedPwd(iSavePWD.getSelected());
		
//		MD5 MD5 = new MD5();
//		String MD5Str = MD5.getMD5ofStr(iETPSW.getText().toString());
		String userID = iETID.getText().toString();
		Message msg = new Message();
		Tools.debug("LoginScreen::requestLogin() 14");
		if(!checkPwdAndUserId()) {
			Tools.debug("LoginScreen::requestLogin() 15");
			messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_PwdAndUserIDShort);
			messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM);
			return false;
		}
		Tools.debug("LoginScreen::requestLogin() 16");
		if(MyArStation.iGameProtocol.create(iIPString, Url.port)) {
			Tools.debug("LoginScreen::requestLogin() 17");
			MyArStation.iGameProtocol.releaseMsgScBuffer();
			MyArStation.iGameProtocol.RequestLoginL(userID, MD5Str, getVersionFromPackage());
			return true;
		}
		else {
			Tools.debug("LoginScreen::requestLogin() 18");
			messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_DISCREATE, iContext.getString(R.string.Connect_Network_Failed));
			return false;
		}
	}
	
	private boolean requestLogin(String aIP) {
		if(iETID == null || iETPSW == null) {
			return false;
		}
		// 登录
		String uin = iETID.getText().toString();
		String psw = iETPSW.getText().toString();

		// 如果是新账号则取消登录成功号码
		if (MyArStation.mAccountStore.search(uin) < 0) {
			MyArStation.mCurrAccountInfo.setSucceedPwd(new byte[0]);
		}
		MyArStation.mCurrAccountInfo.setName(uin);
		String MD5Str = "";
		if (!FAKE_PWD.equals(psw)) {
			MyArStation.mCurrAccountInfo.setPwdLength((byte) (psw.length()));
			byte[] md5One = MD5.toMD5(psw); // 2次md5加密
			MyArStation.mCurrAccountInfo.setInputPwd(MD5.toMD5(md5One));
			MyArStation.mCurrAccountInfo.setInputPwdOne(md5One);
			MyArStation.mCurrAccountInfo.setSucceedPwd(md5One);
			MD5 MD5 = new MD5();
			MD5Str = MD5.getMD5ofStr(iETPSW.getText().toString());
		}
		else {
			byte[] md5 = MyArStation.mCurrAccountInfo.getSucceedPwd();
			if(md5 != null && (md5.length > 0 && md5.length <= 16)) {
				MD5Str = MD5.MD5ByteToStr(MyArStation.mCurrAccountInfo.getSucceedPwd());
			}
		}
		if(!iSavePWD.getSelected()) {
			MyArStation.mCurrAccountInfo.setSucceedPwd(new byte[0]);
		}
		MyArStation.mCurrAccountInfo.setSavedPwd(iSavePWD.getSelected());
		
//		MD5 MD5 = new MD5();
//		String MD5Str = MD5.getMD5ofStr(iETPSW.getText().toString());
		String userID = iETID.getText().toString();
		Message msg = new Message();
		if(!checkPwdAndUserId()) {
			messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_PwdAndUserIDShort);
			return false;
		}
		if(MyArStation.iGameProtocol.create(aIP, Url.port)) {
			MyArStation.iGameProtocol.releaseMsgScBuffer();
			MyArStation.iGameProtocol.RequestLoginL(userID, MD5Str, getVersionFromPackage());
			return true;
		}
		else {
			messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_DISCREATE, iContext.getString(R.string.Connect_Network_Failed));
			return false;
		}
	}
	
//	private void requestVisitorLogin() {
//		if(GameMain.iGameProtocol.create(iIPString, Url.port)) {
//			GameMain.iGameProtocol.releaseMsgScBuffer();
//			GameMain.iGameProtocol.RequestVisitorLoginL(getVersionFromPackage());
//		}
//		else {
//			messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_DISCREATE, iContext.getString(R.string.Connect_Network_Failed));
//		}
//	}
	
//	private void requestVisitorLogin(String aIP) {
//		if(GameMain.iGameProtocol.create(aIP, Url.port)) {
//			GameMain.iGameProtocol.releaseMsgScBuffer();
//			GameMain.iGameProtocol.RequestVisitorLoginL(getVersionFromPackage());
//		}
//		else {
//			messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_DISCREATE, iContext.getString(R.string.Connect_Network_Failed));
//		}
//	}
	
	private boolean requestThreeLogin(String uid, int channelID) {
		Tools.debug("LoginScreen::requestThreeLogin()::1");
		if (MyArStation.iGameProtocol.create(iIPString, Url.port)) {
			Tools.debug("LoginScreen::requestThreeLogin()::2");
			MyArStation.iGameProtocol.releaseMsgScBuffer();
			Tools.debug("LoginScreen::requestThreeLogin()::3");
			boolean tResult = MyArStation.iGameProtocol.RequestThirdLogin(uid, channelID, getVersionFromPackage());
			Tools.debug("LoginScreen::requestThreeLogin()::Result=" + tResult);
			return tResult;
		} else {
			Tools.debug("LoginScreen::requestThreeLogin()::5");
			messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM);
			messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_DISCREATE,
					iContext.getString(R.string.Connect_Network_Failed));
			Tools.debug("LoginScreen::requestThreeLogin()::6");
			return false;
		}
	}

	private boolean requestThreeLogin(String aIP) {
		if (MyArStation.iGameProtocol.create(aIP, Url.port)) {
			MyArStation.iGameProtocol.releaseMsgScBuffer();
			MyArStation.iGameProtocol.RequestThirdLogin(Util.getUUID(MyArStation.mGameMain.getApplicationContext()), iChannelID, getVersionFromPackage());
			return true;
		} else {
			messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_DISCREATE,
					iContext.getString(R.string.Connect_Network_Failed));
			return false;
		}
	}	
	
	private boolean checkPwdAndUserId() {
		if(iETID.getText().toString().length() <= 0 || iETPSW.getText().toString().length() <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
		Tools.debug("LoginScreen::OnProcessCmd()::MsgID=" 
				+ aMsgID
				+ " Type=" + aMobileType);
		GameProtocol gameProtocol = MyArStation.iGameProtocol;
		MobileMsg pMobileMsg = (MobileMsg) socket;
		switch (aMobileType) {
		case IM.IM_RESPONSE:
			{
				switch (aMsgID) {
				case RspMsg.MSG_RSP_LOGIN:		//登录
					//removeProgressDialog();
					gameProtocol.setSessionID(pMobileMsg.iSessionID);
					MBRspLogin pRspLogin = (MBRspLogin)pMobileMsg.m_pMsgPara;
					if(pRspLogin == null) {
						break;
					}
					Tools.debug(pRspLogin.iMsg);
					int type = pRspLogin.iResult;
					Tools.println(pRspLogin.toString() + "pMobileMsg.iSessionID " + pMobileMsg.iSessionID);
					MyArStation.mGameMain.iPlayer.iUserID = pRspLogin.iUserId;
					switch (type) {
						case ResultCode.SUCCESS://登陆成功
							messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_ResultSuccess, pRspLogin);
							break;
						case ResultCode.ERR_LOGIN_ROLE_NULL: //登录成功，创建角色
							//Tools.debug(pRspLogin.toString());
							messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_CREATE_ROLE, pRspLogin.iMsg);
							break;
						case ResultCode.ERR_LOGIN_NEED_RESET_THIRD_USERINFO://第三方帐户需要重置用户信息，转成公版用户
							messageEvent(MessageEventID.EMESSAGE_EVENT_RESET_THIRD_USERINFO, pRspLogin);
							break;
						case ResultCode.ERR_LOGIN_NEED_PUBLIC_UID://已经转过公版，需要使用公版帐登录
							messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_SHOW_MSG, pRspLogin.iMsg);
							break;
						default:	//发生错误
							//报错
							messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_SHOW_MSG, pRspLogin.iMsg);
							messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_REBUILD_SYSCOM);
							break;
					}
					break;
				case RspMsg.MSG_RSP_THIRD_LOGIN:		//第三方登录
					//removeProgressDialog();
					gameProtocol.setSessionID(pMobileMsg.iSessionID);
					MBRspThirdLogin pRspThirdLogin = (MBRspThirdLogin)pMobileMsg.m_pMsgPara;
					if(pRspThirdLogin == null) {
						break;
					}
					Tools.debug(pRspThirdLogin.iMsg);
					type = pRspThirdLogin.iResult;
					MyArStation.mGameMain.iPlayer.iUserID = pRspThirdLogin.iUserId;
					Tools.debug(pRspThirdLogin.toString() + "pMobileMsg.iSessionID " + pMobileMsg.iSessionID);
					switch (type) {
					case ResultCode.SUCCESS://登陆成功
						messageEvent(MessageEventID.EMESSAGE_EVENT_LOGIN_SUCCESS, pRspThirdLogin);
						break;
					case ResultCode.ERR_LOGIN_ROLE_NULL: //登录成功，创建角色
						//Tools.debug(pRspLogin.toString());
						saveThirdData(pRspThirdLogin.iUserName, pRspThirdLogin.iUserPwd);
						messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_CREATE_ROLE, pRspThirdLogin.iMsg);
						break;
					case ResultCode.ERR_REG_FAULT://创建账号过多
						isNewVisitor = false;
						messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_SHOW_MSG, pRspThirdLogin.iMsg);
						messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_REBUILD_SYSCOM);
						break;
					case ResultCode.ERR_LOGIN_NEED_RESET_THIRD_USERINFO://第三方帐户需要重置用户信息，转成公版用户
						messageEvent(MessageEventID.EMESSAGE_EVENT_RESET_THIRD_USERINFO, pRspThirdLogin);
						break;
					case ResultCode.ERR_LOGIN_NEED_PUBLIC_UID://已经转过公版，需要使用公版帐登录
						messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_SHOW_MSG, pRspThirdLogin.iMsg);
						messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_REBUILD_SYSCOM);
						break;
					default:	//发生错误
						//报错
						messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_SHOW_MSG, pRspThirdLogin.iMsg);
						messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_REBUILD_SYSCOM);
						break;
					}
					break;
					
				case RspMsg.MSG_RSP_VISITORLOGIN:		//游客登录
					removeProgressDialog();
					gameProtocol.setSessionID(pMobileMsg.iSessionID);
					MBRspVisitorLogin pRspVisitorLogin = (MBRspVisitorLogin)pMobileMsg.m_pMsgPara;
					if(pRspVisitorLogin == null) {
						break;
					}
					type = pRspVisitorLogin.iResult;
					Tools.debug(pRspVisitorLogin.toString());
					MyArStation.mGameMain.iPlayer.iUserID = pRspVisitorLogin.iUserId;
					switch (type) {
					case ResultCode.SUCCESS://登陆成功
						messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_VISITORLOGIN, pRspVisitorLogin);
						break;
					default:
						//报错
						messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_SHOW_MSG, pRspVisitorLogin.iMsg);
						break;
					}
					break;
					
				case RspMsg.MSG_RSP_UPDATE:		//更新
					MBRspUpdate rspUpdate = (MBRspUpdate)pMobileMsg.m_pMsgPara;
					if(rspUpdate == null) {
						return;
					}
					Tools.debug(rspUpdate.toString());
					if(rspUpdate.iResult == ResultCode.SUCCESS) {
						messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspUpdate, rspUpdate);
					}
					else {
						messageEvent(rspUpdate.iResult, rspUpdate.iMsg);
					}
					break;
				case RspMsg.MSG_RSP_RESET_THIRD_USERINFO:
					MBRspResetThirdUserInfo rspResetThirdUserInfo = (MBRspResetThirdUserInfo)pMobileMsg.m_pMsgPara;
					if(rspResetThirdUserInfo == null) {
						return;
					}
					System.out.println(rspResetThirdUserInfo.toString());
					Tools.debug(rspResetThirdUserInfo.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_RESET_THIRD_USERINFO, rspResetThirdUserInfo);
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

	@Override
	public void handleMessage(Message msg) {
		Tools.debug("LoginScreen::handleMessage()::Start");
		Tools.debug("LoginScreen::handleMessage()::ID=" + msg.what);
		switch (msg.what) {
		//正常用户登录成功
		case MessageEventID.EMESSAGE_EVENT_SOCKET_ResultSuccess:
			{
				MyArStation.msgManager.setClean(false);
				iRspLogin = (MBRspLogin)msg.obj;
				iServerVersion = iRspLogin.iServerVersion;
				MyArStation.mGameMain.iPlayer.iUserID = iRspLogin.iUserId;
				Tools.debug("Player.iUserID = " + MyArStation.mGameMain.iPlayer.iUserID);
				saveData();		
				//是否需要更新
				downloader = null;
				if(iRspLogin.iNeedUpdate == 1 && iRspLogin.iServerVersion > getVersionFromPackage()) {
					downloader = new Downloader(iRspLogin.iServerVersion, "lyingdice.apk", MyArStation.mGameMain, MyArStation.getInstance().iHandler, iRspLogin.iTotalSize);
					showUpdateConfirm(iServerVersion, iRspLogin.iUrl);
				}
				else {
					MyArStation.getInstance().iPlayer.apkUrl = iRspLogin.iUrl;
					//
					Tools.debug("MessageEventID.EMESSAGE_EVENT_SOCKET_ResultSuccess");
					//删除之前下载的文件
					removeDownloadFile(filePath);
					//隐藏系统控件
					hideSysCom();
					//将状态改成登录成功
					iGameState = 3;
					//切换到主界面GameCanvas
					//messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
					iGameCanvas.changeView(new HomeScreen());
				}
			}
			break;
		//第三方登录
		case MessageEventID.EMESSAGE_EVENT_LOGIN_SUCCESS:
		{
			MyArStation.msgManager.setClean(false);
			MBRspThirdLogin login = (MBRspThirdLogin)msg.obj;
			iServerVersion = login.iServerVersion;
			MyArStation.mGameMain.iPlayer.iUserID = login.iUserId;
			saveThirdData(login.iUserName, login.iUserPwd);		
			//是否需要更新
			downloader = null;
			if(login.iNeedUpdate == 1 && login.iServerVersion > getVersionFromPackage()) {
				downloader = new Downloader(login.iServerVersion, "lyingdice.apk", MyArStation.mGameMain, MyArStation.getInstance().iHandler, login.iTotalSize);
				showUpdateConfirm(iServerVersion, login.iUrl);
			}
			else {
				MyArStation.getInstance().iPlayer.apkUrl = login.iUrl;
				//
				Tools.debug("MessageEventID.EMESSAGE_EVENT_SOCKET_ResultSuccess");
				//删除之前下载的文件
				removeDownloadFile(filePath);
				//隐藏系统控件
				hideSysCom();
				//将状态改成登录成功
				iGameState = 3;
				//切换到主界面GameCanvas
				//messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
				iGameCanvas.changeView(new HomeScreen());
			}
		}
		break;
			
		//游客登录
		case MessageEventID.EMESSAGE_EVENT_SOCKET_VISITORLOGIN:
			{
				if(isTest) {
					//切换到大厅主界面
					iGameCanvas.changeView(new HallScreen(MyArStation.getInstance()));
				}
//				GameMain.msgManager.setClean(false);
//				MBRspVisitorLogin rspVisitorLogin = (MBRspVisitorLogin)msg.obj;
//				iServerVersion = rspVisitorLogin.iServerVersion;
//				GameMain.mGameMain.iPlayer.iUserID = rspVisitorLogin.iUserId;
//				saveData();
//				//是否需要更新
//				downloader = null;
//				if(rspVisitorLogin.iNeedUpdate == 1 && rspVisitorLogin.iServerVersion > getVersionFromPackage()) {
//					downloader = new Downloader(rspVisitorLogin.iServerVersion, "lyingdice.apk", GameMain.mGameMain, GameMain.getInstance().iHandler, rspVisitorLogin.iTotalSize);
//					showUpdateConfirm(iServerVersion, rspVisitorLogin.iUrl);
//				}
//				else {
//					GameMain.getInstance().iPlayer.apkUrl = rspVisitorLogin.iUrl;
//					//
//					Tools.debug("MessageEventID.EMESSAGE_EVENT_SOCKET_ResultSuccess");
//					//删除之前下载的文件
//					//downloader.delete(rspVisitorLogin.iServerVersion);
//					//Downloader.removeFile();
//					removeDownloadFile(filePath);
//					//隐藏系统控件
//					hideSysCom();
//					//将状态改成登录成功
//					iGameState = 3;
//					//切换主界面GameView
//					GameMain.mGameMain.iPlayer.ibVisitor = true;
//					//
//					GameMain.mGameMain.mainLayout.removeAllViews();
//					//
//					iGameCanvas.changeView(new TutorialScreen());
//				}
			}
			break;
			
		//需要创建角色	
		case MessageEventID.EMESSAGE_EVENT_REQ_CREATE_ROLE:
			{
				MyArStation.msgManager.setClean(false);
				saveData();
				hideSysCom();
				if(((String)msg.obj).length() > 0) {
					Tools.showSimpleToast(iContext, Gravity.CENTER, (String)msg.obj, Toast.LENGTH_LONG);
				}
				//切换到创建角色界面
				iGameCanvas.changeView(new CreateRoleScreen());
			}
			break;
		
		//更新回应
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspUpdate:
			{
				MBRspUpdate rspUpdate = (MBRspUpdate)msg.obj;
				downloader.freshDate(rspUpdate);
				if(bar != null) {
					bar.setMax(rspUpdate.iTotalSize);
					bar.setProgress(rspUpdate.iStartIndex);
				}
				if(rspUpdate.iStartIndex < rspUpdate.iTotalSize) {
					MyArStation.iGameProtocol.requsetUpdate(getVersionFromPackage(), rspUpdate.iStartIndex);
				}
				else {
					if(updateDialog != null) {
						updateDialog.dismiss();
					}
					MyArStation.iGameProtocol.setCurTaskMsgID(0);
					downloader.reName();
					downloader.updataCompelete(iServerVersion, 1);
					downloader.installApk();
				}
			}
			break;
			
		//用户名或密码太短	
		case MessageEventID.EMESSAGE_EVENT_SOCKET_PwdAndUserIDShort:
			{
				Tools.showSimpleToast(MyArStation.mGameMain, Gravity.CENTER, MyArStation.mGameMain.getString(R.string.ShowTips), Toast.LENGTH_LONG);
			}
			break;
		//设置账号和密码
		case MessageEventID.EMESSAGE_EVENT_LOGIN_LOADDATA:
		{
			if(isTest) {
				loadTestData();
			}
			else {
				loadData();
			}
		}
		break;
			
		//Socket断点续传下载
		case MessageEventID.EMESSAGE_EVENT_SOCKET_Reconnection:
			{
				Tools.debug("LoginScreen::handleMessage() 重新连接下载。。。！");
				reUpdate();
			}
			break;
		
		//HTTP下载成功
		case MessageEventID.EMESSAGE_EVENT_HTTP_UPDATE_PROGRESS:
			{
				if(bar != null) {
					bar.setProgress(msg.getData().getInt("size"));
					//显示下载成功信息
					if(bar.getProgress()==bar.getMax()){
						Toast.makeText(iContext, "下载成功", 1).show();
					}
				}
			}
			break;
			
		//按装apk包
		case MessageEventID.EMESSAGE_EVENT_INSTALL_APK:
			{
				installApk(filePath);
			}
			break;
			
		//显示控件
//		case MessageEventID.EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM:
//			{
//				//
//				iGameState = 1;
//				//显示控件的情况下，都默认是老用户
//				isNewVisitor = false;
//				//
//				if(absoluteLayout == null) {
//					GameMain.mGameMain.mainLayout.removeAllViews();
//					absoluteLayout = (RelativeLayout) LayoutInflater.from(GameMain.getInstance().getApplicationContext()).inflate(R.layout.new_login, null);
//					GameMain.mGameMain.mainLayout.addView(absoluteLayout);
//				}
//				//
//				Tools.debug("LoginScreen::handleMessage()::创建控件1");
//				displaySysCom();
//				Tools.debug("LoginScreen::handleMessage()::创建控件2");
//				showSysCom();
//			}
//			break;
			
		//隐藏控件
		case MessageEventID.EMESSAGE_EVENT_REQ_HIDE_SYSCOM:
			{
				hideSysCom();
			}
			break;
			
		//创建控件
		case MessageEventID.EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM:
		case MessageEventID.EMESSAGE_EVENT_REQ_REBUILD_SYSCOM:
		{
			Tools.debug("LoginScreen::handleMessage()::创建控件Start");
			if(!iInitOK) {
				Tools.debug("LoginScreen::handleMessage()::创建控件break");
				break;
			}
			//
			iGameState = 1;
			//
			Tools.debug("LoginScreen::handleMessage()::创建控件1");
			if(absoluteLayout == null) {
				MyArStation.mGameMain.mainLayout.removeAllViews();
				absoluteLayout = (RelativeLayout) LayoutInflater.from(MyArStation.getInstance().getApplicationContext()).inflate(R.layout.new_login, null);
				MyArStation.mGameMain.mainLayout.addView(absoluteLayout);
				//
				Tools.debug("LoginScreen::handleMessage()::创建控件2");
				displaySysCom();
				showSysCom();
				Tools.debug("LoginScreen::handleMessage()::创建控件3");
			}
			else {
				Tools.debug("LoginScreen::handleMessage()::创建控件4");
				displaySysCom();
				showSysCom();
				Tools.debug("LoginScreen::handleMessage()::创建控件5");
			}
			Tools.debug("LoginScreen::handleMessage()::创建控件6");
			String tIDStr = null;
			tIDStr = iSP.getString(MyArStation.mGameMain.getString(R.string.Key_SaveData_Login_ID), null);
			Tools.debug("LoginScreen::handleMessage()::创建控件6.1");
			String tPSWStr = null;
			tPSWStr = iSP.getString(MyArStation.mGameMain.getString(R.string.Key_SaveData_Login_Password), null);
			Tools.debug("LoginScreen::handleMessage()::创建控件7");
			if(tIDStr != null && tPSWStr != null) {
				MyArStation.mCurrAccountInfo.setName(tIDStr);
				MyArStation.mCurrAccountInfo.setPwdLength((byte) (tPSWStr.length()));
				byte[] md5One = MD5.toMD5(tPSWStr); // 2次md5加密
				MyArStation.mCurrAccountInfo.setInputPwd(MD5.toMD5(md5One));
				MyArStation.mCurrAccountInfo.setInputPwdOne(md5One);
				MyArStation.mCurrAccountInfo.setSucceedPwd(md5One);
				MyArStation.mAccountStore.pushAndUpdate(MyArStation.mCurrAccountInfo);
				MyArStation.mAccountStore.commit();
			}
			//
			Tools.debug("LoginScreen::handleMessage()::创建控件8");
			if(iETID != null && iETPSW != null) {
				Tools.debug("LoginScreen::handleMessage()::创建控件9");
				if(TextUtils.isEmpty(MyArStation.mCurrAccountInfo.getName()) && 
						!TextUtils.isEmpty(MyArStation.mCurrIMEIAccountInfo.getName())) {
					isNewVisitor = true;
					Tools.debug("LoginScreen::handleMessage()::创建控件10");
					iETID.setText(MyArStation.mCurrIMEIAccountInfo.getName());
					byte[] bytes =  MyArStation.mCurrIMEIAccountInfo.getSucceedPwd();
					iETPSW.setText(bytes != null && bytes.length > 0 ? FAKE_PWD : "");
				}
				else {
					isNewVisitor = false;
					Tools.debug("LoginScreen::handleMessage()::创建控件11");
					iETID.setText(MyArStation.mCurrAccountInfo.getName());
					byte[] bytes =  MyArStation.mCurrAccountInfo.getSucceedPwd();
					iETPSW.setText(bytes != null && bytes.length > 0 ? FAKE_PWD : "");
				}
			}
			Tools.debug("LoginScreen::handleMessage()::创建控件12");
		}
		break;
			
		//显示信息
		case MessageEventID.EMESSAGE_EVENT_REQ_SHOW_MSG:
			{
				iGameState = 1;
				showSysCom();
				if(((String)msg.obj).length() > 0) {
					Tools.showSimpleToast(iContext, Gravity.CENTER, (String)msg.obj, Toast.LENGTH_LONG);
				}
				removeProgressDialog();
			}
			break;
			
		//关闭Socket
		case MessageEventID.EMESSAGE_EVENT_CLOSE_SOCKET:
			{
				Tools.debug("LoginScreen::handleMessage()::close socket!!!");
				//将状态改成登正常
				iGameState = 1;
				//显示系统控件
				showSysCom();
				//
				if(registDialog != null) {
					registDialog.dismiss();
					registDialog = null;
				}
				ibShowDialog = false;
			}
			break;
			
		case SocketErrorType.Error_DecodeError:
		case SocketErrorType.Error_DisConnection:
		case SocketErrorType.Error_TimeOut://接受数据超时处理
			{
				if(iHasReconnected) {
					Tools.debug("LoginScreen::handleMessage()::iHasReconnected 1");
					//将状态改成登正常
					iGameState = 1;
					//显示系统控件
					showSysCom();
				}
				else {
					Tools.debug("LoginScreen::handleMessage()::iHasReconnected 7");
					iHasReconnected = true;
					//下载配置文件
					String szUrl = "http://www.funoble.com/configip.zip";//configip.php";
					URLAvailability tUrlLib = new URLAvailability();
					URL tUrl = tUrlLib.isConnect(szUrl);
					if(tUrl == null) {
						Tools.debug("LoginScreen::handleMessage()::iHasReconnected 8");
						//将状态改成登正常
						iGameState = 1;
						//显示系统控件
						messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM);
					}
					else {
						Tools.debug("LoginScreen::handleMessage()::iHasReconnected 9");
						String filePath = Environment.getExternalStorageDirectory().getPath()+File.separator+"/lyingdice/download/";
						downloadConfig(szUrl, new File(filePath));
					}
					tUrlLib = null;
					Tools.debug("LoginScreen::handleMessage()::iHasReconnected 10");
				}
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_LOGIN_AUTO_LOGIN:
			{
				//切换到登录状态
				if(requestLogin()) {
					//登录状态
					iGameState = 2;
					iLoginDelay = 0;
					//隐藏系统控件
					hideSysCom();
				}
//				else {
//					//将状态改成登正常
//					iGameState = 1;
//					//显示系统控件
//					showSysCom();
//				}
			}
			break;
			
		//重新连接网络
		case MessageEventID.EMESSAGE_EVENT_RECONNET_IPSTR:
			{
				Tools.debug("LoginScreen::handleMessage()::RECONNET start");
				iIPString = (String)msg.obj;
				loadData();
				Tools.debug("LoginScreen::handleMessage()::RECONNET end");
//				//老用户
//				if(!isNewVisitor) {
//					Tools.debug("LoginScreen::handleMessage()::RECONNET 1");
//					//切换到登录状态
//					if(requestLogin(ipStr)) {
//						Tools.debug("LoginScreen::handleMessage()::RECONNET 2");
//						//登录状态
//						iGameState = 2;
//						iLoginDelay = 0;
//						//隐藏系统控件
//						hideSysCom();
//						Tools.debug("LoginScreen::handleMessage()::RECONNET 3");
//					}
//					else {
//						Tools.debug("LoginScreen::handleMessage()::RECONNET 4");
//						//将状态改成登正常
//						iGameState = 1;
//						//显示系统控件
//						showSysCom();
//						Tools.debug("LoginScreen::handleMessage()::RECONNET 5");
//					}
//				}
//				//新用户，启动第三方登录
//				else {
//					Tools.debug("LoginScreen::handleMessage()::RECONNET 6");
//					//登录状态
//					iGameState = 2;
//					iLoginDelay = 0;
//					//隐藏系统控件
//					hideSysCom();
//					//发送登录请求
//					requestThreeLogin(ipStr);
//					Tools.debug("LoginScreen::handleMessage()::RECONNET 7");
//				}
//				Tools.debug("LoginScreen::handleMessage()::RECONNET end");
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_TO_CONNECTED:
			{
				if(iHasReconnected) {
					//复位重连标志
					//iHasReconnected = false;
					//
//					Tools.showSimpleToast(iContext, Gravity.CENTER, "重连成功！", Toast.LENGTH_LONG);
				}
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_RESET_THIRD_USERINFO:
			MBRspLogin pRspLogin = (MBRspLogin) msg.obj;
			if(pRspLogin.iMsg != null && pRspLogin.iMsg.length() > 0) {
				Tools.showSimpleToast(ActivityUtil.mContext, pRspLogin.iMsg);
			}
			if(registDialog== null) {
				registDialog = new ReRegistDialog(MyArStation.mGameMain, this);
			}
			ibShowDialog = true;
			registDialog.show();
			break;
		case MessageEventID.EMESSAGE_EVENT_LOGIN_CANCEL_REGISTER:
			ibShowDialog = false;
			MyArStation.msgManager.setClean(false);
			iGameCanvas.changeView(new HomeScreen());
			break;
		default:
			{
				if(registDialog != null) {
					registDialog.handleMessage(msg);
				}
			}
			break;
		}
		Tools.debug("LoginScreen::handleMessage()::end");
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void showUpdateConfirm(int version, final String url) {
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).stopMusic();
		//
		updateDialog = new Dialog(MyArStation.mGameMain, R.style.MSGDialogStyle);
		updateDialog.setContentView(R.layout.r_okcanceldialogview);
		updateDialog.setCancelable(false);
		bar = (MyProgressBar)updateDialog.findViewById(R.id.pbPercent);
		final Button btnOk = (Button) updateDialog.findViewById(R.id.ok);
//		final MBRspLogin pRspLogin = (MBRspLogin)GameMain.iGameProtocol.getMobileMsgCS(RspMsg.MSG_RSP_LOGIN);
//		final MBRspVisitorLogin pRspVisitorLogin = (MBRspVisitorLogin)GameMain.iGameProtocol.getMobileMsgCS(RspMsg.MSG_RSP_VISITORLOGIN);
//		int version = pRspLogin != null ? pRspLogin.iServerVersion : (pRspVisitorLogin != null ? pRspVisitorLogin.iServerVersion : 0);
		String filePath = Environment.getExternalStorageDirectory().getPath()+File.separator+"/lyingdice/download/";
//		btnOk.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(pRspLogin != null || pRspVisitorLogin != null) {
					if(!installApk(filePath)) {
//						LoadInfo loadInfo = downloader.getDownloaderInfors();
//						GameMain.iGameProtocol.requsetUpdate(getVersionFromPackage(), loadInfo.complete);
//						GameMain.iGameProtocol.setCurTaskMsgID(ReqMsg.MSG_REQ_UPDATE);
//						final String url = pRspLogin != null ? pRspLogin.iUrl : (pRspVisitorLogin != null ? pRspVisitorLogin.iUrl : "");
						if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
							//开始下载文件
							download(url, new File(filePath));
						}else{
							//显示SDCard错误信息
//							Toast.makeText(MainActivity.this, R.string.sdcarderror, 1).show();
						}
						TextView tv = (TextView)updateDialog.findViewById(R.id.dialog_message);
						tv.setText(R.string.Update_Content);
						btnOk.setVisibility(View.GONE);
					}
//				}
//			}
//		});
		Button btnCancel = (Button) updateDialog.findViewById(R.id.cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyArStation.mGameMain.quit();
				updateDialog.dismiss();
			}
		});
		
		Button btnWeb = (Button) updateDialog.findViewById(R.id.btnUserWeb);
//		final String url = pRspLogin != null ? pRspLogin.iUrl : (pRspVisitorLogin != null ? pRspVisitorLogin.iUrl : "");
		URLAvailability urlAvailab = new URLAvailability();
		if(url.length() > 0 && urlAvailab.isConnect(url) != null) {
			btnWeb.setVisibility(View.VISIBLE);
		}
		else {
			btnWeb.setVisibility(View.GONE);
		}
		btnWeb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent viewIntent = new
				Intent("android.intent.action.VIEW",Uri.parse(url));
				MyArStation.mGameMain.startActivity(viewIntent);
				MyArStation.mGameMain.finish();
				updateDialog.dismiss();
			}
		});
		updateDialog.show();
		urlAvailab = null;
	}
	
	private int getVersionFromPackage() {
		int appVersion = 0;
		PackageManager manager = MyArStation.mGameMain.getPackageManager();
		try {
		        PackageInfo info = manager.getPackageInfo(MyArStation.mGameMain.getPackageName(), 0);
		        appVersion = info.versionCode; // 版本名，versionCode同理
		        Tools.debug("appVersion" + appVersion);
		} catch (NameNotFoundException e) {
		        e.printStackTrace();
		}
		return appVersion;
	}
	
	private void reUpdate() {
		LoadInfo loadInfo = downloader.getDownloaderInfors();
		if(loadInfo != null) {
			MyArStation.iGameProtocol.cleanMsgBuffer();
			MyArStation.iGameProtocol.create(iIPString, Url.port);
			MyArStation.iGameProtocol.requsetUpdate(getVersionFromPackage(), loadInfo.complete);
		}
	}
	
	/**
  	 * 主线程(UI线程)
  	 * 对于显示控件的界面更新只是由UI线程负责，如果是在非UI线程更新控件的属性值，更新后的显示界面不会反映到屏幕上
  	 * 如果想让更新后的显示界面反映到屏幕上，需要用Handler设置。
  	 * @param path
  	 * @param savedir
  	 */
    private void download(final String path, final File savedir) {
    	new Thread(new Runnable() {			
			@Override
			public void run() {
				//开启3个线程进行下载
				FileDownloader loader = new FileDownloader(iContext, path, savedir, 3);
				bar.setMax(loader.getFileSize());//设置进度条的最大刻度为文件的长度
		    	
				try {
					loader.download(new DownloadProgressListener() {
						@Override
						public void onDownloadSize(int size) {//实时获知文件已经下载的数据长度
							Tools.debug("size = " + size);
							Message msg = new Message();
							msg.what = MessageEventID.EMESSAGE_EVENT_HTTP_UPDATE_PROGRESS;
							msg.getData().putInt("size", size);
							MyArStation.mGameMain.iHandler.sendMessage(msg);//发送消息
						}

						@Override
						public void onDownloadFinish(int size, String fileName) {
							Tools.debug("size = " + size + "fileName = " + fileName);
							messageEvent(MessageEventID.EMESSAGE_EVENT_INSTALL_APK);
						}
						
						@Override
						public void onError(int aErrorID) {
							
						}
					});
				} catch (Exception e) {
					MyArStation.mGameMain.iHandler.obtainMessage(MessageEventID.EMESSAGE_EVENT_HTTP_UPDATE_ERROR).sendToTarget();
				}
			}
		}).start();
	}
    
	public boolean installApk(String filePath) {
		File file = new File(filePath);
		file = getUninatllApkInfo(MyArStation.mGameMain, filePath);
		if(null == file) {
			return false;
		}
		// TODO Auto-generated method stub
		Log.e("OpenFile", file.getName());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
		"application/vnd.android.package-archive");
		MyArStation.mGameMain.startActivity(intent);
		MyArStation.mGameMain.finish();
		return true;
	}
	
	 public File getUninatllApkInfo(Context context, String archiveFilePath) {  
		 File apkFile = new File(archiveFilePath);  
		 File apk = null;
		 if(apkFile.isDirectory()) {
			 File[] files = apkFile.listFiles();
			 for(File file:files) {
				 if (!file.exists() || !file.getName().toLowerCase().endsWith(".apk")) {  
					 Tools.debug("file path is not correct");  
					 continue;
				 }  
				 apk = file;
				 try {
					 PackageManager pm = context.getPackageManager();  
					 PackageInfo info = pm.getPackageArchiveInfo(apk.getPath(), PackageManager.GET_ACTIVITIES);  
					 if (info != null) {  
						 if(info.versionCode != iServerVersion) {
							 Tools.debug("info.versionCode = " +info.versionCode + "version = " + iServerVersion + " apk = " + apk.getName());
							 apk.delete();
							 continue;
						 }
						 else {
							 return apk;
						 }
					 }  
					 return null;
				 } catch (Exception e) {
				 }
			 }
		 }
		 else {
			 apk = apkFile;
			 if (!apk.exists() || !apk.getName().toLowerCase().endsWith(".apk")) {  
				 Tools.debug("file path is not correct");  
				 return null;
			 }  
			 try {
				 PackageManager pm = context.getPackageManager();  
				 PackageInfo info = pm.getPackageArchiveInfo(apk.getPath(), PackageManager.GET_ACTIVITIES);  
				 if (info != null) {  
					 if(info.versionCode != iServerVersion) {
						 Tools.debug("info.versionCode = " +info.versionCode + "version = " + iServerVersion);
						 apk.delete();
						 return null;
					 }
					 else {
						 return apk;
					 }
				 }  
				 return null;
			 } catch (Exception e) {
			 }
		 }
		return apk;
	 }  
	 
	 private void removeDownloadFile(String filePath) {
		 File file = new File(filePath);
		 if(file.exists() && file.isDirectory()) {
			 File[] files = file.listFiles();
			 for(File apk : files) {
				 apk.delete();
				 Tools.debug("remove " + apk.getName());
			 }
		 }
	 }
	 
	 //下载配置文件
	  private void downloadConfig(final String path, final File savedir) {
	    	new Thread(new Runnable() {			
				@Override
				public void run() {
					//开启1个线程进行下载
					FileDownloader loader = new FileDownloader(iContext, path, savedir, 1);
					try {
						loader.download(new DownloadProgressListener() {
							@Override
							public void onDownloadSize(int size) {//实时获知文件已经下载的数据长度
								Tools.debug("Configfile size = " + size);
							}

							@Override
							public void onDownloadFinish(int size, String fileName) {
								Tools.debug("LoginScreen::downloadConfig()::Configfile size = " + size + " Config fileName = " + fileName);
								FileReader fr;
								try {
									fr = new FileReader(fileName);
									BufferedReader br = new BufferedReader(fr);
									String ipStr = br.readLine();// 读取一行,如果想读整个文件需要用循环来读
									Tools.debug("LoginScreen::downloadConfig()::Configfile data = " + ipStr);//这个可以直接打印出来看
									if(ipStr != null && ipStr.length() > 0) {
										Tools.debug("LoginScreen::downloadConfig() 1");
										messageEvent(MessageEventID.EMESSAGE_EVENT_RECONNET_IPSTR, ipStr);
									}
									else {
										Tools.debug("LoginScreen::downloadConfig() 2");
										messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM);
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									Tools.debug("LoginScreen::downloadConfig() 3");
									messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM);
									e.printStackTrace();
								} 
							}
							
							@Override
							public void onError(int aErrorID) {
								Tools.debug("LoginScreen::downloadConfig() 4");
								messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM);
							}
						});
					} catch (Exception e) {
						messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM);
					}
				}
			}).start();
		}

	/* 
	 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	/* 
	 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(s == null) {
			return;
		}
		byte[] bytes = null;
		AccountInfo accountInfo =  MyArStation.mAccountStore.get(s.toString());
		com.funoble.myarstation.common.IMEIStore.AccountInfo imeiAccountInfo =  MyArStation.mIMEIStore.get(s.toString());
		if(accountInfo != null) {
			MyArStation.mCurrAccountInfo.setSucceedPwd(accountInfo.getSucceedPwd());
			bytes =  MyArStation.mCurrAccountInfo.getSucceedPwd();
		}
		else if(imeiAccountInfo != null){
			bytes =  MyArStation.mCurrIMEIAccountInfo.getSucceedPwd(); 
		}
		if(iETPSW != null) {
			iETPSW.setText(bytes != null && bytes.length > 0 ? FAKE_PWD : "");
		}
	}

	/* 
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 */
	@Override
	public void afterTextChanged(Editable s) {
	}

	/* 
	 * @see com.funoble.lyingdice.view.ReRegistDialog.Reregist#OnReregistSucces(java.lang.String, java.lang.String)
	 */
	@Override
	public void OnReregistSucces(String uid, String pwd) {
		ibShowDialog = false;
		iGameCanvas.changeView(new HomeScreen());
	}

	/* 
	 * @see com.funoble.lyingdice.view.ReRegistDialog.Reregist#OnCancel()
	 */
	@Override
	public void OnCancel() {
		ibShowDialog = false;
		messageEvent(MessageEventID.EMESSAGE_EVENT_LOGIN_CANCEL_REGISTER);
	}
	
	private boolean isNewVisitor() {
		if(TextUtils.isEmpty(MyArStation.mCurrAccountInfo.getName()) && TextUtils.isEmpty(MyArStation.mCurrIMEIAccountInfo.getName())) {
			return true;
		}
		return false;
	}
	
	
//	private boolean loadTestUIPak() {
//		testProject = CPakManager.loadSynProject("test.pak");
//		if(testProject != null) {
//			testPrSprite = testProject.getSprite(0);
//			return true;
//		}
//		else {
//			Tools.println("载入文件失败");
//		}
//		return false;
//	}
}
