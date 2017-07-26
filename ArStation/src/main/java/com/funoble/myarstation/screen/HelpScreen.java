/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: HelpScreen.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-4 下午05:34:11
 *******************************************************************************/
package com.funoble.myarstation.screen;

import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.MessageEventID;


public class HelpScreen extends GameView implements OnClickListener{
	private String helpUrl = "http://www.funoble.com/help/help.html";
	private String defualUrl = "file:///android_asset/help/help.html";
	private int	iGameState = 0; //pak在数据动画
	private Scene iSceneLoading = null;
	//资源
	private Project iUIPak = null;
	
	private WebView 	wvHelp;
	private WebView 	wvAbout;
	private Button 		btnHelp;
	private Button 		btnAbout;
	private Button 		btnReturn;
	private RelativeLayout mainRelativeLayout;
	
	private boolean     isHelp = true;
	
	//动画
	AnimationSet set = new AnimationSet(true);
	Animation putup_in_animation;
	LayoutAnimationController controller;
	Animation putinAnimation;
	Animation putoutAnimation;
	Animation putupOutAnimation;
	
	private void initAnim() {
		putinAnimation = AnimationUtils.loadAnimation(MyArStation.getInstance().getApplicationContext(), R.anim.push_right_in);
		putoutAnimation = AnimationUtils.loadAnimation(MyArStation.getInstance().getApplicationContext(), R.anim.push_left_out);
	}
	
	private void animationIn() {
		mainRelativeLayout.startAnimation(putinAnimation);
		mainRelativeLayout.setVisibility(View.VISIBLE);
		
	}
	
	private void animationOut() {
		mainRelativeLayout.startAnimation(putoutAnimation);
		putoutAnimation.setAnimationListener(new AnimationListener() {
			
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
	}

	/**
	 * construct
	 */
	public HelpScreen() {
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#init()
	 */
	@Override
	public void init() {
		messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HELP);
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
			if(iGameState != 1) {
				animationOut();
				iGameState = 1;
			}
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
		case MessageEventID.EMESSAGE_EVENT_TO_HELP:
			MyArStation.mGameMain.mainLayout.removeAllViews();
			mainRelativeLayout = (RelativeLayout) LayoutInflater.from(MyArStation.mGameMain.getApplicationContext()).inflate(R.layout.new_help, null);
			MyArStation.mGameMain.mainLayout.addView(mainRelativeLayout);
			wvHelp = ((WebView) mainRelativeLayout.findViewById(R.id.wvHelp));
			wvHelp.setBackgroundColor(0);
			WebSettings settings = wvHelp.getSettings();
	        settings.setSupportZoom(false);          //支持缩放
	        settings.setBuiltInZoomControls(true);  //启用内置缩放装置
	        settings.setJavaScriptEnabled(true);    //启用JS脚本
	        wvHelp.setWebViewClient(new WebViewClient() {
	            //当点击链接时,希望覆盖而不是打开新窗口
	            @Override
	            public boolean shouldOverrideUrlLoading(WebView view, String url) {
	                view.loadUrl(url);  //加载新的url
//	                progressBar1.setVisibility(View.VISIBLE);
	                return true;    //返回true,代表事件已处理,事件流到此终止
	            }

				/* 
				 * @see android.webkit.WebViewClient#onReceivedError(android.webkit.WebView, int, java.lang.String, java.lang.String)
				 */
				@Override
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
//					 view.loadUrl("http://www.funnoble.com/index.php");  //加载新的url
					view.loadUrl(helpUrl);
					Tools.debug("onReceivedError" + description);
//		             progressBar1.setVisibility(View.VISIBLE);
//					super.onReceivedError(view, errorCode, description, failingUrl);
				}

	        });
	                       
	        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
	        wvHelp.setOnKeyListener(new View.OnKeyListener() {
	            @Override
	            public boolean onKey(View v, int keyCode, KeyEvent event) {
	                if (event.getAction() == KeyEvent.ACTION_DOWN) {
	                    if (keyCode == KeyEvent.KEYCODE_BACK && wvHelp.canGoBack()) {
	                    	wvHelp.goBack();   //后退
	                        return true;    //已处理
	                    }
	                }
	                return false;
	            }
	        });
	             
	        wvHelp.setWebChromeClient(new WebChromeClient() {
	            //当WebView进度改变时更新窗口进度
	            @Override
	            public void onProgressChanged(WebView view, int newProgress) {
	                //Activity的进度范围在0到10000之间,所以这里要乘以100
//	                GameMain.getInstance().setProgress(newProgress * 100);
//	                progressBar1.setProgress(newProgress);
//	                if(newProgress >= 100) {
//	                	progressBar1.setVisibility(View.INVISIBLE);
//	                }
	            }
	        });
//			wvHelp.loadUrl("http://www.funnoble.com/index.php");
//			wvHelp.loadUrl(Url);
	        checkWebViewUrl();
			wvHelp.requestFocus();
			wvAbout = ((WebView) mainRelativeLayout.findViewById(R.id.wvAbout));
			wvAbout.setBackgroundColor(0);
			wvAbout.loadUrl("file:///android_asset/help/about.html");
			
			btnHelp = (Button)mainRelativeLayout.findViewById(R.id.btnHelp);
			btnHelp.setOnClickListener(this);
			btnAbout = (Button)mainRelativeLayout.findViewById(R.id.btnAbout);
			btnAbout.setOnClickListener(this);
			btnReturn = (Button)mainRelativeLayout.findViewById(R.id.btnHelpReturn);
			btnReturn.setOnClickListener(this);
			switchTag(true);
			initAnim();
			animationIn();
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
		int id = v.getId();
		if (id == R.id.btnAbout) {
			if(wvAbout.getVisibility() == View.VISIBLE) {
				return;
			}
			switchTag(false);
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
		} else if (id == R.id.btnHelp) {
			if(wvHelp.getVisibility() == View.VISIBLE) {
				return;
			}
			switchTag(true);
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
		} else if (id == R.id.btnHelpReturn) {
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			animationOut();
		} else {
		}
	}
	
	private void switchTag(boolean ahelp) {
		isHelp = ahelp;
		btnHelp.setSelected(isHelp);
		btnAbout.setSelected(!isHelp);
		
		wvHelp.setVisibility(isHelp ? View.VISIBLE : View.GONE);
		wvAbout.setVisibility(isHelp ? View.GONE : View.VISIBLE);
	}
	
	public void checkWebViewUrl() {
		if(helpUrl == null || helpUrl.equals("")) {
			helpUrl = defualUrl;
		}
		new AsyncTask<String, Void, Integer>() {

			@Override
			protected Integer doInBackground(String... params) {
				int responseCode = -1;
				try {
					URL url = new URL(params[0]);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					responseCode = connection.getResponseCode();
				} catch (Exception e) {
					Tools.debug("Loading webView error:" + e.getMessage());
				}
				return responseCode;
			}

			@Override
			protected void onPostExecute(Integer result) {
				if (result != 200) {
					wvHelp.loadUrl(defualUrl);
					wvHelp.setBackgroundColor(0);
				} else {
					wvHelp.loadUrl(helpUrl);
					wvHelp.setBackgroundColor(0);
				}
			}
		}.execute(helpUrl);
	}
}
