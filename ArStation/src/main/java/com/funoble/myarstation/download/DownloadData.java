/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: DownloadData.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-7-19 下午06:32:16
 *******************************************************************************/
package com.funoble.myarstation.download;

import java.util.Arrays;


public class DownloadData {
	public String iFileName;
	public int iFileTotalSize;
	public int iCurrentSize;
	public int iStartIndex;
	private boolean ibComplete;
	public byte[]	data;
	
	public DownloadData() {
		iFileTotalSize = 0;
		iCurrentSize = 0;
		iStartIndex = 0;
		ibComplete = false;
		data = new byte[0];
	}

	public boolean isComplete() {
		if(iFileTotalSize > 0 && iFileTotalSize == iStartIndex) {
			return ibComplete = true;
		}
		return ibComplete = false;
	}
	/* 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if(this.iFileName.equals(((DownloadData)o).iFileName)) {
			return true;
		}
		return false;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DownloadData [iFileName=" + iFileName + ", iFileTotalSize="
				+ iFileTotalSize + ", iCurrentSize=" + iCurrentSize
				+ ", iStartIndex=" + iStartIndex + ", ibComplete=" + ibComplete
				+ ", data=" + Arrays.toString(data) + "]";
	}
	
}
