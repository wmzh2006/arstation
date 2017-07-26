/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: HelpScreen.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-4 下午05:34:11
 *******************************************************************************/
package com.funoble.myarstation.screen;

import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.funoble.myarstation.common.MD5;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.common.Util;
import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.socket.protocol.MBRspAccount;
import com.funoble.myarstation.socket.protocol.MBRspPlayerInfoThree;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.MessageEventID;
import com.funoble.myarstation.view.ShowAccountDialog;


public class AdScreen extends GameView implements OnClickListener{
	
	private int	iGameState = 0; //pak在数据动画
	private Scene iSceneLoading = null;
	//资源
	private Project iUIPak = null;
	
	private WebView 	wvAd;
	private Button 		btnReturn;
	private Button 		btnSHowAccount;
	private RelativeLayout mainRelativeLayout;
	private RelativeLayout rlLayout;
	private ProgressBar progressBar1;
	private boolean     isHelp = true;
	private String Url;
	private int iScreenType = 0;
	
	//动画
	AnimationSet set = new AnimationSet(true);
	Animation putup_in_animation;
	LayoutAnimationController controller;
	Animation putinAnimation;
	Animation putoutAnimation;
	Animation putupOutAnimation;
	private ShowAccountDialog showAccountDialog;
	private Dialog dialog;
	private EditText content;
	private void initAnim() {
		putinAnimation = AnimationUtils.loadAnimation(MyArStation.getInstance().getApplicationContext(), R.anim.push_right_in);
		putoutAnimation = AnimationUtils.loadAnimation(MyArStation.getInstance().getApplicationContext(), R.anim.push_left_out);
	}
	
	private void animationIn() {
		mainRelativeLayout.startAnimation(putinAnimation);
		mainRelativeLayout.setVisibility(View.VISIBLE);
		putinAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
//				showProgressDialog(R.string.Loading_String);
			}
		});
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
				if(iScreenType == 0) {
					GameCanvas.getInstance().changeView(new ChooseRoomScreen());
				}
				else {
					GameCanvas.getInstance().changeView(new HomeScreen());
				}
			}
		});
	}

	/**
	 * construct
	 */
	public AdScreen() {
	}
	
	public AdScreen(String url) {
		this.Url = url;
		this.iScreenType =  0;
	}

	public AdScreen(String url, int screen) {
		this.Url = url;
		this.iScreenType = screen;
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#init()
	 */
	@Override
	public void init() {
		messageEvent(MessageEventID.EMESSAGE_EVENT_TO_AD);
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#releaseResource()
	 */
	@Override
	public void releaseResource() {
		iSceneLoading = null;
		iUIPak = null;
		if(showAccountDialog != null) {
			showAccountDialog.dismiss();
			showAccountDialog = null;
		}
		if(dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
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
//				animationOut();
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
		case MessageEventID.EMESSAGE_EVENT_TO_AD:
			MyArStation.mGameMain.mainLayout.removeAllViews();
			mainRelativeLayout = (RelativeLayout) LayoutInflater.from(MyArStation.mGameMain.getApplicationContext()).inflate(R.layout.new_ad, null);
			MyArStation.mGameMain.mainLayout.addView(mainRelativeLayout);
			rlLayout = (RelativeLayout) mainRelativeLayout.findViewById(R.id.rl2);
			wvAd = ((WebView) rlLayout.findViewById(R.id.wvad));
			progressBar1 = ((ProgressBar) rlLayout.findViewById(R.id.progressBar1));
			WebSettings settings = wvAd.getSettings();
	        settings.setSupportZoom(true);          //支持缩放
	        settings.setBuiltInZoomControls(false);  //启用内置缩放装置
	        settings.setJavaScriptEnabled(true);    //启用JS脚本
	        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
	        wvAd.setWebViewClient(new WebViewClient() {
	            //当点击链接时,希望覆盖而不是打开新窗口
	            @Override
	            public boolean shouldOverrideUrlLoading(WebView view, String url) {
	                view.loadUrl(url);  //加载新的url
	                progressBar1.setVisibility(View.VISIBLE);
	                return true;    //返回true,代表事件已处理,事件流到此终止
	            }
	        });
	                       
	        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
	        wvAd.setOnKeyListener(new View.OnKeyListener() {
	            @Override
	            public boolean onKey(View v, int keyCode, KeyEvent event) {
	                if (event.getAction() == KeyEvent.ACTION_DOWN) {
	                    if (keyCode == KeyEvent.KEYCODE_BACK && wvAd.canGoBack()) {
	                    	wvAd.goBack();   //后退
	                        return true;    //已处理
	                    }
	                }
	                return false;
	            }
	        });
	             
	        wvAd.setWebChromeClient(new WebChromeClient() {
	            //当WebView进度改变时更新窗口进度
	            @Override
	            public void onProgressChanged(WebView view, int newProgress) {
	                //Activity的进度范围在0到10000之间,所以这里要乘以100
	            	MyArStation.getInstance().setProgress(newProgress * 100);
	                progressBar1.setProgress(newProgress);
	                if(newProgress >= 100) {
//	                	removeProgressDialog();
	                	progressBar1.setVisibility(View.INVISIBLE);
	                }
//	                Tools.showSimpleToast(GameMain.getInstance().getApplicationContext(), newProgress+"");
	            }
	        });
//			wvAd.loadUrl("http://www.funnoble.com/index.php");
			wvAd.loadUrl(Url);
			wvAd.requestFocus();
			
			btnReturn = (Button)mainRelativeLayout.findViewById(R.id.btnadReturn);
			btnReturn.setOnClickListener(this);
			btnSHowAccount = (Button)mainRelativeLayout.findViewById(R.id.btnAccount);
			btnSHowAccount.setOnClickListener(this);
			if(dialog == null) {
				dialog = new Dialog(MyArStation.mGameMain, R.style.MSGDialogStyle);
				dialog.setContentView(R.layout.new_pwd);
				dialog.setCancelable(true);
				TextView title = (TextView)dialog.findViewById(R.id.dialog_title);
//				title.setText(R.string.Return_LoginScreen_tip_title);
				content = (EditText)dialog.findViewById(R.id.dialog_message);
//				content.setText(R.string.Return_LoginScreen_tip_Content);
				Button commit = (Button)dialog.findViewById(R.id.ok);
				commit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String pwd = content.getText().toString().trim();
						if(pwd == null || pwd.equals("")) {
							Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), "密码为空！");
						}
						else {
							MD5 md5 = new MD5();
							String md5Psw = md5.getMD5ofStr(pwd);
							MyArStation.iGameProtocol.requestAccount(0, md5Psw);
							MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
							dialog.dismiss();
						}
					}
				});
				Button cancle = (Button)dialog.findViewById(R.id.cancel);
				cancle.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
						dialog.dismiss();
					}
				});
			}
			initAnim();
			animationIn();
			iGameState = -1;
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_ACCOUNT:
			MBRspAccount rspAccount = (MBRspAccount)msg.obj;
			if(rspAccount.iMsg != null && rspAccount.iMsg.length() > 0) {
				Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), rspAccount.iMsg);
			}
			if(rspAccount.nResult == 0) {
				if(showAccountDialog == null) {
					showAccountDialog = new ShowAccountDialog(MyArStation.mGameMain);
				}
				showAccountDialog.Show("您的兑奖码是：" +rspAccount.iAccount);
			}
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
		MobileMsg pMobileMsg = (MobileMsg) socket;
		switch (aMobileType) {
			case IM.IM_RESPONSE:
			{
				switch (aMsgID) {
				case RspMsg.MSG_RSP_ACCOUNT:
					MBRspAccount rspAccount = (MBRspAccount)pMobileMsg.m_pMsgPara;
					if(rspAccount == null) {
						break;
					}
					Tools.debug(rspAccount.toString());
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_ACCOUNT, rspAccount);
					break;
				default:
					
					break;
				}
			}
		}
	}

	/* 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btnadReturn) {
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
			animationOut();
		} else if (id == R.id.btnAccount) {
			//			GameMain.getInstance().iGameProtocol.requestAccount(0);
			content.setText("");
			dialog.show();
		} else {
		}
	}
}
