package com.funoble.myarstation.communication.task;

import android.os.Handler;

import com.funoble.myarstation.communication.RestMsg;

/**
 * POST 任务
 * 
 * @author sunny
 */

public class PostTask extends Task
{
	public PostTask(RestMsg restMsg, Handler handler)
	{
		super(restMsg, handler);
	}

	@Override
	public void run()
	{
		if (msg == null)
			return;
//		byte[] byteArrayResponse = HttpClientExecutor.getInstance().executePost(msg.getUrl(), msg.getFile(), handler);
//		GameDataCenter.getInstance().setCookiestore(HttpClientExecutor.getInstance().getCookiestore());
//		if (byteArrayResponse != null)
//		{
//			try
//			{
//				int size = byteArrayResponse.length;
//				handler.sendEmptyMessage(HTTPERROR.MSG_POST_FILE);
//			}
//			catch (Exception e)
//			{
//				if (handler != null)
//				{
////					handler.sendEmptyMessage(GameDataCenter.MSG_PARSE_MSG_ERROR);
//				}
//				e.printStackTrace();
//			}
//		}
	}
}
