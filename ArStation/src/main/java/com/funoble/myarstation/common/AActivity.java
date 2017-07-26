/*******************************************************************************
 * Copyright (C) 1998-2009 BBG Inc.All Rights Reserved. FileName: AActivity.java
 * Description： History： 版本号 作者 日期 简要介绍相关操作 1.0 sunny 2009-11-9 Create
 *******************************************************************************/
package  com.funoble.myarstation.common;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.protocol.Communicate;
import com.funoble.myarstation.socket.protocol.HandShake;

/**
 * <p>
 * activity基类, 实现公共功能
 * </p>
 */
public abstract class AActivity extends Activity
{
    /**
     * action, 用于intent跳入
     */
    public static final String ACTION = "unkunow";
    /**
     * 商品搜索
     */
    public static final int ACTION_SEARCH_ITEMS = 1;

    /**
     * 商品详情
     */
    public static final int ACTION_ITEM_DETAIL = 2;

    /**
     * 抓取商品图片
     */
    public static final int ACTION_ITEM_IMAGE = 3;
    
    /**
     * 握手
     */
    public static final int ACTION_HANDSHAKE = 4;



    /**
     * 轮询
     */
    public static final int MSG_GLOABLE_SLEEP = -100;
    /**
     * 获取协议消息失败
     */
    public static final int MSG_GET_SERVER_MSG_ERROR = -101;
    /**
     * 解析协议消息
     */
    public static final int MSG_PARSE_SERVER_MSG = -102;
    /**
     * 从服务器获取图片
     */
    public static final int MSG_GET_IMAGE_FROM_URL = -103;
    /**
     * 服务器下发的出错提示
     */
    public static final int MSG_SERVER_ERROR = -104;
    /**
     * 握手应答消息
     */
    public static final int MSG_GET_HANDSHAKE = -105;


    /**
     * tag
     */
    protected final String TAG;
    /**
     * 固定竖屏
     */
    public static final byte SCREEN_ORIENTATION_PORTRAIT = 1;
    /**
     * 固定横屏
     */
    public static final byte SCREEN_ORIENTATION_LANDSCAPE = 2;
    /**
     * 自动切换屏幕
     */
    public static final byte SCREEN_ORIENTATION_AUTO = 3;
    /**
     * 屏幕模式
     */
    protected byte screen_type = SCREEN_ORIENTATION_AUTO;

    /**
     * 是否正在执行退出应用。 变态的需求用变态的方法解决
     */
    public static boolean isExitingApp = false;
    /**
     * 系统当前显示的Activity实例
     */
    public static AActivity currentActivity = null;
    /**
     * res
     */
    public static Resources res = null;
    /**
     * 
     */
    public Handler handle = null;

    /**
     * 同步帧MPF Min Seconds per Frame
     */
    protected final short MPF = 1000;
    /**
     * 
     */
    private final int MAX_STEP = 10000;
    /**
     * step
     */
    protected int step = 0;

    /**
     * 当前正在显示的对话框ID, -1表示当前没有任何弹出对话框
     */
    public int curr_dialog_key = -1;
    /**
     * 普通进度条对话框ID， 按了取消需要退出到登录界面
     */
    public static final int DIALOG_NORMAL_PROGRESS = 0;
    /**
     * 界面退出foregroud时不需要清除的对话框ID
     */
    public static final int DIALOG_NOT_NEED_CLEAR = 1;

    /**
     * 进度条对话框显示内容
     */
    protected String progressDlgMsg = "";


//    protected static FactoryCenter factory;

    /**
     * 
     */
    public AActivity()
    {
        TAG = this.getClass().getName();

        handle = new Handler()
        {
            @Override
            public void handleMessage(Message message)
            {
                AActivity.this.doHandleMessage(message);
            }
        };
    }

    /**
     * 顶层处理消息，再传给下面
     * 
     * @param message
     */
    private final void doHandleMessage(Message message)
    {
        step = step > MAX_STEP ? 0 : step + 1;
        boolean isNeedChildDeal = true;
        switch (message.what)
        {
            case MSG_PARSE_SERVER_MSG: // 解析服务器消息

                Communicate p = (Communicate) message.obj;
                if(p == null)
                {
                    Tools.debug("AActivity doHandleMessage, Communicate is null");
                    return;
                }
//                factory.getReceiveMessageHandle().parseMsg((JSONObject) message.obj, message.getData().getInt("Action"));
                break;

            case MSG_GET_SERVER_MSG_ERROR: // 获取协议消息失败

                break;
                
            case MSG_GET_HANDSHAKE: //握手应答
                
                checkVerion((HandShake)message.obj);
                break;
        }

        if(isNeedChildDeal)
        {
            handleMessage(message);
        }
    }

    /**
     * 消息处理(由子类实现)
     * 
     * @param message
     */
    protected abstract void handleMessage(Message message);

    /**
     * Invoked when the Activity is created.
     * 
     * @param savedInstanceState
     *            a Bundle containing state saved from a previous execution, or
     *            null if this is a new execution
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.w(TAG, "onCreate..");

        super.onCreate(savedInstanceState);

        if(res == null)
        {
            res = this.getResources();
        }
//        if(factory == null)
//        {
//            factory = new FactoryCenter();
//        }

        if(screen_type == SCREEN_ORIENTATION_PORTRAIT)
        {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 固定直屏
        }
        else if(screen_type == SCREEN_ORIENTATION_LANDSCAPE)
        {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 固定横屏
        }

        /* 全屏显示 */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 不全屏， 显示电池栏
        // Window win = getWindow();
        // win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Notification that something is about to happen, to give the Activity a
     * chance to save state. 意外情况发生时调用，以保存当前用户状态
     * 
     * @param outState
     *            a Bundle into which this Activity should save its state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        Log.w(TAG, "SIS called");
    }

    @Override
    protected void onDestroy()
    {
        Log.w(TAG, "onDestroy..");
        super.onDestroy();
    }

    @Override
    protected void onRestart()
    {
        Log.w(TAG, "onRestart..");
        super.onRestart();
    }

    @Override
    protected void onResume()
    {
        Log.w(TAG, "onResume..");
        Log.i(TAG, "isTaskRoot:" + this.isTaskRoot());
        Log.i(TAG, "isExitingApp:" + isExitingApp);
        if(isExitingApp)
        {
            // 强制关闭
//            if(this instanceof MainActivity)
//            {
//                android.os.Process.killProcess(android.os.Process.myPid());
//            }
//            else
            {
                finish();
            }
        }
        currentActivity = this;

        Display dis = this.getWindowManager().getDefaultDisplay();
        /* 屏幕尺寸改变则重新初始化相关参数 */
        if(dis.getWidth() != Tools.screenWidth || dis.getHeight() != Tools.screenHeight)
        {
            Tools.setScreenSize(dis.getWidth(), dis.getHeight());
            // factory.notifyAllSceneChange();
        }
        // 清除对话框
        if(curr_dialog_key != -1)
        {
            removeDialog(curr_dialog_key);
            curr_dialog_key = -1;
        }

        super.onResume();
    }

    @Override
    protected void onStart()
    {
        Log.w(TAG, "onStart..");
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        Log.w(TAG, "onStop..");
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Invoked when the user selects an item from the Menu.
     * 
     * @param item
     *            the Menu entry which was selected
     * @return true if the Menu item was legit (and we consumed it), false
     *         otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        }

        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        Dialog dialog = null;
        switch (id)
        {
            case DIALOG_NORMAL_PROGRESS:

                ProgressDialog pdialog = new ProgressDialog(this);
                dialog = pdialog;
                pdialog.setMessage(progressDlgMsg);
                pdialog.setIndeterminate(true);
                pdialog.setCancelable(true);
        }

        curr_dialog_key = id;
        return dialog;
    }

    /**
     * 
     */
    public void removeDialog()
    {
        if(curr_dialog_key >= 0)
        {
            removeDialog(curr_dialog_key);
            curr_dialog_key = -1;
        }
    }

    /**
     * 显示进度框
     * 
     * @param id
     * @param msg
     */
    public void showDialog(int id, String msg)
    {
        progressDlgMsg = msg;
        showDialog(id);
    }

    /**
     * 显示软键盘
     * 
     * @param view
     */
    protected static void showSoftKeyBoard(View view)
    {
        if(view == null)
        {
            return;
        }
        try
        {
            view.requestFocus();
            InputMethodManager imm = (InputMethodManager) AActivity.currentActivity
                    .getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
        catch(Exception e)
        {
        }
    }

    /**
     * 隐藏软键盘
     * 
     * @param view
     */
    protected static void hideSoftKeyBoard(View view)
    {
        if(view == null)
        {
            return;
        }
        try
        {
            InputMethodManager imm = (InputMethodManager) AActivity.currentActivity
                    .getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 隐藏输入法
        }
        catch(Exception e)
        {
        }
    }

    /**
     * 返回主界面
     */
    public static void back2MainActivity()
    {
        AActivity.currentActivity.startActivity(new Intent(AActivity.currentActivity, MyArStation.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    /*
     * 取消进度条 (non-Javadoc)
     * @see
     * android.content.DialogInterface.OnCancelListener#onCancel(android.content
     * .DialogInterface)
     */
    public void onCancel(DialogInterface dialog)
    {

    }
    
    /**
     * 根据握手应答检测版本信息
     * @param hand
     */
    private final static void checkVerion(final HandShake hand)
    {
        if(hand != null)
        {
            if(hand.isNeedUpdate && hand.nextVer > 0)   //需要更新
            {
                
//                new AlertDialog.Builder(AActivity.currentActivity)
//                .setIcon(R.drawable.alert_dialog_icon)
//                .setTitle(R.string.tip_title)
//                .setMessage(Util.replace(new StringBuffer(res.getString(R.string.needupdate)), "{0}", 
//                        hand.nextVerName != null ? hand.nextVerName : ""))
//                .setCancelable(false)
//                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() 
//                {
//                  public void onClick(DialogInterface dialog, int whichButton) 
//                  {
//                      AActivity.updateNewVersion(hand.url);
//                  }
//                }).show();
            }
        }
    }
    
    /**
     * 更新版本
     * @param url
     */
    private final static void updateNewVersion(String url)
    {
        Tools.debug("update version. url:" + url);
//        Uri uri = Uri.parse("http://google.com");   
        Uri uri = Uri.parse(url);   
        Intent it = new Intent(Intent.ACTION_VIEW, uri);   
        AActivity.currentActivity.startActivity(it);  
    }


}
