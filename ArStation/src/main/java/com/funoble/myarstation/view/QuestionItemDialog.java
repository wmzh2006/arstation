package com.funoble.myarstation.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.funoble.myarstation.adapter.RestPswdAdapter;
import com.funoble.myarstation.adapter.RestPswdAdapter.ItemListen;
import com.funoble.myarstation.data.cach.RestPwdData;
import com.funoble.myarstation.data.cach.RoomListData;
import com.funoble.myarstation.data.cach.VipData;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;

/**
 * <p>
 * 
 * </p>
 */
public class QuestionItemDialog extends PopupWindow implements
		android.view.View.OnClickListener, ItemListen{

	private ViewGroup mLayout;
	private ListView listView;
	private ArrayList<RestPwdData> dates;
	private RestPswdAdapter adapter;
	ItemListen itemListen;
	public static final String [] questions = {
			"您最喜欢的明星是谁？",
			"您的情敌是谁？",
			"您的小学班主任是谁？",
			"您的初恋情人是谁？"
	};
	/**
	 * @param context
	 */
	public QuestionItemDialog(Context context) {
//		super(GameMain.mGameMain.getApplicationContext());
		super(context);

		mLayout = (ViewGroup) LayoutInflater.from(MyArStation.mGameMain.getApplicationContext()).inflate(
				R.layout.new_room_limit, null);
		setContentView(mLayout);
		listView = (ListView) mLayout.findViewById(R.id.lvroomlimit);
		dates = new ArrayList<RestPwdData>();
		for(int i = 0; i < questions.length; i++) {
			RestPwdData date = new RestPwdData(i+1, questions[i]);
			dates.add(date);
		}
		adapter = new RestPswdAdapter(MyArStation.mGameMain.getApplicationContext(), dates);
		adapter.setItemListen(this);
		listView.setAdapter(adapter);
		setOutsideTouchable(true);
		setFocusable(true);
		setTouchable(true);
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
	}


//	/* 
//	 * @see android.app.Dialog#onStart()
//	 */
//	@Override
//	protected void onStart() {
//		Window dialogWindow = this.getWindow();
//		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//		dialogWindow.setGravity(Gravity.CENTER);
//		lp.width = Tools.px2dip(GameMain.mGameMain.getApplicationContext(), 200);
//		lp.height = LayoutParams.WRAP_CONTENT;
////		lp.dimAmount = 0.0f; //设置黑暗度
//        dialogWindow.setAttributes(lp);
//	}

	public void setItemListen(ItemListen l) { 
		itemListen = l;
	}
	/* 
	 * @see com.funoble.lyingdice.adapter.VipRoomLimitListAdapter.ItemListen#onClick(java.lang.Object)
	 */
	@Override
	public void onClick(Object o) {
		itemListen.onClick(o);
		this.dismiss();
	}


	/* 
	 * @see android.widget.PopupWindow#showAtLocation(android.view.View, int, int, int)
	 */
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
	}


	/* 
	 * @see android.widget.PopupWindow#showAsDropDown(android.view.View)
	 */
	@Override
	public void showAsDropDown(View anchor) {
		super.showAsDropDown(anchor);
	}


	/* 
	 * @see android.widget.PopupWindow#showAsDropDown(android.view.View, int, int)
	 */
	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		super.showAsDropDown(anchor, xoff, yoff);
	}


	/* 
	 * @see android.widget.PopupWindow#dismiss()
	 */
	@Override
	public void dismiss() {
		try {
			super.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
