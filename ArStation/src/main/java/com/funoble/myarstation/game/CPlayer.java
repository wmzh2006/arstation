package com.funoble.myarstation.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.R.integer;

import com.funoble.myarstation.data.cach.FriendListInfo;
import com.funoble.myarstation.data.cach.RankData;
import com.funoble.myarstation.screen.RankScreen;
import com.funoble.myarstation.socket.protocol.MBRspFriendList;
import com.funoble.myarstation.socket.protocol.MBRspRank;
import com.funoble.myarstation.store.data.RoomDetailData;
import com.funoble.myarstation.view.GameView;

public class CPlayer {
	//技能
	public static final int E_TSI_ZHAI = 1;//直接喊斋
	public static final int E_TSI_LIGHTNING = 2;//雷击
	
	public static final int MAX_FRIEND_COUNT = 200;		//最多的好友个数
	public static final int MAX_ITEM_TYPE_COUNT = 6;	//实时道具最多6种
	
	//实时道具的ID
	public static final int E_RTI_VIE_CHOP = 0;			//抢劈道具
	public static final int E_RTI_CHANGE_DICE = 1;		//千王之王道具
	public static final int E_RIT_SHIELD = 2;			//反弹盾
	public static final int E_RIT_KILL_ORDER = 3;		//追杀令道具
	public static final int R_RTI_MANY_CHOP = 4;		//闪电劈
	public static final int E_RIT_UNARMED = 5;			//禁劈道具
	public static final int E_RIT_REBEL_CHOP = 6;		//反劈道具
	public static final int E_RIT_BOTTLE = 7;			//啤酒瓶
	public static final int E_RIT_CAKE = 8;				//蛋糕
	
	public boolean ibInit = false;
	public boolean ibVisitor = false;
	
	public int 		iUserID = 0;				//全球唯一ID
	public String	stUserNick;					//昵称
	public int      iMoney = 0;					//凤币
	public int		iGb = 0;					//游戏币
	public int 		iVipLevel = 0;				//Vip级别	
	public int		iVipTime = 0;				//Vip到期时间	
	public int		iGameLevel = 1;				//级别
	public int		iGameExp = 0;				//当前级别的经验
	public int		iCurrentDrunk = 0;			//当前醉酒度
	public int		iDingCount = 0;				//魅力值
	public int		iReward = 0;				//登录奖励
	public int		iBeKillsCount = 0;			//被灌倒多少次
	public int		iModelID = 0;				//角色模型ID（为-1时，表示没选择人物）
	public int		iSex = 0;					//性别（为-1时，表示没选择人物）
	public int		iWinning = 0;				//胜率
	public int 		iNextExp = 0;				//升级经验
	public String	iTitle;						//职位称号
	public int		iGoodsCount = 0;			//物品个数
	public int		iWinCount = 0;				//胜利次数
	public int		iFailCount = 0;				//失败次数
	public int 		iMaxHit = 0; 				//最大连击数
	public int		iWealthRank = 0;			//财富排行榜
	public int 		iLevelRank = 0; 			//等级排行榜
	public int 		iVictoryRank = 0; 			//胜场排行榜
	
	public int		iProcessGoodsTime = 0;		//运行道具的时间
	public String	szLittlePicName;			//头像图片名
	public String   szBigPicName;				//大图片
	public int		iVIPExp = 0;				//VIP的经验
	public short    nAlreadyRemunerate = 0;		//是否已经补偿
	public int		iShakeTime = 0;				//被好友摇的时间
	public int		iAchievement = 0;			//达到的成就(最大连击数)
	public int		iDingPower = 0;				//顶人的点数
	public int 		iShakePower = 0;			//摇人的点数
	public int		iDiceID = 0;				//骰子的ID
	
	public boolean	iHasWineCark = false;		//是否有酒水卡
	public boolean	iHasGiftPak = false;		//是否有礼品包
	
	public int		iMaxBoomCount = 5;			//最大炸弹数
	public int		iCurrentBoomCount = 5;		//当前炸弹个数
	public int		iMaxMP = 100;				//最大魔法值
	public int		iCurrentMP = 0;				//当前魔法值
	
	public int		iItemTypeCount = 0;			//道具有多少种类
	public int[]	iItemIDs;					//道具的ID
	public int[]	iItemCounts;				//道具有多少个
	
//	public int		iFriendCount = 0;			//好友个数	
//	public int[]	iFriendUIDs;				//好友列表
	public int		iIntimate = 0;				//亲密度
	public int		iNextIntimate = 0;			//下一级的亲密度
	public String	iFriendTitle;				//关系称号
	public int		iZhaiSkill = 0;				//直接喊斋技能 0 --- 没学会    1 --- 直接喊斋     2 --- 雷劈
	
	public int 		iCarID = 0;					//车ID
	public int		iHouseID = 0;				//房子ID
	public int 		iPetID = 0;					//宠物ID
	public String 	iHonor = "";				//称号
	public int		iHasActivity = 0;			//今天是否有活动
	public int		iActivityCount = 0;			//可报名活动的个数
	
	//排行榜玩家的个数
	public int 		iRankTotalCount = 0;
	//大厅数据
	public int		iOnLineCount = 0;			//同时在线人数	
	public List<FriendListInfo> iFriendList;	//暂存好友列表
	public MBRspFriendList      iRspFriendList	;//暂存最后一次成功应答数据
	public HashMap<Integer, List<RankData>> iRankList; //暂存排行榜
	public HashMap<Integer, MBRspRank> iRspRank;//暂存最后一次成功应答数据
	//
	public boolean 	ibGetGift = false;			//是否获得礼品
	public String apkUrl = ""; 						//游戏下载地址
	//商城
	public int rmbToatlCount = 0;
	public int rmbStartIndex = 0;
	public int gbToatlCount = 0;
	public int gbStartIndex = 0;
	//主页
	public String iChangeUrl = "";
	public int iLeaveMsgCount = 0;
	public int iMissionRewardCount = 0;
	public String[] urls = new String[3];//格式：礼品页面|每日奖励页面|公告页面
	public String iTips = "";
	public HashMap<Integer, RoomDetailData> iRoomInfo;	//房间数据
	//初始化数据
	public void init() {		
		iItemTypeCount = 0;			//道具有多少种类
		iItemIDs = new int[MAX_ITEM_TYPE_COUNT];					//道具的ID
		iItemCounts = new int[MAX_ITEM_TYPE_COUNT];				//道具有多少个
//		iFriendCount = 0;	//好友个数	
//		iFriendUIDs = new int[MAX_FRIEND_COUNT];		//好友列表
		iFriendList = new ArrayList<FriendListInfo>();
		iRankList = new HashMap<Integer, List<RankData>>(4);
		iRankList.put(0, new ArrayList<RankData>());
		iRankList.put(1, new ArrayList<RankData>());
		iRankList.put(2, new ArrayList<RankData>());
		iRankList.put(3, new ArrayList<RankData>());
		iRspRank = new HashMap<Integer, MBRspRank>(4);
		urls = new String[3];
		iHonor = "";
		iRoomInfo = new HashMap<Integer, RoomDetailData>();
		iTips = "";
	}
	
	public boolean isMyFrient(int aUID) {
		for(int i=0; i<iFriendList.size(); i++) {
			if(iFriendList.get(i).iFriendUserID == aUID) {
				return true;
			}
		}
		return false;
	}
	
	public void reset() {
		ibVisitor = false;
		iFriendList.clear();
		iRspFriendList = null;
		for(int i = 0; i < iRankList.size(); i++) {
			iRankList.get(i).clear();
		}
		for(int i = 0; i < iRspRank.size(); i++) {
			iRspRank.clear();
		}
		ibGetGift = false;
		iLeaveMsgCount = 0;
		iReward = 0;
		iMissionRewardCount = 0;
		urls = null;
		iHonor = "";
		iRoomInfo.clear();
		iTips = "";
	}
	
	public void refreshRankList(int userid, int action) {
		for(int j = 0; j < iRankList.size(); j++) {
			List<RankData>  data0 = iRankList.get(j);
			Iterator<RankData> iterator = data0.iterator();
			while(iterator.hasNext()) {
				RankData data = iterator.next();
				if(data.iRankUserID == userid) {
					if(action == 0) {
						data.iFriend = RankScreen.NEXUS_FRIEND;
					}
					else {
						data.iFriend = RankScreen.NEXUS_STRANGER;
					}
					break;
				}
			}
		}
	}
	
	public void deleteFriend(int aUID) {
		for(int i=0; i<iFriendList.size(); i++) {
			if(iFriendList.get(i).iFriendUserID == aUID) {
				if(iFriendList.get(i).iFriendStatus > 0) {
					iRspFriendList.iFriendOLCount --;
				}
				iRspFriendList.iFriendTotalCount --;
				iFriendList.remove(i);
				break;
			}
		}
	}
	
	public void setFriendDrunk(int aUID, int aDrunk, int aOnLine) {
		for(int i=0; i<iFriendList.size(); i++) {
			if(iFriendList.get(i).iFriendUserID == aUID) {
				iFriendList.get(i).iFriendStatus = aOnLine;
				iFriendList.get(i).iUpdate = aDrunk;
				break;
			}
		}
	}
}
