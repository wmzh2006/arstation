/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: DemoAppSocket.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-4-27 下午03:01:54
 *******************************************************************************/
package com.funoble.myarstation.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.common.Util;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.socket.protocol.MBReqAccount;
import com.funoble.myarstation.socket.protocol.MBReqActivityDetail;
import com.funoble.myarstation.socket.protocol.MBReqActivityEnroll;
import com.funoble.myarstation.socket.protocol.MBReqActivityList;
import com.funoble.myarstation.socket.protocol.MBReqActivityRank;
import com.funoble.myarstation.socket.protocol.MBReqAddFriend;
import com.funoble.myarstation.socket.protocol.MBReqAgreeFriend;
import com.funoble.myarstation.socket.protocol.MBReqAward;
import com.funoble.myarstation.socket.protocol.MBReqBusiness;
import com.funoble.myarstation.socket.protocol.MBReqBusinessDetail;
import com.funoble.myarstation.socket.protocol.MBReqBuyWine;
import com.funoble.myarstation.socket.protocol.MBReqChangeDiceType;
import com.funoble.myarstation.socket.protocol.MBReqChat;
import com.funoble.myarstation.socket.protocol.MBReqChop;
import com.funoble.myarstation.socket.protocol.MBReqCleanDrunk;
import com.funoble.myarstation.socket.protocol.MBReqCommitPlayerData;
import com.funoble.myarstation.socket.protocol.MBReqCreateRole;
import com.funoble.myarstation.socket.protocol.MBReqDelLeaveMsg;
import com.funoble.myarstation.socket.protocol.MBReqDelPic;
import com.funoble.myarstation.socket.protocol.MBReqDing;
import com.funoble.myarstation.socket.protocol.MBReqFeekBack;
import com.funoble.myarstation.socket.protocol.MBReqFriendEventSelect;
import com.funoble.myarstation.socket.protocol.MBReqFriendList;
import com.funoble.myarstation.socket.protocol.MBReqGiftFriendGoods;
import com.funoble.myarstation.socket.protocol.MBReqGiftFriendGoodsTwo;
import com.funoble.myarstation.socket.protocol.MBReqGiftList;
import com.funoble.myarstation.socket.protocol.MBReqGoodsList;
import com.funoble.myarstation.socket.protocol.MBReqGoodsListGb;
import com.funoble.myarstation.socket.protocol.MBReqHart;
import com.funoble.myarstation.socket.protocol.MBReqHomeGiftList;
import com.funoble.myarstation.socket.protocol.MBReqHomeReceiveGiftList;
import com.funoble.myarstation.socket.protocol.MBReqHomeTakeGift;
import com.funoble.myarstation.socket.protocol.MBReqInviteFriend;
import com.funoble.myarstation.socket.protocol.MBReqKick;
import com.funoble.myarstation.socket.protocol.MBReqLeaveMsg;
import com.funoble.myarstation.socket.protocol.MBReqLeaveMsgB;
import com.funoble.myarstation.socket.protocol.MBReqLeaveMsgList;
import com.funoble.myarstation.socket.protocol.MBReqLeaveMsgListB;
import com.funoble.myarstation.socket.protocol.MBReqLeaveRoom;
import com.funoble.myarstation.socket.protocol.MBReqLightning;
import com.funoble.myarstation.socket.protocol.MBReqLogin;
import com.funoble.myarstation.socket.protocol.MBReqLoginRoom;
import com.funoble.myarstation.socket.protocol.MBReqLoginRoomB;
import com.funoble.myarstation.socket.protocol.MBReqLoginRoomUnite;
import com.funoble.myarstation.socket.protocol.MBReqLoginVipRoom;
import com.funoble.myarstation.socket.protocol.MBReqMainPage;
import com.funoble.myarstation.socket.protocol.MBReqMainPageB;
import com.funoble.myarstation.socket.protocol.MBReqMissionList;
import com.funoble.myarstation.socket.protocol.MBReqMyGoods;
import com.funoble.myarstation.socket.protocol.MBReqPlayerInfo;
import com.funoble.myarstation.socket.protocol.MBReqPlayerInfoFour;
import com.funoble.myarstation.socket.protocol.MBReqPlayerInfoThree;
import com.funoble.myarstation.socket.protocol.MBReqPlayerInfoTwo;
import com.funoble.myarstation.socket.protocol.MBReqRank;
import com.funoble.myarstation.socket.protocol.MBReqReady;
import com.funoble.myarstation.socket.protocol.MBReqRegister;
import com.funoble.myarstation.socket.protocol.MBReqRegisterB;
import com.funoble.myarstation.socket.protocol.MBReqResetPwd;
import com.funoble.myarstation.socket.protocol.MBReqResetThirdUserInfo;
import com.funoble.myarstation.socket.protocol.MBReqRoomList;
import com.funoble.myarstation.socket.protocol.MBReqRoomListB;
import com.funoble.myarstation.socket.protocol.MBReqRoomListC;
import com.funoble.myarstation.socket.protocol.MBReqSearchPeople;
import com.funoble.myarstation.socket.protocol.MBReqSendFace;
import com.funoble.myarstation.socket.protocol.MBReqSetPwdQuestion;
import com.funoble.myarstation.socket.protocol.MBReqShakeFriend;
import com.funoble.myarstation.socket.protocol.MBReqShout;
import com.funoble.myarstation.socket.protocol.MBReqThirdLogin;
import com.funoble.myarstation.socket.protocol.MBReqTrace;
import com.funoble.myarstation.socket.protocol.MBReqUID;
import com.funoble.myarstation.socket.protocol.MBReqUnite;
import com.funoble.myarstation.socket.protocol.MBReqUpLoadPic;
import com.funoble.myarstation.socket.protocol.MBReqUpLoadShareText;
import com.funoble.myarstation.socket.protocol.MBReqUpdate;
import com.funoble.myarstation.socket.protocol.MBReqUseItem;
import com.funoble.myarstation.socket.protocol.MBReqVIPinfo;
import com.funoble.myarstation.socket.protocol.MBReqVipUpgrade;
import com.funoble.myarstation.socket.protocol.MBReqVisitorLogin;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.MobileMsgHead;
import com.funoble.myarstation.socket.protocol.MsgPara;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.ReqMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.SocketErrorType;


/**
 * description 
 * version 1.0
 * author 
 * update 2013-6-21 上午12:02:37 
 */
public class GameProtocol implements SocketHandler, TimerClient, SocketSend {
	public static final boolean Debug = false;
	public static final int 	BUFFER_LEN = 8 * 1024;
	//Socket
	private SocketEngine 			socketEx = null;
	private GameSocketHandler		issh=null;
	private String 					ip="127.0.0.1";
	private int 					port=20000;
	private Context					iContext;
	
	//通信逻辑相关
	private	int						iSessionID;			//与服务器通话的ID，防止作弊
	private	MobileMsg 				iMobileMsgCS;		//客户端到服务器通信包
	private	MobileMsg				iMsgSC;				//服务器到客户端通信包
	private HashMap<Integer, MsgPara> iMsgScArray;	//服务器到客户端通信包数组
	private byte[]					iRevBuffer;		//服务器到客户端通信包数缓存
	private byte[]					iPakBuffer;		//一个数据包的缓存
	private int 					iRecvLen = 0; //接收到的数据总长度
	//游戏逻辑相关
	public	short 					iRoomID;			//房间ID
	public	short 					iTableID;			//桌子ID
	public  short 					iSeatID;			//座位ID
	public	short 					iOtherPlayerNum;	//其他玩家数
	private Timer					iSendTimer;			//发送计时器
	private TimerTask 				mTimerTask;
	private long					time;
	private int 					iCurTaskMsgID;
	private SendThread				iSendThread;//
	
    private void startTimer(int aDelayTime, int aObviateMsgID){  
    	if(obviateStartTimer(aObviateMsgID)) {
    		return;
    	}
        if (iSendTimer == null) {  
            iSendTimer = new Timer();  
        }  
        if (mTimerTask == null) {  
            mTimerTask = new TimerTask() {  
                @Override  
                public void run() {  
                	Tools.debug("stopTimer run() time over = " + (System.currentTimeMillis() - time ));
    				OnReportError(this, SocketErrorType.Error_TimeOut);
                }  
            };  
        }  
  
        if(iSendTimer != null && mTimerTask != null ) {
        	time = System.currentTimeMillis();
        	Tools.debug("start time = " + time); 
        	iSendTimer.schedule(mTimerTask, aDelayTime * 1000);  
        }
  
    }  
  
    private void stopTimer() {  
    	Tools.debug("stopTimer time over = " + (System.currentTimeMillis() - time ));
    	time = System.currentTimeMillis();
        if (iSendTimer != null) {  
            iSendTimer.cancel();  
            iSendTimer = null;  
        }  
        if (mTimerTask != null) {  
            mTimerTask.cancel();  
            mTimerTask = null;  
        }     
    }  
	
    public void setCurTaskMsgID(int aCurTaskMsgID) {
    	iCurTaskMsgID = aCurTaskMsgID;
    }
    
    public int getCurTaskMsgID() {
    	return iCurTaskMsgID;
    }
    
    private boolean obviateStartTimer(int aMsgID) {
    	switch (aMsgID) {
		case ReqMsg.MSG_REQ_READY:
		case ReqMsg.MSG_REQ_CHAT:
			return true;
		default:
			return false;
		}
    }
    
    public boolean isConnect() {
    	boolean connect = false;
    	if(socketEx == null) {
    		return connect;
    	}
        if (socketEx.isClosed() == false && socketEx.isConnected() == true) {
        	connect = true;
        }
        else {
        	connect = false;
        }
        return connect;
    }
    
	public GameProtocol(GameSocketHandler issh) {
		iSendThread = new SendThread(this);
		iSendThread.start();
		this.issh = issh;
		iContext = (Context)issh;
		initialize();
	}
	
	private void initialize() {
		this.iSessionID = -1;
		this.iMobileMsgCS = new MobileMsg();
		this.iMsgSC = new MobileMsg();
		this.iMsgScArray = new HashMap<Integer, MsgPara>();
		this.iRoomID = -1;
		this.iTableID = -1;
		this.iSeatID = -1;
		this.iOtherPlayerNum = 0;
		this.iRevBuffer = new byte[BUFFER_LEN + 2];
		this.iPakBuffer = new byte[BUFFER_LEN + 2];
	}
	
//获取服务器应答包
	public MobileMsg GetPacketSC() {
		return iMsgSC;
	}
	
	public void setSessionID(int aSessionID) {
		iSessionID = aSessionID;
	}
	
	//解析数据
	public int Decode(byte[] data, int aMsgSize) {
		MobileMsg pMobileMsg = iMsgSC;
		if(pMobileMsg == null || aMsgSize < 0) {
			return -1;
		}
		
		int len = pMobileMsg.Decode(data, (short) aMsgSize);
		if(len > 0) {
			iMsgScArray.put((int) iMsgSC.m_MsgHead.m_sMsgID, iMsgSC.m_pMsgPara);
		}
		return len;
	}
	
	//调用完构造后调用这个函数创建sock对象
	public boolean create(String aIp, int aPort) {
		this.ip = aIp;
		if(this.ip == null) this.ip = "";
		this.port = aPort;
		if(this.port <0) this.port = 0;
		iRecvLen = 0;
//		if(!start()) {
		iSendThread.Sleep(true);
		iSendThread.purse();
		Tools.debug("GameProtocol::create() startTimer:0");
		TimerCtl.startTimer(this,0,0,false);
//			return false;
//		}
		return true;
	}
	
	//这个方法一般不需要直接调用
	public boolean start() {
		return startClient();
	}
	
	//停止socket   	
	public void stop() {
		TimerCtl.stopTimer(this,0);
		iSendThread.Sleep(true);
		iSendThread.purse();
		stopClient();
	}
	
	public synchronized boolean OnSend(byte[] aOutBuffer, int aOutLenght) {
		Tools.debug("GameProtocol::OnSend()::start");
		byte[] data = new byte[aOutLenght];
		System.arraycopy(aOutBuffer, 0, data, 0, aOutLenght);
		//Tools.debug("OnSend aOutLenght " + aOutLenght);
		iSendThread.SendOrder(data);
		Tools.debug("GameProtocol::OnSend()::end");
		return true;
	}
	
	
	//获得IP地址
	public String getIp() {
		return ip;
	}
	
	//获得端口号	
	public int getPort() {
		return port;
	}
	
	protected boolean startClient() {
		if(socketEx != null) {
			try{
				socketEx.close();
			}catch(IOException e){};
			OnReportError(socketEx, SocketErrorType.Error_Closefailed);
			socketEx = null;
		}
		try{
			socketEx = new SocketEngine(this,ip,port);
//			TimerCtl.stopTimer(this,eventId);
			return true;
		}catch(IOException e)
		{
			OnReportError(socketEx, SocketErrorType.Error_DisConnection);
			socketEx = null;
		}
		return false;
		
	}
	
	protected void stopClient() {
		if(socketEx != null) {
			try{
				socketEx.close();
			}catch(IOException e){
				e.printStackTrace();
			};
			socketEx = null;
		}
	}
	
	
	public void timerEvent(int id) {
		Tools.debug("GameProtocol::timerEvent() id = " + id);
		start();
	}
	
	public void cleanMsgBuffer() {
		iRecvLen = 0;
	}
	
	public void OnReceive(Object socket,byte[] aInBuffer, int aInLenght){
		if(aInLenght <= 0) {
			return;
		}
		Tools.println("StartNotify 收到数据长度=" + aInLenght + " 当前长度=" + iRecvLen);
		int nRecvLen = 0;
		int nRecvAllLen = 0;
		int offset = iRecvLen;
		System.arraycopy(aInBuffer, 0, iRevBuffer, offset, aInLenght);
		iRecvLen += aInLenght;
		nRecvAllLen = iRecvLen;
		offset = 0;
		while(true) {
			if(0 == nRecvAllLen) {
				break;
			}
			int tParaA = iRevBuffer[offset];
			if(tParaA < 0) {
				tParaA += 256;
			}
			int tParaB = iRevBuffer[offset+1];
			if(tParaB < 0 ) {
				tParaB += 256;
			}
			nRecvLen = (tParaA << 8) | tParaB; /*包长度*/
			Tools.println("StartNotify 数据包长=" + nRecvLen);
			if ( (4096 < nRecvLen) || (8 > nRecvLen) ) {	
				iRecvLen = 0;
				Tools.println("StartNotify 非法数据!");
				Tools.println("StartNotify " + Util.bytesToHexString(iRevBuffer));
				return;
			}
			nRecvAllLen = nRecvAllLen - nRecvLen;
			if( nRecvAllLen < 0 ) {
				nRecvAllLen = nRecvAllLen + nRecvLen;
				//停止计时器
//				stopTimer();
				//不是完整的数据包，重新计时等待
//				startTimer(15, 0);
				//
				Tools.debug("StartNotify  数据包不完整nRecvAllLen =" + nRecvAllLen);
				break;
			}
			System.arraycopy(iRevBuffer, offset, iPakBuffer, 0, nRecvLen);
			offset += nRecvLen;
			//请求解压
			int tRet = Decode(iPakBuffer, nRecvLen);
			Tools.println("StartNotify 解码结果 =" + tRet + " 包长=" + nRecvLen);
			if(tRet > 0) {
				Tools.println("StartNotify 处理数据 MsgID=" + iMsgSC.m_MsgHead.m_sMsgID);
				DoProcessCmd(iMsgSC, iMsgSC.m_MsgHead.m_cMsgType, iMsgSC.m_MsgHead.m_sMsgID);
			}
			else {
				Tools.println("StartNotify 解码出错Ret=" + tRet);
			}
			//停止计时器
			stopTimer();
		}
		if( 0 == nRecvAllLen ) {
			iRecvLen = 0;
			Tools.println("StartNotify 所有数据处理完成！");
		}
		else {
			if (nRecvAllLen > 4096 || nRecvAllLen < 0) {
				iRecvLen = 0;
				return;
			}
			//
			iRecvLen = nRecvAllLen;
			System.arraycopy(iRevBuffer, offset, iRevBuffer, 0, nRecvAllLen);
			Tools.println("StartNotify 拷贝没处理完成的数据！");
		}
	}
	
	protected void DoProcessCmd(Object socket, int aMobileType, int aMsgID) {
		if(issh !=null) {
			issh.OnProcessCmd(socket, aMobileType, aMsgID);
		}
	}
	
	public void OnConnect(Object socket) {
		iSendThread.Sleep(false);
		if(issh !=null) issh.OnConnect(socket);
	}
	
	
	public void OnClose(Object socket) {
		iSendThread.Stop();
		if(issh !=null) issh.OnClose(socket);
		Log.e("", "appsocket onColse!");
	}
	
	public void OnReportError(Object socket, int aErrorCode) {
		if(issh !=null) issh.OnReportError(socket, aErrorCode);
	}
	
	/**
	 * @param aUID
	 * @param aPwd
	 * @param aVersion
	 * @return 请求成功
	 */
	public boolean RequestLoginL(String aUID, String aPwd, int aVersion) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = -1;
		pMobileMsg.iFlag = 1;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead = null;
		pMobileMsg.m_MsgHead = new MobileMsgHead();
		pMobileMsg.m_CSHead.iVer = 1200;
		pMobileMsg.m_CSHead.iPlatformID = 'A';
		pMobileMsg.m_CSHead.iMobileType = 1;
		pMobileMsg.m_CSHead.iUID = aUID + Util.getPhoneStatus() +"|" + Util.getUUID(MyArStation.mGameMain.getApplicationContext());
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_LOGIN;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_LOGIN, (short)IM.IM_REQUEST);
		MBReqLogin pReqLogin = (MBReqLogin)pMobileMsg.m_pMsgPara;
		if(pReqLogin != null) {
			pReqLogin.iUserName = aUID;
			pReqLogin.iUserPswd = aPwd;
			pReqLogin.iVersion = aVersion;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
			if(pMobileMsg.Decode(temp.array(), len) < 0) {
				return false;
			}
			else {
				Result = OnSend(pData, len);
			}
		}
		return Result;
	}
	
	/**第三方登录请求
	 * @param aUID
	 * @param aChannelID
	 * @param aVersion
	 * @return
	 */
	public boolean RequestThirdLogin(String aUID, int aChannelID, int aVersion) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = -1;
		pMobileMsg.iFlag = 1;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead = null;
		pMobileMsg.m_MsgHead = new MobileMsgHead();
		pMobileMsg.m_CSHead.iVer = 1200;
		pMobileMsg.m_CSHead.iPlatformID = 'A';
		pMobileMsg.m_CSHead.iMobileType = 1;
		pMobileMsg.m_CSHead.iUID = aUID + Util.getPhoneStatus() + "|" + Util.getUUID(MyArStation.mGameMain.getApplicationContext());
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_THIRD_LOGIN;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_THIRD_LOGIN, (short)IM.IM_REQUEST);
		MBReqThirdLogin pReqLogin = (MBReqThirdLogin)pMobileMsg.m_pMsgPara;
		if(pReqLogin != null) {
			pReqLogin.iUserName = aUID;
			pReqLogin.iChannelID  = aChannelID;
			pReqLogin.iVersion = aVersion;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
			if(pMobileMsg.Decode(temp.array(), len) < 0) {
				return false;
			}
			else {
				Result = OnSend(pData, len);
			}
		}
		return Result;
	}
	/**
	 * @param aVersion
	 * @return 请求成功
	 */
	public boolean RequestVisitorLoginL(int aVersion) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = -1;
		pMobileMsg.iFlag = 1;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead = null;
		pMobileMsg.m_MsgHead = new MobileMsgHead();
		pMobileMsg.m_CSHead.iVer = 1200;
		pMobileMsg.m_CSHead.iPlatformID = 'A';
		pMobileMsg.m_CSHead.iMobileType = 1;
		pMobileMsg.m_CSHead.iUID = 0 + Util.getPhoneStatus();
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_VISITORLOGIN;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_VISITORLOGIN, (short)IM.IM_REQUEST);
		MBReqVisitorLogin pReqLogin = (MBReqVisitorLogin)pMobileMsg.m_pMsgPara;
		if(pReqLogin != null) {
			pReqLogin.iVersion = aVersion;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
			if(pMobileMsg.Decode(temp.array(), len) < 0) {
				return false;
			}
			else {
				Result = OnSend(pData, len);
			}
		}
		return Result;
	}
	
	/**
	 * @param aUID
	 * @param aPwd
	 * @return true 请求成功
	 */
	public boolean RequestRegister(String aUID, String aPwd) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = -1;
		pMobileMsg.iFlag = 1;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead = null;
		pMobileMsg.m_MsgHead = new MobileMsgHead();
		pMobileMsg.m_CSHead.iVer = (short) 1200;
		pMobileMsg.m_CSHead.iPlatformID = 'A';
		pMobileMsg.m_CSHead.iMobileType = 1;
		pMobileMsg.m_CSHead.iUID = "";
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_REGISTER;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_REGISTER, (short)IM.IM_REQUEST);
		MBReqRegister pReqRegister = (MBReqRegister)pMobileMsg.m_pMsgPara;
		if(pReqRegister != null) {
			pReqRegister.iUserID = aUID;
			pReqRegister.iUserPswd = aPwd;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
			if(pMobileMsg.Decode(temp.array(), len) < 0) {
				return false;
			}
			else {
				Result = OnSend(pData, len);
			}
		}
		return Result;
	}
	
	public void releaseMsgScBuffer() {
		iMsgScArray.clear();
	}
	
	/**
	 * @param aMsgID//服务器到客户端通信包数组ID
	 * @return MobileMsg 已接受服务器到客户端通信包数组
	 */
	public MsgPara getMobileMsgCS(int aMsgID) {
		return iMsgScArray.get(aMsgID);
	}
	
	public boolean RequsetCteateRole(int aModelID, String aNick) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_CREATEROLE;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_CREATEROLE, (short)IM.IM_REQUEST);
		MBReqCreateRole pCreateRole = (MBReqCreateRole)pMobileMsg.m_pMsgPara;
		if(pCreateRole != null) {
			pCreateRole.iModelID = aModelID;
			pCreateRole.iNick = aNick;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aRoomID -1 //默认系统分别
	 * @param aTableID -1//默认系统分别
	 * @return true    发送成功
	 */
	public boolean RequsetLoginRoom(int aRoomID, int aTableID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_LOGINROOM;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_LOGINROOM, (short)IM.IM_REQUEST);
		MBReqLoginRoom pMbReqLoginRoom = (MBReqLoginRoom)pMobileMsg.m_pMsgPara;
		if(pMbReqLoginRoom != null) {
			pMbReqLoginRoom.iRoomID = aRoomID;
			pMbReqLoginRoom.iTableID = aTableID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @return true 发送成功
	 */
	public boolean RequsetReady(int aUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_READY;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_READY, (short)IM.IM_REQUEST);
		MBReqReady pMbReq = (MBReqReady)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aDesUserID
	 * @return true 发送成功
	 */
	public boolean requestHomeReveiceGiftList(int aDesUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_HOME_RECEIVE_GIFT_LIST;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_HOME_RECEIVE_GIFT_LIST, (short)IM.IM_REQUEST);
		MBReqHomeReceiveGiftList pMbReq = (MBReqHomeReceiveGiftList)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iDesUserID = aDesUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @param aDice
	 * @param Count
	 * @param aShoutOne 是否直接喊斋
	 * @return true 发送成功
	 */
	public boolean RequsetShout(int aUserID, int aDice, int Count, short aShoutOne) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_SHOUT;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_SHOUT, (short)IM.IM_REQUEST);
		MBReqShout pMbReq = (MBReqShout)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aUserID;
			pMbReq.iDice = aDice;
			pMbReq.iCount = Count;
			pMbReq.nShoutOne = aShoutOne;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aDiceType
	 * @return
	 */
	public boolean RequestChangeDiceType(int aDiceType) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_CHANGE_DICE_TYPE;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_CHANGE_DICE_TYPE, (short)IM.IM_REQUEST);
		MBReqChangeDiceType pMbReq = (MBReqChangeDiceType)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iDiceType = aDiceType;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aDesUserID
	 * @param aGoodsID
	 * @param aCount
	 * @return
	 */
	public boolean RequestGiftFriendGoods(int aDesUserID, int aGoodsID, int aCount) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_GIFT_FRIEND_GOODS;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_GIFT_FRIEND_GOODS, (short)IM.IM_REQUEST);
		MBReqGiftFriendGoods pMbReq = (MBReqGiftFriendGoods)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iDesUserID = aDesUserID;
			pMbReq.iGoodsID = aGoodsID;
			pMbReq.iCount = aCount;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aDesUserID
	 * @param aGoodsID
	 * @param aCount
	 * @return
	 */
	public boolean RequestGiftFriendGoodsTwo(int aDesUserID, int aGoodsID, int aCount) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_GIFT_FRIEND_GOODS_TWO;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_GIFT_FRIEND_GOODS_TWO, (short)IM.IM_REQUEST);
		MBReqGiftFriendGoodsTwo pMbReq = (MBReqGiftFriendGoodsTwo)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iDesUserID = aDesUserID;
			pMbReq.iGoodsID = aGoodsID;
			pMbReq.iCount = aCount;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @return
	 */
	public boolean RequestFriendEventSelect(int aEventID, int aKey, int aSelectID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_FRIEND_EVENT_SELECT;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_FRIEND_EVENT_SELECT, (short)IM.IM_REQUEST);
		MBReqFriendEventSelect pMbReq = (MBReqFriendEventSelect)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iEventID = aEventID;
			pMbReq.iKey = aKey;
			pMbReq.iSelectID = aSelectID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aType
	 * @return
	 */
	public boolean RequestInviteFriend(int aType) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_INVITE_FRIEND;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_INVITE_FRIEND, (short)IM.IM_REQUEST);
		MBReqInviteFriend pMbReq = (MBReqInviteFriend)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iType = aType;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aFaceID
	 * @return
	 */
	public boolean RequestSendFace(int aFaceID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_SEND_FACE;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_SEND_FACE, (short)IM.IM_REQUEST);
		MBReqSendFace pMbReq = (MBReqSendFace)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iFaceID = aFaceID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @return
	 */
	public boolean RequestGiftList(int aUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_GIFT_LIST;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_GIFT_LIST, (short)IM.IM_REQUEST);
		MBReqGiftList pMbReq = (MBReqGiftList)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param iItemID
	 * @param aDesUserIDs
	 * @return
	 */
	public boolean RequestUseItem(int iItemID, Vector<Integer> aDesUserIDs) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_USE_ITEM;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_USE_ITEM, (short)IM.IM_REQUEST);
		MBReqUseItem pMbReq = (MBReqUseItem)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iItemID = iItemID;
			pMbReq.iDesUserCount = aDesUserIDs.size();
			pMbReq.iDesSeatIDs.addAll(aDesUserIDs);
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @return true成功
	 */
	public boolean requsetchop(int aUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_CHOP;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_CHOP, (short)IM.IM_REQUEST);
		MBReqChop pMbReq = (MBReqChop)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @return true成功
	 */
	public boolean requsetLeaveRoom(int aUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_LEAVEROOM;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_LEAVEROOM, (short)IM.IM_REQUEST);
		MBReqLeaveRoom pMbReq = (MBReqLeaveRoom)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aActionID
	 * @param aStartIndex
	 * @return true成功
	 */
	public boolean requsetGoodsList(int aActionID, int aStartIndex) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_GOODSLIST;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_GOODSLIST, (short)IM.IM_REQUEST);
		MBReqGoodsList pMbReq = (MBReqGoodsList)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iActionID = aActionID;
			pMbReq.iStartIndex = aStartIndex;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aActionID
	 * @param aStartIndex
	 * @return true成功
	 */
	public boolean requsetGoodsListGb(int aActionID, int aStartIndex) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_GOODSLIST_GB;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_GOODSLIST_GB, (short)IM.IM_REQUEST);
		MBReqGoodsListGb pMbReq = (MBReqGoodsListGb)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iActionID = aActionID;
			pMbReq.iStartIndex = aStartIndex;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @param aGoodsID
	 * @param aActionID动作ID	0 --- 买商品		1 --- 卖商品
	 * @return
	 */
	public boolean requestBusiness(int aUserID, int aGoodsID, int aActionID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_BUSINESS;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_BUSINESS, (short)IM.IM_REQUEST);
		MBReqBusiness pMbReq = (MBReqBusiness)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iActionID = aActionID;
			pMbReq.iUserID = aUserID;
			pMbReq.iGoodsID = aGoodsID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @param aStartIndex
	 * @return
	 */
	public boolean requestMyGoods(int aUserID, int aStartIndex) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_MYGOODS;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_MYGOODS, (short)IM.IM_REQUEST);
		MBReqMyGoods pMbReq = (MBReqMyGoods)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aUserID;
			pMbReq.iStartIndex = aStartIndex;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @param aStartIndex
	 * @return
	 */
	public boolean requestBusinessDetail(int aUserID, int aStartIndex) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_BUSINESSDETAIL;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_BUSINESSDETAIL, (short)IM.IM_REQUEST);
		MBReqBusinessDetail pMbReq = (MBReqBusinessDetail)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aUserID;
			pMbReq.iStartIndex = aStartIndex;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @param aActionID //动作ID	0 --- 获取VIP信息		1 --- 获取其它
	 * @return
	 */
	public boolean requestVipIfno(int aUserID, int aActionID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_VIPINFO;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_VIPINFO, (short)IM.IM_REQUEST);
		MBReqVIPinfo pMbReq = (MBReqVIPinfo)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iActionID = aActionID;
			pMbReq.iUserID = aUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aMsg聊天内容
	 * @return
	 */
	public boolean requestChat(String aMsg) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_CHAT;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_CHAT, (short)IM.IM_REQUEST);
		MBReqChat pMbReq = (MBReqChat)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iMsg = aMsg;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aSeatID
	 * @return
	 */
	public boolean requestPlayerInfo(int aSeatID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_PLAYINFO;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_PLAYINFO, (short)IM.IM_REQUEST);
		MBReqPlayerInfo pMbReq = (MBReqPlayerInfo)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iSeatID = aSeatID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	/**
	 * @param aUserID
	 * @return
	 */
	public boolean requestPlayerInfoTwo(int aUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_PLAYINFO_TWO;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_PLAYINFO_TWO, (short)IM.IM_REQUEST);
		MBReqPlayerInfoTwo pMbReq = (MBReqPlayerInfoTwo)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iDesUserID = aUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @return
	 */
	public boolean requestPlayerInfoThree(int aUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_PLAYINFO_THR;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_PLAYINFO_THR, (short)IM.IM_REQUEST);
		MBReqPlayerInfoThree pMbReq = (MBReqPlayerInfoThree)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iDesUserID = aUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @return
	 */
	public boolean requestPlayerInfoFour(int aUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_PLAYINFO_FOUR;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_PLAYINFO_FOUR, (short)IM.IM_REQUEST);
		MBReqPlayerInfoFour pMbReq = (MBReqPlayerInfoFour)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iDesUserID = aUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	public boolean requestShakeFriend(int aUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_SHAKE_FRIEND;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_SHAKE_FRIEND, (short)IM.IM_REQUEST);
		MBReqShakeFriend pMbReq = (MBReqShakeFriend)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iDesUserID = aUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	public boolean requestKick(int aUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_KICK;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_KICK, (short)IM.IM_REQUEST);
		MBReqKick pMbReq = (MBReqKick)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @return
	 */
	public boolean requsetcLeanDrunk(int aUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_CLEAN_DRUNK;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_CLEAN_DRUNK, (short)IM.IM_REQUEST);
		MBReqCleanDrunk pMbReq = (MBReqCleanDrunk)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @return
	 */
	public boolean requsetBuyWine(int aUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_BUY_WINE;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_BUY_WINE, (short)IM.IM_REQUEST);
		MBReqBuyWine pMbReq = (MBReqBuyWine)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @return
	 */
	public boolean requsetMainPage(int aUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_MAINPAGE;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_MAINPAGE, (short)IM.IM_REQUEST);
		MBReqMainPage pMbReq = (MBReqMainPage)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	public boolean requestHomeTakeGift(int aIndex) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_HOME_TAKE_GIFT;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_HOME_TAKE_GIFT, (short)IM.IM_REQUEST);
		MBReqHomeTakeGift pMbReq = (MBReqHomeTakeGift)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iIndex = aIndex;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @return
	 */
	public boolean requsetMainPageB(int aUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_MAINPAGE_B;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_MAINPAGE_B, (short)IM.IM_REQUEST);
		MBReqMainPageB pMbReq = (MBReqMainPageB)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aTime
	 * @return
	 */
	public boolean requsetHeart(long aTime) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_HART;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_HART, (short)IM.IM_REQUEST);
		MBReqHart pMbReq = (MBReqHart)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iTime = aTime;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aContent//反馈内容
	 * @return
	 */
	public boolean requsetFeekBack(String aContent) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_FEEDBACK;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_FEEDBACK, (short)IM.IM_REQUEST);
		MBReqFeekBack pMbReq = (MBReqFeekBack)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iContent = aContent;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aContent
	 * @return
	 */
	public boolean requestUpLoadShareText(String aContent) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_UPLOAD_SHARETEXT;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_UPLOAD_SHARETEXT, (short)IM.IM_REQUEST);
		MBReqUpLoadShareText pMbReq = (MBReqUpLoadShareText)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iContent = aContent;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param iVersion 客户端版本号
	 * @param iCurrentSize 开始的索引
	 * @return
	 */
	public boolean requsetUpdate(int aVersion, int aStartIndex) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_UPDATE;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_UPDATE, (short)IM.IM_REQUEST);
		MBReqUpdate pMbReq = (MBReqUpdate)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iVersion = aVersion;
			pMbReq.iStartIndex = aStartIndex;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	public boolean requestHomeGiftList(int aAction) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_HOME_GIFT_LIST;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_HOME_GIFT_LIST, (short)IM.IM_REQUEST);
		MBReqHomeGiftList pMbReq = (MBReqHomeGiftList)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iAction = aAction;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @param aAction// 0 --- 加好友  1 --- 删好友
	 * @return
	 */
	public boolean requestAddFriend(int aUserID, int aAction) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_ADD_FRIEND;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_ADD_FRIEND, (short)IM.IM_REQUEST);
		MBReqAddFriend pMbReq = (MBReqAddFriend)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aUserID;
			pMbReq.iAction = aAction;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aRankType
	 * @param aStartIndex
	 * @return
	 */
	public boolean requestRank(int aRankType, int aStartIndex) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_RANK;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_RANK, (short)IM.IM_REQUEST);
		MBReqRank pMbReq = (MBReqRank)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iRankType = aRankType;
			pMbReq.iStartIndex = aStartIndex;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
 
	/**
	 * @param aDesUserID
	 * @return
	 */
	public boolean requestTrace(int aDesUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_TRACE;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_TRACE, (short)IM.IM_REQUEST);
		MBReqTrace pMbReq = (MBReqTrace)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aDesUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	public boolean requestDelLeaveMsg(int aDesUserID, int aLeaveMsgID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_DEL_LEAVE_MSG;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_DEL_LEAVE_MSG, (short)IM.IM_REQUEST);
		MBReqDelLeaveMsg pMbReq = (MBReqDelLeaveMsg)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iDesUserID = aDesUserID;
			pMbReq.iFeedID = aLeaveMsgID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aAction
	 * @return
	 */
	public boolean requestDelPic(int aAction) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_DEL_PIC;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_DEL_PIC, (short)IM.IM_REQUEST);
		MBReqDelPic pMbReq = (MBReqDelPic)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iAction = aAction;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aAction
	 * @return
	 */
	public boolean requestRoomListB(int aAction) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_ROOM_LIST_B;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_ROOM_LIST_B, (short)IM.IM_REQUEST);
		MBReqRoomListB pMbReq = (MBReqRoomListB)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iAction = aAction;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	/**
	 * @param aAction
	 * @return
	 */
	public boolean requestRoomList(int aAction) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_ROOM_LIST;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_ROOM_LIST, (short)IM.IM_REQUEST);
		MBReqRoomList pMbReq = (MBReqRoomList)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iAction = aAction;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aAction
	 * @return
	 */
	public boolean requestActivityRank(int aAction) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_ACTIVITY_RANK;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_ACTIVITY_RANK, (short)IM.IM_REQUEST);
		MBReqActivityRank pMbReq = (MBReqActivityRank)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iActivityID = aAction;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	/**
	 * @param aDesUserID
	 * @return
	 */
	public boolean requestDing(int aDesUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_DING;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_DING, (short)IM.IM_REQUEST);
		MBReqDing pMbReq = (MBReqDing)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aDesUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aStartIndex
	 * @return
	 */
	public boolean requestFriendList(int aStartIndex) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_FRIEND_LIST;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_FRIEND_LIST, (short)IM.IM_REQUEST);
		MBReqFriendList pMbReq = (MBReqFriendList)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iStartIndex = aStartIndex;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	public boolean requestUploadPic(int aFileSize, int aCurrentSize, byte[] aBuf) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN + aCurrentSize];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_UPLOAD_PIC;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_UPLOAD_PIC, (short)IM.IM_REQUEST);
		MBReqUpLoadPic pMbReq = (MBReqUpLoadPic)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iFileSize = aFileSize;
			pMbReq.iCurrentSize = aCurrentSize;
			Tools.debug("requestUploadPic " + aCurrentSize);
			pMbReq.iBuf = aBuf;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	

	/**
	 * @param aActivityType
	 * @param aStartIndex
	 * @return
	 */
	public boolean requestActivity(int aActivityType, int aStartIndex) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_ACTIVITY_LIST;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_ACTIVITY_LIST, (short)IM.IM_REQUEST);
		MBReqActivityList pMbReq = (MBReqActivityList)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iActivityType = aActivityType;
			pMbReq.iStartIndex = aStartIndex;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	/**
	 * @param aActivityID
	 * @return
	 */
	public boolean requestActivityEnroll(int aActivityID, int aAction) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_ACTIVITY_ENROLL;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_ACTIVITY_ENROLL, (short)IM.IM_REQUEST);
		MBReqActivityEnroll pMbReq = (MBReqActivityEnroll)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iActivityID = aActivityID;
			pMbReq.iAction = aAction;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
 
	public boolean requestActivityDetail(int aActivityID, int aAction) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_ACTIVITY_DETAIL;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_ACTIVITY_DETAIL, (short)IM.IM_REQUEST);
		MBReqActivityDetail pMbReq = (MBReqActivityDetail)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iActivityID = aActivityID;
			pMbReq.iAction = aAction;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aDesUserID
	 * @param aMsg
	 * @return
	 */
	public boolean requestLeaveMsg(int aDesUserID, String aMsg) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_LEAVE_MSG;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_LEAVE_MSG, (short)IM.IM_REQUEST);
		MBReqLeaveMsg pMbReq = (MBReqLeaveMsg)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aDesUserID;
			pMbReq.iMsg = aMsg;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aDesUserID
	 * @param aMsg
	 * @return
	 */
	public boolean requestLeaveListMsg(int aDesUserID, int aStartIndex) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_LEAVE_MSG_LIST;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_LEAVE_MSG_LIST, (short)IM.IM_REQUEST);
		MBReqLeaveMsgList pMbReq = (MBReqLeaveMsgList)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aDesUserID;
			pMbReq.iStartIndex = aStartIndex;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aAction
	 * @return
	 */
	public boolean requestAccount(int aAction, String aPwd) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_ACCOUNT;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_ACCOUNT, (short)IM.IM_REQUEST);
		MBReqAccount pMbReq = (MBReqAccount)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iAction = aAction;
			pMbReq.iPwd = aPwd;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	

	/**
	 * @param aDesUserID
	 * @param aMsg
	 * @param aSecret 0--普通、1--私信
	 * @param aReply 0--没回复、1--回复
	 * @return
	 */
	public boolean requestLeaveMsgB(int aDesUserID, String aMsg, short aSecret, short aReply) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_LEAVE_MSG_B;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_LEAVE_MSG_B, (short)IM.IM_REQUEST);
		MBReqLeaveMsgB pMbReq = (MBReqLeaveMsgB)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aDesUserID;
			pMbReq.iMsg = aMsg;
			pMbReq.iSecret = aSecret;
			pMbReq.iReply = aReply;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aDesUserID
	 * @param aMsg
	 * @return
	 */
	public boolean requestLeaveListMsgB(int aDesUserID, int aStartIndex) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_LEAVE_MSG_LIST_B;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_LEAVE_MSG_LIST_B, (short)IM.IM_REQUEST);
		MBReqLeaveMsgListB pMbReq = (MBReqLeaveMsgListB)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aDesUserID;
			pMbReq.iStartIndex = aStartIndex;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aDesUserID
	 * @param aAction // 0 --- 同意加好友    1 --- 拒绝加好友
	 * @return
	 */
	public boolean requestAgreeFriend(int aDesUserID, int aAction) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_AGREE_FRIEND;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_AGREE_FRIEND, (short)IM.IM_REQUEST);
		MBReqAgreeFriend pMbReq = (MBReqAgreeFriend)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iDesUID = aDesUserID;
			pMbReq.iAction = aAction;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	
	/**
	 * @param aAction 0--普遍
	 * @return true 发送成功
	 */
	public boolean requestSearchPeople(int aAction ) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_SEARCH_PEOPLE;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_SEARCH_PEOPLE, (short)IM.IM_REQUEST);
		MBReqSearchPeople pMbReq = (MBReqSearchPeople)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iAction = aAction ;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aAction
	 * @return
	 */
	public boolean requestRoomListC(int aAction) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_ROOM_LIST_C;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_ROOM_LIST_C, (short)IM.IM_REQUEST);
		MBReqRoomListC pMbReq = (MBReqRoomListC)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iAction = aAction;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aRoomID -1 //默认系统分别
	 * @param aTableID -1//默认系统分别
	 * @return true    发送成功
	 */
	public boolean RequestLoginRoomB(int aRoomID, int aTableID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_LOGINROOM_B;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_LOGINROOM_B, (short)IM.IM_REQUEST);
		MBReqLoginRoomB pMbReqLoginRoom = (MBReqLoginRoomB)pMobileMsg.m_pMsgPara;
		if(pMbReqLoginRoom != null) {
			pMbReqLoginRoom.iRoomID = aRoomID;
			pMbReqLoginRoom.iTableID = aTableID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aAction 0 --- 订房     1 --- 进入
	 * @param aPara -1//默认系统分别
	 * @param pswd   //登陆房间密码
	 * @return true    发送成功
	 */
	public boolean RequestLoginVipRoom(int aRoomID, int aAction, int aPara, String pswd) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_LOGIN_VIPROOM;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_LOGIN_VIPROOM, (short)IM.IM_REQUEST);
		MBReqLoginVipRoom pMbReq = (MBReqLoginVipRoom)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iRoomID = aRoomID;
			pMbReq.iAction = aAction;
			pMbReq.iPara = aPara;
			pMbReq.iUserPwd = pswd;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aPwdQFlag//提示问题标识,不能是0，以1开始，客户端自定义
	 * @param pswdquestion
	 * @return
	 */
	public boolean RequestSetPWDQuestion(short aPwdQFlag, String pswdquestion) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_SET_PWD_QUESTION;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_SET_PWD_QUESTION, (short)IM.IM_REQUEST);
		MBReqSetPwdQuestion pMbReq = (MBReqSetPwdQuestion)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iPwdQFlag = aPwdQFlag;
			pMbReq.iPwdQ = pswdquestion;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	public boolean RequestResetPswd(short aPwdQFlag, String pswdquestion, String psw) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_RESET_PWD;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_RESET_PWD, (short)IM.IM_REQUEST);
		MBReqResetPwd pMbReq = (MBReqResetPwd)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iPwdQFlag = aPwdQFlag;
			pMbReq.iPwdQ = pswdquestion;
			pMbReq.iNewPwd = psw;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUID
	 * @param aPwd
	 * @param aPwdQFlag
	 * @param aPwdQ
	 * @return
	 */
	public boolean RequestRegisterB(String aUID, String aPwd, short aPwdQFlag, String aPwdQ ) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = -1;
		pMobileMsg.iFlag = 1;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead = null;
		pMobileMsg.m_MsgHead = new MobileMsgHead();
		pMobileMsg.m_CSHead.iVer = (short) 1200;
		pMobileMsg.m_CSHead.iPlatformID = 'A';
		pMobileMsg.m_CSHead.iMobileType = 1;
		pMobileMsg.m_CSHead.iUID = "";
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_REGISTER_B;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_REGISTER_B, (short)IM.IM_REQUEST);
		MBReqRegisterB pReqRegister = (MBReqRegisterB)pMobileMsg.m_pMsgPara;
		if(pReqRegister != null) {
			pReqRegister.iUserID = aUID;
			pReqRegister.iUserPswd = aPwd;
			pReqRegister.iPwdQFlag = aPwdQFlag;
			pReqRegister.iPwdQ = aPwdQ;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
			if(pMobileMsg.Decode(temp.array(), len) < 0) {
				return false;
			}
			else {
				Result = OnSend(pData, len);
			}
		}
		return Result;
	}
	
	/**
	 * @param aUID
	 * @param aPwd
	 * @param aPwdQFlag
	 * @param aPwdQ
	 * @return
	 */
	public boolean RequestResetThirdUserInfo(String aUID, String aPwd, short aPwdQFlag, String aPwdQ ) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 1;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead = null;
		pMobileMsg.m_MsgHead = new MobileMsgHead();
		pMobileMsg.m_CSHead.iVer = (short) 1200;
		pMobileMsg.m_CSHead.iPlatformID = 'A';
		pMobileMsg.m_CSHead.iMobileType = 1;
		pMobileMsg.m_CSHead.iUID = "";
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_RESET_THIRD_USERINFO;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_RESET_THIRD_USERINFO, (short)IM.IM_REQUEST);
		MBReqResetThirdUserInfo pReq = (MBReqResetThirdUserInfo)pMobileMsg.m_pMsgPara;
		if(pReq != null) {
			pReq.iUserID = aUID;
			pReq.iUserPswd = aPwd;
			pReq.iPwdQFlag = aPwdQFlag;
			pReq.iPwdQ = aPwdQ;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
			if(pMobileMsg.Decode(temp.array(), len) < 0) {
				return false;
			}
			else {
				Result = OnSend(pData, len);
			}
		}
		return Result;
	}
	
	/**
	 * @param aUserID
	 * @return true成功
	 */
	public boolean requestLighting(int aUserID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_LIGHTNING;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_LIGHTNING, (short)IM.IM_REQUEST);
		MBReqLightning pMbReq = (MBReqLightning)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iUserID = aUserID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aDesUserID
	 * @param iType //类型 0 --- 结盟    1 --- 解散
	 * @return
	 */
	public boolean RequestUinte(int aDesUserID, int iType) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_UNITE;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_UNITE, (short)IM.IM_REQUEST);
		MBReqUnite pMbReq = (MBReqUnite)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iDesUID = aDesUserID;
			pMbReq.iAction = iType;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aRoomID -1 //默认系统分别
	 * @param aTableID -1//默认系统分别
	 * @return true    发送成功
	 */
	public boolean RequsetLoginRoomUnite(int aRoomID, int aTableID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_LOGINROOM_UNITE;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_LOGINROOM_UNITE, (short)IM.IM_REQUEST);
		MBReqLoginRoomUnite pMbReq = (MBReqLoginRoomUnite)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iRoomID = aRoomID;
			pMbReq.iTableID = aTableID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aUserName
	 * @return
	 */
	public boolean requestUID(String aUserName) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_UID;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_UID, (short)IM.IM_REQUEST);
		MBReqUID pMbReq = (MBReqUID)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iDesUserName = aUserName;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/**
	 * @param aAction
	 * @return true 发送成功
	 */
	public boolean RequestVipUpgrade(int aAction) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_VIP_UPGRADE;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_VIP_UPGRADE, (short)IM.IM_REQUEST);
		MBReqVipUpgrade pMbReq = (MBReqVipUpgrade)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iAction = aAction;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	/**
	 * 判断网络状态是否可用
	 * @return true:网络可用; false:网络不可用
	 */
	
	
	public boolean isConnectInternet() {
		ConnectivityManager conManager=(ConnectivityManager)iContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if(networkInfo != null){ 
			return networkInfo.isAvailable();
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.socket.SocketSend#OnThreadSend(byte[], int)
	 */
	@Override
	public boolean OnThreadSend(byte[] data, int len) {
		if(socketEx == null) OnClose(socketEx);
		try{
			Tools.debug("OnSend OnThreadSend " + len);
			if(!isConnectInternet()) {
				OnReportError(this, SocketErrorType.Error_NetWork_Enable);
				return false;
			}
//			stopTimer();
//			startTimer(30, iMobileMsgCS.m_MsgHead.m_sMsgID);
//			GameMain.iHeart.startTimer(120, 120);
			OutputStream ops = socketEx.getOutputStream();
			ops.write(data, 0, len);
			ops.flush();
			iCurTaskMsgID = iMobileMsgCS.m_MsgHead.m_sMsgID;
			Tools.debug("iSessionID = " + iSessionID + " m_sMsgID = " + iMobileMsgCS.m_MsgHead.m_sMsgID + " 发送成功！");
		}catch(IOException e) {
			e.printStackTrace();
			OnReportError(this, SocketErrorType.Error_NetWork_Enable);
			return false;
		}
		return true;
	}

	public boolean RequestMissionList(int aType) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_MISSION_LIST;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_MISSION_LIST, (short)IM.IM_REQUEST);
		MBReqMissionList pMbReq = (MBReqMissionList)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iType = aType;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	public boolean RequestAward(int aGiftID) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_AWARD;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_AWARD, (short)IM.IM_REQUEST);
		MBReqAward pMbReq = (MBReqAward)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iGiftID = aGiftID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	public boolean RequestCommitPlayerData(int iSelectedRoleID, String aName) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_COMMIT_PLAYER_DATA;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_COMMIT_PLAYER_DATA, (short)IM.IM_REQUEST);
		MBReqCommitPlayerData pMbReq = (MBReqCommitPlayerData)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iNick = aName;
			pMbReq.iSex = (short) iSelectedRoleID;
		}
		else {
			return false;
		}
		boolean Result = false;
		len = (short) pMobileMsg.Encode(pData, len);
		if( len >= 0) {
			if(Debug) {
				ByteBuffer temp = ByteBuffer.wrap(pData, 0, len);
				if(pMobileMsg.Decode(temp.array(), len) < 0) {
					return false;
				}
			}
			Result = OnSend(pData, len);
		}
		return Result;
	}
	
	/* 
	 * @see com.funoble.lyingdice.socket.SocketHandler#OnTimeOut(java.lang.Object)
	 */
	@Override
	public void OnTimeOut(Object socket) {
		Tools.debug("GameProtocol::OnTimeOut()");
		MyArStation.iGameProtocol.requsetHeart(System.currentTimeMillis());
	}
}
