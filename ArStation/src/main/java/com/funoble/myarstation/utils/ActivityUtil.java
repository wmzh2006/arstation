package com.funoble.myarstation.utils;

import java.util.ArrayList;
import java.util.List;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.common.Util;
import com.funoble.myarstation.game.MyArStation;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

public final class ActivityUtil {
	public static final boolean ibTEST = false;
	
	public static int PIC_BG_W_STANDARD = 100;//800x480屏，图片底框 宽，首页、个人资料界面用
	public static int PIC_BG_H_STANDARD = 100;
	public static int PIC_BG_X_STANDARD = 348;
	public static int PIC_BG_Y_STANDARD = 195;
	
	public static int NICK_BG_W_STANDARD = 268;//800x480屏，昵称底框 宽，首页、个人资料界面用
	public static int NICK_BG_H_STANDARD = 32;
	public static int NICK_BG_X_STANDARD = 476;
	public static int NICK_BG_Y_STANDARD = 212;
	
	public static int VIP_BG_X_STANDARD = 559;
	public static int VIP_BG_Y_STANDARD = 255;
	
	public static int HART_BG_X_STANDARD = 714;
	public static int HART_BG_Y_STANDARD = 255;
	
	public static int FB_BG_X_STANDARD = 559;
	public static int FB_BG_Y_STANDARD = 297;
	
	public static int GB_BG_X_STANDARD = 714;
	public static int GB_BG_Y_STANDARD = 297;
	
	public static int LEVEL_BG_W_STANDARD = 290;//800x480屏，昵称底框 宽，首页、个人资料界面用
	public static int LEVEL_BG_H_STANDARD = 28;
	public static int LEVEL_BG_X_STANDARD = 619;
	public static int LEVEL_BG_Y_STANDARD = 339;
	public static int EXP_BG_X_STANDARD = 478;
	public static int EXP_BG_Y_STANDARD = 317;
	
	public static int ACTIVITY_INF_BG_X_STANDARD = 247;
	public static int ACTIVITY_INF_BG_Y_STANDARD = 49;
	public static int ACTIVITY_INF_TEXT_Y_STANDARD = 55;
	
	public static int INFO_PIC_BG_W_STANDARD = 196;//800x480屏，图片底框 宽，首页、个人资料界面用
	public static int INFO_PIC_BG_H_STANDARD = 196;
	public static int INFO_PIC_BG_X_STANDARD = 40;
	public static int INFO_PIC_BG_Y_STANDARD = 30;
	
	public static int INFO_CAR_BG_W_STANDARD = 188;//800x480屏，图片底框 宽，首页、个人资料界面用
	public static int INFO_CAR_BG_H_STANDARD = 188;
	public static int INFO_CAR_BG_X_STANDARD = 378;
	public static int INFO_CAR_BG_Y_STANDARD = 250;
	
	public static float PAK_ZOOM_X = 1f;//(float)800/(float)480;//pak的缩放率 X方向
	public static float PAK_ZOOM_Y = 1f;//(float)480/(float)320;
	
	public static final float DENSITY_STANDARD = 1;
	public static final int DPI_STANDARD = 160;
	public static final float DENSITY_ZOOM_X_STANDARD = (float)3;//由基准屏800x480，dpi=240，480/160所得
	public static final float DENSITY_ZOOM_Y_STANDARD = (float)2;//由基准屏480x320，dpi=240，320/160所得
	public static float DENSITY_ZOOM_X = 1;//密度、dpi、分辨率，引起的缩放率 X方向，专用于系统控件位置调整，以density=1.0，dpi=160为基准
	public static float DENSITY_ZOOM_Y = 1;
	
	public static final int TYPE_SCREEN_WVGA800 = 0;
	public static final int TYPE_SCREEN_HVGA = 1;
	public static int TYPE_SCREEN = TYPE_SCREEN_HVGA;
	public static String PATH_SCREEN_WVGA800 = "wvga800/";//屏幕路径
	public static String PATH_SCREEN_HVGA = "hvga/";//屏幕路径
	public static String PATH_SCREEN = PATH_SCREEN_HVGA;//屏幕路径
	public static int SCREEN_WIDTH_STANDARD = 800;//标准屏
	public static int SCREEN_HEIGHT_STANDARD = 480;//标准屏
//	public static int SCREEN_WIDTH_HVGA = 480;//低屏
//	public static int SCREEN_HEIGHT_HVGA = 320;//
	public static float ZOOM_X = 1;//X方向 缩放率
	public static float ZOOM_Y = 1;//Y方向 缩放率
//	public static int TEXTSIZE_SMALL = 10;
	public static float TEXTSIZE_NICK = 24;
	public static float TEXTSIZE_SMALL = 17;
//	public static float TEXTSIZE_NORMAL = 13;
	public static float TEXTSIZE_NORMAL = 22;
//	public static float TEXTSIZE_BIG = 20;
	public static float TEXTSIZE_BIG = 32;
	public static float TEXTSIZE_HUGEA = 33;
	public static float TEXTSIZE_HUGEB = 36;
	public static float TEXTSIZE_HUGEC = 54;
	public static float TEXTSIZE_RESULT = 33;
	public static float TEXTSIZE_SHOUT_DICE = 18;
	public static int SCREEN_WIDTH = 0;  //屏幕宽
	public static int SCREEN_HEIGHT = 0; //屏幕高
	public static int DENSITYDPI = 0; //密度
	
	//屏幕各个动画播放点的坐标
	public static int SCREEN_POS_X[] = {0,400,800,0,400,800,0,400,800};
	public static int SCREEN_POS_Y[] = {0,0,0,240,240,240,480,480,480};
	
	public static final Paint mPaint = new Paint();  //画笔
	public static final Paint mNamePaint = new Paint();  //名字 画笔
	public static final Paint mVipPaint = new Paint();  //VIP 画笔
	public static final Paint mNoVipPaint = new Paint();  //VIP 画笔
	public static final Paint mHartPaint = new Paint();  //VIP 画笔
	public static final Paint mCallPaint = new Paint();  //喊 画笔
	public static final Paint mNormalNickPaint = new Paint();//昵称 画笔
	public static final Paint mVipNickPaint = new Paint();  //VIP 昵称 画笔
	public static final Paint mLoading	= new Paint(); //载入数据
	
	//详细资料界面---------------
	public static final Paint mDetailNickPaint = new Paint(); //详细资料 昵称 画笔
	public static final Paint mDetailNickVipPaint = new Paint(); //详细资料 昵称 画笔
	public static final Paint mDetailNumPaint = new Paint();//数字画笔
	public static final Paint mDetailTextPaint = new Paint();//文字画笔
	public static final Paint mDetailLevelPaint = new Paint();//级别文字
	public static final Paint mDetailBack = new Paint();
	//详细资料界面---------------
	
	public static final Paint mDicePaint = new Paint();  //骰子 画笔
	public static final Paint mMoneyPaint = new Paint();  //钱 画笔
	public static final Paint mBeatDownPaint = new Paint();  //灌倒人数 画笔
	public static final Paint mGreenBeatDownPaint = new Paint();  //绿色灌倒人数 画笔
	public static final Paint mRedBeatDownPaint = new Paint();  //红色灌倒人数 画笔
	public static final Paint mPaint_NORMAL_WHITE_CENTER = new Paint();  //中Size 白色 居中 画笔
	public static final Paint mAutoBuyBeerPaint = new Paint();  //自动买酒 画笔
	public static final Paint mChatPaint = new Paint();  //对话框画笔
	public static final Paint mPlayerCallPaint = new Paint();  //玩家头上的喊点 画笔
	public static final Paint mCallNamePaint = new Paint();  //中间喊点框 名字 画笔
	public static final Paint mTitlePaint = new Paint();  //职称 画笔
	public static final Paint mRectPaint = new Paint();	//矩形画笔
	public static final Paint mSelectRolePaint = new Paint();	//选择角色
	public static final Paint mLeaveMsgPaint = new Paint();	//留言条数
	public static final Paint mWwwwPaint = new Paint();  //职称 画笔
	
	public static final Paint mPlayerName = new Paint(); //桌子上面的玩家名字
	public static final Paint mVipPlayerName = new Paint(); //桌子上面的VIP玩家名字
	public static final Paint mDrunk = new Paint(); //桌子上面的玩家的醉酒度
	public static final Paint mAlphaRect = new Paint();//桌子上面的玩家的醉酒度的半透明矩形
	public static final Paint mDice = new Paint(); //桌子上面的玩家喊出来的骰子个数
	public static final Paint mResultCount = new Paint(); //桌子上面的结果
	public static final Paint mResultGb = new Paint();	//游戏中一局结果显示获得的金币
	public static final Paint mRoomName = new Paint();	//游戏中的房间名称
	public static final Paint mWheelBigNum = new Paint();	//游戏中滚轮选中的数字
	public static final Paint mWheelNum = new Paint();	//游戏中滚轮的数字
	public static final Paint mChat = new Paint(); //桌子上面的聊天泡泡内容
//	public static final Paint mWheelAlphaRect = new Paint();//滚轮的半透明矩形
	public static final Paint mAlphaLine = new Paint(); //雷电的线条
	public static final Paint mAlphaLineB = new Paint(); //雷电的线条
	public static final Paint mAlphaLineC = new Paint(); //雷电的粒子
	
	public static Resources resources = null;
	public static Context mContext = null;
	
//	public static List<String> iMsgList = new ArrayList<String>();//公告列表
//	public static ArrayAdapter<String> iMsgListAdapter = new ArrayAdapter<String>
//	(GameMain.getInstance().getApplicationContext(), android.R.layout.simple_list_item_1,iMsgList);//公告适配器
	
	//易宝支付
	//test
//	public static final String ENVIRONMENT = "ENV_TEST";
//	public static final String CUSTOMER_NUMBER = "10040011726";
//	public static final String KEY = "3j5817R4Uq618f2r9j2R427kq7MI7g94i54vUFmMhnki44s123X93rX3d34Q";
	
	public static final String ENVIRONMENT = "ENV_LIVE";
	public static final String CUSTOMER_NUMBER = "10011822339";
	public static final String KEY = "68759S0BGKj34348Jex10VW716I7888HHQhoy27nY5Fv0Z0sxYTT715985i8";
	
	public static void setDefaultPaint(){
		mPaint.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mPaint.setShadowLayer(0.5f, 1f, 1f, Color.BLACK); 
	}
	
	public static void setNamePaint(){
		mNamePaint.setTextSize(TEXTSIZE_NICK);     //设置字体大小
		mNamePaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mNamePaint.setColor(Color.WHITE);
		mVipPaint.setTextSize(TEXTSIZE_NICK);     //设置字体大小
		mVipPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mVipPaint.setColor(Color.YELLOW);
		mVipPaint.setTextAlign(Align.CENTER);
		mNoVipPaint.setTextSize(TEXTSIZE_NICK);     //设置字体大小
		mNoVipPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mNoVipPaint.setColor(Color.GRAY);
		mNoVipPaint.setTextAlign(Align.CENTER);
		mHartPaint.setTextSize(TEXTSIZE_NICK);     //设置字体大小
		mHartPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mHartPaint.setColor(Color.WHITE);
		mHartPaint.setTextAlign(Align.CENTER);
		mTitlePaint.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mTitlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mTitlePaint.setColor(Color.WHITE);
		mTitlePaint.setTextAlign(Align.CENTER); 	//水平居中
		mRectPaint.setColor(Color.BLUE);
		mCallPaint.setTextSize(TEXTSIZE_BIG);     //设置字体大小
		mCallPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mCallPaint.setColor(Color.WHITE);
		
		mNormalNickPaint.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mNormalNickPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mNormalNickPaint.setColor(Color.WHITE);
		mNormalNickPaint.setTextAlign(Align.CENTER);
		mNormalNickPaint.setFakeBoldText(true);
		
		mVipNickPaint.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mVipNickPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mVipNickPaint.setColor(Color.YELLOW);
		mVipNickPaint.setTextAlign(Align.CENTER);
		mVipNickPaint.setFakeBoldText(true);
		
		mLoading.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mLoading.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mLoading.setColor(Color.WHITE);
		
		mDetailNickPaint.setTextSize(TEXTSIZE_BIG);     //设置字体大小
		mDetailNickPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mDetailNickPaint.setColor(Color.WHITE);
		mDetailNickPaint.setTextAlign(Align.LEFT);
		mDetailNickPaint.setFakeBoldText(true);
		
		mDetailNickVipPaint.setTextSize(TEXTSIZE_BIG);     //设置字体大小
		mDetailNickVipPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mDetailNickVipPaint.setColor(Color.YELLOW);
		mDetailNickVipPaint.setTextAlign(Align.LEFT);
		mDetailNickVipPaint.setFakeBoldText(true);
		
		mDetailNumPaint.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mDetailNumPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mDetailNumPaint.setColor(Color.WHITE);
		mDetailNumPaint.setTextAlign(Align.LEFT);
		mDetailNumPaint.setFakeBoldText(true);
		
		mDetailTextPaint.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mDetailTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mDetailTextPaint.setColor(Color.WHITE);
		mDetailTextPaint.setTextAlign(Align.CENTER);
		mDetailTextPaint.setFakeBoldText(true);
		
		mDetailLevelPaint.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mDetailLevelPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mDetailLevelPaint.setColor(Color.rgb(21, 91, 0));
		mDetailLevelPaint.setTextAlign(Align.CENTER);
		mDetailLevelPaint.setFakeBoldText(true);
		
		mDetailBack.setColor(Color.BLACK);
		mDetailBack.setAlpha(150);
		
		mDicePaint.setTextSize(TEXTSIZE_BIG);     //设置字体大小
		mDicePaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mDicePaint.setColor(Color.RED);
		mDicePaint.setStyle(Style.STROKE);
		mMoneyPaint.setTextSize(TEXTSIZE_SMALL);     //设置字体大小
		mMoneyPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mMoneyPaint.setColor(Color.WHITE);
		mMoneyPaint.setTextAlign(Align.CENTER); 	//水平居中
//		mMoneyPaint.setColor(Color.CYAN);
		mBeatDownPaint.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mBeatDownPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mBeatDownPaint.setColor(Color.WHITE);
		mGreenBeatDownPaint.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mGreenBeatDownPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mGreenBeatDownPaint.setColor(Color.GREEN);
		mRedBeatDownPaint.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mRedBeatDownPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mRedBeatDownPaint.setColor(Color.RED);
		mPaint_NORMAL_WHITE_CENTER.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mPaint_NORMAL_WHITE_CENTER.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mPaint_NORMAL_WHITE_CENTER.setColor(Color.WHITE);
		mPaint_NORMAL_WHITE_CENTER.setTextAlign(Align.CENTER);
		mAutoBuyBeerPaint.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mAutoBuyBeerPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
//		mBeatDownPaint.setColor(Color.MAGENTA);
		mChatPaint.setTextSize(TEXTSIZE_SMALL);     //设置字体大小
		mChatPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mPlayerCallPaint.setTextSize(TEXTSIZE_BIG);     //设置字体大小
		mPlayerCallPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mCallNamePaint.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mCallNamePaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		
		mPlayerName.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mPlayerName.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mPlayerName.setColor(Color.WHITE);
		mPlayerName.setTextAlign(Align.CENTER);
		mPlayerName.setShadowLayer(0.5f, 1f, 1f, Color.BLACK); 
		
//		LinearGradient lgGradient = new LinearGradient(0, 0, 10, 10, Color.YELLOW, Color.GREEN, TileMode.MIRROR);
		mVipPlayerName.setTextSize(TEXTSIZE_NORMAL);     	//设置字体大小
		mVipPlayerName.setFlags(Paint.ANTI_ALIAS_FLAG);		//设置字体样式
		mVipPlayerName.setColor(Color.YELLOW);
		mVipPlayerName.setTextAlign(Align.CENTER);
		mVipPlayerName.setShadowLayer(0.5f, 1f, 1f, Color.BLACK); 
//		mVipPlayerName.setShader(lgGradient);
//		lgGradient = null;
		
		mWwwwPaint.setTextSize(TEXTSIZE_NORMAL);     	//设置字体大小
		mWwwwPaint.setFlags(Paint.ANTI_ALIAS_FLAG);		//设置字体样式
		mWwwwPaint.setColor(Color.WHITE);
		mWwwwPaint.setTextAlign(Align.CENTER);
		mWwwwPaint.setShadowLayer(0.5f, 1f, 1f, Color.BLACK); 
		
		mDrunk.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mDrunk.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mDrunk.setColor(Color.GREEN);
		mDrunk.setTextSkewX(45f);
		mDrunk.setShadowLayer(0.5f, 1f, 1f, Color.BLACK); 
		
		mAlphaRect.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mAlphaRect.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mAlphaRect.setColor(Color.GRAY); 
		mAlphaRect.setAlpha(150);
		
		mDice.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mDice.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mDice.setColor(Color.GREEN);
		mDice.setTextAlign(Align.CENTER);
		mDice.setShadowLayer(0.5f, 1f, 1f, Color.BLACK);
		
		mResultCount.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mResultCount.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mResultCount.setColor(Color.RED);
		mResultCount.setTextAlign(Align.CENTER);
//		mResultCount.setTextSkewX(1f);
		mResultCount.setShadowLayer(0.5f, 1f, 1f, Color.BLACK);
		
		mResultGb.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mResultGb.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mResultGb.setColor(Color.YELLOW);
		mResultGb.setTextAlign(Align.CENTER);
		mResultGb.setShadowLayer(0.5f, 1f, 1f, Color.BLACK);
		
		mRoomName.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mRoomName.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mRoomName.setColor(Color.BLACK);
		mRoomName.setTextAlign(Align.CENTER);
		mRoomName.setShadowLayer(0.5f, 1f, 1f, Color.GRAY);
		
		 //游戏中滚轮选中的数字
		if(DENSITYDPI >= 240) {
			mWheelBigNum.setTextSize(TEXTSIZE_HUGEC);     //设置字体大小
//			System.out.println("HugeC DENSITYDPI=" + DENSITYDPI);
		}
		else if(DENSITYDPI >= 160) {
			mWheelBigNum.setTextSize(TEXTSIZE_HUGEB);     //设置字体大小
//			System.out.println("HugeB DENSITYDPI=" + DENSITYDPI);
		}
		else {
			mWheelBigNum.setTextSize(TEXTSIZE_HUGEA);     //设置字体大小
//			System.out.println("HugeA DENSITYDPI=" + DENSITYDPI);
		}
		 mWheelBigNum.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		 mWheelBigNum.setColor(Color.BLACK);
		 mWheelBigNum.setTextAlign(Align.CENTER);
		 mWheelBigNum.setShadowLayer(0.5f, 1f, 1f, Color.GRAY);
		
		 //游戏中滚轮的数字
		mWheelNum.setTextSize(TEXTSIZE_BIG);     //设置字体大小
		mWheelNum.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mWheelNum.setColor(Color.BLACK);
		mWheelNum.setTextAlign(Align.CENTER);
		mWheelNum.setShadowLayer(0.5f, 1f, 1f, Color.GRAY);
		
		//聊天泡泡的文字
		mChat.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mChat.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mChat.setColor(Color.BLACK);
		mChat.setShadowLayer(0.5f, 1f, 1f, Color.GRAY); 
		
		//选择角色的文字
		mSelectRolePaint.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mSelectRolePaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mSelectRolePaint.setColor(Color.WHITE);
		mSelectRolePaint.setTextAlign(Align.LEFT);
		mSelectRolePaint.setShadowLayer(0.5f, 1f, 1f, Color.GRAY); 
		
		//新留言的文字
		mLeaveMsgPaint.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
		mLeaveMsgPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mLeaveMsgPaint.setColor(Color.WHITE);
		mLeaveMsgPaint.setTextAlign(Align.RIGHT);
		mLeaveMsgPaint.setShadowLayer(0.5f, 1f, 1f, Color.GRAY); 
		
		//滚轮的半透明rect
//		mWheelAlphaRect.setTextSize(TEXTSIZE_NORMAL);     //设置字体大小
//		mWheelAlphaRect.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
//		mWheelAlphaRect.setColor(Color.BLACK); 
//		mWheelAlphaRect.setAlpha(150);
		
		mAlphaLine.setStrokeWidth(10f);     //设置字体大小
		mAlphaLine.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mAlphaLine.setColor(Color.BLUE); 
		mAlphaLine.setAlpha(150);
		//mAlphaLine.setShadowLayer(0.5f, 1f, 1f, Color.GRAY); 
		
		mAlphaLineB.setStrokeWidth(4f);     //设置字体大小
		mAlphaLineB.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mAlphaLineB.setColor(Color.WHITE); 
		//mAlphaLine.setAlpha(150);
		
		mAlphaLineC.setStrokeWidth(10f);     //设置字体大小
		mAlphaLineC.setFlags(Paint.ANTI_ALIAS_FLAG);// 设置字体样式
		mAlphaLineC.setColor(Color.RED); 
		mAlphaLineC.setAlpha(150);
	}
	
	public static void setContext(Context context) {
		mContext = context;
	}
	
	public static void setResources(Activity activity){
		resources = activity.getResources();
	}

	public static void setNoTitleBar(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	public static void setFullScreenMode(Activity activity){
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public static void getDisplay(Activity activity){
		Window window = activity.getWindow();
		WindowManager windowManager = window.getWindowManager();
		Display display =  windowManager.getDefaultDisplay();
		DisplayMetrics metric = new DisplayMetrics();
		display.getMetrics(metric);
		int screenW = metric.widthPixels;  // 屏幕宽度（像素）
        int screenH = metric.heightPixels;  // 屏幕高度（像素）
		float density = metric.density;
		DENSITYDPI = metric.densityDpi;
		float xdpi = metric.xdpi;
		float ydpi = metric.ydpi;
		
//		if(screenW % 2 != 0 || screenH % 2 != 0) {
//			screenW  = (int)(metric.widthPixels * density + 0.5f);      // 屏幕宽（px，如：480px）  
//			screenH = (int)(metric.heightPixels * density + 0.5f);     // 屏幕高（px，如：800px）  
//		}
		ActivityUtil.SCREEN_WIDTH = screenW > screenH ? screenW : screenH;
		ActivityUtil.SCREEN_HEIGHT = screenH < screenW ? screenH : screenW;
		
		TYPE_SCREEN = TYPE_SCREEN_WVGA800;
		PATH_SCREEN = PATH_SCREEN_WVGA800;
		PAK_ZOOM_X = (float)SCREEN_WIDTH/(float)SCREEN_WIDTH_STANDARD;
		PAK_ZOOM_Y = (float)SCREEN_HEIGHT/(float)SCREEN_HEIGHT_STANDARD;
		
		ZOOM_X = (float)SCREEN_WIDTH/(float)SCREEN_WIDTH_STANDARD;
		ZOOM_Y = (float)SCREEN_HEIGHT/(float)SCREEN_HEIGHT_STANDARD;
		TEXTSIZE_NICK = TEXTSIZE_NICK * ZOOM_X;
		TEXTSIZE_SMALL = TEXTSIZE_SMALL * ZOOM_X;
		TEXTSIZE_NORMAL = TEXTSIZE_NORMAL * ZOOM_X;
		TEXTSIZE_BIG = TEXTSIZE_BIG * ZOOM_X;
		Tools.debug("screenW:" + screenW
				+"\nscreenH:" + screenH
				+"\ndensity:" + density
				+"\ndensityDpi:" + DENSITYDPI
				+"\nxdpi:" + xdpi
				+"\nydpi:" + ydpi
				+"\nwidthPixels:" + metric.widthPixels
				+"\nheightPixels:" + metric.heightPixels);
		
		//屏幕各个动画播放点的坐标
		for(int i=0; i<9; i++) {
			SCREEN_POS_X[i] = (int)(SCREEN_POS_X[i] * ZOOM_X);
			SCREEN_POS_Y[i] = (int)(SCREEN_POS_Y[i] * ZOOM_Y);
		}
	}
	
	//设置屏幕：横屏
	public static void setScreenOrientation(Activity activity){
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	public static void setKeepScreenOn(Activity activity) {
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	/**
	 * make true current connect service is wifi
	 * @param mContext
	 * @return
	 */
	public static boolean isWifi() {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}
}
