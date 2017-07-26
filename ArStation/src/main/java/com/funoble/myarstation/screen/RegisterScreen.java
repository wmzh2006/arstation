package com.funoble.myarstation.screen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.funoble.myarstation.adapter.RestPswdAdapter.ItemListen;
import com.funoble.myarstation.common.MD5;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.RestPwdData;
import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.GameButtonHandler;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.socket.GameProtocol;
import com.funoble.myarstation.socket.protocol.MBRspRegisterB;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.SocketErrorType;
import com.funoble.myarstation.socket.protocol.ProtocolType.Url;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.update.DownloadProgressListener;
import com.funoble.myarstation.update.FileDownloader;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.URLAvailability;
import com.funoble.myarstation.view.GameEventID;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.MessageEventID;
import com.funoble.myarstation.view.QuestionItemDialog;

public class RegisterScreen extends GameView implements GameButtonHandler, OnClickListener, ItemListen{
	
	private final int GAME_STATE_NORMAL = 0;
	private final int GAME_STATE_LOADING = 1;
	
	private Project iUIPak = null;
	private Scene iSceneLoading = null;
	private Vector<SpriteButton> iSpriteButtonList = new Vector<SpriteButton>();//精灵按钮 列表
	private int iChangeTick = 0;
	private SharedPreferences iSP;
	private EditText iETID;//账号输入框
	private EditText iETPSW;//密码输入框
	private EditText iETPSWConfirm;//确认密码输入框
	private Paint iLoadingPaint;//
	private Context iContext = MyArStation.mGameMain;
	private AbsoluteLayout absoluteLayout = null;
	private ScrollView layout = null;
	private Button question;
	private EditText answer;
	private Button commit;
	private int	iGameState = GAME_STATE_NORMAL;
	private boolean iHasReconnected = true;
	private QuestionItemDialog questionItemDialog;
	private RestPwdData data;
	@Override
	public void init() {
		//载入背景图
		MyArStation.iImageManager.loadLoginBack();
		//
		mViewId = 1;
		iLoadingPaint = new Paint();
		iLoadingPaint.setColor(Color.BLACK);
		iSP = MyArStation.getInstance().getPreferences(Activity.MODE_PRIVATE);
		loadUIPak();
		messageEvent(MessageEventID.EMESSAGE_EVENT_TO_REGISTER);
		iGameState = GAME_STATE_NORMAL;
//		displaySysCom();
	}

	@Override
	public void releaseResource() {
		for (int i=0; i<iSpriteButtonList.size(); i++) {
			SpriteButton tSB = (SpriteButton)iSpriteButtonList.elementAt(i);
			tSB.releaseResource();
			tSB = null;
		}
		iSpriteButtonList.removeAllElements();
		iSceneLoading = null;
		iUIPak = null;
		FrameLayout.LayoutParams btnLytp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		btnLytp.gravity = Gravity.LEFT | Gravity.TOP;
		MyArStation.mGameMain.mainLayout.setLayoutParams(btnLytp);
		if(questionItemDialog != null) {
			questionItemDialog.dismiss();
		}
	}

	@Override
	public void paintScreen(Canvas g, Paint paint) {
		g.drawBitmap(MyArStation.iImageManager.iBackLoginBmp,
				0,
				0,
				null);
		if(iGameState == GAME_STATE_NORMAL) {
//			if(iScene != null) {
//				iScene.paint(g, 0, 0);
//			}
//			for(int i=0; i<iSpriteButtonList.size(); i++) {
//				((SpriteButton)iSpriteButtonList.elementAt(i)).paint(g);
//			}
		}
		else if(iGameState == GAME_STATE_LOADING) {
			if(iSceneLoading != null) {
				iSceneLoading.paint(g, 0, 0);
//				g.drawText("正在读取数据", 278*ActivityUtil.ZOOM_X, 288*ActivityUtil.ZOOM_Y, ActivityUtil.mLoading);
			}
		}


	}

	@Override
	public void performL() {
		if(iGameState == GAME_STATE_NORMAL) {
//			if(iScene != null) {
//				iScene.handle();
//			}
//			for(int i=0; i<iSpriteButtonList.size(); i++) {
//				((SpriteButton)iSpriteButtonList.elementAt(i)).perform();
//			}
		}
		else if(iGameState == GAME_STATE_LOADING) {
			if(iSceneLoading != null) {
				iSceneLoading.handle();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			questionItemDialog.dismiss();
			hideSysCom();
			messageEvent(MessageEventID.EMESSAGE_EVENT_TO_LOGIN);
			return true;
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
//		System.out.println("ProgressScreen onDown event.getAction() = " +e.getAction());
		boolean result= false;
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		result = pointerPressed(aX, aY);
		return result;
	}

	@Override
	public boolean onLongPress(MotionEvent e) {
//		System.out.println("ProgressScreen onLongPress event.getAction() = " +e.getAction());
		boolean result= false;
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		result = pointerReleased(aX, aY);
		return result;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
//		System.out.println("ProgressScreen onSingleTapUp event.getAction() = " +e.getAction());
		boolean result= false;
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		result = pointerReleased(aX, aY);
		return result;
	}

	@Override
	public boolean pointerPressed(int aX, int aY) {
		if(iGameState != GAME_STATE_NORMAL) {
			return false;
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
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
				pSB.pressed();
//				System.out.println("RegisterScreen pointerPressed  pSB.pressed() ");
				result = true;
				break;
			}
		}
		return result;
	}

	@Override
	public boolean pointerReleased(int aX, int aY) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean ItemAction(int aEventID) {
		switch (aEventID) {
		case GameEventID.ESPRITEBUTTON_EVENT_REGISTER_CONFIRM:
			if(!checkIDAndPassword()) {
				return true;
			}
			//
			iHasReconnected = false;
			//登录状态
			iGameState = GAME_STATE_LOADING;
			//隐藏系统控件
			messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_HIDE_SYSCOM);
			//注册
			requestRegister();
			return true;
		}
		return false;
	}
	
	private void loadUIPak() {
			iSceneLoading = LoadLoading();
//		if(iScene != null) {
//			iScene.setViewWindow(0, 0, 800, 480);
//			iScene.initDrawList();
//			iScene.initNinePatchList(new int[] {
//					R.drawable.new_regist_back, 
//					R.drawable.new_regist_back, 
//					R.drawable.new_text_bg,
//					R.drawable.new_text_bg,
//					R.drawable.new_text_bg});//R.drawable.new_ttitle, R.drawable.new_back00});
//			
//			Map pMap = iScene.getLayoutMap(Scene.SpriteLayout);
//			Vector<?> pSpriteList = pMap.getSpriteList();
//			Sprite pSprite;
//			for (int i=0; i<pSpriteList.size(); i++) {
//				pSprite = (Sprite)pSpriteList.elementAt(i);
//				int pX = pSprite.getX();
//				int pY = pSprite.getY();
//				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_REGISTER_CONFIRM;//GameEventID.ESPRITEBUTTON_EVENT_REGISTER_OFFSET + pSprite.getIndex();
//				TPoint pPoint = new TPoint();
//				pPoint.iX = pX;
//				pPoint.iY = pY;
//				//精灵按钮
//				SpriteButton pSpriteButton;
//				pSpriteButton = new SpriteButton(pSprite);
//				pSpriteButton.setEventID(pEventID);
//				pSpriteButton.setPosition(pX, pY);
//				pSpriteButton.setHandler(this);
//				iSpriteButtonList.addElement(pSpriteButton);
//			}
//			
//		}
	}

	private void changeScreen() {
		iChangeTick++;
		if (iChangeTick>=2) {
			iChangeTick = 0;
		}
	}
	
	//显示系统组件
	private void displaySysCom() {
		float pxETIDX = 310; //800*480的像素值
		float pxETIDY = 184;
		float pxETPSWY = 236;
		float pxETPSWConfirmY = 288;
		float pxW = 288;
		//转成对应屏幕分辨率的值
		pxETIDX *= ActivityUtil.ZOOM_X; 
		pxETIDY *= ActivityUtil.ZOOM_Y;
		pxETPSWY *= ActivityUtil.ZOOM_Y;
		pxETPSWConfirmY *= ActivityUtil.ZOOM_Y;
		pxW *= ActivityUtil.ZOOM_X;
		//
		int dipETIDX = (int)(pxETIDX / ActivityUtil.DENSITY_ZOOM_X);//(int) (iGameCanvas.px2dip(iContext, pxETIDX) * ActivityUtil.DENSITY_ZOOM_X);
		int dipETIDY = (int)(pxETIDY / ActivityUtil.DENSITY_ZOOM_Y);//(int) (iGameCanvas.px2dip(iContext, pxETIDY) * ActivityUtil.DENSITY_ZOOM_Y);
		int dipETPSWY = (int)(pxETPSWY / ActivityUtil.DENSITY_ZOOM_Y);//(int)(iGameCanvas.px2dip(iContext, pxETPSWY) * ActivityUtil.DENSITY_ZOOM_Y);
		int dipETPSWConfirmY = (int)(pxETPSWConfirmY / ActivityUtil.DENSITY_ZOOM_Y);
		int dipRight = (int)((pxETIDX + pxW) / ActivityUtil.DENSITY_ZOOM_X);
//		
		iETID = (EditText) absoluteLayout.findViewById(R.id.editTextRegisterID);
		if (iETID != null) {
			iETID.setVisibility(View.VISIBLE);
			LayoutParams lp = (LayoutParams) iETID.getLayoutParams();
			lp.x = dipETIDX;//(int) (lp.x*ActivityUtil.DENSITY_ZOOM_X);lp.width = dipRight - lp.x;
			lp.y = dipETIDY;//(int) (lp.y*ActivityUtil.DENSITY_ZOOM_Y);
			lp.width = dipRight - lp.x;
			iETID.setLayoutParams(lp);
		}
		iETPSW = (EditText) absoluteLayout.findViewById(R.id.editTextRegisterPSW);
		if (iETPSW != null) {
			iETPSW.setVisibility(View.VISIBLE);
			LayoutParams lp = (LayoutParams) iETPSW.getLayoutParams();
			lp.x = dipETIDX;//(int) (lp.x*ActivityUtil.DENSITY_ZOOM_X);
			lp.y = dipETPSWY;//(int) (lp.y*ActivityUtil.DENSITY_ZOOM_Y);
			lp.width = dipRight - lp.x;
			iETPSW.setLayoutParams(lp);
		}
		iETPSWConfirm = (EditText) absoluteLayout.findViewById(R.id.editTextRegisterPSWConfirm);
		if (iETPSWConfirm != null) {
//			iETPSWConfirm.setVisibility(View.VISIBLE);
			LayoutParams lp = (LayoutParams) iETPSWConfirm.getLayoutParams();
			lp.x = dipETIDX;//(int) (lp.x*ActivityUtil.DENSITY_ZOOM_X);
			lp.y = dipETPSWConfirmY;//(int) (lp.y*ActivityUtil.DENSITY_ZOOM_Y);
			lp.width = dipRight - lp.x;
			iETPSWConfirm.setLayoutParams(lp);
		}
	}
	
	//隐藏系统组件
	private void hideSysCom() {
		if(layout != null) {
			layout.setVisibility(View.INVISIBLE);
		}
//		if (iETID != null) {
//			iETID.setVisibility(View.INVISIBLE);
//		}
//		if (iETPSW != null) {
//			iETPSW.setVisibility(View.INVISIBLE);
//		}
//		if (iETPSWConfirm != null) {
//			iETPSWConfirm.setVisibility(View.INVISIBLE);
//		}
	}
	
	//隐藏系统组件
	private void showSysCom() {
		if(layout != null) {
			layout.setVisibility(View.VISIBLE);
		}
//		if (iETID != null) {
//			iETID.setVisibility(View.VISIBLE);
//		}
//		if (iETPSW != null) {
//			iETPSW.setVisibility(View.VISIBLE);
//		}
//		if (iETPSWConfirm != null) {
//			iETPSWConfirm.setVisibility(View.VISIBLE);
//		}
	}
	
	/**
	 * description 检查账号、密码
	 * @return true:合法，false:非法
	 */
	private boolean checkIDAndPassword() {
		boolean tbError = false;//是否出错了
		String tInfo = "";//提示语
		String tStr = "";
		if (iETID != null) {
			tStr = iETID.getText().toString();//id字符串
			if (tStr.length()<6) {
				tInfo = "账号过短！";
				tbError = true;
			}
		}
		if (!tbError) {
			tStr = "";
			tStr = iETPSW.getText().toString();//密码字符串
			if (tStr.length()<6) {
				tInfo = "密码过短！";
				tbError = true;
			}
			if (!tbError) {
				String tConfirmStr = "";
				tConfirmStr = iETPSWConfirm.getText().toString();//确认密码字符串
				if (!tConfirmStr.equals(tStr)) {
					tInfo = "两次输入的密码不一致！";
					tbError = true;
				}
			}
		}
		if(!tbError) {
			tStr = answer.getText().toString().trim();
			if(tStr.length() < 1) {
				tInfo = "请输入密保答案！";
				tbError = true;
			}
		}
		if (tbError) {
			messageEvent(GameEventID.ESPRITEBUTTON_EVENT_REGISTER_MSGBOX, tInfo);
		}
		return !tbError;
	}
	
	//保存账号密码
	private void saveData() {
//		String tIDStr = "";
//		if (iETID != null) {
//			tIDStr = iETID.getText().toString();
//			SharedPreferences.Editor tSPE = iSP.edit();
//			tSPE.putString(GameMain.mGameMain.getString(R.string.Key_SaveData_Login_ID), tIDStr);
//			tSPE.commit();
//		}
//		String tPSWStr = "";
//		if (iETPSW != null) {
//			tPSWStr = iETPSW.getText().toString();
			SharedPreferences.Editor tSPE = iSP.edit();
//			tSPE.putString(GameMain.mGameMain.getString(R.string.Key_SaveData_Login_Password), tPSWStr);
			tSPE.clear();
			tSPE.commit();
//		}
		String ui = iETID.getText().toString();
		String psw = iETPSW.getText().toString();
		MyArStation.mCurrAccountInfo.setName(ui);
		MyArStation.mCurrAccountInfo.setPwdLength((byte) (psw.length()));
		byte[] md5One = MD5.toMD5(psw); // 2次md5加密
		MyArStation.mCurrAccountInfo.setInputPwd(MD5.toMD5(md5One));
		MyArStation.mCurrAccountInfo.setInputPwdOne(md5One);
		MyArStation.mCurrAccountInfo.setSucceedPwd(md5One);
		MyArStation.mCurrAccountInfo.setSavedPwd(true);
		MyArStation.mAccountStore.pushAndUpdate(MyArStation.mCurrAccountInfo);
		MyArStation.mAccountStore.commit();
	}

	private boolean requestRegister() {
		MD5 md5 = new MD5();
		String psw = md5.getMD5ofStr(iETPSW.getText().toString());
		String ui = iETID.getText().toString();
		if(MyArStation.iGameProtocol.create(Url.ip, Url.port)) {
			return MyArStation.iGameProtocol.RequestRegisterB(ui, psw, (short)data.key, answer.getText().toString().trim());
		}
		return false;
	}
	
	private boolean requestRegister(String aIP) {
		MD5 md5 = new MD5();
		String psw = md5.getMD5ofStr(iETPSW.getText().toString());
		String ui = iETID.getText().toString();
		if(MyArStation.iGameProtocol.create(aIP, Url.port)) {
			return MyArStation.iGameProtocol.RequestRegister(ui, psw);
		}
		return false;
	}
	
	private boolean AutoRequestRegister() {
		return false;
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#OnProcessCmd(java.lang.Object, int, int)
	 */
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
		GameProtocol gameProtocol = MyArStation.iGameProtocol;
		MobileMsg pMobileMsg = (MobileMsg) socket;
		switch (aMobileType) {
		case ProtocolType.IM.IM_NOTIFY:
			{
			}
			break;
		case ProtocolType.IM.IM_RESPONSE:
			{
				switch (aMsgID) {
				case ProtocolType.RspMsg.MSG_RSP_REGISTER_B:
					gameProtocol.setSessionID(pMobileMsg.iSessionID);
					MBRspRegisterB pRspRegister = (MBRspRegisterB)pMobileMsg.m_pMsgPara;
					removeProgressDialog();
					if(pRspRegister == null) {
						iGameState = GAME_STATE_NORMAL;
						showSysCom();
						break;
					}
					Tools.debug(pRspRegister.toString());
					int type = pRspRegister.nResult;
					switch (type) {
					case ResultCode.SUCCESS:
						messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RegisterSuccess, pRspRegister.iMsg);
						break;
					default:
						messageEvent(type, pRspRegister.iMsg);
						break;
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

	@SuppressWarnings("deprecation")
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RegisterSuccess:
			saveData();
			hideSysCom();
			removeProgressDialog();
			MyArStation.iGameProtocol.stop();
//			if(((String)msg.obj).length() > 0) {
//				Tools.showSimpleToast(GameMain.mGameMain, Gravity.CENTER, (String)msg.obj, Toast.LENGTH_LONG);
//			}
			//去登录界面
			GameCanvas.getInstance().changeView(new LoginScreen(true));
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_DISCREATE:
			if(((String)msg.obj).length() > 0) {
				Tools.showSimpleToast(iContext, Gravity.CENTER, (String)msg.obj, Toast.LENGTH_LONG);
			}
			removeProgressDialog();
			//
			iGameState = GAME_STATE_NORMAL;
			showSysCom();
			break;
		case ResultCode.ERR_REG_USER_ALREADY_EXIST:
			if(((String)msg.obj).length() > 0) {
				Tools.showSimpleToast(iContext, Gravity.CENTER, (String)msg.obj, Toast.LENGTH_LONG);
			}
			removeProgressDialog();
			//
			iGameState = GAME_STATE_NORMAL;
			showSysCom();
			break;
		case MessageEventID.EMESSAGE_EVENT_TO_REGISTER:
			{
				MyArStation.mGameMain.mainLayout.removeAllViews();
//				if(absoluteLayout == null) {
				layout = (ScrollView) LayoutInflater.from(MyArStation.getInstance().getApplicationContext()).inflate(R.layout.new_regist, null);
				FrameLayout.LayoutParams btnLytp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				btnLytp.gravity = Gravity.CENTER;
				MyArStation.mGameMain.mainLayout.setLayoutParams(btnLytp);
				MyArStation.mGameMain.mainLayout.addView(layout);
				iETID = (EditText) layout.findViewById(R.id.etaccount);
				iETPSW= (EditText) layout.findViewById(R.id.etpswd);
				iETPSWConfirm = (EditText) layout.findViewById(R.id.etpswdsecond);
				answer = (EditText) layout.findViewById(R.id.etAnser);
				question = (Button) layout.findViewById(R.id.btnSelectQ);
				question.setOnClickListener(this);
				commit = (Button) layout.findViewById(R.id.btncommit);
				commit.setOnClickListener(this);
				questionItemDialog = new QuestionItemDialog(MyArStation.mGameMain.getApplicationContext());
				data = new RestPwdData(1, QuestionItemDialog.questions[0]);
				question.setText(data.value);
				questionItemDialog.update(question, question.getWidth(), LayoutParams.WRAP_CONTENT);
				questionItemDialog.setFocusable(false);  
				questionItemDialog.setOutsideTouchable(true);
				questionItemDialog.setItemListen(this);
//				}
//				displaySysCom();
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_REGISTER_MSGBOX:
			{
				Tools.showSimpleToast(MyArStation.getInstance().getApplicationContext(), Gravity.CENTER,
						(String)(msg.obj), Toast.LENGTH_SHORT);
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_REQ_HIDE_SYSCOM:
			{
				//将状态改成Loading
//				iGameState = GAME_STATE_LOADING;
				//
				hideSysCom();
			}
			break;
			
		case SocketErrorType.Error_DecodeError:
		case SocketErrorType.Error_DisConnection:
		case SocketErrorType.Error_TimeOut://接受数据超时处理
			{
				if(iHasReconnected) {
					//将状态改成登正常
					iGameState = GAME_STATE_NORMAL;
					//显示系统控件
					showSysCom();
				}
				else {
					iHasReconnected = true;
					//下载配置文件
					String szUrl = "http://www.funoble.com/configip.zip";//configip.php";
					URLAvailability tUrlLib = new URLAvailability();
					URL tUrl = tUrlLib.isConnect(szUrl);
					if(tUrl == null) {
						//将状态改成登正常
						iGameState = GAME_STATE_NORMAL;
						//显示系统控件
						showSysCom();
					}
					else {
						String filePath = Environment.getExternalStorageDirectory().getPath()+File.separator+"/lyingdice/download/";
						downloadConfig(szUrl, new File(filePath));
					}
					tUrlLib = null;
				}
			}
			break;
			
			//重新连接网络
		case MessageEventID.EMESSAGE_EVENT_RECONNET_IPSTR:
			{
				String ipStr = (String)msg.obj;
				//切换到登录状态
				if(requestRegister(ipStr)) {
					//登录状态
					iGameState = GAME_STATE_LOADING;
					//隐藏系统控件
					hideSysCom();
				}
				else {
					//将状态改成登正常
					iGameState = GAME_STATE_NORMAL;
					//显示系统控件
					showSysCom();
				}
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM:
			{
				//将状态改成登正常
				iGameState = GAME_STATE_NORMAL;
				//显示系统控件
				showSysCom();
			}	
			break;
			
		
		default:
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
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
								System.out.println("Configfile size = " + size);
							}

							@Override
							public void onDownloadFinish(int size, String fileName) {
								System.out.println("Configfile size = " + size + " Config fileName = " + fileName);
								FileReader fr;
								try {
									fr = new FileReader(fileName);
									BufferedReader br = new BufferedReader(fr);
									String ipStr = br.readLine();// 读取一行,如果想读整个文件需要用循环来读
									System.out.println("Configfile data = " + ipStr);//这个可以直接打印出来看
									if(ipStr != null && ipStr.length() > 0) {
										messageEvent(MessageEventID.EMESSAGE_EVENT_RECONNET_IPSTR, ipStr);
									}
									else {
										messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM);
									}
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							@Override
							public void onError(int aErrorID) {
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
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btnSelectQ) {
			questionItemDialog.setWidth(question.getWidth());
			questionItemDialog.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
			questionItemDialog.showAsDropDown(question);
		} else if (id == R.id.btncommit) {
			ItemAction(GameEventID.ESPRITEBUTTON_EVENT_REGISTER_CONFIRM);
		} else {
		}
	}

	/* 
	 * @see com.funoble.lyingdice.adapter.RestPswdAdapter.ItemListen#onClick(java.lang.Object)
	 */
	@Override
	public void onClick(Object o) {
		data = (RestPwdData) o;
		if(data != null) {
			question.setText(data.value);
		}
	}
}