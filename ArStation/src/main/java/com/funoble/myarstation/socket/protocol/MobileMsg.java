/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: CacheMsgHead.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-15 上午11:42:13
 *******************************************************************************/
package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.NotifyMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.ReqMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;



public class MobileMsg {
	
	private static final int CACHE_MSGHEAD_LEN = 2 + 1 + 4;
	public static final int MAX_CACHE_MSG_LEN = 1024 * 2;
	public int 				iSessionID;	//后台分配的会话ID(第一次连接时,客户端填-1，以后的会话要带服务器返回的id)
	public byte 			iFlag;		//标识是否包含CS协议头数据(连接建立的第一个包此值为1，包含)（1-包括,0-不包括）
	
	public 	CSHead 			m_CSHead;
	public	MobileMsgHead	m_MsgHead;
	public  MsgPara			m_pMsgPara = null;		 
	/**
	 * construct
	 */
	public MobileMsg() {
		iSessionID = -1;
		iFlag = 0;
		m_CSHead = new CSHead();
		m_MsgHead = new MobileMsgHead();
	}
	
	public MsgPara CreateMsgPara(short sMsgID, short sMsgType) {
		switch (sMsgType) {
			case IM.IM_REQUEST:
			{
				switch (sMsgID) {
					case ReqMsg.MSG_REQ_REGISTER:
						return (MsgPara) new MBReqRegister();
					case ReqMsg.MSG_REQ_LOGIN:
						return (MsgPara) new MBReqLogin();
					case ReqMsg.MSG_REQ_CREATEROLE:
						return (MsgPara) new MBReqCreateRole();
					case ReqMsg.MSG_REQ_LOGINROOM:
						return (MsgPara) new MBReqLoginRoom();
					case ReqMsg.MSG_REQ_READY:
						return (MsgPara) new MBReqReady();
					case ReqMsg.MSG_REQ_SHOUT:
						return (MsgPara) new MBReqShout();
					case ReqMsg.MSG_REQ_CHOP:
						return (MsgPara) new MBReqChop();
					case ReqMsg.MSG_REQ_LEAVEROOM:
						return (MsgPara) new MBReqLeaveRoom();
					case ReqMsg.MSG_REQ_GOODSLIST:
						return (MsgPara) new MBReqGoodsList();
					case ReqMsg.MSG_REQ_BUSINESS:
						return (MsgPara) new MBReqBusiness();
					case ReqMsg.MSG_REQ_VIPINFO:
						return (MsgPara) new MBReqVIPinfo();
					case ReqMsg.MSG_REQ_MYGOODS:
						return (MsgPara) new MBReqMyGoods();
					case ReqMsg.MSG_REQ_BUSINESSDETAIL:
						return (MsgPara) new MBReqBusinessDetail();
					case ReqMsg.MSG_REQ_CHAT:
						return (MsgPara) new MBReqChat();
					case ReqMsg.MSG_REQ_PLAYINFO:
						return (MsgPara) new MBReqPlayerInfo();
					case ReqMsg.MSG_REQ_CLEAN_DRUNK:
						return (MsgPara) new MBReqCleanDrunk();
					case ReqMsg.MSG_REQ_MAINPAGE:
						return (MsgPara) new MBReqMainPage();
					case ReqMsg.MSG_REQ_FEEDBACK:
						return (MsgPara) new MBReqFeekBack();
					case ReqMsg.MSG_REQ_BUY_WINE:
						return (MsgPara) new MBReqBuyWine();
					case ReqMsg.MSG_REQ_UPDATE:
						return (MsgPara) new MBReqUpdate();
					case ReqMsg.MSG_REQ_HART:
						return (MsgPara) new MBReqHart();
					case ReqMsg.MSG_REQ_VISITORLOGIN:
						return (MsgPara) new MBReqVisitorLogin();
					case ReqMsg.MSG_REQ_RANK:
						return (MsgPara) new MBReqRank();
					case ReqMsg.MSG_REQ_TRACE:
						return (MsgPara) new MBReqTrace();
					case ReqMsg.MSG_REQ_PLAYINFO_TWO:
						return (MsgPara) new MBReqPlayerInfoTwo();
					case ReqMsg.MSG_REQ_ADD_FRIEND:
						return (MsgPara) new MBReqAddFriend();
					case ReqMsg.MSG_REQ_FRIEND_LIST:
						return (MsgPara) new MBReqFriendList();
					case ReqMsg.MSG_REQ_DING:
						return (MsgPara) new MBReqDing();
					case ReqMsg.MSG_REQ_UPLOAD_PIC:
						return (MsgPara) new MBReqUpLoadPic();
					case ReqMsg.MSG_REQ_UPLOAD_SHARETEXT:
						return (MsgPara) new MBReqUpLoadShareText();
					case ReqMsg.MSG_REQ_DEL_PIC:
						return (MsgPara) new MBReqDelPic();
					case ReqMsg.MSG_REQ_ROOM_LIST:
						return (MsgPara) new MBReqRoomList();
					case ReqMsg.MSG_REQ_DOWNLOAD_PIC:
						return (MsgPara) new MBReqDownLoadPic();
					case ReqMsg.MSG_REQ_KICK:
						return (MsgPara) new MBReqKick();
					case ReqMsg.MSG_REQ_PLAYINFO_THR:
						return (MsgPara) new MBReqPlayerInfoThree();					case ReqMsg.MSG_REQ_SHAKE_FRIEND:
						return (MsgPara) new MBReqShakeFriend();					case ReqMsg.MSG_REQ_ACTIVITY_LIST:
						return (MsgPara) new MBReqActivityList();					case ReqMsg.MSG_REQ_ACTIVITY_ENROLL:
						return (MsgPara) new MBReqActivityEnroll();					case ReqMsg.MSG_REQ_ACTIVITY_DETAIL:
						return (MsgPara) new MBReqActivityDetail();					case ReqMsg.MSG_REQ_ACTIVITY_RANK:
						return (MsgPara) new MBReqActivityRank();					case ReqMsg.MSG_REQ_USE_ITEM:
						return (MsgPara) new MBReqUseItem();					case ReqMsg.MSG_REQ_THIRD_LOGIN:
						return (MsgPara) new MBReqThirdLogin();					case ReqMsg.MSG_REQ_GIFT_LIST:
						return (MsgPara) new MBReqGiftList();					case ReqMsg.MSG_REQ_CHANGE_DICE_TYPE:
						return (MsgPara) new MBReqChangeDiceType();					case ReqMsg.MSG_REQ_GIFT_FRIEND_GOODS:
						return (MsgPara) new MBReqGiftFriendGoods();					case ReqMsg.MSG_REQ_INVITE_FRIEND:
						return (MsgPara) new MBReqInviteFriend();					case ReqMsg.MSG_REQ_FRIEND_EVENT_SELECT:
						return (MsgPara) new MBReqFriendEventSelect();					case ReqMsg.MSG_REQ_SEND_FACE:
						return (MsgPara) new MBReqSendFace();					case ReqMsg.MSG_REQ_ROOM_LIST_B:
						return (MsgPara) new MBReqRoomListB();					case ReqMsg.MSG_REQ_LEAVE_MSG:
						return (MsgPara) new MBReqLeaveMsg();					case ReqMsg.MSG_REQ_LEAVE_MSG_LIST:
						return (MsgPara) new MBReqLeaveMsgList();					case ReqMsg.MSG_REQ_ACCOUNT:
						return (MsgPara) new MBReqAccount();					case ReqMsg.MSG_REQ_GOODSLIST_GB:
						return (MsgPara) new MBReqGoodsListGb();					case ReqMsg.MSG_REQ_MAINPAGE_B:
						return (MsgPara) new MBReqMainPageB();					case ReqMsg.MSG_REQ_DEL_LEAVE_MSG:
						return (MsgPara) new MBReqDelLeaveMsg();					case ReqMsg.MSG_REQ_LEAVE_MSG_B:
						return (MsgPara) new MBReqLeaveMsgB();					case ReqMsg.MSG_REQ_LEAVE_MSG_LIST_B:
						return (MsgPara) new MBReqLeaveMsgListB();					case ReqMsg.MSG_REQ_AGREE_FRIEND:
						return (MsgPara) new MBReqAgreeFriend();					case ReqMsg.MSG_REQ_SEARCH_PEOPLE:
						return (MsgPara) new MBReqSearchPeople();					case ReqMsg.MSG_REQ_ROOM_LIST_C:
						return (MsgPara) new MBReqRoomListC();					case ReqMsg.MSG_REQ_LOGIN_VIPROOM:
						return (MsgPara) new MBReqLoginVipRoom();					case ReqMsg.MSG_REQ_LOGINROOM_B:
						return (MsgPara) new MBReqLoginRoomB();					case ReqMsg.MSG_REQ_SET_PWD_QUESTION:
						return (MsgPara) new MBReqSetPwdQuestion();					case ReqMsg.MSG_REQ_RESET_PWD:
						return (MsgPara) new MBReqResetPwd();					case ReqMsg.MSG_REQ_REGISTER_B:
						return (MsgPara) new MBReqRegisterB();					case ReqMsg.MSG_REQ_LIGHTNING:
						return (MsgPara) new MBReqLightning();					case ReqMsg.MSG_REQ_UNITE:
						return (MsgPara) new MBReqUnite();					case ReqMsg.MSG_REQ_LOGINROOM_UNITE:
						return (MsgPara) new MBReqLoginRoomUnite();					case ReqMsg.MSG_REQ_UID:
						return (MsgPara) new MBReqUID();					case ReqMsg.MSG_REQ_RESET_THIRD_USERINFO:
						return (MsgPara) new MBReqResetThirdUserInfo();
					case ReqMsg.MSG_REQ_VIP_UPGRADE:
						return (MsgPara) new MBReqVipUpgrade();					case ReqMsg.MSG_REQ_MISSION_LIST:
						return (MsgPara) new MBReqMissionList();					case ReqMsg.MSG_REQ_AWARD:
						return (MsgPara) new MBReqAward();					case ReqMsg.MSG_REQ_COMMIT_PLAYER_DATA:
						return (MsgPara) new MBReqCommitPlayerData();					case ReqMsg.MSG_REQ_PLAYINFO_FOUR:
						return (MsgPara) new MBReqPlayerInfoFour();					case ReqMsg.MSG_REQ_HOME_GIFT_LIST:
						return (MsgPara) new MBReqHomeGiftList();					case ReqMsg.MSG_REQ_GIFT_FRIEND_GOODS_TWO:
						return (MsgPara) new MBReqGiftFriendGoodsTwo();					case ReqMsg.MSG_REQ_HOME_RECEIVE_GIFT_LIST:
						return (MsgPara) new MBReqHomeReceiveGiftList();					case ReqMsg.MSG_REQ_HOME_TAKE_GIFT:
						return (MsgPara) new MBReqHomeTakeGift();						default:
						return null;
				}
			}
			case IM.IM_RESPONSE:
			{
				switch (sMsgID) {
					case RspMsg.MSG_RSP_REGISTER:
						return (MsgPara) new MBRspRegister();
					case RspMsg.MSG_RSP_LOGIN:
						return (MsgPara) new MBRspLogin();
					case RspMsg.MSG_RSP_CREATEROLE:
						return (MsgPara) new MBRspCreateRole();
					case RspMsg.MSG_RSP_LOGINROOM:
						return (MsgPara) new MBRspLoginRoom();
					case RspMsg.MSG_RSP_LEAVEROOM:
						return (MsgPara) new MBRspLeaveRoom();
					case RspMsg.MSG_RSP_GOODSLIST:
						return (MsgPara) new MBRspGoodsList();
					case RspMsg.MSG_RSP_BUSINESS:
						return (MsgPara) new MBRspBusiness();
					case RspMsg.MSG_RSP_VIPINFO:
						return (MsgPara) new MBRspVIPinfo();
					case RspMsg.MSG_RSP_MYGOODS:
						return (MsgPara) new MBRspMyGoods();
					case RspMsg.MSG_RSP_BUSINESSDETAIL:
						return (MsgPara) new MBRspBusinessDetail();
					case RspMsg.MSG_RSP_PLAYINFO:
						return (MsgPara) new MBRspPlayerInfo();
					case RspMsg.MSG_RSP_MAINPAGE:
						return (MsgPara) new MBRspMainPage();
					case RspMsg.MSG_RSP_FEEDBACK:
						return (MsgPara) new MBRspFeekBack();
					case RspMsg.MSG_RSP_BUY_WINE:
						return (MsgPara) new MBRspBuyWine();
					case RspMsg.MSG_RSP_UPDATE:
						return (MsgPara) new MBRspUpdate();
					case RspMsg.MSG_RSP_HART:
						return (MsgPara) new MBRspHart();
					case RspMsg.MSG_RSP_VISITORLOGIN:
						return (MsgPara) new MBRspVisitorLogin();
					case RspMsg.MSG_RSP_RANK:
						return (MsgPara) new MBRspRank();
					case RspMsg.MSG_RSP_TRACE:
						return (MsgPara) new MBRspTrace();
					case RspMsg.MSG_RSP_PLAYINFO_TWO:
						return (MsgPara) new MBRspPlayerInfoTwo();
					case RspMsg.MSG_RSP_ADD_FRIEND:
						return (MsgPara) new MBRspAddFriend();
					case RspMsg.MSG_RSP_FRIEND_LIST:
						return (MsgPara) new MBRspFriendList();
					case RspMsg.MSG_RSP_DING:
						return (MsgPara) new MBRspDing();
					case RspMsg.MSG_RSP_UPLOAD_PIC:
						return (MsgPara) new MBRspUpLoadPic();
					case RspMsg.MSG_RSP_UPLOAD_SHARETEXT:
						return (MsgPara) new MBRspUpLoadShareText();
					case RspMsg.MSG_RSP_DEL_PIC:
						return (MsgPara) new MBRspDelPic();
					case RspMsg.MSG_RSP_ROOM_LIST:
						return (MsgPara) new MBRspRoomList();
					case RspMsg.MSG_RSP_DOWNLOAD_PIC:
						return (MsgPara) new MBRspDownLoadPic();
					case RspMsg.MSG_RSP_KICK:
						return (MsgPara) new MBRspKick();
					case RspMsg.MSG_RSP_PLAYINFO_THR:
						return (MsgPara) new MBRspPlayerInfoThree();
					case RspMsg.MSG_RSP_SHAKE_FRIEND:
						return (MsgPara) new MBRspShakaFriend();
					case RspMsg.MSG_RSP_ACTIVITY_LIST:
						return (MsgPara) new MBRspActivityList();
					case RspMsg.MSG_RSP_ACTIVITY_ENROLL:
						return (MsgPara) new MBRspActivityEnroll();
					case RspMsg.MSG_RSP_ACTIVITY_DETAIL:
						return (MsgPara) new MBRspActivityDetail();
					case RspMsg.MSG_RSP_USE_ITEM:
						return (MsgPara) new MBRspUseItem();
					case RspMsg.MSG_RSP_GIFT_LIST:
						return (MsgPara) new MBRspGiftList();
					case RspMsg.MSG_RSP_ACTIVITY_RANK:
						return (MsgPara) new MBRspActivityRank();
					case RspMsg.MSG_RSP_CHANGE_DICE_TYPE:
						return (MsgPara) new MBRspChangeDiceType();
					case RspMsg.MSG_RSP_GIFT_FRIEND_GOODS:
						return (MsgPara) new MBRspGiftFriendGoods();
					case RspMsg.MSG_RSP_INVITE_FRIEND:
						return (MsgPara) new MBRspInviteFriend();
					case RspMsg.MSG_RSP_FRIEND_EVENT_SELECT:
						return (MsgPara) new MBRspFriendEventSelect();
					case RspMsg.MSG_RSP_ROOM_LIST_B:
						return (MsgPara) new MBRspRoomListB();
					case RspMsg.MSG_RSP_LEAVE_MSG:
						return (MsgPara) new MBRspLeaveMsg();
					case RspMsg.MSG_RSP_LEAVE_MSG_LIST:
						return (MsgPara) new MBRspLeaveMsgList();
					case RspMsg.MSG_RSP_ACCOUNT:
						return (MsgPara) new MBRspAccount();
					case RspMsg.MSG_RSP_GOODSLIST_GB:
						return (MsgPara) new MBRspGoodsListGb();
					case RspMsg.MSG_RSP_MAINPAGE_B:
						return (MsgPara) new MBRspMainPageB();
					case RspMsg.MSG_RSP_DEL_LEAVE_MSG:
						return (MsgPara) new MBRspDelLeaveMsg();
					case RspMsg.MSG_RSP_LEAVE_MSG_B:
						return (MsgPara) new MBRspLeaveMsgB();
					case RspMsg.MSG_RSP_LEAVE_MSG_LIST_B:
						return (MsgPara) new MBRspLeaveMsgListB();
					case RspMsg.MSG_RSP_AGREE_FRIEND:
						return (MsgPara) new MBRspAgreeFriend();
					case RspMsg.MSG_RSP_SEARCH_PEOPLE:
						return (MsgPara) new MBRspSearchPeople();
					case RspMsg.MSG_RSP_ROOM_LIST_C:
						return (MsgPara) new MBRspRoomListC();
					case RspMsg.MSG_RSP_LOGINROOM_B:
						return (MsgPara) new MBRspLoginRoomB();
					case RspMsg.MSG_RSP_LOGIN_VIPROOM:
						return (MsgPara) new MBRspLoginVipRoom();
					case RspMsg.MSG_RSP_SET_PWD_QUESTION:
						return (MsgPara) new MBRspSetPwdQuestion();
					case RspMsg.MSG_RSP_RESET_PWD:
						return (MsgPara) new MBRspResetPwd();
					case RspMsg.MSG_RSP_REGISTER_B:
						return (MsgPara) new MBRspRegisterB();
					case RspMsg.MSG_RSP_UNITE:
						return (MsgPara) new MBRspUnite();
					case RspMsg.MSG_RSP_LOGINROOM_UNITE:
						return (MsgPara) new MBRspLoginRoomUnite();
					case RspMsg.MSG_RSP_UID:
						return (MsgPara) new MBRspUID();
					case RspMsg.MSG_RSP_RESET_THIRD_USERINFO:
						return (MsgPara) new MBRspResetThirdUserInfo();
					case RspMsg.MSG_RSP_THIRD_LOGIN:
						return (MsgPara) new MBRspThirdLogin();
					case RspMsg.MSG_RSP_VIP_UPGRADE:
						return (MsgPara) new MBRspVipUpgrade();
					case RspMsg.MSG_RSP_MISSION_LIST:
						return (MsgPara) new MBRspMissionList();
					case RspMsg.MSG_RSP_AWARD:
						return (MsgPara) new MBRspAward();
					case RspMsg.MSG_RSP_COMMIT_PLAYER_DATA:
						return (MsgPara) new MBRspCommitPlayerData();
					case RspMsg.MSG_RSP_PLAYINFO_FOUR:
						return (MsgPara) new MBRspPlayerInfoFour();
					case RspMsg.MSG_RSP_HOME_GIFT_LIST:
						return (MsgPara) new MBRspHomeGiftList();
					case RspMsg.MSG_RSP_GIFT_FRIEND_GOODS_TWO:
						return (MsgPara) new MBRspGiftFriendGoodsTwo();
					case RspMsg.MSG_RSP_HOME_RECEIVE_GIFT_LIST:
						return (MsgPara) new MBRspHomeReceiveGiftList();
					case RspMsg.MSG_RSP_HOME_TAKE_GIFT:
						return (MsgPara) new MBRspHomeTakeGift();
						default:
						return null;
				}
			}
			case IM.IM_NOTIFY:
			{
				switch (sMsgID) {
					case NotifyMsg.MSG_NOTIFY_LOGINROOM:
						return (MsgPara)new MBNotifyLoginRoom();
					case NotifyMsg.MSG_NOTIFY_READY:
						return (MsgPara)new MBNotifyReady();
					case NotifyMsg.MSG_NOTIFY_SHOW_READY:
						return (MsgPara)new MBNotifyShowReady();
					case NotifyMsg.MSG_NOTIFY_START:
						return (MsgPara)new MBNotifyStart();
					case NotifyMsg.MSG_NOTIFY_SHOUT:
						return (MsgPara)new MBNotifyShout();
					case NotifyMsg.MSG_NOTIFY_CHOP:
						return (MsgPara)new MBNotifyChop();
					case NotifyMsg.MSG_NOTIFY_RESULT:
						return (MsgPara)new MBNotifyResult();
					case NotifyMsg.MSG_NOTIFY_LEAVEROOM:
						return (MsgPara)new MBNotifyLeaveRoom();
					case NotifyMsg.MSG_NOTIFY_KICK:
						return (MsgPara)new MBNotifyKick();
					case NotifyMsg.MSG_NOTIFY_RECHARGE:
						return (MsgPara)new MBNotifyRecharge();
					case NotifyMsg.MSG_NOTIFY_CHAT:
						return (MsgPara)new MBNotifyChat();
					case NotifyMsg.MSG_NOTIFY_CLEAN_DRUNK:
						return (MsgPara) new MBNotifyCleanDrunk();
					case NotifyMsg.MSG_NOTIFY_SYSMSG:
						return (MsgPara) new MBNotifySysMsg();
					case NotifyMsg.MSG_NOTIFY_ACTIVITY_START:
						return (MsgPara) new MBNotifyActivityStart();
					case NotifyMsg.MSG_NOTIFY_ACTIVITY_RANKING:
						return (MsgPara) new MBNotifyActivityRanking();
					case NotifyMsg.MSG_NOTIFY_SHOW_ITEM:
						return (MsgPara) new MBNotifyShowItem();
					case NotifyMsg.MSG_NOTIFY_VIE_CHOP:
						return (MsgPara) new MBNotifyVieChop();
					case NotifyMsg.MSG_NOTIFY_CHANGE_DICE:
						return (MsgPara) new MBNotifyChangeDice();
					case NotifyMsg.MSG_NOTIFY_SHIELD:
						return (MsgPara) new MBNotifyShield();
					case NotifyMsg.MSG_NOTIFY_MANY_CHOP:
						return (MsgPara) new MBNotifyManyChop();
					case NotifyMsg.MSG_NOTIFY_TRIGGER_SHIELD:
						return (MsgPara) new MBNotifyTriggerShield();
					case NotifyMsg.MSG_NOTIFY_KILL_ORDER:
						return (MsgPara) new MBNotifyKillOrder();
					case NotifyMsg.MSG_NOTIFY_UNARMED:
						return (MsgPara) new MBNotifyUnarmed();
					case NotifyMsg.MSG_NOTIFY_DING:
						return (MsgPara) new MBNotifyDing();
					case NotifyMsg.MSG_NOTIFY_LEVELUP:
						return (MsgPara) new MBNotifyLevelUp();
					case NotifyMsg.MSG_NOTIFY_GET_GIFT:
						return (MsgPara) new MBNotifyGetGift();
					case NotifyMsg.MSG_NOTIFY_FRIEND_EVENT:
						return (MsgPara) new MBNotifyFriendEvent();
					case NotifyMsg.MSG_NOTIFY_PLAY_ANIMATION:
						return (MsgPara) new MBNotifyPlayAnimation();
					case NotifyMsg.MSG_NOTIFY_FACE:
						return (MsgPara) new MBNotifyFace();
					case NotifyMsg.MSG_NOTIFY_REBEL_CHOP:
						return (MsgPara) new MBNotifyRebelChop();
					case NotifyMsg.MSG_NOTIFY_LEARN_SKILL:
						return (MsgPara) new MBNotifyLearnSkill();
					case NotifyMsg.MSG_NOTIFY_SHAKE_FRIEND:
						return (MsgPara) new MBNotifyShakeFriend();
					case NotifyMsg.MSG_NOTIFY_ACTIVITY_INFO:
						return (MsgPara) new MBNotifyActivityInfo();
					case NotifyMsg.MSG_NOTIFY_LEAVEMSG:
						return (MsgPara) new MBNotifyLeaveMsg();
					case NotifyMsg.MSG_NOTIFY_LOGINROOM_B:
						return (MsgPara) new MBNotifyLoginRoomB();
					case NotifyMsg.MSG_NOTIFY_NO_PWD_QUESTION:
						return (MsgPara) new MBNotifyNoPwdQuestion();
					case NotifyMsg.MSG_NOTIFY_LIGHTNING:
						return (MsgPara) new MBNotifyLightning();
					case NotifyMsg.MSG_NOTIFY_VIE_CHOP_B:
						return (MsgPara) new MBNotifyVieChopB();
					case NotifyMsg.MSG_NOTIFY_UNITE:
						return (MsgPara) new MBNotifyUnite();
					case NotifyMsg.MSG_NOTIFY_START_UNITE:
						return (MsgPara) new MBNotifyStartUnite();
					case NotifyMsg.MSG_NOTIFY_RESULT_UNITE:
						return (MsgPara) new MBNotifyResultUnite();
					case NotifyMsg.MSG_NOTIFY_CHANGE_STATUS:
						return (MsgPara) new MBNotifyChangeStatus();
					case NotifyMsg.MSG_NOTIFY_MISSION_REWARD:
						return (MsgPara) new MBNotifyMissionReward();
					case NotifyMsg.MSG_NOTIFY_PLAY_ANIMATION_TWO:
						return (MsgPara) new MBNotifyPlayAnimationTwo();
						default:
						break;
				}
			}

			default:
				return null;
		}
	}
	
	public int CreatePara() {
		if(m_pMsgPara != null) {
			m_pMsgPara = null;
		}
		m_pMsgPara = CreateMsgPara(m_MsgHead.m_sMsgID, (short) m_MsgHead.m_cMsgType);
		if(m_pMsgPara == null) {
			return -1;
		}
		return 0;
	}
	
	public int Encode(byte[] aOputBuffer, short aOutLength) {
		if(aOputBuffer == null) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.wrap(aOputBuffer);
		Temp.clear();
		short TempLength = 0;
		aOutLength = 2;
		TempLength += aOutLength;
		Temp.position(TempLength);
		Temp.putInt(iSessionID);
		Temp.put(iFlag);
		TempLength = (short) Temp.position();
		if(iFlag == 1) {
			TempLength = (short) m_CSHead.Encode(aOputBuffer, TempLength);
			if(TempLength < 0) {
				return -1;
			}
		}
		TempLength = (short) m_MsgHead.Encode(aOputBuffer, TempLength);
		if(TempLength == -1) {
			return -2;
		}
		else {
			aOutLength = TempLength;
		}
		if(m_pMsgPara == null) {
			return -3;
		}
		TempLength = (short) m_pMsgPara.Encode(aOputBuffer, TempLength);
		if(TempLength == -1) {
			return -3;
		}
		else {
			aOutLength = TempLength;
		}
		Temp.putShort(0, aOutLength);
		return aOutLength;
	}
	
	public int Decode(byte[] aInBuffer, short aInLenght) {
		if(aInBuffer == null || aInLenght <= 0) {
			return -1;
		}
		ByteBuffer Temp = ByteBuffer.wrap(aInBuffer, 0, aInLenght);
		short shLeftLenght = 0;
		short sTempLength;
		short sMsgSize;
//		Temp.put(aInBuffer);
//		Temp.flip();
		shLeftLenght = Temp.getShort();
		Tools.debug("StartNotify !!!!!!!!shLeftLenght = " + shLeftLenght + "aInLenght = " + aInLenght);
		sMsgSize = shLeftLenght;
		if(shLeftLenght > aInLenght) {
			return -2;
		}
		iSessionID = Temp.getInt();
		iFlag = Temp.get();
		sTempLength = (short) Temp.position();
		if(iFlag == 1) {
			sTempLength = (short) m_CSHead.Decode(Temp.array(), sTempLength);
			if(sTempLength < 0) {
				return -3;
			}
		}
		sTempLength = (short) m_MsgHead.Decode(Temp.array(), sTempLength);
		if(sTempLength == -1) {
			return -4;
		}
		shLeftLenght -= sTempLength;
		if(shLeftLenght == 0) {
			return -5;
		}
		
		if(CreatePara() == -1) {
			return -6;
		}
		
		sTempLength = (short) m_pMsgPara.Decode(Temp.array(), sTempLength);
		Tools.debug("StartNotify !!!!!!!!sTempLength = " + sTempLength + " sMsgSize = " + sMsgSize);
		if(sTempLength < 0) {
			return -7;
		}
		return sTempLength;
	}
}
