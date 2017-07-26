package  com.funoble.myarstation.communication;


/**
 * UrlExecutor 抽象实现
 * 
 * @author Samuel (totobacoo@gmail.com)
 * @version Nov 12, 2009 11:58:55 AM
 */

public abstract class AbstractUrlExecutor implements UrlExecutor
{
    // 缺省连接时间
    protected static final int DEFAULT_CONNECT_TIMEOUT = 30000;

    // 缺省读取时间
    protected static final int DEFAULT_READ_TIMEOUT = 30000;

    // 缺省最大连接数
    protected static final int DEFAULT_MAX_CONNECTIONS = 5;

    // 缺省单主机最大连接数
    protected static final int DEFAULT_MAX_CONNECTIONS_PER_HOST = 2;
}