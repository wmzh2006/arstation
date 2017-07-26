package com.funoble.myarstation.update;

public interface DownloadProgressListener {
	public void onDownloadSize(int size);
	public void onDownloadFinish(int size, String fileName);
	public void onError(int aErrorID);
}
