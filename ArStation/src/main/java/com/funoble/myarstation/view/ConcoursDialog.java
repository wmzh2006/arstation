package com.funoble.myarstation.view;

import com.funoble.myarstation.data.cach.Concours;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

/**
 * <p>
 * 
 * </p>
 */
/**
 * description 活动开始提示界面
 * version 1.0
 * author 
 * update 2012-11-15 下午06:15:27 
 */

public class ConcoursDialog extends Dialog implements
		android.view.View.OnClickListener {

	private ViewGroup mLayout;

	private TextView tvTip;

	private Button btnJoin;
	private Button btnAbort;
	private Button btnWait;
	private OnConcoursListener concoursSelectListener;
	private Concours concours;
	/**
	 * @param context
	 */
	public ConcoursDialog(Context context, OnConcoursListener l) {
		super(context, R.style.ITODialogStyle);
		this.concoursSelectListener = l;
		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_concours, null);

		setContentView(mLayout);
		this.getWindow().setWindowAnimations(R.style.putupinanimation);
		tvTip = (TextView) mLayout.findViewById(R.id.tvConcoursContent);
		btnJoin = (Button) mLayout.findViewById(R.id.btnjoin);
		btnJoin.setOnClickListener(this);
		btnAbort = (Button) mLayout.findViewById(R.id.btnabort);
		btnAbort.setOnClickListener(this);
		btnWait = (Button) mLayout.findViewById(R.id.btnwait);
		btnWait.setOnClickListener(this);
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
//		p.height = (int) (d.getHeight() * 0.9); // 高度设置为屏幕的1.0
		p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
//		// p.alpha = 1.0f; //设置本身透明度
//		// p.dimAmount = 0.0f; //设置黑暗度
//
//		getWindow().setAttributes(p); // 设置生效
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
		if (id == R.id.btnjoin) {
			if(concoursSelectListener != null){
				concoursSelectListener.OnJoint(concours);
			}
		} else if (id == R.id.btnabort) {
			if(concoursSelectListener != null){
				concoursSelectListener.OnAbort(concours);
			}
		} else if (id == R.id.btnwait) {
			if(concoursSelectListener != null){
				concoursSelectListener.OnWait(concours);
			}
		} else {
		}
		this.dismiss();
	}

	public void show(Concours concours) {
		if(concours == null) {
			return;
		}
		this.concours = concours;
		tvTip.setText(concours.iMsg);
		this.show();
	}
	
	public interface OnConcoursListener {
		public void OnJoint(Concours concours);//参加
		public void OnAbort(Concours concours);//放弃
		public void OnWait(Concours concours);//挂起
	}
}
