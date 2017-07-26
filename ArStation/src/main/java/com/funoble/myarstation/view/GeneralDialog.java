package com.funoble.myarstation.view;

import android.app.Dialog;
import android.content.Context;
import android.text.SpannableString;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.funoble.myarstation.game.R;
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
public class GeneralDialog extends Dialog implements
		android.view.View.OnClickListener {

	private ViewGroup mLayout;

	private TextView tvTip;

	private Button btnSubmit;

	private Button btnCancel;
	private OnGeneralDialogListener dialogListener;
	/**
	 * @param context
	 */
	public GeneralDialog(Context context) {
		super(context, R.style.MSGDialogStyle);

		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_buywine, null);

		setContentView(mLayout);

		tvTip = (TextView) mLayout.findViewById(R.id.tvContent);
		btnSubmit = (Button) mLayout.findViewById(R.id.btnbuy);
		btnSubmit.setOnClickListener(this);
		btnCancel = (Button) mLayout.findViewById(R.id.btncancel);
		btnCancel.setOnClickListener(this);
		this.getWindow().setWindowAnimations(R.style.putupinanimation);
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
		if (id == R.id.btnbuy) {
			if(dialogListener != null) dialogListener.OnConfirm();
		} else if (id == R.id.btncancel) {
			if(dialogListener != null) dialogListener.OnCancel();
		} else {
		}
		this.dismiss();
	}

	public void Show(String text) {
		tvTip.setText(text);
		this.show();
	}
	
	public void Show(SpannableString text) {
		tvTip.setText(text);
		this.show();
	}
	
	public void setConfirmText(String text) {
		if(text == null) {
			return;
		}
		btnSubmit.setText(text);
	}
	
	public void setCancelText(String text) {
		if(text == null) {
			return;
		}
		btnCancel.setText(text);
	}
	
	public interface OnGeneralDialogListener {
		public void OnConfirm();
		public void OnCancel();
	}
	
	public void setOnGeneralDialogListener(OnGeneralDialogListener l) {
		dialogListener = l;
	}
}
