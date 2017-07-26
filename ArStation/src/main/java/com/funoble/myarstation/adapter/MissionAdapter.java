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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;


public class MissionAdapter extends BaseAdapter {

	private Context			mContext;

	private List<MissionItem>	items;
	
	private OnSubmitMissionListener submitMissionListen;
	/**
	 * 当前选中状态的行
	 */
	private View			selectedRow	= null;
	
	/**
	 * construct
	 */
	public MissionAdapter(Context c, List<MissionItem> items) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_mission_child_item_layout, null);
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
		
		MissionItem item = items.get(position);
        ChildViewHolder holder = new ChildViewHolder();
        holder.mIcon = (ImageView) convertView.findViewById(R.id.img);
        Bitmap bitmap = MyArStation.iHttpDownloadManager.getGoodsIcon(item.picName);
        holder.mIcon.setImageBitmap(bitmap);
        holder.mChildName = (TextView) convertView.findViewById(R.id.item_name);
        holder.mChildName.setText(parseSecret(item.name));
        holder.mDetail = (TextView) convertView.findViewById(R.id.item_detail);
        holder.mDetail.setText(item.detail);
        holder.mbButton = (Button) convertView.findViewById(R.id.item_mission_get);
        holder.mbButton.setOnClickListener(new ButtonOnClickListener(item));
        if(item.missionState == 0) {
        	holder.mbButton.setVisibility(View.INVISIBLE);
//        	convertView.setBackgroundColor(Color.TRANSPARENT);
        }
        else {
        	holder.mbButton.setVisibility(View.VISIBLE);
//        	convertView.setBackgroundColor(Color.argb(0x54,0xf5,0xf5,0xdc));
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
	
	public void setOnSubmitListen(OnSubmitMissionListener l) {
		submitMissionListen = l;
	}

    private class ChildViewHolder {
        ImageView mIcon;
        TextView mChildName;
        TextView mDetail;
        Button mbButton;
    }

    public interface OnSubmitMissionListener {
    	public void onSubmitMission(MissionItem item);
    }


    public class ButtonOnClickListener implements OnClickListener {
    	private MissionItem item;
    	
    	public ButtonOnClickListener(MissionItem item) {
    		this.item = item;
    	}
		/* 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			if(submitMissionListen != null) submitMissionListen.onSubmitMission(item);
		}
    	
	}
    
    public static SpannableString parseSecret(String input) {
	    Pattern SECRET_PATTERN = Pattern.compile("(（.+）|\\(.+\\))");
		SpannableString mSpannableString = new SpannableString(input);
		Matcher mAtMatcher = SECRET_PATTERN.matcher(input);
		while(mAtMatcher.find()){
			mSpannableString.setSpan(new ForegroundColorSpan(Color.GREEN), mAtMatcher.start(), mAtMatcher.end(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
		return mSpannableString;
	}
}
