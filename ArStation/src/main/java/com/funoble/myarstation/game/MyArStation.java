package com.funoble.myarstation.game;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.funoble.myarstation.common.AccountStore;
import com.funoble.myarstation.common.IMEIStore;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.Heart;
import com.funoble.myarstation.data.cach.RoomData;
import com.funoble.myarstation.download.HttpDownloadManager;
//import com.funoble.lyingdice.screen.UniteGame;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.screen.AlbumScreen;
import com.funoble.myarstation.screen.ChooseRoomScreen;
import com.funoble.myarstation.screen.ClassicalGame;
import com.funoble.myarstation.screen.CreateRoleScreen;
import com.funoble.myarstation.screen.HallScreen;
import com.funoble.myarstation.screen.HomeScreen;
import com.funoble.myarstation.screen.LoginScreen;
import com.funoble.myarstation.screen.LogoScreen;
import com.funoble.myarstation.screen.RegisterScreen;
import com.funoble.myarstation.socket.GameProtocol;
import com.funoble.myarstation.socket.GameSocketHandler;
import com.funoble.myarstation.socket.protocol.MBNotifyActivityInfo;
import com.funoble.myarstation.socket.protocol.MBNotifyFriendEvent;
import com.funoble.myarstation.socket.protocol.MBNotifyGetGift;
import com.funoble.myarstation.socket.protocol.MBNotifyLearnSkill;
import com.funoble.myarstation.socket.protocol.MBNotifyLeaveMsg;
import com.funoble.myarstation.socket.protocol.MBNotifyMissionReward;
import com.funoble.myarstation.socket.protocol.MBNotifyShakeFriend;
import com.funoble.myarstation.socket.protocol.MBNotifySysMsg;
import com.funoble.myarstation.socket.protocol.MBRspFriendEventSelect;
import com.funoble.myarstation.socket.protocol.MBRspMainPage;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.NotifyMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.ReqMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.SocketErrorType;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.store.data.Goods;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.Graphics;
import com.funoble.myarstation.utils.MobileInfo;
import com.funoble.myarstation.utils.ScreenBrightnessManager;
import com.funoble.myarstation.utils.SendSMSUtil;
import com.funoble.myarstation.utils.SettingManager;
import com.funoble.myarstation.view.EventSelectDialog;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.MessageEventID;
import com.funoble.myarstation.view.EventSelectDialog.OnGeneralDialogListener;
import com.yeepay.android.plugin.YeepayUtils;

	//
public class MyArStation extends Activity implements OnGestureListener,
												Handler.Callback, 
												GameSocketHandler,
												OnGeneralDialogListener {
	//
	public static MyArStation mGameMain = null;
	public static MediaManager[] iSoundEngine = null;
	public static GameProtocol	iGameProtocol;
	public static CPakManager iPakManager;
	public static Heart iHeart;
//	public static DownloadManager iDownloadManager;
	public static HttpDownloadManager iHttpDownloadManager;
	public static CPlayer iPlayer;	//自己
	public static CImageManager iImageManager;
	public FrameLayout mainLayout;
	//
	public static List<Goods> iStoreGoods;
	public static List<Goods> iStoreGoodsGb;
	public Handler iHandler;
	private GestureDetector mGestureDetector;
	private GameCanvas gameCanvas = null;
	public static boolean ibInit = false;
	public boolean ibRelease = true;//是否要释放资源
	private boolean ibRegisterService = false;
	public SendSMSUtil sendSMS;
	public static MsgManager msgManager;
	//帐号存储
	public static AccountStore				mAccountStore;
	//当前输入的用户账户信息
	public static AccountStore.AccountInfo	mCurrAccountInfo;
	//帐号存储
	public static IMEIStore				mIMEIStore;
	//当前输入的用户账户信息
	public static IMEIStore.AccountInfo	mCurrIMEIAccountInfo;
	//时间
	public static EventSelectDialog 	eventSelectDialog = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        Tools.debug("onCreate Start");
        //
        mGameMain = this;
        mGestureDetector = new GestureDetector(mGameMain);
        
        //设置运行环境
        ActivityUtil.setContext(this.getApplicationContext());
        ActivityUtil.setNoTitleBar(mGameMain);
        ActivityUtil.setFullScreenMode(mGameMain);
        ActivityUtil.setScreenOrientation(mGameMain);
        ActivityUtil.getDisplay(mGameMain);
        ActivityUtil.setResources(mGameMain);
        ActivityUtil.setKeepScreenOn(mGameMain);
        ActivityUtil.setNamePaint();
        Graphics.setPaint();  //设置图片类画笔
        //让屏幕常亮
        ScreenBrightnessManager.setBrightness((float)SettingManager.getInstance().getiBrightness());
 
        //初始化为LOGO界面
        setContentView(R.layout.framemain);
        mainLayout = (FrameLayout) findViewById(R.id.mainview);
        gameCanvas = (GameCanvas) findViewById(R.id.viewLogin);
        msgManager = new MsgManager();
        setDisplay(new LogoScreen(this));  		
        
        //创建观察者
        iHandler = new Handler(this);
        //创建声音引擎
		int level = loadLevel();
		Tools.debug("GameMain::onCreate() 1");
    	iSoundEngine = new MediaManager[MediaManager.MAX_SOUND_ENGINE_COUNT];
    	Tools.debug("GameMain::onCreate() 2");
    	for(int i=0; i<MediaManager.MAX_SOUND_ENGINE_COUNT; i++) {
    		Tools.debug("GameMain::onCreate() 3.1 i=" + i);
    		iSoundEngine[i] = MediaManager.createMediaManager(i);
    		Tools.debug("GameMain::onCreate() 3.2");
    		iSoundEngine[i].loadLevel = level;
    		Tools.debug("GameMain::onCreate() 3.3");
    		iSoundEngine[i].initMediaManager();
    	}
    	Tools.debug("GameMain::onCreate() 4");
        //
        initGameProtocol();
        iPlayer = new CPlayer();
        iPlayer.init();
        iStoreGoods = new ArrayList<Goods>();
        iStoreGoodsGb = new ArrayList<Goods>();
        //
        iImageManager = new CImageManager();
        sendSMS = new SendSMSUtil();
        //事件窗口init
        eventSelectDialog = new EventSelectDialog(this);
		eventSelectDialog.setOnGeneralDialogListener(this);
		eventSelectDialog.setConfirmText("确定");
		eventSelectDialog.setCancelText("取消");
        //
//        Tools.debug("onCreate End");
		test();
    }

    public void init() {
    	//初始化资源
    	Tools.debug("GameMain::init()::initSound");
    	initSound();
    	setVolumeControlStream(AudioManager.STREAM_MUSIC);
        Tools.debug("GameMain::init()::initPakManager");
        //
        initPakManager();
        //载入图片
        iImageManager.init();
        //
        Tools.debug("GameMain::init() 1");
        initAccountStore();
        Tools.debug("GameMain::init() 2");
        initIMEIAccountStore();
        Tools.debug("GameMain::init() 3");
        //
        ibInit = true;
    }
    
    private void setDisplay(GameView gameView) {
    	gameCanvas.changeView(gameView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//    	if (gameCanvas != null) {
////    		Tools.debug("GameMain onTouchEvent event.getY( = " +event.getY());
//    		gameCanvas.onTouchEvent(event);
//		}
    	if(mGestureDetector != null){
//    		Tools.debug("GameMain mGestureDetector event.getAction() = " +event.getAction());
    		if(!mGestureDetector.onTouchEvent(event)) {
    			if (gameCanvas != null) {
//    	    		Tools.debug("GameMain onTouchEvent event.getY( = " +event.getY());
    	    		return gameCanvas.onTouchEvent(event);
    			}
    		}
    	}
//    	Tools.debug("GameMain onTouchEvent event.getAction() = " +event.getAction());
    	return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
//    	gameCanvas.onKeyDown(keyCode, event);
//    	return true;
    	Log.v("gamemain","onKeyDown = " + keyCode);
//    	MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
    	if (gameCanvas.onKeyDown(keyCode, event)) {
    		return true;
    	}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			onDestroy();
			quit();
//			surfaceDestroyed(getHolder());
//			return true;
		}
    	return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub

    	return true;
    }

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
//		Tools.debug("GameMain onDown event.getAction() = " +e.getAction());
		if (gameCanvas != null) {
			return gameCanvas.onDown(e);
		}
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
//		Tools.debug("GameMain onFling velocityX = " +velocityX + "  velocityY =" +velocityY);
		if (gameCanvas != null) {
			gameCanvas.onFling(e1, e2, velocityX, velocityY);
		}
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
//		Tools.debug("GameMain onLongPress event.getAction() = " +e.getAction());
		if (gameCanvas != null) {
			gameCanvas.onLongPress(e);
		}
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
//		Tools.debug("GameMain onScroll distanceY = " +distanceY);
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
//		Tools.debug("GameMain onShowPress event.getAction() = " +e.getAction());
		
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
//		Tools.debug("GameMain onSingleTapUp event.getAction() = " +e.getAction());
		if (gameCanvas != null) {
			return gameCanvas.onSingleTapUp(e);
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.v("gamemain","onDestroy");
		super.onDestroy();
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Log.v("gamemain","finish");
		super.finish();
	}
	
    public void quit() {
    	for(int i=0; i<MediaManager.MAX_SOUND_ENGINE_COUNT; i++) {
    		iSoundEngine[i].releaseAll();
    	}
    	this.deleteDatabase("webview.db"); 
		this.deleteDatabase("webviewCache.db");
    	finish();
    	//退出程序时，通知os删除该进程
    	android.os.Process.killProcess(android.os.Process.myPid());
//    	iFemaleSoundEngine.releaseAll();
    }
    
    public static MyArStation getInstance() {
    	return mGameMain;
    }

	@Override
	public boolean handleMessage(Message msg) {
		if (msg == null) {
			return true;
		}
		switch (msg.what) {
		case MessageEventID.EMESSAGE_EVENT_REQ_DELETE_SYSCOM:
			mainLayout.removeAllViews();
			break;
		case MessageEventID.EMESSAGE_EVENT_TO_PROGRESS:
			mainLayout.removeAllViews();
			RoomData data = (RoomData)msg.obj;
    		if(data == null) {
    			data = new RoomData();
    			setDisplay(new ClassicalGame(data));  			//初始化为 游戏桌子界面
    		}
    		else {
    			if(data.roomType == RoomData.TYPE_UNITE) {
//        			setDisplay(new UniteGame(data));  	
    			}
    			else {
        			setDisplay(new ClassicalGame(data));  	
    			}
    		}
	        GameView view = gameCanvas.getCurrentView();
			break;
		case MessageEventID.EMESSAGE_EVENT_TO_LOGIN:
			mainLayout.removeAllViews();
	        setDisplay(new LoginScreen(false));  			//初始化为 login界面
//	        Tools.debug("gameCanvas.getCurrentView");
	        view = gameCanvas.getCurrentView();
	        if(view != null) {
	        	view.removeProgressDialog();
	        }
	        if(msg.obj != null) {
	        	Tools.showSimpleToast(this, Gravity.CENTER, (String)msg.obj, Toast.LENGTH_LONG);
	        }
			break;
		case MessageEventID.EMESSAGE_EVENT_TO_HOME:
			mainLayout.removeAllViews();
			setDisplay(new HomeScreen());  			//初始化为 游戏首页界面
			break;
//		case MessageEventID.EMESSAGE_EVENT_TO_REGISTER:
//			setDisplay(new RegisterScreen());  	
//			break;
		case MessageEventID.EMESSAGE_EVENT_TO_CREATEROLE:
			setDisplay(new CreateRoleScreen());  			//打开创建角色界面
			break;
		case MessageEventID.EMESSAGE_EVENT_TO_HALLGAME:
			setDisplay(new HallScreen(mGameMain));  			//初始化为 游戏首页界面
			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_RegisterSuccess:
//			gameCanvas = null;
//	        setContentView(R.layout.login);
//	        gameCanvas = (GameCanvas) findViewById(R.id.viewLogin);
//	        setDisplay(new LoginScreen());  			//初始化为 login界面
//			Tools.showSimpleToast(this, Gravity.CENTER,
//					GameMain.mGameMain.getString(R.string.Req_Register_Success) , Toast.LENGTH_SHORT);
//			break;
		case ResultCode.ERR_DECODEBUF:
		case ResultCode.ERR_TIMEOUT:
		case ResultCode.ERR_BUSY: 
			Tools.showSimpleToast(this, Gravity.CENTER, (String)msg.obj, Toast.LENGTH_LONG);
			view = gameCanvas.getCurrentView();
			if(view != null) {
				view.removeProgressDialog();
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_ReqLeaveRoom:
//			gameCanvas.stopThread();
//			gameCanvas = null;
//			setContentView(R.layout.login);
//			gameCanvas = (GameCanvas) findViewById(R.id.viewLogin);
			setDisplay(new HomeScreen());  			//初始化为 游戏首页界面
			view = gameCanvas.getCurrentView();
			if(view != null) {
//				view.showProgressDialog(getString(R.string.Loading_String), true);
				MBRspMainPage rspLogin = (MBRspMainPage)MyArStation.iGameProtocol.getMobileMsgCS(RspMsg.MSG_RSP_MAINPAGE);
				if(rspLogin != null) {
					MyArStation.iGameProtocol.requsetLeaveRoom(rspLogin.iUserId);
				}
			}
			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyKICKOpenHomeScreen:
//			mainLayout.removeAllViews();
//			setDisplay(new HomeScreen());  			//初始化为 游戏首页界面
//			view = gameCanvas.getCurrentView();
//			if(view != null) {
//				messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyKick, msg.obj);
//			}
//			gameCanvas.setFocusable(true);
//			break;
		case MessageEventID.EMESSAGE_EVENT_CLOSE_SOCKET:
		case SocketErrorType.Error_DecodeError:
		case SocketErrorType.Error_DisConnection:
		case SocketErrorType.Error_TimeOut://接受数据超时处理
			iGameProtocol.stop();
			view = gameCanvas.getCurrentView();
			if(view != null) {
				view.removeProgressDialog();
			}
			Tools.showSimpleToast(this, Gravity.CENTER, "网络断开", Toast.LENGTH_LONG);
			if(view instanceof LoginScreen
					|| view instanceof RegisterScreen) {
				view.handleMessage(msg);
			}
			else {
				mainLayout.removeAllViews();
				setDisplay(new LoginScreen(false));  			//初始化为 login界面
			}
			break;
		case SocketErrorType.Error_NetWork_Enable:
			Tools.showSimpleToast(this, Gravity.CENTER, "网络不可以用", Toast.LENGTH_LONG);
			break;
		case MessageEventID.EMESSAGE_EVENT_GAME_OTHERROOM:
//			mainLayout.removeAllViews();
			setDisplay(new ChooseRoomScreen());
			view = gameCanvas.getCurrentView();
			if(view != null) {
				MBRspMainPage rspLogin = (MBRspMainPage)MyArStation.iGameProtocol.getMobileMsgCS(RspMsg.MSG_RSP_MAINPAGE);
				if(rspLogin != null) {
					MyArStation.iGameProtocol.requsetLeaveRoom(rspLogin.iUserId);
				}
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_FRINDEVENT:
		{
			MBNotifyFriendEvent notifyFriendEvent = (MBNotifyFriendEvent)msg.obj;
			eventSelectDialog.setFriendEvent(notifyFriendEvent);
			if(notifyFriendEvent.iDialogType == 1) {
				eventSelectDialog.showSingle(notifyFriendEvent.iMsg, 12);
			}
			else {
				eventSelectDialog.show(notifyFriendEvent.iMsg, 12);
			}
		}
		break;
		case MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_RSP_FRINDEVENTSELECT:
		{
			MBRspFriendEventSelect rspFriendEventSelect = (MBRspFriendEventSelect)msg.obj;
			if(rspFriendEventSelect.iMsg.length() > 0) {
				Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), 
						rspFriendEventSelect.iMsg);
			}
			if(rspFriendEventSelect.iResult == 0) {
				iPlayer.iGb = rspFriendEventSelect.iGB;
				messageEvent(MessageEventID.EMESSAGE_EVENT_TO_REFRESH_HOMEMAIN);
			}
		}
		break;
		case MessageEventID.EMESSAGE_EVENT_TO_ACTIVITY_INFO:
		{
			MBNotifyActivityInfo notifyActivityInfo = (MBNotifyActivityInfo)msg.obj;
			iPlayer.iHasActivity = notifyActivityInfo.iHasActivity;
			iPlayer.iActivityCount = notifyActivityInfo.iActivityCount;
		}
		break;
		default:
			if (gameCanvas != null) {
				gameCanvas.getCurrentView().handleMessage(msg);
			}
			break;
		}
		return true;
	}
	
	private void messageEvent(int aEventID) {
		Message tMes = new Message();
		tMes.what = aEventID;
		MyArStation.getInstance().iHandler.sendMessage(tMes);
	}
	
	private void messageEvent(int aEventID, Object aMsg) {
		Message tMes = new Message();
		tMes.what = aEventID;
		tMes.obj = aMsg;
		MyArStation.getInstance().iHandler.sendMessage(tMes);
	}
	
	private void initSound() {
		Tools.debug("GameMain::initSound() 1");
//    	for(int i=0; i<MediaManager.MAX_SOUND_ENGINE_COUNT; i++) {
//    		Tools.debug("GameMain::initSound() 2. i=" + i);
//    		
//    	}
		Tools.debug("GameMain::initSound() 3");
		settingSound();
		Tools.debug("GameMain::initSound() 4");
	}

	private void initGameProtocol() {
		iGameProtocol = new GameProtocol(this);
		iHeart = Heart.getInstance();
//		iDownloadManager = new DownloadManager(iHandler);
		iHttpDownloadManager = new HttpDownloadManager(iHandler);
	}
	
	private void initPakManager() {
		iPakManager = new CPakManager();
	}
	
	@Override
	protected void onResume() {
		Tools.debug("GameMain onResume()");
		if(ibInit) {
			ibRelease = true;
			if (msgManager != null) {
				msgManager.show();
			}
			//
			if(gameCanvas != null) {
				gameCanvas.onResume();
			}
			//
			if(ibRegisterService == true) {
				unbindService(mConnection);
			}
			settingSound();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		//
		if(ibInit) {
			ibRelease = false;
			for(int i=0; i<MediaManager.MAX_SOUND_ENGINE_COUNT; i++) {
				iSoundEngine[i].setOpenMediaState(false);
			}
			//
			if(gameCanvas != null) {
				gameCanvas.onPause();
			}
			//
			startServer();
			//
			if (msgManager != null) {
				msgManager.pause();
			}
		}
		//
		super.onPause();
	}

	@Override
	public void OnReportError(Object socket, int aErrorCode) {
		Tools.debug("OnReportError" + aErrorCode);
		GameView view = gameCanvas.getCurrentView();
		if(view == null) {
			return;
		}
		if(SocketErrorType.Error_TimeOut == aErrorCode &&
				iGameProtocol.getCurTaskMsgID() == ReqMsg.MSG_REQ_UPDATE) {
			Message msg = new Message();
			msg.what = MessageEventID.EMESSAGE_EVENT_SOCKET_Reconnection;
			iHandler.sendMessage(msg);
		}
		else {
			Message msg = new Message();
			msg.what = aErrorCode;
			iHandler.sendMessage(msg);
		}
	}

	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
		Tools.debug("GameMain::OnProcessCmd()::MsgID=" 
				+ aMsgID
				+ " Type=" + aMobileType);
		MobileMsg pMobileMsg = (MobileMsg) socket;
		if(aMobileType == IM.IM_NOTIFY) {
			if(aMsgID == NotifyMsg.MSG_NOTIFY_GET_GIFT) {
				MBNotifyGetGift rspGetGift = (MBNotifyGetGift)pMobileMsg.m_pMsgPara;
				if(rspGetGift == null) {
					return;
				}
				Tools.println(rspGetGift.toString());
				//
				iPlayer.ibGetGift = true;
			}
			else if(aMsgID == NotifyMsg.MSG_NOTIFY_LEARN_SKILL) {
				MBNotifyLearnSkill nLearnSkill = (MBNotifyLearnSkill)pMobileMsg.m_pMsgPara;
				if(nLearnSkill != null) {
					MyArStation.iPlayer.iZhaiSkill = nLearnSkill.iSkillID;
				}
			}
			else if(aMsgID == NotifyMsg.MSG_NOTIFY_SYSMSG){
				MBNotifySysMsg sNotifySysMsg = (MBNotifySysMsg)pMobileMsg.m_pMsgPara;
				Tools.debug("GameMain::OnProcessCmd()::MSG_NOTIFY_SYSMSG. MsgType = " 
						+ sNotifySysMsg.iType
						+ " len=" + (sNotifySysMsg.iMsg.length()>>2));
				if(sNotifySysMsg != null && sNotifySysMsg.iType >= 1) {
					msgManager.upDate(sNotifySysMsg.iMsg);
				}
			}
			else if(aMsgID == NotifyMsg.MSG_NOTIFY_SHAKE_FRIEND) {
				MBNotifyShakeFriend notifyShakeFriend = (MBNotifyShakeFriend)pMobileMsg.m_pMsgPara;
				if(notifyShakeFriend != null) {
					if(notifyShakeFriend.iDesUserID == MyArStation.iPlayer.iUserID) {
						MyArStation.iPlayer.iCurrentDrunk = notifyShakeFriend.iCurrentDrunk;
						Tools.debug(notifyShakeFriend.toString());
						messageEvent(MessageEventID.EMESSAGE_EVENT_TO_REFRESH_HOMEMAIN);
					}
				}
			}
			else if(aMsgID == NotifyMsg.MSG_NOTIFY_FRIEND_EVENT) {
				MBNotifyFriendEvent notifyFriendEvent = (MBNotifyFriendEvent)pMobileMsg.m_pMsgPara;
				if(notifyFriendEvent == null) {
					return;
				}
				Tools.debug(notifyFriendEvent.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_FRINDEVENT, notifyFriendEvent);
				return;
			}
			else if(aMsgID == NotifyMsg.MSG_NOTIFY_ACTIVITY_INFO) {
				MBNotifyActivityInfo notifyActivityInfo = (MBNotifyActivityInfo)pMobileMsg.m_pMsgPara;
				if(notifyActivityInfo == null) {
					return;
				}
				Tools.debug(notifyActivityInfo.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_TO_ACTIVITY_INFO, notifyActivityInfo);
				return;
			}
			else if(aMsgID == NotifyMsg.MSG_NOTIFY_LEAVEMSG) {
				MBNotifyLeaveMsg notifyLeaveMsg = (MBNotifyLeaveMsg)pMobileMsg.m_pMsgPara;
				if(notifyLeaveMsg == null) {
					return;
				}
				Tools.debug(notifyLeaveMsg.toString());
				iPlayer.iLeaveMsgCount = notifyLeaveMsg.iLeaveMsgCount;
				messageEvent(MessageEventID.EMESSAGE_EVENT_TO_LEAVEMSG_INFO);
				return;
			}
			else if(aMsgID == NotifyMsg.MSG_NOTIFY_MISSION_REWARD) {
				MBNotifyMissionReward notifyMissionReward = (MBNotifyMissionReward)pMobileMsg.m_pMsgPara;
				if(notifyMissionReward == null) {
					return;
				}
				Tools.println(notifyMissionReward.toString());
				iPlayer.iMissionRewardCount = notifyMissionReward.iCount;
				return;
			}
		}
		else if(aMobileType == IM.IM_RESPONSE) {
			if(aMsgID == RspMsg.MSG_RSP_FRIEND_EVENT_SELECT) {
				MBRspFriendEventSelect rspFriendEventSelect = (MBRspFriendEventSelect)pMobileMsg.m_pMsgPara;
				if(rspFriendEventSelect == null) {
					return;
				}
				Tools.debug(rspFriendEventSelect.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_RSP_FRINDEVENTSELECT, rspFriendEventSelect);
				return;
			}
		}
		if(gameCanvas != null) {
			GameView view = gameCanvas.getCurrentView();
			if(view != null) {
				view.OnProcessCmd(socket, aMobileType, aMsgID);
			}
		}
	}
	
	@Override
	public void OnConnect(Object socket) {
		Tools.debug("OnConnect");
		if(gameCanvas != null) {
			GameView view = gameCanvas.getCurrentView();
			if(view == null) {
				return;
			}
			if(view instanceof LoginScreen) {
				Message msg = new Message();
				msg.what = MessageEventID.EMESSAGE_EVENT_TO_CONNECTED;
				iHandler.sendMessage(msg);
			}
		}
	}

	@Override
	public void OnClose(Object socket) {
		Tools.debug("GameMain::OnClose() start");
		if(gameCanvas != null) {
			GameView view = gameCanvas.getCurrentView();
			if(view == null) {
				return;
			}
			//在登录界面，断网
			if(view instanceof LoginScreen) {
				iGameProtocol.stop();
				Message msg = new Message();
				msg.what = MessageEventID.EMESSAGE_EVENT_CLOSE_SOCKET;
				msg.obj = mGameMain.getString(R.string.Connect_Network_DisConnect);
				iHandler.sendMessage(msg);
			}
			//在其它界面断网
			else if(!(view instanceof RegisterScreen)) {
				iGameProtocol.stop();
				Message msg = new Message();
				msg.what = MessageEventID.EMESSAGE_EVENT_TO_LOGIN;
				msg.obj = mGameMain.getString(R.string.Connect_Network_DisConnect);
				iHandler.sendMessage(msg);
			}
		}
	}
	
	public GameCanvas getGameCanvas() {
		return gameCanvas;
	}
	
	public Context getContext() {
		return this.getApplicationContext();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("onActivityResult", "requestCode=" + requestCode + "  resultCode=" + resultCode + " data == null" + (data == null));
		if(resultCode == 200) {
			if (data != null) {
				Bundle params = data.getExtras();
				String requestId = params.getString("requestId");
				if (requestId == null) {
					requestId = "";
				}
				String amount = params.getString("amount");
				String returnCode = params.getString("returnCode");
				String customerNumber = params.getString("customerNumber");
				String time = params.getString("time");
				String hmac = params.getString("hmac");
				String appId = params.getString("appId");
				String errMsg = params.getString("errMsg");
//				Log.e("YeepayExampleActivity", "mPayBackInfo.nCode=" + returnCode);
//				Log.e("YeepayExampleActivity", "mPayBackInfo.customerNumber=" + customerNumber);
//				Log.e("YeepayExampleActivity", "mPayBackInfo.requestId=" + requestId);
//				Log.e("YeepayExampleActivity", "mPayBackInfo.amount=" + amount);
//				Log.e("YeepayExampleActivity", "mPayBackInfo.time=" + time);
//				Log.e("YeepayExampleActivity", "mPayBackInfo.hmac=" + hmac);
//				Log.e("YeepayExampleActivity", "appId=" + appId);
				
				StringBuilder builder = new StringBuilder();
				builder.append(returnCode).append("$");
				builder.append(customerNumber).append("$");
				builder.append(requestId).append("$");
				builder.append(amount).append("$");
				builder.append(appId).append("$");
				builder.append(errMsg).append("$");
				builder.append(time);
				YeepayUtils.hmacSign(builder.toString(), ActivityUtil.KEY);
//				Log.e("YeepayExampleActivity", "result=" + result);
			} else {
//			tvResult.setText("data == null");
			}
		}
		else if (requestCode == MessageEventID.EMESSAGE_EVENT_REQUEST_CAMERA_IMAGE || requestCode == MessageEventID.EMESSAGE_EVENT_REQ_LOADPHOTOFROMALBUM) {// 从相机获取图片 
			Uri uri = null;
			if (data != null) {
				uri = data.getData();
			}
			GameView view = gameCanvas.getCurrentView();
			if (view != null && view instanceof AlbumScreen)
			{
				((AlbumScreen) view).onGetImage(uri, requestCode, null);
			}
		}
		else if(requestCode == 300) {
			iGameProtocol.RequestInviteFriend(0);
		}
			super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void settingSound() {
		Tools.debug("GameMain::settingSound() 1");
		boolean isSound = SettingManager.getInstance().isSound();
		boolean isMusic = SettingManager.getInstance().isMusic();
		boolean isWifi = SettingManager.getInstance().isWifiDownload();
		Tools.debug("GameMain::settingSound() 2");
		Tools.debug("GameMain::settingSound() gamemain isSound = " +isSound + " isMusic =" + isMusic + " isWifi " + isWifi);
		for(int i=0; i<MediaManager.MAX_SOUND_ENGINE_COUNT; i++) {
			if(iSoundEngine[i] != null) {
				iSoundEngine[i].setOpenBgState(isMusic);
				iSoundEngine[i].setOpenEffectState(isSound);
			}
		}
		Tools.debug("GameMain::settingSound() 3");
	}
	
	private void startServer() {
		ibRegisterService = true;
		 bindService(new Intent(MyArStation.this, LocalService.class), mConnection, Context.BIND_AUTO_CREATE); 
	}
	
	private ServiceConnection mConnection = new ServiceConnection() { 
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) { 
		} 
		@Override
		public void onServiceDisconnected(ComponentName className) { 
		}

	};

	/* 
	 * @see com.funoble.lyingdice.view.EventSelectDialog.OnGeneralDialogListener#OnConfirm(com.funoble.lyingdice.socket.protocol.MBNotifyFriendEvent)
	 */
	@Override
	public void OnConfirm(MBNotifyFriendEvent event) {
		if(event != null) {
			iGameProtocol.RequestFriendEventSelect(event.iEventID, event.iKey, 1);
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.EventSelectDialog.OnGeneralDialogListener#OnCancel(com.funoble.lyingdice.socket.protocol.MBNotifyFriendEvent)
	 */
	@Override
	public void OnCancel(MBNotifyFriendEvent event) {
	} 
	
	void initAccountStore() {
		mAccountStore = new AccountStore(getApplicationContext());
		// 打开账号存储
		mAccountStore.open();
		mCurrAccountInfo = new AccountStore.AccountInfo();
		mCurrAccountInfo.copy(mAccountStore.peek());
		Tools.debug("GameMain::InitAccountStore::" + mCurrAccountInfo.toString());
	}
	
	void initIMEIAccountStore() {
		mIMEIStore = new IMEIStore(getApplicationContext());
		// 打开账号存储
		mIMEIStore.open();
		mCurrIMEIAccountInfo = new IMEIStore.AccountInfo();
		mCurrIMEIAccountInfo.copy(mIMEIStore.peek());
		Tools.debug("GameMain::InitAccountStore::" + mCurrIMEIAccountInfo.toString());
	}

	/* 
	 * @see android.app.Activity#onLowMemory()
	 */
	@Override
	public void onLowMemory() {
		long totalMemory = Runtime.getRuntime().totalMemory();
		long freeMemory = Runtime.getRuntime().freeMemory();
		Tools.debug("onLowMemory()...........totalMemory: " + totalMemory + "freeMemory: " + freeMemory);
		super.onLowMemory();
	}
	
	public void test() {
		ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		int memory = activityManager.getMemoryClass();
//		Tools.debug("test()...........memory: " + memory + " used: " + used() / 1024 / 1024 +"M" + "可用内存: " + MobileInfo.getMobileInfo(this.getApplicationContext()));
//		Tools.debug("几核: " + MobileInfo.getNumCoure()+" cupinfo: " + MobileInfo.getCpuInfo() + "cup 频率" + MobileInfo.getMaxCpuFreq() + "/" + MobileInfo.getMinCpuFreq());
	}
	
	private int loadLevel() {
		int level = 0;
		int heap = MobileInfo.getAppLimitMemory(this.getApplicationContext());
		int MaxCpuFreq = MobileInfo.getMaxCpuFreqInt();
		float memory = MobileInfo.getAvailMemoryInt(this.getApplicationContext());
		int multiple  = (int) (memory / heap);
//		Tools.debug("multiple:" + multiple + "heap: " + heap);
		if(MaxCpuFreq <= 600) {
			if(multiple < 2) {
				level = 3;
			}
			else {
				level = 2;
			}
		}
		else if(MaxCpuFreq > 600 && MaxCpuFreq <= 800) {
			if(multiple < 2) {
				level = 2;
			}
			else {
				level = 1;
			}
		}
		else if(MaxCpuFreq > 800) {
			if(multiple < 2) {
				level = 1;
			}
			else {
				level = 0;
			}
		}
//		Tools.debug("levle: " + level);
		return level;
	}
	public long used(){
		long total = Runtime.getRuntime().totalMemory();
		long free = Runtime.getRuntime().freeMemory();
		return (total - free);
	}
}