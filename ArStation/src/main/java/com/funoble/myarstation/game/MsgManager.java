/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: ShowMessageTips.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2013-4-25 下午04:02:19
 *******************************************************************************/
package com.funoble.myarstation.game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.common.Util;
import com.funoble.myarstation.game.R;

import android.graphics.Paint;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MsgManager implements OnClickListener {
	ViewGroup mInflater;
//	private  RelativeLayout maiLayout;
	private  TextView mainTip;
	private  Timer timer = new Timer();
	private  ArrayList<MsgTask> msgTasks = new ArrayList<MsgTask>();
	private boolean isShow = false;
	private boolean isPause = false;
	private boolean isClean = false;
	private MsgTask currMsgTask;
	//动画
	private Animation putinAnimation = AnimationUtils.loadAnimation(MyArStation.getInstance().getApplicationContext(), R.anim.push_up_in);
	private Animation putoutAnimation = AnimationUtils.loadAnimation(MyArStation.getInstance().getApplicationContext(), R.anim.push_up_out);
	
	private Handler handler = new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 0://next
				removeMsgTask(currMsgTask);
				if(isShow) {
					currMsgTask = nextMsgTask();
					if(currMsgTask != null) {
						SpannableString text = Util.parseSecret(currMsgTask.getMsg().getContent());
						mainTip.setText(text);
					}
					else {
						isShow = false;
						Visibility(isShow);
					}
				}
				break;
			case 1://start
				if(!isShow) {
					if(!msgTasks.isEmpty()) {
						currMsgTask = msgTasks.get(0);
						if(currMsgTask == null) break;
						SpannableString text = Util.parseSecret(currMsgTask.getMsg().getContent());
						mainTip.setText(text);
						startMsgTask(currMsgTask);
						isShow = true;
						Visibility(isShow);
					}
					else {
						isShow = false;
						Visibility(isShow);
					}
				}
				break;
			case 2://clean
				stopMsgTask(currMsgTask);
				currMsgTask = null;
				msgTasks.clear();
				if(isShow) {
					isShow = false;
					Visibility(isShow);
				}
				break;
			case 3://pause
				stopMsgTask(currMsgTask);
				removeMsgTask(currMsgTask);
				currMsgTask = null;
				if(isShow) {
					isShow = false;
					Visibility(isShow);
				}
				isPause = true;
				break;
			default:
				break;
			}
			return false;
		}
	});
	
	public MsgManager() {
		mainTip = (TextView) MyArStation.mGameMain.findViewById(R.id.tvMaintips);
//		mainTip.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);  
		mainTip.setOnClickListener(this);
	}
	
	public void show() {
		Message msg = new Message();
		msg.what = 1;
		handler.sendMessage(msg);
	}

	private void Visibility(boolean show) {
		mainTip.setAnimation(isShow ? putinAnimation : putoutAnimation);
		mainTip.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}
	
	public void pause(){
		Message msg = new Message();
		msg.what = 3;
		handler.sendMessage(msg);
	}
	
	public void upDate(String msg) {
		if(msg == null || msg.length() <= 0) {
			return;
		}
		int size = (msg.length() >> 2);
		if(size >= 10) {
			size = 10;
		}
		else if(size <= 3) {
			size = 3;
		}
		Tools.debug("MsgManager::upDate()1::size = " 
				+ size);
		upDate(msg, size);
	}
	
	public void upDate(String msg, int secondDel) {
		Tools.debug("MsgManager::upDate()2::start");
		if(msg == null || msg.length() <= 0 || isClean) {
			return;
		}
		Tools.debug("MsgManager::upDate()2::secondDel = " 
				+ secondDel);
		putMsgTask(new MsgTask(new Msg(msg), secondDel));
		show();
		Tools.debug("MsgManager::upDate()2::end");
	}

	public void setClean(boolean clean) {
		isClean = clean;
	}
	
	private void putMsgTask(MsgTask msgTask) {
		synchronized (msgTasks) {
			msgTasks.add(msgTask);
		}
	}

	private void startMsgTask(MsgTask msgTask) {
		if(msgTask != null) {
			timer.schedule(msgTask, msgTask.getDeltaTime());
		}
	}
	
	private MsgTask nextMsgTask() {
		MsgTask msgTask = null;
		if(!msgTasks.isEmpty() && isShow) {
			msgTask = msgTasks.get(0);
			timer.schedule(msgTask, msgTask.getDeltaTime());
		}
		return msgTask;
	}
	
	private boolean removeMsgTask(MsgTask msg) {
		synchronized (msgTasks) {
			if(msg != null) {
				msgTasks.remove(msg);
			}
		}
		return true;
	}
	
	private boolean stopMsgTask(MsgTask msgTask) {
		if (msgTask != null) {
			msgTask.cancel();
			return true;
		}
		return false;
	}
	
	public void stopMsgTask() {
		Message msg = new Message();
		msg.what = 2;
		handler.sendMessage(msg);
	}
	
	public class MsgTask extends TimerTask {
		private long deltaTime; // 时间增量，及任务执行等待时间
		private Msg msg;
		public MsgTask(Msg msg, long deltaTime) {
			super();
			this.msg = msg;
			this.deltaTime = deltaTime * 1000;
		}

		public long getDeltaTime() {
			return deltaTime;
		}

		public void setDeltaTime(long deltaTime) {
			this.deltaTime = deltaTime;
		}

		public Msg getMsg() {
			return msg;
		}

		public void setMsgKey(Msg msg) {
			this.msg = msg;
		}

		@Override
		public void run() {// 等待时间到了以后，就执行
			if(isClean) return;
			Message msg = new Message();
			msg.what = 0;
			msg.obj = this;
			handler.sendMessage(msg);
			this.cancel();
		}

		/* 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object o) {
			return msg.equals(((MsgTask)o).msg);
		}

	}

	public class Msg {
		private String content;
		
		public Msg(String content){
			this.content = content;
		}
		
		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Msg) {
				Msg msg = (Msg) obj;

				return this.content == msg.getContent();
			} else {
				return false;
			}
		}
	}

	/* 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tvMaintips) {
			pause();
		}
	}
	
}
