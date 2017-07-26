/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: GoodsAdapter.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-29 下午04:42:08
 *******************************************************************************/
package com.funoble.myarstation.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.CacheUtils;
import com.funoble.myarstation.data.cach.RankData;
import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.screen.FriendInfoScreen;
import com.funoble.myarstation.screen.RankScreen;
import com.funoble.myarstation.socket.protocol.MBRspLogin;
import com.funoble.myarstation.socket.protocol.ProtocolType.ActionID;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.store.data.Goods;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.MessageEventID;


public class RankAdapter extends BaseAdapter {

	private Context			mContext;

	private List<RankData>	items;
	
	/**
	 * 当前选中状态的行
	 */
	private View			selectedRow	= null;
	
	/**
	 * construct
	 */
	public RankAdapter(Context c, List<RankData> items) {
		mContext = c;
		this.items = items;
	}

	/* 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return items.size();
	}

	/* 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	/* 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_rank_row, null);
//			convertView.setBackgroundColor(0xffFBF9F2);
			convertView.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						setSelectedRow(v);
					}
					else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP)
					{
						setSelectedRow(null);
					}
					return false;
				}
			});
		}
		
		final RankData e = items.get(position);
		((TextView) convertView.findViewById(R.id.tvRankNum)).setText(e.iRank+"");
		((TextView) convertView.findViewById(R.id.tvRankNick)).setText(e.iRankNick);
		((TextView) convertView.findViewById(R.id.tvRankValue)).setText(e.iRankValue+"");
//		setStatus(e, convertView);
		if(e.iRankUserID == e.iSelfID) {
//			convertView.setBackgroundResource(R.drawable.list_selected);
		}
		else {
//			convertView.setBackgroundResource(R.drawable.list_normal);
		}
		final Button AddFriend = ((Button)convertView.findViewById(R.id.btnAddFriend));
		if(e.iRankUserID == e.iSelfID) {
			AddFriend.setVisibility(View.INVISIBLE);
		}
//		else if(e.iFriend != RankScreen.NEXUS_SELF && e.iFriend != RankScreen.NEXUS_FRIEND) {
//			AddFriend.setText(R.string.Friend);
//			AddFriend.setOnClickListener(new Buttonclick(false, e.iRankUserID));
//			AddFriend.setVisibility(View.VISIBLE);
//		}
		else {
			AddFriend.setText(R.string.BeFriend);
			AddFriend.setOnClickListener(new Buttonclick(true, e.iRankUserID));
			AddFriend.setVisibility(View.VISIBLE);
		}
		Button trace = ((Button)convertView.findViewById(R.id.btntrace));
		if(e.iRankUserID != e.iSelfID) {
			setStatus(e, trace);			
		}
		else {
			((Button)convertView.findViewById(R.id.btntrace)).setVisibility(View.INVISIBLE);
		}
		
		Bitmap img = MyArStation.iHttpDownloadManager.getImage(e.iRankPicName);
		if (img != null) {
			((ImageView) convertView.findViewById(R.id.ivPic)).setImageBitmap(img);
		}
		else {
//			((ImageView) convertView.findViewById(R.id.ivPic)).setImageResource(R.drawable.defaultitem);
		}
		((ImageView) convertView.findViewById(R.id.icon)).setBackgroundResource(e.iIconID);
		trace.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					if(MyArStation.iGameProtocol != null && v.isEnabled()) {
						if(MyArStation.iGameProtocol.requestTrace(e.iRankUserID)) {
							GameView view = MyArStation.getInstance().getGameCanvas().getCurrentView();
							if(view != null) {
								view.showProgressDialog(MyArStation.getInstance().getString(R.string.Loading_String), true);
							}
						}
						
					}
				}
			});
		
		return convertView;
	}

	class Buttonclick implements OnClickListener {
		boolean beFriend = false;
		int uID;
		public Buttonclick(boolean beFriend, int uID) {
			this.beFriend = beFriend;
			this.uID = uID;
		}
		
		/* 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			if(!beFriend) {
				if(MyArStation.iGameProtocol != null && v.isEnabled()) {
					if(requestAddFriend(uID)) {
						GameView view = MyArStation.getInstance().getGameCanvas().getCurrentView();
						if(view != null) {
							view.showProgressDialog(MyArStation.getInstance().getString(R.string.Loading_String), true);
						}
					}
				}
			}
			else {
				Message msg = new Message();
				msg.what = MessageEventID.EMESSAGE_EVENT_RANK_SEEFRIENDINF;
				msg.obj = uID;
				MyArStation.getInstance().iHandler.sendMessage(msg);
			}
		}
		
	}
	/**
	 * 设置当前选中状态的行View
	 * 
	 * @param v
	 */
	public void setSelectedRow(View v) {
		if (selectedRow == v) {
			return;
		}
//		if (selectedRow != null) {
//			selectedRow.setBackgroundResource(R.drawable.list_normal); // 设置未选中的状态
//		}
//		selectedRow = v;
//		if (v != null) {
//			v.setBackgroundResource(R.drawable.list_selected); // 设置选中状态
//		}
	}
	
	public void setStatus(RankData rankdata, View view) {
		Button trace = (Button)view;
		trace.setVisibility(View.INVISIBLE);
		trace.setEnabled(true);
		switch (rankdata.iOnLine) {
		case RankScreen.OFFLINE:
//			((ImageView)view.findViewById(R.id.ivStatus)).setBackgroundResource(R.drawable.offline);
//			trace.setBackgroundResource(R.drawable.new_com_btn_select);
			trace.setText(R.string.Rank_OffLine);
			trace.setEnabled(false);
			break;
		case RankScreen.ONLINE:
//			((ImageView)view.findViewById(R.id.ivStatus)).setBackgroundResource(R.drawable.online);
//			trace.setBackgroundResource(R.drawable.new_com_btn_select);
			trace.setText(R.string.Rank_OnLine);
			trace.setEnabled(false);
			break;
		case RankScreen.ONGAME:
//			((ImageView)view.findViewById(R.id.ivStatus)).setBackgroundResource(R.drawable.ongame);
//			trace.setBackgroundResource(R.drawable.new_btn_general);
			trace.setText(R.string.Trance);
			trace.setEnabled(true);
			break;

		default:
			break;
		}
	}
	
	private boolean requestAddFriend(int iUserId) {
		return MyArStation.iGameProtocol.requestAddFriend(iUserId, ActionID.AddFriend);
	}
	
}
