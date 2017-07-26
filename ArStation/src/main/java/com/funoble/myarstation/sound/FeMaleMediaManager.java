package com.funoble.myarstation.sound;
///*******************************************************************************
// * Copyright (C)   Inc.All Rights Reserved.
// * FileName: SoundEngine.java 
// * Description:
// * History:  
// * 版本号 作者 日期 简要介绍相关操作 
// * 1.0 cole  2012-5-9 上午10:02:18
// *******************************************************************************/
//package com.funoble.lyingdice.sound;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Random;
//
//import android.content.Context;
//import android.content.res.AssetFileDescriptor;
//import android.content.res.AssetManager;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.media.SoundPool;
//import android.os.Handler;
//
//import com.funoble.lyingdice.common.Tools;
//import com.funoble.lyingdice.game.GameMain;
//
//public class FeMaleMediaManager {
//	/** 
//     * 声音开关 背景音乐 
//     */  
//    private boolean blnOpenBgSound = false;   
//      
//    /** 
//     * 声音开关 特效音乐 
//     */  
//    private boolean blnOpenEffectSound = false;   
//    
//	private SoundPool iSoundPool;
//	private MediaPlayer iMediaPlayer;
//
//	private boolean iPlaying = false;
//	private boolean iLoop = false;
//
//	private String iName;
//	private Context iContext;
//	private AudioManager iAudioManager;
//	private HashMap<Integer, Integer> iSoundPoolMap;
//	private HashMap<Integer, String> iMusicMap;
//
//	private float iVolumeCurrent;
//	private float iVolumeMax;
//	private float iSoundVolume;
//
//	private AssetManager assetManager;
//	private static FeMaleMediaManager iMediaManager = null;
//
//	private final int maxStreams = 10; //streamType音频池的最大音频流数目为10    
//    private final int srcQuality = 100;  
//    private final int soundPriority = 1;  
//    private final float soundSpeed = 1f;//播放速度 0.5 -2 之间   
//    
//    private Handler iHandler;
//	private Runnable mDelayedTimeTask;
//	
//	private AssetFileDescriptor[] soundListName = null;
//	
//    private HashMap<Integer, MediaPlayer> iMapPlayer;
//    
//    private boolean iRandom = true;
//    
//    private int iCurrentMediaIDIndex = 0;
//    
//    private final int MEDIALISTID_SIZE = 2;
//    
//    private HashMap<Integer, Integer> HashMapMediaID;
//	 /** 
//     * media 背景音乐 
//     */  
//    private int[] mediaListID = {  
//            SoundsResID.bg,
//            SoundsResID.bg1
//    }; 
//    
//    private String[] mediaListName= {  
//    		"bg.ogg",
//    		"bg2.ogg"
//    };  
//    
//    /** 
//    * sound 
//    */  
//   private int[] soundListID = {  
//		SoundsResID.one,
//		SoundsResID.two,
//		SoundsResID.three,
//		SoundsResID.four,
//		SoundsResID.five,
//		SoundsResID.six,
//		SoundsResID.seven,
//		SoundsResID.eight,
//		SoundsResID.nine,
//		SoundsResID.ten,
//		SoundsResID.eleven,
//		SoundsResID.twelve,
//		SoundsResID.thirteen,
//		SoundsResID.fourteen,
////		SoundsResID.fifteen,
////		SoundsResID.sixteen,
////		SoundsResID.seventeen,
////		SoundsResID.eighteen,
////		SoundsResID.nineteen,
////		SoundsResID.twenty,
////		SoundsResID.twentyone,
////		SoundsResID.twentytwo,
////		SoundsResID.twentythree,
////		SoundsResID.twentyfour,
////		SoundsResID.twentyfive,
////		SoundsResID.twentysix,
////		SoundsResID.twentyseven,
////		SoundsResID.twentyeight,
////		SoundsResID.twentynine,
////		SoundsResID.thirty,
//		SoundsResID.one_point,
//		SoundsResID.two_point,
//		SoundsResID.three_point,
//		SoundsResID.four_point,
//		SoundsResID.five_point,
//		SoundsResID.six_point,
//		SoundsResID.pi,
//		SoundsResID.water,
//		SoundsResID.burp1
//   };  
//   
//
//   private void loadSoundRes() {
//		try {
//			soundListName = new AssetFileDescriptor[]{
//			assetManager.openFd("sounds/modelone/1ge.ogg"), 
//			assetManager.openFd("sounds/modelone/2ge.ogg"), 
//			assetManager.openFd("sounds/modelone/3ge.ogg"), 
//			assetManager.openFd("sounds/modelone/4ge.ogg"), 
//			assetManager.openFd("sounds/modelone/5ge.ogg"), 
//			assetManager.openFd("sounds/modelone/6ge.ogg"), 
//			assetManager.openFd("sounds/modelone/7ge.ogg"), 
//			assetManager.openFd("sounds/modelone/8ge.ogg"), 
//			assetManager.openFd("sounds/modelone/9ge.ogg"), 
//			assetManager.openFd("sounds/modelone/10ge.ogg"), 
//			assetManager.openFd("sounds/modelone/11ge.ogg"), 
//			assetManager.openFd("sounds/modelone/12ge.ogg"), 
//			assetManager.openFd("sounds/modelone/13ge.ogg"), 
//			assetManager.openFd("sounds/modelone/14ge.ogg"), 
////			assetManager.openFd("sounds/modelone/15ge.ogg"), 
////			assetManager.openFd("sounds/modelone/16ge.ogg"), 
////			assetManager.openFd("sounds/modelone/17ge.ogg"), 
////			assetManager.openFd("sounds/modelone/18ge.ogg"), 
////			assetManager.openFd("sounds/modelone/19ge.ogg"), 
////			assetManager.openFd("sounds/modelone/20ge.ogg"), 
////			assetManager.openFd("sounds/modelone/21ge.ogg"), 
////			assetManager.openFd("sounds/modelone/22ge.ogg"), 
////			assetManager.openFd("sounds/modelone/23ge.ogg"), 
////			assetManager.openFd("sounds/modelone/24ge.ogg"), 
////			assetManager.openFd("sounds/modelone/25ge.ogg"), 
////			assetManager.openFd("sounds/modelone/26ge.ogg"), 
////			assetManager.openFd("sounds/modelone/27ge.ogg"), 
////			assetManager.openFd("sounds/modelone/28ge.ogg"), 
////			assetManager.openFd("sounds/modelone/29ge.ogg"), 
////			assetManager.openFd("sounds/modelone/30ge.ogg"), 
//			assetManager.openFd("sounds/modelone/1d.ogg"), 
//			assetManager.openFd("sounds/modelone/2d.ogg"),
//			assetManager.openFd("sounds/modelone/3d.ogg"), 
//			assetManager.openFd("sounds/modelone/4d.ogg"),
//			assetManager.openFd("sounds/modelone/5d.ogg"), 
//			assetManager.openFd("sounds/modelone/6d.ogg"), 
//			assetManager.openFd("sounds/modelone/pi.ogg"),
//			assetManager.openFd("sounds/water.ogg"),
//			assetManager.openFd("sounds/burp2.ogg")
//			};
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//   
//	public static FeMaleMediaManager getMediaManagerInstance() {
//		if (iMediaManager == null) {
//			iMediaManager = new FeMaleMediaManager(GameMain.mGameMain);
//		}
//		return iMediaManager;
//	}
//
//	public FeMaleMediaManager(Context context) {
//		iContext = context;
//		iHandler = new Handler();
//	}
//	
//	public void initFeMaleMediaManager() {
//		iAudioManager = (AudioManager) GameMain.getInstance().getSystemService(
//				Context.AUDIO_SERVICE);
//		iVolumeCurrent = iAudioManager
//				.getStreamVolume(AudioManager.STREAM_MUSIC);
//		iVolumeMax = iAudioManager
//				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//		iSoundVolume = iVolumeCurrent / iVolumeMax;
//		assetManager = iContext.getAssets();
////		initMediaPlayer();
//		initSoundPool();
//	}
//	
//	private void initMediaPlayer() {  
//		HashMapMediaID = new HashMap<Integer, Integer>();
//		iMusicMap = new HashMap<Integer, String>();
//		for(int i = 0; i < mediaListID.length; i++) {
//			iMusicMap.put(mediaListID[i], mediaListName[i]);
//			HashMapMediaID.put(i, mediaListID[i]);
//		}
//	}  
//		 
//	private void initSoundPool(){  
//		loadSoundRes();
//		iSoundPool = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, srcQuality);
//        int len = soundListID.length;
//		iSoundPoolMap = new HashMap<Integer, Integer>(len);
//		for (int i = 0; i < len; i++) {
//			iSoundPoolMap.put(soundListID[i], iSoundPool.load(soundListName[i], 1));
////			System.out.println("aSoundID len =" + len + "aSoundID = " + i);
//		}
//    }  
//	
//	public synchronized void playerSound(int aSound, int aLoop) {
//		Tools.debug("female playerSound");
//	  if(!blnOpenEffectSound){  
//            return;  
//        }  
//		if (iSoundPool != null) {
//			Tools.debug("female playerSound");
//			iSoundPool.play(iSoundPoolMap.get(aSound), iSoundVolume, iSoundVolume, 1, aLoop, 1f);
//		}
//	}
//
//	public synchronized void pauseSound(int aSoundID) {
//		try {
//			iSoundPool.pause(aSoundID);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public synchronized void stopSound(int aSoundID) {
//		try {
//			iSoundPool.stop(aSoundID);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void loadSound(int[] aSoundID, AssetFileDescriptor[] aSoundName) {
//		int len = aSoundID.length;
//		iSoundPoolMap = new HashMap<Integer, Integer>(len);
//		for (int i = 0; i < len; i++) {
//			iSoundPoolMap.put(aSoundID[i], iSoundPool.load(aSoundName[i], 1));
////			System.out.println("aSoundID len =" + len + "aSoundID = " + i);
//		}
//	}
//	
//	public boolean loadMusic(int aMusicID) throws IOException {
//		releaseMusic();
//		iMediaPlayer = new MediaPlayer();
//		AssetFileDescriptor fileDescriptor = assetManager.openFd("sounds/" + 
//				iMusicMap.get(aMusicID));
//		if(fileDescriptor == null) {
//			return false;
//		}
//		iMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
//				fileDescriptor.getStartOffset(), fileDescriptor.getLength());
//		iMediaPlayer.prepare();
//		
//		iMediaPlayer
//		.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//			
//			@Override
//			public void onCompletion(MediaPlayer mp) {
//				iPlaying = false;
////				if(iMediaPlayer != null && iRandom) {
////					int iCurrentMediaIDIndex = randomID();
////					Integer ID = HashMapMediaID.get(iCurrentMediaIDIndex);
////					if(ID != null) {
////						try {
////							loadMusic(ID);
////						} catch (IOException e) {
////							e.printStackTrace();
////						}
////					}
////				}
//				if (iLoop && !iRandom) {
//					mp.start();
//				}
//			}
//		});
//		return true;
//	}
//
//	public synchronized void playMusic(int aMusicID, boolean aLoop) {
//		try {
//			if(loadMusic(aMusicID) == false){
//				return;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Tools.debug("playMusic blnOpenBgSound = " + blnOpenBgSound);
//		if(iMediaPlayer == null) {
//			return;
//		}
//		iLoop = aLoop;
//		if (iPlaying) {
//			return;
//		}
//		if (blnOpenBgSound) {
//			iPlaying = true;
//			iMediaPlayer.start();
//		}
//		else {
//			iPlaying = false;
//			iMediaPlayer.stop();
//		}
//	}
//	
//
//	public void setMusicVolume(float aVolume) {
//		iMediaPlayer.setVolume(aVolume, aVolume);
//	}
//
//	public void setSoundVolume(float aVolume) {
//		iSoundVolume = aVolume;
//	}
//
//    /** 
//     * 暂停MediaPlayer音乐 
//     */  
//    public synchronized void pauseMusic(){  
//    	try {
//			iLoop = false;
//			if (iPlaying) {
//				iPlaying = false;
//				iMediaPlayer.pause();
//			}
//		} catch (Exception e) {
//			System.out.println("SoundEngine::pauseMusic" + iName + " "
//					+ e.toString());
//		}
//    }
//    
//	public synchronized void stopMusic() {
//		try {
//			iLoop = false;
//			if (iPlaying) {
//				iPlaying = false;
//				iMediaPlayer.stop();
//			}
//		} catch (Exception e) {
//			System.out.println("SoundEngine::stopMusic" + iName + " "
//					+ e.toString());
//		}
//	}
//
//	public void releaseAll() {
//		releaseMusic();
//		releaseSound();
//	}
//
//	public void releaseMusic() {
//		if (iMediaPlayer != null) {
//			iPlaying = false;
//			iLoop = false;
//			iMediaPlayer.release();
//			iMediaPlayer = null;
//		}
//	}
//
//	public void releaseSound() {
//		if (iSoundPool != null) {
//			iSoundPool.release();
//			iSoundPool = null;
//			iSoundPoolMap.clear();
//			iSoundPool = null;
//		}
//	}
//
//	public void playSoundDelayed(final int soundId, final int aLoop,
//			final long millisec) {
//		// TIMER
//		mDelayedTimeTask = new Runnable() {
//			int counter = 0;
//
//			public void run() {
//				counter++;
//
//				if (counter == 1) {
//					boolean ret = iHandler.postDelayed(this, millisec);
//					if (ret == false)
//						System.err
//								.println("playSound_Delayed::mHandler.postAtTime FAILED!");
//				} else {
////					System.out.println("playSound_Delayed");
//					playerSound(soundId, aLoop);
//				}
//			}
//		};
//		mDelayedTimeTask.run();
//	}
//	
//	 /*** 
//     * 是否开启背景音乐 
//     */  
//    public void setOpenBgState(boolean bgSound){  
//    	blnOpenBgSound = bgSound;  
//    	Tools.debug("setOpenBgState blnOpenBgSound = " + blnOpenBgSound + " bgSound = " + bgSound);
//    	if(iMediaPlayer == null) {
//    		return;
//    	}
//        if(!blnOpenBgSound) {
//        	iPlaying = false;
//        	iMediaPlayer.pause();
//        }
//        else {
//        	iPlaying = true;
//        	iMediaPlayer.start();
//        }
//    }  
//    
//    /*** 
//     * 是否开启特效音乐 
//     */  
//    public void setOpenEffectState(boolean effectSound){  
//        blnOpenEffectSound = effectSound;  
//        Tools.debug("setOpenEffectState blnOpenEffectSound = " + blnOpenEffectSound + " effectSound = " + effectSound);
//        if(!effectSound && iSoundPoolMap != null){  
//        	Collection<Integer> cl = iSoundPoolMap.values();
//        	Iterator<Integer> iter = cl.iterator();
//        	while(iter.hasNext()) {
//        		iSoundPool.pause(iter.next());  
//        	}
//        }  
//    }  
//    
//    /**
//     * param aState
//     * description 
//     */
//    public void setOpenMediaState(boolean aState) {
//    	setOpenEffectState(aState);
//    	setOpenBgState(aState);
//    }
//    
//    private int randomID() {
//    	Random random = new Random();
//    	int IDIndex = 0;
//    	int size =  mediaListID.length;
//    	do {
//    		IDIndex = Math.abs(random.nextInt() % size);
//    	}while(IDIndex == iCurrentMediaIDIndex);
//    	return IDIndex;
//    }
//}
