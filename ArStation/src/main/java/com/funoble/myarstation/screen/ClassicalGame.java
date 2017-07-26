package com.funoble.myarstation.screen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import android.app.Dialog;
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
import android.os.Looper;
import android.os.Message;
//import android.sax.Element;
//import android.text.format.Time;
//import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.ChatContent;
import com.funoble.myarstation.data.cach.Concours;
import com.funoble.myarstation.data.cach.RoomData;
import com.funoble.myarstation.game.CGamePlayer;
import com.funoble.myarstation.game.CPlayer;
import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.OvalTimerCount;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.game.OvalTimerCount.TimerCountOut;
import com.funoble.myarstation.gamebase.CBuilding;
import com.funoble.myarstation.gamebase.GameButtonHandler;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.ResFile;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.ShakeListener;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.gamelogic.SpriteButtonSelect;
import com.funoble.myarstation.gamelogic.ShakeListener.OnShakeListener;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.screen.QuickSelectRoomScreen.OnSelectRoomListen;
import com.funoble.myarstation.socket.protocol.MBNotifyActivityRanking;
import com.funoble.myarstation.socket.protocol.MBNotifyActivityStart;
import com.funoble.myarstation.socket.protocol.MBNotifyChangeDice;
import com.funoble.myarstation.socket.protocol.MBNotifyChat;
import com.funoble.myarstation.socket.protocol.MBNotifyChop;
import com.funoble.myarstation.socket.protocol.MBNotifyCleanDrunk;
import com.funoble.myarstation.socket.protocol.MBNotifyDing;
import com.funoble.myarstation.socket.protocol.MBNotifyFace;
import com.funoble.myarstation.socket.protocol.MBNotifyFriendEvent;
import com.funoble.myarstation.socket.protocol.MBNotifyKick;
import com.funoble.myarstation.socket.protocol.MBNotifyKillOrder;
import com.funoble.myarstation.socket.protocol.MBNotifyLeaveRoom;
import com.funoble.myarstation.socket.protocol.MBNotifyLevelUp;
import com.funoble.myarstation.socket.protocol.MBNotifyLightning;
import com.funoble.myarstation.socket.protocol.MBNotifyLoginRoom;
import com.funoble.myarstation.socket.protocol.MBNotifyLoginRoomB;
import com.funoble.myarstation.socket.protocol.MBNotifyManyChop;
import com.funoble.myarstation.socket.protocol.MBNotifyPlayAnimation;
import com.funoble.myarstation.socket.protocol.MBNotifyPlayAnimationTwo;
import com.funoble.myarstation.socket.protocol.MBNotifyReady;
import com.funoble.myarstation.socket.protocol.MBNotifyRebelChop;
import com.funoble.myarstation.socket.protocol.MBNotifyResult;
import com.funoble.myarstation.socket.protocol.MBNotifyShakeFriend;
import com.funoble.myarstation.socket.protocol.MBNotifyShield;
import com.funoble.myarstation.socket.protocol.MBNotifyShout;
import com.funoble.myarstation.socket.protocol.MBNotifyShowItem;
import com.funoble.myarstation.socket.protocol.MBNotifyShowReady;
import com.funoble.myarstation.socket.protocol.MBNotifyStart;
import com.funoble.myarstation.socket.protocol.MBNotifySysMsg;
import com.funoble.myarstation.socket.protocol.MBNotifyTriggerShield;
import com.funoble.myarstation.socket.protocol.MBNotifyUnarmed;
import com.funoble.myarstation.socket.protocol.MBNotifyVieChop;
import com.funoble.myarstation.socket.protocol.MBNotifyVieChopB;
import com.funoble.myarstation.socket.protocol.MBRspActivityEnroll;
import com.funoble.myarstation.socket.protocol.MBRspActivityRank;
import com.funoble.myarstation.socket.protocol.MBRspAddFriend;
import com.funoble.myarstation.socket.protocol.MBRspBuyWine;
import com.funoble.myarstation.socket.protocol.MBRspDing;
import com.funoble.myarstation.socket.protocol.MBRspFriendEventSelect;
import com.funoble.myarstation.socket.protocol.MBRspKick;
import com.funoble.myarstation.socket.protocol.MBRspLeaveRoom;
import com.funoble.myarstation.socket.protocol.MBRspLoginRoom;
import com.funoble.myarstation.socket.protocol.MBRspLoginRoomB;
import com.funoble.myarstation.socket.protocol.MBRspPlayerInfo;
import com.funoble.myarstation.socket.protocol.MBRspPlayerInfoFour;
import com.funoble.myarstation.socket.protocol.MBRspPlayerInfoThree;
import com.funoble.myarstation.socket.protocol.MBRspPlayerInfoTwo;
import com.funoble.myarstation.socket.protocol.MBRspUseItem;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.ActivityAction;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.NotifyMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.SocketErrorType;
import com.funoble.myarstation.sound.CallPointManager;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.store.data.AnimFormatData;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.Graphics;
import com.funoble.myarstation.utils.Lightning;
import com.funoble.myarstation.view.BuyDialog;
import com.funoble.myarstation.view.ChatView;
import com.funoble.myarstation.view.ConcoursDialog;
import com.funoble.myarstation.view.EventSelectDialog;
import com.funoble.myarstation.view.FaceSelectDialog;
import com.funoble.myarstation.view.GameEventID;
import com.funoble.myarstation.view.GameMenuDialog;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.InfoPopView;
import com.funoble.myarstation.view.MessageEventID;
import com.funoble.myarstation.view.ChatView.ChatNotifyNewMessage;
import com.funoble.myarstation.view.ConcoursDialog.OnConcoursListener;
import com.funoble.myarstation.view.EventSelectDialog.OnGeneralDialogListener;
import com.funoble.myarstation.view.FaceSelectDialog.OnSelectFaceListener;
import com.funoble.myarstation.view.GameMenuDialog.OnClickMenuListener;

/**
 * description 
 * version 1.0
 * author 
 * update 2012-11-13 上午10:27:03 
 */
public class ClassicalGame extends GameView implements GameButtonHandler, TimerCountOut, 
														OnTouchListener, OnClickMenuListener,
														OnConcoursListener,
														OnShakeListener, ChatNotifyNewMessage,
														OnGeneralDialogListener,
														OnSelectFaceListener,
														OnSelectRoomListen {
	//按钮动画
	private final long ANIMATION_TIME = 300;
	private final long TIME_INTERVAL = 30;
	
	//最大动画按钮的个数
	private final int MAX_ICON_BUTTON_COUNT = 9;
	
	//延迟显示反劈按钮的时间
	private final int SHOW_REBELBUTTON_DELAY = 24;
	
	//
	private final int ICON_LAYOUT_ID = 100;//弹出按钮的容器
	//图标按钮ID
	//图标按钮ID
	private final int ICON_BUTTON_ID_BOTTLE = 0;	//啤酒瓶
	private final int ICON_BUTTON_ID_CAKE = 1;		//蛋糕
	private final int ICON_BUTTON_ID_CHANGEDICE = 2;//改变骰子
	private final int ICON_BUTTON_ID_DISUNITE = 3;	//解盟
	private final int ICON_BUTTON_ID_FRIEND = 4;	//加好友
	private final int ICON_BUTTON_ID_HART = 5;		//心
	private final int ICON_BUTTON_ID_INFO = 6;		//信息
	private final int ICON_BUTTON_ID_KICKOFF = 7;	//踢人
	private final int ICON_BUTTON_ID_UNITE = 8;		//结盟
	
	//
	private final int MAX_GB_SPRITE = 8;	//金币动画的个数
	
	//
	private final int MAX_HART_SPRITE = 32;	//飞心动画的个数
	private final int MAX_ANIM_SPRITE = 16;	//动态动画的个数
	
	//
	private final int MAX_ITEM_SPRITE_TYPE = 2; //道具动画的类型数
	private final int ITEM_BOTTLE_SPRITE_ID = 0;	//啤酒瓶道具的动画ID
	private final int ITEM_CAKE_SPRITE_ID = 1;		//蛋糕道具的动画ID
	
	//
	private final int MAX_DICE_SPRITE = 5;	//骰子动画的个数
	private final int DICE_MOVIE_STATE_IDLE = 0; 	//停止
	private final int DICE_MOVIE_STATE_RUNNING = 1; //运动
	private final int DICE_MOVIE_STATE_HOME = 2; 	//飞向骰盅
	
	//
	private final int MAX_MODEL_COUNT = 4;	//最大的模型个数
	private final int MAX_GAME_PLAYER = 6; 	//桌子的最大玩家数
	private final int GAME_STATE_LOADING = 0;//载入数据
	private final int GAME_STATE_LOGINROOM_START = 1; //开始进入房间
	private final int GAME_STATE_SITDOWN = 2; //坐下
	private final int GAME_STATE_WAIT_READY = 3; //等待准备
	private final int GAME_STATE_LEAVEROOM = 4;	//离开房间,去主菜单
	private final int GAME_STATE_CHANGE_DESK = 5; //换桌
	private final int GAME_STATE_CHANGE_ROOM = 6; //离开房间，去选择房间
	private final int GAME_STATE_CHANGEDESK_LOADING = 7; //正在换桌
	private final int GAME_STATE_JOIN_ACTIVITY = 8; //准备参加活动
	private final int GAME_STATE_JOINACTIVITY_LOADING = 9; //正在参加活动
	
	//
	private final int PLAY_ROCK_PLAYER_COUNT = 6;//播放摇滚音乐的人数
	private final int PLAY_ROCK_DICE_COUNT = 12;//播放摇滚音乐需要的个数
	
	//后台的游戏逻辑类型
	private final int E_TGLT_NORMAL = 0;	//1可变
	private final int E_TGLT_NOTCHANGE = 1;	//1不可变
	private final int E_TGLT_SHOUTONENOTCHANGE = 2;  //喊了1后，1不可变
	private final int E_TGLT_ACTIVITY_SHOUTONENOTCHANGE = 3; //活动房，喊了1后，1不可变

	//后台的 游戏状态
	private final int E_TGS_WAIT_PLAYER = 0;		//等待玩家
	private final int E_TGS_WAIT_START = 1;			//等待开始
	private final int E_TGS_PLAYING = 2;			//正在游戏
	private final int E_TGS_SHIELD = 3;				//触发了催眠怀表
	private final int E_TGS_WAIT_RESULT = 4;		//等待结果
	private final int E_TGS_WAIT_READY = 5;			//等待准备
	private final int E_TGS_SHOW_PIKONG = 6;		//显示劈空动画
	private final int E_TGS_SHOW_MONEY = 7;			//显示结果金币
	private final int E_TGS_SHOW_DRINK = 8;			//显示喝酒
	
	//摇手机监听器
	private ShakeListener iShakeListener;
	
	//系统控件
	private Dialog 		 dialog;
	private RelativeLayout iAbLayout = null;
	private ChatView 		iChatView = null;
	private BuyDialog		iBuyWineDialog = null;
	private GameMenuDialog		iGameMenuDialog = null;
//	private PlayerInfoDialog iPlayerInfDialog = null;
	private ConcoursDialog 	iConcoursDialog = null;
	private List<AnimationSet> mOutAnimatinSets = new ArrayList<AnimationSet>();
	private List<AnimationSet> mInAnimatinSets = new ArrayList<AnimationSet>();
	private List<Button> mList = new ArrayList<Button> ();
	private EventSelectDialog 	eventSelectDialog = null;
	private FaceSelectDialog  feedBackDialog = null;
//	private StroeDialog stroeDialog;
	private MissionView  missionView = null;
	
//	private List<TextView> mTextViewList = new ArrayList<TextView> ();
	private int iLyW = 0;
	private int iLyH = 0;
	private int iIconButtonW = 0;
	private int iIconButtonH = 0;
	private int mButtonStartX = 0;
	private int mButtonStartY = 0;
	private int[] mButtonEndX = null;
	private int[] mButtonEndY = null;
	private int[] mAnimToButton = null;	//动画对应的按钮
	private int[] mButtonToAnim = null; //按钮对应的动画
	private boolean iIconButtonShow = false;
	private int iOutStartCount = 0;		//开始向外动画的个数
	private int iOutCompleteCount = 0;	//结束向外动画的个数
	private int iSelectBtID = 0;	//选择的动画按钮
	private boolean ibSelectBt = false;	//是否选择了动画按钮
	
	//Player
	private int	iPlayerCount = 0;	//玩家个数
	private CGamePlayer[] iPlayerList = null;	//桌子的玩家列表
	private int iSelfSeatID = 0;	//自己的座位ID
	private int[] iChangedSeatID = null; //根据自己的座位，转换视觉的座位ID
	private int iSelectSeatID = 0; //选择的座位ID
	
	//
	private GameCanvas iGameCanvas = null;
	private Project iUIPak = null;
//	private Project iDeskPak = null;
	private Project iFacePak = null;
	private Project[] iItemPak = null;
	private Project iWeiSePak = null; //唯色加1
	private Project iPiKongPak = null; //劈空
	private Project iZhaiPak = null; //喊斋 
	private Project iLightningPak = null; //雷击
//	private Scene iScene = null;	//背景
	private Scene iSceneDeskEx = null; //扩展桌子
	private Scene iSceneLoading = null;	//载入数据
	private Scene iSceneDesk = null;	//桌子
	private Scene iSceneBar = null;		//状态栏
	private Scene iSceneWheel = null;	//滚轮
	private Scene iSceneOther = null;	//其它按钮
	private Scene iSceneExp = null;		//经验条
	private Sprite iDrunkSprite = null; //醉酒度
	private Sprite iShoutSprite = null; //喊点信息框
	private Sprite iBigCupSprite = null; // 大骰盅动画
	private Sprite iChopSprite = null;  //劈动画
	private Sprite iRebelChopSprite = null; //反劈动画
	private Sprite iNumSprite = null;	//数字动画
	private Sprite iBigNumSprite = null; //大数字动画
	private Sprite iXSprite = null; //"X"动画
	private Sprite iGbSprite = null; //金币动画
	private Sprite iDrinkSprite = null; //喝酒动画
	private Sprite iWinnerSprite = null; //灌倒人动画
	private Sprite iStartSprite = null;	//星星
	private Sprite iHartSprite = null;	//飞心动画
	private Sprite iBeDingSprite = null; //被顶动画
	private Sprite iLoveSprite = null;	//爱心动画
	private Sprite iLevelUpSprite = null; //升级动画
	private Sprite iDoubleSprite = null;	//加倍动画
	private Sprite[] iBrightDiceSprite = null; //发光的骰子动画
	private Sprite[] iItemSprite = null; //道具动画
	private Sprite[] iAnimSprite = null;
	private Sprite iWeiSeSprite = null; //唯色动画
	private Sprite iPiKongSprite = null; //劈空动画
	private Sprite iLightningExp = null; //雷击爆炸
	private Sprite iNextLightningExp = null; //反弹雷击爆炸
//	private Sprite iZhaiSprite = null; //喊斋动画
//	private Vector<Integer> iAnimEndMotionList = null;//动态动画的动作
//	private Vector<Sprite> iAnimSpriteList = null; //动态动画
	private Vector<Sprite> iHartSpriteList = null; //飘心动画
	private Vector<Sprite> iLevelUpSpriteList = null; //升级动画
	private Vector<Sprite> iItemSpriteList = null; //道具动画
	private Vector<Sprite> iWeiSeSpriteList = null; //唯色加1动画列表
	private Vector<SpriteButton> iSpriteButtonList = null;//精灵按钮 列表
	private Vector<SpriteButton> iPlayerButtonList = null;//玩家按钮
	private SpriteButton iPreChopButton = null;	//抢劈按钮
	private SpriteButton iRebelChopButton = null; //反劈按钮
	private SpriteButton iChatButton = null;	//聊天按钮
	private SpriteButton iMenuButton = null;	//菜单按钮
	private SpriteButton iFaceButton = null;	//表情按钮
	private SpriteButton iGiftBUtton = null;	//任务按钮
	private SpriteButtonSelect iZhaiButton = null; //喊斋技能按钮
	private SpriteButton iLightningButton = null; //雷击技能按钮
	private Bitmap iBackBmp = null;	//背景图
	private Bitmap iDefualPhtopBitmap = null;//透明头像
	private NinePatch iNinePatch = null; //经验条
//	private NinePatch iExpBmp = null;

	//游戏逻辑
	private int iLogicType = 0;//
	private int iServerState = -1; //后台的状态
	private int iGameState = 0;	// 0 --- 载入数据
	private int iGameTick = 0;
	private int iLoadingTick = 0;	//载入数据的等待时间
	private int iResultInfOffsetY = 0; //显示结果数据的偏移量
	private int iMaxResultInfOffsetY = 50; //最大偏移量
	private int iShowBigTextTick = 0; //显示结果时，控制显示大字体的时间
	private int iPlayingCount = 1;	//正在玩游戏的玩家个数
	private short iSelectShoutOne = 0; //是否选择了直接喊斋 
	private boolean ibShoutOne = false; //有没有喊了斋
	private int iLastShoutCount = 0; //上一次喊的个数
	private int iLastShoutDice = 0;	//上一次喊的骰子
	private int iLastShoutSeatID = 0; //上次喊点的座位号
	private int iCurrentShoutSeatID = 0; //当前喊点的座位号
	private int iChopSeatID = 0;	//当前劈人的座位号
	private int iRebelChopSeatID = -1; //当前反劈人的座位号
	private int iChopDice = 0;		//被劈的骰子
	private int iChopDiceCount = 0; //被劈骰子的个数
	private int iWinSeatID = 0;		//胜利者的座位ID
	private int iFailSeatID = 0;	//失败者的座位ID
	private int iFailCurrentDrunk = 0; //失败者的醉酒度
	private int iShowingResultSeatID = 0; //正在显示结果的座位ID
	private int[] iPlayingSeatIDs = null; //正在玩游戏的人的座位ID
	private int iFlashBetTick = 0;	//闪烁底注的时间片
	private int iDoubleTimes = 0;	//翻倍的次数
	private boolean iDoubleSpriteEnd = true; //翻倍动画
	private boolean iLightningExpEnd = true; //雷击爆炸动画
	private boolean iNextLightningExpEnd = true; //反弹雷击爆炸动画
	private int iWinePrice = 0;		//酒价
	private int iCleanDrunkPrice = 0;	//醒酒价格
	private int iRewardExp = 0;		//灌倒人获得的经验
	private String iRoomName;		//房间名称
	private String iWineName;		//酒名
	private String iBetStr;			//底注
	private String iResultMsg;		//一局结束后，显示的信息
	private boolean	ibAutoReqInfo = false; //是否自动获取个人信息（当降级时，需要自动获取）
	private boolean iWantToShowRebleChopButton = false; //是否需要显示反劈按钮
	private long	iShowRebelChopButtonDelay = 0;	//显示反劈按钮的延迟
	
	//活动
	private boolean ibHangUpActivity = false;
	private boolean iNeedShowActivityDialog = false; //是否需要显示参加活动的对话框
	private int iActivityRoomID = 0; //活动房间ID
	private String iActivityInfo;	//活动房的提示信息
	private String iActivityDialogInfo;	//参加活动对话框的提示信息
	private Concours iActivityConcours = null;
	
	//计时器
	private OvalTimerCount iOvalTimerCount = null;
	RectF iTempRect = null;
	
	//
	private float iSDX = 0;		//控制SceneDesk的坐标
	private int iSDSpeed = 0;	
	private float iDrunkTextSkewX = 0;
	private int iDrunkTextAlpha = 0;
	
	//
	private float iSBX = 0;		//控制SceneBar的坐标
	private int iSBSpeed = 0;

	//金币动画
	private int iActiveGbSpriteIndex = 0;	//开始动的金币索引
	private float[] iGbX;	//金币坐标
	private float[] iGbY;	//金币坐标
	private int[] iGbStartX;
	private int[] iGbStartY;
	private int[] iGbEndX;
	private int[] iGbEndY;
	private float[] iLineK;
	private float[] iGbSpeed;
	private int[] iLifeTick;	//生命周期
	
	//动态动画
	private SynAnimation iSynAnimation = null;
//	private float[] iAnimX;
//	private float[] iAnimY;
//	private int[] iAnimStartX;
//	private int[] iAnimStartY;
//	private int[] iAnimEndX;
//	private int[] iAnimEndY;
//	private float[] iAnimLineK;
//	private float[] iAnimSpeed;
//	private int[] iAnimLifeTick;	//飞心的生命周期
//	private int[] iStartAnimID;
//	private int[] iStartMotion;
//	private int[] iEndAnimID;
//	private int[] iEndMotion;
//	private String[] iAnimName;		
	
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
	private int[] iHartType;
	
	//摇手机时的骰子动画
	private int iDiceMovieState = 0;
	private int iDiceRunningTick = 0;
	private int[] iDiceMovieNum;
	private int[] iDiceMovieX;
	private int[] iDiceMovieY;
	private int[] iDiceMovieSpeedX;
	private int[] iDiceMovieSpeedY;
	
	//烟花动画
//	private int	  iYanHuaSpriteCount = -1;
//	private int	  iYanHuaSpriteDelay = 0;	
	
	//状态栏的骰子动画
	private boolean iBarDiceSet = false;
	private int	iBarDiceY = 0;
	private boolean iHideBarDice = false;
	private int iChangeDice = 0;	//改变后的骰子
	
	//状态栏的经验条动画
	private boolean iBarExpSet = false;
	private int iBarExpY = 0;
	private RectF iExpRect;
	private RectF iExpTitleRect;
	private boolean ibLockExp = false;	//升级时，锁定经验条
	
	//滚轮动画
	private boolean iWheelVisible = false;
	private boolean iWheelForceChop = false;
	private long iWheelPressTick = 0;	//按下滚轮的时间
	private int iWheelRunPart = 0;		//滚动部分
	private int iWheelRunDir = 0;		//滚动方向
	private int iWheelRunTick = 0;		//滚动时间
	private int iWheelCurrentNum = 0;	
	private int iWheelCurrentDice = 0;
	private int iWheelMinNum = 0;
	private int iWheelMaxNum = 0;
	private int iPressedY = 0;
	private int iTuchPart = 0;
	private int iLastTuchPart = 0;
	private Rect iWheelDiceRect = null;
	private Rect iWheelNumRect = null;
	private int iLeftOffset = 0;
	private int iRightOffset = 0;
	private int iLastLeftOffset = 0;
	private int iLastRightOffset = 0;
	private int iLeftDist = (int)(86 * ActivityUtil.ZOOM_Y);
	private int iRunSpeed = (int)(38 * ActivityUtil.ZOOM_Y);
	
	//记录音乐
	private int iMusicPos = 0;
	
	//弹出按钮的半径
	private int RADIUS = (int)(100 * ActivityUtil.PAK_ZOOM_Y);
	
	//闪电
	private final int MAX_POS_COUNT = 16;
	private float iLightStartX = 400;
	private float iLightStartY = 200;
	private float iLightX = 0;
	private float iLightY = 0;
	private int	iLightState = -1;
	private int iLightningType = 0;	//雷击类型
	private int iDesLightningID = 0; //雷击目标ID
	private int iLightningVip = 0;	//使用者的VIP级数
	private int iNextDesSeatID = -1; //被反弹雷击的目标座位ID
	private int iNextDesType = 0; //被反弹雷击的结果类型
	private float iNextLightStartX = 400;
	private float iNextLightStartY = 200;
	private float iNextLightX = 0;
	private float iNextLightY = 0;
	private int	iNextLightState = -1;
	private int iNextDesLightningID = 0;
	
	private int MAXLINES = 5;//最大雷击数
	private int CURLINES = 1;//
	private Lightning[] lightnings = null; //雷击
	private Lightning  lightning = null; //愤怒电
	
	private boolean createBMP = false;
	
	//超时处理
	final int MAX_DELAYCOUNT = 3;//最大超时次数
	int countDownAdd = 5;//超时加速
	int iOldMaxDelayTime = 20;
	int iCurrentMaxDelayTime = iOldMaxDelayTime;
	int iDelayCount = 0;
	int iRelDelay = 0;
	long iStartDelayTime = 0;
	
	private QuickSelectRoomScreen quickSelectRoomScreen;
	private InfoPopView infoPopView;
	//请求登录房间参数
	RoomData iRoomPair = null;
	
	public ClassicalGame() {
		iRoomPair = new RoomData(-1, -1, 5);
	}
	
	public ClassicalGame(RoomData data) {
		iRoomPair = new RoomData(data.roomID, data.tableID, data.roomType);
	}
	
	@Override
	public void init() {
		//释放主界面的Pak
		CPakManager.ReleaseUIPak();
		CPakManager.ReleaseMyHomePak();
		MyArStation.iImageManager.loadLoginBack();
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
		//初始化Player列表
		iPlayerList = new CGamePlayer[MAX_GAME_PLAYER];
		for(int i=0; i<MAX_GAME_PLAYER; i++) {
			iPlayerList[i] = new CGamePlayer();
			iPlayerList[i].init();
		}
		//
		iChangedSeatID = new int[MAX_GAME_PLAYER];
		//
//		iRealSeatID = new int[MAX_GAME_PLAYER];
		//
		iBrightDiceSprite = new Sprite[CGamePlayer.MAX_DICE_COUNT];
		for(int i=0; i<CGamePlayer.MAX_DICE_COUNT; i++) {
			iBrightDiceSprite[i] = new Sprite();
		}
		//
		iItemSprite = new Sprite[MAX_ITEM_SPRITE_TYPE];
		for(int i=0; i<MAX_ITEM_SPRITE_TYPE; i++) {
			iItemSprite[i] = new Sprite();
		}
		//
		iAnimSprite = new Sprite[MAX_ITEM_SPRITE_TYPE];
		for(int i=0; i<MAX_ITEM_SPRITE_TYPE; i++) {
			iAnimSprite[i] = new Sprite();
		}
		//
		iPlayingSeatIDs = new int[MAX_GAME_PLAYER];
		//
//		iAnimEndMotionList = new Vector<Integer>();//动态动画的动作
//		iAnimSpriteList = new Vector<Sprite>(); //动态动画
		//
		iHartSpriteList = new Vector<Sprite>(); //飘心动画
		//
		iLevelUpSpriteList = new Vector<Sprite>(); //升级动画
		//
		iItemSpriteList = new Vector<Sprite>(); //道具动画
		//
		iWeiSeSpriteList = new Vector<Sprite>(); //唯色加1动画列表
		//
		iSpriteButtonList = new Vector<SpriteButton>();//精灵按钮 列表
		//
		iPlayerButtonList = new Vector<SpriteButton>();
		//
//		iOtherButtonList = new Vector<SpriteButton>();
		//
		iWheelDiceRect = new Rect();
		iWheelNumRect = new Rect();
//		//释放主界面的Pak
//		GameMain.iPakManager.ReleaseUIPak();
//		GameMain.iPakManager.ReleaseMyHomePak();
		//载入房间Pak
		loadUIPak();
		//载入经验图片
		Bitmap bitmap = BitmapFactory.decodeResource(MyArStation.mGameMain.getResources(), R.drawable.exp3);
		iNinePatch = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
//		bitmap = BitmapFactory.decodeResource(GameMain.mGameMain.getResources(), R.drawable.exp2);
//	    iExpBmp = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
//		bitmap = null;
		//
		iTempRect = new RectF(0f,0f,0f,0f);
		//
		resetScreenOffset();
		//
		iMaxResultInfOffsetY *= ActivityUtil.ZOOM_Y;
		//
		iGbX = new float[MAX_GB_SPRITE];	//金币坐标
		iGbY = new float[MAX_GB_SPRITE];	//金币坐标
		iGbStartX = new int[MAX_GB_SPRITE];
		iGbStartY = new int[MAX_GB_SPRITE];
		iGbEndX = new int[MAX_GB_SPRITE];
		iGbEndY = new int[MAX_GB_SPRITE];
		iLineK = new float[MAX_GB_SPRITE];
		iGbSpeed  = new float[MAX_GB_SPRITE];	//金币坐标
		iLifeTick = new int[MAX_GB_SPRITE];
		//动态动画
//		iAnimX = new float[MAX_HART_SPRITE];
//		iAnimY = new float[MAX_HART_SPRITE];
//		iAnimStartX = new int[MAX_HART_SPRITE];
//		iAnimStartY = new int[MAX_HART_SPRITE];
//		iAnimEndX = new int[MAX_HART_SPRITE];
//		iAnimEndY = new int[MAX_HART_SPRITE];
//		iAnimLineK = new float[MAX_HART_SPRITE];
//		iAnimSpeed = new float[MAX_HART_SPRITE];
//		iAnimLifeTick = new int[MAX_HART_SPRITE];	//飞心的生命周期
//		iStartAnimID = new int[MAX_HART_SPRITE];
//		iStartMotion = new int[MAX_HART_SPRITE];
//		iEndAnimID = new int[MAX_HART_SPRITE];
//		iEndMotion = new int[MAX_HART_SPRITE];
//		iAnimName = new String[MAX_HART_SPRITE];	
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
		iHartType = new int[MAX_HART_SPRITE];	//心的类型
		//摇手机时的骰子动画
		iDiceMovieState = 0;
		iDiceMovieNum = new int[MAX_DICE_SPRITE];
		iDiceMovieX = new int[MAX_DICE_SPRITE];
		iDiceMovieY = new int[MAX_DICE_SPRITE];
		iDiceMovieSpeedX = new int[MAX_DICE_SPRITE];
		iDiceMovieSpeedY = new int[MAX_DICE_SPRITE];
		//经验条
		iExpRect = new RectF();
		iExpTitleRect = new RectF();
		setExpRect();
		//初始化系统控件
		messageEvent(GameEventID.ESPRITEBUTTON_EVENT_INIT_SYSTEM_BUTTON);
		//
//		iVibrator = ( Vibrator )iContext.getSystemService(Service.VIBRATOR_SERVICE);
		iShakeListener = new ShakeListener(MyArStation.getInstance().getApplicationContext());
		iShakeListener.setOnShakeListener(this);
		lightnings = new Lightning[MAXLINES];
		for(int i = 0; i < MAXLINES; i++) {
			lightnings[i] = new Lightning();
			lightnings[i].init(new Point((int)iLightStartX, (int)iLightStartY), new Point((int)iLightX, (int)iLightX));
		}
		lightning = new Lightning();
		lightning.init(new Point((int)iLightStartX, (int)iLightStartY), new Point((int)iLightX, (int)iLightX));
		quickSelectRoomScreen = new QuickSelectRoomScreen(1);
		quickSelectRoomScreen.init();
		quickSelectRoomScreen.setOnSelectRoom(this);
		infoPopView = new InfoPopView();
		infoPopView.init();
		//动态动画
		iSynAnimation = new SynAnimation();
		iSynAnimation.init();
		//发送登录房间请求
		requestLoginRoom(iRoomPair);
	}

	/**
	 * 
	 */
	private void resetScreenOffset() {
		iSDX = -500;
		//转成对应屏幕分辨率的值
		iSDX *= (float)ActivityUtil.SCREEN_WIDTH / (float)ActivityUtil.SCREEN_WIDTH_STANDARD; 
		iSDSpeed = (int)(iSDX / 6);
		//
		iSBX = -iSDX;		//控制SceneBar的坐标
		iSBSpeed = -iSDSpeed;
		iDrunkTextSkewX = 30;
	}
	
	private void initSystemButton() {
		// TODO Auto-generated method stub
        initViews();
        initChatView();
	}
	
	private void initAnimation() {
		// TODO Auto-generated method stub
//    	RotateAnimation outRotaAni = new RotateAnimation(0, 360, 0, 0);
//    	outRotaAni.setDuration(ANIMATION_TIME);
//    	RotateAnimation inRotaAni = new RotateAnimation(360, 0, 0, 0);
//    	inRotaAni.setDuration(ANIMATION_TIME);
    	//计算动画
    	int size = mList.size();
    	int angleIndex = 0;
    	for (int i = 0; i < size; i ++) {
    		final Button button = mList.get(i);
    		int x;
    		int y;
    		angleIndex = button.getId();
    		double angle = angleIndex * 60;
			x = (int) (RADIUS * Math.sin(Math.toRadians(angle)));
			y = (int) (RADIUS * Math.cos(Math.toRadians(angle)));
			mButtonEndX[button.getId()] = mButtonStartX + x * 8 / 9;
			mButtonEndY[button.getId()] = mButtonStartY - y * 8 / 9;
    		long time = ANIMATION_TIME - i * TIME_INTERVAL;
    		//
    		TranslateAnimation outTranAni = new TranslateAnimation(0, x ,0 , -y);
    		outTranAni.setDuration(time);
    		AnimationSet outSet = new AnimationSet(true);
    		outSet.addAnimation(outTranAni);
    		outSet.setFillAfter(true);
    		mOutAnimatinSets.add(outSet);
    		//
    		final AnimationSet outAfterSet = new AnimationSet(true);
    		TranslateAnimation outAfterTranAni = new TranslateAnimation(x, x * 8 / 9  ,-y , -y * 8 / 9);
    		outAfterTranAni.setDuration(time);
    		outAfterSet.addAnimation(outAfterTranAni);
    		outAfterSet.setFillAfter(true);
    		outSet.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationEnd(Animation animation) {
//					button.startAnimation(outAfterSet);
					Button pB = mList.get(mAnimToButton[button.getId()]);
					pB.setAnimation(null);
					LayoutParams lp = (LayoutParams)pB.getLayoutParams();
					lp.leftMargin = mButtonEndX[button.getId()];
					lp.topMargin = mButtonEndY[button.getId()];
					pB.setLayoutParams(lp);
					iOutCompleteCount ++;	//结束向外动画的个数
//					System.out.println("IconButton out end mAnimToButton[" + button.getId()+ "]=" 
//							+ mAnimToButton[button.getId()]
//							+ " lpX=" + lp.x + " lpY=" + lp.y);
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				@Override
				public void onAnimationStart(Animation animation) {
//					System.out.println("IconButton out start 111111111111-----------------");
				}
    		});
    		outAfterSet.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationEnd(Animation animation) {
//					Button pB = mList.get(mAnimToButton[button.getId()]);
//					pB.setAnimation(null);
//					LayoutParams lp = (LayoutParams)pB.getLayoutParams();
//					lp.x = mButtonEndX[button.getId()];
//					lp.y = mButtonEndY[button.getId()];
//					pB.setLayoutParams(lp);
//					iOutCompleteCount ++;	//结束向外动画的个数
//					System.out.println("IconButton out end mAnimToButton[" + button.getId()+ "]=" 
//							+ mAnimToButton[button.getId()]
//							+ " lpX=" + lp.x + " lpY=" + lp.y);
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				@Override
				public void onAnimationStart(Animation animation) {
				}
    		});
    		//
    		TranslateAnimation inTranAni = new TranslateAnimation(x * 8 / 9, 0 , -y * 8 / 9 , 0);
    		inTranAni.setDuration(time);
    		AnimationSet inSet = new AnimationSet(true);
    		inSet.addAnimation(inTranAni);
    		inSet.setFillAfter(true);
    		mInAnimatinSets.add(inSet);
    		inTranAni.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationEnd(Animation animation) {
//					button.setAnimation(null);
//					button.setVisibility(View.GONE);
					Button pB = mList.get(mAnimToButton[button.getId()]);
					pB.setAnimation(null);
					pB.setVisibility(View.GONE);
					iOutCompleteCount ++;
					if(iOutCompleteCount >= iOutStartCount) {
						iAbLayout.setVisibility(View.GONE);
						iIconButtonShow = false;
					}
//					System.out.println("IconButton in end 22222222222222-----------------");
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				@Override
				public void onAnimationStart(Animation animation) {
//					System.out.println("IconButton in start 111111111111-----------------");
				}
    		});
		}
	}

	private void initViews() {
		// TODO Auto-generated method stub
		MyArStation.mGameMain.mainLayout.removeAllViews();
		RelativeLayout temp = (RelativeLayout) LayoutInflater.from(MyArStation.mGameMain.getApplicationContext()).inflate(R.layout.new_classicalgame, null);
		MyArStation.mGameMain.mainLayout.addView(temp);
		iAbLayout = (RelativeLayout) temp.findViewById(R.id.classicalgame);
		iLyW = (int)(260 * ActivityUtil.ZOOM_Y);
		iLyH = (int)(260 * ActivityUtil.ZOOM_Y);
		LayoutParams lp = new LayoutParams(iLyW, iLyH);
		lp.leftMargin = 0;
		lp.topMargin = 0;
		iAbLayout.setLayoutParams(lp);
		iAbLayout.setId(ICON_LAYOUT_ID);
		iAbLayout.setBackgroundColor(Color.TRANSPARENT);//(Color.GRAY);//
		iAbLayout.setOnTouchListener(this);
		lp = null;
		
		int btW = (int)(50 * ActivityUtil.ZOOM_Y);
		int btH = (int)(50 * ActivityUtil.ZOOM_Y);
		iIconButtonW = btW;
		iIconButtonH = btH;
		
		mButtonStartX = (iLyW>>1)-(btW>>1);
		mButtonStartY = (iLyH>>1)-(btH>>1);
		mButtonEndX = new int[MAX_ICON_BUTTON_COUNT];
		mButtonEndY = new int[MAX_ICON_BUTTON_COUNT];
		mAnimToButton = new int[MAX_ICON_BUTTON_COUNT];
		mButtonToAnim = new int[MAX_ICON_BUTTON_COUNT];
		
		for(int i=0; i<MAX_ICON_BUTTON_COUNT; i++) {
			Button pButton = new Button(MyArStation.getInstance().getApplicationContext());
			pButton.setId(ICON_BUTTON_ID_BOTTLE + i);
//			pButton.setOnClickListener(this);
			pButton.setOnTouchListener(this);
			pButton.setBackgroundResource(R.drawable.icon_bottle + i);
			pButton.setVisibility(View.GONE);
			LayoutParams pLp = 
				new LayoutParams(btW, btH);
			pLp.leftMargin = (iLyW>>1)-(btW>>1);
			pLp.topMargin = (iLyH>>1)-(btH>>1);
			pButton.setLayoutParams(pLp);
			iAbLayout.addView(pButton);
			pLp = null;
			mList.add(pButton);
		}
		iIconButtonShow = false;
		//创建TextView
//		int textViewW = 100;
//		int textViewH = 100;
//		TextView pTextView = new TextView(GameMain.getInstance().getApplicationContext());
//		pTextView.setBackgroundResource(R.drawable.chat);
//		pTextView.setSingleLine(false);
//		LayoutParams pLp = 
//			new LayoutParams(textViewW, LayoutParams.WRAP_CONTENT, (iLyW>>1)-(btW>>1), (iLyH>>1)-(btH>>1));
//		pTextView.setLayoutParams(pLp);
//		pTextView.setVisibility(View.GONE);
//		mTextViewList.add(pTextView);
//		System.out.println("IconButton init iAbLayout view count =" + iAbLayout.getChildCount() + " 111111111111111111");
	}
	
	private void startInAnimation() {
		iOutStartCount = 0;		//开始向外动画的个数
		iOutCompleteCount = 0;	//结束向外动画的个数
		for (int i = 0; i < mList.size(); i ++) {
			Button button = mList.get(i);
			//复位坐标
			LayoutParams lp = (LayoutParams)button.getLayoutParams();
			lp.width = iIconButtonW;
			lp.height = iIconButtonH;
			lp.leftMargin = mButtonStartX;
			lp.topMargin = mButtonStartY;
			button.setLayoutParams(lp);
			if(button.getVisibility() == View.VISIBLE) {
				//设置动画
				button.startAnimation(mInAnimatinSets.get(iOutStartCount));
				iOutStartCount ++;
			}
			else {
				button.setAnimation(null);
			}
		}
	}

	private void startOutAnimation() {
		iOutStartCount = 0;		//开始向外动画的个数
		iOutCompleteCount = 0;	//结束向外动画的个数
		for (int i = 0; i < mList.size(); i ++) {
			Button button = mList.get(i);
			button.setAnimation(null);
			if(button.getVisibility() == View.VISIBLE) {
				button.startAnimation(mOutAnimatinSets.get(iOutStartCount));
				mAnimToButton[iOutStartCount] = button.getId();
				mButtonToAnim[button.getId()] = iOutStartCount;
//				System.out.println("IconButton mAnimToButton[" + iOutStartCount +"]=" + button.getId());
				iOutStartCount ++;
			}
		}
	}

	@Override
	public void releaseResource() {
		//恢复房间的默认背景图
		//GameMain.iImageManager.releaseRoomBack();
		//
		iActivityConcours = null;
		iRoomPair = null;
		//
		iWheelNumRect = null;
		iWheelDiceRect = null;
		iDiceMovieNum = null;
		iDiceMovieX = null;
		iDiceMovieY = null;
		iDiceMovieSpeedX = null;
		iDiceMovieSpeedY = null;
		iGbX = null;	//金币坐标
		iGbY = null;	//金币坐标
		iGbStartX = null;
		iGbStartY = null;
		iGbEndX = null;
		iGbEndY = null;
		iLineK = null;
		iGbSpeed = null;
		iLifeTick = null;	
		iHartX = null;
		iHartY = null;
		iHartStartX = null;
		iHartStartY = null;
		iHartEndX = null;
		iHartEndY = null;
		iHartLineK = null;
		iHartSpeed = null;
		iHartLifeTick = null;	//飞心的生命周期		
		iHartType = null;
		//动态动画
//		iAnimX = null;
//		iAnimY = null;
//		iAnimStartX = null;
//		iAnimStartY = null;
//		iAnimEndX = null;
//		iAnimEndY = null;
//		iAnimLineK = null;
//		iAnimSpeed = null;
//		iAnimLifeTick = null;
//		iStartAnimID = null;
//		iStartMotion = null;
//		iEndAnimID = null;
//		iEndMotion = null;
//		iAnimName = null;
//		iBottleX = null;
//		iBottleY = null;
//		iBottleStartX = null;
//		iBottleStartY = null;
//		iBottleEndX = null;
//		iBottleEndY = null;
//		iBottleLineK = null;
//		iBottleSpeed = null;
//		iBottleLifeTick = null;	//飞心的生命周期		
//		iBottleType = null;
		iTempRect = null;
		if(iOvalTimerCount != null) {
			iOvalTimerCount.stop();
			iOvalTimerCount = null;
		}
//		iExpBmp = null;
		iNinePatch = null; //经验条
		iBackBmp = null;
		iLightningButton = null;
		iZhaiButton = null;
		iFaceButton = null;
		iMenuButton = null;
		iChatButton = null;
		iGiftBUtton = null;
		iRebelChopButton = null;
		iPreChopButton = null;
		iPlayerButtonList = null;
		iSpriteButtonList = null;
		iHartSpriteList = null;
//		iAnimEndMotionList = null;//动态动画的动作
//		iAnimSpriteList = null; //动态动画
		iSynAnimation = null;
		iLevelUpSpriteList = null;
		iWeiSeSpriteList = null;
		iItemSpriteList = null;
//		iZhaiSprite = null;
	
		iNextLightningExp = null; //反弹雷击爆炸
		iLightningExp = null; //雷击爆炸
		
		iPiKongSprite = null;
		iWeiSeSprite = null;
		iChopSprite = null;  //劈动画
		iRebelChopSprite = null; //反劈动画
//		iCupSprite = null;	//骰盅动画
		iBigCupSprite = null; // 大骰盅动画
		iShoutSprite = null;	
		iDrunkSprite = null;
		iNumSprite = null;
		iBigNumSprite = null;
		iHartSprite = null;	//飞心动画
//		iBottleSprite = null; //啤酒瓶动画
		iBeDingSprite = null; //被顶动画
		iLoveSprite = null;	//爱心动画
		iLevelUpSprite = null; //升级动画		
		iDoubleSprite = null; //加倍动画
		iWinnerSprite = null;
		iStartSprite = null;
		iDrinkSprite = null;
		iGbSprite = null;
		iXSprite = null;
		iSceneExp = null;
		iSceneDesk = null;
		iSceneWheel = null;
		iSceneOther = null;
		iSceneBar = null;
		iSceneDeskEx = null;
		iLightningPak = null;
		iZhaiPak = null;
		iPiKongPak = null;
		iWeiSePak = null;
		for(int i=0; i<MAX_ITEM_SPRITE_TYPE; i++) {
			iItemPak[i] = null;
		}
		iItemPak = null;
		iFacePak = null;
//		iDeskPak = null;
		iUIPak = null;
		iPlayingSeatIDs = null;
		for(int i=0; i<iBrightDiceSprite.length; i++) {
			iBrightDiceSprite[i] = null;
		}
		iBrightDiceSprite = null;
		for(int i=0; i<iItemSprite.length; i++) {
			iItemSprite[i] = null;
		}
		iItemSprite = null;
//		for(int i=0; i<iModelSprite.length; i++) {
//			iModelSprite[i] = null;
//		}
//		iModelSprite = null;
//		iRealSeatID = null;
		iChangedSeatID = null;
		//
		for(int i=0; i<iPlayerList.length; i++) {
			iPlayerList[i].releaseResource();
			iPlayerList[i] = null;
		}
		iPlayerList = null;
		//
		mButtonToAnim = null;
		mAnimToButton = null;
		mButtonEndX = null;
		mButtonEndY = null;
		int size = mOutAnimatinSets.size();
		for(int i=0; i<size; i++) {
			mOutAnimatinSets.remove(0);
			mInAnimatinSets.remove(0);
		}
		mOutAnimatinSets = null;
		mInAnimatinSets = null;
		size = mList.size();
		for(int i=0; i<size; i++) {
			Button button = mList.get(i);
	    	button.setAnimation(null);
		}
		for(int i=0; i<size; i++) {
			mList.remove(0);
		}
		mList = null;
		if(iAbLayout != null) {
			iAbLayout.removeAllViews();
			iAbLayout.removeAllViewsInLayout();
		}
		iAbLayout = null;
		if(iChatView != null) {
			iChatView.dismissAll();
		}
		iChatView = null;
		if(iBuyWineDialog != null) {
			iBuyWineDialog.dismiss();
			iBuyWineDialog = null;
		}
//		if(iPlayerInfDialog != null) {
//			iPlayerInfDialog.dismiss();
//			iPlayerInfDialog = null;
//		}
		if(iGameMenuDialog != null) {
			iGameMenuDialog.dismiss();
			iGameMenuDialog = null;
		}
		if(iConcoursDialog != null) {
			iConcoursDialog.dismiss();
			iConcoursDialog = null;
		}
		if(iShakeListener != null) {
			iShakeListener.stopShake();
			iShakeListener = null;
		}
		if(eventSelectDialog != null) {
			eventSelectDialog.dismiss();
			eventSelectDialog = null;
		}
		if(feedBackDialog != null) {
			feedBackDialog.dismiss();
			feedBackDialog = null;
		}
//		if(stroeDialog != null) {
//			stroeDialog.dismiss();
//			stroeDialog = null;
//		}
		if(dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		quickSelectRoomScreen = null;
		infoPopView = null;
		missionView = null;
		if(iDefualPhtopBitmap != null) {
			iDefualPhtopBitmap.recycle();
		}
		iDefualPhtopBitmap = null;
	}
	
	
	//飞心动画
	private void paintHartSprite(Canvas g) {
		//道具动画
		for(int i=0; i<iItemSpriteList.size(); i++) {
			Sprite tempSprite = iItemSpriteList.get(i);
			tempSprite.paintStatic(g, 1, tempSprite.getX(), tempSprite.getY());
		}
		//动态动画
		if(iSynAnimation != null) iSynAnimation.paintScreen(g, null);
//		for(int i=0; i<iAnimSpriteList.size(); i++) {
//			Sprite tempSprite = iAnimSpriteList.get(i);
//			int tEndMotion = iAnimEndMotionList.get(i);
//			tempSprite.paintStatic(g, tEndMotion, tempSprite.getX(), tempSprite.getY());
//		}
//		for(int i=0; i<MAX_ANIM_SPRITE; i++) {
//			if(iAnimLifeTick[i] <= 0) {
//				continue;
//			}
//			//
//			iAnimSprite[i].paintAction(g, iStartMotion[i], (int)iAnimX[i], (int)iAnimY[i]);//iStartMotion[i],
//		}
		//飞心动画
		for(int i=0; i<MAX_HART_SPRITE; i++) {
			if(iHartLifeTick[i] <= 0) {
				continue;
			}
			if(iHartType[i] < 10) {
				iHartSprite.paint(g, (int)iHartX[i], (int)iHartY[i]);
			}
			else {
				iItemSprite[iHartType[i]-10].paint(g, (int)iHartX[i], (int)iHartY[i]);
			}
		}
		//飘心动画
		for(int i=0; i<iHartSpriteList.size(); i++) {
			Sprite tempSprite = iHartSpriteList.get(i);
			tempSprite.paintStatic(g, 0, tempSprite.getX(), tempSprite.getY());
		}
		//升级动画
		for(int i=0; i<iLevelUpSpriteList.size(); i++) {
			Sprite tempSprite = iLevelUpSpriteList.get(i);
			tempSprite.paintStatic(g, 0, tempSprite.getX(), tempSprite.getY());
//			System.out.println("LevelUp paint Sprite x=" + tempSprite.getX() + " y=" + tempSprite.getY());
		}
		//闪电
		if(iLightState >= 0) {
			int tEndCount = MAX_POS_COUNT-1;
			if(iLightState == 0) {
				tEndCount >>= 2;
			}
			else if(iLightState == 1) {
				tEndCount >>= 1;
			}
			else if(iLightState == 2) {
				tEndCount = (MAX_POS_COUNT >> 1);
				tEndCount += (MAX_POS_COUNT >> 2);
			}
			if(iLightState < 6) {
//				if(iLightningVip > 0) {
//					for(int i=0; i<tEndCount; i++) {
//						g.drawLine(iLinesX[i], iLinesY[i], iLinesX[i+1], iLinesY[i+1], ActivityUtil.mAlphaLineC);
//					}
//				}
//				else {
//					for(int i=0; i<tEndCount; i++) {
//						g.drawLine(iLinesX[i], iLinesY[i], iLinesX[i+1], iLinesY[i+1], ActivityUtil.mAlphaLine);
//					}
//				}
//				for(int i=0; i<tEndCount; i++) {
//					g.drawLine(iLinesX[i], iLinesY[i], iLinesX[i+1], iLinesY[i+1], ActivityUtil.mAlphaLineB);
//				}
				for(int i = 0; i < CURLINES; i++) {
					lightnings[i].draw(g);
				}
			}
			if(iLightState > 3 && iLightState < 6) {
				g.drawCircle(iLightX, iLightY, 4, ActivityUtil.mAlphaLineB);
			}
		}
		//反弹闪电
		if(iNextLightState >= 0) {
			int tEndCount = MAX_POS_COUNT-1;
			if(iNextLightState == 0) {
				tEndCount >>= 2;
			}
			else if(iNextLightState == 1) {
				tEndCount >>= 1;
			}
			else if(iNextLightState == 2) {
				tEndCount = (MAX_POS_COUNT >> 1);
				tEndCount += (MAX_POS_COUNT >> 2);
			}
			if(iNextLightState < 6) {
//				if(iLightningVip > 0) {
//					for(int i=0; i<tEndCount; i++) {
//						g.drawLine(iNextLinesX[i], iNextLinesY[i], iNextLinesX[i+1], iNextLinesY[i+1], ActivityUtil.mAlphaLineC);
//					}
//				}
//				else {
//					for(int i=0; i<tEndCount; i++) {
//						g.drawLine(iNextLinesX[i], iNextLinesY[i], iNextLinesX[i+1], iNextLinesY[i+1], ActivityUtil.mAlphaLine);
//					}
//				}
//				for(int i=0; i<tEndCount; i++) {
//					g.drawLine(iNextLinesX[i], iNextLinesY[i], iNextLinesX[i+1], iNextLinesY[i+1], ActivityUtil.mAlphaLineB);
//				}
				lightnings[0].draw(g);
			}
			if(iNextLightState > 3 && iNextLightState < 6) {
				g.drawCircle(iNextLightX, iNextLightY, 4, ActivityUtil.mAlphaLineB);
			}
		}
	}
	
	private void paintPlayer(Canvas g) {
		//画玩家
		for(int i=0; i<MAX_GAME_PLAYER; i++) {
			if(iPlayerList[i].ibInit == false) {
				continue;
			}
			if(iPlayerList[i].iSprite == null 
					|| iPlayerList[i].iCupSprite == null) {
				continue;
			}
			if(iPlayerList[i].iBmp == null) {
				continue;
			}
			//骰盅
			//如果后台状态不在等待玩家准备，就把骰盅画到Player下面
			if(iPlayerList[i].iCupMotionID == 0 || iPlayerList[i].iCupMotionID == 3) {
				iPlayerList[i].iCupSprite.paintAction(g, 
						iPlayerList[i].iCupMotionID,
						(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX), 
						(int)(iPlayerList[iChangedSeatID[i]].iCupY));
			}
			if(iPlayerList[i].iGameState == CGamePlayer.PLAYER_STATE_PLAYING
					&& iPlayerList[i].iPlayingState == CGamePlayer.PLAYING_STATE_SHOUTING) {
				//倒计时
				int tempR = iPlayerList[i].iBmp.getHeight();
				if(iOvalTimerCount != null) {
					iOvalTimerCount.draw(g, 
						(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX-tempR), 
						(int)(iPlayerList[iChangedSeatID[i]].iY-iPlayerList[i].iBmp.getHeight()-(iPlayerList[i].iBmp.getHeight()>>1)));
				}
			}
			if(!iLightningExpEnd && i == iDesLightningID) {
				 if(iLightningType == 0) {
						//人物头像,正在出场
						iPlayerList[iChangedSeatID[i]].iSprite.paintActionBmp(
							g, 
							(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX), 
							(int)(iPlayerList[iChangedSeatID[i]].iY),
							iPlayerList[i].iMotionID,
							iPlayerList[i].iBmp);
				 }
				iLightningExp.paintStatic(g, iLightningType, (int)iLightX, (int)iLightY);
			}
			else if(!iNextLightningExpEnd && i == iNextDesLightningID) {
				 if(iNextDesType == 0) {
						//人物头像,正在出场
						iPlayerList[iChangedSeatID[i]].iSprite.paintActionBmp(
							g, 
							(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX), 
							(int)(iPlayerList[iChangedSeatID[i]].iY),
							iPlayerList[i].iMotionID,
							iPlayerList[i].iBmp);
				 }
				iNextLightningExp.paintStatic(g, iNextDesType, (int)iNextLightX, (int)iNextLightY);	
			}
			else {
				if(iPlayerList[i].iMotionID == CGamePlayer.MOTION_STATE_SITDOWNING 
						&& iPlayerList[i].iCarBmp != null) {
					//人物头像,正在出场
					iPlayerList[iChangedSeatID[i]].iSprite.paintActionBmp(
						g, 
						(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX), 
						(int)(iPlayerList[iChangedSeatID[i]].iY),
						iPlayerList[i].iMotionID,
						iPlayerList[i].iCarBmp);
//					iPlayerList[iChangedSeatID[i]].iSprite.paintAction(g, 
//							iPlayerList[i].iMotionID,
//							(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX), 
//							(int)(iPlayerList[iChangedSeatID[i]].iY));
				}
				else {
					iPlayerList[iChangedSeatID[i]].iSprite.paintActionBmp(
							g, 
							(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX), 
							(int)(iPlayerList[iChangedSeatID[i]].iY),
							iPlayerList[i].iMotionID,
							iPlayerList[i].iBmp);
				}
			}
			if(!iLightningExpEnd && i == iDesLightningID && iLightningType > 0) {
			}
			else if(!iNextLightningExpEnd && i == iNextDesLightningID && iNextDesType > 0) {
			}
			else {
				//Buff
				int tempOffsetY = 0;
				if(iPlayerList[i].ibKillOrder) { //追杀令
					g.drawBitmap(MyArStation.iImageManager.iStateBuffBmps[MyArStation.iImageManager.IMG_BUFF_KILLORDER],
							(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX+(iPlayerList[i].iBmp.getWidth()>>1)),
							(int)(iPlayerList[iChangedSeatID[i]].iY-MyArStation.iImageManager.iStateBuffBmps[MyArStation.iImageManager.IMG_BUFF_KILLORDER].getHeight()),
							null);
					tempOffsetY = MyArStation.iImageManager.iStateBuffBmps[MyArStation.iImageManager.IMG_BUFF_KILLORDER].getHeight();
				}
				if(iPlayerList[i].ibShield) { //反弹盾
					g.drawBitmap(MyArStation.iImageManager.iStateBuffBmps[MyArStation.iImageManager.IMG_BUFF_SHIELD],
							(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX+(iPlayerList[i].iBmp.getWidth()>>1)),
							(int)(iPlayerList[iChangedSeatID[i]].iY-MyArStation.iImageManager.iStateBuffBmps[MyArStation.iImageManager.IMG_BUFF_SHIELD].getHeight()-tempOffsetY),
							null);
					tempOffsetY += MyArStation.iImageManager.iBuffBmps[MyArStation.iImageManager.IMG_BUFF_SHIELD].getHeight();
				}
				if(iPlayerList[i].ibUnArmed) {//被禁劈
					g.drawBitmap(MyArStation.iImageManager.iStateBuffBmps[MyArStation.iImageManager.IMG_BUFF_UNARMED],
							(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX+(iPlayerList[i].iBmp.getWidth()>>1)),
							(int)(iPlayerList[iChangedSeatID[i]].iY-MyArStation.iImageManager.iStateBuffBmps[MyArStation.iImageManager.IMG_BUFF_UNARMED].getHeight()-tempOffsetY),
							null);
					tempOffsetY += MyArStation.iImageManager.iBuffBmps[MyArStation.iImageManager.IMG_BUFF_SHIELD].getHeight();
				}
				if(iPlayerList[i].ibCrazed) {//是否暴走状态
					g.drawBitmap(MyArStation.iImageManager.iStateBuffBmps[MyArStation.iImageManager.IMG_BUFF_CRAZED],
							(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX+(iPlayerList[i].iBmp.getWidth()>>1)),
							(int)(iPlayerList[iChangedSeatID[i]].iY-MyArStation.iImageManager.iStateBuffBmps[MyArStation.iImageManager.IMG_BUFF_CRAZED].getHeight()-tempOffsetY),
							null);
					//tempOffsetY = GameMain.iImageManager.iStateBuffBmps[GameMain.iImageManager.IMG_BUFF_KILLORDER].getHeight();
				}
				//醉酒度
				iTempRect.set((iSDX+iPlayerList[iChangedSeatID[i]].iX-(iPlayerList[i].iBmp.getWidth()>>1)), 
						(iPlayerList[iChangedSeatID[i]].iY-2-ActivityUtil.TEXTSIZE_NORMAL), 
						(iSDX+iPlayerList[iChangedSeatID[i]].iX+(iPlayerList[i].iBmp.getWidth()>>1)),
						(iPlayerList[iChangedSeatID[i]].iY));
				g.drawRoundRect(iTempRect, 4f, 4f, ActivityUtil.mAlphaRect);
				iDrunkSprite.paint(g,
						(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX-iDrunkSprite.getSpriteWidth()-(iDrunkSprite.getSpriteWidth()>>1)),
						(int)(iPlayerList[iChangedSeatID[i]].iY-2-ActivityUtil.TEXTSIZE_NORMAL));
				g.drawText(""+iPlayerList[i].iCurrentDrunk+"/5", 
						(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX-(iDrunkSprite.getSpriteWidth()>>1)), 
						(int)(iPlayerList[iChangedSeatID[i]].iY-2), 
						ActivityUtil.mDrunk);
				//名字
				if(iPlayerList[i].iVipLevel > 0) {
					g.drawText(iPlayerList[i].stUserNick, 
							(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX), 
							(int)(iPlayerList[iChangedSeatID[i]].iY+ActivityUtil.TEXTSIZE_NORMAL), 
							ActivityUtil.mVipPlayerName);
				}
				else {
					g.drawText(iPlayerList[i].stUserNick, 
							(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX), 
							(int)(iPlayerList[iChangedSeatID[i]].iY+ActivityUtil.TEXTSIZE_NORMAL), 
							ActivityUtil.mPlayerName);
				}
			}
			//旁观状态
			if(iPlayerList[i].iGameState >= CGamePlayer.PLAYER_STATE_LOOK
					&& iPlayerList[i].iGameState != CGamePlayer.PLAYER_STATE_RESULT) {
				//喊点信息框
				iShoutSprite.paintAction(g,
					2,
					(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX),
					(int)(iPlayerList[iChangedSeatID[i]].iY-iPlayerList[i].iBmp.getHeight()));		
			}
		}
	}
	
	private void paintPlaying(Canvas g) {
		//画玩家
		for(int i=0; i<MAX_GAME_PLAYER; i++) {
			if(iPlayerList[i].ibInit == false) {
				continue;
			}
			if(iPlayerList[i].iSprite == null 
					|| iPlayerList[i].iCupSprite == null) {
				continue;
			}
			if(iPlayerList[i].iBmp == null) {
				continue;
			}
			//喊点信息框
			if(iPlayerList[i].iGameState == CGamePlayer.PLAYER_STATE_PLAYING) {
				if(iPlayerList[i].iPlayingState == CGamePlayer.PLAYING_STATE_SHOUTED) {
					if(!iLightningExpEnd && i == iDesLightningID && iLightningType > 0) {
					}
					else if(!iNextLightningExpEnd && i == iNextDesLightningID && iNextDesType > 0) {
					}
					else {
						int tempOffsetX = 0;
						if(iWheelVisible && iChangedSeatID[i] == 5) {
							tempOffsetX = (int)(-24*ActivityUtil.ZOOM_X);
						}
						//喊点信息框
						iShoutSprite.paintAction(g,
							iPlayerList[i].ibShoutOne ? 1 : 0,
							(int)(iSDX+tempOffsetX+iPlayerList[iChangedSeatID[i]].iX),
							(int)(iPlayerList[iChangedSeatID[i]].iY-iPlayerList[i].iBmp.getHeight()));
						iTempRect.set(iSDX+tempOffsetX+iPlayerList[iChangedSeatID[i]].iX+iShoutSprite.getSpriteLeft(),
							iPlayerList[iChangedSeatID[i]].iY-iPlayerList[i].iBmp.getHeight()+iShoutSprite.getSpriteTop(),
							iSDX+tempOffsetX+iPlayerList[iChangedSeatID[i]].iX+iShoutSprite.getSpriteLeft()+iShoutSprite.getSpriteWidth(),
							iPlayerList[iChangedSeatID[i]].iY+iShoutSprite.getSpriteTop()+iShoutSprite.getSpriteHeight());
						iTempRect.inset(4, 2);
						//喊的内容
						int tenNum = iPlayerList[i].iLastShoutCount / 10;
						int bitNum = iPlayerList[i].iLastShoutCount % 10;
						int offsetX = (int)(iSDX+tempOffsetX+iPlayerList[iChangedSeatID[i]].iX-(iPlayerList[i].iBmp.getWidth()>>2));
						offsetX -= (iNumSprite.getSpriteWidth()>>1);
						int tempY = (int)iTempRect.top - 1 + (iShoutSprite.getSpriteHeight()-(iShoutSprite.getSpriteHeight()>>4)-(iShoutSprite.getSpriteHeight()>>3)+iNumSprite.getSpriteHeight()>>1);
						if(tenNum > 0) {
							iNumSprite.paintAction(g, 
									tenNum, 
									offsetX, 
									tempY); 
							offsetX += iNumSprite.getSpriteWidth();
						}
						iNumSprite.paintAction(g, 
							bitNum, 
							offsetX, 
							tempY); 
						//
						offsetX += iNumSprite.getSpriteWidth();	
						if(offsetX < (int)(iSDX+tempOffsetX+iPlayerList[iChangedSeatID[i]].iX)) {
							offsetX = (int)(iSDX+tempOffsetX+iPlayerList[iChangedSeatID[i]].iX);
						}
						tempY = (int)iTempRect.top - 1 + (iShoutSprite.getSpriteHeight()-(iShoutSprite.getSpriteHeight()>>4)-(iShoutSprite.getSpriteHeight()>>3)+iXSprite.getSpriteHeight()>>1);
						iXSprite.paint(g, 
							offsetX,
							tempY);//(int)(iPlayerList[i].iY-iPlayerList[i].iBmp.getHeight()-(iShoutSprite.getSpriteHeight()>>1)+((int)ActivityUtil.TEXTSIZE_SHOUT_DICE>>1)));
						//骰子图片
						int tempDiceModelID = iPlayerList[i].iDiceID;
						int tempDice = iPlayerList[i].iLastShoutDice - 1;
						tempY = (int)iTempRect.top - 1 + (iShoutSprite.getSpriteHeight()-(iShoutSprite.getSpriteHeight()>>4)-(iShoutSprite.getSpriteHeight()>>3)-MyArStation.iImageManager.iLittleDiceBmps[0][0].getHeight()>>1);
						g.drawBitmap(MyArStation.iImageManager.iLittleDiceBmps[tempDiceModelID][tempDice],
							iTempRect.right-MyArStation.iImageManager.iLittleDiceBmps[0][0].getWidth(),
							tempY,
							null);
					}
				}
				else if(iPlayerList[i].iPlayingState == CGamePlayer.PLAYING_STATE_CHOPING) {
					//劈动画
					iChopSprite.paint(g,
						(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX),
						(int)(iPlayerList[iChangedSeatID[i]].iY-iPlayerList[i].iBmp.getHeight()));
				}
				else if(iPlayerList[i].iPlayingState == CGamePlayer.PLAYING_STATE_REBEL_CHOPING) {
					//反劈动画
					iRebelChopSprite.paint(g,
						(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX),
						(int)(iPlayerList[iChangedSeatID[i]].iY-iPlayerList[i].iBmp.getHeight()));
				}
				//星星动画
				iStartSprite.paint(g, (int)(428*ActivityUtil.PAK_ZOOM_X), (int)(456*ActivityUtil.PAK_ZOOM_Y));
			}
		}
		//基准点
//		g.drawRect((int)(iSDX+iPlayerList[i].iX-1), 
//			(int)(iPlayerList[i].iY-1), 
//			(int)(iSDX+iPlayerList[i].iX+1), 
//			(int)(iPlayerList[i].iY+1), 
//			ActivityUtil.mPaint);
		//显示自己的骰子
		if(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING) {
			if((iHideBarDice || iBarDiceSet == false)) {
				int tempDiceX = 0;
				//骰子图片
				int tempDiceModelID = iPlayerList[iSelfSeatID].iDiceID;
				int tempNextDiceCount = iPlayerList[iSelfSeatID].iNextDiceCount;
			//	System.out.print("test tempDiceModelID=" + tempDiceModelID);
			//	System.out.print("test tempNextDiceCount=" + tempNextDiceCount);
				for(int i=0; i<CGamePlayer.MAX_DICE_COUNT; i++) {
					int tempDice = iPlayerList[iSelfSeatID].iDices[i] - 1;
					if(i >= tempNextDiceCount) {
						g.drawBitmap(MyArStation.iImageManager.iDiceBmps[tempDiceModelID][tempDice],
							tempDiceX,
							iBarDiceY-MyArStation.iImageManager.iDiceBmps[0][0].getHeight(),
							null);
					}
					else {
						g.drawBitmap(MyArStation.iImageManager.iDiceBmps[tempDiceModelID+1][tempDice],
								tempDiceX,
								iBarDiceY-MyArStation.iImageManager.iDiceBmps[0][0].getHeight(),
								null);
					}
					tempDiceX += (MyArStation.iImageManager.iDiceBmps[0][0].getWidth() + 2);
				}
			}
		}
		//滚轮
		if(iCurrentShoutSeatID == iSelfSeatID && iWheelVisible == true) {
			if(iWheelForceChop == false) {
				//滚轮背景
				iSceneWheel.paint(g, 0, 0);
				//喊 劈 按钮
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).paint(g, iSceneWheel.iCameraX, 0);
				}
				//
				g.save();    
				g.clipRect(iWheelDiceRect.left, iWheelDiceRect.top + 10 * ActivityUtil.ZOOM_Y, 
						iWheelDiceRect.right, iWheelDiceRect.bottom - 8 * ActivityUtil.ZOOM_Y);
				//骰子
				int tempX = (int)(752 * ActivityUtil.ZOOM_X - (MyArStation.iImageManager.iDiceBmps[0][0].getWidth()>>1));
				int tempY = (int)(236 * ActivityUtil.ZOOM_Y - (MyArStation.iImageManager.iDiceBmps[0][0].getHeight()>>1));	
				int tempDiceModelID = iPlayerList[iSelfSeatID].iDiceID;
				int tempDiceIndex = 0;
				for(int i=0; i<6; i++) {
					if(i == 5) {
						tempDiceIndex = 0;
					}
					else {
						tempDiceIndex = i+1;
					}
					g.drawBitmap(MyArStation.iImageManager.iDiceBmps[tempDiceModelID][tempDiceIndex],
						tempX,
						iRightOffset + tempY + i * iLeftDist,
						null);
				}
				//蒙层
				g.drawBitmap(MyArStation.iImageManager.iCoverBmp,
						iWheelDiceRect.left,
						iWheelDiceRect.top,
						null);
				//
				g.restore();
				//
				g.save();    
				g.clipRect(iWheelNumRect.left, iWheelNumRect.top + 10 * ActivityUtil.ZOOM_Y, 
						iWheelNumRect.right, iWheelNumRect.bottom - 8 * ActivityUtil.ZOOM_Y);
				//数字
				for(int i=iWheelMinNum; i<=iWheelMaxNum; i++) {
					g.drawText(""+i, 656* ActivityUtil.ZOOM_X, iLeftOffset + 256 * ActivityUtil.ZOOM_Y + (i-iWheelMinNum) * iLeftDist, ActivityUtil.mWheelBigNum);
				}
				//蒙层
				g.drawBitmap(MyArStation.iImageManager.iCoverBmp,
						iWheelNumRect.left,
						iWheelNumRect.top,
						null);
				//
				g.restore();
				
				//喊斋按钮
				iZhaiButton.paint(g, iSceneWheel.iCameraX, 0);
				
				//雷击按钮
				iLightningButton.paint(g, iSceneWheel.iCameraX, 0);
			}
			else {
				//只画劈
				for(int i=1; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).paint(g, iSceneWheel.iCameraX, 0);
				}
			}	
			//
			g.restore();
		}
		//抢劈
		iPreChopButton.paint(g, 0, 0);
		//反劈
		iRebelChopButton.paint(g, 0, 0);
	}
	
	private void paintResult(Canvas g) {
		try {
			//劈动画-----------------------
			iChopSprite.paint(g,
				(int)(iSDX+iPlayerList[iChangedSeatID[iChopSeatID]].iX),
				(int)(iPlayerList[iChangedSeatID[iChopSeatID]].iY-iPlayerList[iChopSeatID].iBmp.getHeight()));
			//反劈动画
			if(iRebelChopSeatID >= 0) {
				iRebelChopSprite.paint(g,
						(int)(iSDX+iPlayerList[iChangedSeatID[iRebelChopSeatID]].iX),
						(int)(iPlayerList[iChangedSeatID[iRebelChopSeatID]].iY-iPlayerList[iRebelChopSeatID].iBmp.getHeight()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		for(int i=0; i<MAX_GAME_PLAYER; i++) {
			//显示结果
			if(iPlayerList[i].iGameState == CGamePlayer.PLAYER_STATE_RESULT) {
				if(iPlayerList[i].iCupMotionID == 4 || iPlayerList[i].iCupMotionID == 5) {
					iPlayerList[i].iCupSprite.paintStatic(g, 
						iPlayerList[i].iCupMotionID,
						(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX), 
						(int)(iPlayerList[iChangedSeatID[i]].iCupY));
				}
				//显示骰子
				if(iPlayerList[i].iPlayingState == CGamePlayer.PLAYING_STATE_SHOWDICE_RESULT) {
					//显示骰盅里的骰子
					int tempLittleW = MyArStation.iImageManager.iDiceBmps[0][0].getWidth() + (MyArStation.iImageManager.iLittleDiceBmps[0][0].getWidth()>>1);
					int tempLIttleH = MyArStation.iImageManager.iDiceBmps[0][0].getHeight() + (MyArStation.iImageManager.iLittleDiceBmps[0][0].getHeight()>>1);
					int tempW = MyArStation.iImageManager.iDiceBmps[0][0].getWidth() + (MyArStation.iImageManager.iDiceBmps[0][0].getWidth()>>1);
					int tempH = MyArStation.iImageManager.iDiceBmps[0][0].getHeight() + (MyArStation.iImageManager.iDiceBmps[0][0].getHeight()>>1);
					int tempDiceModelID = iPlayerList[i].iDiceID;	
					int tempOffset = (int)(18f * ActivityUtil.ZOOM_X);
					//先显示没有的骰子
					for(int k=0; k<CGamePlayer.MAX_DICE_COUNT; k++) {
						if(iPlayerList[i].iBrightDices[k] == true) {
							continue;
						}
						int tempDice = iPlayerList[i].iDices[k]-1;
						//中
						if(k == 0) {
							g.drawBitmap(MyArStation.iImageManager.iLittleDiceBmps[tempDiceModelID][tempDice],
									(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX+(tempOffset>>3)-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getWidth()>>1)),
									(int)(iPlayerList[iChangedSeatID[i]].iCupY-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getHeight()>>1)),
									null);
						}
						//左
						else if(k == 1) {
							g.drawBitmap(MyArStation.iImageManager.iLittleDiceBmps[tempDiceModelID][tempDice],
									(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX-tempLittleW),
									(int)(iPlayerList[iChangedSeatID[i]].iCupY+tempOffset-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getHeight()>>1)),
									null);
						}
						//右
						else if(k == 2) {
							g.drawBitmap(MyArStation.iImageManager.iLittleDiceBmps[tempDiceModelID][tempDice],
									(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX+MyArStation.iImageManager.iDiceBmps[0][0].getWidth()-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getWidth()>>1)),
									(int)(iPlayerList[iChangedSeatID[i]].iCupY-(tempOffset>>1)-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getHeight()>>1)),
									null);	
						}
						//上
						else if(k == 3) {
							g.drawBitmap(MyArStation.iImageManager.iLittleDiceBmps[tempDiceModelID][tempDice],
									(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX-tempOffset-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getWidth()>>1)),
									(int)(iPlayerList[iChangedSeatID[i]].iCupY-tempLIttleH),
									null);	
						}
						//下
						else {
							g.drawBitmap(MyArStation.iImageManager.iLittleDiceBmps[tempDiceModelID][tempDice],
									(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX+(tempOffset>>1)-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getWidth()>>1)),
									(int)(iPlayerList[iChangedSeatID[i]].iCupY+MyArStation.iImageManager.iDiceBmps[0][0].getHeight()-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getHeight()>>1)),
									null);
						}
					}
					//再显示有的骰子
					for(int k=0; k<CGamePlayer.MAX_DICE_COUNT; k++) {
						if(iPlayerList[i].iBrightDices[k] == false) {
							continue;
						}
						int tempDice = iPlayerList[i].iDices[k]-1;
						if(iGameTick % 6 > 2) {
							//中
							if(k == 0) {
								g.drawBitmap(MyArStation.iImageManager.iLittleDiceBmps[tempDiceModelID][tempDice],
										(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX+(tempOffset>>3)-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getWidth()>>1)),
										(int)(iPlayerList[iChangedSeatID[i]].iCupY-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getHeight()>>1)),
										null);
							}
							//左
							else if(k == 1) {
								g.drawBitmap(MyArStation.iImageManager.iLittleDiceBmps[tempDiceModelID][tempDice],
										(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX-tempLittleW),
										(int)(iPlayerList[iChangedSeatID[i]].iCupY+tempOffset-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getHeight()>>1)),
										null);
							}
							//右
							else if(k == 2) {
								g.drawBitmap(MyArStation.iImageManager.iLittleDiceBmps[tempDiceModelID][tempDice],
										(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX+MyArStation.iImageManager.iDiceBmps[0][0].getWidth()-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getWidth()>>1)),
										(int)(iPlayerList[iChangedSeatID[i]].iCupY-(tempOffset>>1)-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getHeight()>>1)),
										null);	
							}
							//上
							else if(k == 3) {
								g.drawBitmap(MyArStation.iImageManager.iLittleDiceBmps[tempDiceModelID][tempDice],
										(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX-tempOffset-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getWidth()>>1)),
										(int)(iPlayerList[iChangedSeatID[i]].iCupY-tempLIttleH),
										null);	
							}
							//下
							else {
								g.drawBitmap(MyArStation.iImageManager.iLittleDiceBmps[tempDiceModelID][tempDice],
										(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX+(tempOffset>>1)-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getWidth()>>1)),
										(int)(iPlayerList[iChangedSeatID[i]].iCupY+MyArStation.iImageManager.iDiceBmps[0][0].getHeight()-(MyArStation.iImageManager.iLittleDiceBmps[0][0].getHeight()>>1)),
										null);
							}
						}
						else {
							//中
							if(k == 0) {
								iBrightDiceSprite[0].paint(g, 
										(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX+(tempOffset>>3)), 
										(int)(iPlayerList[iChangedSeatID[i]].iCupY));
								g.drawBitmap(MyArStation.iImageManager.iDiceBmps[tempDiceModelID][tempDice],
										(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX+(tempOffset>>3)-(MyArStation.iImageManager.iDiceBmps[0][0].getWidth()>>1)),
										(int)(iPlayerList[iChangedSeatID[i]].iCupY-(MyArStation.iImageManager.iDiceBmps[0][0].getHeight()>>1)),
										null);
							}
							//左
							else if(k == 1) {
								iBrightDiceSprite[1].paint(g, 
										(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX-tempW+(MyArStation.iImageManager.iDiceBmps[0][0].getWidth()>>1)), 
										(int)(iPlayerList[iChangedSeatID[i]].iCupY+tempOffset));
								g.drawBitmap(MyArStation.iImageManager.iDiceBmps[tempDiceModelID][tempDice],
										(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX-tempW),
										(int)(iPlayerList[iChangedSeatID[i]].iCupY+tempOffset-(MyArStation.iImageManager.iDiceBmps[0][0].getHeight()>>1)),
										null);
							}
							//右
							else if(k == 2) {
								iBrightDiceSprite[2].paint(g, 
										(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX+MyArStation.iImageManager.iDiceBmps[0][0].getWidth()), 
										(int)(iPlayerList[iChangedSeatID[i]].iCupY-(tempOffset>>1)));
								g.drawBitmap(MyArStation.iImageManager.iDiceBmps[tempDiceModelID][tempDice],
										(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX+(MyArStation.iImageManager.iDiceBmps[0][0].getWidth()>>1)),
										(int)(iPlayerList[iChangedSeatID[i]].iCupY-(tempOffset>>1)-(MyArStation.iImageManager.iDiceBmps[0][0].getHeight()>>1)),
										null);	
							}
							//上
							else if(k == 3) {
								iBrightDiceSprite[3].paint(g, 
										(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX-tempOffset), 
										(int)(iPlayerList[iChangedSeatID[i]].iCupY-tempH+(MyArStation.iImageManager.iDiceBmps[0][0].getHeight()>>1)));
								g.drawBitmap(MyArStation.iImageManager.iDiceBmps[tempDiceModelID][tempDice],
										(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX-tempOffset-(MyArStation.iImageManager.iDiceBmps[0][0].getWidth()>>1)),
										(int)(iPlayerList[iChangedSeatID[i]].iCupY-tempH),
										null);	
							}
							//下
							else {
								iBrightDiceSprite[4].paint(g, 
										(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX+(tempOffset>>1)),
										(int)(iPlayerList[iChangedSeatID[i]].iCupY+MyArStation.iImageManager.iDiceBmps[0][0].getHeight()));
								g.drawBitmap(MyArStation.iImageManager.iDiceBmps[tempDiceModelID][tempDice],
										(int)(iSDX+iPlayerList[iChangedSeatID[i]].iCupX+(tempOffset>>1)-(MyArStation.iImageManager.iDiceBmps[0][0].getWidth()>>1)),
										(int)(iPlayerList[iChangedSeatID[i]].iCupY+(MyArStation.iImageManager.iDiceBmps[0][0].getHeight()>>1)),
										null);
							}
						}
					}
				}
				//显示唯色加1动画
				for(int k=0; k<iWeiSeSpriteList.size(); k++) {
					Sprite tempSprite = iWeiSeSpriteList.get(k);
					tempSprite.paintStatic(g, 0, tempSprite.getX(), tempSprite.getY());
				}
			}
		}
		//喊点信息框------------
		if(iPlayerList[iLastShoutSeatID].iLastShoutCount > 0) {
			iShoutSprite.paintAction(g,
				ibShoutOne ? 1 : 0,
				(int)(iSDX+iPlayerList[iChangedSeatID[iLastShoutSeatID]].iX),
				(int)(iPlayerList[iChangedSeatID[iLastShoutSeatID]].iY-iPlayerList[iLastShoutSeatID].iBmp.getHeight()));
			iTempRect.set(iSDX+iPlayerList[iChangedSeatID[iLastShoutSeatID]].iX+iShoutSprite.getSpriteLeft(),
				iPlayerList[iChangedSeatID[iLastShoutSeatID]].iY-iPlayerList[iLastShoutSeatID].iBmp.getHeight()+iShoutSprite.getSpriteTop(),
				iSDX+iPlayerList[iChangedSeatID[iLastShoutSeatID]].iX+iShoutSprite.getSpriteLeft()+iShoutSprite.getSpriteWidth(),
				iPlayerList[iChangedSeatID[iLastShoutSeatID]].iY+iShoutSprite.getSpriteTop()+iShoutSprite.getSpriteHeight());
			iTempRect.inset(4, 2);
			//喊的内容
			int tenNum = iPlayerList[iLastShoutSeatID].iLastShoutCount / 10;
			int bitNum = iPlayerList[iLastShoutSeatID].iLastShoutCount % 10;
			int offsetX = (int)(iSDX+iPlayerList[iChangedSeatID[iLastShoutSeatID]].iX-(iPlayerList[iLastShoutSeatID].iBmp.getWidth()>>2));
			offsetX -= (iNumSprite.getSpriteWidth()>>1);
			int tempY = (int)iTempRect.top - 1 + (iShoutSprite.getSpriteHeight()-(iShoutSprite.getSpriteHeight()>>4)-(iShoutSprite.getSpriteHeight()>>3)+iNumSprite.getSpriteHeight()>>1);
			if(tenNum > 0) {
				iNumSprite.paintAction(g, 
						tenNum, 
						offsetX, 
						tempY); 
				offsetX += iNumSprite.getSpriteWidth();
			}
			iNumSprite.paintAction(g, 
				bitNum, 
				offsetX, 
				tempY); 
			//
			offsetX += iNumSprite.getSpriteWidth();	
			if(offsetX < (int)(iSDX+iPlayerList[iChangedSeatID[iLastShoutSeatID]].iX)) {
				offsetX = (int)(iSDX+iPlayerList[iChangedSeatID[iLastShoutSeatID]].iX);
			}
			tempY = (int)iTempRect.top - 1 + (iShoutSprite.getSpriteHeight()-(iShoutSprite.getSpriteHeight()>>4)-(iShoutSprite.getSpriteHeight()>>3)+iXSprite.getSpriteHeight()>>1);
			iXSprite.paint(g, 
				offsetX,
				tempY);//(int)(iPlayerList[i].iY-iPlayerList[i].iBmp.getHeight()-(iShoutSprite.getSpriteHeight()>>1)+((int)ActivityUtil.TEXTSIZE_SHOUT_DICE>>1)));
			//骰子图片
			int tempDiceModelID = iPlayerList[iLastShoutSeatID].iDiceID;
			int tempDice = iPlayerList[iLastShoutSeatID].iLastShoutDice - 1;
			tempY = (int)iTempRect.top - 1 + (iShoutSprite.getSpriteHeight()-(iShoutSprite.getSpriteHeight()>>4)-(iShoutSprite.getSpriteHeight()>>3)-MyArStation.iImageManager.iLittleDiceBmps[0][0].getHeight()>>1);
			g.drawBitmap(MyArStation.iImageManager.iLittleDiceBmps[tempDiceModelID][tempDice],
				iTempRect.right-MyArStation.iImageManager.iLittleDiceBmps[0][0].getWidth(),
				tempY,
				null);
		}
		//显示结果统计框-----------
		iTempRect.set(iPlayerList[0].iX-(ActivityUtil.SCREEN_WIDTH>>3), 
				(ActivityUtil.SCREEN_HEIGHT >> 1)-(ActivityUtil.SCREEN_HEIGHT>>4), 
				iPlayerList[0].iX+(ActivityUtil.SCREEN_WIDTH>>3),
				(ActivityUtil.SCREEN_HEIGHT >> 1)+(ActivityUtil.SCREEN_HEIGHT>>4));
		g.drawRoundRect(iTempRect, 4f, 4f, ActivityUtil.mAlphaRect);
		//画多少个骰子
		int tenNum = iChopDiceCount / 10;
		int bitNum = iChopDiceCount % 10;
		int offsetX = (int)(iPlayerList[0].iX-(ActivityUtil.SCREEN_WIDTH>>4));
		if(iShowBigTextTick > 0) {
			if(tenNum > 0) {
				offsetX -= (iBigNumSprite.getSpriteWidth()>>1);
				iBigNumSprite.paintAction(g, 
					tenNum, 
					offsetX, 
					(ActivityUtil.SCREEN_HEIGHT>>1)+(iBigNumSprite.getSpriteHeight()>>1)); 
				offsetX += iBigNumSprite.getSpriteWidth();
			}
			iBigNumSprite.paintAction(g, 
					bitNum, 
					offsetX, 
					(ActivityUtil.SCREEN_HEIGHT>>1)+(iBigNumSprite.getSpriteHeight()>>1));
		}
		else {
			if(tenNum > 0) {
				offsetX -= (iNumSprite.getSpriteWidth()>>1);
				iNumSprite.paintAction(g, 
					tenNum, 
					offsetX, 
					(ActivityUtil.SCREEN_HEIGHT>>1)+(iNumSprite.getSpriteHeight()>>1)); 
				offsetX += iNumSprite.getSpriteWidth();
			}
			iNumSprite.paintAction(g, 
					bitNum, 
					offsetX, 
					(ActivityUtil.SCREEN_HEIGHT>>1)+(iNumSprite.getSpriteHeight()>>1));
		}
		//
		iXSprite.paint(g, 
				(int)(iPlayerList[0].iX),
				(ActivityUtil.SCREEN_HEIGHT>>1)+(iXSprite.getSpriteHeight()>>1));
		//骰子图片
		if(iChopDice >= 1) {
			g.drawBitmap(MyArStation.iImageManager.iDiceBmps[0][iChopDice-1],
					iPlayerList[0].iX+(ActivityUtil.SCREEN_WIDTH>>4)-(MyArStation.iImageManager.iDiceBmps[0][iChopDice-1].getWidth()>>1),
					(ActivityUtil.SCREEN_HEIGHT>>1)-(MyArStation.iImageManager.iDiceBmps[0][iChopDice-1].getHeight()>>1),
					null);
		}
	}
	
	
	private void paintPiKong(Canvas g) {
		iPiKongSprite.paintStatic(g, 
				0, 
				(int)(iSDX+iPlayerList[iChangedSeatID[iSelfSeatID]].iCupX), 
				(int)(250*ActivityUtil.ZOOM_Y));
	}
	
	
	private void paintResultMoney(Canvas g) {
		//金币动画
		for(int i=0; i<iActiveGbSpriteIndex; i++) {
			if(iLifeTick[i] > 0) {
				iGbSprite.paint(g, (int)iGbX[i], (int)iGbY[i]);
			}
		}
		//
		int tOffset = 0;
		if(iPlayerList[iChangedSeatID[iSelfSeatID]].iBmp == null) {
			tOffset = (int)ActivityUtil.TEXTSIZE_SHOUT_DICE;
		}
		else {
			tOffset = 4 + iPlayerList[iChangedSeatID[iSelfSeatID]].iBmp.getHeight() / 5;
		}
		//画玩家的金币信息
		for(int i=0; i<MAX_GAME_PLAYER; i++) {
			if(iPlayerList[i].ibInit == false) {
				continue;
			}
			if(iPlayerList[i].iPlayingState != CGamePlayer.PLAYING_STATE_SHOWED_RESULT) {
				continue;
			}
			//
			if(iPlayerList[i].iAddExp > 0) {
				int tempOffsetY = (tOffset<<1);
				if(iPlayerList[i].iAddExp != 0) {
					g.drawText("+"+iPlayerList[i].iAddExp+"经验", 
							(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX), 
							(int)(iPlayerList[iChangedSeatID[i]].iY-iResultInfOffsetY-iPlayerList[i].iBmp.getHeight()+tOffset), 
							ActivityUtil.mDice);
				}
				if(iPlayerList[i].iAddGb != 0) {
					g.drawText("+"+iPlayerList[i].iAddGb+"金币", 
						(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX), 
						(int)(iPlayerList[iChangedSeatID[i]].iY-iResultInfOffsetY-iPlayerList[i].iBmp.getHeight()+(tOffset<<1)), 
						ActivityUtil.mResultGb);
					tempOffsetY += tOffset;
				}
				if(iPlayerList[i].iAddVipExp != 0) {
					g.drawText("+"+iPlayerList[i].iAddVipExp+"经验(VIP特权)", 
							(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX), 
							(int)(iPlayerList[iChangedSeatID[i]].iY-iResultInfOffsetY-iPlayerList[i].iBmp.getHeight()+tempOffsetY), 
							ActivityUtil.mDice);
					tempOffsetY += tOffset;
				}
				if(iPlayerList[i].iAddFriendExp != 0) {
					g.drawText("+"+iPlayerList[i].iAddFriendExp+"经验(好友获胜)", 
							(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX), 
							(int)(iPlayerList[iChangedSeatID[i]].iY-iResultInfOffsetY-iPlayerList[i].iBmp.getHeight()+tempOffsetY), 
							ActivityUtil.mDice);
					tempOffsetY += tOffset;
				}
				if(i == iWinSeatID) {
					if(iFailCurrentDrunk >= 5) {
						g.drawText("+"+iRewardExp+"经验(灌倒对方)", 
								(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX), 
								(int)(iPlayerList[iChangedSeatID[i]].iY-iResultInfOffsetY-iPlayerList[i].iBmp.getHeight()+tempOffsetY), 
								ActivityUtil.mDice);
					}
				}
			}
			else {
				g.drawText(""+iPlayerList[i].iAddExp+"经验", 
						(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX), 
						(int)(iPlayerList[iChangedSeatID[i]].iY-iResultInfOffsetY-iPlayerList[i].iBmp.getHeight()+tOffset), 
						ActivityUtil.mResultCount);
				if(iPlayerList[i].iAddGb != 0) {
					g.drawText(""+iPlayerList[i].iAddGb+"金币", 
						(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX), 
						(int)(iPlayerList[iChangedSeatID[i]].iY-iResultInfOffsetY-iPlayerList[i].iBmp.getHeight()+(tOffset<<1)), 
						ActivityUtil.mResultCount);
				}
			}
		}
	}
	
	//喝酒动画
	private void paintResultDrink(Canvas g) {
		//画玩家的喝酒动画或者灌倒对方的动画
		for(int i=0; i<MAX_GAME_PLAYER; i++) {
			if(iPlayerList[i].ibInit == false) {
				continue;
			}
			if(iPlayerList[i].iSprite == null 
					|| iPlayerList[i].iCupSprite == null) {
				continue;
			}
			if(iPlayerList[i].iBmp == null) {
				continue;
			}
			//喝酒动画
			if(i == iFailSeatID) {
				iDrinkSprite.paintStatic(g, 
						0, 
						iPlayerList[iChangedSeatID[i]].iX, 
						iPlayerList[iChangedSeatID[i]].iY-(iPlayerList[i].iBmp.getHeight()>>1));
			}
			//灌倒对方的动画
			else if(i == iWinSeatID) {
				if(iPlayerList[iFailSeatID].iCurrentDrunk >= 4) {
					iWinnerSprite.paintStatic(g, 
						0, 
						iPlayerList[iChangedSeatID[i]].iX, 
						iPlayerList[iChangedSeatID[i]].iY-(iPlayerList[i].iBmp.getHeight()>>1));
				}
			}
		}
	}
	
	private void paintWaitStart(Canvas g) {
		//如果后台状态正在等待玩家准备，就把骰盅画到所有东西的上面
		if(iPlayerList[iSelfSeatID].iCupMotionID == 1 || iPlayerList[iSelfSeatID].iCupMotionID == 2) {
			iPlayerList[iSelfSeatID].iCupSprite.paintStatic(g,
					iPlayerList[iSelfSeatID].iCupMotionID,
					(int)(iSDX+iPlayerList[iChangedSeatID[iSelfSeatID]].iCupX), 
					(int)(iPlayerList[iChangedSeatID[iSelfSeatID]].iCupY));
		}
//		else {
			//骰子动画
			if(iDiceMovieState == DICE_MOVIE_STATE_RUNNING
					|| iDiceMovieState == DICE_MOVIE_STATE_HOME) {
				for(int i=0; i<MAX_DICE_SPRITE; i++) {
					int tempDiceModelID = iPlayerList[iSelfSeatID].iDiceID;
					g.drawBitmap(
							MyArStation.iImageManager.iDiceBmps[tempDiceModelID][iDiceMovieNum[i]-1],
							iDiceMovieX[i]-(MyArStation.iImageManager.iDiceBmps[0][0].getWidth()>>1),
							iDiceMovieY[i]-(MyArStation.iImageManager.iDiceBmps[0][0].getHeight()>>1),
							null);
				}
			}
//		}
		//星星动画
		iStartSprite.paint(g, (int)(428*ActivityUtil.PAK_ZOOM_X), (int)(456*ActivityUtil.PAK_ZOOM_Y));
	}
	
	private void paintChat(Canvas g) {
		int offsetX = MyArStation.iImageManager.iChatBmp.getWidth() >> 1;
		int offsetY = MyArStation.iImageManager.iChatBmp.getHeight() >> 4;
		for(int i=0; i<MAX_GAME_PLAYER; i++) {
			if(iPlayerList[i].iChatTick > 0) {
//				Tools.debug("face paint chat x=" + iSDX+iPlayerList[iChangedSeatID[i]].iX);
				//聊天泡泡
				g.drawBitmap(MyArStation.iImageManager.iChatBmp,
					(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX-offsetX),
					(int)(iPlayerList[iChangedSeatID[i]].iY-iPlayerList[i].iBmp.getHeight()-offsetY),
					null);
				int tempY = 0;
				//聊天内容
				g.drawText(iPlayerList[i].iChatStrA, 
						(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX+2+-offsetX), 
						(int)(iPlayerList[iChangedSeatID[i]].iY-iPlayerList[i].iBmp.getHeight()-offsetY+ActivityUtil.TEXTSIZE_NORMAL), 
						ActivityUtil.mChat);
				tempY += ActivityUtil.TEXTSIZE_NORMAL + 2;
				g.drawText(iPlayerList[i].iChatStrB, 
						(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX+2-offsetX), 
						(int)(iPlayerList[iChangedSeatID[i]].iY-iPlayerList[i].iBmp.getHeight()-offsetY+ActivityUtil.TEXTSIZE_NORMAL+tempY), 
						ActivityUtil.mChat);
				tempY += ActivityUtil.TEXTSIZE_NORMAL + 2;
				g.drawText(iPlayerList[i].iChatStrC, 
						(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX+2-offsetX), 
						(int)(iPlayerList[iChangedSeatID[i]].iY-iPlayerList[i].iBmp.getHeight()-offsetY+ActivityUtil.TEXTSIZE_NORMAL+tempY), 
						ActivityUtil.mChat);
			}
		}
	}
	
	//显示表情
	private void paintFace(Canvas g) {
		//画玩家
		for(int i=0; i<MAX_GAME_PLAYER; i++) {
			if(iPlayerList[i].ibInit == false) {
				continue;
			}
//			Tools.debug("face bbbbbbbbbbbbbb");
			if(iPlayerList[i].iFaceSprite == null
					|| iPlayerList[i].ibFaceEnd == true) {
				continue;
			}
//			Tools.debug("face paint iPlayerList[" + i + "].iFaceMotionID="+
//					iPlayerList[i].iFaceMotionID);
//			Tools.debug("face paint x=" + iSDX+iPlayerList[iChangedSeatID[i]].iX);
//			Tools.debug("face paint y=" + iPlayerList[iChangedSeatID[i]].iY);
			//
			iPlayerList[i].iFaceSprite.paintStatic(g, 
					iPlayerList[i].iFaceMotionID,
					(int)(iSDX+iPlayerList[iChangedSeatID[i]].iX), 
					(int)(iPlayerList[iChangedSeatID[i]].iY)-(iPlayerList[i].iBmp.getHeight()>>1));
			if(iPlayerList[i].lightning != null && iPlayerList[i].iVipLevel >= 5 && iPlayerList[i].iFaceMotionID == 2) {
				for(int j = 0; j < CGamePlayer.MAX_LIGHTNING_COUNT; j++) {
					if(iPlayerList[i].lightning[j] != null) {
						iPlayerList[i].lightning[j].draw(g);
					}
				}
			}
		}
	}
	
	//显示经验条
	void paintExpBar(Canvas g) {
		//显示经验条
		if(iBarExpSet == false) {
			//
			if(iSceneExp != null) {
				iSceneExp.paint(g, 0, 0);
				//职称
				Rect tempRect = new Rect();
//			if(iExpRect.width() >= 15) {
				tempRect.set((int)(iExpRect.left+iSDX), (int)(iExpRect.top+iBarExpY), (int)(iExpRect.right+iSDX), (int)(iExpRect.bottom+iBarExpY));
				if(iNinePatch != null) {
					iNinePatch.draw(g, tempRect);
				}
				ActivityUtil.mTitlePaint.getTextBounds(MyArStation.iPlayer.iTitle, 0, MyArStation.iPlayer.iTitle.length(), tempRect);
				float x = iExpTitleRect.centerX() - tempRect.centerX() + iSDX;
				float y = iExpTitleRect.centerY() - tempRect.centerY() + iBarExpY;
//			}	//职称文字
				ActivityUtil.mTitlePaint.setTextAlign(Align.LEFT);
				g.drawText(MyArStation.iPlayer.iTitle, x, y, ActivityUtil.mTitlePaint);
				ActivityUtil.mTitlePaint.setTextAlign(Align.CENTER);
			}
//			else if(iExpRect.width() > 0) {
//				int tW = (int)(iExpRect.width());
//				if(tW < 6) {
//					tW = 6;
//				}
//				tempRect.set((int)(iExpRect.left+iSDX), (int)(iExpRect.top+iBarExpY), (int)(iExpRect.left+tW+iSDX), (int)(iExpRect.bottom+iBarExpY));
//				if(iExpBmp != null) {
//					iExpBmp.draw(g, tempRect);
//				}
//			}
		
		}
	}
	
	@Override
	public void paintScreen(Canvas g, Paint paint) {
		//载入数据
		switch(iGameState) {
		case GAME_STATE_LOADING:
		case GAME_STATE_LEAVEROOM:
		case GAME_STATE_CHANGEDESK_LOADING:
		case GAME_STATE_JOINACTIVITY_LOADING:
			{
				g.drawBitmap(MyArStation.iImageManager.loadLoginBack(),
						0,
						0,
						null);
				if(iSceneLoading != null) {
					iSceneLoading.paint(g, 0, 0);
//					g.drawText("正在读取数据", 278*ActivityUtil.ZOOM_X, 288*ActivityUtil.ZOOM_Y, ActivityUtil.mLoading);
				}
			}
			break;
			
		case GAME_STATE_LOGINROOM_START:
			{
				g.drawBitmap(MyArStation.iImageManager.iRoomBmp,
						0,
						0,
						null);
				if(iSceneDesk == null || iSceneDeskEx == null) {
					break;
				}
//				iSceneDeskEx.paint(g, 0, 0);
				iSceneDesk.paint(g, 0, 0);
				//画玩家的头像
				paintPlayer(g);
				//状态栏
				if(iSceneBar == null) {
					break;
				}
				iSceneBar.paint(g, 0, 0);
				//显示经验条
				paintExpBar(g);
				//菜单按钮
				iMenuButton.paint(g, 0, 0);
				//聊天按钮
				iChatButton.paint(g, 0, 0);
				//表情按钮
				iFaceButton.paint(g, 0, 0);
				//任务按钮
				iGiftBUtton.paint(g, 0, 0);
				//房间名
				g.drawText(iRoomName, iSBX+718*ActivityUtil.ZOOM_X, 470*ActivityUtil.ZOOM_Y, ActivityUtil.mRoomName);
				//金币
				g.drawText(""+MyArStation.iPlayer.iGb, iSBX+486*ActivityUtil.ZOOM_X, 474*ActivityUtil.ZOOM_Y, ActivityUtil.mMoneyPaint);
				//酒的瓶数
				g.drawText(""+iPlayerList[iSelfSeatID].iCurrentWineCount, iSBX+598*ActivityUtil.ZOOM_X, 470*ActivityUtil.ZOOM_Y, ActivityUtil.mMoneyPaint);
			}
			break;
			
		case GAME_STATE_SITDOWN:
			{
				g.drawBitmap(iBackBmp,
					0,
					0,
					null);
				//场景动画
				if(iSceneDesk == null || iSceneDeskEx == null) {
					break;
				}
				iSceneDesk.paint(g, 0, 0);
				iSceneDeskEx.paint(g, 0, 0);
				//画玩家的头像
				paintPlayer(g);
				//
				if(iWheelVisible == false) {
					//菜单按钮
					iMenuButton.paint(g, 0, 0);
					//聊天按钮
					iChatButton.paint(g, 0, 0);
					//表情按钮
					iFaceButton.paint(g, 0, 0);
					//任务按钮
					iGiftBUtton.paint(g, 0, 0);
				}
				//正在游戏中
				if(iServerState == E_TGS_PLAYING) {
					paintPlaying(g);
					if(iBarExpY < 48) {
						//显示经验条
						paintExpBar(g);
					}
				}
				//显示结果
				else if(iServerState == E_TGS_WAIT_READY) {
					paintResult(g);
				}
				//显示劈空动画
				else if(iServerState == E_TGS_SHOW_PIKONG) {
					paintPiKong(g);
				}
				//显示结果金币动画
				else if(iServerState == E_TGS_SHOW_MONEY) { 
					paintResultMoney(g);
					//显示经验条
					paintExpBar(g);
				}
				//显示结果喝酒动画
				else if(iServerState == E_TGS_SHOW_DRINK) {
					paintResultDrink(g);
					//显示经验条
					paintExpBar(g);
				}
				//显示等待开始
				else if(iServerState == E_TGS_WAIT_START 
						|| iServerState == E_TGS_WAIT_PLAYER) {
					paintWaitStart(g);
					//显示经验条
					paintExpBar(g);
				}
				//显示等待准备
				else {//if(iServerState == E_TGS_WAIT_READY) {
					//显示经验条
					paintExpBar(g);
				}
				//飞心动画
				paintHartSprite(g);
				//加倍动画
				if(iDoubleTimes > 1 && !iDoubleSpriteEnd) {
					int tMotionID = 0;
					switch(iDoubleTimes) {
					case 2:
						tMotionID = 1;
						break;
						
					case 4:
						tMotionID = 2;
						break;
						
					case 8:
						tMotionID = 3;
						break;
						
					case 16:
						tMotionID = 4;
						break;
						
					case 32:
						tMotionID = 5;
						break;
						
					case 64:
						tMotionID = 6;
						break;
					}
					iDoubleSprite.paintStatic(g, 
							tMotionID,
							(int)(iSDX+iPlayerList[iChangedSeatID[iSelfSeatID]].iCupX), 
							(int)(250*ActivityUtil.ZOOM_Y));
				}
				//
				if(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING
						|| iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_RESULT) {
					//显示底注和倍数
					g.drawText(iBetStr+"X"+iDoubleTimes+"倍", iSBX+718*ActivityUtil.ZOOM_X, 470*ActivityUtil.ZOOM_Y, ActivityUtil.mRoomName);
				}
				else {
					//房间名
					g.drawText(iRoomName, iSBX+718*ActivityUtil.ZOOM_X, 470*ActivityUtil.ZOOM_Y, ActivityUtil.mRoomName);
				}
				//显示聊天气泡
				paintChat(g);
				//显示表情
				paintFace(g);
				//金币
				g.drawText(""+MyArStation.iPlayer.iGb, iSBX+486*ActivityUtil.ZOOM_X, 470*ActivityUtil.ZOOM_Y, ActivityUtil.mMoneyPaint);
				//酒的瓶数
				g.drawText(""+iPlayerList[iSelfSeatID].iCurrentWineCount, iSBX+598*ActivityUtil.ZOOM_X, 470*ActivityUtil.ZOOM_Y, ActivityUtil.mMoneyPaint);
				//活动房的信息
				if(iLogicType == E_TGLT_ACTIVITY_SHOUTONENOTCHANGE) {
					iTempRect.set(iSDX+2, 
							2, 
							iSDX+((ActivityUtil.SCREEN_WIDTH>>2)+(ActivityUtil.SCREEN_WIDTH>>4)),//
							28*ActivityUtil.ZOOM_Y);
					g.drawRoundRect(iTempRect, 4f, 4f, ActivityUtil.mAlphaRect);
					g.drawText(iActivityInfo, 
							iSDX+(((ActivityUtil.SCREEN_WIDTH>>2)+(ActivityUtil.SCREEN_WIDTH>>4))>>1), //
							22*ActivityUtil.ZOOM_Y, ActivityUtil.mPlayerName);
				}
				quickSelectRoomScreen.paintScreen(g, paint);
				if(infoPopView != null)infoPopView.paintScreen(g, paint);
				if(missionView != null)missionView.paintScreen(g, paint);
			}
			break;
		case GAME_STATE_CHANGE_DESK:
		case GAME_STATE_JOIN_ACTIVITY:
			{
				g.drawBitmap(MyArStation.iImageManager.iRoomBmp,
						0,
						0,
						null);
				if(iSceneDesk == null || iSceneDeskEx == null) {
					break;
				}
				iSceneDeskEx.paint(g, 0, 0);
				iSceneDesk.paint(g, 0, 0);
				//画玩家的头像
				paintPlayer(g);
			}
			break;
			
		default:
			break;
		}
	}

	@Override
	public void performL() {
		createBMP();
		switch(iServerState) {
		case E_TGS_WAIT_PLAYER:
			{
				//骰子动画
				diceMoviePerform();
			}
			break;
			
		case E_TGS_WAIT_START:
			{
				//骰子动画
				diceMoviePerform();
				//
				if(iPlayerList[iSelfSeatID].iCupMotionID == 1) {
					iGameTick --;
					//自动摇骰盅
					if(iGameTick == 0) {
						iPlayerList[iSelfSeatID].iCupMotionID = 3;
						iPlayerList[iSelfSeatID].iCupSprite.resetFrameID();
						if(iShakeListener != null) {
							iShakeListener.stopShake();
						}
						//发送准备请求
						requestReady();
						break;
					}
				}
			}
			break;
			
		case E_TGS_WAIT_READY:	//统计结果
			{
				//让被劈的骰子发光
				iGameTick ++;
				if(iGameTick > 6) {
					iGameTick = 0;
				}
				//
				if(iPlayerList[iShowingResultSeatID].iCupMotionID == 4) {
					//执行骰盅逻辑，如果动作播放完
					if(iPlayerList[iShowingResultSeatID].iCupSprite.performAction(4) == true) {
						iPlayerList[iShowingResultSeatID].iPlayingState = CGamePlayer.PLAYING_STATE_SHOWDICE_RESULT;
						iPlayerList[iShowingResultSeatID].iCupMotionID = 5;
						iPlayerList[iShowingResultSeatID].iCupSprite.resetFrameID();
						iChopDiceCount += iPlayerList[iShowingResultSeatID].iChopDiceCount;
						if(iPlayerList[iShowingResultSeatID].iChopDiceCount > 0) {
							iShowBigTextTick = 4;
						}
						//判断是否唯色，如果是，则加入唯色动画
						if(iPlayerList[iShowingResultSeatID].iChopDiceCount > 5) {
							Sprite pSprite = new Sprite();
							pSprite.copyFrom(iWeiSeSprite);
							pSprite.setPosition((short)iPlayerList[iChangedSeatID[iShowingResultSeatID]].iCupX, 
									(short)iPlayerList[iChangedSeatID[iShowingResultSeatID]].iCupY);
							iWeiSeSpriteList.addElement(pSprite);
						}
					}
				}
				else if(iPlayerList[iShowingResultSeatID].iCupMotionID == 5) {
					iShowBigTextTick --;
//					if(iShowBigTextTick <= 0) {
//						ActivityUtil.mResultCount.setTextSize(ActivityUtil.TEXTSIZE_RESULT);
//						ActivityUtil.mResultCount.setColor(Color.CYAN);
//					}
					//执行骰盅逻辑，如果动作播放完
					if(iPlayerList[iShowingResultSeatID].iCupSprite.performAction(5) == true) {
						iPlayerList[iShowingResultSeatID].iPlayingState = CGamePlayer.PLAYING_STATE_SHOWED_RESULT;
						iPlayerList[iShowingResultSeatID].iCupMotionID = 0;
						iPlayerList[iShowingResultSeatID].iCupSprite.resetFrameID();
						//查找下一个显示结果的座位
						findNextShowResultSeat();
						//如果找到的座位已经显示完
						if(iPlayerList[iShowingResultSeatID].iPlayingState == CGamePlayer.PLAYING_STATE_SHOWED_RESULT) {
							if(iChopDiceCount == 0 && iPlayingCount > 2) {
								//显示劈空动画
								iServerState = E_TGS_SHOW_PIKONG;
								iGameTick = 0;
								iPiKongSprite.resetFrameID();
							}
							else {
								iServerState = E_TGS_SHOW_MONEY;
								iGameTick = 48;
								iResultInfOffsetY = 0;
								
								ActivityUtil.mDice.setAlpha(255);
								ActivityUtil.mDice.setShadowLayer(0.5f, 1f, 1f, Color.BLACK);
								ActivityUtil.mResultCount.setAlpha(255);
								ActivityUtil.mResultCount.setShadowLayer(0.5f, 1f, 1f, Color.BLACK);
								ActivityUtil.mResultGb.setAlpha(255);
								ActivityUtil.mResultGb.setShadowLayer(0.5f, 1f, 1f, Color.BLACK);
								//复位背景
								creatBackBmp();
							}
						}
						//否则开始显示
						else {
							iPlayerList[iShowingResultSeatID].iGameState = CGamePlayer.PLAYER_STATE_RESULT;
							iPlayerList[iShowingResultSeatID].iPlayingState = CGamePlayer.PLAYING_STATE_SHOWING_RESULT;
							iPlayerList[iShowingResultSeatID].iCupMotionID = 4;
							iPlayerList[iShowingResultSeatID].iCupSprite.resetFrameID();
						}
					}
				}
			}
			break;
			
		case E_TGS_PLAYING:
			{
				if(iPlayerList[iSelfSeatID].iGameState != CGamePlayer.PLAYER_STATE_PLAYING) {
					break;
				}
				//是否延迟显示反劈
				if(iWantToShowRebleChopButton) {
					long timeSt = System.currentTimeMillis();
					if(timeSt - iShowRebelChopButtonDelay >= 1500) {
						iRebelChopButton.setVision(true);
						iRebelChopButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_SHOW);
						//iRebelChopSeatID = -1;
						iWantToShowRebleChopButton = false;
					}
				}
				//自己的骰子
				if(iBarDiceSet == false) {
					iBarDiceY -= 8;
					if(iBarDiceY <= ActivityUtil.SCREEN_HEIGHT - 2) {
						iBarDiceY = ActivityUtil.SCREEN_HEIGHT - 2;
						//播放计时器动画
						iOvalTimerCount.start(iCurrentShoutSeatID == iSelfSeatID ? true : false, iDelayCount);
						//
						iBarDiceSet = true;
						//将状态栏的骰子画入背景
						drawBarDiceToBack();
					}
				}
				if(iHideBarDice == true) {
					iBarDiceY += 8;
					if(iBarDiceY >= ActivityUtil.SCREEN_HEIGHT + iPlayerList[iSelfSeatID].iBmp.getHeight()) {
						iBarDiceY = ActivityUtil.SCREEN_HEIGHT + iPlayerList[iSelfSeatID].iBmp.getHeight();
						iHideBarDice = false;
						iBarDiceSet = false;
						for(int i=0; i<CGamePlayer.MAX_DICE_COUNT; i++) {
							iPlayerList[iSelfSeatID].iDices[i] = iChangeDice;
						}
					}
				}
				//经验条
				if(iBarExpY < 48) {
					iBarExpY += 8;
					iSceneExp.iCameraY = -(int)iBarExpY;
				}
				//滚轮
				if(iCurrentShoutSeatID == iSelfSeatID) {
					//反劈按钮
					if(iWheelVisible == false 
								&& iRebelChopButton.isVision() == true) {
						iRebelChopButton.perform();
					}
					//喊斋按钮
					if(iZhaiButton.isVision() == true) {
						iZhaiButton.perform();
					}
					//雷击按钮
					if(iLightningButton.isVision() == true) {
						iLightningButton.perform();
					}
					//按钮
					for(int i=0; i<iSpriteButtonList.size(); i++) {
						((SpriteButton)iSpriteButtonList.elementAt(i)).perform();
					}
					//数字和骰子
					if(iWheelVisible == true 
							&& iWheelForceChop == false) {
						//如果有惯性
						if(iWheelRunTick > 0) {
							iWheelRunTick --;
							if(iWheelRunTick <= 0) {
								iWheelRunTick = 0;
							}
							if(iWheelRunTick % 2 == 1) {
								iRunSpeed -= 1;
							}
							if(iRunSpeed <= 4) {
								iRunSpeed = 4;
								iWheelRunTick = 0;
							}
							if(iWheelRunPart == 1) {		//数字部分滚动
								if(iWheelRunDir == 1) { //向上滚动
									wheelNumDragUp();
								}
								else {
									wheelNumDragDown();
								}
							}
							else if(iWheelRunPart == 2) {  	//骰子部分滚动
								if(iWheelRunDir == 1) {	//向上滚动
									wheelDiceDragUp();
								}
								else {
									wheelDiceDragDown();
								}
							}
						}
						//当松手后
						if(iPressedY == 0 && iWheelRunTick <= 0) {
							//是否对准基线
							int tempY = 0;//(int)(ActivityUtil.TEXTSIZE_HUGEC/2);// % iLeftDist;
							tempY = iLeftOffset % iLeftDist;
							if(tempY < 0) {
								if(tempY <= -4) {
									if(tempY < -28) {
										iLeftOffset -= 4;
									}
									else {
										iLeftOffset += 4;
									}
								}
								else {
									iLeftOffset -= tempY;
								}
							}
							//
							int tempTop = (int)(-(iWheelMaxNum - iWheelMinNum) * iLeftDist);
							if(iLeftOffset < tempTop) {
								iLeftOffset = tempTop;
							}
							else {
								int tempMinNum = iWheelMinNum;
								if(iLastShoutDice == 1) {
									if(tempMinNum <= iLastShoutCount) {
										tempMinNum = iLastShoutCount + 1;
									}
								}
								else {
									if(iLastShoutCount > tempMinNum) {
										tempMinNum = iLastShoutCount;
									}
								}
								if(iLeftOffset > (iWheelMinNum-tempMinNum)*iLeftDist) {
									iLeftOffset = (iWheelMinNum-tempMinNum)*iLeftDist;
								}
							}
							iLastLeftOffset = iLeftOffset;
							//
							tempY = iRightOffset % iLeftDist;
							if(tempY < 0) {
								if(tempY <= -4) {
									if(tempY < -28) {
										iRightOffset -= 4;
									}
									else {
										iRightOffset += 4;
									}
								}
								else {
									iRightOffset -= tempY;
								}
							}
							//
							tempTop = (int)(-5 * iLeftDist);
							if(iRightOffset < tempTop) {
								iRightOffset = tempTop;
							}
							else {
								if(iRightOffset > 0) {
									iRightOffset = 0;
								}
								if(iLastShoutCount >= iWheelCurrentNum) {
									int tempDice = iLastShoutDice + 1;
									tempTop = (int)(-(tempDice - iLastShoutDice) * iLeftDist);
									if(iRightOffset > tempTop) {
										iRightOffset = tempTop;
									}
								}
							}
							iLastRightOffset = iRightOffset;
						}
					}
					//计算滚轮的值
					iWheelCurrentNum = iWheelMinNum - iLeftOffset / iLeftDist;
					if(iLeftOffset % iLeftDist < -28) {
						iWheelCurrentNum ++;
					}
					if(iWheelCurrentNum > iWheelMaxNum) {
						iWheelCurrentNum = iWheelMaxNum;
					}
					//
					iWheelCurrentDice = 2 - iRightOffset / iLeftDist;
					if(iRightOffset % iLeftDist < -28) {
						iWheelCurrentDice ++;
					}
					if(iWheelCurrentDice >= 7) {
						iWheelCurrentDice = 1;
					}
					//滚轮两边相互作用
					if(iLastTuchPart == 1) {
						if(iWheelCurrentNum <= iLastShoutCount) {
							if(iWheelCurrentDice != 1
									&& iWheelCurrentDice <= iLastShoutDice) {
								iWheelCurrentDice = iLastShoutDice + 1;
								iRightOffset = (int)(-(iWheelCurrentDice - 2) * iLeftDist); 
								iLastRightOffset = iRightOffset;
							}
							if(iWheelCurrentDice >= 7) {
								iWheelCurrentDice = 1;
							}
						}
					}
					else if(iLastTuchPart == 2) {
						if(iWheelCurrentDice != 1 
								&& iWheelCurrentDice <= iLastShoutDice
								&& iWheelCurrentNum <= iLastShoutCount) {
							iWheelCurrentNum ++;
							iLeftOffset = (int)(-(iWheelCurrentNum - iWheelMinNum) * iLeftDist); 
							iLastLeftOffset = iLeftOffset;
						}
					}
				}
				else {
					//抢劈
					if(iPreChopButton.isVision() == true) {
						iPreChopButton.perform();
					}
					//反劈
					else if(iRebelChopButton.isVision() == true) {
						iRebelChopButton.perform();
					}
				}
				//房间名称和底注
				if(iFlashBetTick > 0) {
					iFlashBetTick --;
					if(iFlashBetTick%4 >= 2) {
						ActivityUtil.mRoomName.setColor(Color.RED);
					}
					else {
						ActivityUtil.mRoomName.setColor(Color.BLACK);
					}
				}
			}
			break;
			
		case E_TGS_SHOW_PIKONG: //显示劈空动画
			{
				//执行骰盅逻辑，如果动作播放完
				if(iPiKongSprite.performAction(0) == true) {
					iServerState = E_TGS_SHOW_MONEY;
					iGameTick = 48;
					iResultInfOffsetY = 0;
					
					ActivityUtil.mDice.setAlpha(255);
					ActivityUtil.mDice.setShadowLayer(0.5f, 1f, 1f, Color.BLACK);
					ActivityUtil.mResultCount.setAlpha(255);
					ActivityUtil.mResultCount.setShadowLayer(0.5f, 1f, 1f, Color.BLACK);
					ActivityUtil.mResultGb.setAlpha(255);
					ActivityUtil.mResultGb.setShadowLayer(0.5f, 1f, 1f, Color.BLACK);
					//复位背景
					creatBackBmp();
					
					//播放翻倍动画
					iDoubleTimes <<= 1;
					iFlashBetTick = 16;
					//
					iDoubleSpriteEnd = false;
					iDoubleSprite.resetFrameID();
				}
				//播放声音
				if(iPiKongSprite.getFrameID() == 5) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bottle, 0);
				}
			}
			break;
			
		case E_TGS_SHOW_MONEY:	//显示结果金币
			{
				iActiveGbSpriteIndex ++;
				if(iActiveGbSpriteIndex > MAX_GB_SPRITE) {
					iActiveGbSpriteIndex = MAX_GB_SPRITE;
				}
				else {
					//
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.coin, 0);
				}
				//计算金币飞行轨迹
				for(int i=0; i<iActiveGbSpriteIndex; i++) {
					iLifeTick[i] --;
					if(iGbStartX[i] == iGbEndX[i]) {
						iGbY[i] += iGbSpeed[i];
					}
					else if(iGbStartY[i] == iGbEndY[i]) {
						iGbX[i] += iGbSpeed[i];
					}
					else {
						iGbX[i] += iGbSpeed[i];
						iGbY[i] = (int)((iGbX[i]-iGbStartX[i]) * iLineK[i]) + iGbStartY[i];
					}
				}
				//
				if(iResultInfOffsetY < iMaxResultInfOffsetY) {
					iResultInfOffsetY ++;
				}
				//
				if(iGameTick <= 0) {
//					System.out.println("PerformResult GameMain.iPlayer.iGameExp=" + GameMain.iPlayer.iGameExp
//							+ " GameMain.iPlayer.iGb=" + GameMain.iPlayer.iGb + " 0000000000000");
					//改变自己的金币和经验
					if(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_RESULT) {
						//加金币
						MyArStation.iPlayer.iGb += iPlayerList[iSelfSeatID].iAddGb;
						//如果没有升级，才加经验
						if(ibLockExp == false) {
							//加经验
							MyArStation.iPlayer.iGameExp += iPlayerList[iSelfSeatID].iAddExp;
							//加VIP额外经验
							MyArStation.iPlayer.iGameExp += iPlayerList[iSelfSeatID].iAddVipExp;
							//加好友亲密度产生的额外经验
							MyArStation.iPlayer.iGameExp += iPlayerList[iSelfSeatID].iAddFriendExp;
							//灌倒人加的额外经验
							if(iSelfSeatID == iWinSeatID) {
								if(iFailCurrentDrunk >= 5) {
									MyArStation.iPlayer.iGameExp += iRewardExp;
								}
							}
							if(MyArStation.iPlayer.iGameExp > MyArStation.iPlayer.iNextExp) {
								MyArStation.iPlayer.iGameExp = MyArStation.iPlayer.iNextExp;
							}
							else if(MyArStation.iPlayer.iGameExp < 0) {
								MyArStation.iPlayer.iGameExp = 0;
								//需要重新获取级别和称号
								ibAutoReqInfo = true;
								MyArStation.iGameProtocol.requestPlayerInfoThree(MyArStation.iPlayer.iUserID);
							}
							//重新计算经验条
							setExpRect();
						}
					}
//					System.out.println("PerformResult GameMain.iPlayer.iGameExp=" + GameMain.iPlayer.iGameExp
//							+ " GameMain.iPlayer.iGb=" + GameMain.iPlayer.iGb + " 111111111111");
					//跳到显示喝酒动画
					iDrinkSprite.resetFrameID();
					iWinnerSprite.resetFrameID();
					//
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.water, 2);
					//
					iServerState = E_TGS_SHOW_DRINK;
					//显示结果Msg
					if(iResultMsg.length() > 0) {
						messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_SHOW_MSG, iResultMsg);
					}
					//显示菜单
					iMenuButton.setVision(true);
					//iMenuButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				}
				else {
					iGameTick --;
					if(iGameTick < 8) {
						int tAlpha = ActivityUtil.mDice.getAlpha();
						tAlpha -= 30;
						if(tAlpha < 0) {
							tAlpha = 0;
						}
						ActivityUtil.mDice.setAlpha(tAlpha);
						int backColor = tAlpha << 24;
						ActivityUtil.mDice.setShadowLayer(0.5f, 1f, 1f, backColor);
						ActivityUtil.mResultCount.setAlpha(tAlpha);
						ActivityUtil.mResultCount.setShadowLayer(0.5f, 1f, 1f, backColor);
						ActivityUtil.mResultGb.setAlpha(tAlpha);
						ActivityUtil.mResultGb.setShadowLayer(0.5f, 1f, 1f, backColor);
					}
				}
				//显示经验条
				iBarExpY -= 8;
				if(iBarExpY < 0) {
					iBarExpY = 0;
				}
				//执行经验条的逻辑
				if(iSceneExp != null) {
					iSceneExp.iCameraY = -(int)iBarExpY;
				}
			}
			break;
			
		case E_TGS_SHOW_DRINK:	//显示结果喝酒动画
			{
				if(iWinnerSprite.performAction(0) == true) {
					iWinnerSprite.resetFrameID();
				}
				if(iDrinkSprite.performAction(0) == true) {
					//失败者的金币
					if(iPlayerList[iFailSeatID].iGameState == CGamePlayer.PLAYER_STATE_RESULT) {
						//
						iPlayerList[iFailSeatID].iCurrentDrunk = iFailCurrentDrunk;
						//计算自己的酒和醉酒度
						if(iSelfSeatID == iFailSeatID) {
							//酒
							iPlayerList[iSelfSeatID].iCurrentWineCount --;
							if(iPlayerList[iSelfSeatID].iCurrentWineCount <= 0) {
								iPlayerList[iSelfSeatID].iCurrentWineCount = 0;
								//提示买酒
//								messageEvent(GameEventID.ESPRITEBUTTON_EVENT_SHOW_BUYWINEDIALOG);
							}
							else if(iPlayerList[iSelfSeatID].iCurrentDrunk >= 5) {
								iPlayerList[iSelfSeatID].iCurrentDrunk = 5;
								//提示醒酒
//								messageEvent(GameEventID.ESPRITEBUTTON_EVENT_SHOW_CLEANDRUNKDIALOG);
							}
						}
					}
					//
					iServerState = E_TGS_WAIT_PLAYER;
					//为了让玩家玩摇骰子，在这里强行改变骰盅状态
					iDiceMovieState = DICE_MOVIE_STATE_IDLE;
				}
			}
			break;
			
		default:
			break;
		}
		
		//载入数据
		switch(iGameState) {
		case GAME_STATE_LOADING:
		case GAME_STATE_LEAVEROOM:
		case GAME_STATE_CHANGEDESK_LOADING:
		case GAME_STATE_JOINACTIVITY_LOADING:
			{
				//计时,如果10秒内还没有应答，则退出到登录界面
				iLoadingTick ++;
				if(iLoadingTick > 138) {
					iLoadingTick = 0;
					closeGame();
					return;
				}
				//
				if(iSceneLoading != null) {
					iSceneLoading.handle();
				}
			}
			break;
		case GAME_STATE_LOGINROOM_START:
			{
				iSDX -= iSDSpeed;
				iSBX -= iSBSpeed;
				iDrunkTextSkewX -= 5;
				iDrunkTextAlpha += 25;
				ActivityUtil.mDrunk.setTextSkewX(iDrunkTextSkewX);
				ActivityUtil.mDrunk.setAlpha(iDrunkTextAlpha);
				ActivityUtil.mPlayerName.setTextSkewX(iDrunkTextSkewX);
				ActivityUtil.mPlayerName.setAlpha(iDrunkTextAlpha);
				ActivityUtil.mVipPlayerName.setTextSkewX(iDrunkTextSkewX);
				ActivityUtil.mVipPlayerName.setAlpha(iDrunkTextAlpha);
				if(iSDX >= 0) {
					//
					iSDX = 0;
					iSBX = 0;
					iDrunkTextAlpha = 255;
					iDrunkTextSkewX = 0;
					ActivityUtil.mDrunk.setAlpha(255);
					ActivityUtil.mDrunk.setTextSkewX(0f);
					ActivityUtil.mPlayerName.setAlpha(255);
					ActivityUtil.mPlayerName.setTextSkewX(0f);
					ActivityUtil.mVipPlayerName.setAlpha(255);
					ActivityUtil.mVipPlayerName.setTextSkewX(0f);
					//
					iGameState = GAME_STATE_SITDOWN;
					//
					//iMenuButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
					//释放loading图
					MyArStation.iImageManager.releaseLoginBack();
					//创建背景图片
					if(iBackBmp == null) {
						creatBackBmp();
					}
					//释放room图
					//GameMain.iImageManager.releaseRoomBack();
					//执行经验条逻辑
					if(iSceneExp != null) {
						iSceneExp.handle();
						iSceneExp.iCameraX = 0;
						iSceneExp.iCameraY = 0;
					}
				}
				else {
					//
					deskPerform();
				}
			}
			break;
			
		case GAME_STATE_SITDOWN:
			{
				//场景动画
				if(iSceneDesk != null && iSceneDeskEx != null) {
					iSceneDesk.handle();
					iSceneDeskEx.handle();
				}
				//执行玩家的动作
				gameplayerPerform();
				//菜单按钮
				iMenuButton.perform();
				//聊天按钮
				iChatButton.perform();
				//表情按钮
				iFaceButton.perform();
				//任务按钮
				iGiftBUtton.perform();
				//飞心动画
				hartSpritePerform();
				//动态动画
				animSpritePerform();
				//飘心动画
				loveSpritePerform();
				//升级动画
				levelUpSpritePerform();
				//道具动画
				itemSpritePerform();
				//唯色动画
				weiSeSpritePerform();
				//加倍动画
				doubleSpritePerform();
				//雷击动画
				lightningSpritePerform();
				quickSelectRoomScreen.performL();
				if(infoPopView != null)infoPopView.performL();
				if(missionView != null)missionView.performL();
			}
			break;
			
		case GAME_STATE_CHANGE_DESK:	//换桌
		case GAME_STATE_JOIN_ACTIVITY:  //参加活动
			{
				iSDX += iSDSpeed;
				if(iSDX <= -500) {
					iSDX = -500;
					iSBX = 500;
					iDrunkTextSkewX = 80;
					iDrunkTextAlpha = 0;
					if(iGameState == GAME_STATE_JOIN_ACTIVITY) {
						iGameState = GAME_STATE_JOINACTIVITY_LOADING;
					}
					else {
						iGameState = GAME_STATE_CHANGEDESK_LOADING;
					}
				}
				else {
					if(iSceneDesk != null && iSceneDeskEx != null) {
						iSceneDesk.handle();
						iSceneDesk.iCameraX = -(int)iSDX;
						iSceneDeskEx.handle();
//						iSceneDeskEx.iCameraX = -(int)iSDX;
					}
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
	private void createBMP() {
		if(createBMP) {
			createBMP = false;
			creatBackBmp();
		}
	}

	//动态动画
	private void animSpritePerform() {
		if(iSynAnimation != null) iSynAnimation.performL();
//		for(int i=0; i<iAnimSpriteList.size(); i++) {
//			Sprite pSprite = iAnimSpriteList.get(i);
//			int tEndMotion = iAnimEndMotionList.get(i);
//			if(pSprite.performAction(tEndMotion) == true) {
//				pSprite = null;
//				iAnimSpriteList.remove(i);
//				i --;
//			}
//		}
		
		//动态动画
//		for(int i=0; i<MAX_ANIM_SPRITE; i++) {
//			if(iAnimLifeTick[i] <= 0) {
//				continue;
//			}
//			iAnimLifeTick[i] --;
//			if(iAnimStartX[i] == iAnimEndX[i]) {
//				iAnimY[i] += iAnimSpeed[i];
//			}
//			else if(iAnimStartY[i] == iAnimEndY[i]) {
//				iAnimX[i] += iAnimSpeed[i];
//			}
//			else {
//				iAnimX[i] += iAnimSpeed[i];
//				iAnimY[i] = (int)((iAnimX[i]-iAnimStartX[i]) * iAnimLineK[i]) + iAnimStartY[i];
//			}
//			//触发第二个动画
//			if(iAnimLifeTick[i] == 0) {	
//				Project tPj = CPakManager.loadSynProject(iAnimName[i]);
//				if(tPj != null) {
//					Sprite pSprite = new Sprite();
//					pSprite.copyFrom(tPj.getSprite(iEndAnimID[i]));
//					pSprite.setPosition((short)iAnimX[i], (short)iAnimY[i]);
//					pSprite.resetFrameID();
//					iAnimSpriteList.addElement(pSprite);
//					iAnimEndMotionList.addElement(iEndMotion[i]);
//				}
//			}
//		}
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
			//触发第二个动画
			if(iHartLifeTick[i] == 0) {
				Sprite pSprite = new Sprite();
				//道具动画
				if(iHartType[i] >= 10) {
					pSprite.copyFrom(iItemSprite[iHartType[i]-10]);
					pSprite.setPosition((short)iHartX[i], (short)iHartY[i]);
					pSprite.resetFrameID();
					iItemSpriteList.addElement(pSprite);
					//播放声音
					int soundID = SoundsResID.bottle + iHartType[i] - 10;
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(soundID, 0);
				}
				//触发飘心动画
				else {
					if(iHartType[i] == 0) {
						pSprite.copyFrom(iBeDingSprite);
					}
					else {
						pSprite.copyFrom(iLoveSprite);
					}
					pSprite.setPosition((short)iHartX[i], (short)iHartY[i]);
					iHartSpriteList.addElement(pSprite);
				}
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
	
	//升级动画
	private void levelUpSpritePerform() {
		for(int i=0; i<iLevelUpSpriteList.size(); i++) {
//			System.out.println("LevelUp perform Sprite i=" + i);
			Sprite pSprite = iLevelUpSpriteList.get(i);
			if(pSprite.performAction(0) == true) {
				pSprite = null;
				iLevelUpSpriteList.remove(i);
				i --;
			}
		}
	}
	
	//道具动画
	private void itemSpritePerform() {
		for(int i=0; i<iItemSpriteList.size(); i++) {
			Sprite pSprite = iItemSpriteList.get(i);
			if(pSprite.performAction(1) == true) {
				pSprite = null;
				iItemSpriteList.remove(i);
				i --;
			}
		}
	}
	
	//唯色动画
	private void weiSeSpritePerform() {
		for(int i=0; i<iWeiSeSpriteList.size(); i++) {
			Sprite pSprite = iWeiSeSpriteList.get(i);
			if(pSprite.performAction(0) == true) {
				pSprite = null;
				iWeiSeSpriteList.remove(i);
				i --;
			}
		}
	}
	
	//加倍动画
	private void doubleSpritePerform() {
		if(!iDoubleSpriteEnd) {
			int tMotionID = 0;
			switch(iDoubleTimes) {
			case 2:
				tMotionID = 1;
				break;
				
			case 4:
				tMotionID = 2;
				break;
				
			case 8:
				tMotionID = 3;
				break;
				
			case 16:
				tMotionID = 4;
				break;
				
			case 32:
				tMotionID = 5;
				break;
				
			case 64:
				tMotionID = 6;
				break;
			}
			//
			if( iDoubleSprite.performAction(tMotionID) == true) {
				iDoubleSpriteEnd = true;
			}
			//播放声音
			if(iDoubleSprite.getFrameID() == 2) {
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.blow, 0);
			}
		}
	}
	
	//雷击动画
	private void lightningSpritePerform() {
		//闪电动画
		if(iLightState >= 0) {
			iLightState ++;
			if(iLightState < 6) {
//				iLinesX[0] = iLightStartX;
//				iLinesY[0] = iLightStartY;
//				//
//				float tOffsetY = Math.abs(iLightY - iLightStartY);
//				float tOffsetX = Math.abs(iLightX - iLightStartX);
//				//以Y为准
//				if(tOffsetY > tOffsetX) {
//					for(int i=0; i<MAX_POS_COUNT; i++) {
//						int temp = (int)(Math.random() * 50);
//						iLinesX[i] = iLightStartX + i * (iLightX - iLightStartX) / MAX_POS_COUNT + 25 - temp;
//					}
//					for(int i=0; i<MAX_POS_COUNT; i++) {
//						int temp = (int)(Math.random() * 16);
//						iLinesY[i] = iLightStartY + (i * (iLightY - iLightStartY) / MAX_POS_COUNT + 8 - temp) * ActivityUtil.DENSITY_ZOOM_Y;
//					}
//				}
//				//以X为准
//				else {
//					for(int i=0; i<MAX_POS_COUNT; i++) {
//						int temp = (int)(Math.random() * 50);
//						if(tOffsetX < 1) {
//							tOffsetX = 1;
//						}
//						iLinesY[i] = iLightStartY + i * (iLightY - iLightStartY) / MAX_POS_COUNT + 25 - temp;
//					}
//					for(int i=0; i<MAX_POS_COUNT; i++) {
//						int temp = (int)(Math.random() * 16);
//						iLinesX[i] = iLightStartX + (i * (iLightX - iLightStartX) / MAX_POS_COUNT + 8 - temp) * ActivityUtil.DENSITY_ZOOM_X;
//					}
//				}
//				iLinesX[0] = iLightStartX;
//				iLinesY[0] = iLightStartY;
//				iLinesX[MAX_POS_COUNT-1] = iLightX;
//				iLinesY[MAX_POS_COUNT-1] = iLightY;
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.electric, 0);				
				for(int i = 0; i < CURLINES; i++) {
					lightnings[i].strikeRandom();
				}
			}
			//爆炸
			if(iLightState == 3) {
				//MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.harm, 0);				
				iLightningExpEnd = false;
				iLightningExp.resetFrameID();
			}
			//是否反弹
			if(iLightState == 7 && iNextDesSeatID >= 0) {
				int bmpH = iPlayerList[iNextDesSeatID].iBmp.getHeight();
				iNextLightStartX = iLightX;//iPlayerList[iChangedSeatID[iChopSeatID]].iX;
				iNextLightStartY = iLightY;//iPlayerList[iChangedSeatID[iChopSeatID]].iY - (bmpH>>1);
				iNextLightX = iPlayerList[iChangedSeatID[iNextDesSeatID]].iX;
				iNextLightY = iPlayerList[iChangedSeatID[iNextDesSeatID]].iY - (bmpH>>1);
				iNextLightState = 0;
				iNextDesLightningID = iNextDesSeatID;
				iNextDesSeatID = -1;
				for(int i = 0; i < CURLINES; i++) {
					lightnings[i].setStrikePoint((int)iNextLightStartX, (int)iNextLightStartY);
					lightnings[i].setStrikePoint2((int)iNextLightX, (int)iNextLightY);
					lightnings[i].setDetTime(8);
					lightnings[i].setVisible(true);
				}
			}
			//
			if(iLightState >= 18) {
				iLightState = -1;
			}
		}
		if(!iLightningExpEnd) {
			if(iLightningExp.performAction(iLightningType) == true) {
				iLightningExpEnd = true;
			}
		}
		//反弹闪电动画
		if(iNextLightState >= 0) {
			iNextLightState ++;
			if(iNextLightState < 6) {
//				iNextLinesX[0] = iNextLightStartX;
//				iNextLinesY[0] = iNextLightStartY;
//				//
//				float tOffsetY = Math.abs(iNextLightY - iNextLightStartY);
//				float tOffsetX = Math.abs(iNextLightX - iNextLightStartX);
//				//以Y为准
//				if(tOffsetY > tOffsetX) {
//					for(int i=0; i<MAX_POS_COUNT; i++) {
//						int temp = (int)(Math.random() * 50);
//						iNextLinesX[i] = iNextLightStartX + i * (iNextLightX - iNextLightStartX) / MAX_POS_COUNT + 25 - temp;
//					}
//					for(int i=0; i<MAX_POS_COUNT; i++) {
//						int temp = (int)(Math.random() * 16);
//						iNextLinesY[i] = iNextLightStartY + (i * (iNextLightY - iNextLightStartY) / MAX_POS_COUNT + 8 - temp) * ActivityUtil.DENSITY_ZOOM_Y;
//					}
//				}
//				//以X为准
//				else {
//					for(int i=0; i<MAX_POS_COUNT; i++) {
//						int temp = (int)(Math.random() * 50);
//						if(tOffsetX < 1) {
//							tOffsetX = 1;
//						}
//						iNextLinesY[i] = iNextLightStartY + i * (iNextLightY - iNextLightStartY) / MAX_POS_COUNT + 25 - temp;
//					}
//					for(int i=0; i<MAX_POS_COUNT; i++) {
//						int temp = (int)(Math.random() * 16);
//						iNextLinesX[i] = iNextLightStartX + (i * (iNextLightX - iNextLightStartX) / MAX_POS_COUNT + 8 - temp) * ActivityUtil.DENSITY_ZOOM_X;
//					}
//				}
//				iNextLinesX[0] = iNextLightStartX;
//				iNextLinesY[0] = iNextLightStartY;
//				iNextLinesX[MAX_POS_COUNT-1] = iNextLightX;
//				iNextLinesY[MAX_POS_COUNT-1] = iNextLightY;
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.electric, 0);
				for(int i = 0; i < CURLINES; i++) {
					lightnings[i].strikeRandom();
				}
			}
			//爆炸
			if(iNextLightState == 3) {
				//MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.harm, 0);				
				iNextLightningExpEnd = false;
				iNextLightningExp.resetFrameID();
			}
			//
			if(iNextLightState >= 18) {
				iNextLightState = -1;
			}
		}
		if(!iNextLightningExpEnd) {
			if(iNextLightningExp.performAction(iNextDesType) == true) {
				iNextLightningExpEnd = true;
			}
		}
	}
	
	//执行玩家的逻辑
	private void gameplayerPerform() {
		for(int i=0; i<MAX_GAME_PLAYER; i++) {
			//聊天泡泡
			if(iPlayerList[i].iChatTick > 0) {
				iPlayerList[i].iChatTick --;
			}
			//玩家动作
			if(iPlayerList[i].ibInit == false) {
				continue;
			}
			if(iPlayerList[i].iMotionID == CGamePlayer.MOTION_STATE_SITDOWNING) {
				//执行坐下动作
				if( iPlayerList[iChangedSeatID[i]].iSprite.performAction(iPlayerList[i].iMotionID) == true) {
					iPlayerList[i].iMotionID = CGamePlayer.MOTION_STATE_SITDOWNED;
					iPlayerList[iChangedSeatID[i]].iSprite.resetFrameID();
				}
			}
			//表情
			if(iPlayerList[i].iFaceSprite != null
					&& iPlayerList[i].ibFaceEnd == false) {
				if( iPlayerList[i].iFaceSprite.performAction(
						iPlayerList[i].iFaceMotionID) == true) {
//					Tools.debug("face aaaaaaaaaaaaaaaaa");
					iPlayerList[i].ibFaceEnd = true;
				}
				if(iPlayerList[i].lightning != null && iPlayerList[i].iVipLevel >= 5 && iPlayerList[i].iFaceMotionID == 2) {
					for(int j = 0; j < CGamePlayer.MAX_LIGHTNING_COUNT; j++) {
						if(iPlayerList[i].lightning[j] != null) {
							iPlayerList[i].lightning[j].strikeRandom();
						}
					}
				}
			}
		}
	}
	
	private void diceMoviePerform() {
		//骰子动画
		if(iDiceMovieState == DICE_MOVIE_STATE_RUNNING) {
			for(int i=0; i<MAX_DICE_SPRITE; i++) {
				iDiceMovieX[i] += iDiceMovieSpeedX[i];
				iDiceMovieY[i] += iDiceMovieSpeedY[i];
				if(iDiceMovieX[i] <= 0) {
					iDiceRunningTick --;
					iDiceMovieX[i] = 0;
					iDiceMovieSpeedX[i] += 1;
					iDiceMovieSpeedX[i] = -iDiceMovieSpeedX[i];
					iDiceMovieNum[i] = (int)(1 + Math.random() * 6);
					if(iDiceMovieNum[i] > 6) {
						iDiceMovieNum[i] = 6;
					}
				}
				else if(iDiceMovieX[i] >= ActivityUtil.SCREEN_WIDTH) {
					iDiceRunningTick --;
					iDiceMovieX[i] = ActivityUtil.SCREEN_WIDTH;
					iDiceMovieSpeedX[i] -= 1;
					iDiceMovieSpeedX[i] = -iDiceMovieSpeedX[i];
					iDiceMovieNum[i] = (int)(1 + Math.random() * 6);
					if(iDiceMovieNum[i] > 6) {
						iDiceMovieNum[i] = 6;
					}
				}
				if(iDiceMovieY[i] <= 0) {
					iDiceRunningTick --;
					iDiceMovieY[i] = 0;
					iDiceMovieSpeedY[i] += 1;
					iDiceMovieSpeedY[i] = -iDiceMovieSpeedY[i];
					iDiceMovieNum[i] = (int)(1 + Math.random() * 6);
					if(iDiceMovieNum[i] > 6) {
						iDiceMovieNum[i] = 6;
					}
				}
				else if(iDiceMovieY[i] >= ActivityUtil.SCREEN_HEIGHT) {
					iDiceRunningTick --;
					iDiceMovieY[i] = ActivityUtil.SCREEN_HEIGHT;
					iDiceMovieSpeedY[i] -= 1;
					iDiceMovieSpeedY[i] = -iDiceMovieSpeedY[i];
					iDiceMovieNum[i] = (int)(1 + Math.random() * 6);
					if(iDiceMovieNum[i] > 6) {
						iDiceMovieNum[i] = 6;
					}
				}
			}
			//
			if(iDiceRunningTick <= 0) {
				iDiceRunningTick = 8;
				iDiceMovieState = DICE_MOVIE_STATE_HOME;
			}
		}
		else if(iDiceMovieState == DICE_MOVIE_STATE_HOME) {
			//
			if(iServerState == E_TGS_WAIT_START
						&& iPlayerList[iSelfSeatID].iCupMotionID == 1) {
				//自动摇骰盅
				iGameTick = 0;
				iPlayerList[iSelfSeatID].iCupMotionID = 3;
				iPlayerList[iSelfSeatID].iCupSprite.resetFrameID();
				if(iShakeListener != null) {
					iShakeListener.stopShake();
				}
				//发送准备请求
				requestReady();
			}
			//
			for(int i=0; i<MAX_DICE_SPRITE; i++) {
				if(iDiceMovieX[i] > iPlayerList[iChangedSeatID[iSelfSeatID]].iCupX + 20) {
					iDiceMovieX[i] -= 20;
				}
				else if(iDiceMovieX[i] < iPlayerList[iChangedSeatID[iSelfSeatID]].iCupX - 20) {
					iDiceMovieX[i] += 20;
				}
				if(iDiceMovieY[i] > iPlayerList[iChangedSeatID[iSelfSeatID]].iCupY - 20 + 20) {
					iDiceMovieY[i] -= 20;
				}
				else if(iDiceMovieY[i] < iPlayerList[iChangedSeatID[iSelfSeatID]].iCupY - 20 - 20) {
					iDiceMovieY[i] += 20;
				}
			}
			//
			iDiceRunningTick --;
			if(iDiceRunningTick <= 0) {
				iDiceMovieState = DICE_MOVIE_STATE_IDLE;
			}
		}
	}
	
	private void playerButtonPerform() {
		//
		for(int i=0; i<iPlayerButtonList.size(); i++) {
			((SpriteButton)iPlayerButtonList.elementAt(i)).perform();
		}
	}
	
	private void deskPerform() {
		if(iSceneDesk != null && iSceneDeskEx != null) {
			iSceneDesk.handle();
			iSceneDesk.iCameraX = -(int)iSDX;
			iSceneDeskEx.handle();
//			iSceneDeskEx.iCameraX = -(int)iSDX;
		}
		if(iSceneBar != null) {
			iSceneBar.handle();
			iSceneBar.iCameraX = -(int)iSBX;
		}
		if(iSceneExp != null) {
			iSceneExp.handle();
			iSceneExp.iCameraX = -(int)iSDX;
		}
	}
	
	private boolean isSeatPlaying(int aSeatID) {
		for(int i=0; i<iPlayingCount; i++) {
			if(iPlayingSeatIDs[i] == aSeatID) {
				return true;
			}
		}
		return false;
	}
	
	private boolean findNextShowResultSeat() {
		int tempCount = 0;
		while(tempCount < MAX_GAME_PLAYER) {
			iShowingResultSeatID --;
			if(iShowingResultSeatID < 0) {
				iShowingResultSeatID += 6;
			}
			if(isSeatPlaying(iShowingResultSeatID) == true) {
				return true;
			}
			tempCount ++;
		}
		return false;
	}
	
	//创建背景图
	private void creatBackBmp() {
		if(iBackBmp == null) {
			//iBackBmp = Bitmap.createBitmap(GameMain.iImageManager.iBackABmp);
			iBackBmp = Bitmap.createBitmap(ActivityUtil.SCREEN_WIDTH, ActivityUtil.SCREEN_HEIGHT, Config.RGB_565);
		}
		Canvas g  = new Canvas(iBackBmp);
		g.drawBitmap(MyArStation.iImageManager.iRoomBmp,
				0,
				0,
				null);
		if(iSceneDesk != null && iSceneDeskEx != null) {
			iSceneDeskEx.iCameraX = 0;
			iSceneDesk.iCameraX = 0;
			iSceneDeskEx.paint(g, 0, 0);
			iSceneDesk.paint(g, 0, 0);
		}
		if(iSceneBar != null) {
			iSceneBar.iCameraX = 0;
			iSceneBar.paint(g, 0, 0);
		}
//		if(iSceneExp != null) {
//			iSceneExp.iCameraX = 0;
//			iSceneExp.paint(g, 0, 0);
//		}
		g = null;
	}
	
	//将状态栏的骰子画入背景
	private void drawBarDiceToBack() {
		Canvas g  = new Canvas(iBackBmp);
		int tempDiceX = 0;
		//显示自己的骰子
		//骰子图片
		int tempDiceModelID = iPlayerList[iSelfSeatID].iDiceID;
		int tempNextDiceCount = iPlayerList[iSelfSeatID].iNextDiceCount;
		for(int i=0; i<CGamePlayer.MAX_DICE_COUNT; i++) {
			int tempDice = iPlayerList[iSelfSeatID].iDices[i] - 1;
			if(i >= tempNextDiceCount) {
				g.drawBitmap(MyArStation.iImageManager.iDiceBmps[tempDiceModelID][tempDice],
					tempDiceX,
					iBarDiceY-MyArStation.iImageManager.iDiceBmps[0][0].getHeight(),
					null);
			}
			else {
				g.drawBitmap(MyArStation.iImageManager.iDiceBmps[tempDiceModelID+1][tempDice],
						tempDiceX,
						iBarDiceY-MyArStation.iImageManager.iDiceBmps[0][0].getHeight(),
						null);
			}
			tempDiceX += (MyArStation.iImageManager.iDiceBmps[0][0].getWidth() + 2);
		}
		g = null;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(quickSelectRoomScreen != null && quickSelectRoomScreen.onKeyDown(keyCode, event)) return true;
		if(infoPopView != null && infoPopView.onKeyDown(keyCode, event)) return true;
		if(missionView != null && missionView.onKeyDown(keyCode, event)) return true;
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//隐藏系统控件
			if(iAbLayout != null) {
//				for (int i = 0; i < mList.size(); i ++) {
//					Button button = mList.get(i);
//					//复位坐标
//					LayoutParams lp = (LayoutParams)button.getLayoutParams();
//					lp.x = mButtonStartX;
//					lp.y = mButtonStartY;
//					button.setLayoutParams(lp);
//				}
//				iAbLayout.setVisibility(View.GONE);
//				iIconButtonShow = false;
				if(iIconButtonShow == true) {
					messageEvent(GameEventID.ESPRITEBUTTON_EVENT_HIDE_SYSTEM_BUTTON);
//					return true;
				}
			}
			showReturnLoginConfirm();
			return true;
		}
		//--------------test--------------------
//		if(keyCode == KeyEvent.KEYCODE_MENU) {
//			//抢劈
//			if(iRebelChopButton.isVision()) {
//				iRebelChopButton.setVision(false);
//				requestUseItem(GameMain.iPlayer.E_RIT_REBEL_CHOP, 0);
//			}
//			return true;
//		}
		//--------------test--------------------
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		pointerPressed(aX, aY);
		return true;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		pointerReleased(aX, aY);
		return true;
	}

	@Override
	public boolean pointerPressed(int aX, int aY) {
		//
		if(iGameState == GAME_STATE_LOADING
				|| iGameState == GAME_STATE_CHANGEDESK_LOADING
				|| iGameState == GAME_STATE_JOINACTIVITY_LOADING
				|| iGameState == GAME_STATE_LEAVEROOM) {
			return true;
		}
		if(quickSelectRoomScreen.pointerPressed(aX, aY)) {
			return true;
		}
		if(infoPopView.pointerPressed(aX, aY)) {
			return true;
		}
		if(missionView.pointerPressed(aX, aY)) {
			return true;
		}
		//如果图标按钮，弹出来了
		if(iIconButtonShow == true) {
			if(iOutStartCount == iOutCompleteCount) {
				messageEvent(GameEventID.ESPRITEBUTTON_EVENT_HIDE_SYSTEM_BUTTON);
			}
		}
		//
		boolean result= false;
		Rect pRect = new Rect();
		//判断滚轮按钮
		SpriteButton pSB;
		for(int i=0; i<iSpriteButtonList.size(); i++) {
			pSB = ((SpriteButton)iSpriteButtonList.elementAt(i));
			if(pSB.isVision() == false) {
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
		//判断喊斋
		if(result == false) {
			if(iZhaiButton.isVision() == true) {
				int pX = iZhaiButton.getX();
				int pY = iZhaiButton.getY();
				Rect pLogicRect = iZhaiButton.getRect();
				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
						pX+pLogicRect.right, pY+pLogicRect.bottom);
				if (pRect.contains(aX, aY)) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					iZhaiButton.pressed();
					result = true;
				}
			}
		}
		//判断雷击
		if(result == false) {
			if(iLightningButton.isVision() == true) {
				int pX = iLightningButton.getX();
				int pY = iLightningButton.getY();
				Rect pLogicRect = iLightningButton.getRect();
				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
						pX+pLogicRect.right, pY+pLogicRect.bottom);
				if (pRect.contains(aX, aY)) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					iLightningButton.pressed();
					result = true;
				}
			}
		}
		//判断抢劈
		if(result == false) {
			if(iPreChopButton.isVision() == true) {
				int pX = iPreChopButton.getX();
				int pY = iPreChopButton.getY();
				Rect pLogicRect = iPreChopButton.getRect();
				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
						pX+pLogicRect.right, pY+pLogicRect.bottom);
				if (pRect.contains(aX, aY)) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					iPreChopButton.pressed();
					result = true;
				}
			}
		}
		//判断反劈
		if(result == false) {
			if(iRebelChopButton.isVision() == true) {
				int pX = iRebelChopButton.getX();
				int pY = iRebelChopButton.getY();
				Rect pLogicRect = iRebelChopButton.getRect();
				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
						pX+pLogicRect.right, pY+pLogicRect.bottom);
				if (pRect.contains(aX, aY)) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					iRebelChopButton.pressed();
					result = true;
				}
			}
		}
		//判断骰盅按钮
		if(result == false) {
			//当骰盅在可摇摆状态时
			if(iServerState == E_TGS_WAIT_START 
					&& iPlayerList[iSelfSeatID].iCupMotionID == 1) {
				int pX = iPlayerList[0].iX;
				int pY = iPlayerList[0].iY;
				pRect.set(pX-(ActivityUtil.SCREEN_WIDTH>>2), 
						pY-(ActivityUtil.SCREEN_HEIGHT>>1),
						pX+(ActivityUtil.SCREEN_WIDTH>>2), 
						pY);
//				System.out.println("l=" + (pX-(ActivityUtil.SCREEN_WIDTH>>2))
//						+ " t=" + (pY-(ActivityUtil.SCREEN_HEIGHT>>1))
//						+ " r=" + (pX+(ActivityUtil.SCREEN_WIDTH>>2))
//						+ " b=" + pY
//						+ " aX=" + aX
//						+ " aY=" + aY);
				if (pRect.contains(aX, aY)) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.shakadice, 0);
					result = true;
					iGameTick = 0;
					iPlayerList[iSelfSeatID].iCupMotionID = 3;
					iPlayerList[iSelfSeatID].iCupSprite.resetFrameID();
					if(iShakeListener != null) {
						iShakeListener.stopShake();
					}
					//发送准备请求
					requestReady();
				}
			}
		}
		//判断玩家按钮
		if(result == false) {
			for(int i=0; i<iPlayerButtonList.size(); i++) {
				pSB = ((SpriteButton)iPlayerButtonList.elementAt(i));
				if(pSB.isVision() == false) {
//					System.out.println("Player pointerPressed not visible!");
					continue;
				}
				int pX = pSB.getX();
				int pY = pSB.getY();
				Rect pLogicRect = pSB.getRect();
				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
						pX+pLogicRect.right, pY+pLogicRect.bottom);
//				System.out.println("Player Rect = " + pRect.toString());
				if (pRect.contains(aX, aY)) {
					int realSeatID = iSelfSeatID + i;
					realSeatID %= 6;
					if(iPlayerList[realSeatID].ibInit == true) {
						MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
						ItemAction(realSeatID + GameEventID.ESPRITEBUTTON_EVENT_PLAYER);
						result = true;
					}
					break;
				}
			}
		}
		//判断聊天按钮
		if(result == false) {
			if(iWheelVisible && !iWheelForceChop) {
				return false;
			}
			//聊天按钮
			if(iChatButton.isVision() == true) {
				int pX = iChatButton.getX();
				int pY = iChatButton.getY();
				Rect pLogicRect = iChatButton.getRect();
				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
						pX+pLogicRect.right, pY+pLogicRect.bottom);
				if (pRect.contains(aX, aY)) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					iChatButton.pressed();
					result = true;
				}
			}
			//表情按钮
			if(result == false && iFaceButton.isVision() == true) {
				int pX = iFaceButton.getX();
				int pY = iFaceButton.getY();
				Rect pLogicRect = iFaceButton.getRect();
				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
						pX+pLogicRect.right, pY+pLogicRect.bottom);
				if (pRect.contains(aX, aY)) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					iFaceButton.pressed();
					result = true;
				}
			}
			//任务按钮
			if(result == false && iGiftBUtton.isVision() == true) {
				int pX = iGiftBUtton.getX();
				int pY = iGiftBUtton.getY();
				Rect pLogicRect = iGiftBUtton.getRect();
				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
						pX+pLogicRect.right, pY+pLogicRect.bottom);
				if (pRect.contains(aX, aY)) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					iGiftBUtton.pressed();
					result = true;
				}
			}
			//菜单按钮
			if(result == false && iMenuButton.isVision() == true) {
				int pX = iMenuButton.getX();
				int pY = iMenuButton.getY();
				Rect pLogicRect = iMenuButton.getRect();
				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
						pX+pLogicRect.right, pY+pLogicRect.bottom);
				if (pRect.contains(aX, aY)) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					iMenuButton.pressed();
					result = true;
				}
			}
		}
		return result;
	}

	@Override
		public boolean onLongPress(MotionEvent e) {
	//		System.out.println("ProgressScreen onLongPress event.getAction() = " +e.getAction());
//			int aX = (int) e.getX();
//			int aY = (int) e.getY();
//			pointerReleased(aX, aY);
			return false;
		}

	@Override
	public boolean pointerReleased(int aX, int aY) {
		if(quickSelectRoomScreen!=null) {
			return quickSelectRoomScreen.pointerReleased(aX, aY);
		}
		if(infoPopView!=null) {
			return infoPopView.pointerReleased(aX, aY);
		}
		if(missionView!=null) {
			return missionView.pointerReleased(aX, aY);
		}
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(iWheelVisible == false) {
			return false;
		}
		int aX = (int)event.getX();
		int aY = (int)event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			{
				//判断滚轮
				if(iWheelVisible == false) {
					return false;
				}
//				System.out.println("onTouchEvent Down aX=" + aX + " aY=" + aY);
				if (iWheelNumRect.contains(aX, aY)) {
					if(iWheelRunTick > 0) {
						iWheelRunTick = 0;
						iWheelRunPart = 0;
						iWheelRunDir = 0;
					}
					iWheelPressTick = System.currentTimeMillis();
					iTuchPart = 1;
					iLastTuchPart = 1;
					iPressedY = aY;
					//如果图标按钮，弹出来了
					if(iIconButtonShow == true) {
						messageEvent(GameEventID.ESPRITEBUTTON_EVENT_HIDE_SYSTEM_BUTTON);
					}
				}
				else if(iWheelDiceRect.contains(aX, aY)) {
					if(iWheelRunTick > 0) {
						iWheelRunTick = 0;
						iWheelRunPart = 0;
						iWheelRunDir = 0;
					}
					iWheelPressTick = System.currentTimeMillis();
					iTuchPart = 2;
					iLastTuchPart = 2;
					iPressedY = aY;
//					System.out.println("onTouchEvent TuchPart = 2 iPressedY = " + iPressedY);
					//如果图标按钮，弹出来了
					if(iIconButtonShow == true) {
						messageEvent(GameEventID.ESPRITEBUTTON_EVENT_HIDE_SYSTEM_BUTTON);
					}
				}
			}
			break;
			
		case MotionEvent.ACTION_MOVE:
			{
				if(iTuchPart == 1) {
					int tempTop = (int)(-(iWheelMaxNum - iWheelMinNum) * iLeftDist);
					iLeftOffset = iLastLeftOffset + (aY - iPressedY);
					if(iLeftOffset < tempTop) {
						iLeftOffset = tempTop;
					}
					else {
						int tempMinNum = iWheelMinNum;
						if(iLastShoutDice == 1) {
							if(tempMinNum <= iLastShoutCount) {
								tempMinNum = iLastShoutCount + 1;
							}
						}
						else {
							if(iLastShoutCount > tempMinNum) {
								tempMinNum = iLastShoutCount;
							}
						}
						if(iLeftOffset > (iWheelMinNum-tempMinNum)*iLeftDist) {
							iLeftOffset = (iWheelMinNum-tempMinNum)*iLeftDist;
						}
					}
				}
				else if(iTuchPart == 2) {
					int tempTop = (int)(-5 * iLeftDist);
					iRightOffset = iLastRightOffset + (aY - iPressedY);
					if(iRightOffset < tempTop) {
						iRightOffset = tempTop;
					}
					else {
						if(iRightOffset > 0) {
							iRightOffset = 0;
						}
						if(iLastShoutCount >= iWheelCurrentNum) {
							int tempDice = iLastShoutDice + 1;
							tempTop = (int)(-(tempDice - iLastShoutDice) * iLeftDist);
							if(iRightOffset > tempTop) {
								iRightOffset = tempTop;
							}
						}
					}
				}
			}
			break;
			
		case MotionEvent.ACTION_UP:
			{
//				int aX = (int)event.getX();
//				int aY = (int)event.getY();
//				System.out.println("onTouchEvent ACTION_UP aX=" + aX + " aY=" + aY);
				if(iWheelVisible == false || iWheelForceChop == true || iTuchPart <= 0) {
					return false;
				}
				iWheelPressTick = System.currentTimeMillis() - iWheelPressTick;
				if(iWheelPressTick < 250) {
//					MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
					if(aY < iPressedY - 100 * ActivityUtil.ZOOM_Y) {
						iWheelRunPart = iTuchPart;
						iWheelRunDir = 1; //向上滚动
						iWheelRunTick = 32;
						iRunSpeed = (int)(24 * ActivityUtil.ZOOM_Y);
					}
					else if(aY < iPressedY - 20 * ActivityUtil.ZOOM_Y) {
						iWheelRunPart = iTuchPart;
						iWheelRunDir = 1; //向上滚动
						iWheelRunTick = 4;
						iRunSpeed = (int)(24 * ActivityUtil.ZOOM_Y);
					}
				
//					MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
					else if(aY > iPressedY + 60 * ActivityUtil.ZOOM_Y) {
						iWheelRunPart = iTuchPart;
						iWheelRunDir = 2; //向下滚动
						iWheelRunTick = 32;
						iRunSpeed = (int)(24 * ActivityUtil.ZOOM_Y);
					}
					else if(aY > iPressedY + 10 * ActivityUtil.ZOOM_Y) {
						iWheelRunPart = iTuchPart;
						iWheelRunDir = 2; //向下滚动
						iWheelRunTick = 4;
						iRunSpeed = (int)(24 * ActivityUtil.ZOOM_Y);
					}
				}
				iPressedY = 0;
				iTuchPart = 0;
				iLastLeftOffset = iLeftOffset;
				iLastRightOffset = iRightOffset;
			}
			break;
		}
		return false;
	}
	
	private void wheelNumDragUp() {
		iLeftOffset -= iRunSpeed;
		int tempTop = (int)(-(iWheelMaxNum - iWheelMinNum) * iLeftDist);
		if(iLeftOffset < tempTop) {
			iLeftOffset = tempTop;
			iWheelRunTick = 0;
		}
		iLastLeftOffset = iLeftOffset;
	}
	
	private void wheelNumDragDown() {
		int tempMinNum = iWheelMinNum;
		if(iLastShoutDice == 1) {
			if(tempMinNum <= iLastShoutCount) {
				tempMinNum = iLastShoutCount + 1;
			}
		}
		else {
			if(iLastShoutCount > tempMinNum) {
				tempMinNum = iLastShoutCount;
			}
		}
		iLeftOffset += iRunSpeed;
		if(iLeftOffset > (iWheelMinNum-tempMinNum)*iLeftDist) {
			iLeftOffset = (iWheelMinNum-tempMinNum)*iLeftDist;
			iWheelRunTick = 0;
		}
		iLastLeftOffset = iLeftOffset;
	}
	
	private void wheelDiceDragUp() {
		iRightOffset -= iRunSpeed;
		int tempTop = (int)(-5 * iLeftDist);
		if(iRightOffset < tempTop) {
			iRightOffset = tempTop;
			iWheelRunTick = 0;
		}
		iLastRightOffset = iRightOffset;
	}
	
	private void wheelDiceDragDown() {
		iRightOffset += iRunSpeed;
		if(iLastShoutCount >= iWheelCurrentNum) {
			int tempDice = iLastShoutDice + 1;
			int tempTop = (int)(-(tempDice - iLastShoutDice) * iLeftDist);
			if(iRightOffset > tempTop) {
				iRightOffset = tempTop;
			}
		}
		if(iRightOffset > 0) {
			iRightOffset = 0;
		}
		iLastRightOffset = iRightOffset;
	}
	
	
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
		Tools.debug("ClassicalGame::OnProcessCmd aMsgID=" + aMsgID);
//		if(stroeDialog != null) {
//			stroeDialog.OnProcessCmd(socket, aMobileType, aMsgID);
//		}
		if(quickSelectRoomScreen != null) quickSelectRoomScreen.OnProcessCmd(socket, aMobileType, aMsgID);
		if(infoPopView != null) infoPopView.OnProcessCmd(socket, aMobileType, aMsgID);
		if(missionView != null) missionView.OnProcessCmd(socket, aMobileType, aMsgID);
		MobileMsg pMobileMsg = (MobileMsg) socket;
		Tools.debug("ClassicalGame::OnProcessCmd 1");
		switch (aMobileType) {
		case IM.IM_NOTIFY:
		{
			switch (aMsgID) {
				case NotifyMsg.MSG_NOTIFY_LOGINROOM:
					MBNotifyLoginRoom pNotifyLoginRoom = (MBNotifyLoginRoom)pMobileMsg.m_pMsgPara;
					if(pNotifyLoginRoom == null) {
						break;
					}
					Tools.debug(pNotifyLoginRoom.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_LOGINROOM, pNotifyLoginRoom);
					break;
					
				case NotifyMsg.MSG_NOTIFY_LOGINROOM_B:
					MBNotifyLoginRoomB pNotifyLoginRoomB = (MBNotifyLoginRoomB)pMobileMsg.m_pMsgPara;
					if(pNotifyLoginRoomB == null) {
						break;
					}
					Tools.debug(pNotifyLoginRoomB.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_LOGINROOM_B, pNotifyLoginRoomB);
					break;
					
				case NotifyMsg.MSG_NOTIFY_READY:
					MBNotifyReady notifyReady = (MBNotifyReady)pMobileMsg.m_pMsgPara;
					if(notifyReady == null) {
						break;
					}
					Tools.debug(notifyReady.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_READY, notifyReady);
//					seatID = notifyReady.iSeatID;
//					playerReady(seatID);
					break;
				case NotifyMsg.MSG_NOTIFY_SHOW_READY:
					MBNotifyShowReady notifyShowReady = (MBNotifyShowReady)pMobileMsg.m_pMsgPara;
					if(notifyShowReady == null) {
						break;
					}
//					Tools.debug(notifyShowReady.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_SHOW_READY, notifyShowReady);
					break;
				case NotifyMsg.MSG_NOTIFY_START:
					MBNotifyStart nitNotifyStart = (MBNotifyStart)pMobileMsg.m_pMsgPara;
					if(nitNotifyStart == null) {
						break;
					}
					Tools.debug(nitNotifyStart.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_START, nitNotifyStart);
//					iPlayingCount = nitNotifyStart.iPlayerCount;
//					iReadySB.setVision(false);
//					iReadyTimeSB.setVision(false);
//					gameRoll();
					break;
				case NotifyMsg.MSG_NOTIFY_SHOUT:
					MBNotifyShout notifyShout = (MBNotifyShout)pMobileMsg.m_pMsgPara;
					if(notifyShout == null) {
						break;
					}
					Tools.debug(notifyShout.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyShout, notifyShout);
					break;
				case NotifyMsg.MSG_NOTIFY_CHOP:
					MBNotifyChop notifyChop = (MBNotifyChop)pMobileMsg.m_pMsgPara;
					if(notifyChop == null) {
						break;
					}
					Tools.debug(notifyChop.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyChop, notifyChop);
					break;
				case NotifyMsg.MSG_NOTIFY_RESULT:
					MBNotifyResult notifyResult = (MBNotifyResult)pMobileMsg.m_pMsgPara;
					if(notifyResult == null) {
						break;
					}
					Tools.debug(notifyResult.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyResult, notifyResult);
					break;
				case NotifyMsg.MSG_NOTIFY_LEAVEROOM:
					MBNotifyLeaveRoom notifyLeaveRoom = (MBNotifyLeaveRoom)pMobileMsg.m_pMsgPara;
					if(notifyLeaveRoom == null) {
						break;
					}
					Tools.debug(notifyLeaveRoom.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_LeaveRoom, notifyLeaveRoom);
					break;
				case NotifyMsg.MSG_NOTIFY_KICK:
					removeProgressDialog();
					MBNotifyKick notifyKick = (MBNotifyKick)pMobileMsg.m_pMsgPara;
					if(notifyKick == null) {
						break;
					}
					Tools.debug(notifyKick.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyKICKOpenHomeScreen, notifyKick);
					break;
				case NotifyMsg.MSG_NOTIFY_SYSMSG:
					removeProgressDialog();
					MBNotifySysMsg sNotifySysMsg = (MBNotifySysMsg)pMobileMsg.m_pMsgPara;
					if(sNotifySysMsg == null) {
						break;
					}
//					System.out.println("NotifySysMsg 0000000000000000 ");
					Tools.debug(sNotifySysMsg.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_SYS_MSG, sNotifySysMsg);
					break;
				case NotifyMsg.MSG_NOTIFY_CHAT:
					MBNotifyChat notifyChat = (MBNotifyChat)pMobileMsg.m_pMsgPara;
					if(notifyChat == null) {
						break;
					}
					Tools.debug(notifyChat.toString());
					if(notifyChat.nResult == ResultCode.SUCCESS) {
						messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyChat, notifyChat);
					}
					break;
				case NotifyMsg.MSG_NOTIFY_CLEAN_DRUNK:
					removeProgressDialog();
					MBNotifyCleanDrunk rspCleanDrunk = (MBNotifyCleanDrunk)pMobileMsg.m_pMsgPara;
					if(rspCleanDrunk == null) {
						break;
					}
					Tools.debug(rspCleanDrunk.toString());
					int type = rspCleanDrunk.iResult;
					switch (type) {
					case ResultCode.SUCCESS://成功
						messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyCleanDunk, rspCleanDrunk);
						break;
						
					default:
						messageEvent(type, rspCleanDrunk.iMsg);
						break;
					}
					break;
				case NotifyMsg.MSG_NOTIFY_ACTIVITY_START:
					removeProgressDialog();
					MBNotifyActivityStart rspActivityStart = (MBNotifyActivityStart)pMobileMsg.m_pMsgPara;
					if(rspActivityStart == null) {
						break;
					}
					Tools.debug(rspActivityStart.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_ACIIVITY_START, rspActivityStart);
					break;
					
				case NotifyMsg.MSG_NOTIFY_ACTIVITY_RANKING:
					removeProgressDialog();
					MBNotifyActivityRanking rspActivityRanking = (MBNotifyActivityRanking)pMobileMsg.m_pMsgPara;
					if(rspActivityRanking == null) {
						break;
					}
					Tools.debug(rspActivityRanking.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_RANKING_SELF, rspActivityRanking);
					break;
				case NotifyMsg.MSG_NOTIFY_SHOW_ITEM:
					MBNotifyShowItem rspShowItem = (MBNotifyShowItem)pMobileMsg.m_pMsgPara;
					if(rspShowItem == null) {
						break;
					}
					Tools.debug(rspShowItem.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_SHOWITEM, rspShowItem);
					break;
				case NotifyMsg.MSG_NOTIFY_VIE_CHOP:
					MBNotifyVieChop rspVieChop = (MBNotifyVieChop)pMobileMsg.m_pMsgPara;
					if(rspVieChop == null) {
						break;
					}
					Tools.debug(rspVieChop.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_NOTIFY_VIE_CHOP, rspVieChop);
					break;
				case NotifyMsg.MSG_NOTIFY_CHANGE_DICE:
					MBNotifyChangeDice notifyChangeDice = (MBNotifyChangeDice)pMobileMsg.m_pMsgPara;
					if(notifyChangeDice == null) {
						break;
					}
					Tools.debug(notifyChangeDice.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_CHANGE_DICE, notifyChangeDice);
					break;
				case NotifyMsg.MSG_NOTIFY_SHIELD:
					MBNotifyShield notifyShield = (MBNotifyShield)pMobileMsg.m_pMsgPara;
					if(notifyShield == null) {
						break;
					}
					Tools.debug(notifyShield.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_SHIELD, notifyShield);
					break;
				case NotifyMsg.MSG_NOTIFY_MANY_CHOP:
					MBNotifyManyChop notifyManyChop = (MBNotifyManyChop)pMobileMsg.m_pMsgPara;
					if(notifyManyChop == null) {
						break;
					}
					Tools.debug(notifyManyChop.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_MANY_CHOP, notifyManyChop);
					break;
				case NotifyMsg.MSG_NOTIFY_TRIGGER_SHIELD:
					MBNotifyTriggerShield notifyTriggerShield = (MBNotifyTriggerShield)pMobileMsg.m_pMsgPara;
					if(notifyTriggerShield == null) {
						break;
					}
					Tools.debug(notifyTriggerShield.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_TRIGGER_SHIELD, notifyTriggerShield);
					break;
				case NotifyMsg.MSG_NOTIFY_KILL_ORDER:
					MBNotifyKillOrder notifyKillOrder = (MBNotifyKillOrder)pMobileMsg.m_pMsgPara;
					if(notifyKillOrder == null) {
						break;
					}
					Tools.debug(notifyKillOrder.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_KILL_ORDER, notifyKillOrder);
					break;
				case NotifyMsg.MSG_NOTIFY_UNARMED:
					MBNotifyUnarmed notifyUnarmed = (MBNotifyUnarmed)pMobileMsg.m_pMsgPara;
					if(notifyUnarmed == null) {
						break;
					}
					Tools.debug(notifyUnarmed.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_UNARMED, notifyUnarmed);
				case NotifyMsg.MSG_NOTIFY_DING:
					MBNotifyDing notifyDing = (MBNotifyDing)pMobileMsg.m_pMsgPara;
					if(notifyDing == null) {
						break;
					}
					Tools.debug(notifyDing.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_DING, notifyDing);
					break;
				case NotifyMsg.MSG_NOTIFY_LEVELUP:
					MBNotifyLevelUp notifyLevelUp = (MBNotifyLevelUp)pMobileMsg.m_pMsgPara;
					if(notifyLevelUp == null) {
						break;
					}
					Tools.debug(notifyLevelUp.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_LEVELUP, notifyLevelUp);
					break;
				case NotifyMsg.MSG_NOTIFY_FRIEND_EVENT:
					MBNotifyFriendEvent notifyFriendEvent = (MBNotifyFriendEvent)pMobileMsg.m_pMsgPara;
					if(notifyFriendEvent == null) {
						break;
					}
					Tools.debug(notifyFriendEvent.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_FRINDEVENT, notifyFriendEvent);
					break;
				case NotifyMsg.MSG_NOTIFY_PLAY_ANIMATION:
					MBNotifyPlayAnimation notifyPlayAnimation = (MBNotifyPlayAnimation)pMobileMsg.m_pMsgPara;
					if(notifyPlayAnimation == null) {
						break;
					}
					Tools.debug(notifyPlayAnimation.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_PALY_ANIMATION, notifyPlayAnimation);
					break;
				case NotifyMsg.MSG_NOTIFY_PLAY_ANIMATION_TWO:
					MBNotifyPlayAnimationTwo notifyPlayAnimationTwo = (MBNotifyPlayAnimationTwo)pMobileMsg.m_pMsgPara;
					if(notifyPlayAnimationTwo == null) {
						break;
					}
					Tools.println(notifyPlayAnimationTwo.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_PALY_ANIMATIONTWO, notifyPlayAnimationTwo);
					break;
				case NotifyMsg.MSG_NOTIFY_FACE:
					MBNotifyFace notifyFace = (MBNotifyFace)pMobileMsg.m_pMsgPara;
					if(notifyFace == null) {
						break;
					}
					Tools.debug(notifyFace.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_FACE, notifyFace);
					break;
					
				case NotifyMsg.MSG_NOTIFY_REBEL_CHOP:
					MBNotifyRebelChop notifyRebelChop = (MBNotifyRebelChop)pMobileMsg.m_pMsgPara;
					if(notifyRebelChop == null) {
						break;
					}
					Tools.debug(notifyRebelChop.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_REBEL_CHOP, notifyRebelChop);
					break;
				case NotifyMsg.MSG_NOTIFY_SHAKE_FRIEND:
					MBNotifyShakeFriend notifyShakeFriend = (MBNotifyShakeFriend)pMobileMsg.m_pMsgPara;
					if(notifyShakeFriend == null) {
						break;
					}
					Tools.debug(notifyShakeFriend.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_SHAKE_FIREND, notifyShakeFriend);
					break;
				case NotifyMsg.MSG_NOTIFY_LIGHTNING:
					MBNotifyLightning notifyLightning = (MBNotifyLightning)pMobileMsg.m_pMsgPara;
					if(notifyLightning == null) {
						break;
					}
					Tools.debug(notifyLightning.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_LIGHTNING, notifyLightning);
					break;
				case NotifyMsg.MSG_NOTIFY_VIE_CHOP_B:
					MBNotifyVieChopB rspVieChopB = (MBNotifyVieChopB)pMobileMsg.m_pMsgPara;
					if(rspVieChopB == null) {
						break;
					}
					Tools.debug(rspVieChopB.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_NOTIFY_VIE_CHOP_B, rspVieChopB);
					break;
					
				default:
					break;
			}
		}
			break;
		case IM.IM_RESPONSE:
		{
			switch (aMsgID) {
			case RspMsg.MSG_RSP_LOGINROOM_B:
//				removeProgressDialog();
				MBRspLoginRoomB rspLoginRoom = (MBRspLoginRoomB)pMobileMsg.m_pMsgPara;
				if(rspLoginRoom == null) {
					break;
				}
				Tools.debug(rspLoginRoom.iMsg);
				int type = rspLoginRoom.iResult;
				Tools.println(rspLoginRoom.toString());
				switch (type) {
				case ResultCode.SUCCESS://成功
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_LOGINROOM_B, rspLoginRoom);
					break;
					
				default:
					messageEvent(type, rspLoginRoom.iMsg);
					break;
				}
				break;
			case RspMsg.MSG_RSP_PLAYINFO:
				MBRspPlayerInfo rspPlayerInfo = (MBRspPlayerInfo)pMobileMsg.m_pMsgPara;
				if(rspPlayerInfo == null) {
					break;
				}
				Tools.debug(rspPlayerInfo.iMsg);
				type = rspPlayerInfo.iResult;
				Tools.debug(rspPlayerInfo.toString());
				switch (type) {
				case ResultCode.SUCCESS://成功
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspPlayerInfo, rspPlayerInfo);
					break;
					
				default:
					messageEvent(type, rspPlayerInfo.iMsg);
					break;
				}
				break;
			case RspMsg.MSG_RSP_BUY_WINE:
				removeProgressDialog();
				MBRspBuyWine rspBuyWine = (MBRspBuyWine)pMobileMsg.m_pMsgPara;
				if(rspBuyWine == null) {
					break;
				}
				Tools.debug(rspBuyWine.iMsg);
				type = rspBuyWine.iResult;
				Tools.debug(rspBuyWine.toString());
				switch (type) {
				case ResultCode.SUCCESS://成功
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspBuyWine, rspBuyWine);
					break;
					
				default:
					messageEvent(type, rspBuyWine.iMsg);
					break;
				}
				break;
			case RspMsg.MSG_RSP_PLAYINFO_TWO:
				removeProgressDialog();
				MBRspPlayerInfoTwo rspMainPage = (MBRspPlayerInfoTwo)pMobileMsg.m_pMsgPara;
				if(rspMainPage == null) {
					break;
				}
				Tools.debug(rspMainPage.toString());
				type = rspMainPage.iResult;
				if(type == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_PlayerInfoTwo, rspMainPage);
				}
				else {
					messageEvent(type, rspMainPage.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_PLAYINFO_THR:
				removeProgressDialog();
				MBRspPlayerInfoThree infoThree = (MBRspPlayerInfoThree)pMobileMsg.m_pMsgPara;
				if(infoThree == null) {
					break;
				}
				Tools.debug(infoThree.toString());
				type = infoThree.iResult;
				if(type == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_PALYERINFOTHR, infoThree);
				}
				else {
					messageEvent(type, infoThree.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_PLAYINFO_FOUR:
				MBRspPlayerInfoFour infoFour = (MBRspPlayerInfoFour)pMobileMsg.m_pMsgPara;
				if(infoFour == null) {
					break;
				}
				Tools.debug(infoFour.toString());
				type = infoFour.iResult;
				if(type == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_PALYERINFOFOUR, infoFour);
				}
				else {
					messageEvent(type, infoFour.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_KICK:
//				removeProgressDialog();
				MBRspKick rspKick = (MBRspKick)pMobileMsg.m_pMsgPara;
				if(rspKick == null) {
					break;
				}
//				Tools.debug(rspKick.toString());
				type = rspKick.nResult;
//				if(type == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_KICK, rspKick);
//				}
//				else {
//					messageEvent(type, rspKick.iMsg);
//				}
				break;
			case RspMsg.MSG_RSP_ADD_FRIEND:
				removeProgressDialog();
				MBRspAddFriend rspAddFriend = (MBRspAddFriend)pMobileMsg.m_pMsgPara;
				if(rspAddFriend == null) {
					break;
				}
				Tools.debug(rspAddFriend.toString());
				type = rspAddFriend.iResult;
				if(type == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_AddFriend, rspAddFriend);
				}
				else {
					messageEvent(type, rspAddFriend.iMsg);
				}
				break;
			
			case RspMsg.MSG_RSP_DING:
				removeProgressDialog();
				MBRspDing rspDing = (MBRspDing)pMobileMsg.m_pMsgPara;
				if(rspDing == null) {
					break;
				}
				Tools.debug(rspDing.toString());
				type = rspDing.iResult;
//				if(type == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspDing, rspDing);
//				}
//				else {
//					messageEvent(type, rspDing.iMsg);
//				}
				break;
			
			case RspMsg.MSG_RSP_ACTIVITY_ENROLL:
				MBRspActivityEnroll rspActivityEnroll = (MBRspActivityEnroll)pMobileMsg.m_pMsgPara;
				if(rspActivityEnroll == null) {
					break;
				}
				Tools.debug(rspActivityEnroll.toString());
				type = rspActivityEnroll.nResult;
				if(type == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_ENROLL, rspActivityEnroll);
				}
				else {
					messageEvent(type, rspActivityEnroll.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_LEAVEROOM:
				MBRspLeaveRoom rspLeaveRoom = (MBRspLeaveRoom)pMobileMsg.m_pMsgPara;
				if(rspLeaveRoom == null) {
					break;
				}
//				Tools.debug(rspLeaveRoom.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspLeaveRoom, rspLeaveRoom);
				break;
			case RspMsg.MSG_RSP_ACTIVITY_RANK:
				MBRspActivityRank rspActivityRank = (MBRspActivityRank)pMobileMsg.m_pMsgPara;
				if(rspActivityRank == null) {
					break;
				}
				Tools.debug(rspActivityRank.toString());
				if(rspActivityRank.nResult == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_RANK, rspActivityRank);
				}
				else {
					messageEvent(rspActivityRank.nResult, rspActivityRank.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_USE_ITEM:
				MBRspUseItem rspUseItem = (MBRspUseItem)pMobileMsg.m_pMsgPara;
				if(rspUseItem == null) {
					break;
				}
				Tools.debug(rspUseItem.toString());
				if(rspUseItem.nResult == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_USEITEM, rspUseItem);
				}
				else {
					messageEvent(rspUseItem.nResult, rspUseItem.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_FRIEND_EVENT_SELECT:
				MBRspFriendEventSelect rspFriendEventSelect = (MBRspFriendEventSelect)pMobileMsg.m_pMsgPara;
				if(rspFriendEventSelect == null) {
					break;
				}
				Tools.debug(rspFriendEventSelect.toString());
				if(rspFriendEventSelect.iResult == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_RSP_FRINDEVENTSELECT, rspFriendEventSelect);
				}
				else {
					messageEvent(rspFriendEventSelect.iResult, rspFriendEventSelect.iMsg);
				}
				break;
				
			default:
				break;
			}
		}
		break;

		default:
//			if(stroeDialog != null) {
//				stroeDialog.OnProcessCmd(socket, aMobileType, aMsgID);
//			}
			break;
		}
		
		Tools.debug("ClassicalGame::OnProcessCmd end");
	}
	
	@Override
	public void handleMessage(Message msg) {
		if (msg == null) {
			return;
		}
		switch (msg.what) {
		case ResultCode.ERR_DECODEBUF:
		case ResultCode.ERR_TIMEOUT:
		case ResultCode.ERR_BUSY: 
		case ResultCode.ERR_ROOM_TABLE_INVALED:
		case ResultCode.ERR_ROOM_TABLE_FULL:
		case ResultCode.ERR_ROOM_FULL:
		case ResultCode.ERR_ROOM_GB_NOTENOUGH:
		case ResultCode.ERR_ROOM_DRUNK:
		case ResultCode.ERR_ROOM_PLAYER_INVALED:
		case ResultCode.ERR_ROOM_GB_LIMIT:
		case ResultCode.ERR_KICK_VIP:
			{
				Context tContext = MyArStation.mGameMain.getApplicationContext();
				if((String)msg.obj != null) {
					Tools.showSimpleToast(tContext, Gravity.CENTER, (String)msg.obj, Toast.LENGTH_LONG);
				}
				removeProgressDialog();
				messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
			}
			break;
		case ResultCode.ERR_FRIEND_ADD_SELF:
		case ResultCode.ERR_KICK_HIGH_VIP:
		case ResultCode.ERR_FRIEND_OTHERSIDE_LIMIT:
		case ResultCode.ERR_FRIEND_UID_INVALED:
		case ResultCode.ERR_FRIEND_ALREADY:
		case ResultCode.ERR_FRIEND_MYSIDE_LIMIT:
		case ResultCode.ERR_KICK_NO_VIP:
		case ResultCode.ERR_FRIEND_VISITOR:
		case ResultCode.ERR_KICK_NOT_FIND_PLAYER:
		case ResultCode.ERR_KICK_FAIL_PLAYINGGAME:
		case ResultCode.ERR_KICK_VIP_INVALID:
		case ResultCode.ERR_USE_ITEM_NULL:
		case ResultCode.ERR_USE_ITEM_REFUSE:
			{
				Context tContext = MyArStation.mGameMain.getApplicationContext();
				if((String)msg.obj != null) {
					Tools.showSimpleToast(tContext, Gravity.CENTER, (String)msg.obj, Toast.LENGTH_LONG);
					iChatView.updateSystemMessage(new ChatContent("[系统消息]", (String)msg.obj));
				}
				removeProgressDialog();
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspfLoginRoom:
			{
				iLoadingTick = 0;
				
				//
				MBRspLoginRoom stPlayerLoginRoomInfo = (MBRspLoginRoom)msg.obj;
				if(stPlayerLoginRoomInfo != null) {
					//
					iRoomPair = null;
					iRoomPair = new RoomData(stPlayerLoginRoomInfo.iRoomID, -1, RoomData.TYPE_CLASSICAL);
					//
					iRoomName = stPlayerLoginRoomInfo.iRoomName;
					//
					iWineName = stPlayerLoginRoomInfo.iWineName;
					iWinePrice = stPlayerLoginRoomInfo.iPrice;
					//test----
//					if(stPlayerLoginRoomInfo.iRoomID == 0) {
//						MediaManager.getMediaManagerInstance().playMusic(SoundsResID.bg1, true);
//					}
//					else if(stPlayerLoginRoomInfo.iRoomID == 1) {
//						MediaManager.getMediaManagerInstance().playMusic(SoundsResID.bg3, true);
//					}
//					else {
//						MediaManager.getMediaManagerInstance().playMusic(SoundsResID.bg4, true);
//					}
//					if(iYanHuaSpriteCount < 0) {
//						MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusic(SoundsResID.bg4, true);
//					}
					//test----
					//播放音乐
					int tMusicID = SoundsResID.bg4 + stPlayerLoginRoomInfo.iRoomID;
					if(tMusicID > SoundsResID.bg8) {
						tMusicID = SoundsResID.bg4;
					}
					//公海、皇家，特殊时间播放，特殊歌曲
//					if(stPlayerLoginRoomInfo.iRoomID == 3 || stPlayerLoginRoomInfo.iRoomID == 4) {
//						Calendar c = Calendar.getInstance();
//						int hour = c.get(Calendar.HOUR_OF_DAY);
//						if(hour >= 2 && hour <= 5) {
//							tMusicID = SoundsResID.bg9;
//						}
//					}
					System.out.println("tMusicID = " + tMusicID);
					//
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusic(tMusicID, true);
					//
					iCleanDrunkPrice = 200;
					iBetStr = stPlayerLoginRoomInfo.iRewardName;
					//游戏逻辑
					iLogicType = stPlayerLoginRoomInfo.iLogicType;
					//
					iActivityInfo = "您正在参加活动";
					//
					iSelfSeatID = stPlayerLoginRoomInfo.iSelfSeatID;
					//计算转换视觉后，玩家的座位ID
					Sprite[] pSprite = new Sprite[MAX_GAME_PLAYER];
					for(int i=0; i<MAX_GAME_PLAYER; i++) {
						pSprite[i] = iPlayerList[i].iSprite;
					}
					int tempSeatID = iSelfSeatID;
					for(int i=0; i<MAX_GAME_PLAYER; i++) {
						//
						iChangedSeatID[tempSeatID] = i;
						//
						iPlayerList[tempSeatID].iSprite = pSprite[i];
						//
						tempSeatID ++;
						if(tempSeatID >= MAX_GAME_PLAYER) {
							tempSeatID = 0;
						}
					}
					pSprite = null;
					//更新酒
					iPlayerList[iSelfSeatID].iCurrentWineCount = stPlayerLoginRoomInfo.iWineCount;
					//更新自己的金币
//					GameMain.iPlayer.iGb -= iPlayerList[iSelfSeatID].iCurrentWineCount * stPlayerLoginRoomInfo.iPrice;
					iPlayerCount = stPlayerLoginRoomInfo.iPlayerCount;
					for(int i = 0; i < iPlayerCount; i++) {
						int seatID = stPlayerLoginRoomInfo.iSeatIDs.get(i);
						if(seatID < 0 || seatID >= MAX_GAME_PLAYER) {
							continue;
						}
						iPlayerList[seatID].iUserID = stPlayerLoginRoomInfo.iUserIDs.get(i);
//						System.out.println("Ding LoginRoom seatID=" + seatID + " UserID=" + iPlayerList[seatID].iUserID);
						iPlayerList[seatID].iSeatID = seatID;
						if(seatID == iSelfSeatID) {
							iPlayerList[seatID].ibSelf = true;
							iPlayerList[seatID].iMotionID = CGamePlayer.MOTION_STATE_SITDOWNING;
							iPlayerList[seatID].iSprite.resetFrameID();
							MyArStation.iPlayer.iCurrentDrunk = stPlayerLoginRoomInfo.iCurrentDrunks.get(i);
						}
						else {
							iPlayerList[seatID].iMotionID = CGamePlayer.MOTION_STATE_SITDOWNED;
							iPlayerList[seatID].iSprite.resetFrameID();
						}
						iPlayerList[seatID].iCupMotionID = 0;
						iPlayerList[seatID].stUserNick = stPlayerLoginRoomInfo.iUserNicks.get(i);
						iPlayerList[seatID].iCurrentDrunk = stPlayerLoginRoomInfo.iCurrentDrunks.get(i);
						iPlayerList[seatID].iGameState = stPlayerLoginRoomInfo.iUserStates.get(i);
						iPlayerList[seatID].iModelID = stPlayerLoginRoomInfo.iModelIDs.get(i); 
						iPlayerList[seatID].stHeadPicName = stPlayerLoginRoomInfo.iLittlePic.get(i);
						iPlayerList[seatID].iDiceID = stPlayerLoginRoomInfo.iDicePakID.get(i) / 10;
						iPlayerList[seatID].iNextDiceCount = stPlayerLoginRoomInfo.iDicePakID.get(i) % 10;
						iPlayerList[seatID].ibCrazed = false;
						Bitmap tmpBmp = MyArStation.iHttpDownloadManager.getImage(iPlayerList[seatID].stHeadPicName);
						if(tmpBmp == null) {
							ResFile tRes = (ResFile)iUIPak.getObject(30, Project.FILE);
							tmpBmp = tRes.getImage();
						}
						float hPara = 1;
						if(tmpBmp != null) {
							hPara = (float)iPlayerList[seatID].iSprite.getFirstFrameHeight() / (float)tmpBmp.getHeight();
							int zoomW = (int)(tmpBmp.getWidth() * hPara);
							iPlayerList[seatID].iBmp = Graphics.zoomBitmap(tmpBmp, 
									zoomW, 
									iPlayerList[seatID].iSprite.getFirstFrameHeight());
							iDefualPhtopBitmap = Graphics.CreateTransBitmap(iPlayerList[seatID].iBmp.getWidth(), iPlayerList[seatID].iBmp.getHeight());
						}
						tmpBmp = null;
						iPlayerList[seatID].ibInit = true;
					}
					if(iGameState == GAME_STATE_LOADING) {
						//初始化计时器
						iOvalTimerCount = null;
						int tempR = iPlayerList[iSelfSeatID].iBmp.getHeight();
						int tempRr = tempR / 5;
						iOvalTimerCount = new OvalTimerCount(tempR, tempRr, this);
						//根据自己的图像宽高设置按钮的宽高
						SpriteButton pSB;
						for(int i=0; i<iPlayerButtonList.size(); i++) {
							pSB = ((SpriteButton)iPlayerButtonList.elementAt(i));
							Rect pLogicRect = pSB.getRect();
							pLogicRect.set(-(iPlayerList[0].iBmp.getWidth()>>1),
									-iPlayerList[0].iBmp.getHeight(),
									(iPlayerList[0].iBmp.getWidth()>>1),
									0);
						}
						//弹出按钮的半径
						RADIUS = tempR;
						//初始化弹出按钮的动画
						initAnimation();
						//载入背景图片
						MyArStation.iImageManager.loadRoomBack("new_back00");
					}
					else {
						//载入背景图片
						MyArStation.iImageManager.loadRoomBack("new_back00");
						//停止计时器动画
						iOvalTimerCount.pause();
						//清除屏幕的骰子
						createBMP = true;
						//
						iChopDiceCount = 0;
					}
					//将反劈的座位ID复位
					iRebelChopSeatID = -1;
					//
					iWantToShowRebleChopButton = false;
					//停止骰子动画
					iDiceMovieState = DICE_MOVIE_STATE_IDLE;
					//改变游戏状态
					iGameState = GAME_STATE_LOGINROOM_START;
				}
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_LOGINROOM_B:
			{
				iLoadingTick = 0;
				//
				MBRspLoginRoomB stPlayerLoginRoomInfo = (MBRspLoginRoomB)msg.obj;
				if(stPlayerLoginRoomInfo != null) {
					//
					iRoomPair = null;
					iRoomPair = new RoomData(stPlayerLoginRoomInfo.iRoomID, -1, RoomData.TYPE_CLASSICAL);
					//
					iRoomName = stPlayerLoginRoomInfo.iRoomName;
					//
					iWineName = stPlayerLoginRoomInfo.ImageName;
					iWinePrice = stPlayerLoginRoomInfo.iRunOffPrice;
					//播放音乐
					int tMusicID = SoundsResID.bg4 + stPlayerLoginRoomInfo.iRoomID;
					if(tMusicID > SoundsResID.bg8) {
						tMusicID = SoundsResID.bg4;
					}
					//公海、皇家，特殊时间播放，特殊歌曲
//					if(stPlayerLoginRoomInfo.iRoomID == 3 || stPlayerLoginRoomInfo.iRoomID == 4) {
//						Calendar c = Calendar.getInstance();
//						int hour = c.get(Calendar.HOUR_OF_DAY);
//						if(hour >= 2 && hour <= 5) {
//							tMusicID = SoundsResID.bg9;
//						}
//					}
					System.out.println("tMusicID = " + tMusicID);
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusic(tMusicID, true);
					//
					iCleanDrunkPrice = 200;
					iBetStr = stPlayerLoginRoomInfo.iRewardName;
					//游戏逻辑
					iLogicType = stPlayerLoginRoomInfo.iLogicType;
					//
					iActivityInfo = "您正在参加活动";
					//
					iSelfSeatID = stPlayerLoginRoomInfo.iSelfSeatID;
					//计算转换视觉后，玩家的座位ID
//					Sprite[] pSprite = new Sprite[MAX_GAME_PLAYER];
					for(int i=0; i<MAX_GAME_PLAYER; i++) {
						iPlayerList[i].iSprite.resetFrameID();
					}
					int tempSeatID = iSelfSeatID;
					for(int i=0; i<MAX_GAME_PLAYER; i++) {
						//
						iChangedSeatID[tempSeatID] = i;
						//
//						iPlayerList[tempSeatID].iSprite = pSprite[i];
						//
						tempSeatID ++;
						if(tempSeatID >= MAX_GAME_PLAYER) {
							tempSeatID = 0;
						}
					}
//					pSprite = null;
					//更新酒
					iPlayerList[iSelfSeatID].iCurrentWineCount = stPlayerLoginRoomInfo.iWineCount;
					//更新自己的金币
//					GameMain.iPlayer.iGb -= iPlayerList[iSelfSeatID].iCurrentWineCount * stPlayerLoginRoomInfo.iPrice;
					iPlayerCount = stPlayerLoginRoomInfo.iPlayerCount;
					for(int i = 0; i < iPlayerCount; i++) {
						int seatID = stPlayerLoginRoomInfo.iSeatIDs.get(i);
						if(seatID < 0 || seatID >= MAX_GAME_PLAYER) {
							continue;
						}
						iPlayerList[seatID].iUserID = stPlayerLoginRoomInfo.iUserIDs.get(i);
//						System.out.println("Ding LoginRoom seatID=" + seatID + " UserID=" + iPlayerList[seatID].iUserID);
						iPlayerList[seatID].iSeatID = seatID;
						if(seatID == iSelfSeatID) {
							iPlayerList[seatID].ibSelf = true;
							iPlayerList[seatID].iMotionID = CGamePlayer.MOTION_STATE_SITDOWNING;
							iPlayerList[seatID].iSprite.resetFrameID();
							MyArStation.iPlayer.iCurrentDrunk = stPlayerLoginRoomInfo.iCurrentDrunks.get(i);
						}
						else {
							iPlayerList[seatID].iMotionID = CGamePlayer.MOTION_STATE_SITDOWNED;
							iPlayerList[seatID].iSprite.resetFrameID();
						}
						iPlayerList[seatID].iCupMotionID = 0;
						iPlayerList[seatID].stUserNick = stPlayerLoginRoomInfo.iUserNicks.get(i);
						iPlayerList[seatID].iCurrentDrunk = stPlayerLoginRoomInfo.iCurrentDrunks.get(i);
						iPlayerList[seatID].iGameState = stPlayerLoginRoomInfo.iUserStates.get(i);
						iPlayerList[seatID].iModelID = stPlayerLoginRoomInfo.iModelIDs.get(i); 
						iPlayerList[seatID].stHeadPicName = stPlayerLoginRoomInfo.iLittlePic.get(i);
						iPlayerList[seatID].iDiceID = stPlayerLoginRoomInfo.iDicePakID.get(i) / 10;
						iPlayerList[seatID].iNextDiceCount = stPlayerLoginRoomInfo.iDicePakID.get(i) % 10;
						iPlayerList[seatID].iVipLevel = stPlayerLoginRoomInfo.iVIPLevels.get(i);
						iPlayerList[seatID].ibCrazed = false;
						Bitmap tmpBmp = MyArStation.iHttpDownloadManager.getImage(iPlayerList[seatID].stHeadPicName);
						if(tmpBmp == null) {
							ResFile tRes = (ResFile)iUIPak.getObject(30, Project.FILE);
							tmpBmp = tRes.getImage();
						}
						float hPara = 1;
						if(tmpBmp != null) {
							hPara = (float)iPlayerList[seatID].iSprite.getFirstFrameHeight() / (float)tmpBmp.getHeight();
							int zoomW = (int)(tmpBmp.getWidth() * hPara);
							iPlayerList[seatID].iBmp = Graphics.zoomBitmap(tmpBmp, 
									zoomW, 
									iPlayerList[seatID].iSprite.getFirstFrameHeight());
							iDefualPhtopBitmap = Graphics.CreateTransBitmap(iPlayerList[seatID].iBmp.getWidth(), iPlayerList[seatID].iBmp.getHeight());
							//获取车的图片
							iPlayerList[seatID].iCarBmp = null;//iPlayerList[seatID].iBmp;
							int tCartID = stPlayerLoginRoomInfo.iCarPakIDs.get(i);
							if(tCartID > 0 && seatID == iSelfSeatID) {
								tmpBmp = MyArStation.iHttpDownloadManager.getGoodsIcon(tCartID);
								hPara = (float)iPlayerList[seatID].iSprite.getFirstFrameHeight() / (float)tmpBmp.getHeight();
								zoomW = (int)(tmpBmp.getWidth() * hPara * 1.5);
								int zoomH = (int)(tmpBmp.getHeight() * hPara * 1.5);
								iPlayerList[seatID].iCarBmp = Graphics.zoomBitmap(tmpBmp, 
									zoomW, 
									zoomH);
							}
						}
						tmpBmp = null;
						iPlayerList[seatID].ibInit = true;
					}
					if(iGameState == GAME_STATE_LOADING) {
						//初始化计时器
						iOvalTimerCount = null;
						int tempR = iPlayerList[iSelfSeatID].iBmp.getHeight();
						int tempRr = tempR / 5;
						iOvalTimerCount = new OvalTimerCount(tempR, tempRr, this);
						//根据自己的图像宽高设置按钮的宽高
						SpriteButton pSB;
						for(int i=0; i<iPlayerButtonList.size(); i++) {
							pSB = ((SpriteButton)iPlayerButtonList.elementAt(i));
							Rect pLogicRect = pSB.getRect();
							pLogicRect.set(-(iPlayerList[0].iBmp.getWidth()>>1),
									-iPlayerList[0].iBmp.getHeight(),
									(iPlayerList[0].iBmp.getWidth()>>1),
									0);
						}
						//弹出按钮的半径
						RADIUS = tempR;
						//初始化弹出按钮的动画
						initAnimation();
						//载入背景图片
						MyArStation.iImageManager.loadRoomBack(stPlayerLoginRoomInfo.ImageName);
					}
					else {
						//载入背景图片
						MyArStation.iImageManager.loadRoomBack(stPlayerLoginRoomInfo.ImageName);
						//停止计时器动画
						iOvalTimerCount.pause();
						//清除屏幕的骰子
						createBMP = true;
						//
						iChopDiceCount = 0;
					}
					//将反劈的座位ID复位
					iRebelChopSeatID = -1;
					//
					iWantToShowRebleChopButton = false;
					//停止骰子动画
					iDiceMovieState = DICE_MOVIE_STATE_IDLE;
					//改变游戏状态
					iGameState = GAME_STATE_LOGINROOM_START;
					//
					resetScreenOffset();
				}			
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspLeaveRoom:
			{
//				iLoadingTick = 0;
				MBRspLeaveRoom stPlayerLeaveRoomInfo = (MBRspLeaveRoom)MyArStation.iGameProtocol.getMobileMsgCS(RspMsg.MSG_RSP_LEAVEROOM);
				if(stPlayerLeaveRoomInfo.iMsg != null && stPlayerLeaveRoomInfo.iMsg.length() > 0) {
					Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), stPlayerLeaveRoomInfo.iMsg);
				}
				if(stPlayerLeaveRoomInfo != null) {
					MyArStation.iPlayer.iGb = stPlayerLeaveRoomInfo.iGB;
				}
				iBarDiceSet = false;
				iBarDiceY = 0;
				iHideBarDice = false;
				iChangeDice = 0;	//改变后的骰子
				iBarExpY = 0;	//经验条的位置
				//动态动画
//				for(int i=0; i<iAnimEndMotionList.size(); i++) {
//					iAnimEndMotionList.remove(i);
//				}
//				for(int i=0; i<iAnimSpriteList.size(); i++) {
//					Sprite pSprite = iAnimSpriteList.get(i);
//					pSprite = null;
//					iAnimSpriteList.remove(i);
//				}
				if(iSynAnimation != null) iSynAnimation.remove();
				
				for(int i=0; i<iItemSpriteList.size(); i++) {
					Sprite pSprite = iItemSpriteList.get(i);
					pSprite = null;
					iItemSpriteList.remove(i);
				}
				if(iGameState == GAME_STATE_JOIN_ACTIVITY
						|| iGameState == GAME_STATE_JOINACTIVITY_LOADING) {
					iRoomPair = null;
					iRoomPair = new RoomData(iActivityConcours.iActivityRoomID, -1, iActivityConcours.iRoomType);
					//
					for(int i=0; i<MAX_GAME_PLAYER; i++) {
						iPlayerList[i].ibInit = false;
						iPlayerList[i].ibFaceEnd = true;
						iPlayerList[i].iChatTick = 0;
						iPlayerList[i].ibCrazed = false;
					}
					iChatView.clearHistoryWords();
					//
//					requestLoginRoom(iRoomPair);
					messageEvent(MessageEventID.EMESSAGE_EVENT_TO_PROGRESS, iRoomPair);
				}
				//换桌子
				else if(iGameState == GAME_STATE_CHANGE_DESK
						|| iGameState == GAME_STATE_CHANGEDESK_LOADING) {
					//
					for(int i=0; i<MAX_GAME_PLAYER; i++) {
						iPlayerList[i].ibInit = false;
						iPlayerList[i].ibFaceEnd = true;
						iPlayerList[i].iChatTick = 0;
					}
					iChatView.clearHistoryWords();
					//
					requestLoginRoom(iRoomPair);
					iChatButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
					iFaceButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
					if(MyArStation.iPlayer.iMissionRewardCount > 0) {
						iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
					}
					else {
						iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
					}
				}
				//换房
				else if(iGameState == GAME_STATE_CHANGE_ROOM) {
					//删除系统控件
					MyArStation.mGameMain.mainLayout.removeAllViews();
					//切换到主界面
//					iGameCanvas.changeView(new UniteGame(iRoomPair));
				}
				else {
					//删除系统控件
					MyArStation.mGameMain.mainLayout.removeAllViews();
					//切换到主界面
					iGameCanvas.changeView(new HomeScreen());
				}
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_LOGINROOM:
			{
				MBNotifyLoginRoom pNotifyLoginRoom = (MBNotifyLoginRoom)msg.obj;
				int seatID = pNotifyLoginRoom.iSeatID;
				iPlayerList[seatID].iSeatID = seatID;
				iPlayerList[seatID].iUserID = pNotifyLoginRoom.iUserID;
				iPlayerList[seatID].stUserNick = pNotifyLoginRoom.iUserName;
				iPlayerList[seatID].iCurrentDrunk = pNotifyLoginRoom.iCurrentDrunk;
				iPlayerList[seatID].iGameState = pNotifyLoginRoom.iUserState;
				iPlayerList[seatID].iModelID = pNotifyLoginRoom.iModelID;
				iPlayerList[seatID].stHeadPicName = pNotifyLoginRoom.iLittlePic;
				iPlayerList[seatID].iDiceID = pNotifyLoginRoom.iDicePakID / 10;
				iPlayerList[seatID].iNextDiceCount = pNotifyLoginRoom.iDicePakID % 10;
				iPlayerList[seatID].iCupMotionID = 0;
				iPlayerList[seatID].iMotionID = CGamePlayer.MOTION_STATE_SITDOWNING;
				iPlayerList[seatID].iSprite.resetFrameID();
				iPlayerList[seatID].iCupSprite.resetFrameID();
				iPlayerList[seatID].ibCrazed = false;
				iPlayerList[seatID].ibInit = true;
				Bitmap tmpBmp = MyArStation.iHttpDownloadManager.getImage(iPlayerList[seatID].stHeadPicName);
				if(tmpBmp == null) {
					ResFile tRes = (ResFile)iUIPak.getObject(30, Project.FILE);
					tmpBmp = tRes.getImage();
				}
				if(tmpBmp != null) {
					float hPara = (float)iPlayerList[seatID].iSprite.getFirstFrameHeight() / (float)tmpBmp.getHeight();
					int zoomW = (int)(tmpBmp.getWidth() * hPara);
					iPlayerList[seatID].iBmp = Graphics.zoomBitmap(tmpBmp, 
						zoomW, 
						iPlayerList[seatID].iSprite.getFirstFrameHeight());
				}
				tmpBmp = null;
				//
				System.out.println("testCarID  111111111111");
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_LOGINROOM_B:
			{
				MBNotifyLoginRoomB pNotifyLoginRoom = (MBNotifyLoginRoomB)msg.obj;
				int seatID = pNotifyLoginRoom.iSeatID;
//				System.out.println("testTrace seatID = " + seatID + " iChangedSeatID[seatID] = " + iChangedSeatID[seatID]);
				iPlayerList[seatID].iSeatID = seatID;
				iPlayerList[seatID].iUserID = pNotifyLoginRoom.iUserID;
				iPlayerList[seatID].stUserNick = pNotifyLoginRoom.iUserName;
				iPlayerList[seatID].iCurrentDrunk = pNotifyLoginRoom.iCurrentDrunk;
				iPlayerList[seatID].iGameState = pNotifyLoginRoom.iUserState;
				iPlayerList[seatID].iModelID = pNotifyLoginRoom.iModelID;
				iPlayerList[seatID].stHeadPicName = pNotifyLoginRoom.iLittlePic;
				iPlayerList[seatID].iDiceID = pNotifyLoginRoom.iDicePakID / 10;
				iPlayerList[seatID].iNextDiceCount = pNotifyLoginRoom.iDicePakID % 10;
				iPlayerList[seatID].iCupMotionID = 0;
				iPlayerList[seatID].iMotionID = CGamePlayer.MOTION_STATE_SITDOWNING;
				iPlayerList[iChangedSeatID[seatID]].iSprite.resetFrameID();
				iPlayerList[seatID].iCupSprite.resetFrameID();
				iPlayerList[seatID].iVipLevel = pNotifyLoginRoom.iVIPLevel;
				iPlayerList[seatID].ibCrazed = false;
				iPlayerList[seatID].ibInit = true;
				Tools.debug("CarID = " + pNotifyLoginRoom.iCarPakID);
				Bitmap tmpBmp = MyArStation.iHttpDownloadManager.getImage(iPlayerList[seatID].stHeadPicName);
				if(tmpBmp == null) {
					ResFile tRes = (ResFile)iUIPak.getObject(30, Project.FILE);
					tmpBmp = tRes.getImage();
				}
				if(tmpBmp != null) {
					float hPara = (float)iPlayerList[seatID].iSprite.getFirstFrameHeight() / (float)tmpBmp.getHeight();
					int zoomW = (int)(tmpBmp.getWidth() * hPara);
					iPlayerList[seatID].iBmp = Graphics.zoomBitmap(tmpBmp, 
						zoomW, 
						iPlayerList[seatID].iSprite.getFirstFrameHeight());
					//获取车的图片
					iPlayerList[seatID].iCarBmp = null;//iPlayerList[seatID].iBmp;
					Tools.debug("CarID = " + pNotifyLoginRoom.iCarPakID);
					if(pNotifyLoginRoom.iCarPakID > 0) {
						tmpBmp = MyArStation.iHttpDownloadManager.getGoodsIcon(pNotifyLoginRoom.iCarPakID);
						hPara = (float)iPlayerList[seatID].iSprite.getFirstFrameHeight() / (float)tmpBmp.getHeight();
						zoomW = (int)(tmpBmp.getWidth() * hPara * 1.5);
						int zoomH = (int)(tmpBmp.getHeight() * hPara * 1.5);
						iPlayerList[seatID].iCarBmp = Graphics.zoomBitmap(tmpBmp, 
							zoomW, 
							zoomH);
					}
				}
				tmpBmp = null;			
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_LeaveRoom:
			{
				MBNotifyLeaveRoom notifyLeaveRoom = (MBNotifyLeaveRoom)msg.obj;
				iPlayerList[notifyLeaveRoom.iSeatID].ibInit = false;
				iPlayerList[notifyLeaveRoom.iSeatID].iAddGb = 0;
				iPlayerList[notifyLeaveRoom.iSeatID].iAddExp = 0;
				iPlayerList[notifyLeaveRoom.iSeatID].iAddFriendExp = 0;
				iPlayerList[notifyLeaveRoom.iSeatID].iAddVipExp = 0;
				iPlayerList[notifyLeaveRoom.iSeatID].iBmp = iDefualPhtopBitmap;
				iPlayerList[notifyLeaveRoom.iSeatID].iCarBmp = null;
				iPlayerList[notifyLeaveRoom.iSeatID].ibFaceEnd = true;
				iPlayerList[notifyLeaveRoom.iSeatID].ibCrazed = false;
//				iPlayerList[iChangedSeatID[notifyLeaveRoom.iSeatID]].iSprite.resetFrameID();
				//如果选中了此玩家
				if(iSelectSeatID == notifyLeaveRoom.iSeatID
						&& iIconButtonShow == true) {
					iSelectSeatID = 0;
					//隐藏图标按钮
					messageEvent(GameEventID.ESPRITEBUTTON_EVENT_HIDE_SYSTEM_BUTTON);
				}
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_SHOW_READY:
			{
				if(iRelDelay >= MAX_DELAYCOUNT) {//超时踢出去
					iGameState = GAME_STATE_LEAVEROOM;
					Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), "您连续挂机超过三次!");
					requestLeaveRoom();
				}
				//复位上次喊的骰子
				iLastShoutCount = 0;
				iLastShoutDice = 0;
				iSelectShoutOne = 0;
				ibShoutOne = false;
				iLastShoutSeatID = 0;
				iCurrentShoutSeatID = 0;
				iChopSeatID = 0;
				iWantToShowRebleChopButton = false;
				iRebelChopSeatID = -1;
				iChopDice = 0;
				iChopDiceCount = 0;
				iShowingResultSeatID = 0;
				iWinSeatID = 0;		//胜利者的座位ID
				iFailSeatID = 0;	//失败者的座位ID
				iDoubleTimes = 1;
				//
				if(iShakeListener != null) {
					iShakeListener.stopShake();
				}
				//
				for(int i=0; i<MAX_GAME_PLAYER; i++) {
					if(iPlayerList[i].ibInit == false) {
						continue;
					}
					iPlayerList[i].iCupMotionID = 0;
					iPlayerList[i].iGameState = CGamePlayer.PLAYER_STATE_WAIT;
					iPlayerList[i].iPlayingState = CGamePlayer.PLAYING_STATE_WAIT;
					iPlayerList[i].iLastShoutCount = 0;
					iPlayerList[i].iLastShoutDice = 0;
					iPlayerList[i].ibShoutOne = false;
				}
				//停止计时器动画
				iOvalTimerCount.pause();
				//
				MBNotifyShowReady notifyShowReady = (MBNotifyShowReady)msg.obj;
				iGameTick = 100;//5秒钟
//				if(iDiceMovieState == DICE_MOVIE_STATE_IDLE) {
				iPlayerList[iSelfSeatID].iCupMotionID = 1;
				iBigCupSprite.resetFrameID();
				if(iShakeListener != null) {
					iShakeListener.startShake();
				}
//				}
				iServerState = E_TGS_WAIT_START;
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_READY:
			{
				MBNotifyReady notifyReady = (MBNotifyReady)msg.obj;
				int seatID = notifyReady.iSeatID;
				if(seatID >= 0 && seatID < MAX_GAME_PLAYER) {
					//将Player的状态置为已经准备
					iPlayerList[seatID].iGameState = CGamePlayer.PLAYER_STATE_READY;
					//Player骰盅的动画改成摇
					iPlayerList[seatID].iCupMotionID = 3;
				}
			}
			break;	
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_START:
			{
				//
//				System.out.println("StartNotify 00000000000000000");
				//
				MBNotifyStart nitNotifyStart = (MBNotifyStart)msg.obj;
				iPlayingCount = nitNotifyStart.iPlayerCount;
				//停止计时器动画
				iOvalTimerCount.pause();
				//停止骰子动画
				iDiceMovieState = DICE_MOVIE_STATE_IDLE;
				//把骰盅的动画停掉
				for(int i=0; i<MAX_GAME_PLAYER; i++) {
					if(iPlayerList[i].ibInit == false) {
						continue;
					}
					iPlayerList[i].iCupMotionID = 0;
					iPlayerList[i].iCupSprite.resetFrameID();
					//改变玩家的状态
					if(iPlayerList[i].iGameState == CGamePlayer.PLAYER_STATE_READY) {
						iPlayerList[i].iGameState = CGamePlayer.PLAYER_STATE_PLAYING;
						iPlayerList[i].iPlayingState = CGamePlayer.PLAYER_STATE_WAIT;
						iPlayerList[i].iLastShoutCount = 0;
						iPlayerList[i].iLastShoutDice = 0;
						iPlayerList[i].ibShoutOne = false;
					}
				}
				//赋值自己的骰子
				for(int k=0; k<CGamePlayer.MAX_DICE_COUNT; k++) {
					iPlayerList[iSelfSeatID].iDices[k] = nitNotifyStart.iDice.get(k);
				}
				//
				iCurrentShoutSeatID = nitNotifyStart.iShouterSeatID;
				//改变玩家的状态
				iPlayerList[nitNotifyStart.iShouterSeatID].iGameState = CGamePlayer.PLAYER_STATE_PLAYING;
				iPlayerList[nitNotifyStart.iShouterSeatID].iPlayingState = CGamePlayer.PLAYING_STATE_SHOUTING;
				//
				iBarDiceSet = false;
				//初始化骰子坐标
				iBarDiceY = ActivityUtil.SCREEN_HEIGHT + iPlayerList[iSelfSeatID].iBmp.getHeight();
				//创建滚轮
				iWheelVisible = false;
				iWheelForceChop = false;
				iWheelPressTick = 0;	//按下滚轮的时间
				iWheelRunPart = 0;		//滚动部分
				iWheelRunDir = 0;		//滚动方向
				iWheelRunTick = 0;		//滚动时间
				iWheelCurrentNum = 0;	
				iWheelCurrentDice = 0;
				iWheelMinNum = 0;
				iWheelMaxNum = 0;
				iPressedY = 0;
				iTuchPart = 0;
				iLeftOffset = 0;
				iLastLeftOffset = 0;
				iRightOffset = 0;
				iLastRightOffset = 0;
				if(iCurrentShoutSeatID == iSelfSeatID) {
					iWheelVisible = true;
					//只显示喊骰按钮
					((SpriteButton)iSpriteButtonList.elementAt(0)).setVision(true);
					//显示喊斋按钮
					if(ibShoutOne == false && MyArStation.iPlayer.iZhaiSkill >= 1) {
						iZhaiButton.setVision(true);
						iZhaiButton.setSelected(true);
					}
					//显示雷击按钮
					//if(GameMain.iPlayer.iZhaiSkill >= 2) {
					//	iLightningButton.setVision(true);
					//}
				}
				else {
					iWheelVisible = false;
				}
				iWheelForceChop = false;
				iWheelCurrentNum = iPlayingCount;
				iWheelCurrentDice = 2;
				iWheelMinNum = iPlayingCount;
				iWheelMaxNum = 6 * iPlayingCount;
				//抢劈
				iPreChopButton.setVision(false);	
				//反劈
				iRebelChopButton.setVision(false);
				//将后台状态置为游戏中
				iServerState = E_TGS_PLAYING;
				//更新菜单
				iGameMenuDialog.updateMenuState(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING ? true : false);
				if(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING) {
					quickSelectRoomScreen.dimiss();
					//改变菜单
					iMenuButton.setVision(false);
				}
				//经验条解锁
				ibLockExp = false;
//				System.out.println("StartNotify 1111111111111111111111 iWheelMaxNum=" + iWheelMaxNum);
//				if(iPlayingCount >= PLAY_ROCK_PLAYER_COUNT) {
//					MediaManager.getMediaManagerInstance().playMusic(SoundsResID.bg2, true);
//				}
			}
			break;	
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyShout:
			{
				//将后台状态置为游戏中
				iServerState = E_TGS_PLAYING;
				//
				MBNotifyShout notifyShout = (MBNotifyShout)msg.obj;
				//
				int seatID = notifyShout.iShouterSeatID;
				int nextSeatID = notifyShout.iNextSeatID;
				iLastShoutSeatID = seatID;
				iCurrentShoutSeatID = nextSeatID;
				//播放计时器动画
				iOvalTimerCount.start(iCurrentShoutSeatID == iSelfSeatID ? true : false, iDelayCount);
				//
				if(notifyShout.iCount >= 11) {
					CallPointManager.getSoundManager().CallGaoDaTa(iPlayerList[seatID].iModelID);
				}
				else {
					int tIndex = (int)(Math.random() * 100);
					if(tIndex >= 70 && notifyShout.iCount >= iPlayingCount+iPlayingCount) {
						CallPointManager.getSoundManager().CallGaoDaTa(iPlayerList[seatID].iModelID);
					}
					else {
						CallPointManager.getSoundManager().CallPoint(iPlayerList[seatID].iModelID, notifyShout.iCount, notifyShout.iDice);
					}
				}
				//
				iPlayerList[seatID].iPlayingState = CGamePlayer.PLAYING_STATE_SHOUTED;
				iPlayerList[seatID].iLastShoutCount = iLastShoutCount = notifyShout.iCount;
				iPlayerList[seatID].iLastShoutDice = iLastShoutDice = notifyShout.iDice;
				if(notifyShout.iDice == 1 || notifyShout.nShoutOne > 0) {
					if(ibShoutOne == false) {
						iSelectShoutOne = 1;
						ibShoutOne = true;
						iDoubleTimes <<= 1;
						iFlashBetTick = 16;
						//
						iDoubleSpriteEnd = false;
						iDoubleSprite.resetFrameID();
					}
				}
				if(ibShoutOne == true) {
					iPlayerList[seatID].ibShoutOne = true;
				}
				//
				iPlayerList[nextSeatID].iGameState = CGamePlayer.PLAYER_STATE_PLAYING;
				iPlayerList[nextSeatID].iPlayingState = CGamePlayer.PLAYING_STATE_SHOUTING;
				//滚轮
				if(iCurrentShoutSeatID == iSelfSeatID) {
					iWheelVisible = true;
					//喊 劈 按钮可见
					for(int i=0; i<iSpriteButtonList.size(); i++) {
						((SpriteButton)iSpriteButtonList.elementAt(i)).setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_SHOW);
						((SpriteButton)iSpriteButtonList.elementAt(i)).setVision(true);
					}
					//喊斋按钮可见
					if(ibShoutOne == false && MyArStation.iPlayer.iZhaiSkill >= 1) {
						iZhaiButton.setVision(true);
						iZhaiButton.setSelected(true);
					}
					//
					//Tools.debug("iLightningButton iZhaiSkill = " + GameMain.iPlayer.iZhaiSkill);
					//显示雷击按钮
					if(MyArStation.iPlayer.iZhaiSkill >= 2) {
						iLightningButton.setVision(true);
						iLightningButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_SHOW);
						//Tools.debug("iLightningButton setVision true!");
					}
					//抢劈
					iPreChopButton.setVision(false);
					//反劈
					iRebelChopButton.setVision(false);
					//菜单按钮
					//iMenuButton.setVision(false);
					//聊天按钮
					iChatButton.setVision(false);
					//表情按钮
					iFaceButton.setVision(false);
					//任务按钮
					iGiftBUtton.setVision(false);
					//
//					if(iYanHuaSpriteCount < 0) {
						iMusicPos = MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).getMusicCurPos();
						if(iPlayingCount >= PLAY_ROCK_PLAYER_COUNT
								&& iLastShoutCount >= PLAY_ROCK_DICE_COUNT) //(ibShoutOne == true || 
						{
							MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusic(SoundsResID.bg2, true);
						}
						else {
							MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusic(SoundsResID.bg3, true);
						}
//					}
				}
				else {
					iWheelVisible = false;
					//喊 劈 按钮不可见
					for(int i=0; i<iSpriteButtonList.size(); i++) {
						((SpriteButton)iSpriteButtonList.elementAt(i)).setVision(false);
					}
					//喊斋按钮不可见
					iZhaiButton.setVision(false);
					//雷击按钮不可见
					iLightningButton.setVision(false);
					//抢劈
					if(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING
							&& iLastShoutSeatID != iSelfSeatID) {
						if(iPreChopButton.isVision() == false) {
							iPreChopButton.setVision(true);
							iPreChopButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_SHOW);
//							System.out.println("PreChopButton 0000000000000");
						}
					}
					//反劈
					iRebelChopButton.setVision(false);
					//菜单按钮
					//iMenuButton.setVision(false);
					//聊天按钮
					iChatButton.setVision(true);
					//表情按钮
					iFaceButton.setVision(true);
					iGiftBUtton.setVision(true);
					if(MyArStation.iPlayer.iMissionRewardCount > 0) {
						iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
					}
					else {
						iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
					}
					//
//					if(iYanHuaSpriteCount < 0) {
						if(iPlayingCount >= PLAY_ROCK_PLAYER_COUNT 
								&& iLastShoutCount >= PLAY_ROCK_DICE_COUNT) //(ibShoutOne == true || 
						{
							MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusic(SoundsResID.bg2, true);
						}
						else {
							int tMusicID = SoundsResID.bg4 + iRoomPair.roomID;
							if(tMusicID > SoundsResID.bg8) {
								tMusicID = SoundsResID.bg4;
							}
							//公海、皇家，特殊时间播放，特殊歌曲
//							if(iRoomPair.roomID == 3 || iRoomPair.roomID == 4) {
//								Calendar c = Calendar.getInstance();
//								int hour = c.get(Calendar.HOUR_OF_DAY);
//								if(hour >= 2 && hour <= 5) {
//									tMusicID = SoundsResID.bg9;
//								}
//							}
							System.out.println("tMusicID = " + tMusicID);
							if(iLastShoutSeatID == iSelfSeatID) {
								MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusicSeekTo(tMusicID, true, iMusicPos);
							}
							else {
								MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusic(tMusicID, true);
							}
						}
//					}
				}
				iWheelCurrentNum = iLastShoutCount + 1;
				iWheelCurrentDice = iLastShoutDice;
				if(iWheelCurrentNum > iWheelMaxNum) {
					//
					iWheelCurrentNum = iWheelMaxNum;
					if(iLastShoutDice == 1) {
						//只显示劈
						iWheelForceChop = true; 
						((SpriteButton)iSpriteButtonList.elementAt(0)).setVision(false);
					}
					else {
						iWheelCurrentDice = iLastShoutDice + 1;
					}
					if(iWheelCurrentDice > 6) {
						iWheelCurrentDice = 1;
					}
				}
				iLeftOffset = (int)(-(iWheelCurrentNum - iWheelMinNum) * iLeftDist); 
				if(iWheelCurrentDice == 1) {
					iRightOffset = (int)(-5 * iLeftDist); 
				}
				else {
					iRightOffset = (int)(-(iWheelCurrentDice - 2) * iLeftDist); 
				}
				//如果自己不在游戏中，把骰盅的动画停掉
				if(iPlayerList[iSelfSeatID].iGameState != CGamePlayer.PLAYER_STATE_PLAYING) {
					for(int i=0; i<MAX_GAME_PLAYER; i++) {
						if(iPlayerList[i].ibInit == false) {
							continue;
						}
						iPlayerList[i].iCupMotionID = 0;
						iPlayerList[i].iCupSprite.resetFrameID();
					}
				}
			}
			break;	
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyChop:
			{
//				System.out.println("StartNotify 44444444444444444444444");
				//将后台状态置为游戏中
				iServerState = E_TGS_PLAYING;
				//停止计时器动画
				iOvalTimerCount.pause();
				//
				MBNotifyChop notifyChop = (MBNotifyChop)msg.obj;
				iPlayerList[notifyChop.iChopSeatID].iPlayingState = CGamePlayer.PLAYING_STATE_CHOPING;
				//
				iChopSeatID = notifyChop.iChopSeatID;
				//反劈
				if(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING
						&& iChopSeatID != iSelfSeatID) {
					if(iLastShoutSeatID == iSelfSeatID) {
					//if(iRebelChopButton.isVision() == false) {
						iRebelChopButton.setVision(true);
						iRebelChopButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_SHOW);
						iRebelChopSeatID = -1;
						iWantToShowRebleChopButton = false;
					}
					else {
						iWantToShowRebleChopButton = true;
						iShowRebelChopButtonDelay = System.currentTimeMillis();
						iRebelChopSeatID = -1;
					}
				}
				//抢劈
				iPreChopButton.setVision(false);
				//滚轮
				iWheelVisible = false;
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).setVision(false);
				}
				//喊斋按钮
				iZhaiButton.setVision(false);
				//雷击按钮
				iLightningButton.setVision(false);
				//菜单按钮
				//iMenuButton.setVision(false);
				//聊天按钮
				iChatButton.setVision(true);
				//表情按钮
				iFaceButton.setVision(true);
				//任务按钮
				iGiftBUtton.setVision(true);
				if(MyArStation.iPlayer.iMissionRewardCount > 0) {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				}
				else {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				}
				//
//				System.out.println("StartNotify 5555555555555555555555555");
				//
//				if(iYanHuaSpriteCount < 0) {
				if(iPlayingCount >= PLAY_ROCK_PLAYER_COUNT 
						&& iLastShoutCount >= PLAY_ROCK_DICE_COUNT) {
						MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusic(SoundsResID.bg2, true);
					}
					else {
						int tMusicID = SoundsResID.bg4 + iRoomPair.roomID;
						if(tMusicID > SoundsResID.bg8) {
							tMusicID = SoundsResID.bg4;
						}
						//公海、皇家，特殊时间播放，特殊歌曲
//						if(iRoomPair.roomID == 3 || iRoomPair.roomID == 4) {
//							Calendar c = Calendar.getInstance();
//							int hour = c.get(Calendar.HOUR_OF_DAY);
//							if(hour >= 2 && hour <= 5) {
//								tMusicID = SoundsResID.bg9;
//							}
//						}
						System.out.println("tMusicID = " + tMusicID);
						MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusicSeekTo(tMusicID, true, iMusicPos);
					}
//				}
				//劈
				MediaManager.getMediaManagerInstance(iPlayerList[iChopSeatID].iModelID).playerSound(SoundsResID.pi, 0);
			}
			break;	

		case MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyResult:
			{
				//复位被劈骰子的个数
				iChopDiceCount = 0;
//				System.out.println("StartNotify 66666666666666666666666666666666");
				//聊天按钮
				iChatButton.setVision(true);
				//表情按钮
				iFaceButton.setVision(true);
				//任务按钮
				iGiftBUtton.setVision(true);
				if(MyArStation.iPlayer.iMissionRewardCount > 0) {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				}
				else {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				}
				//反劈
				iRebelChopButton.setVision(false);
				//
				MBNotifyResult iNotifyResult = (MBNotifyResult)msg.obj;
				if(iNotifyResult.iMsg.length() > 0) {
					//提示
					Context tContext = MyArStation.mGameMain.getApplicationContext();
					Tools.showSimpleToast(tContext, Gravity.CENTER, iNotifyResult.iMsg, Toast.LENGTH_LONG);
					//
					iChatView.updateSystemMessage(new ChatContent("[系统消息]", iNotifyResult.iMsg));
				}
				//有人逃跑或者人数不够
				if(iNotifyResult.nResult == ResultCode.ERR_RESULT_PLAYER_RUNOFF) {
					//复位后台状态
					iServerState = E_TGS_WAIT_PLAYER;
					//复位上次喊的骰子
					iLastShoutCount = 0;
					iLastShoutDice = 0;
					iLastShoutSeatID = 0;
					iCurrentShoutSeatID = 0;
					//
					iWheelVisible = false;
					iWheelForceChop = false;
					//抢劈
					iPreChopButton.setVision(false);
					//反劈
					iRebelChopButton.setVision(false);
//					System.out.println("PreChopButton 33333333333333333" + "iGameState " + iGameState);
					//
					iGameTick = 0;
					for(int i=0; i<MAX_GAME_PLAYER; i++) {
						if(iPlayerList[i].ibInit == false) {
							continue;
						}
						iPlayerList[i].iCupMotionID = 0;
						iPlayerList[i].iGameState = CGamePlayer.PLAYER_STATE_WAIT;
						iPlayerList[i].iPlayingState = CGamePlayer.PLAYING_STATE_WAIT;
						iPlayerList[i].iLastShoutCount = 0;
						iPlayerList[i].iLastShoutDice = 0;
						iPlayerList[i].ibCrazed = false;
					}
					//停止计时器动画
					iOvalTimerCount.pause();
					//停止骰子动画
					iDiceMovieState = DICE_MOVIE_STATE_IDLE;
					//复位背景
					createBMP = true;
					//
//					if(iYanHuaSpriteCount < 0) {
					int tMusicID = SoundsResID.bg4 + iRoomPair.roomID;
					if(tMusicID > SoundsResID.bg8) {
						tMusicID = SoundsResID.bg4;
					}
					//公海、皇家，特殊时间播放，特殊歌曲
//					if(iRoomPair.roomID == 3 || iRoomPair.roomID == 4) {
//						Calendar c = Calendar.getInstance();
//						int hour = c.get(Calendar.HOUR_OF_DAY);
//						if(hour >= 2 && hour <= 5) {
//							tMusicID = SoundsResID.bg9;
//						}
//					}
					System.out.println("tMusicID = " + tMusicID);
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusicSeekTo(tMusicID, true, iMusicPos);
//					}
					//更新菜单
					iGameMenuDialog.updateMenuState(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING ? true : false);
					//菜单按钮
					iMenuButton.setVision(true);
					//iMenuButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				}
				else if(iNotifyResult.nResult == ResultCode.SUCCESS) {
					//后台状态
					iServerState = E_TGS_WAIT_READY;
					//停止计时器动画
					iOvalTimerCount.pause();
					//停止骰子动画
					iDiceMovieState = DICE_MOVIE_STATE_IDLE;
					//复位上次喊的骰子
					iLastShoutCount = 0;
					iLastShoutDice = 0;
//					iLastShoutSeatID = 0;
					iCurrentShoutSeatID = 0;
					//
					iResultMsg = iNotifyResult.iDrunkMsg;
					//
					iGameTick = 0;
					//
					iWinSeatID = iNotifyResult.iWinnerSeatID;		//胜利者的座位ID
					iFailSeatID = iNotifyResult.iFailSeatID;	//失败者的座位ID
					iFailCurrentDrunk = iNotifyResult.iFailCurrentDrunk;
					iCleanDrunkPrice = iNotifyResult.iWinnerKillsCount; //失败者醒酒需要的金币
					iRewardExp = iNotifyResult.iWinnderGetGB;	//灌倒人获得的经验
					//
					if(iSelfSeatID == iFailSeatID 
							&& iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING) {
						//醉酒度
						MyArStation.iPlayer.iCurrentDrunk = iFailCurrentDrunk;
					}
					iChopDice = iNotifyResult.iChopDice;
					iPlayingCount = iNotifyResult.iPlayerCount;
					for(int i=0; i<iNotifyResult.iPlayerCount; i++) {
						int seatID = iNotifyResult.iSeatIDs[i];
						//记录玩游戏的人的座位ID
						iPlayingSeatIDs[i] = seatID;
						//改变状态
						iPlayerList[seatID].iGameState = CGamePlayer.PLAYER_STATE_RESULT;
						iPlayerList[seatID].iPlayingState = CGamePlayer.PLAYING_STATE_WAIT_SHOW_RESULT;
						iPlayerList[seatID].ibCrazed = false;
						//
						iPlayerList[seatID].iAddExp = iNotifyResult.iScores[i];
						//
						iPlayerList[seatID].iAddGb = iNotifyResult.iGBs[i];
						//
						iPlayerList[seatID].iAddVipExp = iNotifyResult.iVIPScores[i];
						//
						iPlayerList[seatID].iAddFriendExp = iNotifyResult.iFriendScores[i];
						//复位被劈的骰子个数
						iPlayerList[seatID].iChopDiceCount = 0;
						//骰子赋值
						for(int k=0; k<CGamePlayer.MAX_DICE_COUNT; k++) {
							iPlayerList[seatID].iDices[k] = iNotifyResult.iDices[i][k];
							if(ibShoutOne) {
								if(iPlayerList[seatID].iDices[k] == iChopDice) {
									iPlayerList[seatID].iBrightDices[k] = true;
									iPlayerList[seatID].iChopDiceCount ++;
								}
								else {
									iPlayerList[seatID].iBrightDices[k] = false;
								}
							}
							else {
								if(iPlayerList[seatID].iDices[k] == iChopDice
										|| iPlayerList[seatID].iDices[k] == 1) {
									iPlayerList[seatID].iBrightDices[k] = true;
									iPlayerList[seatID].iChopDiceCount ++;
								}
								else {
									iPlayerList[seatID].iBrightDices[k] = false;
								}
							}
						}
						//判断是否唯色，如果是，唯色加1
						if(iPlayerList[seatID].iChopDiceCount >= 5) {
							iPlayerList[seatID].iChopDiceCount = 6;
						}
					}
					//
					iShowingResultSeatID = iChopSeatID;
					//劈的人先显示结果
					iPlayerList[iShowingResultSeatID].iGameState = CGamePlayer.PLAYER_STATE_RESULT;
					iPlayerList[iShowingResultSeatID].iPlayingState = CGamePlayer.PLAYING_STATE_SHOWING_RESULT;
					//设置开骰盅动画
					iPlayerList[iShowingResultSeatID].iCupMotionID = 4;
					iPlayerList[iShowingResultSeatID].iCupSprite.resetFrameID();
					//计算金币斜率
					iActiveGbSpriteIndex = 0;
					int bmpW = iPlayerList[iNotifyResult.iFailSeatID].iBmp.getWidth();
					int bmpH = iPlayerList[iNotifyResult.iFailSeatID].iBmp.getHeight();
					for(int i=0; i<MAX_GB_SPRITE; i++) {
						iLifeTick[i] = 18;
						iGbX[i] = iGbStartX[i] = iPlayerList[iChangedSeatID[iNotifyResult.iFailSeatID]].iX - (bmpW>>1) + (int)(Math.random() * bmpW);
						iGbY[i] = iGbStartY[i] = iPlayerList[iChangedSeatID[iNotifyResult.iFailSeatID]].iY - bmpH + (int)(Math.random() * bmpH);
						iGbEndX[i] = iPlayerList[iChangedSeatID[iNotifyResult.iWinnerSeatID]].iX; 
						iGbEndY[i] = iPlayerList[iChangedSeatID[iNotifyResult.iWinnerSeatID]].iY; 
						if(iGbEndX[i]-iGbStartX[i] != 0) {
							iLineK[i] = ((float)(iGbEndY[i]-iGbStartY[i]))/((float)(iGbEndX[i]-iGbStartX[i]));
							iGbSpeed[i] = ((float)(iGbEndX[i] - iGbStartX[i])) / (float)18;
						}
						else {
							iGbSpeed[i] = ((float)(iGbEndY[i]-iGbStartY[i])) / (float)18;
						}
					}
				}
				//更新菜单
				//boolean bPlaynigGame = false;
				//if(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING
				//		|| iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_RESULT) {
				//	bPlaynigGame = true;
				//}
				//iGameMenuDialog.updateMenuState(bPlaynigGame);
				//显示参加活动对话框
				if(iNeedShowActivityDialog == true) {
					iNeedShowActivityDialog = false;
					//
					showMotionStart(iActivityConcours);
				}
				//
//				System.out.println("StartNotify 777777777777777777777777");
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_INIT_SYSTEM_BUTTON:
			{
				initSystemButton();
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_SHOW_SYSTEM_BUTTON:
			{
//				//初始化弹出按钮的动画
//				initAnimation();
				//
				iAbLayout.setVisibility(View.VISIBLE);
				//根据选择的位置将按钮隐藏
				enableIconButton();
				//
				startOutAnimation();
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_HIDE_SYSTEM_BUTTON:
			{
				startInAnimation();
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_DELET_SYSTEM_BUTTON:
			{
				//删除系统控件
				if(iAbLayout != null) {
					iAbLayout.removeAllViews();
					iAbLayout.removeAllViewsInLayout();
					iAbLayout.setVisibility(View.GONE);
//					System.out.println("IconButton delete 222222222222222-----------------");
				}
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_SYSTEM_BUTTON_CHANGEXY:
			{
				int buttonID = msg.arg1;
				int animID = msg.arg2;
				Button pB = mList.get(buttonID);
				pB.setAnimation(null);
				LayoutParams lp = (LayoutParams)pB.getLayoutParams();
				lp.leftMargin = mButtonEndX[animID];
				lp.topMargin = mButtonEndY[animID];
				pB.setLayoutParams(lp);
//				System.out.println("IconButton out animID=" + animID
//						+ " buttonID=" + buttonID
//						+ " lpX=" + lp.x + " lpY=" + lp.y);
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_KICK:
			{
				MBRspKick rspKick = (MBRspKick)msg.obj;
				if(rspKick.iMsg.length() > 0) {
					//提示
					Context tContext = MyArStation.mGameMain.getApplicationContext();
					Tools.showSimpleToast(tContext, Gravity.CENTER, rspKick.iMsg, Toast.LENGTH_LONG);
					//
					iChatView.updateSystemMessage(new ChatContent("[系统消息]", rspKick.iMsg));
				}
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyKICKOpenHomeScreen:
			{
//				System.out.println("KickOff 0000000000000000");
				MBNotifyKick notifyKick = (MBNotifyKick)msg.obj;
				if(notifyKick.iMsg.length() > 0) {
//					System.out.println("KickOff msg.length=" + notifyKick.iMsg.length());
					Tools.showSimpleToast(MyArStation.mGameMain, Gravity.CENTER, notifyKick.iMsg, Toast.LENGTH_LONG);
					iChatView.updateSystemMessage(new ChatContent("[系统消息]", notifyKick.iMsg));
				}
				if(notifyKick.iSeatID == iSelfSeatID) {
					MyArStation.iPlayer.iGb += notifyKick.iPrice * notifyKick.iWineCount;
					if(MyArStation.iPlayer.iGb < 0) {
						MyArStation.iPlayer.iGb = 0;
					}
					messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
				}
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_AddFriend:
			{
				MBRspAddFriend rspAddFriend = (MBRspAddFriend)msg.obj;
				if(rspAddFriend.iMsg.length() > 0) {
					Tools.showSimpleToast(MyArStation.mGameMain, Gravity.CENTER, rspAddFriend.iMsg, Toast.LENGTH_LONG);
					iChatView.updateSystemMessage(new ChatContent("[系统消息]", rspAddFriend.iMsg));
				}
			}	
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspDing:
			{
				MBRspDing rspDing = (MBRspDing)msg.obj;
				if(rspDing.iMsg.length() > 0) {
					if(rspDing.iResult != 0) {
						Tools.showSimpleToast(MyArStation.mGameMain, Gravity.CENTER, rspDing.iMsg, Toast.LENGTH_LONG);
					}
					//else {
						//顶好友成功，奖200金币
					//	GameMain.iPlayer.iGb += 200;
					//}
					iChatView.updateSystemMessage(new ChatContent("[系统消息]", rspDing.iMsg));
				}
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_SINGLE_PIC://单张图片下完
			{
				String tPicName = (String) msg.obj;//图片名
//				if (tPicName.equals(iPInfoLittlePicName)) {
//					setNameCardPic();
//				}
//				playerUpdateHeadPic(tPicName);
				for(int i=0; i<MAX_GAME_PLAYER; i++) {
					if(iPlayerList[i].ibInit == false) {
						continue;
					}
					if(tPicName.equals(iPlayerList[i].stHeadPicName)) {
						Bitmap tmpBmp = MyArStation.iHttpDownloadManager.getImage(iPlayerList[i].stHeadPicName);
						float hPara = (float)iPlayerList[i].iSprite.getFirstFrameHeight() / (float)tmpBmp.getHeight();
						int zoomW = (int)(tmpBmp.getWidth() * hPara);
						iPlayerList[i].iBmp = Graphics.zoomBitmap(tmpBmp, 
								zoomW, 
								iPlayerList[i].iSprite.getFirstFrameHeight());
						tmpBmp = null;
					}
				}
			}
			break;	
			
		case MessageEventID.EMESSAGE_EVENT_NOTIFY_CHANGE_DICE:
			{
				MBNotifyChangeDice notifyChangeDice = (MBNotifyChangeDice)msg.obj;
				iChangeDice = notifyChangeDice.iDice;
				//复位画布
				createBMP = true;
				//改变骰子
				iHideBarDice = true;
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_SYS_MSG:
			{
				if(iGameState < GAME_STATE_SITDOWN) {
					break;
				}
				MBNotifySysMsg sNotifySysMsg = (MBNotifySysMsg)msg.obj;
				if(sNotifySysMsg.iMsg.length() > 0) {
//					if(sNotifySysMsg.iType == 1) {
//						Tools.showSimpleToast(GameMain.mGameMain, Gravity.CENTER, sNotifySysMsg.iMsg, Toast.LENGTH_LONG);
//					}
					iChatView.updateSystemMessage(new ChatContent("[系统消息]", sNotifySysMsg.iMsg));
				}
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyCleanDunk: //醒酒应答
			{
				MBNotifyCleanDrunk rspCleanDrunk = (MBNotifyCleanDrunk)msg.obj;
				if(rspCleanDrunk.iSeatID < 0) {
					MyArStation.iPlayer.iGb = rspCleanDrunk.iCurrentGb;
					MyArStation.iPlayer.iCurrentDrunk = 0;
				}
				else {
					iPlayerList[rspCleanDrunk.iSeatID].iCurrentDrunk = 0;
					if(rspCleanDrunk.iSeatID == iSelfSeatID) {
						MyArStation.iPlayer.iGb = rspCleanDrunk.iCurrentGb;
						MyArStation.iPlayer.iCurrentDrunk = 0;
					}
					else {
						if(rspCleanDrunk.iMsg.length() > 0) {
							Tools.showSimpleToast(MyArStation.mGameMain, Gravity.CENTER, rspCleanDrunk.iMsg, Toast.LENGTH_LONG);
						}
					}
				}
				if(rspCleanDrunk.iMsg.length() > 0) {
					//Tools.showSimpleToast(GameMain.mGameMain, Gravity.CENTER, rspCleanDrunk.iMsg, Toast.LENGTH_LONG);
					iChatView.updateSystemMessage(new ChatContent("[系统消息]", rspCleanDrunk.iMsg));
				}
			}
			break;
			
			//显示信息
		case MessageEventID.EMESSAGE_EVENT_REQ_SHOW_MSG:
			{
				if(((String)msg.obj).length() > 0) {
					//Tools.showSimpleToast(GameMain.mGameMain, Gravity.CENTER, (String)msg.obj, Toast.LENGTH_LONG);
					iChatView.updateSystemMessage(new ChatContent("[系统消息]", (String)msg.obj));
				}
				removeProgressDialog();
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyChat:
			{
				if(iGameState < GAME_STATE_SITDOWN) {
					break;
				}
				MBNotifyChat notifyChat = (MBNotifyChat)msg.obj;
				if(notifyChat.iSeatID >= 0 && notifyChat.iSeatID <= MAX_GAME_PLAYER) {
					iChatView.updateHostoryList(new ChatContent(iPlayerList[notifyChat.iSeatID].stUserNick,
							notifyChat.iMsg));
					int colCount = (int)(MyArStation.iImageManager.iChatBmp.getWidth() / ActivityUtil.TEXTSIZE_NORMAL);
					int tCount = notifyChat.iMsg.length();
					int tStartIndex = 0;
					if(notifyChat.iMsg.length() > colCount) {
						tCount = colCount;
					}
					if(tCount > 0) {
						iPlayerList[notifyChat.iSeatID].iChatTick = 48;
						iPlayerList[notifyChat.iSeatID].iChatStrA = notifyChat.iMsg.substring(0, tCount);
						if(iPlayerList[notifyChat.iSeatID].iChatStrA == null) {
							iPlayerList[notifyChat.iSeatID].iChatStrA = " ";
							iPlayerList[notifyChat.iSeatID].iChatStrB = "";
							iPlayerList[notifyChat.iSeatID].iChatStrC = "";
						}
					}
					else {
						iPlayerList[notifyChat.iSeatID].iChatStrA = "";
						iPlayerList[notifyChat.iSeatID].iChatStrB = "";
						iPlayerList[notifyChat.iSeatID].iChatStrC = "";
						break;
					}
					tStartIndex += tCount;
					tCount = notifyChat.iMsg.length() - tStartIndex;
					if(tCount > colCount) {
						tCount = colCount;
					}
					if(tCount > 0) {
						iPlayerList[notifyChat.iSeatID].iChatStrB = notifyChat.iMsg.substring(tStartIndex, tStartIndex+tCount);
						if(iPlayerList[notifyChat.iSeatID].iChatStrB == null) {
							iPlayerList[notifyChat.iSeatID].iChatStrB = " ";
							iPlayerList[notifyChat.iSeatID].iChatStrC = "";
						}
					}
					else {
						iPlayerList[notifyChat.iSeatID].iChatStrB = "";
						iPlayerList[notifyChat.iSeatID].iChatStrC = "";
						break;
					}
					tStartIndex += tCount;
					tCount = notifyChat.iMsg.length() - tStartIndex;
					if(tCount > colCount) {
						tCount = colCount;
					}
					if(tCount > 0) {
						iPlayerList[notifyChat.iSeatID].iChatStrC = notifyChat.iMsg.substring(tStartIndex, tStartIndex+tCount);
						if(iPlayerList[notifyChat.iSeatID].iChatStrC == null) {
							iPlayerList[notifyChat.iSeatID].iChatStrC = " ";
						}
					}
					else {
						iPlayerList[notifyChat.iSeatID].iChatStrC = "";
					}
				}
			}
			break;	
			
		case GameEventID.ESPRITEBUTTON_EVENT_BUTTON_CHAT: //弹出聊天窗口
			{
				iChatView.showAtLocation(iGameCanvas, Gravity.CENTER, 0, 0);
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_BUTTON_MENU: //弹出菜单窗口
			{
				//活动房的信息
				if(iLogicType == E_TGLT_ACTIVITY_SHOUTONENOTCHANGE) {
					showReturnLoginConfirm();
				}
				else {
					iGameMenuDialog.show(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING ? true : false);
				}
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_BUTTON_FACE: //弹出表情窗口
			{
				feedBackDialog.show();
//				stroeDialog.show();
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspBuyWine: //
		{
			MBRspBuyWine buyWine = (MBRspBuyWine) msg.obj;
			if(buyWine.iMsg.length() > 0) {
				Tools.showSimpleToast(MyArStation.mGameMain, Gravity.CENTER, buyWine.iMsg, Toast.LENGTH_LONG);
				iChatView.updateSystemMessage(new ChatContent("[系统消息]", buyWine.iMsg));
			}
			MyArStation.iPlayer.iGb = buyWine.iCurrentGb;
			iPlayerList[iSelfSeatID].iCurrentWineCount = buyWine.iWineCount;
			//再判断是否要醒酒
//			if(iPlayerList[iSelfSeatID].iCurrentDrunk >= 5) {
//				iBuyWineDialog.Show(GameMain.mGameMain.getApplicationContext().getString(R.string.cleandrunktip,
//						""+iCleanDrunkPrice), BuyDialog.TYPE_BUYCLEANDRINK);
//			}
		}
		break;
		
		case GameEventID.ESPRITEBUTTON_EVENT_SHOW_BUYWINEDIALOG:	//显示买酒对话框
			{
				iBuyWineDialog.Show(MyArStation.mGameMain.getApplicationContext().getString(R.string.buywinetip,
						iWineName + iWinePrice), BuyDialog.TYPE_BUYWINE);
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_SHOW_CLEANDRUNKDIALOG:
			{
				iBuyWineDialog.Show(MyArStation.mGameMain.getApplicationContext().getString(R.string.cleandrunktip,
						""+iCleanDrunkPrice), BuyDialog.TYPE_BUYCLEANDRINK);
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_RSP_PALYERINFOTHR:
			{
				MBRspPlayerInfoThree infoThree = (MBRspPlayerInfoThree)msg.obj;
				if(MyArStation.iPlayer.iUserID == infoThree.iUserId) {
					MyArStation.iPlayer.iUserID = infoThree.iUserId;//用户uid
					MyArStation.iPlayer.stUserNick = infoThree.iNick;//用户昵称
					MyArStation.iPlayer.iVipLevel = infoThree.iVipLevel;//Vip级别	
					MyArStation.iPlayer.iGameLevel = infoThree.iGameLevel;	//级别
					MyArStation.iPlayer.iGameExp = infoThree.iGameExp;	//当前级别的经验
					MyArStation.iPlayer.iCurrentDrunk = infoThree.iCurrentDrunk;//当前醉酒度
					MyArStation.iPlayer.iMaxHit = infoThree.iMaxDrunk;//最大连击数
					MyArStation.iPlayer.iWinCount = infoThree.iKillsCount;//胜
					MyArStation.iPlayer.iFailCount = infoThree.iBeKillsCount;//负
					MyArStation.iPlayer.iModelID = infoThree.iModelID;	//角色模型ID（为-1时，表示没选择人物）
					MyArStation.iPlayer.iSex = infoThree.iSex;	//性别（为-1时，表示没选择人物）
					MyArStation.iPlayer.iWinning = infoThree.iWinning;	//胜率
					MyArStation.iPlayer.iTitle = infoThree.iTitle;//人物称号
					MyArStation.iPlayer.iGb = infoThree.iGb;//游戏币
					MyArStation.iPlayer.iReward = infoThree.iReward;//奖
					MyArStation.iPlayer.iMoney = infoThree.iMoney;//凤智币
					MyArStation.iPlayer.iWealthRank = infoThree.iWealthRank;//财富排名
					MyArStation.iPlayer.iLevelRank = infoThree.iLevelRank;//等级排名
					MyArStation.iPlayer.iVictoryRank = infoThree.iVictoryRank;//胜场排名
					MyArStation.iPlayer.szLittlePicName = infoThree.iLittlePicName;//头像图片
					MyArStation.iPlayer.szBigPicName = infoThree.iBigPicName;//大图片
					MyArStation.iPlayer.iDingCount = infoThree.iSupportCount;//支持次数
					MyArStation.iPlayer.iRankTotalCount = infoThree.iRankTotalCount;//排行榜总数
					MyArStation.iPlayer.iDiceID = infoThree.nDiceResID; //骰子的资源ID
					MyArStation.iPlayer.iNextExp = infoThree.iNextExp;
					//更新自己的骰子类型
					iPlayerList[iSelfSeatID].iDiceID = infoThree.nDiceResID / 10;
					iPlayerList[iSelfSeatID].iNextDiceCount = infoThree.nDiceResID % 10;
					//更新VIP信息
					iPlayerList[iSelfSeatID].iVipLevel = MyArStation.iPlayer.iVipLevel;
					//是否自动获取个人信息
					if(ibAutoReqInfo) {
						ibAutoReqInfo = false;
						//重新计算经验条
						setExpRect();
					}
					else {
						infoPopView.loadScene(0);
						infoPopView.setAmendButton(false);
						infoPopView.setPlayerInfo(MyArStation.iPlayer);;
						infoPopView.show();
					}
				}
				else {
					CPlayer cPlayer = new CPlayer();
					cPlayer.iUserID = infoThree.iUserId;//用户uid
					cPlayer.stUserNick = infoThree.iNick;//用户昵称
					cPlayer.iVipLevel = infoThree.iVipLevel;//Vip级别	
					cPlayer.iGameLevel = infoThree.iGameLevel;	//级别
					cPlayer.iGameExp = infoThree.iGameExp;	//当前级别的经验
					cPlayer.iCurrentDrunk = infoThree.iCurrentDrunk;//当前醉酒度
					cPlayer.iMaxHit = infoThree.iMaxDrunk;//最大连击数
					cPlayer.iWinCount = infoThree.iKillsCount;//胜
					cPlayer.iFailCount = infoThree.iBeKillsCount;//负
					cPlayer.iModelID = infoThree.iModelID;	//角色模型ID（为-1时，表示没选择人物）
					cPlayer.iSex = infoThree.iSex;	//性别（为-1时，表示没选择人物）
					cPlayer.iWinning = infoThree.iWinning;	//胜率
					cPlayer.iTitle = infoThree.iTitle;//人物称号
					cPlayer.iGb = infoThree.iGb;//游戏币
					cPlayer.iReward = infoThree.iReward;//奖
					cPlayer.iMoney = infoThree.iMoney;//凤智币
					cPlayer.iWealthRank = infoThree.iWealthRank;//财富排名
					cPlayer.iLevelRank = infoThree.iLevelRank;//等级排名
					cPlayer.iVictoryRank = infoThree.iVictoryRank;//胜场排名
					cPlayer.szLittlePicName = infoThree.iLittlePicName;//头像图片
					cPlayer.szBigPicName = infoThree.iBigPicName;//大图片
					cPlayer.iDingCount = infoThree.iSupportCount;//支持次数
					cPlayer.iRankTotalCount = infoThree.iRankTotalCount;//排行榜总数
					cPlayer.iDiceID = infoThree.nDiceResID; //骰子的资源ID
					cPlayer.iFriendTitle = infoThree.iFriendTitle;
					cPlayer.iNextExp = infoThree.iNextExp;
					cPlayer.iIntimate = infoThree.iFriendExp;
					cPlayer.iNextIntimate = infoThree.iNextFriendExp;
					cPlayer.iZhaiSkill = infoThree.iZhaiSkill;	// 0 --- 没学   1 --- 喊斋    2 --- 雷劈
					infoPopView.loadScene(1);
					infoPopView.setPlayerInfo(cPlayer);;
					infoPopView.show();
					cPlayer = null;
				}
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_PALYERINFOFOUR:
		{
			MBRspPlayerInfoFour infoFour = (MBRspPlayerInfoFour)msg.obj;
			if(MyArStation.iPlayer.iUserID == infoFour.iUserId) {
				MyArStation.iPlayer.iUserID = infoFour.iUserId;//用户uid
				MyArStation.iPlayer.stUserNick = infoFour.iNick;//用户昵称
				MyArStation.iPlayer.iVipLevel = infoFour.iVipLevel;//Vip级别	
				MyArStation.iPlayer.iGameLevel = infoFour.iGameLevel;	//级别
				MyArStation.iPlayer.iGameExp = infoFour.iGameExp;	//当前级别的经验
				MyArStation.iPlayer.iCurrentDrunk = infoFour.iCurrentDrunk;//当前醉酒度
				MyArStation.iPlayer.iMaxHit = infoFour.iMaxDrunk;//最大连击数
				MyArStation.iPlayer.iWinCount = infoFour.iKillsCount;//胜
				MyArStation.iPlayer.iFailCount = infoFour.iBeKillsCount;//负
				MyArStation.iPlayer.iModelID = infoFour.iModelID;	//角色模型ID（为-1时，表示没选择人物）
				MyArStation.iPlayer.iSex = infoFour.iSex;	//性别（为-1时，表示没选择人物）
				MyArStation.iPlayer.iWinning = infoFour.iWinning;	//胜率
				MyArStation.iPlayer.iTitle = infoFour.iTitle;//人物称号
				MyArStation.iPlayer.iGb = infoFour.iGb;//游戏币
				MyArStation.iPlayer.iReward = infoFour.iReward;//奖
				MyArStation.iPlayer.iMoney = infoFour.iMoney;//凤智币
				MyArStation.iPlayer.iWealthRank = infoFour.iWealthRank;//财富排名
				MyArStation.iPlayer.iLevelRank = infoFour.iLevelRank;//等级排名
				MyArStation.iPlayer.iVictoryRank = infoFour.iVictoryRank;//胜场排名
				MyArStation.iPlayer.szLittlePicName = infoFour.iLittlePicName;//头像图片
				MyArStation.iPlayer.szBigPicName = infoFour.iBigPicName;//大图片
				MyArStation.iPlayer.iDingCount = infoFour.iSupportCount;//支持次数
				MyArStation.iPlayer.iRankTotalCount = infoFour.iRankTotalCount;//排行榜总数
				MyArStation.iPlayer.iDiceID = infoFour.nDiceResID; //骰子的资源ID
				MyArStation.iPlayer.iNextExp = infoFour.iNextExp;
				MyArStation.iPlayer.iHonor = infoFour.iHonor;
				//更新自己的骰子类型
				iPlayerList[iSelfSeatID].iDiceID = infoFour.nDiceResID / 10;
				iPlayerList[iSelfSeatID].iNextDiceCount = infoFour.nDiceResID % 10;
				//更新VIP信息
				iPlayerList[iSelfSeatID].iVipLevel = MyArStation.iPlayer.iVipLevel;
				//是否自动获取个人信息
				if(ibAutoReqInfo) {
					ibAutoReqInfo = false;
					//重新计算经验条
					setExpRect();
				}
				else {
					infoPopView.loadScene(0);
					infoPopView.setAmendButton(false);
					infoPopView.setPlayerInfo(MyArStation.iPlayer);;
					infoPopView.show();
				}
			}
			else {
				CPlayer cPlayer = new CPlayer();
				cPlayer.iUserID = infoFour.iUserId;//用户uid
				cPlayer.stUserNick = infoFour.iNick;//用户昵称
				cPlayer.iVipLevel = infoFour.iVipLevel;//Vip级别	
				cPlayer.iGameLevel = infoFour.iGameLevel;	//级别
				cPlayer.iGameExp = infoFour.iGameExp;	//当前级别的经验
				cPlayer.iCurrentDrunk = infoFour.iCurrentDrunk;//当前醉酒度
				cPlayer.iMaxHit = infoFour.iMaxDrunk;//最大连击数
				cPlayer.iWinCount = infoFour.iKillsCount;//胜
				cPlayer.iFailCount = infoFour.iBeKillsCount;//负
				cPlayer.iModelID = infoFour.iModelID;	//角色模型ID（为-1时，表示没选择人物）
				cPlayer.iSex = infoFour.iSex;	//性别（为-1时，表示没选择人物）
				cPlayer.iWinning = infoFour.iWinning;	//胜率
				cPlayer.iTitle = infoFour.iTitle;//人物称号
				cPlayer.iGb = infoFour.iGb;//游戏币
				cPlayer.iReward = infoFour.iReward;//奖
				cPlayer.iMoney = infoFour.iMoney;//凤智币
				cPlayer.iWealthRank = infoFour.iWealthRank;//财富排名
				cPlayer.iLevelRank = infoFour.iLevelRank;//等级排名
				cPlayer.iVictoryRank = infoFour.iVictoryRank;//胜场排名
				cPlayer.szLittlePicName = infoFour.iLittlePicName;//头像图片
				cPlayer.szBigPicName = infoFour.iBigPicName;//大图片
				cPlayer.iDingCount = infoFour.iSupportCount;//支持次数
				cPlayer.iRankTotalCount = infoFour.iRankTotalCount;//排行榜总数
				cPlayer.iDiceID = infoFour.nDiceResID; //骰子的资源ID
				cPlayer.iFriendTitle = infoFour.iFriendTitle;
				cPlayer.iNextExp = infoFour.iNextExp;
				cPlayer.iIntimate = infoFour.iFriendExp;
				cPlayer.iNextIntimate = infoFour.iNextFriendExp;
				cPlayer.iZhaiSkill = infoFour.iZhaiSkill;	// 0 --- 没学   1 --- 喊斋    2 --- 雷劈
				cPlayer.iHonor = infoFour.iHonor;
				
				infoPopView.loadScene(1);
				infoPopView.setPlayerInfo(cPlayer);;
				infoPopView.show();
				cPlayer = null;
			}
		}
		break;
		
		//活动开始通知
		case MessageEventID.EMESSAGE_EVENT_NOTIFY_ACIIVITY_START:
			{
				MBNotifyActivityStart rspActivityStart = (MBNotifyActivityStart)msg.obj;
				iActivityConcours = null;
				iActivityConcours = new Concours(rspActivityStart.iMsg, 
						rspActivityStart.iActivityType,
						rspActivityStart.iActivityStatus, 
						rspActivityStart.iActivityID,
						rspActivityStart.iActivityRoomID,
						rspActivityStart.iRoomType);
//				iActivityRoomID = rspActivityStart.iActivityRoomID;
//				iActivityDialogInfo = rspActivityStart.iMsg;
				if(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING) {
					//是否需要显示参加活动对话框
					if(ibHangUpActivity == false) {
						iNeedShowActivityDialog = true;
					}
				}
				else {
					//显示参加活动对话框
					if(ibHangUpActivity == false && 
							(iGameState != GAME_STATE_LOADING || 
							iGameState != GAME_STATE_LEAVEROOM || 
							iGameState != GAME_STATE_CHANGEDESK_LOADING ||
							iGameState != GAME_STATE_JOINACTIVITY_LOADING)) {
						showMotionStart(iActivityConcours);
					}
				}
			}
			break;
			
		//活动排名通知
		case MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_RANKING_SELF:
			{
				MBNotifyActivityRanking rspActivityRanking = (MBNotifyActivityRanking)msg.obj;
				iActivityInfo = rspActivityRanking.iMsg;
			}
			break;
			
		//使用道具应答
		case MessageEventID.EMESSAGE_EVENT_RSP_USEITEM:
			{
				MBRspUseItem rspUseItem = (MBRspUseItem)msg.obj;
				if(rspUseItem.iItemID == 0) { //抢劈卡
					iChatView.updateSystemMessage(new ChatContent("[系统消息]", 
							"抢劈卡使用成功，还剩" + rspUseItem.iItemCount + "张"));
				}
				else if(rspUseItem.iItemID == 1) { //千王之王
					iChatView.updateSystemMessage(new ChatContent("[系统消息]", 
						"千王之王卡使用成功，还剩" + rspUseItem.iItemCount + "张"));
				}
				else if(rspUseItem.iItemID == 6) { //反劈卡
					iChatView.updateSystemMessage(new ChatContent("[系统消息]", 
							"反劈卡使用成功，还剩" + rspUseItem.iItemCount + "张"));
				}
				else if(rspUseItem.iItemID == 7) { //反劈卡
					iChatView.updateSystemMessage(new ChatContent("[系统消息]", 
							"啤酒瓶使用成功，还剩" + rspUseItem.iItemCount + "个"));
				}
				else if(rspUseItem.iItemID == 8) { //反劈卡
					iChatView.updateSystemMessage(new ChatContent("[系统消息]", 
							"蛋糕使用成功，还剩" + rspUseItem.iItemCount + "个"));
				}
				else {
					iChatView.updateSystemMessage(new ChatContent("[系统消息]", 
							"卡片使用成功，还剩" + rspUseItem.iItemCount + "张"));
				}
			}
			break;
			
		//抢劈道具的消息
		case MessageEventID.EMESSAGE_EVENT_NOTIFY_NOTIFY_VIE_CHOP:
			{
				MBNotifyVieChop rspVieChop = (MBNotifyVieChop)msg.obj;
				//将后台状态置为游戏中
				iServerState = E_TGS_PLAYING;
				//停止计时器动画
				iOvalTimerCount.pause();
				//
				iPlayerList[rspVieChop.iVieChopSeatID].iPlayingState = CGamePlayer.PLAYING_STATE_CHOPING;
				//
				iChopSeatID = rspVieChop.iVieChopSeatID;
				//
				iDoubleTimes <<= 1;
				iFlashBetTick = 16;
				//
				iDoubleSpriteEnd = false;
				iDoubleSprite.resetFrameID();
				//
				iChopDiceCount = 0;
				//抢劈
				iPreChopButton.setVision(false);
				//反劈
				if(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING
						&& iChopSeatID != iSelfSeatID) {
					if(iLastShoutSeatID == iSelfSeatID) {
					//if(iRebelChopButton.isVision() == false) {
						iRebelChopButton.setVision(true);
						iRebelChopButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_SHOW);
						iRebelChopSeatID = -1;
						iWantToShowRebleChopButton = false;
					}
					else {
						iRebelChopSeatID = -1;
						iWantToShowRebleChopButton = true;
						iShowRebelChopButtonDelay = System.currentTimeMillis();
					}
				}
				//滚轮
				iWheelVisible = false;
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).setVision(false);
				}
				//喊斋按钮
				iZhaiButton.setVision(false);
				//雷击按钮
				iLightningButton.setVision(false);
				//菜单按钮
				//iMenuButton.setVision(false);
				//聊天按钮
				iChatButton.setVision(true);
				//表情按钮
				iFaceButton.setVision(true);
				//任务按钮
				iGiftBUtton.setVision(true);
				if(MyArStation.iPlayer.iMissionRewardCount > 0) {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				}
				else {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				}
				//
				CallPointManager.getSoundManager().CallQiangPi(iPlayerList[iChopSeatID].iModelID);
			}
			break;
			
			//抢劈道具的消息B
		case MessageEventID.EMESSAGE_EVENT_NOTIFY_NOTIFY_VIE_CHOP_B:
			{
				MBNotifyVieChopB rspVieChopB = (MBNotifyVieChopB)msg.obj;
				//将后台状态置为游戏中
				iServerState = E_TGS_PLAYING;
				//停止计时器动画
				iOvalTimerCount.pause();
				//
				iPlayerList[rspVieChopB.iVieChopSeatID].iPlayingState = CGamePlayer.PLAYING_STATE_CHOPING;
				//
				iChopSeatID = rspVieChopB.iVieChopSeatID;
				//
				iDoubleTimes <<= 1;
				iFlashBetTick = 16;
				//
				iDoubleSpriteEnd = false;
				iDoubleSprite.resetFrameID();
				//
				if(iPlayerList[iChopSeatID].iVipLevel > 0 
						&& iPlayerList[rspVieChopB.iDesSeatID].ibInit) {
					int bmpH = iPlayerList[rspVieChopB.iDesSeatID].iBmp.getHeight();
					iLightStartX = iPlayerList[iChangedSeatID[iChopSeatID]].iX;
					iLightStartY = iPlayerList[iChangedSeatID[iChopSeatID]].iY - (bmpH>>1);
					iLightX = iPlayerList[iChangedSeatID[rspVieChopB.iDesSeatID]].iX;
					iLightY = iPlayerList[iChangedSeatID[rspVieChopB.iDesSeatID]].iY - (bmpH>>1);
					iLightState = 0;
					iLightningType = rspVieChopB.iType;
					iDesLightningID = rspVieChopB.iDesSeatID;
					iLightningVip = iPlayerList[iChopSeatID].iVipLevel;
					CURLINES = iLightningVip;
					if(CURLINES > MAXLINES) {
						CURLINES = MAXLINES;
					}
					for(int i = 0; i < CURLINES; i++) {
						lightnings[i].setStrikePoint((int)iLightStartX,(int)iLightStartY);
						lightnings[i].setStrikePoint2((int)iLightX, (int)iLightY);
						lightnings[i].setShadowColor(0xffff0000);
						lightnings[i].setVisible(true);
						lightnings[i].setDetTime(8);
					}
					ActivityUtil.mAlphaLine.setStrokeWidth(4 + iLightningVip);
					ActivityUtil.mAlphaLineC.setStrokeWidth(10 + (iLightningVip<<1));
					if(iLightningType > 0 || !iPlayerList[rspVieChopB.iNextDesSeatID].ibInit) {
						iNextDesSeatID = -1;
						iNextDesType = 0;
					}
					else {
						iNextDesSeatID = rspVieChopB.iNextDesSeatID;
						iNextDesType = rspVieChopB.iNextDesType;
					}
				}
				//
				iChopDiceCount = 0;
				//抢劈
				iPreChopButton.setVision(false);
				//反劈
				if(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING
						&& iChopSeatID != iSelfSeatID) {
					//if(iRebelChopButton.isVision() == false) {
					if(iLastShoutSeatID == iSelfSeatID) {
						iRebelChopButton.setVision(true);
						iRebelChopButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_SHOW);
						iRebelChopSeatID = -1;
						iWantToShowRebleChopButton = false;
					}
					else {
						iRebelChopSeatID = -1;
						iWantToShowRebleChopButton = true;
						iShowRebelChopButtonDelay = System.currentTimeMillis();
					}
				}
				//滚轮
				iWheelVisible = false;
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).setVision(false);
				}
				//喊斋按钮
				iZhaiButton.setVision(false);
				//雷击按钮
				iLightningButton.setVision(false);
				//菜单按钮
				//iMenuButton.setVision(false);
				//聊天按钮
				iChatButton.setVision(true);
				//表情按钮
				iFaceButton.setVision(true);
				//任务按钮
				iGiftBUtton.setVision(true);
				if(MyArStation.iPlayer.iMissionRewardCount > 0) {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				}
				else {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				}
				//
				CallPointManager.getSoundManager().CallQiangPi(iPlayerList[iChopSeatID].iModelID);
			}
			break;	
			
		//反劈道具的消息
		case MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_REBEL_CHOP:
			{
				MBNotifyRebelChop rspRebelChop = (MBNotifyRebelChop)msg.obj;
				//将后台状态置为游戏中
				iServerState = E_TGS_PLAYING;
				//停止计时器动画
				iOvalTimerCount.pause();
				//
				iPlayerList[rspRebelChop.iRebelChopSeatID].iPlayingState = CGamePlayer.PLAYING_STATE_REBEL_CHOPING;
				//
				iRebelChopSeatID = rspRebelChop.iRebelChopSeatID;
				iRebelChopSprite.resetFrameID();
				//
				iDoubleTimes <<= 1;
				iFlashBetTick = 16;
				//
				iDoubleSpriteEnd = false;
				iDoubleSprite.resetFrameID();
				//
				iWantToShowRebleChopButton = false;
				//抢劈
				iPreChopButton.setVision(false);
				//反劈
				iRebelChopButton.setVision(false);
				//滚轮
				iWheelVisible = false;
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).setVision(false);
				}
				//喊斋按钮
				iZhaiButton.setVision(false);
				//雷击按钮
				iLightningButton.setVision(false);
				//菜单按钮
				//iMenuButton.setVision(false);
				//聊天按钮
				iChatButton.setVision(true);
				//表情按钮
				iFaceButton.setVision(true);
				//任务按钮
				iGiftBUtton.setVision(true);
				if(MyArStation.iPlayer.iMissionRewardCount > 0) {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				}
				else {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				}
				//
				CallPointManager.getSoundManager().CallFanPi(iPlayerList[iRebelChopSeatID].iModelID);
			}
			break;		
			
		case MessageEventID.EMESSAGE_EVENT_NOTIFY_KILL_ORDER: //追杀令通知
			{
				MBNotifyKillOrder notifyKillOrder = (MBNotifyKillOrder)msg.obj;
				int tSeatID = notifyKillOrder.iUserSeatID;
				int tDSeatID = notifyKillOrder.iDesSeatID;
			}
			break;
			
		case MessageEventID.EMESSAGE_EVENT_NOTIFY_DING://顶通知
			{
				MBNotifyDing notifyDing = (MBNotifyDing) msg.obj;
				int tSeatID = notifyDing.iUserSeatID;
				int tDSeatID = notifyDing.iDesSeatID;
				if((tSeatID < 0 || tSeatID >= 6) || (tDSeatID < 0 || tDSeatID >= 6)) {
					break;
				}
				//飞心动画
				int bmpW = iPlayerList[notifyDing.iUserSeatID].iBmp.getWidth();
				int bmpH = iPlayerList[notifyDing.iUserSeatID].iBmp.getHeight();
				int tempIndex = 0;
				for(int i=0; i<MAX_HART_SPRITE; i++) {
					if(iHartLifeTick[i] <= 0) {
						tempIndex = i;
						break;
					}
				}
				iHartLifeTick[tempIndex] = 18;
				iHartX[tempIndex] = iHartStartX[tempIndex] = iPlayerList[iChangedSeatID[notifyDing.iUserSeatID]].iX;
				iHartY[tempIndex] = iHartStartY[tempIndex] = iPlayerList[iChangedSeatID[notifyDing.iUserSeatID]].iY - (bmpH>>1);
				iHartEndX[tempIndex] = iPlayerList[iChangedSeatID[notifyDing.iDesSeatID]].iX; 
				iHartEndY[tempIndex] = iPlayerList[iChangedSeatID[notifyDing.iDesSeatID]].iY - (bmpH>>1); 
				if(iHartEndX[tempIndex]-iHartStartX[tempIndex] != 0) {
					iHartLineK[tempIndex] = ((float)(iHartEndY[tempIndex]-iHartStartY[tempIndex]))/((float)(iHartEndX[tempIndex]-iHartStartX[tempIndex]));
					iHartSpeed[tempIndex] = ((float)(iHartEndX[tempIndex] - iHartStartX[tempIndex])) / (float)18;
				}
				else {
					iHartSpeed[tempIndex] = ((float)(iHartEndY[tempIndex]-iHartStartY[tempIndex])) / (float)18;
				}
				iHartType[tempIndex] = notifyDing.iSpriteType;
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_NOTIFY_LEVELUP://升级通知
			{
				//
				MBNotifyLevelUp notifyLevelUp = (MBNotifyLevelUp) msg.obj;
				if(notifyLevelUp.iSeatID == iSelfSeatID) {
					MyArStation.iPlayer.iGameLevel = notifyLevelUp.iGameLevel;
					MyArStation.iPlayer.iNextExp = notifyLevelUp.iNextExp;
					MyArStation.iPlayer.iGameExp = notifyLevelUp.iGameExp;
					MyArStation.iPlayer.iTitle = notifyLevelUp.iTitle;
					MyArStation.iPlayer.iDiceID = notifyLevelUp.nDiceID;
					//更新自己的骰子类型
					iPlayerList[iSelfSeatID].iDiceID = notifyLevelUp.nDiceID / 10;
					iPlayerList[iSelfSeatID].iNextDiceCount = notifyLevelUp.nDiceID % 10;
					//重新计算经验条
					setExpRect();
					//锁定经验条
					ibLockExp = true;
				}
//				iLevelUpSprite.resetFrameID();
				Sprite pSprite = new Sprite();
				pSprite.copyFrom(iLevelUpSprite);
				pSprite.setPosition((short)(iPlayerList[iChangedSeatID[notifyLevelUp.iSeatID]].iX),
						(short)(iPlayerList[iChangedSeatID[notifyLevelUp.iSeatID]].iY-20));
				pSprite.resetFrameID();
				iLevelUpSpriteList.addElement(pSprite);
				//
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_RSP_FRINDEVENTSELECT:
			{
				MBRspFriendEventSelect rspFriendEventSelect = (MBRspFriendEventSelect)msg.obj;
				if(rspFriendEventSelect.iMsg.length() > 0) {
					Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), 
							rspFriendEventSelect.iMsg);
				}
				MyArStation.mGameMain.iPlayer.iGb = rspFriendEventSelect.iGB;
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
		case MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_PALY_ANIMATIONTWO: //播放动画2
		{
			MBNotifyPlayAnimationTwo notifyPlayAnimation = (MBNotifyPlayAnimationTwo)msg.obj;
			if(iSynAnimation != null) iSynAnimation.setAnimationData(notifyPlayAnimation.szAnimDes, iPlayerList, iChangedSeatID);
//			AnimFormatData animFormatData = new AnimFormatData(notifyPlayAnimation.szAnimDes);
//			//动态动画 寻找控制器
//			int tempIndex = 0;
//			for(int i=0; i<MAX_HART_SPRITE; i++) {
//				if(iAnimLifeTick[i] <= 0) {
//					tempIndex = i;
//					break;
//				}
//			}
//			//动态动画 设置动作
//			iStartAnimID[tempIndex] = animFormatData.nStartID;
//			iStartMotion[tempIndex] = animFormatData.nStartMotion;
//			iEndAnimID[tempIndex] = animFormatData.nEndID;
//			iEndMotion[tempIndex] = animFormatData.nEndMotion;
//			//动态动画 寻找Pak
//			iAnimName[tempIndex] = animFormatData.szPak;
//			Project tPj = CPakManager.loadSynProject(animFormatData.szPak);
//			if(tPj == null) {
//				break;
//			}
//			//
//			Sprite tsp = tPj.getSprite(animFormatData.nStartID);
//			iAnimSprite[tempIndex].copyFrom(tsp);
//			//设置生命周期
//			iAnimLifeTick[tempIndex] = 18;
//			//计算起始坐标
//			if(animFormatData.nStartType == 0) { //位置模式
//				int tSeatID = animFormatData.nX1;
//				if(tSeatID < 0 || tSeatID >= 6) {
//					if(tSeatID < 0) {
//						tSeatID = 6;
//					}
//					if(tSeatID > 14) {
//						tSeatID = 14;
//					}
//					//6 ~ 14  左上 ， 上，  右上 ，左，中，右，左下，下，右下
//					iAnimX[tempIndex] = iAnimStartX[tempIndex] = ActivityUtil.SCREEN_POS_X[tSeatID-6];
//					iAnimY[tempIndex] = iAnimStartY[tempIndex] = ActivityUtil.SCREEN_POS_Y[tSeatID-6];
//				}
//				else {
//					//0 ~ 5  玩家的座位ID
//					int bmpH = 80;
//					if(iPlayerList[tSeatID].iBmp != null) {
//						bmpH = iPlayerList[tSeatID].iBmp.getHeight();
//					}
//					iAnimX[tempIndex] = iAnimStartX[tempIndex] = iPlayerList[iChangedSeatID[tSeatID]].iX;
//					iAnimY[tempIndex] = iAnimStartY[tempIndex] = iPlayerList[iChangedSeatID[tSeatID]].iY - (bmpH>>1);
//				}
//			}
//			else { //坐标模式
//				iAnimX[tempIndex] = iAnimStartX[tempIndex] = (int)(animFormatData.nX1 * ActivityUtil.ZOOM_X);
//				iAnimY[tempIndex] = iAnimStartY[tempIndex] = (int)(animFormatData.nY1 * ActivityUtil.ZOOM_X);
//			}
//			//计算结束坐标
//			if(animFormatData.nEndType == 0) { //浣嶇疆妯″紡
//				int tDesSeatID = animFormatData.nX2;
//				if(tDesSeatID < 0 || tDesSeatID >= 6) {
//					//6 ~ 14  左上 ， 上，  右上 ，左，中，右，左下，下，右下
//					iAnimEndX[tempIndex] = ActivityUtil.SCREEN_POS_X[tDesSeatID-6];
//					iAnimEndY[tempIndex] = ActivityUtil.SCREEN_POS_Y[tDesSeatID-6];
//				}
//				else {
//					int bmpH = 80;
//					if(iPlayerList[tDesSeatID].iBmp != null) {
//						bmpH = iPlayerList[tDesSeatID].iBmp.getHeight();
//					}
//					iAnimEndX[tempIndex] = iPlayerList[iChangedSeatID[tDesSeatID]].iX; 
//					iAnimEndY[tempIndex] = iPlayerList[iChangedSeatID[tDesSeatID]].iY - (bmpH>>1);
//				}
//			}
//			else {	//坐标模式
//				iAnimEndX[tempIndex] = (int)(animFormatData.nX2 * ActivityUtil.ZOOM_X);
//				iAnimEndY[tempIndex] = (int)(animFormatData.nY2 * ActivityUtil.ZOOM_Y);
//			}
//			//计算速度
//			if(iAnimEndX[tempIndex]-iAnimStartX[tempIndex] != 0) {
//				iAnimLineK[tempIndex] = ((float)(iAnimEndY[tempIndex]-iAnimStartY[tempIndex]))/((float)(iAnimEndX[tempIndex]-iAnimStartX[tempIndex]));
//				iAnimSpeed[tempIndex] = ((float)(iAnimEndX[tempIndex] - iAnimStartX[tempIndex])) / (float)18;
//			}
//			else {
//				iAnimSpeed[tempIndex] = ((float)(iAnimEndY[tempIndex]-iAnimStartY[tempIndex])) / (float)18;
//			}
		}
		break;
		case MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_PALY_ANIMATION: //播放动画
			{
				MBNotifyPlayAnimation notifyPlayAnimation = (MBNotifyPlayAnimation)msg.obj;
				
//				if(notifyPlayAnimation.iAnimationID == 0) { //播放烟花
//					int tCount =(int)(Math.random() * 6);
//					tCount += 18;
//					iYanHuaSpriteCount = tCount;
//					//停止背景音乐
//					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).stopMusic();
//				}
				
				//
//				Tools.debug("cakesprite iAnimationID=" + notifyPlayAnimation.iAnimationID);
				int tSeatID = notifyPlayAnimation.iUserSeatID;
				int tDesSeatID = notifyPlayAnimation.iDesSeatID;
				if((tSeatID < 0 || tSeatID >= 6) || (tDesSeatID < 0 || tDesSeatID >= 6)) {
					break;
				}
				//道具动画
				int bmpW = iPlayerList[notifyPlayAnimation.iUserSeatID].iBmp.getWidth();
				int bmpH = iPlayerList[notifyPlayAnimation.iUserSeatID].iBmp.getHeight();
				int tempIndex = 0;
				for(int i=0; i<MAX_HART_SPRITE; i++) {
					if(iHartLifeTick[i] <= 0) {
						tempIndex = i;
						break;
					}
				}
				iHartLifeTick[tempIndex] = 18;
				iHartX[tempIndex] = iHartStartX[tempIndex] = iPlayerList[iChangedSeatID[notifyPlayAnimation.iUserSeatID]].iX;
				iHartY[tempIndex] = iHartStartY[tempIndex] = iPlayerList[iChangedSeatID[notifyPlayAnimation.iUserSeatID]].iY - (bmpH>>1);
				iHartEndX[tempIndex] = iPlayerList[iChangedSeatID[notifyPlayAnimation.iDesSeatID]].iX; 
				iHartEndY[tempIndex] = iPlayerList[iChangedSeatID[notifyPlayAnimation.iDesSeatID]].iY - (bmpH>>1); 
				if(iHartEndX[tempIndex]-iHartStartX[tempIndex] != 0) {
					iHartLineK[tempIndex] = ((float)(iHartEndY[tempIndex]-iHartStartY[tempIndex]))/((float)(iHartEndX[tempIndex]-iHartStartX[tempIndex]));
					iHartSpeed[tempIndex] = ((float)(iHartEndX[tempIndex] - iHartStartX[tempIndex])) / (float)18;
				}
				else {
					iHartSpeed[tempIndex] = ((float)(iHartEndY[tempIndex]-iHartStartY[tempIndex])) / (float)18;
				}
				//道具动画加10，是为了区分飞心动画
				iHartType[tempIndex] = notifyPlayAnimation.iAnimationID + 10;
				if(iHartType[tempIndex] < 10) {
					iHartType[tempIndex] = 10;
				}
				else if(iHartType[tempIndex] >= 10 + MAX_ITEM_SPRITE_TYPE) {
					iHartType[tempIndex] = 10;
				}
				//
				//如果目标座位是自己，并且是蛋糕动画
				if(notifyPlayAnimation.iDesSeatID == iSelfSeatID
							&& notifyPlayAnimation.iAnimationID == ITEM_CAKE_SPRITE_ID) {
					//加入遮挡物
					Sprite pSprite = new Sprite();
					//载入动画
					pSprite.copyFrom(iItemPak[ITEM_CAKE_SPRITE_ID].getSprite(1));
					//
					pSprite.setPosition((short)0, (short)iPlayerList[iChangedSeatID[notifyPlayAnimation.iDesSeatID]].iY);
					pSprite.resetFrameID();
					iItemSpriteList.addElement(pSprite);
					//
//					Tools.debug("cakesprite asdfsdfsadfasdfsdfasdfasdfsfd");
				}
				//如果是啤酒瓶,且自己用在自己身上，表示暴走
				if(notifyPlayAnimation.iDesSeatID == notifyPlayAnimation.iUserSeatID
						&& notifyPlayAnimation.iAnimationID == ITEM_BOTTLE_SPRITE_ID) {
					iPlayerList[notifyPlayAnimation.iDesSeatID].ibCrazed = true;
				}
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_FACE: //播放表情
			{
				MBNotifyFace notifyFace = (MBNotifyFace)msg.obj;
				int tSeatID = notifyFace.iSeatID;
				if(tSeatID < 0 || tSeatID >= 6) {
					break;
				}
				int tFaceID = notifyFace.iFaceID;
				if(tFaceID < 0) {
					tFaceID = 0;
				}
				else if(tFaceID > 14) {
					tFaceID = 14;
				}
				if(iPlayerList[tSeatID].iFaceSprite == null) {
					iPlayerList[tSeatID].iFaceSprite = new Sprite();
					iPlayerList[tSeatID].iFaceSprite.copyFrom( iFacePak.getSprite(0) );
				}
				iPlayerList[tSeatID].iFaceMotionID = tFaceID;
				iPlayerList[tSeatID].iFaceSprite.resetFrameID();
				iPlayerList[tSeatID].ibFaceEnd = false;
				Tools.debug("face iPlayerList[" + tSeatID + "].iFaceMotionID="
						+ iPlayerList[tSeatID].iFaceMotionID);
				if(iPlayerList[tSeatID].iVipLevel >= 5 && tFaceID == 2) {
					for(int i = 0; i < CGamePlayer.MAX_LIGHTNING_COUNT; i++) {
						if(iPlayerList[tSeatID].lightning[i] == null) {
							iPlayerList[tSeatID].lightning[i] = new Lightning();
						}
						int x = iFacePak.getSprite(0).getX();
						int w = iFacePak.getSprite(0).getFirstFrameWidth() / 2;
						iPlayerList[tSeatID].lightning[i].setStrikePoint((int)(iPlayerList[iChangedSeatID[tSeatID]].iX - w), 
								(int)(iPlayerList[iChangedSeatID[tSeatID]].iY)-(iPlayerList[tSeatID].iBmp.getHeight()>>1));
						iPlayerList[tSeatID].lightning[i].setStrikePoint2((int)(iPlayerList[iChangedSeatID[tSeatID]].iX + w), 
								(int)(iPlayerList[iChangedSeatID[tSeatID]].iY)-(iPlayerList[tSeatID].iBmp.getHeight()>>1));
						iPlayerList[tSeatID].lightning[i].setDetTime(89);
						iPlayerList[tSeatID].lightning[i].setVisible(true);
						iPlayerList[tSeatID].lightning[i].setDsiplacment(80);
						iPlayerList[tSeatID].lightning[i].setMinDsiplacment(8);
					}
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.electric, 0);				
				}
				
			}
			break;
		
		case MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_SHAKE_FIREND:
			{
				MBNotifyShakeFriend notifyShakeFriend = (MBNotifyShakeFriend)msg.obj;
				iPlayerList[notifyShakeFriend.iDesSeatID].iCurrentDrunk = notifyShakeFriend.iCurrentDrunk;
				if(notifyShakeFriend.iDesSeatID == iSelfSeatID) {
					MyArStation.iPlayer.iCurrentDrunk = notifyShakeFriend.iCurrentDrunk;
				}
			}
			break;
		
		case MessageEventID.EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_LIGHTNING: //雷击通知
			{
				//将后台状态置为游戏中
				iServerState = E_TGS_PLAYING;
				//停止计时器动画
				iOvalTimerCount.pause();
				//
				MBNotifyLightning lightning = (MBNotifyLightning)msg.obj;
				iPlayerList[lightning.iChopSeatID].iPlayingState = CGamePlayer.PLAYING_STATE_CHOPING;
				//
//				Tools.debug("Notify Lightning...ChopSeatID = " + lightning.iChopSeatID);
				//
				int bmpH = iPlayerList[lightning.iDesSeatID].iBmp.getHeight();
				iLightStartX = iPlayerList[iChangedSeatID[lightning.iDesSeatID]].iX;
				iLightStartY = 0;//iPlayerList[iChangedSeatID[lightning.iDesSeatID]].iY;
				iLightX = iPlayerList[iChangedSeatID[lightning.iDesSeatID]].iX;
				iLightY = iPlayerList[iChangedSeatID[lightning.iDesSeatID]].iY - (bmpH>>1);
				iLightState = 0;
				iLightningType = lightning.iType;
				iDesLightningID = lightning.iDesSeatID;
				iLightningVip = iPlayerList[lightning.iChopSeatID].iVipLevel;
				ActivityUtil.mAlphaLine.setStrokeWidth(4 + iLightningVip);
				ActivityUtil.mAlphaLineC.setStrokeWidth(10 + (iLightningVip<<1));
				if(iLightningVip > 0) {
					CURLINES = iLightningVip;
				}
				else {
					CURLINES = 1;
				}
				if(CURLINES > MAXLINES) {
					CURLINES = MAXLINES;
				}
				for(int i = 0; i < CURLINES; i++) {
					lightnings[i].setStrikePoint((int)iLightStartX,(int)iLightStartY);
					lightnings[i].setStrikePoint2((int)iLightX, (int)iLightY);
					if(iLightningVip > 0) {
						lightnings[i].setShadowColor(0xffff0000);
					}
					else {
						lightnings[i].setShadowColor(0xff0000ff);
					}
					lightnings[i].setVisible(true);
					lightnings[i].setDetTime(8);
				}
				//
				if(iLightningType > 0) {
					iDoubleTimes <<= 1;
					iFlashBetTick = 16;
					//
					iDoubleSpriteEnd = false;
					iDoubleSprite.resetFrameID();
				}
				//
				iChopSeatID = lightning.iChopSeatID;
				//反劈
				if(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING
						&& iChopSeatID != iSelfSeatID) {
					//if(iRebelChopButton.isVision() == false) {
					if(iLastShoutSeatID == iSelfSeatID) {
						iRebelChopButton.setVision(true);
						iRebelChopButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_SHOW);
						iRebelChopSeatID = -1;
						iWantToShowRebleChopButton = false;
					}
					else {
						iRebelChopSeatID = -1;
						iWantToShowRebleChopButton = true;
						iShowRebelChopButtonDelay = System.currentTimeMillis();
					}
				}
				//抢劈
				iPreChopButton.setVision(false);
				//滚轮
				iWheelVisible = false;
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).setVision(false);
				}
				//喊斋按钮
				iZhaiButton.setVision(false);
				//雷击按钮
				iLightningButton.setVision(false);
				//菜单按钮
				//iMenuButton.setVision(false);
				//聊天按钮
				iChatButton.setVision(true);
				//表情按钮
				iFaceButton.setVision(true);
				//任务按钮
				iGiftBUtton.setVision(true);
				if(MyArStation.iPlayer.iMissionRewardCount > 0) {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				}
				else {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				}
				//
//				System.out.println("StartNotify 5555555555555555555555555");
				//
//				if(iYanHuaSpriteCount < 0) {
				if(iPlayingCount >= PLAY_ROCK_PLAYER_COUNT 
						&& iLastShoutCount >= PLAY_ROCK_DICE_COUNT) {
						MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusic(SoundsResID.bg2, true);
					}
					else {
						int tMusicID = SoundsResID.bg4 + iRoomPair.roomID;
						if(tMusicID > SoundsResID.bg8) {
							tMusicID = SoundsResID.bg4;
						}
						//公海、皇家，特殊时间播放，特殊歌曲
//						if(iRoomPair.roomID == 3 || iRoomPair.roomID == 4) {
//							Calendar c = Calendar.getInstance();
//							int hour = c.get(Calendar.HOUR_OF_DAY);
//							if(hour >= 2 && hour <= 5) {
//								tMusicID = SoundsResID.bg9;
//							}
//						}
						System.out.println("tMusicID = " + tMusicID);
						MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusicSeekTo(tMusicID, true, iMusicPos);
					}
//				}
				//劈
				MediaManager.getMediaManagerInstance(iPlayerList[iChopSeatID].iModelID).playerSound(SoundsResID.pi, 0);				
			}
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_SHOW_STORE://显示商城
//			if(stroeDialog == null) {
//				stroeDialog = new StroeDialog(GameMain.mGameMain);
//				stroeDialog.setOnCloseListen(new StoreDialogClose() {
//					
//					@Override
//					public void onStoreDialogClose() {
//						stroeDialog = null;
//					}
//				});
//			}
//			stroeDialog.show();
			break;
		default:
//			if(stroeDialog!= null) {
//				stroeDialog.handleMessage(msg);
//			}
			break;
		}
	}
	
	
	public boolean ItemAction(int aEventID) {
		quickSelectRoomScreen.ItemAction(aEventID);
		infoPopView.ItemAction(aEventID);
		switch (aEventID) {
		case GameEventID.ESPRITEBUTTON_EVNET_WHEEL_SHOUT: 	//喊
			{
				//
				if(iZhaiButton.isVision() == true) {
					iSelectShoutOne = (short)(iZhaiButton.getSelected() == true ? 1 : 0);
				}
				if(ibShoutOne) {
					iSelectShoutOne = 1;
				}
				//如果图标按钮，弹出来了
				if(iIconButtonShow == true) {
					messageEvent(GameEventID.ESPRITEBUTTON_EVENT_HIDE_SYSTEM_BUTTON);
				}
				//
				iWheelVisible = false;
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					SpriteButton pSB = ((SpriteButton)iSpriteButtonList.elementAt(i));
					pSB.setVision(false);
				}
				//喊斋按钮
				iZhaiButton.setVision(false);
				//暂停倒计时
				iOvalTimerCount.pause();
				//菜单按钮
				//iMenuButton.setVision(false);
				//聊天按钮
				iChatButton.setVision(true);
				//表情按钮
				iFaceButton.setVision(true);
				//任务按钮
				iGiftBUtton.setVision(true);
				if(MyArStation.iPlayer.iMissionRewardCount > 0) {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				}
				else {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				}
				System.out.println("test iSelectShoutOne = " + iSelectShoutOne);
				//发送喊骰请求
				requestShout(iWheelCurrentDice, iWheelCurrentNum, iSelectShoutOne);
				int delay = iOvalTimerCount.getDelay();
				if(delay >= iCurrentMaxDelayTime) {
					iDelayCount++;
				}
				else {
					if(iDelayCount > 0) {
						iDelayCount--;
					}
				}
				iRelDelay = 0;
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_WHEEL_CHOP:	//劈
			{
				//如果图标按钮，弹出来了
				if(iIconButtonShow == true) {
					messageEvent(GameEventID.ESPRITEBUTTON_EVENT_HIDE_SYSTEM_BUTTON);
				}
				//
				iWheelVisible = false;
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).setVision(false);
				}
				//喊斋按钮
				iZhaiButton.setVision(false);
				//暂停倒计时
				iOvalTimerCount.pause();
				//菜单按钮
				//iMenuButton.setVision(false);
				//聊天按钮
				iChatButton.setVision(true);
				//表情按钮
				iFaceButton.setVision(true);
				//任务按钮
				iGiftBUtton.setVision(true);
				if(MyArStation.iPlayer.iMissionRewardCount > 0) {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				}
				else {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				}
				//发送劈请求
				requestChop();
			}
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_BUTTON_PRECHOP: //抢劈
			{
				//如果图标按钮，弹出来了
				if(iIconButtonShow == true) {
					messageEvent(GameEventID.ESPRITEBUTTON_EVENT_HIDE_SYSTEM_BUTTON);
				}
				//抢劈
				iPreChopButton.setVision(false);
				//
				requestUseItem(MyArStation.iPlayer.E_RTI_VIE_CHOP, 0);
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_BUTTON_CHAT: //聊天
			{
				messageEvent(GameEventID.ESPRITEBUTTON_EVENT_BUTTON_CHAT);
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_BUTTON_MENU: //菜单
			{
				//messageEvent(GameEventID.ESPRITEBUTTON_EVENT_BUTTON_MENU);
				if(iPlayerList[iSelfSeatID].iGameState != CGamePlayer.PLAYER_STATE_PLAYING) {
					quickSelectRoomScreen.show();
				}
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_BUTTON_FACE: //表情
			{
				messageEvent(GameEventID.ESPRITEBUTTON_EVENT_BUTTON_FACE);
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_BUTTON_REBELCHOP: //反劈
			{
				//如果图标按钮，弹出来了
				if(iIconButtonShow == true) {
					messageEvent(GameEventID.ESPRITEBUTTON_EVENT_HIDE_SYSTEM_BUTTON);
				}
				//抢劈
				iRebelChopButton.setVision(false);
				//
				requestUseItem(MyArStation.iPlayer.E_RIT_REBEL_CHOP, 0);
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_BUTTON_LIGHTNING:	//雷击
			{
				//如果图标按钮，弹出来了
				if(iIconButtonShow == true) {
					messageEvent(GameEventID.ESPRITEBUTTON_EVENT_HIDE_SYSTEM_BUTTON);
				}
				//
				iWheelVisible = false;
				for(int i=0; i<iSpriteButtonList.size(); i++) {
					((SpriteButton)iSpriteButtonList.elementAt(i)).setVision(false);
				}
				//喊斋按钮
				iZhaiButton.setVision(false);
				//雷击按钮
				iLightningButton.setVision(false);
				//暂停倒计时
				iOvalTimerCount.pause();
				//菜单按钮
				//iMenuButton.setVision(false);
				//聊天按钮
				iChatButton.setVision(true);
				//表情按钮
				iFaceButton.setVision(true);
				//任务按钮
				iGiftBUtton.setVision(true);
				if(MyArStation.iPlayer.iMissionRewardCount > 0) {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				}
				else {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				}
				//发送劈请求
				requestLightning();
			}
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_BUTTON_MISSION:	//任务
		{
			if(iGiftBUtton != null) {
				iGiftBUtton.setVision(true);
				if(MyArStation.iPlayer.iMissionRewardCount > 0) {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
				}
				else {
					iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				}
			}
			MyArStation.iPlayer.iMissionRewardCount = 0;
			if(missionView != null) missionView.show();
		}
		break;
			
		default:
			{
//				System.out.println("1111111111111111111111");
				//如果图标按钮，弹出来了
				if(iIconButtonShow == true) {
					if(iOutStartCount == iOutCompleteCount) {
						messageEvent(GameEventID.ESPRITEBUTTON_EVENT_HIDE_SYSTEM_BUTTON);
					}
					return true;
				}
//				System.out.println("2222222222222222222222");
				int seatID = aEventID - GameEventID.ESPRITEBUTTON_EVENT_PLAYER;
				if(seatID < 0 || seatID >= MAX_GAME_PLAYER) {
					break;
				}
//				System.out.println("3333333333333333333333");
				if(iIconButtonShow == false) {
					iIconButtonShow = true;
					//
					iSelectSeatID = seatID;
					//
					LayoutParams lp = (LayoutParams)iAbLayout.getLayoutParams();
					lp.leftMargin = iPlayerList[iChangedSeatID[seatID]].iX - (iLyW >> 1);
					lp.topMargin = iPlayerList[iChangedSeatID[seatID]].iY
								- (iPlayerList[iSelfSeatID].iBmp.getHeight()>>1) 
								- (iLyH >> 1);
					iAbLayout.setLayoutParams(lp);
					//
					messageEvent(GameEventID.ESPRITEBUTTON_EVENT_SHOW_SYSTEM_BUTTON);
				}
//				System.out.println("4444444444444444444444444444");
			}
			break;
		}
//		System.out.println("5555555555555555555555555");
		return true;
	}
	
	private void loadUIPak() {
		iUIPak = CPakManager.loadDeskPak();
		if (iUIPak != null) {
//			iSceneLoading = iUIPak.getScene(0);
//			if(iSceneLoading != null) {
//				iSceneLoading.setViewWindow(0, 0, 800, 480);
//				iSceneLoading.initDrawList();
//			}
			iSceneDesk = iUIPak.getScene(4);
			if(iSceneDesk != null) {
				iSceneDesk.setViewWindow(0, 0, 800, 480);
				iSceneDesk.initDrawList();
				//
				iDrunkSprite = iUIPak.getSprite(35);	//酒杯动画
				iShoutSprite = iUIPak.getSprite(48);	//喊点信息框动画
				iBigCupSprite = iUIPak.getSprite(49);	//大骰盅动画
				iChopSprite = iUIPak.getSprite(50); //劈动画
				iRebelChopSprite = iUIPak.getSprite(71); //反劈动画
				iNumSprite = iUIPak.getSprite(52);//数字动画
				iBigNumSprite = iUIPak.getSprite(53); //大数字动画
				iXSprite = iUIPak.getSprite(54); //"X"动画
				iGbSprite = iUIPak.getSprite(55); //金币动画
				iDrinkSprite = iUIPak.getSprite(56);//喝酒动画
				iWinnerSprite = iUIPak.getSprite(57);//灌倒人动画
				iStartSprite = iUIPak.getSprite(63);//星星动画
				iHartSprite = iUIPak.getSprite(62);	//飞心动画
				iBeDingSprite = iUIPak.getSprite(59); //被顶动画
				iLoveSprite = iUIPak.getSprite(60);	//爱心动画
				iLevelUpSprite = iUIPak.getSprite(58); //升级动画
				iDoubleSprite = iUIPak.getSprite(72);//加倍动画
				for(int i=0; i<CGamePlayer.MAX_DICE_COUNT; i++) {
					iBrightDiceSprite[i].copyFrom(iUIPak.getSprite(51));
				}
				//
				Map pMap = iSceneDesk.getLayoutMap(Scene.SpriteLayout);
				Vector<?> pSpriteList = pMap.getSpriteList();
				Sprite pSprite = null;
				for (int i=0; i<pSpriteList.size(); i++) {
					pSprite = (Sprite)pSpriteList.elementAt(i);
					int seatID = pSprite.getIndex() - 42;
					if(seatID < 0 || seatID >= MAX_GAME_PLAYER) {
						continue;
					}
					iPlayerList[seatID].iX = pSprite.getX();
					iPlayerList[seatID].iY = pSprite.getY();
					iPlayerList[seatID].iSprite = pSprite;
					iPlayerList[seatID].iCupSprite.copyFrom(iBigCupSprite);
					//精灵按钮
					int pX = pSprite.getX();
					int pY = pSprite.getY();
					int pEventID = pSprite.getIndex() - 42 + GameEventID.ESPRITEBUTTON_EVENT_PLAYER;
					SpriteButton pSpriteButton;
					pSpriteButton = new SpriteButton(pSprite);
					pSpriteButton.setEventID(pEventID);
					pSpriteButton.setPosition(pX, pY);
					pSpriteButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
					pSpriteButton.setHandler(this);
//					pSpriteButton.setVision(false);
					iPlayerButtonList.addElement(pSpriteButton);
				}
				//求桌子的中点坐标
				int tempCenX = iPlayerList[0].iX;
				int tempCenY = (iPlayerList[0].iY + iPlayerList[3].iY) >> 1;//ActivityUtil.SCREEN_HEIGHT >> 1;
				int tempH = iPlayerList[0].iY - iPlayerList[3].iY;
				//求各个玩家骰盅的坐标
				iPlayerList[0].iCupX = tempCenX;
				iPlayerList[0].iCupY = tempCenY + (tempH >> 3);
//				System.out.println("init seatID=0" + " cupX=" + iPlayerList[0].iCupX + " cupY=" + iPlayerList[0].iCupY);
				
				iPlayerList[1].iCupX = tempCenX - (ActivityUtil.SCREEN_WIDTH >> 3) - (ActivityUtil.SCREEN_WIDTH >> 5);
				iPlayerList[1].iCupY = tempCenY;
//				System.out.println("init seatID=1" + " cupX=" + iPlayerList[1].iCupX + " cupY=" + iPlayerList[1].iCupY);
				
				iPlayerList[2].iCupX = tempCenX - (ActivityUtil.SCREEN_WIDTH >> 3) - (ActivityUtil.SCREEN_WIDTH >> 5);
				iPlayerList[2].iCupY = tempCenY - (tempH >> 2);
//				System.out.println("init seatID=2" + " cupX=" + iPlayerList[2].iCupX + " cupY=" + iPlayerList[2].iCupY);
				
				iPlayerList[3].iCupX = tempCenX;
				iPlayerList[3].iCupY = tempCenY - (tempH >> 2) - (tempH >> 4);
//				System.out.println("init seatID=3" + " cupX=" + iPlayerList[3].iCupX + " cupY=" + iPlayerList[3].iCupY);
				
				iPlayerList[4].iCupX = tempCenX + (ActivityUtil.SCREEN_WIDTH >> 3) + (ActivityUtil.SCREEN_WIDTH >> 5);
				iPlayerList[4].iCupY = tempCenY - (tempH >> 2);
//				System.out.println("init seatID=4" + " cupX=" + iPlayerList[4].iCupX + " cupY=" + iPlayerList[4].iCupY);
				
				iPlayerList[5].iCupX = tempCenX + (ActivityUtil.SCREEN_WIDTH >> 3) + (ActivityUtil.SCREEN_WIDTH >> 5);
				iPlayerList[5].iCupY = tempCenY;
//				System.out.println("init seatID=5" + " cupX=" + iPlayerList[5].iCupX + " cupY=" + iPlayerList[5].iCupY);
			}
			//状态栏
			iSceneBar = iUIPak.getScene(2);
			if(iSceneBar != null) {
				iSceneBar.setViewWindow(0, 0, 800, 480);
				iSceneBar.initDrawList();
				iSceneBar.initNinePatchList(new int[] {R.drawable.back04, R.drawable.new_text_bg3, R.drawable.new_text_bg3});
			}
			//滚轮
			iSceneWheel = iUIPak.getScene(3);
			if(iSceneWheel != null) {
				iSceneWheel.setViewWindow(0, 0, 800, 480);
				iSceneWheel.initDrawList();
				Map pMap = iSceneWheel.getLayoutMap(Scene.SpriteLayout);
				Vector<?> pSpriteList = pMap.getSpriteList();
				Sprite pSprite = null;
				for (int i=0; i<pSpriteList.size(); i++) {
					pSprite = (Sprite)pSpriteList.elementAt(i);
					int pX = pSprite.getX();
					int pY = pSprite.getY();
					int pEventID = pSprite.getIndex() - 38 + GameEventID.ESPRITEBUTTON_EVNET_WHEEL_SHOUT;
					//精灵按钮
					SpriteButton pSpriteButton;
					pSpriteButton = new SpriteButton(pSprite);
					pSpriteButton.setEventID(pEventID);
					pSpriteButton.setPosition(pX, pY);
					pSpriteButton.setHandler(this);
					pSpriteButton.setVision(false);
					iSpriteButtonList.addElement(pSpriteButton);
				}
				//
				pSpriteList = iSceneWheel.getLayoutMap(Scene.BuildLayout).getSpriteList();
				if(pSpriteList.size() > 1) {
					CBuilding building = ((CBuilding)pSpriteList.elementAt(0));
					pSprite = building.getSprite();
					iWheelNumRect.set(building.getX() + pSprite.getSpriteLeft(),
							building.getY() + pSprite.getSpriteTop(),
							building.getX() + pSprite.getSpriteLeft() + pSprite.getSpriteWidth(),
							building.getY() + pSprite.getSpriteTop() + pSprite.getSpriteHeight());
//					System.out.println("loadpak iWheelNumRect = " + iWheelNumRect.toString());
					building = ((CBuilding)pSpriteList.elementAt(1));
					pSprite = building.getSprite();
					iWheelDiceRect.set(building.getX() + pSprite.getSpriteLeft(),
							building.getY() + pSprite.getSpriteTop(),
							building.getX() + pSprite.getSpriteLeft() + pSprite.getSpriteWidth(),
							building.getY() + pSprite.getSpriteTop() + pSprite.getSpriteHeight());
//					System.out.println("loadpak iWheelDiceRect = " + iWheelDiceRect.toString());
				}
				pSprite = null;
				pSpriteList = null;
			}
			//其它按钮，抢劈、聊天、菜单
			iSceneOther = iUIPak.getScene(5);
			if(iSceneOther != null) {
				iSceneOther.setViewWindow(0, 0, 800, 480);
				iSceneOther.initDrawList();
				Map pMap = iSceneOther.getLayoutMap(Scene.SpriteLayout);
				Vector<?> pSpriteListOther = pMap.getSpriteList();
				Sprite pSprite = null;
				for (int i=0; i<pSpriteListOther.size(); i++) {
					pSprite = (Sprite)pSpriteListOther.elementAt(i);
					int pX = pSprite.getX();
					int pY = pSprite.getY();
//					int pEventID = pSprite.getIndex() - 38 + GameEventID.ESPRITEBUTTON_EVNET_WHEEL_SHOUT;
					//精灵按钮
					SpriteButton pSpriteButton;
					pSpriteButton = new SpriteButton(pSprite);
//					pSpriteButton.setEventID(pEventID);
					pSpriteButton.setPosition(pX, pY);
					pSpriteButton.setHandler(this);
					pSpriteButton.setVision(false);
//					iOtherButtonList.addElement(pSpriteButton);
					if(pSprite.getIndex() == 40) { //抢劈按钮
						pSpriteButton.setEventID(GameEventID.ESPRITEBUTTON_EVENT_BUTTON_PRECHOP);
						iPreChopButton = pSpriteButton;
					}
					else if(pSprite.getIndex() == 41) { //聊天按钮
						pSpriteButton.setEventID(GameEventID.ESPRITEBUTTON_EVENT_BUTTON_CHAT);
						iChatButton = pSpriteButton;
						iChatButton.setVision(true);
					}
					else if(pSprite.getIndex() == 61) {	//菜单按钮
						pSpriteButton.setEventID(GameEventID.ESPRITEBUTTON_EVENT_BUTTON_MENU);
						iMenuButton = pSpriteButton;
						iMenuButton.setVision(true);
						//改变菜单动画
						//iMenuButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
					}
					else if(pSprite.getIndex() == 69) {	//表情按钮
						pSpriteButton.setEventID(GameEventID.ESPRITEBUTTON_EVENT_BUTTON_FACE);
						iFaceButton = pSpriteButton;
						iFaceButton.setVision(true);
					}
					else if(pSprite.getIndex() == 65) {	//任务按钮
						pSpriteButton.setEventID(GameEventID.ESPRITEBUTTON_EVENT_BUTTON_MISSION);
						iGiftBUtton = pSpriteButton;
						//任务按钮
						iGiftBUtton.setVision(true);
						if(MyArStation.iPlayer.iMissionRewardCount > 0) {
							iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
						}
						else {
							iGiftBUtton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
						}
					}
					else if(pSprite.getIndex() == 70) { //反劈按钮
						pSpriteButton.setEventID(GameEventID.ESPRITEBUTTON_EVENT_BUTTON_REBELCHOP);
						iRebelChopButton = pSpriteButton;
						iRebelChopButton.setAutoDisappear(true);
					}
				}
			}
			
			//经验条
			iSceneExp = iUIPak.getScene(1);
			if(iSceneExp != null) {
				iSceneExp.setViewWindow(0, 0, 800, 480);
				iSceneExp.initDrawList();
				iSceneExp.initNinePatchList(new int[] {R.drawable.gdbj});				
			}
			
			//桌子Pak
//			iDeskPak = GameMain.iPakManager.loadDeskPak();
			iSceneDeskEx = iUIPak.getScene(6);
			if(iSceneDeskEx != null) {
				iSceneDeskEx.setViewWindow(0, 0, 800, 480);
				iSceneDeskEx.initDrawList();
				//iSceneDeskEx.initNinePatchList(new int[] {R.drawable.back04, R.drawable.new_text_bg, R.drawable.new_text_bg});
			}
		}
		
		//表情Pak
		iFacePak = MyArStation.iPakManager.loadFacePak();
		
		//道具动画
		iItemPak = new Project[MAX_ITEM_SPRITE_TYPE];
		for(int i=0; i<MAX_ITEM_SPRITE_TYPE; i++) {
			iItemPak[i] = MyArStation.iPakManager.loadItemPak(i);
			iItemSprite[i].copyFrom(iItemPak[i].getSprite(0));
		}
		
		//唯色加1
		iWeiSePak = MyArStation.iPakManager.loadWeiSePak();
		iWeiSeSprite = iWeiSePak.getSprite(0);
		
		//劈空
		iPiKongPak = MyArStation.iPakManager.loadPiKongPak();
		iPiKongSprite = iPiKongPak.getSprite(0);;
		
		//喊斋技能按钮
		iZhaiPak = MyArStation.iPakManager.loadZhaiPak();
		Sprite tZaiSprite = iZhaiPak.getSprite(0);
		iZhaiButton = new SpriteButtonSelect(tZaiSprite);
		iZhaiButton.setEventID(GameEventID.ESPRITEBUTTON_EVENT_BUTTON_ZHAI);
		iZhaiButton.setPosition((int)(492*ActivityUtil.PAK_ZOOM_X), (int)(368*ActivityUtil.PAK_ZOOM_Y));
		iZhaiButton.setHandler(this);
		iZhaiButton.setVision(false);
		//雷击技能按钮
		Sprite tLightningSprite = iZhaiPak.getSprite(1);
		iLightningButton = new SpriteButton(tLightningSprite);
		iLightningButton.setEventID(GameEventID.ESPRITEBUTTON_EVENT_BUTTON_LIGHTNING);
		iLightningButton.setPosition((int)(418*ActivityUtil.PAK_ZOOM_X), (int)(368*ActivityUtil.PAK_ZOOM_Y));
		iLightningButton.setHandler(this);
		iLightningButton.setVision(false);
		//雷击光效
		iLightningPak = MyArStation.iPakManager.loadLightningPak();
		iLightningExp = iLightningPak.getSprite(0); //雷击爆炸
		iNextLightningExp = new Sprite();
		iNextLightningExp.copyFrom(iLightningExp);
	}
	
	private void messageEvent(int aEventID, int aArg1, int aArg2) {
		Message tMes = new Message();
		tMes.what = aEventID;
		tMes.arg1 = aArg1;
		tMes.arg2 = aArg2;
		MyArStation.getInstance().iHandler.sendMessage(tMes);
	}
	
	private void showReturnLoginConfirm() {
		if(iGameState == GAME_STATE_LEAVEROOM)  {
			return;
		}
		String infoStr = MyArStation.mGameMain.getString(R.string.Return_HomeScreen_tip_Content);
		//if(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING) {
		if(!iMenuButton.isVision()) {
			infoStr = MyArStation.mGameMain.getString(
					R.string.Return_HomeScreen_tip_Content_Warning, iWinePrice);
		}
//		if(dialog == null) {
			dialog = new Dialog(MyArStation.mGameMain, R.style.MSGDialogStyle);
			dialog.setContentView(R.layout.new_dialog);
			dialog.setCancelable(true);
			TextView title = (TextView)dialog.findViewById(R.id.dialog_title);
			title.setText(R.string.Return_HomeScreen_tip_title);
			TextView content = (TextView)dialog.findViewById(R.id.dialog_message);
			content.setText(infoStr);
			Button commit = (Button)dialog.findViewById(R.id.ok);
			commit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					if(iShakeListener != null) {
						iShakeListener.stopShake();
					}
					//
					iGameState = GAME_STATE_LEAVEROOM;
					//
					requestLeaveRoom();
					dialog.dismiss();
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
//		}
//		//根据自己的状态，给出相应提示
//		dialog = new AlertDialog.Builder(GameMain.mGameMain).setTitle(R.string.Return_HomeScreen_tip_title)
//				.setMessage(infoStr).setCancelable(false)
//				.setPositiveButton(R.string.Return_LoginScreen_Ok, new DialogInterface.OnClickListener()
//				{
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
////								hideSysCom();
////								messageEvent(MessageEventID.EMESSAGE_EVENT_TO_LOGIN);
//								MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
//								//
//								if(iShakeListener != null) {
//									iShakeListener.stopShake();
//								}
//								//
//								iGameState = GAME_STATE_LEAVEROOM;
//								//
//								requestLeaveRoom();
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
	
	//登录房间请求
	private void requestLoginRoom(RoomData data) {
		iLoadingTick = 0;
		MyArStation.iGameProtocol.RequestLoginRoomB(data.roomID, data.tableID);
	}
	
	//退出房间请求
	private void requestLeaveRoom() {
		iLoadingTick = 0;
		MyArStation.iGameProtocol.requsetLeaveRoom(MyArStation.iPlayer.iUserID);
	}

	//发送准备请求
	private void requestReady() {
		MyArStation.iGameProtocol.RequsetReady(MyArStation.iPlayer.iUserID);
	}
	
	//发送喊骰请求
	private void requestShout(int aShoutDice, int aShoutCount, short aShoutOne) {
		MyArStation.iGameProtocol.RequsetShout(MyArStation.iPlayer.iUserID, aShoutDice, aShoutCount, aShoutOne);
	}
	
	//发送劈请求
	private void requestChop() {
		MyArStation.iGameProtocol.requsetchop(MyArStation.iPlayer.iUserID);
	}
	
	//发送雷击请求
	private void requestLightning() {
		MyArStation.iGameProtocol.requestLighting(MyArStation.iPlayer.iUserID);
	}
	
	// 发送踢人请求
	private void requestKick(int aUserID) {
		MyArStation.iGameProtocol.requestKick(aUserID);
	}
	
	//发送加好友请求
	private void requestAddFriend(int aFriendUserID, int aAction) {
		MyArStation.iGameProtocol.requestAddFriend(aFriendUserID, aAction);
	}
	
	//发送顶好友请求
	private void requestDing(int aDesUserID) {
//		System.out.println("Ding DesUserID=" + aDesUserID);
		MyArStation.iGameProtocol.requestDing(aDesUserID);
	}
	
	//发送使用道具的请求
	private void requestUseItem(int aItemID, int aDesUserID) {
		Vector<Integer> aDesUserIDList = new Vector<Integer>();
		aDesUserIDList.add(aDesUserID);
		MyArStation.iGameProtocol.RequestUseItem(aItemID, aDesUserIDList);
		aDesUserIDList = null;
	}
	
	@Override
	public void TimeOut() {
		if(iCurrentShoutSeatID != iSelfSeatID) {
			return;
		}
//		int shoutCount = iPlayingCount;
//		int shoutDice = 2;
//		//如果喊了
//		if(iLastShoutCount > 1) {
//			shoutCount = iLastShoutCount + 1;
//			//如果大于最大值
//			if(shoutCount > iPlayingCount * 5) {
//				shoutCount = iLastShoutCount;
//				shoutDice = iLastShoutDice;
//				if(shoutDice == 1) {
//					//发送劈请求
//					requestChop();
//					return;
//				}
//				else if(shoutDice == 6) {
//					shoutDice = 1;
//				}
//				else {
//					shoutDice ++;
//				}
//			}
//			else {
//				shoutDice = iLastShoutDice;
//			}
//		}
		//
		iCurrentMaxDelayTime = iOvalTimerCount.getDelay();
		System.out.println("iCurrentMaxDelayTime " + iCurrentMaxDelayTime);
		iDelayCount++;
		iRelDelay++;
		if(iZhaiButton.isVision() == true) {
			iSelectShoutOne = (short)(iZhaiButton.getSelected() == true ? 1 : 0);
		}
		if(ibShoutOne) {
			iSelectShoutOne = 1;
		}
		//
		iWheelVisible = false;
		for(int i=0; i<iSpriteButtonList.size(); i++) {
			SpriteButton pSB = ((SpriteButton)iSpriteButtonList.elementAt(i));
			pSB.setVision(false);
		}
		//喊斋按钮
		iZhaiButton.setVision(false);
		//雷击按钮
		iLightningButton.setVision(false);
		//如果大于最大值
		if(iWheelCurrentNum >= iPlayingCount * 6) {
			if(iWheelCurrentDice == 1) {
				//发送劈请求
				requestChop();
				return;
			}
		}
		//发送喊骰请求
		requestShout(iWheelCurrentDice, iWheelCurrentNum, iSelectShoutOne);
	}

//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		System.out.println("onclick 00000000000000000 vID=" + v.getId());
//		switch(v.getId()) {
//		case ICON_BUTTON_ID_INFO:		//信息
//			{
//				System.out.println("onclick 1111111111111111111");
//			}
//			break;
//		
//		case ICON_BUTTPN_ID_FRIEND:		//加好友
//			{
//				System.out.println("onclick 22222222222222222");
//			}
//			break;
//			
//		case ICON_BUTTPN_ID_KICKOFF:	//踢人
//			{
//				System.out.println("onclick 333333333333333");
//			}
//			break;
//			
//		case ICON_BUTTPN_ID_CHOP:		//闪电劈
//			{
//				System.out.println("onclick 444444444444444444");
//			}
//			break;
//			
//		case ICON_BUTTPN_ID_KILLORDER:	//追杀令
//			{
//				System.out.println("onclick 555555555555555555");
//			}
//			break;
//			
//		case ICON_BUTTPN_ID_UNARM:		//禁劈
//			{
//				System.out.println("onclick 666666666666666666666");
//			}
//			break;
//			
//		case ICON_BUTTPN_ID_HART:		//顶
//			{
//				System.out.println("onclick 7777777777777777777");
//			}
//			break;
//						
//		default:
//			{
//				System.out.println("onclick 88888888888888888");
//				startInAnimation();
//			}
//			break;
//		}
//	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(iIconButtonShow == false || 
				iOutCompleteCount < iOutStartCount) {
			return false;
		}
		
		int tX = (int)(event.getX());
		int tY = (int)(event.getY());
//		System.out.println("onTouch 00000000000000000 vID=" + v.getId()
//				+ " tX=" + tX
//				+ " tY=" + tY);
//		System.out.println("onclick onTouch 00000000000000000 vID=" + v.getId());
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			if(v.getId() < ICON_LAYOUT_ID) {
				//
				scaleLittleIconButton();
				//
				Button button = (Button)v;
				LayoutParams lp = (LayoutParams)button.getLayoutParams();
				lp.leftMargin -= (iIconButtonW);
				lp.topMargin -= (iIconButtonH - (iIconButtonH >> 3));
				lp.width = iIconButtonW << 1;
				lp.height = iIconButtonH << 1;
				button.setLayoutParams(lp);
				//
				iAbLayout.removeViewInLayout(v);
				iAbLayout.addView(v);
				//
//				System.out.println("onTouch button 00000000000000000 buttonID=" + v.getId());
			}
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE) {
			if(v.getId() == ICON_LAYOUT_ID) {
				if(ibSelectBt == true) {
					Button button = mList.get(iSelectBtID);
					LayoutParams lp = (LayoutParams)button.getLayoutParams();
					Rect pRect = new Rect();
					pRect.set(lp.leftMargin, lp.topMargin, lp.leftMargin+lp.width, lp.topMargin+lp.height);
					if(pRect.contains(tX, tY)) {
						//
//						System.out.println("onTouch button ok! buttonID = " + button.getId());
					}
					else {
						ibSelectBt = false;
						iSelectBtID = 0;
						//
//						System.out.println("onTouch button gone! buttonID = " + button.getId());
					}
					pRect = null;
				}
				else {
					for (int i = 0; i < mList.size(); i ++) {
						Button button = mList.get(i);
						if(button.getVisibility() == View.VISIBLE) {
							LayoutParams lp = (LayoutParams)button.getLayoutParams();
							int temDis = (tX-lp.leftMargin)*(tX-lp.leftMargin) + (tY-lp.topMargin)*(tY-lp.topMargin);
//							System.out.println("onTouch layout move buttonId=" + button.getId() + " temDis=" + temDis);
							if(temDis < 2300) {
								ibSelectBt = true;
								iSelectBtID = button.getId();
								break;
							}
						}	
					}
					if(ibSelectBt == true) {
						//
						scaleLittleIconButton();
						//
						Button button = mList.get(iSelectBtID);
						LayoutParams lp = (LayoutParams)button.getLayoutParams();
						lp.leftMargin -= (iIconButtonW);
						lp.topMargin -= (iIconButtonH - (iIconButtonH >> 3));
						lp.width = iIconButtonW << 1;
						lp.height = iIconButtonH << 1;
						button.setLayoutParams(lp);
						
						//
						iAbLayout.removeViewInLayout(button);
						iAbLayout.addView(button);
						//
//						System.out.println("onTouch select buttonId=" + button.getId());
					}
				}
			}
			else {
				Button button = (Button)v;
//				System.out.println("onTouch button move buttonId=" + button.getId());
			}
		}
		else if(event.getAction() == MotionEvent.ACTION_UP) {
			if(v.getId() < ICON_LAYOUT_ID) {
				//
				Rect pRect = new Rect();
				Button button = (Button)v;
				LayoutParams lp = (LayoutParams)button.getLayoutParams();
				pRect.set(0, 0, lp.width, lp.height);
	//			System.out.println("onTouch tX=" + tX + " tY=" + tY 
	//					+ " layX=" + tLp.x + " layY=" + tLp.y
	//					+ " Rect=" + pRect.toString());
				//
				if(pRect.contains(tX, tY)) {
					//
//					System.out.println("onTouch event go go go!");
					//
					messageEvent(GameEventID.ESPRITEBUTTON_EVENT_HIDE_SYSTEM_BUTTON);
					//
					handleIconButtonEvent(button.getId());
				}
				pRect = null;
				//
				resetIconButton();
			}
			else if(v.getId() == ICON_LAYOUT_ID) {
				if(ibSelectBt == true) {
					Button button = mList.get(iSelectBtID);
					LayoutParams lp = (LayoutParams)button.getLayoutParams();
					Rect pRect = new Rect();
					pRect.set(lp.leftMargin, lp.topMargin, lp.leftMargin+lp.width, lp.topMargin+lp.height);
					if(pRect.contains(tX, tY)) {
						//
//						System.out.println("onTouch buttonID=" + button.getId() + " event go go go!");
						//
						messageEvent(GameEventID.ESPRITEBUTTON_EVENT_HIDE_SYSTEM_BUTTON);
						//
						handleIconButtonEvent(button.getId());
					}
					pRect = null;
				}
				//
				iSelectBtID = 0;
				ibSelectBt = false;
				//
				resetIconButton();
				//
//				System.out.println("onTouch layout UP!");
			}
		}
		return true;
	}
	
	private void enableIconButton() {
		Button pButton = null;
		//如果选择了自己
		if(iSelectSeatID == iSelfSeatID) {
			//改变骰子
//			if(iServerState == E_TGS_PLAYING) {
//				if(GameMain.iPlayer.iItemCounts[GameMain.iPlayer.E_RTI_CHANGE_DICE] > 0) {
					pButton = mList.get(ICON_BUTTON_ID_CHANGEDICE);
					pButton.setVisibility(View.VISIBLE);
//				}
//			}
			//信息
			pButton = mList.get(ICON_BUTTON_ID_INFO);
			pButton.setVisibility(View.VISIBLE);
			//反弹盾
//			if(GameMain.iPlayer.iItemCounts[GameMain.iPlayer.E_RIT_SHIELD] > 0
//					&& iPlayerList[iSelfSeatID].ibShield == false) {
//				pButton = mList.get(ICON_BUTTON_ID_SHIELD);
//				pButton.setVisibility(View.VISIBLE);
//			}
			return;
		}
		
		//闪电劈
//		if(GameMain.iPlayer.iItemCounts[GameMain.iPlayer.R_RTI_MANY_CHOP] > 0) {
//			pButton = mList.get(ICON_BUTTON_ID_CHOP);
//			pButton.setVisibility(View.VISIBLE);
//		}
		//加好友
//		if(GameMain.iPlayer.isMyFrient(iPlayerList[iSelectSeatID].iUserID) == false) {
			pButton = mList.get(ICON_BUTTON_ID_FRIEND);
			pButton.setVisibility(View.VISIBLE);
//		}
		//心
		pButton = mList.get(ICON_BUTTON_ID_HART);
		pButton.setVisibility(View.VISIBLE);
		//信息
		pButton = mList.get(ICON_BUTTON_ID_INFO);
		pButton.setVisibility(View.VISIBLE);
		//追杀令
//		if(GameMain.iPlayer.iItemCounts[GameMain.iPlayer.E_RIT_KILL_ORDER] > 0) {
//			pButton = mList.get(ICON_BUTTON_ID_KILLORDER);
//			pButton.setVisibility(View.VISIBLE);
//		}
		//踢人
		pButton = mList.get(ICON_BUTTON_ID_KICKOFF);
		pButton.setVisibility(View.VISIBLE);
		//禁劈
//		if(GameMain.iPlayer.iItemCounts[GameMain.iPlayer.E_RIT_KILL_ORDER] > 0) {
//			pButton = mList.get(ICON_BUTTON_ID_UNARM);
//			pButton.setVisibility(View.VISIBLE);
//		}
		//蛋糕
		pButton = mList.get(ICON_BUTTON_ID_CAKE);
		pButton.setVisibility(View.VISIBLE);
		//啤酒瓶
		pButton = mList.get(ICON_BUTTON_ID_BOTTLE);
		pButton.setVisibility(View.VISIBLE);
	}
	
	private void resetIconButton() {
		for (int i = 0; i < mList.size(); i ++) {
			Button button = mList.get(i);
			if(button.getVisibility() == View.VISIBLE) {
				//复位坐标
				LayoutParams lp = (LayoutParams)button.getLayoutParams();
				lp.width = iIconButtonW;
				lp.height = iIconButtonH;
				lp.leftMargin = mButtonEndX[mButtonToAnim[button.getId()]];
				lp.topMargin = mButtonEndY[mButtonToAnim[button.getId()]];
				button.setLayoutParams(lp);
			}
		}
	}
	
	private void scaleLittleIconButton() {
		for (int i = 0; i < mList.size(); i ++) {
			Button button = mList.get(i);
			if(button.getVisibility() == View.VISIBLE) {
				//复位坐标
				LayoutParams lp = (LayoutParams)button.getLayoutParams();
				lp.width = iIconButtonW >> 1;
				lp.height = iIconButtonH >> 1;
				lp.leftMargin = mButtonEndX[mButtonToAnim[button.getId()]] + (lp.width>>1);
				lp.topMargin = mButtonEndY[mButtonToAnim[button.getId()]] + (lp.height>>1);
				button.setLayoutParams(lp);
			}
		}
	}
	
	private void handleIconButtonEvent(int aButtonID) {
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
		switch(aButtonID) {
		case ICON_BUTTON_ID_BOTTLE://啤酒瓶
			{
				requestUseItem(MyArStation.iPlayer.E_RIT_BOTTLE, iPlayerList[iSelectSeatID].iSeatID);
//				Tools.debug("animID DesSeatID=" + iPlayerList[iSelectSeatID].iSeatID);
			}
			break;
			
		case ICON_BUTTON_ID_CAKE: //蛋糕
			{
				requestUseItem(MyArStation.iPlayer.E_RIT_CAKE, iPlayerList[iSelectSeatID].iSeatID);
			}
			break;
		
		case ICON_BUTTON_ID_CHANGEDICE://改变骰子
			{
				messageEvent(GameEventID.ESPRITEBUTTON_EVENT_SHOW_STORE);
//				requestUseItem(GameMain.iPlayer.E_RTI_CHANGE_DICE, iPlayerList[iSelectSeatID].iUserID);
			}
			break;
			
		case ICON_BUTTON_ID_FRIEND:	//加好友
			{
				requestAddFriend(iPlayerList[iSelectSeatID].iUserID, 0);
			}
			break;
			
		case ICON_BUTTON_ID_HART:		//心
			{
//				System.out.println("Ding ItemAction iSelectSeatID=" + iSelectSeatID + " UserID=" + iPlayerList[iSelectSeatID].iUserID);
				requestDing(iPlayerList[iSelectSeatID].iUserID);
			}
			break;
			
		case ICON_BUTTON_ID_INFO:		//信息
			{
//				GameMain.iGameProtocol.requestPlayerInfoThree(iPlayerList[iSelectSeatID].iUserID);
				MyArStation.iGameProtocol.requestPlayerInfoFour(iPlayerList[iSelectSeatID].iUserID);
			}
			break;
			
			
		case ICON_BUTTON_ID_KICKOFF:	//踢人
			{
				requestKick(iPlayerList[iSelectSeatID].iUserID);
			}
			break;
			
						
		default:
			{
			}
			break;
		}
	}
	
	/*
	 * 
	 */
	private void initChatView() {
		iChatView = new ChatView(MyArStation.mGameMain.getApplicationContext());
		iChatView.setNotifyNewMessage(this);
		iBuyWineDialog = new BuyDialog(MyArStation.mGameMain);
//		iPlayerInfDialog = new PlayerInfoDialog(GameMain.mGameMain);
		iGameMenuDialog = new GameMenuDialog(MyArStation.mGameMain, this);
		iConcoursDialog = new ConcoursDialog(MyArStation.mGameMain, this);
		eventSelectDialog = new EventSelectDialog(MyArStation.mGameMain);
		eventSelectDialog.setOnGeneralDialogListener(this);
		eventSelectDialog.setConfirmText("确定");
		eventSelectDialog.setCancelText("取消");
		feedBackDialog = new FaceSelectDialog(MyArStation.mGameMain);
		feedBackDialog.setOnSelectFaceListener(this);
		missionView = new MissionView(MyArStation.mGameMain);
//		stroeDialog = new StroeDialog(GameMain.mGameMain);
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameMenuDialog.OnClickMenuListener#onClickMenu(android.view.View)
	 */
	@Override
	public void onClickMenu(View v) {
		int id = v.getId();
		if (id == R.id.btnchangeRoom) {
			//
			if(iShakeListener != null) {
				iShakeListener.stopShake();
			}
			//
//			iGameState = GAME_STATE_CHANGE_ROOM;
//			requestLeaveRoom();
			quickSelectRoomScreen.show();
		} else if (id == R.id.btnchangetable) {
			//
			if(iShakeListener != null) {
				iShakeListener.stopShake();
			}
			//
			iGameState = GAME_STATE_CHANGE_DESK;
			iServerState = E_TGS_WAIT_PLAYER;
			for(int i=0; i<MAX_GAME_PLAYER; i++) {
				iPlayerList[i].iGameState = CGamePlayer.PLAYER_STATE_WAIT;
				iPlayerList[i].iPlayingState = CGamePlayer.PLAYER_STATE_WAIT;
				iPlayerList[i].iLastShoutCount = 0;
				iPlayerList[i].iLastShoutDice = 0;
				iPlayerList[i].ibShoutOne = false;
			}
			requestLeaveRoom();
		} else if (id == R.id.btnexit) {
			showReturnLoginConfirm();
		} else {
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.ConcoursDialog.OnConcoursListener#OnJoint()
	 */
	@Override
	public void OnJoint(Concours concours) {
		//
		if(iShakeListener != null) {
			iShakeListener.stopShake();
		}
		//
		iGameState = GAME_STATE_JOIN_ACTIVITY;
		iServerState = E_TGS_WAIT_PLAYER;
		for(int i=0; i<MAX_GAME_PLAYER; i++) {
			iPlayerList[i].iGameState = CGamePlayer.PLAYER_STATE_WAIT;
			iPlayerList[i].iPlayingState = CGamePlayer.PLAYER_STATE_WAIT;
			iPlayerList[i].iLastShoutCount = 0;
			iPlayerList[i].iLastShoutDice = 0;
			iPlayerList[i].ibShoutOne = false;
		}
		
		if(iAbLayout != null) {
			if(iIconButtonShow == true) {
				messageEvent(GameEventID.ESPRITEBUTTON_EVENT_HIDE_SYSTEM_BUTTON);
			}
		}
		requestLeaveRoom();
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
		ibHangUpActivity = true;
	}
	
	private void requestActivitEnroll(int aActivityID, int aAction) {
		iLoadingTick = 0;
		if(MyArStation.getInstance().iGameProtocol.requestActivityEnroll(aActivityID, aAction)) {
//			showProgressDialog(R.string.Loading_String);
		}
	}
	
	/**
	 * description 活动进场提示
	 */
	private void showMotionStart(Concours e) {
		if(iConcoursDialog != null && iConcoursDialog.isShowing()) {
			return;
		}
		int tStrID = R.string.Motion_Start_Content;//相应提示
		Context tContext = MyArStation.mGameMain.getApplicationContext();
		String str = tContext.getString(tStrID);
		if(e != null) {
			str = e.iMsg;
		}
		iConcoursDialog.show(e);
	}

	@Override
	public void onShake() {
		// TODO Auto-generated method stub
		if(iServerState != E_TGS_WAIT_START &&
				iServerState != E_TGS_WAIT_PLAYER) {
			return;
		}
		if(iPlayerList[iSelfSeatID].iCupMotionID == 1) {
			if(iDiceMovieState == DICE_MOVIE_STATE_IDLE) {
				//
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.shakadice, 0);
				//
				if(iServerState == E_TGS_WAIT_START) {
					iGameTick = 0;
					iPlayerList[iSelfSeatID].iCupMotionID = 3;
					iPlayerList[iSelfSeatID].iCupSprite.resetFrameID();
					if(iShakeListener != null) {
						iShakeListener.stopShake();
					}
					//发送准备请求
					requestReady();
				}
				//播放骰子动画
				iDiceMovieState = DICE_MOVIE_STATE_RUNNING;
				iDiceRunningTick = (int)(12 + Math.random() * 6);
				for(int i=0; i<MAX_DICE_SPRITE; i++) {
					iDiceMovieX[i] = iPlayerList[iChangedSeatID[iSelfSeatID]].iCupX;
					iDiceMovieY[i] = iPlayerList[iChangedSeatID[iSelfSeatID]].iCupY;
					iDiceMovieSpeedX[i] = (int) (72 * ActivityUtil.PAK_ZOOM_Y * Math.sin(Math.toRadians(70 * i)));
					iDiceMovieSpeedY[i] = (int) (72 * ActivityUtil.PAK_ZOOM_Y * Math.cos(Math.toRadians(70 * i)));
					iDiceMovieNum[i] = (int)(1 + Math.random() * 6);
					if(iDiceMovieNum[i] > 6) {
						iDiceMovieNum[i] = 6;
					}
				}
			}
//			else if(iDiceMovieState == DICE_MOVIE_STATE_RUNNING) {
//				for(int i=0; i<MAX_DICE_SPRITE; i++) {
//					if(iDiceMovieSpeedX[i] <= 0) {
//						iDiceMovieSpeedX[i] -= 4;
//					}
//					else {
//						iDiceMovieSpeedX[i] += 4;
//					}
//					if(iDiceMovieSpeedY[i] <= 0) {
//						iDiceMovieSpeedY[i] -= 4;
//					}
//					else {
//						iDiceMovieSpeedY[i] += 4;
//					}
//				}
//			}
		}
	}

	@Override
	public void newMessage(boolean aNew) {
		if(aNew == false) {
			return;
		}
		iChatButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
	}
	
	private void closeGame() {
		//切换到主界面
//		iGameCanvas.changeView(new LoginScreen());
		messageEvent(SocketErrorType.Error_TimeOut);
	}

	/* 
	 * @see com.funoble.lyingdice.view.EventSelectDialog.OnGeneralDialogListener#OnConfirm()
	 */
	@Override
	public void OnConfirm(MBNotifyFriendEvent event) {
		if(event != null) {
			MyArStation.getInstance().iGameProtocol.RequestFriendEventSelect(event.iEventID, event.iKey, 1);
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.EventSelectDialog.OnGeneralDialogListener#OnCancel()
	 */
	@Override
	public void OnCancel(MBNotifyFriendEvent event) {
	}
	
	private void sendFace(int aFaceID) {
		MyArStation.getInstance().iGameProtocol.RequestSendFace(aFaceID);
	}

	/* 
	 * @see com.funoble.lyingdice.view.FaceSelectDialog.OnSelectFaceListener#onSelect(int)
	 */
	@Override
	public void onSelect(int type) {
		sendFace(type);
	}
	
	
	//设置经验条
	private void setExpRect() {
		float w = 234;
		float tW = w*ActivityUtil.ZOOM_X*MyArStation.iPlayer.iGameExp/MyArStation.iPlayer.iNextExp;
		float tLeft = 8*ActivityUtil.ZOOM_X;
		float tTop = 451*ActivityUtil.ZOOM_Y;
		float tRight = tLeft + tW;
		float tBottom = tTop + 21*ActivityUtil.ZOOM_Y;
		iExpRect.set(tLeft, tTop, tRight, tBottom);
		iExpTitleRect.set(tLeft, tTop, tLeft+w*ActivityUtil.ZOOM_X, tBottom);
	}

	/* 
	 * @see com.funoble.lyingdice.screen.QuickSelectRoomScreen.OnSelectRoomListen#OnSelectRoom(com.funoble.lyingdice.data.cach.RoomData)
	 */
	@Override
	public void OnSelectRoom(RoomData data) {
		quickSelectRoomScreen.dimiss();
		if(iPlayerList[iSelfSeatID].iGameState == CGamePlayer.PLAYER_STATE_PLAYING ||
				iGameState == GAME_STATE_LEAVEROOM)  {
			return;
		}
		if(iShakeListener != null) {
			iShakeListener.stopShake();
		}
		if(data.roomType == RoomData.TYPE_UNITE) {
			iGameState = GAME_STATE_CHANGE_ROOM;
		}
		else {
			//
			iGameState = GAME_STATE_CHANGE_DESK;
			iServerState = E_TGS_WAIT_PLAYER;
			for(int i=0; i<MAX_GAME_PLAYER; i++) {
				iPlayerList[i].iGameState = CGamePlayer.PLAYER_STATE_WAIT;
				iPlayerList[i].iPlayingState = CGamePlayer.PLAYER_STATE_WAIT;
				iPlayerList[i].iLastShoutCount = 0;
				iPlayerList[i].iLastShoutDice = 0;
				iPlayerList[i].ibShoutOne = false;
			}
		}
		iRoomPair = data;
		requestLeaveRoom();
	}
}
