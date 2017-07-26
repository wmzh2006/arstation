package com.funoble.myarstation.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.funoble.myarstation.adapter.RestPswdAdapter.ItemListen;
import com.funoble.myarstation.common.MD5;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.RestPwdData;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.socket.protocol.MBRspResetThirdUserInfo;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;

/**
 * <p>
 * 
 * </p>
 */
public class ReRegistDialog extends Dialog implements
		android.view.View.OnClickListener, ItemListen{

	private ViewGroup mLayout;

	private Button question;
	private EditText answer;
	private Button commit;
	private Button cancel;
	private EditText iETID;//账号输入框
	private EditText iETPSW;//密码输入框
	private EditText iETPSWConfirm;//确认密码输入框
	private QuestionItemDialog questionItemDialog;
	private RestPwdData data;
	private Reregist lReregist;
	/**
	 * @param context
	 */
	public ReRegistDialog(Context context, Reregist l) {
		super(context, R.style.MSGDialogStyle);

		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_reregist, null);

		setContentView(mLayout);
		iETID = (EditText) mLayout.findViewById(R.id.etaccount);
		iETPSW= (EditText) mLayout.findViewById(R.id.etpswd);
		iETPSWConfirm = (EditText) mLayout.findViewById(R.id.etpswdsecond);
		answer = (EditText) mLayout.findViewById(R.id.etAnser);
		question = (Button) mLayout.findViewById(R.id.btnSelectQ);
		question.setOnClickListener(this);
		commit = (Button) mLayout.findViewById(R.id.btncommit);
		commit.setOnClickListener(this);
		cancel = (Button) mLayout.findViewById(R.id.btncancel);
		cancel.setOnClickListener(this);
		questionItemDialog = new QuestionItemDialog(MyArStation.mGameMain.getApplicationContext());
		data = new RestPwdData(1, QuestionItemDialog.questions[0]);
		question.setText(data.value);
		questionItemDialog.update(question, question.getWidth(), LayoutParams.WRAP_CONTENT);
		questionItemDialog.setFocusable(false);  
		questionItemDialog.setOutsideTouchable(true);
		questionItemDialog.setItemListen(this);
		lReregist = l;
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
		p.height = LayoutParams.WRAP_CONTENT; // 高度设置为屏幕的1.0
		p.width = LayoutParams.WRAP_CONTENT; // 宽度设置为屏幕的0.8
//		// p.alpha = 1.0f; //设置本身透明度
//		// p.dimAmount = 0.0f; //设置黑暗度
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
		int id = v.getId();
		if (id == R.id.btnSelectQ) {
			questionItemDialog.setWidth(question.getWidth());
			questionItemDialog.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
			questionItemDialog.showAsDropDown(question);
		} else if (id == R.id.btncommit) {
			if(checkIDAndPassword()) {
				RequestResetThirdUserInfo();
				this.dismiss();
			}
		} else if (id == R.id.btncancel) {
			cancel();
		} else {
		}
	}

	private boolean RequestResetThirdUserInfo() {
		MD5 md5 = new MD5();
		String psw = md5.getMD5ofStr(iETPSW.getText().toString());
		String ui = iETID.getText().toString();
		return MyArStation.iGameProtocol.RequestResetThirdUserInfo(ui, psw, (short)data.key, answer.getText().toString().trim());
	}
	
	/**
	 * description 检查账号、密码
	 * @return true:合法，false:非法
	 */
	private boolean checkIDAndPassword() {
		boolean tbError = false;//是否出错了
		String tInfo = "";//提示语
		String tStr = "";
		if (iETID != null) {
			tStr = iETID.getText().toString();//id字符串
			if (tStr.length()<6) {
				tInfo = "账号过短！";
				tbError = true;
			}
		}
		if (!tbError) {
			tStr = "";
			tStr = iETPSW.getText().toString();//密码字符串
			if (tStr.length()<6) {
				tInfo = "密码过短！";
				tbError = true;
			}
			if (!tbError) {
				String tConfirmStr = "";
				tConfirmStr = iETPSWConfirm.getText().toString();//确认密码字符串
				if (!tConfirmStr.equals(tStr)) {
					tInfo = "两次输入的密码不一致！";
					tbError = true;
				}
			}
		}
		if(!tbError) {
			tStr = answer.getText().toString().trim();
			if(tStr.length() < 1) {
				tInfo = "请输入密保答案！";
				tbError = true;
			}
		}
		if (tbError) {
			Tools.showSimpleToast(MyArStation.getInstance().getApplicationContext(), Gravity.CENTER,
					tInfo, Toast.LENGTH_SHORT);		}
		return !tbError;
	}
	
	/* 
	 * @see com.funoble.lyingdice.adapter.RestPswdAdapter.ItemListen#onClick(java.lang.Object)
	 */
	@Override
	public void onClick(Object o) {
		data = (RestPwdData) o;
		if(data != null) {
			question.setText(data.value);
		}
	}

	public void handleMessage(Message msg) {
		switch (msg.what) {
		case MessageEventID.EMESSAGE_EVENT_RSP_RESET_THIRD_USERINFO:
			MyArStation.msgManager.setClean(false);
			MBRspResetThirdUserInfo ResetThirdUserInfo = (MBRspResetThirdUserInfo) msg.obj;
			Tools.showSimpleToast(ActivityUtil.mContext, ResetThirdUserInfo.iMsg);
			if(ResetThirdUserInfo.nResult == 0) {
				saveData();
				String pwdMD5 = MD5.MD5ByteToStr(MyArStation.mCurrAccountInfo.getSucceedPwd());
				if(lReregist != null)lReregist.OnReregistSucces(ResetThirdUserInfo.iUserID, pwdMD5);
			}
			else {
				this.show();
			}
			break;

		default:
			break;
		}
	}
	
	//保存账号密码
	private void saveData() {
		SharedPreferences iSP = MyArStation.getInstance().getPreferences(Activity.MODE_PRIVATE);
		SharedPreferences.Editor tSPE = iSP.edit();
		tSPE.clear();
		tSPE.commit();
		String ui = iETID.getText().toString();
		String psw = iETPSW.getText().toString();
		MyArStation.mCurrAccountInfo.setName(ui);
		MyArStation.mCurrAccountInfo.setPwdLength((byte) (psw.length()));
		byte[] md5One = MD5.toMD5(psw); // 2次md5加密
		MyArStation.mCurrAccountInfo.setInputPwd(MD5.toMD5(md5One));
		MyArStation.mCurrAccountInfo.setInputPwdOne(md5One);
		MyArStation.mCurrAccountInfo.setSucceedPwd(md5One);
		MyArStation.mCurrAccountInfo.setSavedPwd(true);
		MyArStation.mAccountStore.pushAndUpdate(MyArStation.mCurrAccountInfo);
		MyArStation.mAccountStore.commit();
	}
	
	public interface Reregist {
		void OnReregistSucces(String uid, String pwd);
		void OnCancel(); 
	}

	/* 
	 * @see android.app.Dialog#dismiss()
	 */
	@Override
	public void dismiss() {
		super.dismiss();
	}

	/* 
	 * @see android.app.Dialog#cancel()
	 */
	@Override
	public void cancel() {
		super.cancel();
		if(lReregist != null)lReregist.OnCancel();
	}
	
	
}
