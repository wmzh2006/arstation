package com.funoble.myarstation.view;

import java.util.ArrayList;
import java.util.List;

import com.funoble.myarstation.adapter.NoticeAdapter;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <p>
 * 
 * </p>
 */
public class NoticeDialog extends Dialog implements
		android.view.View.OnClickListener {

	private ViewGroup mLayout;


	private Button btnClose;
	private ListView iNoticeView;
	private List<String> iNoticeList;
	private NoticeAdapter iNoticeAdapter;
	/**
	 * @param context
	 */
	public NoticeDialog(Context context) {
		super(context, R.style.MSGDialogStyle);

		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_notice, null);

		setContentView(mLayout);
		iNoticeView = (ListView)mLayout.findViewById(R.id.lvNotice);
		iNoticeList = new ArrayList<String>();
		btnClose = (Button) mLayout.findViewById(R.id.btnclose);
		btnClose.setOnClickListener(this);
		iNoticeAdapter = new NoticeAdapter(MyArStation.getInstance(), iNoticeList);
		iNoticeView.setAdapter(iNoticeAdapter);
	}

	
	public void upNotice(List<String> notices) {
		if(notices == null) {
			return;
		}
		iNoticeList = notices;
		iNoticeAdapter.setItems(iNoticeList);
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
		p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的1.0
		p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
		// p.alpha = 1.0f; //设置本身透明度
		// p.dimAmount = 0.0f; //设置黑暗度

		getWindow().setAttributes(p); // 设置生效
		getWindow().setGravity(Gravity.CENTER); // 设置靠右对齐
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
		if (id == R.id.btnclose) {
			this.dismiss();
		} else {
		}
	}

}
