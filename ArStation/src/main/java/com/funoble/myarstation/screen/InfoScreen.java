package com.funoble.myarstation.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.common.Util;
import com.funoble.myarstation.data.cach.MessageData;
import com.funoble.myarstation.game.CPlayer;
import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.CBuilding;
import com.funoble.myarstation.gamebase.GameButtonHandler;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.gamelogic.SpriteButtonGift;
import com.funoble.myarstation.gamelogic.SpriteButtonGift.OnGiftSelectListen;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.socket.protocol.MBRspChangeDiceType;
import com.funoble.myarstation.socket.protocol.MBRspDelLeaveMsg;
import com.funoble.myarstation.socket.protocol.MBRspHart;
import com.funoble.myarstation.socket.protocol.MBRspHomeReceiveGiftList;
import com.funoble.myarstation.socket.protocol.MBRspHomeTakeGift;
import com.funoble.myarstation.socket.protocol.MBRspLeaveMsgB;
import com.funoble.myarstation.socket.protocol.MBRspLeaveMsgListB;
import com.funoble.myarstation.socket.protocol.MBRspPlayerInfoFour;
import com.funoble.myarstation.socket.protocol.MBRspPlayerInfoThree;
import com.funoble.myarstation.socket.protocol.MBRspPlayerInfoTwo;
import com.funoble.myarstation.socket.protocol.MBRspResetPwd;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.store.data.GiveGiftData;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.GiftUtil;
import com.funoble.myarstation.utils.Graphics;
import com.funoble.myarstation.utils.TPoint;
import com.funoble.myarstation.view.ChangeDiceDialog;
import com.funoble.myarstation.view.ChangeDicePopView;
import com.funoble.myarstation.view.ChatView;
import com.funoble.myarstation.view.GameEventID;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.InfoPopView;
import com.funoble.myarstation.view.MessageDialog;
import com.funoble.myarstation.view.MessageEventID;
import com.funoble.myarstation.view.SetAserPswddDialog;
import com.funoble.myarstation.view.InfoPopView.onClickReameListen;

public class InfoScreen extends GameView implements GameButtonHandler, 
													onClickReameListen {
	private final int HEARTOFFSET = 10000;
	private final int ONESUNINDEX = 25;
	private GameCanvas iGameCanvas = null;
	
	//0 --- 载入数据     
	//1 --- 各面板进场   
	//2 --- 正常   
	//3 --- 退回主界面 
	//4 --- 创建详细资料页面
	//5 --- 显示详细资料页面
	//6 --- 修改名字
	//7 --- 拍照
	private int	iGameState = 0;	

	private boolean createBMP = false;
	private float iSppX = 0;
	private float iSoX = 0;
	private float iSbY = 0;
	private int iSppSpeed = 0;
	private int iSoSpeed = 0;
	private int iSbSpeed = 0;
	
//	private Project iUIPak = null;
	private Project iMyHomePak = null;
	private Project iInfoPak = null;
//	private Scene iScene = null;
	private Scene iSceneLoading = null;
	private Scene iSceneMyHome = null;
	private Scene iSceneDetail = null;
	private Scene iScenePlayerPhoto = null;
//	private Scene iSceneOperate = null;
	private Scene iSceneBar = null;
	private Vector<SpriteButton> iSpriteButtonList = new Vector<SpriteButton>();//精灵按钮 列表
	private Vector<SpriteButton> iSpriteButtonListBar = new Vector<SpriteButton>();//精灵按钮 列表
	private Vector<SpriteButton> iSpriteButtonListPhoto = new Vector<SpriteButton>();//精灵按钮 列表
	private Vector<SpriteButton> iSpritebuttonListDetail = new Vector<SpriteButton>();//精灵按钮 列表
	private Vector<SpriteButtonGift> iGiftSpriteButtons = new Vector<SpriteButtonGift>();//精灵按钮 列表
	private CBuilding iDiceSprite = null; 	//骰子动画
	private CBuilding iVipSprite = null;	//VIP动画
	private CBuilding iCardSprite = null;	//酒水卡动画
	private Sprite iSkillZhaiSprite = null;	//斋技能的图标
	private Sprite iSkillLightnSprite = null; //雷劈技能的图标
	private CBuilding iPhotoBoxSprite = null;  //相框
	private CBuilding iBalloonSprite = null;	//气球
	private CBuilding iBalloonSunSprite = null;	//气球
	private CBuilding iSexSprite = null;	//性别
	
	private Sprite iDrunkSprite = null;	//酒杯动画
	private NinePatch iNinePatch = null; //经验条
//	private NinePatch iExpBmp = null;
	private Bitmap iBackBmp = null;	//背景图
	
	private Paint iLovePaint;//顶 画笔
	
	//玩家信息
	private Paint iExpPaint;
	private RectF iExpRect;
	private Bitmap iPicBMP = null;//照片图片 原图
	private Bitmap iPicBMPZoom = null;//照片图片 缩放图
	private float iPicX = -1000*ActivityUtil.ZOOM_X;
	private float iPicY = -1000*ActivityUtil.ZOOM_Y;
	private float iCarPicX = -1000*ActivityUtil.ZOOM_X;
	private float iCarPicY = -1000*ActivityUtil.ZOOM_Y;

	private Paint iLoadingPaint;//
	private Bitmap iCarBMP = null;	//汽车头像
	
	private Context Context = MyArStation.mGameMain;
	
	private MBRspPlayerInfoTwo iPlayerInfo = null;
//	private ChangeDiceDialog  changeDiceDialog;
	private MessageDialog   messageDialog;
	private ChatView 		iChatView = null;
	
	private int iUserID = 0;
	private int[] iPosition;
	private RectF	iLeaveNotifyPos;
	private int iScreenType = 0;
	private CPlayer iPlayer = null;
	private SetAserPswddDialog    setAserPswddDialog; //设置密码问题
	private InfoPopView infoPopView;//详细信息
	private ChangeDicePopView changeDicePopView;
	
	public InfoScreen() {
		iUserID = MyArStation.iPlayer.iUserID;
		iScreenType = 1;//返回主页
	}
	
	public InfoScreen(int aUserID, int aPosition) {
		iUserID = aUserID;
		iPosition = new int[1];
		iPosition[0] = aPosition;
	}
	
	@Override
	public void init() {
		
		iSceneLoading = LoadLoading();
		loadResThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				initial();
			}
		});
		loadResThread.start();
	}

	/**
	 * 
	 */
	private void initial() {
		//
		iGameCanvas = GameCanvas.getInstance();
		//
		iPlayer = MyArStation.iPlayer;
		//
		iExpRect = new RectF();
		setExpRect();
		initPaint();
		loadUIPak();
		//载入经验图片
		Bitmap bitmap = BitmapFactory.decodeResource(MyArStation.mGameMain.getResources(), R.drawable.exp4);
		iNinePatch = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
		bitmap = BitmapFactory.decodeResource(MyArStation.mGameMain.getResources(), R.drawable.exp2);
//		iExpBmp = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
		bitmap = null;
		//
		iBackBmp = Bitmap.createBitmap(ActivityUtil.SCREEN_WIDTH, ActivityUtil.SCREEN_HEIGHT, Config.ARGB_8888);
		//初始化图片
		initPic();
		//初始化汽车图片
		initCarPic();
		//
		requestPlayerInfo();
		//
		//800 * 400 的坐标
		iSppX = 500;
		iSoX = 500;
		iSbY = 100;
		//转成对应屏幕分辨率的值
		iSppX *= (float)ActivityUtil.SCREEN_WIDTH / (float)ActivityUtil.SCREEN_WIDTH_STANDARD; 
		iSoX *= (float)ActivityUtil.SCREEN_WIDTH / (float)ActivityUtil.SCREEN_WIDTH_STANDARD;
		iSbY *= (float)ActivityUtil.SCREEN_HEIGHT / (float)ActivityUtil.SCREEN_HEIGHT_STANDARD;
		iSppSpeed = (int)(iSppX / 6);
		iSoSpeed = (int)(iSoX / 6);
		iSbSpeed = (int)(iSbY / 6);
		infoPopView = new InfoPopView();
		infoPopView.init();
		infoPopView.setOnClickRename(this);
		changeDicePopView = new ChangeDicePopView();
		MyArStation.iGameProtocol.requestHomeReveiceGiftList(iUserID);
		messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM);
	}

	@Override
	public void releaseResource() {
		if(null != infoPopView) infoPopView.releaseResource();
		if(null != changeDicePopView) changeDicePopView.releaseResource();
		iSexSprite = null;
		iBalloonSprite = null;
		iBalloonSunSprite = null;
		iPhotoBoxSprite = null;
		iSkillLightnSprite = null; //雷劈技能的图标
		iSkillZhaiSprite = null;	//斋技能的图标
		iBackBmp = null;
//		iExpBmp = null;
		iNinePatch = null;
		iSceneDetail = null;
		iSceneMyHome = null;
		iSceneLoading = null;
		iInfoPak = null;
		iMyHomePak = null;
//		iUIPak = null;

		if(iCarBMP != null) {
			iCarBMP.recycle();
			iCarBMP = null;
		}
		if (iPicBMP != null) {
			iPicBMP.recycle();
			iPicBMP = null;
		}
		if (iPicBMPZoom != null) {
			iPicBMPZoom.recycle();
			iPicBMPZoom = null;
		}

//		if(changeDiceDialog != null) {
//			changeDiceDialog.dismiss();
//			changeDiceDialog = null;
//		}
		if(messageDialog != null) {
			messageDialog.dismiss();
			messageDialog = null;
		}
		if(iChatView != null) {
			iChatView.dismissAll();
		}
		iChatView = null;
		if(setAserPswddDialog != null) {
			setAserPswddDialog.dismiss();
			setAserPswddDialog = null;
		}
		iGiftSpriteButtons = null;
	}

	@Override
	public void paintScreen(Canvas g, Paint paint) {
		//载入数据
		if(iGameState == 0) {
			if(MyArStation.iImageManager.loadLoginBack() != null) {
				g.drawBitmap(MyArStation.iImageManager.loadLoginBack(),
						0,
						0,
						null);
			}
			if(iSceneLoading != null) {
				iSceneLoading.paint(g, 0, 0);
//				g.drawText("正在读取数据", 278*ActivityUtil.ZOOM_X, 288*ActivityUtil.ZOOM_Y, ActivityUtil.mLoading);
			}
			return;
		}
		else if(iGameState == 2) {
			if(iBackBmp != null) {
				g.drawBitmap(iBackBmp,
						0,
						0,
						null);
			}
			if(iSceneMyHome != null) {
				iSceneMyHome.paintLayer(g, 0, 0, Scene.BuildLayout);
			}
			if(iScenePlayerPhoto != null) {
			}
			if(iPlayer.iVipLevel > 0) {
				g.drawText(""+iPlayer.stUserNick, 228*ActivityUtil.ZOOM_X, 182*ActivityUtil.ZOOM_Y, ActivityUtil.mVipNickPaint);
			}
			else {
				g.drawText(""+iPlayer.stUserNick, 228*ActivityUtil.ZOOM_X, 182*ActivityUtil.ZOOM_Y, ActivityUtil.mNormalNickPaint);
			}
			if(iSceneBar != null) {
				//
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).paint(g, iSceneBar.iCameraX, iSceneBar.iCameraY);
				}
				drawNewMsgCount(g);
			}
			for(int i = 0; i <  iGiftSpriteButtons.size(); i++) {
				iGiftSpriteButtons.get(i).paint(g);
			}
			if(null != infoPopView) infoPopView.paintScreen(g, paint);
			if(null != changeDicePopView) changeDicePopView.paintScreen(g, paint);
		}
		//各面板进场
		else if(iGameState >= 1) {
			if(iSceneMyHome != null) {
				iSceneMyHome.paint(g, 0, 0);
			}
			if(iScenePlayerPhoto != null) {
				//
				if (iPicBMP != null) {
					g.drawBitmap(iPicBMP, iSppX+iPicX, iPicY, null);
				}
				if (iPicBMPZoom != null) {
					g.drawBitmap(iPicBMPZoom, iSppX+iPicX, iPicY, null);
				}
				//
				iScenePlayerPhoto.paint(g, 0, 0);
//				//昵称
//				g.drawText(""+iPlayer.stUserNick, 228*ActivityUtil.ZOOM_X, 192*ActivityUtil.ZOOM_Y, ActivityUtil.mVipNickPaint);
				if(iPlayer.iVipLevel > 0) {
					g.drawText(""+iPlayer.stUserNick, 228*ActivityUtil.ZOOM_X, 182*ActivityUtil.ZOOM_Y, ActivityUtil.mVipNickPaint);
				}
				else {
					g.drawText(""+iPlayer.stUserNick, 228*ActivityUtil.ZOOM_X, 182*ActivityUtil.ZOOM_Y, ActivityUtil.mNormalNickPaint);
				}
				for(int i=0; i<iSpriteButtonListPhoto.size(); i++) {
					((SpriteButton)iSpriteButtonListPhoto.elementAt(i)).paint(g, iScenePlayerPhoto.iCameraX, iScenePlayerPhoto.iCameraY);
				}
			}
			if(iSceneBar != null) {
				iSceneBar.paint(g, 0, 0);
				//
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).paint(g, iSceneBar.iCameraX, iSceneBar.iCameraY);
				}
				drawNewMsgCount(g);
			}
		}		
	}

	@Override
	public void performL() {
		if(iGameState == 2 && createBMP) {//创建图片
			createBMP = false;
			//创建背景
			creatBackBmp();
		}
		//载入数据
		if(iGameState == 0) {
			if(iSceneLoading != null) {
				iSceneLoading.handle();
			}
		}
		//各面板进场
		else if(iGameState == 1) {
			iSppX -= iSppSpeed;
			if(iSppX <= 0) {
				iSppX = 0;
			}
			iSoX -= iSoSpeed;
			if(iSoX <= 0) {
				iSoX = 0;
			}
			iSbY -= iSbSpeed;
			if(iSbY <= 0) {
				iSbY = 0;
			}
			if(iSppX == 0 && iSoX == 0 && iSbY == 0) {
				iGameState = 2;
				//创建背景
				creatBackBmp();
			}
			//
			if(iSceneMyHome != null) {
				iSceneMyHome.handle();
			}
			//
			if(iScenePlayerPhoto != null) {
				iScenePlayerPhoto.handle();
				iScenePlayerPhoto.iCameraX = -(int)iSppX;
				//
				for(int i=0; i<iSpriteButtonListPhoto.size(); i++) {
					((SpriteButton)iSpriteButtonListPhoto.elementAt(i)).perform();
				}
			}
			//
			if(iSceneBar != null) {
				iSceneBar.handle();
				iSceneBar.iCameraY = -(int)iSbY;
				//
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).perform();
				}
			}
		}
		//正常
		else if(iGameState == 2) {
			if(null != infoPopView) infoPopView.performL();
			if(null != changeDicePopView) changeDicePopView.performL();
			for(int i = 0; iGiftSpriteButtons != null && i < iGiftSpriteButtons.size(); i++) {
				iGiftSpriteButtons.get(i).perform();
			}
			//
			if(iSceneMyHome != null) {
				iSceneMyHome.handle();
			}
			//
			if(iScenePlayerPhoto != null) {
				iScenePlayerPhoto.handle();
				iScenePlayerPhoto.iCameraX = 0;
				//
				for(int i=0; i<iSpriteButtonListPhoto.size(); i++) {
					((SpriteButton)iSpriteButtonListPhoto.elementAt(i)).perform();
				}
			}
			//
			if(iSceneBar != null) {
				iSceneBar.handle();
				iSceneBar.iCameraY = 0;
				//
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).perform();
				}
			}
		}		
		else if(iGameState == 3) {
			if(iSppX < 500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < 100) {
				iSbY += iSbSpeed;
			}
			//
			if(iSceneMyHome != null) {
				iSceneMyHome.handle();
			}
			//
			if(iScenePlayerPhoto != null) {
				iScenePlayerPhoto.handle();
				iScenePlayerPhoto.iCameraX = -(int)iSppX;
				//
				for(int i=0; i<iSpriteButtonListPhoto.size(); i++) {
					((SpriteButton)iSpriteButtonListPhoto.elementAt(i)).perform();
				}
			}
			//
			if(iSceneBar != null) {
				iSceneBar.handle();
				iSceneBar.iCameraY = -(int)iSbY;
				//
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).perform();
				}
			}			
			if(iSppX >= 500 && iSoX >= 500 && iSbY >= 100) {
				//切换到个人资料界面
				if(iScreenType == 1) {
					iGameCanvas.changeView(new FriendInfoScreen());
				}
				else {
					iGameCanvas.changeView(new FriendInfoScreen(iUserID, iPosition[0]));
				}
			}
		}
		else if(iGameState == 4) {
			//iGameState = 5;
			//创建背景
			//creatBackBmp();
			if(iSceneDetail != null) {
				iSceneDetail.handle();
				//
				for(int i=0; i<iSpritebuttonListDetail.size(); i++) {
					((SpriteButton)iSpritebuttonListDetail.elementAt(i)).perform();
				}
			}
		}
		else if(iGameState == 6) {
			if(iSppX < 500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < 100) {
				iSbY += iSbSpeed;
			}
			//
			if(iSceneMyHome != null) {
				iSceneMyHome.handle();
			}
			//
			if(iScenePlayerPhoto != null) {
				iScenePlayerPhoto.handle();
				iScenePlayerPhoto.iCameraX = -(int)iSppX;
				//
				for(int i=0; i<iSpriteButtonListPhoto.size(); i++) {
					((SpriteButton)iSpriteButtonListPhoto.elementAt(i)).perform();
				}
			}
			//
			if(iSceneBar != null) {
				iSceneBar.handle();
				iSceneBar.iCameraY = -(int)iSbY;
				//
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).perform();
				}
			}			
			if(iSppX >= 500 && iSoX >= 500 && iSbY >= 100) {
				//切换到修改名称界面
				iGameCanvas.changeView(new CreateRoleScreen(true));
			}
		}
		else if(iGameState == 7) {
			if(iSppX < 500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < 100) {
				iSbY += iSbSpeed;
			}
			//
			if(iSceneMyHome != null) {
				iSceneMyHome.handle();
			}
			//
			if(iScenePlayerPhoto != null) {
				iScenePlayerPhoto.handle();
				iScenePlayerPhoto.iCameraX = -(int)iSppX;
				//
				for(int i=0; i<iSpriteButtonListPhoto.size(); i++) {
					((SpriteButton)iSpriteButtonListPhoto.elementAt(i)).perform();
				}
			}
			//
			if(iSceneBar != null) {
				iSceneBar.handle();
				iSceneBar.iCameraY = -(int)iSbY;
				//
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).perform();
				}
			}			
			if(iSppX >= 500 && iSoX >= 500 && iSbY >= 100) {
				//切换到个人资料界面
				GameCanvas.getInstance().changeView(new AlbumScreen(1, iPlayer, iUserID, 0));
			}
		}
	}

	//创建背景图
	private void creatBackBmp() {
		iSoX = 0;
		iSbY = 0;
		iSppX = 0;
		Canvas g  = new Canvas(iBackBmp);

		if(iSceneMyHome != null) {
			iSceneMyHome.paintGround(g, 0, 0);//.paint(g, 0, 0);
		}
		if(iScenePlayerPhoto != null) {
			//
			if (iPicBMP != null) {
				g.drawBitmap(iPicBMP, iSppX+iPicX, iPicY, null);
			}
			if (iPicBMPZoom != null) {
				g.drawBitmap(iPicBMPZoom, iSppX+iPicX, iPicY, null);
			}
			//
			iScenePlayerPhoto.iCameraX = 0;
			iScenePlayerPhoto.paint(g, 0, 0);
//			//财富榜
//			if (iPlayer.iWealthRank > 0) {
//				g.drawText("财富榜  "+iPlayer.iWealthRank, iSppX+306*ActivityUtil.ZOOM_X, 108*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
//			}
//			else {
//				g.drawText("财富榜  ??", iSppX+306*ActivityUtil.ZOOM_X, 108*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
//			}
//			//等级榜
//			if (iPlayer.iLevelRank > 0) {
//				g.drawText("等级榜  "+iPlayer.iLevelRank, iSppX+306*ActivityUtil.ZOOM_X, 138*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
//			}
//			else {
//				g.drawText("等级榜  ??", iSppX+306*ActivityUtil.ZOOM_X, 138*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
//			}
//			//魅力榜
//			if (iPlayer.iVictoryRank > 0) {
//				g.drawText("魅力榜  "+iPlayer.iVictoryRank, iSppX+306*ActivityUtil.ZOOM_X, 168*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
//			}
//			else {
//				g.drawText("魅力榜  ??", iSppX+306*ActivityUtil.ZOOM_X, 168*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
//			}
//			//灌倒
//			if(iPlayer.iReward <= 99999) {
//				g.drawText("灌倒 "+iPlayer.iReward, iSppX+306*ActivityUtil.ZOOM_X, 198*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
//			}
//			else {
//				g.drawText("灌倒 "+iPlayer.iReward/10000+"万", iSppX+306*ActivityUtil.ZOOM_X, 198*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
//			}
//			//胜场
//			if(iPlayer.iWinCount <= 99999) {
//				g.drawText("胜场 "+iPlayer.iWinCount, iSppX+306*ActivityUtil.ZOOM_X, 228*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
//			}
//			else {
//				g.drawText("胜场 "+iPlayer.iWinCount/10000+"万", iSppX+306*ActivityUtil.ZOOM_X, 228*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
//			}
//			//连击
//			g.drawText("连击 "+iPlayer.iAchievement, iSppX+306*ActivityUtil.ZOOM_X, 258*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
//			//醉酒度
//			if(iDrunkSprite != null) {
//				for(int i=0; i<iPlayer.iCurrentDrunk; i++) {//
//					iDrunkSprite.paint(g, (int)(iSppX+(306+i*22)*ActivityUtil.ZOOM_X), (int)(308*ActivityUtil.ZOOM_Y));
//				}
//			}
//			String tInf;
//			switch (iPlayer.iCurrentDrunk) {
//			case 0:
//				tInf = "神采飞扬";
//				break;
//			
//			case 1:
//				tInf = "霞飞双颊";
//				break;
//				
//			case 2:
//				tInf = "满脸通红";
//				break;
//				
//			case 3:
//				tInf = "胡言乱语";
//				break;
//				
//			case 4:
//				tInf = "东倒西歪";
//				break;
//				
//			default:
//				tInf = "烂醉如泥";
//				break;
//			}
//			if(iPlayer.iCurrentDrunk > 0) {
//						g.drawText(tInf, iSppX+306*ActivityUtil.ZOOM_X, 302*ActivityUtil.ZOOM_Y, ActivityUtil.mRedBeatDownPaint);
//			}
//			else {
//				g.drawText(tInf, iSppX+306*ActivityUtil.ZOOM_X, 314*ActivityUtil.ZOOM_Y, ActivityUtil.mGreenBeatDownPaint);
//			}
//			//凤币
//			g.drawText(""+GameMain.iPlayer.iMoney, iSppX+68*ActivityUtil.ZOOM_X, 406*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
//			//金币
//			tInf = ""+iPlayer.iGb;
//			if(iPlayer.iGb > 99999999) {
//				tInf = "" + (iPlayer.iGb / 10000);
//				tInf += "万";
//			}
//			g.drawText(tInf, iSppX+218*ActivityUtil.ZOOM_X, 368*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
//			//
//			Rect tempRect = new Rect();
//			//职称
//			if(iExpRect.width() > 0) {
//				tempRect.set((int)(iExpRect.left+iSppX), (int)(iExpRect.top), (int)(iExpRect.right+iSppX), (int)(iExpRect.bottom));
//				iNinePatch.draw(g, tempRect);
//			}
//			//职称文字
//			g.drawText(iPlayer.iTitle, iSppX+188*ActivityUtil.ZOOM_X, 443*ActivityUtil.ZOOM_Y, ActivityUtil.mTitlePaint);
		}
//		if(iSceneOperate != null) {
//			iSceneOperate.iCameraX = 0;
//			iSceneOperate.paint(g, 0, 0);
//			//技能
//			String tInf = "技能：";
//			g.drawText(tInf, iSoX+468*ActivityUtil.ZOOM_X, 128*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
//			if(iPlayer.iZhaiSkill >= 2) {
//				iSkillZhaiSprite.paintAction(g, 1, (int)(iSoX+518*ActivityUtil.ZOOM_X), (int)(168*ActivityUtil.ZOOM_Y)); //斋技能的图标
//				iSkillLightnSprite.paintAction(g, 1, (int)(iSoX+618*ActivityUtil.ZOOM_X), (int)(168*ActivityUtil.ZOOM_Y)); //雷技能的图标
//			}
//			else if(iPlayer.iZhaiSkill == 1) {
//				iSkillZhaiSprite.paintAction(g, 1, (int)(iSoX+518*ActivityUtil.ZOOM_X), (int)(168*ActivityUtil.ZOOM_Y)); //斋技能的图标
//				iSkillLightnSprite.paintAction(g, 0, (int)(iSoX+618*ActivityUtil.ZOOM_X), (int)(168*ActivityUtil.ZOOM_Y)); //雷技能的图标
//			}
//			else {
//				iSkillZhaiSprite.paintAction(g, 0, (int)(iSoX+518*ActivityUtil.ZOOM_X), (int)(168*ActivityUtil.ZOOM_Y)); //斋技能的图标
//				iSkillLightnSprite.paintAction(g, 0, (int)(iSoX+618*ActivityUtil.ZOOM_X), (int)(168*ActivityUtil.ZOOM_Y)); //雷技能的图标
//			}
//			g.drawText("喊斋", iSoX+518*ActivityUtil.ZOOM_X, 218*ActivityUtil.ZOOM_Y, iLovePaint);
//			g.drawText("雷劈", iSoX+618*ActivityUtil.ZOOM_X, 218*ActivityUtil.ZOOM_Y, iLovePaint);
//			//车
			if(iPlayer.iCarID == 0) {
//				g.drawText("无车族", iSoX+468*ActivityUtil.ZOOM_X, 288*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
			}
			else {
				if (iCarBMP != null) {
					g.drawBitmap(iCarBMP, iSoX+iCarPicX, iCarPicY, null);
				}
//				tInf = "座驾：";
//				g.drawText(tInf, iSoX+468*ActivityUtil.ZOOM_X, 268*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
			}
//		}
		if(iSceneBar != null) {
			iSceneBar.iCameraY = 0;
			iSceneBar.paint(g, 0, 0);		
		}
		g = null;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(null != infoPopView && infoPopView.onKeyDown(keyCode, event)) return true;
		if(null != changeDicePopView && changeDicePopView.onKeyDown(keyCode, event)) return true;
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(iGameState == 4) {//在详细资料界面
				iGameState = 2;
			}
			else if(iGameState == 2) {
				//返回
				iGameState = 3;
			}
			//-----Test-------
//			iPlayer.iDingCount += 9000;
//			setHeart();
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
		if(infoPopView != null && infoPopView.pointerPressed(aX, aY)) return true;
		if(changeDicePopView != null && changeDicePopView.pointerPressed(aX, aY)) return true;
		boolean result= false;
		result = pointerPressedForPop(aX, aY);
		if (result) {
			return result;
		}
		Rect pRect = new Rect();
		SpriteButton pSB;
//		for(int i=0; i<iSpriteButtonList.size(); i++) {
//			pSB = ((SpriteButton)iSpriteButtonList.elementAt(i));
//			if(!pSB.isVision()) {
//				continue;
//			}
//			int pX = pSB.getX();
//			int pY = pSB.getY();
//			Rect pLogicRect = pSB.getRect();
//			pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
//					pX+pLogicRect.right, pY+pLogicRect.bottom);
//			if (pRect.contains(aX, aY)) {
//				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
//				pSB.pressed();
//				//System.out.println("ProgressScreen pointerPressed  pSB.pressed() ");
//				result = true;
//				break;
//			}
//		}
		if(result == false) {
			if(iGameState == 2) {
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					pSB = ((SpriteButton)iSpriteButtonListBar.elementAt(i));
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
			}
		}
		if(result == false) {
			if(iGameState == 2) {
				for(int i=iGiftSpriteButtons.size() - 1; i >= 0; i--) {
					pSB = ((SpriteButton)iGiftSpriteButtons.elementAt(i));
					if(!pSB.isVision() || pSB.getActionID() == SpriteButton.ESPRITE_BUTTON_ACTION_UNABLE) {
						continue;
					}
					int pX = pSB.getX();
					int pY = pSB.getY();
					Rect pLogicRect = pSB.getRect();
					pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
							pX+pLogicRect.right, pY+pLogicRect.bottom);
					if (pRect.contains(aX, aY)) {
						SpriteButtonGift pSB1 = iGiftSpriteButtons.remove(i);
						pSB1.data.index = i;
						iGiftSpriteButtons.add((SpriteButtonGift) pSB1);
						MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
						pSB1.pressed();
						result = true;
						break;
					}
				}
			}
		}
		if(result == false) {
			if(iGameState == 2) {
				for(int i=0; i<iSpriteButtonListPhoto.size(); i++) {
					pSB = ((SpriteButton)iSpriteButtonListPhoto.elementAt(i));
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
				if(iPhotoBoxSprite != null && iPicBMPZoom != null) {
					int pX = (int) iPicX;
					int pY = (int) iPicY;
					pRect.set(pX, pY,
							pX + iPicBMPZoom.getWidth(), pY+iPicBMPZoom.getHeight());
					if (pRect.contains(aX, aY)) {
						MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
						ItemAction(GameEventID.ESPRITEBUTTON_EVENT_INFO_PHOTO);
						result = true;
					}
				}
			}
		}
		if(result == false) {
			if(iGameState == 4) {
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
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	private boolean pointerPressedForPop(int aX, int aY) {
		boolean result= false;
		return result;
	}
	
	@Override
	public boolean pointerReleased(int aX, int aY) {
		boolean result= true;
		return result;
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean ItemAction(int aEventID) {
		if(infoPopView != null) infoPopView.ItemAction(aEventID);
		if(changeDicePopView != null) changeDicePopView.ItemAction(aEventID);
		switch (aEventID) {
		case GameEventID.ESPRITEBUTTON_EVENT_INFO_RETURN://返回
			iGameState = 3;	
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_INFO_DETAIL://详细资料
//			iGameState = 4;
			if(null != infoPopView) {
				infoPopView.setPlayerInfo(iPlayer);
				infoPopView.show();
			}
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_INFO_PHOTO://拍照
			//切换到照相
			iGameState = 7;
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_INFO_CHANGEDICE://改变骰子
//			messageEvent(MessageEventID.EMESSAGE_EVENT_SHOW_CHANGEDICE);
			if(changeDicePopView != null) {
				changeDicePopView.show();
			}
			return true;
			
		case GameEventID.ESPRITEBUTTON_EVENT_INFO_LEAVEMSG: //查看留言
			messageEvent(GameEventID.ESPRITEBUTTON_EVENT_INFO_LEAVEMSG);
			return true;
			
		case GameEventID.ESPRITEBUTTON_EVENT_INFO_PHRASE: //编辑常用语
			messageEvent(GameEventID.ESPRITEBUTTON_EVENT_INFO_PHRASE);
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_INFO_SETTINGPWD://设置密码
			messageEvent(GameEventID.ESPRITEBUTTON_EVENT_INFO_SETTINGPWD);
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_INFO_DETAIL_CLOSE://详细资料关闭
			iGameState = 2;
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_INFO_DETAIL_MODIFY://详细资料修改
			break;
		}
		return false;
	}
	
	//初始化 相片、头像
	private void initPic() {
		iPicBMP = MyArStation.iHttpDownloadManager.getImage(iPlayer.szBigPicName);
		int tX_Offset = ActivityUtil.INFO_PIC_BG_X_STANDARD;
		int tY_Offset = ActivityUtil.INFO_PIC_BG_Y_STANDARD;
		int tW_BG = ActivityUtil.INFO_PIC_BG_W_STANDARD;
		int tH_BG = ActivityUtil.INFO_PIC_BG_H_STANDARD;
		if (iPicBMP == null) {
			return;
		}
		else {
			float tZoom = 1;//图片 最终缩放率(X、Y方向)
			float tSrcW = iPicBMP.getWidth();
			float tSrcH = iPicBMP.getHeight();
			float tPicBGW = (ActivityUtil.INFO_PIC_BG_W_STANDARD*ActivityUtil.ZOOM_X);//图片底板限制宽
			float tPicBGH = (ActivityUtil.INFO_PIC_BG_H_STANDARD*ActivityUtil.ZOOM_Y);
			float tPicZoomX = tPicBGW/tSrcW;
			float tPicZoomY = tPicBGH/tSrcH;
			if (tSrcW>tPicBGW && tSrcH>tPicBGH) {
				//宽高都超出，取缩放率小的
				tZoom = tPicZoomX<tPicZoomY ? tPicZoomX : tPicZoomY;
//				System.out.println("HS 宽高都超出 tZoom="+tZoom+", tPicZoomX="+tPicZoomX+", tPicZoomY="+tPicZoomY);
			}
			else if (tSrcW>tPicBGW) {
				tZoom = tPicZoomX;
//				System.out.println("HS 宽超出 tZoom="+tZoom+", tPicZoomX="+tPicZoomX+", tPicZoomY="+tPicZoomY);
			}
			else if (tSrcH>tPicBGH) {
				tZoom = tPicZoomY;
//				System.out.println("HS 高超出 tZoom="+tZoom+", tPicZoomX="+tPicZoomX+", tPicZoomY="+tPicZoomY);
			}
			else {
				tZoom = tPicZoomY;
				if(tPicZoomX < tPicZoomY) {
					tZoom = tPicZoomX;
				}
			}
			int tZoomW = (int) (iPicBMP.getWidth()*tZoom);
			int tZoomH = (int) (iPicBMP.getHeight()*tZoom);
			iPicBMPZoom = Graphics.zoomBitmap(iPicBMP, tZoomW, tZoomH);
			if (iPicBMPZoom != iPicBMP) {
				//如果是不一样内存的，iPicBMPZoom是新创建的，先释放iPicBMP的内存
				iPicBMP.recycle();
			}
			iPicBMP = null;
//			System.out.println("HS initPic iPicBMPZoom w="+iPicBMPZoom.getWidth()+", h="+iPicBMPZoom.getHeight());
			float centerX = iPhotoBoxSprite.getX() + (tPicBGW - tZoomW) / 2;
			iPicX = centerX+tX_Offset*ActivityUtil.ZOOM_X;//+tW_BG/2*ActivityUtil.ZOOM_X-iPicBMPZoom.getWidth()/2;
			iPicY = iPhotoBoxSprite.getY()+tY_Offset*ActivityUtil.ZOOM_Y;//+tH_BG/2*ActivityUtil.ZOOM_Y-iPicBMPZoom.getHeight()/2;
		}
	}	
	
	//初始化 相片、头像
	private void initCarPic() {
		iCarBMP = null;
		if(iPlayer.iCarID <= 0) {
			return;
		}
		iCarBMP = MyArStation.iHttpDownloadManager.getGoodsIcon(iPlayer.iCarID);
		int tX_Offset = ActivityUtil.INFO_CAR_BG_X_STANDARD;
		int tY_Offset = ActivityUtil.INFO_CAR_BG_Y_STANDARD;
		int tW_BG = ActivityUtil.INFO_CAR_BG_W_STANDARD;
		int tH_BG = ActivityUtil.INFO_CAR_BG_H_STANDARD;
		if (iCarBMP == null) {
			return;
		}
		else {
			float tZoom = 1;//图片 最终缩放率(X、Y方向)
			float tSrcW = iCarBMP.getWidth();
			float tSrcH = iCarBMP.getHeight();
			float tPicBGW = ((ActivityUtil.INFO_CAR_BG_W_STANDARD-4)*ActivityUtil.ZOOM_X);//图片底板限制宽
			float tPicBGH = ((ActivityUtil.INFO_CAR_BG_H_STANDARD-4)*ActivityUtil.ZOOM_Y);
			float tPicZoomX = tPicBGW/tSrcW;
			float tPicZoomY = tPicBGH/tSrcH;
			if (tSrcW>tPicBGW && tSrcH>tPicBGH) {
				//宽高都超出，取缩放率小的
				tZoom = tPicZoomX<tPicZoomY ? tPicZoomX : tPicZoomY;
//				System.out.println("HS 宽高都超出 tZoom="+tZoom+", tPicZoomX="+tPicZoomX+", tPicZoomY="+tPicZoomY);
			}
			else if (tSrcW>tPicBGW) {
				tZoom = tPicZoomX;
//				System.out.println("HS 宽超出 tZoom="+tZoom+", tPicZoomX="+tPicZoomX+", tPicZoomY="+tPicZoomY);
			}
			else if (tSrcH>tPicBGH) {
				tZoom = tPicZoomY;
//				System.out.println("HS 高超出 tZoom="+tZoom+", tPicZoomX="+tPicZoomX+", tPicZoomY="+tPicZoomY);
			}
			else {
				tZoom = tPicZoomY;
				if(tPicZoomX < tPicZoomY) {
					tZoom = tPicZoomX;
				}
			}
			int tZoomW = (int) (iCarBMP.getWidth()*tZoom);
			int tZoomH = (int) (iCarBMP.getHeight()*tZoom);
			Bitmap tBmp = Graphics.zoomBitmap(iCarBMP, tZoomW, tZoomH);
			if (tBmp != iCarBMP) {
				//如果是不一样内存的，iPicBMPZoom是新创建的，先释放iPicBMP的内存
				iCarBMP.recycle();
			}
			iCarBMP = tBmp;
			tBmp = null;
//			System.out.println("HS initPic iPicBMPZoom w="+iPicBMPZoom.getWidth()+", h="+iPicBMPZoom.getHeight());
			iCarPicX = tX_Offset*ActivityUtil.ZOOM_X+tW_BG/2*ActivityUtil.ZOOM_X-iPicBMPZoom.getWidth()/2;
			iCarPicY = tY_Offset*ActivityUtil.ZOOM_Y+tH_BG/2*ActivityUtil.ZOOM_Y-iPicBMPZoom.getHeight()/2;
		}
	}	
	
	//初始化画笔
	private void initPaint() {
		iExpPaint = new Paint();
		iExpPaint.setColor(Color.MAGENTA);
		iLoadingPaint = new Paint();
		iLoadingPaint.setColor(Color.BLACK);
		iLovePaint = new Paint();
		iLovePaint.setTextSize(ActivityUtil.TEXTSIZE_NORMAL);     //设置字体大小
		iLovePaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		iLovePaint.setColor(Color.WHITE);
		iLovePaint.setTextAlign(Align.CENTER); 	//水平居中
	}
	
	//设置经验条
	private void setExpRect() {
		//float tW = 260*ActivityUtil.ZOOM_X*GameMain.iPlayer.iGameExp/GameMain.iPlayer.iNextExp;
		float tW = 578 * MyArStation.iPlayer.iGameExp / MyArStation.iPlayer.iNextExp * ActivityUtil.ZOOM_X;//*iPlayer.iGameExp/100;
		float tLeft = 119 * ActivityUtil.ZOOM_X;
		float tTop = 185 * ActivityUtil.ZOOM_Y;//418
		float tRight = tLeft + tW;
		float tBottom = tTop + 32 * ActivityUtil.ZOOM_Y;
		iExpRect.set(tLeft, tTop, tRight, tBottom);
	}
	
	private void loadUIPak() {
//		iUIPak =CPakManager.loadUIPak();//Project.loadProject(ActivityUtil.PATH_SCREEN+"gameshome_map.pak", false);
		iMyHomePak = CPakManager.loadMyHomePak();
		iInfoPak = CPakManager.loadInfoPak();
		if(iMyHomePak != null) {
			iSceneMyHome = iMyHomePak.getScene(0);
			if(iSceneMyHome != null) {
				iSceneMyHome.setViewWindow(0, 0, 800, 480);
				iSceneMyHome.initDrawList();
				//动画
				Vector<?> pSpriteList = iSceneMyHome.getLayoutMap(Scene.BuildLayout).getSpriteList();
				if(pSpriteList.size() >= 2) {
					iBalloonSprite = ((CBuilding)pSpriteList.elementAt(0));//气球动画
					iBalloonSunSprite = ((CBuilding)pSpriteList.elementAt(3));//气球动画
					iVipSprite = ((CBuilding)pSpriteList.elementAt(1));	//VIP动画
				}
			}
			iScenePlayerPhoto = iMyHomePak.getScene(1);
			if(iScenePlayerPhoto != null) {
				iScenePlayerPhoto.setViewWindow(0, 0, 800, 480);
				iScenePlayerPhoto.initDrawList();
				//
				Vector<?> pSpriteList = iScenePlayerPhoto.getLayoutMap(Scene.BuildLayout).getSpriteList();
				if(pSpriteList.size() >= 1) {
					iPhotoBoxSprite = ((CBuilding)pSpriteList.elementAt(0));//骰子动画
				}
			}
//			iSceneOperate = iUIPak.getScene(7);
//			if(iSceneOperate != null) {
//				iSceneOperate.setViewWindow(0, 0, 800, 480);
//				iSceneOperate.initDrawList();
//				iSceneOperate.initNinePatchList(new int[] {R.drawable.gdbj});
//				//动画
//				Vector<?> pSpriteList = iSceneOperate.getLayoutMap(Scene.BuildLayout).getSpriteList();
//				if(pSpriteList.size() >= 3) {
//					iDiceSprite = ((CBuilding)pSpriteList.elementAt(0));//骰子动画
//					iVipSprite = ((CBuilding)pSpriteList.elementAt(1));	//VIP动画
//					iCardSprite = ((CBuilding)pSpriteList.elementAt(2));	//酒水卡动画
//				}
//				//动画按钮
//				Map pMap = iSceneOperate.getLayoutMap(Scene.SpriteLayout);
//				pSpriteList = pMap.getSpriteList();
//				Sprite pSprite;
//				for (int i=0; i<pSpriteList.size(); i++) {
//					pSprite = (Sprite)pSpriteList.elementAt(i);
//					int pX = pSprite.getX();
//					int pY = pSprite.getY();
//					int pEventID = GameEventID.ESPRITEBUTTON_EVENT_HOME_OFFSET + pSprite.getIndex();
//					TPoint pPoint = new TPoint();
//					pPoint.iX = pX;
//					pPoint.iY = pY;
//					//精灵按钮
//					SpriteButton pSpriteButton;
//					pSpriteButton = new SpriteButton(pSprite);
//					pSpriteButton.setEventID(pEventID);
//					pSpriteButton.setPosition(pX, pY);
//					pSpriteButton.setHandler(this);
//					iSpriteButtonList.addElement(pSpriteButton);
//					
//				}
//			}
			//操作栏
			iSceneBar = iMyHomePak.getScene(2);
			if(iSceneBar != null) {
				iSceneBar.setViewWindow(0, 0, 800, 480);
				iSceneBar.initDrawList();
				//
				Map pMap = iSceneBar.getLayoutMap(Scene.SpriteLayout);
				Vector<?> pSpriteList = pMap.getSpriteList();
				Sprite pSprite;
				for (int i=0; i<pSpriteList.size(); i++) {
					pSprite = (Sprite)pSpriteList.elementAt(i);
					int pX = pSprite.getX();
					int pY = pSprite.getY();
					int pEventID = GameEventID.ESPRITEBUTTON_EVENT_INFO_OFFSET + pSprite.getIndex();
					TPoint pPoint = new TPoint();
					pPoint.iX = pX;
					pPoint.iY = pY;
					//精灵按钮
					SpriteButton pSpriteButton;
					pSpriteButton = new SpriteButton(pSprite);
					pSpriteButton.setEventID(pEventID);
					pSpriteButton.setPosition(pX, pY);
					pSpriteButton.setHandler(this);
					iSpriteButtonListBar.addElement(pSpriteButton);
					if(GameEventID.ESPRITEBUTTON_EVENT_INFO_LEAVEMSG == pEventID) {
						pSpriteButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
						Rect rect = pSpriteButton.getRect();
						iLeaveNotifyPos = new RectF(
								pX+rect.right - (rect.width() >> 2) - 10 * ActivityUtil.ZOOM_X, 
								pY - 4 * ActivityUtil.ZOOM_X - ActivityUtil.mLeaveMsgPaint.getTextSize() / 2, 
								pX+rect.right + 10 * ActivityUtil.ZOOM_X,
								pY + 4 * ActivityUtil.ZOOM_X + ActivityUtil.mLeaveMsgPaint.getTextSize() / 2);
						Tools.debug(iLeaveNotifyPos.toString());
					}
				}
			}
		}
		//
		if(iInfoPak != null) {
			//详细资料界面
			iSceneDetail = iInfoPak.getScene(0);
			if(iSceneDetail != null) {
				iSceneDetail.setViewWindow(0, 0, 800, 480);
				iSceneDetail.initDrawList();
				Vector<?> pSpriteList = iSceneDetail.getLayoutMap(Scene.BuildLayout).getSpriteList();
				if(pSpriteList.size() >= 1) {
					iSexSprite = ((CBuilding)pSpriteList.elementAt(0));//性别动画
				}
				//
				Map pMap = iSceneDetail.getLayoutMap(Scene.SpriteLayout);
				pSpriteList = pMap.getSpriteList();
				Sprite pSprite;
				for (int i=0; i<pSpriteList.size(); i++) {
					pSprite = (Sprite)pSpriteList.elementAt(i);
					int pX = pSprite.getX();
					int pY = pSprite.getY();
					int pEventID = GameEventID.ESPRITEBUTTON_EVENT_INFO_DETAIL_OFFSET + pSprite.getIndex();
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
		//
		Project tSkillIconPak = MyArStation.iPakManager.loadSkillIconPak();
		if(tSkillIconPak != null) {
			iSkillZhaiSprite = tSkillIconPak.getSprite(0);;	//斋技能的图标
			iSkillLightnSprite = tSkillIconPak.getSprite(1);; //雷劈技能的图标
		}
	}
	
	private void addGifts(GiveGiftData giftID, boolean drop) {
		Sprite temp = new Sprite();
		int i = giftID.index;
		int index = giftID.id + GiftUtil.GIFTID_1;
		if(index < GiftUtil.GIFTID_1 || index > GiftUtil.GIFTID_9) return;
		temp.copyFrom(getGiftSprite(index));
		SpriteButtonGift button = new SpriteButtonGift(temp);
		int x = (int) (GiftUtil.giftPaintLeft + (i % GiftUtil.giftMaxRow) * GiftUtil.giftSpaceX);
		int col = i / GiftUtil.giftMaxRow;
		if(col > GiftUtil.giftMaxCol) {
			col = 0;
		}
		int y = (int) (GiftUtil.giftPaintBottom - col  * GiftUtil.giftSpaceY);
		button.setPosition(x, drop ? 0 : y);
		button.iEndY = y;
		button.setDropStep(0, y);
		button.data = giftID;
		button.isDrop = drop;
		button.setEventID(i+ index);
//		button.setActionID(SpriteButtonGift.ESPRITE_BUTTON_ACTION_UNABLE);
		button.setOnGiftSelect(new OnGiftSelectListen() {
			
			@Override
			public void OnGiftSelect(int aEventID, GiveGiftData data) {
				for(int i = iGiftSpriteButtons.size() - 1; i >= 0; i--) {
					SpriteButtonGift tButtonGift = iGiftSpriteButtons.get(i);
					if(tButtonGift.getEventID() == aEventID) {
						SpriteButtonGift spriteButtonGift = iGiftSpriteButtons.get(i);
						spriteButtonGift.setVision(false);
						MyArStation.iGameProtocol.requestHomeTakeGift(spriteButtonGift.data.index);
						break;
					}
				}
			}
		});
		iGiftSpriteButtons.add(button);
	}
	
	/**
	 * 
	 */
	private Sprite getGiftSprite(int index) {
		Sprite stSprite = null;
		if(index < GiftUtil.GIFTID_1 || index > GiftUtil.GIFTID_9) {
			return stSprite;
		}
		Project giftpProject = CPakManager.loadMyHomePak();
		if(giftpProject != null) {
			stSprite = giftpProject.getSprite(index);
		}
		return stSprite;
	}
	/**
	 * @param animFormatData
	 */
	private Point getNextGiftPostion() {
		Point point = new Point();
		if(iGiftSpriteButtons != null) {
//			int x = (int) (GiftUtil.giftPaintLeft + (i % GiftUtil.giftMaxRow) * GiftUtil.giftSpaceX);
//			int col = i / GiftUtil.giftMaxRow;
//			if(col > GiftUtil.giftMaxCol) {
//				col = 0;
//			}
//			int y = (int) (GiftUtil.giftPaintBottom - col  * GiftUtil.giftSpaceY);
			int size = iGiftSpriteButtons.size();
			int row = (int) (size % GiftUtil.giftMaxRow);
			int col = (int) (size / GiftUtil.giftMaxRow);
			if(col > GiftUtil.giftMaxCol) {
				col = 0;
			}
			point.x = (int) (GiftUtil.giftPaintLeft + row* GiftUtil.giftSpaceX); 
			point.y = (int) (GiftUtil.giftPaintBottom - col* GiftUtil.giftSpaceY); 
		}
		return point;
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#OnProcessCmd(java.lang.Object, int, int)
	 */
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
		if(infoPopView != null) infoPopView.OnProcessCmd(socket, aMobileType, aMsgID);;
		if(changeDicePopView != null) changeDicePopView.OnProcessCmd(socket, aMobileType, aMsgID);;
		MobileMsg pMobileMsg = (MobileMsg) socket;
		switch (aMobileType) {
		case IM.IM_RESPONSE:
		{
			switch (aMsgID) {
			case RspMsg.MSG_RSP_PLAYINFO_THR:
				MBRspPlayerInfoThree rspMainPage = (MBRspPlayerInfoThree)pMobileMsg.m_pMsgPara;
				if(rspMainPage == null) {
					break;
				}
				Tools.debug(rspMainPage.toString());
				int type = rspMainPage.iResult;
				if(type == ResultCode.SUCCESS) {
					//切换到进场状态
					iGameState = 1;
					//
					MyArStation.iPlayer.iRankTotalCount = rspMainPage.iRankTotalCount; //排行榜总数
					MyArStation.iPlayer.iWealthRank = rspMainPage.iWealthRank; //财富排名
					MyArStation.iPlayer.iLevelRank = rspMainPage.iLevelRank; //等级排名
					MyArStation.iPlayer.iVictoryRank = rspMainPage.iVictoryRank; //胜场排名
					MyArStation.iPlayer.iVipLevel = rspMainPage.iVipLevel;			//Vip级别	
					MyArStation.iPlayer.iGameLevel = rspMainPage.iGameLevel;		//级别
					MyArStation.iPlayer.iGameExp = rspMainPage.iGameExp;			//当前级别的经验
					MyArStation.iPlayer.iNextExp = rspMainPage.iNextExp;			//升级经验
					MyArStation.iPlayer.iCurrentDrunk = rspMainPage.iCurrentDrunk;	//当前醉酒度
					MyArStation.iPlayer.iWinCount = rspMainPage.iKillsCount;		//胜
					MyArStation.iPlayer.iFailCount = rspMainPage.iBeKillsCount;	//负
					MyArStation.iPlayer.iModelID = rspMainPage.iModelID;			//角色模型ID（为-1时，表示没选择人物）
					MyArStation.iPlayer.iSex = rspMainPage.iSex;					//性别（为-1时，表示没选择人物）
					MyArStation.iPlayer.iWinning =	rspMainPage.iWinning;			//胜率
					MyArStation.iPlayer.iReward = rspMainPage.iReward;				//奖
					MyArStation.iPlayer.iTitle = rspMainPage.iTitle;				//人物称号
					MyArStation.iPlayer.iGb = rspMainPage.iGb;						//游戏币
					MyArStation.iPlayer.iMoney = rspMainPage.iMoney;				//凤智币
					MyArStation.iPlayer.iDingCount = rspMainPage.iSupportCount;	//支持数
					MyArStation.iPlayer.iAchievement = rspMainPage.iMaxDrunk;		//最大连击数
					MyArStation.iPlayer.iMaxHit = rspMainPage.iMaxDrunk;		//最大连击数
					MyArStation.iPlayer.iDiceID = rspMainPage.nDiceResID;			//骰子ID
					MyArStation.iPlayer.iCarID = rspMainPage.iCarID;				//汽车ID
					MyArStation.iPlayer.stUserNick = rspMainPage.iNick;				//汽车ID
					iPlayer = MyArStation.iPlayer;
					//设置状态
					setHeart();
					if(iVipSprite != null) {//VIP动画
						iVipSprite.setActionID(MyArStation.iPlayer.iVipLevel);
					}
					if(iPhotoBoxSprite != null) {//醉酒度
						iPhotoBoxSprite.setActionID(iPlayer.iCurrentDrunk);
					}
					if(iSexSprite != null) { //性别
						iSexSprite.setActionID(iPlayer.iSex);
//						System.out.println("InfoScreen iPlayeriSex = " + iPlayer.iSex);
					}
					if(iDiceSprite != null) {//骰子动画
						//更新自己的骰子类型
						iDiceSprite.setActionID(MyArStation.iPlayer.iDiceID / 10);
					}
					if(iCardSprite != null) {//酒水卡动画
						iCardSprite.setActionID(rspMainPage.nHasCard);
					}
					//初始化汽车图片
					initCarPic();
					//重设经验条
					setExpRect();
				}
				else {
					messageEvent(type, rspMainPage.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_PLAYINFO_FOUR:
				MBRspPlayerInfoFour rspPlayerInfoFour = (MBRspPlayerInfoFour)pMobileMsg.m_pMsgPara;
				if(rspPlayerInfoFour == null) {
					break;
				}
				Tools.debug(rspPlayerInfoFour.toString());
				type = rspPlayerInfoFour.iResult;
				if(type == ResultCode.SUCCESS) {
					//切换到进场状态
					iGameState = 1;
					//
					MyArStation.iPlayer.iRankTotalCount = rspPlayerInfoFour.iRankTotalCount; //排行榜总数
					MyArStation.iPlayer.iWealthRank = rspPlayerInfoFour.iWealthRank; //财富排名
					MyArStation.iPlayer.iLevelRank = rspPlayerInfoFour.iLevelRank; //等级排名
					MyArStation.iPlayer.iVictoryRank = rspPlayerInfoFour.iVictoryRank; //胜场排名
					MyArStation.iPlayer.iVipLevel = rspPlayerInfoFour.iVipLevel;			//Vip级别	
					MyArStation.iPlayer.iGameLevel = rspPlayerInfoFour.iGameLevel;		//级别
					MyArStation.iPlayer.iGameExp = rspPlayerInfoFour.iGameExp;			//当前级别的经验
					MyArStation.iPlayer.iNextExp = rspPlayerInfoFour.iNextExp;			//升级经验
					MyArStation.iPlayer.iCurrentDrunk = rspPlayerInfoFour.iCurrentDrunk;	//当前醉酒度
					MyArStation.iPlayer.iWinCount = rspPlayerInfoFour.iKillsCount;		//胜
					MyArStation.iPlayer.iFailCount = rspPlayerInfoFour.iBeKillsCount;	//负
					MyArStation.iPlayer.iModelID = rspPlayerInfoFour.iModelID;			//角色模型ID（为-1时，表示没选择人物）
					MyArStation.iPlayer.iSex = rspPlayerInfoFour.iSex;					//性别（为-1时，表示没选择人物）
					MyArStation.iPlayer.iWinning =	rspPlayerInfoFour.iWinning;			//胜率
					MyArStation.iPlayer.iReward = rspPlayerInfoFour.iReward;				//奖
					MyArStation.iPlayer.iTitle = rspPlayerInfoFour.iTitle;				//人物称号
					MyArStation.iPlayer.iGb = rspPlayerInfoFour.iGb;						//游戏币
					MyArStation.iPlayer.iMoney = rspPlayerInfoFour.iMoney;				//凤智币
					MyArStation.iPlayer.iDingCount = rspPlayerInfoFour.iSupportCount;	//支持数
					MyArStation.iPlayer.iAchievement = rspPlayerInfoFour.iMaxDrunk;		//最大连击数
					MyArStation.iPlayer.iMaxHit = rspPlayerInfoFour.iMaxDrunk;		//最大连击数
					MyArStation.iPlayer.iDiceID = rspPlayerInfoFour.nDiceResID;			//骰子ID
					MyArStation.iPlayer.iCarID = rspPlayerInfoFour.iCarID;				//汽车ID
					MyArStation.iPlayer.stUserNick = rspPlayerInfoFour.iNick;				//汽车ID
					MyArStation.iPlayer.iHonor = rspPlayerInfoFour.iHonor;				//汽车ID
					iPlayer = MyArStation.iPlayer;
					//设置状态
					setHeart();
					if(iVipSprite != null) {//VIP动画
						iVipSprite.setActionID(MyArStation.iPlayer.iVipLevel);
					}
					if(iPhotoBoxSprite != null) {//醉酒度
						iPhotoBoxSprite.setActionID(iPlayer.iCurrentDrunk);
					}
					if(iSexSprite != null) { //性别
						iSexSprite.setActionID(iPlayer.iSex);
//						System.out.println("InfoScreen iPlayeriSex = " + iPlayer.iSex);
					}
					if(iDiceSprite != null) {//骰子动画
						//更新自己的骰子类型
						iDiceSprite.setActionID(MyArStation.iPlayer.iDiceID / 10);
					}
					if(iCardSprite != null) {//酒水卡动画
						iCardSprite.setActionID(rspPlayerInfoFour.nHasCard);
					}
					//初始化汽车图片
					initCarPic();
					//重设经验条
					setExpRect();
				}
				else {
					messageEvent(type, rspPlayerInfoFour.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_HART:
				MBRspHart rspHart = (MBRspHart)pMobileMsg.m_pMsgPara;
				if(rspHart == null) {
					return;
				}
				Tools.debug(rspHart.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_Heart, rspHart);
				break;
			case RspMsg.MSG_RSP_CHANGE_DICE_TYPE:
				MBRspChangeDiceType rspChangeDiceType = (MBRspChangeDiceType)pMobileMsg.m_pMsgPara;
				if(rspChangeDiceType == null) {
					return;
				}
				Tools.debug(rspChangeDiceType.toString());
//				if(rspChangeDiceType.nResult == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_CHANGEDICE, rspChangeDiceType);
//				}
//				else {
//					messageEvent(rspChangeDiceType.nResult, rspChangeDiceType.iMsg);
//				}
				break;
			case RspMsg.MSG_RSP_AGREE_FRIEND:
			case RspMsg.MSG_RSP_LEAVE_MSG_B:
				MBRspLeaveMsgB rspLeaveMsg = (MBRspLeaveMsgB)pMobileMsg.m_pMsgPara;
				if(rspLeaveMsg == null) {
					break;
				}
				Tools.debug(rspLeaveMsg.toString());
				type = rspLeaveMsg.iResult;
				messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_LEAVEMSG, rspLeaveMsg);
				break;
			case RspMsg.MSG_RSP_LEAVE_MSG_LIST_B:
				removeProgressDialog();
				MBRspLeaveMsgListB rspLeaveMsgList = (MBRspLeaveMsgListB)pMobileMsg.m_pMsgPara;
				if(rspLeaveMsgList == null) {
					break;
				}
				Tools.debug(rspLeaveMsgList.toString());
				type = rspLeaveMsgList.iResult;
				if(type == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_LEAVELISTMSG, rspLeaveMsgList);
				}
				else {
					messageEvent(type, rspLeaveMsgList.iMsg);
				}
				break;	
			case RspMsg.MSG_RSP_DEL_LEAVE_MSG:
				MBRspDelLeaveMsg rspDelLeaveMsg = (MBRspDelLeaveMsg)pMobileMsg.m_pMsgPara;
				if(rspDelLeaveMsg == null) {
					break;
				}
				Tools.debug(rspDelLeaveMsg.toString());
				type = rspDelLeaveMsg.iResult;
				break;	
			case RspMsg.MSG_RSP_RESET_PWD:
				MBRspResetPwd rspResetPwd = (MBRspResetPwd)pMobileMsg.m_pMsgPara;
				if(rspResetPwd == null) {
					return;
				}
				Tools.debug(rspResetPwd.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RSPRESETPSWD, rspResetPwd);
//				}
				break;
			case RspMsg.MSG_RSP_HOME_RECEIVE_GIFT_LIST:
				MBRspHomeReceiveGiftList receiveGiftList = (MBRspHomeReceiveGiftList)pMobileMsg.m_pMsgPara;
				if(receiveGiftList == null) {
					break;
				}
				Tools.println(receiveGiftList.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_HOMERECEIVEGIFTLIST, receiveGiftList);
				break;	
			case RspMsg.MSG_RSP_HOME_TAKE_GIFT:
				MBRspHomeTakeGift homeTakeGift = (MBRspHomeTakeGift)pMobileMsg.m_pMsgPara;
				if(homeTakeGift == null) {
					break;
				}
				Tools.println(homeTakeGift.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_HOMETAKEGIFT, homeTakeGift);
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

	/**
	 * 
	 */
	private void setHeart() {
		if(iBalloonSprite != null) {//气球动画
			if(iPlayer.iDingCount < HEARTOFFSET) {
				iBalloonSprite.setActionID(0);
			}
			else if(iPlayer.iDingCount >= HEARTOFFSET && iPlayer.iDingCount < ONESUNINDEX * HEARTOFFSET) {
				iBalloonSprite.setActionID(iPlayer.iDingCount / HEARTOFFSET);
			}
			else if((iPlayer.iDingCount >= ONESUNINDEX * HEARTOFFSET) && (iPlayer.iDingCount < (ONESUNINDEX * HEARTOFFSET * 2))) {
				iBalloonSprite.setActionID((iPlayer.iDingCount - ONESUNINDEX * HEARTOFFSET) / HEARTOFFSET);
				if(iBalloonSunSprite != null) {//气球动画
					iBalloonSunSprite.setActionID(ONESUNINDEX);
				}
			}
			else if((iPlayer.iDingCount >= ONESUNINDEX * HEARTOFFSET * 2) && (iPlayer.iDingCount < (ONESUNINDEX * HEARTOFFSET * 3))) {
				iBalloonSprite.setActionID((iPlayer.iDingCount - ONESUNINDEX * HEARTOFFSET * 2) / HEARTOFFSET);
				if(iBalloonSunSprite != null) {//气球动画
					iBalloonSunSprite.setActionID(ONESUNINDEX+1);
				}
			}
			else if((iPlayer.iDingCount >= ONESUNINDEX * HEARTOFFSET * 3) && (iPlayer.iDingCount < (ONESUNINDEX * HEARTOFFSET * 4))) {
				iBalloonSprite.setActionID((iPlayer.iDingCount - ONESUNINDEX * HEARTOFFSET * 3) / HEARTOFFSET);
				if(iBalloonSunSprite != null) {//气球动画
					iBalloonSunSprite.setActionID(ONESUNINDEX+2);
				}
			}
			else if((iPlayer.iDingCount >= ONESUNINDEX * HEARTOFFSET * 4) && (iPlayer.iDingCount < (ONESUNINDEX * HEARTOFFSET * 5))) {
				iBalloonSprite.setActionID((iPlayer.iDingCount - ONESUNINDEX * HEARTOFFSET * 4) / HEARTOFFSET);
				if(iBalloonSunSprite != null) {//气球动画
					iBalloonSunSprite.setActionID(ONESUNINDEX+3);
				}
			}
			else if((iPlayer.iDingCount >= ONESUNINDEX * HEARTOFFSET * 5)) {
				iBalloonSprite.setActionID(ONESUNINDEX);
				if(iBalloonSunSprite != null) {//气球动画
					iBalloonSunSprite.setActionID(ONESUNINDEX+3);
				}
			}
		}
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case MessageEventID.EMESSAGE_EVENT_SOCKET_Heart:
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_ShowMainPageLoading:
			showProgressDialog(R.string.Loading_String);
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_SINGLE_PIC://单张图片下完
			{
				if(iGameState == 2 || iGameState == 1) {
					String tPicName = (String) msg.obj;//图片名
					if (tPicName.equals(MyArStation.iPlayer.szBigPicName)) {
						initPic();
						//
						createBMP = true;
					}
					else if(tPicName.equals(MyArStation.iPlayer.iCarID+".png")) {
						initCarPic();
						createBMP = true;
					}
					else {
						if(messageDialog != null) {
							messageDialog.upPic();
						}
					}
				}
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SHOW_CHANGEDICE: 
			{
//				changeDiceDialog.show();
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM:
			MyArStation.mGameMain.mainLayout.removeAllViews();
//			changeDiceDialog = new ChangeDiceDialog(GameMain.mGameMain);
			setAserPswddDialog = new SetAserPswddDialog(MyArStation.mGameMain);
			setAserPswddDialog.setTtile("更改密码");
			break;
		case MessageEventID.EMESSAGE_EVENT_RSP_CHANGEDICE:
			MBRspChangeDiceType rspChangeDiceType = (MBRspChangeDiceType)msg.obj;
			if(rspChangeDiceType.iMsg.length() > 0) {
				Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), Gravity.CENTER, rspChangeDiceType.iMsg, Toast.LENGTH_SHORT);
			}
			if(rspChangeDiceType.nResult == ResultCode.SUCCESS) {
				MyArStation.iPlayer.iDiceID = rspChangeDiceType.iDiceType * 10;
				Tools.println("diceID "+MyArStation.iPlayer.iDiceID+"");
				//设置状态
//				if(iDiceSprite != null) {//骰子动画
//					iDiceSprite.setActionID(GameMain.iPlayer.iDiceID);
//					//iGameState = 4;
//				}
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_INFO_LEAVEMSG:
			if(messageDialog == null) {
				messageDialog = new MessageDialog(MyArStation.mGameMain);
			}
			if(MyArStation.iPlayer.iLeaveMsgCount > 0) {
				messageDialog.reset();
			}
			if(messageDialog.messageDatas.isEmpty()) {
				Tools.debug("userid = " + MyArStation.getInstance().iPlayer.iUserID);
				MyArStation.iGameProtocol.requestLeaveListMsgB(MyArStation.getInstance().iPlayer.iUserID, messageDialog.iStartIndex);
			}
			MyArStation.iPlayer.iLeaveMsgCount = 0;
			messageDialog.show();
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_INFO_PHRASE:
			if(iChatView == null) {
				iChatView = new ChatView(MyArStation.mGameMain.getApplicationContext());
			}
			iChatView.showAtLocationNotBar(iGameCanvas, Gravity.CENTER, 0, 0);
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_LEAVEMSG:
			MBRspLeaveMsgB leaveMsg = (MBRspLeaveMsgB) msg.obj;
			if(leaveMsg.iMsg.length() > 0)
				Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), leaveMsg.iMsg);
			if(leaveMsg.iResult == 0) {
				MessageData data = new MessageData();
				data.reset(leaveMsg.iPic, 
						leaveMsg.iNick, 
						leaveMsg.iText,
						leaveMsg.iUID,
						leaveMsg.iLeaveMsgID, 
						Util.getTimeState(leaveMsg.iTime+""), 
						true,
						leaveMsg.iType);
				if(messageDialog != null) {
					messageDialog.iUserID = iUserID;
					messageDialog.setMessageData(data);
					messageDialog.show();
				}
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_LEAVELISTMSG:
			MBRspLeaveMsgListB leaveMsgList = (MBRspLeaveMsgListB)msg.obj;
			if(leaveMsgList.iMsg.length() > 0) {
				Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), leaveMsgList.iMsg);
			}
			int allCount = leaveMsgList.iAllMsgCount;
			int count = leaveMsgList.iMsgCount;
			int startIndex = leaveMsgList.iStartIndex;
			List<MessageData> datas = new ArrayList<MessageData>();
			for(int i = 0; i < count; i++) {
				MessageData data = new MessageData();
				data.reset(leaveMsgList.iPic.get(i), leaveMsgList.iNick.get(i),
						leaveMsgList.iText.get(i), leaveMsgList.iUID.get(i), 
						leaveMsgList.iLeaveMsgID.get(i), 
						Util.getTimeState(leaveMsgList.iTime.get(i)+""),
						true, leaveMsgList.iType.get(i));
				datas.add(data);
			}
			
			if(messageDialog != null) {
				messageDialog.iAllCount = allCount;
				messageDialog.iCount += count;
				messageDialog.iUserID = iUserID;
				messageDialog.iStartIndex = startIndex;
				messageDialog.setMessageDatas(datas);
				messageDialog.show();
			}
			datas = null;
			break;	
		case GameEventID.ESPRITEBUTTON_EVENT_INFO_SETTINGPWD:
			if(!MyArStation.iPlayer.ibVisitor) {
				setAserPswddDialog.show();
			}
			else {
				Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), "游客不能设置密码");
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RSPRESETPSWD://设置密码
			removeProgressDialog();
			MBRspResetPwd rspResetPwd = (MBRspResetPwd) msg.obj;
			if(rspResetPwd.iMsg.length() > 0) {
				Tools.showSimpleToast(MyArStation.mGameMain, rspResetPwd.iMsg);
			}
			if(rspResetPwd.nResult == 0) {
				MyArStation.mAccountStore.pushAndUpdate(MyArStation.mCurrAccountInfo);
				MyArStation.mAccountStore.commit();
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_RSP_HOMERECEIVEGIFTLIST:
		{
			MBRspHomeReceiveGiftList receiveGiftList = (MBRspHomeReceiveGiftList) msg.obj;
			showMessage(receiveGiftList.iMsg);
			if(receiveGiftList.nResult == 0) {
				for(int i = 0; i < receiveGiftList.iCount; i++) {
					GiveGiftData giveGiftData = new GiveGiftData(receiveGiftList.IIDs.get(i), 
							i, 
							receiveGiftList.iNick.get(i), 
							receiveGiftList.iTime.get(i),
							receiveGiftList.iCharm.get(i));
					addGifts(giveGiftData, false);
				}
			}
		}
		break;
		case MessageEventID.EMESSAGE_EVENT_SHOWMSG:
		{
			showMessage((String) msg.obj);
		}
		break;
		case MessageEventID.EMESSAGE_EVENT_HOMETAKEGIFT:
		{
			MBRspHomeTakeGift rspHomeTakeGift = (MBRspHomeTakeGift) msg.obj;
			showMessage(rspHomeTakeGift.iMsg);
			if(rspHomeTakeGift.iResult == 0) {
				int index = rspHomeTakeGift.iIndex;
				if(index >= iGiftSpriteButtons.size())return;
				GiveGiftData data2  = null;
				data2 = iGiftSpriteButtons.lastElement().data;
				if(data2 != null) {
					MyArStation.iPlayer.iDingCount += data2.cham;
					messageEvent(MessageEventID.EMESSAGE_EVENT_SHOWMSG, data2.toString());
					iGiftSpriteButtons.removeElementAt(iGiftSpriteButtons.size()-1);;
					break;
				}
					
			}
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void requestPlayerInfo() {
//		GameMain.iGameProtocol.requestPlayerInfoThree(GameMain.iPlayer.iUserID);
		MyArStation.iGameProtocol.requestPlayerInfoFour(MyArStation.iPlayer.iUserID);
	}
	
	private void drawNewMsgCount(Canvas g) {
		if(iLeaveNotifyPos != null && MyArStation.iPlayer.iLeaveMsgCount > 0) {
			ActivityUtil.mLeaveMsgPaint.setColor(Color.WHITE);
			int radius = (int) (22 * ActivityUtil.ZOOM_X);
			RectF tempRect1 = new RectF(iLeaveNotifyPos.left, iLeaveNotifyPos.top-iSceneBar.iCameraY, iLeaveNotifyPos.right, iLeaveNotifyPos.bottom-iSceneBar.iCameraY);
			g.drawOval(tempRect1/*, 4 * ActivityUtil.ZOOM_X, 4 * ActivityUtil.ZOOM_X*/, ActivityUtil.mLeaveMsgPaint);
//			g.drawCircle(iLeaveNotifyPos.x - iSceneOperate.iCameraX, iLeaveNotifyPos.y, radius, ActivityUtil.mLeaveMsgPaint);
			ActivityUtil.mLeaveMsgPaint.setColor(Color.RED);
//			iLeaveNotifyPos.inset(2*ActivityUtil.ZOOM_X, 2*ActivityUtil.ZOOM_X);
			RectF tempRect = new RectF(iLeaveNotifyPos.left, iLeaveNotifyPos.top-iSceneBar.iCameraY, iLeaveNotifyPos.right, iLeaveNotifyPos.bottom-iSceneBar.iCameraY);
			tempRect.inset(4*ActivityUtil.ZOOM_X, 4*ActivityUtil.ZOOM_X);
			g.drawOval(tempRect/*, 4 * ActivityUtil.ZOOM_X, 4 * ActivityUtil.ZOOM_X*/, ActivityUtil.mLeaveMsgPaint);
//			g.drawCircle(iLeaveNotifyPos.x - iSceneOperate.iCameraX, iLeaveNotifyPos.y, radius - 4 * ActivityUtil.ZOOM_X, ActivityUtil.mLeaveMsgPaint);
			ActivityUtil.mLeaveMsgPaint.setColor(Color.WHITE);
//			ActivityUtil.mLeaveMsgPaint.setTextAlign(Align.CENTER);
			String count = ""+MyArStation.iPlayer.iLeaveMsgCount;
			if(MyArStation.iPlayer.iLeaveMsgCount > 99) {
				count = "..";
			}
			Rect rect = new Rect();
			ActivityUtil.mLeaveMsgPaint.getTextBounds(count, 0, count.length(), rect);
			float x = (tempRect.centerX() + rect.centerX());
			float y = (tempRect.centerY() - rect.centerY());
			g.drawText(count, x, y, ActivityUtil.mLeaveMsgPaint);
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.InfoPopView.onClickReameListen#onClickRename()
	 */
	@Override
	public void onClickRename() {
		iGameState = 6;
	}
}
