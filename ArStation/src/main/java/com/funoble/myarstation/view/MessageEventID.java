package com.funoble.myarstation.view;

//message 事件ID
public abstract class MessageEventID {
	public static final int EMESSAGE_EVENT_TO_PROGRESS = 0;//到游戏桌子界面 事件
	public static final int EMESSAGE_EVENT_TO_LOGIN = EMESSAGE_EVENT_TO_PROGRESS + 1;//回到登录界面
	public static final int EMESSAGE_EVENT_TO_HOME = EMESSAGE_EVENT_TO_LOGIN + 1;//回到首页界面
	public static final int EMESSAGE_EVENT_TO_REGISTER = EMESSAGE_EVENT_TO_HOME + 1;//到注册界面
	public static final int EMESSAGE_EVENT_TO_CREATEROLE = EMESSAGE_EVENT_TO_REGISTER + 1;//到创建角色界面
	public static final int EMESSAGE_EVENT_TO_STORE = EMESSAGE_EVENT_TO_CREATEROLE + 1;//商城界面
	public static final int EMESSAGE_EVENT_TO_SETTING = EMESSAGE_EVENT_TO_STORE + 1;//设置界面
	public static final int EMESSAGE_EVENT_TO_HELP = EMESSAGE_EVENT_TO_SETTING + 1;//帮助界面
	public static final int EMESSAGE_EVENT_TO_GUIDE = EMESSAGE_EVENT_TO_HELP + 1;//新手教程界面
	public static final int EMESSAGE_EVENT_TO_Rank = EMESSAGE_EVENT_TO_GUIDE + 1;//排行榜界面
	public static final int EMESSAGE_EVENT_TO_INFO = EMESSAGE_EVENT_TO_Rank + 1;//个人资料界面
	public static final int EMESSAGE_EVENT_TO_FRIENDINFO = EMESSAGE_EVENT_TO_INFO + 1;//好友资料界面
	public static final int EMESSAGE_EVENT_TO_ALBUM = EMESSAGE_EVENT_TO_FRIENDINFO + 1;//相册界面
	public static final int EMESSAGE_EVENT_TO_SELECTROOM = EMESSAGE_EVENT_TO_ALBUM + 1;//选择房间界面
	public static final int EMESSAGE_EVENT_TO_ACTIVIY = EMESSAGE_EVENT_TO_SELECTROOM + 1;//活动界面
	public static final int EMESSAGE_EVENT_TO_AD = EMESSAGE_EVENT_TO_ACTIVIY + 1;//网页
	public static final int EMESSAGE_EVENT_TO_SHAKEADDFRIEND = EMESSAGE_EVENT_TO_AD + 1;//摇一摇加好友
	public static final int EMESSAGE_EVENT_TO_HALLGAME = EMESSAGE_EVENT_TO_SHAKEADDFRIEND + 1;//游戏大厅
	
	public static final int EMESSAGE_EVENT_CHAT_ADD = 100;//添加聊天记录
	public static final int EMESSAGE_EVENT_CHAT_CLOSE = EMESSAGE_EVENT_CHAT_ADD+1;//关闭聊天框
	public static final int EMESSAGE_EVENT_HOME_INIT_MSG = EMESSAGE_EVENT_CHAT_CLOSE+1;//初始化公告组件
	public static final int EMESSAGE_EVENT_GAME_OTHERROOM = EMESSAGE_EVENT_HOME_INIT_MSG+1;//其它房间
	
	public static final int SOCKET = 1000;//偏移
	public static final int EMESSAGE_EVENT_SOCKET_ResultSuccess = SOCKET;//请求成功
	public static final int EMESSAGE_EVENT_SOCKET_DISCREATE = SOCKET + 1;//创建失败
	public static final int EMESSAGE_EVENT_SOCKET_DISCONNECT = SOCKET + 2;//连接失败
	public static final int EMESSAGE_EVENT_SOCKET_RegisterSuccess = SOCKET + 3;//请求成功
	public static final int EMESSAGE_EVENT_SOCKET_NotifyShout = SOCKET + 4;//应答喊点
	public static final int EMESSAGE_EVENT_SOCKET_NotifyChop = SOCKET + 5;//应答劈
	public static final int EMESSAGE_EVENT_SOCKET_NotifyResult = SOCKET + 6;//应答结果
	public static final int EMESSAGE_EVENT_SOCKET_ReqLeaveRoom = SOCKET + 7;//请求退出房间
	public static final int EMESSAGE_EVENT_SOCKET_RspLeaveRoom = SOCKET + 8;//应答退出房间
	public static final int EMESSAGE_EVENT_SOCKET_NotifyKick = SOCKET + 9;//通知被踢
	public static final int EMESSAGE_EVENT_SOCKET_NotifLoginRoom = SOCKET + 10;//通知登陆房间 
	public static final int EMESSAGE_EVENT_SOCKET_NotifShowReady = SOCKET + 11;//通知显示准备 
	public static final int EMESSAGE_EVENT_SOCKET_RspfLoginRoom = SOCKET + 12;//登陆房间
	public static final int EMESSAGE_EVENT_SOCKET_RspCreateRoleSuccess = SOCKET + 13;//创建角色成功
	public static final int EMESSAGE_EVENT_SOCKET_RspGoodsList = SOCKET + 14;//商品列表
	public static final int EMESSAGE_EVENT_SOCKET_Change = SOCKET + 15;//充值成功调整
	public static final int EMESSAGE_EVENT_SOCKET_Buy = SOCKET + 16;//购买物品成功
	public static final int EMESSAGE_EVENT_SOCKET_VIPInfo = SOCKET + 17;//请求vip信息成功
	public static final int EMESSAGE_EVENT_SOCKET_MyGoodsInfo = SOCKET + 18;//请求我的物品 成功
	public static final int EMESSAGE_EVENT_SOCKET_BusinessDetail = SOCKET + 19;//请求物品明细 成功
	public static final int EMESSAGE_EVENT_SOCKET_NotifyRecharge = SOCKET + 20;//充值成功通知
	public static final int EMESSAGE_EVENT_SOCKET_NotifyKICKOpenHomeScreen = SOCKET + 21;//被踢通知打开主页
	public static final int EMESSAGE_EVENT_SOCKET_NotifyChat = SOCKET + 22;//聊天内容通知
	public static final int EMESSAGE_EVENT_SOCKET_RspPlayerInfo = SOCKET + 23;//应答玩家消息
	public static final int EMESSAGE_EVENT_SOCKET_NotifyCleanDunk = SOCKET + 24;//应答购买醒酒丸
	public static final int EMESSAGE_EVENT_SOCKET_RspMainPage = SOCKET + 25;//应答主页信息
	public static final int EMESSAGE_EVENT_SOCKET_ShowDrunkMsg = SOCKET + 26;//显示灌倒消息
	public static final int EMESSAGE_EVENT_SOCKET_ShowMainPageLoading = SOCKET + 27;//显示主界面Loading
	public static final int EMESSAGE_EVENT_SOCKET_RspFeekBack = SOCKET + 28;//反馈应答
	public static final int EMESSAGE_EVENT_SOCKET_RspBuyWine = SOCKET + 29;//购买酒应答
	public static final int EMESSAGE_EVENT_SOCKET_RspUpdate = SOCKET + 30;//应答更新
	public static final int EMESSAGE_EVENT_SOCKET_ShowUpdateDialog = SOCKET + 31;//显示更新dialog
	public static final int EMESSAGE_EVENT_SOCKET_PwdAndUserIDShort = SOCKET + 32;//提示账号或密码过短
	public static final int EMESSAGE_EVENT_SOCKET_Heart = SOCKET + 33;//应答心跳
	public static final int EMESSAGE_EVENT_SOCKET_LeaveRoom = SOCKET + 34;//通知离开房间
	public static final int EMESSAGE_EVENT_SOCKET_NOTIFY_READY = SOCKET + 35;//通知准备
	public static final int EMESSAGE_EVENT_SOCKET_NOTIFY_START = SOCKET + 36;//通知开始
	public static final int EMESSAGE_EVENT_SOCKET_NOTIFY_SHOW_READY = SOCKET + 37;//通知显示开始
	public static final int EMESSAGE_EVENT_SOCKET_NOTIFY_LOGINROOM = SOCKET + 38;//通知登陆房间
	public static final int EMESSAGE_EVENT_SOCKET_VISITORLOGIN = SOCKET + 39;//应答游客登录
	public static final int EMESSAGE_EVENT_SOCKET_Rank_Wealths = SOCKET + 40;//应答财富
	public static final int EMESSAGE_EVENT_SOCKET_Rank_Level = SOCKET + 41;//应答等级
	public static final int EMESSAGE_EVENT_SOCKET_Rank_Victory = SOCKET + 42;//应答胜场
	public static final int EMESSAGE_EVENT_SOCKET_PlayerInfoTwo = SOCKET + 43;//应答玩家信息2
	public static final int EMESSAGE_EVENT_SOCKET_AddFriend = SOCKET + 44;//应答加好友
	public static final int EMESSAGE_EVENT_SOCKET_FriendList = SOCKET + 45;//应答好友列表
	public static final int EMESSAGE_EVENT_SOCKET_RequestFriendInfo = SOCKET + 46;//请求好友信息
	public static final int EMESSAGE_EVENT_SOCKET_RspDing = SOCKET + 47;//应答顶
	public static final int EMESSAGE_EVENT_SOCKET_Reconnection = SOCKET + 48;//重连
	public static final int EMESSAGE_EVENT_SOCKET_RspUpLoadPic = SOCKET + 49;//上传图片应答
	public static final int EMESSAGE_EVENT_REQUEST_CAMERA_IMAGE = SOCKET + 50;//请求拍照
	public static final int EMESSAGE_EVENT_SOCKET_RSP_DELPIC = SOCKET + 51;//应道删除图片
	public static final int EMESSAGE_EVENT_SOCKET_RSP_ROOMLIST = SOCKET + 52;//应答房间列表
	public static final int EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_PIC = SOCKET + 53;//应答完成图片下载
	public static final int EMESSAGE_EVENT_SOCKET_RSP_KICK = SOCKET + 54;//应答踢人
	public static final int EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_SINGLE_PIC = SOCKET + 55;//单张图片下载完成通知
	public static final int EMESSAGE_EVENT_SOCKET_NOTIFY_SYS_MSG = SOCKET + 56;//系统消息提示
	public static final int EMESSAGE_EVENT_UPDATE_SHAKE_TIME = SOCKET + 57;//更新摇好友时间
	public static final int EMESSAGE_EVENT_COUNTDOWNTIME_UP = SOCKET + 58;//要好友倒计时完成
	public static final int EMESSAGE_EVENT_SOCKET_RSP_SHAKE_FRIEND = SOCKET + 59;//应答摇
	public static final int EMESSAGE_EVENT_SOCKET_PlayerInfoTHREE = SOCKET + 60;//应答玩家消息
	public static final int EMESSAGE_EVENT_HTTP_UPDATE_PROGRESS = SOCKET + 61;//http更新显示进度
	public static final int EMESSAGE_EVENT_HTTP_UPDATE_ERROR = SOCKET + 62;//http更新出错
	public static final int EMESSAGE_EVENT_INSTALL_APK = SOCKET + 63;//安装apk
	public static final int EMESSAGE_EVENT_REQUEST_ACIIVITY_DETAIL = SOCKET + 64;//请求活动详情
	public static final int EMESSAGE_EVENT_RSP_ACIIVITY_DAILY = SOCKET + 65;//应答每日活动
	public static final int EMESSAGE_EVENT_RSP_ACIIVITY_WEEKLY = SOCKET + 66;//应答每周活动
	public static final int EMESSAGE_EVENT_RSP_ACIIVITY_MONTHLY = SOCKET + 67;//应答每月活动
	public static final int EMESSAGE_EVENT_RSP_ACIIVITY_COMP = SOCKET + 68;//应答争霸赛
	public static final int EMESSAGE_EVENT_REQ_ACIIVITY_ENROLL = SOCKET + 69;//请求参与
	public static final int EMESSAGE_EVENT_RSP_ACIIVITY_DETAIL = SOCKET + 70;//应答活动详情
	public static final int EMESSAGE_EVENT_RSP_ACIIVITY_RESUOLT = SOCKET + 71;//应答活动结果
	public static final int EMESSAGE_EVENT_RSP_ACIIVITY_ENROLL = SOCKET + 72;//应答活动报名
	public static final int EMESSAGE_EVENT_RSP_ACIIVITY_JOIN = SOCKET + 73;//应答活动参与
	public static final int EMESSAGE_EVENT_REQ_ACIIVITY_RESULT = SOCKET + 74;//应答活动结果
	public static final int EMESSAGE_EVENT_NOTIFY_ACIIVITY_START = SOCKET + 75;//活动开始消息
	public static final int EMESSAGE_EVENT_RSP_ACIIVITY_RANK = SOCKET + 76;//活动名次排行
	public static final int EMESSAGE_EVENT_RSP_ACIIVITY_RANKING_SELF = SOCKET + 77;//自己活动名次排行
	public static final int EMESSAGE_EVENT_RSP_USEITEM = SOCKET + 78;//使用道具
	public static final int EMESSAGE_EVENT_NOTIFY_SHOWITEM = SOCKET + 79;//显示道具
	public static final int EMESSAGE_EVENT_NOTIFY_NOTIFY_VIE_CHOP = SOCKET + 80;//道具抢劈
	public static final int EMESSAGE_EVENT_NOTIFY_CHANGE_DICE = SOCKET + 81;//使用千王之王道具的消息
	public static final int EMESSAGE_EVENT_NOTIFY_SHIELD = SOCKET + 82;////使用催眠怀表的消息
	public static final int EMESSAGE_EVENT_NOTIFY_MANY_CHOP = SOCKET + 83;//使用连劈闪电的消息
	public static final int EMESSAGE_EVENT_NOTIFY_TRIGGER_SHIELD = SOCKET + 84;//触发催眠怀表的消息
	public static final int EMESSAGE_EVENT_NOTIFY_KILL_ORDER = SOCKET + 85;//使用追杀令的消息
	public static final int EMESSAGE_EVENT_NOTIFY_UNARMED = SOCKET + 86;//使用禁劈道具的消息
	public static final int EMESSAGE_EVENT_REQ_LISTLOGINROOM = SOCKET + 87;//请求登录房间
	public static final int EMESSAGE_EVENT_REQ_LOADPHOTOFROMALBUM = SOCKET + 88;//从相册读取图片
	public static final int EMESSAGE_EVENT_RSP_PALYERINFOTHR = SOCKET + 89;//应答玩家信息3
	public static final int EMESSAGE_EVENT_NOTIFY_DING = SOCKET + 90;//顶通知
	public static final int EMESSAGE_EVENT_NOTIFY_LEVELUP = SOCKET + 91;//升级通知
	public static final int EMESSAGE_EVENT_SOCKET_RspTrace = SOCKET + 92;//应答追踪
	public static final int EMESSAGE_EVENT_RSP_SHAKE_FRIEND = SOCKET + 93;//应答摇好友
	public static final int EMESSAGE_EVENT_RANK_SEEFRIENDINF = SOCKET + 94;//查看好友详情
	public static final int EMESSAGE_EVENT_RSP_DELETE_FRIEND = SOCKET + 95;//删除好友
	public static final int EMESSAGE_EVENT_RSP_GIFTLIST = SOCKET + 96;//礼品信息应答
	public static final int EMESSAGE_EVENT_REQ_ACTIVITY_RANK = SOCKET + 97;//请求活动排行
	public static final int EMESSAGE_EVENT_SHOW_CHANGEDICE = SOCKET + 98;//显示换骰子界面
	public static final int EMESSAGE_EVENT_RSP_CHANGEDICE = SOCKET + 99;//换骰子应答
	public static final int EMESSAGE_EVENT_RSP_GIFT_FRIEND_GOODS = SOCKET + 100;//赠送好友物品应答
	public static final int EMESSAGE_EVENT_SOCKET_RSP_ROOMLIST_B = SOCKET + 101;//房间列表B应答
	public static final int EMESSAGE_EVENT_SOCKET_RSP_LEAVEMSG = SOCKET + 102;//应答留言
	public static final int EMESSAGE_EVENT_SOCKET_RSP_LEAVELISTMSG = SOCKET + 103;//应答留言列表
	public static final int EMESSAGE_EVENT_SOCKET_RSP_ACCOUNT = SOCKET + 104;//应答账号
	public static final int EMESSAGE_EVENT_SOCKET_RspGoodsListGb = SOCKET + 105;//金币商品列表
	public static final int EMESSAGE_EVENT_SOCKET_RspSearchPeople = SOCKET + 106;//寻找玩家
	public static final int EMESSAGE_EVENT_SOCKET_RspLoginVipRoom = SOCKET + 107;//viproom
	public static final int EMESSAGE_EVENT_SOCKET_NOTIFY_NOANSWER = SOCKET + 108;//设置密保通知
	public static final int EMESSAGE_EVENT_SOCKET_RSPSETANSWER = SOCKET + 109;//设置密保应答
	public static final int EMESSAGE_EVENT_SOCKET_RSPRESETPSWD = SOCKET + 110;//设置密码
	public static final int EMESSAGE_EVENT_SOCKET_NOTIFY_LOGINROOM_B = SOCKET + 111;//通知登陆房间B
	public static final int EMESSAGE_EVENT_SOCKET_RSP_LOGINROOM_B = SOCKET + 112;//登陆房间B
	public static final int EMESSAGE_EVENT_NOTIFY_NOTIFY_VIE_CHOP_B = SOCKET + 113;//道具抢劈B
	
	public static final int EMESSAGE_EVENT_REQ_DISPLAY_SYSCOM = 2000;//请求显示当前VIEW的系统控件
	public static final int EMESSAGE_EVENT_REQ_HIDE_SYSCOM = 2001;//请求隐藏当前VIEW的系统控件
	public static final int EMESSAGE_EVENT_NOTIFY_SOCKET_ERROR = 2002; //网络出错
	public static final int EMESSAGE_EVENT_REQ_REBUILD_SYSCOM = 2003; //请求创建系统控件
	public static final int EMESSAGE_EVENT_RECONNET_IPSTR = 2004;	//请求用新的IP重新连接网络
	public static final int EMESSAGE_EVENT_TO_CONNECTED = 2005;	//连接成功
	public static final int EMESSAGE_EVENT_REQ_THIRDLOGIN = 2006;	//第三方登录请求
	public static final int EMESSAGE_EVENT_CLOSE_SOCKET = 2007; //关闭socket
	public static final int EMESSAGE_EVENT_TO_REFRESH_HOMEMAIN = 2008;	//刷新主界面
	public static final int EMESSAGE_EVENT_TO_ACTIVITY_INFO = 2009;	//活动信息
	public static final int EMESSAGE_EVENT_TO_LEAVEMSG_INFO = 2010;	//留言条数
	public static final int EMESSAGE_EVENT_REQ_DELETE_SYSCOM = 2011;	//留言条数
	public static final int EMESSAGE_EVENT_ANIMATIONTWO = 2012;	//动画
	
	public static final int EMESSAGE_EVENT_REQ_CREATE_ROLE = 3000;//请求创建角色
	public static final int EMESSAGE_EVENT_REQ_SHOW_MSG = 3001;	//请求显示信息
	public static final int EMESSAGE_EVENT_SHOW_DECR = 3002;	//描述
	
	public static final int CLASSICALGAME = 4000;//应答事件
	public static final int EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_FRINDEVENT = CLASSICALGAME+1;//好友事件通知
	public static final int EMESSAGE_EVENT_CLASSICALGAME_RSP_FRINDEVENTSELECT = CLASSICALGAME+2;//好友事件选着应答
	public static final int EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_PALY_ANIMATION = CLASSICALGAME+3;//播放动画通知
	public static final int EMESSAGE_EVENT_HOME_RSP_INVITE = CLASSICALGAME+4;//邀请好友应答
	public static final int EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_FACE = CLASSICALGAME+5;//表情通知
	public static final int EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_REBEL_CHOP = CLASSICALGAME + 6; //反劈通知
	public static final int EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_SHAKE_FIREND = CLASSICALGAME + 7; //醒酒通知
	public static final int EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_LIGHTNING = CLASSICALGAME + 8; //雷击通知
	public static final int EMESSAGE_EVENT_CLASSICALGAME_PALYERINFOFOUR = CLASSICALGAME + 9; //用户信息
	public static final int EMESSAGE_EVENT_CLASSICALGAME_NOTIFY_PALY_ANIMATIONTWO = CLASSICALGAME+10;//播放动画通知2


	public static final int SHAKEADDFRIENDS = 5000;//
	public static final int EMESSAGE_EVENT_SHAKEADDFRIENDS_INIT = SHAKEADDFRIENDS + 1;//初始化控件
	
	public static final int UNITEGAME = 6000;
	public static final int EMESSAGE_EVENT_SOCKET_RESPONE_UNITE = UNITEGAME + 1;//应当解盟
	public static final int EMESSAGE_EVENT_SOCKET_NOTIFY_STARTUNITE = UNITEGAME + 2;//游戏开始通知
	public static final int EMESSAGE_EVENT_SOCKET_NOTIFY_RESULTUNITE = UNITEGAME + 3;//游戏结束通知
	public static final int EMESSAGE_EVENT_SOCKET_NOTIFY_UNITE = UNITEGAME + 4;//同(解)盟通知
	public static final int EMESSAGE_EVENT_UNITEGAME_PALYERINFOFOUR = UNITEGAME + 5; //用户信息
	
	public static final int STORESCREEN = 7000;
	public static final int EMESSAGE_EVENT_SOCKET_RESPONE_UID = STORESCREEN + 1;//请求uid
	public static final int EMESSAGE_EVENT_SOCKET_RESPONE_VIPUPGRADE = STORESCREEN + 2;//vip升级
	
	public static final int REGISTERSCREEN = 8000;
	public static final int EMESSAGE_EVENT_REQUEST_REGISTER = REGISTERSCREEN + 1;//注册请求
	
	public static final int LOGIONSCREEN = 9000;
	public static final int EMESSAGE_EVENT_RESET_THIRD_USERINFO = LOGIONSCREEN + 1;//第三方帐户需要重置用户信息，转成公版用户
	public static final int EMESSAGE_EVENT_RSP_RESET_THIRD_USERINFO = LOGIONSCREEN + 2;//
	public static final int EMESSAGE_EVENT_LOGIN_LOADDATA = LOGIONSCREEN + 3;//
	public static final int EMESSAGE_EVENT_LOGIN_SUCCESS = LOGIONSCREEN + 4;//登录成功
	public static final int EMESSAGE_EVENT_LOGIN_CANCEL_REGISTER = LOGIONSCREEN + 5;//登录成功
	
	public static final int HOMESCREEN = 10000;
	public static final int EMESSAGE_EVENT_HOME_AWARD = HOMESCREEN + 1;//领奖应答
	
	public static final int MISSIONSCREEN = 10010;
	public static final int EMESSAGE_EVENT_MISSIONSCREEN_INIT = MISSIONSCREEN + 1;//初始化
	public static final int EMESSAGE_EVENT_MISSIONSCREEN_RSPMISSIONLIST = MISSIONSCREEN + 2;//任务列表应答
	public static final int EMESSAGE_EVENT_MISSIONSCREEN_RSPAWARD = MISSIONSCREEN + 3;//领奖应答
	
	public static final int FRIENDDETAILSCREEN = 10020;
	public static final int EMESSAGE_EVENT_RSP_HOMEGIFTLIST = FRIENDDETAILSCREEN + 1;//应答礼品列表
	public static final int EMESSAGE_EVENT_NOTIFY_PLAYANIMATE = FRIENDDETAILSCREEN + 2;//播放动画
	public static final int EMESSAGE_EVENT_RSP_HOMERECEIVEGIFTLIST = FRIENDDETAILSCREEN + 3;//应答礼品列表
	public static final int EMESSAGE_EVENT_SHOWMSG = FRIENDDETAILSCREEN + 4;//应答礼品列表
	
	public static final int INFOSCREEN = 10030;
	public static final int EMESSAGE_EVENT_HOMETAKEGIFT = INFOSCREEN + 0;//捡礼物
}	

