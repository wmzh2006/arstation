package  com.funoble.myarstation.communication.task;

import android.os.Handler;

import com.funoble.myarstation.communication.RestMsg;

/**
 * 网络线程任务
 * 
 * @author sunny
 */

public abstract class Task implements Runnable
{
    protected RestMsg msg;
    protected Handler handler;
    
    public Task(RestMsg restMsg, Handler handler) {
        this.setMsg(restMsg, handler);
    }

    public void setMsg(RestMsg restMsg, Handler handler) {
        this.msg = restMsg;
        this.handler = handler;
    }

    public RestMsg getMsg() {
        return msg;
    }
    
    public Handler getHandler() {
    	return handler;
    }
}
