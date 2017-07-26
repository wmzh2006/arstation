/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: ImageSaveManager.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-12-17 下午09:29:16
 *******************************************************************************/
package com.funoble.myarstation.imagemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.common.Util;
import com.funoble.myarstation.utils.ImageUtil;


public class ImageSaveManager {
	private final String 	TAG = "ImageSaveManager";
	   /**
     * 图片存放的sd卡路径
     */
	private File sdCard = android.os.Environment.getExternalStorageDirectory();
	private final String path = sdCard.getPath()+ "/lyingdice/cache/";
    private final String pathImage = sdCard.getPath()+ "/lyingdice/cache/img/";
    private final String pathImagehide = sdCard.getPath()+ "/lyingdice/.cache/.img/";
	private final int		MB = 1048576;
	private final int 		FREE_SD_SPACE_NEEDED_TO_CACHE = 2;//4M
	private final String 	WHOLESALE_CONV = "WHOLESALE_CONV";
	private final int 		CACHE_SIZE = 2; //M
	private static final int HARD_CACHE_CAPACITY = 30;
	private final int		mTimeDiff = 30 * 86400000;//30天
	
	public final HashMap<String, Bitmap> mHardBitmapCache = new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) { 
        /** 
		 * fields serialVersionUID 
		 */ 
		private static final long serialVersionUID = 1L;

		@Override 
        protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) { 
            if (size() >HARD_CACHE_CAPACITY) { 
               //当map的size大于30时，把最近不常用的key放到mSoftBitmapCache中，从而保证mHardBitmapCache的效率 
               mSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue())); 
                return true; 
            } else 
                return false; 
        } 
    }; 
    
    /** 
     *当mHardBitmapCache的key大于30的时候，会根据LRU算法把最近没有被使用的key放入到这个缓存中。 
     *Bitmap使用了SoftReference，当内存空间不足时，此cache中的bitmap会被垃圾回收掉 
     */ 
    private final static ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache = new ConcurrentHashMap<String,SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2); 
    
    /** 
     * 从缓存中获取图片 
     */ 
    public Bitmap getBitmapFromCache(String url) { 
        // 先从mHardBitmapCache缓存中获取 
        synchronized (mHardBitmapCache) { 
            final Bitmap bitmap =mHardBitmapCache.get(url); 
            if (bitmap != null) { 
                //如果找到的话，把元素移到linkedhashmap的最前面，从而保证在LRU算法中是最后被删除 
                mHardBitmapCache.remove(url); 
                mHardBitmapCache.put(url,bitmap); 
                return bitmap; 
            } 
        } 
        //如果mHardBitmapCache中找不到，到mSoftBitmapCache中找 
        SoftReference<Bitmap>bitmapReference = mSoftBitmapCache.get(url); 
        if (bitmapReference != null) { 
            final Bitmap bitmap =bitmapReference.get(); 
            if (bitmap != null) { 
                return bitmap; 
            } else { 
                mSoftBitmapCache.remove(url); 
            } 
        } 
        return null; 
    } 
    
    public void saveImage2SDCard(byte[] data, String fileName) {
    	Tools.debug("ImageSaveManager::saveImage2SDCard start......");
        if(data != null && data.length > 0) {
            /*sdcard可读状态*/
            if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                FileOutputStream fos = null;
                try {
                	File file1 = new File(path); 
                    File file = new File(pathImage); 
                    File fileHide = new File(pathImagehide); 
                    if(file.exists()) {
                    	file.renameTo(fileHide);
                    }
                    if(file1.exists()) {
                    	file1.delete();
                    }
                    if(!fileHide.exists()) {  //创建目录
                    	fileHide.mkdirs();
                    }
                    else {
                    	removeCache(pathImagehide);
                    	File imgFile = new File(pathImagehide+Util.getFileNameNotExt(fileName));
//                    	if(!imgFile.exists()) { //已经存在则不管
                    		fos = new FileOutputStream(imgFile);
                    		fos.write(data);
                    		fos.flush();
                    		data = null;                    
//                    	}
                    }
                }
                catch(Exception e) {
                	e.printStackTrace();
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
        Tools.debug("ImageSaveManager::saveImage2SDCard end......");
    }
    
    /**
     * @param data
     * @param fileName
     * @param path
     * @param cachSize //缓冲大小MB
     */
    public void saveImage2SDCard(byte[] data, String fileName, String path, int cachSize) {
    	Tools.debug("ImageSaveManager::saveImage2SDCard start......");
    	if(data != null && data.length > 0) {
    		/*sdcard可读状态*/
    		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
    			FileOutputStream fos = null;
    			try {
    				File file = new File(path); 
    				if(!file.exists()) {  //创建目录
    					file.mkdirs();
    				}
    				else {
    					removeCache(path, cachSize * MB);
    					File imgFile = new File(path+Util.getFileNameNotExt(fileName));
    					fos = new FileOutputStream(imgFile);
    					fos.write(data);
    					fos.flush();
    					data = null;                    
//                    	}
    				}
    			}
    			catch(Exception e) {
    				e.printStackTrace();
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
    	Tools.debug("ImageSaveManager::saveImage2SDCard end......");
    }
    
    /**
     * @param data
     * @param fileName
     * @param path
     * @param cachSize //缓冲大小MB
     */
    public void savePak2SDCard(byte[] data, String fileName, String path, int cachSize) {
    	Tools.debug("saveImage2SDCard");
    	if(data != null && data.length > 0) {
    		/*sdcard可读状态*/
    		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
    			FileOutputStream fos = null;
    			try {
    				File file = new File(path); 
    				if(!file.exists()) {  //创建目录
    					file.mkdirs();
    				}
    				else {
    					removeCache(path, cachSize * MB);
    					File imgFile = new File(path + fileName);
    					fos = new FileOutputStream(imgFile);
    					fos.write(data);
    					fos.flush();
    					data = null;                    
//                    	}
    				}
    			}
    			catch(Exception e) {
    				e.printStackTrace();
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
    
    public Bitmap loadRoundedCornerBitmap(String aFileName) throws FileNotFoundException {
    	Bitmap img = null;
    	if(externalMemoryAvailable()) {
			FileInputStream fis = null;
			BitmapFactory.Options opt = new BitmapFactory.Options();  
			File file = new File(pathImage); 
            File fileHide = new File(pathImagehide); 
			opt.inPreferredConfig = Bitmap.Config.RGB_565;   
			opt.inPurgeable = true;  
			opt.inInputShareable = true;  
			try {
                if(file.exists()) {
//                	Tools.debug("ImageSaveManager::loadRoundedCornerBitmap 242");
                	file.renameTo(fileHide);
//                	Tools.debug("ImageSaveManager::loadRoundedCornerBitmap 244");
                }
                if(!fileHide.exists()) {  //创建目录
//                	Tools.debug("ImageSaveManager::loadRoundedCornerBitmap 247");
                	fileHide.mkdirs();
//                	Tools.debug("ImageSaveManager::loadRoundedCornerBitmap 248");
                }
                else {
//               	Tools.debug("ImageSaveManager::loadRoundedCornerBitmap 252");
					File file1 = new File(pathImagehide + Util.getFileNameNotExt(aFileName));  
//					Tools.debug("ImageSaveManager::loadRoundedCornerBitmap 254");
					fis = new FileInputStream(file1);
//					Tools.debug("ImageSaveManager::loadRoundedCornerBitmap 256");
					img = BitmapFactory.decodeStream(fis, null, opt);
//					Tools.debug("ImageSaveManager::loadRoundedCornerBitmap 258");
					img = ImageUtil.getRoundedCornerBitmap(img, 7.0f);
//					Tools.debug("ImageSaveManager::loadRoundedCornerBitmap 260");
					updateFileTime(file1);
//					Tools.debug("ImageSaveManager::loadRoundedCornerBitmap 262");
                }
			}
//			catch (FileNotFoundException e) {
//				try {
//					 	if(file.exists()) {
//		                	file.renameTo(fileHide);
//		                }
//		                if(!fileHide.exists()) {  //创建目录
//		                	fileHide.mkdirs();
//		                }
//		                else {
//							File file1 = new File(pathImagehide + aFileName);  
//							fis = new FileInputStream(file1);
//							img = BitmapFactory.decodeStream(fis, null, opt);
//							img = ImageUtil.getRoundedCornerBitmap(img, 7.0f);
//							updateFileTime(file1);
//		                }
//				} catch (Exception e2) {
//				}
//			}
			finally {
					try {
						if(fis != null) {
							fis.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
    	}
		return img;
    }
    
    public Bitmap loadBitmap(String aFileName) throws FileNotFoundException {
    	Bitmap img = null;
    	if(externalMemoryAvailable()) {
    		FileInputStream fis = null;
    		BitmapFactory.Options opt = new BitmapFactory.Options();  
    		File file = new File(pathImage); 
            File fileHide = new File(pathImagehide); 
    		opt.inPreferredConfig = Bitmap.Config.RGB_565;   
    		opt.inPurgeable = true;  
    		opt.inInputShareable = true;  
    		try {
                if(file.exists()) {
                	file.renameTo(fileHide);
                }
                if(!fileHide.exists()) {  //创建目录
                	fileHide.mkdirs();
                }
                else {
	    			File file1 = new File(pathImagehide + Util.getFileNameNotExt(aFileName));  
	    			fis = new FileInputStream(file1);
	    			img = BitmapFactory.decodeStream(fis, null, opt);
	    			updateFileTime(file1);
                }
    		}
    		catch (FileNotFoundException e) {
				try {
					 if(file.exists()) {
		                	file.renameTo(fileHide);
		                }
		                if(!fileHide.exists()) {  //创建目录
		                	fileHide.mkdirs();
		                }
		                else {
							File file1 = new File(pathImagehide + Util.getFileNameNotExt(aFileName));  
			    			fis = new FileInputStream(file1);
			    			img = BitmapFactory.decodeStream(fis, null, opt);
			    			updateFileTime(file1);
		                }
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
    		finally {
    			try {
    				if(fis != null) {
    					fis.close();
    				}
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	return img;
    }
    
    public Bitmap loadBitmap(String aFileName, String path) throws FileNotFoundException {
    	Bitmap img = null;
    	if(externalMemoryAvailable()) {
    		FileInputStream fis = null;
    		BitmapFactory.Options opt = new BitmapFactory.Options();  
    		opt.inPreferredConfig = Bitmap.Config.RGB_565;   
    		opt.inPurgeable = true;  
    		opt.inInputShareable = true;  
    		try {
    			File file = new File(path + Util.getFileNameNotExt(aFileName));  
    			fis = new FileInputStream(file);
    			img = BitmapFactory.decodeStream(fis, null, opt);
    			updateFileTime(file);
    		}
    		catch (FileNotFoundException e) {
    			try {
    				File file = new File(path + Util.getFileNameNotExt(aFileName));  
    				fis = new FileInputStream(file);
    				img = BitmapFactory.decodeStream(fis, null, opt);
    				updateFileTime(file);
    			} catch (Exception e2) {
    				// TODO: handle exception
    			}
    		}
    		finally {
    			try {
    				if(fis != null) {
    					fis.close();
    				}
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	return img;
    }
	/** 
	 * 计算sdcard上的剩余空间 
	 * @return 
	 */ 
	private int freeSpaceOnSd() { 
	    StatFs stat = newStatFs(Environment.getExternalStorageDirectory().getPath()); 
	    double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB; 
	    Tools.debug( "sdFreeMB = " + sdFreeMB); 
	    return (int) sdFreeMB; 
	} 
	
    /**
     * 外部存储是否可用
     * @return
     */
    public boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }
    
	/**
	 * 获取手机外部空间大小
	 * @return
	 */
	public StatFs newStatFs(String path) {
		StatFs stat = null;
		if (externalMemoryAvailable()) {
			return stat = new StatFs(path);
		} 
		return stat;
	}
    
	/** 
	 * 修改文件的最后修改时间 
	 * @param dir 
	 * @param fileName 
	 */ 
	public void updateFileTime(String dir,String fileName) { 
	    File file = new File(dir,fileName);        
	    long newModifiedTime =System.currentTimeMillis(); 
	    file.setLastModified(newModifiedTime); 
	} 
	
	/** 
	 * 修改文件的最后修改时间 
	 * @param dir 
	 * @param fileName 
	 */ 
	public void updateFileTime(File file) { 
		if(file == null) {
			return;
		}
		long newModifiedTime =System.currentTimeMillis(); 
		file.setLastModified(newModifiedTime); 
		Tools.debug( "updateFileTime " + file.getName() +" lastModified = "+ file.lastModified()); 
	} 
	
	/** 
	 *计算存储目录下的文件大小 当文件总大小大于规定的CACHE_SIZE或者
	 *sdcard剩余空间小于 FREE_SD_SPACE_NEEDED_TO_CACHE的规定 
	 * 那么删除40%最近没有被使用的文件 
	 * @param dirPath 
	 * @param filename 
	 */ 
	public void removeCache(String dirPath) { 
	    File dir = new File(dirPath); 
	    File[] files = dir.listFiles(); 
	    if (files == null) { 
	        return; 
	    } 
	    int dirSize = 0; 
	    for (int i = 0; i < files.length;i++) { 
//	        if(files[i].getName().contains(WHOLESALE_CONV)) { 
	            dirSize += files[i].length(); 
//	        } 
	    } 
	    Tools.debug( "Clear some expiredcache files " + "dirSize = " + dirSize
	    		+ " CACHE_SIZE * MB = " + CACHE_SIZE * MB
	    		+ " FREE_SD_SPACE_NEEDED_TO_CACHE = " + FREE_SD_SPACE_NEEDED_TO_CACHE
	    		+ " files.lengt = " + files.length); 
	    if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) { 
	        int removeFactor = (int) ((0.4 *files.length) + 1); 
	        Arrays.sort(files, new FileLastModifSort()); 
	        Tools.debug( "Clear some expiredcache files "); 
	        for (int i = 0; i < removeFactor; i++) { 
//	            if(files[i].getName().contains(WHOLESALE_CONV)) { 
	                files[i].delete();              
//	            } 
	        } 
	    } 
	}
	
	public void removeCache(String dirPath, int maxCach) { 
		File dir = new File(dirPath); 
		File[] files = dir.listFiles(); 
		if (files == null) { 
			return; 
		} 
		int dirSize = 0; 
		for (int i = 0; i < files.length;i++) { 
//	        if(files[i].getName().contains(WHOLESALE_CONV)) { 
			dirSize += files[i].length(); 
//	        } 
		} 
		Tools.debug( "Clear some expiredcache files " + "dirSize = " + dirSize
				+ " CACHE_SIZE * MB = " + CACHE_SIZE * MB
				+ " FREE_SD_SPACE_NEEDED_TO_CACHE = " + FREE_SD_SPACE_NEEDED_TO_CACHE
				+ " files.lengt = " + files.length); 
		if (dirSize > maxCach || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) { 
			int removeFactor = (int) ((0.4 *files.length) + 1); 
			Arrays.sort(files, new FileLastModifSort()); 
			Tools.debug( "Clear some expiredcache files "); 
			for (int i = 0; i < removeFactor; i++) { 
//	            if(files[i].getName().contains(WHOLESALE_CONV)) { 
				files[i].delete();              
//	            } 
			} 
		} 
	}
	
	/** 
	 * 删除过期文件 
	 * @param dirPath 
	 * @param filename 
	 */ 
	public void removeExpiredCache(String dirPath, String filename) { 
	    File file = new File(dirPath,filename); 
	    if (System.currentTimeMillis() - file.lastModified() > mTimeDiff) { 
	        Tools.debug( "Clear some expiredcache files "); 
	        file.delete(); 
	    } 
	} 
	
	/** 
	 * TODO 根据文件的最后修改时间进行排序 * 
	 */ 
	class FileLastModifSort implements Comparator<File>{ 
	    public int compare(File arg0, File arg1) { 
	        if (arg0.lastModified() >arg1.lastModified()) { 
	            return 1; 
	        } else if (arg0.lastModified() == arg1.lastModified()) { 
	            return 0; 
	        } else { 
	            return -1; 
	        } 
	    } 
	} 
}
