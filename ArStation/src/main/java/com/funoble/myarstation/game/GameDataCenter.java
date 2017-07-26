package com.funoble.myarstation.game;
//package com.funoble.lyingdice.game;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Vector;
//
//import org.apache.http.client.CookieStore;
//import org.apache.http.cookie.Cookie;
//
//import android.content.Context;
//import android.os.Handler;
//
////import com.funoble.lyingdice.communication.GameProtocol;
//import com.funoble.lyingdice.protocol.ReceiveMessageHandle;
//import com.funoble.lyingdice.protocol.SendMessageHandle;
//
///**
// * 作用:
// * <p>
// * </p>
// */
//public class GameDataCenter
//{
//	
//	
//	
//	/**
//	 * 解析服务器消息出错
//	 */
//	public static final int	MSG_PARSE_MSG_ERROR	= -100;
//	private CookieStore		cookiestore;
//	
//	public CookieStore getCookiestore()
//	{
//		return cookiestore;
//	}
//	public class MyCookieStore implements CookieStore{
//		 List<Cookie> cookies=new ArrayList<Cookie>();
//		@Override
//		public void addCookie(Cookie cookie) {
//			// TODO Auto-generated method stub
//			cookies.add(cookie);
//		}
//
//		@Override
//		public List<Cookie> getCookies() {
//			// TODO Auto-generated method stub
//			return cookies;
//		}
//
//		@Override
//		public boolean clearExpired(Date date) {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public void clear() {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
//  public void removeALLCookiestore(){
//	  if(this.cookiestore!=null){
//		  cookiestore.clear();
//		 this.cookiestore=new MyCookieStore();
//	  }
//  }
//	public void setCookiestore(CookieStore cookiestore)
//	{
//		this.cookiestore = cookiestore;
//	}
//
//	/**
//	 * 通讯出错
//	 */
//	public static final int			MSG_HTTP_ERROR				= -101;
//
//	public static final int			MSG_PARSE_SERVER_MSG		= -102;
//	/**
//	 * 从服务器获取图片
//	 */
//	public static final int			MSG_GET_IMAGE_FROM_URL		= -103;
//	/**
//	 * 服务器下发的出错提示
//	 */
//	public static final int			MSG_SERVER_ERROR			= -104;
//	/**
//	 * 获取位置信息成功
//	 */
//	public static final int			MSG_GET_LOCATION_SUCC		= -105;
//	/**
//	 * 定位失败
//	 */
//	public static final int			MSG_GET_LOCATION_ERROR		= -106;
//
//	/**
//	 * 请求从google地图服务根据经纬度反向解析为地址
//	 */
//	public static final int			ACTION_GET_GEO_INFO			= -1010;
//	
//	/**
//	 * 请求图片
//	 */
//	public static final int			ACTION_GET_IMAGE			= -1000;
//	
//	/**
//	 * 获取SKEY
//	 */
//	public static final int			ACTION_GET_SKEY				= -1016;
//	public static final int        ACTION_GET_SEND_SKEY			=-1017;
////	private List<Store>				mPlayStoreList				= null;
////	/** 最新热词 */
////	private Vector<String>			mVectorHotWord				= null;
////
////	/**
////	 * 工具栏实例， 暂时放这
////	 */
////	private ToolBar					mToolBar;
////
//	private Handler					mHandler;
////
////	private ITOViewFlipper			mMainFlipper;
////
////	private MyLoactionHelper		mMyLoactionHelper;
////
////	private LoginHelper				mLoginHelper;
//	private Context					mContext;
//
//
//	// 历史管理
////	private HistoryManager			mHistoryManager;
//
//	private SendMessageHandle		mSendMessageHandle;
//
//	private ReceiveMessageHandle	mReceiveMessageHandle;
//	
////	private GameProtocol			iGameProtocol;
//	
//    private  int viewId=-1;
//	public int getViewId() {
//		return viewId;
//	}
//	public void setViewId(int viewId) {
//		this.viewId = viewId;
//	}
//
////	private MSFHelper				mMSFHelper;
//	/**
//	 * 全局的地图实例
//	 */
////	private GloableMapView			mGloableMapView;
//
//
//	private static final GameDataCenter	mInstance					= new GameDataCenter();
//	private String					uin							= "";
//
////	public String getUin()
////	{
////		if(uin == null || uin.trim().length() == 0)
////		{
////			uin = SettingDataManager.loadLastSuccUin(mContext);
////		}
////		return uin;
////	}
//
////	public void setUin(String uin)
////	{
////		SettingDataManager.saveLastSuccUin(getContext(), uin);
////		this.uin = uin;
////	}
//
////	public void setStoreId(long id){
////		mStoreId = id;
////	}
////	
////	public long getStoreId(){
////		return mStoreId;
////	}
//	
//	/**
//	 * 作用：
//	 * <p>
//	 * </p>
//	 * 
//	 * @return
//	 */
//	public static GameDataCenter getInstance()
//	{
//		return mInstance;
//	}
//
//	/**
//	 * 作用：
//	 * <p>
//	 * 初始化各管理类
//	 * </p>
//	 */
//	public void init(Context context, Handler mainHandler)
//	{
//		mContext = context;
//		mHandler = mainHandler;
////		mMainFlipper = flipper;
//
////		mHistoryManager = new HistoryManager(context);
//		mSendMessageHandle = new SendMessageHandle();
//		mReceiveMessageHandle = new ReceiveMessageHandle();
////		iGameProtocol = new GameProtocol();
////		mMyLoactionHelper = new MyLoactionHelper(context);
////		mLoginHelper = new LoginHelper();
////		mMSFHelper = new MSFHelper(context);
////		mGloableMapView = new GloableMapView(context);
//	}
//
//	/**
//	 * 获取全局的mapview
//	 * 
//	 * @return
//	 */
////	public GloableMapView getGloableMapView()
////	{
////		return mGloableMapView;
////	}
//
////	public LoginHelper getLoginHelper()
////	{
////		return mLoginHelper;
////	}
//
////	public MSFHelper getMSFHelper()
////	{
////		return mMSFHelper;
////	}
//
//	/**
//	 * 获取位置辅助类
//	 * 
//	 * @return
//	 */
////	public MyLoactionHelper getMyLoactionHelper()
////	{
////		return mMyLoactionHelper;
////	}
//
//	public ReceiveMessageHandle getReceiveMessageHandle()
//	{
//		return mReceiveMessageHandle;
//	}
//
//	public SendMessageHandle getSendMessageHandle()
//	{
//		return mSendMessageHandle;
//	}
//
////	public HistoryManager getHistoryManager()
////	{
////		return mHistoryManager;
////	}
//
////	public void setToolBar(ToolBar bar)
////	{
////		mToolBar = bar;
////	}
//
////	public ToolBar getToolBar()
////	{
////		return mToolBar;
////	}
//
//	public Context getContext()
//	{
//		return mContext;
//	}
//
////	public ITOViewFlipper getMainFlipper()
////	{
////		return mMainFlipper;
////	}
//
//	public Handler getMainHandler()
//	{
//		return mHandler;
//	}
//
////	public GameProtocol getGameProtocol() {
////		return iGameProtocol;
////	}
//
//	/**
//	 * /**
//	 * 设置轮播场馆列表
//	 * 
//	 * @param list
//	 */
////	public void setPlayStoreList(List<Store> list)
////	{
////		mPlayStoreList = list;
////	}
//
//	/**
//	 * 获取轮播场馆列表
//	 * 
//	 * @param list
//	 */
////	public List<Store> getPlayStoreList()
////	{
////		return mPlayStoreList;
////	}
//	
//	/**
//	 * 设置热词数组
//	 * @param vector
//	 */
////	public void setHotWords(Vector<String> vector){
////		mVectorHotWord = vector;
////	}
//	
//	/**
//	 * 获取热词数组
//	 * @return
//	 */
////	public Vector<String> getHotWords(){
////		return mVectorHotWord;
////	}
//	
//	/**
//	 * 内存不足时候处理
//	 */
//	public void onLowMemory()
//	{
////		CacheUtils.clearCache();
//		System.gc();
//	}
//
//
//}
