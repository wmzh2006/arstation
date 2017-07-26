package com.funoble.myarstation.socket.protocol;

public interface ProtocolType {
	public class Url implements ProtocolType {
//		public static final String ip = "funoble.3322.org";
//		public static final String ip = "funoble.xicp.net";
//		public static final String ip = "dht.funnoble.com";
//		public static final String ip = "www.funoble.com";
		public static final String ip = "120.132.132.101";
//		public static final int port = 60001;
		public static final int port = 60002;
//		public static int port = 60001;
	}

	public class ActionID implements ProtocolType {
		public static final int AddFriend = 0;
		public static final int DelFriend = 1;
	}
	
	public class ActivityAction implements ProtocolType {
		public static final int NULL 	= 0;// 0 --- 无操作
		public static final int ENROLL 	= 1;//报名
		public static final int JIONIN 	= 2;//参与
		public static final int HANGUP 	= 3;//挂起
		public static final int ABORT 	= 4;//放弃
	}
	
	public class ResultCode implements ProtocolType {
		//成功
		public static final int SUCCESS					= 	0;		//请求成功
		//编辑码 -1 ～ -100
		public static final int ERR_DECODEBUF 			= 	-1;		//解码出错
		public static final int ERR_TIMEOUT	 			=	-2;		//超时
		public static final int ERR_BUSY				=	-3;     //服务器忙
		public static final int ERR_TABLE_PLYER_NULL	=	-4;		//座位上的player为空
		public static final int ERR_PLAYER_NULL			=	-5;		//player突然变成空

		//注册   -101 ~ -200
		public static final int  ERR_REG_USER_ALREADY_EXIST	 	= -101;	//此用户已被注册
		public static final int  ERR_REG_NAME_PWD_INVALID		= -102;	//用户名或者密码长度非法
		public static final int  ERR_REG_FAULT		= -103;	//申请账号失败

		//登录	-201 ~ -300
		public static final int ERR_LOGIN_USER_INVALID	=	-201;	//不存在此用户
		public static final int ERR_LOGIN_PWD_INVALID	=	-202;	//密码无效	
		public static final int ERR_LOGIN_ROLE_NULL		=	-203;	//没有创建角色
		public static final int ERR_LOGIN_NAME_INVALID	=	-204;	//帐号非法
		public static final int ERR_LOGIN_NEED_RESET_THIRD_USERINFO =	-205;//第三方帐户需要重置用户信息，转成公版用户
		public static final int ERR_LOGIN_NEED_PUBLIC_UID =		-206;//已经转过公版，需要使用公版帐登录

		//错误码
		public static final int ERR_RESET_THIRD_PWD_INVALID	=	-230;//密码非法
		public static final int ERR_RESET_THIRD_USER_ID_INVALID	=	-231;//帐号非法
		public static final int ERR_RESET_THIRD_PWQ_INVALID	=	-232;//密码提示问题非法

		
		//创建角色
		public static final int ERR_CREATEROLE_MODELID_INVALID	= -301;	//模型ID非法
		public static final int ERR_CREATEROLE_NICK_LEN_INVALID = -302;	//昵称长度非法


		//进入房间 -401 ~ -500
		public static final int ERR_ROOM_TABLE_INVALED	=	-401;	//桌子ID非法
		public static final int ERR_ROOM_TABLE_FULL		=	-402;	//桌子已满
		public static final int ERR_ROOM_FULL			=	-403;	//房间已满
		public static final int ERR_ROOM_GB_NOTENOUGH	=	-404;	//金币不够
		public static final int ERR_ROOM_DRUNK			=	-405;	//醉了
		public static final int ERR_ROOM_PLAYER_INVALED	=	-406;   //非法用户
		public static final int ERR_ROOM_GB_LIMIT		=	-407;	//金币超过房间的最大限制

		//举手 -501 ~ -600
		public static final int ERR_HANDUP_PLAYER_NULL  =    -501;	//找不到相应的Player
		public static final int ERR_HANDUP_TABLE_NULL   =    -502;	//找不到相应的桌子
		public static final int ERR_HANDUP_PLAYING		=	 -503;	//正在游戏中
		public static final int ERR_HANDUP_TABLEID_INVALID = -504;	//桌子ID不可用
		
		//结果 -601 ~ 700			
		public static final int ERR_RESULT_PLAYER_RUNOFF =	-601;	//有人逃跑
		
		//退出房间 -701 ~ -800
		public static final int	ERR_STANDUP_RUNOFF		=	-701;	//逃跑
		public static final int ERR_STANDUP_SEATID_INVALID	= -702;	//座位ID非法

		//喊劈 -801 ~ -900
		public static final int ERR_SHOUT_GAME_NOT_START	= -801;	//游戏没开始
		public static final int ERR_SHOUT_NEXT_PLAYER		= -802;	//找不到下一个Player
		public static final int ERR_CHOP_GAME_NOT_START     = -803;	//游戏没开始
		public static final int ERR_SHOUT_PLAYER_INVALED	= -804;	//非法玩家
		public static final int ERR_SHOUT_DICE_INVALED		= -805;	//喊了非法的点数

		//踢 -901 ～ -1000
		public static final int	ERR_KICK_DRUNK				= -901;	//醉
		public static final int ERR_KICK_NO_WINE			= -902;	//没有酒
		public static final int ERR_KICK_NO_GB_DRUNK        = -903;	//没有足够的金币解酒
		public static final int ERR_KICK_NO_GB_WINE			= -904;	//没有足够的金币买酒
		public static final int ERR_KICK_NO_GB_ROOM			= -905;	//您的金币少于房间的限制
		public static final int ERR_KICK_VIP				= -906;	//您被VIP玩家踢出房间
		public static final int ERR_KICK_NO_VIP				= -907;	//您不是VIP
		public static final int ERR_KICK_NOT_FIND_PLAYER	= -908;	//找不到指定的PLAYER
		public static final int ERR_KICK_HIGH_VIP			= -909;	//您没有足够的特权
		public static final int ERR_KICK_FAIL_PLAYINGGAME	= -910;	//正在游戏中
		public static final int ERR_KICK_VIP_INVALID 		= -911;	//您的VIP已经过期


		//获取商品 -1001 ~ -1100			
		public static final int ERR_GOODS_NULL				= -1001;//没有商品

		//交易商品 -1101 ~ -1200			
		public static final int ERR_BUSINESS_GOODSID_INVALED	= -1101;//商品ID无效
		public static final int ERR_BUSINESS_MONEY_NOTENOUGH	= -1102;//凤智币不足
		public static final int ERR_BUSINESS_GB_NOTENOUGH		= -1103;//金币不足
		public static final int ERR_BUSINESS_ALREADY_BUY 		= -1104; //物品已经拥有

		//聊天 - 1301 ~ - 1400
		public static final int ERR_CHAT_SEATID_INVALID		 = -1301;//座位ID非法	

		//获取Player信息 -1401 ~ -1500
		public static final int ERR_PLAYINFO_SEATID_INVALED  = -1401;//座位ID非法 
		public static final int ERR_PLAYINFO_PLAYER_NULL	= -1402;//座位上的Player为空

		//清除醉酒度   -1501 ~ -1600
		public static final int ERR_CLEAN_DRUNK_GB_NOTENOUGH	= -1501;//金币不足

		//买酒	-1601 ~ -1700
		public static final int ERR_BUY_WINE_GB_NOTENOUGH	 = -1601;//金币不足

		//更新 -1701 ~ -1800
		public static final int ERR_UPDATE_NULL				=  -1701;		//没有更新

		//排行榜 -1801 ~ 1900
		public static final int ERR_RANK_TYPE_INVALED		= -1801;		//排行榜类型非法
		public static final int ERR_RANK_BUSY				= -1802;		//访问排行榜过于频繁

		//追踪	-1901 ~ -2000
		public static final int ERR_TRACE_PLAYER_NULL		= -1901;		//找不到玩家
		public static final int ERR_TRACE_PLAYER_IDLE		= -1902;		//玩家不在游戏中

		//好友 -2001 ~ -2100
		public static final int ERR_FRIEND_ADD_SELF			= -2001;		//不能加自己为好友
		public static final int ERR_FRIEND_UID_INVALED		= -2002;		//此用户不存在
		public static final int ERR_FRIEND_ALREADY			= -2003;		//已经是好友
		public static final int ERR_FRIEND_OTHERSIDE_LIMIT  = -2004;		//对方好友数已达到上限
		public static final int ERR_FRIEND_MYSIDE_LIMIT		= -2005;		//自己的好友数已经达到上限
		public static final int ERR_FRIEND_VISITOR			= -2006;		//不能添加游客为好友


		//顶 -2101 ~ -2200
		public static final int ERR_DING_SELF				= -2101;		//不能顶自己
		public static final int ERR_DING_LIMIT				= -2102;		//已经达到上限
		public static final int ERR_DING_ALREADY_DONE		= -2103;		//已经顶过
		public static final int ERR_DING_NOT_FRIEND			= -2014;		//没有成为好友
		public static final int ERR_DING_PLAYER_NULL		= -2015;		//没有选择好友

		//下载文件 -2201 ~ -2300
		public static final int ERR_DOWNLOAD_NOT_FOUND		= -2201;		//找不到文件

		//系统公告 -2301 ~ -2400
		public static final int ERR_BULLETIN_NULL			= -2301;		//没有系统公告

		//上传图片 -2401 ~ -2500
		public static final int ERR_UPLOAD_PIC_HOLD			= -2401;		//已经有图片	
		
		//使用道具  -2601 ~ -2700
		public static final int ERR_USE_ITEM_NULL			= -2601;		//没有这个道具
		public static final int ERR_USE_ITEM_REFUSE			= -2602;		//不允许使用道具

		//活动
		public static final int ERR_ACTIVITY_LIST_END		= -2701;		//活动列表结束
		public static final int ERR_ACTIVITY_INVALED		= -2702;		//活动ID无效
		public static final int ERR_ACTIVITY_STOP_SIGNUP	= -2703;		//已经停止报名
		public static final int ERR_ACTIVITY_ALREADY_SIGNUP = -2704;		//已经报名
		public static final int ERR_ACTIVITY_NOT_START		= -2705;		//活动还没有开始
		public static final int ERR_ACTIVITY_ALREADY_END	= -2706;		//活动已经结束
		public static final int ERR_ACTIVITY_TYPE_INVALED   = -2707;		//活动类型无效
		public static final int ERR_ACTIVITY_NO_PLACES		= -2708;		//已经没有报名名额
		public static final int ERR_ACTIVITY_FB_NOT_ENOUGH  = -2709;		//报名凤智币不足
		public static final int ERR_ACTIVITY_GB_NOT_ENOUGH	= -2710;		//报名金币不足
		public static final int ERR_ACTIVITY_LEVEL_NOT_ENOUGH = -2711;		//报名级数不足
		public static final int ERR_ACTIVITY_NOT_SIGNUP		= -2712;		//没有报名
	}
	
	public class SocketErrorType implements ProtocolType {
		public static final int Error_DisConnection = -10000;
		public static final int Error_TimeOut = -10001;
		public static final int Error_Closefailed = -10002;
		public static final int Error_NetWork_Enable = -10003;
		public static final int Error_DecodeError = -10004;
	}
	
	
	public class HTTPERROR implements ProtocolType {
		public static final int MSG_HTTP_ERROR = -20000;		//http错误
		public static final int MSG_PARSE_HTTP_ERRO = -20001;	//http异常
		public static final int MSG_GET_IMAGE = -20002;			//获取图片
		public static final int MSG_GET_GOODSICON = -20003;			//获取商城图片
		public static final int MSG_GET_PAK = -20004;			//获取pak
		public static final int MSG_POST_FILE = -20005;			//上传文件
	}
	
	
	public class IM implements ProtocolType{
		public static final int IM_REQUEST = 1;
		public static final int IM_RESPONSE = 2;
		public static final int IM_NOTIFY = 3;
	}
	
	public class ReqMsg implements ProtocolType {//请求
		public static final int MSG_REQ_REGISTER			=	1000;//注册
		public static final int MSG_REQ_LOGIN				=	1002;//登录
		public static final int MSG_REQ_CREATEROLE			=	1004;//创建角色
		public static final int MSG_REQ_LOGINROOM			= 	1006;//登录房间请求
		public static final int MSG_REQ_READY				=   1009;//准备请求
		public static final int MSG_REQ_SHOUT				=   1013;//喊点请求
		public static final int MSG_REQ_CHOP				=   1015;//劈请求
		public static final int MSG_REQ_LEAVEROOM			=   1018;//离开房间请求
		public static final int MSG_REQ_GOODSLIST			=   1023;//商品列表请求
		public static final int MSG_REQ_BUSINESS			=   1025;//商品交易请求
		public static final int MSG_REQ_VIPINFO				=   1027;//VIP信息请求
		public static final int MSG_REQ_MYGOODS				=   1029;//我的商品信息请求
		public static final int MSG_REQ_BUSINESSDETAIL		=   1031;//交易明细请求
		public static final int MSG_REQ_CHAT				=   1034;//聊天请求
		public static final int MSG_REQ_PLAYINFO			=   1036;//player信息请求
		public static final int MSG_REQ_CLEAN_DRUNK			=   1038;//解酒请求
		public static final int MSG_REQ_MAINPAGE			=   1040;//主页信息请求
		public static final int MSG_REQ_FEEDBACK			=   1042;//反馈请求
		public static final int MSG_REQ_BUY_WINE			=   1044;//买酒请求
		public static final int MSG_REQ_UPDATE				=   1046;//更新请求
		public static final int MSG_REQ_HART 				=   1048;//心跳请求
		public static final int MSG_REQ_VISITORLOGIN		=   1050;//游客登录请求
		public static final int MSG_REQ_RANK				=   1052;//排行榜请求
		public static final int MSG_REQ_TRACE				=   1054;//追踪请求
		public static final int MSG_REQ_PLAYINFO_TWO		=   1056;//player信息请求
		public static final int MSG_REQ_ADD_FRIEND			=	1058;//加好友请求
		public static final int MSG_REQ_FRIEND_LIST			=	1060;//好友列表请求
		public static final int MSG_REQ_DING 				=	1062; //顶请求
		public static final int MSG_REQ_UPLOAD_PIC		    =	1064;//上传图片请求
		public static final int MSG_REQ_DOWNLOAD_PIC		=	1066;//下载图片请求
		public static final int MSG_REQ_BULLETIN			=	1068;//系统公告请求
		public static final int MSG_REQ_UPLOAD_SHARETEXT 	=	1070;//上传分享文本请求
		public static final int MSG_REQ_DEL_PIC			 	=   1072;//删除图片请求
		public static final int MSG_REQ_ROOM_LIST			=   1074;//房间列表请求
		public static final int MSG_REQ_KICK				=   1076;//踢人请求
		public static final int MSG_REQ_PLAYINFO_THR 		= 	1079;//player信息
		public static final int MSG_REQ_SHAKE_FRIEND 		=	1081;//摇醒好友请求
		public static final int MSG_REQ_ACTIVITY_LIST     	= 	1086;//活动列表请求
		public static final int MSG_REQ_ACTIVITY_DETAIL 	=	1088; //活动详情请求
		public static final int	MSG_REQ_ACTIVITY_ENROLL 	=	1090; //活动报名请求
		public static final int	MSG_REQ_ACTIVITY_RANK		=	1093;//活动排名请求
		public static final int	MSG_REQ_USE_ITEM 			=	1083; //使用道具请求
		public static final int	MSG_REQ_THIRD_LOGIN   		=	1103;//第三方用户登录请求
		public static final int	MSG_REQ_GIFT_LIST			=	1110;//礼品列表请求
		public static final int	MSG_REQ_CHANGE_DICE_TYPE    =	1115;//改变骰子类型请求
		public static final int	MSG_REQ_GIFT_FRIEND_GOODS	=	1117;//赠送好友物品的请求
		public static final int	MSG_REQ_INVITE_FRIEND	 	=	1119;//邀请好友请求
		public static final int	MSG_REQ_FRIEND_EVENT_SELECT 	=	1222;//好友事件选择请求
		public static final int	MSG_REQ_SEND_FACE			=	1225;//发送表情请求
		public static final int	MSG_REQ_ROOM_LIST_B			=	1227;//房间列表请求
		public static final int	MSG_REQ_LEAVE_MSG	 		=	1106;//留言请求
		public static final int	MSG_REQ_LEAVE_MSG_LIST	 	=	1108;//留言列表请求
		public static final int	MSG_REQ_ACCOUNT				=   1230;//获取帐号请求
		public static final int	MSG_REQ_GOODSLIST_GB		=	1234;//商品列表请求
		public static final int	MSG_REQ_MAINPAGE_B			=   1239;//主页信息请求
		public static final int	MSG_REQ_DEL_LEAVE_MSG		=	1242;//删除留言请求
		public static final int	MSG_REQ_LEAVE_MSG_B			=	1244;//留言请求
		public static final int	MSG_REQ_LEAVE_MSG_LIST_B	=	1246;//留言列表请求
		public static final int	MSG_REQ_AGREE_FRIEND		= 	1248;//同意加好友请求
		public static final int	MSG_REQ_SEARCH_PEOPLE		=	1250;//寻找玩家请求
		public static final int	MSG_REQ_ROOM_LIST_C			=   1252;//房间列表请求C
		public static final int	MSG_REQ_LOGIN_VIPROOM		=	1254;//登录VIP房间请求
		public static final int	MSG_REQ_LOGINROOM_B			=   1255;//登录房间请求
		public static final int	MSG_REQ_SET_PWD_QUESTION    =   1300;//设置密码提示问题请求
		public static final int MSG_REQ_RESET_PWD			=   1302;//重设置密码请求
		public static final int MSG_REQ_REGISTER_B			=	1259;//注册请求B
		public static final int MSG_REQ_LIGHTNING			=	1261;//雷击请求
		public static final int MSG_REQ_UNITE				=	1264;//同盟请求
		public static final int MSG_REQ_LOGINROOM_UNITE		=	1269;//登录房间请求
		public static final int MSG_REQ_UID					=   1271;//UID请求
		public static final int MSG_REQ_RESET_THIRD_USERINFO	=   1305;//重置第三方帐号信息请求
		public static final int MSG_REQ_VIP_UPGRADE			=	1274;//VIP升级加速请求
		public static final int MSG_REQ_MISSION_LIST		=   1276;//任务列表请求
		public static final int MSG_REQ_AWARD				=	1278;//领奖请求
		public static final int	MSG_REQ_COMMIT_PLAYER_DATA  = 	1113;//上传第三方玩家数据请求
		public static final int	MSG_REQ_PLAYINFO_FOUR		=	1281;//player信息请求
		public static final int	MSG_REQ_HOME_GIFT_LIST		=   1284;//获取家园礼品列表请求
		public static final int	MSG_REQ_GIFT_FRIEND_GOODS_TWO	= 1287;//赠送好友物品的请求
		public static final int	MSG_REQ_HOME_RECEIVE_GIFT_LIST  = 1289;//获取家园收到的礼品列表请求
		public static final int	MSG_REQ_HOME_TAKE_GIFT		=   1291;//捡家园礼品的请求
		public static final int	MSG_REQ_CREATE_BAR			=	1293;//创建酒吧的请求
		public static final int	MSG_REQ_BAR_INFO			=   1295;//酒吧详情的请求
		public static final int	MSG_REQ_BAR_LIST			=	1297;//酒吧列表的请求
		public static final int	MSG_REQ_BAR_OPERATING		=	1299;//操作酒吧的请求
		public static final int	MSG_REQ_AGREE_CANDIDATES	=	1401;//同意应聘的请求
	}
	
	public class RspMsg implements ProtocolType {//应答
		public static final int MSG_RSP_REGISTER	 	= 	1001;//注册应答
		public static final int MSG_RSP_LOGIN		 	= 	1003;//登陆应答
		public static final int MSG_RSP_CREATEROLE	 	= 	1005;//创建角色应答
		public static final int MSG_RSP_LOGINROOM		=	1007;//登录房间应答
		public static final int MSG_RSP_LEAVEROOM		=	1019;//离开房间应答
		public static final int MSG_RSP_GOODSLIST		=	1024;//商品列表应答
		public static final int MSG_RSP_BUSINESS		=	1026;//商品交易应答
		public static final int MSG_RSP_VIPINFO			=	1028;//VIP信息应答
		public static final int MSG_RSP_MYGOODS			=	1030;//我的商品信息应答
		public static final int MSG_RSP_BUSINESSDETAIL	=	1032;//交易明细应答
		public static final int MSG_RSP_PLAYINFO		=	1037;//player信息应答
		public static final int MSG_RSP_MAINPAGE		=	1041;//主页信息应答
		public static final int MSG_RSP_FEEDBACK		=	1043;//反馈应答
		public static final int MSG_RSP_BUY_WINE		=	1045;//买酒应答
		public static final int MSG_RSP_UPDATE			=	1047;//更新应答
		public static final int MSG_RSP_HART 			=	1049;//心跳应答
		public static final int MSG_RSP_VISITORLOGIN	=	1051;//游客登录应答
		public static final int MSG_RSP_RANK			=	1053;//排行榜应答
		public static final int MSG_RSP_TRACE			=	1055;//追踪应答
		public static final int MSG_RSP_PLAYINFO_TWO	=	1057;//player信息应答
		public static final int MSG_RSP_ADD_FRIEND		=	1059;//加好友应答
		public static final int MSG_RSP_FRIEND_LIST		=	1061;//好友列表应答
		public static final int MSG_RSP_DING 			=   1063; //顶应答
		public static final int MSG_RSP_UPLOAD_PIC		=	1065;//上传图片应答
		public static final int MSG_RSP_DOWNLOAD_PIC	=	1067;//下载图片应答
		public static final int MSG_RSP_BULLETIN		=	1069;//系统公告应答
		public static final int MSG_RSP_UPLOAD_SHARETEXT =	1071;//上传分享文本应答
		public static final int MSG_RSP_DEL_PIC			=	1073;//删除图片应答
		public static final int MSG_RSP_ROOM_LIST		= 	1075;//房间列表应答
		public static final int MSG_RSP_KICK			=	1077;//踢人应答
		public static final int MSG_RSP_PLAYINFO_THR 	=	1080; //player信息应答
		public static final int MSG_RSP_SHAKE_FRIEND 	=	1082; //摇醒好友应答 
		public static final int MSG_RSP_ACTIVITY_LIST 	= 	1087; //活动列表应答
		public static final int MSG_RSP_ACTIVITY_DETAIL =	1089; //活动详情应答
		public static final int MSG_RSP_ACTIVITY_ENROLL =	1091; //活动报名应答
		public static final int MSG_RSP_ACTIVITY_RANK	=	1094;//活动排名应答
		public static final int MSG_RSP_USE_ITEM 		=	1084; //使用道具应答 
		public static final int MSG_RSP_GIFT_LIST		=	1111;//礼品列表应答
		public static final int	MSG_RSP_CHANGE_DICE_TYPE    =   1116;//改变骰子类型应答
		public static final int	MSG_RSP_GIFT_FRIEND_GOODS	= 	1118;//赠送好友物品的应答
		public static final int	MSG_RSP_INVITE_FRIEND	 	=	1120;//邀请好友应答
		public static final int	MSG_RSP_FRIEND_EVENT_SELECT = 	1223;//好友事件选择应答
		public static final int MSG_RSP_ROOM_LIST_B			=   1228;//房间列表应答
		public static final int MSG_RSP_LEAVE_MSG	 		=	1107;//留言应答
		public static final int MSG_RSP_LEAVE_MSG_LIST	 	=	1109;//留言列表应答
		public static final int MSG_RSP_ACCOUNT				=	1231;//获取帐号应答
		public static final int MSG_RSP_GOODSLIST_GB		=	1235;//商品列表应答
		public static final int MSG_RSP_MAINPAGE_B			=   1240;//主页信息应答
		public static final int MSG_RSP_DEL_LEAVE_MSG		=	1243;//删除留言应答
		public static final int MSG_RSP_LEAVE_MSG_B			=	1245;//留言应答
		public static final int MSG_RSP_LEAVE_MSG_LIST_B	=	1247;//留言列表应答
		public static final int MSG_RSP_AGREE_FRIEND		=	1249;//同意加好友应答
		public static final int MSG_RSP_SEARCH_PEOPLE		=	1251;//寻找好友应答
		public static final int MSG_RSP_ROOM_LIST_C			=   1253;//房间列表应答C
		public static final int	MSG_RSP_LOGINROOM_B			=	1256;//登录房间应答
		public static final int	MSG_RSP_LOGIN_VIPROOM		=	1258;//登录VIP房间应答
		public static final int MSG_RSP_SET_PWD_QUESTION 	= 	1301;//设置密码提示问题应答
		public static final int MSG_RSP_RESET_PWD			=	1303;//重设置密码应答
		public static final int	MSG_RSP_REGISTER_B			=	1260;//注册应答B
		public static final int	MSG_RSP_UNITE				=	1265;//同盟应答
		public static final int	MSG_RSP_LOGINROOM_UNITE		=	1270;//登录房间应答
		public static final int	MSG_RSP_UID					=   1272;//UID应答
		public static final int	MSG_RSP_RESET_THIRD_USERINFO	=	1306;//重置第三方帐号信息响应
		public static final int MSG_RSP_THIRD_LOGIN			=	1273;//第三方用户登录应答
		public static final int	MSG_RSP_VIP_UPGRADE			=	1275;		//VIP升级加速应答
		public static final int	MSG_RSP_MISSION_LIST		=	1277;//任务列表应答
		public static final int	MSG_RSP_AWARD				=	1279;//领奖应答
		public static final int MSG_RSP_COMMIT_PLAYER_DATA =	1114; //上传第三方玩家数据应答
		public static final int	MSG_RSP_PLAYINFO_FOUR		=   1282;//player信息应答
		public static final int	MSG_RSP_HOME_GIFT_LIST		=   1285;//获取家园礼品列表应答
		public static final int	MSG_RSP_GIFT_FRIEND_GOODS_TWO	= 1288;//赠送好友物品的应答
		public static final int	MSG_RSP_HOME_RECEIVE_GIFT_LIST  = 1290;//获取家园收到的礼品列表应答
		public static final int	MSG_RSP_HOME_TAKE_GIFT		=   1292;//捡家园礼品的应答
		public static final int	MSG_RSP_CREATE_BAR			=	1294;//创建酒吧的应答
		public static final int	MSG_RSP_BAR_INFO			=	1296;//酒吧详情的应答
		public static final int	MSG_RSP_BAR_LIST			=	1298;//酒吧列表的应答
		public static final int	MSG_RSP_BAR_OPERATING		=	1400;//操作酒吧的应答
		public static final int	MSG_RSP_AGREE_CANDIDATES    = 	1402;//同意应聘的应答
	}
	
	public class NotifyMsg implements ProtocolType {//通知
		public static final int  MSG_NOTIFY_LOGINROOM	=	1008;//登录房间消息
		public static final int  MSG_NOTIFY_READY		=	1010;//准备消息
		public static final int  MSG_NOTIFY_SHOW_READY	=	1021;//显示准备按钮的消息
		public static final int  MSG_NOTIFY_START		=	1012;//开始消息
		public static final int  MSG_NOTIFY_SHOUT		=	1014;//喊点消息
		public static final int  MSG_NOTIFY_CHOP		=	1016;//劈消息
		public static final int  MSG_NOTIFY_RESULT		=	1017;//结果消息
		public static final int  MSG_NOTIFY_LEAVEROOM	=	1020;//离开房间消息
		public static final int  MSG_NOTIFY_KICK		=	1022;//踢人消息
		public static final int  MSG_NOTIFY_RECHARGE	=	1033;//充值消息
		public static final int  MSG_NOTIFY_CHAT		=	1035;//聊天消息
		public static final int  MSG_NOTIFY_CLEAN_DRUNK	=	1039;//解酒应答
		public static final int  MSG_NOTIFY_SYSMSG		=	1078;//系统消息
		public static final int  MSG_NOTIFY_ACTIVITY_START	= 1092;//活动开始消息
		public static final int  MSG_NOTIFY_ACTIVITY_RANKING = 1095;//活动排名消息
		public static final int  MSG_NOTIFY_SHOW_ITEM		= 1096;//显示道具按钮消息
		public static final int  MSG_NOTIFY_VIE_CHOP		= 1085;//使用抢劈道具通知
		public static final int  MSG_NOTIFY_CHANGE_DICE		= 1097;//使用千王之王道具的消息
		public static final int  MSG_NOTIFY_SHIELD			= 1098;//使用催眠怀表的消息
		public static final int  MSG_NOTIFY_TRIGGER_SHIELD	= 1099;//触发催眠怀表的消息
		public static final int  MSG_NOTIFY_KILL_ORDER		= 1100;//使用追杀令的消息
		public static final int  MSG_NOTIFY_MANY_CHOP		= 1101;//使用连劈闪电的消息
		public static final int  MSG_NOTIFY_UNARMED			= 1102;//使用禁劈道具的消息
		public static final int  MSG_NOTIFY_DING			= 1104;//顶好友的消息
		public static final int  MSG_NOTIFY_LEVELUP			= 1105;//升级的消息
		public static final int  MSG_NOTIFY_GET_GIFT		= 1112;//获得礼品的消息
		public static final int  MSG_NOTIFY_FRIEND_EVENT    = 1221;//好友事件通知
		public static final int  MSG_NOTIFY_PLAY_ANIMATION	= 1224;//播放动画的通知
		public static final int  MSG_NOTIFY_FACE			= 1226;//发送表情通知
		public static final int  MSG_NOTIFY_REBEL_CHOP		= 1229;//使用反劈道具通知
		public static final int  MSG_NOTIFY_LEARN_SKILL		= 1236;//学会技能通知
		public static final int  MSG_NOTIFY_SHAKE_FRIEND	= 1237;//醒酒通知
		public static final int  MSG_NOTIFY_ACTIVITY_INFO	= 1238;//活动信息
		public static final int  MSG_NOTIFY_LEAVEMSG		= 1241;//留言通知
		public static final int  MSG_NOTIFY_LOGINROOM_B		= 1257;//登录房间消息
		public static final int  MSG_NOTIFY_NO_PWD_QUESTION	= 1304;//通知客户端他还没设置密码提示问题，如果未设置密码提示问题，此消息登录成功应答后会下发
		public static final int  MSG_NOTIFY_LIGHTNING		= 1262;//雷击消息
		public static final int  MSG_NOTIFY_VIE_CHOP_B		= 1263;//使用抢劈道具通知B
		public static final int  MSG_NOTIFY_UNITE			= 1266;//同(解)盟通知
		public static final int  MSG_NOTIFY_RESULT_UNITE	= 1267;//同盟房结果消息
		public static final int  MSG_NOTIFY_START_UNITE		= 1268;//开始消息UNITE
		public static final int  MSG_NOTIFY_CHANGE_STATUS  = 1280;//改变状态通知
		public static final int  MSG_NOTIFY_MISSION_REWARD = 1283;//获得任务奖励的通知
		public static final int  MSG_NOTIFY_PLAY_ANIMATION_TWO	= 1286;//播放动画的通知
	}
}
