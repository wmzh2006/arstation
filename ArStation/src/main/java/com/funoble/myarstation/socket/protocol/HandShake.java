/*******************************************************************************
 * Copyright (C) 1998-2009 BBG Inc.All Rights Reserved.
 * FileName:   HandShake.java 
 * Description：
 * History：
 * 版本号   作者       日期      简要介绍相关操作
 * 1.0     sunny    2009-11-25   Create
 *******************************************************************************/
package  com.funoble.myarstation.socket.protocol;

/**
 * <p>
 * 握手信息
 * </p>
 */
public class HandShake
{
    /**
     * 是否需更新版本
     */
    public final boolean isNeedUpdate;
    /**
     * 升级后的内部编号 (update为false时，此属性不存在)
     */
    public final int nextVer;
    /**
     * 升级后的版本名 (update为false时，此属性不存在)
     */
    public final String nextVerName;
    /**
     * 下载地址
     */
    public final String url;
    
    /**
     * 
     * @param update
     * @param ver
     * @param vername
     * @param url
     */
    public HandShake(boolean update, int ver, String vername, String url)
    {
        this.isNeedUpdate = update;
        this.nextVer = ver;
        this.nextVerName = vername;
        this.url = url;
    }

}
