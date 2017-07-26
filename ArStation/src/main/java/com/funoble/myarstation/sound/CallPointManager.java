/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: SoundManager.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-14 下午09:00:17
 *******************************************************************************/
package com.funoble.myarstation.sound;

import java.util.HashMap;


public class CallPointManager {
//	public final int MAX_MODEL_COUNT = 5;
	
	MediaManager[] iMediaManager = null;
	CallPointdHashMap	iSound;
	static CallPointManager iSoundManager = null;
	HashMap<Integer, CallPointdHashMap> iSoundMap;
	
	public static CallPointManager getSoundManager() {
		if(iSoundManager == null) {
			iSoundManager = new CallPointManager();
		}
		return iSoundManager;
	}
	
	public CallPointManager() {
		iMediaManager = new MediaManager[MediaManager.MAX_SOUND_ENGINE_COUNT];
		for(int i=0; i<MediaManager.MAX_SOUND_ENGINE_COUNT; i++) {
			iMediaManager[i] = MediaManager.getMediaManagerInstance(i);
		}
		iSoundMap = new HashMap<Integer,  CallPointdHashMap>();
		HashMap<Integer, Integer> count = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> pointCount = new HashMap<Integer, Integer>();
		count.put(1, SoundsResID.one);
		count.put(2, SoundsResID.two);
		count.put(3, SoundsResID.three);
		count.put(4, SoundsResID.four);
		count.put(5, SoundsResID.five);
		count.put(6, SoundsResID.six);
		count.put(7, SoundsResID.seven);
		count.put(8, SoundsResID.eight);
		count.put(9, SoundsResID.nine);
		count.put(10, SoundsResID.ten);
		pointCount.put(1, SoundsResID.one_point);
		pointCount.put(2, SoundsResID.two_point);
		pointCount.put(3, SoundsResID.three_point);
		pointCount.put(4, SoundsResID.four_point);
		pointCount.put(5, SoundsResID.five_point);
		pointCount.put(6, SoundsResID.six_point);	
		CallPointdHashMap sound = new CallPointdHashMap(count, pointCount);
		iSoundMap.put(0, sound);
	}
	
	private CallPoint getSound(int aCount, int aPointCount) {
		CallPointdHashMap sound = iSoundMap.get(0);
		return getCount(sound, aCount, aPointCount);
	}
	
	private CallPoint getCount(CallPointdHashMap aSound, int aCount, int aPointCount) {
		return aSound.getCallContent(aCount, aPointCount);
	}
	
	//喊骰
	public void CallPoint(int aModelID, int aCount, int aPointCount) {
		/*
		 
		//精简版
		int tIndex = 0;
		if(aModelID == 0 || aModelID == 2) {
			tIndex = 0;
		}
		else if(aModelID == 1 || aModelID == 3 || aModelID == 4) {
			tIndex = 1;
		}
		else {
			return;
		}
		CallPoint sound = getSound(aCount, aPointCount);
		if(sound != null) {
			int tDelay = 580;
			if(tIndex == 0) {
				tDelay = 1000;
			}
			else if(tIndex == 1) {
				tDelay = 1100;
			}
			
			iMediaManager[tIndex].playerSound(sound.iSoundCountID, 0);
			iMediaManager[tIndex].playSoundDelayed(sound.iSountPointCountID, 0, tDelay);
		}
		*/
		
		//豪华版
		int tIndex = 0;
		if(aModelID >= 0 && aModelID < 3) {
			tIndex = aModelID;
		}
		else if(aModelID == 3 || aModelID == 4) {
			tIndex = 3;
		}
		else {
			return;
		}
		CallPoint sound = getSound(aCount, aPointCount);
		if(sound != null) {
			int tDelay = 580;
			if(tIndex == 2) {
				tDelay = 1000;
			}
			else if(tIndex > 2) {
				tDelay = 1100;
			}
			
			iMediaManager[tIndex].playerSound(sound.iSoundCountID, 0);
			iMediaManager[tIndex].playSoundDelayed(sound.iSountPointCountID, 0, tDelay);
		}
	}
	
	//劈
	public void CallPi(int aModelID) {
		/*
		//精简版 
		int tIndex = 0;
		if(aModelID == 0 || aModelID == 2) {
			tIndex = 0;
		}
		else if(aModelID == 1 || aModelID == 3 || aModelID == 4) {
			tIndex = 1;
		}
		else {
			return;
		}
		*/
		
		//豪华版
		int tIndex = 0;
		if(aModelID >= 0 && aModelID < 3) {
			tIndex = aModelID;
		}
		else if(aModelID == 3 || aModelID == 4) {
			tIndex = 3;
		}
		else {
			return;
		}
		iMediaManager[tIndex].playerSound(SoundsResID.pi, 0);
	}
	
	//抢劈
	public void CallQiangPi(int aModelID) {
		/*
		//精简版
		int tIndex = 0;
		if(aModelID == 0 || aModelID == 2) {
			tIndex = 0;
		}
		else if(aModelID == 1 || aModelID == 3 || aModelID == 4) {
			tIndex = 1;
		}
		else {
			return;
		}
		*/
		//豪华版
		int tIndex = 0;
		if(aModelID >= 0 && aModelID < 3) {
			tIndex = aModelID;
		}
		else if(aModelID == 3 || aModelID == 4) {
			tIndex = 3;
		}
		else {
			return;
		}
		iMediaManager[tIndex].playerSound(SoundsResID.qiangpi, 0);
	}
	
	//搞大它
	public void CallGaoDaTa(int aModelID) {
		/*
		//精简版
		int tIndex = 0;
		if(aModelID == 0 || aModelID == 2) {
			tIndex = 0;
		}
		else if(aModelID == 1 || aModelID == 3 || aModelID == 4) {
			tIndex = 1;
		}
		else {
			return;
		}
		*/
		//豪华版
		int tIndex = 0;
		if(aModelID >= 0 && aModelID < 3) {
			tIndex = aModelID;
		}
		else if(aModelID == 3 || aModelID == 4) {
			tIndex = 3;
		}
		else {
			return;
		}
		int tID = (int)(Math.random() * 3);
		if(tID > 2) {
			tID = 2;
		}
		iMediaManager[tIndex].playerSound(SoundsResID.z0 + tID, 0);
	}
	
	//抢劈
	public void CallFanPi(int aModelID) {
		/*
		//精简版
		int tIndex = 0;
		if(aModelID == 0 || aModelID == 2) {
			tIndex = 0;
		}
		else if(aModelID == 1 || aModelID == 3 || aModelID == 4) {
			tIndex = 1;
		}
		else {
			return;
		}
		*/
		//豪华版
		int tIndex = 0;
		if(aModelID >= 0 && aModelID < 3) {
			tIndex = aModelID;
		}
		else if(aModelID == 3 || aModelID == 4) {
			tIndex = 3;
		}
		else {
			return;
		}
		iMediaManager[tIndex].playerSound(SoundsResID.fanpi, 0);
	}
}
