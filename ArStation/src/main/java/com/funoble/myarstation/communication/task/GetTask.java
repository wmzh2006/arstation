package com.funoble.myarstation.communication.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.funoble.myarstation.communication.RestMsg;
import com.funoble.myarstation.imagemanager.ImageSaveManager;

import java.io.File;
import java.io.FileInputStream;

/**
 * GET 任务
 * 
 * @author sunny
 */

public class GetTask extends Task {
	private ImageSaveManager ImageSave;
	
    private final String pathBase = "/lyingdice/.cache/.res/";
    
	private File sdCard = android.os.Environment.getExternalStorageDirectory();
	private String path = sdCard.getPath()+pathBase;
	
	public GetTask(RestMsg restMsg, Handler handler) {
		super(restMsg, handler);
		ImageSave = new ImageSaveManager();
	}

	@Override
	public void run() {
//		if (msg == null) {
//			return;
//		}
//
//		byte[] byteArrayResponse = HttpClientExecutor.getInstance().executeGet(
//				msg.getUrl(), msg.getAction(), this.handler);
//		if (byteArrayResponse != null) {
//			try {
//				if(this.msg.getAction() == HTTPERROR.MSG_GET_IMAGE) {
//					Message message = this.handler
//					.obtainMessage(HTTPERROR.MSG_GET_IMAGE);
//					message.obj = byteArrayResponse;
//					Bundle data = new Bundle();
//					data.putString("Name", Util.getPicNameByUrlPath(msg.getUrl()));
//					message.setData(data);
//
//					//test-----------------
//					byte[] pixels = (byte[]) message.obj;
//					String picName = message.getData().getString("Name");
//					saveImage(pixels, picName);
////					getImage(picName);
//					//test-----------------
//
//					message.sendToTarget();
//				}
//				else if(this.msg.getAction() == HTTPERROR.MSG_GET_GOODSICON) {
//					Message message = this.handler
//					.obtainMessage(HTTPERROR.MSG_GET_GOODSICON);
//					message.obj = byteArrayResponse;
//					Bundle data = new Bundle();
//					data.putString("Name", Util.getPicNameByUrlPath(msg.getUrl()));
//					message.setData(data);
//
//					//test-----------------
//					byte[] pixels = (byte[]) message.obj;
//					String picName = message.getData().getString("Name");
//					saveImage(pixels, picName, path);
////					getImage(picName);
//					//test-----------------
//
//					message.sendToTarget();
//				}
//				else if(this.msg.getAction() == HTTPERROR.MSG_GET_PAK) {
//					Message message = this.handler
//							.obtainMessage(HTTPERROR.MSG_GET_PAK);
//					message.obj = byteArrayResponse;
//					Bundle data = new Bundle();
//					data.putString("Name", Util.getPicNameByUrlPath(msg.getUrl()));
//					message.setData(data);
//
//					//test-----------------
//					byte[] pixels = (byte[]) message.obj;
//					String picName = message.getData().getString("Name");
//					savePak(pixels, picName, DataPath.pakPath);
//					//test-----------------
//
//					message.sendToTarget();
//				}
//			} catch (Exception e) {
//				if (handler != null) {
//					handler.sendEmptyMessage(HTTPERROR.MSG_PARSE_HTTP_ERRO);
//				}
//				e.printStackTrace();
//			}
//			Tools.debug("response bytes size:" + byteArrayResponse.length);
//		}
//	}
//
//	public void saveImage(byte[] bmp, String fileName) {
//		if (bmp != null) {
//			try {
//				ImageSave.saveImage2SDCard(bmp, fileName);
//			}
//			catch (OutOfMemoryError e) {
//				// 再试一次
//				try {
//					ImageSave.saveImage2SDCard(bmp,fileName);
//				}
//				catch (Exception e2) {
//				}
//			}
//			catch (Exception e) {
//
//			}
//		}
	}
	
	public void saveImage(byte[] bmp, String fileName, String path) {
		if (bmp != null) {
			try {
				ImageSave.saveImage2SDCard(bmp, fileName, path, 5);
			}
			catch (OutOfMemoryError e) {
				// 再试一次
				try {
					ImageSave.saveImage2SDCard(bmp,fileName, path, 5);
				}
				catch (Exception e2) {
				}
			}
			catch (Exception e) {
				
			}
		}
	}
	
	public void savePak(byte[] bmp, String fileName, String path) {
		if (bmp != null) {
			try {
				ImageSave.savePak2SDCard(bmp, fileName, path, 5);
			}
			catch (OutOfMemoryError e) {
				// 再试一次
				try {
					ImageSave.savePak2SDCard(bmp,fileName, path, 5);
				}
				catch (Exception e2) {
				}
			}
			catch (Exception e) {
				
			}
		}
	}
	
	public Bitmap getImage(String aFileName) {
		Bitmap img = null;
		if(aFileName == null || aFileName.length() <= 0) {
			return img;
		}
		FileInputStream fis = null;
		BitmapFactory.Options opt = new BitmapFactory.Options();  
		opt.inPreferredConfig = Bitmap.Config.RGB_565;   
		opt.inPurgeable = true;  
		opt.inInputShareable = true;  
		try {
			img = ImageSave.loadRoundedCornerBitmap(aFileName);
		}
		catch(Exception e) {
		}
		finally {
			if(fis != null) {
				try {
					fis.close();
				}
				catch(Exception e){
					
				}
			}
		}
		return img;
	}
}
