package com.funoble.myarstation.communication;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Http Rest 交互消息
 * 
 * @author sunny
 */

public class RestMsg
{
	private int					action;

	private String				url;

	private Map<String, String>	params;

	private InputStream			is;
	private OutputStream			os;
	
	private File 				file;
	// private final Object protocolControl;

	/**
	 * 构造POST
	 * 
	 * @param action
	 * @param url
	 * @param params
	 * @param pc protocolControl 协议实例
	 */
	public RestMsg(int action, String url, InputStream is)
	{
		this.action = action;
		this.url = url;
		this.is = is;

	}
	
	/**
	 * 构造POST
	 * 
	 * @param action
	 * @param url
	 * @param params
	 * @param pc protocolControl 协议实例
	 */
	public RestMsg(int action, String url, Map<String, String> params)
	{
		this.action = action;
		this.url = url;
		this.params = params;
		
	}
	
	/**
	 * 构造POST
	 * 
	 * @param action
	 * @param url
	 * @param params
	 * @param pc protocolControl 协议实例
	 */
	public RestMsg(int action, String url, File file)
	{
		this.action = action;
		this.url = url;
		this.file = file;
		
	}

	/**
	 * 构造GET
	 * 
	 * @param action
	 * @param url
	 */
	public RestMsg(int action, String url)
	{
		this.action = action;
		this.url = url;


	}



	public int getAction()
	{
		return action;
	}

	public void setAction(int action)
	{
		this.action = action;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public Map<String, String> getParams()
	{
		return params;
	}

	public void setParams(Map<String, String> params)
	{
		this.params = params;
	}

	public File getFile() {
		return file;
	}
	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RestMsg [action=" + action + ", url=" + url + "]";
	}


}
