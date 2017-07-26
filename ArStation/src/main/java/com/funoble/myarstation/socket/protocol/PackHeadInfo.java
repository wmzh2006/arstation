package com.funoble.myarstation.socket.protocol;

import java.nio.ByteBuffer;


public class PackHeadInfo {
	public 	int 		iPackHeadLen = 0;
	protected	char 		iPlatform;		//<item name="Platform"	type="char"		desc="终端平台" />					1
	protected	short 		iProtocolVer;	//<item name="ProtocolVer"	type="short"		desc="协议版本号" />		2
	protected 	int 		iPakLength;		//<item name="Len"	type="int"		desc="包长字段" len="INCLUDE"/>			4
	protected	int  		iHallVer;		//<item name="Ver"	type="int"		desc="大厅版本号" />					4
	protected	int 		iUid;			//<item name="Uin"	type="int"		desc="QQ号码" />						4 
	protected	String 		iMoblieName;	// <item name="MobileType"	type="char"	size="array2"		desc="手机型号" />	arrary2
	protected 	String 		iExString;		//<item n
	
	public 		boolean		lega  = false;
	public PackHeadInfo() {
		init();
	}

	
	public PackHeadInfo(char aPlatform, short aProtocolVer, int aPakLength, 
			int aHallVer, int aUid, String aMoblieName, String aExString) {
		this.iPlatform = aPlatform;
		this.iProtocolVer = aProtocolVer;
		this.iPakLength = aPakLength;
		this.iHallVer = aHallVer;
		this.iUid = aUid;
		this.iMoblieName = aMoblieName;
		this.iExString = aExString;
	}

	public void init() {
		this.iPlatform = 'A';
		this.iProtocolVer = 1;
		this.iPakLength = 0;
		this.iHallVer = 1000;
		this.iUid = 0;
		this.iMoblieName = "android";
		this.iExString = " android";
	}


	public char getPlatform() {
		return iPlatform;
	}


	public void setPlatform(char aPlatform) {
		this.iPlatform = aPlatform;
	}


	public short getProtocolVer() {
		return iProtocolVer;
	}


	public void setProtocolVer(short aProtocolVer) {
		this.iProtocolVer = aProtocolVer;
	}


	public int getPakLength() {
		return iPakLength;
	}


	public void setPakLength(int aPakLength) {
		this.iPakLength = aPakLength;
	}


	public int getHallVer() {
		return iHallVer;
	}


	public void setHallVer(int aHallVer) {
		this.iHallVer = aHallVer;
	}


	public int getUid() {
		return iUid;
	}


	public void setUid(int aUid) {
		this.iUid = aUid;
	}


	public String getMoblieName() {
		return iMoblieName;
	}


	public void setMoblieName(String aMoblieName) {
		this.iMoblieName = aMoblieName;
	}


	public String getExString() {
		return iExString;
	}


	public void setExString(String aExString) {
		this.iExString = aExString;
	}

	public ByteBuffer encodePackHeadInf() {
		 ByteBuffer bb = ByteBuffer.allocate(iPackHeadLen + 1);
		 bb.clear();
		 bb.putInt(iPakLength);
		 bb.putShort(iProtocolVer);
		 bb.putInt(iHallVer);
		 bb.put((byte) iPlatform);
		 bb.putInt(iMoblieName.length());
		 bb.put(iMoblieName.getBytes(), 0, iMoblieName.length());
		 bb.putInt(iUid);
		 int len = iExString.length();
		 bb.putInt(len);
		 len = iExString.getBytes().length;
		 bb.put(iExString.getBytes(), 0, len);
		 bb.limit(iPackHeadLen);
		 return bb;
	}
	
	public boolean decodePackHeadInf(ByteBuffer data) {
		int pos = 0;
		if(data != null) {
			byte start = data.get();
			if(start != 2) {
				return lega = false;
			}
			int packLen = data.getInt();
			if(packLen != data.limit()) {
				return lega = false;
			}
			pos = data.position();
			byte end = data.get(data.limit()-1);
			if(end != 3) {
				return lega =false;
			}
			int iProtocolVer = data.getShort();
			if(iProtocolVer != 1) {
				return lega = false;
			}
			pos = data.position();
		}
			
		return lega;
	}
	
	public int getPackHeadLen() {
		iPackHeadLen = 1 + 2 + 4 * 3 + 4 + iMoblieName.length() + 4 + iExString.length();
		return iPackHeadLen;
	}


	@Override
	public String toString() {
		return "PackHeadInfo [iPackHeadLen=" + iPackHeadLen + ", iPlatform="
				+ iPlatform + ", iProtocolVer=" + iProtocolVer
				+ ", iPakLength=" + iPakLength + ", iHallVer=" + iHallVer
				+ ", iUid=" + iUid + ", iMoblieName=" + iMoblieName
				+ ", iExString=" + iExString + ", lega=" + lega + "]";
	}
}
