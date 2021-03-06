package com.funoble.myarstation.gamelogic;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeListener implements SensorEventListener {

	private static final int FORCE_THRESHOLD = 15;
	private static final int TIME_THRESHOLD = 300;
	private static final int SHAKE_TIMEOUT = 800;
	private static final int SHAKE_DURATION = 500;
	private static final int SHAKE_COUNT = 2;

	private SensorManager mSensorMgr;
	private float mLastX = -1.0f, mLastY = -1.0f, mLastZ = -1.0f;
	private long mLastTime;
	private OnShakeListener mShakeListener;
	private Context mContext;
	private int mShakeCount = 0;
	private long mLastShake;
	private long mLastForce;
	
	private boolean ibOpen = false;

	public interface OnShakeListener {
		public void onShake();
//		public void onShakeHorizontal();
//		public void onShakeVertical();
	}

	public ShakeListener(Context context) {
		mContext = context;
		resume();
	}

	public void setOnShakeListener(OnShakeListener listener) {
		mShakeListener = listener;
	}

	public void resume() {
		mSensorMgr = (SensorManager) mContext
				.getSystemService(Context.SENSOR_SERVICE);
		if (mSensorMgr == null) {
			throw new UnsupportedOperationException("Sensors not supported");
		}
		
		boolean supported = mSensorMgr.registerListener(this, mSensorMgr
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		if (!supported) {
//			mSensorMgr.unregisterListener(this);
//			throw new UnsupportedOperationException(
//					"Accelerometer not supported");
		}
	}

	public void pause() {
		if (mSensorMgr != null) {
			mSensorMgr.unregisterListener(this);
			mSensorMgr = null;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(ibOpen == false) {
			return;
		}
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
			return;
		}

		long now = System.currentTimeMillis();

//		if ((now - mLastForce) > SHAKE_TIMEOUT) {
//			mShakeCount = 0;
//		}

		if ((now - mLastTime) > TIME_THRESHOLD) {
//			long diff = now - mLastTime;
//			float speed = Math.abs(event.values[SensorManager.DATA_X]
//					+ event.values[SensorManager.DATA_Y]
//					+ event.values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ)
//					/ diff * 10000;
			float speed = Math.abs(event.values[SensorManager.DATA_X] - mLastX) 
							+ Math.abs(event.values[SensorManager.DATA_Y] - mLastY)
							+ Math.abs(event.values[SensorManager.DATA_Z] - mLastZ);
//			System.out.println("ShakeTest speed=" + speed);
			if (speed > FORCE_THRESHOLD) {
//				if ((++mShakeCount >= SHAKE_COUNT)
//						&& (now - mLastShake > SHAKE_DURATION)) {
				if(now - mLastShake > SHAKE_DURATION) {
					mLastShake = now;
					mShakeCount = 0;
					if (mShakeListener != null) {
						mShakeListener.onShake();
					}
				}
				mLastForce = now;
			}
			mLastTime = now;
		}
		mLastX = event.values[SensorManager.DATA_X];
		mLastY = event.values[SensorManager.DATA_Y];
		mLastZ = event.values[SensorManager.DATA_Z];
	}
	
	public void startShake() {
		ibOpen = true;
		mLastTime = System.currentTimeMillis();
		resume();
	}
	
	public void stopShake() {
		ibOpen = false;
		pause();
	}
}
