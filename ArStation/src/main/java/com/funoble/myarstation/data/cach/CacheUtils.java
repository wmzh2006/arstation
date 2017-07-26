/*******************************************************************************
 * Copyright (C) 1998-2009 BBG Inc.All Rights Reserved.
 * FileName:   CacheUtils.java 
 * Description：
 * History：
 * 版本号   作者       日期      简要介绍相关操作
 * 1.0     sunny    2009-11-22   Create
 *******************************************************************************/
package  com.funoble.myarstation.data.cach;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * <p>
 * 管理cache 
 * </p>
 */
public class CacheUtils {
    /**
     * 图片cache名
     */
    private final static String imageCacheName = "imgCache";
    /**
     * 图片cache最大数
     */
    private final static int imageCacheMaxSize = 25;

    /**
     * 图片cache 
     */
    private static final Cache<String, Bitmap> cacheImage = new DefaultCache<String, Bitmap>(imageCacheName, 
            imageCacheMaxSize, 0);
    
    /**
     * 插入图片缓存
     * @param name
     * @param img
     */
    public static void putImage2Cache(String name, Bitmap img) {
        cacheImage.put(name, img);
    }
    
    /**
     * 根据名称从cache中获取图片
     * @param name
     * @return
     */
    public static Bitmap getImageFromCache(String name) {
        return cacheImage.get(name);
    }
    
    /**
     * 从本地或cache获取图片
     * @param name
     * @return
     */
    public static Bitmap getImage(String name) {
        Bitmap bm = getImageFromCache(name);
        if(bm == null) {
            bm = ImageDataManager.loadBitmapByFileName(name);   //到本地找
            if(bm != null) {
                putImage2Cache(name, bm);
            }
        }
        return bm;
    }
    
    public static void clearBitmap(String bitmapName) {
    	if (bitmapName == null){
    		return;
    	}
    	if (cacheImage.isEmpty()){
    		return;
    	}
    	if (cacheImage.containsKey(bitmapName)){
    		
    		Bitmap tmpBit = cacheImage.remove(bitmapName);
    		if (tmpBit != null){
    			tmpBit = null;
    			Log.i("remove bitmap index = ", "cacheImage,size = " + getCacheSize());
    		}
    	}
    }
    
    public static int getCacheSize() {
    	if (cacheImage.isEmpty()){
    		return 0;
    	}
    	return cacheImage.size();
    }
    
    /**
     * 清除cache
     */
    public static void clearCache() {
    	if (cacheImage.isEmpty()) {
    		return;
    	}
    	Bitmap bitmap = null;
    	Set<Entry<String , Bitmap>> set = cacheImage.entrySet();
		Iterator<Entry<String, Bitmap>> iterator = set.iterator();
		if (iterator == null) {
			return;
		}
		while(iterator.hasNext()){
			Entry<String, Bitmap> tmp = iterator.next();
			if (tmp != null){
				bitmap = tmp.getValue();
				if (bitmap != null){
					bitmap = null;
				}
			}
		}
		cacheImage.clear();
		System.gc();
    }
}
