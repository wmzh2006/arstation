package com.funoble.myarstation.screen;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.ConstantUtil;
import com.funoble.myarstation.utils.Graphics;
import com.funoble.myarstation.utils.LogUtil;
import com.funoble.myarstation.utils.ResourceLoader;
import com.funoble.myarstation.view.GameView;

public class LogoScreen extends GameView {
	private Bitmap []logoBitmap;  //LOGO图片
	private int logFrm = 0;
	private boolean iReleaseSelf = false;
	private GameCanvas gameCanvas = null;
	private MyArStation iGameMain = null;

	public LogoScreen(MyArStation aGameMain){
		iGameMain = aGameMain;
		gameCanvas = GameCanvas.getInstance();
	}

	@Override
	public void init() {
		logoBitmap = new Bitmap[ConstantUtil.LOGO_BITMAP_LENGTH];
		logoBitmap[0] = ResourceLoader.createImage(R.drawable.log);
		logFrm = 0;
	}

	@Override
	public void paintScreen(Canvas g, Paint paint) {
//		LogUtil.Log_debug("logo paint!!!");
		paint.setColor(Color.WHITE);
		g.drawRect(0, 0, ActivityUtil.SCREEN_WIDTH, 
				ActivityUtil.SCREEN_HEIGHT, paint);
		Graphics.drawImage(g, logoBitmap[0], ActivityUtil.SCREEN_WIDTH >> 1, 
				ActivityUtil.SCREEN_HEIGHT >> 1, Graphics.HCENTER | Graphics.VCENTER);
	}

	@Override
	public void performL() {
		//Tools.debug("logo update!!!");
		//LogUtil.Log_debug("logo update!!!");
		logFrm++;
		if(logFrm == 10) {
			Tools.debug("LogoScreen::performL::start init()");
			iGameMain.init();
			Tools.debug("LogoScreen::performL::end init()");
		}
		else if(logFrm >= 12) {
			if(iGameMain.ibInit && iReleaseSelf == false){
				iReleaseSelf = true;
				Tools.debug("LogoScreen::performL::start init LoginScreen()");
				gameCanvas.changeView(new LoginScreen());
				Tools.debug("LogoScreen::performL::end init LoginScreen()");
			}
		}
	}

	@Override
	public void releaseResource() {
		// TODO Auto-generated method stub
		logoBitmap[0] = null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pointerPressed(int aX, int aY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pointerReleased(int aX, int aY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#OnProcessCmd(java.lang.Object, int, int)
	 */
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}


}
