package com.funoble.myarstation.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ListView;

import com.funoble.myarstation.adapter.GiftAdapter;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;

/**
 * <p>
 * 
 * </p>
 */
public class GiftDialog extends Dialog implements
		android.view.View.OnClickListener {

	private ViewGroup mLayout;
	private Button btnGift;
	private ListView lsMsg;
	private List<String> gifts;
	private GiftAdapter adapter;
	
	/**
	 * @param context
	 */
	public GiftDialog(Context context) {
		super(context, R.style.MSGDialogStyle);

		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_gift, null);

		setContentView(mLayout);
		lsMsg = (ListView)mLayout.findViewById(R.id.lvGiftContent);
		gifts = new ArrayList<String>();
		adapter = new GiftAdapter(context, gifts);
		lsMsg.setAdapter(adapter);
		btnGift = (Button) mLayout.findViewById(R.id.btnGet);
		btnGift.setOnClickListener(this);
		this.getWindow().setWindowAnimations(R.style.putupinanimation);
		this.setCancelable(false);
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
		p.height = (int) (d.getHeight() * 1.0); // 高度设置为屏幕的1.0
		p.width = (int) (d.getWidth() * 1.0); // 宽度设置为屏幕的0.8
		getWindow().setAttributes(p); // 设置生效
		getWindow().setGravity(Gravity.TOP | Gravity.CENTER); // 设置靠右对齐
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
		if (id == R.id.btnGet) {
			this.dismiss();
		} else {
		}
	}
	
	public void setGiftDatas(List<String> datas) {
		if(datas == null) {
			return;
		}
		gifts.clear();
		gifts.addAll(datas);
		adapter.notifyDataSetChanged();
		lsMsg.setAdapter(adapter);
		this.show();
	}
}
