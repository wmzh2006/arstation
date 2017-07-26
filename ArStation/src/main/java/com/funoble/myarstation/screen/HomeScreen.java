package com.funoble.myarstation.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.Concours;
import com.funoble.myarstation.data.cach.RoomData;
import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.GameButtonHandler;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.ResFile;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.screen.BasePopScreen.OnAnimateListen;
import com.funoble.myarstation.screen.QuickSelectRoomScreen.OnSelectRoomListen;
import com.funoble.myarstation.socket.protocol.MBNotifyActivityInfo;
import com.funoble.myarstation.socket.protocol.MBNotifyActivityStart;
import com.funoble.myarstation.socket.protocol.MBNotifyGetGift;
import com.funoble.myarstation.socket.protocol.MBNotifyNoPwdQuestion;
import com.funoble.myarstation.socket.protocol.MBNotifyPlayAnimation;
import com.funoble.myarstation.socket.protocol.MBNotifyPlayAnimationTwo;
import com.funoble.myarstation.socket.protocol.MBNotifyRecharge;
import com.funoble.myarstation.socket.protocol.MBRspActivityEnroll;
import com.funoble.myarstation.socket.protocol.MBRspAward;
import com.funoble.myarstation.socket.protocol.MBRspFeekBack;
import com.funoble.myarstation.socket.protocol.MBRspGiftList;
import com.funoble.myarstation.socket.protocol.MBRspHart;
import com.funoble.myarstation.socket.protocol.MBRspInviteFriend;
import com.funoble.myarstation.socket.protocol.MBRspLogin;
import com.funoble.myarstation.socket.protocol.MBRspMainPageB;
import com.funoble.myarstation.socket.protocol.MBRspResetPwd;
import com.funoble.myarstation.socket.protocol.MBRspSetPwdQuestion;
import com.funoble.myarstation.socket.protocol.MBRspVisitorLogin;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.ActivityAction;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.NotifyMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.Graphics;
import com.funoble.myarstation.utils.ImageUtil;
import com.funoble.myarstation.utils.TPoint;
import com.funoble.myarstation.utils.WebUrls;
import com.funoble.myarstation.view.ConcoursDialog;
import com.funoble.myarstation.view.FeedBackDialog;
import com.funoble.myarstation.view.GameEventID;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.GiftDialog;
import com.funoble.myarstation.view.MessageEventID;
import com.funoble.myarstation.view.NoticeDialog;
import com.funoble.myarstation.view.SetAserPswddDialog;
import com.funoble.myarstation.view.ConcoursDialog.OnConcoursListener;

public class HomeScreen extends GameView implements OnClickListener, GameButtonHandler,
													OnConcoursListener,
													OnSelectRoomListen,
													OnAnimateListen {
	
	private GameCanvas iGameCanvas = null;
	//
	private int	iGameState = 0;	//0 --- 载入数据     
								//1 --- 主界面面板进场  
								//2 --- 主界面    
								//3 --- 从主界面退场到个人资料        
								//4 --- 主界面操作面板退场到更多   
								//5 --- 主界面操着面板进场
								//6 --- 从更多界面退场到个人资料
								//7 --- 更多面板进场
								//8 --- 更多界面
								//9 --- 更多面板退场
								//10 --- 退场到快速游戏
								//11 --- 退场到选择房间
								//12 --- 退场到排行榜
								//13 --- 退场到参加活动
								//14 --- 退场到商城
								//15 --- 退场到好友
								//16 --- 退场到照相
								//17 --- 更多退场到照相
								//18 --- 更多退场到商场
								//19 --- 更多退场到好友
								//20 --- 更多退场到设置
								//21 --- 更多退场到帮助
								//22 --- 更多退场到教程
								//23---- 从个人资料到选择房间
								//24----从选择房间到个人资料
								//25---- 从选择房间进入游戏界面退场
								//26----进入个人资料
								//27---切换账号
								//28----兑换界面
	private float iSppX = 0;
	private float iSoX = 0;
	private float iSbX = 0;
	private float iSbY = 0;
	private float iBarMaxOffset = 0;
	private float iSbTwoY = 0;
	private int iSppSpeed = 0;
	private int iSoSpeed = 0;
	private int iSbSpeed = 0;
	private int iSbTwoSpeed = 0;
	//资源
	private Project iUIPak = null;
	private Project iActivityInfPak = null; //活动信息 
	private Scene iSceneLoading = null;
	private Scene iScenePlayerPhoto = null;
	private Scene iSceneOperate = null;
	private Scene iSceneBar = null;
	private Scene iSceneBarTwo = null;
//	private Sprite iDrunkSprite = null;	//酒杯动画
	private Sprite iActivityInfSprite = null; //活动信息动画
	private Vector<SpriteButton> iSpriteButtonList = new Vector<SpriteButton>();//精灵按钮 列表
	private Vector<SpriteButton> iSpriteButtonListBar = new Vector<SpriteButton>();//精灵按钮 列表
	private Vector<SpriteButton> iSpriteButtonListBarTwo = new Vector<SpriteButton>();//精灵按钮 列表
	private Vector<SpriteButton> iSpriteButtonListPhoto = new Vector<SpriteButton>();//精灵按钮 列表
	private SpriteButton iGiftButton = null; //领奖按钮
	private SpriteButton iMisSpriteButton = null; //任务
	private NinePatch iNinePatch = null; //经验条
//	private NinePatch iExpBmp = null;
	private Bitmap iBackBmp = null;	//背景图
	private Paint iExpPaint;
	private RectF iExpRect;
	private Paint iLovePaint;//顶 画笔
	
	private boolean createBMP = false;
	private Bitmap iPicBMP = null;//照片图片 原图
	private Bitmap iPicBMPZoom = null;//照片图片 缩放图
	private float iPicX = -1000*ActivityUtil.ZOOM_X;
	private float iPicY = -1000*ActivityUtil.ZOOM_Y;
	
	private Paint iOnlinePaint;//在线人数 描边画笔
	private Paint iLoadingPaint;//
	private Context Context = MyArStation.mGameMain;
	
	private List<String> iNocticeList;
//	private NoticeDialog       iNoticeDialog;
	private Dialog 		 dialog;
	private AlertDialog 		 dialogEnroll;
	private MBRspMainPageB iPlayerInfo = null;
	private FeedBackDialog	feedBackDialog;	//反馈界面
	private ConcoursDialog	concuorDialog;	//活动参加提示
	private GiftDialog		giftDialog;//奖品信息框
//	private ChargeDialog    chargeDialog; //充值
	private SetAserPswddDialog    setAserPswddDialog; //设置密码问题
	private BulletinView    iBulletinView; //登录领奖和消息
	private String[] urls = new String[4];//基础网址|礼品页面|公告页面|每日奖励页面
	private QuickSelectRoomScreen selectRoomScreen;
	private MissionView missionView;
	private boolean ibMoreScene = false;
	private RoomData iRoomData = null;
	//动态动画
	private SynAnimation iSynAnimation = null;
	//
	private int	iLoginDelay = 0; //登录状态的延迟	
	
	@Override
	public void init() {
		iGameCanvas = GameCanvas.getInstance();
		//载入背景图
		MyArStation.iImageManager.loadLoginBack();
		//GameMain.iImageManager.loadMainBack();
		
		//
//		GameMain.iImageManager.iBackLoginBmp = null;
		//
		iExpRect = new RectF();
		setExpRect();
		initPaint();
		loadUIPak();
		//快速选择房间
		selectRoomScreen = new QuickSelectRoomScreen();
		selectRoomScreen.init();
		selectRoomScreen.setOnSelectRoom(this);
		selectRoomScreen.setOnAnimateListen(this);
		selectRoomScreen.setDim(false);
		selectRoomScreen.setPop(false);
		//载入经验图片
		Bitmap bitmap = BitmapFactory.decodeResource(MyArStation.mGameMain.getResources(), R.drawable.exp3);
		iNinePatch = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
//		bitmap = BitmapFactory.decodeResource(GameMain.mGameMain.getResources(), R.drawable.exp2);
//		iExpBmp = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
//		bitmap = null;
		//载入标题图片
//		ResFile tRes = (ResFile)iUIPak.getObject(0, Project.FILE);
//		Bitmap tmpBmp = tRes.getImage();
//		int tPicBGW = (int)(290*ActivityUtil.ZOOM_X);//图片底板限制宽
//		int tPicBGH = (int)(120*ActivityUtil.ZOOM_Y);
//		iTitleBMP = Graphics.zoomBitmap(tmpBmp, tPicBGW, tPicBGH);
		//
		iBackBmp = Bitmap.createBitmap(ActivityUtil.SCREEN_WIDTH, ActivityUtil.SCREEN_HEIGHT, Config.ARGB_8888);
		//初始化图片
		initPic();
		//
		messageEvent(MessageEventID.EMESSAGE_EVENT_HOME_INIT_MSG);
//		MediaManager.getMediaManagerInstance().stopMusic();
		
		//800 * 400 的坐标
		iSppX = -500;
//		iSppY = 0;
		iSoX = 500;
//		iSoY = 0;
		iSbX = -500;
//		iSbY = 0;
		//转成对应屏幕分辨率的值
		iSppX *= (float)ActivityUtil.SCREEN_WIDTH / (float)ActivityUtil.SCREEN_WIDTH_STANDARD; 
		iSoX *= (float)ActivityUtil.SCREEN_WIDTH / (float)ActivityUtil.SCREEN_WIDTH_STANDARD;
		iSbX *= (float)ActivityUtil.SCREEN_HEIGHT / (float)ActivityUtil.SCREEN_HEIGHT_STANDARD;
		iSppSpeed = (int)(iSppX / 6);
		iSoSpeed = (int)(iSoX / 6);
//		iSbSpeed = (int)(iSbX / 6);
		
		iNocticeList = new ArrayList<String>();
		//如果有奖励信息，则显示领奖按钮
		if(MyArStation.iPlayer.ibGetGift) {
			iGiftButton.setVision(true);
		}
		if(MyArStation.iPlayer.iMissionRewardCount > 0) {
			if(iMisSpriteButton != null)iMisSpriteButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
		}
//		iBulletinView = new BulletinView(GameMain.mGameMain);
//		missionView = new MissionView(GameMain.mGameMain);
		//
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusic(SoundsResID.bg1, true);
		//动态动画
		iSynAnimation = new SynAnimation();
		iSynAnimation.init();
		if (!MyArStation.iPlayer.ibInit) {
			requestMainPage();
			iGameState = 0;
		}
		else {
			iGameState = 1;
		}
	}

	@Override
	public void releaseResource() {
		iSynAnimation = null;
		iGiftButton = null;
		iMisSpriteButton = null;
		iBackBmp = null;
//		iTitleBMP = null;
//		iExpBmp = null;
		iActivityInfSprite = null;
		iNinePatch = null;
		iActivityInfPak = null;
		iUIPak = null;
		if (iPicBMP != null) {
			iPicBMP.recycle();
			iPicBMP = null;
		}
		if (iPicBMPZoom != null) {
			iPicBMPZoom.recycle();
			iPicBMPZoom = null;
		}
		if(dialogEnroll != null) {
			dialogEnroll.dismiss();
		}
		if(feedBackDialog != null) {
			feedBackDialog.dismiss();
			feedBackDialog = null;
		}
		if(concuorDialog != null) {
			concuorDialog.dismiss();
			concuorDialog = null;
		}
		if(giftDialog != null) {
			giftDialog.dismiss();
			giftDialog = null;
		}
//		if(iNoticeDialog != null) {
//			iNoticeDialog.dismiss();
//			iNoticeDialog = null;
//		}
//		if(chargeDialog != null) {
//			chargeDialog.dismiss();
//			chargeDialog = null;
//		}
		if(setAserPswddDialog != null) {
			setAserPswddDialog.dismiss();
			setAserPswddDialog = null;
		}
		if(iBulletinView != null) {
			iBulletinView.dismiss();
			iBulletinView = null;
		}
		if(missionView != null) {
			missionView.dismiss();
			missionView = null;
		}
		if(dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		//GameMain.iImageManager.releaseMainBack();
	}

	@Override
	public void paintScreen(Canvas g, Paint paint) {
		//载入数据
		if(iGameState == 0) {
			g.drawBitmap(MyArStation.iImageManager.loadLoginBack(),
					0,
					0,
					null);
			if(iSceneLoading != null) {
				iSceneLoading.paint(g, 0, 0);
//				g.drawText("正在读取数据", 278*ActivityUtil.ZOOM_X, 288*ActivityUtil.ZOOM_Y, ActivityUtil.mLoading);
			}
		}
		else if(iGameState == 23) {


//			if(iBackBmp != null) {
//				g.drawBitmap(iBackBmp,
//						0,
//						0,
//						null);
//			}
			g.drawBitmap(MyArStation.iImageManager.loadLoginBack(),
					0,
					0,
					null);
			if(iSceneBar != null) {
				iSceneBar.paint(g, 0, 0);
//				//
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).paint(g, iSceneBar.iCameraX, iSceneBar.iCameraY);
				}
			}
			if(iSceneBarTwo != null) {
				iSceneBarTwo.paint(g, 0, 0);
//				//
				for(int i=0; i<iSpriteButtonListBarTwo.size(); i++) {
					((SpriteButton)iSpriteButtonListBarTwo.elementAt(i)).paint(g, iSceneBarTwo.iCameraX, iSceneBarTwo.iCameraY);
				}
			}
			if(iScenePlayerPhoto != null) {
				iScenePlayerPhoto.paint(g, 0, 0);
				//
				for(int i=0; i<iSpriteButtonListPhoto.size(); i++) {
					((SpriteButton)iSpriteButtonListPhoto.elementAt(i)).paint(g, iScenePlayerPhoto.iCameraX, iScenePlayerPhoto.iCameraY);
				}
			}
			paintActivityInfo(g);
			selectRoomScreen.paintScreen(g, paint);
			//动态动画
			if(iSynAnimation != null) iSynAnimation.paintScreen(g, null);
		}
		//主界面退场到选择房间
		else if(iGameState == 10) {
			g.drawBitmap(MyArStation.iImageManager.loadLoginBack(),
					0,
					0,
					null);
			if(iSceneOperate != null) {
				iSceneOperate.paint(g, 0, 0);
				if(!ibMoreScene) {
					paintPlayerInfo(g);
				}
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).paint(g, iSceneOperate.iCameraX, iSceneOperate.iCameraY);
				}
			}
			if(iSceneBar != null) {
				iSceneBar.paint(g, 0, 0);
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).paint(g, iSceneBar.iCameraX, iSceneBar.iCameraY);
				}
			}
			if(iSceneBarTwo != null) {
				iSceneBarTwo.paint(g, 0, 0);
				for(int i=0; i<iSpriteButtonListBarTwo.size(); i++) {
					((SpriteButton)iSpriteButtonListBarTwo.elementAt(i)).paint(g, iSceneBarTwo.iCameraX, iSceneBarTwo.iCameraY);
				}
			}
			if(iScenePlayerPhoto != null) {
				iScenePlayerPhoto.paint(g, 0, 0);
				for(int i=0; i<iSpriteButtonListPhoto.size(); i++) {
					((SpriteButton)iSpriteButtonListPhoto.elementAt(i)).paint(g, iScenePlayerPhoto.iCameraX, iScenePlayerPhoto.iCameraY);
				}
			}
			paintActivityInfo(g);
			//动态动画
			if(iSynAnimation != null) iSynAnimation.paintScreen(g, null);
		}
		//主界面退场到其它界面
		else if((iGameState >= 11 && iGameState <= 16) || iGameState == 28) {
			g.drawBitmap(MyArStation.iImageManager.loadLoginBack(),
					0,
					0,
					null);
			if(iSceneOperate != null) {
				iSceneOperate.paint(g, 0, 0);
				if(!ibMoreScene) {
					paintPlayerInfo(g);
				}
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).paint(g, iSceneOperate.iCameraX, iSceneOperate.iCameraY);
				}
			}
			if(iSceneBar != null) {
				iSceneBar.paint(g, 0, 0);
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).paint(g, iSceneBar.iCameraX, iSceneBar.iCameraY);
				}
			}
			if(iSceneBarTwo != null) {
				iSceneBarTwo.paint(g, 0, 0);
				for(int i=0; i<iSpriteButtonListBarTwo.size(); i++) {
					((SpriteButton)iSpriteButtonListBarTwo.elementAt(i)).paint(g, iSceneBarTwo.iCameraX, iSceneBarTwo.iCameraY);
				}
			}
			if(iScenePlayerPhoto != null) {
				iScenePlayerPhoto.paint(g, 0, 0);
				for(int i=0; i<iSpriteButtonListPhoto.size(); i++) {
					((SpriteButton)iSpriteButtonListPhoto.elementAt(i)).paint(g, iScenePlayerPhoto.iCameraX, iScenePlayerPhoto.iCameraY);
				}
			}
			paintActivityInfo(g);
			selectRoomScreen.paintScreen(g, paint);
		}
		//更多界面
		else if(iGameState >= 6) {
			g.drawBitmap(MyArStation.iImageManager.loadLoginBack(),
					0,
					0,
					null);
			if(iSceneOperate != null) {
				iSceneOperate.paint(g, 0, 0);
				//
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).paint(g, iSceneOperate.iCameraX, iSceneOperate.iCameraY);
				}
			}
			if(iSceneBar != null) {
				iSceneBar.paint(g, 0, 0);
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).paint(g, iSceneBar.iCameraX, iSceneBar.iCameraY);
				}
			}
			if(iSceneBarTwo != null) {
				iSceneBarTwo.paint(g, 0, 0);
				for(int i=0; i<iSpriteButtonListBarTwo.size(); i++) {
					((SpriteButton)iSpriteButtonListBarTwo.elementAt(i)).paint(g, iSceneBar.iCameraX, iSceneBar.iCameraY);
				}
			}
			if(iScenePlayerPhoto != null) {
				iScenePlayerPhoto.paint(g, 0, 0);
				//
				for(int i=0; i<iSpriteButtonListPhoto.size(); i++) {
					((SpriteButton)iSpriteButtonListPhoto.elementAt(i)).paint(g, iScenePlayerPhoto.iCameraX, iScenePlayerPhoto.iCameraY);
				}
			}
			//活动信息动画
			paintActivityInfo(g);
			//动态动画
			if(iSynAnimation != null) iSynAnimation.paintScreen(g, null);
		}
		//主界面
		else if(iGameState == 2) {
//			if(iBackBmp != null) {
//				g.drawBitmap(iBackBmp,
//						0,
//						0,
//						null);
//			}
			g.drawBitmap(MyArStation.iImageManager.loadLoginBack(),
					0,
					0,
					null);
			if(iSceneOperate != null) {
				iSceneOperate.paint(g, 0, 0);
				if(!ibMoreScene) {
					paintPlayerInfo(g);
				}
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).paint(g, iSceneOperate.iCameraX, iSceneOperate.iCameraY);
				}
			}
			if(iSceneBar != null) {
				iSceneBar.paint(g, 0, 0);
//				//
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).paint(g, iSceneBar.iCameraX, iSceneBar.iCameraY);
				}
			}
			if(iSceneBarTwo != null) {
				iSceneBarTwo.paint(g, 0, 0);
//				//
				for(int i=0; i<iSpriteButtonListBarTwo.size(); i++) {
					((SpriteButton)iSpriteButtonListBarTwo.elementAt(i)).paint(g, iSceneBar.iCameraX, iSceneBar.iCameraY);
				}
			}
			if(iScenePlayerPhoto != null) {
				iScenePlayerPhoto.paint(g, 0, 0);
				//
				for(int i=0; i<iSpriteButtonListPhoto.size(); i++) {
					((SpriteButton)iSpriteButtonListPhoto.elementAt(i)).paint(g, iScenePlayerPhoto.iCameraX, iScenePlayerPhoto.iCameraY);
				}
			}
			paintActivityInfo(g);
			if(selectRoomScreen != null)selectRoomScreen.paintScreen(g, paint);
			//动态动画
			if(iSynAnimation != null) iSynAnimation.paintScreen(g, null);
		}
		//主界面面板进场  
		else if(iGameState >= 1) {
			g.drawBitmap(MyArStation.iImageManager.loadLoginBack(),
					0,
					0,
					null);
			if(iSceneOperate != null) {
				iSceneOperate.paint(g, 0, 0);
				if(!ibMoreScene) {
					paintPlayerInfo(g);
				}
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).paint(g, iSceneOperate.iCameraX, iSceneOperate.iCameraY);
				}
			}
			if(iSceneBar != null) {
				iSceneBar.paint(g, 0, 0);
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).paint(g, iSceneBar.iCameraX, iSceneBar.iCameraY);
				}
			}
			if(iSceneBarTwo != null) {
				iSceneBarTwo.paint(g, 0, 0);
				for(int i=0; i<iSpriteButtonListBarTwo.size(); i++) {
					((SpriteButton)iSpriteButtonListBarTwo.elementAt(i)).paint(g, iSceneBar.iCameraX, iSceneBar.iCameraY);
				}
			}
			if(iScenePlayerPhoto != null) {
				iScenePlayerPhoto.paint(g, 0, 0);
				//
				for(int i=0; i<iSpriteButtonListPhoto.size(); i++) {
					((SpriteButton)iSpriteButtonListPhoto.elementAt(i)).paint(g, iScenePlayerPhoto.iCameraX, iScenePlayerPhoto.iCameraY);
				}
			}
			paintActivityInfo(g);
		}
		if(iBulletinView != null)iBulletinView.paintScreen(g, paint);
		if(missionView != null)missionView.paintScreen(g, paint);
		//动态动画
		if(iSynAnimation != null) iSynAnimation.paintScreen(g, null);
	}

	/**
	 * @param g
	 */
	private void paintActivityInfo(Canvas g) {
		if(iActivityInfSprite != null) {
			if(MyArStation.iPlayer.iHasActivity == 1) { 
				iActivityInfSprite.paintAction(
					g,
					1,
					(int)(ActivityUtil.ACTIVITY_INF_BG_X_STANDARD*ActivityUtil.ZOOM_X), 
					(int)(-iSbY+ActivityUtil.ACTIVITY_INF_BG_Y_STANDARD*ActivityUtil.ZOOM_Y));
			}
			else {
				if(MyArStation.iPlayer.iActivityCount > 0) {
					iActivityInfSprite.paintAction(
						g,
						0,
						(int)(ActivityUtil.ACTIVITY_INF_BG_X_STANDARD*ActivityUtil.ZOOM_X), 
						(int)(-iSbY+ActivityUtil.ACTIVITY_INF_BG_Y_STANDARD*ActivityUtil.ZOOM_Y));
					//可报名活动的个数
					g.drawText(""+MyArStation.iPlayer.iActivityCount, 
							ActivityUtil.ACTIVITY_INF_BG_X_STANDARD*ActivityUtil.ZOOM_X, 
							-iSbY+ActivityUtil.ACTIVITY_INF_TEXT_Y_STANDARD*ActivityUtil.ZOOM_Y, 
							ActivityUtil.mPlayerName);
				}
			}
		}
	}

	/**
	 * @param g
	 */
	private void paintPlayerInfo(Canvas g) {
		int tOffsetX = 0;
		int tOffsetY = 0;
		//
		if (iPicBMP != null) {
			g.drawBitmap(iPicBMP, iSoX+iPicX+tOffsetX, iPicY+tOffsetY, null);
		}
		if (iPicBMPZoom != null) {
			g.drawBitmap(iPicBMPZoom, iSoX+iPicX+tOffsetX, iPicY+tOffsetY, null);
		}
		//昵称
		g.drawText(""+MyArStation.iPlayer.stUserNick, 
				iSoX+ActivityUtil.NICK_BG_X_STANDARD*ActivityUtil.ZOOM_X+tOffsetX, 
				ActivityUtil.NICK_BG_Y_STANDARD*ActivityUtil.ZOOM_Y+tOffsetY, 
				ActivityUtil.mNamePaint);	
		//VIP
		if(MyArStation.iPlayer.iVipLevel <= 0) {
			g.drawText("未开VIP",//+GameMain.iPlayer.iVipLevel, 
					iSoX+ActivityUtil.VIP_BG_X_STANDARD*ActivityUtil.ZOOM_X+tOffsetX, 
					ActivityUtil.VIP_BG_Y_STANDARD*ActivityUtil.ZOOM_Y+tOffsetY, 
					ActivityUtil.mNoVipPaint);
		}
		else {
			g.drawText("VIP "+MyArStation.iPlayer.iVipLevel, 
					iSoX+ActivityUtil.VIP_BG_X_STANDARD*ActivityUtil.ZOOM_X+tOffsetX, 
					ActivityUtil.VIP_BG_Y_STANDARD*ActivityUtil.ZOOM_Y+tOffsetY, 
					ActivityUtil.mVipPaint);
		}
		//支持数
		String tInf = ""+MyArStation.iPlayer.iDingCount;
		if(MyArStation.iPlayer.iDingCount > 9999999) {
			tInf = "" + (MyArStation.iPlayer.iDingCount / 10000);
			tInf += "万";
		}
		g.drawText(tInf, 
				iSoX+ActivityUtil.HART_BG_X_STANDARD*ActivityUtil.ZOOM_X+tOffsetX, 
				ActivityUtil.HART_BG_Y_STANDARD*ActivityUtil.ZOOM_Y+tOffsetY, 
				ActivityUtil.mHartPaint);
		//凤币
		tInf = ""+MyArStation.iPlayer.iMoney;
		if(MyArStation.iPlayer.iMoney > 999999) {
			tInf = "" + (MyArStation.iPlayer.iMoney / 10000);
			tInf += "万";
		}
		g.drawText(tInf, 
				iSoX+ActivityUtil.FB_BG_X_STANDARD*ActivityUtil.ZOOM_X+tOffsetX, 
				ActivityUtil.FB_BG_Y_STANDARD*ActivityUtil.ZOOM_Y+tOffsetY, 
				ActivityUtil.mHartPaint);
		//金币
		tInf = ""+MyArStation.iPlayer.iGb;
		if(MyArStation.iPlayer.iGb > 99999999) {
			tInf = "" + (MyArStation.iPlayer.iGb / 10000);
			tInf += "万";
		}
		g.drawText(tInf, 
				iSoX+ActivityUtil.GB_BG_X_STANDARD*ActivityUtil.ZOOM_X+tOffsetX, 
				ActivityUtil.GB_BG_Y_STANDARD*ActivityUtil.ZOOM_Y+tOffsetY, 
				ActivityUtil.mHartPaint);
		//职称
		Rect tempRect = new Rect();
		if(iExpRect.width() > 0) {
			tempRect.set((int)(iExpRect.left+iSoX+tOffsetX), (int)(iExpRect.top+tOffsetY), (int)(iExpRect.right+iSoX), (int)(iExpRect.bottom));
			if(iNinePatch != null) {
				iNinePatch.draw(g, tempRect);
			}
		}
		//职称文字
		g.drawText(MyArStation.iPlayer.iTitle, 
				iSoX+ActivityUtil.LEVEL_BG_X_STANDARD*ActivityUtil.ZOOM_X+tOffsetX, 
				ActivityUtil.LEVEL_BG_Y_STANDARD*ActivityUtil.ZOOM_Y+tOffsetY, 
				ActivityUtil.mHartPaint);
		//醉酒度
//				if(iDrunkSprite != null) {
//					for(int i=0; i<GameMain.iPlayer.iCurrentDrunk; i++) {//
//						iDrunkSprite.paint(g, (int)(iSppX+(152+i*24)*ActivityUtil.ZOOM_X), (int)(344*ActivityUtil.ZOOM_Y));
//					}
//				}
		//在线人数
//				g.drawText("在线人数  "+GameMain.iPlayer.iOnLineCount, iSoX+550*ActivityUtil.ZOOM_X, 66*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
		//
	}

	@Override
	public void performL() {
		if(iBulletinView != null)iBulletinView.performL();
		if(missionView != null)missionView.performL();
		//动态动画
		if(iSynAnimation != null) iSynAnimation.performL();;
		if(iGameState == 2 && createBMP) {
			createBMP = false;
			creatBackBmp();
		}
		//载入数据
		if(iGameState == 0) {
			iLoginDelay ++;
			//10秒钟内强行切换状态
			iLoginDelay ++;
			if(iLoginDelay > 160) {
				Tools.debug("HomeScreen::performL()::Login Time Out!");
				//
				iLoginDelay = 0;
				//
				GameCanvas.getInstance().changeView(new LoginScreen(false));
				return;
			}
			if(iSceneLoading != null) {
				iSceneLoading.handle();
			}
		}
		//主界面面板进场  
		else if(iGameState == 1) {
			iSppX -= iSppSpeed;
			if(iSppX >= 0) {
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
			iSbTwoY -= iSbTwoSpeed;
			if(iSbTwoY <= 0) {
				iSbTwoY = 0;
			}
			if(iSppX == 0 && iSoX == 0 && iSbTwoY == 0 && iSbY == 0) {
				iGameState = 2;
				//创建背景图
				creatBackBmp();
			}
			spriteButtonPerform();
		}
		//主界面和更多界面
		else if(iGameState == 2 || iGameState == 8) {
			if(iMisSpriteButton != null && 
					MyArStation.iPlayer.iMissionRewardCount > 0 &&
					iMisSpriteButton.getActionID() == SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL) {
				iMisSpriteButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
			}
			spriteButtonPerform();
			selectRoomScreen.performL();
			if(iBulletinView != null)iBulletinView.performL();
			if(missionView != null)missionView.performL();
		}
		//3 --- 从主界面退场到个人资料        
		//6 --- 从更多界面退场到个人资料
		else if(iGameState == 3 || iGameState == 6) {
			if(iSppX > -500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < iBarMaxOffset) {
				iSbY += iSbSpeed;
			}
			if(iSbTwoY < iBarMaxOffset) {
				iSbTwoY += iSbTwoSpeed;
			}
			spriteButtonPerform();
			if(iSppX <= -500 && iSoX >= 500 && iSbY >= iBarMaxOffset && iSbTwoY >= iBarMaxOffset) {
				//切换到个人资料界面
				iGameCanvas.changeView(new InfoScreen());
			}
		}
		//4 --- 主界面操作面板退场到更多   	
		else if(iGameState == 4) {
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			spriteButtonPerform();
			if(iSoX >= 500) {
				ibMoreScene = true;
				//切换到个人资料界面
				iSceneOperate = null;
				iSceneOperate = iUIPak.getScene(6);
				if(iSceneOperate != null) {
					iSceneOperate.setViewWindow(0, 0, 800, 480);
					iSceneOperate.initDrawList();
					iSceneOperate.initNinePatchList(null);
					iSceneOperate.iCameraX = -(int)iSoX;
					//
					Map pMap = iSceneOperate.getLayoutMap(Scene.SpriteLayout);
					Vector<?> pSpriteList = pMap.getSpriteList();
					Sprite pSprite;
					iSpriteButtonList.removeAllElements();
					for (int i=0; i<pSpriteList.size(); i++) {
						pSprite = (Sprite)pSpriteList.elementAt(i);
						int pX = pSprite.getX();
						int pY = pSprite.getY();
						int pEventID = GameEventID.ESPRITEBUTTON_EVENT_MORE_OFFSET + pSprite.getIndex();
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
					showLeaveMsg();
				}
				iGameState = 7;
			}
		}
		//5 --- 主界面操着面板进场
		else if(iGameState == 5) {
			iSoX -= iSoSpeed;
			if(iSoX <= 0) {
				iSoX = 0;
			}
			if(iSoX == 0) {
				iGameState = 2;
			}
			spriteButtonPerform();
		}
		//7 --- 更多面板进场
		else if(iGameState == 7) {
			iSoX -= iSoSpeed;
			if(iSoX <= 0) {
				iSoX = 0;
			}
			if(iSoX == 0) {
				iGameState = 8;
			}
			spriteButtonPerform();
		}
		//9 --- 更多面板退场	
		else if(iGameState == 9) {
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			spriteButtonPerform();
			if(iSoX >= 500) {
				//切换到个人资料界面
				iSceneOperate = null;
				iSceneOperate = iUIPak.getScene(5);
				if(iSceneOperate != null) {
					iSceneOperate.setViewWindow(0, 0, 800, 480);
					iSceneOperate.initDrawList();
//					iSceneOperate.initNinePatchList(new int[] {R.drawable.new_bg_shell, R.drawable.new_online_back, R.drawable.new_online_back});
					iSceneOperate.iCameraX = -(int)iSoX;
					//
					Map pMap = iSceneOperate.getLayoutMap(Scene.SpriteLayout);
					Vector<?> pSpriteList = pMap.getSpriteList();
					Sprite pSprite;
					iSpriteButtonList.removeAllElements();
					for (int i=0; i<pSpriteList.size(); i++) {
						pSprite = (Sprite)pSpriteList.elementAt(i);
						int pX = pSprite.getX();
						int pY = pSprite.getY();
						int pEventID = GameEventID.ESPRITEBUTTON_EVENT_HOME_OFFSET + pSprite.getIndex();
						TPoint pPoint = new TPoint();
						pPoint.iX = pX;
						pPoint.iY = pY;
						//精灵按钮
						SpriteButton pSpriteButton;
						pSpriteButton = new SpriteButton(pSprite);
						pSpriteButton.setEventID(pEventID);
						pSpriteButton.setPosition(pX, pY);
						pSpriteButton.setHandler(this);
						//有奖品才显示
						if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_HOME_GIFT) {
							pSpriteButton.setVision(MyArStation.iPlayer.ibGetGift ? true : false);
							iGiftButton = pSpriteButton;
						}
						else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_HOME_CHANGE && MyArStation.iPlayer.ibVisitor) {
							pSpriteButton.setVision(false);
						}
						else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_HOME_MISSION) {
							iMisSpriteButton = pSpriteButton;
							if(MyArStation.iPlayer.iMissionRewardCount > 0) {
								iMisSpriteButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
							}
						}
						iSpriteButtonList.addElement(pSpriteButton);
					}
					showLeaveMsg();
				}
				iGameState = 5;
			}
		}
		else if(iGameState == 24) {
			iSbTwoY -= iSbTwoSpeed;
			if(iSbTwoY <= 0) {
				iSbTwoY = 0;
			}
			spriteButtonPerform();
			if(iSbTwoY == 0) {
				//切换到个人资料界面
				iSoX = 500;
				iSceneOperate = null;
				if(ibMoreScene) {
					iSceneOperate = iUIPak.getScene(6);
				}
				else {
					iSceneOperate = iUIPak.getScene(5);
				}
				if(iSceneOperate != null) {
					iSceneOperate.setViewWindow(0, 0, 800, 480);
					iSceneOperate.initDrawList();
					if(ibMoreScene) {
						iSceneOperate.initNinePatchList(new int[] {R.drawable.gdbj});
					}
					iSceneOperate.iCameraX = -(int)iSoX;
					//
					Map pMap = iSceneOperate.getLayoutMap(Scene.SpriteLayout);
					Vector<?> pSpriteList = pMap.getSpriteList();
					Sprite pSprite;
					for (int i=0; i<pSpriteList.size(); i++) {
						pSprite = (Sprite)pSpriteList.elementAt(i);
						int pX = pSprite.getX();
						int pY = pSprite.getY();
						int pEventID = (ibMoreScene ? GameEventID.ESPRITEBUTTON_EVENT_MORE_OFFSET : GameEventID.ESPRITEBUTTON_EVENT_HOME_OFFSET) + pSprite.getIndex();
						TPoint pPoint = new TPoint();
						pPoint.iX = pX;
						pPoint.iY = pY;
						//精灵按钮
						SpriteButton pSpriteButton;
						pSpriteButton = new SpriteButton(pSprite);
						pSpriteButton.setEventID(pEventID);
						pSpriteButton.setPosition(pX, pY);
						pSpriteButton.setHandler(this);
						if(!ibMoreScene) {
							//有奖品才显示
							if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_HOME_GIFT) {
								pSpriteButton.setVision(MyArStation.iPlayer.ibGetGift ? true : false);
								iGiftButton = pSpriteButton;
							}
							else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_HOME_CHANGE && MyArStation.iPlayer.ibVisitor) {
								pSpriteButton.setVision(false);
							}
							else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_HOME_MISSION) {
								iMisSpriteButton = pSpriteButton;
								if(MyArStation.iPlayer.iMissionRewardCount > 0) {
									iMisSpriteButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
								}
							}
						}
						iSpriteButtonList.addElement(pSpriteButton);
					}
					showLeaveMsg();
					iGameState = 5;
				}
			}
		}
		//10 --- 从主界面退场到快速游戏        
		else if(iGameState == 11) {
			if(iSppX > -500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < iBarMaxOffset) {
				iSbY += iSbSpeed;
			}
			if(iSbTwoY < iBarMaxOffset) {
				iSbTwoY += iSbTwoSpeed;
			}
			spriteButtonPerform();
			selectRoomScreen.performL();
			if(iSppX <= -500 && iSoX >= 500 && iSbY >= iBarMaxOffset && iSbTwoY >= iBarMaxOffset && !selectRoomScreen.vision()) {
				//切换到快速游戏   
				iGameCanvas.changeView(new ClassicalGame());
			}
		}
		//11 --- 退场到选择房间
		else if(iGameState == 10) {
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			
			if(iSbTwoY < iBarMaxOffset) {
				iSbTwoY += iSbTwoSpeed;
			}
			
			spriteButtonPerform();
			if(iSoX >= 500 && iSbTwoY >= iBarMaxOffset) {
				iSceneOperate = null;
				iSpriteButtonList.removeAllElements();
				//切换到选择房间
				selectRoomScreen.show();
				iGameState = 23;
			}
		}
		//12 --- 退场到排行榜
		else if(iGameState == 12) {
			if(iSppX > -500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < iBarMaxOffset) {
				iSbY += iSbSpeed;
			}
			if(iSbTwoY < iBarMaxOffset) {
				iSbTwoY += iSbTwoSpeed;
			}
			spriteButtonPerform();
			if(iSppX <= -500 && iSoX >= 500 && iSbY >= iBarMaxOffset && iSbTwoY >= iBarMaxOffset) {
				//切换到排行榜
				GameCanvas.getInstance().changeView(new RankScreen());
			}
		}
		//13 --- 退场到参加活动
		else if(iGameState == 13) {
			if(iSppX > -500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < iBarMaxOffset) {
				iSbY += iSbSpeed;
			}
			if(iSbTwoY < iBarMaxOffset) {
				iSbTwoY += iSbTwoSpeed;
			}
			spriteButtonPerform();
			selectRoomScreen.performL();
			if(iSppX <= -500 && iSoX >= 500 && iSbY >= iBarMaxOffset && iSbTwoY >= iBarMaxOffset && !selectRoomScreen.vision()) {
				//切换到个参加活动
				GameCanvas.getInstance().changeView(new ActivityScreen());
				//test去教程
//				GameCanvas.getInstance().changeView(new TutorialScreen());
				//test去炸金花
//				GameCanvas.getInstance().changeView(new GoldflowerGame());
			}
		}
		//14 --- 退场到商城
		else if(iGameState == 14 || iGameState == 18) {
			if(iSppX > -500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < iBarMaxOffset) {
				iSbY += iSbSpeed;
			}
			if(iSbTwoY < iBarMaxOffset) {
				iSbTwoY += iSbTwoSpeed;
			}
			spriteButtonPerform();
			if(iSppX <= -500 && iSoX >= 500 && iSbY >= iBarMaxOffset && iSbTwoY >= iBarMaxOffset) {
				//切换到商城
//				GameCanvas.getInstance().changeView(new StoreScreen());
			}
		}
		//15 --- 退场到好友
		else if(iGameState == 15 || iGameState == 19 || iGameState == 26) {
			if(iSppX > -500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < iBarMaxOffset) {
				iSbY += iSbSpeed;
			}
			if(iSbTwoY < iBarMaxOffset) {
				iSbTwoY += iSbTwoSpeed;
			}
			spriteButtonPerform();
			if(iSppX <= -500 && iSoX >= 500 && iSbY >= iBarMaxOffset && iSbTwoY >= iBarMaxOffset) {
				//切换到好友
				if(MyArStation.iPlayer.iLeaveMsgCount <= 0 && iGameState != 26) {
					iGameCanvas.changeView(new FriendInfoScreen());
				}
				else {
					iGameCanvas.changeView(new InfoScreen());
				}
			}
		}
		//16 --- 退场到照相
		else if(iGameState == 16 || iGameState == 17) {
			if(iSppX > -500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < iBarMaxOffset) {
				iSbY += iSbSpeed;
			}
			if(iSbTwoY < iBarMaxOffset) {
				iSbTwoY += iSbTwoSpeed;
			}
			spriteButtonPerform();
			if(iSppX <= -500 && iSoX >= 500 && iSbY >= iBarMaxOffset && iSbTwoY >= iBarMaxOffset) {
				//切换到照相
				GameCanvas.getInstance().changeView(new AlbumScreen());
			}
		}
		//20 --- 更多退场到设置
		else if(iGameState == 20) {
			if(iSppX > -500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < iBarMaxOffset) {
				iSbY += iSbSpeed;
			}
			if(iSbTwoY < iBarMaxOffset) {
				iSbTwoY += iSbTwoSpeed;
			}
			spriteButtonPerform();
			if(iSppX <= -500 && iSoX >= 500 && iSbY >= iBarMaxOffset && iSbTwoY >= iBarMaxOffset) {
				//切换到设置
				GameCanvas.getInstance().changeView(new SettingSceen());
			}
		}
		//21 --- 更多退场到帮助
		else if(iGameState == 21) {
			if(iSppX > -500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < iBarMaxOffset) {
				iSbY += iSbSpeed;
			}
			if(iSbTwoY < iBarMaxOffset) {
				iSbTwoY += iSbTwoSpeed;
			}
			spriteButtonPerform();
			if(iSppX <= -500 && iSoX >= 500 && iSbY >= iBarMaxOffset && iSbTwoY >= iBarMaxOffset) {
				//切换到帮助
				GameCanvas.getInstance().changeView(new HelpScreen());
			}
		}
		//22 --- 更多退场到教程
		else if(iGameState == 22) {
			if(iSppX > -500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < iBarMaxOffset) {
				iSbY += iSbSpeed;
			}
			if(iSbTwoY < iBarMaxOffset) {
				iSbTwoY += iSbTwoSpeed;
			}
			spriteButtonPerform();
			if(iSppX <= -500 && iSoX >= 500 && iSbY >= iBarMaxOffset && iSbTwoY >= iBarMaxOffset) {
				//切换到教程
				GameCanvas.getInstance().changeView(new TutorialScreen());
			}
		}
		else if(iGameState == 23) {
			selectRoomScreen.performL();
			spriteButtonPerform();
		}
		else if(iGameState == 25) {
			if(iSppX > -500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < iBarMaxOffset) {
				iSbY += iSbSpeed;
			}
			if(iSbTwoY < iBarMaxOffset) {
				iSbTwoY += iSbTwoSpeed;
			}
			spriteButtonPerform();
			if(iSppX <= -500 && iSoX >= 500 && iSbY >= iBarMaxOffset && iSbTwoY >= iBarMaxOffset) {
				if(iRoomData.roomType == RoomData.TYPE_UNITE) {
//					GameCanvas.getInstance().changeView(new UniteGame(iRoomData));
				}
				else {
					GameCanvas.getInstance().changeView(new ClassicalGame(iRoomData));
				}
			}
		}
		//切换账号
		else if(iGameState == 27) {
			if(iSppX > -500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < iBarMaxOffset) {
				iSbY += iSbSpeed;
			}
			if(iSbTwoY < iBarMaxOffset) {
				iSbTwoY += iSbTwoSpeed;
			}
			spriteButtonPerform();
			if(iSppX <= -500 && iSoX >= 500 && iSbY >= iBarMaxOffset && iSbTwoY >= iBarMaxOffset) {
				GameCanvas.getInstance().changeView(new LoginScreen(false));
			}
		}
		//切换账号
		else if(iGameState == 28) {
			if(iSppX > -500) {
				iSppX += iSppSpeed;
			}
			if(iSoX < 500) {
				iSoX += iSoSpeed;
			}
			if(iSbY < iBarMaxOffset) {
				iSbY += iSbSpeed;
			}
			if(iSbTwoY < iBarMaxOffset) {
				iSbTwoY += iSbTwoSpeed;
			}
			spriteButtonPerform();
			selectRoomScreen.performL();
			if(iSppX <= -500 && iSoX >= 500 && iSbY >= iBarMaxOffset && iSbTwoY >= iBarMaxOffset && !selectRoomScreen.vision()) {
				GameCanvas.getInstance().changeView(new AdScreen(MyArStation.iPlayer.iChangeUrl, 1));
			}
		}
		
	}
	
	private void spriteButtonPerform() {
//		if(iScene != null) {
//			iScene.handle();
//		}
		if(iScenePlayerPhoto != null) {
			iScenePlayerPhoto.handle();
			iScenePlayerPhoto.iCameraX = -(int)iSppX;
			//
			for(int i=0; i<iSpriteButtonListPhoto.size(); i++) {
				((SpriteButton)iSpriteButtonListPhoto.elementAt(i)).perform();
			}
		}
		if(iSceneOperate != null) {
			iSceneOperate.handle();
			iSceneOperate.iCameraX = -(int)iSoX;
			//
			for(int i=0; i<iSpriteButtonList.size(); i++) {
				((SpriteButton)iSpriteButtonList.elementAt(i)).perform();
			}
		}
		if(iSceneBar != null) {
			iSceneBar.handle();
			iSceneBar.iCameraY = (int)iSbY;
			for(int i=0; i<iSpriteButtonListBar.size(); i++) {
				((SpriteButton)iSpriteButtonListBar.elementAt(i)).perform();
			}
		}
		if(iSceneBarTwo != null) {
			iSceneBarTwo.handle();
			iSceneBarTwo.iCameraY = (int)iSbTwoY;
			for(int i=0; i<iSpriteButtonListBarTwo.size(); i++) {
				((SpriteButton)iSpriteButtonListBarTwo.elementAt(i)).perform();
			}
		}
	}

	//创建背景图
	private void creatBackBmp() {
		iSoX = 0;
		iSbX = 0;
		iSppX = 0;
		Canvas g  = new Canvas(iBackBmp);
		g.drawBitmap(MyArStation.iImageManager.iBackLoginBmp,
				0,
				0,
				null);
		if(iScenePlayerPhoto != null) {
			iScenePlayerPhoto.iCameraX = 0;
			iScenePlayerPhoto.paint(g, 0, 0);
		}
		if(iSceneOperate != null) {
			iSceneOperate.iCameraX = 0;
			paintPlayerInfo(g);
		}
		if(iSceneBarTwo != null) {
			iSceneBarTwo.iCameraX = 0;
			iSceneBarTwo.paint(g, 0, 0);
		}
		if(iSceneBar != null) {
			iSceneBar.iCameraX = 0;
			iSceneBar.paint(g, 0, 0);
			iSceneOperate.paint(g, 0, 0);
		}
		g = null;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(iBulletinView != null && iBulletinView.onKeyDown(keyCode, event)) return true;
			if(missionView != null && missionView.onKeyDown(keyCode, event)) return true;
			showReturnLoginConfirm();
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
		if (result) {
			return result;
		}
		if(missionView != null && missionView.pointerPressed(aX, aY)) return true;
		if(iBulletinView != null && iBulletinView.pointerPressed(aX, aY)) return true;
		if(selectRoomScreen !=null &&selectRoomScreen.pointerPressed(aX, aY)) return true;
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
				//System.out.println("ProgressScreen pointerPressed  pSB.pressed() ");
				result = true;
				break;
			}
		}
		if(result == false) {
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
		if(result == false  && iGameState != 23) {
			for(int i=0; i<iSpriteButtonListBarTwo.size(); i++) {
				pSB = ((SpriteButton)iSpriteButtonListBarTwo.elementAt(i));
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
		if(result == false) {
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
		}
		if (result == false) {
			int pX = (int) iPicX;
			int pY = (int) iPicY;
			if(iPicBMPZoom == null) {
				return result;
			}
			pRect.set(pX, pY,
					pX+iPicBMPZoom.getWidth(), pY+iPicBMPZoom.getHeight());
			if (pRect.contains(aX, aY)) {
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
				iGameState = 26;
			}
		}
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
		if(aEventID == GameEventID.ESPRITEBUTTON_EVENT_HOME_INFO) {//我的酒吧
			return true;
		}
		if(selectRoomScreen.ItemAction(aEventID))return true;
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.windowgo, 0);
		switch (aEventID) {
		case GameEventID.ESPRITEBUTTON_EVENT_HOME_QUICKSTART: //快速开始
			iGameState = 11; //退出到快速游戏界面
			selectRoomScreen.setOnAnimateListen(null);
			selectRoomScreen.dimiss();
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_HOME_SELECTROOM:
			iGameState = 10; //11 --- 退场到选择房间
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_HOME_SHOP:	//商城
			iGameState = 14;//14 --- 退场到商城
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_MORE_SHOP:	//商城
			iGameState = 18;//18 --- 更多退场到商城
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_MORE_SETTING: //设置
			iGameState = 20;//20 --- 更多退场到设置
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.windowgo, 0);
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_MORE_HELP: //帮助
			iGameState = 21;//21 --- 更多退场到帮助
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.windowgo, 0);
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_MORE_GUIDE: //教程
			iGameState = 22;//22 --- 更多退场到教程
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.windowgo, 0);
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_MORE_FEEDBACK: //反馈
			messageEvent(GameEventID.ESPRITEBUTTON_EVENT_MORE_FEEDBACK);
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_MORE_MESSAGE: //公告信息
			messageEvent(GameEventID.ESPRITEBUTTON_EVENT_MORE_MESSAGE);
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_MORE_FRIEND:
			iGameState = 19;//19 --- 更多退场到好友
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_HOME_FRIEND://好友
			iGameState = 15;//15 --- 退场到好友
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.windowgo, 0);
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_INVI_FRIEND:
		case GameEventID.ESPRITEBUTTON_EVENT_MORE_INVI_FRIEND:
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.windowgo, 0);
			Intent shareInt=new Intent(Intent.ACTION_SEND);
	        shareInt.setType("text/plain");   
	        shareInt.putExtra(Intent.EXTRA_SUBJECT, "选择分享方式");   
	        shareInt.putExtra(Intent.EXTRA_TEXT, MyArStation.getInstance().getString(R.string.share_weibo, MyArStation.getInstance().iPlayer.apkUrl));    
	        shareInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	        MyArStation.mGameMain.startActivityForResult(shareInt, 300);
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_HOME_MORE://更多
			iGameState = 4; //退出到更多界面
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.windowgo, 0);
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_HOME_INFO://我的酒吧
//			if(iGameState == 8) {
//				iGameState = 6;	//退出到个人资料界面
//			}
//			else {
//				iGameState = 3; //退出到个人资料界面
//			}
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_HOME_RANK://排行榜
			iGameState = 12;//12 --- 退场到排行榜
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.windowgo, 0);
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_HOME_PHOTO://拍照
			iGameState = 16;//退场到照相
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.windowgo, 0);
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_MORE_PHOTO://拍照
			iGameState = 17;//退场到照相
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.windowgo, 0);
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_HOME_MOTION://活动
			iGameState = 13;//13 --- 退场到参加活动
			selectRoomScreen.setOnAnimateListen(null);
			selectRoomScreen.dimiss();
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_MORE_BACK: //返回
			ibMoreScene = false;
			iGameState = 9;
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.windowgo, 0);
			return true;
			
		case GameEventID.ESPRITEBUTTON_EVENT_HOME_GIFT: //领奖
			if(iGiftButton != null)iGiftButton.setVision(false);
			MyArStation.iPlayer.ibGetGift = false;
			requestGiftList();
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_HOME_CHANGEGIFT://兑换礼品
			iGameState = 28;
			selectRoomScreen.setOnAnimateListen(null);
			selectRoomScreen.dimiss();
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_HOME_CHANGE://充值
			messageEvent(GameEventID.ESPRITEBUTTON_EVENT_HOME_CHANGE);
			return true;
		case MessageEventID.EMESSAGE_EVENT_TO_LEAVEMSG_INFO:
			showLeaveMsg();
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_HOME_CHANGEACOUNT://撤换账号
			iGameState = 27;
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_HOME_MISSION://任务
			if(missionView == null) missionView = new MissionView(Context);
			if(iMisSpriteButton != null) iMisSpriteButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
			MyArStation.iPlayer.iMissionRewardCount = 0;
			missionView.show();
			break;
		}
		return false;
	}
	
	//初始化画笔
	private void initPaint() {
		iExpPaint = new Paint();
		iExpPaint.setColor(Color.MAGENTA);
		iLovePaint = new Paint();
		iLovePaint.setTextSize(ActivityUtil.TEXTSIZE_NORMAL);     //设置字体大小
		iLovePaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		iLovePaint.setColor(Color.WHITE);
//		iLovePaint.setTextAlign(Align.CENTER); 	//水平居中
		iOnlinePaint = new Paint();
		iOnlinePaint.setTextSize(ActivityUtil.TEXTSIZE_NORMAL);     //设置字体大小
		iOnlinePaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		iOnlinePaint.setColor(Color.BLACK);
		iLoadingPaint = new Paint();
		iLoadingPaint.setColor(Color.BLACK);
	}
	
	//设置经验条
	private void setExpRect() {
		float tW = ActivityUtil.LEVEL_BG_W_STANDARD*ActivityUtil.ZOOM_X*MyArStation.iPlayer.iGameExp/MyArStation.iPlayer.iNextExp;
		float tLeft = ActivityUtil.EXP_BG_X_STANDARD*ActivityUtil.ZOOM_X;
		float tTop = ActivityUtil.EXP_BG_Y_STANDARD*ActivityUtil.ZOOM_Y;
		float tRight = tLeft + tW;
		float tBottom = tTop + ActivityUtil.LEVEL_BG_H_STANDARD*ActivityUtil.ZOOM_Y;
		iExpRect.set(tLeft, tTop, tRight, tBottom);
	}
	
	//初始化 相片、头像
	private void initPic() {
		iPicBMP = MyArStation.iHttpDownloadManager.getImage(MyArStation.iPlayer.szBigPicName);
		int tX_Offset = ActivityUtil.PIC_BG_X_STANDARD;
		int tY_Offset = ActivityUtil.PIC_BG_Y_STANDARD;
		int tW_BG = ActivityUtil.PIC_BG_W_STANDARD;
		int tH_BG = ActivityUtil.PIC_BG_H_STANDARD;
		if (iPicBMP == null) {
			return;
		}
		else {
			float tZoom = 1;//图片 最终缩放率(X、Y方向)
			float tSrcW = iPicBMP.getWidth();
			float tSrcH = iPicBMP.getHeight();
			float tPicBGW = ((ActivityUtil.PIC_BG_W_STANDARD-4)*ActivityUtil.ZOOM_X);//图片底板限制宽
			float tPicBGH = ((ActivityUtil.PIC_BG_H_STANDARD-4)*ActivityUtil.ZOOM_Y);
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
			iPicBMPZoom = ImageUtil.getRoundedCornerBitmap(iPicBMPZoom, 7.0f);
			if (iPicBMPZoom != iPicBMP) {
				//如果是不一样内存的，iPicBMPZoom是新创建的，先释放iPicBMP的内存
				iPicBMP.recycle();
			}
			iPicBMP = null;
//			System.out.println("HS initPic iPicBMPZoom w="+iPicBMPZoom.getWidth()+", h="+iPicBMPZoom.getHeight());
			iPicX = tX_Offset*ActivityUtil.ZOOM_X+tW_BG/2*ActivityUtil.ZOOM_X-iPicBMPZoom.getWidth()/2;
			iPicY = tY_Offset*ActivityUtil.ZOOM_Y+tH_BG/2*ActivityUtil.ZOOM_Y-iPicBMPZoom.getHeight()/2;
		}
	}
	
	private void loadUIPak() {
		iSceneLoading = LoadLoading();
		iUIPak = CPakManager.loadUIPak();//Project.loadProject(ActivityUtil.PATH_SCREEN+"gameshome_map.pak", false);
		if (iUIPak != null) {
			iScenePlayerPhoto = iUIPak.getScene(4);
			if(iScenePlayerPhoto != null) {
				iScenePlayerPhoto.setViewWindow(0, 0, 800, 480);
				iScenePlayerPhoto.initDrawList();
//				iScenePlayerPhoto.initNinePatchList(new int[] {R.drawable.btmck, R.drawable.new_ttitle, R.drawable.new_back02});//R.drawable.new_ttitle, R.drawable.new_back00});
				//
				Map pMap = iScenePlayerPhoto.getLayoutMap(Scene.SpriteLayout);
				Vector<?> pSpriteList = pMap.getSpriteList();
				Sprite pSprite;
				for (int i=0; i<pSpriteList.size(); i++) {
					pSprite = (Sprite)pSpriteList.elementAt(i);
					int pX = pSprite.getX();
					int pY = pSprite.getY();
					int pEventID = GameEventID.ESPRITEBUTTON_EVENT_HOME_OFFSET + pSprite.getIndex();
					TPoint pPoint = new TPoint();
					pPoint.iX = pX;
					pPoint.iY = pY;
					//精灵按钮
					SpriteButton pSpriteButton;
					pSpriteButton = new SpriteButton(pSprite);
					pSpriteButton.setEventID(pEventID);
					pSpriteButton.setPosition(pX, pY);
					pSpriteButton.setHandler(this);
					iSpriteButtonListPhoto.addElement(pSpriteButton);
//					//有奖品才显示
//					if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_HOME_GIFT) {
//						pSpriteButton.setVision(false);
//						iGiftButton = pSpriteButton;
//					}
//					else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_HOME_CHANGE && GameMain.iPlayer.ibVisitor) {
//						pSpriteButton.setVision(false);
//					}
				}
				//
//				iDrunkSprite = iUIPak.getSprite(35);	//酒杯动画
			}
			iSceneOperate = iUIPak.getScene(5);
			if(iSceneOperate != null) {
				iSceneOperate.setViewWindow(0, 0, 800, 480);
				iSceneOperate.initDrawList();
//				iSceneOperate.initNinePatchList(new int[] {R.drawable.new_bg_shell, R.drawable.new_online_back, R.drawable.new_online_back});
				//
				Map pMap = iSceneOperate.getLayoutMap(Scene.SpriteLayout);
				Vector<?> pSpriteList = pMap.getSpriteList();
				Sprite pSprite;
				for (int i=0; i<pSpriteList.size(); i++) {
					pSprite = (Sprite)pSpriteList.elementAt(i);
					int pX = pSprite.getX();
					int pY = pSprite.getY();
					int pEventID = GameEventID.ESPRITEBUTTON_EVENT_HOME_OFFSET + pSprite.getIndex();
					TPoint pPoint = new TPoint();
					pPoint.iX = pX;
					pPoint.iY = pY;
					//精灵按钮
					SpriteButton pSpriteButton;
					pSpriteButton = new SpriteButton(pSprite);
					pSpriteButton.setEventID(pEventID);
					pSpriteButton.setPosition(pX, pY);
					pSpriteButton.setHandler(this);
					//有奖品才显示
					if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_HOME_GIFT) {
						pSpriteButton.setVision(false);
						iGiftButton = pSpriteButton;
					}
					else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_HOME_CHANGE && MyArStation.iPlayer.ibVisitor) {
						pSpriteButton.setVision(false);
					}
					else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_HOME_MISSION) {
						iMisSpriteButton = pSpriteButton;
						if(MyArStation.iPlayer.iMissionRewardCount > 0) {
							iMisSpriteButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
						}
					}
					iSpriteButtonList.addElement(pSpriteButton);
				}
			}
			iSceneBar = iUIPak.getScene(25);
			if(iSceneBar != null) {
				iSceneBar.setViewWindow(0, 0, 800, 480);
				iSceneBar.initDrawList();
				iSceneBar.initNinePatchList(null);
				//
				Rect temp = iSceneBar.getLayoutMap(Scene.RectangleLayout).getNineRect(0);
				if(temp != null) {
					iBarMaxOffset = temp.bottom;
					iSbY = iBarMaxOffset;
					iSbSpeed = (int) (iBarMaxOffset / 6);
					iSceneBar.iCameraY = (int)iBarMaxOffset;
				}
				Map pMap = iSceneBar.getLayoutMap(Scene.SpriteLayout);
				Vector<?> pSpriteList = pMap.getSpriteList();
				Sprite pSprite;
				for (int i=0; i<pSpriteList.size(); i++) {
					pSprite = (Sprite)pSpriteList.elementAt(i);
					int pX = pSprite.getX();
					int pY = pSprite.getY();
					int pEventID = GameEventID.ESPRITEBUTTON_EVENT_HOME_OFFSET + pSprite.getIndex();
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
				}
			}
			iSceneBarTwo = iUIPak.getScene(26);
			if(iSceneBarTwo != null) {
				iSceneBarTwo.setViewWindow(0, 0, 800, 480);
				iSceneBarTwo.initDrawList();
				iSceneBarTwo.initNinePatchList(null);
				Rect temp = iSceneBarTwo.getLayoutMap(Scene.RectangleLayout).getNineRect(0);
				if(temp != null) {
					iSbTwoY = iBarMaxOffset;
					iSbTwoSpeed = (int) (iBarMaxOffset / 6);
					iSceneBarTwo.iCameraY = (int)iBarMaxOffset;
				}
				//
				Map pMap = iSceneBarTwo.getLayoutMap(Scene.SpriteLayout);
				Vector<?> pSpriteList = pMap.getSpriteList();
				Sprite pSprite;
				for (int i=0; i<pSpriteList.size(); i++) {
					pSprite = (Sprite)pSpriteList.elementAt(i);
					int pX = pSprite.getX();
					int pY = pSprite.getY();
					int pEventID = GameEventID.ESPRITEBUTTON_EVENT_HOME_OFFSET + pSprite.getIndex();
					TPoint pPoint = new TPoint();
					pPoint.iX = pX;
					pPoint.iY = pY;
					//精灵按钮
					SpriteButton pSpriteButton;
					pSpriteButton = new SpriteButton(pSprite);
					pSpriteButton.setEventID(pEventID);
					pSpriteButton.setPosition(pX, pY);
					pSpriteButton.setHandler(this);
					iSpriteButtonListBarTwo.addElement(pSpriteButton);
				}
			}
		}
		
		//载入活动信息Pak
		iActivityInfPak = CPakManager.loadActivityInfPak();
		iActivityInfSprite = iActivityInfPak.getSprite(0);
		//
		showLeaveMsg();
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#OnProcessCmd(java.lang.Object, int, int)
	 */
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
		if(missionView != null)missionView.OnProcessCmd(socket, aMobileType, aMsgID);
		if(selectRoomScreen != null)selectRoomScreen.OnProcessCmd(socket, aMobileType, aMsgID);
		MobileMsg pMobileMsg = (MobileMsg) socket;
		switch (aMobileType) {
		case IM.IM_RESPONSE:
		{
			switch (aMsgID) {
			case RspMsg.MSG_RSP_LOGIN:
				GameCanvas.getInstance().changeView(new LoginScreen());
				break;
			case RspMsg.MSG_RSP_GIFT_LIST:
				MBRspGiftList rspGiftList = (MBRspGiftList)pMobileMsg.m_pMsgPara;
				if(rspGiftList == null) {
					break;
				}
				Tools.debug(rspGiftList.toString());
				int resultcode = rspGiftList.iResult;
				if(ResultCode.SUCCESS == resultcode) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_GIFTLIST, rspGiftList);
				}
				else {
					MyArStation.iPlayer.ibGetGift = false;
					if(iGiftButton != null)iGiftButton.setVision(false);
					messageEvent(resultcode, rspGiftList);
				}
				break;
			case RspMsg.MSG_RSP_MAINPAGE_B:
				MBRspMainPageB rspMainPage = (MBRspMainPageB)pMobileMsg.m_pMsgPara;
				if(rspMainPage == null) {
					break;
				}
				Tools.println(rspMainPage.toString());
				int type = rspMainPage.iResult;
				if(type == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspMainPage, rspMainPage);
				}
				else {
					messageEvent(type, rspMainPage.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_FEEDBACK:
				removeProgressDialog();
				MBRspFeekBack rspFeekBack = (MBRspFeekBack)pMobileMsg.m_pMsgPara;
				if(rspFeekBack == null) {
					break;
				}
				Tools.debug(rspFeekBack.toString());
				type = rspFeekBack.nResult;
				if(type == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspFeekBack, rspFeekBack);
				}
				else {
					messageEvent(type, rspFeekBack.iMsg);
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
			case RspMsg.MSG_RSP_ACTIVITY_ENROLL:
				MBRspActivityEnroll rspActivityEnroll = (MBRspActivityEnroll)pMobileMsg.m_pMsgPara;
				if(rspActivityEnroll == null) {
					return;
				}
				Tools.debug(rspActivityEnroll.toString());
				if(rspActivityEnroll.nResult == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_ENROLL, rspActivityEnroll);
				}
				else {
					messageEvent(rspActivityEnroll.nResult, rspActivityEnroll);
				}
				break;
			case RspMsg.MSG_RSP_INVITE_FRIEND:
				MBRspInviteFriend rspInviteFriend = (MBRspInviteFriend)pMobileMsg.m_pMsgPara;
				if(rspInviteFriend == null) {
					return;
				}
				Tools.debug(rspInviteFriend.toString());
//				if(rspInviteFriend.iResult == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_HOME_RSP_INVITE, rspInviteFriend);
//				}
				break;
			case RspMsg.MSG_RSP_SET_PWD_QUESTION:
				MBRspSetPwdQuestion rspSetPwdQuestion = (MBRspSetPwdQuestion)pMobileMsg.m_pMsgPara;
				if(rspSetPwdQuestion == null) {
					return;
				}
				Tools.debug(rspSetPwdQuestion.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RSPSETANSWER, rspSetPwdQuestion);
//				}
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
			case RspMsg.MSG_RSP_AWARD:
				MBRspAward rspAward = (MBRspAward)pMobileMsg.m_pMsgPara;
				if(rspAward == null) {
					return;
				}
				Tools.println(rspAward.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_HOME_AWARD, rspAward);
//				}
				break;
			default:
				break;
			}
			break;
		}
		case IM.IM_NOTIFY:
		{
			switch (aMsgID) {
			case NotifyMsg.MSG_NOTIFY_ACTIVITY_START:
				removeProgressDialog();
				MBNotifyActivityStart rspActivityStart = (MBNotifyActivityStart)pMobileMsg.m_pMsgPara;
				if(rspActivityStart == null) {
					break;
				}
				Tools.debug(rspActivityStart.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_ACIIVITY_START, rspActivityStart);
				break;
			case NotifyMsg.MSG_NOTIFY_GET_GIFT:
				MBNotifyGetGift rspGetGift = (MBNotifyGetGift)pMobileMsg.m_pMsgPara;
				if(rspGetGift == null) {
					break;
				}
				Tools.debug(rspGetGift.toString());
				if(iGiftButton != null)iGiftButton.setVision(true);
				break;
			case NotifyMsg.MSG_NOTIFY_PLAY_ANIMATION:
				MBNotifyPlayAnimation rspPlayAnimation = (MBNotifyPlayAnimation)pMobileMsg.m_pMsgPara;
				if(rspPlayAnimation == null) {
					break;
				}
				Tools.debug(rspPlayAnimation.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_PALY_ANIMATION, rspPlayAnimation);
				break;
			case NotifyMsg.MSG_NOTIFY_RECHARGE:
				MBNotifyRecharge notifyRecharge = (MBNotifyRecharge)pMobileMsg.m_pMsgPara;
				if(notifyRecharge == null) {
					return;
				}
				Tools.debug(notifyRecharge.toString());
				if(notifyRecharge.iResult == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyRecharge, notifyRecharge);
				}
				else {
					messageEvent(notifyRecharge.iResult, notifyRecharge.iMsg);
				}
				break;
			case NotifyMsg.MSG_NOTIFY_NO_PWD_QUESTION:
				MBNotifyNoPwdQuestion notifyNoPwdQuestion = (MBNotifyNoPwdQuestion)pMobileMsg.m_pMsgPara;
				if(notifyNoPwdQuestion == null) {
					return;
				}
				Tools.debug(notifyNoPwdQuestion.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_NOANSWER, notifyNoPwdQuestion);
				break;
			case NotifyMsg.MSG_NOTIFY_PLAY_ANIMATION_TWO:
				MBNotifyPlayAnimationTwo notifyPlayAnimationTwo = (MBNotifyPlayAnimationTwo)pMobileMsg.m_pMsgPara;
				if(notifyPlayAnimationTwo == null) {
					break;
				}
				Tools.println(notifyPlayAnimationTwo.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_ANIMATIONTWO, notifyPlayAnimationTwo);
				break;
			default:
				break;
			}
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void handleMessage(Message msg) {
		if(missionView != null)missionView.handleMessage(msg);
		switch (msg.what) {
		case ResultCode.ERR_DECODEBUF:
		case ResultCode.ERR_TIMEOUT:
		case ResultCode.ERR_BUSY: 
		case ResultCode.ERR_ROOM_TABLE_INVALED:
		case ResultCode.ERR_ROOM_TABLE_FULL:
		case ResultCode.ERR_ROOM_FULL:
		case ResultCode.ERR_ROOM_GB_NOTENOUGH:
		case ResultCode.ERR_ROOM_DRUNK:
		case ResultCode.ERR_KICK_NO_WINE:
		case ResultCode.ERR_KICK_NO_GB_WINE:
			Tools.showSimpleToast(Context, Gravity.CENTER, (String)msg.obj, Toast.LENGTH_LONG);
			removeProgressDialog();
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspMainPage:
			iPlayerInfo = (MBRspMainPageB)msg.obj;
			if(iPlayerInfo.iMsg.length() > 0) {
				Tools.showSimpleToast(Context, Gravity.CENTER, iPlayerInfo.iMsg, Toast.LENGTH_LONG);
			}
			MyArStation.iHeart.startTimer(120, 120);
			//处理 公告
//			if (iPlayerInfo.iText.length() >0 ){
//				iNocticeList.add(0, iPlayerInfo.iText);
//				if (!ActivityUtil.iMsgList.contains(iPlayerInfo.iText)) {
//					ActivityUtil.iMsgList.add(0, iPlayerInfo.iText);
//				}
//				showNewNotice();
//			}
			//将数据赋值给Player
			MyArStation.iPlayer.ibInit = true;
			MyArStation.iPlayer.iTips = iPlayerInfo.iText;
			MyArStation.iPlayer.iUserID = iPlayerInfo.iUserId; //用户uid
			MyArStation.iPlayer.stUserNick = iPlayerInfo.iNick;//用户昵称
			MyArStation.iPlayer.iMoney = iPlayerInfo.iMoney;	//凤智币
			MyArStation.iPlayer.iGb = iPlayerInfo.iGb;			//游戏币
			MyArStation.iPlayer.iVipLevel = iPlayerInfo.iVipLevel;//Vip级别	
			MyArStation.iPlayer.iVipTime = iPlayerInfo.iVipTime;	//Vip到期时间
			MyArStation.iPlayer.iGameLevel = iPlayerInfo.iGameLevel;	//级别
			MyArStation.iPlayer.iGameExp = iPlayerInfo.iGameExp;	//当前级别的经验
			MyArStation.iPlayer.iCurrentDrunk = iPlayerInfo.iCurrentDrunk;//当前醉酒度
			MyArStation.iPlayer.iDingCount = iPlayerInfo.iMaxDrunk;//支持数（原来是最大醉酒度，一直没有用)
			MyArStation.iPlayer.iWinCount = iPlayerInfo.iKillsCount;	//灌倒多少人
//			iPlayerInfo.iBeKillsCount;//有多少条领奖信息
			MyArStation.iPlayer.iModelID = iPlayerInfo.iModelID;//角色模型ID（为-1时，表示没选择人物）
			MyArStation.iPlayer.iSex = iPlayerInfo.iSex;	//性别（为-1时，表示没选择人物）
			MyArStation.iPlayer.iWinning = iPlayerInfo.iWinning;	//胜率
			MyArStation.iPlayer.iTitle = iPlayerInfo.iTitle;		//人物称号
			MyArStation.iPlayer.iReward = iPlayerInfo.iReward;		//奖
			MyArStation.iPlayer.iNextExp = iPlayerInfo.iNextExp;	//升级经验
			MyArStation.iPlayer.szLittlePicName = iPlayerInfo.iLittlePicName;//头像图片
			MyArStation.iPlayer.szBigPicName = iPlayerInfo.iBigPicName;//大图片
			MyArStation.iPlayer.iOnLineCount = iPlayerInfo.iOnLineCount; //同时在线人数
			MyArStation.iPlayer.iZhaiSkill = iPlayerInfo.iZhaiSkill; //直接喊斋技能 0 --- 没学会    1 --- 学会
//			GameMain.iPlayer.iChangeUrl = iPlayerInfo.iUrl;//
			//获取网址
			String url = iPlayerInfo.iUrl;
			MyArStation.iPlayer.urls = url.split("\\|");
			for(int i = 0; i < MyArStation.iPlayer.urls.length; i++) {
				Tools.debug("HomeScreen::handleMessage() url" + i + "=" + MyArStation.iPlayer.urls[i]);
			}
			MyArStation.iPlayer.iChangeUrl = WebUrls.baseUrl + (MyArStation.iPlayer.urls != null && MyArStation.iPlayer.urls.length >= 1? MyArStation.iPlayer.urls[0] : "") + (MyArStation.iPlayer.urls != null && MyArStation.iPlayer.urls.length >= 2? MyArStation.iPlayer.urls[1] : "");
			Tools.debug("HomeScreen::handleMessage() iChangeUrl=" + MyArStation.iPlayer.iChangeUrl);
			//显示公告
			showReward();
			//如果有奖励信息，则显示领奖按钮
			if(iPlayerInfo.iBeKillsCount > 0 || MyArStation.iPlayer.ibGetGift) {
				MyArStation.iPlayer.ibGetGift = true;
				if(iGiftButton != null)iGiftButton.setVision(true);
			}
			//设置经验槽
			setExpRect();
			//设置照片
			initPic();
			//切换状态到进场
			iGameState = 1;
			//
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.windowgo, 0);
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_ShowMainPageLoading:
			showProgressDialog(R.string.Loading_String);
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspFeekBack:
			MBRspFeekBack rspFeekBack = (MBRspFeekBack)msg.obj;
			Tools.showSimpleToast(Context, Gravity.CENTER, rspFeekBack.iMsg, Toast.LENGTH_LONG);
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_Heart:
			MBRspHart rspHart = (MBRspHart)msg.obj;
			MyArStation.iPlayer.iOnLineCount = rspHart.OnlineCount;
			break;
		case MessageEventID.EMESSAGE_EVENT_HOME_INIT_MSG:
			MyArStation.mGameMain.mainLayout.removeAllViews();
			concuorDialog = new ConcoursDialog(MyArStation.mGameMain, this);
			giftDialog = new GiftDialog(MyArStation.mGameMain);
			setAserPswddDialog = new SetAserPswddDialog(MyArStation.mGameMain);
			iBulletinView = new BulletinView(MyArStation.mGameMain);
			missionView = new MissionView(MyArStation.mGameMain);
//			initNoticeBoard();
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_SINGLE_PIC://单张图片下完
			{
				String tPicName = (String) msg.obj;//图片名
				if (tPicName.equals(MyArStation.iPlayer.szBigPicName)) {
					initPic();
					//
//					if(iGameState == 2) {
//						creatBackBmp();
						createBMP = true;
//					}
				}
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_NOTIFY_ACIIVITY_START:
			removeProgressDialog();
			MBNotifyActivityStart notifyActivityStart = (MBNotifyActivityStart)msg.obj;
			if(notifyActivityStart.iMsg.length() > 0) {
				Tools.showSimpleToast(MyArStation.mGameMain, Gravity.CENTER, notifyActivityStart.iMsg, Toast.LENGTH_LONG);
			}
			if(iGameState != 0) {
				showMotionStart(notifyActivityStart);
			}
			break;	
		case MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_ENROLL:		//没有报名
			removeProgressDialog();
			MBRspActivityEnroll rspActivityEnroll = (MBRspActivityEnroll)msg.obj;
			if(rspActivityEnroll.iMsg.length() > 0) {
				Tools.showSimpleToast(MyArStation.mGameMain, Gravity.CENTER, rspActivityEnroll.iMsg, Toast.LENGTH_LONG);
			}
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_MORE_FEEDBACK:
			feedBackDialog = new FeedBackDialog(MyArStation.getInstance());	
			feedBackDialog.show();
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_MORE_MESSAGE:
//			showAllNotice();
			showReward();
			break;
		case MessageEventID.EMESSAGE_EVENT_RSP_GIFTLIST:
			MyArStation.iPlayer.ibGetGift = false;
			if(iGiftButton != null)iGiftButton.setVision(false);
			MBRspGiftList giftList = (MBRspGiftList) msg.obj;
			giftDialog.setGiftDatas(giftList.iGiftMsgs);
			giftDialog.show();
			break;
		case MessageEventID.EMESSAGE_EVENT_HOME_RSP_INVITE:
			MBRspInviteFriend rspInviteFriend = (MBRspInviteFriend)msg.obj;
			if(rspInviteFriend.iMsg.length() > 0) {
				Tools.showSimpleToast(MyArStation.getInstance().getApplicationContext(),
						rspInviteFriend.iMsg);
			}
			if(rspInviteFriend.iResult == ResultCode.SUCCESS) {
				MyArStation.iPlayer.iGb = rspInviteFriend.iGB;
				//
//				if(iGameState == 2) {
//					creatBackBmp();
					createBMP = true;
//				}
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_PALY_ANIMATION:
				MBNotifyPlayAnimation animation = (MBNotifyPlayAnimation)msg.obj;
			break;
		case MessageEventID.EMESSAGE_EVENT_TO_REFRESH_HOMEMAIN://刷新界面
			{
				//
//				if(iGameState == 2) {
//					creatBackBmp();
					createBMP = true;
//				}
			}
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_HOME_CHANGE:
//			if(chargeDialog == null) chargeDialog = new ChargeDialog(GameMain.mGameMain);
//			chargeDialog.show();
//			setAserPswddDialog.setCancelable(false);
//			setAserPswddDialog.setType(0);
//			setAserPswddDialog.show();
			break;
		case MessageEventID.EMESSAGE_EVENT_TO_ACTIVITY_INFO:
			MBNotifyActivityInfo activityInfo = (MBNotifyActivityInfo)msg.obj;
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyRecharge:
			MBNotifyRecharge notifyRecharge = (MBNotifyRecharge)msg.obj;
			if(notifyRecharge.iMsg.length() > 0) {
				Tools.showSimpleToast(MyArStation.mGameMain, Gravity.CENTER, notifyRecharge.iMsg, 1);
			}
//			chargeDialog.updateInfo(notifyRecharge.iMoney, 0);
			MyArStation.iPlayer.iMoney = notifyRecharge.iMoney;
			messageEvent(MessageEventID.EMESSAGE_EVENT_TO_REFRESH_HOMEMAIN);
			break;
		case MessageEventID.EMESSAGE_EVENT_TO_LEAVEMSG_INFO:
				showLeaveMsg();
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_NOANSWER:
			setAserPswddDialog.setCancelable(false);
			setAserPswddDialog.setType(1);
			setAserPswddDialog.show();
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RSPSETANSWER:
			removeProgressDialog();
			MBRspSetPwdQuestion mbRspSetPwdQuestion = (MBRspSetPwdQuestion) msg.obj;
			if(mbRspSetPwdQuestion.iMsg.length() > 0) {
				Tools.showSimpleToast(MyArStation.mGameMain, mbRspSetPwdQuestion.iMsg);
			}
			if(mbRspSetPwdQuestion.nResult != 0) {
				setAserPswddDialog.setCancelable(false);
				setAserPswddDialog.setType(1);
				setAserPswddDialog.show();
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RSPRESETPSWD:
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
		case MessageEventID.EMESSAGE_EVENT_HOME_AWARD://领奖应答
			MBRspAward rspAward = (MBRspAward) msg.obj;
			Tools.showSimpleToast(Context, rspAward.iMsg);
			break;
		case MessageEventID.EMESSAGE_EVENT_ANIMATIONTWO: //播放动画2
		{
			MBNotifyPlayAnimationTwo notifyPlayAnimation = (MBNotifyPlayAnimationTwo)msg.obj;
			if(iSynAnimation != null) iSynAnimation.setAnimationData(notifyPlayAnimation.szAnimDes, null, null);
		}
		break;
		default:
			break;
		}
	}

	/**
	 * 打开登录领奖界面
	 */
	private void showReward() {
		if(iBulletinView == null) iBulletinView = new BulletinView(MyArStation.mGameMain);
		if(MyArStation.iPlayer.urls !=null && MyArStation.iPlayer.urls.length >= 3) {
			iBulletinView.setRewardUrl(MyArStation.iPlayer.urls[0] + MyArStation.iPlayer.urls[3]);
			iBulletinView.setNewsUrl(MyArStation.iPlayer.urls[0] + MyArStation.iPlayer.urls[2]);
		}
		iBulletinView.setObtain(MyArStation.iPlayer.iReward == 0 ? false : true);
		iBulletinView.show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onClick(View v) {
	}
	
	private void showReturnLoginConfirm() {
		//根据自己的状态，给出相应提示
		if(dialog == null) {
			dialog = new Dialog(MyArStation.mGameMain, R.style.MSGDialogStyle);
			dialog.setContentView(R.layout.new_dialog);
			dialog.setCancelable(true);
			TextView title = (TextView)dialog.findViewById(R.id.dialog_title);
			title.setText(R.string.Return_LoginScreen_tip_title);
			TextView content = (TextView)dialog.findViewById(R.id.dialog_message);
			StringBuffer temp = new StringBuffer(MyArStation.iPlayer.iTips);
			int len = temp.length();
			temp.append("\n"+MyArStation.mGameMain.getResources().getString(R.string.Return_LoginScreen_tip_Content));
			SpannableStringBuilder style=new SpannableStringBuilder(temp);   
			style.setSpan(new ForegroundColorSpan(0xff99cccc), 0, len ,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
			content.setText(style);
			Button commit = (Button)dialog.findViewById(R.id.ok);
			commit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
//					messageEvent(MessageEventID.EMESSAGE_EVENT_TO_LOGIN);
					MyArStation.mGameMain.quit();
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
				}
			});
			Button cancle = (Button)dialog.findViewById(R.id.cancel);
			cancle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					dialog.dismiss();
				}
			});
		}
//		dialog = new AlertDialog.Builder(GameMain.mGameMain).setTitle(R.string.Return_LoginScreen_tip_title).setMessage(
//				GameMain.mGameMain.getString(R.string.Return_LoginScreen_tip_Content)).setCancelable(false)
//				.setPositiveButton(R.string.Return_LoginScreen_Ok, new DialogInterface.OnClickListener()
//				{
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
////								hideSysCom();
//								messageEvent(MessageEventID.EMESSAGE_EVENT_TO_LOGIN);
////								GameCanvas.getInstance().changeView(new LoginScreen());
//								MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
//					}
//				}).setNegativeButton(R.string.Return_LoginScreen_Cancel, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
//					}
//				}).create();
		dialog.show();
//		dialog.setCancelable(true);
	}
	
	private void requestMainPage() {
//		MBRspLogin login = (MBRspLogin)GameMain.iGameProtocol.getMobileMsgCS(RspMsg.MSG_RSP_LOGIN);
//		MBRspVisitorLogin visitorlogin = (MBRspVisitorLogin)GameMain.iGameProtocol.getMobileMsgCS(RspMsg.MSG_RSP_VISITORLOGIN);
		int userid = MyArStation.iPlayer.iUserID;
//		if(login != null ) {
		MyArStation.iGameProtocol.requsetMainPageB(userid);
//			messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_ShowMainPageLoading);
//		}
	}
	
	private void initNoticeBoard() {
//		iNoticeDialog = new NoticeDialog(GameMain.getInstance());
	}
	
	private void showNewNotice() {
		if(dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
//		iNoticeDialog.upNotice(iNocticeList);
//		iNoticeDialog.show();
	}
	
	private void showAllNotice() {
//		iNoticeDialog.upNotice(ActivityUtil.iMsgList);
//		iNoticeDialog.show();
	}
	
	public void colseNotice() {
//		if(iNoticeDialog != null && iNoticeDialog.isShowing()) {
//			iNoticeDialog.dismiss();
//		}
	}
	
	public void showLeaveMsg() {
		for(int i = 0; iSpriteButtonList != null && i < iSpriteButtonList.size(); i++) {
			SpriteButton button = iSpriteButtonList.get(i);
			int eventid = button.getEventID();
			if(GameEventID.ESPRITEBUTTON_EVENT_HOME_FRIEND == eventid || GameEventID.ESPRITEBUTTON_EVENT_MORE_FRIEND == eventid) {
				if(MyArStation.iPlayer.iLeaveMsgCount > 0) {
					button.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				}
				else {
					button.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				}
				break;
			}
		}
	}
	/**
	 * description 活动进场提示
	 */
	private void showMotionStart(final MBNotifyActivityStart e) {
		if(concuorDialog != null && concuorDialog.isShowing()) {
			return;
		}
		int tStrID = R.string.Motion_Start_Content;//相应提示
		String str = Context.getString(tStrID);
		if(e != null) {
			str = e.iMsg;
		}
		concuorDialog.show(new Concours(e.iMsg, 
				e.iActivityType,
				e.iActivityStatus, 
				e.iActivityID,
				e.iActivityRoomID,
				e.iRoomType));
	}
	
	private void requestLoginRoom(RoomData pair) {
		messageEvent(MessageEventID.EMESSAGE_EVENT_TO_PROGRESS, pair);
	}
	
	private void requestActivitEnroll(int aActivityID, int aAction) {
		if(MyArStation.iGameProtocol.requestActivityEnroll(aActivityID, aAction)) {
//			showProgressDialog(R.string.Loading_String);
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.ConcoursDialog.OnConcoursListener#OnJoint()
	 */
	@Override
	public void OnJoint(Concours concours) {
		RoomData pair = new RoomData(concours.iActivityRoomID, -1, concours.iRoomType);
		requestLoginRoom(pair);
	}

	/* 
	 * @see com.funoble.lyingdice.view.ConcoursDialog.OnConcoursListener#OnAbort()
	 */
	@Override
	public void OnAbort(Concours concours) {
		requestActivitEnroll(concours.iActivityID, ActivityAction.ABORT);
	}

	/* 
	 * @see com.funoble.lyingdice.view.ConcoursDialog.OnConcoursListener#OnWait()
	 */
	@Override
	public void OnWait(Concours concours) {
		requestActivitEnroll(concours.iActivityID, ActivityAction.HANGUP);
	}
	
	private void requestGiftList() {
		MyArStation.iGameProtocol.RequestGiftList(MyArStation.iPlayer.iUserID);
	}

	/* 
	 * @see com.funoble.lyingdice.screen.QuickSelectRoomScreen.OnSelectRoomListen#OnSelectRoom(com.funoble.lyingdice.data.cach.RoomData)
	 */
	@Override
	public void OnSelectRoom(RoomData data) {
		iRoomData = data;
		if(iRoomData != null) {
			iGameState = 25;
		}
//		selectRoomScreen.dimiss();
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen.OnAnimateListen#onEntering()
	 */
	@Override
	public void onEntering() {
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen.OnAnimateListen#onEntered()
	 */
	@Override
	public void onEntered() {
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen.OnAnimateListen#onExiting()
	 */
	@Override
	public void onExiting() {
		iSbTwoY -= iSbTwoSpeed;
		if(iSbTwoY <= 0) {
			iSbTwoY = 0;
		}
		spriteButtonPerform();
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen.OnAnimateListen#onExited()
	 */
	@Override
	public void onExited() {
		iGameState = 24;
	}
}
