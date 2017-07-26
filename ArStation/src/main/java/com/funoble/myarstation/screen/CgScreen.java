package com.funoble.myarstation.screen;

import java.io.File;
import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.funoble.myarstation.common.MD5;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.CBuilding;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.socket.GameProtocol;
import com.funoble.myarstation.socket.protocol.MBRspLogin;
import com.funoble.myarstation.socket.protocol.MBRspUpdate;
import com.funoble.myarstation.socket.protocol.MBRspVisitorLogin;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.Url;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.update.DownloadProgressListener;
import com.funoble.myarstation.update.Downloader;
import com.funoble.myarstation.update.FileDownloader;
import com.funoble.myarstation.update.LoadInfo;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.TPoint;
import com.funoble.myarstation.utils.URLAvailability;
import com.funoble.myarstation.view.GameEventID;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.MessageEventID;
import com.funoble.myarstation.view.MyProgressBar;

public class CgScreen extends GameView {
	private GameCanvas gameCanvas = null;
	private Project iUIPak = null;
	private Scene iScene = null;
	private int iGameTick = 0;
	
	@Override
	public void init() {
		gameCanvas = GameCanvas.getInstance();
		loadUIPak();
	}
	
	@Override
	public void releaseResource() {
		iScene = null;
		iUIPak = null;
	}
	
	@Override
	public void paintScreen(Canvas g, Paint paint) {
		g.drawBitmap(MyArStation.iImageManager.iBackLoginBmp,
				0,
				0,
				null);
		if(iScene != null) {
			iScene.paint(g, 0, 0);
		}
	}

	@Override
	public void performL() {
		if(iScene == null) {
			return;
		}
		iScene.handle();
		iGameTick ++;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
//		System.out.println("ProgressScreen onDown event.getAction() = " +e.getAction());
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		pointerPressed(aX, aY);
		return true;
	}

	@Override
	public boolean onLongPress(MotionEvent e) {
//		System.out.println("ProgressScreen onLongPress event.getAction() = " +e.getAction());
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		pointerReleased(aX, aY);
		return true;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
//		System.out.println("ProgressScreen onSingleTapUp event.getAction() = " +e.getAction());
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		pointerReleased(aX, aY);
		return true;
	}

	@Override
	public boolean pointerPressed(int aX, int aY) {
//		System.out.println("CGScreen pointerPressed");
		return true;
	}

	@Override
	public boolean pointerReleased(int aX, int aY) {
//		System.out.println("CGScreen pointerReleased");
		if(iGameTick > 10) {
			ItemAction(0);
		}
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
	}
	
	@Override
	public void handleMessage(Message msg) {
	}
	
	
	public boolean ItemAction(int aEventID) {
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
//		messageEvent(MessageEventID.EMESSAGE_EVENT_TO_LOGIN);
		gameCanvas.changeView(new LoginScreen());
		return true;
	}
	
	private void loadUIPak() {
		iUIPak = MyArStation.iPakManager.loadUIPak();
		if (iUIPak != null) {
			iScene = iUIPak.getScene(0);
			if(iScene != null) {
				Vector<?> spriteList = iScene.getLayoutMap(Scene.BuildLayout).getSpriteList();
				CBuilding building = ((CBuilding)spriteList.elementAt(0));
				building.setRepeat(false);
				iScene.setViewWindow(0, 0, 800, 480);
				iScene.initDrawList();
			}
		}
	}
	
}
