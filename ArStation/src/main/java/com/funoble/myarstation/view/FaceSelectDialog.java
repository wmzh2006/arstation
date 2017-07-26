package com.funoble.myarstation.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.funoble.myarstation.adapter.FaceAdapter;
import com.funoble.myarstation.common.Tools;
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
/**
 * description 
 * version 1.0
 * author 
 * update 2012-11-16 下午05:02:35 
 */
public class FaceSelectDialog extends Dialog implements
		android.view.View.OnClickListener, OnItemClickListener {
	private final int FACE_MAX = 15;
	private ViewGroup mLayout;
	
	private OnSelectFaceListener dialogListener;
	
	private GridView gvFaces;
	private List<Integer> faceResIDs;
	private FaceAdapter   faceAdapter;
	
	/**
	 * @param context
	 */
	public FaceSelectDialog(Context context) {
		super(context, R.style.MSGDialogStyle);

		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_face, null);

		setContentView(mLayout);
		gvFaces = (GridView) mLayout.findViewById(R.id.gvFave);
		gvFaces.setOnItemClickListener(this);
		faceResIDs = new ArrayList<Integer>();
		
		for(int i = 0; i < FACE_MAX; i++) {
			faceResIDs.add(Integer.valueOf(R.drawable.face00 + i));
		}
		faceAdapter = new  FaceAdapter(MyArStation.mGameMain.getApplicationContext(),
				faceResIDs);
		gvFaces.setAdapter(faceAdapter);
		this.setCanceledOnTouchOutside(true);
//		this.getWindow().setWindowAnimations(R.style.animation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Dialog#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
//		p.height = (int) (d.getHeight() * 0.9); // 高度设置为屏幕的1.0
//		p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.8
		p.width = Tools.px2dip(MyArStation.mGameMain.getApplicationContext(), 560 * ActivityUtil.ZOOM_X);
		 p.alpha = 5.0f; //设置本身透明度
		 p.dimAmount = 0.0f; //设置黑暗度
//
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
		int type = 0;
		switch (v.getId()) {
//		case R.id.face0:
//			type = FACE_TYPE_DESPISE;
//			break;
//		case R.id.face1:
//			type = FACE_TYPE_SMILE;
//			break;
//		case R.id.face2:
//			type = FACE_TYPE_ANGER;
//			break;
//		case R.id.face3:
//			type = FACE_TYPE_CRY;
//			break;
//		case R.id.face4:
//			type = FACE_TYPE_PRAISE;
//			break;

		default:
			break;
		}
		if(dialogListener != null) dialogListener.onSelect(type);
		this.dismiss();
	}
	
	public void setOnSelectFaceListener(OnSelectFaceListener l) {
		dialogListener = l;
	}
	
	public interface  OnSelectFaceListener {
		public void onSelect(int type);
	}

	/* 
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(dialogListener != null) dialogListener.onSelect(position);
		this.dismiss();
	}
}
