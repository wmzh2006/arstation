/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: Lightning.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2013-8-25 下午07:22:07
 *******************************************************************************/
package com.funoble.myarstation.utils;

import java.util.Random;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.common.Util;
import com.funoble.myarstation.game.MyArStation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;


public class Lightning {
	final int kMaxLines = 200;
	
	Point m_strikePoint;
	Point m_strikePoint2;
	boolean m_split;
	int m_displacement;
	int m_minDisplacement;
	long m_seed;
	float m_lineWidth;
	int m_tColor;
	int m_opacity;
	int m_time;
	int m_shadowColor;
	boolean m_visible;
	private Path path = new Path();  
	private Random random = new Random();  
	Paint paint = new Paint();
	
	public Lightning() {
		init(new Point(0,0), new Point(0,0));
	}
	
	public void drawLightning(float x1, float y1, float x2, float y2, int displace, int minDisplace) {
		if (displace < minDisplace) {
			path.moveTo(x1, y1);
			path.lineTo(x2, y2);
		}
		else {
			float mid_x = (x2+x1)/2;
			float mid_y = (y2+y1)/2;
			mid_x += (Math.random()-.5)*displace;
			mid_y += (Math.random()-.5)*displace;
			drawLightning(x1,y1,mid_x,mid_y,displace/2, minDisplace);
			drawLightning(x2,y2,mid_x,mid_y,displace/2, minDisplace);
		}
	}
	
	public void init(Point strikePoint,Point strikePoint2){
		 m_strikePoint = strikePoint;
		 m_strikePoint2 = strikePoint2;
		 m_tColor = Color.WHITE;
		 m_opacity = 255;
		 m_seed = random.nextLong();
		 m_split = false;
		 m_displacement = (int) (160 * ActivityUtil.ZOOM_X);
		 m_minDisplacement = (int) (8 * ActivityUtil.ZOOM_X);
		 m_visible = true;
		 m_time = 8;
		 m_shadowColor = Color.RED;
//		 Shader mShasder = new LinearGradient(0, 0, 40, 60, new int[]{Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW}, null, Shader.TileMode.REPEAT);  
//		 paint.setShader(mShasder);
		 paint.setColor(m_tColor);
		 paint.setAlpha(m_opacity);
		 paint.setStyle(Style.STROKE);  
		 paint.setAntiAlias(true);  
		 setShadowColor(m_shadowColor);
		 paint.setStrokeWidth(Tools.dip2px(MyArStation.mGameMain.getApplicationContext(), 2));
	}
	
	public void draw(Canvas canvas) {
		if(!m_visible) {
			return;
		}
		path.reset();// 重置path  
		drawLightning(m_strikePoint.x, m_strikePoint.y, m_strikePoint2.x, m_strikePoint2.y, m_displacement, m_minDisplacement);
//		canvas.drawPath(path, paint2);
	    canvas.drawPath(path, paint);
//	    canvas.drawCircle(200, 40, 30, paint);  
	}

	public void setStrikePoint(int x, int y) {
		m_strikePoint.x = x;
		m_strikePoint.y = y;
	}
	
	public void setStrikePoint2(int x, int y) {
		m_strikePoint2.x = x;
		m_strikePoint2.y = y;
	}
	
	public void strikeRandom() {
		if(!m_visible) {
			return;
		}
		if(m_time > 0) {
			m_time--;
			if(m_time <= 0) {
				m_time = 0;
				m_visible = false;
			}
		}
		m_seed = random.nextLong();
	}
	
	public void setDetTime(int time) {
		m_time = time;
	}
	
	public void setVisible(boolean visible) {
		m_visible = visible;
	}
	
	public void setShadowColor(int color) {
		m_shadowColor = color;
		paint.setShadowLayer(5, 0, 0, m_shadowColor);
	}
	
	public void setMinDsiplacment(int minDisplacement) {
		m_minDisplacement = (int) (minDisplacement * ActivityUtil.ZOOM_X);
	}
	
	public void setDsiplacment(int displacement) {
		m_displacement = (int) (displacement * ActivityUtil.ZOOM_X);
	}
}
