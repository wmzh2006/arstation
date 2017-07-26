/*******************************************************************************
 * Copyright (C) 1998-2009 BBG Inc.All Rights Reserved. FileName: Tools.java
 * Description： History： 版本号 作者 日期 简要介绍相关操作 1.0 sunny 2009-11-9 Create
 *******************************************************************************/
package  com.funoble.myarstation.common;

import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.GameButtonHandler;
import com.funoble.myarstation.socket.protocol.ProtocolType;
import com.funoble.myarstation.socket.protocol.ProtocolType.Url;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <p>
 * </p>
 */
public class Tools
{

    private static final String TAG_DEBUG = "mDebug";
    
    public static final boolean IS_DEBUG = true;

    /**
     * 屏幕宽度
     */
    public static int screenWidth = 0;
    /**
     * 屏幕高度
     */
    public static int screenHeight = 0;
    /**
     * 不包含系统通知栏的屏幕高度
     */
    public static int screenHeightNotSystemStatusBar = 0;
    /**
     * 屏幕分辨率字符串，例如320x480, 小数在前
     */
    public static String screenString = "";

    public static Toast toast;
    /**
     * 作用：
     * <p>
     * 打印debug日志
     * </p>
     * 
     * @param info
     */
    public static void debug(String info)
    {
       if(Url.port == 60002){
    	   Log.d(TAG_DEBUG, info);
       }
    }
    public static void println(String info)
    {
//    	System.out.println(info);
    }

    /**
     * 设置屏幕尺寸参数，并初始化相关信息
     * 
     * @param width
     * @param height
     */
    public static void setScreenSize(int width, int height)
    {
        screenWidth = width;
        screenHeight = height;
        if(width > height)
        {
            screenString = height + "x" + width;
        }
        else
        {
            screenString = width + "x" + height;
        }
    }
    

    public static int dip2px(Context context, float dipValue)
    {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5f);
    }
    
    /**
	 * 隐藏软键盘
	 * 
	 * @param view
	 */
	public static void hideSoftKeyBoard(View view)
	{
		if (view == null)
		{
			return;
		}
		try
		{
			view.requestFocus();
			InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 隐藏输入法
		}
		catch (Exception e)
		{
		}

	}
    
    /**
     * 准确显示位置的toast控件[默认当前是显示系统通知栏，计算坐标时会减去通知栏高度]
     * @param v 要做停靠基准的视图
     * @param gravity 目前只支持以v为中心的 左边，右边，上居中，下居中显示
     * @param info 显示字符
     * @param time 显示时长，只能取Toast.LENGTH_LONG和Toast.LENGTH_SHORT
     * @param maybeToastWidth toast文字可能的长度，单位dip(因为无法准确预估toast的长度，这个值需要自己尝试设置)
     */
    public static void showToast(View v, int gravity, int resId, int time, int maybeToastWidth)
    {
    	if(v != null)
    	{
    		showToast(v, gravity, v.getContext().getString(resId), time, maybeToastWidth, true);
    	}
    }
    
    /**
     * 准确显示位置的toast控件
     * @param v 要做停靠基准的视图
     * @param gravity 目前只支持以v为中心的 左边，右边，上居中，下居中显示
     * @param info 显示字符
     * @param time 显示时长，只能取Toast.LENGTH_LONG和Toast.LENGTH_SHORT
     * @param maybeToastWidth toast文字可能的长度，单位dip(因为无法准确预估toast的长度，这个值需要自己尝试设置)
     * @param isShowedSystemStatusBar 当前是否在显示系统通知栏
     */
    public static void showToast(View v, int gravity, String info, int time, int mybeToastWidth, boolean isShowedSystemStatusBar)
    {
    	if(v == null || info == null)
    	{
    		return;
    	}
    	Context context = v.getContext();
    	int[] xy = new int[2];
		v.getLocationOnScreen(xy);	//获取控件在屏幕中的坐标(包括了通知栏高度)
		if(isShowedSystemStatusBar)	//要减去系统通知栏, toast是按程序可视区域为准的
		{
			Rect r = new Rect();
			v.getWindowVisibleDisplayFrame(r);	//
			int h = r.height();
			Tools.debug("screenHeight:" + Tools.screenHeight + ",WindowVisibleDisplayFrame:" + r.height());
			xy[1] -= (Tools.screenHeight - r.height());
		}
		
		Toast toast = Toast.makeText(context, info, time);
		int x = 0;
		int y = 0;
		switch(gravity)
		{
			case Gravity.LEFT:
				x = xy[0] - mybeToastWidth;
				y = xy[1];
				break;
				
			case Gravity.RIGHT:
				x = xy[0] + v.getWidth();
				y = xy[1];
				break;
				
			case Gravity.TOP:
				x = xy[0] + (v.getWidth() - dip2px(context, mybeToastWidth)) / 2;
				y = xy[1] - dip2px(context, 45);	//因为toast的高度都是固定，这里写死40dip
				break;
				
			case Gravity.BOTTOM:
				x = xy[0] + (v.getWidth() - dip2px(context, mybeToastWidth)) / 2;
				y = xy[1] + v.getHeight();
				break;
		}
		
		toast.setGravity(Gravity.LEFT | Gravity.TOP, x, y);
		
		toast.show();
    }
    
    /**
     * description 简单的Toast显示
     * @param context 上下文
     * @param gravity 布局，Gravity.CENTER，Gravity.TOP，......
     * @param info 显示内容
     * @param time 持续时间，只能是Toast.LENGTH_SHORT 和 Toast.LENGTH_LONG
     */
    public static void showSimpleToast(Context context, int gravity, String info, int time)
    {
    	if(context == null || info == null || info.equals("")) {
    		return;
    	}
    	if(toast == null) {
    		View toastRoot = MyArStation.mGameMain.getLayoutInflater().inflate(R.layout.new_my_toast, null);
    		toast =  new Toast(context);
    		toast.setView(toastRoot);
    		TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
    		tv.setText(info);
    		toast.setDuration(time);
    	}
    	TextView tv=(TextView)toast.getView().findViewById(R.id.TextViewInfo);
		tv.setText(info);
    	toast.setDuration(time);
    	toast.setGravity(gravity, 0, 0);
    	toast.show();
    }
    
    public static void showSimpleToast(Context context,String info) {
    	showSimpleToast(context, Gravity.CENTER, info, Toast.LENGTH_SHORT);
    }

}
