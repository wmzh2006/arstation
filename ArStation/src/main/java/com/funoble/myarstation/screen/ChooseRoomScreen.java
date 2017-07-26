/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: SelectRoomScreen.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-7-18 下午09:41:53
 *******************************************************************************/
package com.funoble.myarstation.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.common.Util;
import com.funoble.myarstation.data.cach.RoomData;
import com.funoble.myarstation.data.cach.RoomListData;
import com.funoble.myarstation.game.CPlayer;
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
import com.funoble.myarstation.socket.protocol.MBRspLeaveRoom;
import com.funoble.myarstation.socket.protocol.MBRspLoginVipRoom;
import com.funoble.myarstation.socket.protocol.MBRspRoomList;
import com.funoble.myarstation.socket.protocol.MBRspRoomListB;
import com.funoble.myarstation.socket.protocol.MBRspRoomListC;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.TPoint;
import com.funoble.myarstation.view.GameEventID;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.MessageEventID;
import com.funoble.myarstation.view.RoomDetailDialog;
import com.funoble.myarstation.view.VipRoomDialog;

public class ChooseRoomScreen extends GameView implements OnClickListener, GameButtonHandler {

	private int	iGameState = 0; //pak在数据动画
	private Scene iSceneLoading = null;
	private Scene iSceneTitle = null;
	private Scene iSceneRoom = null;
	private Vector<SpriteButton> iSpriteButtonListTile = new Vector<SpriteButton>();//精灵按钮 列表
	private Vector<SpriteButton> iSpriteListIcon = new Vector<SpriteButton>();//精灵按钮 列表
	//资源
	private Project iUIPak = null;
	Sprite headSprite;
	Rect headRect;
//	Bitmap headBitmap;
	Paint namePaint;
	Paint moneypPaint;
	Paint roomNamePaint;
	Paint roomLimitPaint;
	Paint roomBetPaint;
	//
	Rect logicRect;
	Rect photoRect;
	Rect roomNameRect;
	Rect roomLimitRect;
	Rect roomBetRect;
	//
	Rect selectTabRect;
	Sprite selectSprite;
	ArrayList<SpriteButton> selectSpriteList = new ArrayList<SpriteButton>();
	ArrayList<SpriteButton> vipselectSpriteList = new ArrayList<SpriteButton>();
	Sprite betSprite;
	Sprite vipStatusSprite;
	ArrayList<SpriteButton> betSpriteList = new ArrayList<SpriteButton>();
	ArrayList<SpriteButton> vipbetSpriteList = new ArrayList<SpriteButton>();
	
	ArrayList<RoomListData> iRoomList;
	ArrayList<RoomListData> iVipRoomList;
	int roomSelectW = 0;
	
	//
	int roomType = 0;//普通房
	ArrayList<Point> photoArrayList = new ArrayList<Point>();
	ArrayList<Point> roomNameArrayList = new ArrayList<Point>();
	ArrayList<Point> roomLimitArrayList = new ArrayList<Point>();
	ArrayList<Point> roomBetArrayList = new ArrayList<Point>();
	HashMap<String, Bitmap> roomBiArrayList = new HashMap<String, Bitmap>();
//	
	ArrayList<Point> vipphotoArrayList = new ArrayList<Point>();
	ArrayList<Point> viproomNameArrayList = new ArrayList<Point>();
	ArrayList<Point> viproomLimitArrayList = new ArrayList<Point>();
	ArrayList<Point> viproomBetArrayList = new ArrayList<Point>();
	SpriteButtonSelect changesButtonSelect;
	private Rect  gbRect = new Rect((int)(328* ActivityUtil.ZOOM_X), (int)(173 * ActivityUtil.ZOOM_Y), (int)(548* ActivityUtil.ZOOM_X), (int)(210 * ActivityUtil.ZOOM_Y));
	//选择房间
	int pressedX = 0;
	int itemW = ActivityUtil.SCREEN_WIDTH;
	int maxCount = 0;
	int minCount = 0;
	boolean touch = false;
	boolean sroll = false;
	int offsetX = 0;
	int lastOffsetX = 0;
	int distW = (int)(itemW);
	int iRunSpeed = 0;
	int runTick = 0;
	int pressTick = 0;
	int runDirct = 0;
	int currentIndex = 0;
	int oldIndex = -1;
	int touchTick = 0;
	int roomCurrentIndex = 0;
	int vipRoomCurrentIndex = 0;
	
	private CPlayer		iPlayerInfo = MyArStation.iPlayer;
	
	int cameraX = -ActivityUtil.SCREEN_WIDTH;
	int speed = 0;
	
	int selectX = ActivityUtil.SCREEN_WIDTH;
	int selectSpeed = 0;
	RoomData pair;
//	private ImageView ivAd;
	private String Url;
	private String picName;
	private int tMusicID = 0;//当前音乐id
	
	VipRoomDialog vipRoomDialog;
	RoomDetailDialog roomDetailDialog;
	//
	/**
	 * construct
	 */
	public ChooseRoomScreen() {
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#init()
	 */
	@Override
	public void init() {
		namePaint = new Paint();
		namePaint.setTextSize(38 * ActivityUtil.ZOOM_X);     //设置字体大小
		namePaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		namePaint.setColor(Color.WHITE);
		namePaint.setTypeface(Typeface.DEFAULT_BOLD);
		namePaint.setTextAlign(Align.CENTER);
		namePaint.setShadowLayer(0.5f, 1f, 1f, Color.GRAY); 
		
		moneypPaint = new Paint();
		moneypPaint.setTextSize(20 * ActivityUtil.ZOOM_X);     //设置字体大小
		moneypPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		moneypPaint.setColor(Color.WHITE);
//		moneypPaint.setTextAlign(Align.LEFT);
		moneypPaint.setShadowLayer(0.5f, 1f, 1f, Color.GRAY); 
		
		
		roomNamePaint = new Paint();
		roomNamePaint.setTextSize(34 * ActivityUtil.ZOOM_X);     //设置字体大小
		roomNamePaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		roomNamePaint.setColor(Color.WHITE);
		roomNamePaint.setTypeface(Typeface.DEFAULT_BOLD);
//		roomNamePaint.setTextAlign(Align.CENTER);
		roomNamePaint.setShadowLayer(0.5f, 1f, 1f, Color.GRAY); 
		
		
		roomLimitPaint = new Paint();
		roomLimitPaint.setTextSize(24 * ActivityUtil.ZOOM_X);     //设置字体大小
		roomLimitPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		roomLimitPaint.setColor(Color.GREEN);
		roomLimitPaint.setTextAlign(Align.LEFT);
		roomLimitPaint.setShadowLayer(0.5f, 1f, 1f, Color.GRAY); 
		
		
		roomBetPaint = new Paint();
		roomBetPaint.setTextSize(24 * ActivityUtil.ZOOM_X);     //设置字体大小
		roomBetPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		roomBetPaint.setColor(Color.WHITE);
		roomBetPaint.setTextAlign(Align.LEFT);
		roomBetPaint.setShadowLayer(0.5f, 1f, 1f, Color.GRAY); 
		
		
		//转成对应屏幕分辨率的值
		cameraX *= (float)ActivityUtil.SCREEN_WIDTH / (float)ActivityUtil.SCREEN_WIDTH_STANDARD; 
		speed = (int)(cameraX / 6);
		
		selectX *= (float)ActivityUtil.SCREEN_WIDTH / (float)ActivityUtil.SCREEN_WIDTH_STANDARD; 
		selectSpeed = (int)(selectX / 6);
		loadUIPak();
		resetRoll(0);
		MyArStation.iImageManager.loadRoomBack("login_back");
		requestRoomListc();
		messageEvent(MessageEventID.EMESSAGE_EVENT_TO_SELECTROOM);
	}

	
	/* 
	 * @see com.funoble.lyingdice.view.GameView#releaseResource()
	 */
	@Override
	public void releaseResource() {
		iSceneLoading = null;
		iUIPak = null;
		iSceneLoading = null;
		iSceneTitle = null;
		iSceneRoom = null;
		headSprite = null; 
		headRect = null;
		namePaint = null;
		moneypPaint = null;
		selectSpriteList = null;
		vipselectSpriteList = null;
		betSpriteList = null;
		vipbetSpriteList = null;
		if(roomBiArrayList != null) {
			Iterator<String> iterator = roomBiArrayList.keySet().iterator();    
			while (iterator.hasNext()) {
				Bitmap tem = roomBiArrayList.get(iterator.next());
				tem.recycle();
				tem = null;
			}
			roomBiArrayList.clear();
			roomBiArrayList = null;
		}
		if(vipRoomDialog != null) {
			vipRoomDialog.dismiss();
			vipRoomDialog = null;
		}
		if(roomDetailDialog != null) {
			roomDetailDialog.dismiss();
			roomDetailDialog = null;
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#paintScreen(android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	public void paintScreen(Canvas g, Paint paint) {
		g.drawBitmap(MyArStation.iImageManager.iBackLoginBmp,
				0,
				0,
				null);
		//载入数据
		if(iGameState == 0) {
			if(iSceneLoading != null) {
				iSceneLoading.paint(g, 0, 0);
//				g.drawText("正在读取数据", 278*ActivityUtil.ZOOM_X, 288*ActivityUtil.ZOOM_Y, ActivityUtil.mLoading);
			}
		}
		else {
			paintTitle(g);
			paintSelectRoom(g);
		}
	}

	/**
	 * @param g
	 */
	private void paintSelectRoom(Canvas g) {
		if(iSceneRoom != null) {
			iSceneRoom.paint(g, 0, 0);
			g.save();
//			Paint paint1 = new Paint();
//			paint1.setStyle(Style.STROKE);
//			paint1.setColor(Color.RED);
//			g.drawRect(logicRect, paint1);
			g.clipRect(photoRect.left + cameraX,
					photoRect.top,
					photoRect.right + cameraX,
					photoRect.bottom);
			paintList(g);
//			paint1 = null;
			
			g.restore();
			piantTab(g);
		}
	}

	/**
	 * @param g
	 */
	private void paintList(Canvas g) {
		if(roomType == 0) {
			paintRoomList(g);
		}
		else {
			paintVipRoomList(g);
		}
	}

	/**
	 * @param g
	 */
	private void piantTab(Canvas g) {
		if(roomType == 0) {
			for(int i=0; i < selectSpriteList.size(); i++) {
				selectSpriteList.get(i).paint(g, -cameraX, 0);
			}
		}
		else {
			for(int i=0; i < vipselectSpriteList.size(); i++) {
				vipselectSpriteList.get(i).paint(g, -cameraX, 0);
			}
		}
	}

	/**
	 * @param g
	 */
	private void paintVipRoomList(Canvas g) {
		if(iGameState == 3 || iGameState == 6) {
			int i = currentIndex;
			if(roomBiArrayList != null && roomBiArrayList.get(iVipRoomList.get(i).iIcon) != null) {
				g.drawBitmap(roomBiArrayList.get(iVipRoomList.get(i).iIcon), offsetX + cameraX + vipphotoArrayList.get(i).x, vipphotoArrayList.get(i).y, null);
			}
			iSpriteListIcon.get(0).paint(g, -offsetX - cameraX - roomSelectW * i, 0);
			g.drawText(iVipRoomList.get(i).iRoomName, offsetX + cameraX + viproomNameArrayList.get(i).x, viproomNameArrayList.get(i).y, roomNamePaint);
			if(iVipRoomList.get(i).iState == 0) {
				roomLimitPaint.setColor(Color.GREEN);
			}
			else {
				roomLimitPaint.setColor(Color.RED);
			}
			g.drawText(iVipRoomList.get(i).iState == 0 ? "状态:未订":"状态:已订", offsetX +  cameraX + viproomLimitArrayList.get(i).x, viproomLimitArrayList.get(i).y, roomLimitPaint);
			vipbetSpriteList.get(i).paint(g,  -offsetX - cameraX , 0);
			if(iVipRoomList.get(i).iState == 0) {
				g.drawText("订房费用:"+iVipRoomList.get(i).iVipBookPrice, offsetX +  cameraX + viproomBetArrayList.get(i).x, viproomBetArrayList.get(i).y, roomBetPaint);
			}
			else {
				g.drawText("到期时间:"+iVipRoomList.get(i).iTime, offsetX +  cameraX + viproomBetArrayList.get(i).x, viproomBetArrayList.get(i).y, roomBetPaint);
			}
		}
		else {
			for(int i = 0; i < vipphotoArrayList.size(); i++) {
				if(roomBiArrayList != null && roomBiArrayList.get(iVipRoomList.get(i).iIcon) != null) {
					g.drawBitmap(roomBiArrayList.get(iVipRoomList.get(i).iIcon), offsetX + cameraX + vipphotoArrayList.get(i).x, vipphotoArrayList.get(i).y, null);
				}
				iSpriteListIcon.get(0).paint(g, -offsetX - cameraX - roomSelectW * i, 0);
				g.drawText(iVipRoomList.get(i).iRoomName, offsetX + cameraX + viproomNameArrayList.get(i).x, viproomNameArrayList.get(i).y, roomNamePaint);
				vipbetSpriteList.get(i).paint(g,  -offsetX - cameraX, 0);
				if(iVipRoomList.get(i).iState == 0) {
					roomLimitPaint.setColor(Color.GREEN);
				}
				else {
					roomLimitPaint.setColor(Color.RED);
				}
				g.drawText(iVipRoomList.get(i).iState == 0 ? "状态:未订":"状态:已订", offsetX +  cameraX + viproomLimitArrayList.get(i).x, viproomLimitArrayList.get(i).y, roomLimitPaint);
				vipbetSpriteList.get(i).paint(g,  -offsetX - cameraX , 0);
				if(iVipRoomList.get(i).iState == 0) {
					g.drawText("订房费用:"+iVipRoomList.get(i).iVipBookPrice, offsetX +  cameraX + viproomBetArrayList.get(i).x, viproomBetArrayList.get(i).y, roomBetPaint);
				}
				else {
					g.drawText("到期时间:"+iVipRoomList.get(i).iTime, offsetX +  cameraX + viproomBetArrayList.get(i).x, viproomBetArrayList.get(i).y, roomBetPaint);
				}
			}
		}
	}

	/**
	 * @param g
	 */
	private void paintRoomList(Canvas g) {
		if(iGameState == 3 || iGameState == 6) {
			int i = currentIndex;
			if(roomBiArrayList != null && roomBiArrayList.get(iRoomList.get(i).iIcon) != null) {
				g.drawBitmap(roomBiArrayList.get(iRoomList.get(i).iIcon), offsetX + selectX + photoArrayList.get(i).x, photoArrayList.get(i).y, null);
			}
			if(illegal(0, i)) {
				roomLimitPaint.setColor(Color.GREEN);
			}
			else {
				roomLimitPaint.setColor(Color.RED);
			}
			iSpriteListIcon.get(0).paint(g, -offsetX - selectX - roomSelectW * i, 0);
			g.drawText(iRoomList.get(i).iRoomName, offsetX + selectX + roomNameArrayList.get(i).x, roomNameArrayList.get(i).y, roomNamePaint);
			iSpriteListIcon.get(1).paint(g,  -offsetX - selectX - roomSelectW * i, 0);
			g.drawText(iRoomList.get(i).iRoomLimitNum, offsetX +  selectX + roomLimitArrayList.get(i).x, roomLimitArrayList.get(i).y, roomLimitPaint);
			betSpriteList.get(i).paint(g,  -offsetX - selectX, 0);
			g.drawText(iRoomList.get(i).iRoomBetNum, offsetX +  selectX + roomBetArrayList.get(i).x, roomBetArrayList.get(i).y, roomBetPaint);
		}
		else {
			for(int i = 0; i < photoArrayList.size(); i++) {
				if(roomBiArrayList != null && roomBiArrayList.get(iRoomList.get(i).iIcon) != null) {
					g.drawBitmap(roomBiArrayList.get(iRoomList.get(i).iIcon), offsetX + selectX + photoArrayList.get(i).x, photoArrayList.get(i).y, null);
				}
				if(illegal(0, i)) {
					roomLimitPaint.setColor(Color.GREEN);
				}
				else {
					roomLimitPaint.setColor(Color.RED);
				}
				iSpriteListIcon.get(0).paint(g, -offsetX - selectX - roomSelectW * i, 0);
				g.drawText(iRoomList.get(i).iRoomName, offsetX + selectX + roomNameArrayList.get(i).x, roomNameArrayList.get(i).y, roomNamePaint);
				iSpriteListIcon.get(1).paint(g,  -offsetX - selectX - roomSelectW * i, 0);
				g.drawText(iRoomList.get(i).iRoomLimitNum, offsetX +  selectX + roomLimitArrayList.get(i).x, roomLimitArrayList.get(i).y, roomLimitPaint);
				betSpriteList.get(i).paint(g,  -offsetX - selectX, 0);
				g.drawText(iRoomList.get(i).iRoomBetNum, offsetX +  selectX + roomBetArrayList.get(i).x, roomBetArrayList.get(i).y, roomBetPaint);
			}
		}
	}

	/**
	 * @param g
	 */
	private void paintTitle(Canvas g) {
		if(iSceneTitle != null) {
			iSceneTitle.paint(g, 0, 0);
			changesButtonSelect.paint(g, -cameraX, 0);
			for(int i = 0; i < iSpriteButtonListTile.size(); i++) {
				iSpriteButtonListTile.get(i).paint(g, -cameraX, 0);
			}
			Rect rect = new Rect();
			String gb = iPlayerInfo.iGb+"";
			moneypPaint.getTextBounds(gb, 0, gb.length(), rect);
			int x = gbRect.left;
			int y = gbRect.centerY() - rect.centerY();
			g.drawText(gb, 
					x+cameraX, 
					y, 
					moneypPaint);
//			Tools.debug("rect " + gbRect.toString());
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
		else if(iGameState == 1) {//进场
			cameraX -= speed;
			if(cameraX >= 0) {
				cameraX = 0;
			}
			iSceneTitle.iCameraX = -cameraX;
			selectX -= selectSpeed;
			if(selectX <= 0) {
				selectX = 0;
			}
			iSceneRoom.iCameraX = -cameraX;
			if(cameraX == 0 && selectX == 0) {
				iGameState = -1;
			}
		}
		else if(iGameState == 3 || iGameState == 6) {//退场
			cameraX += speed;
			if(cameraX <= -ActivityUtil.SCREEN_WIDTH) {
				cameraX = -ActivityUtil.SCREEN_WIDTH;
			}
			iSceneTitle.iCameraX = -cameraX;
			selectX += selectSpeed;
			if(selectX >= ActivityUtil.SCREEN_WIDTH) {
				selectX = ActivityUtil.SCREEN_WIDTH;
			}
			iSceneRoom.iCameraX = -cameraX;
			if(selectX == ActivityUtil.SCREEN_WIDTH && cameraX == -ActivityUtil.SCREEN_WIDTH) {
				if(iGameState == 3) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
				}
				else if(iGameState == 6) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_TO_PROGRESS, pair);
				}
				iGameState = 4;
			}
		}
		else {
			changesButtonSelect.perform();
			for(int i = 0; i < iSpriteButtonListTile.size(); i++) {
				SpriteButton temButton = iSpriteButtonListTile.get(i);
				if(pressedX != 0 || oldIndex != currentIndex) {
					Tools.debug("oldIndex" + oldIndex + " roomType " + roomType);
					if(temButton.getEventID() == GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_START) {
						if(illegal(roomType, currentIndex)) {
							temButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
						}
						else {
							temButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_UNABLE);
						}
					}
				}
				temButton.perform();
			}
			roll();
//			if(touch && !sroll) {
//				touchTick++;
//				if(touchTick >= 3) {
//					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
//					ItemAction(GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_START);
//					touchTick = 0;
//				}
//			}
		}
	}

	/**
	 * 
	 */
	private void roll() {
//		if(runTick > 0) {
//			runTick--;
//			if(runTick < 0) {
//				runTick = 0;
//			}
//			if(runTick % 2 == 1) {
//				iRunSpeed -= 1;
//			}
//			if(iRunSpeed <= 4) {
//				iRunSpeed = 4;
//				runTick = 0;
//			}
//			if(runDirct == 1) { //向左滚动
//				offsetX -= iRunSpeed;
//				if(offsetX < (-(maxCount - minCount) * distW)) {
//					offsetX = (-(maxCount - minCount) * distW);
//				}
//			}
//			else {
//				offsetX += iRunSpeed;
//				if(offsetX > 0) {
//					offsetX = 0;
//				}
//			}
//			lastOffsetX = offsetX;
//		}
//		//当松手后
//		if(pressedX == 0 && runTick <= 0) {
//			//是否对准基线
//			int tempY = 0;//(int)(ActivityUtil.TEXTSIZE_HUGEC/2);// % iLeftDist;
//			tempY = offsetX % distW;
////			Tools.debug("tempY" + tempY);
//			if(tempY < 0) {
//				if(tempY <= -10) {
//					if(tempY < -(distW >> 1)) {
//						offsetX -= 10;
//					}
//					else {
//						offsetX += 10;
//					}
//				}
//				else {
//					offsetX -= tempY;
//				}
//			}
//			//
//			int tempTop = (int)(-(maxCount - minCount) * distW);
//			if(offsetX < tempTop) {
//				offsetX = tempTop;
//			}
//			lastOffsetX = offsetX;
//			oldIndex = currentIndex;
//		}
		
		//计算索引
		oldIndex = currentIndex;
//		CurrentIndex();
		if(roomType == 0) {
			for(int i = 0; i < selectSpriteList.size(); i++) {
				if(currentIndex == i) {
					selectSpriteList.get(i).setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_PRESSED);
				}
				else {
					selectSpriteList.get(i).setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				}
			}
		}
		else {
			for(int i = 0; i < vipselectSpriteList.size(); i++) {
				if(currentIndex == i) {
					vipselectSpriteList.get(i).setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_PRESSED);
				}
				else {
					vipselectSpriteList.get(i).setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
				}
			}
		}
//		Tools.debug("currentIndex" + currentIndex);
	}

	/**
	 * 
	 */
	private void CurrentIndex() {
		currentIndex = -(offsetX / distW);
//		if(offsetX % distW < -(distW >> 1)) {
//			currentIndex ++;
//		}
		if((offsetX - lastOffsetX) < 0) {
			currentIndex ++;
		}
		if(currentIndex < 0) {
			currentIndex = 0;
		}
		if(currentIndex >= maxCount) {
			currentIndex = maxCount - 1;
		}
		offsetX = -(currentIndex * distW);
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(iGameState != -1) {
			return false;
		}
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(logicRect.contains(x, y)) {
				if(pressTick > 0) {
					runTick = 0;
					pressTick = 0;
					runDirct = 0;
					iRunSpeed = 0;
				}
				sroll = false;
				touchTick = 0;
				pressedX = x;
				offsetX = lastOffsetX + (x - pressedX);
				touch = true;
				pressTick = (int) System.currentTimeMillis();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if(touch) {
				offsetX = lastOffsetX + (x - pressedX);
				touchTick = 0;	
				if(Math.abs(x - pressedX) > 10) {
					sroll = true;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if(touch == false) {
				break;
			}
			if(offsetX > photoRect.left) {
				offsetX = photoRect.left;
			}
			else {
				if(offsetX < -(maxCount - minCount) * distW) {
					offsetX = -(maxCount - minCount) * distW;
				}
			}
//			
//			
//			pressTick = (int) (System.currentTimeMillis() - pressTick);
//			if(pressTick < 250) {
////				MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//				if(x < pressedX - 100 * ActivityUtil.PAK_ZOOM_X) {
//					runDirct = 1; //向左滚动
//					runTick = 32;
//					iRunSpeed = (int)(70 * ActivityUtil.PAK_ZOOM_X);
//				}
//				else if(x < pressedX - 20 * ActivityUtil.PAK_ZOOM_X) {
//					runDirct = 1; //向左滚动
//					runTick = 4;
//					iRunSpeed = (int)(50 * ActivityUtil.PAK_ZOOM_X);
//				}
//			
////				MediaManager.getMediaManagerInstance().playerSound(SoundsResID.click, 0);
//				else if(x > pressedX + 60 * ActivityUtil.PAK_ZOOM_X) {
//					runDirct = 2; //向右滚动
//					runTick = 32;
//					iRunSpeed = (int)(70 * ActivityUtil.PAK_ZOOM_X);
//				}
//				else if(x > pressedX + 10 * ActivityUtil.PAK_ZOOM_X) {
//					runDirct = 2; //向右滚动
//					runTick = 4;
//					iRunSpeed = (int)(50 * ActivityUtil.PAK_ZOOM_X);
//				}
//			}
			oldIndex = currentIndex;
			if(!sroll) {
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
				ItemAction(GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_START);
			}
			else {
				CurrentIndex();
				changeMusic();
				//
			}
			pressedX = 0;
			touch = false;
			sroll = false;
			lastOffsetX = offsetX;
			break;

		default:
			break;
		}
		return false;
	}

	/**
	 * 
	 */
	private void changeMusic() {
		int roomid = 0;
		if(roomType == 1) {
			roomid = iVipRoomList.get(currentIndex).iRoomID;
		}
		else {
			roomid = iRoomList.get(currentIndex).iRoomID;
		}
		tMusicID = SoundsResID.bg4;// + roomid;
//		if(tMusicID > SoundsResID.bg8) {
//			tMusicID = SoundsResID.bg4;
//		}
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playMusic(tMusicID, true);
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
			if(iGameState == -1) {
				iGameState = 3;
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
		boolean result= false;
		int aX = (int) e.getX();
		int aY = (int) e.getY();
		result = pointerPressed(aX, aY);
		return result;
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
		if(iGameState != -1) {
			return false;
		}
		boolean result= false;
		if (result) {
			return result;
		}
		Rect pRect = new Rect();
		SpriteButton pSB;
		for(int i=0; i<iSpriteButtonListTile.size(); i++) {
			pSB = ((SpriteButton)iSpriteButtonListTile.elementAt(i));
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
				//System.out.println("ProgressScreen pointerPressed  pSB.pressed() ");
				return result = true;
			}
		}
		if(changesButtonSelect != null) {
			if(!changesButtonSelect.isVision()) {
				return false;
			}
			int pX = changesButtonSelect.getX();
			int pY = changesButtonSelect.getY();
			Rect pLogicRect = changesButtonSelect.getRect();
			pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
					pX+pLogicRect.right, pY+pLogicRect.bottom);
			if (pRect.contains(aX, aY)) {
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
				changesButtonSelect.pressed();
				//System.out.println("ProgressScreen pointerPressed  pSB.pressed() ");
				return result = true;
			}
		}
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
		case MessageEventID.EMESSAGE_EVENT_TO_SELECTROOM:
			MyArStation.getInstance().mainLayout.removeAllViewsInLayout();
			vipRoomDialog = new VipRoomDialog(MyArStation.mGameMain);
			roomDetailDialog = new RoomDetailDialog(MyArStation.mGameMain);
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_ROOMLIST_B:
			MBRspRoomListC rspRoomListc = (MBRspRoomListC)msg.obj;
			iPlayerInfo.iGb = rspRoomListc.iGB;
			iRoomList = new ArrayList<RoomListData>();
			iVipRoomList = new ArrayList<RoomListData>();
			int count = rspRoomListc.iCount;
			for(int i = 0; i < count; i++) {				
				RoomListData roomListData = new RoomListData(rspRoomListc.iRoomID.get(i),
						rspRoomListc.iTableID.get(i),
						rspRoomListc.iRoomName.get(i),
						rspRoomListc.iRoomLimit.get(i),
						rspRoomListc.iRoomBet.get(i),
						rspRoomListc.iMinGB.get(i),
						rspRoomListc.iMaxGB.get(i),
						rspRoomListc.iIconName.get(i), rspRoomListc.iType.get(i));
				iRoomList.add(roomListData);
			}
			int vipcount = rspRoomListc.iVIPCount;
			for(int i = 0; i < vipcount; i++) {
				RoomListData roomListData = new RoomListData(rspRoomListc.iVIPRoomID.get(i),
						rspRoomListc.iVIPTableID.get(i),
						rspRoomListc.iVIPRoomName.get(i),
						rspRoomListc.iState.get(i),
						rspRoomListc.iVIPRoomIconName.get(i),
						rspRoomListc.iVIPBetA.get(i),
						rspRoomListc.iVIPBetB.get(i),
						rspRoomListc.iVIPBetC.get(i),
						rspRoomListc.iRoomPrice.get(i)+"",
						Util.TimetoString(rspRoomListc.iSecond.get(i), "HH:mm:ss"),
						rspRoomListc.iVIPType.get(i));
				iVipRoomList.add(roomListData);
			}
			
			picName = rspRoomListc.iPicName;
			Url = rspRoomListc.iUrl;
			float w = selectSprite.getFirstFrameWidth();;
			float space = (5 * ActivityUtil.ZOOM_X);
			float tabmaxW = (w  + space) * count;
			float centerx = (ActivityUtil.SCREEN_WIDTH - tabmaxW) / 2;
			float startx =  centerx;
			
			for(int i = 0; i < count; i++) {
				photoArrayList.add(new Point(photoRect.left + i * roomSelectW, photoRect.top));
				Rect rect = new Rect();
				String roomname = iRoomList.get(i).iRoomName;
				roomNamePaint.getTextBounds(roomname, 0, roomname.length(), rect);
				int x = roomNameRect.centerX()- rect.centerX();
				int y = roomNameRect.centerY() - rect.centerY();
				roomNameArrayList.add(new Point(x + i * roomSelectW, y));
				roomLimitArrayList.add(new Point(roomLimitRect.left + i * roomSelectW, roomLimitRect.top));
				roomBetArrayList.add(new Point(roomBetRect.left + i * roomSelectW, roomBetRect.top));
				Bitmap bkmBitmap = MyArStation.iImageManager.getThumBitmap(iRoomList.get(i).iIcon, photoRect.width(), photoRect.height());
				roomBiArrayList.put(iRoomList.get(i).iIcon, bkmBitmap);
				betSpriteStatus(i);
				initSelectTab(w, space, startx, i);
			}
			//vip房
			tabmaxW = (w  + space)* vipcount;
			centerx = (ActivityUtil.SCREEN_WIDTH - tabmaxW) / 2;
			startx =  centerx;
			for(int i = 0; i < vipcount; i++) {
				vipphotoArrayList.add(new Point(photoRect.left + i * roomSelectW, photoRect.top));
				Rect rect = new Rect();
				String roomname = iVipRoomList.get(i).iRoomName;
				roomNamePaint.getTextBounds(roomname, 0, roomname.length(), rect);
				int x = roomNameRect.centerX()- rect.centerX();
				int y = roomNameRect.centerY() - rect.centerY();
				viproomNameArrayList.add(new Point(x + i * roomSelectW, y));
				viproomLimitArrayList.add(new Point(roomLimitRect.left + i * roomSelectW, roomLimitRect.top));
				viproomBetArrayList.add(new Point(roomBetRect.left + i * roomSelectW, roomBetRect.top));
				Bitmap bkmBitmap = MyArStation.iImageManager.getThumBitmap(iVipRoomList.get(i).iIcon, photoRect.width(), photoRect.height());
				roomBiArrayList.put(iVipRoomList.get(i).iIcon, bkmBitmap);
				initVipRoomStatus(i);
				initVipSelectTab(w, space, startx, i);
			}
			minCount = 1;
			maxCount = count;
			Tools.debug("bitmap size " + roomBiArrayList.size()+"");
			priorityIndex();
			changeMusic();
			iGameState = 1;
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_PIC:
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_SINGLE_PIC:
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_START:
			if(roomType == 0) {
				int size = iRoomList.size();
				if(size == 0) {
					break;
				}
				if(currentIndex >= size) {
					currentIndex = size -1;
				}
				if(currentIndex < 0) {
					currentIndex = 0;
				}
				RoomListData data = iRoomList.get(currentIndex);
				if(iPlayerInfo.iGb >= data.iMinGB && iPlayerInfo.iGb <= data.iMaxGB) {
					pair = null;
					pair = new RoomData(data.iRoomID, data.iTableID, data.iRoomType);
					iGameState = 6;
				}
				else {
					Tools.showSimpleToast(MyArStation.mGameMain, Gravity.CENTER, MyArStation.mGameMain.getString(R.string.select_room), Toast.LENGTH_LONG);
				}
			}
			else {
				int size = iVipRoomList.size();
				if(size == 0) {
					break;
				}
				if(currentIndex >= size) {
					currentIndex = size -1;
				}
				if(currentIndex < 0) {
					currentIndex = 0;
				}
				RoomListData data = iVipRoomList.get(currentIndex);
				if(data.iState == 1) {
					vipRoomDialog.setTitle(data.iRoomName);
					vipRoomDialog.setDate(data);
				}
				else {
					vipRoomDialog.setTitle(data.iRoomName);
					vipRoomDialog.setDate(data);
				}
				vipRoomDialog.show();
			}
			
			break;
		case MessageEventID.EMESSAGE_EVENT_REQ_LISTLOGINROOM:
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspLoginVipRoom:
			MBRspLoginVipRoom rspLoginVipRoom = (MBRspLoginVipRoom) msg.obj;
			if(rspLoginVipRoom.iMsg.length() > 0) {
				Tools.showSimpleToast(MyArStation.getInstance().getApplicationContext(), rspLoginVipRoom.iMsg);
			}
			
			for(int i = 0; i < iVipRoomList.size(); i++) {
				if(iVipRoomList.get(i).iRoomID == rspLoginVipRoom.iClientRoomID) {
					iVipRoomList.get(i).iState = rspLoginVipRoom.iState;
					initVipRoomStatus(i);
					iVipRoomList.get(i).iTime = Util.TimetoString(rspLoginVipRoom.iSecond, "hh:mm:ss");
					break;
				}
			}
			if(rspLoginVipRoom.iResult == 0) {
				pair = null;
				pair = new RoomData(rspLoginVipRoom.iRoomID, rspLoginVipRoom.iTableID, rspLoginVipRoom.iType);
				iGameState = 6;
			}
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_DETAIL:
			String title = "";
			String url = "";
			if(roomType == 0) {
				RoomListData data = iRoomList.get(currentIndex);
				if(data != null) {
					title = data.iRoomName;
					url = data.iRoomID+"";
				}
			}
			else {
				RoomListData data = iVipRoomList.get(currentIndex);
				if(data != null) {
					title = data.iRoomName;
					url = data.iRoomID+"";
				}
			}
			roomDetailDialog.Show(url, title);
			break;
		default:
			break;
		}
 	}

	/**
	 * @param count
	 */
	private void priorityIndex() {
		if(iRoomList == null) {
			return;
		}
		Iterator<RoomListData> iterator = iRoomList.iterator();
		int count = iRoomList.size();
		int tempIndex = 0;
		int maxIndex = 0;
		int max = 0;
		while(iterator.hasNext()) {
			RoomListData data = iterator.next();
			if(MyArStation.iPlayer.iGb >= data.iMinGB && MyArStation.iPlayer.iGb <= data.iMaxGB) {
				if(data.iMinGB >= max) {
					max = data.iMinGB;
					maxIndex = tempIndex;
				}
			}
			
			tempIndex++;
		}
		currentIndex = maxIndex;
		if(currentIndex < 0) {
			currentIndex = 0;
		}
		if(currentIndex >= count) {
			currentIndex = count - 1;
		}
		offsetX = -(distW * currentIndex);
		lastOffsetX = offsetX;
	}

	/**
	 * @param i
	 */
	private void initVipRoomStatus(int i) {
		Sprite sprite = new Sprite();
		sprite.copyFrom(vipStatusSprite);
		SpriteButton button = new SpriteButton(sprite);
		RoomListData date = iVipRoomList.get(i);
		if(date.iState == 0) {
			button.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
		}
		else {
			button.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_UNABLE);
		}
		button.setPosition(vipStatusSprite.getX()+roomSelectW * i, vipStatusSprite.getY());
		vipbetSpriteList.add(button);
	}

	/**
	 * @param i
	 */
	private void betSpriteStatus(int i) {
		Sprite sprite = new Sprite();
		sprite.copyFrom(betSprite);
		SpriteButton button = new SpriteButton(sprite);
		RoomListData date = iRoomList.get(i);
		if(iPlayerInfo.iGb >= date.iMinGB && iPlayerInfo.iGb <= date.iMaxGB) {
			button.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
		}
		else {
			button.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_UNABLE);
		}
		button.setPosition(betSprite.getX()+roomSelectW * i, betSprite.getY());
		betSpriteList.add(button);
	}

	/**
	 * @param w
	 * @param space
	 * @param startx
	 * @param i
	 */
	private void initSelectTab(float w, float space, float startx, int i) {
		Sprite sprite = new Sprite();
		sprite.copyFrom(selectSprite);
		SpriteButton button = new SpriteButton(sprite);
		button.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
		button.setPosition((int) (startx + (w + space) * i), selectSprite.getY());
		button.setEventID(GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_TAB + i);
		selectSpriteList.add(button);
	}

	/**
	 * @param w
	 * @param space
	 * @param startx
	 * @param i
	 */
	private void initVipSelectTab(float w, float space, float startx, int i) {
		Sprite sprite = new Sprite();
		sprite.copyFrom(selectSprite);
		SpriteButton button = new SpriteButton(sprite);
		button.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
		button.setPosition((int) (startx + (w + space) * i), selectSprite.getY());
		button.setEventID(GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_TAB + i);
		vipselectSpriteList.add(button);
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
			case RspMsg.MSG_RSP_ROOM_LIST:
				MBRspRoomList rspRoomList = (MBRspRoomList)pMobileMsg.m_pMsgPara;
				if(rspRoomList == null) {
					break;
				}
				Tools.debug(rspRoomList.toString());
				if(rspRoomList.nResult == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_ROOMLIST, rspRoomList);
				}
				else {
					messageEvent(rspRoomList.nResult, rspRoomList.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_ROOM_LIST_B:
				MBRspRoomListB rspRoomListb = (MBRspRoomListB)pMobileMsg.m_pMsgPara;
				if(rspRoomListb == null) {
					break;
				}
				Tools.debug(rspRoomListb.toString());
				if(rspRoomListb.nResult == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_ROOMLIST_B, rspRoomListb);
				}
				else {
					messageEvent(rspRoomListb.nResult, rspRoomListb.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_ROOM_LIST_C:
				MBRspRoomListC rspRoomListc = (MBRspRoomListC)pMobileMsg.m_pMsgPara;
				if(rspRoomListc == null) {
					break;
				}
				Tools.debug(rspRoomListc.toString());
				if(rspRoomListc.nResult == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_ROOMLIST_B, rspRoomListc);
				}
				else {
					messageEvent(rspRoomListc.nResult, rspRoomListc.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_LEAVEROOM:
				MBRspLeaveRoom rspLeaveRoom = (MBRspLeaveRoom)pMobileMsg.m_pMsgPara;
				if(rspLeaveRoom == null) {
					break;
				}
				Tools.debug(rspLeaveRoom.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspLeaveRoom, rspLeaveRoom);
				break;
			case RspMsg.MSG_RSP_LOGIN_VIPROOM:
				MBRspLoginVipRoom rspLoginVipRoom = (MBRspLoginVipRoom)pMobileMsg.m_pMsgPara;
				if(rspLoginVipRoom == null) {
					break;
				}
				Tools.debug(rspLoginVipRoom.toString());
				messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspLoginVipRoom, rspLoginVipRoom);
				break;
			default:
				break;
			}
		}
		break;
		default:
			break;
		}
	}


	private boolean illegal(int Type, int index){
		if(0 == Type) {
			int size = iRoomList.size();
			if(size == 0) {
				return false;
			}
			if(index >= size) {
				index = size -1;
			}
			if(index < 0) {
				index = 0;
			}
			RoomListData date = iRoomList.get(index);
			if(iPlayerInfo.iGb >= date.iMinGB && iPlayerInfo.iGb <= date.iMaxGB) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return true;
		}
	}

	/* 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.btnStart:
//			MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
//			break;
		default:
			break;
		}
	}
	
	private void requestRoomList() {
		if(MyArStation.iGameProtocol.requestRoomList(0)) {
//			showProgressDialog(R.string.Loading_String);
		}
	}
	
	private void requestRoomListB() {
		if(MyArStation.iGameProtocol.requestRoomListB(0)) {
//			showProgressDialog(R.string.Loading_String);
		}
	}
	
	private void requestRoomListc() {
		if(MyArStation.iGameProtocol.requestRoomListC(0)) {
//			showProgressDialog(R.string.Loading_String);
		}
	}
	
	private Vector<String> cutString(String src) {
		Vector<String> str = new Vector<String>();
		if(src.contains("|")) {
			int index = src.indexOf("|");
			str.add(0, src.substring(0, index));
			str.add(1, src.substring(index+1));
		}
		else {
			str.add(new String(src));
			str.add(new String(""));
		}
		return str;
	}
	
	private void loadUIPak() {
		iUIPak = CPakManager.loadUIPak();//Project.loadProject(ActivityUtil.PATH_SCREEN+"gameshome_map.pak", false);
		iSceneLoading = LoadLoading();
		if (iUIPak != null) {
			iSceneTitle = iUIPak.getScene(21);
			if(iSceneTitle != null) {//标题
				iSceneTitle.setViewWindow(0, 0, 800, 480);
				iSceneTitle.initDrawList();
				iSceneTitle.initNinePatchList(new int[] {R.drawable.gdbj, R.drawable.gdbj});
				iSceneTitle.iCameraX = -cameraX;
				Map pMap = iSceneTitle.getLayoutMap(Scene.SpriteLayout);
				Vector<?> pSpriteList = pMap.getSpriteList();
				Sprite pSprite;
				for (int i=0; i<pSpriteList.size(); i++) {
					pSprite = (Sprite)pSpriteList.elementAt(i);
					int pX = pSprite.getX();
					int pY = pSprite.getY();
					int pEventID = GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_OFFSET + pSprite.getIndex();
					TPoint pPoint = new TPoint();
					pPoint.iX = pX;
					pPoint.iY = pY;
					//精灵按钮
					SpriteButton pSpriteButton;
					pSpriteButton = new SpriteButton(pSprite);
					pSpriteButton.setEventID(pEventID);
					pSpriteButton.setPosition(pX, pY);
					pSpriteButton.setHandler(this);
					if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_HEAD) {
						pSpriteButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
						headRect = new Rect(pSpriteButton.getRect());
						headSprite = pSprite;
					}
					else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_SWITCH) {
						changesButtonSelect = new SpriteButtonSelect(pSprite);
						changesButtonSelect.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_MSG);
						changesButtonSelect.setEventID(GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_SWITCH);
						changesButtonSelect.setPosition(pX, pY);
						changesButtonSelect.setSelected(true);
						changesButtonSelect.setHandler(this);
						pSpriteButton = null;
					}
					else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_OFFSET + 78) {
						selectTabRect = new Rect();
						selectTabRect.set(pSpriteButton.getRect().left, 
								pSpriteButton.getRect().top,
								pSpriteButton.getRect().right,
								pSpriteButton.getRect().bottom);
						selectSprite = pSprite;
					}
					else {
						iSpriteButtonListTile.add(pSpriteButton);
					}
				}
			}
			iSceneRoom = iUIPak.getScene(22);
			if(iSceneRoom != null) {
				iSceneRoom.setViewWindow(0, 0, 800, 480);
				iSceneRoom.initDrawList();
				iSceneRoom.iCameraX = -cameraX;
				Map pMap = iSceneRoom.getLayoutMap(Scene.SpriteLayout);
				Vector<?> pSpriteList = pMap.getSpriteList();
				Sprite pSprite;
				for (int i=0; i<pSpriteList.size(); i++) {
					pSprite = (Sprite)pSpriteList.elementAt(i);
					int pX = pSprite.getX();
					int pY = pSprite.getY();
					int pEventID = GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_OFFSET + pSprite.getIndex();
					TPoint pPoint = new TPoint();
					pPoint.iX = pX;
					pPoint.iY = pY;
					SpriteButton pSpriteButton = null;
					if(pEventID != GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_OFFSET + 83 || pEventID != GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_OFFSET + 84) {
						pSpriteButton = new SpriteButton(pSprite);
						pSpriteButton.setEventID(pEventID);
						pSpriteButton.setPosition(pX, pY);
						pSpriteButton.setActionID(SpriteButton.ESPRITE_BUTTON_ACTION_NORMAL);
						
					}
					//精灵按钮
					if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_OFFSET + 79) {//选择房间区域矩形
						logicRect = new Rect();
						Rect pLogicRect = pSpriteButton.getRect();
						logicRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
								pX+pLogicRect.right, pY+pLogicRect.bottom);
						Tools.debug("logic" + logicRect.toString());
						
					}
					else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_OFFSET + 80) {
						roomNameRect = new Rect(pX, pY, 0, 0);
						iSpriteListIcon.add(pSpriteButton);
					}
					else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_OFFSET + 81) {
						roomLimitRect = new Rect(pX, pY, 0, 0);
						iSpriteListIcon.add(pSpriteButton);
					}
					else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_OFFSET + 82) {
						roomBetRect = new Rect(pX, pY, 0, 0);
//						iSpriteListIcon.add(pSpriteButton);
						betSprite = pSprite;
					}
					else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_OFFSET + 83) {
						photoRect = new Rect(pX, pY, 0, 0);
					}
					else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_OFFSET + 84) {
						photoRect.right = pX;
						photoRect.bottom = pY;
						distW = photoRect.width();
						roomSelectW  = distW;
						roomNameRect = new Rect(photoRect.left,
								photoRect.top, 
								photoRect.right,
								(int) (roomNamePaint.getTextSize()+photoRect.top));
						Tools.debug("roomNameRect"  + roomNameRect.toString());
					}
					else if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_OFFSET + 86){
						vipStatusSprite = pSprite;
					}
				}
			}
		}
	}

	/* 
	 * @see com.funoble.lyingdice.gamebase.GameButtonHandler#ItemAction(int)
	 */
	@Override
	public boolean ItemAction(int aEventID) {
		switch (aEventID) {
		case GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_START:
			messageEvent(GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_START);
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_TNTO:
			iGameState = 3;
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_SWITCH:
			if(roomType == 0 && !changesButtonSelect.getSelected()) {
				roomCurrentIndex = currentIndex;
				resetRoll(vipRoomCurrentIndex);
				maxCount = vipphotoArrayList.size();
				roomType = 1;
			}
			else {
				vipRoomCurrentIndex = currentIndex;
				resetRoll(roomCurrentIndex);
				maxCount = photoArrayList.size();
				roomType = 0;
			}
			changeMusic();
			return true;
		case GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_DETAIL:
			messageEvent(GameEventID.ESPRITEBUTTON_EVENT_CHOOSEROOM_DETAIL);
			break;
		default:
			break;
		}
		return false;
	}

	/**
	 * 
	 */
	private void resetRoll(int index) {
		currentIndex = index;
		distW = photoRect.width();
		offsetX =  -(distW * currentIndex);
		lastOffsetX = offsetX;
		iRunSpeed = (int)(distW / 2 * ActivityUtil.ZOOM_X);
		runTick = 0;
		pressTick = 0;
		runDirct = 0;
		oldIndex = -1;
	}
}
