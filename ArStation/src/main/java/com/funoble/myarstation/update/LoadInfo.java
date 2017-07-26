package com.funoble.myarstation.update;

public class LoadInfo {

	public int fileSize;// 文件大小
	public int complete;// 完成度
	public int version;
	
	public LoadInfo() {
		
	}
	
	public LoadInfo(int fileSize, int complete, int version) {
		this.fileSize = fileSize;
		this.complete = complete;
		this.version = version;
	}
	
}
