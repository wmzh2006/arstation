/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: FriendListAdapter.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-29 下午04:42:08
 *******************************************************************************/
package com.funoble.myarstation.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Message;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.funoble.myarstation.data.cach.CacheUtils;
import com.funoble.myarstation.data.cach.FriendListInfo;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.screen.RankScreen;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ImageUtil;
import com.funoble.myarstation.view.MessageEventID;


public class SearchPeopleListAdapter extends BaseAdapter {

	private Context			mContext;

	private List<FriendListInfo>	items;
	
	/**
	 * 当前选中状态的行
	 */
	public int pos = 0;
	/**
	 * 当前选中状态的行
	 */
	private View			selectedRow	= null;
	
	/**
	 * construct
	 */
	public SearchPeopleListAdapter(Context c, List<FriendListInfo> items) {
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
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_friend_list_row, null);
//			convertView.setBackgroundColor(0xffFBF9F2);
			convertView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						setSelectedRow(v);
					}
					else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
						setSelectedRow(null);
					}
					return false;
				}
			});
		}
		final FriendListInfo e = items.get(position);
		((TextView) convertView.findViewById(R.id.tvFriendNick)).setText(e.iFriendNick);
		RatingBar r = ((RatingBar)convertView.findViewById(R.id.drunk0));
		r.setRating(e.iUpdate);
		r.setVisibility(View.INVISIBLE);
		r.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		Button btnDetail = (Button)convertView.findViewById(R.id.btnDetail);
		btnDetail.setText("添加好友");
		btnDetail.setOnClickListener(new Buttonclick(e.iFriendUserID, position));
		Bitmap img = MyArStation.iHttpDownloadManager.getImage(e.iFriendPicName);
		if (img != null) {
			((ImageView) convertView.findViewById(R.id.ivPic)).setImageBitmap(img);
		}
		else {
		}
		return convertView;
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
	
	class Buttonclick implements OnClickListener {
		private int	id;

		public Buttonclick(int id, int apos) {
			this.id = id;
			pos = apos;
		}

		@Override
		public void onClick(View v) {
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
//			setSelectedRow(v);
			MyArStation.iGameProtocol.requestAddFriend(id, 0);
		}

	}
	
	public void setSelectedRow(int userid, int pos) {
		Pair<Integer, Integer> pair = new Pair<Integer, Integer>(userid, pos);
		messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RequestFriendInfo, pair);
	}
	
	private void messageEvent(int aEventID, Object aMsg) {
		Message tMes = new Message();
		tMes.what = aEventID;
		tMes.obj = aMsg;
		MyArStation.getInstance().iHandler.sendMessage(tMes);
	}
	
	private String getStatus(int aStatus) {
		String temp = "";
		switch (aStatus) {
		case RankScreen.OFFLINE:
			temp = MyArStation.getInstance().getString(R.string.offline);
			break;
		case RankScreen.ONLINE:
			temp = MyArStation.getInstance().getString(R.string.online);
			break;
		case RankScreen.ONGAME:
			temp = MyArStation.getInstance().getString(R.string.ongame);
			break;
		default:
			break;
		}
		return temp;
	}
}
