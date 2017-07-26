/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: GoodsAdapter.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-29 下午04:42:08
 *******************************************************************************/
package com.funoble.myarstation.adapter;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.store.data.ActivityData;
import com.funoble.myarstation.view.GeneralDialog;
import com.funoble.myarstation.view.MessageEventID;
import com.funoble.myarstation.view.GeneralDialog.OnGeneralDialogListener;


public class ActivityAdapter extends BaseAdapter {

	private Context			mContext;

	private List<ActivityData>	items;
	
	/**
	 * 当前选中状态的行
	 */
	private View			selectedRow	= null;
	
	public GeneralDialog 		dialog;
	
	public ActivityData 		activityData;
	/**
	 * construct
	 */
	public ActivityAdapter(Context c, List<ActivityData> items) {
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

	public ActivityData getItemByID(int ID) {
		Iterator<ActivityData> iterator = items.iterator();
		while(iterator.hasNext()) {
			if(ID == iterator.next().acitivityID) {
				return (ActivityData) iterator;
			}
		}
		return null;
	}
	/* 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_activity_list_item, null);
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
		ActivityData e = items.get(position);
		((TextView) convertView.findViewById(R.id.tvactivity_list_row_name)).setText(e.activityName);
		((TextView) convertView.findViewById(R.id.tvactivity_list_row_time)).setText(e.activityTime);
		((TextView) convertView.findViewById(R.id.tvactivity_list_row_number)).setText(e.activitynumber+"");
		((TextView) convertView.findViewById(R.id.tvactivity_list_row_status)).setText(e.activityStatusText);
		Button button = ((Button) convertView.findViewById(R.id.btnactivity_list_row));
		if(e.activityStatus == 1) {
			button.setVisibility(View.VISIBLE);
			button.setText("报名");
		}
		else if(e.activityStatus == 2) {
			button.setVisibility(View.VISIBLE);
			button.setText("参与");
		}
		else if(e.activityStatus == 3) {
			button.setVisibility(View.VISIBLE);
			button.setText("结果");
		}
		else {
			button.setVisibility(View.VISIBLE);
			button.setText("详情");
		}
		button.setOnClickListener(new Buttonclick(e, MessageEventID.EMESSAGE_EVENT_REQ_ACIIVITY_ENROLL)); 
		convertView.setOnClickListener(new Buttonclick(e, MessageEventID.EMESSAGE_EVENT_REQUEST_ACIIVITY_DETAIL));
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
	
	class Buttonclick implements OnClickListener {
		private ActivityData	e;
		private int	iEventID;
		
		public Buttonclick(ActivityData e, int aEventID) {
			this.e = e;
			this.iEventID = aEventID;
		}

		@Override
		public void onClick(View v) {
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			if(iEventID == MessageEventID.EMESSAGE_EVENT_REQ_ACIIVITY_ENROLL) {
				if(e.activityStatus == 1) {
					activityData = e;
					showConfirm();
				}
				else if(e.activityStatus == 3) {
					Message msg = new Message();
					msg.what = MessageEventID.EMESSAGE_EVENT_REQ_ACIIVITY_RESULT;
					msg.obj = e;
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					MyArStation.getInstance().iHandler.sendMessage(msg);
				}
				else if(e.activityStatus == 2){
					Message msg = new Message();
					msg.what = iEventID;
					msg.obj = e;
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					MyArStation.getInstance().iHandler.sendMessage(msg);
				}
				else {
					Message msg = new Message();
					msg.what = MessageEventID.EMESSAGE_EVENT_REQUEST_ACIIVITY_DETAIL;
					msg.obj = e;
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					MyArStation.getInstance().iHandler.sendMessage(msg);
				}
			}
			else {
				Message msg = new Message();
				msg.what = MessageEventID.EMESSAGE_EVENT_REQUEST_ACIIVITY_DETAIL;
				msg.obj = e;
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
				MyArStation.getInstance().iHandler.sendMessage(msg);
			}
		}
	}
	
	private String StatusToString(int status) {
		switch (status) {
		case 0:
			return mContext.getString(R.string.activity_status_signup);
		case 1:
			return mContext.getString(R.string.activity_status_getready);
		case 2:
			return mContext.getString(R.string.activity_status_underway);
		case 3:
			return mContext.getString(R.string.activity_status_end);
		case 4:
			return mContext.getString(R.string.activity_status_result);
		case 5:
			return mContext.getString(R.string.activity_status_enrolled);
		}
		return "";
	}
	
	public void showConfirm() {
		String str = "";
		if(activityData.aFB > 0) {
			str = MyArStation.mGameMain.getString(R.string.activity_prompt_contentFB, activityData.activityName, activityData.aFB);
		}
		else if(activityData.aGB > 0){
			str = MyArStation.mGameMain.getString(R.string.activity_prompt_contentGB, activityData.activityName, activityData.aGB);
		}
		else {
			str = MyArStation.mGameMain.getString(R.string.activity_prompt_contentGB, activityData.activityName, activityData.aGB);
		}
		if(dialog == null) {
			dialog = new GeneralDialog(MyArStation.mGameMain);
			dialog.setConfirmText(MyArStation.getInstance().getString(R.string.activity_enroll));
			dialog.setCancelText(MyArStation.getInstance().getString(R.string.Return_HomeScreen_Cancel));
			dialog.setOnGeneralDialogListener(new OnGeneralDialogListener() {
				
				@Override
				public void OnConfirm() {
					Message msg = new Message();
					msg.what = MessageEventID.EMESSAGE_EVENT_REQ_ACIIVITY_ENROLL;
					msg.obj = activityData;
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					MyArStation.getInstance().iHandler.sendMessage(msg);
				}
				
				@Override
				public void OnCancel() {
				}
			});
		}
		dialog.Show(str);
//		 dialog = new AlertDialog.Builder(GameMain.mGameMain).setTitle(R.string.activity_prompt).setMessage(
//				 str).setCancelable(false)
//				.setPositiveButton(R.string.activity_enroll, new DialogInterface.OnClickListener()
//				{
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						Message msg = new Message();
//						msg.what = MessageEventID.EMESSAGE_EVENT_REQ_ACIIVITY_ENROLL;
//						msg.obj = e;
//						MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//						GameMain.getInstance().iHandler.sendMessage(msg);
//					}
//				}).setNegativeButton(R.string.Return_HomeScreen_Cancel, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						
//					}
//				}).create();
//		dialog.show();
	}
}
