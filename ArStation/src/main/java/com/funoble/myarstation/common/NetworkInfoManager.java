/*******************************************************************************
 * Copyright (C) 1998-2009 ddl Inc.All Rights Reserved.
 * FileName: MProxyInfoV2.java
 * Description：
 * History：
 * 版本号 作者 日期 简要介绍相关操作
 * 1.0 sunny 2009-9-14 Create
 *******************************************************************************/

package com.funoble.myarstation.common;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;

/**
 * <p>
 * 接入点信息
 * </p>
 */
public class NetworkInfoManager
{

	/**
	 * 电信的wap网关地址
	 */
	public static final String	PROXY_CTWAP			= "10.0.0.200";
	/**
	 * cmwap
	 */
	public static final int		MPROXYTYPE_CMWAP	= 1;

	/**
	 * wifi
	 */
	public static final int		MPROXYTYPE_WIFI		= 2;

	/**
	 * cmnet
	 */
	public static final int		MPROXYTYPE_CMNET	= 4;

	/**
	 * uninet服务器列表
	 */
	public static final int		MPROXYTYPE_UNINET	= 8;

	/**
	 * uniwap服务器列表
	 */
	public static final int		MPROXYTYPE_UNIWAP	= 16;

	/**
	 * net类服务器列表
	 */
	public static final int		MPROXYTYPE_NET		= 32;

	/**
	 * wap类服务器列表
	 */
	public static final int		MPROXYTYPE_WAP		= 64;

	/**
	 * 默认服务器列表
	 */
	public static final int		MPROXYTYPE_DEFAULT	= 128;

	/**
	 * 代理IP, 在apn为wap类型时有效
	 */
	private static String		sProxy_host			= null;

	/**
	 * 代理端口, 在apn为wap类型时有效
	 */
	private static int			sProxy_port			= 0;

	/**
	 * 作用：
	 * <p>
	 * 获取代理IP, 在apn为wap类型时有效
	 * </p>
	 * 
	 * @return
	 */
	public static String getProxyHost()
	{
		return sProxy_host;
	}

	/**
	 * 作用：
	 * <p>
	 * 获取代理端口, 在apn为wap类型时有效
	 * </p>
	 * 
	 * @return
	 */
	public static int getProxyPort()
	{
		return sProxy_port;
	}

	/**
	 * 
	 * 作用：
	 * <p>
	 * 获取代理url
	 * </p>
	 * 
	 * @return
	 */
	public static String getProxyUrl()
	{
		if (sProxy_host == null || sProxy_host.length() == 0)
		{
			return null;
		}
		if (sProxy_port <= 0)
		{
			return null;
		}
		return "http://" + sProxy_host + ":" + sProxy_port;

	}

	/**
	 * 支持类型能否符合特定的type
	 * 
	 * @param supportedType
	 * @param type 特定网络类型
	 * @return
	 */
	public static boolean isCanUse(int supportedType, int type)
	{
		return supportedType != 0 && (supportedType & type) == type;
	}

	/**
	 * 获取当前联网类型
	 * 
	 * @return
	 */
	public static int getNetWorkType()
	{
		return getNetWorkType(MyArStation.getInstance().getApplicationContext());
	}

	/**
	 * 获取当前联网类型
	 * 
	 * @param act 当前活动Activity
	 * @return 联网类型 -1表示未知的联网类型, 正确类型： MPROXYTYPE_WIFI | MPROXYTYPE_CMWAP |
	 *         MPROXYTYPE_CMNET
	 */
	private static int getNetWorkType(Context context)
	{
		try
		{
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			String typeName = info.getTypeName();
			if (typeName.toUpperCase().equals("WIFI")) // wifi网络
			{
				return MPROXYTYPE_WIFI;
			}
			else
			{
				String extraInfo = info.getExtraInfo().toLowerCase();
				if (extraInfo == null)
				{ // 默认当net类处理
					return MPROXYTYPE_NET;
				}

				// 转换为小写再处理
				extraInfo = extraInfo.toLowerCase();
				Tools.debug("extraInfo:" + extraInfo);
				if (extraInfo.equals("cmwap")) // cmwap
				{
					setProxyInfo();
					return MPROXYTYPE_CMWAP;
				}
				else if (extraInfo.equals("cmnet") || extraInfo.equals("epc.tmobile.com")) // cmnet
				{
					return MPROXYTYPE_CMNET;
					// 目前测试， 只有wifi有新协议，暂时这么写
					// return MPROXYTYPE_WIFI;
				}
				else if (extraInfo.equals("uniwap"))
				{
					setProxyInfo();
					return MPROXYTYPE_UNIWAP;
				}
				else if (extraInfo.equals("uninet"))
				{
					return MPROXYTYPE_UNINET;
				}
				else if (extraInfo.indexOf("wap") != -1)
				{ // wap类
					setProxyInfo();
					return MPROXYTYPE_WAP;
				}
				else if (extraInfo.indexOf("net") != -1)
				{ // net 类
					return MPROXYTYPE_NET;
				}
			}

		}
		catch (Exception e)
		{
		}

		return -1;

	}

	private static void setProxyInfo()
	{
		sProxy_host = android.net.Proxy.getDefaultHost();
		sProxy_port = android.net.Proxy.getDefaultPort();
	}

}
