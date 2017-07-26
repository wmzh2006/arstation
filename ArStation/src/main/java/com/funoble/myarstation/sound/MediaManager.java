/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: SoundEngine.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-5-9 上午10:02:18
 *******************************************************************************/
package com.funoble.myarstation.sound;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import android.R.integer;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.SoundPool;
import android.os.Handler;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;

public class MediaManager {
	
	//
	public final static int OTHER_SOUND_ID = 5;
	public final static int MAX_SOUND_ENGINE_COUNT = 5;
	/** 
     * 声音开关 背景音乐 
     */  
    private boolean blnOpenBgSound = false;   
      
    /** 
     * 声音开关 特效音乐 
     */  
    private boolean blnOpenEffectSound = false;   
    
	private SoundPool iSoundPool;
	private MediaPlayer iMediaPlayer;

	private boolean iPlaying = false;
	private boolean iLoop = false;

	private String iName;
	private Context iContext;
	private AudioManager iAudioManager;
	private HashMap<Integer, Integer> iSoundPoolMap;
	private HashMap<Integer, String> iMusicMap;

	private float iVolumeCurrent;
	private float iVolumeMax;
	private float iSoundVolume;

	private AssetManager assetManager;
	private static MediaManager[] iMediaManager = new MediaManager[MAX_SOUND_ENGINE_COUNT];

	private final int maxStreams = 10; //streamType音频池的最大音频流数目为10    
    private final int srcQuality = 100;  
//    private final int soundPriority = 1;  
//    private final float soundSpeed = 1f;//播放速度 0.5 -2 之间   
    
    private Handler iHandler;
	private Runnable mDelayedTimeTask;
	
	private AssetFileDescriptor[] soundListName = null;
	
//    private HashMap<Integer, MediaPlayer> iMapPlayer;
    
    private int iCurrentMediaIDIndex = -1;
    
//    private final int MEDIALISTID_SIZE = 2;
    
    private HashMap<Integer, Integer> HashMapMediaID;
    
    private int iModelID = 0;
    
    public int loadLevel = 0;
	 /** 
     * media 背景音乐 
     */  
    private int[] mediaListID = {  
            SoundsResID.bg1,
            SoundsResID.bg2,
            SoundsResID.bg3,
            SoundsResID.bg4,
            SoundsResID.bg5,
            SoundsResID.bg6,
            SoundsResID.bg7,
            SoundsResID.bg8
    }; 
    
    private String[] mediaListName= {  
    		"bg1.ogg",
    		"bg2.ogg",
    		"bg3.ogg",
    		"bg4.ogg",
    		"bg5.ogg",
    		"bg6.ogg",
    		"bg7.ogg",
    		"bg8.ogg"
    };  
    
    /** 
    * player's sound 
    */  
   private int[] soundListID = {  
//		SoundsResID.one,
		SoundsResID.two,
		SoundsResID.three,
		SoundsResID.four,
		SoundsResID.five,
		SoundsResID.six,
		SoundsResID.seven,
		SoundsResID.eight,
		SoundsResID.nine,
		SoundsResID.ten,
		SoundsResID.one_point,
		SoundsResID.two_point,
		SoundsResID.three_point,
		SoundsResID.four_point,
		SoundsResID.five_point,
		SoundsResID.six_point,
		SoundsResID.pi,
		SoundsResID.qiangpi,
		SoundsResID.fanpi,
		SoundsResID.z0,
		SoundsResID.z1,
		SoundsResID.z2
   };  
   
   private int[] soundListID1 = {  
			SoundsResID.two,
			SoundsResID.three,
			SoundsResID.four,
			SoundsResID.five,
			SoundsResID.six,
			SoundsResID.one_point,
			SoundsResID.two_point,
			SoundsResID.three_point,
			SoundsResID.four_point,
			SoundsResID.five_point,
			SoundsResID.six_point,
			SoundsResID.pi,
			SoundsResID.qiangpi,
			SoundsResID.fanpi,
			SoundsResID.z0,
			SoundsResID.z1,
			SoundsResID.z2
	   };  
   
   private int[] soundListID2 = {  
			SoundsResID.pi,
			SoundsResID.qiangpi,
			SoundsResID.fanpi,
			SoundsResID.z0,
			SoundsResID.z1,
			SoundsResID.z2
	   };
   
   private int[] soundListID3 = {  
	   };  
   
   /**
    * other sound
    */
   private int[] otherSoundListID = {
   		SoundsResID.water,
   		SoundsResID.coin,
   		SoundsResID.bn_pressed, 
   		SoundsResID.shakadice,
   		SoundsResID.windowgo,
   		SoundsResID.blow,
   		SoundsResID.bottle,
   		SoundsResID.cake,
   		SoundsResID.electric
//   		SoundsResID.harm
   };
   
   private void loadSoundRes() {
		try {
			switch(iModelID) {
			//------------------------------------------------------------
			case 0: //男
			{
				 switch (loadLevel) {
				 case 1:
					 soundListName = new AssetFileDescriptor[]{
							assetManager.openFd("sounds/modelzero/2ge.ogg"), 
							assetManager.openFd("sounds/modelzero/3ge.ogg"), 
							assetManager.openFd("sounds/modelzero/4ge.ogg"), 
							assetManager.openFd("sounds/modelzero/5ge.ogg"), 
							assetManager.openFd("sounds/modelzero/6ge.ogg"), 
							assetManager.openFd("sounds/modelzero/1d.ogg"), 
							assetManager.openFd("sounds/modelzero/2d.ogg"), 
							assetManager.openFd("sounds/modelzero/3d.ogg"), 
							assetManager.openFd("sounds/modelzero/4d.ogg"), 
							assetManager.openFd("sounds/modelzero/5d.ogg"), 
							assetManager.openFd("sounds/modelzero/6d.ogg"), 
							assetManager.openFd("sounds/modelzero/pi.ogg"),
							assetManager.openFd("sounds/modelzero/qiangpi.ogg"),
							assetManager.openFd("sounds/modelzero/fanpi.ogg"),
							assetManager.openFd("sounds/modelzero/z0.ogg"),
							assetManager.openFd("sounds/modelzero/z1.ogg"),
							assetManager.openFd("sounds/modelzero/z2.ogg")
					};
					break;
				case 2:
					soundListName = new AssetFileDescriptor[]{
							assetManager.openFd("sounds/modelzero/pi.ogg"),
							assetManager.openFd("sounds/modelzero/qiangpi.ogg"),
							assetManager.openFd("sounds/modelzero/fanpi.ogg"),
							assetManager.openFd("sounds/modelzero/z0.ogg"),
							assetManager.openFd("sounds/modelzero/z1.ogg"),
							assetManager.openFd("sounds/modelzero/z2.ogg")
					};
					break;
				case 3:
					soundListName = new AssetFileDescriptor[]{};
					break;
				default:
					soundListName = new AssetFileDescriptor[]{
							assetManager.openFd("sounds/modelzero/2ge.ogg"), 
							assetManager.openFd("sounds/modelzero/3ge.ogg"), 
							assetManager.openFd("sounds/modelzero/4ge.ogg"), 
							assetManager.openFd("sounds/modelzero/5ge.ogg"), 
							assetManager.openFd("sounds/modelzero/6ge.ogg"), 
							assetManager.openFd("sounds/modelzero/7ge.ogg"), 
							assetManager.openFd("sounds/modelzero/8ge.ogg"), 
							assetManager.openFd("sounds/modeltwo/9ge.ogg"), 
							assetManager.openFd("sounds/modelzero/10ge.ogg"),  
							assetManager.openFd("sounds/modelzero/1d.ogg"), 
							assetManager.openFd("sounds/modelzero/2d.ogg"), 
							assetManager.openFd("sounds/modelzero/3d.ogg"), 
							assetManager.openFd("sounds/modelzero/4d.ogg"), 
							assetManager.openFd("sounds/modelzero/5d.ogg"), 
							assetManager.openFd("sounds/modelzero/6d.ogg"), 
							assetManager.openFd("sounds/modelzero/pi.ogg"),
							assetManager.openFd("sounds/modelzero/qiangpi.ogg"),
							assetManager.openFd("sounds/modelzero/fanpi.ogg"),
							assetManager.openFd("sounds/modelzero/z0.ogg"),
							assetManager.openFd("sounds/modelzero/z1.ogg"),
							assetManager.openFd("sounds/modelzero/z2.ogg")
						};
						break;
			 		}
				}
				break;
			
			//------------------------------------------------------------
			case 1: //女
				{
					switch (loadLevel) {
					case 1:
						soundListName = new AssetFileDescriptor[]{
								assetManager.openFd("sounds/modelone/2ge.ogg"), 
								assetManager.openFd("sounds/modelone/3ge.ogg"), 
								assetManager.openFd("sounds/modelone/4ge.ogg"), 
								assetManager.openFd("sounds/modelone/5ge.ogg"), 
								assetManager.openFd("sounds/modelone/6ge.ogg"), 
								assetManager.openFd("sounds/modelone/1d.ogg"), 
								assetManager.openFd("sounds/modelone/2d.ogg"), 
								assetManager.openFd("sounds/modelone/3d.ogg"), 
								assetManager.openFd("sounds/modelone/4d.ogg"), 
								assetManager.openFd("sounds/modelone/5d.ogg"), 
								assetManager.openFd("sounds/modelone/6d.ogg"), 
								assetManager.openFd("sounds/modelone/pi.ogg"),
								assetManager.openFd("sounds/modelone/qiangpi.ogg"),
								assetManager.openFd("sounds/modelone/fanpi.ogg"),
								assetManager.openFd("sounds/modelone/z0.ogg"),
								assetManager.openFd("sounds/modelone/z1.ogg"),
								assetManager.openFd("sounds/modelone/z2.ogg")
						};
						break;
					case 2:
						soundListName = new AssetFileDescriptor[]{
								assetManager.openFd("sounds/modelone/pi.ogg"),
								assetManager.openFd("sounds/modelone/qiangpi.ogg"),
								assetManager.openFd("sounds/modelone/fanpi.ogg"),
								assetManager.openFd("sounds/modelone/z0.ogg"),
								assetManager.openFd("sounds/modelone/z1.ogg"),
								assetManager.openFd("sounds/modelone/z2.ogg")
						};
						break;
					case 3:
						soundListName = new AssetFileDescriptor[]{
						};
						break;
					default:
						soundListName = new AssetFileDescriptor[]{
								assetManager.openFd("sounds/modelone/2ge.ogg"), 
								assetManager.openFd("sounds/modelone/3ge.ogg"), 
								assetManager.openFd("sounds/modelone/4ge.ogg"), 
								assetManager.openFd("sounds/modelone/5ge.ogg"), 
								assetManager.openFd("sounds/modelone/6ge.ogg"), 
								assetManager.openFd("sounds/modelone/7ge.ogg"), 
								assetManager.openFd("sounds/modelone/8ge.ogg"), 
								assetManager.openFd("sounds/modelone/9ge.ogg"), 
								assetManager.openFd("sounds/modelone/10ge.ogg"),  
								assetManager.openFd("sounds/modelone/1d.ogg"), 
								assetManager.openFd("sounds/modelone/2d.ogg"), 
								assetManager.openFd("sounds/modelone/3d.ogg"), 
								assetManager.openFd("sounds/modelone/4d.ogg"), 
								assetManager.openFd("sounds/modelone/5d.ogg"), 
								assetManager.openFd("sounds/modelone/6d.ogg"), 
								assetManager.openFd("sounds/modelone/pi.ogg"),
								assetManager.openFd("sounds/modelone/qiangpi.ogg"),
								assetManager.openFd("sounds/modelone/fanpi.ogg"),
								assetManager.openFd("sounds/modelone/z0.ogg"),
								assetManager.openFd("sounds/modelone/z1.ogg"),
								assetManager.openFd("sounds/modelone/z2.ogg")
						};
						break;
					}
				}
				break;
				
			//------------------------------------------------------------
			case 2: //男
			{
				 switch (loadLevel) {
				 case 1:
					 soundListName = new AssetFileDescriptor[]{
							assetManager.openFd("sounds/modeltwo/2ge.ogg"), 
							assetManager.openFd("sounds/modeltwo/3ge.ogg"), 
							assetManager.openFd("sounds/modeltwo/4ge.ogg"), 
							assetManager.openFd("sounds/modeltwo/5ge.ogg"), 
							assetManager.openFd("sounds/modeltwo/6ge.ogg"), 
							assetManager.openFd("sounds/modeltwo/1d.ogg"), 
							assetManager.openFd("sounds/modeltwo/2d.ogg"), 
							assetManager.openFd("sounds/modeltwo/3d.ogg"), 
							assetManager.openFd("sounds/modeltwo/4d.ogg"), 
							assetManager.openFd("sounds/modeltwo/5d.ogg"), 
							assetManager.openFd("sounds/modeltwo/6d.ogg"), 
							assetManager.openFd("sounds/modeltwo/pi.ogg"),
							assetManager.openFd("sounds/modeltwo/qiangpi.ogg"),
							assetManager.openFd("sounds/modeltwo/fanpi.ogg"),
							assetManager.openFd("sounds/modeltwo/z0.ogg"),
							assetManager.openFd("sounds/modeltwo/z1.ogg"),
							assetManager.openFd("sounds/modeltwo/z2.ogg")
					};
					break;
				case 2:
					soundListName = new AssetFileDescriptor[]{
							assetManager.openFd("sounds/modeltwo/pi.ogg"),
							assetManager.openFd("sounds/modeltwo/qiangpi.ogg"),
							assetManager.openFd("sounds/modeltwo/fanpi.ogg"),
							assetManager.openFd("sounds/modeltwo/z0.ogg"),
							assetManager.openFd("sounds/modeltwo/z1.ogg"),
							assetManager.openFd("sounds/modeltwo/z2.ogg")
					};
					break;
				case 3:
					soundListName = new AssetFileDescriptor[]{};
					break;
				default:
					soundListName = new AssetFileDescriptor[]{
							assetManager.openFd("sounds/modeltwo/2ge.ogg"), 
							assetManager.openFd("sounds/modeltwo/3ge.ogg"), 
							assetManager.openFd("sounds/modeltwo/4ge.ogg"), 
							assetManager.openFd("sounds/modeltwo/5ge.ogg"), 
							assetManager.openFd("sounds/modeltwo/6ge.ogg"), 
							assetManager.openFd("sounds/modeltwo/7ge.ogg"), 
							assetManager.openFd("sounds/modeltwo/8ge.ogg"), 
							assetManager.openFd("sounds/modeltwo/9ge.ogg"), 
							assetManager.openFd("sounds/modeltwo/10ge.ogg"),  
							assetManager.openFd("sounds/modeltwo/1d.ogg"), 
							assetManager.openFd("sounds/modeltwo/2d.ogg"), 
							assetManager.openFd("sounds/modeltwo/3d.ogg"), 
							assetManager.openFd("sounds/modeltwo/4d.ogg"), 
							assetManager.openFd("sounds/modeltwo/5d.ogg"), 
							assetManager.openFd("sounds/modeltwo/6d.ogg"), 
							assetManager.openFd("sounds/modeltwo/pi.ogg"),
							assetManager.openFd("sounds/modeltwo/qiangpi.ogg"),
							assetManager.openFd("sounds/modeltwo/fanpi.ogg"),
							assetManager.openFd("sounds/modeltwo/z0.ogg"),
							assetManager.openFd("sounds/modeltwo/z1.ogg"),
							assetManager.openFd("sounds/modeltwo/z2.ogg")
						};
						break;
			 		}
				}
				break;
			
			//------------------------------------------------------------
			case 3: //女
				{
					switch (loadLevel) {
					case 1:
						soundListName = new AssetFileDescriptor[]{
								assetManager.openFd("sounds/modelthr/2ge.ogg"), 
								assetManager.openFd("sounds/modelthr/3ge.ogg"), 
								assetManager.openFd("sounds/modelthr/4ge.ogg"), 
								assetManager.openFd("sounds/modelthr/5ge.ogg"), 
								assetManager.openFd("sounds/modelthr/6ge.ogg"), 
								assetManager.openFd("sounds/modelthr/1d.ogg"), 
								assetManager.openFd("sounds/modelthr/2d.ogg"), 
								assetManager.openFd("sounds/modelthr/3d.ogg"), 
								assetManager.openFd("sounds/modelthr/4d.ogg"), 
								assetManager.openFd("sounds/modelthr/5d.ogg"), 
								assetManager.openFd("sounds/modelthr/6d.ogg"), 
								assetManager.openFd("sounds/modelthr/pi.ogg"),
								assetManager.openFd("sounds/modelthr/qiangpi.ogg"),
								assetManager.openFd("sounds/modelthr/fanpi.ogg"),
								assetManager.openFd("sounds/modelthr/z0.ogg"),
								assetManager.openFd("sounds/modelthr/z1.ogg"),
								assetManager.openFd("sounds/modelthr/z2.ogg")
						};
						break;
					case 2:
						soundListName = new AssetFileDescriptor[]{
								assetManager.openFd("sounds/modelthr/pi.ogg"),
								assetManager.openFd("sounds/modelthr/qiangpi.ogg"),
								assetManager.openFd("sounds/modelthr/fanpi.ogg"),
								assetManager.openFd("sounds/modelthr/z0.ogg"),
								assetManager.openFd("sounds/modelthr/z1.ogg"),
								assetManager.openFd("sounds/modelthr/z2.ogg")
						};
						break;
					case 3:
						soundListName = new AssetFileDescriptor[]{
						};
						break;
					default:
						soundListName = new AssetFileDescriptor[]{
								assetManager.openFd("sounds/modelthr/2ge.ogg"), 
								assetManager.openFd("sounds/modelthr/3ge.ogg"), 
								assetManager.openFd("sounds/modelthr/4ge.ogg"), 
								assetManager.openFd("sounds/modelthr/5ge.ogg"), 
								assetManager.openFd("sounds/modelthr/6ge.ogg"), 
								assetManager.openFd("sounds/modelthr/7ge.ogg"), 
								assetManager.openFd("sounds/modelthr/8ge.ogg"), 
								assetManager.openFd("sounds/modelthr/9ge.ogg"), 
								assetManager.openFd("sounds/modelthr/10ge.ogg"),  
								assetManager.openFd("sounds/modelthr/1d.ogg"), 
								assetManager.openFd("sounds/modelthr/2d.ogg"), 
								assetManager.openFd("sounds/modelthr/3d.ogg"), 
								assetManager.openFd("sounds/modelthr/4d.ogg"), 
								assetManager.openFd("sounds/modelthr/5d.ogg"), 
								assetManager.openFd("sounds/modelthr/6d.ogg"), 
								assetManager.openFd("sounds/modelthr/pi.ogg"),
								assetManager.openFd("sounds/modelthr/qiangpi.ogg"),
								assetManager.openFd("sounds/modelthr/fanpi.ogg"),
								assetManager.openFd("sounds/modelthr/z0.ogg"),
								assetManager.openFd("sounds/modelthr/z1.ogg"),
								assetManager.openFd("sounds/modelthr/z2.ogg")
						};
						break;
					}
				}
				break;
				
			//------------------------------------------------------------	
			default:	//其它
				{
					soundListName = new AssetFileDescriptor[]{
							assetManager.openFd("sounds/water.ogg"),
							assetManager.openFd("sounds/coin.ogg"),
							assetManager.openFd("sounds/bn_pressed.ogg"), 
							assetManager.openFd("sounds/shakadice.ogg"),
							assetManager.openFd("sounds/windowgo.ogg"),
							assetManager.openFd("sounds/blow.ogg"),
							assetManager.openFd("sounds/bottle.ogg"),
							assetManager.openFd("sounds/cake.ogg"),
							assetManager.openFd("sounds/electric.ogg")
							//assetManager.openFd("sounds/harm.ogg"),
					};
				}
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
   
    public static MediaManager createMediaManager(int aModelID) {
    	int tIndex = aModelID;
    	if(aModelID < 0 || aModelID >= MediaManager.MAX_SOUND_ENGINE_COUNT) {
    		tIndex = 0; 
    	}
		if (iMediaManager[tIndex] == null) {
			iMediaManager[tIndex] = new MediaManager(MyArStation.mGameMain, tIndex);
		}
		return iMediaManager[tIndex];
    }
   
	public static MediaManager getMediaManagerInstance(int aModelID) {
		int tIndex = 0;
	/*
	 	//精简版	
		if(aModelID == 0 || aModelID == 2) {
			tIndex = 0;
		}
		else if(aModelID == 1 || aModelID == 3 || aModelID == 4) {
			tIndex = 1;
		}
		else {
			tIndex = MAX_SOUND_ENGINE_COUNT-1;
		}
	*/	
		//豪华版
		if(aModelID >= 0 && aModelID < 3) {
			tIndex = aModelID;
		}
		else if(aModelID == 3 || aModelID == 4) {
			tIndex = 3;
		}
		else {
			tIndex = MAX_SOUND_ENGINE_COUNT-1;
		}
		return iMediaManager[tIndex];
	}

	public MediaManager(Context context, int aModelID) {
		iContext = context;
		iModelID = aModelID;
		iHandler = new Handler();
	}
	
	public void initMediaManager() {
		Tools.debug("MediaManager::initMediaManager 1");
		iAudioManager = (AudioManager) MyArStation.getInstance().getSystemService(
				Context.AUDIO_SERVICE);
		Tools.debug("MediaManager::initMediaManager 2");
		assetManager = iContext.getAssets();
		Tools.debug("MediaManager::initMediaManager 3");
		initMediaPlayer();
		Tools.debug("MediaManager::initMediaManager 4");
		initSoundPool();
		Tools.debug("MediaManager::initMediaManager 5");
	}
	
	private float getSoudVolume() {
		iVolumeCurrent = iAudioManager
		.getStreamVolume(AudioManager.STREAM_MUSIC);
		iVolumeMax = iAudioManager
		.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		return (iVolumeCurrent / iVolumeMax);
	}
	
	private void initMediaPlayer() {  
		HashMapMediaID = new HashMap<Integer, Integer>();
		iMusicMap = new HashMap<Integer, String>();
		for(int i = 0; i < mediaListID.length; i++) {
			iMusicMap.put(mediaListID[i], mediaListName[i]);
			HashMapMediaID.put(i, mediaListID[i]);
		}
	}  
		 
	private void initSoundPool(){  
		Tools.debug("MediaManager::initSoundPool 1");
		loadSoundRes();
//		Tools.debug("MediaManager::initSoundPool 2");
		iSoundPool = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, srcQuality);
//		Tools.debug("MediaManager::initSoundPool 3");
		if(iModelID < MAX_SOUND_ENGINE_COUNT-1) {
			Tools.debug("MediaManager::initSoundPool 4");
			switch (loadLevel) {
			case 1:
				{
					Tools.debug("MediaManager::initSoundPool 5");
					int len = soundListID1.length;
					iSoundPoolMap = new HashMap<Integer, Integer>(len);
					for (int i = 0; i < len; i++) {
						iSoundPoolMap.put(soundListID1[i], iSoundPool.load(soundListName[i], 1));
					}
					Tools.debug("MediaManager::initSoundPool 6");
				}
				break;
			case 2:
				{
					Tools.debug("MediaManager::initSoundPool 7");
					int len = soundListID2.length;
					iSoundPoolMap = new HashMap<Integer, Integer>(len);
					for (int i = 0; i < len; i++) {
						iSoundPoolMap.put(soundListID2[i], iSoundPool.load(soundListName[i], 1));
					}
					Tools.debug("MediaManager::initSoundPool 8");
				}
				break;
			case 3:
				{
					Tools.debug("MediaManager::initSoundPool 9");
					int len = soundListID3.length;
					iSoundPoolMap = new HashMap<Integer, Integer>(len);
					for (int i = 0; i < len; i++) {
						iSoundPoolMap.put(soundListID3[i], iSoundPool.load(soundListName[i], 1));
					}
					Tools.debug("MediaManager::initSoundPool 10");
				}
				break;
			default:
				{
					Tools.debug("MediaManager::initSoundPool 11");
					int len = soundListID.length;
					iSoundPoolMap = new HashMap<Integer, Integer>(len);
					for (int i = 0; i < len; i++) {
						iSoundPoolMap.put(soundListID[i], iSoundPool.load(soundListName[i], 1));
					}
					Tools.debug("MediaManager::initSoundPool 12");
				}
				break;
			}
		}
		else {
			Tools.debug("MediaManager::initSoundPool 13");
			int len = otherSoundListID.length;
			iSoundPoolMap = new HashMap<Integer, Integer>(len);
			for (int i = 0; i < len; i++) {
				iSoundPoolMap.put(otherSoundListID[i], iSoundPool.load(soundListName[i], 1));
			}
			Tools.debug("MediaManager::initSoundPool 14");
		}
		Tools.debug("MediaManager::initSoundPool 15");
    }  
	
	public synchronized void playerSound(int aSound, int aLoop) {
		if(!blnOpenEffectSound){  
            return;  
        }  
		if (iSoundPool != null || iSoundPoolMap != null) {
			//System.out.println("MediaManager ModelID=" + iModelID + " aSound=" + aSound + " iSoundVolume = " + iSoundVolume);
			iSoundVolume = getSoudVolume();
			try {
				iSoundPool.play(iSoundPoolMap.get(aSound), iSoundVolume, iSoundVolume, 1, aLoop, 1f);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public synchronized void pauseSound(int aSoundID) {
		try {
			iSoundPool.pause(aSoundID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void stopSound(int aSoundID) {
		try {
			iSoundPool.stop(aSoundID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadSound(int[] aSoundID, AssetFileDescriptor[] aSoundName) {
		int len = aSoundID.length;
		iSoundPoolMap = new HashMap<Integer, Integer>(len);
		for (int i = 0; i < len; i++) {
			iSoundPoolMap.put(aSoundID[i], iSoundPool.load(aSoundName[i], 1));
//			System.out.println("aSoundID len =" + len + "aSoundID = " + i);
		}
	}
	
	public boolean loadMusic(int aMusicID) throws IOException {
		if(iCurrentMediaIDIndex == aMusicID) {
//			System.out.println("iCurrentMediaIDIndex=" + iCurrentMediaIDIndex +
//					" aMusicID = " + aMusicID );
			return true;
		}
		releaseMusic();
		iCurrentMediaIDIndex = aMusicID;
		iMediaPlayer = new MediaPlayer();
		AssetFileDescriptor fileDescriptor = assetManager.openFd("sounds/" + 
				iMusicMap.get(aMusicID));
		if(fileDescriptor == null) {
			return false;
		}
		iMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
				fileDescriptor.getStartOffset(), fileDescriptor.getLength());
		iMediaPlayer.prepare();
		iMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				iPlaying = false;
//				System.out.println("MediaManager Play Complete!");
				if (iLoop) {
//					System.out.println("MediaManager Loop !!!");
					iPlaying = true;
					mp.start();
				}
			}
		});
		return true;
	}

	public synchronized void playMusic(int aMusicID, boolean aLoop) {
//		if (true) {
//			return;
//		}
		try {
			if(loadMusic(aMusicID) == false){
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Tools.debug("playMusic blnOpenBgSound = " + blnOpenBgSound);
		if(iMediaPlayer == null) {
			return;
		}
		iLoop = aLoop;
//		System.out.println("MediaManager IsPlaying = " + iPlaying);
		if (iPlaying) {
//			System.out.println("MediaManager return OK!");
			return;
		}
		if (blnOpenBgSound) {
//			System.out.println("MediaManager blnOpenBgSound=true, iPlaying=true!");
			iPlaying = true;
//			try {
//				iMediaPlayer.prepare();
//			} catch (IllegalStateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			iMediaPlayer.start();
		}
//		else {
//			System.out.println("MediaManager blnOpenBgSound=false, iPlaying=false!");
//			iPlaying = false;
//			iMediaPlayer.stop();
//		}
	}
	
	public synchronized void playMusicSeekTo(int aMusicID, boolean aLoop, int position) {
//		if (true) {
//			return;
//		}
		try {
			if(loadMusic(aMusicID) == false){
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Tools.debug("playMusic blnOpenBgSound = " + blnOpenBgSound);
		if(iMediaPlayer == null) {
			return;
		}
		iLoop = aLoop;
		if (iPlaying) {
			return;
		}
		if (blnOpenBgSound) {
			iPlaying = true;
			int Duration = iMediaPlayer.getDuration();
			Tools.debug("Duration:" + Duration + "position:"+position);
			if(position > 0 && position < Duration) {
				iMediaPlayer.seekTo(position);
				iMediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {
					
					@Override
					public void onSeekComplete(MediaPlayer mp) {
						iMediaPlayer.start();
					}
				});
			}
			else {
				iMediaPlayer.start();
			}
			
		}
	}

	public int getMusicCurPos() {
		if(iMediaPlayer == null) {
			return 0;
		}
		return iMediaPlayer.getCurrentPosition();
	}
	
	public void setMusicVolume(float aVolume) {
		iMediaPlayer.setVolume(aVolume, aVolume);
	}

	public void setSoundVolume(float aVolume) {
		iSoundVolume = aVolume;
	}

    /** 
     * 暂停MediaPlayer音乐 
     */  
    public synchronized void pauseMusic(){  
    	try {
//    		System.out.println("MediaManager Pause music!");
			iLoop = false;
			if (iPlaying) {
				iPlaying = false;
				iMediaPlayer.pause();
			}
		} catch (Exception e) {
			System.out.println("SoundEngine::pauseMusic" + iName + " "
					+ e.toString());
		}
    }
    
	public synchronized void stopMusic() {
		try {
//			System.out.println("MediaManager stop music iPlaying=false!");
			iCurrentMediaIDIndex = -1;
			iLoop = false;
			if (iPlaying) {
				iPlaying = false;
				iMediaPlayer.stop();
			}
		} catch (Exception e) {
			System.out.println("SoundEngine::stopMusic" + iName + " "
					+ e.toString());
		}
	}

	public void releaseAll() {
		releaseMusic();
		releaseSound();
	}

	public void releaseMusic() {
		if (iMediaPlayer != null) {
			iPlaying = false;
			iLoop = false;
			iMediaPlayer.release();
			iMediaPlayer = null;
			iCurrentMediaIDIndex = -1;
		}
	}

	public void releaseSound() {
		if (iSoundPool != null) {
			iSoundPool.release();
			iSoundPool = null;
			iSoundPoolMap.clear();
			iSoundPool = null;
		}
	}

	public void playSoundDelayed(final int soundId, final int aLoop,
			final long millisec) {
		// TIMER
		mDelayedTimeTask = new Runnable() {
			int counter = 0;

			public void run() {
				counter++;

				if (counter == 1) {
					boolean ret = iHandler.postDelayed(this, millisec);
					if (ret == false)
						System.err
								.println("playSound_Delayed::mHandler.postAtTime FAILED!");
				} else {
//					System.out.println("playSound_Delayed");
					playerSound(soundId, aLoop);
				}
			}
		};
		mDelayedTimeTask.run();
	}
	
	 /*** 
     * 是否开启背景音乐 
     */  
    public void setOpenBgState(boolean bgSound){  
    	blnOpenBgSound = bgSound;  
    	Tools.debug("setOpenBgState blnOpenBgSound = " + blnOpenBgSound + " bgSound = " + bgSound);
    	if(iMediaPlayer == null) {
    		return;
    	}
        if(!blnOpenBgSound) {
//        	System.out.println("MediaManager SetOpenBgState iPlaying=false!");
        	iPlaying = false;
        	iMediaPlayer.pause();
        }
        else {
//        	System.out.println("MediaManager SetOpenBgState iPlaying=true!");
        	if(iCurrentMediaIDIndex >= 0) {
        		iPlaying = true;
        		iMediaPlayer.start();
        	}
        }
    }  
    
    /*** 
     * 是否开启特效音乐 
     */  
    public void setOpenEffectState(boolean effectSound){  
        if(blnOpenEffectSound == effectSound) {
        	return;
        }
        Tools.debug("setOpenEffectState blnOpenEffectSound = " + blnOpenEffectSound + " effectSound = " + effectSound);
        if(!effectSound && iSoundPoolMap != null && iSoundPool != null){  
        	Collection<Integer> cl = iSoundPoolMap.values();
        	Iterator<Integer> iter = cl.iterator();
        	while(iter.hasNext()) {
        		iSoundPool.pause(iter.next());  
        	}
        }  
        blnOpenEffectSound = effectSound;  
    }  
    
    /**
     * param aState
     * description 
     */
    public void setOpenMediaState(boolean aState) {
    	setOpenEffectState(aState);
    	setOpenBgState(aState);
    }
    
//    private int randomID() {
//    	Random random = new Random();
//    	int IDIndex = 0;
//    	int size =  mediaListID.length;
//    	do {
//    		IDIndex = Math.abs(random.nextInt() % size);
//    	}while(IDIndex == iCurrentMediaIDIndex);
//    	return IDIndex;
//    }
}
