package  com.funoble.myarstation.communication;

import android.os.Handler;

import com.funoble.myarstation.communication.task.GetTask;
import com.funoble.myarstation.communication.task.PostTask;
import com.funoble.myarstation.communication.task.Task;
import com.funoble.myarstation.communication.task.ThreadPoolService;
import com.funoble.myarstation.socket.protocol.RetMsg;


/**
 * REST Service Client
 * 
 * @author sunny
 */

public class RestClient
{
    /**
     * 服务地址
     */
    public static final String DDL_URL  = "http://www.dundunle.com:8081";
    
    
    /**
     * 
     * <p>
     * 
     * </p>
     */
    
    
    public enum TaskMehod
    {
        GET, POST
    }

    /**
     * 添加一个网络任务
     * 
     * @param task
     */
    public static void addTask(TaskMehod method, RestMsg restMsg, Handler handler)
    {
        Task task = null;
        switch (method)
        {
            case GET:
                task = new GetTask(restMsg, handler);
                break;
            case POST:
                task = new PostTask(restMsg, handler);
                break;
            default:
                task = new PostTask(restMsg, handler);
                break;
        }
        ThreadPoolService.getInstance().execute(task);
    }
}
