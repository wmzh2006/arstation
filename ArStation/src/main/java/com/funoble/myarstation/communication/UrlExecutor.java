package  com.funoble.myarstation.communication;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Map;

import android.os.Handler;


/**
 * url executor for handling request & response
 * 
 * @author Samuel (totobacoo@gmail.com)
 * @version Nov 12, 2009 12:07:40 PM
 */

public interface UrlExecutor
{
    /** 
     * 设置连接超时时间
     * 
     * @param milliSecond
     * @see DEFAULT_CONNECT_TIMEOUT
     */
    public void setConnectTimeout(int milliSecond);

    /**
     * 设置数据读取超时时间
     * 
     * @param milliSecond
     * @see DEFAULT_READ_TIMEOUT
     */
    public void setReadTimeout(int milliSecond);

    /**
     * 设置最大连接数
     * 
     * @param maxConnections
     * @see DEFAULT_MAX_CONNECTIONS
     */
    public void setMaxConnections(int maxConnections);

    /**
     * 设置每主机最大连接数
     * 
     * @param maxHostConnections
     * @see DEFAULT_MAX_CONNECTIONS_PER_HOST
     */
    public void setMaxConnectionsPerHost(int maxHostConnections);

    /**
     * 设置是否 keepAlive
     * 
     * @param keepAlive
     */
    public void setKeepAlive(boolean keepAlive);


    /**
     * POST
     * 
     * @param url
     *            服务地址
     * @param postMsg
     *            post数据
     * @return POST响应
     * @return
     */
    public byte[] executePost(String url, Map<String, String> params, Handler handler);
    /**
     * POST
     * 
     * @param url
     *            服务地址
     * @param postMsg
     *            post数据
     * @return POST响应
     * @return
     */
    public byte[] executePost(String url, ByteBuffer params, Handler handler);
   
    /**
     * POST
     * 
     * @param url
     *            服务地址
     * @param postMsg
     *            post数据
     * @return POST响应
     * @return
     */
    public byte[] executePost(String url, File file, Handler handler);


    /**
     * GET
     * 
     * @param url
     *            服务地址
     * @return GET 获取的 http 资源，如文本、图片、文件
     * @return
     */
    public byte[] executeGet(String url, int action, Handler handler);
}
