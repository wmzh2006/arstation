package com.funoble.myarstation.screen;
//package com.funoble.lyingdice.screen;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Vector;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.Service;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.os.Message;
//import android.os.Vibrator;
//import android.util.Log;
//import android.util.Pair;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AbsoluteLayout.LayoutParams;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.funoble.lyingdice.adapter.GameMotionAdapter;
//import com.funoble.lyingdice.common.Tools;
//import com.funoble.lyingdice.data.cach.GameMotionData;
//import com.funoble.lyingdice.game.GameMain;
//import com.funoble.lyingdice.game.R;
//import com.funoble.lyingdice.gamebase.Map;
//import com.funoble.lyingdice.gamebase.Project;
//import com.funoble.lyingdice.gamebase.Scene;
//import com.funoble.lyingdice.gamebase.Sprite;
//import com.funoble.lyingdice.gamelogic.ArtNumber;
//import com.funoble.lyingdice.gamelogic.CallContentManager;
//import com.funoble.lyingdice.gamelogic.DialogBoxManager;
//import com.funoble.lyingdice.gamelogic.DiceManager;
//import com.funoble.lyingdice.gamelogic.ItemAnimManager;
//import com.funoble.lyingdice.gamelogic.Player;
//import com.funoble.lyingdice.gamelogic.PlayerHPManager;
//import com.funoble.lyingdice.gamelogic.PlayerManager;
//import com.funoble.lyingdice.gamelogic.PlayerName;
//import com.funoble.lyingdice.gamelogic.PlayerNameManager;
//import com.funoble.lyingdice.gamelogic.ShakeListener;
//import com.funoble.lyingdice.gamelogic.SpriteButton;
//import com.funoble.lyingdice.gamelogic.SpriteButtonWheel;
//import com.funoble.lyingdice.gamelogic.SpriteButtonWheelDice;
//import com.funoble.lyingdice.screen.FriendInfoScreen.FriendInfo;
//import com.funoble.lyingdice.socket.protocol.BufferSize;
//import com.funoble.lyingdice.socket.protocol.MBNotifyActivityRanking;
//import com.funoble.lyingdice.socket.protocol.MBNotifyActivityStart;
//import com.funoble.lyingdice.socket.protocol.MBNotifyChangeDice;
//import com.funoble.lyingdice.socket.protocol.MBNotifyChat;
//import com.funoble.lyingdice.socket.protocol.MBNotifyChop;
//import com.funoble.lyingdice.socket.protocol.MBNotifyCleanDrunk;
//import com.funoble.lyingdice.socket.protocol.MBNotifyKick;
//import com.funoble.lyingdice.socket.protocol.MBNotifyKillOrder;
//import com.funoble.lyingdice.socket.protocol.MBNotifyLeaveRoom;
//import com.funoble.lyingdice.socket.protocol.MBNotifyLoginRoom;
//import com.funoble.lyingdice.socket.protocol.MBNotifyManyChop;
//import com.funoble.lyingdice.socket.protocol.MBNotifyReady;
//import com.funoble.lyingdice.socket.protocol.MBNotifyResult;
//import com.funoble.lyingdice.socket.protocol.MBNotifyShield;
//import com.funoble.lyingdice.socket.protocol.MBNotifyShout;
//import com.funoble.lyingdice.socket.protocol.MBNotifyShowItem;
//import com.funoble.lyingdice.socket.protocol.MBNotifyShowReady;
//import com.funoble.lyingdice.socket.protocol.MBNotifyStart;
//import com.funoble.lyingdice.socket.protocol.MBNotifySysMsg;
//import com.funoble.lyingdice.socket.protocol.MBNotifyTriggerShield;
//import com.funoble.lyingdice.socket.protocol.MBNotifyUnarmed;
//import com.funoble.lyingdice.socket.protocol.MBNotifyVieChop;
//import com.funoble.lyingdice.socket.protocol.MBRspActivityEnroll;
//import com.funoble.lyingdice.socket.protocol.MBRspActivityRank;
//import com.funoble.lyingdice.socket.protocol.MBRspAddFriend;
//import com.funoble.lyingdice.socket.protocol.MBRspBuyWine;
//import com.funoble.lyingdice.socket.protocol.MBRspKick;
//import com.funoble.lyingdice.socket.protocol.MBRspLeaveRoom;
//import com.funoble.lyingdice.socket.protocol.MBRspLoginRoom;
//import com.funoble.lyingdice.socket.protocol.MBRspMainPage;
//import com.funoble.lyingdice.socket.protocol.MBRspPlayerInfo;
//import com.funoble.lyingdice.socket.protocol.MBRspPlayerInfoTwo;
//import com.funoble.lyingdice.socket.protocol.MBRspUseItem;
//import com.funoble.lyingdice.socket.protocol.MobileMsg;
//import com.funoble.lyingdice.socket.protocol.ProtocolType.ActionID;
//import com.funoble.lyingdice.socket.protocol.ProtocolType.ActivityAction;
//import com.funoble.lyingdice.socket.protocol.ProtocolType.IM;
//import com.funoble.lyingdice.socket.protocol.ProtocolType.NotifyMsg;
//import com.funoble.lyingdice.socket.protocol.ProtocolType.ResultCode;
//import com.funoble.lyingdice.socket.protocol.ProtocolType.RspMsg;
//import com.funoble.lyingdice.sound.CallPointManager;
//import com.funoble.lyingdice.sound.FeMaleMediaManager;
//import com.funoble.lyingdice.sound.MediaManager;
//import com.funoble.lyingdice.sound.SoundsResID;
//import com.funoble.lyingdice.utils.ActivityUtil;
//import com.funoble.lyingdice.utils.Graphics;
//import com.funoble.lyingdice.utils.SettingManager;
//import com.funoble.lyingdice.utils.TPoint;
//import com.funoble.lyingdice.view.GameEventID;
//import com.funoble.lyingdice.view.GameView;
//import com.funoble.lyingdice.view.MessageEventID;
//
//public class ProgressScreen extends GameView implements OnClickListener{
//	private int iGameState = 0;	//游戏状态
//	private Scene iSceneLoading = null;	//载入资料屏幕
//	
//	private final int RULE_ONE_CAN_CHANGE = 0;//规则 1可变
//	private final int RULE_ONE_CANNOT_CHANGE = 1;//1不可变
//	private final int RULE_ONE_CANNOT_CHANGE_WHEN_CALL = 2;//1喊过后不可变
//	private int iRule = RULE_ONE_CANNOT_CHANGE_WHEN_CALL;//房间规则
//	private boolean ibZhai = false;//是否【斋】
//	private int[] iX_Offset_PlayerNameStr = {78,54};//玩家名字 偏移量 X，0index为800屏，1index为480屏
//	private int[] iY_Offset_PlayerNameStr = {20,16};//
//	private int[] iX_ReadyTime = {483,285};//准备时间 偏移量 X，0index为800屏，1index为480屏
//	private int[] iY_ReadyTime = {457,303};//
//	private int[] iX_PlayerInfoBG = {222,139};//玩家信息底板 X，0index为800屏，1index为480屏
//	private int[] iY_PlayerInfoBG = {102,63};//
//	private int[] iX_PlayerInfo_Head = {iX_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+16,iX_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+8};//玩家信息 头像  X，0index为800屏，1index为480屏
//	private int[] iY_PlayerInfo_Head = {iY_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+14,iY_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+14};//
//	private int[] iX_PlayerInfo_Name = {iX_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+124,iX_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+79};//玩家信息 名字  X，0index为800屏，1index为480屏
//	private int[] iY_PlayerInfo_Name = {iY_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+39,iY_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+26};//
//	private int[] iX_PlayerInfo_Title = {iX_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+124,iX_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+79};//玩家信息 称号  X，0index为800屏，1index为480屏
//	private int[] iY_PlayerInfo_Title = {iY_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+80,iY_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+54};//
//	private int[] iX_PlayerInfo_Win = {iX_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+90,iX_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+54};//玩家信息 胜场  X，0index为800屏，1index为480屏
//	private int[] iY_PlayerInfo_Win = {iY_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+128,iY_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+86};//
//	private int[] iX_PlayerInfo_Lose = {iX_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+262,iX_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+171};//玩家信息 负场  X，0index为800屏，1index为480屏
//	private int[] iY_PlayerInfo_Lose = {iY_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+128,iY_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+86};//
//	private int[] iX_PlayerInfo_WinPer = {iX_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+90,iX_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+54};//玩家信息 胜率  X，0index为800屏，1index为480屏
//	private int[] iY_PlayerInfo_WinPer = {iY_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+159,iY_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+107};//
//	private int[] iX_PlayerInfo_BeatDown = {iX_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+262,iX_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+171};//玩家信息 获奖  X，0index为800屏，1index为480屏
//	private int[] iY_PlayerInfo_BeatDown = {iY_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+159,iY_PlayerInfoBG[ActivityUtil.TYPE_SCREEN]+107};//
//	private int[] iX_CallBG = {340,220};//中间喊点提示底板 X，0index为800屏，1index为480屏
//	private int[] iY_CallBG = {250,190};//
//	private int[] iX_Offset_Chat_Me = {0,0};//我的聊天框  X偏移量，0index为800屏，1index为480屏
//	private int[] iY_Offset_Chat_Me = {-20,-10};//
//	private int[] iX_Offset_Chat = {16,11};//玩家聊天框  X偏移量，0index为800屏，1index为480屏
//	private int[] iY_Offset_Chat = {62,42};//
//	
//	private int[][] iX_Offset_Dice_Player = {{0,36,65,32,-8}, {0,29,50,25,-2}};//玩家 骰子 X偏移量，0index为屏幕，1index为骰子偏移位置
//	private int[][] iY_Offset_Dice_Player = {{0,-7,24,49,38}, {0,-4,19,33,28}};
//	
//	public static final int HP_STUN = 5;//醉酒度为5时，玩家晕
//	public static final int SCENE_INDEX_CALL = 1;//选择喊点 界面索引
//	public static final int SCENE_INDEX_BUYBEER_NOTICE = 2;//买酒通知 界面索引
//	public static final int SCENE_INDEX_BUYHP = 3;//买醒酒丸 界面索引
//	public static final int SCENE_INDEX_PLAYERINFO = 4;//玩家信息 界面索引
//	public static final int SCENE_INDEX_CHAT = 5;//聊天 界面索引
//	public static final int SCENE_INDEX_BUYBEER = 6;//买酒 界面索引
//	public static final int SCENE_INDEX_PLAYERINFO_ME = 7;//自己信息 界面索引
//	public static final int SCENE_INDEX_MENU = 8;//菜单 界面索引
//	public static final int SCENE_INDEX_ITEM = 9;//使用道具 界面索引
//	public static final int SCENE_INDEX_ITEM_TARGET = 10;//使用道具的目标 界面索引
//	
//	private Project iUIPak = null;
//	private Scene iScene = null; 
//	private Scene iPopScene = null;//顶层ui
//	
//	private Vector<SpriteButton> iSpriteButtonList = new Vector<SpriteButton>();//精灵按钮 列表
//	private Vector<SpriteButton> iPopSBList = new Vector<SpriteButton>();//顶层 精灵按钮 列表
//	
//	private PlayerManager iPlayerManager;//玩家管理器
//	private PlayerHPManager iPlayerHPManager;//玩家管理器
//	private PlayerNameManager iPlayerNameManager;//玩家喊点框管理器
//	private HashMap<Integer,SpriteButton> iPlayerNameBGSBList = new HashMap<Integer,SpriteButton>();//玩家名字底板列表,key是客户端位置
//	private HashMap<Integer,TPoint> iPlayerNameBGLocationList = new HashMap<Integer,TPoint>();//全部玩家名字底板位置 列表,key是客户端位置
//	private HashMap<Integer,String> iPlayerNameStrList = new HashMap<Integer,String>();//全部玩家名字 列表,key是客户端位置
//	private DiceManager iMyDiceManager;//自己的骰子管理器
//	private DiceManager iDiceManager;//骰子管理器
//	private CallContentManager iCallContentManager;//喊内容管理器
////	private Player iTestPlayer;//测试 玩家
//	private HashMap<Integer,TPoint> iPlayerLocationList = new HashMap<Integer,TPoint>();//其它玩家位置 列表
//	private HashMap<Integer,TPoint> iPlayerHPLocationList = new HashMap<Integer,TPoint>();//其它玩家醉酒度位置 列表
//	private HashMap<Integer,TPoint> iPlayerNameLocationList = new HashMap<Integer,TPoint>();//全部玩家名字框位置 列表
//	private HashMap<Integer,TPoint> iDiceLocationList = new HashMap<Integer,TPoint>();//自己骰子位置 列表
//	private HashMap<Integer,TPoint> iOtherPFirstDiceLocList = new HashMap<Integer,TPoint>();//其它玩家的首骰子位置 列表
//	
//	public static Project iPlayerBGPak;//玩家头像底
//	private HashMap<Integer,Project> iPlayerPakList = new HashMap<Integer,Project>();
////	public Project iPlayerPak;//通用的玩家
//	public Project iPlayerHPPak;//通用的醉酒度槽
//	public Project iPlayerHPMyPak;//自己的醉酒度槽
//	public Project iPlayerNamePak;//通用的名字框
//	public Project iPlayerNameMyPak;//自己的名字框
//	private HashMap<Integer,Project> iBigDicePakList = new HashMap<Integer,Project>();
//	private HashMap<Integer,Project> iSmallDicePakList = new HashMap<Integer,Project>();
//	public static Project iBigDicePakForWheelDiceItem;//滚轮的大骰子。PS：在大骰子列表中获取，不要释放资源
//	public static Project iBigDicePak;//大骰子
//	public static Project iDicePak;//通用的骰子
//	public Project iNumberShadePak;//选骰子时 个数的遮罩图
//	public static Project iCallBGPak;//喊点框
//	public static Project iZhaiPak;//斋
//	public static Project iDiceCupPak;//玩家桌面的小骰盅
//	public static Project iDrinkPak;//喝酒
//	private Project iArtNumberPak;//美术字
//	private Project iItemAnimationPak;//道具动画
//	
//	private static final int GAME_STATE_READY = 0;//准备状态
//	private static final int GAME_STATE_ROLL = 1;//摇骰子状态
//	private static final int GAME_STATE_GAMING = 2;//游戏进行中状态
//	private static final int GAME_STATE_DUEL = 3;//劈状态
//	private static final int GAME_STATE_DRINK = 4;//喝酒状态
//	private static final int GAME_STATE_RESULT = 5;//结果状态
//	private static final int GAME_STATE_RESULT_IRREGULAR = 6;//非正常结果状态
//	private static final int GAME_STATE_ADDSCORE = 7;//加奖励状态
//	
//	private int iState;
//	private int iSerLocation = 0;//自己在后台桌子的真正位置
//	
//	private SpriteButton iDiceCupSB;//骰盅 伪按钮
//	private SpriteButton iCallSB;//喊按钮
//	private SpriteButton iDuelSB;//劈按钮
//	private SpriteButton iReadySB;//准备按钮
//	private HashMap<Integer,SpriteButton> iPlayerDiceBGSBList = new HashMap<Integer,SpriteButton>();//其它玩家骰子底板 列表
//	private SpriteButtonWheel iWheelSB;// 伪按钮
//	private SpriteButtonWheelDice iWheelDiceSB;// 骰子选择 伪按钮
//	private SpriteButton iCallConfirmSB;//喊 确定(打钩)按钮
//	private SpriteButton iReadyTimeSB;//倒计时框 伪按钮
//	
//	private int iLastLoc = -1;//上一家喊的玩家后台位置
//	private int iLastNum = 2;//上一家喊的骰子个数
//	private int iLastPoint = 2;//上一家喊的骰子点数
//	private boolean ibCallOne = false;//一 是否被喊过了
//	private int iPlayingCount = 6;//正在游戏的玩家数
//	private boolean ibCallSafe = false;//喊 是否合法的;
//	private int iMinNum = 0;//喊时 最小个数
//	private int iDefNum = 0;//喊时 默认个数
//	private int iDefPoint = 0;//喊时 默认点数
//	private boolean ibCanCall = false;//是否能喊;
//	private int iLastSelectedNum = 0;//上次选的骰子个数
//	private int iDuelLoc = 0;//劈的玩家 后台位置
//	private int iCurShowDiceLoc = -1;//当前显示骰子结果的玩家 后台位置
//	private int iLastShowDiceLoc = -1;//上一次显示骰子结果的玩家 后台位置
//	private int iResultCount = 0;//结果 骰子数量
//	private int iResultPoint = 2;//结果 骰子点
//	
//	private int iReadyTime = 0;//准备时的倒计时
//	
//	private int iTurnTick = 0;//计时tick
//	private int iTurnTime = 0;//轮到自己的倒计时
//	
//	private int[] iX_GBs = {62,44};//游戏币，坐标X，0index为800屏，1index为480屏
//	private int[] iY_GBs = {470,313};//游戏币，坐标Y
//	private int[] iX_Beers = {208,136};//酒数量，坐标X，0index为800屏，1index为480屏
//	private int[] iY_Beers = {470,313};//酒数量，坐标Y
//	private int[] iX_RoomTitleStrs = {734,437};//房间名字、杯、称号，坐标X，0index为800屏，1index为480屏
//	private int[] iY_RoomTitleStrs = {32,19};//房间名字、杯、称号，坐标Y
//	private int[] iX_BeatDowns = {734,437};//奖，坐标X，0index为800屏，1index为480屏
//	private int[] iY_BeatDowns = {56,34};//奖，坐标Y
//	private int iGB = 0;//钱
//	private int iBeer = 0;//持有的酒数量
//	private String iRoomTitleStr = "";//房间名字、杯、称号，反正不知道是什么
//	private int iBeatDown = 0;//灌醉人数
//	private String iRankStr = "";//名次
//	
//	//自动购买酒 数据
//	private int iAutoBuyTick = 0;//计时，3秒自动关闭界面
//	private int[] iX_AutoBuys = {215,137};//自动购买酒，底板坐标X，0index为800屏，1index为480屏
//	private int[] iY_AutoBuys = {135,88};//自动购买酒，底板坐标X
//	private int[] iX_BuyNum = {iX_AutoBuys[ActivityUtil.TYPE_SCREEN]+127,iX_AutoBuys[ActivityUtil.TYPE_SCREEN]+74};//购买数量X，0index为800屏，1index为480屏
//	private int[] iY_BuyNum = {iY_AutoBuys[ActivityUtil.TYPE_SCREEN]+52,iY_AutoBuys[ActivityUtil.TYPE_SCREEN]+44};
//	private int[] iX_Prize = {iX_AutoBuys[ActivityUtil.TYPE_SCREEN]+127,iX_AutoBuys[ActivityUtil.TYPE_SCREEN]+74};//单价X，0index为800屏，1index为480屏
//	private int[] iY_Prize = {iY_AutoBuys[ActivityUtil.TYPE_SCREEN]+87,iY_AutoBuys[ActivityUtil.TYPE_SCREEN]+67};
//	private int[] iX_TotalPrize = {iX_AutoBuys[ActivityUtil.TYPE_SCREEN]+127,iX_AutoBuys[ActivityUtil.TYPE_SCREEN]+74};//购买总额X，0index为800屏，1index为480屏
//	private int[] iY_TotalPrize = {iY_AutoBuys[ActivityUtil.TYPE_SCREEN]+122,iY_AutoBuys[ActivityUtil.TYPE_SCREEN]+90};
//	private int[] iX_LeftMoney = {iX_AutoBuys[ActivityUtil.TYPE_SCREEN]+127,iX_AutoBuys[ActivityUtil.TYPE_SCREEN]+74};//剩余金币X，0index为800屏，1index为480屏
//	private int[] iY_LeftMoney = {iY_AutoBuys[ActivityUtil.TYPE_SCREEN]+157,iY_AutoBuys[ActivityUtil.TYPE_SCREEN]+113};
//	private int iX_AutoBuy = 215;//自动购买酒，底板坐标X
//	private int iY_AutoBuy = 135;//
//	private int iBuyNum = 40;//购买数量
//	private int iPrize = 50;//单价
//	private int iTotalPrize = 60;//购买总额
//	private int iLeftMoney = 70;//剩余金币
//	
//	//玩家信息
//	private static final int PLAYER_HEAD_SPRITE_INDEX = 6;
//	private static final int aUserID = 0;
//	private int iPInfoUserID = 0;//用户登陆id 名片的（其他玩家，或是自己的）
//	private int iPInfoRoleID = 0;//角色ID
//	private Sprite iPInfoHeadS = null;//头像
//	private String iPInfoName = "叫什么名字好";//名字
//	private String iPInfoTitle = "叫什么称号好";//称号
//	private int iPInfoMoney = 90;//钱
//	private int iPInfoWin = 100;//胜
//	private int iPInfoLose = 110;//负
//	private int iPInfoBeatDown = 120;//灭
//	private int iPInfoWinPer = 130;//胜率
//	private String iPInfoLittlePicName = "";//头像图片名
//	private Bitmap iHeadBMP = null;//头像图片
//	
//	//道具
//	private HashMap<Integer,TPoint> iItemNumBGLocList = new HashMap<Integer,TPoint>();//道具数量框位置 列表
//	private HashMap<Integer,Integer> iItemIDList = new HashMap<Integer,Integer>();//道具ID 列表
//	private HashMap<Integer,Integer> iItemNumList = new HashMap<Integer,Integer>();//道具数量 列表
//	private HashMap<Integer,Integer> iItemCanUseList = new HashMap<Integer,Integer>();//道具能否使用 列表
//	private HashMap<Integer,ArtNumber> iItemArtNumList = new HashMap<Integer,ArtNumber>();//道具数量 列表
//	private SpriteButton iItemSB;//使用道具 按钮
//	private SpriteButton iQuickDuelSB;//抢开 按钮
//	private SpriteButton iQuickDuelNumBGSB;//抢开数量框 伪按钮
//	private Vector<Integer> iSelectTargetLocList = new Vector<Integer>();//已选(道具)目标的后台位置列表
//	private PlayerManager iTargetManager;//目标玩家管理器
//	private HashMap<Integer,TPoint> iTargetLocationList = new HashMap<Integer,TPoint>();//目标玩家位置 列表
//	private final int SELECT_TYPE_SINGLE = 0;//单选
//	private final int SELECT_TYPE_MULTIPLE = 1;//多选
//	private int iSelectType = SELECT_TYPE_SINGLE;//选择类型，0：单选，1：多选
//	private int iUseItemID = -1;//使用道具的ID
//	private int iServerUseItemID = -1;//后台通知的使用道具的ID
//	private ItemAnimManager iItemAnimManager;
//	private int iChangeDicePoint = 1;//千王 效果 骰子所变成的点
//	
//	private final int[] MAP_ITEM_KEY = {0, 2, 1, 5, 4, 3};//映射
//	
//	final int E_RTI_VIE_CHOP = 0;	//抢劈道具
//	final int E_RTI_CHANGE_DICE = 1;//千王之王道具
//	final int E_RIT_SHIELD = 2;		//催眠怀表
//	final int E_RIT_KILL_ORDER = 3;	//追杀令道具
//	final int E_RTI_MANY_CHOP = 4;	//连劈闪电
//	final int E_RIT_UNARMED = 5;	//禁劈道具
//
//	
//	private int iUserID = 0;//用户登陆id 自己的
//	
//	private boolean ibChange = false;
//	
//	// ------------------ test ----------------------
//	private int iTGameTick = 0;
//	private int iTCallLoc = 0;//正在喊的玩家的后台位置
//	
//	private boolean icanT = true;
//	private boolean ibTest = ActivityUtil.ibTEST;//是否测试用模拟游戏逻辑
//	
//	private MBRspMainPage iPlayerLoginInfo = null;
//	private MBRspLoginRoom iPlayerLoginRoomInfo = null;
//	private MBNotifyResult iNotifyResult = null;//返回结果
//	
//	private final int TAB_INDEX_COMMON = 0;//tab索引 常用语
//	private final int TAB_INDEX_FACE = 1;//表情
//	private final int TAB_INDEX_RECORD = 2;//聊天记录
//	private int iCurTabIndex = TAB_INDEX_COMMON;//当前tab索引
//	private ListView iListView;//列表视图？
//	private ArrayAdapter<String> iCommonListAdapter;//常用语适配器
//	private List<String> iCommonDataList;//常用语列表
//	private ArrayAdapter<String> iRecordListAdapter;//聊天记录适配器
//	private List<String> iRecordDataList;//聊天记录列表
//	private EditText iChatET;//聊天内容编辑框
//	private Button iChatSendB;//发送按钮
//	private Button iChatCommonB;//常用语按钮
//	private Button iChatRecordB;//聊天记录按钮
//	
//	private Dialog iMotionDialog;
//	private ListView iMotionView;
//	private List<GameMotionData> iMotionData;
//	private GameMotionAdapter iMotionAdapter;
//	
//	private Project iChatBoxBGPak;//对话框底图pak
//	private DialogBoxManager iDialogBoxManager;
//	
//	private ShakeListener iShakeListener;
//	
//	private Vibrator iVibrator;
//	private boolean ibVibrate = SettingManager.getInstance().isShaka();//是否震动
//	
//	private static ProgressScreen iProgressScreen;
//	
//	private Context iContext = GameMain.mGameMain;
//	
//	private MBNotifyActivityStart notifyActivityStart = null;
//	private AlertDialog 		dialogEnroll;
//	public ProgressScreen(){
//		
//	}
//	
//	public static ProgressScreen getInstance() {
//		return iProgressScreen;
//	}
//	
//	@Override
//	public void init() {
//		Tools.println("PS init ~~~ ");
//		iProgressScreen = this;
//		iState = -1;
//		loadPlayerProjectL();
//		iUIPak = GameMain.iPakManager.loadUIPak();//Project.loadProject(ActivityUtil.PATH_SCREEN+"lyingdice_map.pak", false);
//		if (iUIPak != null) {
//			iSceneLoading = iUIPak.getScene(12);
//		}
//		if(iScene != null) {
//			iScene.setViewWindow(0, 0, 800, 480);
//			
//			Map pMap = iScene.getLayoutMap(Scene.SpriteLayout);
//			Vector<?> pSpriteList = pMap.getSpriteList();
//			Sprite pSprite;
//			for (int i=0; i<pSpriteList.size(); i++) {
//				pSprite = (Sprite)pSpriteList.elementAt(i);
//				int pX = pSprite.getX();
//				int pY = pSprite.getY();
//				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_GAME_OFFSET + pSprite.getIndex();
//				TPoint pPoint = new TPoint();
//				pPoint.iX = pX;
//				pPoint.iY = pY;
//				//记录玩家位置
//				if (pEventID>=GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER0 && pEventID<=GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER5) {
//					int tLocation = pEventID - GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER0;
//					iPlayerLocationList.put(tLocation, pPoint);
//				}
//				else if (pEventID>=GameEventID.ESPRITEBUTTON_EVENT_GAME_MY_HP && pEventID<=GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER5_HP) {
//					int tLocation = pEventID - GameEventID.ESPRITEBUTTON_EVENT_GAME_MY_HP;
//					iPlayerHPLocationList.put(tLocation, pPoint);
//				}
//				else if (pEventID>=GameEventID.ESPRITEBUTTON_EVENT_GAME_MY_NAME && pEventID<=GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER5_NAME) {
//					int tLocation = pEventID - GameEventID.ESPRITEBUTTON_EVENT_GAME_MY_NAME;
//					iPlayerNameLocationList.put(tLocation, pPoint);
//				}
//				else if (pEventID>=GameEventID.ESPRITEBUTTON_EVENT_GAME_DICE1 && pEventID<=GameEventID.ESPRITEBUTTON_EVENT_GAME_DICE5) {
//					int tLocation = pEventID - GameEventID.ESPRITEBUTTON_EVENT_GAME_DICE1;
//					iDiceLocationList.put(tLocation, pPoint);
//				}
//				else if (pEventID>=GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER1_DICE && pEventID<=GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER5_DICE) {
//					int tLocation = pEventID - GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER1_DICE + 1;
//					iOtherPFirstDiceLocList.put(tLocation, pPoint);
//				}
//				else if (pEventID ==GameEventID.ESPRITEBUTTON_EVENT_GAME_WHEEL) {
//					SpriteButtonWheel pSBW;
//					pSBW = new SpriteButtonWheel(pSprite);
//					pSBW.setShadePak(iNumberShadePak);
//					pSBW.setEventID(pEventID);
//					pSBW.setPosition(pX, pY);
//					pSBW.initItem();
//					pSBW.setLastCall(ibZhai, iLastNum, iLastPoint);
//					iSpriteButtonList.addElement(pSBW);
//					iWheelSB = pSBW;
//					pSBW.setVision(false);
//				}
//				else if (pEventID ==GameEventID.ESPRITEBUTTON_EVENT_GAME_WHEEL_DICE) {
//					SpriteButtonWheelDice pSBWD;
//					pSBWD = new SpriteButtonWheelDice(pSprite);
//					pSBWD.setShadePak(iNumberShadePak);
//					pSBWD.setEventID(pEventID);
//					pSBWD.setPosition(pX, pY);
//					pSBWD.initItem();
//					iSpriteButtonList.addElement(pSBWD);
//					iWheelDiceSB = pSBWD;
//					pSBWD.setVision(false);
//				}
//				else if (pEventID == GameEventID.ESPRITEBUTTON_EVENT_GAME_CALL) {
//				}
//				else {
//					//精灵按钮
//					SpriteButton pSpriteButton;
//					pSpriteButton = new SpriteButton(pSprite);
//					pSpriteButton.setEventID(pEventID);
//					pSpriteButton.setPosition(pX, pY);
//					if (pEventID>=GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER0_NAMEBG && pEventID<=GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER5_NAMEBG) {
//						//名字底板 特殊处理
//						int tLocation = pEventID - GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER0_NAMEBG;
//						pSpriteButton.setVision(false);
//						iPlayerNameBGSBList.put(tLocation, pSpriteButton);
//						iPlayerNameBGLocationList.put(tLocation, pPoint);
//					}
//					else {
//						iSpriteButtonList.addElement(pSpriteButton);
//					}
//					if (pEventID ==GameEventID.ESPRITEBUTTON_EVENT_GAME_CALL2) {
//						iCallSB = pSpriteButton;
//						pSpriteButton.setVision(false);
//					}
//					else if (pEventID ==GameEventID.ESPRITEBUTTON_EVENT_GAME_DUEL) {
//						iDuelSB = pSpriteButton;
//						pSpriteButton.setVision(false);
//					}
//					else if (pEventID ==GameEventID.ESPRITEBUTTON_EVENT_GAME_READY) {
//						iReadySB = pSpriteButton;
//						pSpriteButton.setVision(false);
//					}
//					else if (pEventID>=GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER1_DICEBG && pEventID<=GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER5_DICEBG) {
//						int tLocation = pEventID - GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER1_DICEBG + 1;
//						pSpriteButton.setVision(false);
//						iPlayerDiceBGSBList.put(tLocation, pSpriteButton);
//					}
//					else if (pEventID ==GameEventID.ESPRITEBUTTON_EVENT_GAME_DICECUP) {
//						iDiceCupSB = pSpriteButton;
//						pSpriteButton.setVision(false);
//					}
//					else if (pEventID == GameEventID.ESPRITEBUTTON_EVENT_GAME_READYTIME) {
//						iReadyTimeSB = pSpriteButton;
//						pSpriteButton.setVision(false);
//					}
//					else if (pEventID == GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM) {
//						iItemSB = pSpriteButton;
//						pSpriteButton.setVision(false);
//					}
//					else if (pEventID == GameEventID.ESPRITEBUTTON_EVENT_GAME_QUICK_DUEL) {
//						iQuickDuelSB = pSpriteButton;
//						pSpriteButton.setVision(false);
//					}
//					else if (pEventID == GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_BG) {
//						iQuickDuelNumBGSB = pSpriteButton;
//						pSpriteButton.setVision(false);
//						int tLocation = pEventID - GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_BG;
//						iItemNumBGLocList.put(tLocation, pPoint);
//					}
//				}
//			}
//		}
//		
//		initItemNumBGLoc();
//		initItemNum();
//		
//		iPlayerManager = new PlayerManager();
//		iTargetManager = new PlayerManager();
//		iPlayerHPManager = new PlayerHPManager();
//		iPlayerNameManager = new PlayerNameManager();
//		iMyDiceManager = new DiceManager();
//		iDiceManager = new DiceManager();
//		iCallContentManager = new CallContentManager();
//		iItemAnimManager = new ItemAnimManager();
//
//		Tools.println("PS init ~~~  22222 ");
//		
//		MediaManager.getMediaManagerInstance().playMusic(SoundsResID.bg1, true);
//		if (ibTest) {
//			popAutoBuyBeer();
//			iSerLocation = 5;
//			setPlayer(0, "", "名字最多六字", 0, 0, 0);
//			setPlayer(1, "", "B", 1, 1, 1);
//			setPlayer(2, "", "C", 2, 1, 2);
//			setPlayer(3, "", "D", 3, 0, 3);
//			setPlayer(4, "", "E", 4, 0, 4);
//			setPlayer(5, "", "F", 5, 0, 5);
//			playerLook(5);
//			playerLook(3);
//			setItemNumByKey(0, 10);
//		}
//		else {
////			initPlayerInfo();
//		}
//		
//		initChatListView();
//		
//		iDialogBoxManager = new DialogBoxManager();
//		if (ibTest) {
//			playerChat(0, "刚不刚快点啊啊点一口刚不刚快点啊啊点二国刚不刚快点啊啊点三团刚不刚快");
//			playerChat(1, "刚不刚快点啊啊点一口刚不刚快点啊啊点二国刚不刚快点啊啊点三团刚不刚快");
//			playerChat(2, "刚不刚快点啊啊点一口刚不刚快点啊啊点二国刚不刚快点啊啊点三团刚不刚快");
//			playerChat(3, "刚不刚快点啊啊点一口刚不刚快点啊啊点二国刚不刚快点啊啊点三团刚不刚快");
//			playerChat(4, "刚不刚快点啊啊点一口刚不刚快点啊啊点二国刚不刚快点啊啊点三团刚不刚快");
//			playerChat(5, "刚不刚快点啊啊点一口刚不刚快点啊啊点二国刚不刚快点啊啊点三团刚不刚快");
//		}
//		
//		initMotionRank();
//		
//		iVibrator = ( Vibrator )iContext.getSystemService(Service.VIBRATOR_SERVICE);
//		iShakeListener = new ShakeListener(iContext);
//		iShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {  
//		    public void onShake() {  
//		        // action while shaking
//		    	if (iReadySB != null && iReadySB.isVision()) {
//		    		Tools.debug("PS  shake ~~~~~~~");
//		    		ItemAction(null, GameEventID.ESPRITEBUTTON_EVENT_GAME_READY);
//		    		if(ibVibrate) {
//		    			if(iVibrator != null) {
//		    				iVibrator.vibrate( new long[]{100,500,100,500},-1);
//		    			}
//		    		}
//		    	}
//		    }  
//		});
//	}
//	
//	private void initPlayerInfo() {
//		iPlayerLoginInfo = (MBRspMainPage)GameMain.iGameProtocol.getMobileMsgCS(RspMsg.MSG_RSP_MAINPAGE);
//		iPlayerLoginRoomInfo = (MBRspLoginRoom)GameMain.iGameProtocol.getMobileMsgCS(RspMsg.MSG_RSP_LOGINROOM);
//		if(iPlayerLoginRoomInfo != null) {
//			iSerLocation =iPlayerLoginRoomInfo.iSelfSeatID;
//			iRule = iPlayerLoginRoomInfo.iLogicType;
//			int playerCount = iPlayerLoginRoomInfo.iPlayerCount;
//			for(int i = 0; i < playerCount; i++) {
//				int seatID = iPlayerLoginRoomInfo.iSeatIDs.get(i);
//				String userNicks = iPlayerLoginRoomInfo.iUserNicks.get(i);
//				int currentDrunks = iPlayerLoginRoomInfo.iCurrentDrunks.get(i);
//				int playerStatus = iPlayerLoginRoomInfo.iUserStates.get(i);
//				int roleID = iPlayerLoginRoomInfo.iModelIDs.get(i);
//				String headPicName = iPlayerLoginRoomInfo.iLittlePic.get(i);
//				int dicePakID = iPlayerLoginRoomInfo.iDicePakID.get(i);
//				setPlayer(seatID, headPicName, userNicks, currentDrunks, roleID, dicePakID);
//				if(playerStatus == 1) {
//					playerReady(seatID);
//				}
//				else if (playerStatus == 3) {//观看状态
//					playerLook(seatID);
//				}
//			}
//		}
//		if(iPlayerLoginInfo != null) {
//			iUserID = iPlayerLoginInfo.iUserId;
//			iGB = iPlayerLoginInfo.iGb;//钱
//		}
//		if(iPlayerLoginRoomInfo != null) {
//			iBuyNum = iPlayerLoginRoomInfo.iWineCount;//购买数量
//			iPrize = iPlayerLoginRoomInfo.iPrice;//单价
//			iTotalPrize = iPrize*iBuyNum;//购买总额
//			iPlayerLoginInfo.iGb -= iTotalPrize;//钱
//			iGB = iPlayerLoginInfo.iGb;
//			iLeftMoney = iGB;//剩余金币
//			iBeer = iBuyNum;//持有的酒数量
//			iBeatDown = iPlayerLoginRoomInfo.iRewardGB;//灌醉人数
//			iRoomTitleStr = iPlayerLoginRoomInfo.iRewardName;
//		}
//	}
//	
//	@Override
//	public void releaseResource() {
//		// TODO Auto-generated method stub
//		for (int i=0; i<iSpriteButtonList.size(); i++) {
//			SpriteButton tSB = (SpriteButton)iSpriteButtonList.elementAt(i);
//			tSB.releaseResource();
//			tSB = null;
//		}
//		for (int i=0; i<iPopSBList.size(); i++) {
//			SpriteButton tSB = (SpriteButton)iPopSBList.elementAt(i);
//			tSB.releaseResource();
//			tSB = null;
//		}
//		iPlayerManager.releaseResource();
//		iTargetManager.releaseResource();
//		iPlayerHPManager.releaseResource();
//		iPlayerNameManager.releaseResource();
//		iMyDiceManager.releaseResource();
//		iDiceManager.releaseResource();
//		iCallContentManager.releaseResource();
//		iItemAnimManager.releaseResource();
//		iPlayerBGPak.releaseSelf();
//		iPlayerBGPak = null;
//		for (int i=0; i<=1; i++) {
//			Project tPlayerPak = null;
//			tPlayerPak = iPlayerPakList.get(i);
//			if (tPlayerPak != null) {
//				tPlayerPak.releaseSelf();
//				tPlayerPak = null;
//			}
//		}
//		iPlayerPakList.clear();
//		iPlayerPakList = null;
//		iPlayerHPMyPak.releaseSelf();
//		iPlayerHPMyPak = null;
//		iPlayerHPPak.releaseSelf();
//		iPlayerHPPak = null;
//		iPlayerNamePak.releaseSelf();
//		iPlayerNamePak = null;
//		iPlayerNameMyPak.releaseSelf();
//		iPlayerNameMyPak = null;
//		for (int i=0; i<5; i++) {
//			Project tBigDicePak = null;
//			tBigDicePak = iBigDicePakList.get(i);
//			if (tBigDicePak != null) {
//				tBigDicePak.releaseSelf();
//				tBigDicePak = null;
//			}
//		}
//		iBigDicePakList.clear();
//		iBigDicePakList = null;
//		for (int i=0; i<5; i++) {
//			Project tSmallDicePak = null;
//			tSmallDicePak = iSmallDicePakList.get(i);
//			if (tSmallDicePak != null) {
//				tSmallDicePak.releaseSelf();
//				tSmallDicePak = null;
//			}
//		}
//		iSmallDicePakList.clear();
//		iSmallDicePakList = null;
//		iBigDicePak.releaseSelf();
//		iBigDicePak = null;
//		iDicePak.releaseSelf();
//		iDicePak = null;
//		iNumberShadePak.releaseSelf();
//		iNumberShadePak = null;
//		iCallBGPak.releaseSelf();
//		iCallBGPak = null;
//		iZhaiPak.releaseSelf();
//		iZhaiPak = null;
//		iDiceCupPak.releaseSelf();
//		iDiceCupPak = null;
//		iDrinkPak.releaseSelf();
//		iDrinkPak = null;
//		iUIPak.releaseSelf();
//		iUIPak = null;
//		MediaManager.getMediaManagerInstance().stopMusic();
//		if (iShakeListener != null) {
//			iShakeListener.pause();
//			iShakeListener = null;
//		}
//		if(iVibrator != null) {
//			iVibrator.cancel();
//			iVibrator = null;
//		}
//		if (iMotionDialog != null) {
//			iMotionDialog.dismiss();
//		}
//		iItemArtNumList.clear();
//		iItemArtNumList = null;
//		iArtNumberPak.releaseSelf();
//		iArtNumberPak = null;
//		iItemAnimationPak.releaseSelf();
//		iItemAnimationPak = null;
//		if(dialogEnroll != null) {
//			dialogEnroll.dismiss();
//		}
//	}
//	
//	//载入player相关的pak
//	private void loadPlayerProjectL() {
//		iPlayerBGPak = null;
//		iPlayerBGPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"frame_weapon.pak", true);
//		Project tPlayerPak = null;
//		for (int i=0; i<=1; i++) {
//			tPlayerPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"player_" + i + "_weapon.pak", true);
//			if (tPlayerPak != null) {
//				tPlayerPak.setID(i);
//				iPlayerPakList.put(i, tPlayerPak);
//			}
//		}
//		tPlayerPak = iPlayerPakList.get(iPInfoRoleID);
//		if (tPlayerPak != null) {
//			iPInfoHeadS = tPlayerPak.getSprite(PLAYER_HEAD_SPRITE_INDEX);
//		}
//		iPlayerHPMyPak = null;
//		iPlayerHPMyPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"player_hp_my_weapon.pak", true);
//		iPlayerHPPak = null;
//		iPlayerHPPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"player_hp_weapon.pak", true);
//		iPlayerNamePak = null;
//		iPlayerNamePak = Project.loadProject(ActivityUtil.PATH_SCREEN+"player_name_weapon.pak", true);
//		iPlayerNameMyPak = null;
//		iPlayerNameMyPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"player_name_my_weapon.pak", true);
//		for (int i=0; i<5; i++) {
//			Project tBigDicePak = null;
//			tBigDicePak = Project.loadProject(ActivityUtil.PATH_SCREEN+"dice_big_"+i+"_weapon.pak", true);
//			iBigDicePakList.put(i, tBigDicePak);
//		}
//		for (int i=0; i<5; i++) {
//			Project tSmallDicePak = null;
//			tSmallDicePak = Project.loadProject(ActivityUtil.PATH_SCREEN+"dice_small_"+i+"_weapon.pak", true);
//			iSmallDicePakList.put(i, tSmallDicePak);
//		}
//		iBigDicePakForWheelDiceItem = getBigDicePak(iSerLocation);
//		iBigDicePak = null;
//		iBigDicePak = Project.loadProject(ActivityUtil.PATH_SCREEN+"dice_big_0_weapon.pak", true);
//		iDicePak = null;
//		iDicePak = Project.loadProject(ActivityUtil.PATH_SCREEN+"dice_small_0_weapon.pak", true);
//		iNumberShadePak = null;
//		iNumberShadePak = Project.loadProject(ActivityUtil.PATH_SCREEN+"numbershade_weapon.pak", true);
//		iCallBGPak = null;
//		iCallBGPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"callbg_weapon.pak", true);
//		iZhaiPak = null;
//		iZhaiPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"fast_weapon.pak", true);
//		iDiceCupPak = null;
//		iDiceCupPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"dicecup_weapon.pak", true);
//		iDrinkPak = null;
//		iDrinkPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"drink_weapon.pak", true);
//		iChatBoxBGPak = null;
//		iChatBoxBGPak = Project.loadProject("chat_weapon.pak", true);
//		if (iHeadBMP != null) {
//			iHeadBMP.recycle();
//			iHeadBMP = null;
//		}
//		iArtNumberPak = null;
//		iArtNumberPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"number_weapon.pak", true);
//		iItemAnimationPak = null;
//		iItemAnimationPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"propsanimation_weapon.pak", true);
//	}
//	
//	/**
//	 * description 拿角色模型
//	 * @param aRoleID 模型ID
//	 * @return
//	 */
//	private Project getPlayerPak(int aRoleID) {
//		Project tPlayerPak = null;
//		tPlayerPak = iPlayerPakList.get(aRoleID);
//		if (tPlayerPak == null) {
//			//没有指定的，给一个默认的
//			Tools.debug("ProgressScreen getPlayerPak 没有指定的，给一个默认的");
//			tPlayerPak = iPlayerPakList.get(0);
//		}
//		return tPlayerPak;
//	}
//
//	@Override
//	public void paintScreen(Canvas g, Paint paint) {
//		// TODO Auto-generated method stub
//		if(iGameState == 0) {
//			if(iSceneLoading != null) {
//				iSceneLoading.paint(g, 0, 0);
//				g.drawText("正在读取数据", 300*ActivityUtil.ZOOM_X, 310*ActivityUtil.ZOOM_Y, ActivityUtil.mCallPaint);
//			}
//		}
//		paint.setColor(Color.BLACK);
//		g.drawRect(0, 0, ActivityUtil.SCREEN_WIDTH, 
//				ActivityUtil.SCREEN_HEIGHT, paint);
//
//		paint.setColor(Color.WHITE);
//		g.drawText("ProgressScreen 界面", 10, 60, paint);
//		if(iScene != null) {
//			iScene.paint(g, 0, 0);
//		}
//		iPlayerHPManager.paint(g);
//		paintPlayerName(g);
//		iPlayerManager.paint(g);
//		iPlayerNameManager.paint(g);
//		iDialogBoxManager.paint(g);
//		iCallContentManager.paint(g);
//		iItemAnimManager.paint(g);
//		for(int i=0; i<iSpriteButtonList.size(); i++) {
//			((SpriteButton)iSpriteButtonList.elementAt(i)).paint(g);
//		}
//		iMyDiceManager.paint(g);
//		iDiceManager.paint(g);
//		paintReadyTime(g);
//		paintTurnTime(g);
//		paintMoney(g);
//		if(iPopScene != null) {
//			iPopScene.paint(g, 0, 0);
//		}
//		for(int i=0; i<iPopSBList.size(); i++) {
//			((SpriteButton)iPopSBList.elementAt(i)).paint(g);
//		}
//		paintAutoBuyBeer(g);
//		paintPlayerInfo(g);
//		paintItemNumForQuickDuel(g);
//		paintItemNumForPopScene(g);
//		paintTargetForPopScene(g);
//	}
//	
//	//画 准备时 的倒计时
//	private void paintReadyTime(Canvas g) {
//		if (iState == GAME_STATE_READY) {
//			if(!iReadyTimeSB.isVision() && iReadyTimeSB != null) {
//				return;
//			}
//			if (iReadyTime >= 10) {
//				g.drawText(""+iReadyTime, (iX_ReadyTime[ActivityUtil.TYPE_SCREEN]-ActivityUtil.mCallPaint.getTextSize()/2)*ActivityUtil.PAK_ZOOM_X, 
//						iY_ReadyTime[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y, ActivityUtil.mCallPaint);
//			}
//			else {
//				g.drawText(""+iReadyTime, iX_ReadyTime[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X, 
//						iY_ReadyTime[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y, ActivityUtil.mCallPaint);
//			}
//		}
//	}
//	
//	//画 准备时 的倒计时
//	private void paintTurnTime(Canvas g) {
//		if (iState == GAME_STATE_GAMING) {
//			if(iReadyTimeSB != null && !iReadyTimeSB.isVision()) {
//				return;
//			}
//			if (iTurnTime >= 10) {
//				g.drawText(""+iTurnTime, (iX_ReadyTime[ActivityUtil.TYPE_SCREEN]-ActivityUtil.mCallPaint.getTextSize()/3)*ActivityUtil.PAK_ZOOM_X, 
//						iY_ReadyTime[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y, ActivityUtil.mCallPaint);
//			}
//			else {
//				g.drawText(""+iTurnTime, iX_ReadyTime[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X, 
//						iY_ReadyTime[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y, ActivityUtil.mCallPaint);
//			}
//		}
//	}
//	
//	//画 钱，房间称号
//	private void paintMoney(Canvas g) {
//		g.drawText(""+iGB, iX_GBs[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X, iY_GBs[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y, ActivityUtil.mMoneyPaint);
//		g.drawText(""+iBeer, iX_Beers[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X, iY_Beers[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y, ActivityUtil.mMoneyPaint);
//		if (!iRankStr.equals("")) {
//			g.drawText("名次", iX_RoomTitleStrs[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X, iY_RoomTitleStrs[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y, ActivityUtil.mPaint_NORMAL_WHITE_CENTER);
//			g.drawText(""+iRankStr, iX_BeatDowns[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X, iY_BeatDowns[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y, ActivityUtil.mPaint_NORMAL_WHITE_CENTER);
//		}
//		else {
//			g.drawText(""+iRoomTitleStr, iX_RoomTitleStrs[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X, iY_RoomTitleStrs[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y, ActivityUtil.mPaint_NORMAL_WHITE_CENTER);
//			if (iBeatDown > 0) {
//				g.drawText(""+iBeatDown, iX_BeatDowns[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X, iY_BeatDowns[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y, ActivityUtil.mPaint_NORMAL_WHITE_CENTER);
//			}
//		}
//	}
//	
//	//画 自动买酒通知
//	private void paintAutoBuyBeer(Canvas g) {
//		if (iPopScene != null) {
//			if (iPopScene.getIndex() == SCENE_INDEX_BUYBEER_NOTICE) {
//				if (ActivityUtil.TYPE_SCREEN >= iX_AutoBuys.length) {
//					return;
//				}
//				g.drawText(""+iBuyNum, iX_BuyNum[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X, iY_BuyNum[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y, ActivityUtil.mBeatDownPaint);
//				g.drawText(""+iPrize, iX_Prize[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X, iY_Prize[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y, ActivityUtil.mBeatDownPaint);
//				g.drawText(""+iTotalPrize, iX_TotalPrize[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X, iY_TotalPrize[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y, ActivityUtil.mBeatDownPaint);
//				g.drawText(""+iLeftMoney, iX_LeftMoney[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X, iY_LeftMoney[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y, ActivityUtil.mBeatDownPaint);
//			}
//		}
//	}
//	
//	//画 玩家信息
//	private void paintPlayerInfo(Canvas g) {
//		if (iPopScene != null) {
//			if (iPopScene.getIndex() == SCENE_INDEX_PLAYERINFO || iPopScene.getIndex() == SCENE_INDEX_PLAYERINFO_ME) {
//				if (iHeadBMP != null) {
//					g.drawBitmap(iHeadBMP, (int) (iX_PlayerInfo_Head[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X), 
//							(int) (iY_PlayerInfo_Head[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y), null);
////					Tools.println("PS paintPlayerInfo iHeadW="+iHeadBMP.getWidth()+",iHeadH="+iHeadBMP.getHeight());
//				}
//				else {
//					iPInfoHeadS.paintAction(g, (int) (iX_PlayerInfo_Head[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X), 
//							(int) (iY_PlayerInfo_Head[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y), 1, 0);
//				}
//				g.drawText(iPInfoName, (int) (iX_PlayerInfo_Name[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X), 
//						(int) (iY_PlayerInfo_Name[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y), ActivityUtil.mBeatDownPaint);
//				g.drawText(iPInfoTitle, (int) (iX_PlayerInfo_Title[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X), 
//						(int) (iY_PlayerInfo_Title[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y), ActivityUtil.mBeatDownPaint);
//				g.drawText(""+iPInfoWin, (int) (iX_PlayerInfo_Win[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X), 
//						(int) (iY_PlayerInfo_Win[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y), ActivityUtil.mBeatDownPaint);
//				g.drawText(""+iPInfoLose, (int) (iX_PlayerInfo_Lose[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X), 
//						(int) (iY_PlayerInfo_Lose[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y), ActivityUtil.mBeatDownPaint);
//				g.drawText(""+iPInfoWinPer+"%", (int) (iX_PlayerInfo_WinPer[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X), 
//						(int) (iY_PlayerInfo_WinPer[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y), ActivityUtil.mBeatDownPaint);
//				g.drawText(""+iPInfoBeatDown, (int) (iX_PlayerInfo_BeatDown[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X), 
//						(int) (iY_PlayerInfo_BeatDown[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y), ActivityUtil.mBeatDownPaint);
//			}
//		}
//	}
//	
//	//画 玩家名字
//	private void paintPlayerName(Canvas g) {
//		for (int i=0; i<=5; i++) {
//			SpriteButton tSB = iPlayerNameBGSBList.get(i);
//			if (tSB != null) {
//				tSB.paint(g);
//			}
//			String tName = iPlayerNameStrList.get(i);
//			TPoint tPoint = iPlayerNameBGLocationList.get(i);
//			if (tName!=null && tPoint!=null) {
//				g.drawText(tName, tPoint.iX+iX_Offset_PlayerNameStr[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X, 
//						tPoint.iY+iY_Offset_PlayerNameStr[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y, ActivityUtil.mPaint_NORMAL_WHITE_CENTER);
//			}
//		}
//	}
//	
//	//画 道具数量 抢开按钮
//	private void paintItemNumForQuickDuel(Canvas g) {
//		if (iQuickDuelNumBGSB!=null && iQuickDuelNumBGSB.isVision()) {
//			ArtNumber p = iItemArtNumList.get(0);
//			if (p!=null) {
//				p.paint(g);
//			}
//		}
//	}
//	//画 道具数量 道具界面
//	private void paintItemNumForPopScene(Canvas g) {
//		if (iPopScene != null && iPopScene.getIndex() == SCENE_INDEX_ITEM) {
//			for (int i=1; i<6; i++) {
//				ArtNumber p = iItemArtNumList.get(i);
//				if (p!=null) {
//					p.paint(g);
//				}
//			}
//		}
//	}
//	//画 道具目标
//	private void paintTargetForPopScene(Canvas g) {
//		if (iPopScene != null && iPopScene.getIndex() == SCENE_INDEX_ITEM_TARGET) {
//			iTargetManager.paint(g);
//		}
//	}
//	
//	@Override
//	public void performL() {
//		// TODO Auto-generated method stub
//		if(iGameState == 0) {
//			if(iSceneLoading != null) {
//				iSceneLoading.handle();
//			}
//		}
////		if(iScene != null) {
////			iScene.handle();
////		}
////		for(int i=0; i<iSpriteButtonList.size(); i++) {
////			((SpriteButton)iSpriteButtonList.elementAt(i)).perform();
////		}
////		if(iPopScene != null) {
////			iPopScene.handle();
////		}
////		for(int i=0; i<iPopSBList.size(); i++) {
////			((SpriteButton)iPopSBList.elementAt(i)).perform();
////		}
////		iPlayerManager.performL();
////		iPlayerHPManager.performL();
////		iPlayerNameManager.performL();
////		iMyDiceManager.performL();
////		iDiceManager.performL();
////		iCallContentManager.performL();
////		iDialogBoxManager.performL();
////		iItemAnimManager.performL();
////		
////		if (ibTest) {
////			testPerformL();
////		}
////		else {
////			realPerformL();
////		}
////		
////		if(ibChange){
////			Message tMes = new Message();
////			tMes.what = MessageEventID.EMESSAGE_EVENT_TO_HOME;
////			GameMain.getInstance().iHandler.sendMessage(tMes);
////			ibChange = false;
////		}
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		Log.v("ProgressScreen","onKeyDown = " + keyCode);
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (iPopScene != null) {
//				//有弹出界面
//				closePopScene();
//			}
//			else {
//				//弹出返回大厅确认框
//				showReturnHomeConfirm();
//			}
//			return true;
//		}
//		if (!ibTest) {
//			return false;
//		}
//		//测试
//		if (keyCode == 19) {
////			showMotionSignUp();
//			iServerUseItemID = getItemID(E_RIT_SHIELD);
//			triggerItem(4);
//		}
////		if (keyCode == 20) {
////			showMotionStart(null);
////		}
////		if (keyCode == 21) {
//////			playerLeave(4);
//////			messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_LeaveRoom, 4);
////			setPlayer(1, "", "B", 1, 1, 1);
////			showMotionRank();
////		}
////		if (keyCode == 22) {
////			playerLeave(1);
//////			messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_LeaveRoom, 1);
////		}
////		//测试
////		if (keyCode == 19) {
////			updatePlayerHP(1, 1);
////			iCallSB.setVision(false);
////		}
////		if (keyCode == 20) {
////			updatePlayerHP(1, 5);
////			iCallSB.setVision(true);
////		}
////		if (keyCode == 21) {
//////			updatePlayerHP(4, 1);
////			playerDrink(4, 1);
////		}
////		if (keyCode == 22) {
//////			updatePlayerHP(4, 5);
////			playerDrink(4, 5);
////		}
//		return false;
//	}
//
//	@Override
//	public boolean onDown(MotionEvent e) {
////		Tools.println("ProgressScreen onDown event.getAction() = " +e.getAction());
//		boolean result= false;
//		int aX = (int) e.getX();
//		int aY = (int) e.getY();
//		result = pointerPressed(aX, aY);
//		return result;
//	}
//	
//	@Override
//	public boolean onLongPress(MotionEvent e) {
////		Tools.println("ProgressScreen onLongPress event.getAction() = " +e.getAction());
//		boolean result= false;
//		int aX = (int) e.getX();
//		int aY = (int) e.getY();
//		result = pointerReleased(aX, aY);
//		return result;
//	}
//	
//	@Override
//	public boolean onSingleTapUp(MotionEvent e) {
////		Tools.println("ProgressScreen onSingleTapUp event.getAction() = " +e.getAction());
//		boolean result= false;
//		int aX = (int) e.getX();
//		int aY = (int) e.getY();
//		result = pointerReleased(aX, aY);
//		return result;
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (iWheelSB != null) {
//			if (iWheelSB.isVision()) {
//				int tEX = (int) event.getX();
//				int tEY = (int) event.getY();
//				Rect pRect = new Rect();
//				int pX = iWheelSB.getX();
//				int pY = iWheelSB.getY();
//				Rect pLogicRect = iWheelSB.getRect();
//				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
//						pX+pLogicRect.right, pY+pLogicRect.bottom);
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					break;
//				case MotionEvent.ACTION_MOVE:
//					break;
//				case MotionEvent.ACTION_UP:
//					break;
//				}
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					iWheelSB.onTouchEvent(event);
//				}
//				else {
//					if (pRect.contains(tEX, tEY)) {
//						return iWheelSB.onTouchEvent(event);
//					}
//				}
////				Tools.println("ProgressScreen onFling tEX = " +tEX + "  tEY =" +tEY);
//			}
//		}
//		if (iWheelDiceSB != null) {
//			if (iWheelDiceSB.isVision()) {
//				int tEX = (int) event.getX();
//				int tEY = (int) event.getY();
//				Rect pRect = new Rect();
//				int pX = iWheelDiceSB.getX();
//				int pY = iWheelDiceSB.getY();
//				Rect pLogicRect = iWheelDiceSB.getRect();
//				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
//						pX+pLogicRect.right, pY+pLogicRect.bottom);
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					break;
//				case MotionEvent.ACTION_MOVE:
//					break;
//				case MotionEvent.ACTION_UP:
//					break;
//				}
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					iWheelDiceSB.onTouchEvent(event);
//				}
//				else {
//					if (pRect.contains(tEX, tEY)) {
//						return iWheelDiceSB.onTouchEvent(event);
//					}
//				}
////				Tools.println("ProgressScreen onFling tEX = " +tEX + "  tEY =" +tEY);
//			}
//		}
//		return false;
//	}
//	
//	@Override
//	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//			float velocityY) {
//		if (iWheelSB != null) {
//			if (iWheelSB.isVision()) {
//				int tEX = (int) e1.getX();
//				int tEY = (int) e1.getY();
//				Rect pRect = new Rect();
//				int pX = iWheelSB.getX();
//				int pY = iWheelSB.getY();
//				Rect pLogicRect = iWheelSB.getRect();
//				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
//						pX+pLogicRect.right, pY+pLogicRect.bottom);
//				if (pRect.contains(tEX, tEY)) {
//					iWheelSB.onFling(e1, e2, velocityX, velocityY);
//				}
////				Tools.println("ProgressScreen onFling tEX = " +tEX + "  tEY =" +tEY);
//			}
//		}
//		if (iWheelDiceSB != null) {
//			if (iWheelDiceSB.isVision()) {
//				int tEX = (int) e1.getX();
//				int tEY = (int) e1.getY();
//				Rect pRect = new Rect();
//				int pX = iWheelDiceSB.getX();
//				int pY = iWheelDiceSB.getY();
//				Rect pLogicRect = iWheelDiceSB.getRect();
//				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
//						pX+pLogicRect.right, pY+pLogicRect.bottom);
//				if (pRect.contains(tEX, tEY)) {
//					iWheelDiceSB.onFling(e1, e2, velocityX, velocityY);
//				}
////				Tools.println("ProgressScreen onFling tEX = " +tEX + "  tEY =" +tEY);
//			}
//		}
//		return false;
//	}
//	
//	@Override
//	public boolean pointerPressed(int aX, int aY) {
//		boolean result= false;
//		result = pointerPressedForPop(aX, aY);
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
//				if (iWheelDiceSB !=null && iWheelDiceSB.isVision() && pSB.getEventID()== GameEventID.ESPRITEBUTTON_EVENT_GAME_X1) {
//					continue;//如果选择骰子框已弹出，跳过聊天按钮，因为它们重叠了
//				}
//				pSB.pressed();
//				Tools.println("ProgressScreen pointerPressed  pSB.pressed() ");
//				result = true;
//				break;
//			}
//		}
//		return result;
//	}
//
//	private boolean pointerPressedForPop(int aX, int aY) {
//		boolean result= false;
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
//				Tools.println("ProgressScreen pointerPressedForPop  pSB.pressed() ");
//				result = true;
//				break;
//			}
//		}
//		if (!result && iPopScene != null) {
//			//在弹出界面 而且没点到任何按钮
//			result = true;
//			switch (iPopScene.getIndex()) {
//			case SCENE_INDEX_CALL:
//				break;
//			}
//		}
//		return result;
//	}
//	
////	//点击玩家 (获取信息)
////	private boolean pointerPressedForPlayer(int aX, int aY) {
////		boolean result= false;
////		int tLoc = -1;//玩家后台位置
////		tLoc = iPlayerManager.getPlayerLocByPress(aX, aY);
////		if (tLoc!=-1 && tLoc!=iSerLocation) {
////			//点到别的玩家
////			Tools.println("ProgressScreen  pointerPressedForPlayer  tLoc = " + tLoc);
////			//弹出 玩家信息
////			popPlayerInfo();
////			result = true;
////		}
////		return result;
////	}
//	
//	@Override
//	public boolean pointerReleased(int aX, int aY) {
//		boolean result= false;
//		result = pointerReleasedForTarget(aX, aY);
//		if (result) {
//			return result;
//		}
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
//				if (iWheelDiceSB !=null && iWheelDiceSB.isVision() && pSB.getEventID()== GameEventID.ESPRITEBUTTON_EVENT_GAME_X1) {
//					continue;//如果选择骰子框已弹出，跳过聊天按钮，因为它们重叠了
//				}
//				pSB.released();
//				ItemAction(null, pSB.getEventID());
//				result = true;
//				MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//				break;
//			}
//		}
//		if (result) {
//			return result;
//		}
//		result = pointerReleasedForPlayer(aX, aY);
//		if (result) {
//			return result;
//		}
//		return result;
//	}
//	
//	private boolean pointerReleasedForPop(int aX, int aY) {
//		boolean result= false;
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
//				MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//				result = true;
//				break;
//			}
//		}
//		if (!result && iPopScene != null) {
//			//在弹出界面 而且没点到任何按钮
//			//先标记该事件 由弹出界面处理
//			result = true;
//			switch (iPopScene.getIndex()) {
//			case SCENE_INDEX_CALL:
//				break;
//			case SCENE_INDEX_BUYBEER_NOTICE:
//				closeAutoBuyBeer();
//				break;
//			case SCENE_INDEX_PLAYERINFO_ME:
//			case SCENE_INDEX_PLAYERINFO:
//				closePlayerInfo();
//				break;
//			case SCENE_INDEX_CHAT:
//				//聊天界面 返回
//				closeChat();
//				break;
//			case SCENE_INDEX_MENU://菜单
//			case SCENE_INDEX_ITEM://道具
//			case SCENE_INDEX_ITEM_TARGET://道具目标
//				closePopScene();
//				break;
//			}
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//		}
//		return result;
//	}
//	
//	//点击玩家 (获取信息)
//	private boolean pointerReleasedForPlayer(int aX, int aY) {
//		boolean result = false;
//		int tLoc = -1;//玩家后台位置
//		tLoc = iPlayerManager.getPlayerLocByPress(aX, aY);
//		if (tLoc!=-1) {
//			//点到别的玩家
//			Tools.println("ProgressScreen  pointerReleasedForPlayer  tLoc = " + tLoc);
//			if (ibTest) {
//				//模拟游戏逻辑
//				//弹出 玩家信息
//				popPlayerInfo(tLoc);
//			}
//			else {
//				//正常游戏逻辑
//				GameMain.iGameProtocol.requestPlayerInfo(tLoc);
//			}
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//			result = true;
//		}
//		return result;
//	}
//	
//	//点击玩家 (道具目标)
//	private boolean pointerReleasedForTarget(int aX, int aY) {
//		boolean result = false;
//		if (iPopScene!=null && iPopScene.getIndex()==SCENE_INDEX_ITEM_TARGET) {
//			int tLoc = -1;//玩家后台位置
//			tLoc = iTargetManager.getPlayerLocByPress(aX, aY);
//			if (tLoc!=-1) {
//				//点到别的玩家
//				System.out.println("ProgressScreen 选择目标 tLoc="+tLoc+",iSelectType="+iSelectType);
//				switch (iSelectType) {
//				case SELECT_TYPE_SINGLE:
//					iSelectTargetLocList.clear();
//					iSelectTargetLocList.addElement(tLoc);
//					iTargetManager.allPlayerUnSelect();
//					iTargetManager.playerSetSelect(tLoc, true);
//					break;
//				case SELECT_TYPE_MULTIPLE:
//					boolean tbSelected = false;//该玩家是否已被选中了
//					int tIndex = -1;//该玩家所在已选列表的Index
//					for (int i=0; i<iSelectTargetLocList.size(); i++) {
//						int tSelectedLoc = iSelectTargetLocList.elementAt(i);//已选的
//						if (tSelectedLoc == tLoc) {
//							tbSelected = true;
//							tIndex = i;
//							break;
//						}
//					}
//					//进行反选
//					if (tbSelected) {
//						iSelectTargetLocList.remove(tIndex);
//					}
//					else {
//						iSelectTargetLocList.addElement(tLoc);
//					}
//					iTargetManager.playerInverseSelect(tLoc);
//					break;
//				}
//				if (ibTest) {
//					//模拟游戏逻辑
//					System.out.println("begin -----------ProgressScreen 已选目标 ------------------------");
//					for (int i=0; i<iSelectTargetLocList.size(); i++) {
//						int tSelectedLoc = iSelectTargetLocList.elementAt(i);//已选的
//						System.out.println("ProgressScreen 已选目标 tLoc="+tSelectedLoc);
//					}
//					System.out.println("end -----------ProgressScreen 已选目标 ------------------------");
//				}
//				else {
//					//正常游戏逻辑
//				}
//				MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//				result = true;
//			}
//		}
//		return result;
//	}
//	
//	public boolean ItemAction(GameView aGV, int aEventID) {
//		if (aGV == null) {
//			switch (aEventID) {
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_READY:
//				iReadyTime = -1;
//				MediaManager.getMediaManagerInstance().playerSound(SoundsResID.shakadice, 0);
//				if (iDiceCupSB != null) {
//					iDiceCupSB.setVision(true);
//					iDiceCupSB.released();
//				}
//				iReadySB.setVision(false);
//				iReadyTimeSB.setVision(false);
//				MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//				if (ibTest) {
//					//模拟游戏逻辑
//					playerReady(iSerLocation);
////					gameStart();
//					gameRoll();
//				}
//				else {
//					//正常游戏逻辑
//					if(iPlayerLoginInfo != null) {
//						GameMain.iGameProtocol.RequsetReady(iUserID);
//					}
//				}
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_DUEL:
//				//自己主动劈，关闭相关的东西
//				closeDiceSelectNotScene();
//				if (ibTest) {
//					//模拟游戏逻辑
//					gameDuel(iSerLocation);
//				}
//				else {
//					//正常游戏逻辑
//					if(iUserID != 0) {
//						GameMain.iGameProtocol.requsetchop(iUserID);
//					}
//				}
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_CALL:
//				popDiceSelect();
//				MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_CALL_RETURN:
//				closeDiceSelect();
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_CALL_CONFIRM:
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_CALL2:
//				iTGameTick = 0;
//				int tNum = 5*iPlayingCount;
//				int tDiceP = 7;
//				if (iWheelSB != null && iWheelSB.isVision()) {
//					tNum = iWheelSB.getValue();
//					tDiceP = iWheelDiceSB.getValue();
//				}
//				if (tDiceP == 7) {//7点 就是 1点
//					tDiceP = 1;
//				}
//				if (ibTest) {
//					//模拟游戏逻辑
//					playerCall(iSerLocation, tNum, tDiceP);
//					iTCallLoc ++;
//					if (iTCallLoc>5) {
//						iTCallLoc = 0;
//					}
//					playerTurn(iTCallLoc);
//				}
//				else {
//					//正常游戏逻辑
//					if(iUserID != 0) {
//						GameMain.iGameProtocol.RequsetShout(iUserID, tDiceP, tNum);
//					}
//				}
//				//自己主动喊点，关闭相关的东西
//				closeDiceSelectNotScene();
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_BUYHP_CONFIRM:
//				//购买醒酒丸界面 确认购买
//				MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//				if(iUserID != 0) {
//					GameMain.iGameProtocol.requsetcLeanDrunk(iUserID);
//					showProgressDialog(R.string.Loading_String);
//					closeBuyHP();
//				}
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_BUYHP_RETURN:
//				//购买醒酒丸界面 返回大厅
//				MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//				if(iUserID != 0) {
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_ReqLeaveRoom);
//				}
//				closePopScene();
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_BUYBEER_CONFIRM:
//				//购买酒界面 确认购买
//				MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//				if(iUserID != 0) {
//					requestBuyWine();
//					closePopScene();
//				}
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_MENU:
//				popMenu();
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_MENU_LEAVE://离开桌子
//				closePopScene();
//				showReturnHomeConfirm();
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_MENU_OTHERROOM://其它房间
//				closePopScene();
//				showToSelectRoomConfirm();
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_MENU_RESUMEGAME://返回游戏
//				closePopScene();
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_X1:
////				GameCanvas.getInstance().test();
////				MediaManager.getMediaManagerInstance().setOpenEffectState(false);
////				MediaManager.getMediaManagerInstance().setOpenBgState(false);
//				popChat();
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_NAMECARD:
//				if (ibTest) {
//					//模拟游戏逻辑
//					//弹出 玩家信息
//					popPlayerInfo(iSerLocation);
//				}
//				else {
//					//正常游戏逻辑
//					GameMain.iGameProtocol.requestPlayerInfo(iSerLocation);
//				}
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_KICK://踢人
//				if (ibTest) {
//					//模拟游戏逻辑
//					//弹出 玩家信息
//				}
//				else {
//					//正常游戏逻辑
//					if(requestKick(iPInfoUserID)) {
//						closePlayerInfo();
//					}
//				}
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_MAKEFRIEND://加好友
//				if (ibTest) {
//					//模拟游戏逻辑
//					//弹出 玩家信息
//				}
//				else {
//					//正常游戏逻辑
//					if(requestAddFriend(iPInfoUserID)) {
//						closePlayerInfo();
//					}
//				}
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM://使用道具 按钮
//				popItem();
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_QUICK_DUEL://抢开按钮
////				//自己主动劈，关闭相关的东西
////				closeDiceSelectNotScene();
//				hideItemThing();
//				iUseItemID = getItemID(E_RTI_VIE_CHOP);
//				if (ibTest) {
//					//模拟游戏逻辑
//					iServerUseItemID = iUseItemID;
//					gameDuel(iSerLocation);
//					int tQDNum = iItemNumList.get(0);
//					setItemNumByID(10,tQDNum-1);
//					setItemAnim(iSerLocation);
//				}
//				else {
//					//正常游戏逻辑
////					if(iUserID != 0) {
////						GameMain.iGameProtocol.requsetchop(iUserID);
////					}
//					int pKey = MAP_ITEM_KEY[E_RTI_VIE_CHOP];
//					int ItemID = iItemIDList.get(pKey);
//					Vector<Integer> userSeatID = new Vector<Integer>();
//					userSeatID.add(new Integer(iSerLocation));
//					requestUseItem(ItemID, userSeatID);
//				}
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_HYPNOSIS://催眠
//				iUseItemID = getItemID(E_RIT_SHIELD);
//				if (ibTest) {
//					//模拟游戏逻辑
//					iServerUseItemID = iUseItemID;
//					setItemAnim(iSerLocation);
//				}
//				else {
//					Integer ItemID = getItemID(E_RIT_SHIELD);
//					Vector<Integer> userSeatID = new Vector<Integer>();
//					userSeatID.add(new Integer(iSerLocation));
//					if(ItemID != null) {
//						requestUseItem(ItemID, userSeatID);
//					}
//				}
//				hideItemThing();
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_LIEKING://千王
//				iUseItemID = getItemID(E_RTI_CHANGE_DICE);
//				if (ibTest) {
//					//模拟游戏逻辑
//					iServerUseItemID = iUseItemID;
//					setItemAnim(iSerLocation);
////					clearMyDice();
//				}
//				else {
//					//正常游戏逻辑
//					Integer ItemID = getItemID(E_RTI_CHANGE_DICE);
//					Vector<Integer> userSeatID = new Vector<Integer>();
//					userSeatID.add(new Integer(iSerLocation));
//					if(ItemID != null) {
//						requestUseItem(ItemID, userSeatID);
//					}
//				}
//				hideItemThing();
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_SILENCE://封口
//				closeItem();
//				iSelectType = SELECT_TYPE_SINGLE;
//				iUseItemID = getItemID(E_RIT_UNARMED);
//				popTarget();
////				if (ibTest) {
////					//模拟游戏逻辑
////				}
////				else {
////					//正常游戏逻辑
////					Vector<Integer> tList = getAllPlayingLoc();
////					if(tList != null && tList.size() > 0) {
////						ItemID = getItemID(E_RIT_UNARMED);
////						int index=(int)(Math.random()*tList.size());
////						userSeatID = new Vector<Integer>();
////						userSeatID.add(new Integer(tList.get(index)));
////						if(ItemID != null) {
////							requestUseItem(ItemID, userSeatID);
////						}
////					}
////				}
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_COMBO_DUEL://连劈
//				closeItem();
//				iSelectType = SELECT_TYPE_SINGLE;
//				iUseItemID = getItemID(E_RTI_MANY_CHOP);
//				popTarget();
////				if (ibTest) {
////					//模拟游戏逻辑
////				}
////				else {
////					//正常游戏逻辑
////				}
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_HUNT://追杀
//				closeItem();
//				iSelectType = SELECT_TYPE_SINGLE;
//				iUseItemID = getItemID(E_RIT_KILL_ORDER);
//				popTarget();
////				Vector<Integer> tList = getAllPlayingLoc();
//////				for (int i=0; i<tList.size(); i++) {
//////					Tools.println("PS IA 追杀 正在游戏中的玩家位置 " + tList.elementAt(i));
//////				}
////				if(tList != null && tList.size() > 0) {
////					ItemID = getItemID(E_RIT_KILL_ORDER);
////					int index=(int)(Math.random()*tList.size());
////					userSeatID = new Vector<Integer>();
////					userSeatID.add(new Integer(tList.get(index)));
////					if(ItemID != null) {
////						requestUseItem(ItemID, userSeatID);
////					}
////				}
////				hideItemThing();
//				return true;
//			case GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_TARGET_CONFIRM://确认 选择目标
//				hideItemThing();
//				if (ibTest) {
//					//模拟游戏逻辑
//					for (int i=0; i<iSelectTargetLocList.size(); i++) {
//						int tLoc = iSelectTargetLocList.elementAt(i);
//						iServerUseItemID = iUseItemID;
//						setItemAnim(0, tLoc);
//					}
//					int tItemKey = getItemKey(iUseItemID);
//					if (tItemKey == 4) {
//						gameDuel(iSerLocation);
//					}
//				}
//				else {
//					//正常游戏逻辑
//					requestUseItem(iUseItemID, iSelectTargetLocList);
//				}
//				return true;
//			}
//		}
//		return false;
//	}
//	
//	//把后台桌子的唯一位置转换成客户端的位置：自己位置为0，按顺时针方向
//	private int transLocation(int aLocation) {
//		int tLocation = 0;
//		int tMaxPlayerNum = 6;//最大玩家数
//		tLocation = (tMaxPlayerNum + aLocation - iSerLocation)%tMaxPlayerNum;
//		Tools.println("ProgressScreen transLocation iSerLocation=" +iSerLocation +" -------- "+aLocation+ " TO " +tLocation);
//		return tLocation;
//	}
//	
//	/**
//	 * 根据后台数据 设置玩家
//	 * @param aLocation 后台位置
//	 * @param aUID UID
//	 * @param aName 名字
//	 * @param aHP 醉酒度
//	 * @param aRoleID 角色模型ID
//	 */
//	private void setPlayer(int aLocation, String aHeadPicName, String aName, int aHP, int aRoleID, int aDicePakID) {
//		int tClientLoc = 0;//客户端位置
//		tClientLoc = transLocation(aLocation);
//		TPoint tSpriteLoc = iPlayerLocationList.get(tClientLoc);//精灵的坐标
//		TPoint tHPLoc = iPlayerHPLocationList.get(tClientLoc);//醉酒度的坐标
//		TPoint tNameLoc = iPlayerNameLocationList.get(tClientLoc);//名字的坐标
//		Project tPlayerPak = getPlayerPak(aRoleID);
//		
//		if (tSpriteLoc==null) {
//			return;
//		}
//		if (tHPLoc==null) {
//			return;
//		}
//		if (tNameLoc==null) {
//			return;
//		}
//		
//		if (tClientLoc == 0) {
//			//自己的
//			iPlayerHPManager.setPlayerHPSrite(iPlayerHPPak, aLocation, tHPLoc.iX, tHPLoc.iY, aHP);
//			iPlayerNameManager.setPlayerNameSprite(iPlayerNameMyPak, aLocation, tNameLoc.iX, tNameLoc.iY);
//		}
//		else {
//			//其它玩家的
////			iPlayerManager.setPlayer(iPlayerPak, tClientLoc-1, aLocation, tSpriteLoc.iX, tSpriteLoc.iY);
//			iPlayerHPManager.setPlayerHPSrite(iPlayerHPPak, aLocation, tHPLoc.iX, tHPLoc.iY, aHP);
//			iPlayerNameManager.setPlayerNameSprite(iPlayerNamePak, aLocation, tNameLoc.iX, tNameLoc.iY);
//		}
//		iPlayerManager.setPlayer(tPlayerPak, tClientLoc, aLocation, tSpriteLoc.iX, tSpriteLoc.iY, aHeadPicName, aDicePakID);
//		SpriteButton tSB = iPlayerNameBGSBList.get(tClientLoc);
//		if (tSB != null) {
//			tSB.setVision(true);//显示名字底板
//		}
//		iPlayerNameStrList.put(tClientLoc, aName);
//		updatePlayerHP(aLocation, aHP);
//	}
//	
//	/**
//	 * 更新玩家的醉酒度
//	 * @param aLocation 后台位置
//	 * @param aHP 醉酒度
//	 */
//	private void updatePlayerHP(int aLocation, int aHP) {
//		iPlayerHPManager.updateHP(aLocation, aHP);
//		iPlayerManager.setPlayerAction(aLocation, aHP);
//	}
//	
//	/**
//	 * 某玩家喝酒
//	 * @param aLocation 后台位置
//	 * @param aHP 喝酒后的醉酒度
//	 */
//	private void playerDrink(int aLocation, int aHP) {
//		int tRoleID = 0;//角色ID
//		tRoleID = iPlayerManager.getPlayerRoleID(aLocation);
//		switch (tRoleID) {
//		case 0:
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.water, 1);
//			MediaManager.getMediaManagerInstance().playSoundDelayed(SoundsResID.burp1, 0, 300);
//			break;
//		case 1:
//			FeMaleMediaManager.getMediaManagerInstance().playerSound(SoundsResID.water, 1);
//			FeMaleMediaManager.getMediaManagerInstance().playSoundDelayed(SoundsResID.burp1, 0, 1500);
//			break;
//
//		default:
//			break;
//		}
//		if(aLocation == iSerLocation) {
//			iBeer--;
//		}
//		iPlayerManager.playerDrink(aLocation, aHP);
//	}
//	
//	/**
//	 * description 某玩家喝醉了
//	 * @param aLocation 后台位置
//	 */
//	public void playerDrinkStun(int aLocation) {
//		if (ibTest) {
//			//模拟游戏逻辑
//			if (aLocation == iSerLocation) {
//				popBuyHP();
//			}
//		}
//		else {
//			//正常游戏逻辑
//			if (aLocation == iSerLocation) {
//				popBuyHP();
//			}
//		}
//	}
//	
//	/**
//	 * description 某玩家喝酒动作结束
//	 * @param aLocation 后台位置
//	 */
//	public void playerDrinkEnd(int aLocation) {
//		messageEvent( MessageEventID.EMESSAGE_EVENT_SOCKET_ShowDrunkMsg);
//		if (aLocation == iSerLocation) {
//			if (iBeer <= 0) {
//				popBuyBeer();
//			}
//		}
//	}
//	
//	/**
//	 * 游戏进入准备阶段
//	 */
//	private void gameReady() {
//		iState = GAME_STATE_READY;
//		iReadyTime = 10;
//		iTGameTick = 0;
//		if (iDiceCupSB != null) {
//			//特殊情况：准备中，桌子上的人走剩自己后，再有人在该桌子坐下，后台会再次notify一次准备，这时把骰盅隐藏
//			iDiceCupSB.setVision(false);
//		}
//		if (iReadySB != null) {
//			iReadySB.setVision(true);
//		}
//		if (iReadyTimeSB != null) {
//			iReadyTimeSB.setVision(true);
//		}
//		allPlayerHideCall();
//		allPlayerHideDiceCup();
//	}
//	
//	/**
//	 * 某玩家已准备
//	 * @param aLocation 后台位置
//	 */
//	private void playerReady(int aLocation) {
//		iPlayerNameManager.playerReady(aLocation);
//		iPlayerManager.playerShowDiceCup(aLocation);
//	}
//	
////	/**
////	 * 所有玩家重置名字
////	 */
////	private void allPlayerResetName() {
////		iPlayerNameManager.allPlayerResetName();
////	}
//	
//	/**
//	 * 所有玩家隐藏喊点框
//	 */
//	private void allPlayerHideCall() {
//		iPlayerNameManager.allPlayerHideCall();
//	}
//	
//	/**
//	 * 所有玩家隐藏自己的骰盅
//	 */
//	private void allPlayerHideDiceCup() {
//		iPlayerManager.allPlayerHideDiceCup();
//	}
//	
//	/**
//	 * description 测试用 模拟游戏逻辑
//	 */
//	private void testPerformL() {
//		if (icanT) {
//			iTGameTick ++;
//			if (iTGameTick >= 10) {
//				iTGameTick = 0;
//				icanT = false;
//				gameReady();
//			}
//		}
//		readyPerformL();
//		rollPerformL();
//		gamingPerformL();
//		duelPerformL();
//		drinkPerformL();
//		addScorePerformL();
//		autoBuyPerformL();
//		
//	}
//	
//	private void readyPerformL() {
//		if (iState != GAME_STATE_READY) {
//			return;
//		}
//		if (iReadyTime>0) {
//			iTGameTick ++;
//			if (iTGameTick >= 10) {
//				iTGameTick = 0;
//				iReadyTime--;
//			}
//		}
//		else {
//			ItemAction(null, GameEventID.ESPRITEBUTTON_EVENT_GAME_READY);
//		}
//		playerReady(iTGameTick);
////		if (iReadyTime>=0 && iReadyTime<=5) {
////			playerChat(iReadyTime, "刚不刚快点啊啊点一口刚不刚快点啊啊点二国刚不刚快点啊啊点三团刚不刚快");
////		}
//	}
//	
//	private void rollPerformL() {
//		if (iState != GAME_STATE_ROLL) {
//			return;
//		}
//		iTGameTick ++;
//		if (iTGameTick >= 11) {
//			iTGameTick = 0;
//			if (iDiceCupSB != null) {
//				iDiceCupSB.released();
//				iDiceCupSB.setVision(false);
//			}
//			gameStart();
//		}
//	}
//	
//	private void gamingPerformL() {
//		if (iState != GAME_STATE_GAMING) {
//			return;
//		}
//		analysisDicePerformL();
//		//轮到自己
//		if (iTCallLoc == iSerLocation) {
//			if (iTurnTime>0) {
//				iTurnTick ++;
//				if (iTurnTick >= 10) {
//					iTurnTick = 0;
//					iTurnTime--;
//				}
//			}
//			//到时间了
////			if (iPlayerNameManager.playerTurnEnd(iSerLocation)) {
//			if (iTurnTime <= 0) {
//				iTGameTick = 0;
//				if (ibCanCall) {
//					//默认喊一个
//					defaultCall();
//					int tNum = iDefNum;
//					int tDiceP = iDefPoint;
//					if (ibCallSafe) {
//						if (iWheelSB != null && iWheelSB.isVision()) {
//							tNum = iWheelSB.getValue();
//							tDiceP = iWheelDiceSB.getValue();
//						}
//					}
//					if (tDiceP == 7) {//7点 就是 1点
//						tDiceP = 1;
//					}
//					playerCall(iSerLocation, tNum, tDiceP);
//					iTCallLoc ++;
//					if (iTCallLoc>5) {
//						iTCallLoc = 0;
//					}
//					playerTurn(iTCallLoc);
//					closeDiceSelect();
//				}
//				else {
//					//劈
//					ItemAction(null, GameEventID.ESPRITEBUTTON_EVENT_GAME_DUEL);
//				}
//			}
//		}
//		else {
//			iTGameTick ++;
//			if (iTGameTick >= 50) {
//				iTGameTick = 0;
//				playerCall(iTCallLoc, iLastNum+1, 4);
//				iTCallLoc ++;
//				if (iTCallLoc>5) {
//					iTCallLoc = 0;
//				}
//				playerTurn(iTCallLoc);
//			}
//		}
//	}
//	
//	private void duelPerformL() {
//		if (iState != GAME_STATE_DUEL) {
//			return;
//		}
//		int tSecond = 3;//每次展现的秒数
//		int tPerTicks = 10*tSecond;//每次展现的tick总数
//		
//		int tShowIndex = 0;//第几次展现玩家骰子
//		int playerCount = 6;
//		if (iTGameTick%tPerTicks == 0) {
//			tShowIndex = iTGameTick/tPerTicks;
//			if (tShowIndex < playerCount) {
//				int tShowLoc = 0;//展现骰子的玩家 后台位置
//				if (tShowIndex < playerCount) {
//					if (iLastShowDiceLoc == -1) {
//						tShowLoc = iDuelLoc;//从劈的玩家 开始
//					}
//					else{
////					tShowLoc = (iLastShowDiceLoc -1 +6)%6;
//						if ((iLastShowDiceLoc-1) < 0) {
//							tShowLoc = 5;
//						}
//						else {
//							tShowLoc = iLastShowDiceLoc-1;
//						}
//					}
//					hideAllDiceBG();
//					iDiceManager.clear();//清除所有其它骰子
//					iCurShowDiceLoc = tShowLoc;
//					iPlayerManager.openDiceCup(iCurShowDiceLoc);
//				}
//			}
//		}
//		if (iTGameTick%tPerTicks == 9) {
//			tShowIndex = iTGameTick/tPerTicks;
//			if (tShowIndex < playerCount) {
//				iLastShowDiceLoc = iCurShowDiceLoc;
//				displayDiceBG(iCurShowDiceLoc);
//				int tPlayerArrayIndex = 0;//这次要显示骰子的玩家于后台骰子数据数组的第一个index(表示玩家)
//				int tPlayerCount = 0;//这次展现的命中骰子数量
//				for (int i=0; i<5; i++) {
//					int tPoint = i;
//					if (tPoint == 0) {
//						tPoint = 1;
//					}
//					if (tPoint == iResultPoint || (!ibZhai && tPoint==1)) {
//						tPlayerCount ++;
//					}
//					setOtherPDice(iCurShowDiceLoc, i, tPoint);
//				}
//				
//				String tText = "本局结果";
//				iResultCount += tPlayerCount;
//				int tCount = iResultCount;//个数
//				int tPoint = iResultPoint;//点数
//				setDuelContent(tText, tCount, tPoint);
//				if (iCurShowDiceLoc == iSerLocation) {
//					allMyDiceSetShade();
//					setMyKeyDice(tPoint);
//					if (!ibZhai) {
//						setMyKeyDice(1);
//					}
//				}
//				else {
//					allDiceSetShade();
//					setKeyDice(tPoint);
//					if (!ibZhai) {
//						setKeyDice(1);
//					}
//				}
//			}
//		}
//		
//		iTGameTick ++;
//		if (iTGameTick >= (playerCount+1)*tPerTicks) {
//			iTGameTick = 0;
//			gameDrink();
//		}
//	}
//	
//	private void drinkPerformL() {
//		if (iState != GAME_STATE_DRINK) {
//			return;
//		}
//		iTGameTick ++;
//		if (iTGameTick >= 60) {
//			iTGameTick = 0;
////			gameReady();
////			playerReady(1);
//			gameAddScore();
//		}
//	}
//	
//	private void addScorePerformL() {
//		if (iState != GAME_STATE_ADDSCORE) {
//			return;
//		}
//		iTGameTick ++;
//		if (iTGameTick >= 60) {
//			iTGameTick = 0;
//			gameReady();
//		}
//	}
//	
//	//看看能不能喊
//	private void analysisDicePerformL() {
//		if (iState != GAME_STATE_GAMING) {
//			return;
//		}
//		matchDicePoint();
//		if (iWheelSB != null && iWheelSB.isVision()) {
//			int tNum = iWheelSB.getValue();
//			int tDiceP = iWheelDiceSB.getValue();
////			Tools.debug("ProgressScreen analysisDicePerformL  iLastNum X iLastPoint = " + iLastNum+ " X " + iLastPoint);
//			if (iCallSB != null) {
//				if (tNum*10+tDiceP <= iLastNum*10+iLastPoint) {
//					iCallSB.setVision(false);
//					ibCallSafe = false;
//				}
//				else {
//					iCallSB.setVision(true);
//					ibCallSafe = true;
//				}
//			}
////			if (iCallConfirmSB != null) {
////				if (tNum*10+tDiceP <= iLastNum*10+iLastPoint) {
////					iCallConfirmSB.setVision(false);
////					ibCallSafe = false;
////				}
////				else {
////					iCallConfirmSB.setVision(true);
////					ibCallSafe = true;
////				}
////			}
//		}
//	}
//	
//	/**
//	 * description 调整点数选择
//	 */
//	private void matchDicePoint() {
//		if (iState != GAME_STATE_GAMING) {
//			return;
//		}
//		if (iWheelSB != null && iWheelSB.isVision() && iWheelDiceSB != null && iWheelDiceSB.isVision()) {
//			int tNum = iWheelSB.getValue();
//			int tDiceP = iWheelDiceSB.getValue();
////			Tools.debug("ProgressScreen matchDicePoint  iLastNum X iLastPoint = " + iLastNum+ " X " + iLastPoint);
//			if (tNum != iLastSelectedNum) {
//				//选择骰子数 发生变化
//				if (tNum > iLastNum) {
//					iWheelDiceSB.setMinPoint(2);
//					iWheelDiceSB.setDefPoint(2);
//				}
//				else if (tNum == iLastNum) {
//					iWheelDiceSB.setMinPoint(iLastPoint+1);
//					iWheelDiceSB.setDefPoint(iLastPoint+1);
//				}
//				if (tNum == iLastNum) {
//					//当前数量与上家喊的数量相同
//					if (tNum*10+tDiceP <= iLastNum*10+iLastPoint) {
//						//非法喊点，重新排列点数
//						iWheelDiceSB.readjustAllItem();
//					}
//				}
//				iLastSelectedNum = tNum;
//			}
//		}
//	}
//	
//	//自动买酒 界面计时并自动关闭
//	private void autoBuyPerformL() {
//		if (iPopScene!=null && iPopScene.getIndex()==SCENE_INDEX_BUYBEER_NOTICE) {
//			iAutoBuyTick++;
//			if (iAutoBuyTick>=30) {
//				closeAutoBuyBeer();
//			}
//		}
//	}
//	
//	/**
//	 * description 正常游戏逻辑
//	 */
//	private void realPerformL() {
//		readyRealPerformL();
//		rollRealPerformL();
//		gamingRealPerformL();
//		duelRealPerformL();
//		resultRealPerformL();
//		irregularResultRealPerformL();
//		drinkRealPerformL();
//		addScoreRealPerformL();
//		autoBuyPerformL();
//		
//	}
//	
//	private void readyRealPerformL() {
//		if (iState != GAME_STATE_READY) {
//			return;
//		}
//		if (iReadyTime>0) {
//			iTGameTick ++;
//			if (iTGameTick >= 10) {
//				iTGameTick = 0;
//				iReadyTime--;
//			}
//		}
//		else {
//			if(iReadyTime == 0) {
//				ItemAction(null, GameEventID.ESPRITEBUTTON_EVENT_GAME_READY);
//				iReadyTime = -1;
//			}
//		}
//	}
//	
//	private void rollRealPerformL() {
//		if (iState != GAME_STATE_ROLL) {
//			return;
//		}
//		iTGameTick ++;
//		if (iTGameTick >= 11) {
//			iTGameTick = 0;
//			if (iDiceCupSB != null) {
//				iDiceCupSB.released();
//				iDiceCupSB.setVision(false);
//			}
//			gameStart();
//		}
//	}
//	
//	private void gamingRealPerformL() {
//		if (iState != GAME_STATE_GAMING) {
//			return;
//		}
//		analysisDicePerformL();
//		//轮到自己
//		if (iTCallLoc == iSerLocation) {
//			if (iTurnTime>0) {
//				iTurnTick ++;
//				if (iTurnTick >= 10) {
//					iTurnTick = 0;
//					iTurnTime--;
//				}
//			}
//			//到时间了
////			if (iPlayerNameManager.playerTurnEnd(iSerLocation)) {
//			if (iTurnTime <= 0) {
//				iTGameTick = 0;
//				if (ibCanCall) {
//					//默认喊一个
//					defaultCall();
//					int tNum = iDefNum;
//					int tDiceP = iDefPoint;
//					if (ibCallSafe) {
//						if (iWheelSB != null && iWheelSB.isVision()) {
//							tNum = iWheelSB.getValue();
//							tDiceP = iWheelDiceSB.getValue();
//						}
//					}
//					if (tDiceP == 7) {//7点 就是 1点
//						tDiceP = 1;
//					}
////					playerCall(iSerLocation, tNum, tDiceP);
//					GameMain.iGameProtocol.RequsetShout(iUserID, tDiceP, tNum);
//					closeDiceSelect();
//				}
//				else {
//					//劈
//					ItemAction(null, GameEventID.ESPRITEBUTTON_EVENT_GAME_DUEL);
//				}
//				iTCallLoc = -1;
//			}
//		}
//	}
//	
//	private void duelRealPerformL() {
//		if (iState != GAME_STATE_DUEL) {
//			return;
//		}
//	}
//	
//	/**
//	 * description 结果演算
//	 */
//	private void resultRealPerformL() {
//		if (iState != GAME_STATE_RESULT) {
//			return;
//		}
//		int tSecond = 3;//每次展现的秒数
//		int tPerTicks = 10*tSecond;//每次展现的tick总数
//		
//		int tShowIndex = 0;//第几次展现玩家骰子
//		int playerCount = iNotifyResult.iPlayerCount;
//		if (iTGameTick%tPerTicks == 0) {
//			tShowIndex = iTGameTick/tPerTicks;
//			if (tShowIndex < playerCount) {
//				int tShowLoc = 0;//展现骰子的玩家 后台位置
//				if (iLastShowDiceLoc == -1) {
//					tShowLoc = iDuelLoc;//从劈的玩家 开始
//				}
//				else {
//					//找出有在玩的上一个玩家 后台位置
//					int tTempShowLoc = (iLastShowDiceLoc -1 +6)%6;//可能有在玩的玩家后台位置
//					boolean tbFind = false;//是否找到
//					while(!tbFind) {
//						for (int j = 0; j < playerCount; j++) {
//							int seatID = iNotifyResult.iSeatIDs[j];
//							if (tTempShowLoc == seatID) {
//								tbFind = true;
//								tShowLoc = tTempShowLoc;
//								break;
//							}
//						}
//						tTempShowLoc = (tTempShowLoc -1 +6)%6;//位置-1，下一个可能有在玩的玩家后台位置
//					}
//				}
//				hideAllDiceBG();
//				iDiceManager.clear();//清除所有其它骰子
//				iCurShowDiceLoc = tShowLoc;
//				iPlayerManager.openDiceCup(iCurShowDiceLoc);
//			}
//		}
//		if (iTGameTick%tPerTicks == 9) {
//			tShowIndex = iTGameTick/tPerTicks;
//			if (tShowIndex < playerCount) {
//				iLastShowDiceLoc = iCurShowDiceLoc;
//				displayDiceBG(iCurShowDiceLoc);
//				int tPlayerArrayIndex = 0;//这次要显示骰子的玩家于后台骰子数据数组的第一个index(表示玩家)
//				int tPlayerCount = 0;//这次展现的命中骰子数量
//				for (int j = 0; j < playerCount; j++) {
//					int seatID = iNotifyResult.iSeatIDs[j];
//					if (iCurShowDiceLoc == seatID) {
//						tPlayerArrayIndex = j;
//						break;
//					}
//				}
//				for (int i = 0; i < BufferSize.MAX_DICE_COUNT; i++) {
//					int dice = iNotifyResult.iDices[tPlayerArrayIndex][i];
//					if (dice == iResultPoint || (!ibZhai && dice==1)) {
//						tPlayerCount ++;
//					}
//					setOtherPDice(iCurShowDiceLoc, i, dice);
//				}
//				
//				String tText = "本局结果";
//				iResultCount += tPlayerCount;
//				int tCount = iResultCount;//个数
//				int tPoint = iResultPoint;//点数
//				setDuelContent(tText, tCount, tPoint);
//				if (iCurShowDiceLoc == iSerLocation) {
//					allMyDiceSetShade();
//					setMyKeyDice(tPoint);
//					if (!ibZhai) {
//						setMyKeyDice(1);
//					}
//				}
//				else {
//					allDiceSetShade();
//					setKeyDice(tPoint);
//					if (!ibZhai) {
//						setKeyDice(1);
//					}
//				}
//			}
//		}
//		
//		iTGameTick ++;
//		if (iTGameTick >= (playerCount+1)*tPerTicks) {
//			iTGameTick = 0;
//			gameDrink();
//		}
//	}
//	
//	/**
//	 * description 非正常结果演算
//	 */
//	private void irregularResultRealPerformL() {
//		if (iState != GAME_STATE_RESULT_IRREGULAR) {
//			return;
//		}
//	}
//	
//	private void drinkRealPerformL() {
//		if (iState != GAME_STATE_DRINK) {
//			return;
//		}
//		iTGameTick ++;
//		if (iTGameTick >= 50) {
//			iTGameTick = 0;
//			gameAddScore();
//		}
//	}
//	
//	private void addScoreRealPerformL() {
//		if (iState != GAME_STATE_ADDSCORE) {
//			return;
//		}
//	}
//	
//	/**
//	 * 设置自己的骰子
//	 * @param aLocation 骰子从左到右的位置，0~4
//	 * @param aPoint 点数
//	 */
//	private void setDice(int aLocation, int aPoint) {
//		Project tPak = null;//骰子pak
//		int tPakID = 0;//骰子pakID
//		tPakID = iPlayerManager.getPlayerDicePakID(iSerLocation);
//		tPak = iBigDicePakList.get(tPakID);
//		if (tPak == null) {
//			tPak = iBigDicePakList.get(0);
//		}
//		TPoint tDiceLoc = iDiceLocationList.get(aLocation);//骰子的坐标
//		iMyDiceManager.setDiceSrite(tPak, aLocation, tDiceLoc.iX, tDiceLoc.iY, aPoint);
//	}
//	
//	private void gameStart() {
//		if (iRule == RULE_ONE_CANNOT_CHANGE) {
//			ibZhai = true;
//		}
//		else {
//			ibZhai = false;
//		}
//		iLastNum = 0;
//		iLastPoint = 0;
//		ibCallOne = false;
//		ibCallSafe = false;
//		ibCanCall = false;
//		iState = GAME_STATE_GAMING;
////		iPlayerNameManager.allPlayerResetName();
//		if (ibTest) {
//			//模拟游戏逻辑
//			for (int i=0; i<5; i++) {
//				setDice(i, i+1);
//			}
//			playerTurn(5);
////			popBuyHP();
//		}
//		else {
//			//正常游戏逻辑
//			MBNotifyStart notifyStart = (MBNotifyStart)GameMain.iGameProtocol.getMobileMsgCS(NotifyMsg.MSG_NOTIFY_START);
//			if(notifyStart != null) {
//				int dicesize = notifyStart.iDice.size();
//				for(int i = 0; i < dicesize; i++) {
//					int point = notifyStart.iDice.get(i);
//					setDice(i, point);
//				}
//				playerTurn(notifyStart.iShouterSeatID);
//			}
//		}
//	}
//	
//	/**
//	 * 轮到某玩家
//	 * @param aLocation 后台位置
//	 */
//	private void playerTurn(int aLocation) {
//		iTCallLoc = aLocation;
//		iPlayerNameManager.playerTurn(aLocation);
//		iPlayerManager.playerShowDiceCup(aLocation);
//		if (aLocation == iSerLocation) {
//			hideItemThing();
//			if (iItemSB != null) {
//				iItemSB.setVision(false);
//			}
//			if (iQuickDuelSB != null) {
//				iQuickDuelSB.setVision(false);
//			}
//			if (iQuickDuelNumBGSB != null) {
//				iQuickDuelNumBGSB.setVision(false);
//			}
//			if (iReadyTimeSB != null) {
//				iReadyTimeSB.setVision(true);
//			}
//			iTurnTick = 0;
//			iTurnTime = 30;
//			popDiceSelectNotScene();
//			//轮到自己
//			ibCanCall = true;
//			if (iWheelSB != null) {
//				iWheelSB.setVision(true);
//			}
//			if (iWheelDiceSB != null) {
//				iWheelDiceSB.setVision(true);
//			}
//			iCallSB.setVision(true);
//			iDuelSB.setVision(true);
//			if (iLastNum>5*iPlayingCount || iLastPoint>7) {
//				//个数 或 点数 喊过顶，应该是出bug了
//				ibCanCall = false;
//				iCallSB.setVision(false);
//				if (iWheelSB != null) {
//					iWheelSB.setVision(false);
//				}
//				if (iWheelDiceSB != null) {
//					iWheelDiceSB.setVision(false);
//				}
//			}
//			else if (iLastNum==5*iPlayingCount && iLastPoint==7) {
//				//个数 点数 都喊到顶了
//				ibCanCall = false;
//				iCallSB.setVision(false);
//				if (iWheelSB != null) {
//					iWheelSB.setVision(false);
//				}
//				if (iWheelDiceSB != null) {
//					iWheelDiceSB.setVision(false);
//				}
//			}
//			if (iLastNum <= 0) {
//				//我第一个 喊，不能【劈】
//				iDuelSB.setVision(false);
//			}
//		}
//		else {
//			closeDiceSelectNotScene();
//			if (ibTest) {
//				if (iItemSB != null) {
//					iItemSB.setVision(true);
//				}
//				if (iLastLoc != iSerLocation) {//如果喊点的上一家不是自己，可以抢开
//					int pQDKey = MAP_ITEM_KEY[E_RTI_VIE_CHOP];
//					ArtNumber pArtNum = iItemArtNumList.get(pQDKey);
//					if (iItemCanUseList.get(pQDKey) == 1) {
//						if (iQuickDuelSB != null) {
//							iQuickDuelSB.setVision(true);
//						}
//						if (iQuickDuelNumBGSB != null) {
//							iQuickDuelNumBGSB.setVision(true);
//						}
//						if (pArtNum != null) {
//							pArtNum.setDraw(true);
//						}
//					}
//					else {
//						if (pArtNum != null) {
//							pArtNum.setDraw(false);
//						}
//					}
//				}
//			}
//		}
//	}
//	
//	private void playerTurn(int aLocation, boolean DuelSB) {
//		iTCallLoc = aLocation;
//		iPlayerNameManager.playerTurn(aLocation);
//		iPlayerManager.playerShowDiceCup(aLocation);
//		if (aLocation == iSerLocation) {
//			hideItemThing();
//			if (iItemSB != null) {
//				iItemSB.setVision(false);
//			}
//			if (iQuickDuelSB != null) {
//				iQuickDuelSB.setVision(false);
//			}
//			if (iQuickDuelNumBGSB != null) {
//				iQuickDuelNumBGSB.setVision(false);
//			}
//			if (iReadyTimeSB != null) {
//				iReadyTimeSB.setVision(true);
//			}
//			iTurnTick = 0;
//			iTurnTime = 30;
//			popDiceSelectNotScene();
//			//轮到自己
//			ibCanCall = true;
//			if (iWheelSB != null) {
//				iWheelSB.setVision(true);
//			}
//			if (iWheelDiceSB != null) {
//				iWheelDiceSB.setVision(true);
//			}
//			iCallSB.setVision(true);
//			iDuelSB.setVision(true);
//			if (iLastNum>5*iPlayingCount || iLastPoint>7) {
//				//个数 或 点数 喊过顶，应该是出bug了
//				ibCanCall = false;
//				iCallSB.setVision(false);
//				if (iWheelSB != null) {
//					iWheelSB.setVision(false);
//				}
//				if (iWheelDiceSB != null) {
//					iWheelDiceSB.setVision(false);
//				}
//			}
//			else if (iLastNum==5*iPlayingCount && iLastPoint==7) {
//				//个数 点数 都喊到顶了
//				ibCanCall = false;
//				iCallSB.setVision(false);
//				if (iWheelSB != null) {
//					iWheelSB.setVision(false);
//				}
//				if (iWheelDiceSB != null) {
//					iWheelDiceSB.setVision(false);
//				}
//			}
////			if (iLastNum <= 0) {
////				//我第一个 喊，不能【劈】
//				iDuelSB.setVision(DuelSB);
////			}
//		}
//		else {
//			closeDiceSelectNotScene();
//			if (ibTest) {
//				if (iItemSB != null) {
//					iItemSB.setVision(true);
//				}
//				if (iLastLoc != iSerLocation) {//如果喊点的上一家不是自己，可以抢开
//					int pQDKey = MAP_ITEM_KEY[E_RTI_VIE_CHOP];
//					ArtNumber pArtNum = iItemArtNumList.get(pQDKey);
//					if (iItemCanUseList.get(pQDKey) == 1) {
//						if (iQuickDuelSB != null) {
//							iQuickDuelSB.setVision(true);
//						}
//						if (iQuickDuelNumBGSB != null) {
//							iQuickDuelNumBGSB.setVision(true);
//						}
//						if (pArtNum != null) {
//							pArtNum.setDraw(true);
//						}
//					}
//					else {
//						if (pArtNum != null) {
//							pArtNum.setDraw(false);
//						}
//					}
//				}
//			}
//		}
//	}
//	
//	/**
//	 * 某玩家喊了
//	 * @param aLocation 后台位置
//	 * @param aCount 骰子个数
//	 * @param aType 骰子的类型(点数)
//	 */
//	private void playerCall(int aLocation, int aCount, int aType) {
//		if (aLocation == iSerLocation) {
//			//若是后台通知自己喊了点，关闭相关的东西
//			closeDiceSelectNotScene();
//		}
//		iLastLoc = aLocation;
//		iLastNum = aCount;
//		iLastPoint = aType;
//		Project tPak = getSmallDicePak(aLocation);
//		iPlayerNameManager.playerCall(aLocation, aCount, aType, tPak);
////		iPlayerNameManager.playerCall(aLocation, aCount, aType);
////		String tName = iPlayerNameManager.getName(aLocation);
//		int tClientLoc = 0;//客户端位置
//		tClientLoc = transLocation(aLocation);
//		String tName = iPlayerNameStrList.get(tClientLoc);
//		setCallContent(aLocation, tName, aCount, aType);
//		if (aType == 1) {
//			iLastPoint = 7;//1点 转成 7点
//			ibCallOne = true;
//			if (iRule != RULE_ONE_CAN_CHANGE) {
//				ibZhai = true;
//			}
//		}
//		if (ibZhai) {
//			iPlayerNameManager.playerZhai(aLocation);
//		}
//		int tRoleID = 0;//角色ID
//		tRoleID = iPlayerManager.getPlayerRoleID(aLocation);
//		CallPointManager.getSoundManager().CallPoint(tRoleID, aCount, aType);
//	}
//	
//	/**
//	 * 设置 喊的内容(中间的，不是玩家名字的)
//	 * @param aLocation 玩家后台位置
//	 * @param aName 玩家名字
//	 * @param aCount 个数
//	 * @param aPoint 点数
//	 */
//	private void setCallContent(int aLocation, String aName, int aCount, int aPoint) {
////		Project tPak = getSmallDicePak(aLocation);
//		iCallContentManager.setCallContentSrite(iDicePak, 
//				(int) (iX_CallBG[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X), 
//				(int) (iY_CallBG[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y), aName, aCount, aPoint);
//	}
//	
//	/**
//	 * 设置 结果内容
//	 * @param aText 文本，如：本局结果
//	 * @param aCount 个数
//	 * @param aPoint 点数
//	 */
//	private void setDuelContent(String aText, int aCount, int aPoint) {
//		iCallContentManager.setDuelContentSrite(iDicePak, 
//				(int) (iX_CallBG[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X), 
//				(int) (iY_CallBG[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y), aText, aCount, aPoint);
//	}
//	
//	/**
//	 * 某玩家劈
//	 * @param aLocation 后台位置
//	 */
//	private void playerDuel(int aLocation) {
////		if (aLocation == iSerLocation) {
////			//若是后台通知自己劈了，关闭相关的东西
////			closeDiceSelectNotScene();
////			hideItemThing();
////		}
//		//有人劈了，关闭相关的东西
//		closeDiceSelectNotScene();
//		hideItemThing();
//		int tRoleID = 0;//角色ID
//		tRoleID = iPlayerManager.getPlayerRoleID(aLocation);
//		switch (tRoleID) {
//		case 0:
//			MediaManager.getMediaManagerInstance().playerSound(SoundsResID.pi, 0);
//			break;
//		case 1:
//			FeMaleMediaManager.getMediaManagerInstance().playerSound(SoundsResID.pi, 0);
//			break;
//
//		default:
//			break;
//		}
//		iPlayerNameManager.playerDuel(aLocation);
//		if (ibZhai) {
//			iPlayerNameManager.playerZhai(aLocation);
//		}
//	}
//	
//	/**
//	 * description 某玩家观看
//	 * @param aLocation 后台位置
//	 */
//	private void playerLook(int aLocation) {
//		iPlayerNameManager.playerLook(aLocation);
//	}
//	
//	private void gameDuel(int aLocation) {
//		iState = GAME_STATE_DUEL;
//		iTGameTick = 0;
//		iDuelLoc = aLocation;
//		int tLocation = aLocation;//劈的玩家 后台位置
//		if (ibTest) {
//			//模拟游戏逻辑，直接出结果
//			iLastShowDiceLoc = -1;
//			iResultCount = 0;
//			iResultPoint = iLastPoint;
////			String tText = "本局结果";
////			int tCount = 4;//个数
////			int tPoint = 2;//点数
////			for (int j=0; j<6; j++) {
////				displayDiceBG(j);
////				for (int i=0; i<5; i++) {
////					setOtherPDice(j, i, i);
////				}
////			}
////			setDuelContent(tText, tCount, tPoint);
////			allDiceSetShade();
////			setKeyDice(tPoint);
////			switch (iRule) {
////			case RULE_ONE_CAN_CHANGE:
////				setKeyDice(1);
////				break;
////			case RULE_ONE_CANNOT_CHANGE:
////				break;
////			}
//		}
//		else {
//			//正常游戏逻辑，没有结果
//		}
//		
//		playerDuel(tLocation);
//	}
//	
//	/**
//	 * description 获取大骰子，查找失败时会返回一个默认的
//	 * @param aLocation 后台位置
//	 * @return
//	 */
//	private Project getBigDicePak(int aLocation) {
//		Project tPak = null;//骰子pak
//		int tPakID = 0;//骰子pakID
//		if (iPlayerManager != null) {
//			tPakID = iPlayerManager.getPlayerDicePakID(aLocation);
//		}
//		tPak = iBigDicePakList.get(tPakID);
//		if (tPak == null) {
//			tPak = iBigDicePakList.get(0);
//		}
//		return tPak;
//	}
//	
//	/**
//	 * description 获取小骰子，查找失败时会返回一个默认的
//	 * @param aLocation 后台位置
//	 * @return
//	 */
//	private Project getSmallDicePak(int aLocation) {
//		Project tPak = null;//骰子pak
//		int tPakID = 0;//骰子pakID
//		tPakID = iPlayerManager.getPlayerDicePakID(aLocation);
//		tPak = iSmallDicePakList.get(tPakID);
//		if (tPak == null) {
//			tPak = iSmallDicePakList.get(0);
//		}
//		return tPak;
//	}
//	
//	/**
//	 * 设置其他玩家的骰子
//	 * @param aLocation 玩家后台位置
//	 * @param aDiceLocation 该玩家的骰子位置，从左到右 0~4
//	 * @param aPoint 该位置骰子的点数
//	 */
//	private void setOtherPDice(int aLocation, int aDiceLocation, int aPoint) {
//		int tClientLoc = 0;//客户端位置
//		tClientLoc = transLocation(aLocation);
//		TPoint tFirstLoc = iOtherPFirstDiceLocList.get(tClientLoc);
//		if (tFirstLoc == null) {
//			return;
//		}
//		int tDiceX = tFirstLoc.iX;
//		int tDiceY = tFirstLoc.iY;
////		int tSpace = 0;//骰子的间距
////		int tDiceW = 30;//骰子的宽度
////		tDiceX = tFirstLoc.iX + (tDiceW+tSpace)*aDiceLocation;
//		if (aDiceLocation < iX_Offset_Dice_Player[0].length) {
//			tDiceX = (int) (tFirstLoc.iX + iX_Offset_Dice_Player[ActivityUtil.TYPE_SCREEN][aDiceLocation]*ActivityUtil.PAK_ZOOM_X);
//			tDiceY = (int) (tFirstLoc.iY + iY_Offset_Dice_Player[ActivityUtil.TYPE_SCREEN][aDiceLocation]*ActivityUtil.PAK_ZOOM_Y);
//		}
//		int tLocInManager = tClientLoc*5 + aDiceLocation;//骰子在管理器的位置
//		Project tPak = null;//骰子pak
//		int tPakID = 0;//骰子pakID
//		tPakID = iPlayerManager.getPlayerDicePakID(aLocation);
//		tPak = iSmallDicePakList.get(tPakID);
//		if (tPak == null) {
//			tPak = iSmallDicePakList.get(0);
//		}
//		iDiceManager.setDiceSrite(tPak, tLocInManager, tDiceX, tDiceY, aPoint);
//	}
//	
//	/**
//	 * 把自己的骰子消失
//	 */
//	public void clearMyDice() {
//		iMyDiceManager.clear();
//	}
//	
//	/**
//	 * 把自己的骰子变成灰
//	 */
//	private void allMyDiceSetShade() {
//		iMyDiceManager.allDiceSetShade();
//	}
//	
//	/**
//	 * 把其它所有骰子变成灰
//	 */
//	private void allDiceSetShade() {
//		iDiceManager.allDiceSetShade();
//	}
//	
//	/**
//	 * 设置自己的关键骰子
//	 * @param aPoint 点数
//	 */
//	private void setMyKeyDice(int aPoint) {
//		iMyDiceManager.setKeyDice(aPoint);
//	}
//	
//	/**
//	 * 设置其它的关键骰子
//	 * @param aPoint 点数
//	 */
//	private void setKeyDice(int aPoint) {
//		iDiceManager.setKeyDice(aPoint);
//	}
//	
//	/**
//	 * 使其它玩家的骰子底板可见
//	 * @param aLocation 玩家后台位置
//	 */
//	private void displayDiceBG(int aLocation) {
//		int tClientLoc = 0;//客户端位置
//		tClientLoc = transLocation(aLocation);
//		SpriteButton tSB = iPlayerDiceBGSBList.get(tClientLoc);
//		if (tSB != null) {
//			tSB.setVision(true);
//		}
//	}
//	
//	private void gameResult() {
//		iState = GAME_STATE_RESULT;
//		iTGameTick = 0;
//		if (ibTest) {
//			//模拟游戏逻辑，劈状态时已处理结果了，这里不出结果
////			String tText = "本局结果";
////			int tCount = 4;//个数
////			int tPoint = 2;//点数
////			for (int j=0; j<6; j++) {
////				displayDiceBG(j);
////				for (int i=0; i<5; i++) {
////					setOtherPDice(j, i, i);
////				}
////			}
////			setDuelContent(tText, tCount, tPoint);
////			setKeyDice(tPoint);
////			if (!ibCallOne) {
////				setKeyDice(1);
////			}
//		}
//		else {
//			//正常游戏逻辑，结果处理
//			iLastShowDiceLoc = -1;
//			iResultCount = 0;//结果骰子数量清零
//			if(iNotifyResult != null) {
//				iResultPoint = iNotifyResult.iChopDice;
//			}
//			
////			int playerCount = iNotifyResult.iPlayerCount;
////			for (int j = 0; j < playerCount; j++) {
////				int seatID = iNotifyResult.iSeatIDs[j];
////				displayDiceBG(seatID);
////				for (int i = 0; i < BufferSize.MAX_DICE_COUNT; i++) {
////					int dice = iNotifyResult.iDices[j][i];
////					setOtherPDice(seatID, i, dice);
////				}
////			}
////			
////			String tText = "本局结果";
////			int tCount = 4;//个数
////			int tPoint = 2;//点数
////			if(iNotifyResult != null) {
////				tCount = iNotifyResult.iDiceCount;
////				tPoint = iNotifyResult.iChopDice;
////			}
////			setDuelContent(tText, tCount, tPoint);
////			allDiceSetShade();
////			setKeyDice(tPoint);
////			switch (iRule) {
////			case RULE_ONE_CAN_CHANGE:
////				setKeyDice(1);
////				break;
////			case RULE_ONE_CANNOT_CHANGE:
////				break;
////			}
//		}
//	}
//	
//	/**
//	 * description 游戏 不正常结果
//	 */
//	private void gameIrregularResult() {
//		iState = GAME_STATE_RESULT_IRREGULAR;
//		iTGameTick = 0;
//		if (ibTest) {
//			//模拟游戏逻辑
//		}
//		else {
//			//正常游戏逻辑
//			closePopScene();
//			if (iDiceCupSB != null) {
//				iDiceCupSB.released();
//				iDiceCupSB.setVision(false);
//			}
//			iReadySB.setVision(false);
//			closeDiceSelect();
//			iCallSB.setVision(false);
//			iDuelSB.setVision(false);
//			if (iItemSB != null) {
//				iItemSB.setVision(false);
//			}
//			if (iQuickDuelSB != null) {
//				iQuickDuelSB.setVision(false);
//			}
//			if (iQuickDuelNumBGSB != null) {
//				iQuickDuelNumBGSB.setVision(false);
//			}
//			hideAllDiceBG();
//			allPlayerHideCall();
//			allPlayerHideDiceCup();
//			clearAllDice();
//			clearAllCallContent();
//			removeAllItemAnim();
//		}
//	}
//	
//	private void gameDrink() {
//		iState = GAME_STATE_DRINK;
//		iTGameTick = 0;
//		hideAllDiceBG();
//		allPlayerHideCall();
//		allPlayerHideDiceCup();
//		clearAllDice();
//		clearAllCallContent();
//		removeAllItemAnim();
//		int tLocation = 5;//喝酒玩家的后台位置
//		int tHP = 6;//喝完酒之后的醉酒度
//		if (ibTest) {
//			//模拟游戏逻辑
//		}
//		else {
//			//正常游戏逻辑
//			if(iNotifyResult != null) {
//				tLocation = iNotifyResult.iFailSeatID;//喝酒玩家的后台位置
//				tHP = iNotifyResult.iFailCurrentDrunk;//喝完酒之后的醉酒度
//			}
//			else {
//				return;
//			}
//		}
//		playerDrink(tLocation, tHP);
//	}
//	
//	/**
//	 * description 游戏加奖励状态
//	 */
//	private void gameAddScore() {
//		iState = GAME_STATE_ADDSCORE;
//		iTGameTick = 0;
//		if (ibTest) {
//			//模拟游戏逻辑
//			iPlayerManager.playerResetScore(3);
//			iPlayerManager.playerAddPoint(3, 2);
//			iPlayerManager.playerAddGB(3, 100);
//			iPlayerManager.playerResetScore(4);
//			iPlayerManager.playerAddPoint(4, 2);
//			iPlayerManager.playerAddGB(4, 100);
//			iPlayerManager.playerResetScore(5);
//			iPlayerManager.playerAddPoint(5, 2);
//			iPlayerManager.playerAddGB(5, 100);
//			iPlayerManager.playerResetScore(0);
//			iPlayerManager.playerAddPoint(0, -2);
//			iPlayerManager.playerAddGB(0, 100);
//			iPlayerManager.playerResetScore(1);
//			iPlayerManager.playerAddPoint(1, -2);
//			iPlayerManager.playerAddGB(1, -100);
//			iPlayerManager.playerAddJiang(1, iRoomTitleStr, 400);
//			iPlayerManager.playerResetScore(2);
//			iPlayerManager.playerAddPoint(2, -2);
//			iPlayerManager.playerAddGB(2, -100);
//			iPlayerManager.playerAddJiang(2, iRoomTitleStr, 3400);
//		}
//		else {
//			//正常游戏逻辑
//			int playerCount = iNotifyResult.iPlayerCount;
//			for (int j = 0; j < playerCount; j++) {
//				int seatID = iNotifyResult.iSeatIDs[j];
//				int score = iNotifyResult.iScores[j];
//				int GB = iNotifyResult.iGBs[j];
//				iPlayerManager.playerResetScore(seatID);
//				iPlayerManager.playerAddPoint(seatID, score);
//				iPlayerManager.playerAddGB(seatID, GB);
//			}
//			int winSeatID = iNotifyResult.iWinnerSeatID;
//			int jiang = iNotifyResult.iWinnderGetGB;
//			iPlayerManager.playerAddJiang(winSeatID, iRoomTitleStr, jiang);
//			//加游戏币
//			if(iNotifyResult.iWinnerSeatID == iSerLocation) {
//				iGB += iNotifyResult.iWinnderGetGB;
//			}
//			for (int j = 0; j < playerCount; j++) {
//				int seatID = iNotifyResult.iSeatIDs[j];
//				int GB = iNotifyResult.iGBs[j];
//				if(seatID == iSerLocation) {
//					iGB += GB;
//					break;
//				}
//			}
//		}
//	}
//	
//	/**
//	 * 隐藏所有骰子底板
//	 */
//	private void hideAllDiceBG() {
//		for (int i=0; i<6; i++) {
//			SpriteButton tSB = iPlayerDiceBGSBList.get(i);
//			if (tSB != null) {
//				tSB.setVision(false);
//			}
//		}
//	}
//	
//	/**
//	 * 清除所有骰子
//	 */
//	private void clearAllDice() {
//		iMyDiceManager.clear();
//		iDiceManager.clear();
//	}
//	
//	/**
//	 * 清除所有中间的喊内容
//	 */
//	private void clearAllCallContent() {
//		iCallContentManager.clear();
//	}
//	
//	private void gameRoll() {
////		MediaManager.getMediaManagerInstance().playerSound(SoundsResID.shakadice, 0);
////		MediaManager.getMediaManagerInstance().setSoundVolume(0.1f);
//		iState = GAME_STATE_ROLL;
//		iTGameTick = 0;
//		if (iDiceCupSB != null) {
//			iDiceCupSB.setVision(true);
//			iDiceCupSB.pressed();
//		}
//		allPlayerHideCall();
//	}
//	
//	//弹出 选择骰子滚轮
//	private void popDiceSelect() {
//		iPopScene = null;
//		iPopSBList.removeAllElements();
//		
//		defaultCall();
//		
//		if (iUIPak != null) {
//			iPopScene = null;
//			iPopScene = iUIPak.getScene(SCENE_INDEX_CALL);
//		}
//		if(iPopScene != null) {
//			iPopScene.setViewWindow(0, 0, 480, 320);
//			
//			Map pMap = iPopScene.getLayoutMap(Scene.SpriteLayout);
//			Vector<?> pSpriteList = pMap.getSpriteList();
//			Sprite pSprite;
//			for (int i=0; i<pSpriteList.size(); i++) {
//				pSprite = (Sprite)pSpriteList.elementAt(i);
//				int pX = pSprite.getX();
//				int pY = pSprite.getY();
//				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_GAME_OFFSET + pSprite.getIndex();
//				TPoint pPoint = new TPoint();
//				pPoint.iX = pX;
//				pPoint.iY = pY;
//				if (pEventID ==GameEventID.ESPRITEBUTTON_EVENT_GAME_WHEEL) {
//					SpriteButtonWheel pSBW;
//					pSBW = new SpriteButtonWheel(pSprite);
//					pSBW.setShadePak(iNumberShadePak);
//					pSBW.setEventID(pEventID);
//					pSBW.setPosition(pX, pY);
//					pSBW.setSize(5*iPlayingCount);
//					pSBW.setDefNum(iDefNum);
//					pSBW.setMinNum(iMinNum);
//					pSBW.initItem();
//					pSBW.setLastCall(ibZhai, iLastNum, iLastPoint);
//					iPopSBList.addElement(pSBW);
//					iWheelSB = pSBW;
////					pSBW.setVision(false);
//				}
//				else if (pEventID == GameEventID.ESPRITEBUTTON_EVENT_GAME_WHEEL_DICE) {
//					SpriteButtonWheelDice pSBWD;
//					pSBWD = new SpriteButtonWheelDice(pSprite);
//					pSBWD.setShadePak(iNumberShadePak);
//					pSBWD.setEventID(pEventID);
//					pSBWD.setPosition(pX, pY);
//					pSBWD.setDefPoint(iDefPoint);
//					pSBWD.initItem();
//					iPopSBList.addElement(pSBWD);
//					iWheelDiceSB = pSBWD;
////					pSBW.setVision(false);
//				}
//				else {
//					//精灵按钮
//					SpriteButton pSpriteButton;
//					pSpriteButton = new SpriteButton(pSprite);
//					pSpriteButton.setEventID(pEventID);
//					pSpriteButton.setPosition(pX, pY);
//					iPopSBList.addElement(pSpriteButton);
//					if (pEventID == GameEventID.ESPRITEBUTTON_EVENT_GAME_CALL_CONFIRM) {
//						iCallConfirmSB = pSpriteButton;
//						iCallConfirmSB.setVision(false);
//					}
//				}
//			}
//		}
//	}
//	
//	//关闭选择骰子界面
//	private void closeDiceSelect() {
//		if (iReadyTimeSB != null) {
//			iReadyTimeSB.setVision(false);
//		}
//		if (iWheelSB != null) {
//			iWheelSB.setVision(false);
//		}
//		if (iWheelDiceSB != null) {
//			iWheelDiceSB.setVision(false);
//		}
//		if (iPopScene!=null && iPopScene.getIndex()==SCENE_INDEX_CALL) {
//			iPopScene = null;
//			iPopSBList.removeAllElements();
//		}
//	}
//	
//	//弹出 选择骰子滚轮 非弹出界面
//	private void popDiceSelectNotScene() {
//		
//		defaultCall();
//		
//		if(iScene != null) {
//			Map pMap = iScene.getLayoutMap(Scene.SpriteLayout);
//			Vector<?> pSpriteList = pMap.getSpriteList();
//			Sprite pSprite;
//			for (int i=0; i<pSpriteList.size(); i++) {
//				pSprite = (Sprite)pSpriteList.elementAt(i);
//				int pX = pSprite.getX();
//				int pY = pSprite.getY();
//				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_GAME_OFFSET + pSprite.getIndex();
//				TPoint pPoint = new TPoint();
//				pPoint.iX = pX;
//				pPoint.iY = pY;
//				if (pEventID ==GameEventID.ESPRITEBUTTON_EVENT_GAME_WHEEL) {
//					for (int j=0; j<iSpriteButtonList.size(); j++) {
//						SpriteButton tSB = iSpriteButtonList.elementAt(j);
//						if (tSB.getEventID() == GameEventID.ESPRITEBUTTON_EVENT_GAME_WHEEL) {
//							iSpriteButtonList.removeElementAt(j);
//							break;
//						}
//					}
//					Project tPak = getSmallDicePak(iLastLoc);
//					SpriteButtonWheel pSBW;
//					pSBW = new SpriteButtonWheel(pSprite);
//					pSBW.setShadePak(iNumberShadePak);
//					pSBW.setDicePak(tPak);
//					pSBW.setEventID(pEventID);
//					pSBW.setPosition(pX, pY);
//					pSBW.setSize(5*iPlayingCount);
//					pSBW.setDefNum(iDefNum);
//					pSBW.setMinNum(iMinNum);
//					pSBW.initItem();
//					pSBW.setLastCall(ibZhai, iLastNum, iLastPoint);
//					iSpriteButtonList.addElement(pSBW);
//					iWheelSB = pSBW;
//					pSBW.setVision(false);
//				}
//				else if (pEventID == GameEventID.ESPRITEBUTTON_EVENT_GAME_WHEEL_DICE) {
//					int tLoc = 0;
//					for (int j=0; j<iSpriteButtonList.size(); j++) {
//						SpriteButton tSB = iSpriteButtonList.elementAt(j);
//						if (tSB.getEventID() == GameEventID.ESPRITEBUTTON_EVENT_GAME_WHEEL_DICE) {
//							iSpriteButtonList.removeElementAt(j);
//							tLoc = j;
//							break;
//						}
//					}
//					iBigDicePakForWheelDiceItem = getBigDicePak(iSerLocation);
//					SpriteButtonWheelDice pSBWD;
//					pSBWD = new SpriteButtonWheelDice(pSprite);
//					pSBWD.setShadePak(iNumberShadePak);
//					pSBWD.setEventID(pEventID);
//					pSBWD.setPosition(pX, pY);
//					pSBWD.setDefPoint(iDefPoint);
//					pSBWD.initItem();
//					iSpriteButtonList.add(tLoc, pSBWD);
//					iWheelDiceSB = pSBWD;
//					pSBWD.setVision(false);
//				}
//			}
//			iLastSelectedNum = iWheelSB.getValue();
//		}
//	}
//	
//	//关闭选择骰子界面相关的东西(滚轮、倒计时框、喊、劈按钮) 用在非弹出界面
//	private void closeDiceSelectNotScene() {
//		if (iReadyTimeSB != null) {
//			iReadyTimeSB.setVision(false);
//		}
//		if (iWheelSB != null) {
//			iWheelSB.setVision(false);
//		}
//		if (iWheelDiceSB != null) {
//			iWheelDiceSB.setVision(false);
//		}
//		if (iCallSB != null) {
//			iCallSB.setVision(false);
//		}
//		if (iDuelSB != null) {
//			iDuelSB.setVision(false);
//		}
//	}
//	
//	//计算喊的 默认个数、点数
//	private void defaultCall() {
//		int tPlayerNum = iPlayingCount;//正在游戏的玩家数
//		int tSize = 5*tPlayerNum;//总个数
//		int tMinNum = 0;//最小个数
//		int tDefNum = 0;//默认个数
//		int tDefPoint = 0;//默认点数
//		if (iLastNum <= 0) {
//			//之前没人喊过
//			tMinNum = tPlayerNum;
//			tDefNum = tPlayerNum;
//			tDefPoint = 2;
//		}
//		else if (iLastNum >= tSize) {
//			//个数 喊到顶了
//			tDefNum = iLastNum;
//			tDefPoint = iLastPoint+1;
//			tMinNum = iLastNum;
//		}
//		else {
//			tDefNum = iLastNum + 1;
//			tDefPoint = iLastPoint;
//			if (iLastPoint>=7) {
//				tMinNum = iLastNum + 1;
//			}
//			else {
//				tMinNum = iLastNum;
//			}
//		}
//		iMinNum = tMinNum;//最小个数
//		iDefNum = tDefNum;//默认个数
//		iDefPoint = tDefPoint;//默认点数
//	}
//	
//	//弹出 界面(只能是简单的，没有特殊按钮的)
//	private void popScene(int aSceneIndex) {
//		if (iPopScene!=null && iPopScene.getIndex()==SCENE_INDEX_CHAT) {
//			messageEvent(MessageEventID.EMESSAGE_EVENT_CHAT_CLOSE);
//		}
//		iPopScene = null;
//		iPopSBList.removeAllElements();
//		
//		if (iUIPak != null) {
//			iPopScene = null;
//			iPopScene = iUIPak.getScene(aSceneIndex);
//		}
//		if(iPopScene != null) {
//			iPopScene.setViewWindow(0, 0, 480, 320);
//			
//			Map pMap = iPopScene.getLayoutMap(Scene.SpriteLayout);
//			Vector<?> pSpriteList = pMap.getSpriteList();
//			Sprite pSprite;
//			for (int i=0; i<pSpriteList.size(); i++) {
//				pSprite = (Sprite)pSpriteList.elementAt(i);
//				int pX = pSprite.getX();
//				int pY = pSprite.getY();
//				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_GAME_OFFSET + pSprite.getIndex();
//				TPoint pPoint = new TPoint();
//				pPoint.iX = pX;
//				pPoint.iY = pY;
//				//精灵按钮
//				SpriteButton pSpriteButton;
//				pSpriteButton = new SpriteButton(pSprite);
//				pSpriteButton.setEventID(pEventID);
//				pSpriteButton.setPosition(pX, pY);
//				iPopSBList.addElement(pSpriteButton);
//			}
//		}
//	}
//	
//	//弹出自动买酒通知界面
//	private void popAutoBuyBeer() {
//		iAutoBuyTick = 0;
//		
//		popScene(SCENE_INDEX_BUYBEER_NOTICE);
//	}
//	
//	//关闭自动买酒通知界面
//	private void closeAutoBuyBeer() {
//		iPopScene = null;
//		iPopSBList.removeAllElements();
//	}
//	
//	//弹出买醒酒丸界面
//	private void popBuyHP() {
//		popScene(SCENE_INDEX_BUYHP);
//	}
//	
//	//关闭买醒酒丸界面
//	private void closeBuyHP() {
//		iPopScene = null;
//		iPopSBList.removeAllElements();
//	}
//	
//	//弹出买酒界面
//	private void popBuyBeer() {
//		popScene(SCENE_INDEX_BUYBEER);
//	}
//	
//	//关闭买酒界面
//	private void closeBuyBeer() {
//		iPopScene = null;
//		iPopSBList.removeAllElements();
//	}
//	
//	//弹出 查看玩家信息界面
//	private void popPlayerInfo(int aLocation) {
//		Project tPlayerPak = null;
//		tPlayerPak = getPlayerPak(iPInfoRoleID);
//		if (tPlayerPak != null) {
//			iPInfoHeadS = tPlayerPak.getSprite(PLAYER_HEAD_SPRITE_INDEX);
//		}
//		if (aLocation == iSerLocation) {
//			popScene(SCENE_INDEX_PLAYERINFO_ME);
//		}
//		else {
//			popScene(SCENE_INDEX_PLAYERINFO);
//		}
//	}
//	
//	/**
//	 * description 设置名片中的头像
//	 */
//	private void setNameCardPic() {
//		iHeadBMP = GameMain.iDownloadManager.getImageNoDefault(iPInfoLittlePicName);
//		int tW = (int) (74*ActivityUtil.PAK_ZOOM_X);
//		int tH = (int) (74*ActivityUtil.PAK_ZOOM_Y);
//		switch (ActivityUtil.TYPE_SCREEN) {
//		case ActivityUtil.TYPE_SCREEN_WVGA800:
//			break;
//		case ActivityUtil.TYPE_SCREEN_HVGA:
//			tW = (int) (44*ActivityUtil.PAK_ZOOM_X);
//			tH = (int) (44*ActivityUtil.PAK_ZOOM_Y);
//			break;
//		default:
//			break;
//		}
//		if (iHeadBMP != null) {
//			iHeadBMP = Graphics.zoomBitmap(iHeadBMP, tW, tH);
//		}
//	}
//	
//	//关闭查看玩家信息界面
//	private void closePlayerInfo() {
//		iPopScene = null;
//		iPopSBList.removeAllElements();
//	}
//	
//	//弹出菜单界面
//	private void popMenu() {
//		popScene(SCENE_INDEX_MENU);
//	}
//	//关闭菜单界面
//	private void closeMenu() {
//		iPopScene = null;
//		iPopSBList.removeAllElements();
//	}
//	
//	//弹出道具界面
//	private void popItem() {
////		popScene(SCENE_INDEX_ITEM);
//		if (iPopScene!=null && iPopScene.getIndex()==SCENE_INDEX_CHAT) {
//			messageEvent(MessageEventID.EMESSAGE_EVENT_CHAT_CLOSE);
//		}
//		iPopScene = null;
//		iPopSBList.removeAllElements();
//		
//		if (iUIPak != null) {
//			iPopScene = null;
//			iPopScene = iUIPak.getScene(SCENE_INDEX_ITEM);
//		}
//		if(iPopScene != null) {
//			iPopScene.setViewWindow(0, 0, 480, 320);
//			
//			Map pMap = iPopScene.getLayoutMap(Scene.SpriteLayout);
//			Vector<?> pSpriteList = pMap.getSpriteList();
//			Sprite pSprite;
//			for (int i=0; i<pSpriteList.size(); i++) {
//				pSprite = (Sprite)pSpriteList.elementAt(i);
//				int pX = pSprite.getX();
//				int pY = pSprite.getY();
//				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_GAME_OFFSET + pSprite.getIndex();
//				TPoint pPoint = new TPoint();
//				pPoint.iX = pX;
//				pPoint.iY = pY;
//				//精灵按钮
//				SpriteButton pSpriteButton;
//				pSpriteButton = new SpriteButton(pSprite);
//				pSpriteButton.setEventID(pEventID);
//				pSpriteButton.setPosition(pX, pY);
//				iPopSBList.addElement(pSpriteButton);
//				
//				int pCanUse = 1;
//				if (pEventID==GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_HYPNOSIS ||
//						pEventID==GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_BG1) {
//					pCanUse = iItemCanUseList.get(1);
//				}
//				else if (pEventID==GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_LIEKING ||
//						pEventID==GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_BG2) {
//					pCanUse = iItemCanUseList.get(2);
//				}
//				else if (pEventID==GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_SILENCE ||
//						pEventID==GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_BG3) {
//					pCanUse = iItemCanUseList.get(3);
//				}
//				else if (pEventID==GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_COMBO_DUEL ||
//						pEventID==GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_BG4) {
//					pCanUse = iItemCanUseList.get(4);
//				}
//				else if (pEventID==GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_HUNT ||
//						pEventID==GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_BG5) {
//					pCanUse = iItemCanUseList.get(5);
//				}
//				if (pCanUse == 0) {
//					pSpriteButton.setVision(false);
//				}
//			}
//		}
//	}
//	//关闭道具界面
//	private void closeItem() {
//		if (iPopScene != null && iPopScene.getIndex() == SCENE_INDEX_ITEM) {
//			iPopScene = null;
//			iPopSBList.removeAllElements();
//		}
//	}
//	
//	//弹出菜单界面
//	private void popTarget() {
////		popScene(SCENE_INDEX_ITEM_TARGET);
//		if (iPopScene!=null && iPopScene.getIndex()==SCENE_INDEX_CHAT) {
//			messageEvent(MessageEventID.EMESSAGE_EVENT_CHAT_CLOSE);
//		}
//		iPopScene = null;
//		iPopSBList.removeAllElements();
//		
//		if (iUIPak != null) {
//			iPopScene = null;
//			iPopScene = iUIPak.getScene(SCENE_INDEX_ITEM_TARGET);
//		}
//		if(iPopScene != null) {
//			iPopScene.setViewWindow(0, 0, 480, 320);
//			
//			Map pMap = iPopScene.getLayoutMap(Scene.SpriteLayout);
//			Vector<?> pSpriteList = pMap.getSpriteList();
//			Sprite pSprite;
//			for (int i=0; i<pSpriteList.size(); i++) {
//				pSprite = (Sprite)pSpriteList.elementAt(i);
//				int pX = pSprite.getX();
//				int pY = pSprite.getY();
//				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_GAME_OFFSET + pSprite.getIndex();
//				TPoint pPoint = new TPoint();
//				pPoint.iX = pX;
//				pPoint.iY = pY;
//				//记录玩家位置
//				if (pEventID>=GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER0 && pEventID<=GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER5) {
//					int tLocation = pEventID - GameEventID.ESPRITEBUTTON_EVENT_GAME_PLAYER0;
//					iTargetLocationList.put(tLocation, pPoint);
//				}
//				else {
//					//精灵按钮
//					SpriteButton pSpriteButton;
//					pSpriteButton = new SpriteButton(pSprite);
//					pSpriteButton.setEventID(pEventID);
//					pSpriteButton.setPosition(pX, pY);
//					iPopSBList.addElement(pSpriteButton);
//				}
//			}
//		}
//		removeAllTargetPlayer();
//		addTargetPlayer();
//		iSelectTargetLocList.clear();//清空已选目标
//	}
//	//关闭道具界面
//	private void closeTarget() {
//		if (iPopScene != null && iPopScene.getIndex() == SCENE_INDEX_ITEM_TARGET) {
//			iPopScene = null;
//			iPopSBList.removeAllElements();
//		}
//	}
//	
//	/* 
//	 * @see com.funoble.lyingdice.view.GameView#OnProcessCmd(java.lang.Object, int, int)
//	 */
//	@Override
//	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
//		MobileMsg pMobileMsg = (MobileMsg) socket;
//		switch (aMobileType) {
//		case IM.IM_NOTIFY:
//		{
//			switch (aMsgID) {
//				case NotifyMsg.MSG_NOTIFY_LOGINROOM:
//					MBNotifyLoginRoom pNotifyLoginRoom = (MBNotifyLoginRoom)pMobileMsg.m_pMsgPara;
//					if(pNotifyLoginRoom == null) {
//						break;
//					}
//					Tools.debug(pNotifyLoginRoom.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_LOGINROOM, pNotifyLoginRoom);
////					int seatID = pNotifyLoginRoom.iSeatID;
////					String usernick = pNotifyLoginRoom.iUserName;
////					int hp = pNotifyLoginRoom.iCurrentDrunk;
////					int userState = pNotifyLoginRoom.iUserState;
////					int roleID = pNotifyLoginRoom.iModelID;
////					setPlayer(seatID, usernick, hp, roleID);
////					if(userState == 1) {
////						playerReady(seatID);
////					}
////					else if (userState == 3) {//观看状态
////						playerLook(seatID);
////					}
//					break;
//				case NotifyMsg.MSG_NOTIFY_READY:
//					MBNotifyReady notifyReady = (MBNotifyReady)pMobileMsg.m_pMsgPara;
//					if(notifyReady == null) {
//						break;
//					}
//					Tools.debug(notifyReady.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_READY, notifyReady);
////					seatID = notifyReady.iSeatID;
////					playerReady(seatID);
//					break;
//				case NotifyMsg.MSG_NOTIFY_SHOW_READY:
//					MBNotifyShowReady notifyShowReady = (MBNotifyShowReady)pMobileMsg.m_pMsgPara;
//					if(notifyShowReady == null) {
//						break;
//					}
//					Tools.debug(notifyShowReady.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_SHOW_READY, notifyShowReady);
////					int TableID = notifyShowReady.iTableID;
////					if(iPlayerLoginRoomInfo.iTableID == TableID) {
////						gameReady();
////					}
//					break;
//				case NotifyMsg.MSG_NOTIFY_START:
//					MBNotifyStart nitNotifyStart = (MBNotifyStart)pMobileMsg.m_pMsgPara;
//					if(nitNotifyStart == null) {
//						break;
//					}
//					Tools.debug(nitNotifyStart.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_START, nitNotifyStart);
////					iPlayingCount = nitNotifyStart.iPlayerCount;
////					iReadySB.setVision(false);
////					iReadyTimeSB.setVision(false);
////					gameRoll();
//					break;
//				case NotifyMsg.MSG_NOTIFY_SHOUT:
//					MBNotifyShout notifyShout = (MBNotifyShout)pMobileMsg.m_pMsgPara;
//					if(notifyShout == null) {
//						break;
//					}
//					Tools.debug(notifyShout.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyShout, notifyShout);
//					break;
//				case NotifyMsg.MSG_NOTIFY_CHOP:
//					MBNotifyChop notifyChop = (MBNotifyChop)pMobileMsg.m_pMsgPara;
//					if(notifyChop == null) {
//						break;
//					}
//					Tools.debug(notifyChop.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyChop, notifyChop);
//					break;
//				case NotifyMsg.MSG_NOTIFY_RESULT:
//					MBNotifyResult notifyResult = (MBNotifyResult)pMobileMsg.m_pMsgPara;
//					if(notifyResult == null) {
//						break;
//					}
//					Tools.debug(notifyResult.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyResult, notifyResult);
//					break;
//				case NotifyMsg.MSG_NOTIFY_LEAVEROOM:
//					MBNotifyLeaveRoom notifyLeaveRoom = (MBNotifyLeaveRoom)pMobileMsg.m_pMsgPara;
//					if(notifyLeaveRoom == null) {
//						break;
//					}
//					Tools.debug(notifyLeaveRoom.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_LeaveRoom, notifyLeaveRoom);
////					playerLeave(notifyLeaveRoom.iSeatID);
//					break;
//				case NotifyMsg.MSG_NOTIFY_KICK:
//					removeProgressDialog();
//					MBNotifyKick notifyKick = (MBNotifyKick)pMobileMsg.m_pMsgPara;
//					if(notifyKick == null) {
//						break;
//					}
//					Tools.debug(notifyKick.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyKICKOpenHomeScreen, notifyKick);
//					break;
//				case NotifyMsg.MSG_NOTIFY_SYSMSG:
//					removeProgressDialog();
//					MBNotifySysMsg sNotifySysMsg = (MBNotifySysMsg)pMobileMsg.m_pMsgPara;
//					if(sNotifySysMsg == null) {
//						break;
//					}
//					Tools.debug(sNotifySysMsg.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_SYS_MSG, sNotifySysMsg);
//					break;
//				case NotifyMsg.MSG_NOTIFY_CHAT:
//					MBNotifyChat notifyChat = (MBNotifyChat)pMobileMsg.m_pMsgPara;
//					if(notifyChat == null) {
//						break;
//					}
//					Tools.debug(notifyChat.toString());
//					if(notifyChat.nResult == ResultCode.SUCCESS) {
//						messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyChat, notifyChat);
//					}
//					break;
//				case NotifyMsg.MSG_NOTIFY_CLEAN_DRUNK:
//					removeProgressDialog();
//					MBNotifyCleanDrunk rspCleanDrunk = (MBNotifyCleanDrunk)pMobileMsg.m_pMsgPara;
//					if(rspCleanDrunk == null) {
//						break;
//					}
//					Tools.debug(rspCleanDrunk.toString());
//					int type = rspCleanDrunk.iResult;
//					switch (type) {
//					case ResultCode.SUCCESS://成功
//						messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyCleanDunk, rspCleanDrunk);
//						break;
//						
//					default:
//						messageEvent(type, rspCleanDrunk.iMsg);
//						break;
//					}
//					break;
//				case NotifyMsg.MSG_NOTIFY_ACTIVITY_START:
//					removeProgressDialog();
//					MBNotifyActivityStart rspActivityStart = (MBNotifyActivityStart)pMobileMsg.m_pMsgPara;
//					if(rspActivityStart == null) {
//						break;
//					}
//					Tools.debug(rspActivityStart.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_ACIIVITY_START, rspActivityStart);
//						
//					break;
//				case NotifyMsg.MSG_NOTIFY_ACTIVITY_RANKING:
//					removeProgressDialog();
//					MBNotifyActivityRanking rspActivityRanking = (MBNotifyActivityRanking)pMobileMsg.m_pMsgPara;
//					if(rspActivityRanking == null) {
//						break;
//					}
//					Tools.debug(rspActivityRanking.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_RANKING_SELF, rspActivityRanking);
//					break;
//				case NotifyMsg.MSG_NOTIFY_SHOW_ITEM:
//					MBNotifyShowItem rspShowItem = (MBNotifyShowItem)pMobileMsg.m_pMsgPara;
//					if(rspShowItem == null) {
//						break;
//					}
//					Tools.debug(rspShowItem.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_SHOWITEM, rspShowItem);
//					break;
//				case NotifyMsg.MSG_NOTIFY_VIE_CHOP:
//					MBNotifyVieChop rspVieChop = (MBNotifyVieChop)pMobileMsg.m_pMsgPara;
//					if(rspVieChop == null) {
//						break;
//					}
//					Tools.debug(rspVieChop.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_NOTIFY_VIE_CHOP, rspVieChop);
//					break;
//				case NotifyMsg.MSG_NOTIFY_CHANGE_DICE:
//					MBNotifyChangeDice notifyChangeDice = (MBNotifyChangeDice)pMobileMsg.m_pMsgPara;
//					if(notifyChangeDice == null) {
//						break;
//					}
//					Tools.debug(notifyChangeDice.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_CHANGE_DICE, notifyChangeDice);
//					break;
//				case NotifyMsg.MSG_NOTIFY_SHIELD:
//					MBNotifyShield notifyShield = (MBNotifyShield)pMobileMsg.m_pMsgPara;
//					if(notifyShield == null) {
//						break;
//					}
//					Tools.debug(notifyShield.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_SHIELD, notifyShield);
//					break;
//				case NotifyMsg.MSG_NOTIFY_MANY_CHOP:
//					MBNotifyManyChop notifyManyChop = (MBNotifyManyChop)pMobileMsg.m_pMsgPara;
//					if(notifyManyChop == null) {
//						break;
//					}
//					Tools.debug(notifyManyChop.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_MANY_CHOP, notifyManyChop);
//					break;
//				case NotifyMsg.MSG_NOTIFY_TRIGGER_SHIELD:
//					MBNotifyTriggerShield notifyTriggerShield = (MBNotifyTriggerShield)pMobileMsg.m_pMsgPara;
//					if(notifyTriggerShield == null) {
//						break;
//					}
//					Tools.debug(notifyTriggerShield.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_TRIGGER_SHIELD, notifyTriggerShield);
//					break;
//				case NotifyMsg.MSG_NOTIFY_KILL_ORDER:
//					MBNotifyKillOrder notifyKillOrder = (MBNotifyKillOrder)pMobileMsg.m_pMsgPara;
//					if(notifyKillOrder == null) {
//						break;
//					}
//					Tools.debug(notifyKillOrder.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_KILL_ORDER, notifyKillOrder);
//					break;
//				case NotifyMsg.MSG_NOTIFY_UNARMED:
//					MBNotifyUnarmed notifyUnarmed = (MBNotifyUnarmed)pMobileMsg.m_pMsgPara;
//					if(notifyUnarmed == null) {
//						break;
//					}
//					Tools.debug(notifyUnarmed.toString());
//					messageEvent(MessageEventID.EMESSAGE_EVENT_NOTIFY_UNARMED, notifyUnarmed);
//					break;
//				default:
//					break;
//			}
//		}
//			break;
//		case IM.IM_RESPONSE:
//		{
//			switch (aMsgID) {
//			case RspMsg.MSG_RSP_LOGINROOM:
//				removeProgressDialog();
//				MBRspLoginRoom rspLoginRoom = (MBRspLoginRoom)pMobileMsg.m_pMsgPara;
//				if(rspLoginRoom == null) {
//					break;
//				}
//				Tools.debug(rspLoginRoom.iMsg);
//				int type = rspLoginRoom.iResult;
//				Tools.debug(rspLoginRoom.toString());
//				switch (type) {
//					case ResultCode.SUCCESS://成功
//						messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspfLoginRoom);
//						break;
//						
//					default:
//						messageEvent(type, rspLoginRoom.iMsg);
//						break;
//				}
//				break;
//			case RspMsg.MSG_RSP_PLAYINFO:
//				MBRspPlayerInfo rspPlayerInfo = (MBRspPlayerInfo)pMobileMsg.m_pMsgPara;
//				if(rspPlayerInfo == null) {
//					break;
//				}
//				Tools.debug(rspPlayerInfo.iMsg);
//				type = rspPlayerInfo.iResult;
//				Tools.debug(rspPlayerInfo.toString());
//				switch (type) {
//				case ResultCode.SUCCESS://成功
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspPlayerInfo, rspPlayerInfo);
//					break;
//					
//				default:
//					messageEvent(type, rspPlayerInfo.iMsg);
//					break;
//				}
//				break;
//			case RspMsg.MSG_RSP_BUY_WINE:
//				removeProgressDialog();
//				MBRspBuyWine rspBuyWine = (MBRspBuyWine)pMobileMsg.m_pMsgPara;
//				if(rspBuyWine == null) {
//					break;
//				}
//				Tools.debug(rspBuyWine.iMsg);
//				type = rspBuyWine.iResult;
//				Tools.debug(rspBuyWine.toString());
//				switch (type) {
//				case ResultCode.SUCCESS://成功
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspBuyWine, rspBuyWine);
//					break;
//					
//				default:
//					messageEvent(type, rspBuyWine.iMsg);
//					break;
//				}
//				break;
//			case RspMsg.MSG_RSP_PLAYINFO_TWO:
//				removeProgressDialog();
//				MBRspPlayerInfoTwo rspMainPage = (MBRspPlayerInfoTwo)pMobileMsg.m_pMsgPara;
//				if(rspMainPage == null) {
//					break;
//				}
//				Tools.debug(rspMainPage.toString());
//				type = rspMainPage.iResult;
//				if(type == ResultCode.SUCCESS) {
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_PlayerInfoTwo, rspMainPage);
//				}
//				else {
//					messageEvent(type, rspMainPage.iMsg);
//				}
//				break;
//			case RspMsg.MSG_RSP_KICK:
//				removeProgressDialog();
//				MBRspKick rspKick = (MBRspKick)pMobileMsg.m_pMsgPara;
//				if(rspKick == null) {
//					break;
//				}
//				Tools.debug(rspKick.toString());
//				type = rspKick.nResult;
//				if(type == ResultCode.SUCCESS) {
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_KICK, rspKick);
//				}
//				else {
//					messageEvent(type, rspKick.iMsg);
//				}
//				break;
//			case RspMsg.MSG_RSP_ADD_FRIEND:
//				removeProgressDialog();
//				MBRspAddFriend rspAddFriend = (MBRspAddFriend)pMobileMsg.m_pMsgPara;
//				if(rspAddFriend == null) {
//					break;
//				}
//				Tools.debug(rspAddFriend.toString());
//				type = rspAddFriend.iResult;
//				if(type == ResultCode.SUCCESS) {
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_AddFriend, rspAddFriend);
//				}
//				else {
//					messageEvent(type, rspAddFriend.iMsg);
//				}
//				break;
//			case RspMsg.MSG_RSP_ACTIVITY_ENROLL:
//				MBRspActivityEnroll rspActivityEnroll = (MBRspActivityEnroll)pMobileMsg.m_pMsgPara;
//				if(rspActivityEnroll == null) {
//					break;
//				}
//				Tools.debug(rspActivityEnroll.toString());
//				type = rspActivityEnroll.nResult;
//				if(type == ResultCode.SUCCESS) {
//					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_ENROLL, rspActivityEnroll);
//				}
//				else {
//					messageEvent(type, rspActivityEnroll.iMsg);
//				}
//				break;
//			case RspMsg.MSG_RSP_LEAVEROOM:
//				MBRspLeaveRoom rspLeaveRoom = (MBRspLeaveRoom)pMobileMsg.m_pMsgPara;
//				if(rspLeaveRoom == null) {
//					break;
//				}
//				Tools.debug(rspLeaveRoom.toString());
//				messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspLeaveRoom, rspLeaveRoom);
//				break;
//			case RspMsg.MSG_RSP_ACTIVITY_RANK:
//				MBRspActivityRank rspActivityRank = (MBRspActivityRank)pMobileMsg.m_pMsgPara;
//				if(rspActivityRank == null) {
//					break;
//				}
//				Tools.debug(rspActivityRank.toString());
//				if(rspActivityRank.nResult == ResultCode.SUCCESS) {
//					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_RANK, rspActivityRank);
//				}
//				else {
//					messageEvent(rspActivityRank.nResult, rspActivityRank.iMsg);
//				}
//				break;
//			case RspMsg.MSG_RSP_USE_ITEM:
//				MBRspUseItem rspUseItem = (MBRspUseItem)pMobileMsg.m_pMsgPara;
//				if(rspUseItem == null) {
//					break;
//				}
//				Tools.debug(rspUseItem.toString());
//				if(rspUseItem.nResult == ResultCode.SUCCESS) {
//					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_USEITEM, rspUseItem);
//				}
//				else {
//					messageEvent(rspUseItem.nResult, rspUseItem.iMsg);
//				}
//				break;
//				default:
//					break;
//			}
//		}
//		break;
//
//		default:
//			break;
//		}
//	}
//	
//	private void initChatListView() {
//		iChatET = (EditText) GameMain.getInstance().findViewById(R.id.editTextChat);
//		initCommonData();
//		iListView = (ListView) GameMain.getInstance().findViewById(R.id.listViewChat);
//		iCommonListAdapter = new ArrayAdapter<String>(GameMain.getInstance().getApplicationContext(),
//				android.R.layout.simple_list_item_1,iCommonDataList);
//		iListView.setAdapter(iCommonListAdapter);
////		iChatListView.setAdapter(new ArrayAdapter<String>(GameMain.getInstance().getApplicationContext(),
////				android.R.layout.simple_expandable_list_item_1,iChatDataList));
//		iListView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
////				Tools.println("PS ListView onItemClick~~~~~");
//				switch (iCurTabIndex) {
//				case TAB_INDEX_COMMON:
//					String t = (String) iCommonListAdapter.getItem(position);
//					iChatET.setText(t);
//					Tools.println("PS ListView onItemClick ~~~~~ setChatText = " + t);
//					break;
//				case TAB_INDEX_FACE:
//					break;
//				case TAB_INDEX_RECORD:
//					break;
//				}
//			}
//		});
//		iChatSendB = (Button) GameMain.getInstance().findViewById(R.id.buttonChatSend);
//		if (iChatSendB != null) {
//			iChatSendB.setOnClickListener(this);
//		}
//		iChatCommonB = (Button) GameMain.getInstance().findViewById(R.id.buttonChatCommon);
//		if (iChatCommonB != null) {
//			iChatCommonB.setOnClickListener(this);
//		}
//		iChatRecordB = (Button) GameMain.getInstance().findViewById(R.id.buttonChatRecord);
//		if (iChatRecordB != null) {
//			iChatRecordB.setOnClickListener(this);
//		}
//		adjustSysCom();
//	}
//	
//	/**
//	 * description 初始化 常用语
//	 */
//	private void initCommonData() {
//		iCommonDataList = new ArrayList<String>();
//		iCommonDataList.add("嘿，交个朋友吧！");
//		iCommonDataList.add("喊得太小了，搞大！");
//		iCommonDataList.add("哎呀，一个都没有，开玩笑的吧！");
//		iCommonDataList.add("太假了，骗子，没有也跟喊！");
//		iCommonDataList.add("没天理啊，这也太多了吧！");
//		iCommonDataList.add("有人要倒了，灌死分钱啦~~");
//		iCommonDataList.add("相信我，我是老实人，不说假话的！");
//		iCommonDataList.add("真话是什么？什么是真话？");
//		iCommonDataList.add("我不会瞎喊的，大胆顶起！");
//		iCommonDataList.add("我说什么你都信，你太笨了！");
//		iRecordDataList = new ArrayList<String>();
//		iRecordListAdapter = new ArrayAdapter<String>(GameMain.getInstance().getApplicationContext(),
//				android.R.layout.simple_list_item_1,iRecordDataList);
//    }
//	
//	private void messageEvent(int aEventID) {
//		Message tMes = new Message();
//		tMes.what = aEventID;
//		GameMain.getInstance().iHandler.sendMessage(tMes);
//	}
//	
//	private void messageEvent(int aEventID, Object aInfo) {
//		Message tMes = new Message();
//		tMes.what = aEventID;
//		tMes.obj = aInfo;
//		GameMain.getInstance().iHandler.sendMessage(tMes);
//	}
//	
//	@Override
//	public void handleMessage(Message msg) {
//		if (msg == null) {
//			return;
//		}
//		switch (msg.what) {
//		case ResultCode.ERR_DECODEBUF:
//		case ResultCode.ERR_TIMEOUT:
//		case ResultCode.ERR_BUSY: 
//		case ResultCode.ERR_ROOM_TABLE_INVALED:
//		case ResultCode.ERR_ROOM_TABLE_FULL:
//		case ResultCode.ERR_ROOM_FULL:
//		case ResultCode.ERR_ROOM_GB_NOTENOUGH:
//		case ResultCode.ERR_ROOM_DRUNK:
//		case ResultCode.ERR_ROOM_PLAYER_INVALED:
//		case ResultCode.ERR_ROOM_GB_LIMIT:
//		case ResultCode.ERR_KICK_VIP:
//			if((String)msg.obj != null) {
//				Tools.showSimpleToast(iContext, Gravity.CENTER, (String)msg.obj, Toast.LENGTH_LONG);
//			}
//			removeProgressDialog();
//			messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
//			break;
//		case ResultCode.ERR_FRIEND_ADD_SELF:
//		case ResultCode.ERR_KICK_HIGH_VIP:
//		case ResultCode.ERR_FRIEND_OTHERSIDE_LIMIT:
//		case ResultCode.ERR_FRIEND_UID_INVALED:
//		case ResultCode.ERR_FRIEND_ALREADY:
//		case ResultCode.ERR_FRIEND_MYSIDE_LIMIT:
//		case ResultCode.ERR_KICK_NO_VIP:
//		case ResultCode.ERR_FRIEND_VISITOR:
//		case ResultCode.ERR_KICK_NOT_FIND_PLAYER:
//		case ResultCode.ERR_KICK_FAIL_PLAYINGGAME:
//		case ResultCode.ERR_KICK_VIP_INVALID:
//		case ResultCode.ERR_USE_ITEM_NULL:
//		case ResultCode.ERR_USE_ITEM_REFUSE:
//			if((String)msg.obj != null) {
//				Tools.showSimpleToast(iContext, Gravity.CENTER, (String)msg.obj, Toast.LENGTH_LONG);
//			}
//			removeProgressDialog();
//			break;
//		case MessageEventID.EMESSAGE_EVENT_CHAT_ADD:
//			String tInfo = (String) msg.obj;
//			Tools.println("ProgressScreen  handleMessage  begin  tInfo = " + tInfo);
//			iRecordDataList.add(0, tInfo);
//			iRecordListAdapter.notifyDataSetChanged();
////			iChatListView.setAdapter(new ArrayAdapter<String>(GameMain.getInstance().getApplicationContext(),
////					android.R.layout.simple_expandable_list_item_1,iChatDataList));
//			Tools.println("ProgressScreen  handleMessage  end");
//			break;
//		case MessageEventID.EMESSAGE_EVENT_CHAT_CLOSE:
//			hideSysCom();
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyShout:
//			if (iState == GAME_STATE_ROLL) {//如果还没Roll完，立马让游戏Start
//				iTGameTick = 0;
//				if (iDiceCupSB != null) {
//					iDiceCupSB.released();
//					iDiceCupSB.setVision(false);
//				}
//				gameStart();
//			}
//			MBNotifyShout notifyShout = (MBNotifyShout)msg.obj;
//			playerCall(notifyShout.iShouterSeatID, notifyShout.iCount, notifyShout.iDice);
//			playerTurn(notifyShout.iNextSeatID);
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyChop:
//			MBNotifyChop notifyChop = (MBNotifyChop)msg.obj;
//			gameDuel(notifyChop.iChopSeatID);
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyResult:
//			iNotifyResult = (MBNotifyResult)msg.obj;
//			if(iNotifyResult.nResult == ResultCode.ERR_RESULT_PLAYER_RUNOFF) {
//				if (!iNotifyResult.iMsg.equals("")) {
//					Tools.showSimpleToast(iContext, Gravity.CENTER, iNotifyResult.iMsg, Toast.LENGTH_LONG);
//				}
//				gameIrregularResult();
//			}
//			else {
//				gameResult();
//				if(iPlayerLoginInfo != null && iNotifyResult.iWinnerSeatID == iSerLocation) {
//					iPlayerLoginInfo.iGb += iNotifyResult.iWinnderGetGB;
//					iPlayerLoginInfo.iKillsCount = iNotifyResult.iWinnerKillsCount;
//				}
//				//原先的加游戏币操作，放在加分演算中
////				if(iNotifyResult.iWinnerSeatID == iSerLocation) {
////					iGB += iNotifyResult.iWinnderGetGB;
////				}
//				if(iNotifyResult.iFailSeatID == iSerLocation) {
//					iPlayerLoginInfo.iCurrentDrunk = iNotifyResult.iFailCurrentDrunk;
//				}
////				int playerCount = iNotifyResult.iPlayerCount;
////				for (int j = 0; j < playerCount; j++) {
////					int seatID = iNotifyResult.iSeatIDs[j];
////					int GB = iNotifyResult.iGBs[j];
////					if(seatID == iSerLocation) {
////						iGB += GB;
////						break;
////					}
////				}
//			}
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspfLoginRoom:
//			removeProgressDialog();
//			initPlayerInfo();
//			popAutoBuyBeer();
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyChat:
//			MBNotifyChat notifyChat = (MBNotifyChat)msg.obj;
//			if(notifyChat.iSeatID != -1) {
//				playerChat(notifyChat.iSeatID, notifyChat.iMsg);
//			}
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspPlayerInfo:
//			MBRspPlayerInfo rspPlayerInfo = (MBRspPlayerInfo)msg.obj;
//			if(rspPlayerInfo.iSeatID != -1) {
//				iPInfoUserID = rspPlayerInfo.iUserId;//用户uid
//				iPInfoRoleID = rspPlayerInfo.iModelID;//角色ID
//				iPInfoName = rspPlayerInfo.iNick;//名字
//				iPInfoTitle = rspPlayerInfo.iTitle;//称号
//				iPInfoMoney = rspPlayerInfo.iGb;//钱
//				iPInfoWin = rspPlayerInfo.iKillsCount;//胜
//				iPInfoLose = rspPlayerInfo.iBeKillsCount;//负
//				iPInfoBeatDown = rspPlayerInfo.iReward;//灭
//				iPInfoWinPer = rspPlayerInfo.iWinning;//胜率
//				popPlayerInfo(rspPlayerInfo.iSeatID);
//			}
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_NotifyCleanDunk:
//			MBNotifyCleanDrunk rspCleanDrunk = (MBNotifyCleanDrunk)msg.obj;
//			Tools.showSimpleToast(iContext, Gravity.CENTER, rspCleanDrunk.iMsg, Toast.LENGTH_LONG);
//			if(rspCleanDrunk.iSeatID == iSerLocation) {
//				iGB = rspCleanDrunk.iCurrentGb;//钱
//				if (iBeer <= 0) {
//					popBuyBeer();
//				}
//			}
//			updatePlayerHP(rspCleanDrunk.iSeatID, 0);
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_ShowDrunkMsg:
//			showDrunkMsg();
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspBuyWine:
//			MBRspBuyWine rspBuyWine = (MBRspBuyWine)msg.obj;
//			Tools.showSimpleToast(iContext, Gravity.CENTER, rspBuyWine.iMsg, Toast.LENGTH_LONG);
//			if(rspBuyWine.iSeatID == iSerLocation) {
//				iGB = rspBuyWine.iCurrentGb;//钱
//				iBeer = rspBuyWine.iWineCount;
//			}
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_LOGINROOM:
//			MBNotifyLoginRoom pNotifyLoginRoom = (MBNotifyLoginRoom)msg.obj;
//			int seatID = pNotifyLoginRoom.iSeatID;
//			String usernick = pNotifyLoginRoom.iUserName;
//			int hp = pNotifyLoginRoom.iCurrentDrunk;
//			int userState = pNotifyLoginRoom.iUserState;
//			int roleID = pNotifyLoginRoom.iModelID;
//			String headPicName = pNotifyLoginRoom.iLittlePic;
//			int	dicePakID = pNotifyLoginRoom.iDicePakID;
//			setPlayer(seatID, headPicName, usernick, hp, roleID, dicePakID);
//			if(userState == 1) {
//				playerReady(seatID);
//			}
//			else if (userState == 3) {//观看状态
//				playerLook(seatID);
//			}
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_READY:
//			MBNotifyReady notifyReady = (MBNotifyReady)msg.obj;
//			seatID = notifyReady.iSeatID;
//			playerReady(seatID);
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_SHOW_READY:
//			MBNotifyShowReady notifyShowReady = (MBNotifyShowReady)msg.obj;
//			int TableID = notifyShowReady.iTableID;
//			if(iPlayerLoginRoomInfo.iTableID == TableID) {
//				gameReady();
//			}
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_START:
//			MBNotifyStart nitNotifyStart = (MBNotifyStart)msg.obj;
//			iPlayingCount = nitNotifyStart.iPlayerCount;
//			iReadySB.setVision(false);
//			iReadyTimeSB.setVision(false);
//			gameRoll();
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_LeaveRoom:
//			MBNotifyLeaveRoom notifyLeaveRoom = (MBNotifyLeaveRoom)msg.obj;
//			playerLeave(notifyLeaveRoom.iSeatID);
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_PlayerInfoTwo:
//			MBRspPlayerInfoTwo iPlayerInfo = (MBRspPlayerInfoTwo)msg.obj;
//			if(iPlayerInfo.iSeatID != -1) {
//				iPInfoUserID = iPlayerInfo.iUserId;//用户uid
//				iPInfoRoleID = iPlayerInfo.iModelID;//角色ID
//				iPInfoName = iPlayerInfo.iNick;//名字
//				iPInfoTitle = iPlayerInfo.iTitle;//称号
//				iPInfoMoney = iPlayerInfo.iGb;//钱
//				iPInfoWin = iPlayerInfo.iKillsCount;//胜
//				iPInfoLose = iPlayerInfo.iBeKillsCount;//负
//				iPInfoBeatDown = iPlayerInfo.iReward;//灭
//				iPInfoWinPer = iPlayerInfo.iWinning;//胜率
//				iPInfoLittlePicName = iPlayerInfo.iLittlePicName;
//				setNameCardPic();
//				popPlayerInfo(iPlayerInfo.iSeatID);
//			}
//			break;	
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_PIC:
////			setNameCardPic();
////			allPlayerUpdateHeadPic();
//			break;	
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_SINGLE_PIC://单张图片下完
//			String tPicName = (String) msg.obj;//图片名
//			if (tPicName.equals(iPInfoLittlePicName)) {
//				setNameCardPic();
//			}
//			playerUpdateHeadPic(tPicName);
//			break;	
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_KICK:
//			MBRspKick rspKick = (MBRspKick)msg.obj;
//			Tools.showSimpleToast(GameMain.mGameMain, Gravity.CENTER, rspKick.iMsg, Toast.LENGTH_LONG);
//			break;	
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_AddFriend:
//			MBRspAddFriend rspAddFriend = (MBRspAddFriend)msg.obj;
//			Tools.showSimpleToast(GameMain.mGameMain, Gravity.CENTER, rspAddFriend.iMsg, Toast.LENGTH_LONG);
//			break;	
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_NOTIFY_SYS_MSG:
//			MBNotifySysMsg notifySysMsg = (MBNotifySysMsg)msg.obj;
//			if(notifySysMsg.iMsg.length() > 0) {
//				Tools.showSimpleToast(GameMain.mGameMain, Gravity.CENTER, notifySysMsg.iMsg, Toast.LENGTH_LONG);
//			}
//			break;	
//		case MessageEventID.EMESSAGE_EVENT_NOTIFY_ACIIVITY_START:
//			removeProgressDialog();
//			notifyActivityStart = (MBNotifyActivityStart)msg.obj;
//			if(notifyActivityStart.iMsg.length() > 0) {
//				Tools.showSimpleToast(GameMain.mGameMain, Gravity.CENTER, notifyActivityStart.iMsg, Toast.LENGTH_LONG);
//			}
//			showMotionStart(notifyActivityStart);
//			break;	
//		case MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_ENROLL:		//没有报名
//			removeProgressDialog();
//			MBRspActivityEnroll rspActivityEnroll = (MBRspActivityEnroll)msg.obj;
//			if(rspActivityEnroll.iMsg.length() > 0) {
//				Tools.showSimpleToast(GameMain.mGameMain, Gravity.CENTER, rspActivityEnroll.iMsg, Toast.LENGTH_LONG);
//			}
//			break;
//		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspLeaveRoom:
//			MBRspLeaveRoom rspLeaveRoom = (MBRspLeaveRoom)msg.obj;
//			removeProgressDialog();
//			if(notifyActivityStart != null) {
//				Pair<Integer, Integer> pair = new Pair<Integer, Integer>(notifyActivityStart.iActivityRoomID, -1);
//				requestLoginRoom(pair);
//			}
//			break;
//		case MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_RANK:
//			MBRspActivityRank rspActivityRank = (MBRspActivityRank)msg.obj;
//			removeProgressDialog();
//			int count = rspActivityRank.iCount;
//			for(int i = 0; i < count; i++) {
//				GameMotionData data = new GameMotionData("第" + (iMotionData.size() + 1) +"名", rspActivityRank.iRankNick.get(i), rspActivityRank.iRankValue.get(i));
//				iMotionData.add(data);
//			}
//			showMotionRank();
//			break;
//		case MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_RANKING_SELF:
//			MBNotifyActivityRanking notifyActivityRanking = (MBNotifyActivityRanking)msg.obj;
//			iRankStr = notifyActivityRanking.iMsg;
//			break;
//		case MessageEventID.EMESSAGE_EVENT_RSP_USEITEM:
//			removeProgressDialog();
//			MBRspUseItem useItem = (MBRspUseItem)msg.obj;
//			if(useItem.iMsg.length() > 0) {
//				Tools.showSimpleToast(GameMain.mGameMain, Gravity.CENTER, useItem.iMsg, Toast.LENGTH_LONG);
//			}
//			int ItemKey = MAP_ITEM_KEY[useItem.iItemID];
//			setItemNumByKey(ItemKey, useItem.iItemCount);
//			break;
//		case MessageEventID.EMESSAGE_EVENT_NOTIFY_SHOWITEM:
//			MBNotifyShowItem showItem = (MBNotifyShowItem)msg.obj;
//			if (showItem != null) {
//				int pMaxCount = showItem.iItemTypeCount;
////				int pMaxCount = BufferSize.MAX_ITEM_TYPE_COUNT;
//				Vector<Integer> pItemIDs = showItem.iItemIDs;
//				Vector<Short> pCanUses = showItem.nCanUses;
//				Vector<Integer> pItemCounts = showItem.iItemCounts;
//				for (int i=0; i<pMaxCount; i++) {
//					int pID = pItemIDs.elementAt(i);
//					int pCanUse = pCanUses.elementAt(i);
//					int pCount = pItemCounts.elementAt(i);
//					int pKey = MAP_ITEM_KEY[i];
//					iItemIDList.put(pKey, pID);
//					iItemCanUseList.put(pKey, pCanUse);
//					setItemNumByKey(pKey, pCount);
//					ArtNumber pArtNum = iItemArtNumList.get(pKey);
//					if (pArtNum != null) {
//						if (pCanUse == 0) {
//							pArtNum.setDraw(false);
//						}
//						else {
//							pArtNum.setDraw(true);
//						}
//					}
//				}
//			}
//			if (iItemSB != null) {
//				iItemSB.setVision(true);
//			}
//			int pQDKey = MAP_ITEM_KEY[E_RTI_VIE_CHOP];
//			ArtNumber pArtNum = iItemArtNumList.get(pQDKey);
//			if (iItemCanUseList.get(pQDKey) == 1) {
//				if (iQuickDuelSB != null) {
//					iQuickDuelSB.setVision(true);
//				}
//				if (iQuickDuelNumBGSB != null) {
//					iQuickDuelNumBGSB.setVision(true);
//				}
//				if (pArtNum != null) {
//					pArtNum.setDraw(true);
//				}
//			}
//			else {
//				if (pArtNum != null) {
//					pArtNum.setDraw(false);
//				}
//			}
//			break;
//		case MessageEventID.EMESSAGE_EVENT_NOTIFY_NOTIFY_VIE_CHOP:
//			iServerUseItemID = getItemID(E_RTI_VIE_CHOP);
//			MBNotifyVieChop notifyVieChop = (MBNotifyVieChop)msg.obj;
//			gameDuel(notifyVieChop.iVieChopSeatID);
//			setItemAnim(notifyVieChop.iVieChopSeatID);
//			break;
//		case MessageEventID.EMESSAGE_EVENT_NOTIFY_CHANGE_DICE:
//			iServerUseItemID = getItemID(E_RTI_CHANGE_DICE);
//			MBNotifyChangeDice notifyChangeDice = (MBNotifyChangeDice)msg.obj;
////			allMyDiceChange(notifyChangeDice.iDice);
//			iChangeDicePoint = notifyChangeDice.iDice;
//			setItemAnim(iSerLocation);
//			break;
//		case MessageEventID.EMESSAGE_EVENT_NOTIFY_SHIELD:
//			iServerUseItemID = getItemID(E_RIT_SHIELD);
//			MBNotifyShield notifyShield = (MBNotifyShield)msg.obj;
//			Tools.showSimpleToast(GameMain.mGameMain, Gravity.CENTER, notifyShield.iUserSeatID+"被催眠测试显示", Toast.LENGTH_LONG);
//			setItemAnim(iSerLocation);
//			break;
//		case MessageEventID.EMESSAGE_EVENT_NOTIFY_MANY_CHOP:
//			iServerUseItemID = getItemID(E_RTI_MANY_CHOP);
//			MBNotifyManyChop notifyManyChop = (MBNotifyManyChop)msg.obj;
//			gameDuel(notifyManyChop.iUserSeatID);
//			setItemAnim(notifyManyChop.iUserSeatID, notifyManyChop.iDesSeatID);
//			break;
//		case MessageEventID.EMESSAGE_EVENT_NOTIFY_TRIGGER_SHIELD:
//			iServerUseItemID = getItemID(E_RIT_SHIELD);
//			iState = GAME_STATE_GAMING;
//			MBNotifyTriggerShield notifyTriggerShield = (MBNotifyTriggerShield)msg.obj;
//			playerCall(notifyTriggerShield.iUserSeatID, notifyTriggerShield.iCount, notifyTriggerShield.iDice);
//			playerTurn(notifyTriggerShield.iNextSeatID, false);
//			triggerItem(notifyTriggerShield.iUserSeatID);
//			break;
//		case MessageEventID.EMESSAGE_EVENT_NOTIFY_KILL_ORDER:
//			iServerUseItemID = getItemID(E_RIT_KILL_ORDER);
//			MBNotifyKillOrder notifyKillOrder = (MBNotifyKillOrder)msg.obj;
//			setItemAnim(notifyKillOrder.iUserSeatID, notifyKillOrder.iDesSeatID);
//			break;
//		case MessageEventID.EMESSAGE_EVENT_NOTIFY_UNARMED:
//			iServerUseItemID = getItemID(E_RIT_UNARMED);
//			MBNotifyUnarmed notifyUnarmed = (MBNotifyUnarmed)msg.obj;
//			setItemAnim(notifyUnarmed.iUserSeatID, notifyUnarmed.iDesSeatID);
//			break;
//		}
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.buttonChatSend:
//			//发送聊天内容
//			String tSendText = "";
//			if (iChatET != null) {
//				tSendText = iChatET.getText().toString();
//				if (ibTest) {
//					//模拟游戏逻辑
//					Tools.println("ProgressScreen onClick send -----["+tSendText+"]----------");
////					Tools.showSimpleToast(GameMain.getInstance(), Gravity.CENTER, tSendText, Toast.LENGTH_LONG);
////					playerChat(iSerLocation, tSendText);
//					for (int i=0; i<=5; i++) {
//						playerChat(i, tSendText);
//					}
//				}
//				else {
//					//正常游戏逻辑
//					Tools.debug("charcontent = " + tSendText);
//					GameMain.mGameMain.iGameProtocol.requestChat(tSendText);
//				}
//				iChatET.setText("");
//				closePopScene();
//			}
//			break;
//		case R.id.buttonChatCommon:
//			//点击常用语按钮
//			if (iCurTabIndex != TAB_INDEX_COMMON) {
//				iCurTabIndex = TAB_INDEX_COMMON;
//				iListView.setAdapter(iCommonListAdapter);
//			}
//			break;
//		case R.id.buttonChatRecord:
//			//点击聊天记录按钮
//			if (iCurTabIndex != TAB_INDEX_RECORD) {
//				iCurTabIndex = TAB_INDEX_RECORD;
//				iListView.setAdapter(iRecordListAdapter);
//			}
//			break;
//		}
//		MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//	}
//	
//	//调整系统组件位置，只能调用一次，否则位置会越来越偏
//	private void adjustSysCom() {
//		int tOffsetX = 0;
//		int tOffsetY = 0;
//		if (iListView != null) {
//			LayoutParams lp = (LayoutParams) iListView.getLayoutParams();
//			int srcX = lp.x;
//			int srcY = lp.y;
//			lp.x = (int) (lp.x*ActivityUtil.DENSITY_ZOOM_X);
//			lp.y = (int) (lp.y*ActivityUtil.DENSITY_ZOOM_Y);
//			tOffsetX = lp.x-srcX;
//			tOffsetY = lp.y-srcY;
//		}
//		if (iChatET != null) {
//			LayoutParams lp = (LayoutParams) iChatET.getLayoutParams();
//			lp.x += tOffsetX;
//			lp.y += tOffsetY;
//		}
//		if (iChatSendB != null) {
//			LayoutParams lp = (LayoutParams) iChatSendB.getLayoutParams();
//			lp.x += tOffsetX;
//			lp.y += tOffsetY;
//		}
//		if (iChatCommonB != null) {
//			LayoutParams lp = (LayoutParams) iChatCommonB.getLayoutParams();
//			lp.x += tOffsetX;
//			lp.y += tOffsetY;
//		}
//		if (iChatRecordB != null) {
//			LayoutParams lp = (LayoutParams) iChatRecordB.getLayoutParams();
//			lp.x += tOffsetX;
//			lp.y += tOffsetY;
//		}
//	}
//	
//	//显示系统组件
//	private void displaySysCom() {
//		if (iListView != null) {
//			iListView.setVisibility(View.VISIBLE);
//		}
//		if (iChatET != null) {
//			iChatET.setVisibility(View.VISIBLE);
//		}
//		if (iChatSendB != null) {
//			iChatSendB.setVisibility(View.VISIBLE);
//		}
//		if (iChatCommonB != null) {
//			iChatCommonB.setVisibility(View.VISIBLE);
//		}
//		if (iChatRecordB != null) {
//			iChatRecordB.setVisibility(View.VISIBLE);
//		}
//	}
//	
//	//隐藏系统组件
//	private void hideSysCom() {
//		if (iListView != null) {
//			iListView.setVisibility(View.INVISIBLE);
//		}
//		if (iChatET != null) {
//			iChatET.setVisibility(View.INVISIBLE);
//		}
//		if (iChatSendB != null) {
//			iChatSendB.setVisibility(View.INVISIBLE);
//		}
//		if (iChatCommonB != null) {
//			iChatCommonB.setVisibility(View.INVISIBLE);
//		}
//		if (iChatRecordB != null) {
//			iChatRecordB.setVisibility(View.INVISIBLE);
//		}
//	}
//	
//	//弹出 聊天界面
//	private void popChat() {
//		displaySysCom();
//		popScene(SCENE_INDEX_CHAT);
//	}
//	
//	//关闭聊天界面 原线程用，非原线程要用message
//	private void closeChat() {
//		hideSysCom();
//		iPopScene = null;
//		iPopSBList.removeAllElements();
//	}
//	
//	private void showReturnHomeConfirm() {
//		//判断自己的状态
//		int tStrID = R.string.Return_HomeScreen_tip_Content;
//		int tMyState = iPlayerNameManager.getPlayerNameAction(iSerLocation);//自己的喊点框状态
//		if (tMyState == PlayerName.ACTION_LOOK) {
//			tStrID = R.string.Return_HomeScreen_tip_Content;
//		}
//		else {
//			if (iState == GAME_STATE_GAMING || iState == GAME_STATE_DUEL) {
//				tStrID = R.string.Return_HomeScreen_tip_Content_Warning;
//			}
//		}
////		switch (tMyState) {
////		case PlayerName.ACTION_NORMAL:
////		case PlayerName.ACTION_LAST:
////		case PlayerName.ACTION_MYTURN:
////		case PlayerName.ACTION_MYTURN_HIDE:
////			tStrID = R.string.Return_HomeScreen_tip_Content_Warning;
////			break;
////		case PlayerName.ACTION_READY:
////		case PlayerName.ACTION_LOOK:
////			tStrID = R.string.Return_HomeScreen_tip_Content;
////			break;
////		case PlayerName.ACTION_DUEL:
////			if (iState == GAME_STATE_DUEL) {
////				tStrID = R.string.Return_HomeScreen_tip_Content_Warning;
////			}
////			else {
////				tStrID = R.string.Return_HomeScreen_tip_Content;
////			}
////			break;
////		case PlayerName.ACTION_HIDE:
////			if (iState == GAME_STATE_GAMING) {
////				tStrID = R.string.Return_HomeScreen_tip_Content_Warning;
////			}
////			break;
////		}
//		//根据自己的状态，给出相应提示
//		AlertDialog dialog = new AlertDialog.Builder(iContext).setTitle(R.string.Return_HomeScreen_tip_title).setMessage(
//				iContext.getString(tStrID)).setCancelable(false)
//				.setPositiveButton(R.string.Return_HomeScreen_Ok, new DialogInterface.OnClickListener()
//				{
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						Tools.debug("iUserID"+iUserID);
//						if (ibTest) {
//							//模拟游戏逻辑
//							hideSysCom();
//							ibChange = true;
//							MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//						}
//						else {
//							//正常游戏逻辑
////							if(iUserID != 0) {
//								hideSysCom();
//								messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_ReqLeaveRoom);
//								MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
////							}
//						}
//					}
//				}).setNegativeButton(R.string.Return_HomeScreen_Cancel, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//					}
//				}).create();
//		dialog.setCancelable(true);
//		dialog.show();
//	}
//	
//	/**
//	 * description 到房间列表的二次确认
//	 */
//	private void showToSelectRoomConfirm() {
//		//判断自己的状态
//		int tStrID = R.string.To_SelectRoom_tip_Content;
//		int tMyState = iPlayerNameManager.getPlayerNameAction(iSerLocation);//自己的喊点框状态
//		if (tMyState == PlayerName.ACTION_LOOK) {
//			tStrID = R.string.To_SelectRoom_tip_Content;
//		}
//		else {
//			if (iState == GAME_STATE_GAMING || iState == GAME_STATE_DUEL) {
//				tStrID = R.string.To_SelectRoom_tip_Content_Warning;
//			}
//		}
//		//根据自己的状态，给出相应提示
//		AlertDialog dialog = new AlertDialog.Builder(iContext).setTitle(R.string.To_SelectRoom_tip_title).setMessage(
//				iContext.getString(tStrID)).setCancelable(false)
//				.setPositiveButton(R.string.To_SelectRoom_Ok, new DialogInterface.OnClickListener()
//				{
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						Tools.debug("iUserID"+iUserID);
//						if (ibTest) {
//							//模拟游戏逻辑
//							hideSysCom();
//							ibChange = true;
//							MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//						}
//						else {
//							//正常游戏逻辑
////							if(iUserID != 0) {
//							hideSysCom();
//							messageEvent(MessageEventID.EMESSAGE_EVENT_GAME_OTHERROOM);
//							MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
////							}
//						}
//					}
//				}).setNegativeButton(R.string.To_SelectRoom_Cancel, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//					}
//				}).create();
//		dialog.setCancelable(true);
//		dialog.show();
//	}
//	
//	/**
//	 * description 玩家离开桌子
//	 * @param aLocation 后台位置
//	 */
//	private void playerLeave(int aLocation) {
//		iPlayerManager.removePlayer(aLocation);
//		iPlayerNameManager.removePlayerName(aLocation);
//		iPlayerHPManager.removePlayerHP(aLocation);
//		int tClientLoc = 0;//客户端位置
//		tClientLoc = transLocation(aLocation);
//		SpriteButton tSB = iPlayerNameBGSBList.get(tClientLoc);
//		if (tSB != null) {
//			tSB.setVision(false);//消失名字底板
//		}
//		iPlayerNameStrList.put(tClientLoc, "");
//	}
//	/**
//	 * description 玩家说话
//	 * @param aLocation 后台位置
//	 * @param aText 说话内容
//	 */
//	private void playerChat(int aLocation, String aText) {
//		int tClientLoc = 0;//客户端位置
//		tClientLoc = transLocation(aLocation);
//		TPoint tP = iPlayerLocationList.get(tClientLoc);
//		int tX = tP.iX;
//		int tY = tP.iY;
//		if (tClientLoc == 0) {
//			//自己，因为player位置不准确，要做处理
//			tX += iX_Offset_Chat_Me[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X;
//			tY += iY_Offset_Chat_Me[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y;
//		}
//		else {
//			tX += iX_Offset_Chat[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_X;
//			tY += iY_Offset_Chat[ActivityUtil.TYPE_SCREEN]*ActivityUtil.PAK_ZOOM_Y;
//		}
//		iDialogBoxManager.setDialogBoxSrite(iChatBoxBGPak, tClientLoc, tX, tY, aText);
//		String tName = iPlayerNameStrList.get(tClientLoc);
//		aText = tName + ":" +aText;
//		messageEvent(MessageEventID.EMESSAGE_EVENT_CHAT_ADD, aText);
//	}
//	
//	/**
//	 * description 关闭弹出界面
//	 */
//	private void closePopScene() {
//		if (iPopScene == null) {
//			return;
//		}
//		switch (iPopScene.getIndex()) {
//		case SCENE_INDEX_CALL:
//			closeDiceSelect();
//			break;
//		case SCENE_INDEX_BUYBEER_NOTICE:
//			closeAutoBuyBeer();
//			break;
//		case SCENE_INDEX_BUYHP:
//			closeBuyHP();
//			break;
//		case SCENE_INDEX_PLAYERINFO_ME:
//		case SCENE_INDEX_PLAYERINFO:
//			closePlayerInfo();
//			break;
//		case SCENE_INDEX_CHAT:
//			closeChat();
//			break;
//		case SCENE_INDEX_BUYBEER:
//			closeBuyBeer();
//			break;
//		case SCENE_INDEX_MENU:
//			closeMenu();
//			break;
//		case SCENE_INDEX_ITEM:
//			closeItem();
//			break;
//		case SCENE_INDEX_ITEM_TARGET:
//			closeTarget();
//			break;
//		}
//	}
//	
//	private void showDrunkMsg() {
//		if(iNotifyResult != null && !iNotifyResult.iDrunkMsg.equals("")) {
//			Tools.showSimpleToast(iContext, Gravity.CENTER, iNotifyResult.iDrunkMsg, Toast.LENGTH_LONG);
//		}
//	}
//	
//	private void requestBuyWine() {
//		GameMain.iGameProtocol.requsetBuyWine(iUserID);
//		showProgressDialog(R.string.Loading_String);
//	}
//	
//	private boolean requestAddFriend(int aUserID) {
//		if(GameMain.iGameProtocol.requestAddFriend(aUserID, ActionID.AddFriend)) {
////			showProgressDialog(R.string.Loading_String);
//			return true;
//		}
//		return false;
//	}
//	
//	private boolean requestKick(int aUserID) {
//		if(GameMain.iGameProtocol.requestKick(aUserID)){
//			return true;
////			showProgressDialog(R.string.Loading_String);
//		}
//		return false;
//	}
//	
//	/**
//	 * description 所有玩家更新头像
//	 */
//	private void allPlayerUpdateHeadPic() {
//		iPlayerManager.allPlayerUpdateHeadPic();
//	}
//	
//	/**
//	 * description 更新某玩家的头像
//	 * @param aPicName 图片名
//	 */
//	private void playerUpdateHeadPic(String aPicName) {
//		iPlayerManager.playerUpdateHeadPic(aPicName);
//	}
//	
//	private void requestActivitEnroll(int aActivityID, int aAction) {
//		if(GameMain.getInstance().iGameProtocol.requestActivityEnroll(aActivityID, aAction)) {
//			showProgressDialog(R.string.Loading_String);
//		}
//	}
//	
//	/**
//	 * description 活动报名提示
//	 */
//	private void showMotionSignUp() {
//		playerLook(iSerLocation);
//		int tStrID = R.string.Motion_SignUp_Content;//相应提示
//		AlertDialog dialog = new AlertDialog.Builder(iContext).setTitle(R.string.Motion_SignUp_Title).setMessage(
//				iContext.getString(tStrID)).setCancelable(false)
//				.setPositiveButton(R.string.Motion_SignUp_Ok, new DialogInterface.OnClickListener()
//				{
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						Tools.debug("iUserID"+iUserID);
////						if (ibTest) {
////							//模拟游戏逻辑
////							hideSysCom();
////							ibChange = true;
////							MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
////						}
////						else {
////							//正常游戏逻辑
//////							if(iUserID != 0) {
////							hideSysCom();
////							messageEvent(MessageEventID.EMESSAGE_EVENT_GAME_OTHERROOM);
////							MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//////							}
////						}
//						Tools.showSimpleToast(iContext, Gravity.CENTER, "报名", Toast.LENGTH_SHORT);
////						requestActivitEnroll(aActivityID, aAction);
//					}
//				}).setNeutralButton(R.string.Motion_SignUp_Later, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//						Tools.showSimpleToast(iContext, Gravity.CENTER, "稍后再说", Toast.LENGTH_SHORT);
//					}
//				}).setNegativeButton(R.string.Motion_SignUp_Cancel, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//						Tools.showSimpleToast(iContext, Gravity.CENTER, "放弃", Toast.LENGTH_SHORT);
//					}
//				}).create();
////		dialog.setCancelable(true);
//		dialog.show();
//		dialog.setMessage("活动报名123");
//	}
//	
//	/**
//	 * description 活动进场提示
//	 */
//	private void showMotionStart(final MBNotifyActivityStart e) {
//		if(dialogEnroll != null && dialogEnroll.isShowing()) {
//			return;
//		}
//		playerLook(iSerLocation);
//		int tStrID = R.string.Motion_Start_Content;//相应提示
//		String str = iContext.getString(tStrID);
//		if(e != null) {
//			str = e.iMsg;
//		}
//		dialogEnroll = new AlertDialog.Builder(iContext).setTitle(R.string.Motion_Start_Title).setMessage(
//				str).setCancelable(false)
//				.setPositiveButton(R.string.Motion_Start_Ok, new DialogInterface.OnClickListener()
//				{
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						Tools.debug("iUserID"+iUserID);
////						if (ibTest) {
////							//模拟游戏逻辑
////							hideSysCom();
////							ibChange = true;
////							MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
////						}
////						else {
////							//正常游戏逻辑
//////							if(iUserID != 0) {
////							hideSysCom();
////							messageEvent(MessageEventID.EMESSAGE_EVENT_GAME_OTHERROOM);
////							MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//////							}
////						}
//						Tools.showSimpleToast(iContext, Gravity.CENTER, "现在就去", Toast.LENGTH_SHORT);
//						if(e != null) {
////							Pair<Integer, Integer> pair = new Pair<Integer, Integer>(e.iActivityRoomID, -1);
////							requestLoginRoom(pair);
//							requestLeaveRoom();
//						}
//					}
//				}).setNeutralButton(R.string.Motion_Start_Later, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//						Tools.showSimpleToast(iContext, Gravity.CENTER, "稍后再说", Toast.LENGTH_SHORT);
//						if(e != null) {
//							requestActivitEnroll(e.iActivityID, ActivityAction.HANGUP);
//						}
//					}
//				}).setNegativeButton(R.string.Motion_Start_Cancel, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//						Tools.showSimpleToast(iContext, Gravity.CENTER, "放弃", Toast.LENGTH_SHORT);
//						if(e != null) {
//							requestActivitEnroll(e.iActivityID, ActivityAction.ABORT);
//						}
//					}
//				}).create();
////		dialogEnroll.setCancelable(true);
//		dialogEnroll.show();
////		dialogEnroll.setTitle("123活动");
////		dialogEnroll.setMessage("123活动进场\n快快快快看");
//	}
//	
//	private void requestLoginRoom(Pair<Integer, Integer> pair) {
//		messageEvent(MessageEventID.EMESSAGE_EVENT_TO_PROGRESS, pair);
//	}
//	
//	private void requestLeaveRoom() {
//		GameMain.iGameProtocol.requsetLeaveRoom(iUserID);
//	}
//	
//	private void initMotionRank() {
//		iMotionDialog = new Dialog(GameMain.mGameMain, R.style.dialog);
////		iMotionDialog.setContentView(R.layout.game_motion);
////		iMotionView = (ListView) iMotionDialog.findViewById(R.id.lvGameMotion);
//		iMotionData = new ArrayList<GameMotionData>();
//		iMotionAdapter = new GameMotionAdapter(iContext, iMotionData);
//		iMotionView.setAdapter(iMotionAdapter);
//		iMotionDialog.setCanceledOnTouchOutside(true);
////		iMotionDialog.show();
//	}
//	
//	private void showMotionRank() {
////		for (int i=0; i<10; i++) {
////			GameMotionData p3 = new GameMotionData("第"+(i+1)+"名", "2", "3");
////			iMotionData.add(p3);
////		}
//		iMotionAdapter.notifyDataSetChanged();
//		iMotionView.setAdapter(iMotionAdapter);
//		iMotionDialog.show();
//	}
//	private void colseMotionRank() {
//		if(iMotionDialog != null && iMotionDialog.isShowing()) {
//			iMotionDialog.dismiss();
//		}
//	}
//	
//	private void requestActivityRank() {
//		if(GameMain.iGameProtocol.requestActivityRank(0)) {
//			showProgressDialog(R.string.Loading_String);
//		}
//	}
//
//	/**
//	 * description 初始化道具数量框位置。这里是针对弹出道具界面的，抢开按钮的在别的地方初始化
//	 */
//	private void initItemNumBGLoc() {
//		Scene pScene = null;
//		if (iUIPak != null) {
//			pScene = iUIPak.getScene(SCENE_INDEX_ITEM);
//		}
//		if(pScene != null) {
//			Map pMap = pScene.getLayoutMap(Scene.SpriteLayout);
//			Vector<?> pSpriteList = pMap.getSpriteList();
//			Sprite pSprite;
//			for (int i=0; i<pSpriteList.size(); i++) {
//				pSprite = (Sprite)pSpriteList.elementAt(i);
//				int pX = pSprite.getX();
//				int pY = pSprite.getY();
//				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_GAME_OFFSET + pSprite.getIndex();
//				TPoint pPoint = new TPoint();
//				pPoint.iX = pX;
//				pPoint.iY = pY;
//				if (pEventID>=GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_BG && pEventID<=GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_BG5) {
//					int tLocation = pEventID - GameEventID.ESPRITEBUTTON_EVENT_GAME_ITEM_BG;
//					iItemNumBGLocList.put(tLocation, pPoint);
//				}
//			}
//		}
//		pScene = null;
//	}
//	/**
//	 * description 初始化道具数量及美术字动画
//	 */
//	private void initItemNum() {
//		for (int i=0; i<6; i++) {
//			iItemIDList.put(i, i+10);
//			iItemNumList.put(i, 0);
//			iItemCanUseList.put(i, 1);
//			ArtNumber pArtNumber = new ArtNumber(iArtNumberPak);
//			TPoint pPoint = iItemNumBGLocList.get(i);
//			if (pPoint != null) {
//				pArtNumber.setPosition(pPoint.iX, pPoint.iY);
//			}
//			pArtNumber.setValue(i);
//			iItemArtNumList.put(i, pArtNumber);
//		}
//	}
//	/**
//	 * description 根据key,设置道具数量
//	 * @param aItemKey 道具Key
//	 * @param aNum 数量
//	 */
//	private void setItemNumByKey(int aItemKey, int aNum) {
//		iItemNumList.put(aItemKey, aNum);
//		ArtNumber pArtNum = iItemArtNumList.get(aItemKey);
//		if (pArtNum != null) {
//			pArtNum.setValue(aNum);
//		}
//	}
//	/**
//	 * description 根据ID,设置道具数量
//	 * @param aItemID 道具ID,后台定的ID
//	 * @param aNum 数量
//	 */
//	private void setItemNumByID(int aItemID, int aNum) {
//		int tKey = -1;
//		Iterator iter = iItemIDList.entrySet().iterator();
//		while (iter.hasNext()) {
//			HashMap.Entry entry = (HashMap.Entry) iter.next();
//			Object key = entry.getKey();
//			Object val = entry.getValue();
//			int tID = (Integer) val;
//			if (tID == aItemID) {
//				tKey = (Integer)key;
//				break;
//			}
//		}
//		setItemNumByKey(tKey, aNum);
//	}
//	
//	private void requestUseItem(int aItem, Vector<Integer> aDesSeatIDs) {
//		if(GameMain.iGameProtocol.RequestUseItem(aItem, aDesSeatIDs)) {
////			showProgressDialog(R.string.Loading_String);
//		}
//	}
//
//	/**
//	 * description 隐藏道具相关的东西
//	 */
//	private void hideItemThing() {
//		closeItem();
//		if (iItemSB != null) {
//			iItemSB.setVision(false);
//		}
//		if (iQuickDuelSB != null) {
//			iQuickDuelSB.setVision(false);
//		}
//		if (iQuickDuelNumBGSB != null) {
//			iQuickDuelNumBGSB.setVision(false);
//		}
//		closeTarget();
//	}
//	
//	/**
//	 * description 获取所有正在游戏中的玩家后台位置
//	 * @return 后台位置列表
//	 */
//	private Vector<Integer> getAllPlayingLoc() {
//		return iPlayerNameManager.getAllPlayingLoc();
//	}
//	
//	/**
//	 * description 自己的骰子都变成1
//	 */
//	public void allMyDiceChangeToOne() {
//		for (int i=0; i<5; i++) {
//			setDice(i, 1);
//		}
//	}
//	
//	/**
//	 * description 自己的骰子都变成后台指定的点数,【千王】效果
//	 */
//	public void allMyDiceChange() {
//		allMyDiceChange(iChangeDicePoint);
//	}
//	/**
//	 * description 自己的骰子都变成1
//	 */
//	private void allMyDiceChange(int aPoint) {
//		for (int i=0; i<5; i++) {
//			setDice(i, aPoint);
//		}
//	}
//	
//	private Integer getItemID(int key) {
//		Integer ItemID = null;
//		if(key > 6) {
//			return ItemID;
//		}
//		ItemID = iItemIDList.get(MAP_ITEM_KEY[key]);
//		return ItemID;
//	}
//	/**
//	 * description 根据道具ID获取Key
//	 * @param aItemID
//	 * @return
//	 */
//	private Integer getItemKey(int aItemID) {
//		int tKey = -1;
//		Iterator iter = iItemIDList.entrySet().iterator();
//		while (iter.hasNext()) {
//			HashMap.Entry entry = (HashMap.Entry) iter.next();
//			Object key = entry.getKey();
//			Object val = entry.getValue();
//			int tID = (Integer) val;
//			if (tID == aItemID) {
//				tKey = (Integer)key;
//				break;
//			}
//		}
//		return tKey;
//	}
//	
//	private void addTargetPlayer(int aLocation, String aHeadPicName, int aRoleID, int aDicePakID) {
//		int tClientLoc = 0;//客户端位置
//		tClientLoc = transLocation(aLocation);
//		TPoint tSpriteLoc = iTargetLocationList.get(tClientLoc);//精灵的坐标
//		Project tPlayerPak = getPlayerPak(aRoleID);
//		iTargetManager.setPlayer(tPlayerPak, tClientLoc, aLocation, tSpriteLoc.iX, tSpriteLoc.iY, aHeadPicName, aDicePakID);
//	}
//	
//	private void addTargetPlayer() {
//		Vector<Integer> tList = getAllPlayingLoc();
//		if (tList == null) {
//			return;
//		}
//		for (int i=0; i<tList.size(); i++) {
//			int tLoc = tList.elementAt(i);
//			if (tLoc == iSerLocation) {
//				continue;
//			}
//			int tClientLoc = 0;//客户端位置
//			tClientLoc = transLocation(tLoc);
//			Player tP = iPlayerManager.getPlayer(tLoc);
//			String tHeadPicName = tP.getHeadPicName();
//			int tRoleID = tP.getRoleID();
//			int tDicePakID = tP.getDicePakID();
//			addTargetPlayer(tLoc, tHeadPicName, tRoleID, tDicePakID);
//		}
//	}
//	
//	private void removeAllTargetPlayer() {
//		iTargetManager.removeAll();
//	}
//	
//	/**
//	 * description 设置道具动画，使用者是自己
//	 * @param aTargetLoc 目标后台位置
//	 */
//	private void setItemAnim(int aTargetLoc) {
//		int tClientLoc = 0;//客户端位置
//		tClientLoc = transLocation(aTargetLoc);
//		TPoint tSpriteLoc = iPlayerLocationList.get(tClientLoc);//精灵的坐标
//		if (tSpriteLoc == null) {
//			return;
//		}
//		int tItemKey = getItemKey(iServerUseItemID);
//		iItemAnimManager.setItemAnimationSprite(iItemAnimationPak, aTargetLoc, tItemKey, tSpriteLoc.iX, tSpriteLoc.iY);
//	}
//	/**
//	 * description 设置道具动画
//	 * @param aUserLoc 使用者后台位置
//	 * @param aTargetLoc 目标后台位置
//	 */
//	private void setItemAnim(int aUserLoc, int aTargetLoc) {
//		int tUserClientLoc = 0;//使用者客户端位置
//		tUserClientLoc = transLocation(aUserLoc);
//		TPoint tUserSpriteLoc = iPlayerLocationList.get(tUserClientLoc);//精灵的坐标
//		if (tUserSpriteLoc == null) {
//			return;
//		}
//		int tTargetClientLoc = 0;//目标客户端位置
//		tTargetClientLoc = transLocation(aTargetLoc);
//		TPoint tSpriteLoc = iPlayerLocationList.get(tTargetClientLoc);//精灵的坐标
//		if (tSpriteLoc == null) {
//			return;
//		}
//		int tItemKey = getItemKey(iServerUseItemID);
//		iItemAnimManager.setItemAnimationSprite(iItemAnimationPak, aTargetLoc, tItemKey, 
//				tUserSpriteLoc.iX, tUserSpriteLoc.iY, tSpriteLoc.iX, tSpriteLoc.iY);
//	}
//	/**
//	 * description 设置道具触发
//	 * @param aUserLoc 使用者后台位置
//	 */
//	private void triggerItem(int aUserLoc) {
//		int tClientLoc = 0;//客户端位置
//		tClientLoc = transLocation(aUserLoc);
//		TPoint tSpriteLoc = iPlayerLocationList.get(tClientLoc);//精灵的坐标
//		if (tSpriteLoc == null) {
//			return;
//		}
//		int tItemKey = getItemKey(iServerUseItemID);
//		iItemAnimManager.triggerItem(iItemAnimationPak, aUserLoc, tItemKey, tSpriteLoc.iX, tSpriteLoc.iY);
//	}
//	private void removeAllItemAnim() {
//		iItemAnimManager.clear();
//	}
//	
//}
