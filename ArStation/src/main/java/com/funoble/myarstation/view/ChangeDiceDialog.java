package com.funoble.myarstation.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.funoble.myarstation.adapter.ChangeDiceAdapter;
import com.funoble.myarstation.adapter.ChangeDiceAdapter.OnChangeDiceListener;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.common.Util;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.store.data.ChangeDiceData;

/**
 * <p>
 * 
 * </p>
 */
/**
 * description 
 * version 1.0
 * author 
 * update 2012-11-16 下午05:02:35 
 */
public class ChangeDiceDialog extends Dialog implements
		OnChangeDiceListener {
	final int MAX_DICE_TYPE_COUNT = 5;
	final int MAX_DICE_COUNT = 6;
	private ViewGroup mLayout;
	private ListView listView;
	private ChangeDiceAdapter adapter;
	private List<ChangeDiceData> list;
	private int[][] diceBitmaps;
	/**
	 * @param context
	 */
	public ChangeDiceDialog(Context context) {
		super(context, R.style.PlayerInfodialog);
		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_changedice, null);
		setContentView(mLayout);
		diceBitmaps = new int[MAX_DICE_TYPE_COUNT][MAX_DICE_COUNT];
		for(int i=0; i<MAX_DICE_TYPE_COUNT; i++) {
			for(int k=0; k<MAX_DICE_COUNT; k++) {
				diceBitmaps[i][k] = R.drawable.new_dice0_big1 + i * MAX_DICE_COUNT + k;
			}
		}
		listView = (ListView) mLayout.findViewById(R.id.change_dice);
		list = new ArrayList<ChangeDiceData>();
		for(int i = 0; i < diceBitmaps.length; i++) {
			ChangeDiceData data = new ChangeDiceData(i, diceBitmaps[i]);
			list.add(data);
		}
		adapter = new ChangeDiceAdapter(MyArStation.getInstance().getApplicationContext(), list);
		adapter.setOnChangeDiceListener(this);
		listView.setAdapter(adapter);
		this.setCancelable(true);
		this.getWindow().setWindowAnimations(R.style.putupinanimation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Dialog#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		Display d = getWindow().getWindowManager().getDefaultDisplay();
		LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
//		p.height = (int) d.getHeight(); // 高度设置为屏幕的1.0
		int tempH = Tools.dip2px(MyArStation.mGameMain, 10 * 2 + 1 * 4);
		p.width = (int) d.getHeight();//((d.getHeight() - tempH) / 6 * 6); // 宽度设置为屏幕的0.8
//		// p.alpha = 1.0f; //设置本身透明度
//		// p.dimAmount = 0.0f; //设置黑暗度
//
		getWindow().setAttributes(p); // 设置生效
		getWindow().setGravity(Gravity.CENTER); // 设置靠右对齐
	}

	/* 
	 * @see com.funoble.lyingdice.adapter.ChangeDiceAdapter.OnChangeDiceListener#OnClick(int)
	 */
	@Override
	public void OnClick(int type) {
		MyArStation.iGameProtocol.RequestChangeDiceType(type);
		this.dismiss();
		Tools.debug("selceted dice = " + type);
	}
}
