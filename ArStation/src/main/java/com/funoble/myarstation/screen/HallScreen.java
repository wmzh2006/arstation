package com.funoble.myarstation.screen;


import java.util.List;
import java.util.Vector;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.BitmapFactory.Options;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.GameButtonHandler;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.gamelogic.SpriteButtonSelect;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.ConstantUtil;
import com.funoble.myarstation.utils.Graphics;
import com.funoble.myarstation.utils.ResourceLoader;
import com.funoble.myarstation.utils.TPoint;
import com.funoble.myarstation.view.GameEventID;
import com.funoble.myarstation.view.GameView;

import fr.tvbarthel.games.chasewhisply.HomeActivity;

public class HallScreen extends GameView implements GameButtonHandler {
	private Bitmap []logoBitmap;  //LOGO图片
	private Dialog 		 dialog;
	private GameCanvas iGameCanvas = null;
	private Context iContext = null;
	private MyArStation iArStation = null;
	private Project iUIPak = null;
	private Scene iScene = null;
//	private Scene iSceneLoading = null;
	private Vector<SpriteButton> iSpriteButtonList = new Vector<SpriteButton>();//精灵按钮 列表
//	private int iGameState = 0; //0 --- 出场    1 --- 正常    2 --- 正在登录    3 --- 登录成功
	
	
	public HallScreen(MyArStation aArStation){
//		gameCanvas = GameCanvas.getInstance();
		iArStation = aArStation;
	}

	@Override
	public void init() {
//		logoBitmap = new Bitmap[ConstantUtil.LOGO_BITMAP_LENGTH];
//		logoBitmap[0] = ResourceLoader.createImage(R.drawable.log);
		//
		iGameCanvas = GameCanvas.getInstance();
		iContext = iArStation.getContext();
		//载入背景图
		MyArStation.iImageManager.loadLoginBack();
		//
		loadUIPak();
		//
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusic(SoundsResID.bg3, true);
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
		for(int i=0; i<iSpriteButtonList.size(); i++) {
			((SpriteButton)iSpriteButtonList.elementAt(i)).paint(g);
		}
		g.drawText("请选择游戏", 88*ActivityUtil.ZOOM_X, 58*ActivityUtil.ZOOM_Y, ActivityUtil.mLoading);
	}

	@Override
	public void performL() {
		if(iScene != null) {
			iScene.handle();
		}
		for(int i=0; i<iSpriteButtonList.size(); i++) {
			((SpriteButton)iSpriteButtonList.elementAt(i)).perform();
		}
	}

	@Override
	public void releaseResource() {
		// TODO Auto-generated method stub
		logoBitmap[0] = null;
//		MyArStation.iImageManager.releaseLoginBack();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showReturnLoginConfirm();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		boolean result= false;
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		result = pointerPressed(aX, aY);
		return result;
//		return false;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		boolean result= false;
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		result = pointerReleased(aX, aY);
		return result;
//		return false;
	}

	@Override
	public boolean pointerPressed(int aX, int aY) {
		// TODO Auto-generated method stub
		Tools.debug("ProgressScreen pointerPressed");
		boolean result= false;
		Rect pRect = new Rect();
		SpriteButton pSB;
		for(int i=0; i<iSpriteButtonList.size(); i++) {
			pSB = ((SpriteButton)iSpriteButtonList.elementAt(i));
			if(!pSB.isVision()) {
				continue;
			}
			int pX = pSB.getX();
			int pY = pSB.getY();
			Rect pLogicRect = pSB.getRect();
			pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
					pX+pLogicRect.right, pY+pLogicRect.bottom);
			if (pRect.contains(aX, aY)) {
				pSB.pressed();
				Tools.debug("ProgressScreen pointerPressed  pSB.pressed() EventID=" + pSB.getEventID());
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
				result = true;
				break;
			}
		}
		return result;
		//return false;
	}

	@Override
	public boolean pointerReleased(int aX, int aY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		boolean result= false;
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		result = pointerReleased(aX, aY);
		return result;
//		return false;
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

	//按下按钮
	public boolean ItemAction(int aEventID) {
		switch (aEventID) {
		case GameEventID.ESPRITEBUTTON_EVENT_GAMEHALL_GAMEA:
			{
				//启动另外程序
			//	openApp("mobi.byss.gun3");
				//切换到帮助页面
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).stopMusic(); //playMusic(SoundsResID.bg1, true);
				String tGameUrl = "http://www.4399.com/flash/187309_3.htm";
				GameCanvas.getInstance().changeView( new HtmlGameScreen(tGameUrl) );
			}
			break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_GAMEHALL_GAMEB:
		{
			//启动另外程序
		//	openApp("mobi.byss.gun3");
			//切换到帮助页面
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).stopMusic(); //playMusic(SoundsResID.bg1, true);
			String tGameUrl = "http://www.4399.com/flash/182103_3.htm";
			GameCanvas.getInstance().changeView( new HtmlGameScreen(tGameUrl) );
		}
		break;
			
		case GameEventID.ESPRITEBUTTON_EVENT_GAMEHALL_GAMEC:
		{
			//启动另外程序
		//	openApp("mobi.byss.gun3");
			//切换到帮助页面
			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).stopMusic(); //playMusic(SoundsResID.bg1, true);
			String tGameUrl = "http://www.4399.com/flash/180775_3.htm";//"http://www.4399.com/flash/187299_2.htm";
			GameCanvas.getInstance().changeView( new HtmlGameScreen(tGameUrl) );
		}
		break;
		
		case GameEventID.ESPRITEBUTTON_EVENT_GAMEHALL_GAMED:
		{
			//启动另外程序
		//	openApp("mobi.byss.gun3");
			//切换到帮助页面
//			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).stopMusic(); //playMusic(SoundsResID.bg1, true);
//			String tGameUrl = "http://www.4399.com/flash/186359_2.htm";//http://www.4399.com/flash/186882_4.htm";
//			GameCanvas.getInstance().changeView( new HtmlGameScreen(tGameUrl) );

			iArStation.startActivity(new Intent(iArStation, HomeActivity.class));
		}
		break;
		
		default:
			break;
		}
		
		return true;
	}
	
	
	//---------------------------------------------------------
	private void showReturnLoginConfirm() {
		//根据自己的状态，给出相应提示
		if(dialog == null) {
			dialog = new Dialog(MyArStation.mGameMain, R.style.MSGDialogStyle);
			dialog.setContentView(R.layout.new_dialog);
			dialog.setCancelable(true);
			TextView title = (TextView)dialog.findViewById(R.id.dialog_title);
			title.setText(R.string.Return_LoginScreen_tip_title);
			TextView content = (TextView)dialog.findViewById(R.id.dialog_message);
			StringBuffer temp = new StringBuffer(MyArStation.iPlayer.iTips);
			int len = temp.length();
			temp.append("\n"+MyArStation.mGameMain.getResources().getString(R.string.Return_LoginScreen_tip_Content));
			SpannableStringBuilder style=new SpannableStringBuilder(temp);   
			style.setSpan(new ForegroundColorSpan(0xff99cccc), 0, len ,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
			content.setText(style);
			Button commit = (Button)dialog.findViewById(R.id.ok);
			commit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
//					messageEvent(MessageEventID.EMESSAGE_EVENT_TO_LOGIN);
					MyArStation.mGameMain.quit();
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
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
//		dialog = new AlertDialog.Builder(GameMain.mGameMain).setTitle(R.string.Return_LoginScreen_tip_title).setMessage(
//				GameMain.mGameMain.getString(R.string.Return_LoginScreen_tip_Content)).setCancelable(false)
//				.setPositiveButton(R.string.Return_LoginScreen_Ok, new DialogInterface.OnClickListener()
//				{
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
////								hideSysCom();
//								messageEvent(MessageEventID.EMESSAGE_EVENT_TO_LOGIN);
////								GameCanvas.getInstance().changeView(new LoginScreen());
//								MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
//					}
//				}).setNegativeButton(R.string.Return_LoginScreen_Cancel, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
//					}
//				}).create();
		dialog.show();
//		dialog.setCancelable(true);
	}
	
	private void loadUIPak() {
		iUIPak = CPakManager.loadUIPak();
		if (iUIPak != null) {
			iScene = iUIPak.getScene(2);
			if(iScene != null) {
				iScene.setViewWindow(0, 0, 800, 480);
				iScene.initDrawList();
				iScene.initNinePatchList(new int[] {R.drawable.new_ttitle});//R.drawable.btmck, R.drawable.new_text_bg, 
				Map pMap = iScene.getLayoutMap(Scene.SpriteLayout);
				Vector<?> pSpriteList = pMap.getSpriteList();
				Sprite pSprite;
				for (int i=0; i<pSpriteList.size(); i++) {
					pSprite = (Sprite)pSpriteList.elementAt(i);
					int pX = pSprite.getX();
					int pY = pSprite.getY();
					int pEventID = GameEventID.ESPRITEBUTTON_EVENT_GAMEHALL + pSprite.getIndex();
					TPoint pPoint = new TPoint();
					pPoint.iX = pX;
					pPoint.iY = pY;
					//精灵按钮
					SpriteButton pSpriteButton;
					pSpriteButton = new SpriteButton(pSprite);
					pSpriteButton.setEventID(pEventID);
					pSpriteButton.setPosition(pX, pY);
					pSpriteButton.setHandler(this);
					iSpriteButtonList.addElement(pSpriteButton);
				}
			}
		}
	}
	
	private void openApp(String packageName) {
	    final PackageManager packageManager = iContext.getPackageManager();//获取packagemanager
	    PackageInfo pi = null;
	    try {  
	    	pi = packageManager.getPackageInfo(packageName, 0);  
	    } catch (NameNotFoundException e) {  
	    	pi = null;  
	    	e.printStackTrace();  
	    }  
	    if(pi == null) {
	    	return;
	    }
	    
	    Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
	    resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	    resolveIntent.setPackage(pi.packageName);
	    List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0);
	    ResolveInfo ri = apps.iterator().next();
	    if (ri != null ) {
	        String tempName = ri.activityInfo.packageName;
	        String className = ri.activityInfo.name;
	        Intent intent = new Intent(Intent.ACTION_MAIN);
	        intent.addCategory(Intent.CATEGORY_LAUNCHER);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	        ComponentName cn = new ComponentName(tempName, className);
	        intent.setComponent(cn);
	        iArStation.startActivity(intent);
	    }
	}
	
	
	
}
