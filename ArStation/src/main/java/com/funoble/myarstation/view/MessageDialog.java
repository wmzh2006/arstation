package com.funoble.myarstation.view;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.funoble.myarstation.adapter.MessageAdapter;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.MessageData;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;

/**
 * <p>
 * 
 * </p>
 */
public class MessageDialog extends Dialog implements
		android.view.View.OnClickListener, OnScrollListener,
		MessageAdapter.OnMessageListener{

	private RelativeLayout rlSendBar;
	private ViewGroup mLayout;
	private EditText etMsg;
	private TextView title;
	private Button btnReturn;
	private Button btnSend;
	private ListView lsMsg;
	public List<MessageData> messageDatas;
	private MessageAdapter    adapter;
	public int iAllCount;
	public int iCount;
	public int iLeaveMsgID;
	public int iUserID;
	public int lastItem = 0;
	public int iStartIndex = 0;
	private MessageData answerData = null;
	private CheckBox isPrivateMsg;
	private String answerTip = "回复@%:";
	/**
	 * @param context
	 */
	public MessageDialog(Context context) {
		super(context, R.style.MSGDialogStyle);

		mLayout = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.new_message, null);

		setContentView(mLayout);
		rlSendBar = (RelativeLayout)mLayout.findViewById(R.id.rlmsgsend);
		lsMsg = (ListView)mLayout.findViewById(R.id.lsmsg);
		lsMsg.setOnScrollListener(this);
		lsMsg.requestFocus();
		messageDatas = new ArrayList<MessageData>();
		adapter = new MessageAdapter(context, messageDatas);
		lsMsg.setAdapter(adapter);
		adapter.setOnMessageListener(this);
		title = (TextView)mLayout.findViewById(R.id.msgtitle);
		iLeaveMsgID = MyArStation.iPlayer.iUserID;
		etMsg = (EditText) mLayout.findViewById(R.id.etmsg);
		btnReturn = (Button) mLayout.findViewById(R.id.btnmsgreturn);
		btnReturn.setOnClickListener(this);
		btnSend = (Button) mLayout.findViewById(R.id.btnmsgsend);
		btnSend.setOnClickListener(this);
		
		isPrivateMsg = (CheckBox) ((LinearLayout)mLayout.findViewById(R.id.llCheck)).findViewById(R.id.btnmsgpprivate);
		this.getWindow().setWindowAnimations(R.style.putupinanimation);
	}

	/**
     * Set the enabled state of this view.
     *
     * @param visibility One of {@link #VISIBLE}, {@link #INVISIBLE}, or {@link #GONE}.
     * @attr ref android.R.styleable#View_visibility
     */
	public void setSendBarVisibility(int visibility) {
		rlSendBar.setVisibility(visibility);
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
//		// p.alpha = 1.0f; //设置本身透明度
//		// p.dimAmount = 0.0f; //设置黑暗度
//	
		// 磨砂表现
//		getWindow().addFlags(LayoutParams.FLAG_BLUR_BEHIND);// android
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
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
		int id = v.getId();
		if (id == R.id.btnmsgreturn) {
			this.dismiss();
		} else if (id == R.id.btnmsgsend) {
			String content = etMsg.getText().toString().trim();
			content = replaceBlank(content);
			String name = "";
			int userid = iUserID;
			short reply = 0;
			if(answerData != null) {
				name = answerData.iName;
				Matcher mAtMatcher = MessageAdapter.AT_PATTERN.matcher(content);
				while(mAtMatcher.find()){
					String atNameMatch = mAtMatcher.group();
					String subAtNameMatch = atNameMatch.substring(0, atNameMatch.length());
					Tools.debug("match " + subAtNameMatch);
					if(subAtNameMatch.equals("@"+answerData.iName)) {
						userid = answerData.uid;
						reply = 1;
						break;
					}
				}
			}
			if(content.length() > 0 && !content.equals(getAnswerTip(name))) {
				short secret = (short) (isPrivateMsg.isChecked() ? 1 : 0);
				etMsg.setText("");
				MyArStation.iGameProtocol.requestLeaveMsgB(userid, content, secret, reply);
				Tools.debug("check " + isPrivateMsg.isChecked());
				answerData = null;
			}
			else {
				Tools.showSimpleToast(MyArStation.mGameMain.getApplicationContext(),
						Gravity.CENTER, "内容不能为空", Toast.LENGTH_SHORT);
				etMsg.setText("");
			}
		} else {
		}
	}
	
	public void setMessageDatas(List<MessageData> datas) {
		if(datas == null) {
			return;
		}
		messageDatas.addAll(datas);
		lsMsg.setAdapter(adapter);
		lsMsg.setSelection(lastItem - 1);
		adapter.notifyDataSetChanged();
		title.setText("留言板" );
	}
	
	public void reset() {
		messageDatas.clear();
		adapter.notifyDataSetChanged();
		iAllCount = 0;
		iCount = 0;
		iLeaveMsgID = 0;
		iUserID = 0;
		lastItem = 0;
		iStartIndex = 0;
		answerData = null;
	}
	
	public void setMessageData(MessageData data) {
		if(data == null) {
			return;
		}
		messageDatas.add(0,data);
		lsMsg.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		iCount++;
		iAllCount++;
		title.setText("留言板" );
	}

	public void upPic() {
		adapter.notifyDataSetChanged();
	}
	/* 
	 * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE: // Idle
			if (lastItem == adapter.getCount()) {  
				int pageIndex = adapter.getCount();
				Tools.debug("goods pageIndex" + pageIndex + "iStartInde " + iStartIndex);
				if(iStartIndex < iAllCount){
					MyArStation.iGameProtocol.requestLeaveListMsgB(iUserID, iStartIndex);
				}
			}
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: // Touch scroll
//			mIsListViewBusy = true;
//			if (adapter != null) {
//				adapter.setSelectedRow(null);
//			}
			break;
		case OnScrollListener.SCROLL_STATE_FLING: // Fling
//			mIsListViewBusy = true;
//			if (adapter != null) {
//				adapter.setSelectedRow(null);
//			}
			break;
		}
	}

	/* 
	 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.AbsListView, int, int, int)
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount;
	}

	/* 
	 * @see com.funoble.lyingdice.adapter.MessageAdapter.OnMessageListener#onAnswer(java.lang.Object)
	 */
	@Override
	public void onAnswer(Object o) {
		answerData = (MessageData)o;
		if(answerData != null) {
			String text = getAnswerTip(answerData.iName);
			etMsg.setText(text);
			etMsg.setSelection(text.length());
		}
	}

	public String getAnswerTip(String s) {
		return (s != null ? answerTip.replace("%", s) : "");
	}
	/* 
	 * @see com.funoble.lyingdice.adapter.MessageAdapter.OnMessageListener#onDelete(java.lang.Object)
	 */
	@Override
	public void onDelete(Object o) {
		for(int i = 0; i < messageDatas.size(); i++) {
			MessageData date = messageDatas.get(i);
			if(date.equals(o)) {
				MyArStation.iGameProtocol.requestDelLeaveMsg(iUserID, date.iLeaveMsgID);
				messageDatas.remove(i);
				refreshList();
				break;
			}
		}
	}

	/* 
	 * @see android.app.Dialog#dismiss()
	 */
	@Override
	public void dismiss() {
		answerData = null;
		etMsg.setText("");
		super.dismiss();
	}

	/* 
	 * @see com.funoble.lyingdice.adapter.MessageAdapter.OnMessageListener#onAccept(java.lang.Object)
	 */
	@Override
	public void onAccept(Object o) {
		MessageData date = (MessageData)o ;
		if(date != null) {
			MyArStation.iGameProtocol.requestAgreeFriend(date.uid, 0);
			for(int i = 0; i < messageDatas.size(); i++) {
				MessageData temp = messageDatas.get(i);
				if(temp.equals(o)) {
					messageDatas.remove(i);
					refreshList();
					break;
				}
			}
		}
	}

	/* 
	 * @see com.funoble.lyingdice.adapter.MessageAdapter.OnMessageListener#onRefuse(java.lang.Object)
	 */
	@Override
	public void onRefuse(Object o) {
		MessageData date = (MessageData)o ;
		if(date != null) {
			MyArStation.iGameProtocol.requestAgreeFriend(date.uid, 1);
			for(int i = 0; i < messageDatas.size(); i++) {
				MessageData temp = messageDatas.get(i);
				if(temp.equals(o)) {
					messageDatas.remove(i);
					refreshList();
					break;
				}
			}
		}
	}
	
	
	private void refreshList() {
		iAllCount--;
		iCount--;
		adapter.notifyDataSetChanged();
		if(iAllCount > 0 && iCount <= 0) {
			Tools.debug("refreshList");
			MyArStation.iGameProtocol.requestLeaveListMsgB(iUserID, iStartIndex);
		}
	}
	
	public String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
}
