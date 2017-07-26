package com.funoble.myarstation.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.R.integer;
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

import com.funoble.myarstation.adapter.ActivityRankAdapter;
import com.funoble.myarstation.data.cach.ActivityRankData;
import com.funoble.myarstation.data.cach.FriendListInfo;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;

/**
 * <p>
 * 
 * </p>
 */
public class ActivityRankDialog extends Dialog implements
		android.view.View.OnClickListener {

	private ViewGroup mLayout;
	private Button btnGift;
	private ListView lsMsg;
	private List<ActivityRankData> gifts;
	private ActivityRankAdapter adapter;
	private String activityDetail;
	private int ActivityId;
	/**
	 * @param context
	 */
	public ActivityRankDialog(Context context) {
		super(context, R.style.MSGDialogStyle);

		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_activity_rank, null);

		setContentView(mLayout);
		lsMsg = (ListView)mLayout.findViewById(R.id.activity_rank);
		gifts = new ArrayList<ActivityRankData>();
		adapter = new ActivityRankAdapter(context, gifts);
		lsMsg.setAdapter(adapter);
		btnGift = (Button) mLayout.findViewById(R.id.btn_activity_rank);
		btnGift.setOnClickListener(this);
		this.getWindow().setWindowAnimations(R.style.putupinanimation);
		this.setCancelable(true);
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
		if (id == R.id.btn_activity_rank) {
			Exporte();
			this.dismiss();
		} else {
		}
	}
	
	public void setDatas(List<ActivityRankData> datas, String Detail, int aActivityId) {
		if(datas == null) {
			return;
		}
		gifts.clear();
		gifts.addAll(datas);
		adapter.notifyDataSetChanged();
		lsMsg.setAdapter(adapter);
		activityDetail = Detail;
		ActivityId = aActivityId;
		this.show();
	}
	
	private void Exporte() {
		String fileName = "ativityId_"+ActivityId + "_.txt";
		String path = "/lyingdice/activityrank/";
		 if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
             File sdCard = android.os.Environment.getExternalStorageDirectory();
             File dir = new File(sdCard.getPath()+path);
             FileOutputStream fos = null;
             File file = null;
             if(!dir.exists()) {  //创建目录
            	 dir.mkdirs();
             }
             else {
            	 try {
            		file = new File(sdCard.getPath()+path, fileName);
					fos = new FileOutputStream(file);
					int count = gifts.size();
					fos.write(activityDetail.getBytes());
					for(int i = 0; i < count; i++) {
						fos.write(gifts.get(i).toString().getBytes());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally {
					try {
						fos.flush();
						fos.close();
						dir = null;
						file = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
             }
		 }
	}
}
