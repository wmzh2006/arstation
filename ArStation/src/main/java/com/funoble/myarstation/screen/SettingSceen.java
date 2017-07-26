/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: SettingSceen.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-3 下午05:59:03
 *******************************************************************************/
package com.funoble.myarstation.screen;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.ScreenBrightnessManager;
import com.funoble.myarstation.utils.SettingManager;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.MessageEventID;


public class SettingSceen extends GameView implements OnClickListener{
	private int	iGameState = 0; //pak在数据动画
	private Scene iSceneLoading = null;
	//资源
	private Project iUIPak = null;
	
	public static final String SCREEN_BRIGHTNESS_KEY = "SETTING_SCREEN_BRIGHTNESS";
	public static final String SHAKA_KEY = "SETTING_SHAKA";
	public static final String SOUND_KEY = "SETTING_SOUND";
	public static final String MUSIC_KEY = "SETTING_MUSIC";
	public static final String WIFIDOWNLOAD_KEY = "SETTING_WIFIDOWNLOA";
	
	private SeekBar sbScreenLight;
	private ToggleButton  tgShake;
	private ToggleButton  tgSound;
	private ToggleButton  tgMusic;
	private ToggleButton  tgWifiDownload;
	
	private boolean		  isShaka = false;
	private boolean		  isSound = false;
	private boolean		  isMusic = false;
	private boolean		  isWifiDownload = false;
	private boolean		  isReturn = false;
	private int		  	  iBrightness;
	private LinearLayout	llTitle;
	private LinearLayout	llContent;
	private Button			btnReturn;
	private Animation		pushRightInAimation;
	private Animation		pushRightOutAimation;
	private LayoutAnimationController animationController;
	private RelativeLayout mainRelativeLayout;
	
	private MyArStation iGameMain = MyArStation.getInstance();
	
	private void initAnimation() {
		pushRightInAimation = AnimationUtils.loadAnimation(MyArStation.getInstance().getApplicationContext(),
				R.anim.push_right_in);
		pushRightOutAimation = AnimationUtils.loadAnimation(MyArStation.getInstance().getApplicationContext(), R.anim.push_left_out);
		animationController = AnimationUtils.loadLayoutAnimation(MyArStation.getInstance().getApplicationContext(),
				R.anim.push_up_in_layoutanim);
	}
	/**
	 * construct
	 */
	public SettingSceen() {
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#init()
	 */
	@Override
	public void init() {
		iUIPak = MyArStation.iPakManager.loadUIPak();//Project.loadProject(ActivityUtil.PATH_SCREEN+"gameshome_map.pak", false);
		if (iUIPak != null) {
			iSceneLoading = iUIPak.getScene(12);
			if(iSceneLoading != null) {
				iSceneLoading.setViewWindow(0, 0, 800, 480);
				iSceneLoading.initDrawList();
			}
		}
		messageEvent(MessageEventID.EMESSAGE_EVENT_TO_SETTING);
	}

	private void AnimationOut() {
		pushRightOutAimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mainRelativeLayout.setVisibility(View.INVISIBLE);
				messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
			}
		});
		mainRelativeLayout.startAnimation(pushRightOutAimation);
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#releaseResource()
	 */
	@Override
	public void releaseResource() {
		iSceneLoading = null;
		iUIPak = null;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#paintScreen(android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	public void paintScreen(Canvas g, Paint paint) {
		//载入数据
		if(iGameState == 0) {
			g.drawBitmap(MyArStation.iImageManager.loadLoginBack(),
					0,
					0,
					null);
			if(iSceneLoading != null) {
				iSceneLoading.paint(g, 0, 0);
//				g.drawText("正在读取数据", 278*ActivityUtil.ZOOM_X, 288*ActivityUtil.ZOOM_Y, ActivityUtil.mLoading);
			}
		}
		else {
			g.drawBitmap(MyArStation.iImageManager.iBackLoginBmp,
					0,
					0,
					null);
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#performL()
	 */
	@Override
	public void performL() {
		//载入数据
		if(iGameState == 0) {
			if(iSceneLoading != null) {
				iSceneLoading.handle();
			}
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if(!isReturn) {
//				isReturn = !isReturn;
				AnimationOut();
//			}
			return true;
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onDown(android.view.MotionEvent)
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onLongPress(android.view.MotionEvent)
	 */
	@Override
	public boolean onLongPress(MotionEvent e) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onSingleTapUp(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#pointerPressed(int, int)
	 */
	@Override
	public boolean pointerPressed(int aX, int aY) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#pointerReleased(int, int)
	 */
	@Override
	public boolean pointerReleased(int aX, int aY) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case MessageEventID.EMESSAGE_EVENT_TO_SETTING:
			MyArStation.mGameMain.mainLayout.removeAllViews();
			mainRelativeLayout = (RelativeLayout) LayoutInflater.from(MyArStation.mGameMain.getApplicationContext()).inflate(R.layout.new_setting, null);
			MyArStation.mGameMain.mainLayout.addView(mainRelativeLayout);
			llTitle = (LinearLayout) iGameMain.findViewById(R.id.llRemarks);
			llTitle.setVisibility(View.VISIBLE);
			llContent = (LinearLayout) iGameMain.findViewById(R.id.llsettiong);
			llContent.setVisibility(View.VISIBLE);
			sbScreenLight = (SeekBar) iGameMain.findViewById(R.id.sbScreen);
			iBrightness = sbScreenLight.getProgress();
			btnReturn = (Button)iGameMain.findViewById(R.id.btnSetingReturn);
			btnReturn.setOnClickListener(this);
			sbScreenLight.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					iBrightness =  seekBar.getProgress();
					SettingManager.getInstance().saveBrightness(iBrightness);
					ScreenBrightnessManager.setBrightness((float) iBrightness);
				}
			});
			
			tgShake = (ToggleButton) iGameMain.findViewById(R.id.tbtnShaka);
			tgShake.setOnClickListener(this);
			tgSound = (ToggleButton) iGameMain.findViewById(R.id.tbtnSound);
			tgSound.setOnClickListener(this);
			tgMusic = (ToggleButton) iGameMain.findViewById(R.id.tbtnMusic);
			tgMusic.setOnClickListener(this);
			tgWifiDownload = (ToggleButton) iGameMain.findViewById(R.id.tbtnDownload);
			tgWifiDownload.setOnClickListener(this);
			loadSetting();
			initAnimation();
			mainRelativeLayout.startAnimation(pushRightInAimation);
			mainRelativeLayout.setLayoutAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					llContent.setLayoutAnimation(animationController);
					llContent.startLayoutAnimation();
					llContent.setVisibility(View.VISIBLE);
				}
			});
			mainRelativeLayout.setVisibility(View.VISIBLE);
			iGameState = -1;
			break;

		default:
			break;
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#OnProcessCmd(java.lang.Object, int, int)
	 */
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
	}

	/* 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
		int id = v.getId();
		if (id == R.id.sbScreen) {
		} else if (id == R.id.tbtnShaka) {
			isShaka = !isShaka;
			SettingManager.getInstance().saveShaka(isShaka);
		} else if (id == R.id.tbtnSound) {
			isSound = !isSound;
			SettingManager.getInstance().saveSound(isSound);
			//总共6个对象
			for(int i=0; i<6; i++) {
				MediaManager.getMediaManagerInstance(i).setOpenEffectState(isSound);
			}
		} else if (id == R.id.tbtnMusic) {
			isMusic = !isMusic;
			SettingManager.getInstance().saveMusic(isMusic);
			for(int i=0; i<6; i++) {
				MediaManager.getMediaManagerInstance(i).setOpenBgState(isMusic);
			}
		} else if (id == R.id.tbtnDownload) {
			isWifiDownload = !isWifiDownload;
			SettingManager.getInstance().saveWifiDownload(isWifiDownload);
		} else if (id == R.id.btnSetingReturn) {
			//			if(!isReturn) {
//				isReturn = !isReturn;
			AnimationOut();
		} else {
		}
	}

	private void initSetting() {
		sbScreenLight.setProgress(iBrightness);
		tgShake.setChecked(isShaka);
		tgSound.setChecked(isSound);
		tgMusic.setChecked(isMusic);
		tgWifiDownload.setChecked(isWifiDownload);
	}
	
	private void loadSetting() {
		iBrightness = SettingManager.getInstance().getiBrightness();
		isShaka = SettingManager.getInstance().isShaka();
		isSound = SettingManager.getInstance().isSound();
		isMusic = SettingManager.getInstance().isMusic();
		isWifiDownload = SettingManager.getInstance().isWifiDownload();
		Tools.debug("setting isSound = " + isSound + " isMusic = " + isMusic);
		initSetting();
	}
	
}
