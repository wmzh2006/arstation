/*******************************************************************************
 * Copyright (C) 1998-2009 BBG Inc.All Rights Reserved.
 * FileName: AutoScrollTextView.java
 * Description：
 * History：
 * 版本号 作者 日期 简要介绍相关操作
 * 1.0 sunny 2011-5-19 Create
 *******************************************************************************/
package com.funoble.myarstation.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;

/*
 * Copyright (C) 2005-2010 puyu Inc.All Rights Reserved.
 * FileName：AutoScrollTextView.java
 * Description：
 * History：
 * 1.0 sunny 2011-5-19 Create
 */

/**
 * <p>
 * 文字滚动控件
 * </p>
 */
public class AutoScrollTextView extends TextView implements OnClickListener
{
	public final static String	TAG								= AutoScrollTextView.class.getSimpleName();

	private float				textLength						= 0f;										// 文本长度
	private float				viewWidth						= 0f;
	private float				step							= 0f;										// 文字的横坐标
	private float				y								= 0f;										// 文字的纵坐标
	private float				temp_view_plus_text_length		= 0.0f;									// 用于计算的临时变量
	private float				temp_view_plus_two_text_length	= 0.0f;									// 用于计算的临时变量
	public boolean				isStarting						= false;									// 是否开始滚动
	private Paint				paint							= null;									// 绘图样式
	private String				text							= "";										// 文本内容


	public AutoScrollTextView(Context context)
	{
		super(context);
		initView();
	}

	public AutoScrollTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView();
	}

	public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView();
	}

	/** */
	/**
	 * 初始化控件
	 */
	private void initView()
	{
		setOnClickListener(this);
	}

	/** */
	/**
	 * 文本初始化，每次更改文本内容或者文本效果等之后都需要重新初始化一下
	 */
	public void init()
	{
		paint = getPaint();
		text = getText().toString();
		textLength = paint.measureText(text);
		viewWidth = getWidth();
		if (viewWidth == 0)
		{
			WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			viewWidth = display.getWidth();
		}
		if(textLength > viewWidth) {
			step = textLength;
			temp_view_plus_text_length =  textLength;
			float det = textLength - viewWidth;
			temp_view_plus_two_text_length = det + textLength;
		}
		else {
			step = textLength;
			temp_view_plus_text_length =  textLength;
			temp_view_plus_two_text_length = textLength;
		}
		y = getTextSize() + getPaddingTop();
	}

	@Override
	public Parcelable onSaveInstanceState()
	{
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);

		ss.step = step;
		ss.isStarting = isStarting;

		return ss;

	}

	@Override
	public void onRestoreInstanceState(Parcelable state)
	{
		if (!(state instanceof SavedState))
		{
			super.onRestoreInstanceState(state);
			return;
		}
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		step = ss.step;
		isStarting = ss.isStarting;

	}

	public static class SavedState extends BaseSavedState
	{
		public boolean	isStarting	= false;
		public float	step		= 0.0f;

		SavedState(Parcelable superState)
		{
			super(superState);
		}

		@Override
		public void writeToParcel(Parcel out, int flags)
		{
			super.writeToParcel(out, flags);
			out.writeBooleanArray(new boolean[] { isStarting });
			out.writeFloat(step);
		}


		public static final Parcelable.Creator<SavedState>	CREATOR	= new Parcelable.Creator<SavedState>()
																	{

																		public SavedState[] newArray(int size)
																		{
																			return new SavedState[size];
																		}

																		@Override
																		public SavedState createFromParcel(Parcel in)
																		{
																			return new SavedState(in);
																		}
																	};

		private SavedState(Parcel in)
		{
			super(in);
			try
			{
				boolean[] b = null;
				in.readBooleanArray(b);
				if (b != null && b.length > 0)
					isStarting = b[0];
				step = in.readFloat();
			}
			catch (Exception e)
			{
			}
		}
	}

	/** */
	/**
	 * 开始滚动
	 */
	public void startScroll()
	{
		isStarting = true;
		invalidate();
	}

	/** */
	/**
	 * 停止滚动
	 */
	public void stopScroll()
	{
		isStarting = false;
		invalidate();
	}


	@Override
	public void onDraw(Canvas canvas)
	{
		if (text == null)
		{
			return;
		}
		if (paint == null)
		{
			return;
		}
		/**
		 * 获得属性色
		 * 修改者Vita
		 */
		paint.setColor(this.getTextColors().getDefaultColor());
		canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
		if (!isStarting)
		{
			return;
		}
		step += 0.8;
		if (step > temp_view_plus_two_text_length)
			step = textLength;
		invalidate();

	}

	@Override
	public void onClick(View v)
	{
		if (isStarting)
			stopScroll();
		else
			startScroll();

	}

}
