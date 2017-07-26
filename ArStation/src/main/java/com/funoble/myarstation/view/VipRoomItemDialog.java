package com.funoble.myarstation.view;

import java.util.ArrayList;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.funoble.myarstation.adapter.VipRoomLimitListAdapter;
import com.funoble.myarstation.adapter.VipRoomLimitListAdapter.ItemListen;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.RoomListData;
import com.funoble.myarstation.data.cach.VipData;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;

/**
 * <p>
 * 
 * </p>
 */
public class VipRoomItemDialog extends PopupWindow implements
		android.view.View.OnClickListener, ItemListen{

	private ViewGroup mLayout;
	private ListView listView;
	private ArrayList<VipData> dates;
	private VipRoomLimitListAdapter adapter;
	ItemListen itemListen;
	/**
	 * @param context
	 */
	public VipRoomItemDialog(Context context) {
//		super(GameMain.mGameMain.getApplicationContext());
		super(context);

		mLayout = (ViewGroup) LayoutInflater.from(MyArStation.mGameMain.getApplicationContext()).inflate(
				R.layout.new_room_limit, null);
		setContentView(mLayout);
		listView = (ListView) mLayout.findViewById(R.id.lvroomlimit);
		dates = new ArrayList<VipData>();
//		for(int i = 0; i < 5; i++) {
//			VipDate date = new VipDate(2, 3, "22233333" + i);
//			dates.add(date);
//		}
		adapter = new VipRoomLimitListAdapter(MyArStation.mGameMain.getApplicationContext(), dates);
		adapter.setItemListen(this);
		listView.setAdapter(adapter);
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
	
	public void setData(Object o) {
		dates.clear();
		RoomListData data = (RoomListData) o;
		if(data == null) {
			return;
		}
		ArrayList<String> bet = data.ivipbet;
		for(int i = 0; i < bet.size(); i++) {
			VipData vipDate = new VipData(i, data.iRoomID, data.iTableID, bet.get(i), data.iState);
			dates.add(vipDate);
		}
		adapter.notifyDataSetChanged();
	}
}
