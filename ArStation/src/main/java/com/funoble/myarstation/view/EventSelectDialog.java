package com.funoble.myarstation.view;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.SpannableString;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.screen.FriendInfoScreen.ShakeCountDownTimer;
import com.funoble.myarstation.socket.protocol.MBNotifyFriendEvent;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;

/**
 * <p>
 * 
 * </p>
 */
/**
 * description 
 * version 1.0
 * author 
 * update 2012-11-16 下午05:02:35 
 */
public class EventSelectDialog extends Dialog implements
		android.view.View.OnClickListener, Callback {
	private final int BTN_SINGEL = 0;
	private final int BTN_DOUBLE = 1;
	private int   btnType = BTN_DOUBLE;
	
	private ViewGroup mLayout;

	private TextView tvTip;

	private Button btnSubmit;

	private Button btnCancel;
	private OnGeneralDialogListener dialogListener;
	
	private SelectCountDownTimer countDownTimer;
	private String cancelText;
	private String confirmText;
	private MBNotifyFriendEvent  event;
	
	private Handler handler = new Handler(this);
	/**
	 * @param context
	 */
	public EventSelectDialog(Context context) {
		super(context, R.style.MSGDialogStyle);

		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_buywine, null);

		setContentView(mLayout);
		ScrollView sView = (ScrollView) mLayout.findViewById(R.id.svbuywine);
		tvTip = (TextView) sView.findViewById(R.id.tvContent);
		btnSubmit = (Button) mLayout.findViewById(R.id.btnbuy);
		btnSubmit.setOnClickListener(this);
		btnCancel = (Button) mLayout.findViewById(R.id.btncancel);
		btnCancel.setOnClickListener(this);
		this.setCancelable(false);
		this.setCanceledOnTouchOutside(false);
		this.getWindow().setWindowAnimations(R.style.putupinanimation);
	}

	private void ininBtn(int btnType){
		this.btnType = btnType;
		int width =  LayoutParams.WRAP_CONTENT;
		int height = LayoutParams.WRAP_CONTENT;//Tools.dip2px(GameMain.mGameMain.getApplicationContext(), 40);
		RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(width, height);
		if(btnType == BTN_DOUBLE) {
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			lp.leftMargin = Tools.dip2px(MyArStation.mGameMain.getApplicationContext(), 60);
			btnSubmit.setLayoutParams(lp);
			RelativeLayout.LayoutParams lp2=new RelativeLayout.LayoutParams(width, height);
			lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			lp2.rightMargin = Tools.dip2px(MyArStation.mGameMain.getApplicationContext(), 60);
			btnCancel.setLayoutParams(lp2);
			btnCancel.setVisibility(View.VISIBLE);
		}
		else {
			btnCancel.setVisibility(View.GONE);
			lp.addRule(RelativeLayout.CENTER_IN_PARENT);
			btnSubmit.setLayoutParams(lp);
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Dialog#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		Display d = getWindow().getWindowManager().getDefaultDisplay();
		LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
		p.height = (int) (d.getHeight() * 1.0); // 高度设置为屏幕的1.0
		p.width = (int) (d.getWidth() * 1.0); // 宽度设置为屏幕的0.8
//		p.alpha = 0.5f; //设置本身透明度
		p.dimAmount = 0.5f; //设置黑暗度
//
		getWindow().setAttributes(p); // 设置生效
		getWindow().setGravity(Gravity.TOP); // 设置靠右对齐
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
		int id = v.getId();
		if (id == R.id.btnbuy) {
			if(dialogListener != null) dialogListener.OnConfirm(event);
			btnSubmit.setText(confirmText);
		} else if (id == R.id.btncancel) {
			if(dialogListener != null) dialogListener.OnCancel(event);
			btnCancel.setText(cancelText);
		} else {
		}
		dismiss();
	}

	public void Show(String text) {
		ininBtn(BTN_DOUBLE);
		tvTip.setText(text);
		if(!this.isShowing())this.show();
	}
	
	public void ShowSingle(String text) {
		ininBtn(BTN_SINGEL);
		tvTip.setText(text);
		if(!this.isShowing())this.show();
	}
	
	public void Show(SpannableString text) {
		tvTip.setText(text);
		this.show();
	}
	
	public void setConfirmText(String text) {
		if(text == null) {
			return;
		}
		confirmText = text;
		btnSubmit.setText(text);
	}
	
	public void setCancelText(String text) {
		if(text == null) {
			return;
		}
		cancelText = text;
		btnCancel.setText(text);
	}
	
	public interface OnGeneralDialogListener {
		public void OnConfirm(MBNotifyFriendEvent event);
		public void OnCancel(MBNotifyFriendEvent event);
	}
	
	public void setOnGeneralDialogListener(OnGeneralDialogListener l) {
		dialogListener = l;
	}
	
	public class SelectCountDownTimer extends CountDownTimer {

		/**
		 * construct
		 * @param millisInFuture
		 * @param countDownInterval
		 */
		public SelectCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			
		}

		/* 
		 * @see android.os.CountDownTimer#onTick(long)
		 */
		@Override
		public void onTick(long millisUntilFinished) {
			messageEvent(MessageEventID.EMESSAGE_EVENT_UPDATE_SHAKE_TIME, (int)(millisUntilFinished / 1000));
		}

		/* 
		 * @see android.os.CountDownTimer#onFinish()
		 */
		@Override
		public void onFinish() {
			messageEvent(MessageEventID.EMESSAGE_EVENT_COUNTDOWNTIME_UP);
		}
		
	}
	
	public String formatTime(int millisUntilFinished) {
		String time = "";
		int resultsec = millisUntilFinished;
//		time = resultsec >= 10 ? resultsec + "" : ("0" + resultsec);
		return resultsec + "";
	}
	
	private void messageEvent(int aEventID) {
		Message tMes = new Message();
		tMes.what = aEventID;
		handler.sendMessage(tMes);
	}
	
	private void messageEvent(int aEventID, Object aMsg) {
		Message tMes = new Message();
		tMes.what = aEventID;
		tMes.obj = aMsg;
		handler.sendMessage(tMes);
	}

	/* 
	 * @see android.os.Handler.Callback#handleMessage(android.os.Message)
	 */
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MessageEventID.EMESSAGE_EVENT_UPDATE_SHAKE_TIME:
			if(btnType == BTN_DOUBLE) {  
				String text = cancelText +"("+ formatTime((Integer) msg.obj)+")";
				btnCancel.setText(text);
			}
			else {
				String text = confirmText +"("+ formatTime((Integer) msg.obj)+")";
				btnSubmit.setText(text);
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_COUNTDOWNTIME_UP:
			if(btnType == BTN_DOUBLE) {  
				if(dialogListener != null) dialogListener.OnCancel(event);
			}
			else {
				if(dialogListener != null) dialogListener.OnConfirm(event);
			}
			btnSubmit.setText(confirmText);
			btnCancel.setText(cancelText);
			dismiss();
			break;

		default:
			break;
		}
		return false;
	}
	
	public void startTimer(int second) {
		if(countDownTimer != null) {
			countDownTimer.cancel();
		}
		countDownTimer = new SelectCountDownTimer(second * 1000, 1000);
		countDownTimer.start();
	}
	
	public void show(String text, int sencond) {
		Show(text);
		startTimer(sencond);
	}
	
	public void showSingle(String text, int sencond) {
		if(text == null || text.equals("")) {
			return;
		}
		ShowSingle(text);
		startTimer(sencond);
	}
	
	public void setFriendEvent(MBNotifyFriendEvent event) {
		this.event = event;
	}
	
	public void dismiss() {
		if(countDownTimer != null)  {
			countDownTimer.cancel();
			countDownTimer = null;
		}
		super.dismiss();
	}
}
