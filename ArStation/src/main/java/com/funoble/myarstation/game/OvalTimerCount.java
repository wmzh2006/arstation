/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: OvalTimerCount.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-10-30 上午10:55:47
 *******************************************************************************/
package com.funoble.myarstation.game;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.SweepGradient;

import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.ConstantUtil;


/**
 * description 
 * version 1.0
 * author 
 * update 2014年1月20日 上午12:08:21 
 */
public class OvalTimerCount {
	Bitmap bigCircle;
	Bitmap smallCircle;
	Bitmap DstCircle;
	public Bitmap Circle;
	private boolean ibInit = false;
	
	int radius;
	int angle = 0;
	int det = 1;
	boolean runnimg = false;
	Thread thread;
	private TimerCountOut timerCountOut;
	long startTime;
	int delay = 0;
	boolean self = false;
	
	public OvalTimerCount(int radius, int archW, TimerCountOut timerCountOut) {
		this.radius = radius;
		bigCircle = BitmapArc(radius, 360, Color.YELLOW, Color.RED);
		smallCircle = BitmapArc(radius - archW, 360, Color.GREEN, Color.BLUE);
		DstCircle = cover(smallCircle, bigCircle, Mode.XOR);
		this.timerCountOut = timerCountOut;
		//
		runnimg = true;
		//创建线程
		thread = new Thread(new timer());
		thread.start();
	}
	
	private Bitmap cover(Bitmap src, Bitmap dst, Mode mode) {
		if(src == null || dst == null) {
			return null;
		}
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap mCombinedBm = Bitmap.createBitmap(dst);
		Canvas canvas = new Canvas(mCombinedBm);
		paint.setXfermode(new PorterDuffXfermode(mode));
		canvas.drawBitmap(src, (Math.abs(dst.getWidth() - src.getWidth())) >> 1, Math.abs((dst.getHeight() - src.getHeight())) >> 1, paint);
		paint.setXfermode(null);
		return mCombinedBm;
	}
	
	private Bitmap BitmapArc(int radius, int aAngle, int color1, int color2) {
		Bitmap dst = Bitmap.createBitmap(radius*2, radius*2, Config.ARGB_8888);
		dst.eraseColor(Color.TRANSPARENT);
		Canvas canvas  = new Canvas(dst);
		Paint paint=new Paint();
		paint.setAntiAlias(true);
		SweepGradient lg = new SweepGradient(radius, radius, color1, color2);
		paint.setShader(lg);
		RectF rectF = new RectF(0, 0, radius*2, radius*2);
		canvas.drawArc(rectF, 0, aAngle, true, paint);
		Matrix matrix = new Matrix();  
		matrix.postRotate(-90); /*反向翻转90度*/  
		int width = dst.getWidth();  
		int height = dst.getHeight();  
		Bitmap img = Bitmap.createBitmap(dst, 0, 0, width, height, matrix, true);  
		   
		return img;
	}
	
	private Bitmap BitmapArc360(int radius, int aAngle, int color1, int color2) {
		Bitmap dst = Bitmap.createBitmap(radius*2, radius*2, Config.ARGB_8888);
		dst.eraseColor(Color.TRANSPARENT);
		Canvas canvas  = new Canvas(dst);
		Paint paint=new Paint();
		paint.setAntiAlias(true);
		SweepGradient lg = new SweepGradient(radius, radius, color1, color2);
		paint.setShader(lg);
		RectF rectF = new RectF(0, 0, radius*2, radius*2);
		canvas.drawArc(rectF, aAngle, 360-aAngle, true, paint);
		Matrix matrix = new Matrix();  
		matrix.postRotate(-90); /*反向翻转90度*/  
		int width = dst.getWidth();  
		int height = dst.getHeight();  
		Bitmap img = Bitmap.createBitmap(dst, 0, 0, width, height, matrix, true);  
		   
		return img;
	}
	
	public void draw(Canvas canvas, int x, int y) {
		if(runnimg && ibInit == true && Circle != null) {//
			canvas.drawCircle(x+radius, y+radius, this.radius, ActivityUtil.mAlphaRect);
			canvas.drawBitmap(Circle, x, y, null);
		}
	}
	
	public void start() {
		ibInit = true;
		angle = 0;
		det = 1;
		startTime = System.currentTimeMillis();
	}
	
	/**
	 * @param aSelf 
	 * @param isDelay
	 */
	public void start(boolean aSelf, int delayCount) {
		if(delayCount >= 3) {
			delayCount = 3;
		}
		else if(delayCount >= 1) {
			delayCount = 2;
		}
		else {
			delayCount = 1;
		}
		ibInit = true;
		angle = 0;
		self = aSelf;
		det = self ? delayCount : 1;
		startTime = System.currentTimeMillis();
	}
	
	public void stop() {
		runnimg = false;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public void pause() {
		if (self) {
			delay = (int) ((System.currentTimeMillis() - startTime) / 1000);
		}
		ibInit = false;
	}
	
	private class timer implements Runnable {

		/* 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			while(runnimg) {
				if(ibInit == false) {
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				try {
//					synchronized (this) {
					long timeSt = System.currentTimeMillis();
					if(angle > 360) {
						angle = 0;
						ibInit = false;
						if(timerCountOut != null) {
							if (self) {
								delay = (int) ((System.currentTimeMillis() - startTime) / 1000);
							}
							timerCountOut.TimeOut();
						}
						continue;
					}
					//
					Bitmap sBitmap = BitmapArc360(radius, angle, Color.WHITE, Color.GRAY);
					Circle = cover(DstCircle, sBitmap, Mode.SRC_IN);
//						Bitmap sBitmap2 = cover(DstCircle, sBitmap, Mode.SRC_IN);
//						Circle = cover(DstCircle, sBitmap2, Mode.SRC_OUT);
					//
					angle += det;
//					System.out.println("计时器 angle = " + angle);
					//
					long timeEnd = System.currentTimeMillis() - timeSt;
					if(timeEnd < ConstantUtil.TMSPF){
						try {
							Thread.sleep(ConstantUtil.TMSPF - timeEnd);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
//					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		
	}
	
	public interface TimerCountOut {
		public void TimeOut();
	}
}
