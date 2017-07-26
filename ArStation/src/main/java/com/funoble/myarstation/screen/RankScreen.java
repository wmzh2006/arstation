/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: RankScreen.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-29 上午11:14:43
 *******************************************************************************/
package com.funoble.myarstation.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.funoble.myarstation.adapter.RankAdapter;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.RankData;
import com.funoble.myarstation.data.cach.RoomData;
import com.funoble.myarstation.game.CPlayer;
import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.socket.protocol.MBRspAddFriend;
import com.funoble.myarstation.socket.protocol.MBRspRank;
import com.funoble.myarstation.socket.protocol.MBRspTrace;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.MessageEventID;

import java.util.List;


public class RankScreen extends GameView implements OnClickListener, AnimationListener, OnScrollListener{
	private int	iGameState = 0; //pak在数据动画
	private Scene iSceneLoading = null;
	//资源
	private Project iUIPak = null;
	public static final int OFFLINE = 0;
	public static final int ONLINE =  1;
	public static final int ONGAME =  2;
	
	public static final int NEXUS_STRANGER  = 0;
	public static final int NEXUS_FRIEND	 = 1;
	public static final int NEXUS_SELF      = 2;
	
	private static final int RankType_Wealths   = 0;
	private static final int RankType_Level	    = 1;
	private static final int RankType_Vectory   = 3;
	
	private ListView 		iMoneyListView;
	private List<RankData>		iMoneyList;
	private RankAdapter	iMoneyAdapter;
	private int 			iMoneyLastItem;
	private int 			iMoneyStartIndex;
	private int 			iMoneyTotalCount;
	
	private ListView 		iWardListView;
	private List<RankData>		iWardList;
	private RankAdapter	iWardAdapter;
	private int 			iWardLastItem;
	private int 			iWardLStartIndex;
	private int 			iWardTotalCount;
	
	private ListView 		iWinListView;
	private List<RankData>		iWinList;
	private RankAdapter	iWinAdapter;
	private int 			iWinlLastItem;
	private int 			iWinLStartIndex;
	private int 			iWinTotalCount;
	
	
	private Button			btMoney;
	private Button			btWard;
	private Button			btWin;
	private Button			btReturn;
	
	private ImageView		ivMyHead;
	private TextView		tvMyNick;
	private TextView		tvMyAttr;
	private TextView		tvMyWealths;
	private TextView		tvMyLevel;
	private TextView		tvMyVictory;
	private ImageView		ivIcon;
	
	private String			iMyMoney;
	private String			iMyLevel;
	private String			iMywin;
	
	private int[] 			iSelectedPos = new int[4];
	private MyArStation		iGameMain = MyArStation.mGameMain;
	private int				iRankType = RankType_Wealths;
//	private MBRspMainPage	iPlayerInfo = (MBRspMainPage)GameMain.iGameProtocol.getMobileMsgCS(RspMsg.MSG_RSP_MAINPAGE);
	private RelativeLayout mainRelativeLayout;
	private CPlayer 		iPlayer = MyArStation.iPlayer;
	//动画
	AnimationSet set = new AnimationSet(true);
	Animation animation;
	LayoutAnimationController controller;
	
	Animation putinAnimation;
	Animation putoutAnimation;
	
	private void animationIn() {
		mainRelativeLayout.startAnimation(putinAnimation);
		mainRelativeLayout.setVisibility(View.VISIBLE);
		
	}
	
	private void animationOut() {
		mainRelativeLayout.startAnimation(putoutAnimation);
		putoutAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mainRelativeLayout.setVisibility(View.INVISIBLE);
				clearCach();
				messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
			}
		});
	}
	
	private void initAnim() {
		animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(500);
		set.addAnimation(animation);

		animation = new TranslateAnimation(
		Animation.RELATIVE_TO_SELF, 50.0f,Animation.RELATIVE_TO_SELF, 0.0f,
		Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f
		);
		animation.setDuration(1000);
		set.addAnimation(animation);

		controller = new LayoutAnimationController(set, 0.5f);
		putinAnimation = AnimationUtils.loadAnimation(MyArStation.getInstance().getApplicationContext(), R.anim.push_right_in);
		putoutAnimation = AnimationUtils.loadAnimation(MyArStation.getInstance().getApplicationContext(), R.anim.push_left_out);
	}
	/**
	 * construct
	 */
	public RankScreen() {
	}
	
	public RankScreen(int auid, int[] aSelectedPos, int aRankType) {
		System.arraycopy(aSelectedPos, 0, iSelectedPos, 0, iSelectedPos.length);
		this.iRankType = aRankType;
	}


	/* 
	 * @see com.funoble.lyingdice.view.GameView#init()
	 */
	@Override
	public void init() {
		iSceneLoading = LoadLoading();
		messageEvent(MessageEventID.EMESSAGE_EVENT_TO_Rank);
	}
	
	/* 
	 * @see com.funoble.lyingdice.view.GameView#releaseResource()
	 */
	@Override
	public void releaseResource() {
		iSceneLoading = null;
		iUIPak = null;
		iSelectedPos = null;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#paintScreen(android.graphics.Canvas, android.graphics.Paint)
	 */
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
		else {
			g.drawBitmap(MyArStation.iImageManager.iBackLoginBmp,
					0,
					0,
					null);
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
	 * @see com.funoble.lyingdice.view.GameView#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK ) {
//			messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
			if(iGameState != 1) {
				iGameState = 1;
				animationOut();
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
		case ResultCode.ERR_RANK_TYPE_INVALED:
		case ResultCode.ERR_RANK_BUSY:
		case ResultCode.ERR_TRACE_PLAYER_NULL:
		case ResultCode.ERR_TRACE_PLAYER_IDLE:
		case ResultCode.ERR_FRIEND_ADD_SELF:
		case ResultCode.ERR_FRIEND_UID_INVALED:
		case ResultCode.ERR_FRIEND_ALREADY:
		case ResultCode.ERR_FRIEND_OTHERSIDE_LIMIT:
		case ResultCode.ERR_FRIEND_MYSIDE_LIMIT:
		case ResultCode.ERR_FRIEND_VISITOR:
			String string = (String) msg.obj;
			if(string.length() > 0) {
				Tools.showSimpleToast(iGameMain, Gravity.CENTER, (String) msg.obj, Toast.LENGTH_SHORT);
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_Rank_Wealths:
			final MBRspRank wealths = (MBRspRank)msg.obj;
			iPlayer.iRspRank.put(RankType_Wealths, wealths);
			if(wealths.iMsg.length() > 0) {
				Tools.showSimpleToast(iGameMain, Gravity.CENTER, wealths.iMsg, Toast.LENGTH_SHORT);
			}
			iMyMoney = wealths.iValue;
			iMoneyTotalCount = wealths.iRankTotalCount;
			iMoneyStartIndex = wealths.iStartIndex;
			setSelfInfo(wealths, RankType_Wealths);
			int Count = wealths.iCount;
			for(int i = 0; i < Count; i++) {
				RankData rank = new RankData(iMoneyList.size()+1, wealths.iRankUserID.get(i), wealths.iUserID, 
						wealths.iRankNick.get(i),  wealths.iRankPicName.get(i),
						wealths.iRankValue.get(i), wealths.iOnLine.get(i), wealths.iFriend.get(i), R.drawable.rmb);
//				iMoneyList.add(rank);
				iPlayer.iRankList.get(RankType_Wealths).add(rank);
			}
			iMoneyAdapter.notifyDataSetChanged();
			iMoneyListView.setAdapter(iMoneyAdapter);
			iMoneyListView.setSelection(iMoneyLastItem - 1);
			if(iGameState == 0) {
				animationIn();
//				iMoneyListView.startLayoutAnimation();
				iGameState = -1;
				
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_Rank_Level:
			final MBRspRank level = (MBRspRank)msg.obj;
			iPlayer.iRspRank.put(RankType_Level, level);
			iMyLevel = level.iValue;
			setSelfInfo(level, RankType_Level);
			if(level.iMsg.length() > 0) {
				Tools.showSimpleToast(iGameMain, Gravity.CENTER, level.iMsg, Toast.LENGTH_LONG);
			}
			int count = level.iCount;
			iWardLStartIndex = level.iStartIndex;
			iWardTotalCount = level.iRankTotalCount;
			for(int i = 0; i < count; i++) {
				RankData rank = new RankData(iWardList.size() + 1, level.iRankUserID.get(i), level.iUserID, 
						level.iRankNick.get(i),  level.iRankPicName.get(i),
						level.iRankValue.get(i), level.iOnLine.get(i), level.iFriend.get(i), R.drawable.icon1);
//				iWardList.add(rank);
				iPlayer.iRankList.get(RankType_Level).add(rank);
			}
			iWardAdapter.notifyDataSetChanged();
			iWardListView.setAdapter(iWardAdapter);
			iWardListView.setSelection(iWardLastItem - 1);
//			iWardListView.startLayoutAnimation();
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_Rank_Victory:
			final MBRspRank victory = (MBRspRank)msg.obj;
			iPlayer.iRspRank.put(RankType_Vectory, victory);
			iMywin = victory.iValue;
			iWinLStartIndex = victory.iStartIndex;
			iWinTotalCount = victory.iRankTotalCount;
			setSelfInfo(victory, RankType_Vectory);
			if(victory.iMsg.length() > 0) {
				Tools.showSimpleToast(iGameMain, Gravity.CENTER, victory.iMsg, Toast.LENGTH_SHORT);
			}
			count = victory.iCount;
			for(int i = 0; i < count; i++) {
				RankData rank = new RankData(iWinList.size() + 1, victory.iRankUserID.get(i), victory.iUserID, 
						victory.iRankNick.get(i),  victory.iRankPicName.get(i),
						victory.iRankValue.get(i), victory.iOnLine.get(i), victory.iFriend.get(i), R.drawable.love);
//				iWinList.add(rank);
				iPlayer.iRankList.get(RankType_Vectory).add(rank);
			}
			iWinAdapter.notifyDataSetChanged();
			iWinListView.setAdapter(iWinAdapter);
			iWinListView.setSelection(iWinlLastItem - 1);
//			iWinListView.startLayoutAnimation();
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_AddFriend:
			MBRspAddFriend rspAddFriend = (MBRspAddFriend)msg.obj;
			if(rspAddFriend.iMsg.length() > 0) {
				Tools.showSimpleToast(iGameMain, Gravity.CENTER, rspAddFriend.iMsg, Toast.LENGTH_SHORT);
			}
			iPlayer.refreshRankList(rspAddFriend.iDesUserID, rspAddFriend.iAction);
			iMoneyAdapter.notifyDataSetChanged();
			iWardAdapter.notifyDataSetChanged();
			iWinAdapter.notifyDataSetChanged();
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_PIC:
			if(iWardAdapter != null) {
				iWardAdapter.notifyDataSetChanged();
				
			}
			if(iMoneyAdapter != null) {
				iMoneyAdapter.notifyDataSetChanged();
				
			}
			if(iWinAdapter != null) {
				iWinAdapter.notifyDataSetChanged();
				
			}
			Bitmap bitmap = MyArStation.iHttpDownloadManager.getImage(MyArStation.iPlayer.szLittlePicName);
			if(bitmap != null) {
				ivMyHead.setImageBitmap(bitmap);
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_SINGLE_PIC:
			if(iWardAdapter != null) {
				iWardAdapter.notifyDataSetChanged();
				
			}
			if(iMoneyAdapter != null) {
				iMoneyAdapter.notifyDataSetChanged();
				
			}
			if(iWinAdapter != null) {
				iWinAdapter.notifyDataSetChanged();
				
			}
			bitmap = MyArStation.iHttpDownloadManager.getImage(MyArStation.iPlayer.szLittlePicName);
			if(bitmap != null) {
				ivMyHead.setImageBitmap(bitmap);
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_TO_Rank:
			MyArStation.mGameMain.mainLayout.removeAllViews();
			mainRelativeLayout = (RelativeLayout) LayoutInflater.from(MyArStation.mGameMain.getApplicationContext()).inflate(R.layout.new_rank, null);
			MyArStation.mGameMain.mainLayout.addView(mainRelativeLayout);
			ivMyHead = (ImageView)mainRelativeLayout.findViewById(R.id.ivRankhead);
			tvMyNick = (TextView)mainRelativeLayout.findViewById(R.id.tvPlayerName);
			tvMyAttr = (TextView)mainRelativeLayout.findViewById(R.id.tvPlayerAttr);
			tvMyVictory = (TextView)mainRelativeLayout.findViewById(R.id.tvwin);
			tvMyLevel = (TextView)mainRelativeLayout.findViewById(R.id.tvLevel);
			tvMyWealths = (TextView)mainRelativeLayout.findViewById(R.id.tvMoney);
			ivIcon = (ImageView)mainRelativeLayout.findViewById(R.id.icon);
			
			iMyMoney = "";
			iMyLevel = "";
			iMywin = "";
			
			btWin = (Button) MyArStation.getInstance().findViewById(R.id.btnWin);
			btWin.setOnClickListener(this);
			btWard = (Button) MyArStation.getInstance().findViewById(R.id.btnAward);
			btWard.setOnClickListener(this);
			btMoney = (Button) MyArStation.getInstance().findViewById(R.id.btnTabMoney);
			btMoney.setOnClickListener(this);
			btMoney.setSelected(true);
			btReturn = (Button) MyArStation.getInstance().findViewById(R.id.btnRankReturn);
			btReturn.setOnClickListener(this);
			
			iMoneyListView = (ListView) MyArStation.getInstance().findViewById(R.id.lvMoney);
			iMoneyListView.setOnScrollListener(this);
			iMoneyList = iPlayer.iRankList.get(RankType_Wealths);
			iMoneyAdapter = new RankAdapter(MyArStation.getInstance(), iMoneyList);
			iMoneyListView.setAdapter(iMoneyAdapter);
			
			iWardListView = (ListView) MyArStation.getInstance().findViewById(R.id.lvAward);
			iWardListView.setOnScrollListener(this);
			iWardList = iPlayer.iRankList.get(RankType_Level);
			iWardAdapter = new RankAdapter(MyArStation.getInstance(), iWardList);
			iWardListView.setAdapter(iWardAdapter);
			
			iWinListView = (ListView) MyArStation.getInstance().findViewById(R.id.lvWin);
			iWinListView.setOnScrollListener(this);
			iWinList = iPlayer.iRankList.get(RankType_Vectory);
			iWinAdapter = new RankAdapter(MyArStation.getInstance(), iWinList);
			iWinListView.setAdapter(iWinAdapter);
			bitmap = MyArStation.iHttpDownloadManager.getImage(MyArStation.iPlayer.szLittlePicName);
			if(bitmap != null) {
				ivMyHead.setImageBitmap(bitmap);
			}
			initAnim();
//			iMoneyListView.setLayoutAnimation(controller);
//			iWardListView.setLayoutAnimation(controller);
//			iWinListView.setLayoutAnimation(controller);
			MBRspRank rank = (MBRspRank) iPlayer.iRspRank.get(iRankType);
			if(iPlayer.iRankList.get(iRankType).size() == 0 || rank == null) {
				requestRank(0);
			}
			else {
				MBRspRank rank1 = (MBRspRank) iPlayer.iRspRank.get(RankType_Wealths);
				if(rank1 != null) {
					iMyMoney = rank1.iValue;
					iMoneyTotalCount = rank1.iRankTotalCount;
					iMoneyStartIndex = rank1.iStartIndex;
					iMoneyListView.setSelection(iSelectedPos[RankType_Wealths]);
					iMoneyAdapter.notifyDataSetChanged();
				}
				MBRspRank rank2 = (MBRspRank) iPlayer.iRspRank.get(RankType_Level);
				if(rank2 != null) {
					iMyLevel = rank2.iValue;
					iWardTotalCount = rank2.iRankTotalCount;
					iWardLStartIndex = rank2.iStartIndex;
					iWardListView.setSelection(iSelectedPos[RankType_Level]);
					iWardAdapter.notifyDataSetChanged();
				}
				MBRspRank rank3 = (MBRspRank) iPlayer.iRspRank.get(RankType_Vectory);
				if(rank3 != null) {
					iMywin = rank3.iValue;
					iWinTotalCount = rank3.iRankTotalCount;
					iWinLStartIndex = rank3.iStartIndex;
					iWinListView.setSelection(iSelectedPos[RankType_Vectory]);
					iWinAdapter.notifyDataSetChanged();
				}
				setSelfInfo(rank, iRankType);
				switchTab(iRankType);
				animationIn();
				iGameState = -1;
				mainRelativeLayout.setVisibility(View.VISIBLE);
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_RANK_SEEFRIENDINF:
			{
				 int uid = (Integer) msg.obj;
				 switch (iRankType) {
				 case RankType_Wealths:
					 iSelectedPos[RankType_Wealths] = iMoneyListView.getFirstVisiblePosition();
					break;
				case RankType_Level:
					iSelectedPos[RankType_Level] = iWardListView.getFirstVisiblePosition();
					break;
				case RankType_Vectory:
					iSelectedPos[RankType_Vectory] = iWinListView.getFirstVisiblePosition();
					break;
				}
				iGameMain.mainLayout.removeAllViews();
				GameCanvas.getInstance().changeView(
						new FriendDetailScreen(uid, 
								iSelectedPos, 
								FriendDetailScreen.SCREENTYPE_RANK, 
								iRankType));
			}
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
			case RspMsg.MSG_RSP_RANK:
				MBRspRank rspRank = (MBRspRank)pMobileMsg.m_pMsgPara;
//				removeProgressDialog();
				if(rspRank == null) {
					break;
				}
				Tools.debug(rspRank.toString());
				if(rspRank.nResult == ResultCode.SUCCESS) {
					int rankType = rspRank.iRankType;
					switch (rankType) {
					case RankType_Wealths:
						messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_Rank_Wealths, rspRank);
						
						break;
					case RankType_Level:
						messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_Rank_Level, rspRank);
						
						break;
					case RankType_Vectory:
						messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_Rank_Victory, rspRank);
						
						break;

					default:
						break;
					}
				}
				else {
					messageEvent(rspRank.nResult, rspRank.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_TRACE:
				MBRspTrace rspTrace = (MBRspTrace)pMobileMsg.m_pMsgPara;
				if(rspTrace == null) {
					break;
				}
				Tools.debug(rspTrace.toString());
				if(rspTrace.iResult == ResultCode.SUCCESS) {
					RoomData pair = new RoomData(rspTrace.iRoomID, rspTrace.iTableID, rspTrace.iRoomType);
					messageEvent(MessageEventID.EMESSAGE_EVENT_TO_PROGRESS, pair);
				}
				else {
					messageEvent(rspTrace.iResult, rspTrace.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_ADD_FRIEND:
				removeProgressDialog();
				MBRspAddFriend rspAddFriend = (MBRspAddFriend)pMobileMsg.m_pMsgPara;
				if(rspAddFriend == null) {
					break;
				}
				Tools.debug(rspAddFriend.toString());
				int type = rspAddFriend.iResult;
				if(type == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_AddFriend, rspAddFriend);
				}
				else {
					messageEvent(type, rspAddFriend.iMsg);
				}
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

	/* 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btnTabMoney) {
			if(btMoney.isSelected()) {
				return;
			}
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			switchTab(RankType_Wealths);
		} else if (id == R.id.btnAward) {
			if(btWard.isSelected()) {
				return;
			}
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			switchTab(RankType_Level);
			if(iWardList != null && iWardList.size() == 0) {
				requestRank(0);
			}
		} else if (id == R.id.btnWin) {
			if(btWin.isSelected()) {
				return;
			}
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			switchTab(RankType_Vectory);
			if(iWinList != null && iWinList.size() == 0) {
				requestRank(0);
			}
		} else if (id == R.id.btnRankReturn) {
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			animationOut();
		} else {
		}
	}
	
	private void switchTab(int rankType) {
		switch (rankType) {
		case RankType_Wealths:
			btMoney.setSelected(true);
			btWard.setSelected(false);
			btWin.setSelected(false);
			
			iMoneyListView.setVisibility(View.VISIBLE);
			iWardListView.setVisibility(View.GONE);
			iWardListView.setVisibility(View.GONE);
			iRankType = rankType;
			tvMyAttr.setText(iMyMoney);
			ivIcon.setBackgroundResource(R.drawable.rmb);
			break;
		case RankType_Level:
			btMoney.setSelected(false);
			btWard.setSelected(true);
			btWin.setSelected(false);
			
			iMoneyListView.setVisibility(View.GONE);
			iWardListView.setVisibility(View.VISIBLE);
			iWinListView.setVisibility(View.GONE);
			iRankType = rankType;
			tvMyAttr.setText(iMyLevel);
			ivIcon.setBackgroundResource(R.drawable.icon1);
			break;
		case RankType_Vectory:
			btMoney.setSelected(false);
			btWard.setSelected(false);
			btWin.setSelected(true);
			
			iMoneyListView.setVisibility(View.GONE);
			iWardListView.setVisibility(View.GONE);
			iWinListView.setVisibility(View.VISIBLE);
			iRankType = rankType;
			tvMyAttr.setText(iMywin);
			ivIcon.setBackgroundResource(R.drawable.love);
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	private void requestRank(int aStartIndex) {
		if(iGameMain.iGameProtocol.requestRank(iRankType, aStartIndex)) {
			if(iGameState != 0) {
				showProgressDialog(R.string.Loading_String);
			}
		}
	}
	
	private void setSelfInfo(MBRspRank aMBRspRank, int type) {
		if(aMBRspRank == null) {
			return;
		}
		tvMyNick.setText(aMBRspRank.iNick);
		tvMyAttr.setText(aMBRspRank.iValue);
		if(type == RankType_Wealths) {
			ivIcon.setBackgroundResource(R.drawable.rmb);
		}
		else if(type == RankType_Level) {
			ivIcon.setBackgroundResource(R.drawable.icon1);
		}
		else if(type == RankType_Vectory) {
			ivIcon.setBackgroundResource(R.drawable.love);
		}
		if(aMBRspRank.iWealths != -1) {
			tvMyWealths.setText(iGameMain.getString(R.string.RankValue, aMBRspRank.iWealths));
		}
		else {
			tvMyWealths.setText(iGameMain.getString(R.string.RankValue_Null, "??"));
		}
		if(aMBRspRank.iLevel != -1) {
			tvMyLevel.setText(iGameMain.getString(R.string.RankValue, aMBRspRank.iLevel));
		}
		else {
			tvMyLevel.setText(iGameMain.getString(R.string.RankValue_Null, "??"));
		}
		if(aMBRspRank.iVictory != -1) {
			tvMyVictory.setText(iGameMain.getString(R.string.RankValue, aMBRspRank.iVictory));
		}
		else {
			tvMyVictory.setText(iGameMain.getString(R.string.RankValue_Null, "??"));
		}
	}
	/* 
	 * @see android.view.animation.Animation.AnimationListener#onAnimationStart(android.view.animation.Animation)
	 */
	@Override
	public void onAnimationStart(Animation animation) {
	}
	/* 
	 * @see android.view.animation.Animation.AnimationListener#onAnimationEnd(android.view.animation.Animation)
	 */
	@Override
	public void onAnimationEnd(Animation animation) {
	}
	/* 
	 * @see android.view.animation.Animation.AnimationListener#onAnimationRepeat(android.view.animation.Animation)
	 */
	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	/* 
	 * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int id = view.getId();
		if (id == R.id.lvMoney) {
			iSelectedPos[RankType_Wealths] = iMoneyListView.getFirstVisiblePosition();
			if (iMoneyLastItem == iMoneyAdapter.getCount() 
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
				int pageIndex = iMoneyStartIndex;
				Tools.debug("rank pageIndex" + pageIndex);
				if(iMoneyListView.getCount() < iMoneyTotalCount){
					requestRank(pageIndex);
				}
			}
		} else if (id == R.id.lvAward) {
			iSelectedPos[RankType_Level] = iWardListView.getFirstVisiblePosition();
			if (iWardLastItem == iWardAdapter.getCount() 
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
				int pageIndex = iWardLStartIndex;
				Tools.debug("myGoods pageIndex" + pageIndex);
				if(iWardListView.getCount() < iWardTotalCount){
					requestRank(pageIndex);
				}
			}
		} else if (id == R.id.lvWin) {
			iSelectedPos[RankType_Vectory] = iWinListView.getFirstVisiblePosition();
			if (iWinlLastItem == iWinAdapter.getCount() 
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
				int pageIndex = iWinLStartIndex;
				Tools.debug("BusinessDetail pageIndex" + pageIndex);
				if(iWinListView.getCount() < iWinTotalCount){
					requestRank(pageIndex);
				}
			}
		} else {
		}
	}

	private void clearCach() {
		for(int i = 0; i < iPlayer.iRankList.size(); i++) {
			iPlayer.iRankList.get(i).clear();
		}
		for(int i = 0; i < iPlayer.iRspRank.size(); i++) {
			iPlayer.iRspRank.clear();
		}
	}
	/* 
	 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.AbsListView, int, int, int)
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int id = view.getId();
		if (id == R.id.lvMoney) {
			iMoneyLastItem = firstVisibleItem + visibleItemCount;
		} else if (id == R.id.lvAward) {
			iWardLastItem = firstVisibleItem + visibleItemCount;
		} else if (id == R.id.lvWin) {
			iWinlLastItem = firstVisibleItem + visibleItemCount;
		} else {
		}
	}
}
