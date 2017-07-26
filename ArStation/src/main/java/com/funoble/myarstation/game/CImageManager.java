package com.funoble.myarstation.game;

import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.Graphics;
import com.funoble.myarstation.utils.ThumbnailUtils;

public class CImageManager {
	public static final int MAX_DICE_TYPE_COUNT = 5;
	public static final int MAX_DICE_COUNT = 6;
	public static final int MAX_BUFF_COUNT = 4;
	public static final int MAX_STATE_BUFF_COUNT = 4;
	
	public static final int IMG_BUFF_SHIELD = 0;		//反弹盾
	public static final int IMG_BUFF_KILLORDER = 1;	//追杀令
	public static final int IMG_BUFF_UNARMED = 2;		//禁劈
	public static final int IMG_BUFF_CRAZED = 3;		//暴走
	
//	public Bitmap	iBackABmp = null;	//背景图A
//	public Bitmap	iBackBBmp = null;	//背景图B
	public Bitmap	iBackLoginBmp = null;	//背景图Login
	public Bitmap   iRoomBmp = null;	//房间背景图
//	public Bitmap   iMainBakBmp = null; //主界面背景图
	public Bitmap[][] iDiceBmps = null;	//骰子图片
	public Bitmap[][] iLittleDiceBmps = null; //小骰子图片
	public Bitmap[]	iBuffBmps = null;	//同盟Buff图片
	public Bitmap[] iStateBuffBmps = null; //状态Buff图片
	public Bitmap iChatBmp = null; //聊天泡泡
	public Bitmap iCoverBmp = null; //滚轮遮罩图
	public void init() {
//		System.out.println("SCREEN_W=" + ActivityUtil.SCREEN_WIDTH + " SCREEN_H=" + ActivityUtil.SCREEN_HEIGHT);
		
		Options opts = new Options();
		opts.inDensity = ActivityUtil.DENSITYDPI;
		Bitmap tmpBmp = BitmapFactory.decodeResource(
				MyArStation.mGameMain.getResources(), 
				R.drawable.main_back,
				opts);
		iBackLoginBmp = Graphics.zoomBitmap(tmpBmp, 
				(int)(tmpBmp.getWidth() * ActivityUtil.PAK_ZOOM_X), 
				(int)(tmpBmp.getHeight() * ActivityUtil.PAK_ZOOM_Y));
//		tmpBmp = BitmapFactory.decodeResource(
//				GameMain.mGameMain.getResources(), 
//				R.drawable.main_back,
//				opts);
//		iMainBakBmp = Graphics.zoomBitmap(tmpBmp, 
//				(int)(tmpBmp.getWidth() * ActivityUtil.PAK_ZOOM_X), 
//				(int)(tmpBmp.getHeight() * ActivityUtil.PAK_ZOOM_Y));		
//		tmpBmp = BitmapFactory.decodeResource(
//				GameMain.mGameMain.getResources(), 
//				R.drawable.new_back00,
//				opts);
//		iRoomBmp = Graphics.zoomBitmap(tmpBmp, 
//				(int)(tmpBmp.getWidth() * ActivityUtil.PAK_ZOOM_X), 
//				(int)(tmpBmp.getHeight() * ActivityUtil.PAK_ZOOM_Y));
//		tmpBmp = null;
		
		iDiceBmps = new Bitmap[MAX_DICE_TYPE_COUNT][];
		iLittleDiceBmps = new Bitmap[MAX_DICE_TYPE_COUNT][];
		for(int i=0; i<MAX_DICE_TYPE_COUNT; i++) {
			iDiceBmps[i] = new Bitmap[MAX_DICE_COUNT];
			iLittleDiceBmps[i] = new Bitmap[MAX_DICE_COUNT];
			for(int k=0; k<MAX_DICE_COUNT; k++) {
				//载入图片后，按照屏幕的尺寸缩放
				tmpBmp = BitmapFactory.decodeResource(
						MyArStation.mGameMain.getResources(), 
						R.drawable.new_dice0_big1 + i * MAX_DICE_COUNT + k,
						opts);
//				System.out.println("bmpWidth=" + tmpBmp.getWidth() + " bmpHeight=" + tmpBmp.getHeight());
				float hPara = ActivityUtil.PAK_ZOOM_Y;
				int zoomW = (int)(tmpBmp.getWidth() * hPara);
				int zoomH = (int)(tmpBmp.getHeight() * hPara);
//				System.out.println("hPara=" + hPara + " zoomW=" + zoomW + " zoomH=" + zoomH);
				int zoomLittleW = zoomW * 3 / 4;
				int zoomLittleH = zoomH * 3 / 4;
//				System.out.println("hPara=" + hPara + " zoomLittleW=" + zoomLittleW + " zoomLittleH=" + zoomLittleH);
				iDiceBmps[i][k] = Graphics.zoomBitmap(tmpBmp, 
						zoomW, 
						zoomH);
				iLittleDiceBmps[i][k] = Graphics.zoomBitmap(tmpBmp, 
						zoomLittleW, 
						zoomLittleH);
				tmpBmp = null;
			}
		}
		
		iBuffBmps = new Bitmap[MAX_BUFF_COUNT];
		for(int i=0; i<MAX_BUFF_COUNT; i++) {
			//载入图片后，按照屏幕的尺寸缩放
			tmpBmp = BitmapFactory.decodeResource(
					MyArStation.mGameMain.getResources(), 
					R.drawable.group0 + i,
					opts);
			int zoomW = (int)(tmpBmp.getWidth() * ActivityUtil.PAK_ZOOM_X);
			int zoomH = (int)(tmpBmp.getHeight() * ActivityUtil.PAK_ZOOM_Y);
			iBuffBmps[i] = Graphics.zoomBitmap(tmpBmp, 
					zoomW, 
					zoomH);
			tmpBmp = null;
		}
		
		iStateBuffBmps = new Bitmap[MAX_STATE_BUFF_COUNT];
		for(int i=0; i<MAX_BUFF_COUNT; i++) {
			//载入图片后，按照屏幕的尺寸缩放
			tmpBmp = BitmapFactory.decodeResource(
					MyArStation.mGameMain.getResources(), 
					R.drawable.new_buff0 + i,
					opts);
			int zoomW = (int)(tmpBmp.getWidth() * ActivityUtil.PAK_ZOOM_X);
			int zoomH = (int)(tmpBmp.getHeight() * ActivityUtil.PAK_ZOOM_Y);
			iStateBuffBmps[i] = Graphics.zoomBitmap(tmpBmp, 
					zoomW, 
					zoomH);
			tmpBmp = null;
		}
		
		//聊天泡泡
		tmpBmp = BitmapFactory.decodeResource(
				MyArStation.mGameMain.getResources(), 
				R.drawable.chat,
				opts);
		int zoomW = (int)(tmpBmp.getWidth() * ActivityUtil.PAK_ZOOM_X);
		int zoomH = (int)(tmpBmp.getHeight() * ActivityUtil.PAK_ZOOM_Y);
		iChatBmp = Graphics.zoomBitmap(tmpBmp, 
				zoomW, 
				zoomH);
		tmpBmp = null;

		//滚轮遮罩图
		tmpBmp = BitmapFactory.decodeResource(
				MyArStation.mGameMain.getResources(), 
				R.drawable.cover,
				opts);
		zoomW = (int)(tmpBmp.getWidth() * ActivityUtil.PAK_ZOOM_X);
		zoomH = (int)(tmpBmp.getHeight() * ActivityUtil.PAK_ZOOM_Y);
		iCoverBmp = Graphics.zoomBitmap(tmpBmp, 
				zoomW, 
				zoomH);
		tmpBmp = null;
		
		opts = null;
	}
	
/*	
	public Bitmap loadMainBack() {	
		if(iMainBakBmp != null) {
			return iMainBakBmp;
		}		
		Options opts = new Options();
		opts.inDensity = ActivityUtil.DENSITYDPI;
		Bitmap tmpBmp = BitmapFactory.decodeResource(
				GameMain.mGameMain.getResources(), 
				R.drawable.main_back,
				opts);
		iMainBakBmp = Graphics.zoomBitmap(tmpBmp, 
				(int)(tmpBmp.getWidth() * ActivityUtil.PAK_ZOOM_X), 
				(int)(tmpBmp.getHeight() * ActivityUtil.PAK_ZOOM_Y));
		
		tmpBmp = null;
		opts = null;
		return iMainBakBmp;
	}
	
	public void releaseMainBack() {
		iMainBakBmp = null;
	}
*/
	
	public Bitmap loadLoginBack() {
		if(iBackLoginBmp != null) {
			return iBackLoginBmp;
		}	
		Options opts = new Options();
		opts.inDensity = ActivityUtil.DENSITYDPI;
		Bitmap tmpBmp = BitmapFactory.decodeResource(
				MyArStation.mGameMain.getResources(), 
				R.drawable.main_back,
				opts);
		iBackLoginBmp = Graphics.zoomBitmap(tmpBmp, 
				(int)(tmpBmp.getWidth() * ActivityUtil.PAK_ZOOM_X), 
				(int)(tmpBmp.getHeight() * ActivityUtil.PAK_ZOOM_Y));
		
		tmpBmp = null;
		opts = null;
		return iBackLoginBmp;
	}

	public void releaseLoginBack() {
		iBackLoginBmp = null;
	}
	
//	public void loadRoomBack(int aID) {
//		if(iRoomBmp != null) {
//			return;
//		}
//		iBackBBmp = null;
//		if(aID < 0 || aID > 3) {
//			iBackBBmp = iBackABmp;
//			return;
//		}
//		Options opts = new Options();
//		opts.inDensity = ActivityUtil.DENSITYDPI;
//		Bitmap tmpBmp = BitmapFactory.decodeResource(
//				GameMain.mGameMain.getResources(), 
//				R.drawable.new_back00,
//				opts);
//		iBackBBmp = Graphics.zoomBitmap(tmpBmp, 
//				(int)(tmpBmp.getWidth() * ActivityUtil.PAK_ZOOM_X), 
//				(int)(tmpBmp.getHeight() * ActivityUtil.PAK_ZOOM_Y));
//		
//		tmpBmp = null;
//		opts = null;
//	}

/*
	//精简版
	public void loadRoomBack(String aID) {
		Tools.println("loadRoomBack + " + aID);
		if(iRoomBmp != null) {
			return;
		}
		//iBackBBmp = null;
		Options opts = new Options();
		opts.inDensity = ActivityUtil.DENSITYDPI;
//		ApplicationInfo appInfo = GameMain.mGameMain.getApplicationInfo();
		int resID = R.drawable.new_back00;//GameMain.mGameMain.getResources().getIdentifier(aID, "drawable", appInfo.packageName);
//		if(resID <= 0) {
//			resID = R.drawable.new_back00;
//			return;
//		}
		Bitmap tmpBmp = BitmapFactory.decodeResource(
				GameMain.mGameMain.getResources(), 
				resID,
				opts);
		if(tmpBmp != null) {
			iRoomBmp = Graphics.zoomBitmap(tmpBmp, 
				(int)(tmpBmp.getWidth() * ActivityUtil.PAK_ZOOM_X), 
				(int)(tmpBmp.getHeight() * ActivityUtil.PAK_ZOOM_Y));
		}
		tmpBmp = null;
		opts = null;
	}
*/	

// 豪华版	
	public void loadRoomBack(String aID) {
		Tools.debug("loadRoomBack + " + aID);
		Options opts = new Options();
		opts.inDensity = ActivityUtil.DENSITYDPI;
		ApplicationInfo appInfo = MyArStation.mGameMain.getApplicationInfo();
		int resID = MyArStation.mGameMain.getResources().getIdentifier(aID, "drawable", appInfo.packageName);
		if(resID <= 0) {
			resID = R.drawable.new_back00;
			return;
		}
		Bitmap tmpBmp = BitmapFactory.decodeResource(
				MyArStation.mGameMain.getResources(), 
				resID,
				opts);
		if(tmpBmp == null) {
			resID = R.drawable.new_back00;
			tmpBmp = BitmapFactory.decodeResource(
					MyArStation.mGameMain.getResources(), 
					resID,
					opts);
		}
		if(tmpBmp != null) {
			iRoomBmp = Graphics.zoomBitmap(tmpBmp, 
				(int)(tmpBmp.getWidth() * ActivityUtil.PAK_ZOOM_X), 
				(int)(tmpBmp.getHeight() * ActivityUtil.PAK_ZOOM_Y));
		}
		tmpBmp = null;
		opts = null;
	}

	public void releaseRoomBack() {
		iRoomBmp = null;
	}
	
	public Bitmap getThumBitmap(String aID, int width, int height) {
		Bitmap bitmap = null;
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		opts.inDensity = ActivityUtil.DENSITYDPI;
		ApplicationInfo appInfo = MyArStation.mGameMain.getApplicationInfo();
		int resID = MyArStation.mGameMain.getResources().getIdentifier(aID, "drawable", appInfo.packageName);
		if(resID <= 0) {
			resID = R.drawable.new_back00;
		}
		BitmapFactory.decodeResource(
				MyArStation.mGameMain.getResources(),
				resID,
				opts);
		opts.inSampleSize = calculateInSampleSize(opts.outWidth, opts.outHeight, width, height);
		opts.inJustDecodeBounds = false;
		Bitmap tmpBmp = BitmapFactory.decodeResource(
				MyArStation.mGameMain.getResources(),
				resID,
				opts);
		bitmap = ThumbnailUtils.extractThumbnail(tmpBmp, width, height);
		tmpBmp = null;
		opts = null;
		return bitmap;
	}
	
	private int calculateInSampleSize(int srcWidth, int srcHeight,
			int reqWidth, int reqHeight) {
		int inSampleSize = 1;
		
		if (srcHeight > reqHeight || srcWidth > reqWidth) {
			float scaleW = (float) srcWidth / (float) reqWidth;
			float scaleH = (float) srcHeight / (float) reqHeight;
			
			float sample = scaleW > scaleH ? scaleW : scaleH;
			// 只能是2的次幂
			if (sample < 3)
				inSampleSize = (int) sample;
			else if (sample < 6.5)
				inSampleSize = 4;
			else if (sample < 8)
				inSampleSize = 8;
			else
				inSampleSize = (int) sample;
		}
		return inSampleSize;
	}
}
