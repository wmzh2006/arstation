package  com.funoble.myarstation.communication.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.funoble.myarstation.common.Tools;


public class ThreadPoolService
{
    /**
     * 默认线程池大小
     */
    public static final int DEFAULT_POOL_SIZE = 2;

    /**
     * 默认一个任务的超时时间，单位为毫秒
     */
    public static final long DEFAULT_TASK_TIMEOUT = 10000;

    /**
     * 池大小
     */
    private int poolSize = DEFAULT_POOL_SIZE;

    /************ Singleton ***********/

    private static ThreadPoolService instance = new ThreadPoolService(DEFAULT_POOL_SIZE);
    
    /**
     * 根据给定大小创建线程池
     */
    private ThreadPoolService(int poolSize)
    {
        setPoolSize(poolSize);
    }
    
    public static ThreadPoolService getInstance()
    {
        return instance;
    }

    /************ Singleton ***********/
    

    private ExecutorService executorService;


    /**
     * 使用线程池中的线程来执行任务
     */
    public void execute(Runnable task)
    {
        executorService.execute(task);
    }


    /**
     * 关闭当前ExecutorService
     * 
     * @param timeout
     *            以毫秒为单位的超时时间
     */
    public void shutdown(long timeout)
    {
        if(executorService != null && !executorService.isShutdown())
        {
            try
            {
                executorService.awaitTermination(timeout, TimeUnit.MILLISECONDS);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            executorService.shutdown();
        }
    }

    /**
     * 关闭当前ExecutorService，随后根据poolSize创建新的ExecutorService
     */
    public void createExecutorService()
    {
        shutdown(1000);
        executorService = Executors.newFixedThreadPool(poolSize);
    }

    /**
     * 调整线程池大小
     * 
     * @see #createExecutorService()
     */
    public void setPoolSize(int poolSize)
    {
        this.poolSize = poolSize;
        createExecutorService();
    }
}
