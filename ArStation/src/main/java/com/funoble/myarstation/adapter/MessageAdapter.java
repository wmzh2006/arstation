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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funoble.myarstation.data.cach.MessageData;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;


public class MessageAdapter extends BaseAdapter {
	/**
	* 内容中的at正则表达式
	*/
	public static final Pattern AT_PATTERN = Pattern.compile("@[\\u4e00-\\u9fa5\\w\\-]{2,12}");
	public static final Pattern SECRET_PATTERN = Pattern.compile("\\[私信\\]");
	private Context			mContext;

	private List<MessageData>		items;
	
	/**
	 * 当前选中状态的行
	 */
	private View			selectedRow	= null;
	
	private OnMessageListener listener = null;
	/**
	 * construct
	 */
	public MessageAdapter(Context c, List<MessageData> items) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_message_row, null);
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
		final MessageData e = items.get(position);
		LinearLayout selLayout = (LinearLayout) convertView.findViewById(R.id.llselectBtn);
		
		TextView name = ((TextView) convertView.findViewById(R.id.tvmsgNick));
		name.setText(e.iName);
		if(e.iType == 3) {//系统消息
			name.setTextColor(Color.RED);
		}
		else {
			if(e.uid == MyArStation.iPlayer.iUserID) {
				name.setTextColor(Color.BLUE);
			}
			else {
				name.setTextColor(0xff0099ff);
			}
		}
		SpannableString text = parse(e.iMsg);
		((TextView) convertView.findViewById(R.id.tvmsgContent)).setText(text);
		SpannableString time = parseSecret((e.iType == 1 ? e.iTime + "[私信]" : e.iTime)); 
		((TextView) convertView.findViewById(R.id.tvmsgTime)).setText(time);
		Bitmap bmp = MyArStation.iHttpDownloadManager.getImage(e.iLittleHead);
		if(bmp != null) {
			((ImageView)convertView.findViewById(R.id.ivmsgPic)).setImageBitmap(bmp);
		}
		TextView answer = (TextView) selLayout.findViewById(R.id.tvmsgAnswer);
		answer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (listener != null) listener.onAnswer(e);
			}
		});
		TextView delete = (TextView) selLayout.findViewById(R.id.tvmsgDelect);
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (listener != null) listener.onDelete(e);
			}
		});
		TextView accept = (TextView) selLayout.findViewById(R.id.tvmsgAccept);
		accept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (listener != null) listener.onAccept(e);
			}
		});
		TextView refuse = (TextView) selLayout.findViewById(R.id.tvmsgRefuse);
		refuse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (listener != null) listener.onRefuse(e);
			}
		});
		if(!e.self) {
			selLayout.setVisibility(View.GONE);
		}
		else {
			selLayout.setVisibility(View.VISIBLE);
			if(2 == e.iType) {//邀请好友
				refuse.setVisibility(View.VISIBLE);
				accept.setVisibility(View.VISIBLE);
				answer.setVisibility(View.GONE);
				delete.setVisibility(View.GONE);
			}
			else if(3 == e.iType) {//系统消息
				refuse.setVisibility(View.GONE);
				accept.setVisibility(View.GONE);
				answer.setVisibility(View.INVISIBLE);
				delete.setVisibility(View.VISIBLE);
			}
			else {
				refuse.setVisibility(View.GONE);
				accept.setVisibility(View.GONE);
				answer.setVisibility(View.VISIBLE);
				delete.setVisibility(View.VISIBLE);
			}
		}
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
//			selectedRow.setBackgroundColor(0x00ffffff); // 设置未选中的状态
//		}
//		selectedRow = v;
//		if (v != null) {
//			v.setBackgroundColor(0xe0CDC8B1); // 设置选中状态
//		}
	}
	
	public void setOnMessageListener(OnMessageListener l) {
		listener = l;
	}
	
	public interface OnMessageListener {
		public void onAnswer(Object o);
		public void onDelete(Object o);
		public void onAccept(Object o);
		public void onRefuse(Object o);
	}
	
	public SpannableString parse(String input) {
		SpannableString mSpannableString = new SpannableString(input);
		Matcher mAtMatcher = AT_PATTERN.matcher(input);
		while(mAtMatcher.find()){
			String atNameMatch = mAtMatcher.group();
			String subAtNameMatch = atNameMatch.substring(1, atNameMatch.length());
			mSpannableString.setSpan(new ForegroundColorSpan(0xff6699cc), mAtMatcher.start(), mAtMatcher.end(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
		return mSpannableString;
	}
	
	public SpannableString parseSecret(String input) {
		SpannableString mSpannableString = new SpannableString(input);
		Matcher mAtMatcher = SECRET_PATTERN.matcher(input);
		while(mAtMatcher.find()){
			mSpannableString.setSpan(new ForegroundColorSpan(0xffff9933), mAtMatcher.start(), mAtMatcher.end(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
		return mSpannableString;
	}
	
}
