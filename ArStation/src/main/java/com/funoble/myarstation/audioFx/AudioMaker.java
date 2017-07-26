package com.funoble.myarstation.audioFx;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.funoble.myarstation.audioFx.AudioProcess.OnDataCaptureListener;
import com.funoble.myarstation.game.MyArStation;

public class AudioMaker {
    int frequency = 44100;//分辨率  
    static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;  
    static final int audioEncodeing = AudioFormat.ENCODING_PCM_16BIT; 
    static final int Max = 1000;//缩小比例最大值  
	
	int minBufferSize;//采集数据需要的缓冲区大小
	AudioRecord audioRecord;//录音
	AudioProcess audioProcess = new AudioProcess();//处理
    
    public void initControl(int frequency) {
    	this.frequency = frequency;
        Context mContext = MyArStation.mGameMain.getApplicationContext();
        //初始化显示
        audioProcess.initDraw(Max ,mContext,frequency);
	}
    
    public void start() { 
    	try {
    		//录音
    		minBufferSize = AudioRecord.getMinBufferSize(frequency, 
    				channelConfiguration, 
    				audioEncodeing);
    		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,frequency,
    				channelConfiguration,
    				audioEncodeing,
    				minBufferSize);
    		audioProcess.frequence = frequency;
    		audioProcess.start(audioRecord, minBufferSize);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void stop() {
    	audioProcess.stop();
    }
    
    public void setDataCaptureListener(OnDataCaptureListener l) {
		audioProcess.setDataCaptureListener(l);
	}
}