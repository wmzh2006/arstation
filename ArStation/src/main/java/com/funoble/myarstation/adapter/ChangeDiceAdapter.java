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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.store.data.ChangeDiceData;
import com.funoble.myarstation.view.GeneralDialog;
import com.funoble.myarstation.view.MessageEventID;


public class ChangeDiceAdapter extends BaseAdapter {

	private Context			mContext;

	private List<ChangeDiceData>	items;
	
	private OnChangeDiceListener ls;
	
	/**
	 * 当前选中状态的行
	 */
	private View			selectedRow	= null;
	
	public GeneralDialog 		dialog;
	/**
	 * construct
	 */
	public ChangeDiceAdapter(Context c, List<ChangeDiceData> items) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_changedice_item, null);
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
		final ChangeDiceData e = items.get(position);
		((ImageView) convertView.findViewById(R.id.ibchange_dice1)).setImageResource(e.dicesResID[0]);
		((ImageView) convertView.findViewById(R.id.ibchange_dice2)).setImageResource(e.dicesResID[1]);
		((ImageView) convertView.findViewById(R.id.ibchange_dice3)).setImageResource(e.dicesResID[2]);
		((ImageView) convertView.findViewById(R.id.ibchange_dice4)).setImageResource(e.dicesResID[3]);
		((ImageView) convertView.findViewById(R.id.ibchange_dice5)).setImageResource(e.dicesResID[4]);
		((ImageView) convertView.findViewById(R.id.ibchange_dice6)).setImageResource(e.dicesResID[5]);
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
//			selectedRow.setBackgroundResource(R.drawable.list_normal); // 设置未选中的状态
		}
		selectedRow = v;
		if (v != null) {
//			v.setBackgroundResource(R.drawable.list_selected); // 设置选中状态
		}
	}
	
	public void setOnChangeDiceListener(OnChangeDiceListener ls) {
		this.ls = ls;
	}
	
	class Buttonclick implements OnClickListener {
		private ChangeDiceData	e;
		
		public Buttonclick(ChangeDiceData e) {
			this.e = e;
		}

		@Override
		public void onClick(View v) {
			if(ls != null) ls.OnClick(e.diceType);
		}
	}
	
	public interface OnChangeDiceListener {
		public void OnClick(int type);
	}
}
