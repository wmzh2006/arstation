package com.funoble.myarstation.view;

import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.funoble.myarstation.adapter.VipRoomLimitListAdapter.ItemListen;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.RoomListData;
import com.funoble.myarstation.data.cach.VipData;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;

/**
 * <p>
 * 
 * </p>
 */
public class VipRoomDialog extends Dialog implements
		android.view.View.OnClickListener, ItemListen{

	private ViewGroup mLayout;
	private TextView title;
	private Button comit;
	private Button cancel;
	private Button bet;
	private EditText pswd;
	VipRoomItemDialog dialog;
	VipData data;
	PopupWindow popupWindow;
	LinearLayout betLayout;
	/**
	 * @param context
	 */
	public VipRoomDialog(Context context) {
		super(context, R.style.MSGDialogStyle);

		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_bookviproom, null);
		setContentView(mLayout);
		title = (TextView) mLayout.findViewById(R.id.tvroomname);
		betLayout = (LinearLayout) mLayout.findViewById(R.id.llbet);
		bet = (Button) mLayout.findViewById(R.id.btnbet);
		bet.setOnClickListener(this);
		comit = (Button) mLayout.findViewById(R.id.btncommit);
		comit.setOnClickListener(this);
		cancel = (Button) mLayout.findViewById(R.id.btncancel);
		cancel.setOnClickListener(this);
		pswd =(EditText) mLayout.findViewById(R.id.etpwd);
		pswd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
		dialog = new VipRoomItemDialog(context);
		measure();
		dialog.setFocusable(false);  
		dialog.setOutsideTouchable(true);
		dialog.setItemListen(this);
		this.setCancelable(false);
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
		int id = v.getId();
		if (id == R.id.btnbet) {
			//			if(data != null && data.) {
//				
//			}
			dialog.setWidth(bet.getWidth());
			dialog.setHeight(LayoutParams.WRAP_CONTENT);
			dialog.showAsDropDown(bet);
		} else if (id == R.id.btncommit) {
			if(data != null) {
				String temString = pswd.getText().toString().trim();
				if(temString != null && !temString.equals("")) {
					if(data.status == 0) {
						MyArStation.iGameProtocol.RequestLoginVipRoom(data.roomID, 0, data.ID, temString);
					}
					else {
						MyArStation.iGameProtocol.RequestLoginVipRoom(data.roomID, 1, data.ID, temString);
					}
					pswd.setText("");
					this.dismiss();
				}
				else {
					Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), "密码不能为空！");
				}
			}
			else {
				this.dismiss();
			}
		} else if (id == R.id.btncancel) {
			this.dismiss();
		} else {
		}
	}


	/* 
	 * @see android.app.Dialog#onStart()
	 */
	@Override
	protected void onStart() {
		measure();
	}


	/**
	 * 
	 */
	private void measure() {
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		lp.width = (int) (500 * ActivityUtil.ZOOM_X);
		lp.height = LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.update(bet, lp.width, LayoutParams.WRAP_CONTENT);
	}


	/* 
	 * @see com.funoble.lyingdice.adapter.VipRoomLimitListAdapter.ItemListen#onClick(java.lang.Object)
	 */
	@Override
	public void onClick(Object o) {
		data = null;
		data = (VipData) o;
		if(data != null) {
			bet.setText(data.roomLimit);
			bet.setTag(data.ID);
		}
	}


	/* 
	 * @see android.app.Dialog#dismiss()
	 */
	@Override
	public void dismiss() {
		dialog.dismiss();
		super.dismiss();
	}
	
	public void setTitle(String name) {
		String temp = name;
		if(temp == null) {
			temp = "";
		}
		title.setText(temp);
	}
	
	public void setDate(Object o) {
		RoomListData data = (RoomListData) o;
		if(data == null) {
			return ;
		}
		if(data.iState == 0) {
			betLayout.setVisibility(View.VISIBLE);
		}
		else {
			betLayout.setVisibility(View.GONE);
		}
		this.data = new VipData(0, data.iRoomID, data.iTableID, data.ivipbet.get(0), data.iState);
		bet.setText(data.ivipbet.get(0));
		bet.setTag(0);
		setTitle(data.iRoomName);
		dialog.setData(o);
	}
	
	
}
