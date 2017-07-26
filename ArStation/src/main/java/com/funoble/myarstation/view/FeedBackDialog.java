package com.funoble.myarstation.view;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <p>
 * 
 * </p>
 */
public class FeedBackDialog extends Dialog implements
		android.view.View.OnClickListener {

	private ViewGroup mLayout;

	private EditText etFeedBack;

	private Button btnSubmit;

	private Button btnCancel;

	private String sendText;
	
	/**
	 * 字数统计
	 */
	private TextView chCounterText;

	/**
	 * @param context
	 */
	public FeedBackDialog(Context context) {
		super(context, R.style.ITODialogStyle);

		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_feedback, null);

		setContentView(mLayout);

		etFeedBack = (EditText) mLayout.findViewById(R.id.feedback_edit_text);
		btnSubmit = (Button) mLayout.findViewById(R.id.btnSend);
		btnSubmit.setOnClickListener(this);
		btnCancel = (Button) mLayout.findViewById(R.id.cancel);
		btnCancel.setOnClickListener(this);
		chCounterText = (TextView) mLayout.findViewById(R.id.feedback_counter);
		etFeedBack.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				sendText = etFeedBack.getText().toString();
				chCounterText.setText(sendText.length() + "/" + 80);
			}

		});
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
		p.height = (int) (d.getHeight() * 0.9); // 高度设置为屏幕的1.0
		p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.8
		// p.alpha = 1.0f; //设置本身透明度
		// p.dimAmount = 0.0f; //设置黑暗度

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
		if (id == R.id.btnSend) {
			if (etFeedBack != null) {
				if (sendText==null || sendText.length()<=0) {
					Tools.showSimpleToast(MyArStation.mGameMain, Gravity.CENTER, MyArStation.mGameMain.getString(R.string.FeekBack_Empty), Toast.LENGTH_SHORT);
				}
				else {
					//把 tSendText 发送到后台
					if(MyArStation.iGameProtocol.requsetFeekBack(sendText)) {
						GameView view = MyArStation.getInstance().getGameCanvas().getCurrentView();
						if(view != null) {
							view.showProgressDialog(R.string.QuestFeekBack_Waiting);
						}
					}
					else {
						Tools.showSimpleToast(MyArStation.mGameMain, Gravity.CENTER, MyArStation.mGameMain.getString(R.string.Req_FeekBack_Failed), Toast.LENGTH_LONG);
					}
					this.dismiss();
				}
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			}
		} else if (id == R.id.cancel) {
			this.dismiss();
		} else {
		}
	}

}
