package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;

public class RetMsg {

	public  int						action;

	public  String					url;
	
	public  int 					retCode;
	
	public  String					retMsg;
	
	public ByteBuffer				data;
	
	public RetMsg(int action, String url, ByteBuffer data) {
		this.action = action;
		this.url = url;
		this.retCode  = -1;
		this.retMsg = "";
		this.data = data;
	}

	public String getUrl() {
		return url;
	}

	public int getAction() {
		return action;
	}

}
