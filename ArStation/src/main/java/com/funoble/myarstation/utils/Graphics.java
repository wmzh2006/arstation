package com.funoble.myarstation.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

//图片绘制工具
public final class Graphics {
//	private static Bitmap[] logoBitmap;
//	public static Bitmap[] systemPublic;
	
	private static Paint paint;

	//锚点相关的常量
	public static final int HCENTER = 1;    	//HCENTER为水平，X不变
	public static final int VCENTER = 1<<1;		//VCENTER为垂直，Y不变
	public static final int LEFT 	= 1<<2;		//X轴左侧
	public static final int RIGHT 	= 1<<3;		//Y轴右侧
	public static final int TOP 	= 1<<4;		//Y轴上方
	public static final int BOTTOM  = 1<<5;		//y轴下方
	
	//private static BitMapManager bitMapManager = null; 

	private Graphics(){ //该类不能继承
		
	}

	//设置画笔
	public static void setPaint(){
		paint = ActivityUtil.mPaint;
	}

//	public static void loadLogoBitMap(Resources r){
//		logoBitmap = new Bitmap[ConstantUtil.LOGO_BITMAP_LENGTH];
//		logoBitmap[0] = BitmapFactory.decodeResource(r, R.drawable.log);
//	}
//
//	public static void drawLogo(int imgId, Canvas g, int x, int y, int anchor,Paint paint){
//		paint.setColor(Color.WHITE);
//		g.drawRect(0, 0, ActivityUtil.SCREEN_WIDTH, ActivityUtil.SCREEN_HEIGHT, paint);
//
//		//g.drawBitmap(logoBitmap[imgId], left, top, paint)
//		//g.drawBitmap(bitmap, left, top, paint)
//		
//		
//	}
//
//	public static void loadSystemPublic(Resources r){
//		//创建系统需要的图片数组对象
//		systemPublic = new Bitmap[ConstantUtil.SYSTEM_BITMAP_LENGTH];
//		systemPublic[0] = BitmapFactory.decodeResource(r, R.drawable.progress);
//	}
//
//	//绘制系统图片
//	public static void drawSystemPublic(int imgId, Canvas canvas, int x, int y, Paint paint){
//		canvas.drawBitmap(systemPublic[imgId], x, y, paint);
//	}

	//画图方法
	public static void drawImage(Canvas g, Bitmap bitmap,int x, int y, int anchor){
		int imgW = bitmap.getWidth();
		int imgH = bitmap.getHeight();
		
		if(HCENTER == (anchor & HCENTER)){ //以图片中心点位锚点
			
			x = x - (imgW >> 1);
		}else if(RIGHT == (anchor & RIGHT)){
			
			x = x - imgW;
		}

		if(VCENTER == (anchor & VCENTER)){

			y = y - (imgH >> 1);
		}else if(BOTTOM == (anchor & BOTTOM)){
			y = y - imgH;
		}

		g.drawBitmap(bitmap, x, y, paint);
	}
	
	public static Bitmap zoomBitmap(Bitmap bitmap,int w,int h){   
		int width = bitmap.getWidth();   
		int height = bitmap.getHeight();   
		Matrix matrix = new Matrix();   
		float scaleWidht = ((float)w / width);   
		float scaleHeight = ((float)h / height);   
		matrix.postScale(scaleWidht, scaleHeight);   
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);   
		return newbmp;   
	}
	
	public static Bitmap CreateTransBitmap(int width, int height) {
		Bitmap temp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(temp);
		canvasTemp.drawColor(Color.TRANSPARENT);   
		canvasTemp = null;
		return temp;
	}
}
