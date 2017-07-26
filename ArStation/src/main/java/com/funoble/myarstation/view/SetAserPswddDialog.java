package com.funoble.myarstation.view;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.InputFilter;
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
import android.widget.EditText;
import android.widget.TextView;

import com.funoble.myarstation.adapter.RestPswdAdapter.ItemListen;
import com.funoble.myarstation.common.MD5;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.RestPwdData;
import com.funoble.myarstation.game.GameCanvas;
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
public class SetAserPswddDialog extends Dialog implements
		android.view.View.OnClickListener, ItemListen{
	public static final int DEFUALT = 0;
	public static final int NOT_PSWD_BOX = 1;
	private ViewGroup mLayout;
	private TextView title;
	private Button comit;
	private Button cancel;
	private Button question;
	private EditText answer;
	private EditText pswd;
	private EditText pswdSecond;
	QuestionItemDialog dialog;
	RestPwdData data;
	private int TYPE = DEFUALT;
	private String pswdMd5;
	/**
	 * @param context
	 */
	public SetAserPswddDialog(Context context) {
		super(context, R.style.MSGDialogStyle);

		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_restpswd, null);
		setContentView(mLayout);
		title = (TextView) mLayout.findViewById(R.id.tvresetpwdti);
		question = (Button) mLayout.findViewById(R.id.btnSelectQ);
		question.setOnClickListener(this);
		comit = (Button) mLayout.findViewById(R.id.btncommit);
		comit.setOnClickListener(this);
		cancel = (Button) mLayout.findViewById(R.id.btncancel);
		cancel.setOnClickListener(this);
		answer =(EditText) mLayout.findViewById(R.id.etAnser);
		answer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
		pswd =(EditText) mLayout.findViewById(R.id.etpswd);
		pswd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
		pswdSecond =(EditText) mLayout.findViewById(R.id.etpswdsecond);
		pswdSecond.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
		dialog = new QuestionItemDialog(context);
		data = new RestPwdData(1, QuestionItemDialog.questions[0]);
		question.setText(data.value);
		measure();
		dialog.setFocusable(false);  
		dialog.setOutsideTouchable(true);
		dialog.setItemListen(this);
		this.setCancelable(false);
	}
	
	public void setTtile(String titleStr) {
		title.setText(titleStr);
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
		if (id == R.id.btnSelectQ) {
			//			if(data != null && data.) {
//				
//			}
			dialog.setWidth(question.getWidth());
			dialog.setHeight(LayoutParams.WRAP_CONTENT);
			dialog.showAsDropDown(question);
		} else if (id == R.id.btncommit) {
			if(checkIDAndPassword()) {
				if(NOT_PSWD_BOX == TYPE) {
					MyArStation.iGameProtocol.RequestSetPWDQuestion((short) data.key, answer.getText().toString());
				}
				else {
					MD5 md5 = new MD5();
					String psw = md5.getMD5ofStr(pswd.getText().toString());
					MyArStation.iGameProtocol.RequestResetPswd((short) data.key, answer.getText().toString(), psw);
					saveData();
				}
				GameCanvas.getInstance().getCurrentView().showProgressDialog(MyArStation.mGameMain.getString(R.string.Loading_String), false);
				answer.setText("");
				pswd.setText("");
				pswdSecond.setText("");
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
		if(TYPE == DEFUALT) {
			pswd.setVisibility(View.VISIBLE);
			pswdSecond.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.VISIBLE);
		}
		else {
			pswd.setVisibility(View.GONE);
			pswdSecond.setVisibility(View.GONE);
			cancel.setVisibility(View.INVISIBLE);
		}
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
        dialog.update(question, lp.width, LayoutParams.WRAP_CONTENT);
	}


	/* 
	 * @see com.funoble.lyingdice.adapter.VipRoomLimitListAdapter.ItemListen#onClick(java.lang.Object)
	 */
	@Override
	public void onClick(Object o) {
		data = null;
		data = (RestPwdData) o;
		if(data != null) {
			question.setText(data.value);
			question.setTag(data.key);
		}
	}


	/* 
	 * @see android.app.Dialog#dismiss()
	 */
	@Override
	public void dismiss() {
		dialog.dismiss();
		pswd.setText("");
		pswdSecond.setText("");
		answer.setText("");
		super.dismiss();
	}
	
	public void setTitle(String name) {
		String temp = name;
		if(temp == null) {
			temp = "";
		}
		title.setText(temp);
	}
	
	/**
	 * description 检查账号、密码
	 * @return true:合法，false:非法
	 */
	private boolean checkIDAndPassword() {
		String tStr = "";
		tStr = answer.getText().toString().trim();//id字符串
		if (tStr.equals("") || tStr.length() < 1) {
			Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), "密保答案不能为空！");
			return false;
		}
		if(DEFUALT == TYPE) {
			tStr = "";
			tStr = pswd.getText().toString().trim();//密码字符串
			if (tStr.length()<6) {
				Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), "密码过短！");
				return false;
			}
			String tConfirmStr = "";
			tConfirmStr = pswdSecond.getText().toString().trim();//确认密码字符串
			if (!tConfirmStr.equals(tStr)) {
				Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(), "两次输入的密码不一致！");
				return false;
			}
		}
		return true;
	} 
	
	/**
	 * @param type 0--默认有重设密码，1--只设密保
	 */
	public void setType(int type) {
		if(type < 0 || type > 1) {
			return;
		}
		TYPE = type;
	}
	
	//保存账号密码
	private void saveData() {
		String psw = pswd.getText().toString();
		MyArStation.mCurrAccountInfo.setPwdLength((byte) (psw.length()));
		byte[] md5One = MD5.toMD5(psw); // 2次md5加密
		MyArStation.mCurrAccountInfo.setInputPwd(MD5.toMD5(md5One));
		MyArStation.mCurrAccountInfo.setInputPwdOne(md5One);
		MyArStation.mCurrAccountInfo.setSucceedPwd(md5One);
		MyArStation.mCurrAccountInfo.setSavedPwd(true);
	}
	
	
}
