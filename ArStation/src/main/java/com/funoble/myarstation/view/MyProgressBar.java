package com.funoble.myarstation.view;

import java.text.DecimalFormat;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class MyProgressBar extends ProgressBar {   
	String text;   
	Paint mPaint;   
	int color = Color.WHITE;
	
	public MyProgressBar (Context context) {   
		super(context);   
		initText();   
	}   
	
	public MyProgressBar (Context context, AttributeSet attrs, int defStyle) {   
		super(context, attrs, defStyle);   
		initText();   
	}   
	
	public MyProgressBar (Context context, AttributeSet attrs) {   
		super(context, attrs);   
		initText();   
	}   
	
	@Override  
	public synchronized void setProgress(int progress) {   
		setText(progress);   
		super.setProgress(progress);   
	}   
	
	@Override  
	protected synchronized void onDraw(Canvas canvas) {   
		super.onDraw(canvas);   
		//this.setText();   
		Rect rect = new Rect();   
		mPaint.setColor(color);
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextSize(Tools.dip2px(MyArStation.mGameMain.getApplicationContext(), 14));
		this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);   
		int x = (getWidth() / 2) - rect.centerX();   
		
		int y = (getHeight() / 2) - rect.centerY();   
		canvas.drawText(this.text, x, y, this.mPaint);   
	}   
	
//初始化，画笔   
	private void initText() {   
		this.mPaint = new Paint();   
		this.mPaint.setColor(Color.WHITE);   
	}   
	
//设置文字内容   
	private void setText(int progress) {   
		double f = (double)progress / (double)this.getMax() ; //除法求值
        DecimalFormat format = new DecimalFormat("##0.00%");
//		double i = (double)Math.round((progress / this.getMax() * 100) / 100.00);   
		this.text = format.format(f);   
	}   
//设置文字内容   
	public void setText(String progress) {   
		this.text = progress;   
	}   
	
	public void setTextColor(int color) {
		this.color = color;
	}
}  
