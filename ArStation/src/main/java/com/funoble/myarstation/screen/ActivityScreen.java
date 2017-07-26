/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: HelpScreen.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-4 下午05:34:11
 *******************************************************************************/
package com.funoble.myarstation.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.os.Messenger;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.funoble.myarstation.adapter.ActivityAdapter;
import com.funoble.myarstation.adapter.DetailAdapter;
import com.funoble.myarstation.adapter.RankAdapter;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.ActivityDetailData;
import com.funoble.myarstation.data.cach.ActivityRankData;
import com.funoble.myarstation.data.cach.RankData;
import com.funoble.myarstation.data.cach.RoomData;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.socket.protocol.MBReqActivityDetail;
import com.funoble.myarstation.socket.protocol.MBReqActivityList;
import com.funoble.myarstation.socket.protocol.MBRspActivityDetail;
import com.funoble.myarstation.socket.protocol.MBRspActivityEnroll;
import com.funoble.myarstation.socket.protocol.MBRspActivityList;
import com.funoble.myarstation.socket.protocol.MBRspActivityRank;
import com.funoble.myarstation.socket.protocol.MBRspRank;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.store.data.ActivityData;
import com.funoble.myarstation.store.data.StockData;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.LinearLayoutForListView;
import com.funoble.myarstation.view.ActivityRankDialog;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.MessageEventID;


public class ActivityScreen extends GameView implements OnClickListener{
	private int	iGameState = 0; //0进入
								//-1无效
								//1退出
	private Scene iSceneLoading = null;
	//资源
	private Project iUIPak = null;
	private static final int ACTIVITYTYPE_DAILY 	= 0;
	private static final int ACTIVITYTYPE_WEEKLY 	= 1;
	private static final int ACTIVITYTYPE_MONTHLY 	= 2;
	private static final int ACTIVITYTYPE_COMP 		= 3;
	
	private Button 		btnReturn;
	private Button 		btnDetailReturn;
	private Button 		btnRank;
	private boolean     isActivity = true;
	private boolean  	bReturn = false;
	
	private ListView			lvDaily;
	private List<ActivityData> 	lsDaily;
	private ActivityAdapter		atDaily;
	private int					lastItemDaily;
	
	private ListView			lvWeekly;
	private List<ActivityData> 	lsWeekly;
	private ActivityAdapter		atWeekly;
	private int					lastItemWeekly;
	
	private ListView			lvMonthly;
	private List<ActivityData> 	lsMonthly;
	private ActivityAdapter		atMonthly;
	private int					lastItemMonthly;
	
	private ListView			lvComp;
	private List<ActivityData> 	lsComp;
	private ActivityAdapter		atComp;
	private int					lastComp;
	
	private Button				btnDaily;
	private Button				btnWeekly;
	private Button				btnMonthly;
	private Button				btnComp;
	
	private LinearLayout		llActivityPage;
	private RelativeLayout		rlActivityDetail;
	
	private Context				context = MyArStation.mGameMain;
	
	private ListView			lvStock;
	private List<StockData> 	lsStock;
	private DetailAdapter		atStock;
	private LinearLayoutForListView lvtes;
	private int					iCurActivityType = ACTIVITYTYPE_DAILY;
	private TextView			tvActivityDetail;
	private int					selectActivityID;
	
	private HashMap<Integer, ActivityDetailData> hmAtivityDetail;
	private HashMap<Integer, ActivityDetailData> hmAtivityResult;
	private RelativeLayout mainRelativeLayout;
	private boolean activityPag = true;
	private ActivityRankDialog activityRankDialog;
	//动画
	AnimationSet set = new AnimationSet(true);
	Animation animation;
	LayoutAnimationController controller;
	
	Animation putinAnimation;
	Animation putoutAnimation;
	Animation putRightOutinAnimation;
	Animation putLeftInAnimation;
	
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
		
		putinAnimation = AnimationUtils.loadAnimation(context, R.anim.push_right_in);
		putoutAnimation = AnimationUtils.loadAnimation(context, R.anim.push_left_out);
		putRightOutinAnimation = AnimationUtils.loadAnimation(context, R.anim.push_right_out);
		putLeftInAnimation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
	}
	
	private void animationIn() {
		llActivityPage.startAnimation(putinAnimation);
		llActivityPage.setVisibility(View.VISIBLE);
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
				messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
			}
		});
	}
	
	private void animationLeftIn() {
		llActivityPage.startAnimation(putLeftInAnimation);
		llActivityPage.setVisibility(View.VISIBLE);
	}
	
	private void animationRightOut() {
		llActivityPage.startAnimation(putRightOutinAnimation);
		putRightOutinAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				animationInDetail();
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				llActivityPage.setVisibility(View.INVISIBLE);
//				animationInDetail();
			}
		});
	}
	
	private void animationInDetail() {
		rlActivityDetail.startAnimation(putinAnimation);
		rlActivityDetail.setVisibility(View.VISIBLE);
		
	}
	
	private void animationOutDetail() {
		rlActivityDetail.startAnimation(putoutAnimation);
		putoutAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				switchTab(true, R.string.activity_list_title);
				animationLeftIn();
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				rlActivityDetail.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	/**
	 * construct
	 */
	public ActivityScreen() {
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#init()
	 */
	@Override
	public void init() {
		iSceneLoading = LoadLoading();
		messageEvent(MessageEventID.EMESSAGE_EVENT_TO_ACTIVIY);
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#releaseResource()
	 */
	@Override
	public void releaseResource() {
		iSceneLoading = null;
		iUIPak = null;
		if(activityRankDialog != null) {
			activityRankDialog.dismiss();
			activityRankDialog = null;
		}
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
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(activityPag) {
//				messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
				if(!bReturn) {
					bReturn = !bReturn;
					animationOut();
				}
			}
//			else {
//				if(!activityPag) {
//					activityPag = true;
//					animationOutDetail();
//				}
//			}
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
		switch (msg.what) {
		case ResultCode.ERR_ACTIVITY_LIST_END://活动列表结束
		case ResultCode.ERR_ACTIVITY_INVALED://活动ID无效
		case ResultCode.ERR_ACTIVITY_STOP_SIGNUP://已经停止报名
		case ResultCode.ERR_ACTIVITY_ALREADY_SIGNUP://已经报名
		case ResultCode.ERR_ACTIVITY_NOT_START://活动还没有开始
		case ResultCode.ERR_ACTIVITY_ALREADY_END://活动已经结束
		case ResultCode.ERR_ACTIVITY_TYPE_INVALED:		//活动类型无效
		case ResultCode.ERR_ACTIVITY_NO_PLACES:		//已经没有报名名额
		case ResultCode.ERR_ACTIVITY_FB_NOT_ENOUGH:		//报名凤智币不足
		case ResultCode.ERR_ACTIVITY_GB_NOT_ENOUGH:		//报名金币不足
		case ResultCode.ERR_ACTIVITY_LEVEL_NOT_ENOUGH:		//报名级数不足
			removeProgressDialog();
			MBRspActivityEnroll rspActivityEnroll = (MBRspActivityEnroll)msg.obj;
			if(rspActivityEnroll.iMsg != "") {
				Tools.showSimpleToast(context, Gravity.CENTER, rspActivityEnroll.iMsg, Toast.LENGTH_LONG);
			}
			requestEnroll(rspActivityEnroll, false);
			break;
		case ResultCode.ERR_ACTIVITY_NOT_SIGNUP:		//没有报名
			removeProgressDialog();
			rspActivityEnroll = (MBRspActivityEnroll)msg.obj;
			if(rspActivityEnroll.iMsg != "") {
				Tools.showSimpleToast(context, Gravity.CENTER, rspActivityEnroll.iMsg, Toast.LENGTH_LONG);
			}
			requestEnroll(rspActivityEnroll, true);
			break;
		case MessageEventID.EMESSAGE_EVENT_REQUEST_ACIIVITY_DETAIL:
			ActivityData pair = (ActivityData)msg.obj;
			if(pair == null) {
				return;
			}
			ActivityDetailData activityDetailData = hmAtivityDetail.get(pair.acitivityID);
			if(activityDetailData != null) {
				setActivityDetail(activityDetailData, R.string.activity_detail_title);
			}
			else {
				requestActivitDetail(pair.acitivityID, 0);
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_REQ_ACIIVITY_RESULT:
			pair = (ActivityData)msg.obj;
			if(pair == null) {
				return;
			}
			activityDetailData = hmAtivityResult.get(pair.acitivityID);
			if(activityDetailData != null) {
				setActivityDetail(activityDetailData, R.string.activity_detail_result);
			}
			else {
				requestActivitDetail(pair.acitivityID, 1);
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_DETAIL:
			removeProgressDialog();
			MBRspActivityDetail rspActivityDetail = (MBRspActivityDetail)msg.obj;
			activityDetailData = new ActivityDetailData();
			activityDetailData.activityID = rspActivityDetail.iActivityID;
			activityDetailData.content = rspActivityDetail.iContent;
			for(int i = 0; i < rspActivityDetail.iCount; i++) {
				StockData data = new StockData(rspActivityDetail.iActivityRank.get(i), rspActivityDetail.iActivityReward.get(i));
				activityDetailData.list.add(data);
			}
			hmAtivityDetail.put(activityDetailData.activityID, activityDetailData);
			setActivityDetail(activityDetailData, R.string.activity_detail_title);
			activityPag = false;
			break;
		case MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_RESUOLT:
			removeProgressDialog();
			rspActivityDetail = (MBRspActivityDetail)msg.obj;
			activityDetailData = new ActivityDetailData();
			activityDetailData.activityID = rspActivityDetail.iActivityID;
			activityDetailData.content = rspActivityDetail.iContent;
			for(int i = 0; i < rspActivityDetail.iCount; i++) {
				StockData data = new StockData(rspActivityDetail.iActivityRank.get(i), rspActivityDetail.iActivityReward.get(i));
				activityDetailData.list.add(data);
			}
			hmAtivityResult.put(activityDetailData.activityID, activityDetailData);
			setActivityDetail(activityDetailData, R.string.activity_detail_result);
			activityPag = false;
			break;
		case MessageEventID.EMESSAGE_EVENT_REQ_ACIIVITY_ENROLL:
			pair = (ActivityData)msg.obj;
			if(pair == null) {
				return;
			}
//			if(pair.activityStatus == 1) {
				requestActivitEnroll(pair.acitivityID, pair.activityStatus);
//			}
//			else if(pair.activityStatus == 2) {
//				requestActivitEnroll(pair.acitivityID, pair.activityStatus);
//			}
			break;
		case MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_DAILY:
			removeProgressDialog();
			final MBRspActivityList rspActivityList = (MBRspActivityList) msg.obj;
			if(rspActivityList.iMsg.length() > 0) {
				Tools.showSimpleToast(context, Gravity.CENTER, rspActivityList.iMsg, Toast.LENGTH_SHORT);
			}
//			iMyWeathls = wealths.iValue;
			int Count = rspActivityList.iCount;
			for(int i = 0; i < Count; i++) {
				ActivityData data = new ActivityData(rspActivityList.iID.get(i), rspActivityList.iName.get(i), rspActivityList.iStartTime.get(i)+"\n"+rspActivityList.iEndTime.get(i), rspActivityList.iPeopleCount.get(i), 
						rspActivityList.iState.get(i), rspActivityList.iFB.get(i), rspActivityList.iGB.get(i), rspActivityList.iLevel.get(i), rspActivityList.iStateText.get(i));
				lsDaily.add(data);
			}
			atDaily.notifyDataSetChanged();
			lvDaily.setAdapter(atDaily);
			lvDaily.setSelection(lastItemDaily - 1);
			lvDaily.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					
					if (lastItemDaily == atDaily.getCount() 
							&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
						int pageIndex = rspActivityList.iStartIndex;
						Tools.debug("rank pageIndex" + pageIndex);
						if(lvDaily.getCount() < rspActivityList.iTotalCount){
							requestActivity(pageIndex);
						}
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					lastItemDaily = firstVisibleItem + visibleItemCount;
				}
			});
//			lvDaily.startLayoutAnimation();
			if(iGameState == 0) {
				animationIn();
				iGameState = -1;
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_WEEKLY:
			removeProgressDialog();
			final MBRspActivityList weekly = (MBRspActivityList) msg.obj;
			if(weekly.iMsg.length() > 0) {
				Tools.showSimpleToast(context, Gravity.CENTER, weekly.iMsg, Toast.LENGTH_SHORT);
			}
//			iMyWeathls = wealths.iValue;
			Count = weekly.iCount;
			for(int i = 0; i < Count; i++) {
				ActivityData data = new ActivityData(weekly.iID.get(i), weekly.iName.get(i), weekly.iStartTime.get(i)+"\n"+weekly.iEndTime.get(i), 
						weekly.iPeopleCount.get(i), weekly.iState.get(i), weekly.iFB.get(i), weekly.iGB.get(i), weekly.iLevel.get(i), weekly.iStateText.get(i));
				lsWeekly.add(data);
			}
			atWeekly.notifyDataSetChanged();
			lvWeekly.setAdapter(atWeekly);
			lvWeekly.setSelection(lastItemWeekly - 1);
			lvWeekly.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					
					if (lastItemWeekly == atWeekly.getCount() 
							&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
						int pageIndex = weekly.iStartIndex;
						if(lvWeekly.getCount() < weekly.iTotalCount){
							requestActivity(pageIndex);
						}
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					lastItemWeekly = firstVisibleItem + visibleItemCount;
				}
			});
//			lvWeekly.startLayoutAnimation();
//			if(iGameState == 0) {
//				animationIn();
//				iGameState = -1;
//			}
			break;
		case MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_MONTHLY:
			removeProgressDialog();
			final MBRspActivityList monthly = (MBRspActivityList) msg.obj;
			if(monthly.iMsg.length() > 0) {
				Tools.showSimpleToast(context, Gravity.CENTER, monthly.iMsg, Toast.LENGTH_SHORT);
			}
//			iMyWeathls = wealths.iValue;
			Count = monthly.iCount;
			for(int i = 0; i < Count; i++) {
				ActivityData data = new ActivityData(monthly.iID.get(i), monthly.iName.get(i), monthly.iStartTime.get(i)+"\n"+monthly.iEndTime.get(i), 
						monthly.iPeopleCount.get(i), monthly.iState.get(i), monthly.iFB.get(i), monthly.iGB.get(i), monthly.iLevel.get(i), monthly.iStateText.get(i));
				lsMonthly.add(data);
			}
			atMonthly.notifyDataSetChanged();
			lvMonthly.setAdapter(atMonthly);
			lvMonthly.setSelection(lastItemMonthly - 1);
			lvMonthly.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					
					if (lastItemMonthly == atMonthly.getCount() 
							&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
						int pageIndex = monthly.iStartIndex;
						if(lvMonthly.getCount() < monthly.iTotalCount){
							requestActivity(pageIndex);
						}
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					lastItemMonthly = firstVisibleItem + visibleItemCount;
				}
			});
//			lvMonthly.startLayoutAnimation();
			break;
		case MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_COMP:
			removeProgressDialog();
			final MBRspActivityList comp = (MBRspActivityList) msg.obj;
			if(comp.iMsg.length() > 0) {
				Tools.showSimpleToast(context, Gravity.CENTER, comp.iMsg, Toast.LENGTH_SHORT);
			}
//			iMyWeathls = wealths.iValue;
			Count = comp.iCount;
			for(int i = 0; i < Count; i++) {
				ActivityData data = new ActivityData(comp.iID.get(i), comp.iName.get(i), comp.iStartTime.get(i)+"\n"+comp.iEndTime.get(i),
						comp.iPeopleCount.get(i), comp.iState.get(i), comp.iFB.get(i), comp.iGB.get(i), comp.iLevel.get(i), comp.iStateText.get(i));
				lsComp.add(data);
			}
			atComp.notifyDataSetChanged();
			lvComp.setAdapter(atComp);
			lvComp.setSelection(lastComp - 1);
			lvComp.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					
					if (lastComp == atComp.getCount() 
							&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
						int pageIndex = comp.iStartIndex;
						if(lvComp.getCount() < comp.iTotalCount){
							requestActivity(pageIndex);
						}
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					lastComp = firstVisibleItem + visibleItemCount;
				}
			});
//			lvComp.startLayoutAnimation();
			break;
		case MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_ENROLL:
			removeProgressDialog();
			rspActivityEnroll = (MBRspActivityEnroll)msg.obj;
			if(rspActivityEnroll.iMsg.length() > 0) {
				Tools.showSimpleToast(context, Gravity.CENTER, rspActivityEnroll.iMsg, Toast.LENGTH_SHORT);
			}
			requestEnroll(rspActivityEnroll, false);
			if(rspActivityEnroll.iActivityStatus == 2) {
				RoomData pair2 = new RoomData(rspActivityEnroll.iActivityRoomID, -1, rspActivityEnroll.iRoomType);
				requestLoginRoom(pair2);
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_TO_ACTIVIY:
			MyArStation.mGameMain.mainLayout.removeAllViews();
			mainRelativeLayout = (RelativeLayout) LayoutInflater.from(MyArStation.mGameMain.getApplicationContext()).inflate(R.layout.new_activity, null);
			MyArStation.mGameMain.mainLayout.addView(mainRelativeLayout);
			mainRelativeLayout.setVisibility(View.VISIBLE);
			btnReturn = (Button)mainRelativeLayout.findViewById(R.id.btnactivity_return);
			btnReturn.setOnClickListener(this);
			btnDetailReturn = (Button)mainRelativeLayout.findViewById(R.id.btnactivity_detail_return);
			btnDetailReturn.setOnClickListener(this);
			btnRank = (Button)mainRelativeLayout.findViewById(R.id.btnactivity_detail_rank);
			btnRank.setOnClickListener(this);
			
			llActivityPage = (LinearLayout)mainRelativeLayout.findViewById(R.id.llactivity_page);
			rlActivityDetail = (RelativeLayout)mainRelativeLayout.findViewById(R.id.rlactivity_detail);
			llActivityPage.setVisibility(View.INVISIBLE);
			rlActivityDetail.setVisibility(View.INVISIBLE);
			
			lvDaily = (ListView)MyArStation.mGameMain.findViewById(R.id.lvdaily);
			lsDaily = new ArrayList<ActivityData>();
//			for(int i = 0; i < 10; i++) {
//				ActivityData data = new ActivityData(i, "每日活动", "开始:10:00\n结束:23:00", 2000, 0,
//						0, 4, 5, "");
//				lsDaily.add(data);
//			}
			atDaily = new ActivityAdapter(context, lsDaily);
			lvDaily.setAdapter(atDaily);
			
			lvWeekly = (ListView)MyArStation.mGameMain.findViewById(R.id.lvweekly);
			lsWeekly = new ArrayList<ActivityData>();
//			for(int i = 0; i < 10; i++) {
//				ActivityData data = new ActivityData(i, "每周活动每周活动", "活动开始时间：10:00\n活动结束时间：23:00", 1000, 3,
//						0, 0, 5, "公布结果");
//				lsWeekly.add(data);
//			}
			atWeekly = new ActivityAdapter(context, lsWeekly);
			lvWeekly.setAdapter(atWeekly);
			
			lvMonthly = (ListView)MyArStation.mGameMain.findViewById(R.id.lvmonthly);
			lsMonthly = new ArrayList<ActivityData>();
//			for(int i = 0; i < 10; i++) {
//				ActivityData data = new ActivityData(i, "每月活动", "活动开始时间：10:00\n活动结束时间：23:00", 1000, 2);
//				lsMonthly.add(data);
//			}
			atMonthly = new ActivityAdapter(context, lsMonthly);
			lvMonthly.setAdapter(atMonthly);
			
			lvComp = (ListView)MyArStation.mGameMain.findViewById(R.id.lvcompetition);
			lsComp = new ArrayList<ActivityData>();
//			for(int i = 0; i < 10; i++) {
//				ActivityData data = new ActivityData(i, "争霸赛", "活动开始时间：10:00\n活动结束时间：23:00", 1000, 3);
//				lsComp.add(data);
//			}
			atComp = new ActivityAdapter(context, lsComp);
			lvComp.setAdapter(atComp);
			
			btnDaily = (Button)MyArStation.mGameMain.findViewById(R.id.btnDaily);
			btnDaily.setOnClickListener(this);
			btnWeekly = (Button)MyArStation.mGameMain.findViewById(R.id.btnWeekly);
			btnWeekly.setOnClickListener(this);
			btnMonthly = (Button)MyArStation.mGameMain.findViewById(R.id.btnMonthly);
			btnMonthly.setOnClickListener(this);
			btnComp = (Button)MyArStation.mGameMain.findViewById(R.id.btnCompetition);
			btnComp.setOnClickListener(this);
			
			hmAtivityDetail = new HashMap<Integer, ActivityDetailData>();
			hmAtivityResult = new HashMap<Integer, ActivityDetailData>();
			lvtes = (LinearLayoutForListView)MyArStation.mGameMain.findViewById(R.id.llrankandreward);
			lsStock = new ArrayList<StockData>();
			activityDetailData = new ActivityDetailData();
			activityDetailData.activityID = 0;
			activityDetailData.content = "活动名称：哈哈哈，1000Q币大奖\n开始时间:10:00\n结束时间:23:00 \t 报名费：2000金币";
			for(int i = 0; i < 10; i++) {
				StockData data = new StockData(i+"名", "QQ币"+i*100);
				activityDetailData.list.add(data);
			}
			hmAtivityDetail.put(activityDetailData.activityID, activityDetailData);
			atStock = new DetailAdapter(context, lsStock);
			lvtes.setAdapter(atStock);
			tvActivityDetail = (TextView)MyArStation.mGameMain.findViewById(R.id.tvtime);
			btnDaily.setSelected(true);
			lvDaily.setVisibility(View.VISIBLE);
			initAnim();
//			lvDaily.setLayoutAnimation(controller);
//			lvWeekly.setLayoutAnimation(controller);
//			lvMonthly.setLayoutAnimation(controller);
//			lvComp.setLayoutAnimation(controller);
			requestActivity(0);
			break;
		case MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_RANK:
			MBRspActivityRank rspActivityRank = (MBRspActivityRank) msg.obj;
			activityRankDialog = new ActivityRankDialog(MyArStation.getInstance());
			StringBuffer content = new StringBuffer();
			content.append("活动id: ").append(+rspActivityRank.iActivityID).append("\n")
			.append("参加活动的人数: ").append(rspActivityRank.iActivityPeopleCount).append("\n")
			.append("获奖人数: ").append(rspActivityRank.iCount).append("\n");
			List<ActivityRankData> list = new ArrayList<ActivityRankData>();
			int count = rspActivityRank.iCount;
			for(int i = 0; i < count; i++) {
				ActivityRankData data = new ActivityRankData(rspActivityRank.iRankUserID.get(i), rspActivityRank.iRankNick.get(i), rspActivityRank.iRankValue.get(i));
				list.add(data);
			}
			activityRankDialog.setDatas(list, content.toString(), rspActivityRank.iActivityID);
			content = null;
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
			switch (aMsgID) {
			case RspMsg.MSG_RSP_ACTIVITY_LIST:
				MBRspActivityList rspActivityList = (MBRspActivityList)pMobileMsg.m_pMsgPara;
				if(rspActivityList == null) {
					return;
				}
				Tools.debug(rspActivityList.toString());
				int result = rspActivityList.nResult;
				if(result ==  ResultCode.SUCCESS) {
					int type = rspActivityList.iActivityType;
					switch (type) {
					case ACTIVITYTYPE_DAILY:
						messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_DAILY, rspActivityList);
						break;
					case ACTIVITYTYPE_WEEKLY:
						messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_WEEKLY, rspActivityList);
						break;
					case ACTIVITYTYPE_MONTHLY:
						messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_MONTHLY, rspActivityList);
						break;
					case ACTIVITYTYPE_COMP:
						messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_COMP, rspActivityList);
						break;
						
					default:
						break;
					}
				}
				else {
					messageEvent(-20000, rspActivityList);
				}
				break;
			case RspMsg.MSG_RSP_ACTIVITY_DETAIL:
				MBRspActivityDetail rspActivityDetail = (MBRspActivityDetail)pMobileMsg.m_pMsgPara;
				if(rspActivityDetail == null) {
					return;
				}
				Tools.debug(rspActivityDetail.toString());
				if(rspActivityDetail.nResult == ResultCode.SUCCESS) {
					int type = rspActivityDetail.iAction;
					switch (type) {
					case 0:
						messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_DETAIL, rspActivityDetail);
						break;
					case 1:
						messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_RESUOLT, rspActivityDetail);
						break;
						
					default:
						break;
					}
				}
				else {
					
				}
				break;
			case RspMsg.MSG_RSP_ACTIVITY_ENROLL:
				MBRspActivityEnroll rspActivityEnroll = (MBRspActivityEnroll)pMobileMsg.m_pMsgPara;
				if(rspActivityEnroll == null) {
					return;
				}
				Tools.debug(rspActivityEnroll.toString());
				if(rspActivityEnroll.nResult == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_RSP_ACIIVITY_ENROLL, rspActivityEnroll);
				}
				else {
					messageEvent(rspActivityEnroll.nResult, rspActivityEnroll);
				}
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
			default:
				break;
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
		if (id == R.id.btnactivity_return) {
			//			messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			animationOut();
		} else if (id == R.id.btnactivity_detail_return) {
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			animationOutDetail();
		} else if (id == R.id.btnDaily) {
			if(lvDaily.getVisibility() == View.VISIBLE) {
				return;
			}
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			btnDaily.setSelected(true);
			btnWeekly.setSelected(false);
			btnMonthly.setSelected(false);
			btnComp.setSelected(false);
			lvDaily.setVisibility(View.VISIBLE);
			lvWeekly.setVisibility(View.GONE);
			lvMonthly.setVisibility(View.GONE);
			lvComp.setVisibility(View.GONE);
			iCurActivityType = ACTIVITYTYPE_DAILY;
			if(lsDaily.size() <= 0) {
				requestActivity(0);
			}
		} else if (id == R.id.btnWeekly) {
			if(lvWeekly.getVisibility() == View.VISIBLE) {
				return;
			}
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			btnDaily.setSelected(false);
			btnWeekly.setSelected(true);
			btnMonthly.setSelected(false);
			btnComp.setSelected(false);
			lvDaily.setVisibility(View.GONE);
			lvWeekly.setVisibility(View.VISIBLE);
			lvMonthly.setVisibility(View.GONE);
			lvComp.setVisibility(View.GONE);
			iCurActivityType = ACTIVITYTYPE_WEEKLY;
			if(lsWeekly.size() <= 0) {
				requestActivity(0);
			}
		} else if (id == R.id.btnMonthly) {
			if(lvMonthly.getVisibility() == View.VISIBLE) {
				return;
			}
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			btnDaily.setSelected(false);
			btnWeekly.setSelected(false);
			btnMonthly.setSelected(true);
			btnComp.setSelected(false);
			lvDaily.setVisibility(View.GONE);
			lvWeekly.setVisibility(View.GONE);
			lvMonthly.setVisibility(View.VISIBLE);
			lvComp.setVisibility(View.GONE);
			iCurActivityType = ACTIVITYTYPE_MONTHLY;
			if(lsMonthly.size() <= 0) {
				requestActivity(0);
			}
		} else if (id == R.id.btnCompetition) {
			if(lvComp.getVisibility() == View.VISIBLE) {
				return;
			}
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			btnDaily.setSelected(false);
			btnWeekly.setSelected(false);
			btnMonthly.setSelected(false);
			btnComp.setSelected(true);
			lvDaily.setVisibility(View.GONE);
			lvWeekly.setVisibility(View.GONE);
			lvMonthly.setVisibility(View.GONE);
			lvComp.setVisibility(View.VISIBLE);
			iCurActivityType = ACTIVITYTYPE_COMP;
			if(lsComp.size() <= 0) {
				requestActivity(0);
			}
		} else if (id == R.id.btnactivity_detail_rank) {
			MyArStation.iGameProtocol.requestActivityRank(selectActivityID);
		} else {
		}
	}
	
	public void requestActivity(int aStartIndex) {
		if(MyArStation.iGameProtocol.requestActivity(iCurActivityType, aStartIndex)) {
			if(iGameState != 0) {
				showProgressDialog(R.string.Loading_String, true);
			}
		}
	}
	
	public void setListViewHeightBasedOnChildren(ListView listView) {
		//获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter(); 
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);  //计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
			Tools.debug("listAdapter.getCount()" + listAdapter.getCount() + " listItem.getMeasuredHeight() = " + listItem.getMeasuredHeight());
		}
		
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = (totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))) / 4;
		Tools.debug("params.height " + params.height + " listView.getDividerHeight() = " + listView.getDividerHeight());
		//listView.getDividerHeight()获取子项间分隔符占用的高度
		//params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}
	
	private void requestActivitEnroll(int aActivityID, int aAction) {
		if(MyArStation.getInstance().iGameProtocol.requestActivityEnroll(aActivityID, aAction)) {
			showProgressDialog(R.string.Loading_String);
		}
	}
	
	private void requestActivitDetail(int aActivityID, int aAction) {
		if(MyArStation.getInstance().iGameProtocol.requestActivityDetail(aActivityID, aAction)) {
			showProgressDialog(R.string.Loading_String);
		}
	}
	
	private void setActivityDetail(ActivityDetailData aData, int resid) {
		selectActivityID = aData.activityID;
		tvActivityDetail.setText(aData.content);
		lsStock.clear();
		lsStock.addAll(aData.list);
		atStock.notifyDataSetChanged();
		lvtes.setAdapter(atStock);
		switchTab(false, resid);
		animationRightOut();
	}
	
	private void switchTab(boolean activityPage, int resid) {
		activityPag = activityPage;
//		llActivityPage.setVisibility(activityPage ? View.VISIBLE : View.GONE);
//		rlActivityDetail.setVisibility(activityPage ? View.GONE : View.VISIBLE);
		if(!activityPage) {
			int useid[] = MyArStation.getInstance().getResources().getIntArray(R.array.id);
			for(int i = 0; i < useid.length; i++) {
				if(MyArStation.iPlayer.iUserID == useid[i]) {
					btnRank.setVisibility(View.VISIBLE);
					break;
				}
			}
			((ScrollView)rlActivityDetail.findViewById(R.id.svdetail)).scrollTo(0, 0);
			setTitleNane(resid);
		}
	}
	
	private void setTitleNane(int resid) {
//		if(rlActivityDetail.getVisibility() == View.VISIBLE) {
			TextView tv = (TextView)rlActivityDetail.findViewById(R.id.tvTitle);
			tv.setText(resid);
//		}
	}
	private void requestLoginRoom(RoomData pair) {
		messageEvent(MessageEventID.EMESSAGE_EVENT_TO_PROGRESS, pair);
	}
	
	private ActivityData freshActivityList(List<ActivityData> list ,ActivityAdapter apAdapter, MBRspActivityEnroll rsp) {
		Iterator<ActivityData> iterator = list.iterator();
		while(iterator.hasNext()) {
			ActivityData data = iterator.next();
			if(rsp.iActivityID == data.acitivityID) {
				data.activityStatus = rsp.iActivityStatus;
				data.activitynumber = rsp.iPeopleCount;
				data.activityStatusText = rsp.iStateText;
				apAdapter.notifyDataSetChanged();
				return data;
			}
		}
		return null;
	}
	
	private void requestEnroll(MBRspActivityEnroll rspActivityEnroll, boolean Enroll) {
		switch (rspActivityEnroll.iActivityType) {
		case 0:
			ActivityData activityData = freshActivityList(lsDaily, atDaily, rspActivityEnroll);
			if(activityData != null && Enroll) {
				atDaily.activityData = activityData;
				atDaily.showConfirm();
			}
			break;
		case 1:
			activityData = freshActivityList(lsWeekly, atWeekly, rspActivityEnroll);
			if(activityData != null && Enroll) {
				atWeekly.activityData = activityData;
				atWeekly.showConfirm();
			}
			break;
		case 2:
			activityData = freshActivityList(lsMonthly, atMonthly, rspActivityEnroll);
			if(activityData != null && Enroll) {
				atMonthly.activityData = activityData;
				atMonthly.showConfirm();
			}
			break;
		case 3:
			activityData = freshActivityList(lsComp, atComp, rspActivityEnroll);
			if(activityData != null && Enroll) {
				atComp.activityData = activityData;
				atComp.showConfirm();
			}
			break;
		}
	}
}
