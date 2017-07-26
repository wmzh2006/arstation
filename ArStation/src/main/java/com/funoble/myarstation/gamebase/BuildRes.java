package com.funoble.myarstation.gamebase;

//import java.io.*;


import java.io.DataInputStream;
public class BuildRes extends BaseRes{
	public int buildType = 1;
	public String name = "";
	public boolean iFlip = false;
	
	public BuildRes(BuildRes res) {
		iType = RES_BUILD;
		iID = res.iID;
		buildType = res.buildType;
		name = res.name;
		iFlip = res.iFlip;
	}
	public BuildRes() {
		iType = RES_BUILD;
		iID = 0;
		buildType = 0;
		name = "";
		iFlip = false;
	}
	
	public void intputStream(DataInputStream  dis)throws Exception
	{
		iID = dis.readInt();
		buildType = dis.readInt();
		name = dis.readUTF();
		iFlip = dis.readByte() == 1 ? true : false;
	}
}