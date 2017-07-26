package com.funoble.myarstation.protocol;
///*******************************************************************************
// * Copyright (C) 1998-2009 BBG Inc.All Rights Reserved.
// * FileName: SendMessageHandle.java
// * Description：
// * History：
// * 版本号 作者 日期 简要介绍相关操作
// * 1.0 sunny 2009-11-17 Create
// *******************************************************************************/
//package com.funoble.lyingdice.protocol;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import android.graphics.Bitmap;
//
//import com.funoble.lyingdice.common.Base64Coder;
//import com.funoble.lyingdice.common.Tools;
//import com.funoble.lyingdice.communication.RestClient;
//import com.funoble.lyingdice.communication.RestMsg;
//import com.funoble.lyingdice.game.GameDataCenter;
//import com.funoble.lyingdice.socket.protocol.RetMsg;
//
///**
// * <p>
// * 协议消息发送类
// * </p>
// */
//public class SendMessageHandle
//{
////	/**
////	 * 获取SKEY的客户请求
////	 * 
////	 * @param uin
////	 * @param clientkey
////	 */
////	public void requestkey(String uin, String clientkey)
////	{
////		Tools.debug("<<requestkey, uin:" + uin + ",clientkey:" + clientkey);
////		Map<String, String> value = new HashMap<String, String>();
////
////		value.put("clientuin", uin);
////		value.put("clientkey", clientkey);
////		value.put("keyindex", "19");
////		sendGet1(RequestUrlTools.SKEY_URL, value, AppEngine.ACTION_GET_SKEY);
////	}
////
////	/**
////	 * 获取热词  change by jason 06-27
////	 */
////	public void requestHotWord(){
////		sendPost(RequestUrlTools.Hotword_URL, null, AppEngine.ACTION_GET_HOTWORD);
////	}
////	
////	/**
////	 * 获取最新场地信息
////	 * 
////	 */
////	public void requestNewStoreList(String city)
////	{
////		Tools.debug("<<requestNewStoreList, city:" + city);
////		// 为测试，修改城市
////		// city = "深圳";
////
////		Map<String, String> value = new HashMap<String, String>();
////		if (city != null && city.length() > 0)
////		{
////			if (city.lastIndexOf("市") == city.length() - 1)
////			{
////				city = city.substring(0, city.length() - 1);
////			}
////			value.put("city", city);
////		}
////		sendGet(RequestUrlTools.new_store_URL, value, AppEngine.ACTION_GET_NEW_STORE_LIST);
////	}
////
////	/**
////	 * 获取场地详情
////	 * 
////	 * @param id
////	 * @param action
////	 */
////	public void requestStoredetail(long id)
////	{
////		Tools.debug("<<requestStoredetail, id:" + id);
////		Map<String, String> value = new HashMap<String, String>();
////		value.put("id", String.valueOf(id));
//////		System.out.println("eiieie" + id);
////		sendGet(RequestUrlTools.Sport_detail_URL, value, AppEngine.ACTION_GET_STORE_DETAIL);
////	}
////
////	/**
////	 *提交场地评星
////	 * 
////	 * @param storeId
////	 * @param star
////	 */
////	public void sendStoreStar(long storeId, float star)
////	{
////		Tools.debug("<<sendStoreStar, storeId:" + storeId);
////		Map<String, String> value = new HashMap<String, String>();
////		value.put("sportId", String.valueOf(storeId));
////		value.put("star", String.valueOf(star));
////
//////		this.sendGet(RequestUrlTools.eitestar, value, AppEngine.ACTION_SEND_STAR);
////		this.sendPost(RequestUrlTools.eitestar, value, AppEngine.ACTION_SEND_STAR);
////	}
////
////
//	public void sendRegister(RetsMsg data) {
//		RestClient.addTask(RestClient.TaskMehod.POST, data);
////		RestClient.addTask(RestClient.TaskMehod.GET, new MsgPara(0, "http://appmedia.qq.com/media/361everywhere/images/activity/android/1.png", null) {
////			
////			@Override
////			public boolean Encode() {
////				// TODO Auto-generated method stub
////				return false;
////			}
////			
////			@Override
////			public boolean Decode() {
////				// TODO Auto-generated method stub
////				return false;
////			}
////		});
//	}
////	/**
////	 * 提交评分
////	 * 
////	 * @param storeId
////	 * @param evaluateContent
////	 * @param action
////	 */
////	public void sendEvaluateContent(long storeId, String evaluateContent)
////	{
////		Tools.debug("<<sendEvaluateContent, storeId:" + storeId + ",evaluateContent:" + evaluateContent);
////		Map<String, String> value = new HashMap<String, String>();
////		value.put("sportId", String.valueOf(storeId));
////		value.put("evaluateContent", String.valueOf(evaluateContent));
////		this.sendPost(RequestUrlTools.evaluatecreate_URL, value, AppEngine.ACTION_SEND_EVALUATECREATE);
////		// this.sendGet( RequestUrlTools.evaluatecreate_URL, value, action);
////	}
////
////	/**
////	 * 获取评论列表
////	 * 
////	 * @param storeId
////	 * @param page
////	 * @param num
////	 */
////	public void requestEvaluateList(long storeId, int page, int num)
////	{
////		Tools.debug("<<requestEvaluateList, storeId:" + storeId + ",page:" + page + ",num:" + num);
////		Map<String, String> value = new HashMap<String, String>();
////		value.put("sportId", String.valueOf(storeId));
////
////		value.put("page", String.valueOf(page));
////		value.put("num", String.valueOf(num));
////		this.sendGet(RequestUrlTools.EvaluateList_URL, value, AppEngine.ACTION_EVALUATE_LIST);
////	}
////
////	/**
////	 * 获取支持列表
////	 * 
////	 * @param storeId
////	 * @param page
////	 * @param num
////	 */
////	public void requestSupportList(long storeId, int page, int num)
////	{
////		Tools.debug("<<requestSupportList, storeId:" + storeId + ",page:" + page + ",num:" + num);
////		Map<String, String> value = new HashMap<String, String>();
////		value.put("sportId", String.valueOf(storeId));
////
////		value.put("page", String.valueOf(page));
////		value.put("num", String.valueOf(num));
////		this.sendGet(RequestUrlTools.SupportList_URL, value, AppEngine.ACTION_SUPPORT_LIST);
////	}
////
////	/**
////	 * 场馆搜索
////	 * 
////	 * @param value搜索的参数 KEY和VALUE
////	 * @param type
////	 * @param action
////	 */
////	public void sendSeachStore(Map<String, String> value, int type)
////	{
////		Tools.debug("<<sendSeachStore, type:" + type);
////		Map<String, String> params = new HashMap<String, String>();
////		if (value != null)
////		{
////			params.putAll(value);
////			String city = params.get(SearchResultView.SEARCH_PARAMS_CITY);
////			if (city != null && city.length() > 0)
////			{
////				if (city.lastIndexOf("市") == city.length() - 1)
////				{
////					city = city.substring(0, city.length() - 1);
////				}
////				params.remove(SearchResultView.SEARCH_PARAMS_CITY);
////				params.put(SearchResultView.SEARCH_PARAMS_CITY, city);
////			}
////			String province = params.get(SearchResultView.SEARCH_PARAMS_PROVINCE);
////			if (province != null && province.length() > 0)
////			{
////				if (province.lastIndexOf("省") == province.length() - 1)
////				{
////					province = province.substring(0, province.length() - 1);
////				}
////				params.remove(SearchResultView.SEARCH_PARAMS_PROVINCE);
////				params.put(SearchResultView.SEARCH_PARAMS_PROVINCE, province);
////			}
////		}
////
////		sendGet(RequestUrlTools.search_store_URL, params, AppEngine.ACTION_GET_STORE_LIST);
////	}
////
////	/**
////	 * 发送支持场地
////	 * 
////	 * @param storeId
////	 */
////	public void sendStoreUp(long storeId)
////	{
////		Tools.debug("<<sendStoreUp, storeId:" + storeId);
////		Map<String, String> value = new HashMap<String, String>();
////		value.put("sportId", String.valueOf(storeId));
////		sendGet(RequestUrlTools.sport_up_URL, value, AppEngine.ACTION_SEND_STORE_UP);
////	}
////
////	/**
////	 * 获取足迹列表
////	 */
////	public void requestFootList(int page, int num)
////	{
////		Tools.debug("<<requestFootList");
////		Map<String, String> value = new HashMap<String, String>();
////		value.put("page", String.valueOf(page));
////		value.put("num", String.valueOf(num));
////		sendGet(RequestUrlTools.My_action_URL, value, AppEngine.ACTION_GET_FOOT_LIST);
////
////	}
////
////	/**
////	 * 获取个人资料
////	 */
////	public void requestMyInfo()
////	{
////		Tools.debug("<<requestMyInfo");
////		// Map<String, String> value = new HashMap<String, String>();
////		// value.put("uin", String.valueOf(uin));
////		sendGet(RequestUrlTools.MyZone_URL, null, AppEngine.ACTION_GET_MY_INFO);
////
////	}
////
////	/**
////	 * 发送附近场地(参数：经纬度)
////	 * @param lon
////	 * @param lat
////	 * change by jason 06-28
////	 */
////	public void sendArroundSite(double lon , double lat){
////		Map<String, String> params = new HashMap<String, String>();
////		params.put("distance", String.valueOf(10000));
////		params.put("lat", String.valueOf(lat));
////		params.put("lon", String.valueOf(lon));
////		params.put("count", String.valueOf(10));
////		sendGet(RequestUrlTools.search_store_URL, params, AppEngine.ACTION_GET_STORE_LIST);
////	}
////	
////	/**
////	 * 发送微博消息
////	 * @param storeId
////	 * @param type 1:评论    2:创建场地
////	 */
////	public void sendMessageToMicroBlog(long storeId , byte type , String microBlodText){
//////		1   评论
//////		2   创建场地
////		Map<String, String> params = new HashMap<String, String>();
////		params.put("sportid", String.valueOf(storeId));
////		params.put("typeid", String.valueOf(type));
////		String typeText = type == 1 ? "评论" : " 创建场地";
////		params.put("typetext", typeText);
////		params.put("micoblog", microBlodText);
//////		Log.v("recivice send microblog content = ", params.toString());
////		sendPost(RequestUrlTools.Sendblog_URL, params, AppEngine.ACTION_GET_SENDBLOG);
////	}
////	
////	/**
////	 * 获取最新场地信息列表(轮播的查看更多按钮)
////	 * @param city
////	 * @param page
////	 * @param num
////	 * change by jason 06-29
////	 */
////	public void requestNewStoreList(Map<String, String> value){
////		String city = value.get(SearchResultView.SEARCH_PARAMS_CITY);
////		if (city != null && city.length() > 0){
////			if (city.lastIndexOf("市") == city.length() - 1)
////			{
////				city = city.substring(0, city.length() - 1);
////			}
////			value.put(SearchResultView.SEARCH_PARAMS_CITY, city);
////			sendGet(RequestUrlTools.btn_new_store_URL , value , AppEngine.ACTION_GET_STORE_LIST);
////		}
////	}
////	
////	/**
////	 * 最热场馆列表请求
////	 * change by jason 06-30 最热列表
////	 * @param value
////	 */
////	public void requestBestHotStoreList(Map<String, String> value){
////		String city = value.get(SearchResultView.SEARCH_PARAMS_CITY);
////		if (city != null && city.length() > 0){
////			if (city.lastIndexOf("市") == city.length() - 1)
////			{
////				city = city.substring(0, city.length() - 1);
////			}
////			value.put(SearchResultView.SEARCH_PARAMS_CITY, city);
////			sendGet(RequestUrlTools.best_hot_store_URL , value , AppEngine.ACTION_GET_STORE_LIST);
////		}
////	}
////	
////	/**
////	 * 获取大篷车活动场馆
////	 * @param value
////	 */
////	public void requestDaPengCheList(){
////		
////		sendGet(RequestUrlTools.DaPengChe_Store_URL , null , AppEngine.ACTION_GET_DPCSTORE_LIST);
////			
////	}
////	
////	/**
////	 * 361day
////	 * @param value
////	 */
////	public void requestAdList(Map<String, String> param) {
////		Map<String, String> value = new HashMap<String, String>();
////		if (param.containsKey(SearchResultView.SEARCH_PARAMS_PAGE_COUNT)) {
////			value.put(SearchResultView.SEARCH_PARAMS_PAGE_COUNT,
////					param.get(SearchResultView.SEARCH_PARAMS_PAGE_COUNT));
////		}
////		if (param.containsKey(SearchResultView.SEARCH_PARAMS_PAGE)) {
////			value.put(SearchResultView.SEARCH_PARAMS_PAGE,
////					param.get(SearchResultView.SEARCH_PARAMS_PAGE));
////		}
////
////		value.put(SearchResultView.SEARCH_PARAMS_AD, String.valueOf(1));
////		sendGet(RequestUrlTools.search_store_URL, value, AppEngine.ACTION_GET_DPCSTORE_LIST);
////	}
////	
////	/**
////	 * 获取我创建的场馆
////	 */
////	public void requestMyCreatedStore(int page, int count)
////	{
////		// count=10
////		Tools.debug("<<requestMyCreatedStore");
////		Map<String, String> value = new HashMap<String, String>();
////		value.put("page", String.valueOf(page));
////		value.put("count", String.valueOf(count));
////		sendGet(RequestUrlTools.My_Sport_URL, value, AppEngine.ACTION_GET_MY_CREATED_STORE);
////
////	}
////	/**
////	 * 获取我的反馈
////	 */
////	public void requestMySuggest()
////	{
////		// count=10
////		Tools.debug("<<requestMySuggest");
////		sendGet(RequestUrlTools.Suggest_URL, null, AppEngine.ACTION_GET_SUGGEST);
////	}
////
////	/**
////	 * 获取反馈回复
////	 */
////	public void requestReply()
////	{
////		// count=10
////		Tools.debug("<<requestReply");
////		sendGet(RequestUrlTools.Reply_URL, null, AppEngine.ACTION_GET_REPLY);
////	}
////	
////	/**
////	 * 提交反馈
////	 * @param action
////	 * @param SuggestConttent
////	 */
////
////	public void sendSuggest(String suggest){
////			Tools.debug("<<sendSuggest");
////			Map<String, String> value = new HashMap<String, String>();
////			value.put("suggest", String.valueOf(suggest));
////			this.sendPost(RequestUrlTools.Create_Suggest_URL, value, AppEngine.ACTION_SEND_SUGGEST);
////	}
////	
////	/**
////	 * 请求滚动公告
////	 * 
////	 * @param action
////	 */
////	public void requestFeeds()
////	{
////		Tools.debug("<<requestFeeds");
////		sendGet(RequestUrlTools.feeds_URL, null, AppEngine.ACTION_FEEDS);
////	}
////
////	/**
////	 * 创建或修改场馆
////	 * 
////	 * @param store
////	 */
////	public void sendCreateOrEditStore(Store store)
////	{
////		Tools.debug("<<sendCreateOrEditStore");
////		Map<String, String> value = new HashMap<String, String>();
////		value.put("name", store.getName());
////		value.put("address", store.getAddress());
////		value.put("jingdu", store.getJingdu() + "");
////		value.put("weidu", store.getWeidu() + "");
////		value.put("province", store.getProvince());
////		value.put("city", store.getCity());
////		value.put("area", store.getArea());
////		value.put("classes", store.getClasses());
////		value.put("type", store.getType() + "");
////		value.put("phone", store.getPhone());
////		// 后台这里比较变态： 创建场地时， qq号码用id作为key。
////		// 修改场地时，qq号码还是用id作为key，场地id字段用sportid作为key
////		value.put("ID", store.getQq() + "");
////		if (store.getId() >= 0)
////		{
////			value.put("sportid", String.valueOf(store.getId()));
////		}
////
////		value.put("email", store.getEmail());
////		value.put("fee", store.getFee());
////		value.put("textstr", store.getTextstr());
////
////		Bitmap bm = store.getMyIcon();
////		if (bm != null)
////		{
////			ByteArrayOutputStream out = new ByteArrayOutputStream();
////			try
////			{
////				bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
////				out.flush();
////				char[] chars = Base64Coder.encode(out.toByteArray());
////				String strBM = new String(chars);
////				value.put("Picture", strBM);
////			}
////			catch (IOException e)
////			{
////				e.printStackTrace();
////			}
////			finally
////			{
////				try
////				{
////					out.close();
////				}
////				catch (Exception e)
////				{
////				}
////			}
////		}
////
////		sendPost(store.getId() >= 0 ? RequestUrlTools.eitesport_URL : RequestUrlTools.createsport_URL, value, AppEngine.ACTION_CREATE_STORE);
////	}
////
////	/**
////	 * 发送POST请求
////	 * 该方法一般情况只在该协议层使用，当然也可以被外界调用
////	 * 
////	 * @param values客户端发送数据
////	 * @param url请求的地址
////	 * @param action响应消息类型
////	 */
////	private void sendPost(String url, Map<String, String> values, int action)
////	{
////		RestMsg restMsg = new RestMsg(action, RequestUrlTools.MAIN_URL + url, values);
//////		if (action == AppEngine.ACTION_GET_SENDBLOG){
//////			Log.v("recivice send microblog content = ", restMsg.getUrl());
//////		}
////		RestClient.addTask(RestClient.TaskMehod.POST, restMsg);
////	}
////
////	private void sendGet1(String url, Map<String, String> values, int action)
////	{
////		RestMsg restMsg = null;
////		restMsg = new RestMsg(action, url + "?" + getData(values).toString(), null);
////		Tools.debug("get skey: " + restMsg.getUrl());
////		RestClient.addTask(RestClient.TaskMehod.GET, restMsg);
////	}
////
////	/**
////	 * 发送GET客户端请求，该方法一般情况只在该协议层使用，当然也可以被外界调用
////	 * 
////	 * @param url请求的地址
////	 * @param values客户端发送数据
////	 * @param action响应消息类型
////	 */
//	private void sendGet(String url, Map<String, String> values, int action)
//	{
//		RetMsg restMsg = null;
//		String realHost = null;
//		if (action == GameDataCenter.ACTION_GET_IMAGE || action == GameDataCenter.ACTION_GET_GEO_INFO
//				|| url.equals(RequestUrlTools.RECORD_OPERATE_URL))
//		{
//			realHost = url;
//		}
//		else
//		{
//			realHost = RequestUrlTools.MAIN_URL + url;
//		}
//		if (values != null)
//		{
//			// StringBuilder sb = new StringBuilder();
//			// java.util.Set<String> keyset = values.keySet();
//			// Iterator<String> ip = keyset.iterator();
//			// /**
//			// * 取出key和值组装成要送到服务端的，头部数据包
//			// */
//			// int count = 0;
//			// while (ip.hasNext())
//			// {
//			// String key = ip.next();
//			// sb.append(key + "=");
//			// sb.append(values.get(key));
//			// if (count < keyset.size() - 1)
//			// {
//			// sb.append("&");
//			// }
//			// count++;
//			// }
//
////			restMsg = new RestMsg(action, realHost + "?" + getData(values).toString(), null);
//
//		}
//		else
//		{
//
//			restMsg = new RetMsg(action, realHost, null);
//				
//		}
////		if (restMsg.getAction() >= AppEngine.ACTION_LOGIN_RECORD
////				&& restMsg.getAction() <= AppEngine.ACTION_SHARE_RECORD){
////			Log.v("sendGet url = ", restMsg.getUrl());
////		}
//		RestClient.addTask(RestClient.TaskMehod.GET, restMsg);
//	}
////
////	public StringBuilder getData(Map<String, String> values)
////	{
////		if (values != null)
////		{
////			StringBuilder sb = new StringBuilder();
////			java.util.Set<String> keyset = values.keySet();
////			Iterator<String> ip = keyset.iterator();
////			/**
////			 * 取出key和值组装成要送到服务端的，头部数据包
////			 */
////			int count = 0;
////			while (ip.hasNext())
////			{
////				String key = ip.next();
////				sb.append(key + "=");
////				sb.append(values.get(key));
////				if (count < keyset.size() - 1)
////				{
////					sb.append("&");
////				}
////				count++;
////			}
////			return sb;
////		}
////		return null;
////	}
////
////	/**
////	 * 根据经纬度解析地址
////	 * 
////	 * @param longitude 经度
////	 * @param latitude 纬度
////	 */
////	public void requestGeoInfo(double longtiude, double latitude)
////	{
////		Tools.debug("<<requestGeoInfo, longtiude:" + longtiude + ",latitude:" + latitude);
////		Map<String, String> value = new HashMap<String, String>();
////		value.put("q", latitude + "," + longtiude);
////		value.put("output", "json");
////		value.put("oe", "utf8");
////		value.put("hl", "zh-CN");
////		value.put("sensor", "true");
////		sendGet(RequestUrlTools.GOOGLE_GEO_SERVICE, value, AppEngine.ACTION_GET_GEO_INFO);
////	}
////
////	/**
////	 * 根据url获取网络图片
////	 * 
////	 * @param url
////	 */
//	public void getImageByUrl(String url)
//	{
//		Tools.debug("<<getImageByUrl, url:" + url);
//
//		sendGet(url, null, GameDataCenter.ACTION_GET_IMAGE);
//	}
////
////	/**
////	 * 发送记录操作协议
////	 * @param actionID
////	 * change by jason 07-13
////	 */
////	public void sendRecordOperate(int actionID , String qq){
//////		http://act.t.l.qq.com/?cpid=641009230&actid=600140&cptype=1&qq=63779393&ip=&reserve=
//////			其中
//////
//////			1， http://act.t.l.qq.com/ 是我们的动作服务器地址
//////
//////			2，cpid ，活动id，对于本次361统计id=641009230
//////
//////			3，actid，用户某种动作的id。并且同一动作，在不同平台是用不同id来表示，具体动作id和名称的对应关系，在附件中
//////
//////			4，cptype，数据类型，这里可以写死为1
//////
//////			5，qq，用户的qq号码。
//////
//////			6，ip，用户ip，没有可以不填，会默认取网关的ip
//////
//////			7，reserve，保留字段，留作以后扩展参数使用，可以直接为空
////		Map<String, String> value = new HashMap<String, String>();
////		value.put("cpid", String.valueOf(641009230));
////		value.put("actid", String.valueOf(actionID));
////		value.put("cptype", String.valueOf(1));
////		value.put("qq", qq);
////		value.put("ip", "");
////		value.put("reserve", "");
////		sendGet(RequestUrlTools.RECORD_OPERATE_URL, value, actionID);
////	}
//}
