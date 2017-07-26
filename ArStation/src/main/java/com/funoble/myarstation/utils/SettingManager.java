/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: SettingManager.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-6-4 下午03:09:31
 *******************************************************************************/
package com.funoble.myarstation.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.funoble.myarstation.game.MyArStation;


public class SettingManager {
	public static final String SCREEN_BRIGHTNESS_KEY = "SETTING_SCREEN_BRIGHTNESS";
	public static final String SHAKA_KEY = "SETTING_SHAKA";
	public static final String SOUND_KEY = "SETTING_SOUND";
	public static final String MUSIC_KEY = "SETTING_MUSIC";
	public static final String WIFIDOWNLOAD_KEY = "SETTING_WIFIDOWNLOA";
	
	private boolean		  isShaka = false;
	private boolean		  isSound = false;
	private boolean		  isMusic = false;
	private boolean		  isWifiDownload = false;
	private int		  	  iBrightness;
	private static SettingManager	iSettingManager = null;
	
	private SharedPreferences shPreferences;
	private SharedPreferences.Editor editor;
	
	public static SettingManager getInstance() {
		if(iSettingManager == null) {
			iSettingManager = new SettingManager();
		}
		return iSettingManager;
	}
	/**
	 * construct
	 */
	private SettingManager() {
		shPreferences = MyArStation.getInstance().getSharedPreferences("setting", Activity.MODE_PRIVATE);
		editor = shPreferences.edit();
	}

	/**
	 * return isShaka : return the property isShaka.
	 */
	public boolean isShaka() {
		return isShaka = shPreferences.getBoolean(SHAKA_KEY, true);
	}

	/**
	 * return isSound : return the property isSound.
	 */
	public boolean isSound() {
		return 	isSound = shPreferences.getBoolean(SOUND_KEY, true);
	}
	
	/**
	 * return isWifiDownload : return the property isWifiDownload.
	 */
	public boolean isWifiDownload() {
		return 	isWifiDownload = shPreferences.getBoolean(WIFIDOWNLOAD_KEY, false);
	}

	/**
	 * return isMusic : return the property isMusic.
	 */
	public boolean isMusic() {
		return isMusic = shPreferences.getBoolean(MUSIC_KEY, true);
	}

	/**
	 * return iBrightness : return the property iBrightness.
	 */
	public int getiBrightness() {
		return iBrightness = shPreferences.getInt(SCREEN_BRIGHTNESS_KEY, 30);
	}

	public void saveBrightness(int aBrightness) {
		editor.putInt(SCREEN_BRIGHTNESS_KEY, aBrightness);
		editor.commit();
	}
	
	public void saveShaka(boolean aShakc) {
		editor.putBoolean(SHAKA_KEY, aShakc);
		editor.commit();
	}
	
	public void saveSound(boolean aSound) {
		editor.putBoolean(SOUND_KEY, aSound);
		editor.commit();
	}
	
	public void saveMusic(boolean aMusic) {
		editor.putBoolean(MUSIC_KEY, aMusic);
		editor.commit();
	}
	
	public void saveWifiDownload(boolean aWifi) {
		editor.putBoolean(WIFIDOWNLOAD_KEY, aWifi);
		editor.commit();
	}
	
}
