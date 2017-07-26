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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funoble.myarstation.data.cach.ActivityRankData;
import com.funoble.myarstation.data.cach.CacheUtils;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.store.data.Goods;


public class ActivityRankAdapter extends BaseAdapter {

	private Context			mContext;

	private List<ActivityRankData>	items;
	
	/**
	 * 当前选中状态的行
	 */
	private View			selectedRow	= null;
	
	/**
	 * construct
	 */
	public ActivityRankAdapter(Context c, List<ActivityRankData> items) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_activity_rank_item, null);
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
		final ActivityRankData e = items.get(position);
		((TextView) convertView.findViewById(R.id.tvactivity_rank_row_uid)).setText(e.iRankUserID+"");
		((TextView) convertView.findViewById(R.id.tvactivity_rank_row_name)).setText(e.iRankNick);
		((TextView) convertView.findViewById(R.id.tvactivity_rank_row_num)).setText(e.iRankValue);
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
//			selectedRow.setBackgroundColor(0xffFBF9F2); // 设置未选中的状态
//		}
//		selectedRow = v;
//		if (v != null) {
//			v.setBackgroundColor(0xffFABE0F); // 设置选中状态
//		}
	}
}
