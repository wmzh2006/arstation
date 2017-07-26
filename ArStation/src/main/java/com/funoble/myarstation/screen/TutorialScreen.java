/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: tutorialScreen.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-5 下午06:03:36
 *******************************************************************************/
package com.funoble.myarstation.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.RelativeLayout;

import com.funoble.myarstation.game.CImageManager;
import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.GameButtonHandler;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.store.data.ImageAdapter;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.MyGallery;
import com.funoble.myarstation.utils.TPoint;
import com.funoble.myarstation.view.GameEventID;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.MessageEventID;


public class TutorialScreen extends GameView implements GameButtonHandler {
	/**
	 * 图片播放器
	 */
//	public MyGallery glPic;
//	private final List<Bitmap> bmList = new ArrayList<Bitmap>();
//	private final List<String> mlImageName = new ArrayList<String>();
//	private RelativeLayout mainRelativeLayout;
//	private int[] tutorial = {
//		R.drawable.tutorial0,	
//		R.drawable.tutorial1,	
//		R.drawable.tutorial2,	
//		R.drawable.tutorial3,	
//		R.drawable.tutorial4,	
//		R.drawable.tutorial5	
//	};
	
//	private Button btnOverLeap;
	
	private Project iGamePak = null;
	private Scene 	iWelComeScene = null;
	private Project iUIPak = null;
	private Scene 	iSceneOne = null;
	private Scene   iSceneTwo = null;
	private Scene   iSceneThr = null;
	private SpriteButton iButtonOne = null;
	private SpriteButton iButtonTwo = null;
	private SpriteButton iExitButton = null;
	
	private int iGameState = 0;
	private int iGameTick = 0;
	private long iLastTime = 0;
	
	/**
	 * construct
	 */
	public TutorialScreen() {
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#init()
	 */
	@Override
	public void init() {
		//载入Pak
		messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_DELETE_SYSCOM);
		iGamePak = CPakManager.loadUIPak();
		MyArStation.iImageManager.loadLoginBack();
		iWelComeScene = iGamePak.getScene(19);
		iWelComeScene.setViewWindow(0, 0, 800, 480);
		iWelComeScene.initDrawList();
		iWelComeScene.initNinePatchList(new int[] {R.drawable.gdbj});
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusic(SoundsResID.bg4, true);		
		loadResThread = new Thread(new Runnable() {
			
			
			@Override
			public void run() {
				loadTutorialPak();
			}
		});
		loadResThread.start();
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#releaseResource()
	 */
	@Override
	public void releaseResource() {
		iExitButton = null;
		iButtonTwo = null;
		iButtonOne = null;
		iSceneOne = null;
		iSceneTwo = null;
		iSceneThr = null;
		iUIPak = null;
		iWelComeScene = null;
		iGamePak = null;
		iGameState = 0;
	}

	private void loadTutorialPak() {
		//载入Pak
		iUIPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"tutorials_map.pak", false);
		//
		if(iUIPak == null) {
			return;
		}
		iSceneOne = iUIPak.getScene(0);
		if(iSceneOne == null) {
			return;
		}
		iSceneTwo = iUIPak.getScene(1);
		if(iSceneTwo == null) {
			return;
		}
		iSceneThr = iUIPak.getScene(2);
		if(iSceneThr == null) {
			return;
		}
		//
		iSceneOne.setViewWindow(0, 0, 800, 480);
		iSceneOne.initDrawList();
		//
		Map pMap = iSceneOne.getLayoutMap(Scene.SpriteLayout);
		Vector<?> pSpriteList = pMap.getSpriteList();
		for(int i=0; i<pSpriteList.size(); i++) {
			Sprite pSprite = (Sprite)pSpriteList.elementAt(i);
			int pX = pSprite.getX();
			int pY = pSprite.getY();
			int pEventID = GameEventID.ESPRITEBUTTON_EVENT_TUTORIAL_NEXT_ONE;//GameEventID.ESPRITEBUTTON_EVENT_TUTORIAL_OFFSET + pSprite.getIndex();
			TPoint pPoint = new TPoint();
			pPoint.iX = pX;
			pPoint.iY = pY;
			//精灵按钮
			SpriteButton pSpriteButton;
			pSpriteButton = new SpriteButton(pSprite);
			pSpriteButton.setEventID(pEventID);
			pSpriteButton.setPosition(pX, pY);
			pSpriteButton.setHandler(this);
			
//			System.out.println("Tutoral ButtonOne x=" + pX + " y=" + pY);

			
//			if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_TUTORIAL_NEXT_ONE) {
				iButtonOne = pSpriteButton;
//				iButtonOne.setVision(true);
//			}
		}
		//
		iSceneTwo.setViewWindow(0, 0, 800, 480);
		iSceneTwo.initDrawList();
		//
		pMap = iSceneTwo.getLayoutMap(Scene.SpriteLayout);
		pSpriteList = pMap.getSpriteList();
		for(int i=0; i<pSpriteList.size(); i++) {
			Sprite pSprite = (Sprite)pSpriteList.elementAt(i);
			int pX = pSprite.getX();
			int pY = pSprite.getY();
			int pEventID = GameEventID.ESPRITEBUTTON_EVENT_TUTORIAL_NEXT_TWO;//GameEventID.ESPRITEBUTTON_EVENT_TUTORIAL_OFFSET + pSprite.getIndex();
			TPoint pPoint = new TPoint();
			pPoint.iX = pX;
			pPoint.iY = pY;
			//精灵按钮
			SpriteButton pSpriteButton;
			pSpriteButton = new SpriteButton(pSprite);
			pSpriteButton.setEventID(pEventID);
			pSpriteButton.setPosition(pX, pY);
			pSpriteButton.setHandler(this);
			
//			System.out.println("Tutoral ButtonTwo x=" + pX + " y=" + pY);
			
//			if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_TUTORIAL_NEXT_TWO) {
				iButtonTwo = pSpriteButton;
//				iButtonTwo.setVision(true);
//			}
		}
		//
		//
		iSceneThr.setViewWindow(0, 0, 800, 480);
		iSceneThr.initDrawList();
		//
		pMap = iSceneThr.getLayoutMap(Scene.SpriteLayout);
		pSpriteList = pMap.getSpriteList();
		for(int i=0; i<pSpriteList.size(); i++) {
			Sprite pSprite = (Sprite)pSpriteList.elementAt(i);
			int pX = pSprite.getX();
			int pY = pSprite.getY();
			int pEventID = GameEventID.ESPRITEBUTTON_EVENT_TUTORIAL_RETURN;//GameEventID.ESPRITEBUTTON_EVENT_TUTORIAL_OFFSET + pSprite.getIndex();
			TPoint pPoint = new TPoint();
			pPoint.iX = pX;
			pPoint.iY = pY;
			//精灵按钮
			SpriteButton pSpriteButton;
			pSpriteButton = new SpriteButton(pSprite);
			pSpriteButton.setEventID(pEventID);
			pSpriteButton.setPosition(pX, pY);
			pSpriteButton.setHandler(this);
			
//			System.out.println("Tutoral ButtonThr x=" + pX + " y=" + pY);
			
//			if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_TUTORIAL_RETURN) {
				iExitButton = pSpriteButton;
//				iExitButton.setVision(true);
//			}
		}
	}
	
	/* 
	 * @see com.funoble.lyingdice.view.GameView#paintScreen(android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	public void paintScreen(Canvas g, Paint paint) {
		if(iGameState == 0) {
			g.drawBitmap(MyArStation.iImageManager.iBackLoginBmp,
					0,
					0,
					null);
			iWelComeScene.paint(g, 0, 0);
			g.drawText("欢迎来到凤智大话骰！我是酒吧服务生，", 
					128*ActivityUtil.ZOOM_X, 
					268*ActivityUtil.ZOOM_Y, 
					ActivityUtil.mNamePaint);
			g.drawText("让我介绍一下游戏的玩法吧！", 
					128*ActivityUtil.ZOOM_X, 
					298*ActivityUtil.ZOOM_Y, 
					ActivityUtil.mNamePaint);
			return;
		}
		else if(iGameState == 1) {
			if(iSceneOne == null) {
				return;
			}
			iSceneOne.paint(g, 0, 0);
			//
			iButtonOne.paint(g);
		}
		else if(iGameState == 2) {
			if(iSceneTwo == null) {
				return;
			}
			iSceneTwo.paint(g, 0, 0);
			//
			iButtonTwo.paint(g);
		}
		else {
			if(iSceneThr == null) {
				return;
			}
			iSceneThr.paint(g, 0, 0);
			//
			iExitButton.paint(g);
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#performL()
	 */
	@Override
	public void performL() {
		if(iGameState == 0) {
			iWelComeScene.handle();
			iGameTick ++;
			if(iGameTick == 8) {
				iLastTime = System.currentTimeMillis();
//				System.out.println("start loadTutorialPak()");
//				loadTutorialPak();
//				System.out.println("end loadTutorialPak()");
			}
			else if(iGameTick > 8) {
				if( (System.currentTimeMillis() - iLastTime)/1000 > 4) { 
					iGameState = 1;
//					System.out.println("end loadTutorialPak() start");
				}
			}
			return;
		}
		else if(iGameState == 1) {
			if(iSceneOne == null) {
				return;
			}
			iSceneOne.handle();
			//
			iButtonOne.perform();
		}
		else if(iGameState == 2) {
			if(iSceneTwo == null) {
				return;
			}
			iSceneTwo.handle();
			//
			iButtonTwo.perform();
		}
		else {
			if(iSceneThr == null) {
				return;
			}
			iSceneThr.handle();
			//
			iExitButton.perform();
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
//		if(iGameState == 0) {
//			return true;
//		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
			return true;
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onDown(android.view.MotionEvent)
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		pointerPressed(aX, aY);
		return true;
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
		if(iGameState == 0) {
			return true;
		}
		boolean result = false;	
		if(iGameState == 1) {
			Rect pLogicRect = iButtonOne.getRect();
			Rect pRect = new Rect();
			int pX = iButtonOne.getX();
			int pY = iButtonOne.getY();
			pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
				pX+pLogicRect.right, pY+pLogicRect.bottom);
//			System.out.println("Tutoral pressed x=" + aX + " y=" + aY);
//			System.out.println("Tutoral rect=" + pRect.toString());
			if (pRect.contains(aX, aY)) {
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
				iButtonOne.pressed();
				result = true;
			}
		}
		else if(iGameState == 2) {
			Rect pLogicRect = iButtonTwo.getRect();
			Rect pRect = new Rect();
			int pX = iButtonTwo.getX();
			int pY = iButtonTwo.getY();
			pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
				pX+pLogicRect.right, pY+pLogicRect.bottom);
//			System.out.println("Tutoral pressed x=" + aX + " y=" + aY);
//			System.out.println("Tutoral rect=" + pRect.toString());
			if (pRect.contains(aX, aY)) {
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
				iButtonTwo.pressed();
				result = true;
			}
		}
		else if(iGameState == 3) {
			Rect pLogicRect = iExitButton.getRect();
			Rect pRect = new Rect();
			int pX = iExitButton.getX();
			int pY = iExitButton.getY();
			pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
				pX+pLogicRect.right, pY+pLogicRect.bottom);
//			System.out.println("Tutoral pressed x=" + aX + " y=" + aY);
//			System.out.println("Tutoral rect=" + pRect.toString());
			if (pRect.contains(aX, aY)) {
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
				iExitButton.pressed();
				result = true;
			}
		}
		return result;
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
		case MessageEventID.EMESSAGE_EVENT_TO_GUIDE:
//			GameMain.mGameMain.mainLayout.removeAllViews();
//			mainRelativeLayout = (RelativeLayout) LayoutInflater.from(GameMain.mGameMain.getApplicationContext()).inflate(R.layout.tutorial, null);
//			GameMain.mGameMain.mainLayout.addView(mainRelativeLayout);
//			glPic = (MyGallery)mainRelativeLayout.findViewById(R.id.glPic);
//			for(int i = 0; i < tutorial.length; i++) {
//				Bitmap bm = BitmapFactory.decodeResource(GameMain.getInstance().getResources(), tutorial[i]);
//				bmList.add(bm);
//			}
//			glPic.setAdapter(new ImageAdapter(GameMain.getInstance(), bmList));
//			glPic.invalidate();
//			btnOverLeap = (Button)mainRelativeLayout.findViewById(R.id.btnOverleap);
//			btnOverLeap.setOnClickListener(this);
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
	
	/**
	 * Call this method to remove all child views from the ViewGroup.
	 */
	public void removeGalleryAllViews() {
//		if (glPic != null) {
//			glPic.removeAllViewsInLayout();
//			glPic.requestLayout();
//			glPic.invalidate();
//		}
	}

	@Override
	public boolean ItemAction(int aEventID) {
		// TODO Auto-generated method stub
		switch(aEventID) {
		case GameEventID.ESPRITEBUTTON_EVENT_TUTORIAL_NEXT_ONE:
			{
				iGameState = 2;
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_TUTORIAL_NEXT_TWO:
			{
				iGameState = 3;
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_TUTORIAL_RETURN:
			{	
				GameCanvas.getInstance().changeView(new HomeScreen());
			}
			break;
			
		default:
			break;
		}
		return false;
	}
}
