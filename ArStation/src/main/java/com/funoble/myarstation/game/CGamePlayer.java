package com.funoble.myarstation.game;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.utils.Lightning;

public class CGamePlayer {
	public static final int MAX_DICE_COUNT = 5;	//最多5个骰子
	public static final int MAX_LIGHTNING_COUNT = 2;	//最多
	
	public static final int PLAYER_STATE_WAIT = 0;		//等待下一局
	public static final int PLAYER_STATE_READY = 1;		//准备
	public static final int PLAYER_STATE_PLAYING = 2;		//游戏开始
	public static final int PLAYER_STATE_LOOK = 3;		//旁观
	public static final int PLAYER_STATE_DRUNK = 4;		//醉酒
	public static final int PLAYER_STATE_NOWINE = 5;		//没有酒
	public static final int PLAYER_STATE_RESULT = 6;		//统计结果
	
	public static final int PLAYING_STATE_WAIT = 0;		//等待
	public static final int PLAYING_STATE_SHOUTING = 1;	//正在喊
	public static final int PLAYING_STATE_SHOUTED = 2;	//正在喊
	public static final int PLAYING_STATE_CHOPING = 3;		//正在劈
	public static final int PLAYING_STATE_CHOPED = 4;		//正在劈
	public static final int PLAYING_STATE_REBEL_CHOPING = 5;//正在反劈
	public static final int PLAYING_STATE_REBEL_CHOPED = 6;	//正在反劈
	public static final int PLAYING_STATE_WAIT_SHOW_RESULT = 7; //等待显示结果
	public static final int PLAYING_STATE_SHOWING_RESULT = 8; //正在显示结果
	public static final int PLAYING_STATE_SHOWDICE_RESULT = 9; //正在显示骰子
	public static final int PLAYING_STATE_SHOWED_RESULT = 10; //显示完结果
	
	public static final int MOTION_STATE_SITDOWNING = 2;	//正在坐下
	public static final int MOTION_STATE_SITDOWNED = 0;		//已经坐下
	
	
	public boolean ibInit = false;				//是否初始化
	public int iUserID = 0;	//UserID
	public String	stUserNick;					//昵称
	public String   stHeadPicName;				//头像名称
	public int 		iVipLevel = 0;				//Vip级别
	public int		iModelID = 0;				//角色模型ID（为-1时，表示没选择人物）
	public int		iGameLevel = 1;				//级别
	public String	iTitle;						//职位称号
	public int		iCurrentDrunk = 0;			//当前醉酒度
	public int		iReward = 0;				//灌倒多少人
	public int		iWinning = 0;				//胜率
	public int		iWinCount = 0;				//胜利次数
	public int		iFailCount = 0;				//失败次数
	public int  	iDiceID = 0;				//骰子ID
	public int		iNextDiceCount = 0;			//下一级的骰子个数
	public int 		iCurrentWineCount = 0;		//当前有多少瓶酒
	
	public int      iGameState = PLAYER_STATE_WAIT;	//游戏状态
	public int		iPlayingState = PLAYING_STATE_WAIT; //玩家Playing的状态
	public int		iSeatID = 0;				//座位号
	public int		iX = 0;						//坐标
	public int		iY = 0;						//坐标
	public int		iCupX = 0;					//骰盅的坐标
	public int 		iCupY = 0;					//骰盅的坐标
	public int		iMotionID = MOTION_STATE_SITDOWNING;//动作
	public int 		iCupMotionID = 0;			//骰盅的动作
	public int      iFaceMotionID = 0;			//表情的动作
	public boolean  ibFaceEnd = true;			//表情动画结束
	public boolean  ibUnArmed = false;			//是否有被禁劈Buf
	public boolean  ibShield = false;			//是否有催眠怀表Buf
	public boolean  ibKillOrder = false;		//是否有追杀令Buf
	public boolean  ibChangeDice = false;		//是否有千王之王
	public boolean  ibCrazed = false;			//是否暴走状态

	public boolean  ibSelf = false;				//是不是玩家自己
	
	public int		iAddExp = 0;				//本局所加的经验
	public int		iAddGb = 0;					//本局所加的金币
	public int 		iAddVipExp = 0;				//本局VIP额外加的经验
	public int		iAddFriendExp = 0;			//本局好友亲密度额外加的经验
	public boolean  ibShoutOne = false;			//是否喊了斋
	public int		iLastShoutCount = 0;		//上次喊的个数
	public int		iLastShoutDice = 0;			//上次喊的骰子
	public int 		iChopDiceCount = 0;			//被劈的骰子个数
	public int[]	iDices = null;				//自己的骰子
	public int[]	iUniteDices = null;			//盟友的骰子
	public boolean[] iBrightDices = null;		//标记是不是被劈的骰子
	
	
	public Sprite	iSprite = null;				//动画	
	public Sprite 	iCupSprite = null;			//骰盅动画
	public Sprite 	iFaceSprite = null;			//表情动画
	public Bitmap	iBmp = null;				//头像
	public Bitmap	iCarBmp = null;				//车进场图像
	
	public int		iChatTick = 0;				//聊天气泡显示的时间
	public String	iChatStrA;					//聊天气泡里的内容
	public String	iChatStrB;					//聊天气泡里的内容
	public String	iChatStrC;					//聊天气泡里的内容
	public Lightning[] lightning;					//愤怒闪电
	//同盟
	public int		iSeatUniteUID;		//盟友UID
	public int		iGroupID = -1;		//组队ID 0 --- 红色   1 --- 绿色    2 ---- 蓝色    3 --- 紫
	
	public void init() {
		iDices = new int[MAX_DICE_COUNT];
		iUniteDices = new int[MAX_DICE_COUNT];
		iBrightDices = new boolean[MAX_DICE_COUNT];
		iCupSprite = new Sprite();
		lightning = new Lightning[MAX_LIGHTNING_COUNT];
	}
	
	public void releaseResource() {
		iCarBmp = null;
		iBmp = null;
		iFaceSprite = null;
		iCupSprite = null;
		iSprite = null;
		iBrightDices = null;
		iDices = null;
		iUniteDices = null;
		lightning = null;
		iGroupID = -1;
	}
}
