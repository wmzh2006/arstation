/*******************************************************************************
 * Copyright (C) 1998-2009 BBG Inc.All Rights Reserved. FileName:
 * HttpMessageHandle.java Description： History： 版本号 作者 日期 简要介绍相关操作 1.0 sunny
 * 2009-11-17 Create
 *******************************************************************************/
package com.funoble.myarstation.protocol;

import org.json.JSONTokener;

import android.os.Handler;
import android.os.Message;

import com.funoble.myarstation.communication.RestMsg;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.socket.protocol.RetMsg;

/**
 * <p>
 * 协议解析类
 * </p>
 */
public class ReceiveMessageHandle
{
	private Handler	uiHandle	= MyArStation.getInstance().iHandler;

	public ReceiveMessageHandle()
	{
	}
	
	public boolean parseMsg(RestMsg msg) {
		switch (msg.getAction()) {
		case 10:
			Message message = uiHandle.obtainMessage(10);
			message.obj = msg;
			uiHandle.sendMessage(message);
			break;

		default:
			break;
		}
		return true;
	}

	/**
	 * 解析消息
	 * 
	 * @param msg
	 *            返回的json数据
	 * @param url
	 *            服务url，用于判断服务类型
	 * @return
	 */
	public boolean parseMsg(JSONTokener msg, int action)
	{
//		Tools.debug(">>parseMsg, action:" + action);
//		uiHandle = AppEngine.getInstance().getMainHandler();
//		switch (action)
//		{
//			case AppEngine.ACTION_SEND_STORE_UP: // 处理场馆详细的顶的响应
//				getMessage(msg, action);
//				break;
//
//			case AppEngine.ACTION_SEND_STAR:
//				getMessage(msg, action);
//				break;
//
//			case AppEngine.ACTION_SEND_EVALUATECREATE:
//				getMessage(msg, action);
//				break;
//
//			case AppEngine.ACTION_GET_STORE_LIST:// 处理场馆列表
//				getStore_List(msg);
//				break;
//
//			case AppEngine.ACTION_GET_NEW_STORE_LIST:// 轮播场馆
//				// getStore_Detail(msg);
//				this.getNewStore_List(msg);
//				break;
//
//			case AppEngine.ACTION_GET_STORE_DETAIL:
//				getSport_detail(msg);
//				break;
//
//			case AppEngine.ACTION_SUPPORT_LIST:
//				this.getSupport_List(msg);
//				break;
//
//			case AppEngine.ACTION_EVALUATE_LIST:
//				this.getEvaluateList(msg);
//				break;
//
//			case AppEngine.ACTION_GET_GEO_INFO:
//				parseGeoInfo(msg);
//				break;
//
//			case AppEngine.ACTION_CREATE_STORE: // 创建场地
//				parseCreateStore(msg);
//				break;
//
//			case AppEngine.ACTION_GET_FOOT_LIST: // 个人足迹
//				this.getFoot_List(msg);
//				break;
//
//			case AppEngine.ACTION_GET_MY_CREATED_STORE: // 我的场地
//				this.getMy_Created_Store_List(msg);
//				break;
//				
//			case AppEngine.ACTION_GET_MY_INFO:
//				getMY_INFO(msg);
//				break;
//				/** change by jason 06-27*/
//			case AppEngine.ACTION_GET_HOTWORD:
//				getHOTWORD(msg);
//				break;
//			case AppEngine.ACTION_FEEDS:
//				this.getFeeds(msg);
//				break;
//			case AppEngine.ACTION_GET_SENDBLOG:
//				getSendMicroblog(msg);
//				break;
//			case AppEngine.ACTION_GET_DPCSTORE_LIST://大篷车场馆
//				getDaPengCheStore_List(msg);
//				break;
//			case AppEngine.ACTION_SEND_SUGGEST://提交反馈
//				getSendSuggest(msg);
//				break;
//			case AppEngine.ACTION_GET_SUGGEST://获取反馈
//				getSuggest(msg);
//				break;
//			case AppEngine.ACTION_GET_REPLY://获取反馈回复
//				getReply(msg);
//				break;
//			default:
//				break;
//		}
//
		return true;
	}
//	
//	/**
//	 * 获取已回复反馈信息
//	 */
//	private void getReply(JSONTokener tk){
//		ServerPageData page = new ServerPageData();
//		try
//		{
//			// tk.nextValue();
//			JSONObject msg = (JSONObject) tk.nextValue();
//			getsend_msg(msg, page);
//			if (!msg.isNull("total"))
//			{
//				page.setTotalcount(msg.getInt("total"));
//			}
//			List<Suggest> ev = new ArrayList<Suggest>();
//			if (!msg.isNull("list"))
//			{
//				JSONArray ar = msg.getJSONArray("list");
//				for (int i = 0; i < ar.length(); i++)
//				{
//					Suggest e = new Suggest();
//					e.replayQQ = ar.getJSONObject(i).getString("FReplyQQ");
//					e.replayTime = ar.getJSONObject(i).getString("FReplyTime");
//					e.reply = ar.getJSONObject(i).getString("FReply");
//					e.suggestId = ar.getJSONObject(i).getString("FSuggestId");
//					e.suggestText = ar.getJSONObject(i).getString("FSuggest");
//					e.suggestTime = ar.getJSONObject(i).getString("FSuggestTime");
//					ev.add(e);
//				}
//				page.setMsg(ev);
//			}
//		}
//		catch (Exception ex)
//		{
//			ServerMsg sermsg = new ServerMsg();
//			sermsg.setResult(-100);
//			sermsg.setResultmsg("无数据返回");
//			page.setMessage(sermsg);
//		}
//		Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_REPLY);
//		message.obj = page;
//		uiHandle.sendMessage(message);
//	}
//	
//	/**
//	 * 获取提交反馈信息
//	 */
//	private void getSuggest(JSONTokener tk){
//		ServerPageData page = new ServerPageData();
//		try
//		{
//			// tk.nextValue();
//			JSONObject msg = (JSONObject) tk.nextValue();
//			getsend_msg(msg, page);
//			if (!msg.isNull("total"))
//			{
//				page.setTotalcount(msg.getInt("total"));
//			}
//			List<Suggest> ev = new ArrayList<Suggest>();
//			if (!msg.isNull("list"))
//			{
//				JSONArray ar = msg.getJSONArray("list");
//				for (int i = 0; i < ar.length(); i++)
//				{
//					Suggest e = new Suggest();
//					e.suggestId = ar.getJSONObject(i).getString("FSuggestId");
//					e.suggestText = ar.getJSONObject(i).getString("FSuggest");
//					e.suggestTime = ar.getJSONObject(i).getString("FSuggestTime");
//					ev.add(e);
//				}
//				page.setMsg(ev);
//			}
//		}
//		catch (Exception ex)
//		{
//			ServerMsg sermsg = new ServerMsg();
//			sermsg.setResult(-100);
//			sermsg.setResultmsg("无数据返回");
//			page.setMessage(sermsg);
//		}
//		Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_SUGGEST);
//		message.obj = page;
//		uiHandle.sendMessage(message);
//	}
//	/**
//	 * 获取提交反馈返回信息
//	 */
//	private void getSendSuggest(JSONTokener tk){
//		ServerPageData page = new ServerPageData();
//		ServerMsg sermsg = new ServerMsg();
//		try
//		{
//			JSONObject obj = (JSONObject) tk.nextValue();;
//			if(!obj.isNull("result"))
//			{
//				sermsg.setResult(obj.getInt("result"));
//			}
//			if(!obj.isNull("resultmsg"))
//			{
//				sermsg.setResultmsg(obj.getString("resultmsg"));
//			}
//		}catch(Exception e){
//			sermsg.setResult(-100);
//			sermsg.setResultmsg("未知错误");
//		}
//		
//		page.setMessage(sermsg);
//		Message message = uiHandle.obtainMessage(AppEngine.ACTION_SEND_SUGGEST);
//		message.obj = page;
//		uiHandle.sendMessage(message);
//	}
//	
//	/**
//	 * 获取发送微博返回来的信息 
//	 * change by jason 06-28
//	 */
//	private void getSendMicroblog(JSONTokener tk){
//		ServerPageData page = new ServerPageData();
//		ServerMsg sermsg = new ServerMsg();
//		try
//		{
//			Object tmpObj = tk.nextValue();;
//			JSONObject obj = (JSONObject) tmpObj;
//			if(!obj.isNull("result"))
//			{
//				sermsg.setResult(obj.getInt("result"));
//			}
//			if(!obj.isNull("resultmsg"))
//			{
//				sermsg.setResultmsg(obj.getString("resultmsg"));
//			}
//		}catch(Exception e){
//			sermsg.setResult(-100);
//			sermsg.setResultmsg("未知错误");
//		}
//		
//		page.setMessage(sermsg);
//		Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_SENDBLOG);
//		message.obj = page;
//		uiHandle.sendMessage(message);
//	}
//
//	/**
//	 * 获取热词 change by jason 06-27
//	 * @param tk
//	 */
//	private void getHOTWORD(JSONTokener tk){
//		ServerPageData page = new ServerPageData();
//		try{
//			JSONArray msg = (JSONArray) tk.nextValue();
//			Vector<String> vector = new Vector<String>();
//			for(int i = 0; i < msg.length(); i++){
//				String str = msg.getJSONObject(i).getString("word");
//				vector.add(str);
//			}
//			page.setMsg(vector);
//		}catch (Exception ex)
//		{
//			ServerMsg sermsg = new ServerMsg();
//			sermsg.setResult(-100);
//			sermsg.setResultmsg("请求的数据格式错误");
//			page.setMessage(sermsg);
//		}
//		Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_HOTWORD);
//		message.obj = page;
//		uiHandle.sendMessage(message);
//	}
//	
//	private void getMY_INFO(JSONTokener tk)
//	{
//
//		ServerPageData page = new ServerPageData();
//		try
//		{
//			// tk.nextValue();
//			JSONObject msg = (JSONObject) tk.nextValue();
//
//			int totalNum = 0;
//			// List<Store> stores = new ArrayList<Store>();
//
//			getsend_msg(msg, page);
//			MyZone myzone = new MyZone();
//			myzone.setEvaluate(msg.getLong("totalEvaluate"));
//			myzone.setSupport(msg.getLong("totalSupport"));
//			myzone.setMyqq(msg.getLong("qq"));
//			page.setMsg(myzone);
//		}
//		catch (Exception ex)
//		{
//			ServerMsg sermsg = new ServerMsg();
//			sermsg.setResult(-100);
//			sermsg.setResultmsg("请求的数据格式错误");
//			page.setMessage(sermsg);
//		}
//		Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_MY_INFO);
//		message.obj = page;
//		uiHandle.sendMessage(message);
//
//	}
//
//	private void parseCreateStore(JSONTokener tk)
//	{
//		Tools.debug("parseCreateStore:" + tk.toString());
//		ServerPageData page = new ServerPageData();
//		ServerMsg servMsg = new ServerMsg();
//		try
//		{
//
//			JSONObject obj = (JSONObject) tk.nextValue();
////			getsend_msg(obj, page);
//			if (!obj.isNull("id"))
//			{
//				page.setMsg(String.valueOf(obj.getLong("id")));
//			}
//			if (!obj.isNull("result"))
//			{
//				servMsg.setResult(obj.getInt("result"));
//			}
//			if (!obj.isNull("resultmsg"))
//			{
//				servMsg.setResultmsg(obj.getString("resultmsg"));
//			}
//		}
//		catch (Exception e)
//		{
//			servMsg.setResult(-100);
//			servMsg.setResultmsg("未知错误");
//		}
//		page.setMessage(servMsg);
//
//		Message msg = uiHandle.obtainMessage(AppEngine.ACTION_CREATE_STORE);
//
//		msg.obj = page;
//		msg.sendToTarget();
//	}
//
//	private void parseGeoInfo(JSONTokener tk)
//	{
//		try
//		{
//			JSONObject obj = (JSONObject) tk.nextValue();
//			String[] jingweidu = obj.getString("name").split(",");
//			double weidu = Double.valueOf(jingweidu[0]);
//			double jingdu = Double.valueOf(jingweidu[1]);
//			JSONArray placemark = obj.getJSONArray("Placemark");
//			if (!placemark.isNull(0))
//			{
//				JSONObject place = placemark.getJSONObject(0);
//				String address = place.getString("address");
//				JSONObject addressDetails = place.getJSONObject("AddressDetails");
//				JSONObject country = addressDetails.getJSONObject("Country");
//				JSONObject administrativeArea = country.getJSONObject("AdministrativeArea");
//				String province = administrativeArea.getString("AdministrativeAreaName");
//				JSONObject locality = administrativeArea.getJSONObject("Locality");
//				String localityName = locality.getString("LocalityName");
//				JSONObject dependentLocality = locality.getJSONObject("DependentLocality");
//				String dependentLocalityName = dependentLocality.getString("DependentLocalityName");
//
//				LocationData data = new LocationData(jingdu, weidu, province, localityName, dependentLocalityName, address);
//				Message msg = uiHandle.obtainMessage(AppEngine.ACTION_GET_GEO_INFO);
//				msg.obj = data;
//				msg.sendToTarget();
//			}
//
//		}
//		catch (Exception e)
//		{
//		}
//	}
//
//	/**
//	 * 读取场馆详情
//	 * 
//	 * @param tk
//	 */
//	private void getSport_detail(JSONTokener tk)
//	{
//		ServerPageData page = new ServerPageData();
//		try
//		{
//			JSONObject msg = (JSONObject) tk.nextValue();
//			page.setMsg(this.getsendStore_row(msg));
//			// ServerPageData page=new ServerPageData();
//
//			getsend_msg(msg, page);
//			if (page.getMsg() != null)
//			{
//			}
//		}
//		catch (Exception ex)
//		{
//			ServerMsg sermsg = new ServerMsg();
//			sermsg.setResult(-100);
//			sermsg.setResultmsg("请求的数据格式错误");
//			page.setMessage(sermsg);
//		}
//		Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_STORE_DETAIL);
//		message.obj = page;
//		uiHandle.sendMessage(message);
//	}
//
//	private void getNewStore_List(JSONTokener tk)
//	{
//		ServerPageData page = new ServerPageData();
//		try
//		{
//			JSONArray ar = (JSONArray) tk.nextValue();
//
//			List<Store> stores = new ArrayList<Store>();
//			for (int i = 0; i < ar.length(); i++)
//			{
//				stores.add(getsendStore_row(ar.getJSONObject(i)));
//			}
//			// ServerPageData page=new ServerPageData();
//			page.setMsg(stores);
//		}
//		catch (Exception ex)
//		{
//			ServerMsg sermsg = new ServerMsg();
//			sermsg.setResult(-100);
//			sermsg.setResultmsg("请求的数据格式错误");
//			page.setMessage(sermsg);
//		}
//		Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_NEW_STORE_LIST);
//		message.obj = page;
//		uiHandle.sendMessage(message);
//	}
//
//	private void getSupport_List(JSONTokener tk)
//	{
//		ServerPageData page = new ServerPageData();
//		try
//		{
//			// tk.nextValue();
//			JSONObject msg = (JSONObject) tk.nextValue();
//			getsend_msg(msg, page);
//			if (!msg.isNull("total"))
//			{
//				page.setTotalcount(msg.getInt("total"));
//			}
//			if (!msg.isNull("pagecount"))
//			{
//				page.setPagecount(msg.getInt("pagecount"));
//			}
//			if (!msg.isNull("pageindex"))
//			{
//				page.setPagecount(msg.getInt("pageindex"));
//			}
//			List<Support> ev = new ArrayList<Support>();
//			if (!msg.isNull("list"))
//			{
//				JSONArray ar = msg.getJSONArray("list");
//				for (int i = 0; i < ar.length(); i++)
//				{
//					Support e = new Support();
//					e.setUserqq(ar.getJSONObject(i).getLong("FSupportQq"));
//
//					e.setTime(ar.getJSONObject(i).getString("FTime"));
//					e.setId(ar.getJSONObject(i).getLong("FSupportId"));
//					ev.add(e);
//				}
//				page.setMsg(ev);
//			}
//
//		}
//		catch (Exception ex)
//		{
//			ServerMsg sermsg = new ServerMsg();
//			sermsg.setResult(-100);
//			sermsg.setResultmsg("无数据返回");
//			page.setMessage(sermsg);
//		}
//		Message message = uiHandle.obtainMessage(AppEngine.ACTION_SUPPORT_LIST);
//		message.obj = page;
//		uiHandle.sendMessage(message);
//	}
//
//	private void getEvaluateList(JSONTokener tk)
//	{
//		ServerPageData page = new ServerPageData();
//		try
//		{
//			// tk.nextValue();
//			JSONObject msg = (JSONObject) tk.nextValue();
//			getsend_msg(msg, page);
//			if (!msg.isNull("total"))
//			{
//				page.setTotalcount(msg.getInt("total"));
//			}
//			if (!msg.isNull("pagecount"))
//			{
//				page.setPagecount(msg.getInt("pagecount"));
//			}
//			if (!msg.isNull("pageindex"))
//			{
//				page.setPagecount(msg.getInt("pageindex"));
//			}
//			List<Evaluate> ev = new ArrayList<Evaluate>();
//			if (!msg.isNull("list"))
//			{
//				JSONArray ar = msg.getJSONArray("list");
//				System.out.println("ar" + ar.length());
//				for (int i = 0; i < ar.length(); i++)
//				{
//					Evaluate e = new Evaluate();
//
//					e.setUserqq(ar.getJSONObject(i).getLong("FEvaluateQq"));
//					e.setUsername(ar.getJSONObject(i).getString("FEvaluateName"));
//					if (!ar.getJSONObject(i).isNull("star"))
//					{
//						e.setEvaluateStar((float) ar.getJSONObject(i).getDouble("star"));
//					}
//					if (!ar.getJSONObject(i).isNull("userHead"))
//					{
//						e.setUserHead(ar.getJSONObject(i).getString("userHead"));
//					}
//					e.setEvaluateContent(ar.getJSONObject(i).getString("FEvaluateContent"));
//					e.setTime(ar.getJSONObject(i).getString("FTime"));
//					e.setId(ar.getJSONObject(i).getLong("FEvaluateId"));
//					ev.add(e);
//				}
//				page.setMsg(ev);
//			}
//
//		}
//		catch (Exception ex)
//		{
//			ex.printStackTrace();
//			ServerMsg sermsg = new ServerMsg();
//			sermsg.setResult(-100);
//			sermsg.setResultmsg("无数据返回");
//			page.setMessage(sermsg);
//		}
//		Message message = uiHandle.obtainMessage(AppEngine.ACTION_EVALUATE_LIST);
//		message.obj = page;
//		uiHandle.sendMessage(message);
//	}
//
//	private void getStore_List(JSONTokener tk)
//	{
//		ServerPageData page = new ServerPageData(); // 这个要废弃
//		SearchStoreResult ssr = null;
//		try
//		{
//			// tk.nextValue();
//			JSONObject msg = (JSONObject) tk.nextValue();
//
//			int totalNum = 0;
//			List<Store> stores = new ArrayList<Store>();
//			// ServerPageData page=new ServerPageData();
//			getsend_msg(msg, page);
//			if (!msg.isNull("total"))
//			{
//				totalNum = msg.getInt("total");
//				page.setTotalcount(msg.getInt("total"));
//			}
//			if (!msg.isNull("pagecount"))
//			{
//				page.setPagecount(msg.getInt("pagecount"));
//			}
//			if (!msg.isNull("pageindex"))
//			{
//				page.setPagecount(msg.getInt("pageindex"));
//			}
//			if (!msg.isNull("list"))
//			{
//				JSONArray ar = msg.getJSONArray("list");
//				for (int i = 0; i < ar.length(); i++)
//				{
//					// System.out.println("fief"+ar.getString(i));
//					stores.add(this.getsendStore_row(ar.getJSONObject(i)));
//					// System.out.println("ffdf"+ar.getJSONObject(i).names().getString(0));
//				}
//			}
//			page.setMsg(stores);
//
//			ssr = new SearchStoreResult(totalNum, stores);
//			Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_STORE_LIST);
//			message.obj = ssr;
//			uiHandle.sendMessage(message);
//		}
//		catch (Exception ex)
//		{
//			ex.printStackTrace();
//			ServerMsg sermsg = new ServerMsg();
//			sermsg.setResult(ServerMsg.ERROR_NO_DATA);
//			sermsg.setResultmsg(AppEngine.getInstance().getContext().getString(R.string.search_nodata2));
//			Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_STORE_LIST);
//			message.obj = sermsg;
//			uiHandle.sendMessage(message);
//		}
//	}
//	
//	private void getDaPengCheStore_List(JSONTokener tk)
//	{
//		ServerPageData page = new ServerPageData(); // 这个要废弃
//		SearchStoreResult ssr = null;
//		try
//		{
//			// tk.nextValue();
//			JSONObject msg = (JSONObject) tk.nextValue();
//			
//			int totalNum = 0;
//			List<Store> stores = new ArrayList<Store>();
//			// ServerPageData page=new ServerPageData();
//			getsend_msg(msg, page);
//			if (!msg.isNull("total"))
//			{
//				totalNum = msg.getInt("total");
//				page.setTotalcount(msg.getInt("total"));
//			}
//			if (!msg.isNull("pagecount"))
//			{
//				page.setPagecount(msg.getInt("pagecount"));
//			}
//			if (!msg.isNull("pageindex"))
//			{
//				page.setPagecount(msg.getInt("pageindex"));
//			}
//			if (!msg.isNull("list"))
//			{
//				JSONArray ar = msg.getJSONArray("list");
//				for (int i = 0; i < ar.length(); i++)
//				{
//					// System.out.println("fief"+ar.getString(i));
//					stores.add(this.getsendStore_row(ar.getJSONObject(i)));
//					// System.out.println("ffdf"+ar.getJSONObject(i).names().getString(0));
//				}
//			}
//			page.setMsg(stores);
//			
//			ssr = new SearchStoreResult(totalNum, stores);
//			Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_DPCSTORE_LIST);
//			message.obj = ssr;
//			uiHandle.sendMessage(message);
//		}
//		catch (Exception ex)
//		{
//			ex.printStackTrace();
//			ServerMsg sermsg = new ServerMsg();
//			sermsg.setResult(ServerMsg.ERROR_NO_DATA);
//			sermsg.setResultmsg(AppEngine.getInstance().getContext().getString(R.string.search_nodata2));
//			Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_DPCSTORE_LIST);
//			message.obj = sermsg;
//			uiHandle.sendMessage(message);
//		}
//	}
//	
////	private void getDaPengCheStore_List(JSONTokener tk)
////	{
////		ServerPageData page = new ServerPageData(); // 这个要废弃
////		SearchStoreResult ssr = null;
////		try
////		{
////			// tk.nextValue();
////			JSONArray msg = (JSONArray) tk.nextValue();
////			
////			int totalNum = 0;
////			List<Store> stores = new ArrayList<Store>();
////			for (int i = 0; i < msg.length(); i++)
////			{
////				// System.out.println("fief"+ar.getString(i));
////				stores.add(this.getsendStore_row(msg.getJSONObject(i)));
////				totalNum = msg.length();
////				// System.out.println("ffdf"+ar.getJSONObject(i).names().getString(0));
////			}
////			page.setMsg(stores);
////			
////			ssr = new SearchStoreResult(totalNum, stores);
////			Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_DPCSTORE_LIST);
////			message.obj = ssr;
////			uiHandle.sendMessage(message);
////		}
////		catch (Exception ex)
////		{
////			ex.printStackTrace();
////			ServerMsg sermsg = new ServerMsg();
////			sermsg.setResult(ServerMsg.ERROR_NO_DATA);
////			sermsg.setResultmsg(AppEngine.getInstance().getContext().getString(R.string.search_nodata2));
////			Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_DPCSTORE_LIST);
////			message.obj = sermsg;
////			uiHandle.sendMessage(message);
////		}
////	}
//
//	private void getMy_Created_Store_List(JSONTokener tk)
//	{
//		ServerPageData page = new ServerPageData(); // 这个要废弃
//		SearchStoreResult ssr = null;
//		try
//		{
//			// tk.nextValue();
//			JSONObject msg = (JSONObject) tk.nextValue();
//
//			int totalNum = 0;
//			List<Store> stores = new ArrayList<Store>();
//			// ServerPageData page=new ServerPageData();
//			getsend_msg(msg, page);
//			if (!msg.isNull("total"))
//			{
//				totalNum = msg.getInt("total");
//				page.setTotalcount(msg.getInt("total"));
//			}
//			if (!msg.isNull("pagecount"))
//			{
//				page.setPagecount(msg.getInt("pagecount"));
//			}
//			if (!msg.isNull("pageindex"))
//			{
//				page.setPagecount(msg.getInt("pageindex"));
//			}
//			if (!msg.isNull("list"))
//			{
//				JSONArray ar = msg.getJSONArray("list");
//				for (int i = 0; i < ar.length(); i++)
//				{
//					// System.out.println("fief"+ar.getString(i));
//					stores.add(this.getsendStore_row(ar.getJSONObject(i)));
//					// System.out.println("ffdf"+ar.getJSONObject(i).names().getString(0));
//				}
//			}
//			page.setMsg(stores);
//
//			// /ssr = new SearchStoreResult(totalNum, stores);
//			Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_MY_CREATED_STORE);
//			message.obj = page;
//			uiHandle.sendMessage(message);
//		}
//		catch (Exception ex)
//		{
//			ex.printStackTrace();
//			ServerMsg sermsg = new ServerMsg();
//			sermsg.setResult(ServerMsg.ERROR_NO_DATA);
//			sermsg.setResultmsg(AppEngine.getInstance().getContext().getString(R.string.search_nodata2));
//			Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_MY_CREATED_STORE);
//			message.obj = sermsg;
//			uiHandle.sendMessage(message);
//		}
//	}
//
//	private void getFoot_List(JSONTokener tk)
//	{
//		ServerPageData page = new ServerPageData(); // 这个要废弃
//		SearchStoreResult ssr = null;
//		try
//		{
//			// tk.nextValue();
//			JSONObject msg = (JSONObject) tk.nextValue();
//
//			int totalNum = 0;
//			List<MyFoot> myFoot = new ArrayList<MyFoot>();
//			// ServerPageData page=new ServerPageData();
//			getsend_msg(msg, page);
//			if (!msg.isNull("total"))
//			{
//				totalNum = msg.getInt("total");
//				page.setTotalcount(msg.getInt("total"));
//			}
//			if (!msg.isNull("pagecount"))
//			{
//				page.setPagecount(msg.getInt("pagecount"));
//			}
//			if (!msg.isNull("pageindex"))
//			{
//				page.setPagecount(msg.getInt("pageindex"));
//			}
//			if (!msg.isNull("list"))
//			{
//				JSONArray ar = msg.getJSONArray("list");
//				for (int i = 0; i < ar.length(); i++)
//				{
//					MyFoot m = new MyFoot();
//					m.mTime = ar.getJSONObject(i).getString("FTime");
//					m.mActionID = ar.getJSONObject(i).getLong("FActionId");
//					m.mActionType = ar.getJSONObject(i).getInt("FActionType");
//					m.mQQID = ar.getJSONObject(i).getLong("FActionQq");
//					if (!ar.getJSONObject(i).isNull("sportInfo"))
//					{
//						m.mSportID = ar.getJSONObject(i).getJSONObject("sportInfo").getLong("FSportId");
//						m.mStoreName = ar.getJSONObject(i).getJSONObject("sportInfo").getString("FName");
//						// msg.getString("")
//					}
//
//					myFoot.add(m);
//				}
//
//			}
//			page.setMsg(myFoot);
//
//			// ssr = new SearchStoreResult(totalNum, stores);
//			Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_FOOT_LIST);
//			message.obj = page;
//			uiHandle.sendMessage(message);
//		}
//		catch (Exception ex)
//		{
//			ex.printStackTrace();
//			ServerMsg sermsg = new ServerMsg();
//			sermsg.setResult(ServerMsg.ERROR_NO_DATA);
//			sermsg.setResultmsg(AppEngine.getInstance().getContext().getString(R.string.search_nodata2));
//			Message message = uiHandle.obtainMessage(AppEngine.ACTION_GET_FOOT_LIST);
//			message.obj = sermsg;
//			uiHandle.sendMessage(message);
//		}
//	}
//	
//	private void getFeeds(JSONTokener tk)
//	{
//		ServerPageData page = new ServerPageData(); // 这个要废弃
//		try
//		{
//			JSONArray msg = (JSONArray) tk.nextValue();
//			List<Feeds> listFeed = new ArrayList<Feeds>();
//			for (int i = 0; i < msg.length(); i++)
//			{
//					Feeds f = new Feeds();
//					f.title = msg.getJSONObject(i).getString("title");
//					f.url = msg.getJSONObject(i).getString("url");
//					Log.i("Feeds", f.title.toString() + f.url.toString());
//					listFeed.add(f);
//				
//			}
//			page.setMsg(listFeed);
//			
//			Message message = uiHandle.obtainMessage(AppEngine.ACTION_FEEDS);
//			message.obj = page;
//			uiHandle.sendMessage(message);
//		}
//		catch (Exception ex)
//		{
//			ex.printStackTrace();
//			ServerMsg sermsg = new ServerMsg();
//			sermsg.setResult(ServerMsg.ERROR_NO_DATA);
//			sermsg.setResultmsg(AppEngine.getInstance().getContext().getString(R.string.search_nodata2));
//			Message message = uiHandle.obtainMessage(AppEngine.ACTION_FEEDS);
//			message.obj = sermsg;
//			uiHandle.sendMessage(message);
//		}
//	}
//
//	private void getStore_Detail(JSONObject msg)
//	{
//
//	}
//
//	private void getMessage(JSONTokener tk, int action)
//	{
//		ServerPageData page = new ServerPageData();
//		try
//		{
//			Tools.debug("msessage:" + tk.toString());
//			JSONObject msg = (JSONObject) tk.nextValue();
//
//			getsend_msg(msg, page);
//
//		}
//		catch (Exception ex)
//		{
//			ServerMsg sermsg = new ServerMsg();
//			sermsg.setResult(-100);
//			sermsg.setResultmsg("无数据返回");
//			page.setMessage(sermsg);
//		}
//		Message message = uiHandle.obtainMessage(action);
//		message.obj = page;
//		uiHandle.sendMessage(message);
//	}
//
//	private void getsendget_new(JSONObject msg)
//	{
//		try
//		{
//			ServerPageData page = new ServerPageData();
//			getsend_msg(msg, page);
////			System.out.println("meff" + page.getMessage().getResultmsg());
//			Message message = uiHandle.obtainMessage(AppEngine.ACTION_SEND_STORE_UP);
//			message.obj = page;
//			uiHandle.sendMessage(message);
//		}
//		catch (Exception ex)
//		{
//
//		}
//	}
//
//	/**
//	 * 提起服务端返回的消息
//	 * 
//	 * @param msg
//	 * @param page
//	 */
//	private void getsend_msg(JSONObject msg, ServerPageData page)
//	{
//		try
//		{
//			ServerMsg sermsg = new ServerMsg();
//			if (!msg.isNull("result"))
//			{
//				sermsg.setResult(msg.getInt("result"));
//			}
//			if (!msg.isNull("resultmsg"))
//			{
//				// sermsg.setResultmsg(new
//				// String(msg.getString("resultmsg").getBytes(),"gb2312"));
//				sermsg.setResultmsg(msg.getString("resultmsg"));
//				// System.out.p
//			}
//			page.setMessage(sermsg);
//		}
//		catch (Exception ex)
//		{
//			getErrorMessage(msg);
//		}
//
//	}
//
//
//	private boolean getErrorMessage(JSONObject msg)
//	{
//		Tools.debug(">>getErrorMessage");
//		// ServerError serr = new ServerError(msg.optInt("code", -1),
//		// msg.optString("msg", "未知错误."));
//		ServerMsg sermsg = new ServerMsg();
//		sermsg.setResult(msg.optInt("code", -1));
//		sermsg.setResultmsg(msg.optString("resultmsg", "未知错误."));
//		ServerPageData page = new ServerPageData();
//		page.setMessage(sermsg);
//		// ServerPageData page=new ServerPageData();
//		// msg.optInt("code", -1)
//		// Tools.debug("get server error response, code:" + serr.code + ", msg:"
//		// + serr.msg);
//
//		Message message = uiHandle.obtainMessage(AppEngine.MSG_SERVER_ERROR);
//		message.obj = page;
//		uiHandle.sendMessage(message);
//
//		return true;
//	}
//
//	/**
//	 * 解析场馆的一条数据，有些属性根据环境不同而获得
//	 * 此方法也可以做为获得场馆详细信息的方法
//	 * 
//	 * @param msg
//	 * @return
//	 */
//	public static Store getsendStore_row(JSONObject msg)
//	{
//		/*
//		 * {name:"南山区夜妙媛健身中心",id:0,check:1,distance:500,star:4.5,up:500,icon:"http://www.qq.com/phone/1.png"
//		 * ,address:"广东省深圳市南山区百富大厦123号",city:"深圳市",province:"广东省",area:"南山",
//		 * activityid
//		 * :0,totalRank:6000,localRank:130,starCount:50,jingdu:56.67,weidu
//		 * :34.56,
//		 * fee:20,classes:"篮球",type:2,phone:4775775757,qq:403488558,email:
//		 * "403488558.qq.com"
//		 * ,textstr:"最好的健身俱乐部",creatorName:"地主10号",evaluatecount
//		 * :200,supportcount:100}
//		 */
//		Store sp = new Store();
//		try
//		{
//			if (!msg.isNull("id"))
//			{
//
//				sp.setId(msg.getLong("id"));
////				System.out.println("id" + sp.getId());
//			}
//			if (!msg.isNull("name"))
//			{
//				sp.setName(msg.getString("name"));
////				System.out.println("name" + sp.getName());
//			}
//			if (!msg.isNull("distance"))
//			{
//				try
//				{
//					sp.setDistance(msg.getInt("distance"));
//				}
//				catch (Exception e)
//				{
//				}
//			}
//			if (!msg.isNull("star"))
//			{
//				sp.setStar((float) msg.getDouble("star"));
//			}
//			if (!msg.isNull("up"))
//			{
//				sp.setUp(msg.getLong("up"));
//			}
//			if (!msg.isNull("fee"))
//			{
//				sp.setFee(msg.getString("fee"));
//			}
//			if (!msg.isNull("icon"))
//			{
//				sp.setIcon(msg.getString("icon"));
////				System.out.println("rier" + sp.getIcon());
//			}
//			if (!msg.isNull("address"))
//			{
//				String Add = msg.getString("address");
//				if(Add.startsWith("&quot;")){
//					Add = Add.replace("&quot;", "");
//				}
//				sp.setAddress(Add);
//			}
//			if (!msg.isNull("city"))
//			{
//				sp.setCity(msg.getString("city"));
//			}
//			if(!msg.isNull("email"))
//			{
//				sp.setEmail(msg.getString("email"));
//			}
//			if (!msg.isNull("province"))
//			{
//				sp.setProvince(msg.getString("province"));
//			}
//			if (!msg.isNull("area"))
//			{
//				sp.setArea(msg.getString("area"));
//			}
//			if (!msg.isNull("activityurl") || !msg.isNull("activity"))
//			{
//				try
//				{
//					if(!msg.isNull("activityurl"))
//					{
//						sp.setActivityurl(msg.getInt("activityurl"));					
//					}
//					else if(!msg.isNull("activity"))
//					{
//						sp.setActivityurl(msg.getInt("activity"));					
//					}
//					
//				}
//				catch (Exception e) {
//				}
//				Tools.debug("activityurl:" + sp.getActivityurl());
//			}
//			if (!msg.isNull("totalRank"))
//			{
//				sp.setTotalRank(msg.getJSONObject("totalRank").getLong("totalRank"));
//
//			}
//			if (!msg.isNull("localRank"))
//			{
//				sp.setLocalRank(msg.getJSONObject("localRank").getLong("localRank"));
//			}
//			if (!msg.isNull("starCount"))
//			{
//				sp.setStarCount(msg.getLong("starCount"));
//			}
//			if (!msg.isNull("lon"))
//			{
//				sp.setJingdu(msg.getDouble("lon"));
//			}
//			if (!msg.isNull("lat"))
//			{
//				sp.setWeidu(msg.getDouble("lat"));
//			}
//			if (!msg.isNull("status"))
//			{
//				sp.setCheck(msg.getInt("status"));
//			}
//			/**
//			 * 读取相册地址
//			 */
//			if (!msg.isNull("picture"))
//			{
//				List<ImageUrl> imglist = new ArrayList<ImageUrl>();
//				int flage = 0;
//				try
//				{
//					JSONArray ar = msg.getJSONArray("picture");
//					for (int i = 0; i < ar.length(); i++)
//					{
//						ImageUrl imgpic = new ImageUrl();
//						imgpic.setSmlurl(ar.getJSONObject(i).getString("smlurl"));
//						imgpic.setBigurl(ar.getJSONObject(i).getString("bigurl"));
//						imglist.add(imgpic);
//					}
//				}
//				catch (Exception ex)
//				{
//					flage = 1;
//				}
//				// if (flage == 0)
//				// {
//				// JSONObject pic = msg.getJSONObject("picture");
//				// ImageUrl imgpic = new ImageUrl();
//				// imgpic.setSmlurl(pic.getString("smlurl"));
//				// imgpic.setBigurl(pic.getString("bigurl"));
//				// imglist.add(imgpic);
//				// }
//				sp.setAlbums(imglist);
//			}
//			if (!msg.isNull("classes"))
//			{
//				sp.setClasses(msg.getString("classes"));
//			}
//			if (!msg.isNull("type"))
//			{
//				// 后台在这暂时还有问题
//				try
//				{
//					sp.setType(msg.getString("type"));
//				}
//				catch (Exception e)
//				{
//				}
//			}
//			if (!msg.isNull("phone"))
//			{
//				sp.setPhone(msg.getString("phone"));
//			}
//			if (!msg.isNull("contactQQ"))
//			{
//				if (!msg.getString("contactQQ").trim().equals(""))
//				{
//					sp.setQq(Long.parseLong(msg.getString("contactQQ").trim()));
//				}
//			}
//			if (!msg.isNull("textstr"))
//			{
//				String desc = msg.getString("textstr");
//				if(desc.startsWith("&quot;")){
//					desc = desc.replace("&quot;", "");
//				}
//				sp.setTextstr(desc);
//			}
//			if (!msg.isNull("createrName"))
//			{
//				sp.setCreatorName(msg.getString("createrName"));
//			}
//			if (!msg.isNull("evaluatecount"))
//			{
//				sp.setEvaluatecount(msg.getLong("evaluatecount"));
//			}
//			if (!msg.isNull("supportcount"))
//			{
//				sp.setSupportcount(msg.getLong("supportcount"));
//			}
//
//
//
//		}
//		catch (Exception ex)
//		{
//			// getErrorMessage(msg);
//			ex.printStackTrace();
//		}
//		return sp;
//	}

}
