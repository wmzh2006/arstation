package com.funoble.myarstation.view;

import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
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
public class RoomDetailDialog extends Dialog implements
		android.view.View.OnClickListener {

	private String baseUrl = "http://www.funnoble.com/help/%.html";
	private String defualUrl = "http://www.funnoble.com/help/0.html";
	private ViewGroup mLayout;

	private WebView 	wvAd;
	private Button 		btnReturn;
	private TextView 		tvTitle;
	private RelativeLayout rlLayout;
	private ProgressBar progressBar1;
	private String Url;
	/**
	 * @param context
	 */
	public RoomDetailDialog(Context context) {
		super(context, R.style.MSGDialogStyle);

		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_roomdetial, null);

		setContentView(mLayout);

		rlLayout = (RelativeLayout) mLayout.findViewById(R.id.rl2);
		wvAd = ((WebView) rlLayout.findViewById(R.id.wvad));
		progressBar1 = ((ProgressBar) rlLayout.findViewById(R.id.progressBar1));
		WebSettings settings = wvAd.getSettings();
        settings.setSupportZoom(false);          //支持缩放
        settings.setBuiltInZoomControls(true);  //启用内置缩放装置
        settings.setJavaScriptEnabled(true);    //启用JS脚本
        wvAd.setWebViewClient(new WebViewClient() {
            //当点击链接时,希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);  //加载新的url
                progressBar1.setVisibility(View.VISIBLE);
                return true;    //返回true,代表事件已处理,事件流到此终止
            }

			/* 
			 * @see android.webkit.WebViewClient#onReceivedError(android.webkit.WebView, int, java.lang.String, java.lang.String)
			 */
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
//				 view.loadUrl("http://www.funnoble.com/index.php");  //加载新的url
				view.loadUrl(defualUrl);
				Tools.debug("onReceivedError" + description);
//	             progressBar1.setVisibility(View.VISIBLE);
//				super.onReceivedError(view, errorCode, description, failingUrl);
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
                	progressBar1.setVisibility(View.INVISIBLE);
                }
            }
        });
//		wvAd.loadUrl("http://www.funnoble.com/index.php");
//		wvAd.loadUrl(Url);
		wvAd.requestFocus();
		
		btnReturn = (Button)mLayout.findViewById(R.id.btnadReturn);
		btnReturn.setOnClickListener(this);
		tvTitle = (TextView)mLayout.findViewById(R.id.tvtitle);
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
		// p.alpha = 1.0f; //设置本身透明度
//		// p.dimAmount = 0.0f; //设置黑暗度
//
//		getWindow().setAttributes(p); // 设置生效
		getWindow().setGravity(Gravity.CENTER | Gravity.TOP); // 设置靠右对齐
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
		default:
			break;
		}
		this.dismiss();
	}

	public void Show(String url, String title) {
		if(url == null || url.equals("")) {
			return; 
		}
		setTitle(title+"帮助");
		Url = baseUrl.replace("%", url);
		checkWebViewUrl();
		wvAd.requestFocus();
		this.show();
	}
	
	public void setTitle(String title) {
		tvTitle.setText(title);
	}
	

	public void checkWebViewUrl() {
		if(Url == null || Url.equals("")) {
			Url = defualUrl;
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
					wvAd.loadUrl(defualUrl);
				} else {
					wvAd.loadUrl(Url);
				}
				Tools.debug("Url " + Url);
			}
		}.execute(Url);
	}
}
