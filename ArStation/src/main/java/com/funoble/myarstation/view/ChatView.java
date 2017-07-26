/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: ChatView.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-11-12 下午04:21:44
 *******************************************************************************/
package com.funoble.myarstation.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.funoble.myarstation.adapter.ChatAdapter;
import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.data.cach.ChatContent;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.phrase.CopyFileFromAssets;
import com.funoble.myarstation.phrase.Phrase;
import com.funoble.myarstation.phrase.SaxPhrase;
import com.funoble.myarstation.phrase.SerializePhraseHandler;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.view.SettingPhraseDialog.OnSettingPhraseListener;


/**
 * description 
 * version 1.0
 * author 
 * update 2012-11-13 上午01:48:58 
 */
public class ChatView extends PopupWindow implements OnClickListener, OnItemClickListener,
													OnItemLongClickListener, OnSettingPhraseListener{
	private String PATH = Environment.getExternalStorageDirectory() + "/lyingdice/phrase";
	private InputStream inputStream = null;
	
	private final int MAXSIZE_HISTORY = 50;
	private ListView lvUsedWords;
	private ChatAdapter adtUsedWords;
	private ArrayList<ChatContent> lsUsedWords;
	
	private ListView lvHistoryWords;
	private ChatAdapter adtHistoryWords;
	private ArrayList<ChatContent> lsHistoryWords;
	
	private Button	btnUsedWords;
	private Button	btnHistoryWords;
	private EditText etContent;
	private Button	btnSend;
	private RelativeLayout rlChatBar;
	private Context context;
	private ChatNotifyNewMessage chatNotifyNewMessage;
	private SettingPhraseDialog settingPhraseDialog = null;
	private boolean hasBar = true;
	/**
	 * construct
	 * @param context
	 */
	public ChatView(Context context) {
		super(context);
		this.context = context;
		RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.new_chat, null);
		rlChatBar = (RelativeLayout)relativeLayout.findViewById(R.id.chatbar);
		lvUsedWords = (ListView)relativeLayout.findViewById(R.id.words);
		lvHistoryWords = (ListView)relativeLayout.findViewById(R.id.historyworks);
		switchTab(false);
		lsUsedWords = new ArrayList<ChatContent>();
//		String[] usedwords = context.getResources().getStringArray(R.array.usedwords);
		List<Phrase> phrases;
		try {
			phrases = getPhrase();
			for(int i = 0; i < phrases.size(); i++) {
				ChatContent chatContent = new ChatContent("", phrases.get(i).getWord(),  i+1+".");
				chatContent.ibUserWords = true;
				chatContent.edit = phrases.get(i).getEdit();
				lsUsedWords.add(chatContent);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
//		usedwords = null;
		adtUsedWords = new ChatAdapter(context, lsUsedWords);
		lvUsedWords.setAdapter(adtUsedWords);
		lsHistoryWords = new ArrayList<ChatContent>();
		adtHistoryWords = new ChatAdapter(context, lsHistoryWords);
		lvHistoryWords.setAdapter(adtHistoryWords);
		lvHistoryWords.setOnItemClickListener(this);
		lvUsedWords.setOnItemClickListener(this);
		lvUsedWords.setOnItemLongClickListener(this);
		
		btnUsedWords = (Button) relativeLayout.findViewById(R.id.btnwords);
		btnUsedWords.setOnClickListener(this);
		btnHistoryWords = (Button) relativeLayout.findViewById(R.id.btnhostorywords);
		btnHistoryWords.setOnClickListener(this);
		etContent = (EditText) relativeLayout.findViewById(R.id.etcontent);
		etContent.setFocusable(true);
		btnSend = (Button) relativeLayout.findViewById(R.id.btnchatsend);
		btnSend.setOnClickListener(this);
		this.setContentView(relativeLayout);
//		this.setAnimationStyle(R.style.animation);
		this.setBackgroundDrawable(new ColorDrawable(0x00000000));
		this.setWidth((int) (ActivityUtil.SCREEN_WIDTH * 0.8f));
		this.setHeight((int) (ActivityUtil.SCREEN_HEIGHT * 0.9f));
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		this.setInputMethodMode(INPUT_METHOD_NEEDED);    
		this.setSoftInputMode(INPUT_METHOD_FROM_FOCUSABLE);
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);  
		switchTab(true);
		lvHistoryWords.setSelected(true);
		settingPhraseDialog = new SettingPhraseDialog(MyArStation.mGameMain);
		settingPhraseDialog.setOnSettingPhraseListener(this);
	}

	/* 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
		int id = v.getId();
		if (id == R.id.btnwords) {
			if(lvUsedWords.isSelected()) {
				return;
			}
			switchTab(false);
			lvUsedWords.setSelected(true);
			lvHistoryWords.setSelected(false);
		} else if (id == R.id.btnhostorywords) {
			if(lvHistoryWords.isSelected()) {
				return;
			}
			switchTab(true);
			lvUsedWords.setSelected(false);
			lvHistoryWords.setSelected(true);
		} else if (id == R.id.btnchatsend) {
			String content = etContent.getText().toString().trim();
			content.replaceAll("\n", "");
			content.replaceAll("\r", "");
			int len = content.length();
			if(content != null && len > 0) {
				requestChat(content);
				 etContent.setText("");
				 dismiss();
			}
			else {
				etContent.setText("");
				Tools.showSimpleToast(context, Gravity.CENTER, context.getString(R.string.chat_tip), Toast.LENGTH_SHORT);
			}
		} else {
		}
	}

	private void switchTab(boolean aBHistoryWords) {
		lvUsedWords.setVisibility(!aBHistoryWords ? View.VISIBLE : View.GONE);
		lvHistoryWords.setVisibility(aBHistoryWords ? View.VISIBLE : View.GONE);
	}
	
	private void requestChat(String aChatContent) {
		MyArStation.iGameProtocol.requestChat(aChatContent);
	}

	/* 
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		 //获得选中项的HashMap对象 
		ChatContent chatContent = (ChatContent)parent.getItemAtPosition(position); 
		if(chatContent == null) {
			return;
		}
		if(chatContent.edit) {
			etContent.setText(chatContent.content);
		}
		else {
			etContent.setText("");
		}
	}
	
	public void showAtLocationNotBar(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		switchTab(false);
		lvUsedWords.setSelected(true);
		lvHistoryWords.setSelected(false);
		rlChatBar.setVisibility(View.GONE);
		hasBar = false;
		if(chatNotifyNewMessage != null) chatNotifyNewMessage.newMessage(false);
	}
	
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		rlChatBar.setVisibility(View.VISIBLE);
		if(chatNotifyNewMessage != null) chatNotifyNewMessage.newMessage(false);
	}
	
	public void showAsDropDown(View anchor) {
		super.showAsDropDown(anchor);
		rlChatBar.setVisibility(View.VISIBLE);
		if(chatNotifyNewMessage != null) chatNotifyNewMessage.newMessage(false);
	}
	
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		super.showAsDropDown(anchor, xoff, yoff);
		rlChatBar.setVisibility(View.VISIBLE);
		if(chatNotifyNewMessage != null) chatNotifyNewMessage.newMessage(false);
	}
	
	public void updateHostoryList(ChatContent content) {
		if(content == null) {
			return;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.CHINESE);      
		Date curDate  =   new Date(System.currentTimeMillis());//获取当前时间      
		String str = formatter.format(curDate);  
		content.time = str;
		lsHistoryWords.add(0,content);
		limitSize();
		adtHistoryWords.notifyDataSetChanged();
		lvHistoryWords.setAdapter(adtHistoryWords);
		if(chatNotifyNewMessage != null && !this.isShowing()) chatNotifyNewMessage.newMessage(true);
	}
	
	public void updateSystemMessage(ChatContent content) {
		if(content == null) {
			return;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.CHINESE);      
		Date curDate  =   new Date(System.currentTimeMillis());//获取当前时间      
		String str = formatter.format(curDate);  
		content.ibSystemMessage = true;
		content.time = str;
		lsHistoryWords.add(0,content);
		limitSize();
		adtHistoryWords.notifyDataSetChanged();
		lvHistoryWords.setAdapter(adtHistoryWords);
		if(chatNotifyNewMessage != null && !this.isShowing()) chatNotifyNewMessage.newMessage(true);
	}
	
	private void limitSize() {
		if(lsHistoryWords.size() > MAXSIZE_HISTORY) {
			for(int i = MAXSIZE_HISTORY; i < lsHistoryWords.size(); i++){
				lsHistoryWords.remove(i);
			}
		}
	}
	
	public void clearHistoryWords() {
		lsHistoryWords.clear();
		adtHistoryWords.notifyDataSetChanged();
		lvHistoryWords.setAdapter(adtHistoryWords);
	}
	
	public void setNotifyNewMessage(ChatNotifyNewMessage notifyNewMessage) {
		chatNotifyNewMessage = notifyNewMessage;
	}
	
	public interface ChatNotifyNewMessage{
		public void newMessage(boolean aNew);
	}

	/* 
	 * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		 //获得选中项的HashMap对象 
		ChatContent chatContent = (ChatContent)parent.getItemAtPosition(position); 
		settingPhraseDialog.show(position, chatContent);
		dismiss();
//		lsUsedWords.set(position, chatContent);
//		adtUsedWords.notifyDataSetChanged();
//		Tools.showSimpleToast(context, chatContent.content);
		return false;
	}
	
	public void dismissAll() {
		settingPhraseDialog.dismiss();
		super.dismiss();
	}
	
	private List<Phrase> getPhrase () throws Throwable {
		List<Phrase> phrases = null;
		SaxPhrase saxPhrase = new SaxPhrase();
		CopyFileFromAssets.copy(context, "phrase.xml", PATH, "phrase.xml", false);
		File file = new File(PATH+"/"+"phrase.xml");
		if(file.exists()) {
			inputStream = new FileInputStream(file);
			if(inputStream == null) {
				inputStream = context.getResources().getAssets().open("phrase.xml");
			}
		}
		try {
			phrases = saxPhrase.getPersons(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
			CopyFileFromAssets.copy(context, "phrase.xml", PATH, "phrase.xml", true);
		}
		return phrases;
	}

	/* 
	 * @see com.funoble.lyingdice.view.SettingPhraseDialog.OnSettingPhraseListener#OnSettingPhrase(java.lang.String)
	 */
	@Override
	public void OnSettingPhrase(String phrase, int position) {
		ChatContent chatContent = new ChatContent("", phrase);
		chatContent.ibUserWords = true;
		chatContent.edit = true;
		chatContent.index = position + 1 + ".";
		lsUsedWords.set(position, chatContent);
		adtUsedWords.notifyDataSetChanged();
		try {
			Save();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		chatContent = null;
		if(hasBar) {
			showAtLocation(MyArStation.getInstance().getGameCanvas(), Gravity.CENTER, 0, 0);
		}
		else {
			showAtLocationNotBar(MyArStation.getInstance().getGameCanvas(), Gravity.CENTER, 0, 0);
		}
	}
	
	private void Save() throws Throwable{
		File file = new File(PATH+"/"+"phrase.xml");
		FileOutputStream outStream = new FileOutputStream(file);
		List<Phrase> phrases = new ArrayList<Phrase>();
		for(int i = 0; i < lsUsedWords.size(); i++) {
			Phrase phrase = new Phrase(lsUsedWords.get(i).content, lsUsedWords.get(i).edit);
			phrases.add(phrase);
		}
		SerializePhraseHandler.save(phrases, outStream);
	}

	/* 
	 * @see com.funoble.lyingdice.view.SettingPhraseDialog.OnSettingPhraseListener#OnSettingPhraseCancel()
	 */
	@Override
	public void OnSettingPhraseCancel() {
		if(hasBar) {
			showAtLocation(MyArStation.getInstance().getGameCanvas(), Gravity.CENTER, 0, 0);
		}
		else {
			showAtLocationNotBar(MyArStation.getInstance().getGameCanvas(), Gravity.CENTER, 0, 0);
		}
	}
}
