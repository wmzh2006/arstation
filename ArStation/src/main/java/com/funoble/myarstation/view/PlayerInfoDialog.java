package com.funoble.myarstation.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.CPlayer;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;

/**
 * <p>
 * 
 * </p>
 */
public class PlayerInfoDialog extends Dialog implements
		android.view.View.OnClickListener {
	private final int VIP_MAX = 5;
	private ViewGroup mLayout;
	private ImageView	ivHead;
	private TextView	iVIP;
	private ImageView	iSex;
	private TextView tvName;
	private TextView tvTitle;
	private TextView tvWinPecent;
	private TextView tvDing;
	private TextView tvFuddle;
	private TextView tvDoubleHit;
	private Context context;
	
//	private int[] vipRes = {
//			R.drawable.new_playerdetail_vip0,
//			R.drawable.new_playerdetail_vip1,
//			R.drawable.new_playerdetail_vip2,
//			R.drawable.new_playerdetail_vip3,
//			R.drawable.new_playerdetail_vip4,
//			R.drawable.new_playerdetail_vip5
//	};
	/**
	 * @param context
	 */
	public PlayerInfoDialog(Context context) {
		super(context, R.style.PlayerInfodialog);
		this.context = context;
		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_playerinfo, null);

		setContentView(mLayout);

		ivHead = (ImageView) mLayout.findViewById(R.id.ivhead);
		iVIP = (TextView) mLayout.findViewById(R.id.ivVip);
		iSex = (ImageView) mLayout.findViewById(R.id.ivsex);
		tvName = (TextView) mLayout.findViewById(R.id.tvName);
		tvTitle = (TextView) mLayout.findViewById(R.id.tvlevel);
		tvWinPecent = (TextView) mLayout.findViewById(R.id.tvwin);
		tvDing = (TextView) mLayout.findViewById(R.id.tvding);
		tvFuddle = (TextView) mLayout.findViewById(R.id.tvFuddle);
		tvDoubleHit = (TextView) mLayout.findViewById(R.id.tvdoublehit);
		this.setCanceledOnTouchOutside(true);
	}

	
	public void show(CPlayer cPlayer) {
		if(cPlayer == null) {
			return;
		}
		Bitmap head = MyArStation.iHttpDownloadManager.getImage(cPlayer.szLittlePicName);
		ivHead.setImageBitmap(head);
		head = null;
//		Bitmap vip;
		int viplevel = cPlayer.iVipLevel;
//		if(viplevel >= VIP_MAX) {
//			vip = BitmapFactory.decodeResource(context.getResources(),
//					vipRes[VIP_MAX]);
//		}
//		else {
//			vip = BitmapFactory.decodeResource(context.getResources(),
//					vipRes[viplevel]);
//		}
//		iVIP.setImageBitmap(vip);
		if(viplevel > 0) {
			tvName.setTextColor(Color.YELLOW);
			iVIP.setTextColor(Color.YELLOW);
			iVIP.setShadowLayer(5F, 0F,0F, Color.RED);
			iVIP.setText(context.getString(R.string.VipLevel, viplevel));
			iVIP.setVisibility(View.VISIBLE);
		}
		else {
			tvName.setTextColor(Color.WHITE);
			ColorStateList csl = (ColorStateList)context.getResources().getColorStateList(R.color.gray_white_color);  
			iVIP.setTextColor(csl);
			iVIP.setVisibility(View.GONE);
		}
//		if(cPlayer.iSex == 0) {
//			iSex.setImageResource(R.drawable.sex_male);
//		}
//		else {
//			iSex.setImageResource(R.drawable.sex_female);
//		}
		Tools.debug("cPlayer.iSex " + cPlayer.iSex);
		tvName.setText(cPlayer.stUserNick);
		tvTitle.setText(cPlayer.iTitle);
		tvWinPecent.setText(cPlayer.iGb+"");
		tvDing.setText(cPlayer.iDingCount+"");
		tvFuddle.setText(cPlayer.iReward+"人");
		tvDoubleHit.setText(cPlayer.iMaxHit+"");
		this.show();
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
//		p.height = (int) (d.getHeight() * 0.9); // 高度设置为屏幕的1.0
		p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.8
//		// p.alpha = 1.0f; //设置本身透明度
//		// p.dimAmount = 0.0f; //设置黑暗度
//
//		getWindow().setAttributes(p); // 设置生效
		getWindow().setGravity(Gravity.CENTER); // 设置靠右对齐
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}
}
