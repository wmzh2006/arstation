package com.funoble.myarstation.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.common.Util;
import com.funoble.myarstation.data.cach.MessageData;
import com.funoble.myarstation.data.cach.RoomData;
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
import com.funoble.myarstation.gamelogic.ShakeListener;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.gamelogic.SpriteButtonGift;
import com.funoble.myarstation.gamelogic.ShakeListener.OnShakeListener;
import com.funoble.myarstation.gamelogic.SpriteButtonGift.OnGiftSelectListen;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.screen.GiftsView.OnClickGiftsListen;
import com.funoble.myarstation.screen.SynAnimation.SynAnimationListen;
import com.funoble.myarstation.socket.protocol.MBNotifyPlayAnimationTwo;
import com.funoble.myarstation.socket.protocol.MBRspAddFriend;
import com.funoble.myarstation.socket.protocol.MBRspDing;
import com.funoble.myarstation.socket.protocol.MBRspGiftFriendGoods;
import com.funoble.myarstation.socket.protocol.MBRspGiftFriendGoodsTwo;
import com.funoble.myarstation.socket.protocol.MBRspHart;
import com.funoble.myarstation.socket.protocol.MBRspHomeGiftList;
import com.funoble.myarstation.socket.protocol.MBRspHomeReceiveGiftList;
import com.funoble.myarstation.socket.protocol.MBRspLeaveMsgB;
import com.funoble.myarstation.socket.protocol.MBRspLeaveMsgListB;
import com.funoble.myarstation.socket.protocol.MBRspPlayerInfoFour;
import com.funoble.myarstation.socket.protocol.MBRspPlayerInfoThree;
import com.funoble.myarstation.socket.protocol.MBRspShakaFriend;
import com.funoble.myarstation.socket.protocol.MBRspTrace;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.NotifyMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.store.data.AnimFormatData;
import com.funoble.myarstation.store.data.GiftData;
import com.funoble.myarstation.store.data.GiveGiftData;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.GiftUtil;
import com.funoble.myarstation.utils.Graphics;
import com.funoble.myarstation.utils.TPoint;
import com.funoble.myarstation.view.GameEventID;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.GeneralDialog;
import com.funoble.myarstation.view.InfoPopView;
import com.funoble.myarstation.view.MessageDialog;
import com.funoble.myarstation.view.MessageEventID;
import com.funoble.myarstation.view.GeneralDialog.OnGeneralDialogListener;

public class FriendDetailScreen extends GameView implements GameButtonHandler, OnShakeListener,
															OnGeneralDialogListener,
															OnClickGiftsListen,
															SynAnimationListen {
	
	private final int HEARTOFFSET = 10000;
	private final int ONESUNINDEX = 25;
	private final int MAX_HART_SPRITE = 16;	//飞心动画的个数
	
	private GameCanvas iGameCanvas = null;
	
	//摇手机监听器
	private ShakeListener iShakeListener;
	
	private int	iGameState = 0;	//0 --- 载入数据     1 --- 各面板进场    2 --- 正常   3 --- 退回主界面  4--刷新界面
	private boolean createBMP = false;
	private long iGameTick = 0;
	private float iSppX = 0;
	private float iSoX = 0;
	private float iSbY = 0;
	private int iSppSpeed = 0;
	private int iSoSpeed = 0;
	private int iSbSpeed = 0;
	
	private Project iUIPak = null;
	private Project iMyHomePak = null;
	private Scene iSceneMyHome = null;
//	private Project iCarPak = null;
//	private Scene iScene = null;
	private Scene iSceneLoading = null;
	private Scene iScenePlayerPhoto = null;
	private Scene iSceneOperate = null;
	private Scene iSceneBar = null;
	private Vector<SpriteButton> iSpriteButtonList = new Vector<SpriteButton>();//精灵按钮 列表
	private Vector<SpriteButton> iSpriteButtonListBar = new Vector<SpriteButton>();//精灵按钮 列表
	private Vector<SpriteButton> iSpriteButtonListPhoto = new Vector<SpriteButton>();//精灵按钮 列表
	private Vector<SpriteButtonGift> iGiftSpriteButtons = new Vector<SpriteButtonGift>();//精灵按钮 列表
//	private CBuilding iDiceSprite = null; 	//骰子动画
	private CBuilding iVipSprite = null;	//VIP动画
//	private CBuilding iCardSprite = null;	//酒水卡动画
//	private SpriteButton iShakeButton = null;
	private SpriteButton iTraceButton = null;
	private SpriteButton iLeaveMsgButton = null;
//	private Sprite iCarSprite = null;
	private Sprite iSkillZhaiSprite = null;	//斋技能的图标
	private Sprite iSkillLightnSprite = null; //雷劈技能的图标
	private CBuilding iPhotoBoxSprite = null;  //相框
	private CBuilding iShakeButton = null;  //摇酒图标
	private CBuilding iBalloonSprite = null;	//气球
	private CBuilding iBalloonSunSprite = null;	//气球
	private CBuilding iSexSprite = null;	//性别
	
	private Sprite iDrunkSprite = null;	//酒杯动画
	private NinePatch iNinePatch = null; //经验条
	private NinePatch iExpBmp = null;
	private Bitmap iBackBmp = null;	//背景图
	
	private Paint iLovePaint;//顶 画笔
	
	private Scene iPopScene = null;//顶层ui
	private Vector<SpriteButton> iPopSBList = new Vector<SpriteButton>();//顶层 精灵按钮 列表
//	private boolean ibDrawLoading = false;//是否要画loading
	public Project iPlayerPak;//角色pak
	public Project iPlayerHPHomePak;//醉酒度
	//玩家信息
	private int iAction;	//
	private int iSecond;	//还有多少秒才可以摇
	private int iOnLine;	//是否在线 0 --- 不在线， 1 --- 在线， 2 --- 在游戏中
	private Paint iExpPaint;
	private RectF iExpRect;
	private RectF iIntimateRect;
	private Bitmap iPicBMP = null;//照片图片 原图
	private Bitmap iPicBMPZoom = null;//照片图片 缩放图
	private float iPicX = -1000*ActivityUtil.ZOOM_X;
	private float iPicY = -1000*ActivityUtil.ZOOM_Y;
	private float iCarPicX = -1000*ActivityUtil.ZOOM_X;
	private float iCarPicY = -1000*ActivityUtil.ZOOM_Y;
//	private Bitmap iHeadBMP;//头像图片
	private Paint iLoadingPaint;//
	private Bitmap iCarBMP = null;	//汽车头像
	private Paint iDrunkPaint;
	
	//界面调整相关
	public static final int SCREENTYPE_RANK = 0;
	public static final int SCREENTYPE_FRIENDLIST = 1;
	
	private int iUserID = 0;
	private int[] iPosition;
	private int iRankType = 0;
	private int iScreanType = SCREENTYPE_FRIENDLIST;
	private CPlayer iPlayer = null;
	
	//飞心动画
	private float[] iHartX;
	private float[] iHartY;
	private int[] iHartStartX;
	private int[] iHartStartY;
	private int[] iHartEndX;
	private int[] iHartEndY;
	private float[] iHartLineK;
	private float[] iHartSpeed;
	private int[] iHartLifeTick;	//飞心的生命周期
	private Sprite iHartSprite = null;	//飞心动画
	private Sprite iBeDingSprite = null; //被顶动画
	
	//飘心动画
	private Vector<Sprite> iHartSpriteList = null; //飘心动画
	private GeneralDialog	iDelriendDialog; //
	private GeneralDialog	iGiveVIPdDialog; //
	private MessageDialog   messageDialog;
	private InfoPopView     infoPopView;
	private GiftsView      giftsView;
	private Vector<GiftData> iGiftDatas = new Vector<GiftData>();
	private ArrayList<Integer> iGiftIDs = new ArrayList<Integer>();
	//
	SynAnimation synAnimation  = null;
	public FriendDetailScreen() {
		
	}
	
	public FriendDetailScreen(int aUserID, int aPosition) {
		iUserID = aUserID;
		iPosition = new int[1];
		iPosition[0] = aPosition;
	}
	
	public FriendDetailScreen(int aUserID, int[] aPosition, int aScreenType, int aRankType) {
		iUserID = aUserID;
		iPosition = aPosition;
		iScreanType = aScreenType;
		iRankType = aRankType;
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
		iShakeListener = new ShakeListener(MyArStation.getInstance().getApplicationContext());
		iShakeListener.setOnShakeListener(this);
		//
		iPlayer = new CPlayer();
		//
		iExpRect = new RectF();
		//
		iIntimateRect = new RectF();
		//
		iAction = 0;
		iSecond = 0;
		iOnLine = 0;
		//
		setExpRect();
		initPaint();
		loadUIPak();
		//载入经验图片
		Bitmap bitmap = BitmapFactory.decodeResource(MyArStation.mGameMain.getResources(), R.drawable.exp3);
		iNinePatch = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
		bitmap = BitmapFactory.decodeResource(MyArStation.mGameMain.getResources(), R.drawable.exp2);
		iExpBmp = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
		bitmap = null;
		//
		iBackBmp = Bitmap.createBitmap(ActivityUtil.SCREEN_WIDTH, ActivityUtil.SCREEN_HEIGHT, Config.ARGB_8888);
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
		//飞心动画
		iHartX = new float[MAX_HART_SPRITE];
		iHartY = new float[MAX_HART_SPRITE];
		iHartStartX = new int[MAX_HART_SPRITE];
		iHartStartY = new int[MAX_HART_SPRITE];
		iHartEndX = new int[MAX_HART_SPRITE];
		iHartEndY = new int[MAX_HART_SPRITE];
		iHartLineK = new float[MAX_HART_SPRITE];
		iHartSpeed = new float[MAX_HART_SPRITE];
		iHartLifeTick = new int[MAX_HART_SPRITE];	//飞心的生命周期
		//
		iHartSpriteList = new Vector<Sprite>(); //飘心动画
		infoPopView = new InfoPopView(1);
		infoPopView.init();
		giftsView = new GiftsView();
		giftsView.init();
		giftsView.setOnClickGiftListen(this);
		synAnimation = new SynAnimation();
		synAnimation.init();
		//
		requestPlayerInfo();
		MyArStation.iGameProtocol.requestHomeReveiceGiftList(iUserID);
		messageEvent(GameEventID.ESPRITEBUTTON_EVENT_FRIEND_INITSTSTEMCOM);
	}

	@Override
	public void releaseResource() {
		if(infoPopView != null) infoPopView.releaseResource();
		if(giftsView != null) giftsView.releaseResource();
		iHartSprite = null;	//飞心动画
		iBeDingSprite = null; //被顶动画
		iHartSpriteList = null; //飘心动画
		iHartX = null;
		iHartY = null;
		iHartStartX = null;
		iHartStartY = null;
		iHartEndX = null;
		iHartEndY = null;
		iHartLineK = null;
		iHartSpeed = null;
		iHartLifeTick = null;	//飞心的生命周期	
		iBackBmp = null;
		iExpBmp = null;
		iNinePatch = null;
		iSkillLightnSprite = null; //雷劈技能的图标
		iSkillZhaiSprite = null;	//斋技能的图标
//		iCarSprite = null;
		iShakeButton = null;
		iTraceButton = null;
		iLeaveMsgButton = null;
//		iCarPak = null;
		iPhotoBoxSprite = null;  //相框
		iBalloonSprite = null;	//气球
		iBalloonSunSprite = null;	//气球
		iSexSprite = null;	//性别
		iUIPak = null;
		iMyHomePak = null;
		iSceneMyHome = null;
		if (iPlayerPak != null) {
			iPlayerPak.releaseSelf();
		}
		iPlayerPak = null;
		if (iPlayerHPHomePak != null) {
			iPlayerHPHomePak.releaseSelf();
		}
		iPlayerHPHomePak = null;
		if (iPicBMP != null) {
			iPicBMP.recycle();
			iPicBMP = null;
		}
		if (iPicBMPZoom != null) {
			iPicBMPZoom.recycle();
			iPicBMPZoom = null;
		}
//		if (iHeadBMP != null) {
//			iHeadBMP.recycle();
//			iHeadBMP = null;
//		}
		if(iCarBMP != null) {
			iCarBMP.recycle();
			iCarBMP = null;
		}
		iPlayer = null;
		iPosition = null;
		if(iShakeListener != null) {
			iShakeListener.stopShake();
			iShakeListener = null;
		}
		if(iGiveVIPdDialog != null) {
			iGiveVIPdDialog.dismiss();
			iGiveVIPdDialog = null;
		}
		if(iDelriendDialog != null) {
			iDelriendDialog.dismiss();
			iDelriendDialog = null;
		}
		if(messageDialog != null) {
			messageDialog.dismiss();
			messageDialog = null;
		}
		iGiftIDs = null;
		iGiftSpriteButtons = null;
	}

	//飞心动画
	private void paintHartSprite(Canvas g) {
		//飞心动画
		for(int i=0; i<MAX_HART_SPRITE; i++) {
			if(iHartLifeTick[i] <= 0) {
				continue;
			}
			iHartSprite.paint(g, (int)iHartX[i], (int)iHartY[i]);
		}
		//飘心动画
		for(int i=0; i<iHartSpriteList.size(); i++) {
			Sprite tempSprite = iHartSpriteList.get(i);
			tempSprite.paintStatic(g, 0, tempSprite.getX(), tempSprite.getY());
		}
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
				if (iPicBMP != null) {
					g.drawBitmap(iPicBMP, iSppX+iPicX, iPicY, null);
				}
				if (iPicBMPZoom != null) {
					g.drawBitmap(iPicBMPZoom, iSppX+iPicX, iPicY, null);
				}
				
				iScenePlayerPhoto.iCameraX = 0;
				iScenePlayerPhoto.paint(g, 0, 0);
//				//摇好友信息
				if(iShakeButton != null) {
					if(iShakeButton.getActionID() == 1
							&& iPlayer.iCurrentDrunk > 0) {
						int tMinute = iSecond / 60;
						int tSecond = iSecond % 60;
						String tInfo = "";
						if(tMinute < 10) {
							tInfo += "0";
						}
						tInfo += tMinute;
						tInfo += ":";
						if(tSecond < 10) {
							tInfo += "0";
						}
						tInfo += tSecond;
						g.drawText(tInfo, iSppX+724*ActivityUtil.ZOOM_X, 296*ActivityUtil.ZOOM_Y, iDrunkPaint);
					}
				}
				for(int i=0; i<iSpriteButtonListPhoto.size(); i++) {
					((SpriteButton)iSpriteButtonListPhoto.elementAt(i)).paint(g, iScenePlayerPhoto.iCameraX, iScenePlayerPhoto.iCameraY);
				}
			}
			if(iSceneOperate != null) {
			}
			//关系称号
			if(iPlayer.iVipLevel > 0) {
				g.drawText(""+iPlayer.stUserNick, 228*ActivityUtil.ZOOM_X, 182*ActivityUtil.ZOOM_Y, ActivityUtil.mVipNickPaint);
			}
			else {
				g.drawText(""+iPlayer.stUserNick, 228*ActivityUtil.ZOOM_X, 182*ActivityUtil.ZOOM_Y, ActivityUtil.mNormalNickPaint);
			}
			//
			for(int i=0; i<iSpriteButtonList.size(); i++) {
				((SpriteButton)iSpriteButtonList.elementAt(i)).paint(g, iSceneOperate.iCameraX, iSceneOperate.iCameraY);
			}
			if(iSceneBar != null) {
				//
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).paint(g, iSceneBar.iCameraX, iSceneBar.iCameraY);
				}
			}
			for(int i = 0; i <  iGiftSpriteButtons.size(); i++) {
				iGiftSpriteButtons.get(i).paint(g);
			}
			//飞心动画
			paintHartSprite(g);
			if(infoPopView != null) infoPopView.paintScreen(g, paint);
			if(giftsView != null) giftsView.paintScreen(g, paint);
			if(synAnimation != null) synAnimation.paintScreen(g, paint);
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
			}
		}		
	}

	@Override
	public void performL() {
		if(iGameState == 2 && createBMP) {
			createBMP = false;
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
//				createBMP = true;
			}
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
				iSceneBar.iCameraY = -(int)iSbY;
				//
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).perform();
				}
			}
		}
		//正常
		else if(iGameState == 2) {
			if(synAnimation != null) synAnimation.performL();
			if(infoPopView != null) infoPopView.performL();
			if(giftsView != null) giftsView.performL();
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
			if(iSceneOperate != null) {
				iSceneOperate.handle();
				iSceneOperate.iCameraX = 0;
				//
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).perform();
				}
			}
			if(iSceneBar != null) {
				iSceneBar.handle();
				iSceneBar.iCameraX = 0;
				//
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).perform();
				}
			}
			//飞心动画
			hartSpritePerform();
			//飘心动画
			loveSpritePerform();
			//
			if(iSecond > 0) {
				long tTick = System.currentTimeMillis();
				if(tTick > iGameTick + 1000) {
					iGameTick = tTick;
					iSecond --;
				}
				if(iSecond <= 0) {
					if(iShakeButton != null) {
						iShakeButton.setActionID(0);
						iShakeListener.startShake();
					}
				}
			}
			for(int i = 0; iGiftSpriteButtons != null && i < iGiftSpriteButtons.size(); i++) {
				iGiftSpriteButtons.get(i).perform();
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
				iSceneBar.iCameraY = -(int)iSbY;
				//
				for(int i=0; i<iSpriteButtonListBar.size(); i++) {
					((SpriteButton)iSpriteButtonListBar.elementAt(i)).perform();
				}
			}			
			if(iSppX >= 500 && iSoX >= 500 && iSbY >= 100) {
				//切换到好友界面
				if(iScreanType == SCREENTYPE_FRIENDLIST) {
					iGameCanvas.changeView(new FriendInfoScreen(iUserID, iPosition[0]));
				}
				else if(iScreanType == SCREENTYPE_RANK){
					iGameCanvas.changeView(new RankScreen(iUserID,iPosition, iRankType));
				}
			}
		}
		else if(iGameState == 4) {
			creatBackBmp();
			iGameState = 2;
			createBMP = true;
		}
	}
	
	//飞心动画
	private void hartSpritePerform() {
		//飞心动画
		for(int i=0; i<MAX_HART_SPRITE; i++) {
			if(iHartLifeTick[i] <= 0) {
				continue;
			}
			iHartLifeTick[i] --;
			if(iHartStartX[i] == iHartEndX[i]) {
				iHartY[i] += iHartSpeed[i];
			}
			else if(iHartStartY[i] == iHartEndY[i]) {
				iHartX[i] += iHartSpeed[i];
			}
			else {
				iHartX[i] += iHartSpeed[i];
				iHartY[i] = (int)((iHartX[i]-iHartStartX[i]) * iHartLineK[i]) + iHartStartY[i];
			}
			//触发飘心动画
			if(iHartLifeTick[i] == 0) {
				//
				Sprite pSprite = new Sprite();
				pSprite.copyFrom(iBeDingSprite);
				pSprite.setPosition((short)iHartX[i], (short)iHartY[i]);
				iHartSpriteList.addElement(pSprite);
				//
				//更新好友信息
				iPlayer.iDingCount ++;
				if(iPlayer.iIntimate > iPlayer.iNextIntimate) {
					iPlayer.iIntimate = iPlayer.iNextIntimate;
					requestPlayerInfo();
				}
				setIntimateExp();
			}
		}
	}
	
	//飘心动画
	private void loveSpritePerform() {
		for(int i=0; i<iHartSpriteList.size(); i++) {
			Sprite pSprite = iHartSpriteList.get(i);
			if(pSprite.performAction(0) == true) {
				pSprite = null;
				iHartSpriteList.remove(i);
				i --;
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
//		if(iScenePlayerPhoto != null) {
//			if (iPicBMP != null) {
//				g.drawBitmap(iPicBMP, iSppX+iPicX, iPicY, null);
//			}
//			if (iPicBMPZoom != null) {
//				g.drawBitmap(iPicBMPZoom, iSppX+iPicX, iPicY, null);
//			}
//			
//			iScenePlayerPhoto.iCameraX = 0;
//			iScenePlayerPhoto.paint(g, 0, 0);
////			//昵称
////			if(iPlayer.iVipLevel > 0) {
////				g.drawText(""+iPlayer.stUserNick, iSppX+38*ActivityUtil.ZOOM_X, 56*ActivityUtil.ZOOM_Y, ActivityUtil.mVipNickPaint);
////			}
////			else {
////				g.drawText(""+iPlayer.stUserNick, iSppX+38*ActivityUtil.ZOOM_X, 56*ActivityUtil.ZOOM_Y, ActivityUtil.mCallPaint);
////			}
////			//财富榜
////			if (iPlayer.iWealthRank > 0) {
////				g.drawText("财富榜  "+iPlayer.iWealthRank, iSppX+306*ActivityUtil.ZOOM_X, 108*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
////			}
////			else {
////				g.drawText("财富榜  ??", iSppX+306*ActivityUtil.ZOOM_X, 108*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
////			}
////			//等级榜
////			if (iPlayer.iLevelRank > 0) {
////				g.drawText("等级榜  "+iPlayer.iLevelRank, iSppX+306*ActivityUtil.ZOOM_X, 138*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
////			}
////			else {
////				g.drawText("等级榜  ??", iSppX+306*ActivityUtil.ZOOM_X, 138*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
////			}
////			//魅力榜
////			if (iPlayer.iVictoryRank > 0) {
////				g.drawText("魅力榜  "+iPlayer.iVictoryRank, iSppX+306*ActivityUtil.ZOOM_X, 168*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
////			}
////			else {
////				g.drawText("魅力榜  ??", iSppX+306*ActivityUtil.ZOOM_X, 168*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
////			}
////			//灌倒
////			if(iPlayer.iReward <= 99999) {
////				g.drawText("灌倒 "+iPlayer.iReward, iSppX+306*ActivityUtil.ZOOM_X, 198*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
////			}
////			else {
////				g.drawText("灌倒 "+iPlayer.iReward/10000+"万", iSppX+306*ActivityUtil.ZOOM_X, 198*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
////			}
////			//胜场
////			if(iPlayer.iWinCount <= 99999) {
////				g.drawText("胜场 "+iPlayer.iWinCount, iSppX+306*ActivityUtil.ZOOM_X, 228*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
////			}
////			else {
////				g.drawText("胜场 "+iPlayer.iWinCount/10000+"万", iSppX+306*ActivityUtil.ZOOM_X, 228*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
////			}
////			//连击
////			g.drawText("连击 "+iPlayer.iAchievement, iSppX+306*ActivityUtil.ZOOM_X, 258*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
////			//
////			Rect tempRect = new Rect();
//			//亲密度
////			if(iIntimateRect.width() >= 8) {
////				tempRect.set((int)(iIntimateRect.left+iSbX), (int)(iIntimateRect.top), (int)(iIntimateRect.right+iSbX), (int)(iIntimateRect.bottom));
////				iNinePatch.draw(g, tempRect);
////			}
//			//关系称号
////			g.drawText(iPlayer.iFriendTitle, iSbX+278*ActivityUtil.ZOOM_X, 416*ActivityUtil.ZOOM_Y, ActivityUtil.mTitlePaint);
//			//职称
////			if(iExpRect.width() > 0) {
////				tempRect.set((int)(iExpRect.left+iSppX), (int)(iExpRect.top), (int)(iExpRect.right+iSppX), (int)(iExpRect.bottom));
////				iNinePatch.draw(g, tempRect);
////			}
//			//职称文字
////			g.drawText(iPlayer.iTitle, iSppX+188*ActivityUtil.ZOOM_X, 443*ActivityUtil.ZOOM_Y, ActivityUtil.mTitlePaint);
//			//
//			//凤币
////			g.drawText(""+iPlayer.iMoney, iSbX+208*ActivityUtil.ZOOM_X, 416*ActivityUtil.ZOOM_Y, ActivityUtil.mMoneyPaint);
//			//金币
////			String tInf = ""+iPlayer.iGb;
////			if(iPlayer.iGb > 99999999) {
////				tInf = "" + (iPlayer.iGb / 10000);
////				tInf += "万";
////			}
////			g.drawText(tInf, iSppX+218*ActivityUtil.ZOOM_X, 368*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
//		}
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
//				if(iCarSprite != null) {
//					iCarSprite.paintAction(g, 0, (int)(iSoX+698*ActivityUtil.ZOOM_X), (int)(288*ActivityUtil.ZOOM_Y));
//				}
				if (iCarBMP != null) {
					g.drawBitmap(iCarBMP, iSoX+iCarPicX, iCarPicY, null);
				}
//				tInf = "座驾：";
//				g.drawText(tInf, iSoX+468*ActivityUtil.ZOOM_X, 268*ActivityUtil.ZOOM_Y, ActivityUtil.mBeatDownPaint);
			}
//		}
		if(iSceneBar != null) {
			iSceneBar.iCameraX = 0;
			iSceneBar.paint(g, 0, 0);
		}
		g = null;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(infoPopView != null && infoPopView.onKeyDown(keyCode, event)) return true;
		if(giftsView != null && giftsView.onKeyDown(keyCode, event)) return true;
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (iPopScene != null) {
				//有弹出界面
				closePopScene();
			}
			else {
				//返回
				iGameState = 3;
			}
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
		if(giftsView != null && giftsView.pointerPressed(aX, aY)) return true;
		boolean result= false;
		result = pointerPressedForPop(aX, aY);
		if (result) {
			return result;
		}
		Rect pRect = new Rect();
		SpriteButton pSB;
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
						ItemAction(GameEventID.ESPRITEBUTTON_EVENT_FRIEND_ZOOM);
						result = true;
					}
				}
			}
		}
		return result;
	}

	private boolean pointerPressedForPop(int aX, int aY) {
		boolean result= false;
//		Rect pRect = new Rect();
//		SpriteButton pSB;
//		for(int i=0; i<iPopSBList.size(); i++) {
//			pSB = ((SpriteButton)iPopSBList.elementAt(i));
//			if(!pSB.isVision()) {
//				continue;
//			}
//			int pX = pSB.getX();
//			int pY = pSB.getY();
//			Rect pLogicRect = pSB.getRect();
//			pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
//					pX+pLogicRect.right, pY+pLogicRect.bottom);
//			if (pRect.contains(aX, aY)) {
//				pSB.pressed();
//				System.out.println("ProgressScreen pointerPressedForPop  pSB.pressed() ");
//				result = true;
//				break;
//			}
//		}
//		if (!result && iPopScene != null) {
//			//在弹出界面 而且没点到任何按钮
//			result = true;
//		}
		return result;
	}
	
	@Override
	public boolean pointerReleased(int aX, int aY) {
		boolean result= true;
//		result = pointerReleasedForPop(aX, aY);
//		if (result) {
//			return result;
//		}
//		Rect pRect = new Rect();
//		SpriteButton pSB;
//		for(int i=0; i<iSpriteButtonList.size(); i++) {
//			pSB = ((SpriteButton)iSpriteButtonList.elementAt(i));
//			if(!pSB.isVision()) {
//				continue;
//			}
//			int pX = pSB.getX();
//			int pY = pSB.getY();
//			Rect pLogicRect = pSB.getRect();
////			pRect.SetRect(TPoint(pX+pLogicRect.iTl.iX, pY+pLogicRect.iTl.iY),
////					TSize(pLogicRect.Size().iWidth, pLogicRect.Size().iHeight));
//			pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
//					pX+pLogicRect.right, pY+pLogicRect.bottom);
//			if (pRect.contains(aX, aY)) {
//				pSB.released();
//				ItemAction(null, pSB.getEventID());
//				result = true;
//				MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//				break;
//			}
//		}
		return result;
	}
	
	private boolean pointerReleasedForPop(int aX, int aY) {
		boolean result= false;
//		Rect pRect = new Rect();
//		SpriteButton pSB;
//		for(int i=0; i<iPopSBList.size(); i++) {
//			pSB = ((SpriteButton)iPopSBList.elementAt(i));
//			if(!pSB.isVision()) {
//				continue;
//			}
//			int pX = pSB.getX();
//			int pY = pSB.getY();
//			Rect pLogicRect = pSB.getRect();
//			pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
//					pX+pLogicRect.right, pY+pLogicRect.bottom);
//			if (pRect.contains(aX, aY)) {
//				pSB.released();
//				ItemAction(null, pSB.getEventID());
//				result = true;
//				MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//				break;
//			}
//		}
//		if (!result && iPopScene != null) {
//			//在弹出界面 而且没点到任何按钮
//			//先标记该事件 由弹出界面处理
//			result = true;
//			switch (iPopScene.getIndex()) {
//			default:
//				closePopScene();
//				break;
//			}
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//		}
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
		if(giftsView != null) giftsView.ItemAction(aEventID);
		switch (aEventID) {
		case GameEventID.ESPRITEBUTTON_EVENT_FRIEND_DETAIL_RETURN://返回
			{
				iGameState = 3;
			}
			return true;
			
		case GameEventID.ESPRITEBUTTON_EVENT_FRIEND_DETAIL_DING:
			{
				requestDing(iUserID);
			}
			return true;
			
		case GameEventID.ESPRITEBUTTON_EVENT_FRIEND_DETAIL_TRACE:
			{
				requestTrace(iUserID);
			}
			return true;
			
		case GameEventID.ESPRITEBUTTON_EVENT_FRIEND_DETAIL_SHAKE:
			{
				requestShake(iUserID);
			}
			return true;
			
		case GameEventID.ESPRITEBUTTON_EVENT_FRIEND_DETAIL_DELETE:
			{
				messageEvent(GameEventID.ESPRITEBUTTON_EVENT_FRIEND_DETAIL_DELFRIEND);
			}
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_FRIEND_GIVE_VIP:
		{
//			messageEvent(GameEventID.ESPRITEBUTTON_EVENT_FRIEND_GIVE_VIP_CONFIRM);
			if(iGiftDatas.size() <= 0) {
				MyArStation.iGameProtocol.requestHomeGiftList(0);
			}
			else {
				if(giftsView != null)giftsView.show();
			}
		}
		return true;
		
		case GameEventID.ESPRITEBUTTON_EVENT_FRIEND_LEAVEMSG:
		{
			messageEvent(GameEventID.ESPRITEBUTTON_EVENT_FRIEND_LEAVEMSG);
		}
		return true;
		case GameEventID.ESPRITEBUTTON_EVENT_FRIEND_DETAIL_INFO:
		{
			if(infoPopView != null) {
				infoPopView.setPlayerInfo(iPlayer);
				infoPopView.show();
			}
		}
		return true;
		case GameEventID.ESPRITEBUTTON_EVENT_FRIEND_ZOOM:
			GameCanvas.getInstance().changeView(new AlbumScreen(2, iPlayer, iUserID, iPosition[0]));
			break;
		default:
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
			float tPicBGW = ((ActivityUtil.INFO_PIC_BG_W_STANDARD-4)*ActivityUtil.ZOOM_X);//图片底板限制宽
			float tPicBGH = ((ActivityUtil.INFO_PIC_BG_H_STANDARD-4)*ActivityUtil.ZOOM_Y);
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
//					System.out.println("HS 宽高都超出 tZoom="+tZoom+", tPicZoomX="+tPicZoomX+", tPicZoomY="+tPicZoomY);
			}
			else if (tSrcW>tPicBGW) {
				tZoom = tPicZoomX;
//					System.out.println("HS 宽超出 tZoom="+tZoom+", tPicZoomX="+tPicZoomX+", tPicZoomY="+tPicZoomY);
			}
			else if (tSrcH>tPicBGH) {
				tZoom = tPicZoomY;
//					System.out.println("HS 高超出 tZoom="+tZoom+", tPicZoomX="+tPicZoomX+", tPicZoomY="+tPicZoomY);
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
//				System.out.println("HS initPic iPicBMPZoom w="+iPicBMPZoom.getWidth()+", h="+iPicBMPZoom.getHeight());
			iCarPicX = tX_Offset*ActivityUtil.ZOOM_X+tW_BG/2*ActivityUtil.ZOOM_X-iPicBMPZoom.getWidth()/2;
			iCarPicY = tY_Offset*ActivityUtil.ZOOM_Y+tH_BG/2*ActivityUtil.ZOOM_Y-iPicBMPZoom.getHeight()/2;
		}
	}	
	
//	//初始化 相片、头像
//	private void initPic() {
//		if (iPlayerLoginInfo != null) {
//			iPicBMP = GameMain.iDownloadManager.getImage(iPlayerLoginInfo.iBigPicName);
//		}
//		int tX_Offset = ActivityUtil.PIC_BG_X_STANDARD;
//		int tY_Offset = ActivityUtil.PIC_BG_Y_STANDARD;
//		int tW_BG = ActivityUtil.PIC_BG_W_STANDARD;
//		int tH_BG = ActivityUtil.PIC_BG_H_STANDARD;
//		int tCutX = 63;//裁剪的起始位置X ，以800x480为基准
//		int tCutY = 4;//裁剪的起始位置Y
//		int tCutW = (int) (74*ActivityUtil.PAK_ZOOM_X);//裁剪的宽
//		int tCutH = (int) (74*ActivityUtil.PAK_ZOOM_Y);//裁剪的高
//		if (iPicBMP == null) {
//			//没有上传的图片，用默认的
//			iPicBMP = BitmapFactory.decodeResource(GameMain.getInstance().getResources(), R.drawable.mypicdef);
//			if (iPicBMP != null) {
//				iPicX = tX_Offset*ActivityUtil.ZOOM_X+tW_BG/2*ActivityUtil.ZOOM_X-iPicBMP.getWidth()/2;
//				iPicY = tY_Offset*ActivityUtil.ZOOM_Y+tH_BG/2*ActivityUtil.ZOOM_Y-iPicBMP.getHeight()/2;
//				switch (ActivityUtil.TYPE_SCREEN) {
//				case ActivityUtil.TYPE_SCREEN_WVGA800:
//					tCutX = (int) (tCutX*ActivityUtil.ZOOM_X);
//					tCutY = (int) (tCutY*ActivityUtil.ZOOM_Y);
//					iHeadBMP = Graphics.zoomBitmap(iPicBMP, tCutW, tCutH);
//					break;
//				case ActivityUtil.TYPE_SCREEN_HVGA:
//					int tSpeW = (int) (44*ActivityUtil.PAK_ZOOM_X);//头像框指定 宽高
//					int tSpeH = (int) (44*ActivityUtil.PAK_ZOOM_Y);
//					float tZoomX = (float)iPicBMP.getWidth()/(float)200;//图片的缩放率
//					float tZoomY = (float)iPicBMP.getHeight()/(float)200;
//					float tZoomHeadX = (float)tSpeW/(float)tCutW;//头像框的缩放率
//					float tZoomHeadY = (float)tSpeH/(float)tCutH;
//					float tOffsetX = (tCutW-tSpeW)*(tZoomX-tZoomHeadX)/2;
//					float tOffsetY = (tCutH-tSpeH)*(tZoomY-tZoomHeadY)/2;
//					tCutX = (int) Math.ceil((tCutX+tOffsetX)*tZoomX);
//					tCutY = (int) Math.ceil((tCutY+tOffsetY)*tZoomY);
//					System.out.println("IS initPic 默认图片 tZoomX="+tZoomX+",tZoomY="+tZoomY+";tCutX="+tCutX+",tCutY="+tCutY);
//					iHeadBMP = Graphics.zoomBitmap(iPicBMP, tSpeW, tSpeH);
//					iHeadX = 285*ActivityUtil.PAK_ZOOM_X;
//					iHeadY = 12*ActivityUtil.PAK_ZOOM_Y;
//					break;
//				default:
//					tCutX = (int) (tCutX*ActivityUtil.ZOOM_X);
//					tCutY = (int) (tCutY*ActivityUtil.ZOOM_Y);
//					iHeadBMP = Graphics.zoomBitmap(iPicBMP, tCutW, tCutH);
//					break;
//				}
//			}
//		}
//		else {
//			float tZoom = 1;//图片 最终缩放率(X、Y方向)
//			float tSrcW = iPicBMP.getWidth();
//			float tSrcH = iPicBMP.getHeight();
//			float tPicBGW = ((ActivityUtil.PIC_BG_W_STANDARD-4)*ActivityUtil.ZOOM_X);//图片底板限制宽
//			float tPicBGH = ((ActivityUtil.PIC_BG_H_STANDARD-4)*ActivityUtil.ZOOM_Y);
//			float tPicZoomX = tPicBGW/tSrcW;
//			float tPicZoomY = tPicBGH/tSrcH;
//			if (tSrcW>tPicBGW && tSrcH>tPicBGH) {
//				//宽高都超出，取缩放率小的
//				tZoom = tPicZoomX<tPicZoomY ? tPicZoomX : tPicZoomY;
////				System.out.println("HS 宽高都超出 tZoom="+tZoom+", tPicZoomX="+tPicZoomX+", tPicZoomY="+tPicZoomY);
//			}
//			else if (tSrcW>tPicBGW) {
//				tZoom = tPicZoomX;
////				System.out.println("HS 宽超出 tZoom="+tZoom+", tPicZoomX="+tPicZoomX+", tPicZoomY="+tPicZoomY);
//			}
//			else if (tSrcH>tPicBGH) {
//				tZoom = tPicZoomY;
////				System.out.println("HS 高超出 tZoom="+tZoom+", tPicZoomX="+tPicZoomX+", tPicZoomY="+tPicZoomY);
//			}
//			int tZoomW = (int) (iPicBMP.getWidth()*tZoom);
//			int tZoomH = (int) (iPicBMP.getHeight()*tZoom);
//			iPicBMPZoom = Graphics.zoomBitmap(iPicBMP, tZoomW, tZoomH);
//			if (iPicBMPZoom != null) {
//				switch (ActivityUtil.TYPE_SCREEN) {
//				case ActivityUtil.TYPE_SCREEN_WVGA800:
//					tCutX = (int) (tCutX*ActivityUtil.ZOOM_X);
//					tCutY = (int) (tCutY*ActivityUtil.ZOOM_Y);
//					iHeadBMP = Graphics.zoomBitmap(iPicBMPZoom, tCutW, tCutH);
//					break;
//				case ActivityUtil.TYPE_SCREEN_HVGA:
//					int tSpeW = (int) (44*ActivityUtil.PAK_ZOOM_X);//头像框指定 宽高
//					int tSpeH = (int) (44*ActivityUtil.PAK_ZOOM_Y);
//					float tZoomX = ActivityUtil.ZOOM_X;//图片的缩放率
//					float tZoomY = ActivityUtil.ZOOM_Y;
//					float tZoomHeadX = (float)tSpeW/(float)tCutW;//头像框的缩放率
//					float tZoomHeadY = (float)tSpeH/(float)tCutH;
//					float tOffsetX = (tCutW-tSpeW)*(tZoomX-tZoomHeadX)/2;
//					float tOffsetY = (tCutH-tSpeH)*(tZoomY-tZoomHeadY)/2;
//					tCutX = (int) Math.ceil((tCutX+tOffsetX)*tZoomX);
//					tCutY = (int) Math.ceil((tCutY+tOffsetY)*tZoomY);
//					System.out.println("IS initPic 指定图片 tZoomX="+tZoomX+",tZoomY="+tZoomY+";tCutX="+tCutX+",tCutY="+tCutY);
//					iHeadBMP = Graphics.zoomBitmap(iPicBMPZoom, tSpeW, tSpeH);
//					iHeadX = 285*ActivityUtil.PAK_ZOOM_X;
//					iHeadY = 12*ActivityUtil.PAK_ZOOM_Y;
//					break;
//				default:
//					tCutX = (int) (tCutX*ActivityUtil.ZOOM_X);
//					tCutY = (int) (tCutY*ActivityUtil.ZOOM_Y);
//					iHeadBMP = Graphics.zoomBitmap(iPicBMPZoom, tCutW, tCutH);
//					break;
//				}
//			}
//			if (iPicBMPZoom != iPicBMP) {
//				//如果是不一样内存的，iPicBMPZoom是新创建的，先释放iPicBMP的内存
//				iPicBMP.recycle();
//			}
//			iPicBMP = null;
//			iPicX = tX_Offset*ActivityUtil.ZOOM_X+tW_BG/2*ActivityUtil.ZOOM_X-iPicBMPZoom.getWidth()/2;
//			iPicY = tY_Offset*ActivityUtil.ZOOM_Y+tH_BG/2*ActivityUtil.ZOOM_Y-iPicBMPZoom.getHeight()/2;
//		}
//	}
	
	//初始化画笔
	private void initPaint() {
		iExpPaint = new Paint();
		iExpPaint.setColor(Color.MAGENTA);
		iLoadingPaint = new Paint();
		iLoadingPaint.setColor(Color.BLACK);
		iDrunkPaint = new Paint();
		iDrunkPaint.setColor(Color.BLACK);
		iDrunkPaint.setTextSize(ActivityUtil.TEXTSIZE_NORMAL);     //设置字体大小
		iDrunkPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		iDrunkPaint.setTextAlign(Align.CENTER); 	//水平居中
		iLovePaint = new Paint();
		iLovePaint.setTextSize(ActivityUtil.TEXTSIZE_NORMAL);     //设置字体大小
		iLovePaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		iLovePaint.setColor(Color.WHITE);
		iLovePaint.setTextAlign(Align.CENTER); 	//水平居中
	}
	
	//设置经验条
	private void setExpRect() {
		float tW = 298 * ActivityUtil.ZOOM_X;//*iPlayer.iGameExp/100;
		float tLeft = 32 * ActivityUtil.ZOOM_X;
		float tTop = 421 * ActivityUtil.ZOOM_Y;//418
		float tRight = tLeft + tW;
		float tBottom = tTop + 28 * ActivityUtil.ZOOM_Y;
		iExpRect.set(tLeft, tTop, tRight, tBottom);
	}
	
	//设置亲密度经验条
	private void setIntimateExp() {
		float tW = 298 * ActivityUtil.ZOOM_X * iPlayer.iIntimate / iPlayer.iNextIntimate;
		float tLeft = 32 * ActivityUtil.ZOOM_X;
		float tTop = 384 * ActivityUtil.ZOOM_Y;
		float tRight = tLeft + tW;
		float tBottom = tTop + 28 * ActivityUtil.ZOOM_Y;
		iIntimateRect.set(tLeft, tTop, tRight, tBottom);
	}
	
	private void loadUIPak() {
		iUIPak = CPakManager.loadUIPak();//Project.loadProject(ActivityUtil.PATH_SCREEN+"gameshome_map.pak", false);
		iMyHomePak = CPakManager.loadMyHomePak();
		if (iUIPak != null) {
//			iDrunkSprite = iUIPak.getSprite(35);	//酒杯动画
			iHartSprite = iUIPak.getSprite(62);	//飞心动画
			iBeDingSprite = iUIPak.getSprite(59); //被顶动画
		}
		
		if(iMyHomePak != null) {
			iSceneMyHome = iMyHomePak.getScene(0);
			if(iSceneMyHome != null) {
				iSceneMyHome.setViewWindow(0, 0, 800, 480);
				iSceneMyHome.initDrawList();
				//动画
				Vector<?> pSpriteList = iSceneMyHome.getLayoutMap(Scene.BuildLayout).getSpriteList();
				if(pSpriteList.size() >= 2) {
					iBalloonSprite = ((CBuilding)pSpriteList.elementAt(0));//气球动画
					iVipSprite = ((CBuilding)pSpriteList.elementAt(1));	//VIP动画
					iBalloonSunSprite = ((CBuilding)pSpriteList.elementAt(3));//气球动画
					iVipSprite.setActionID(0);
				}
			}
			iScenePlayerPhoto = iMyHomePak.getScene(3);
			if(iScenePlayerPhoto != null) {
				iScenePlayerPhoto.setViewWindow(0, 0, 800, 480);
				iScenePlayerPhoto.initDrawList();
				//
				Vector<?> pSpriteList = iScenePlayerPhoto.getLayoutMap(Scene.BuildLayout).getSpriteList();
				if(pSpriteList.size() >= 1) {
					iPhotoBoxSprite = ((CBuilding)pSpriteList.elementAt(0));//骰子动画
					iShakeButton = ((CBuilding)pSpriteList.elementAt(1));//骰子动画
				}
				
				Map pMap = iScenePlayerPhoto.getLayoutMap(Scene.SpriteLayout);
				pSpriteList = pMap.getSpriteList();
				Sprite pSprite;
				for (int i=0; i<pSpriteList.size(); i++) {
					pSprite = (Sprite)pSpriteList.elementAt(i);
					int pX = pSprite.getX();
					int pY = pSprite.getY();
					int pEventID = GameEventID.ESPRITEBUTTON_EVENT_FRIEND_DETAIL_OFFSET + pSprite.getIndex();
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
				}
			}
			//操作栏
			iSceneBar = iMyHomePak.getScene(4);
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
					int pEventID = GameEventID.ESPRITEBUTTON_EVENT_FRIEND_DETAIL_OFFSET + pSprite.getIndex();
					TPoint pPoint = new TPoint();
					pPoint.iX = pX;
					pPoint.iY = pY;
					//精灵按钮
					SpriteButton pSpriteButton;
					pSpriteButton = new SpriteButton(pSprite);
					pSpriteButton.setEventID(pEventID);
					pSpriteButton.setPosition(pX, pY);
					pSpriteButton.setHandler(this);
					if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_FRIEND_DETAIL_TRACE) {
						iTraceButton = pSpriteButton;
					}
					else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_FRIEND_LEAVEMSG) {
						iLeaveMsgButton = pSpriteButton;
					}
					iSpriteButtonListBar.addElement(pSpriteButton);
				}
			}
		}
		//
		Project tSkillIconPak = CPakManager.loadSkillIconPak();
		if(tSkillIconPak != null) {
			iSkillZhaiSprite = tSkillIconPak.getSprite(0);;	//斋技能的图标
			iSkillLightnSprite = tSkillIconPak.getSprite(1);; //雷劈技能的图标
		}
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

	//--test----
	private void InitGifts() {
		int size = (int) (Math.random() * 150);
		for(int i = 0; i < size/**iGiftIDs.size()*/; i++) {
			Sprite temp = new Sprite();
//			int index = iGiftIDs.get(i) + GiftUtil.GIFTID_1;
			int index = (i % 9) + GiftUtil.GIFTID_1;
			if(index < GiftUtil.GIFTID_1 || index > GiftUtil.GIFTID_9) continue;
			temp.copyFrom(getGiftSprite(index));
			SpriteButtonGift button = new SpriteButtonGift(temp);
			Point point = getNextGiftPostion();
//			Tools.println("i:" + i + " row: " + i % GiftUtil.giftMaxRow + " col: " + col);
			button.setPosition(point.x, point.y);
//			button.iGiftID = iGiftIDs.get(i);
			button.setEventID(i + index);
			button.setOnGiftSelect(new OnGiftSelectListen() {
				
				@Override
				public void OnGiftSelect(int aEventID, GiveGiftData data) {
					for(int i = iGiftSpriteButtons.size() - 1; i >= 0; i--) {
						SpriteButtonGift tButtonGift = iGiftSpriteButtons.get(i);
						if(tButtonGift.getEventID() == aEventID) {
							iGiftSpriteButtons.remove(i);
							break;
						}
					}
				}
			});
			iGiftSpriteButtons.add(button);
		}
	}
	//--test----
	
	private void addGifts(GiveGiftData giftID, boolean drop) {
		int size = iGiftSpriteButtons.size();
		Sprite temp = new Sprite();
		int i = size;
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
		button.setEventID(i + index);
		button.setActionID(SpriteButtonGift.ESPRITE_BUTTON_ACTION_UNABLE);
		iGiftSpriteButtons.add(button);
	}
	
	/* 
	 * @see com.funoble.lyingdice.view.GameView#OnProcessCmd(java.lang.Object, int, int)
	 */
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
		if(infoPopView != null) infoPopView.OnProcessCmd(socket, aMobileType, aMsgID);
//		if(giftsView != null) giftsView.OnProcessCmd(socket, aMobileType, aMsgID);
		MobileMsg pMobileMsg = (MobileMsg) socket;
		switch (aMobileType) {
		case IM.IM_RESPONSE:
		{
			switch (aMsgID) {
			case RspMsg.MSG_RSP_PLAYINFO_THR:
				MBRspPlayerInfoThree rspInfoThree = (MBRspPlayerInfoThree)pMobileMsg.m_pMsgPara;
				if(rspInfoThree == null) {
					break;
				}
				Tools.println(rspInfoThree.toString());
				int type = rspInfoThree.iResult;
				if(type == ResultCode.SUCCESS) {
					//
					iPlayer.stUserNick = rspInfoThree.iNick;
					iPlayer.iRankTotalCount = rspInfoThree.iRankTotalCount; //排行榜总数
					iPlayer.iWealthRank = rspInfoThree.iWealthRank; //财富排名
					iPlayer.iLevelRank = rspInfoThree.iLevelRank; //等级排名
					iPlayer.iVictoryRank = rspInfoThree.iVictoryRank; //胜场排名
					iPlayer.iVipLevel = rspInfoThree.iVipLevel;			//Vip级别	
					iPlayer.iGameLevel = rspInfoThree.iGameLevel;		//级别
					iPlayer.iGameExp = rspInfoThree.iGameExp;			//当前级别的经验
					iPlayer.iCurrentDrunk = rspInfoThree.iCurrentDrunk;	//当前醉酒度
					iPlayer.iWinCount = rspInfoThree.iKillsCount;		//胜
					iPlayer.iFailCount = rspInfoThree.iBeKillsCount;	//负
					iPlayer.iModelID = rspInfoThree.iModelID;			//角色模型ID（为-1时，表示没选择人物）
					iPlayer.iSex = rspInfoThree.iSex;					//性别（为-1时，表示没选择人物）
					iPlayer.iWinning =	rspInfoThree.iWinning;			//胜率
					iPlayer.iReward = rspInfoThree.iReward;				//奖
					iPlayer.iTitle = rspInfoThree.iTitle;				//人物称号
					iPlayer.iGb = rspInfoThree.iGb;						//游戏币
					iPlayer.iMoney = rspInfoThree.iMoney;				//凤智币
					iPlayer.iDingCount = rspInfoThree.iSupportCount;	//支持数
					iPlayer.szBigPicName = rspInfoThree.iBigPicName;
					iPlayer.iAchievement = rspInfoThree.iMaxDrunk;		//最大连击数
					iPlayer.iDiceID = rspInfoThree.nDiceResID;			//骰子ID
					iPlayer.iHasWineCark = rspInfoThree.nHasCard > 0 ? true : false;
					iPlayer.iHasGiftPak = rspInfoThree.nHasLicense > 0 ? true : false;
					iPlayer.iIntimate = rspInfoThree.iFriendExp;		//亲密度
					iPlayer.iNextIntimate = rspInfoThree.iNextFriendExp;//下一级亲密度
					iPlayer.iFriendTitle = rspInfoThree.iFriendTitle;	//关系称号
					iPlayer.iZhaiSkill = rspInfoThree.iZhaiSkill;	// 0 --- 没学   1 --- 喊斋    2 --- 雷劈
					iPlayer.iCarID = rspInfoThree.iCarID;			//车ID
					iPlayer.iHouseID = rspInfoThree.iHouseID;		//房子ID
					iPlayer.iPetID = rspInfoThree.iPetID;			//宠物ID
					iPlayer.iMaxHit = rspInfoThree.iMaxDrunk;		//最大连击数
					setHeart();
					if(iVipSprite != null) {//VIP动画
						iVipSprite.setActionID(iPlayer.iVipLevel);
					}
					if(iPhotoBoxSprite != null) {//醉酒度
						iPhotoBoxSprite.setActionID(iPlayer.iCurrentDrunk);
					}
					if(iSexSprite != null) { //性别
						iSexSprite.setActionID(iPlayer.iSex);
//						System.out.println("InfoScreen iPlayeriSex = " + iPlayer.iSex);
					}
					//
					iGameTick = System.currentTimeMillis();
					//
					iAction = rspInfoThree.iAction;
					iSecond = rspInfoThree.iSecond;
					iOnLine = rspInfoThree.nOnLine;
					if(iShakeButton != null) {
						if(iSecond > 0) {
								iShakeButton.setActionID(1);
						}
						else if(iSecond == 0 && iPlayer.iCurrentDrunk <= 0) {
							iShakeButton.setActionID(2);
						}
						else {
							iShakeButton.setActionID(0);
							iShakeListener.startShake();
						}
					}
					if(iTraceButton != null) {
						if(iOnLine < 2) {
							iTraceButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_UNABLE);
						}
					}
					//陌生人
					if(iPlayer.iIntimate < 0) {
						//不能追踪
						if(iTraceButton != null) {
							iTraceButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_UNABLE);
						}
						//不能留言
						if(iLeaveMsgButton != null) {
							iLeaveMsgButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_UNABLE);
						}
					}
					//初始化图片
					initPic();
					//初始化汽车图片
					initCarPic();
					//初始化关系经验条
					setIntimateExp();
					//切换到进场状态
					iGameState = 1;
					
					//
//					System.out.println("InfoThree vipLevel=" + iPlayer.iVipLevel
//							+ " DiceID=" + iPlayer.iDiceID
//							+ " hasCard=" + rspInfoThree.nHasCard);
				}
				else {
					messageEvent(type, rspInfoThree.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_PLAYINFO_FOUR:
				MBRspPlayerInfoFour rspPlayerInfoFour = (MBRspPlayerInfoFour)pMobileMsg.m_pMsgPara;
				if(rspPlayerInfoFour == null) {
					break;
				}
				Tools.println(rspPlayerInfoFour.toString());
				type = rspPlayerInfoFour.iResult;
				if(type == ResultCode.SUCCESS) {
					//
					iPlayer.stUserNick = rspPlayerInfoFour.iNick;
					iPlayer.iRankTotalCount = rspPlayerInfoFour.iRankTotalCount; //排行榜总数
					iPlayer.iWealthRank = rspPlayerInfoFour.iWealthRank; //财富排名
					iPlayer.iLevelRank = rspPlayerInfoFour.iLevelRank; //等级排名
					iPlayer.iVictoryRank = rspPlayerInfoFour.iVictoryRank; //胜场排名
					iPlayer.iVipLevel = rspPlayerInfoFour.iVipLevel;			//Vip级别	
					iPlayer.iGameLevel = rspPlayerInfoFour.iGameLevel;		//级别
					iPlayer.iGameExp = rspPlayerInfoFour.iGameExp;			//当前级别的经验
					iPlayer.iCurrentDrunk = rspPlayerInfoFour.iCurrentDrunk;	//当前醉酒度
					iPlayer.iWinCount = rspPlayerInfoFour.iKillsCount;		//胜
					iPlayer.iFailCount = rspPlayerInfoFour.iBeKillsCount;	//负
					iPlayer.iModelID = rspPlayerInfoFour.iModelID;			//角色模型ID（为-1时，表示没选择人物）
					iPlayer.iSex = rspPlayerInfoFour.iSex;					//性别（为-1时，表示没选择人物）
					iPlayer.iWinning =	rspPlayerInfoFour.iWinning;			//胜率
					iPlayer.iReward = rspPlayerInfoFour.iReward;				//奖
					iPlayer.iTitle = rspPlayerInfoFour.iTitle;				//人物称号
					iPlayer.iGb = rspPlayerInfoFour.iGb;						//游戏币
					iPlayer.iMoney = rspPlayerInfoFour.iMoney;				//凤智币
					iPlayer.iDingCount = rspPlayerInfoFour.iSupportCount;	//支持数
					iPlayer.szBigPicName = rspPlayerInfoFour.iBigPicName;
					iPlayer.iAchievement = rspPlayerInfoFour.iMaxDrunk;		//最大连击数
					iPlayer.iDiceID = rspPlayerInfoFour.nDiceResID;			//骰子ID
					iPlayer.iHasWineCark = rspPlayerInfoFour.nHasCard > 0 ? true : false;
					iPlayer.iHasGiftPak = rspPlayerInfoFour.nHasLicense > 0 ? true : false;
					iPlayer.iIntimate = rspPlayerInfoFour.iFriendExp;		//亲密度
					iPlayer.iNextIntimate = rspPlayerInfoFour.iNextFriendExp;//下一级亲密度
					iPlayer.iFriendTitle = rspPlayerInfoFour.iFriendTitle;	//关系称号
					iPlayer.iZhaiSkill = rspPlayerInfoFour.iZhaiSkill;	// 0 --- 没学   1 --- 喊斋    2 --- 雷劈
					iPlayer.iCarID = rspPlayerInfoFour.iCarID;			//车ID
					iPlayer.iHouseID = rspPlayerInfoFour.iHouseID;		//房子ID
					iPlayer.iPetID = rspPlayerInfoFour.iPetID;			//宠物ID
					iPlayer.iMaxHit = rspPlayerInfoFour.iMaxDrunk;		//最大连击数
					iPlayer.iHonor = rspPlayerInfoFour.iHonor;		//称号
					setHeart();
					if(iVipSprite != null) {//VIP动画
						iVipSprite.setActionID(iPlayer.iVipLevel);
					}
					if(iPhotoBoxSprite != null) {//醉酒度
						iPhotoBoxSprite.setActionID(iPlayer.iCurrentDrunk);
					}
					if(iSexSprite != null) { //性别
						iSexSprite.setActionID(iPlayer.iSex);
//						System.out.println("InfoScreen iPlayeriSex = " + iPlayer.iSex);
					}
					//
					iGameTick = System.currentTimeMillis();
					//
					iAction = rspPlayerInfoFour.iAction;
					iSecond = rspPlayerInfoFour.iSecond;
					iOnLine = rspPlayerInfoFour.nOnLine;
					if(iShakeButton != null) {
						if(iSecond > 0) {
							iShakeButton.setActionID(1);
						}
						else if(iSecond == 0 && iPlayer.iCurrentDrunk <= 0) {
							iShakeButton.setActionID(2);
						}
						else {
							iShakeButton.setActionID(0);
							iShakeListener.startShake();
						}
					}
					if(iTraceButton != null) {
						if(iOnLine < 2) {
							iTraceButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_UNABLE);
						}
					}
					//陌生人
					if(iPlayer.iIntimate < 0) {
						//不能追踪
						if(iTraceButton != null) {
							iTraceButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_UNABLE);
						}
						//不能留言
						if(iLeaveMsgButton != null) {
							iLeaveMsgButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_UNABLE);
						}
					}
					//初始化图片
					initPic();
					//初始化汽车图片
					initCarPic();
					//初始化关系经验条
					setIntimateExp();
					//切换到进场状态
					iGameState = 1;
					
					//
//					System.out.println("InfoThree vipLevel=" + iPlayer.iVipLevel
//							+ " DiceID=" + iPlayer.iDiceID
//							+ " hasCard=" + rspPlayerInfoFour.nHasCard);
				}
				else {
					messageEvent(type, rspPlayerInfoFour.iMsg);
				}
				break;
				
			case RspMsg.MSG_RSP_HART:
				{
					MBRspHart rspHart = (MBRspHart)pMobileMsg.m_pMsgPara;
					if(rspHart == null) {
						return;
					}
					Tools.debug(rspHart.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_Heart, rspHart);
				}
				break;
			
			case RspMsg.MSG_RSP_DING:
				{
					removeProgressDialog();
					MBRspDing rspDing = (MBRspDing)pMobileMsg.m_pMsgPara;
					if(rspDing == null) {
						break;
					}
					Tools.debug(rspDing.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspDing, rspDing);
				}
				break;
				
			case RspMsg.MSG_RSP_TRACE:
				{
					removeProgressDialog();
					MBRspTrace rspTrace = (MBRspTrace)pMobileMsg.m_pMsgPara;
					if(rspTrace == null) {
						break;
					}
					Tools.debug(rspTrace.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspTrace, rspTrace);
				}
				break;
				
			case RspMsg.MSG_RSP_SHAKE_FRIEND:
				{
					removeProgressDialog();
					MBRspShakaFriend rspShakeFriend = (MBRspShakaFriend)pMobileMsg.m_pMsgPara;
					if(rspShakeFriend == null) {
						break;
					}
					Tools.debug(rspShakeFriend.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_SHAKE_FRIEND, rspShakeFriend);
				}
				break;
				
			case RspMsg.MSG_RSP_ADD_FRIEND:
				{
					removeProgressDialog();
					MBRspAddFriend rspAddFriend = (MBRspAddFriend)pMobileMsg.m_pMsgPara;
					if(rspAddFriend == null) {
						break;
					}
					Tools.debug(rspAddFriend.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_DELETE_FRIEND, rspAddFriend);
				}
				break;
			case RspMsg.MSG_RSP_GIFT_FRIEND_GOODS:
			{
				MBRspGiftFriendGoods rspGiftFriendGoods = (MBRspGiftFriendGoods)pMobileMsg.m_pMsgPara;
				if(rspGiftFriendGoods == null) {
					break;
				}
				Tools.debug(rspGiftFriendGoods.toString());
//				if(rspGiftFriendGoods.nResult == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_GIFT_FRIEND_GOODS, rspGiftFriendGoods);
//				}
			}
			break;
			case RspMsg.MSG_RSP_GIFT_FRIEND_GOODS_TWO:
			{
				MBRspGiftFriendGoodsTwo rspGiftFriendGoods = (MBRspGiftFriendGoodsTwo)pMobileMsg.m_pMsgPara;
				if(rspGiftFriendGoods == null) {
					break;
				}
				Tools.debug(rspGiftFriendGoods.toString());
//				if(rspGiftFriendGoods.nResult == ResultCode.SUCCESS) {
				messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_GIFT_FRIEND_GOODS, rspGiftFriendGoods);
//				}
			}
			break;
			case RspMsg.MSG_RSP_LEAVE_MSG_B:
				MBRspLeaveMsgB rspLeaveMsg = (MBRspLeaveMsgB)pMobileMsg.m_pMsgPara;
				if(rspLeaveMsg == null) {
					break;
				}
				Tools.debug(rspLeaveMsg.toString());
				type = rspLeaveMsg.iResult;
				if(type == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_LEAVEMSG, rspLeaveMsg);
				}
				else {
					messageEvent(type, rspLeaveMsg.iMsg);
				}
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
			case RspMsg.MSG_RSP_HOME_GIFT_LIST:
				MBRspHomeGiftList reMbRspHomeGiftList = (MBRspHomeGiftList)pMobileMsg.m_pMsgPara;
				if(reMbRspHomeGiftList == null) {
					break;
				}
				Tools.println(reMbRspHomeGiftList.toString());
				type = reMbRspHomeGiftList.iResult;
				messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_HOMEGIFTLIST, reMbRspHomeGiftList);
				break;	
			case RspMsg.MSG_RSP_HOME_RECEIVE_GIFT_LIST:
				MBRspHomeReceiveGiftList receiveGiftList = (MBRspHomeReceiveGiftList)pMobileMsg.m_pMsgPara;
				if(receiveGiftList == null) {
					break;
				}
				Tools.println(receiveGiftList.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_HOMERECEIVEGIFTLIST, receiveGiftList);
				break;	
			default:
				break;
			}
		}
		break;
		case IM.IM_NOTIFY:
		{
			switch (aMsgID) {
			case NotifyMsg.MSG_NOTIFY_PLAY_ANIMATION_TWO:
				MBNotifyPlayAnimationTwo notifyPlayAnimationTwo = (MBNotifyPlayAnimationTwo) pMobileMsg.m_pMsgPara;
				if(notifyPlayAnimationTwo == null) {
					return;
				}
				Tools.println(notifyPlayAnimationTwo.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_PLAYANIMATE, notifyPlayAnimationTwo);
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
			{
				showProgressDialog(R.string.Loading_String);
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_SINGLE_PIC://单张图片下完
			{
				if(iGameState == 2 || iGameState == 1) {
					String tPicName = (String) msg.obj;//图片名
//				System.out.println("FriendDetail picname=" + tPicName);
					if (tPicName.equals(iPlayer.szBigPicName)) {
//					System.out.println("FriendDetail initPic. createBackBmp. picname=" + tPicName);
						//
						initPic();
						createBMP = true;
					}
					else if(tPicName.equals(iPlayer.iCarID+".png")) {
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
		
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspDing:
			{
				MBRspDing rspDing = (MBRspDing)msg.obj;
				if(rspDing == null) {
					break;
				}
				if(rspDing.iResult != 0) {
					if(rspDing.iMsg.length() > 0) {
						Tools.showSimpleToast(
								MyArStation.mGameMain.getApplicationContext(),
							Gravity.CENTER, 
							rspDing.iMsg, 
							Toast.LENGTH_LONG);
					}
					break;
				}
				//更新自己的信息
				MyArStation.iPlayer.iGb += rspDing.iAddGB;
				//
				iPlayer.iIntimate += rspDing.iAddIntimate;
				//飞心动画
				int tempIndex = 0;
				for(int i=0; i<MAX_HART_SPRITE; i++) {
					if(iHartLifeTick[i] <= 0) {
						tempIndex = i;
						break;
					}
				}
				iHartLifeTick[tempIndex] = 18;
				iHartX[tempIndex] = iHartStartX[tempIndex] = (int)(370 * ActivityUtil.ZOOM_X);
				iHartY[tempIndex] = iHartStartY[tempIndex] = (int)(416 * ActivityUtil.ZOOM_Y);
				iHartEndX[tempIndex] = (int)((644 - 30 + Math.random() * 60) * ActivityUtil.ZOOM_X); 
				iHartEndY[tempIndex] = (int)((139 - 30 + Math.random() * 60) * ActivityUtil.ZOOM_Y); 
				if(iHartEndX[tempIndex]-iHartStartX[tempIndex] != 0) {
					iHartLineK[tempIndex] = ((float)(iHartEndY[tempIndex]-iHartStartY[tempIndex]))/((float)(iHartEndX[tempIndex]-iHartStartX[tempIndex]));
					iHartSpeed[tempIndex] = ((float)(iHartEndX[tempIndex] - iHartStartX[tempIndex])) / (float)18;
				}
				else {
					iHartSpeed[tempIndex] = ((float)(iHartEndY[tempIndex]-iHartStartY[tempIndex])) / (float)18;
				}
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspTrace:
			{
				MBRspTrace rspTrace = (MBRspTrace)msg.obj;
				if(rspTrace == null) {
					break;
				}
				if(rspTrace.iResult != 0) {
					if(rspTrace.iMsg.length() > 0) {
						Tools.showSimpleToast(
								MyArStation.mGameMain.getApplicationContext(),
							Gravity.CENTER, 
							rspTrace.iMsg, 
							Toast.LENGTH_LONG);
					}
					break;
				}
				//进入房间
				RoomData roomPair = new RoomData(
						rspTrace.iRoomID, rspTrace.iTableID, rspTrace.iRoomType);
				Message tMes = new Message();
				tMes.obj = roomPair;
				tMes.what = MessageEventID.EMESSAGE_EVENT_TO_PROGRESS;
				MyArStation.mGameMain.iHandler.sendMessage(tMes);
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_RSP_SHAKE_FRIEND:
			{
				MBRspShakaFriend rspShakeFriend = (MBRspShakaFriend)msg.obj;
				if(rspShakeFriend.iMsg.length() > 0) {
					Tools.showSimpleToast(
							MyArStation.mGameMain.getApplicationContext(),
							Gravity.CENTER, 
							rspShakeFriend.iMsg, 
							Toast.LENGTH_LONG);
				}
				if(rspShakeFriend.iResult != 0) {
					break;
				}
				//
				iPlayer.iCurrentDrunk = rspShakeFriend.nDrunk;
				iPlayer.iIntimate += rspShakeFriend.iMinute;
				setIntimateExp();
				//
				iAction = rspShakeFriend.iAction;
				iSecond = rspShakeFriend.iSecond;
				iOnLine = rspShakeFriend.nOnLine;
				//
				iGameTick = System.currentTimeMillis();
				if(iPhotoBoxSprite != null) {
					iPhotoBoxSprite.setActionID(iPlayer.iCurrentDrunk);
				}
				//
				if(iShakeButton != null) {
					if(iSecond > 0) {
						iShakeButton.setActionID(1);
					}
					else if(iSecond == 0 && iPlayer.iCurrentDrunk <= 0) {
						iShakeButton.setActionID(2);
					}
				}
				if(iTraceButton != null) {
					if(iOnLine < 2) {
						iTraceButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_UNABLE);
					}
					else {
						iTraceButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
					}
				}
				//陌生人
				if(iPlayer.iIntimate < 0) {
					//不能追踪
					if(iTraceButton != null) {
						iTraceButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_UNABLE);
					}
					//不能留言
					if(iLeaveMsgButton != null) {
						iLeaveMsgButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_UNABLE);
					}
				}
				//
				MyArStation.iPlayer.setFriendDrunk(iUserID, iPlayer.iCurrentDrunk, iOnLine);
				MyArStation.iPlayer.iGb += rspShakeFriend.iAction; //iAction这里是金币			
				createBMP =  true;
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_RSP_DELETE_FRIEND:
			{
				MBRspAddFriend rspAddFriend = (MBRspAddFriend)msg.obj;	
				//切换到好友界面
				if(iScreanType == SCREENTYPE_FRIENDLIST) {
					//
					MyArStation.iPlayer.deleteFriend(rspAddFriend.iDesUserID);
					//
					iGameCanvas.changeView(new FriendInfoScreen(iUserID, iPosition[0]));
				}
				else if(iScreanType == SCREENTYPE_RANK){
					//
					MyArStation.iPlayer.refreshRankList(rspAddFriend.iDesUserID, rspAddFriend.iAction);
					//
					iGameCanvas.changeView(new RankScreen(iUserID,iPosition, iRankType));
				}
			}
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_FRIEND_DETAIL_DELFRIEND:
			{
				iDelriendDialog = new GeneralDialog(MyArStation.mGameMain);
				iDelriendDialog.setOnGeneralDialogListener(this);
				iDelriendDialog.setConfirmText("确定");
				iDelriendDialog.setCancelText("取消");
				SpannableString ss = new SpannableString("您确定要与"+iPlayer.stUserNick+"断交?");
		        ss.setSpan(new ForegroundColorSpan(Color.RED), 5, 5 + iPlayer.stUserNick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				iDelriendDialog.Show(ss);
			}
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_FRIEND_GIVE_VIP_CONFIRM:
		{	
			final GiftData data = (GiftData) msg.obj;
			if(iGiveVIPdDialog == null) {
				iGiveVIPdDialog = new GeneralDialog(MyArStation.mGameMain);
			}
			iGiveVIPdDialog.setOnGeneralDialogListener(new OnGeneralDialogListener() {
				
				@Override
				public void OnConfirm() {
//					GameMain.getInstance().iGameProtocol.RequestGiftFriendGoods(iUserID, 0, 1);
					MyArStation.iGameProtocol.RequestGiftFriendGoodsTwo(iUserID, data.iID, 1);
				}
				
				@Override
				public void OnCancel() {
					
				}
			});
			iGiveVIPdDialog.setConfirmText("确定");
			iGiveVIPdDialog.setCancelText("取消");
			SpannableString ss = new SpannableString("您确定赠送 "+iPlayer.stUserNick+data.iNames + " " + "?");
			ss.setSpan(new ForegroundColorSpan(Color.GREEN), 6, 6 + iPlayer.stUserNick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			iGiveVIPdDialog.Show(ss);
		}
		break;
		case MessageEventID.EMESSAGE_EVENT_RSP_GIFT_FRIEND_GOODS:
			MBRspGiftFriendGoodsTwo rspGiftFriendGoods = (MBRspGiftFriendGoodsTwo)msg.obj;
			if(rspGiftFriendGoods.iMsg.length() > 0) {
				Tools.showSimpleToast(
						MyArStation.mGameMain.getApplicationContext(),
						Gravity.CENTER, 
						rspGiftFriendGoods.iMsg, 
						Toast.LENGTH_LONG);
			}
			if(rspGiftFriendGoods.nResult == ResultCode.SUCCESS) {
				MyArStation.iPlayer.iMoney = rspGiftFriendGoods.iMoney;
				MyArStation.iPlayer.iGb = rspGiftFriendGoods.iCurrentGb;
				iPlayer.iIntimate = rspGiftFriendGoods.iFriendExp;		//亲密度
				iPlayer.iNextIntimate = rspGiftFriendGoods.iNextFriendExp;//下一级亲密度
				iPlayer.iFriendTitle = rspGiftFriendGoods.iFriendTitle;	//关系称号
				int goodsID = rspGiftFriendGoods.iGoodsID;	
				iGiftIDs.add(goodsID);
				GiveGiftData data = new GiveGiftData();
				data.id = goodsID;
				addGifts(data, true);
				setIntimateExp();
	//			if(iVipSprite != null) {//VIP动画
	//				iVipSprite.setActionID(iPlayer.iVipLevel);
	//			}
				iGameState = 4;
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_LEAVEMSG:
			MBRspLeaveMsgB leaveMsg = (MBRspLeaveMsgB) msg.obj;
			if(leaveMsg.iMsg.length() > 0)
				Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), leaveMsg.iMsg);
			MessageData data = new MessageData();
			data.reset(leaveMsg.iPic, 
					leaveMsg.iNick, 
					leaveMsg.iText,
					leaveMsg.iUID,
					leaveMsg.iLeaveMsgID, 
					Util.getTimeState(leaveMsg.iTime+""), false, leaveMsg.iType);
			if(messageDialog != null) {
				messageDialog.iUserID = iUserID;
				messageDialog.setMessageData(data);
				messageDialog.show();
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
				data = new MessageData();
				data.reset(leaveMsgList.iPic.get(i), leaveMsgList.iNick.get(i),
						leaveMsgList.iText.get(i), leaveMsgList.iUID.get(i), 
						leaveMsgList.iLeaveMsgID.get(i), Util.getTimeState(leaveMsgList.iTime.get(i)+""),
						false, leaveMsgList.iType.get(i));
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
		case GameEventID.ESPRITEBUTTON_EVENT_FRIEND_LEAVEMSG:
			if(messageDialog == null) {
				messageDialog = new MessageDialog(MyArStation.mGameMain);
			}
			if(messageDialog.messageDatas.isEmpty()) {
				MyArStation.iGameProtocol.requestLeaveListMsgB(iUserID, messageDialog.iStartIndex);
			}
			messageDialog.show();
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_FRIEND_INITSTSTEMCOM:
			MyArStation.mGameMain.mainLayout.removeAllViews();
			break;
		case MessageEventID.EMESSAGE_EVENT_RSP_HOMEGIFTLIST:
			MBRspHomeGiftList rspHomeGiftList = (MBRspHomeGiftList)msg.obj;
			showMessage(rspHomeGiftList.iMsg);
			if(rspHomeGiftList.iResult == 0) {
				count = rspHomeGiftList.iCount;
				for(int i = 0; i < count; i++) {
					GiftData giftData = new GiftData(rspHomeGiftList.iIDs.get(i),
							rspHomeGiftList.iMoneys.get(i), 
							rspHomeGiftList.iGbs.get(i),
							rspHomeGiftList.iIntimates.get(i), 
							rspHomeGiftList.iNames.get(i));
					iGiftDatas.add(giftData);
				}
				if(giftsView != null) {
					giftsView.setGiftData(iGiftDatas);
					giftsView.show();
				}
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
		case MessageEventID.EMESSAGE_EVENT_NOTIFY_PLAYANIMATE:
		{
			MBNotifyPlayAnimationTwo notifyPlayAnimationTwo = (MBNotifyPlayAnimationTwo) msg.obj;
			if(synAnimation != null){
				AnimFormatData animFormatData = new AnimFormatData(notifyPlayAnimationTwo.szAnimDes);
				Point point = getNextGiftPostion();
				animFormatData.nX1 = animFormatData.nX2 = point.x;
				animFormatData.nY1 = 0;
				animFormatData.nY2 = point.y;
				synAnimation.setAnimationData(animFormatData);
				synAnimation.setOnSynAnimationListen(this);
			}
		}
		break;
		case MessageEventID.EMESSAGE_EVENT_SHOWMSG:
		{
			showMessage((String) msg.obj);
		}
		break;
		default:
			break;
		}
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
	
	/**
	 * description 关闭弹出界面
	 */
	private void closePopScene() {
		if (iPopScene == null) {
			return;
		}
		iPopScene = null;
		iPopSBList.removeAllElements();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void requestPlayerInfo() {
//		MBRspLogin login = (MBRspLogin)GameMain.iGameProtocol.getMobileMsgCS(RspMsg.MSG_RSP_LOGIN);
//		MBRspVisitorLogin visitorlogin = (MBRspVisitorLogin)GameMain.iGameProtocol.getMobileMsgCS(RspMsg.MSG_RSP_VISITORLOGIN);
//		int userid = login != null ? login.iUserId : (visitorlogin != null ? visitorlogin.iUserId : 0);
//		GameMain.iGameProtocol.requestPlayerInfoThree(iUserID);
		MyArStation.iGameProtocol.requestPlayerInfoFour(iUserID);
	}
	
	private void requestDing(int aDesUserID) {
		MyArStation.iGameProtocol.requestDing(aDesUserID);
	}
	
	private void requestTrace(int aDesUserID) {
		MyArStation.iGameProtocol.requestTrace(aDesUserID);
	}
	
	private void requestShake(int aDesUserID) {
		MyArStation.iGameProtocol.requestShakeFriend(aDesUserID);
	}

	private void requestDeleteFriend(int aDesUserID) {
		MyArStation.iGameProtocol.requestAddFriend(aDesUserID, 1);
	}
	
	@Override
	public void onShake() {
		// TODO Auto-generated method stub
		if(iShakeButton == null) {
			return;
		}
		if(iShakeButton.getActionID() == 0) {
			iShakeButton.setActionID(2);
			requestShake(iUserID);
			iShakeListener.stopShake();
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GeneralDialog.OnGeneralDialogListener#OnConfirm()
	 */
	@Override
	public void OnConfirm() {
		requestDeleteFriend(iUserID);
	}

	/* 
	 * @see com.funoble.lyingdice.view.GeneralDialog.OnGeneralDialogListener#OnCancel()
	 */
	@Override
	public void OnCancel() {
	}

	/* 
	 * @see com.funoble.lyingdice.screen.GiftsView.OnClickGiftsListen#OnClickGifts(com.funoble.lyingdice.store.data.GiftData)
	 */
	@Override
	public void OnClickGifts(GiftData aGiftData) {
		if(aGiftData != null) {
			messageEvent(GameEventID.ESPRITEBUTTON_EVENT_FRIEND_GIVE_VIP_CONFIRM, aGiftData);
		}
	}

	/* 
	 * @see com.funoble.lyingdice.screen.SynAnimation.SynAnimationListen#onSynAnimationStart()
	 */
	@Override
	public void onSynAnimationStart() {
	}

	/* 
	 * @see com.funoble.lyingdice.screen.SynAnimation.SynAnimationListen#onSynAnimationPlaying()
	 */
	@Override
	public void onSynAnimationPlaying() {
	}

	/* 
	 * @see com.funoble.lyingdice.screen.SynAnimation.SynAnimationListen#onSynAnimationEnd()
	 */
	@Override
	public void onSynAnimationEnd() {
	}
	
}
