package com.funoble.myarstation.view;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.resmanager.CPakManager;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class GameView{
	/**
	 * 视图ID
	 */
	public int					mViewId = -1;
	protected  static ProgressDialog	mProgressDialog;
	protected Thread loadResThread;
	
	public abstract void init();			//初始化当前屏幕
	public abstract void releaseResource(); //释放当前屏幕资源
	public abstract void paintScreen(Canvas g, Paint paint);//屏幕绘制方法
	public abstract void performL();
    public abstract boolean onTouchEvent(MotionEvent event);
	public abstract boolean onKeyDown(int keyCode, KeyEvent event);
	public abstract boolean onDown(MotionEvent e);
	public abstract boolean onLongPress(MotionEvent e);
	public abstract boolean onSingleTapUp(MotionEvent e);
	public abstract boolean pointerPressed(int aX, int aY);
	public abstract boolean pointerReleased(int aX, int aY);
	public abstract boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);
	
	public abstract void handleMessage(Message msg);
	
	public abstract void OnProcessCmd(Object socket, int aMobileType, int aMsgID);
	
	/**
	 * 显示loading对话框
	 */
	protected final void showProgressDialog(int resId, boolean cancelable)
	{
		showProgressDialog(MyArStation.mGameMain.getString(resId), cancelable);
	}

	/**
	 * 显示loading对话框
	 */
	protected final void showProgressDialog(int resId)
	{
		showProgressDialog(MyArStation.mGameMain.getString(resId), true);
	}

	/**
	 * 显示loading对话框
	 */
	public final void showProgressDialog(String tip, boolean cancelable)
	{
		removeProgressDialog();
//		Tools.debug("GameView showProgressDialog.....");
		mProgressDialog = ProgressDialog.createDialog(MyArStation.mGameMain);
		mProgressDialog.setMessage(tip);
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
//			Tools.debug("GameView removeProgressDialog.....");
			mProgressDialog.dismiss();
		}
	}

	protected void messageEvent(int aEventID, Object aMsg) {
		Message tMes = new Message();
		tMes.what = aEventID;
		tMes.obj = aMsg;
		MyArStation.getInstance().iHandler.sendMessage(tMes);
	}
	
	protected void messageEvent(int aEventID) {
		messageEvent(aEventID, null);
	}
	
	protected void showMessage(String msg) {
		if(msg == null || msg.length() <=0 ) {
			return;
		}
		Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), msg);
	}
	

	public Scene LoadLoading() {
		Scene sceneLoading = null;
		Project loadingScene = CPakManager.loadLoading();
		if(loadingScene != null) {
			sceneLoading = loadingScene.getScene(0);
			if(sceneLoading != null) {
				sceneLoading.setViewWindow(0, 0, 800, 480);
				sceneLoading.initDrawList();
			}
		}
		return sceneLoading;
	}
}
