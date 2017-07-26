package com.funoble.myarstation.game;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.ConstantUtil;
import com.funoble.myarstation.view.GameView;

public class GameCanvas extends SurfaceView implements SurfaceHolder.Callback, Runnable{
	private GameView currentView;
	private GameView tempView;
	
	private static GameCanvas canvas = null;
//	private GameWorld gameWorld = null;
	private Paint mPaint;
	
	public boolean isRunning = false;
	public Thread iTh;
	private boolean isStop = false;
	protected SurfaceHolder surfaceHolder = null;
	
	//算FPS
	private long iStartTime = 0;
	private byte iFPSTick = 0;
	private String iFPSStr = "FPS:";
	
	protected static ProgressDialog	mProgressDialog;
	
	public static GameCanvas getInstance(){
		
		if(canvas == null){
			canvas = new GameCanvas(MyArStation.mGameMain);
		}

		return canvas;
	}

	public GameCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		canvas = this;
		mPaint = ActivityUtil.mPaint;
//		this.setKeepScreenOn(true);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
//		mPaint = new Paint();
//		mPaint.setAntiAlias(true);
//		this.setLongClickable(true);
//		getHolder().addCallback(this);
	}
	
	public GameCanvas(Context context) {
		super(context);
		canvas = this;
		mPaint = ActivityUtil.mPaint;
//		this.setKeepScreenOn(true);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
//		mPaint = new Paint();
//		mPaint.setAntiAlias(true);
//		this.setLongClickable(true);
//		getHolder().addCallback(this);
	}
	
	public void changeView(GameView view){
		tempView = view;
		if(tempView != currentView){
			//释放资源
			releaseCurrentScreenResource();		
			//切换view
			currentView = view;
			//初始化view
			initCureentScreet();
		}
	}
	
	private void initCureentScreet(){
		if(currentView != null){
			currentView.init();
		}
	}

	private void releaseCurrentScreenResource(){
		try {
			if(currentView != null){
				currentView.releaseResource();
				currentView = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Override
//	protected void onDraw(Canvas canvas) {
//		// TODO Auto-generated method stub
//		if(currentView != null){
//			currentView.paintScreen(canvas, paint);
//		}
//	}

	private void countFPS() {
//		iFPSTick++;
//		if (iFPSTick >= ConstantUtil.FPS) {
//			iFPSTick = 0;
//			long tempCurTime = System.nanoTime();
//			long tempSpan = tempCurTime - iStartTime;
//			iStartTime = tempCurTime;
//			double tempFPS = Math.round(100000000000.0/tempSpan*ConstantUtil.FPS)/100.0;
//			iFPSStr = "FPS:" + tempFPS;
//		}
		iFPSTick++;
		long timeSt = System.currentTimeMillis();
		if(timeSt - iStartTime >= 1000) {
			iStartTime = timeSt;
			iFPSStr = "FPS:" + iFPSTick;
			iFPSTick = 0;
		}
	}
	
	private void drawFPS(Canvas aCanvas) {
//		int tOldColor = mPaint.getColor();
//		mPaint.setColor(Color.RED);
//		aCanvas.drawText(iFPSStr, 0, 20, mPaint);
//		mPaint.setColor(tOldColor);
	}
	
	public void paint(Canvas canvas){
		if(currentView != null){
			currentView.paintScreen(canvas, mPaint);
		}
		drawFPS(canvas);
	}

	//画布逻辑更新
	public void update(){
		if(currentView != null){
			currentView.performL();
		}
	}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (currentView != null) {
//    		Tools.debug("GameCanvas onTouchEvent event.getAction() = " +event.getAction());
    		return currentView.onTouchEvent(event);
    	}
    	return false;
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
//		Log.v("GameCanvas","onKeyDown = " + keyCode);
		return currentView.onKeyDown(keyCode, event);
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
////			onDestroy();
//			quit();
////			gameWorld.isRunning = false;
////			isRunning = false;
////			surfaceDestroyed(getHolder());
////			return true;
//		}
//		return super.onKeyDown(keyCode, event);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
//		ActivityUtil.SCREEN_WIDTH = width > height ? width : height;
//		ActivityUtil.SCREEN_HEIGHT = height < width ? height : width;
	}

	//当界面创建的时候
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		initGameWorld();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
//		gameWorld.isRunning = false;
//		isRunning = false;
//		MediaManager.getMediaManagerInstance().stopMusic();
		if (MyArStation.getInstance().ibRelease) {
			if(currentView != null){
				currentView.releaseResource();
			}
			if(MyArStation.iHttpDownloadManager != null){
				MyArStation.iHttpDownloadManager.removeTask();
//				Tools.debug("remove task");
			}
			System.gc();
		}
//		Log.v("GameCanvas", "surfaceDestroyed");
	}
	
	public void quit() {
		MyArStation.getInstance().quit();
	}
	
	public void run() {
		Canvas canvas = null;
		iStartTime = System.currentTimeMillis();
		while(isRunning){
			if(!isStop){
				countFPS();
				long timeSt = System.currentTimeMillis();
				try{
					synchronized (surfaceHolder) {
						canvas = surfaceHolder.lockCanvas(null);
						update();
						if (canvas != null) {
							paint(canvas);
						}
					}
				}catch (Exception e) {
//					Tools.debug("run error!!! " + e);
					e.printStackTrace();
				}finally{
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
				long timeEnd = System.currentTimeMillis() - timeSt;
				if(timeEnd < ConstantUtil.MSPF){
					try {
						Thread.sleep(ConstantUtil.MSPF - timeEnd);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
//		gameCanvas.quit();
	}
	
	public void initGameWorld(){
		if(iTh != null) {
			return;
		}
		isRunning = true;
		isStop = false;		
		Thread thread = new Thread(this);
		iTh = thread;
		thread.start();
		Tools.debug("initGameWorld" + iTh);
	}
	
	public boolean onDown(MotionEvent e) {
		if (currentView != null) {
			return currentView.onDown(e);
		}
		return false;
	}
	
	public boolean onLongPress(MotionEvent e) {
		if (currentView != null) {
			return currentView.onLongPress(e);
		}
		return false;
	}

	public boolean onSingleTapUp(MotionEvent e) {
		if (currentView != null) {
			return currentView.onSingleTapUp(e);
		}
		return false;
	}
	
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (currentView != null) {
			return currentView.onFling(e1, e2, velocityX, velocityY);
		}
		return false;
	}
	
	/**
	 * 显示loading对话框
	 */
	public final void showProgressDialog(String tip, boolean cancelable)
	{
		mProgressDialog = new ProgressDialog(getContext());
		mProgressDialog.setMessage(tip);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(cancelable);
		mProgressDialog.show();
	}

	/**
	 * 销毁loading对话框
	 */
	public final void removeProgressDialog()
	{
		if (mProgressDialog != null)
		{
//			Tools.debug("GameCanvas removeProgressDialog.....");
			mProgressDialog.dismiss();
		}
	}
	
	public GameView getCurrentView() {
		return currentView;
	}
	
	public void stopThread() {
		if(iTh != null && iTh.isAlive()) {
			isRunning = false;
		}
	}
	
	public void onPause() {
		isStop = true;
	}
	
	public void onResume() {
		isStop = false;
	}
}
