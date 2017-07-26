package com.funoble.myarstation.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.LinearLayout;

import com.funoble.myarstation.game.R;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;

/**
 * <p>
 * 
 * </p>
 */
public class GameMenuDialog extends Dialog implements
		android.view.View.OnClickListener {

	private ViewGroup mLayout;
	private LinearLayout iMenu;
	private Button btnChangeRoom;
	private Button btnChangeTable;
	private Button btnExit;
	private Context context;
	//动画
	AnimationSet set = new AnimationSet(true);
	Animation putup_in_animation;
	LayoutAnimationController controller;
	Animation putinAnimation;
	Animation putoutAnimation;
	Animation putupOutAnimation;
	private OnClickMenuListener iMenuListener;
	private boolean ibOnClick = false;
	
	private void initAnim() {
		putinAnimation = AnimationUtils.loadAnimation(context, R.anim.push_right_in);
		putoutAnimation = AnimationUtils.loadAnimation(context, R.anim.push_left_out);
		putupOutAnimation = AnimationUtils.loadAnimation(context, R.anim.push_up_out);
		putup_in_animation = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
		controller = AnimationUtils.loadLayoutAnimation(context, R.anim.push_up_in_layoutanim);
	}
	
	/**
	 * @param context
	 */
	public GameMenuDialog(Context context, OnClickMenuListener listener) {
		super(context, R.style.PlayerInfodialog);
		this.context = context;
		this.iMenuListener = listener;
		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_game_menu, null);

		setContentView(mLayout);

		iMenu = (LinearLayout) mLayout.findViewById(R.id.menu);
		btnChangeRoom = (Button) mLayout.findViewById(R.id.btnchangeRoom);
		btnChangeRoom.setOnClickListener(this);
		btnChangeTable = (Button) mLayout.findViewById(R.id.btnchangetable);
		btnChangeTable.setOnClickListener(this);
		btnChangeTable.setVisibility(View.GONE);
		btnExit = (Button) mLayout.findViewById(R.id.btnexit);
		btnExit.setOnClickListener(this);
		initAnim();
//		mLayout.setLayoutAnimation(controller);
		this.setCanceledOnTouchOutside(true);
	}
	
	public void show(boolean abPlaying) {
		ibOnClick = false;
		btnChangeRoom.setEnabled(!abPlaying);
		btnChangeTable.setEnabled(!abPlaying);
		this.show();
	}
	
	public void updateMenuState(boolean abPlaying) {
		if(this.isShowing() == false) {
			return;
		}
		btnChangeRoom.setEnabled(!abPlaying);
		btnChangeTable.setEnabled(!abPlaying);
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
////		p.height = (int) (d.getHeight() * 0.9); // 高度设置为屏幕的1.0
//		p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.8
//		// p.alpha = 1.0f; //设置本身透明度
		 p.dimAmount = 0.1f; //设置黑暗度
//
		getWindow().setAttributes(p); // 设置生效
		// 磨砂表现
//		getWindow().addFlags(LayoutParams.FLAG_BLUR_BEHIND);// android
		getWindow().setGravity(Gravity.RIGHT | Gravity.TOP); // 设置靠右对齐
//		iMenu.startLayoutAnimation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
		if(iMenuListener != null && ibOnClick == false) {
			iMenuListener.onClickMenu(v);
			ibOnClick = true;
		}
		this.dismiss();
	}
	
	public interface OnClickMenuListener {
		public void onClickMenu(View v); 
	}
}
