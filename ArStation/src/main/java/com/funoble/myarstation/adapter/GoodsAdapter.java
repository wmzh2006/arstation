/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: GoodsAdapter.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-29 下午04:42:08
 *******************************************************************************/
package com.funoble.myarstation.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.CacheUtils;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.socket.protocol.MBReqLogin;
import com.funoble.myarstation.socket.protocol.MBReqMainPage;
import com.funoble.myarstation.socket.protocol.MBRspLogin;
import com.funoble.myarstation.socket.protocol.MBRspMainPage;
import com.funoble.myarstation.socket.protocol.ProtocolType.ReqMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.store.data.Goods;
import com.funoble.myarstation.view.GameView;
//import com.funoble.lyingdice.screen.StoreScreen;


public class GoodsAdapter extends BaseAdapter {

	private Context			mContext;

	private List<Goods>		items;
	
	public AlertDialog 		dialog;
	
	/**
	 * 当前选中状态的行
	 */
	private View			selectedRow	= null;
	
	/**
	 * construct
	 */
	public GoodsAdapter(Context c, List<Goods> items) {
		mContext = c;
		this.items = items;
	}

	/* 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return items.size();
	}

	/* 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	/* 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_goods_row, null);
//			convertView.setBackgroundColor(0xffFBF9F2);
			convertView.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						setSelectedRow(v);
					}
					else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP)
					{
						setSelectedRow(null);
					}
					return false;
				}
			});
		}
		final Goods e = items.get(position);
		Button btTem = (Button) convertView.findViewById(R.id.ivArrow);
		btTem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
//				showConfirmBuy(e);
				if(MyArStation.iGameProtocol.requestBusiness(MyArStation.getInstance().iPlayer.iUserID, e.iGoodsID, 0)) {
					GameView view = MyArStation.getInstance().getGameCanvas().getCurrentView();
					if(view != null) {
//						if(view instanceof StoreScreen) {
//							StoreScreen screen = (StoreScreen)view;
//							screen.iGoodsPrice = e.iPrice;
//						}
						view.showProgressDialog(MyArStation.getInstance().getString(R.string.Loading_String), true);
					}
				}
			}
		});
		if(e.iType == 1) {
			((ImageView)convertView.findViewById(R.id.ivFbi)).setImageResource(R.drawable.rmb);
		}
		else {
			((ImageView)convertView.findViewById(R.id.ivFbi)).setImageResource(R.drawable.fbi);
		}
		((TextView) convertView.findViewById(R.id.tvName)).setText(e.iName);
		((TextView) convertView.findViewById(R.id.tvPrice)).setText(e.iPrice+"");
		((TextView) convertView.findViewById(R.id.tvDes)).setText(e.iDesc);
		Bitmap img = MyArStation.iHttpDownloadManager.getGoodsIcon(e.iIconID);
		((ImageView) convertView.findViewById(R.id.ivPic)).setImageBitmap(img);
		return convertView;
	}

	/**
	 * 设置当前选中状态的行View
	 * 
	 * @param v
	 */
	public void setSelectedRow(View v) {
		if (selectedRow == v) {
			return;
		}
		if(v.getId() == R.id.ivArrow) {
			
		}
//		if (selectedRow != null) {
//			selectedRow.setBackgroundColor(0xffFBF9F2); // 设置未选中的状态
//		}
//		selectedRow = v;
//		if (v != null) {
//			v.setBackgroundColor(0xffFABE0F); // 设置选中状态
//		}
	}
	
	private void showConfirmBuy(final Goods e) {
		 dialog = new AlertDialog.Builder(MyArStation.mGameMain).setTitle(R.string.confirmBuyTitile).setMessage(
				 MyArStation.mGameMain.getString(R.string.confirmBuyTips)).setCancelable(false)
				.setPositiveButton(R.string.Return_HomeScreen_Ok, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MBRspMainPage loginInfo = (MBRspMainPage)MyArStation.iGameProtocol.getMobileMsgCS(RspMsg.MSG_RSP_MAINPAGE);
						if(loginInfo != null && loginInfo.iUserId != 0) {
							if(MyArStation.iGameProtocol.requestBusiness(loginInfo.iUserId, e.iGoodsID, 0)) {
								GameView view = MyArStation.getInstance().getGameCanvas().getCurrentView();
								if(view != null) {
									view.showProgressDialog(MyArStation.getInstance().getString(R.string.Loading_String), true);
								}
							}
						}
					}
				}).setNegativeButton(R.string.Return_HomeScreen_Cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				}).create();
		dialog.show();
	}
}
