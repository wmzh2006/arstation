package com.funoble.myarstation.screen;

import java.util.Vector;
import java.util.jar.Attributes.Name;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.common.Util;
import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.GameButtonHandler;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.socket.protocol.MBRspCommitPlayerData;
import com.funoble.myarstation.socket.protocol.MBRspCreateRole;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.CallPointManager;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.TPoint;
import com.funoble.myarstation.view.GameEventID;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.MessageEventID;

public class CreateRoleScreen extends GameView implements GameButtonHandler {

	private final int GAME_STATE_NORMAL = 0;
	private final int GAME_STATE_LOADING = 1;
	
	private RelativeLayout absoluteLayout;
	private EditText iETID;//昵称输入框
	private TextView tvDes;
	
	private Project iUIPak = null;
	private Scene iScene = null;
	private Scene iSceneLoading = null;
	private Vector<SpriteButton> iSpriteButtonList = new Vector<SpriteButton>();//精灵按钮 列表
	private SpriteButton iButtonPlayerA = null;
	private SpriteButton iButtonPlayerB = null;
	private SpriteButton iButtonPlayerC = null;
	private SpriteButton iButtonPlayerD = null;
	private SpriteButton iButtonPlayerE = null;
	
	private int iGameState = GAME_STATE_NORMAL;
	private int iSelectedRoleID = 0;//已选的角色ID
	
//	private TextUtil textUtil;
	private String[] RoleDescs;
	private String[] RoleNames;
	private Point[] rolesPoints = new Point[5];
	private boolean isRecreate = false;
	
	public CreateRoleScreen() {
		isRecreate = false;
	}
	
	public CreateRoleScreen(boolean recreate) {
		isRecreate = recreate;
	}
	
	@Override
	public void init() {
		loadUIPak();
		RoleDescs = MyArStation.getInstance().getResources().getStringArray(R.array.rolesDesc);
		RoleNames = MyArStation.getInstance().getResources().getStringArray(R.array.rolesname);
		messageEvent(GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_CREATE_SYSCOM);
//		textUtil = new TextUtil(RoleDescs[iSelectedRoleID],
//								168*ActivityUtil.ZOOM_X, 
//								276*ActivityUtil.ZOOM_Y,
//								474*ActivityUtil.ZOOM_X,
//								109*ActivityUtil.ZOOM_Y,
//								Color.BLACK,
//								Color.WHITE, 
//								0,
//								(int) (24 * ActivityUtil.ZOOM_X));
	}

	@Override
	public void releaseResource() {
		iButtonPlayerA = null;
		iButtonPlayerB = null;
		iButtonPlayerC = null;
		iButtonPlayerD = null;
		iButtonPlayerE = null;
//		iSpriteButtonList.clear();
//		iSpriteButtonList = null;
		iSceneLoading = null;
		iScene = null;
		iUIPak = null;
	}

	@Override
	public void paintScreen(Canvas g, Paint paint) {
		g.drawBitmap(MyArStation.iImageManager.iBackLoginBmp,
				0,
				0,
				null);
		if(iGameState == GAME_STATE_NORMAL) {
			if(iScene != null) {
				iScene.paint(g, 0, 0);
			}
			for(int i=0; i<iSpriteButtonList.size(); i++) {
				((SpriteButton)iSpriteButtonList.elementAt(i)).paint(g);
			}
			
			if(RoleNames != null && rolesPoints != null) {
				for(int i = 0; i < rolesPoints.length; i++) {
					if(iSelectedRoleID == i) {
						ActivityUtil.mSelectRolePaint.setColor(Color.BLUE);
					}
					else {
						ActivityUtil.mSelectRolePaint.setColor(Color.WHITE);
					}
					g.drawText(RoleNames[i], rolesPoints[i].x, rolesPoints[i].y + ActivityUtil.mSelectRolePaint.getTextSize(), ActivityUtil.mSelectRolePaint);
				}
			}
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
			if(iScene != null) {
				iScene.handle();
			}
			for(int i=0; i<iSpriteButtonList.size(); i++) {
				((SpriteButton)iSpriteButtonList.elementAt(i)).perform();
			}
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
			hideSysCom();
			if(isRecreate) {
				GameCanvas.getInstance().changeView(new InfoScreen());
			}
			else {
				GameCanvas.getInstance().changeView(new LoginScreen());
			}
			//
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
//		System.out.println("CreateRoleScreen aEventID=" + aEventID);
		int tCount = (int)(Math.random() * 14);
		int tDice = (int)(Math.random() * 7);
		if(tCount < 2) {
			tCount = 2;
		}
		if(tDice < 1) {
			tDice = 1;
		}
		else if(tDice > 6) {
			tDice = 6;
		}
		//
		switch (aEventID) {
		case GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_CREATE:
			{
				messageEvent(GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_REQUEST);
			}
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_RECREATE:
		{
			messageEvent(GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_REQUEST);
		}
		break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_PLAYER1:
			{
//				System.out.println("CreateRoleScreen 11111111111111111111");
				iSelectedRoleID = 0;
				iButtonPlayerA.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				iButtonPlayerB.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				iButtonPlayerC.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				iButtonPlayerD.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				iButtonPlayerE.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				//
				CallPointManager.getSoundManager().CallPoint(0, tCount, tDice);
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_PLAYER2:
			{
//				System.out.println("CreateRoleScreen 2222222222222222222");
				iSelectedRoleID = 1;
				iButtonPlayerA.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				iButtonPlayerB.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				iButtonPlayerC.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				iButtonPlayerD.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				iButtonPlayerE.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				//
				CallPointManager.getSoundManager().CallPoint(1, tCount, tDice);
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_PLAYER3:
			{
				iSelectedRoleID = 2;
				iButtonPlayerA.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				iButtonPlayerB.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				iButtonPlayerC.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				iButtonPlayerD.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				iButtonPlayerE.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				//
				CallPointManager.getSoundManager().CallPoint(2, tCount, tDice);
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_PLAYER4:
			{
//				System.out.println("CreateRoleScreen 333333333333333333");
				iSelectedRoleID = 3;
				iButtonPlayerA.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				iButtonPlayerB.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				iButtonPlayerC.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				iButtonPlayerD.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				iButtonPlayerE.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				//
				CallPointManager.getSoundManager().CallPoint(3, tCount, tDice);
			}
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_PLAYER5:
		{
//				System.out.println("CreateRoleScreen 333333333333333333");
			iSelectedRoleID = 4;
			iButtonPlayerA.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
			iButtonPlayerB.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
			iButtonPlayerC.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
			iButtonPlayerD.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
			iButtonPlayerE.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
			//
			CallPointManager.getSoundManager().CallPoint(iSelectedRoleID, tCount, tDice);
		}
		break;
			
		default:
			break;
		}
		messageEvent(MessageEventID.EMESSAGE_EVENT_SHOW_DECR);
//		textUtil = null;
//		textUtil = new TextUtil(RoleDescs[iSelectedRoleID],
//				168*ActivityUtil.ZOOM_X, 
//				276*ActivityUtil.ZOOM_Y,
//				474*ActivityUtil.ZOOM_X,
//				109*ActivityUtil.ZOOM_Y,
//				Color.BLACK,
//				Color.WHITE, 
//				0,
//				(int) (24 * ActivityUtil.ZOOM_X));
		return true;
	}
	
	private void loadUIPak() {
		iUIPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"createrole_map.pak", false);
		if (iUIPak != null) {
			if(isRecreate) {
				iScene = iUIPak.getScene(1);
			}
			else {
				iScene = iUIPak.getScene(0);
			}
		}
		if(iScene != null) {
			iScene.setViewWindow(0, 0, 800, 480);
			iScene.initDrawList();
			iScene.initNinePatchList(new int[] {R.drawable.gdbj, R.drawable.gdbj, R.drawable.new_text_bg3, R.drawable.new_text_bg3});
			Map pMap = iScene.getLayoutMap(Scene.SpriteLayout);
			Vector<?> pSpriteList = pMap.getSpriteList();
			Sprite pSprite;
			int size = pSpriteList.size();
			for (int i=0; i<pSpriteList.size(); i++) {
				pSprite = (Sprite)pSpriteList.elementAt(i);
				int pX = pSprite.getX();
				int pY = pSprite.getY();
				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_OFFSET + pSprite.getIndex();
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
				if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_PLAYER1) {
					iButtonPlayerA = pSpriteButton;
					iButtonPlayerA.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
					Rect rect = pSpriteButton.getRect();
					rolesPoints[0] = new Point((int) (pSpriteButton.getX()), pSpriteButton.getY() + rect.bottom);
				}
				else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_PLAYER2) {
					iButtonPlayerB = pSpriteButton;
					iButtonPlayerB.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
					Rect rect = pSpriteButton.getRect();
					rolesPoints[1] = new Point((int) (pSpriteButton.getX()), pSpriteButton.getY() + rect.bottom);
				}
				else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_PLAYER3) {
					iButtonPlayerC = pSpriteButton;
					iButtonPlayerC.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
					Rect rect = pSpriteButton.getRect();
					rolesPoints[2] = new Point((int) (pSpriteButton.getX()), pSpriteButton.getY() + rect.bottom);
				}
				else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_PLAYER4) {
					iButtonPlayerD = pSpriteButton;
					iButtonPlayerD.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
					Rect rect = pSpriteButton.getRect();
					rolesPoints[3] = new Point((int) (pSpriteButton.getX()), pSpriteButton.getY() + rect.bottom);
				}
				else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_PLAYER5) {
					iButtonPlayerE = pSpriteButton;
					iButtonPlayerE.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
					Rect rect = pSpriteButton.getRect();
					rolesPoints[4] = new Point((int) (pSpriteButton.getX()), pSpriteButton.getY() + rect.bottom);
				}
			}
		}
	}
	
	//显示系统组件
	private void displaySysCom() {
		try {
			MyArStation.mGameMain.mainLayout.removeAllViews();
			if(absoluteLayout == null) {
				absoluteLayout = (RelativeLayout) LayoutInflater.from(MyArStation.getInstance().getApplicationContext()).inflate(R.layout.createrole, null);
				MyArStation.mGameMain.mainLayout.addView(absoluteLayout);
			}
			float pxETIDX = 308; //800*480的像素值
			float pxETIDY = 405;
			float pxW = 332;
			float pxH = 47;
			//转成对应屏幕分辨率的值
			pxETIDX *= ActivityUtil.ZOOM_X; 
			pxETIDY *= ActivityUtil.ZOOM_Y;
			pxW *= ActivityUtil.ZOOM_X;
			pxH *= ActivityUtil.ZOOM_Y;
			//
//		int dipETIDX = (int)(pxETIDX / ActivityUtil.DENSITY_ZOOM_X);//(int) (iGameCanvas.px2dip(iContext, pxETIDX) * ActivityUtil.DENSITY_ZOOM_X);
//		int dipETIDY = (int)(pxETIDY / ActivityUtil.DENSITY_ZOOM_Y);//(int) (iGameCanvas.px2dip(iContext, pxETIDY) * ActivityUtil.DENSITY_ZOOM_Y);
//		int dipRight = (int)((pxETIDX + pxW) / ActivityUtil.DENSITY_ZOOM_X);
//		
			iETID = (EditText) absoluteLayout.findViewById(R.id.editTextCreateRoleName);
			if (iETID != null) {
				iETID.setVisibility(View.VISIBLE);
				LayoutParams lp = (LayoutParams) iETID.getLayoutParams();
				lp.leftMargin = (int) pxETIDX;//(int) (lp.x*ActivityUtil.DENSITY_ZOOM_X);lp.width = dipRight - lp.x;
				lp.topMargin = (int) pxETIDY;//(int) (lp.y*ActivityUtil.DENSITY_ZOOM_Y);
				lp.width = (int) pxW;
				lp.height = (int) pxH;
				iETID.setLayoutParams(lp);
				if(isRecreate && MyArStation.iPlayer.stUserNick != null) {
					String[] nane = MyArStation.iPlayer.stUserNick.split("\\(");
					iETID.setHint(nane != null ? nane[0] : MyArStation.iPlayer.stUserNick);
				}
			}
			float x = 168*ActivityUtil.ZOOM_X;
			float y = 276*ActivityUtil.ZOOM_Y;
			float w = 474*ActivityUtil.ZOOM_X;
			float h = 104*ActivityUtil.ZOOM_Y;
			tvDes = (TextView) absoluteLayout.findViewById(R.id.editTextCreateRoleDes);
			if(tvDes != null) {
				tvDes.setVisibility(View.VISIBLE);
				LayoutParams lParams = (LayoutParams) tvDes.getLayoutParams();
				lParams.leftMargin = (int) x;
				lParams.topMargin = (int) y;
				lParams.width = (int) w;
				lParams.height = (int) h;
				tvDes.setLayoutParams(lParams);
				tvDes.setText(RoleDescs[iSelectedRoleID]);
				tvDes.setMovementMethod(ScrollingMovementMethod.getInstance());  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//隐藏系统组件
	private void hideSysCom() {
		if (iETID != null) {
			iETID.setVisibility(View.INVISIBLE);
		}
		if(tvDes != null) {
			tvDes.setVisibility(View.INVISIBLE);
		}
	}
	
	//显示系统组件
	private void showSysCom() {
		if (iETID != null) {
			iETID.setVisibility(View.VISIBLE);
		}
		if(tvDes != null) {
			tvDes.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * description 检查呢称
	 * @return true:合法，false:非法
	 */
	private boolean checkUserName() {
		boolean tbError = false;//是否出错了
		String tInfo = "";//提示语
		String tStr = "";
		if (iETID != null) {
			tStr = iETID.getText().toString().replace(" ", "");//id字符串
			if (tStr == null || tStr.length() < 2){
					tInfo = "昵称过短！";
					tbError = true;
			}
			else if(!Util.inputValidName(tStr)) {
				tInfo = "昵称不能含有特殊字符！";
				tbError = true;
			}
		}
		if (tbError) {
			Tools.showSimpleToast(MyArStation.getInstance().getApplicationContext(), Gravity.CENTER,
					tInfo, Toast.LENGTH_SHORT);
		}
		return !tbError;
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
			case RspMsg.MSG_RSP_CREATEROLE:
				removeProgressDialog();
				MBRspCreateRole pRSCreateRole = (MBRspCreateRole)pMobileMsg.m_pMsgPara;
				if(pRSCreateRole == null) {
					messageEvent(GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_SHOW_SYSCOM);
					break;
				}
				int type = pRSCreateRole.nResult;
				Tools.debug(pRSCreateRole.toString());
				switch (type) {
					case ResultCode.SUCCESS://成功
						messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspCreateRoleSuccess, pRSCreateRole);
						break;						
					default:
						messageEvent(GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_SHOW_SYSCOM);
						messageEvent(type, pRSCreateRole);
						break;
				}
				break;
			case RspMsg.MSG_RSP_COMMIT_PLAYER_DATA:
				MBRspCommitPlayerData pRSCommitPlayerData = (MBRspCommitPlayerData)pMobileMsg.m_pMsgPara;
				if(pRSCommitPlayerData == null) {
					messageEvent(GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_SHOW_SYSCOM);
					break;
				}
				type = pRSCommitPlayerData.nResult;
				Tools.debug(pRSCommitPlayerData.toString());
				messageEvent(GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_RENAME, pRSCommitPlayerData);
				
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
		switch (msg.what) {
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspCreateRoleSuccess:
			{
				MBRspCreateRole pRSCreateRole = (MBRspCreateRole)msg.obj;
				//去教程
				MyArStation.mGameMain.mainLayout.removeAllViews();
				MyArStation.mGameMain.mainLayout.invalidate();
				GameCanvas.getInstance().changeView(new TutorialScreen());
			}
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_RENAME:
			MBRspCommitPlayerData rspCommitPlayerData = (MBRspCommitPlayerData) msg.obj;
			if(rspCommitPlayerData.iMsg != null && rspCommitPlayerData.iMsg.length() > 0) {
				Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), rspCommitPlayerData.iMsg);
			}
			if(rspCommitPlayerData.nResult == 0) {
				GameCanvas.getInstance().changeView(new InfoScreen());
			}
			else {
				messageEvent(GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_SHOW_SYSCOM);
			}
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_CREATE_SYSCOM:
			{
				iGameState = GAME_STATE_NORMAL;
				displaySysCom();
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_SHOW_SYSCOM:
			{
				iGameState = GAME_STATE_NORMAL;
				showSysCom();
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_HIDE_SYSCOM:
			{
				iGameState = GAME_STATE_LOADING;
				hideSysCom();
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_CREATEROLE_REQUEST:
			{
				if(!checkUserName()) {
					break;
				}
				iGameState = GAME_STATE_LOADING;
				hideSysCom();
				if(isRecreate) {
					MyArStation.iGameProtocol.RequestCommitPlayerData(iSelectedRoleID, iETID.getText().toString().trim());
				}
				else {
					MyArStation.iGameProtocol.RequsetCteateRole(iSelectedRoleID, iETID.getText().toString().trim());
				}
			}	
			break;
		case MessageEventID.EMESSAGE_EVENT_SHOW_DECR:
			if(tvDes != null) {
				tvDes.setText(RoleDescs[iSelectedRoleID]);
				tvDes.scrollTo(0, 0);
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
	
}
