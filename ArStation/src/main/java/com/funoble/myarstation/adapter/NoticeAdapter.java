/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: StringAdapter.java 
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
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.funoble.myarstation.game.R;


public class NoticeAdapter extends BaseAdapter {

	private Context			mContext;

	private List<String>		items;
	
	/**
	 * 当前选中状态的行
	 */
	private View			selectedRow	= null;
	
	/**
	 * construct
	 */
	public NoticeAdapter(Context c, List<String> items) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_notice_row, null);
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
		final String e = items.get(position);
		((TextView) convertView.findViewById(R.id.tvNotice)).setText(e);
		return convertView;
	}

	/**
	 * 设置当前选中状态的行View
	 * 
	 * @param v
	 */
	public void setSelectedRow(View v) {
//		if (selectedRow == v) {
//			return;
//		}
//		if(v.getId() == R.id.ivArrow) {
//			
//		}
//		if (selectedRow != null) {
//			selectedRow.setBackgroundColor(0xffFBF9F2); // 设置未选中的状态
//		}
//		selectedRow = v;
//		if (v != null) {
//			v.setBackgroundColor(0xffFABE0F); // 设置选中状态
//		}
	}
	
	public void setItems(List<String> items) {
		this.items = items;
		notifyDataSetChanged();
	}
}
