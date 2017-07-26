package com.funoble.myarstation.screen;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.TPoint;
import com.funoble.myarstation.utils.WebUrls;
import com.funoble.myarstation.view.GameEventID;

/**
 * <p>
 * 
 * </p>
 */
public class BulletinView extends BasePopScreen implements
		android.view.View.OnClickListener,
		OnTouchListener {

	private final int EVENTID_INIT = 6;
	private final int EVENTID_CLOSE = 7;
	private final int EVENTID_SHOW = 8;
	private final int EVENTID_HIDE = 9;
	private Project iUiProject = null;
	private Scene  iReWardScene = null;
	private Scene  iNewsScene = null;
	private ArrayList<SpriteButton> iRewardButtons = new ArrayList<SpriteButton>();
	private ArrayList<SpriteButton> iNewsButtons = new ArrayList<SpriteButton>();
	private SpriteButton getButton = null;
	
	private final int PAGE_REWARD = 0;//登录奖励
	private final int PAGE_NEWS = 1;//公告
	private int currentPageIndex = PAGE_REWARD; //
	private RelativeLayout mLayout;
	private WebView webView;
	private WebView webView2;
	private Context iContext;
	
	
	private String rewardUrl = "";
	private String newsUrl = "";
	private boolean isobtain = true;
	private int WebX = (int) (88 * ActivityUtil.ZOOM_X);
	private int WebY = (int) (121 * ActivityUtil.ZOOM_Y);
	private int WebW = (int) (616 * ActivityUtil.ZOOM_X);
	private int WebH = (int) (303 * ActivityUtil.ZOOM_Y);
	private int WebMH = (int) (239 *ActivityUtil.ZOOM_Y);
	
	private MyHandler iHandler = new MyHandler();
	
	public  class MyHandler extends Handler {

		/* 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EVENTID_INIT:
				initSystemCom();
				break;
			case STATE_ENTERING:
				if(currentPageIndex == PAGE_REWARD) {
					setSize(WebX+iSoX, WebY, WebW, WebMH);
				}
				else {
					setSize2(WebX+iSoX, WebY, WebW, WebH);
				}
				break;
			case STATE_ENTERED:
				iSoX = 0;
				if(currentPageIndex == PAGE_REWARD) {
					setSize(WebX+iSoX, WebY, WebW, WebMH);
				}
				else {
					setSize2(WebX+iSoX, WebY, WebW, WebH);
				}
				break;
			case STATE_EXITING:
				if(currentPageIndex == PAGE_REWARD) {
					setSize(WebX+iSoX, WebY, WebW, WebMH);
				}
				else {
					setSize2(WebX+iSoX, WebY, WebW, WebH);
				}
				break;
			case STATE_EXITED:
				if(currentPageIndex == PAGE_REWARD) {
					setSize(WebX+iSoX, WebY, WebW, WebMH);
				}
				else {
					setSize2(WebX+iSoX, WebY, WebW, WebH);
				}
				messageEvent(EVENTID_HIDE);
				break;
			case EVENTID_CLOSE:
//				isVision = false;
//				mLayout.setVisibility(View.GONE);
//				mLayout.setFocusable(false);
				iState = STATE_EXITING;
				break;
			case EVENTID_HIDE:
				isVision = false;
				mLayout.setVisibility(View.GONE);
				mLayout.setFocusable(false);
				break;
			case EVENTID_SHOW:
				isVision = true;
				iSoX = iSoffsetX;
				iReWardScene.iCameraX = -iSoX;
				iNewsScene.iCameraX = -iSoX;
				switchTab(isobtain ? PAGE_REWARD : PAGE_NEWS);
				MyArStation.mGameMain.mainLayout.bringChildToFront(mLayout);
				mLayout.setVisibility(View.VISIBLE);
				setSize(WebX+iSoX, WebY, WebW, WebMH);
				setSize2(WebX+iSoX, WebY, WebW, WebH);
//				mLayout.setFocusable(true);
				iState = STATE_ENTERING;
				openUrl();
				break;
			case GameEventID.ESPRITEBUTTON_EVENT_BULLETIN_REWARD:
				switchTab(PAGE_REWARD);
				webView.setVisibility(currentPageIndex == PAGE_REWARD ? View.VISIBLE : View.GONE);
				webView2.setVisibility(currentPageIndex == PAGE_REWARD ? View.GONE : View.VISIBLE);
//				openUrl();
				break;
			case GameEventID.ESPRITEBUTTON_EVENT_BULLETIN_NEWS:
				switchTab(PAGE_NEWS);
				webView.setVisibility(currentPageIndex == PAGE_REWARD ? View.VISIBLE : View.GONE);
				webView2.setVisibility(currentPageIndex == PAGE_REWARD ? View.GONE : View.VISIBLE);
//				openUrl();
				break;
			default:
				break;
			}
		}
		
	}
	
	private void SendMessage(int EventId) {
		Message message = new Message();
		message.what = EventId;
		iHandler.sendMessage(message);
	}
	/**
	 * @param context
	 */
	public BulletinView(Context context) {
		iContext = context;
		init();
		SendMessage(EVENTID_INIT);
//		switchTab(currentPageIndex);
	}
	
	
	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#init()
	 */
	@Override
	public void init() {
		super.init();
	}
	/**
	 * @param context
	 */
	private void initSystemCom() {
		mLayout = (RelativeLayout) LayoutInflater.from(iContext).inflate(
				R.layout.new_missionreward, null);
		mLayout.setVisibility(View.GONE);
//		mLayout.setOnTouchListener(this);
		MyArStation.mGameMain.mainLayout.addView(mLayout);
		try {
			webView = (WebView) mLayout.findViewById(R.id.webView1);
			setSize(WebX+iSoX, WebY, WebW, WebMH);
			webView.setBackgroundColor(Color.TRANSPARENT);
			webView.invalidate();
			webView2 = (WebView) mLayout.findViewById(R.id.webView2);
			setSize(WebX+iSoX, WebY, WebW, WebH);
			webView2.setBackgroundColor(Color.TRANSPARENT);
			webView2.invalidate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);          //支持缩放
        settings.setBuiltInZoomControls(false);  //启用内置缩放装置
        settings.setJavaScriptEnabled(true);    //启用JS脚本
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebViewClient(new WebViewClient() {
            //当点击链接时,希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);  //加载新的url
                return true;    //返回true,代表事件已处理,事件流到此终止
            }
        });
                       
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
//                    	webView.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
             
        webView.setWebChromeClient(new WebChromeClient() {
            //当WebView进度改变时更新窗口进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //Activity的进度范围在0到10000之间,所以这里要乘以100
            }
        });
        settings = webView2.getSettings();
        settings.setSupportZoom(true);          //支持缩放
        settings.setBuiltInZoomControls(false);  //启用内置缩放装置
        settings.setJavaScriptEnabled(true);    //启用JS脚本
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView2.setWebViewClient(new WebViewClient() {
        	//当点击链接时,希望覆盖而不是打开新窗口
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		view.loadUrl(url);  //加载新的url
        		return true;    //返回true,代表事件已处理,事件流到此终止
        	}
        });
        
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        webView2.setOnKeyListener(new View.OnKeyListener() {
        	@Override
        	public boolean onKey(View v, int keyCode, KeyEvent event) {
        		if (event.getAction() == KeyEvent.ACTION_DOWN) {
        			if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
//                    	webView.goBack();   //后退
        				return true;    //已处理
        			}
        		}
        		return false;
        	}
        });
        
        webView2.setWebChromeClient(new WebChromeClient() {
        	//当WebView进度改变时更新窗口进度
        	@Override
        	public void onProgressChanged(WebView view, int newProgress) {
        		//Activity的进度范围在0到10000之间,所以这里要乘以100
        	}
        });
        
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView2.setDownloadListener(new MyWebViewDownLoadListener());
        openUrl();
	}
	/**
	 * 
	 */
	private void setSize(int x, int y, int w, int h) {
		LayoutParams lParams = (LayoutParams) webView.getLayoutParams();
		lParams.leftMargin = x;
		lParams.topMargin = y;
		lParams.width = w;
		lParams.height = h;
		webView.setLayoutParams(lParams);
		webView.invalidate();
	}
	
	private void setSize2(int x, int y, int w, int h) {
		LayoutParams lParams = (LayoutParams) webView2.getLayoutParams();
		lParams.leftMargin = x;
		lParams.topMargin = y;
		lParams.width = w;
		lParams.height = h;
		webView2.setLayoutParams(lParams);
		webView2.invalidate();
	}

	public void setRewardUrl(String url) {
		rewardUrl = WebUrls.baseUrl+url;
		Tools.println(rewardUrl);
	}
	
	public void setNewsUrl(String url) {
		newsUrl = WebUrls.baseUrl+url;
		Tools.println(newsUrl);
	}
	
	public void setObtain(boolean obtain) {
		isobtain  = obtain;
	}
	
	public void openUrl() {
//		String url = currentPageIndex == PAGE_REWARD ? rewardUrl : newsUrl;
		webView.setVisibility(currentPageIndex == PAGE_REWARD ? View.VISIBLE : View.GONE);
		webView2.setVisibility(currentPageIndex == PAGE_REWARD ? View.GONE : View.VISIBLE);
		checkWebViewUrl(rewardUrl, webView);
		checkWebViewUrl(newsUrl, webView2);
	}
	
	public void dismiss() {
		SendMessage(EVENTID_CLOSE);
	}
	/* 
	 * @see android.app.Dialog#show()
	 */
	public void show() {
		SendMessage(EVENTID_SHOW);
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Dialog#onStart()
	 */
//	@Override
//	protected void onStart() {
//		super.onStart();
//		Display d = getWindow().getWindowManager().getDefaultDisplay();
//		LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
//		p.height = (int) (d.getHeight() * 1.0); // 高度设置为屏幕的1.0
//		p.width = (int) (d.getWidth() *1.0); // 宽度设置为屏幕的0.8
//		getWindow().setGravity(Gravity.TOP | Gravity.CENTER); // 设置靠右对齐
//	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#messageEvent(int, java.lang.Object)
	 */
	@Override
	protected void messageEvent(int aEventID, Object aMsg) {
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#messageEvent(int)
	 */
	@Override
	protected void messageEvent(int aEventID) {
		SendMessage(aEventID);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
		switch (v.getId()) {
//		case R.id.close:
//			dismiss();
//			break;
//		case R.id.confirm:
//			if(currentPageIndex == PAGE_REWARD && isobtain) {
//				GameMain.iPlayer.iReward = 0;
//				GameMain.iGameProtocol.RequestAward(0);
//			}
//			dismiss();
//			break;
//		case R.id.mission:
//			currentPageIndex = PAGE_REWARD;
//			openUrl();
//			switchTab(currentPageIndex);
//			break;
//		case R.id.news:
//			currentPageIndex = PAGE_NEWS;
//			openUrl();
//			switchTab(currentPageIndex);
//			break;
		default:
			break;
		}
	}
	
	private void switchTab(int current) {
//		if(reWardSpriteButton == null || 
//				newsSpriteButton == null ||
//				getButton == null) {
//			return;
//		}
		currentPageIndex = current;
//		reWardSpriteButton.setActionID(currentPageIndex == PAGE_REWARD ? SpriteButton.ESPRITE_BUTTON_ACTION_PRESSED : SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
//		newsSpriteButton.setActionID(currentPageIndex == PAGE_REWARD ? SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL : SpriteButton.ESPRITE_BUTTON_ACTION_PRESSED);
//		newsTag.setClickable(currentPageIndex == PAGE_REWARD ? true : false);
//		newsTag.setSelected(currentPageIndex == PAGE_REWARD ? false : true);
		getButton.setVision((currentPageIndex == PAGE_REWARD && isobtain)? true : false);
	}

	/* 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(mLayout.getVisibility() == View.VISIBLE) return true;
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#loadPak()
	 */
	@Override
	protected void loadPak() {
		iUiProject = CPakManager.loadBulletinPak();
		iReWardScene = iUiProject.getScene(0);
		if(iReWardScene != null) {
			iReWardScene.setViewWindow(0, 0, 800, 480);
			iReWardScene.initDrawList();
			iReWardScene.initNinePatchList(null);
			ViewRect = iReWardScene.getLayoutMap(Scene.RectangleLayout).getNineRect(0);
			if(ViewRect != null) {
				iSoffsetX = ActivityUtil.SCREEN_WIDTH - ViewRect.left;
				iSoX = iSoffsetX;
				iSoSpeed = (iSoX / 6);
				iReWardScene.iCameraX = -iSoX;
			}
			//
			Vector<?> pSpriteList = null;
			Map pMap = iReWardScene.getLayoutMap(Scene.SpriteLayout);
			pSpriteList = pMap.getSpriteList();
			Sprite pSprite;
			for (int i=0; i<pSpriteList.size(); i++) {
				pSprite = (Sprite)pSpriteList.elementAt(i);
				int pX = pSprite.getX();
				int pY = pSprite.getY();
				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_BULLETIN + pSprite.getIndex();
				TPoint pPoint = new TPoint();
				pPoint.iX = pX;
				pPoint.iY = pY;
				//精灵按钮
				SpriteButton pSpriteButton;
				pSpriteButton = new SpriteButton(pSprite);
				pSpriteButton.setEventID(pEventID);
				pSpriteButton.setPosition(pX, pY);
				pSpriteButton.setHandler(this);
				if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_BULLETIN_GET) {
					getButton = pSpriteButton;
				}
				iRewardButtons.add(pSpriteButton);
			}
		}
		
		iNewsScene = iUiProject.getScene(1);
		if(iNewsScene != null) {
			iNewsScene.setViewWindow(0, 0, 800, 480);
			iNewsScene.initDrawList();
			iNewsScene.initNinePatchList(null);
			ViewRect = iReWardScene.getLayoutMap(Scene.RectangleLayout).getNineRect(0);
			if(ViewRect != null) {
				iSoffsetX = ActivityUtil.SCREEN_WIDTH - ViewRect.left;
				iSoX = iSoffsetX;
				iSoSpeed = (iSoX / 6);
				iNewsScene.iCameraX = -iSoX;
			}
			Map pMap = iNewsScene.getLayoutMap(Scene.SpriteLayout);
			Vector<?> pSpriteList = pMap.getSpriteList();
			Sprite pSprite;
			for (int i=0; i<pSpriteList.size(); i++) {
				pSprite = (Sprite)pSpriteList.elementAt(i);
				int pX = pSprite.getX();
				int pY = pSprite.getY();
				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_BULLETIN + pSprite.getIndex();
				TPoint pPoint = new TPoint();
				pPoint.iX = pX;
				pPoint.iY = pY;
				//精灵按钮
				SpriteButton pSpriteButton;
				pSpriteButton = new SpriteButton(pSprite);
				pSpriteButton.setEventID(pEventID);
				pSpriteButton.setPosition(pX, pY);
				pSpriteButton.setHandler(this);
				iNewsButtons.add(pSpriteButton);
			}
		}
		
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#releaseResource()
	 */
	@Override
	public void releaseResource() {
		super.releaseResource();
		iUiProject = null;
		iSpriteButtonList = null;
		iRewardButtons = null;
		iNewsButtons = null;
		getButton = null;
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#paintScreen(android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	public void paintScreen(Canvas g, Paint paint) {
		super.paintScreen(g, paint);
		if(!isVision) return;
		g.drawRect(rect, iPaint);
		if(currentPageIndex == PAGE_REWARD) {
			if(iReWardScene != null) {
				iReWardScene.paint(g, iReWardScene.iCameraX, 0);
				if(iRewardButtons != null) {
					for(int i = 0; i < iRewardButtons.size(); i++) {
						iRewardButtons.get(i).paint(g, iReWardScene.iCameraX, 0);
					}
				}
			}
		}
		else {
			if(iNewsScene != null) {
				iNewsScene.paint(g, iNewsScene.iCameraX, 0);
				if(iNewsButtons != null) {
					for(int i = 0; i < iNewsButtons.size(); i++) {
						iNewsButtons.get(i).paint(g, iNewsScene.iCameraX, 0);
					}
				}
			}
		}
		
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#performL()
	 */
	@Override
	public void performL() {
		if(!isVision) return;
		switch (iState) {
		case STATE_LOADING:
			if(iSceneLoading != null) {
				iSceneLoading.handle();
			}
			break;
		case STATE_ENTERING:
			iSoX -= iSoSpeed;
			if(iSoX <= 0) {
				iSoX = 0;
				iState = STATE_ENTERED;
				messageEvent(STATE_ENTERING);
			}
			break;
		case STATE_ENTERED:
			iSoX = 0;
			messageEvent(STATE_ENTERED);
			break;
		case STATE_EXITING:
			if(iSoX < iSoffsetX) {
				iSoX += iSoSpeed;
				messageEvent(STATE_EXITING);
			}
			else {
				iState = STATE_EXITED;
			}
			break;
		case STATE_EXITED:
			iSoX = iSoffsetX;
			iState = STATE_INVALID;
			messageEvent(STATE_EXITED);
			isVision =false;
			break;

		default:
			break;
		}
		performButton();
	}
	/**
	 * 
	 */
	private void performButton() {
		if(iReWardScene != null) {
			iReWardScene.handle();
			iReWardScene.iCameraX = -iSoX;
			if(iRewardButtons != null) {
				for(int i = 0; i < iRewardButtons.size(); i++) {
					iRewardButtons.get(i).perform();
				}
			}
		}
		if(iNewsScene != null) {
			iNewsScene.handle();
			iNewsScene.iCameraX = -iSoX;
			if(iNewsButtons != null) {
				for(int i = 0; i < iNewsButtons.size(); i++) {
					iNewsButtons.get(i).perform();
				}
			}
		}
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(!isVision) return false;
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			dimiss();
			return true;
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#pointerPressed(int, int)
	 */
	@Override
	public boolean pointerPressed(int aX, int aY) {
		if(!isVision) return false;
//		if(iState != STATE_ENTERED) return false;
		Rect pRect = new Rect();
		SpriteButton pSB;
		if(null != ViewRect && ViewRect.contains(aX, aY)) {
			for(int i=0; i<iRewardButtons.size(); i++) {
				pSB = ((SpriteButton)iRewardButtons.get(i));
				if(!pSB.isVision()) {
					continue;
				}
				int pX = pSB.getX();
				int pY = pSB.getY();
				Rect pLogicRect = pSB.getRect();
				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
						pX+pLogicRect.right, pY+pLogicRect.bottom);
				if (pRect.contains(aX, aY)) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					pSB.pressed();
					return true;
				}
			}
			for(int i=0; i<iNewsButtons.size(); i++) {
				pSB = ((SpriteButton)iNewsButtons.get(i));
				if(!pSB.isVision()) {
					continue;
				}
				int pX = pSB.getX();
				int pY = pSB.getY();
				Rect pLogicRect = pSB.getRect();
				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
						pX+pLogicRect.right, pY+pLogicRect.bottom);
				if (pRect.contains(aX, aY)) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					pSB.pressed();
					return true;
				}
			}
			return true;
		}
		else {
			dismiss();
			return true;
		}
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#OnProcessCmd(java.lang.Object, int, int)
	 */
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
		super.OnProcessCmd(socket, aMobileType, aMsgID);
	}

	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#ItemAction(int)
	 */
	@Override
	public boolean ItemAction(int aEventID) {
		if(!isVision || !ibTouch) return false;
		switch (aEventID) {
		case GameEventID.ESPRITEBUTTON_EVENT_BULLETIN_NEWS:
			if(currentPageIndex != PAGE_NEWS) {
				messageEvent(GameEventID.ESPRITEBUTTON_EVENT_BULLETIN_NEWS);
			}
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_BULLETIN_REWARD:
			if(currentPageIndex != PAGE_REWARD) {
				messageEvent(GameEventID.ESPRITEBUTTON_EVENT_BULLETIN_REWARD);
			}
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_BULLETIN_CLOSE:
			MyArStation.iPlayer.iReward = 0;
			dismiss();
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_BULLETIN_GET:
			MyArStation.iPlayer.iReward = 0;
			if(currentPageIndex == PAGE_REWARD && isobtain) {
				MyArStation.iGameProtocol.RequestAward(0);
			}
			dismiss();
			return true;

		default:
			return false;
		}
	}
	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#dimiss()
	 */
	@Override
	public void dimiss() {
		dismiss();
	}
	
	public void checkWebViewUrl(final String url, final WebView aWebView) {
		if(url == null || url.equals("")) {
			return;
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
				if (result == 200) {
					aWebView.loadUrl(url);
				} 
				else {
					aWebView.clearView();
				}
			}
		}.execute(url);
	}
	
	private class MyWebViewDownLoadListener implements DownloadListener {
		
		@Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
				long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			MyArStation.mGameMain.startActivity(intent);
		}
		
	}
}

