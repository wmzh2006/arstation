/*******************************************************************************
 * Copyright (C) 1998-2009 BBG Inc.All Rights Reserved.
 * FileName:   ImageDataManager.java 
 * Description：
 * History：
 * 版本号   作者       日期      简要介绍相关操作
 * 1.0     sunny    2009-11-22   Create
 *******************************************************************************/
package  com.funoble.myarstation.data.cach;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * <p>
 * 图片管理器
 * </p>
 */
public class ImageDataManager {
    /**
     * 图片存放的sd卡路径
     */
    private static final String pathImage = "/lyingdice/cache/img/";

    /**
//     * 根据文件名从本地sd卡读取图片
     * @param fileName
     * @return
     */
    public static Bitmap loadBitmapByFileName(String fileName) {
    	Bitmap img = null;
    	
    	/*sdcard可读状态*/
    	if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
    		File sdCard = android.os.Environment.getExternalStorageDirectory();
    		FileInputStream fis = null;
    		try {
    			File file = new File(sdCard.getPath()+pathImage+fileName);  
    			fis = new FileInputStream(file);
    			img = BitmapFactory.decodeStream(fis);
    			
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
    	}
    	return img;
    }
    
    /**
//     * 根据文件名从本地sd卡读取图片
     * @param fileName
     * @return
     */
    public static Bitmap loadBitmapByFileName(String aFolderName, String fileName) {
    	Bitmap img = null;
    	
    	/*sdcard可读状态*/
    	if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
    		File sdCard = android.os.Environment.getExternalStorageDirectory();
    		FileInputStream fis = null;
    		try {
    			File file = new File(sdCard.getPath()+pathImage+aFolderName + "/" +fileName);  
    			fis = new FileInputStream(file);
    			img = BitmapFactory.decodeStream(fis);
    			
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
    	}
    	return img;
    }
    
    /**
     * 保存图片到文件
     * @param data
     * @param fileName
     */
    public static void saveImage2SDCard(byte[] data, String fileName) {
        if(data != null && data.length > 0) {
            /*sdcard可读状态*/
            if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                File sdCard = android.os.Environment.getExternalStorageDirectory();
                FileOutputStream fos = null;
                try {
                    File file = new File(sdCard.getPath()+pathImage); 
                    if(!file.exists()) {  //创建目录
                        file.mkdirs();
                    }
                    else {
                        File[] files = file.listFiles();
                        if(files != null && files.length > 200) {//最多存储200张图片
                            for(int i = 0, len = files.length; i < len; i++)
                            {
                                files[i].delete();
                            }
                        }
                    }
                    File imgFile = new File(sdCard.getPath()+pathImage+fileName);
                    if(!imgFile.exists()) { //已经存在则不管
                        fos = new FileOutputStream(imgFile);
                        fos.write(data);
                        fos.flush();
                        data = null;                    
                    }
                }
                catch(Exception e) {
                	
                }
                finally {
                    if(fos != null) {
                        try {
                            fos.close();
                        }
                        catch(Exception e){}
                    }
                }
            }          
        }
    }
    /**
     * 保存图片到文件
     * @param data
     * @param fileName
     */
    public static void saveImage2SDCard(byte[] data, String aFolderName, String fileName, boolean aCover) {
    	if(data != null && data.length > 0) {
    		/*sdcard可读状态*/
    		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
    			File sdCard = android.os.Environment.getExternalStorageDirectory();
    			FileOutputStream fos = null;
    			try {
    				File file = new File(sdCard.getPath()+pathImage + aFolderName + "/"); 
    				if(!file.exists()) {  //创建目录
    					file.mkdirs();
    				}
    				else {
    					File[] files = file.listFiles();
    					if(files != null && files.length > 200) {//最多存储200张图片
    						for(int i = 0, len = files.length; i < len; i++)
    						{
    							files[i].delete();
    						}
    					}
    				}
    				File imgFile = new File(sdCard.getPath()+pathImage + aFolderName + "/" +fileName);
    				if(!imgFile.exists() || aCover) { //已经存在则不管
    					fos = new FileOutputStream(imgFile);
    					fos.write(data);
    					fos.flush();
    					data = null;                    
    				}
    			}
    			catch(Exception e) {
    				
    			}
    			finally {
    				if(fos != null) {
    					try {
    						fos.close();
    					}
    					catch(Exception e){}
    				}
    			}
    		}          
    	}
    }
    
    /**
     * 删除图片
     * @param fileName
     * @return
     */
    public static boolean deleteImage(String fileName) {
    	boolean delete = false;
        /*sdcard可读状态*/
        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File sdCard = android.os.Environment.getExternalStorageDirectory();
            try {
                File file = new File(sdCard.getPath()+pathImage+fileName); 
                if(!file.exists()) {  
                	delete = false;
                }
                else {
                	delete = file.delete();
                }
            }
            catch(Exception e) {
            	
            }
        }   
        return delete;
    }
    /**
     * 删除图片
     * @param fileName
     * @return
     */
    public static boolean deleteFolderByUserID(String aFolderName) {
    	boolean delete = false;
    	/*sdcard可读状态*/
    	if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
    		File sdCard = android.os.Environment.getExternalStorageDirectory();
    		try {
    			File file = new File(sdCard.getPath()+pathImage+aFolderName+"/"); 
    			if(!file.exists()) {  
    				delete = false;
    			}
    			else if(file.isDirectory()){
    				File[] files = file.listFiles();
					for(int i = 0; i < files.length; i++) {
						delete = files[i].delete();
					}
    			}
    			else {
    				delete = file.delete();
    			}
    		}
    		catch(Exception e) {
    			
    		}
    	}   
    	return delete;
    }
}
