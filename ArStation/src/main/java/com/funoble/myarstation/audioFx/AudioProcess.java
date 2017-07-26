package com.funoble.myarstation.audioFx;

import java.util.ArrayList;

import com.funoble.myarstation.common.Tools;

import android.content.Context;
import android.media.AudioRecord;
import android.util.Log;

public class AudioProcess {
	public static final float pi= (float) 3.1415926;
	//应该把处理前后处理后的普线都显示出来
	private ArrayList<short[]> inBuf = new ArrayList<short[]>();//原始录入数据
	private ArrayList<int[]> outBuf = new ArrayList<int[]>();//处理后的数据
	private boolean isRecording = false;

	//上下文
	Context mContext;
	public int frequence = 0;
	private int length = 256;
	//缩小的比例
	public int rate = 10001;
	
	private OnDataCaptureListener dataCatureListener;
	
	//初始化画图的一些参数
	public void initDraw(int rate, Context mContext, int frequence){
		this.mContext = mContext;
		this.rate = rate;
		this.frequence = frequence;
	}
	
	//启动程序
	public void start(AudioRecord audioRecord, int minBufferSize) {
		isRecording = true;
		new RecordThread(audioRecord, minBufferSize).start();
	}
	
	//停止程序
	public void stop(){
		isRecording = false;
		inBuf.clear();
	}
	
	//录音线程
	class RecordThread extends Thread{
		private AudioRecord audioRecord;
		private int minBufferSize;
		
		public RecordThread(AudioRecord audioRecord,int minBufferSize){
			this.audioRecord = audioRecord;
			this.minBufferSize = minBufferSize;
		}
		
		public void run(){
			try{
				short[] buffer = new short[minBufferSize];
				audioRecord.startRecording();
				while(isRecording){
					int res = audioRecord.read(buffer, 0, minBufferSize);
					//将录音结果存放到inBuf中,以备画时域图使用
					synchronized (inBuf){
						inBuf.add(buffer);
					}
					//保证长度为2的幂次数
					length=up2int(res);
//					length = 256;
					short[]tmpBuf = new short[length];
					System.arraycopy(buffer, 0, tmpBuf, 0, length);
					//---------------------
					double[] x = new double[length];
					double[] y = new double[length];
					for(int i=0;i < length; i++){
						Short short1 = tmpBuf[i];
						x[i] = short1.doubleValue();
						y[i] = 0;
					}
					FFT fft = new FFT(length);
					fft.fft(x, y);
					int[]outInt = new int[length];
					for (int i = 0; i < length; i++) {
						outInt[i] = (int) Math.round(Math.sqrt(x[i]*x[i] + y[i]*y[i]));
					}
					synchronized (outBuf) {
						outBuf.add(outInt);
						if(dataCatureListener != null && outBuf.size() > 0) {
							for(int i = 0; i < outBuf.size(); i++) {
								int max = 25;
								int[]buf = outBuf.get(i);
								for(int j = 0; j < buf.length; j++) {
									if(max < (buf[i] / rate)) {
										Tools.debug("buf[i] / rate" + buf[i] / rate);
										dataCatureListener.onFftDataCapture(buf, rate);
									}
								}
							}
							outBuf.clear();
						}
					}
				}
				audioRecord.stop();
				Thread.sleep(1000);
			}catch (Exception e) {
				Log.i("Rec E",e.toString());
			}
			
		}
	}
	
	/**
	 * 向上取最接近iint的2的幂次数.比如iint=320时,返回256
	 * @param iint
	 * @return
	 */
	private int up2int(int iint) {
		int ret = 1;
		while (ret<=iint) {
			ret = ret << 1;
		}
		return ret>>1;
	}
	
	public void setDataCaptureListener(OnDataCaptureListener l) {
		this.dataCatureListener = l;
	}
	
	public interface OnDataCaptureListener {
		public void onFftDataCapture(int[] fft,int samplingRate);
	}
}
