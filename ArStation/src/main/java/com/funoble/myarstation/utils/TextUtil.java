/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: TextUitl.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2013-6-14 下午11:26:51
 *******************************************************************************/
package com.funoble.myarstation.utils;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

public class TextUtil {
	private float mTextPosx = 0;// x坐标
	private float mTextPosy = 0;// y坐标
	private float mTextWidth = 0;// 绘制宽度
	private float mTextHeight = 0;// 绘制高度
	private int mFontHeight = 0;// 绘制字体高度
	private int mPageLineNum = 0;// 每一页显示的行数
	private int mCanvasBGColor = 0;// 背景颜色
	private int mFontColor = 0;// 字体颜色
	private int mAlpha = 0;// Alpha值
	private int mRealLine = 0;// 字符串真实的行数
	private int mCurrentLine = 0;// 当前行
	private int mTextSize = 0;// 字体大小
	private String mStrText = "";
	private Vector<String> mString = null;
	private Paint mPaint = null;

	public TextUtil(String StrText, float x, float y, float w, float h,
			int bgcolor, int textcolor, int alpha, int textsize) {
		mPaint = new Paint();
		mString = new Vector<String>();
		this.mStrText = StrText;
		this.mTextPosx = x;
		this.mTextPosy = y;
		this.mTextWidth = w;
		this.mTextHeight = h;
		this.mCanvasBGColor = bgcolor;
		this.mFontColor = textcolor;
		this.mAlpha = alpha;
		this.mTextSize = textsize;
		initText();
	}

	public void initText() {
		mString.clear();// 清空Vector
		// 对画笔属性的设置
//		mPaint.setARGB(this.mAlpha, Color.red(this.mFontColor),
//				Color.green(this.mFontColor), Color.blue(this.mFontColor));
		mPaint.setColor(this.mFontColor);
		mPaint.setTextSize(this.mTextSize);
		// mPaint.setColor(Color.BLUE);
		mPaint.setAntiAlias(true);

		this.getTextIfon();
	}

	public void getTextIfon() {
		char ch;
		int w = 0;
		int istart = 0;
		FontMetrics fm = mPaint.getFontMetrics();// 得到系统默认字体属性
		mFontHeight = (int) (Math.ceil(fm.descent - fm.top) + 2);// 获得字体高度
		mPageLineNum = (int) (mTextHeight / mFontHeight);// 获得行数

		int count = this.mStrText.length();
		for (int i = 0; i < count; i++) {
			ch = this.mStrText.charAt(i);
			float[] widths = new float[1];
			String str = String.valueOf(ch);
			mPaint.getTextWidths(str, widths);
			if (ch == '\n') {
				mRealLine++;// 真实的行数加一
				mString.addElement(this.mStrText.substring(istart, i));
				istart = i + 1;
				w = 0;
			} else {
				w += (int) Math.ceil(widths[0]);
				if (w > this.mTextWidth) {
					mRealLine++;// 真实的行数加一
					mString.addElement(this.mStrText.substring(istart, i));
					istart = i;
					i--;
					w = 0;
				} else {
					if (i == count - 1) {
						mRealLine++;// 真实的行数加一
						mString.addElement(this.mStrText.substring(istart,
								count));
					}
				}
			}
		}
	}

	public void drawText(Canvas canvas) {
		for (int i = this.mCurrentLine, j = 0; i < this.mRealLine; i++, j++) {
			if (j > this.mPageLineNum) {
				break;
			}
			canvas.drawText((String) (mString.elementAt(i)), this.mTextPosx,
					this.mTextPosy + this.mFontHeight * (j+1), mPaint);
		}
	}
}