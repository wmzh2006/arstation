package com.funoble.myarstation.utils;


import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
import android.view.WindowManager;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;

public class ScreenBrightnessManager {


	/** * 判断是否开启了自动亮度调节 */
//
//	public static boolean isAutoBrightness(ContentResolver aContentResolver) {    
//
//	boolean automicBrightness = false;    
//
//	try{        
//
//	automicBrightness = Settings.System.getInt(aContentResolver,                
//
//	Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;   
//
//	 } 
//
//	catch(SettingNotFoundException e) 
//
//	{       
//
//	 e.printStackTrace();  
//
//	  }    
//
//	return automicBrightness;
//	}

	/** * 获取屏幕的亮度 */
	
	public static int getScreenBrightness(Activity activity) {   
		int nowBrightnessValue = 0;    
		ContentResolver resolver = activity.getContentResolver();    
		try{        
			nowBrightnessValue = android.provider.Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);  
		}
		catch(Exception e) {       
			e.printStackTrace();  
		}    
		return nowBrightnessValue;
	}
	
	/** * 设置亮度 */

	public static void setBrightness(float brightness) {// 0 ~ 1默示亮度   
		if(brightness == 0) {
			brightness = 10.0f;
		}
		WindowManager.LayoutParams lp = MyArStation.getInstance().getWindow().getAttributes();   
		lp.screenBrightness = brightness / 100;  
		Tools.debug("set  lp.screenBrightness == " + lp.screenBrightness);
		MyArStation.getInstance().getWindow().setAttributes(lp); 
	}
	

//	/** * 停止自动亮度调节 */
//
//	public static void stopAutoBrightness(Activity activity) {   
//
//	 Settings.System.putInt(activity.getContentResolver(),          
//
//	  Settings.System.SCREEN_BRIGHTNESS_MODE,           
//
//	 Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
//	 }
	//能开启，那自然应该能关闭了哟哟，那怎么关闭呢？很简单的：

	/** * 开启亮度自动调节 */ 

//	public static void startAutoBrightness(Activity activity) {   
//
//	 Settings.System.putInt(activity.getContentResolver(),           
//
//	 Settings.System.SCREEN_BRIGHTNESS_MODE,            
//
//	Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
//
//	}
	

	/** * 保存亮度设置状态 */
	public static void saveBrightness(ContentResolver resolver, int brightness) {    
		Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");   
		android.provider.Settings.System.putInt(resolver, "screen_brightness", brightness);    
		resolver.notifyChange(uri, null);
	}
}