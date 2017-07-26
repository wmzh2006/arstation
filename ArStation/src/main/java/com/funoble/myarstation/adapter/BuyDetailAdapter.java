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

import com.funoble.myarstation.data.cach.CacheUtils;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.store.data.Goods;


public class BuyDetailAdapter extends BaseAdapter {

	private Context			mContext;

	private List<Goods>	items;
	
	/**
	 * 当前选中状态的行
	 */
	private View			selectedRow	= null;
	
	/**
	 * construct
	 */
	public BuyDetailAdapter(Context c, List<Goods> items) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_buydetail_row, null);
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
		final Goods e = items.get(position);
		((TextView) convertView.findViewById(R.id.tvName)).setText(e.iName);
		((TextView) convertView.findViewById(R.id.tvPrice)).setText(e.iPrice+"");
		((TextView) convertView.findViewById(R.id.tvDes)).setText(e.iDesc);
		((TextView) convertView.findViewById(R.id.tvBuyTime)).setText(e.iBuyTime);
		Bitmap img = MyArStation.iHttpDownloadManager.getGoodsIcon(e.iIconID);
		((ImageView) convertView.findViewById(R.id.ivPic)).setImageBitmap(img);
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
