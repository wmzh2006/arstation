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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <p>
 * 
 * </p>
 */
public class BuyDialog extends Dialog implements
		android.view.View.OnClickListener {

	private ViewGroup mLayout;

	private TextView tvTip;

	private Button btnSubmit;

	private Button btnCancel;

	private String sendText;

	public static final int TYPE_BUYWINE = 0;
	public static final int TYPE_BUYCLEANDRINK = 1;
	private int iType = -1;
							//0买酒
						  //1买醒酒丸
	/**
	 * @param context
	 */
	public BuyDialog(Context context) {
		super(context, R.style.ITODialogStyle);

		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_buywine, null);

		setContentView(mLayout);
		ScrollView sView = (ScrollView) mLayout.findViewById(R.id.svbuywine);
		tvTip = (TextView) sView.findViewById(R.id.tvContent);
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
		if (id == R.id.btnbuy) {
			switch (iType) {
			case TYPE_BUYCLEANDRINK:
				MyArStation.mGameMain.iGameProtocol.requsetcLeanDrunk(MyArStation.iPlayer.iUserID);
				break;
			case TYPE_BUYWINE:
				MyArStation.mGameMain.iGameProtocol.requsetBuyWine(MyArStation.iPlayer.iUserID);
				
				break;

			default:
				break;
			}
			this.dismiss();
		} else if (id == R.id.btncancel) {
			this.dismiss();
		} else {
		}
	}

	public void Show(String text, int aType) {
//		System.out.println("BuyWine debug 22222222222222222");
		if(text == null || aType < 0) {
			return;
		}
//		System.out.println("BuyWine debug 333333333333333");
		iType = aType;
		tvTip.setText(text);
		this.show();
//		System.out.println("BuyWine debug 4444444444444444");
	}
}
