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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.funoble.myarstation.data.cach.RestPwdData;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;


public class RestPswdAdapter extends BaseAdapter {

	private Context			mContext;

	private List<RestPwdData>	items;
	
	/**
	 * 当前选中状态的行
	 */
	private View			selectedRow	= null;
	private ItemListen     Itemlisten;
	/**
	 * construct
	 */
	public RestPswdAdapter(Context c) {
		mContext = c;
		this.items = null;
	}
	/**
	 * construct
	 */
	public RestPswdAdapter(Context c, List<RestPwdData> items) {
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
	public RestPwdData getItem(int position) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_room_limit_row, null);
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
		final RestPwdData e = items.get(position);
		((TextView) convertView.findViewById(R.id.tvroomlimitrow)).setText(e.value);
		convertView.setOnClickListener(new Buttonclick(e));
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
		if (selectedRow != null) {
//			((LinearLayout)selectedRow.findViewById(R.id.rlroomName)).setBackgroundResource(R.drawable.select_room_list);// 设置选中状态
		}
		selectedRow = v;
		if (v != null) {
//			((LinearLayout)v.findViewById(R.id.rlroomName)).setBackgroundResource(R.drawable.select_room_pressed); // 设置未选中的状态
		}
	}
	
	class Buttonclick implements OnClickListener {
		RestPwdData data;
		public Buttonclick(RestPwdData data) {
			this.data = data;
		}

		@Override
		public void onClick(View v) {
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			if(Itemlisten != null) Itemlisten.onClick(data);
		}
	}
	
	public void setItemListen(ItemListen l){
		this.Itemlisten = l;
	}
	
	public interface ItemListen {
		public void onClick(Object o);
	}
}
