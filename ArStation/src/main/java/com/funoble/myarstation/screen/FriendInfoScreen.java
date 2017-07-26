/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: FriendInfo.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-7-10 下午09:33:52
 *******************************************************************************/
package com.funoble.myarstation.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.os.Message;
import android.os.Vibrator;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.funoble.myarstation.adapter.FriendListAdapter;
import com.funoble.myarstation.adapter.SearchPeopleListAdapter;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.FriendListInfo;
import com.funoble.myarstation.data.cach.RoomData;
import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.socket.protocol.MBRspAddFriend;
import com.funoble.myarstation.socket.protocol.MBRspDing;
import com.funoble.myarstation.socket.protocol.MBRspFriendList;
import com.funoble.myarstation.socket.protocol.MBRspPlayerInfoThree;
import com.funoble.myarstation.socket.protocol.MBRspPlayerInfoTwo;
import com.funoble.myarstation.socket.protocol.MBRspSearchPeople;
import com.funoble.myarstation.socket.protocol.MBRspShakaFriend;
import com.funoble.myarstation.socket.protocol.MBRspTrace;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.SettingManager;
import com.funoble.myarstation.utils.ShakeListener;
import com.funoble.myarstation.utils.ShakeListener.OnShakeListener;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.MessageEventID;


/**
 * description 
 * version 1.0
 * author 
 * update 2013-6-27 下午11:54:15 
 */
public class FriendInfoScreen extends GameView implements OnClickListener, 
															OnItemSelectedListener,
															OnItemClickListener, 
															OnShakeListener,
															OnScrollListener {
	
	private int	iGameState = 0; //pak在数据动画
	private Scene iSceneLoading = null;
	//资源
	private Project iUIPak = null;
	
	private static	final  int  FRRIEND_STATUS_STRACE = 0;
	private static	final  int  FRRIEND_STATUS_SHAKE = 1;
	private static	final  int  FRRIEND_STATUS_TIME = 2;
	private static	final  int  FRRIEND_STATUS_NONE = 3;
	
	private ListView 			iFriendListView;
	private List<FriendListInfo>	iFriendList;
	private FriendListAdapter	iFriendAdapter;
	private int 				iFriendLastItem;
	private int 				iFriendTotal;
	private int 				iStartIndex;
	
	private ListView 			iSearchListView;
	private List<FriendListInfo>	iSearchList;
	private SearchPeopleListAdapter	iSearchAdapter;
	private int 				iSearchLastItem;
	private int 				iSearchTotal;
	private int 				iSearchStartIndex;
	
	private TextView			tvOnlineCount;
	
	private Button				btnSuport;
	private Button	 			btnTrace;
	private Button	 			btnShake;
	private TextView	 		tvTime;
	
	
	private MyArStation  iGameMain = MyArStation.mGameMain;
	
	private HashMap<Integer, FriendInfo>  iFriendInfos;
	private int iCurrentFriendID;
	private int iOldFriendID;
	
	private int iSelectPos = -1;
	
	private FriendInfo friendInfo;
	
	private ShakeCountDownTimer shakeCountDownTimer;
	
	private ShakeListener iShakeListener = null;
	
	private RelativeLayout	rlContent;
	private Button	btnReturn;
	private Vibrator iVibrator;
	private boolean ibVibrate = SettingManager.getInstance().isShaka();//是否震动
	private Animation pushRightInAnimation;
	private Animation pushLeftOutAnimation;
	private static final int STATUS_NONE = (1 << 0);
	private static final int STATUS_IN = (1 << 1);
	private static final int STATUS_OUT = (1 << 2);
	
	private int status	= STATUS_NONE;
	private boolean bReturn = false;
	private RelativeLayout mainRelativeLayout;
	
	private Button searchPeople;
	private boolean iFrieds = true;
	
	private boolean ibScrollStop = false; //是否停止滚动
	
	private void initAnimation() {
		pushRightInAnimation = AnimationUtils.loadAnimation(iGameMain, R.anim.push_right_in);
		pushLeftOutAnimation = AnimationUtils.loadAnimation(iGameMain, R.anim.push_left_out);
	}
	
	private void pushInAnimation() {
		rlContent.startAnimation(pushRightInAnimation);
		rlContent.setVisibility(View.VISIBLE);
		mainRelativeLayout.setVisibility(View.VISIBLE);
		iGameState = -1;
		pushRightInAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				iShakeListener.start();
			}
		});
	}
	
	private void pushOutAnimation() {
		mainRelativeLayout.startAnimation(pushLeftOutAnimation);
		pushLeftOutAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				MyArStation.getInstance().iPlayer.iFriendList.clear();
				MyArStation.mGameMain.iPlayer.iRspFriendList = null;
				messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
			}
		});
	}
	
	public FriendInfoScreen() {
	}
	
	public FriendInfoScreen(int aUserID, int aPosition) {
		iCurrentFriendID = aUserID;
		iSelectPos = aPosition;
	}
	
	/* 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btnFriendReturn) {
			if(iFrieds) {
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
				pushOutAnimation();
			}
			else {
				SwitchTab(true);
			}
		} else if (id == R.id.btnShake) {
			MyArStation.iGameProtocol.requestSearchPeople(0);
		} else {
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#init()
	 */
	@Override
	public void init() {
//		Tools.debug("FriendInfoScreen::init");
		//删除下载任务
		MyArStation.iHttpDownloadManager.cleanTask();
		//
		loadUIPak();
		messageEvent(MessageEventID.EMESSAGE_EVENT_TO_FRIENDINFO);
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#releaseResource()
	 */
	@Override
	public void releaseResource() {
//		Tools.debug("FriendInfoScreen::releaseResource");
		//删除下载任务
		MyArStation.iHttpDownloadManager.cleanTask();
		//
		cancelTimeAndVibrator();
		iSceneLoading = null;
		iUIPak = null;
		iFriendList = null;
		iFriendListView = null;
		iFriendAdapter = null;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#paintScreen(android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	public void paintScreen(Canvas g, Paint paint) {
		//载入数据
		g.drawBitmap(MyArStation.iImageManager.iBackLoginBmp,
				0,
				0,
				null);
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
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#performL()
	 */
	@Override
	public void performL() {
		//载入数据
		if(iGameState == 0) {
			if(iSceneLoading != null) {
				iSceneLoading.handle();
			}
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK ) {
			if(!bReturn) {
				if(shakeCountDownTimer != null) {
					shakeCountDownTimer.cancel();
				}
				if(iVibrator != null) {
					iVibrator.cancel();
				}
				if(iFrieds) {
					bReturn = !bReturn;
					pushOutAnimation();
				}
				else {
					SwitchTab(true);
				}
			}
			return true;
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onDown(android.view.MotionEvent)
	 */
	@Override
	public boolean onDown(MotionEvent e) {
	//	ibScrollStop = false;
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onLongPress(android.view.MotionEvent)
	 */
	@Override
	public boolean onLongPress(MotionEvent e) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onSingleTapUp(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#pointerPressed(int, int)
	 */
	@Override
	public boolean pointerPressed(int aX, int aY) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#pointerReleased(int, int)
	 */
	@Override
	public boolean pointerReleased(int aX, int aY) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		removeProgressDialog();
		switch (msg.what) {
		case ResultCode.ERR_TRACE_PLAYER_NULL:
		case ResultCode.ERR_TRACE_PLAYER_IDLE:
		case ResultCode.ERR_DING_SELF:
		case ResultCode.ERR_DING_LIMIT:
		case ResultCode.ERR_DING_ALREADY_DONE:
		case ResultCode.ERR_DING_NOT_FRIEND:
		case ResultCode.ERR_DING_PLAYER_NULL:
			Tools.showSimpleToast(iGameMain, Gravity.CENTER, (String)msg.obj, Toast.LENGTH_SHORT);
		break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_FriendList:
//			Tools.debug("FriendInfoScreen::handleMessage::MessageEventID.EMESSAGE_EVENT_SOCKET_FriendList");
			//删除下载任务
			MyArStation.iHttpDownloadManager.cleanTask();
			//
			final MBRspFriendList FriendList = (MBRspFriendList)msg.obj;
			MyArStation.mGameMain.iPlayer.iRspFriendList = FriendList;
			if(FriendList.iMsg.length() > 0) {
				Tools.showSimpleToast(iGameMain, Gravity.CENTER, FriendList.iMsg, Toast.LENGTH_SHORT);
			}
			tvOnlineCount.setText(iGameMain.getString(R.string.FriendAmount, FriendList.iFriendOLCount, FriendList.iFriendTotalCount));
			int count = FriendList.iCount;
			iFriendTotal = FriendList.iFriendTotalCount;
			iStartIndex = FriendList.iStartIndex;
			for(int i = 0; i < count; i++) {
				FriendListInfo info = new FriendListInfo(FriendList.iFriendUserID.get(i), FriendList.iFriendNick.get(i),
						FriendList.iFriendPicName.get(i), "", FriendList.iOnLine.get(i), FriendList.iUpdate.get(i));
				MyArStation.getInstance().iPlayer.iFriendList.add(info);
			}
			iFriendListView.setAdapter(iFriendAdapter);
			sortFriendList();
			iFriendListView.setSelection(iFriendLastItem - 1);
			if(status == STATUS_NONE) {
				status = STATUS_IN;
				pushInAnimation();
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_PlayerInfoTHREE:
//			MBRspPlayerInfoThree iPlayerInfo = (MBRspPlayerInfoThree)msg.obj;
//			friendInfo = new FriendInfo(iPlayerInfo.iUserId, iPlayerInfo.iNick,
//					iPlayerInfo.iGb, iPlayerInfo.iReward, iPlayerInfo.iWinning, 
//					iPlayerInfo.iKillsCount, iPlayerInfo.iWealthRank, iPlayerInfo.iLevelRank,
//					iPlayerInfo.iVictoryRank, iPlayerInfo.iSupportCount,
//					"", iPlayerInfo.iBigPicName,
//					iPlayerInfo.iBeKillsCount, iPlayerInfo.iRankTotalCount, iPlayerInfo.iAction, iPlayerInfo.iSecond);
//			iFriendInfos.put(iPlayerInfo.iUserId, friendInfo);
//			iOldFriendID = iPlayerInfo.iUserId;
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RequestFriendInfo:
			Pair<Integer, Integer> pair = (Pair)msg.obj;
			iCurrentFriendID = pair.first;
			//删除系统控件
			MyArStation.mGameMain.mainLayout.removeAllViews();
//			Tools.debug("FriendInfoScreen::handleMessage::MessageEventID.EMESSAGE_EVENT_SOCKET_RequestFriendInfo");
			//删除下载任务
			MyArStation.iHttpDownloadManager.cleanTask();
			//切换到个人资料页面
			iSelectPos = iFriendListView.getFirstVisiblePosition();
			GameCanvas pGameCanvas = GameCanvas.getInstance();
			if(iCurrentFriendID == MyArStation.getInstance().iPlayer.iUserID) {
				pGameCanvas.changeView(new InfoScreen(iCurrentFriendID, iSelectPos));
			}
			else {
				pGameCanvas.changeView(new FriendDetailScreen(iCurrentFriendID, iSelectPos));
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspDing:
//			MBRspDing rspDing = (MBRspDing)msg.obj;
//			if(rspDing.iMsg != null) {
//				Tools.showSimpleToast(GameMain.mGameMain, Gravity.CENTER, rspDing.iMsg, Toast.LENGTH_LONG);
//			}
//			if(iCurrentFriendID == rspDing.iDesUserID) {
//				tvSuport.setText(String.valueOf(rspDing.iSupportCount));
//			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_PIC:
			if(iFriendAdapter != null) {
				ibScrollStop = true;
				iFriendAdapter.notifyDataSetChanged();
				ibScrollStop = true;
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_SINGLE_PIC:
			if(iFriendAdapter != null) {
				ibScrollStop = true;
				iFriendAdapter.notifyDataSetChanged();
				ibScrollStop = true;
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_UPDATE_SHAKE_TIME:
//			friendInfo = iFriendInfos.get(iCurrentFriendID);
//			if(friendInfo != null) {
//				int sec = (Integer) msg.obj;
//				friendInfo.setSec(sec);
//				String time = formatTime(sec);
//				System.out.println("time = " + time);
//			}
//			updateFriendShakeTime(iCurrentFriendID, (Integer) msg.obj, FRRIEND_STATUS_TIME);
			break;
		case MessageEventID.EMESSAGE_EVENT_COUNTDOWNTIME_UP:
//			updateFriendShakeTime(iCurrentFriendID, 0, FRRIEND_STATUS_SHAKE);
//			System.out.println("time's up");
			break;
		case MessageEventID.EMESSAGE_EVENT_TO_FRIENDINFO:
//			Tools.debug("FriendInfoScreen::handleMessage::MessageEventID.EMESSAGE_EVENT_TO_FRIENDINFO");
//			//删除下载任务
//			GameMain.iHttpDownloadManager.cleanTask();
			//
			MyArStation.mGameMain.mainLayout.removeAllViews();
			mainRelativeLayout = (RelativeLayout) LayoutInflater.from(MyArStation.mGameMain.getApplicationContext()).inflate(R.layout.new_friendinfo, null);
			MyArStation.mGameMain.mainLayout.addView(mainRelativeLayout);
			mainRelativeLayout.setVisibility(View.GONE);
			initAnimation();
			iOldFriendID = iCurrentFriendID;
			iFriendInfos = new HashMap<Integer, FriendInfo>();
			rlContent = (RelativeLayout)iGameMain.findViewById(R.id.rlContent);
			rlContent.setVisibility(View.INVISIBLE);
			btnReturn = (Button)iGameMain.findViewById(R.id.btnFriendReturn);
			btnReturn.setOnClickListener(this);
			tvOnlineCount = (TextView)iGameMain.findViewById(R.id.tvfriendlist);
			iFriendListView = (ListView)iGameMain.findViewById(R.id.lvfriend);
			iFriendListView.setOnItemSelectedListener(this);
			iFriendListView.setOnItemClickListener(this);
			iFriendListView.setOnScrollListener(this);
			iFriendList = MyArStation.getInstance().iPlayer.iFriendList;
			iFriendAdapter = new FriendListAdapter(MyArStation.getInstance(), iFriendList);
			iFriendListView.setAdapter(iFriendAdapter);
			
			iSearchListView = (ListView)iGameMain.findViewById(R.id.lvSearchfriend);
			iSearchListView.setOnItemSelectedListener(this);
			iSearchListView.setOnItemClickListener(this);
			iSearchListView.setOnScrollListener(this);
			iSearchList = new ArrayList<FriendListInfo>();
			iSearchAdapter = new SearchPeopleListAdapter(MyArStation.getInstance(), iSearchList);
			iSearchListView.setAdapter(iFriendAdapter);
			searchPeople = (Button)iGameMain.findViewById(R.id.btnShake);
			searchPeople.setOnClickListener(this);
			
			iShakeListener = new ShakeListener(MyArStation.getInstance().getApplicationContext());
			iShakeListener.setOnShakeListener(this);
	        
			if(iFriendList.size() == 0 || MyArStation.mGameMain.iPlayer.iRspFriendList == null) {
				requestFriendList(0);
			}
			else {
				MBRspFriendList friendList2 = MyArStation.mGameMain.iPlayer.iRspFriendList;
				tvOnlineCount.setText(iGameMain.getString(R.string.FriendAmount, friendList2.iFriendOLCount, friendList2.iFriendTotalCount));
				iFriendLastItem = iFriendList.size();
				iFriendTotal = friendList2.iFriendTotalCount;
				iStartIndex = friendList2.iStartIndex;
				sortFriendList();
				iFriendListView.setSelection(iSelectPos);
				if(status == STATUS_NONE) {
					status = STATUS_IN;
					pushInAnimation();
				}
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspSearchPeople:
//			Tools.debug("FriendInfoScreen::handleMessage::MessageEventID.EMESSAGE_EVENT_SOCKET_RspSearchPeople");
			//删除下载任务
			MyArStation.iHttpDownloadManager.cleanTask();
			//
			iShakeListener.start();
			MBRspSearchPeople mbRspSearchPeople = (MBRspSearchPeople) msg.obj;
			if(mbRspSearchPeople.iMsg.length() > 0) {
				Tools.showSimpleToast(iGameMain, Gravity.CENTER, mbRspSearchPeople.iMsg, Toast.LENGTH_SHORT);
			}
			if(mbRspSearchPeople.iResult == 0) {
				count = mbRspSearchPeople.iCount;
				iSearchList.clear();
				for(int i = 0; i < count; i++) {
					FriendListInfo info = new FriendListInfo(mbRspSearchPeople.iUserID.get(i), mbRspSearchPeople.iNick.get(i),
							mbRspSearchPeople.iPicName.get(i), "", 0, 0);
					iSearchList.add(info);
				}
				iSearchListView.setAdapter(iSearchAdapter);
				iSearchListView.setSelection(iSearchLastItem - 1);
			}
			if(iFrieds) {
				SwitchTab(false);
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_AddFriend:
//			MBRspAddFriend rspAddFriend = (MBRspAddFriend)msg.obj;
//			if(rspAddFriend.iMsg.length() > 0) {
//				Tools.showSimpleToast(iGameMain, Gravity.CENTER, rspAddFriend.iMsg, Toast.LENGTH_SHORT);
//			}
			break;
		default:
			break;
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#OnProcessCmd(java.lang.Object, int, int)
	 */
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
		MobileMsg pMobileMsg = (MobileMsg) socket;
		switch (aMobileType) {
		case IM.IM_RESPONSE:
		{
			switch (aMsgID) {
			case RspMsg.MSG_RSP_FRIEND_LIST:
				MBRspFriendList rspFriendList = (MBRspFriendList)pMobileMsg.m_pMsgPara;
				if(rspFriendList == null) {
					break;
				}
//				Tools.debug(rspFriendList.toString());
				if(rspFriendList.nResult == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_FriendList, rspFriendList);
				}
				else {
					messageEvent(rspFriendList.nResult, rspFriendList.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_TRACE:
//				MBRspTrace rspTrace = (MBRspTrace)pMobileMsg.m_pMsgPara;
//				if(rspTrace == null) {
//					break;
//				}
//				Tools.debug(rspTrace.toString());
//				if(rspTrace.iResult == ResultCode.SUCCESS) {
//					RoomData pair = new RoomData(rspTrace.iRoomID, rspTrace.iTableID, rspTrace.iRoomType);
//					messageEvent(MessageEventID.EMESSAGE_EVENT_TO_PROGRESS, pair);
//				}
//				else {
//					messageEvent(rspTrace.iResult, rspTrace.iMsg);
//				}
				break;
			case RspMsg.MSG_RSP_PLAYINFO_TWO:
//				removeProgressDialog();
//				MBRspPlayerInfoTwo rspMainPage = (MBRspPlayerInfoTwo)pMobileMsg.m_pMsgPara;
//				if(rspMainPage == null) {
//					break;
//				}
//				Tools.debug(rspMainPage.toString());
//				int type = rspMainPage.iResult;
//				if(type == ResultCode.SUCCESS) {
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_PlayerInfoTwo, rspMainPage);
//				}
//				else {
//					messageEvent(type, rspMainPage.iMsg);
//				}
				break;
			case RspMsg.MSG_RSP_PLAYINFO_THR:
//				removeProgressDialog();
//				MBRspPlayerInfoThree rspMainPage1 = (MBRspPlayerInfoThree)pMobileMsg.m_pMsgPara;
//				if(rspMainPage1 == null) {
//					break;
//				}
//				Tools.debug(rspMainPage1.toString());
//				int type = rspMainPage1.iResult;
//				if(type == ResultCode.SUCCESS) {
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_PlayerInfoTHREE, rspMainPage1);
//				}
//				else {
//					messageEvent(type, rspMainPage1.iMsg);
//				}
				break;
			case RspMsg.MSG_RSP_DING:
//				removeProgressDialog();
//				MBRspDing rspDing = (MBRspDing)pMobileMsg.m_pMsgPara;
//				if(rspDing == null) {
//					break;
//				}
//				Tools.debug(rspDing.toString());
//				int type = rspDing.iResult;
//				if(type == ResultCode.SUCCESS) {
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspDing, rspDing);
//				}
//				else {
//					messageEvent(type, rspDing.iMsg);
//				}
				break;
			case RspMsg.MSG_RSP_SHAKE_FRIEND:
//				removeProgressDialog();
//				MBRspShakaFriend rspShakaFriend = (MBRspShakaFriend)pMobileMsg.m_pMsgPara;
//				if(rspShakaFriend == null) {
//					break;
//				}
//				Tools.debug(rspShakaFriend.toString());
//				int type = rspShakaFriend.iResult;
//				if(type == ResultCode.SUCCESS) {
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_SHAKE_FRIEND, rspShakaFriend);
//				}
//				else {
//					messageEvent(type, rspShakaFriend.iMsg);
//				}
				break;
			case RspMsg.MSG_RSP_SEARCH_PEOPLE:
				MBRspSearchPeople rspSearchPeople = (MBRspSearchPeople)pMobileMsg.m_pMsgPara;
				if(rspSearchPeople == null) {
					break;
				}
//				Tools.debug(rspSearchPeople.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspSearchPeople, rspSearchPeople);
				break;
			case RspMsg.MSG_RSP_ADD_FRIEND:
//				removeProgressDialog();
//				MBRspAddFriend rspAddFriend = (MBRspAddFriend)pMobileMsg.m_pMsgPara;
//				if(rspAddFriend == null) {
//					break;
//				}
//				Tools.debug(rspAddFriend.toString());
//				messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_AddFriend, rspAddFriend);
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

	private void sortFriendList() {
		ComparatorUser comparator=new ComparatorUser();
		Collections.sort(MyArStation.getInstance().iPlayer.iFriendList, comparator);
		
		ListIterator<FriendListInfo> it = MyArStation.getInstance().iPlayer.iFriendList.listIterator();
        while (it.hasNext()) {
            int pos = it.nextIndex();
            FriendListInfo data = it.next();
            if(data.iFriendUserID == MyArStation.getInstance().iPlayer.iUserID) {
            	FriendListInfo info = new FriendListInfo(
            			data.iFriendUserID, data.iFriendNick, 
            			data.iFriendPicName, data.iFriendLevel, 
            			data.iFriendStatus, data.iUpdate);
            	it.remove();
            	MyArStation.getInstance().iPlayer.iFriendList.add(0, info);
            	break;
            }
        }
		iFriendAdapter.notifyDataSetChanged();
	}

	/* 
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Tools.debug("FriendInfoScreen::onItemSelected id:" + id + ", position:" + position);
		//
		iFriendAdapter.setSelectedRow(view);
	}

	/* 
	 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
	 */
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		Tools.debug("FriendInfoScreen::onNothingSelected");
	}

	/* 
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Tools.debug("FriendInfoScreen::onItemClick id:" + id + ", position:" + position);
		iFriendAdapter.setSelectedRow(view);
	}
	
	private void requestFriendList(int aStartIndex) {
		if(iGameMain.iGameProtocol.requestFriendList(aStartIndex)) {
			Tools.debug("FriendInfoScreen::requestFriendList");
			//删除下载任务
			MyArStation.iHttpDownloadManager.cleanTask();
			//
			if(status != STATUS_NONE) {
				showProgressDialog(R.string.Loading_String);
			}
		}
	}
	
//	private void requestTraceFriend() {
//		if(GameMain.iGameProtocol != null) {
//			if(GameMain.iGameProtocol.requestTrace(iCurrentFriendID)) {
//				//删除下载任务
//				GameMain.iHttpDownloadManager.cleanTask();
//				//
//				showProgressDialog(R.string.Loading_String);
//			}
//		}
//	}
	
	public void cancelTimeAndVibrator () {
		if(shakeCountDownTimer != null) {
			shakeCountDownTimer.cancel();
		}
		if(iShakeListener != null) {
			iShakeListener.stop();
			iShakeListener = null;
		}
		if(iVibrator != null) {
			iVibrator.cancel();
		}
	}
	
//	private void switchAction(FriendInfo aFriendInfo) {
//		switch (aFriendInfo.iAction) {
//		case FRRIEND_STATUS_STRACE:
//			btnShake.setVisibility(View.GONE);
//			btnTrace.setVisibility(View.VISIBLE);
//			tvTime.setVisibility(View.GONE);
//			break;
//		case FRRIEND_STATUS_SHAKE:
//			btnShake.setVisibility(View.VISIBLE);
//			btnTrace.setVisibility(View.GONE);
//			tvTime.setVisibility(View.GONE);
//			
//			break;
//		case FRRIEND_STATUS_TIME:
//			btnShake.setVisibility(View.GONE);
//			btnTrace.setVisibility(View.GONE);
//			tvTime.setVisibility(View.VISIBLE);
//			tvTime.setText(formatTime(aFriendInfo.iSec));
//			break;
//		case FRRIEND_STATUS_NONE:
//			btnShake.setVisibility(View.GONE);
//			btnTrace.setVisibility(View.GONE);
//			tvTime.setVisibility(View.GONE);
//			break;
//
//		default:
//			break;
//		}
//	}
	
//	private void requestPlayerInfo(int userid) {
//		if(GameMain.iGameProtocol.requestPlayerInfoThree(userid)) {
//			//删除下载任务
//			GameMain.iHttpDownloadManager.cleanTask();
//			//
//			showProgressDialog(R.string.Loading_String);
//		}
//	}
	
//	private void requestDing(int userid) {
//		if(GameMain.iGameProtocol.requestDing(userid)) {
//			showProgressDialog(R.string.Loading_String);
//		}
//	}
	
//	private void requestShakeFriend(int userid) {
//		if(GameMain.iGameProtocol.requestShakeFriend(userid)) {
//			showProgressDialog(R.string.Loading_String);
//		}
//	}
	
//	public void setPhotos(FriendInfo aFriendInfo) {
//		bmList.clear();
//		Bitmap bm = GameMain.iDownloadManager.getImage(aFriendInfo.iPicName);
//		if(bm != null) {
//			bmList.add(bm);
//			iGallery.setAdapter(new ImageAdapter(GameMain.getInstance(), bmList));
//			iGallery.invalidate();
//		}
//	}
	
	public class FriendInfo {
		public String iFriendNick;
		public int	  iFriendMoney;
		public int    iLose;
		public int    iAward;
		public int	  iWinPecent;
		public int    iVectory;
		public int    iRankWealths;
		public int    iRankLevel;
		public int    iRankVectory;
		public int    iSuport;
		public String iFriendShare;
		public String iPicName;
		public int    iTotalRank;
		public int    iUserID;
		public int    iAction;
		private int    iSec;
		public long   iTimeStamp;
		public long   iNewTimeStamp;
		
		public FriendInfo() {
			this.iFriendNick = "";
			this.iFriendMoney = 0;
			this.iAward = 0;
			this.iLose = 0;
			this.iTotalRank = 0;
			this.iWinPecent = 0;
			this.iVectory = 0;
			this.iRankWealths = 0;
			this.iRankLevel = 0;
			this.iRankVectory = 0;
			this.iSuport = 0;
			this.iFriendShare = "";
			this.iPicName = "";
			this.iUserID = 0;
			this.iAction = 0;
			this.iSec = 0;
			this.iTimeStamp = System.currentTimeMillis();
		}
		/**
		 * construct
		 * @param iFriendNick
		 * @param iFriendMoney
		 * @param iAward
		 * @param iWinPecent
		 * @param iVectory
		 * @param iRankWealths
		 * @param iRankLevel
		 * @param iRankVectory
		 * @param iSuport
		 * @param iFriendShare
		 * @param iPicName
		 */
		public FriendInfo(int iUserID, String iFriendNick, int iFriendMoney, int iAward,
				int iWinPecent, int iVectory, int iRankWealths, int iRankLevel,
				int iRankVectory, int iSuport, String iFriendShare,
				String iPicName, int iLose, int iTotalRank, int iAction, int iSec) {
			this.iUserID = iUserID;
			this.iFriendNick = iFriendNick;
			this.iFriendMoney = iFriendMoney;
			this.iAward = iAward;
			this.iLose = iLose;
			this.iTotalRank = iTotalRank;
			this.iWinPecent = iWinPecent;
			this.iVectory = iVectory;
			this.iRankWealths = iRankWealths;
			this.iRankLevel = iRankLevel;
			this.iRankVectory = iRankVectory;
			this.iSuport = iSuport;
			this.iFriendShare = iFriendShare;
			this.iPicName = iPicName;
			this.iAction = iAction;
			this.iSec = iSec;
			this.iTimeStamp = System.currentTimeMillis();
		}
		
		public void setSec(int aSec) {
			this.iTimeStamp = System.currentTimeMillis();
			iSec = aSec;
		}
		
		public void upTime() {
			if(iSec != 0) {
				iNewTimeStamp = System.currentTimeMillis();
				long temp = (iNewTimeStamp - this.iTimeStamp) / 1000;
				System.out.println("temp = " + temp);
				int temTime = (int) (iSec - temp);
				iSec = temTime >= 0 ? temTime : 0;
				iTimeStamp = iNewTimeStamp;
			}
		}
		
		public int getSec() {
			return iSec;
		}
	}
	
	public class ShakeCountDownTimer extends CountDownTimer {

		/**
		 * construct
		 * @param millisInFuture
		 * @param countDownInterval
		 */
		public ShakeCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			
		}

		/* 
		 * @see android.os.CountDownTimer#onTick(long)
		 */
		@Override
		public void onTick(long millisUntilFinished) {
//			int min = (int) (millisUntilFinished / (60 * 1000));
//			int sec = (int) (millisUntilFinished % (60 * 1000));
//			int resultsec = (int)(Math.round(sec / 1000));
//			String time = "";
//			time = resultsec >= 10 ? (min + ":" + resultsec) : (min + ": 0" + resultsec);
//			System.out.println("time = " + time);
			messageEvent(MessageEventID.EMESSAGE_EVENT_UPDATE_SHAKE_TIME, (int)(millisUntilFinished / 1000));
		}

		/* 
		 * @see android.os.CountDownTimer#onFinish()
		 */
		@Override
		public void onFinish() {
			messageEvent(MessageEventID.EMESSAGE_EVENT_COUNTDOWNTIME_UP);
		}
		
	}
	
	public String formatTime(int millisUntilFinished) {
		String time = "";
		int min = (int) (millisUntilFinished / 60);
		int sec = (int) (millisUntilFinished % 60);
		int resultsec = (int)(Math.round(sec));
		time = resultsec >= 10 ? (min + ":" + resultsec) : (min + ":0" + resultsec);
		return time;
	}
	
//	public void updateFriendShakeTime(int aCurrentUseID, int aSec, int aAction) {
//		friendInfo = iFriendInfos.get(aCurrentUseID);
//		if(friendInfo != null) {
//			int sec = aSec;
//			friendInfo.setSec(sec);
//			friendInfo.iAction = aAction;
//			String time = formatTime(friendInfo.getSec());
//			switchAction(friendInfo);
//			System.out.println(friendInfo.iFriendNick + " time = " + time);
//		}
//	}
	/* 
	 * @see com.funoble.lyingdice.gamelogic.ShakeListener.OnShakeListener#onShake()
	 */
	@Override
	public void onShake() {
		iShakeListener.stop();
		MyArStation.iGameProtocol.requestSearchPeople(0);
	}
	
	public class ComparatorUser implements Comparator{
		
		public int compare(Object arg0, Object arg1) {
			FriendListInfo user0=(FriendListInfo)arg0;
			FriendListInfo user1=(FriendListInfo)arg1;
			
			if(user0.iFriendStatus > user1.iFriendStatus) {
				return -1;
			}
			else if(user0.iFriendStatus == user1.iFriendStatus) {
				return 0;
			}
			else {
				return 1;
			}
		}
		
	}
	
	private void loadUIPak() {
		iSceneLoading = LoadLoading();
	}

	/* 
	 * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(iFriendAdapter == null) {
			return;
		}
//		Tools.debug("滚动停止");
		//启动下载任务
		MyArStation.iHttpDownloadManager.startTask();
		//
		ibScrollStop = true;
		//
		if (iFriendLastItem == iFriendAdapter.getCount() 
				&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
			int pageIndex = iStartIndex;
			Tools.debug("FriendInfoScreen::BusinessDetail pageIndex" + pageIndex);
			if(iFriendListView.getCount() < iFriendTotal){
				requestFriendList(pageIndex);
			}
		}
	}

	/* 
	 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.AbsListView, int, int, int)
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		iFriendLastItem = firstVisibleItem + visibleItemCount;
		if(!ibScrollStop) {
//			Tools.debug("滚动中");
			//停止下载任务
			MyArStation.iHttpDownloadManager.StopTask();
		}
		ibScrollStop = false;
	}
	
	private void SwitchTab(boolean isFriend) {
		this.iFrieds = isFriend;
		iFriendListView.setVisibility(iFrieds ? View.VISIBLE : View.GONE);
		iSearchListView.setVisibility(!iFrieds ? View.VISIBLE : View.GONE);
		
		tvOnlineCount.setVisibility(iFrieds ? View.VISIBLE : View.GONE);
	}
}
