package com.funoble.myarstation.update;

public class DownloadInfo {

	public int version;
	public int startPos;// 开始点
	public int endPos;// 结束点
	public int compeleteSize;// 完成度
	public int compelete;// 是否完成1完成0未完成
	
	public DownloadInfo(int version, int startPos, int endPos, int compeleteSize, int compelete) {
		super();
		this.version = version;
		this.startPos = startPos;
		this.endPos = endPos;
		this.compeleteSize = compeleteSize;
		this.compelete = 0;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DownloadInfo [version=" + version + ", startPos=" + startPos
				+ ", endPos=" + endPos + ", compeleteSize=" + compeleteSize
				+ "]";
	}
}
