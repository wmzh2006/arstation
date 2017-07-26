/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: DemoAppSocket.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-4-27 下午03:01:54
 *******************************************************************************/
package com.funoble.myarstation.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.common.Util;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.socket.protocol.MBReqAddFriend;
import com.funoble.myarstation.socket.protocol.MBReqBusiness;
import com.funoble.myarstation.socket.protocol.MBReqBusinessDetail;
import com.funoble.myarstation.socket.protocol.MBReqBuyWine;
import com.funoble.myarstation.socket.protocol.MBReqChat;
import com.funoble.myarstation.socket.protocol.MBReqChop;
import com.funoble.myarstation.socket.protocol.MBReqCleanDrunk;
import com.funoble.myarstation.socket.protocol.MBReqCreateRole;
import com.funoble.myarstation.socket.protocol.MBReqDelPic;
import com.funoble.myarstation.socket.protocol.MBReqDing;
import com.funoble.myarstation.socket.protocol.MBReqDownLoadPic;
import com.funoble.myarstation.socket.protocol.MBReqFeekBack;
import com.funoble.myarstation.socket.protocol.MBReqFriendList;
import com.funoble.myarstation.socket.protocol.MBReqGoodsList;
import com.funoble.myarstation.socket.protocol.MBReqHart;
import com.funoble.myarstation.socket.protocol.MBReqLeaveRoom;
import com.funoble.myarstation.socket.protocol.MBReqLogin;
import com.funoble.myarstation.socket.protocol.MBReqLoginRoom;
import com.funoble.myarstation.socket.protocol.MBReqMainPage;
import com.funoble.myarstation.socket.protocol.MBReqMyGoods;
import com.funoble.myarstation.socket.protocol.MBReqPlayerInfo;
import com.funoble.myarstation.socket.protocol.MBReqPlayerInfoTwo;
import com.funoble.myarstation.socket.protocol.MBReqRank;
import com.funoble.myarstation.socket.protocol.MBReqReady;
import com.funoble.myarstation.socket.protocol.MBReqRegister;
import com.funoble.myarstation.socket.protocol.MBReqRoomList;
import com.funoble.myarstation.socket.protocol.MBReqShout;
import com.funoble.myarstation.socket.protocol.MBReqTrace;
import com.funoble.myarstation.socket.protocol.MBReqUpLoadPic;
import com.funoble.myarstation.socket.protocol.MBReqUpLoadShareText;
import com.funoble.myarstation.socket.protocol.MBReqUpdate;
import com.funoble.myarstation.socket.protocol.MBReqVIPinfo;
import com.funoble.myarstation.socket.protocol.MBReqVisitorLogin;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.MobileMsgHead;
import com.funoble.myarstation.socket.protocol.MsgPara;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.ReqMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.SocketErrorType;


public class DownloadProtocol implements SocketHandler, TimerClient {
	public static final boolean Debug = false;
	public static final int 	BUFFER_LEN = 8 * 1024;
	//Socket
	private SocketEngine 			socketEx = null;
	private final int 				eventId = 1;
	private DownloadSocketHandler		issh=null;
	private String 					ip="127.0.0.1";
	private int 					port=20000;
	private Context					iContext;
	
	//通信逻辑相关
	private	int						iSessionID;			//与服务器通话的ID，防止作弊
	private	MobileMsg 				iMobileMsgCS;		//客户端到服务器通信包
	private	MobileMsg				iMsgSC;				//服务器到客户端通信包
	private HashMap<Integer, MsgPara> iMsgScArray;	//服务器到客户端通信包数组
	private ByteBuffer				iMsgSCBuffer;	//服务器到客户端通信包数缓存
	private byte[]					iRevBuffer;		//服务器到客户端通信包数缓存
	private byte[]					iPakBuffer;		//一个数据包的缓存
	private int 					iRecvLen = 0; //接收到的数据总长度
	private int						iCurrentBufferLen;
	//游戏逻辑相关
	public	short 					iRoomID;			//房间ID
	public	short 					iTableID;			//桌子ID
	public  short 					iSeatID;			//座位ID
	public	short 					iOtherPlayerNum;	//其他玩家数
	private Timer					iSendTimer;			//发送计时器
	private TimerTask 				mTimerTask;
	private long					time;
	private int 					iCurTaskMsgID;
	
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
    
	public DownloadProtocol(DownloadSocketHandler issh) {
		this.issh = issh;
//		iContext = (Context)issh;
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
		this.iMsgSCBuffer = ByteBuffer.allocate(BUFFER_LEN + 2);
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
			return aMsgSize;
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
//		iMsgSCBuffer = ByteBuffer.allocate(BUFFER_LEN + 2);
//		iCurrentBufferLen = 0;
		cleanMsgBuffer();
		if(!start()) {
			TimerCtl.startTimer(this,eventId,30*1000,true);
			return false;
		}
		return true;
	}
	
	//这个方法一般不需要直接调用
	public boolean start() {
		return startClient();
	}
	
	//停止socket   	
	public boolean stop() {
		TimerCtl.stopTimer(this,eventId);
		return stopClient();
	}
	
	public boolean OnSend(byte[] aOutBuffer, int aOutLenght) {
		if(socketEx == null) return false;
		try{
			if(!isConnectInternet()) {
				OnReportError(this, SocketErrorType.Error_NetWork_Enable);
				return false;
			}
			stopTimer();
			startTimer(15, iMobileMsgCS.m_MsgHead.m_sMsgID);
			DataOutputStream Out;
			Out = new DataOutputStream(socketEx.getOutputStream());
			Out.write(aOutBuffer, 0, aOutLenght);
			Out.flush();
			iCurTaskMsgID = iMobileMsgCS.m_MsgHead.m_sMsgID;
			Tools.debug("m_sMsgID = " + iMobileMsgCS.m_MsgHead.m_sMsgID + " 发送成功！");
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
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
			TimerCtl.stopTimer(this,eventId);
			return true;
		}catch(IOException e)
		{
			OnReportError(socketEx, SocketErrorType.Error_DisConnection);
			socketEx = null;
		}
		return false;
		
	}
	
	protected boolean stopClient() {
		if(socketEx != null) {
			try{
				socketEx.close();
				return true;
			}catch(IOException e){
				e.printStackTrace();
			}
			finally {
				socketEx = null;
				return false;
			}
		}
		else {
			return true;
		}
	}
	
	
	public void timerEvent(int id) {
		start();
	}
	
	public void cleanMsgBuffer() {
//		iMsgSCBuffer = null;
//		iMsgSCBuffer = ByteBuffer.allocate(BUFFER_LEN + 2);
		System.out.println("DownloadPic 当前长度=" + iRecvLen + " 清空Buf!");
		iRecvLen = 0;
	}
	
	public void OnReceive(Object socket,byte[] aInBuffer, int aInLenght){
		if(aInLenght <= 0) {
			return;
		}
//		System.out.println("DownloadPic 收到数据长度=" + aInLenght + " 接收前的长度=" + iRecvLen);
		int nRecvLen = 0;
		int nRecvAllLen = 0;
		int offset = iRecvLen;
		System.arraycopy(aInBuffer, 0, iRevBuffer, offset, aInLenght);
		iRecvLen += aInLenght;
		nRecvAllLen = iRecvLen;
//		System.out.println("DownloadPic 当前长度=" + iRecvLen);
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
//			System.out.println("DownloadPic 数据包长=" + nRecvLen);
			if ( (BUFFER_LEN <= nRecvLen) || (8 > nRecvLen) ) {	
				iRecvLen = 0;
//				System.out.println("DownloadPic 非法数据! offset=" + offset);
//				System.out.println("DownloadPic " + Util.bytesToHexString(iRevBuffer));
				return;
			}
			nRecvAllLen = nRecvAllLen - nRecvLen;
			if( nRecvAllLen < 0 ) {
				nRecvAllLen = nRecvAllLen + nRecvLen;
				//停止计时器
				stopTimer();
				//不是完整的数据包，重新计时等待
				startTimer(15, 0);
				//
//				System.out.println("DownloadPic  数据包不完整nRecvAllLen =" + nRecvAllLen);
				break;
			}
			System.arraycopy(iRevBuffer, offset, iPakBuffer, 0, nRecvLen);
			offset += nRecvLen;
			//请求解压
			int tRet = Decode(iPakBuffer, nRecvLen);
//			System.out.println("DownloadPic 解码结果 =" + tRet + " 包长=" + nRecvLen);
//			System.out.println("DownloadPic 解码后的长度=" + nRecvAllLen);
			if(tRet > 0) {
//				System.out.println("DownloadPic 处理数据 MsgID=" + iMsgSC.m_MsgHead.m_sMsgID);
				DoProcessCmd(iMsgSC, iMsgSC.m_MsgHead.m_cMsgType, iMsgSC.m_MsgHead.m_sMsgID);
			}
			else {
//				System.out.println("DownloadPic 解码出错Ret=" + tRet);
			}
			//停止计时器
			stopTimer();
		}
		if( 0 == nRecvAllLen ) {
			iRecvLen = 0;
//			System.out.println("DownloadPic 所有数据处理完成！");
		}
		else {
			if (nRecvAllLen >= BUFFER_LEN || nRecvAllLen < 0) {
				iRecvLen = 0;
				return;
			}
			//
			iRecvLen = nRecvAllLen;
			System.arraycopy(iRevBuffer, offset, iRevBuffer, 0, nRecvAllLen);
//			System.out.println("DownloadPic 拷贝没处理完成的数据！");
		}
	}
	
	public void OnReceiveOld(Object socket,byte[] aInBuffer, int aInLenght){
		stopTimer();
		if(iMsgSCBuffer.position() + aInLenght <= BUFFER_LEN) {
			Tools.debug("iMsgSCBuffer.position()" + iMsgSCBuffer.position());
//			int pos = iMsgSCBuffer.position();
//			iMsgSCBuffer.position(pos);
			iMsgSCBuffer.put(aInBuffer);
			Tools.debug("iMsgSCBuffer = " + iMsgSCBuffer.toString() + "aInBuffer = " + aInBuffer + "byte[] = " + Util.bytesToHexString(iMsgSCBuffer.array()));
		}
//		Tools.debug("OnReceive" + Util.bytesToHexString(aInBuffer));
//		Tools.debug("aInBuffer len = " + aInBuffer.length + "aInLenght = " + aInLenght);
//		int dataLen = 0; 
//		int pos = 0;
//		ByteBuffer Temp = ByteBuffer.wrap(iMsgSCBuffer.array(), 0, iMsgSCBuffer.position());
//		Tools.debug(" Temp.capacity() = " + Temp.capacity() + "Temp.limit()" + Temp.limit() + "Temp.position" + Temp.position());
////		Temp.put(aInBuffer);
//		Tools.debug(" put Temp.capacity() = " + Temp.capacity() + "Temp.limit()" + Temp.limit() + "Temp.position" + Temp.position());
//		Tools.debug("Temp = " + Util.bytesToHexString(Temp.array()));
////		Temp.flip();
//		Tools.debug(" Temp.toString = " + Temp.toString());
//		dataLen = Temp.getShort();
//		Tools.debug("dataLen = " + dataLen);
//		do{
//			byte[] data = new byte[dataLen];
//			Tools.debug("do Temp = " + Util.bytesToHexString(Temp.array()));
//			Tools.debug(" do Temp.capacity() = " + Temp.capacity() + "Temp.limit()" + Temp.limit() + "Temp.position" + Temp.position());
//			Tools.debug("data = " + Util.bytesToHexString(data));
//			if(dataLen > Temp.limit() - pos) {
//			}
//			else {
//				Temp.position(pos);
//				Temp.get(data, 0, dataLen);
//			}
//			pos = Temp.position();
//			Tools.debug(" do 2 Temp.capacity() = " + Temp.capacity() + "Temp.limit()" + Temp.limit() + "Temp.position" + Temp.position());
//			Tools.debug("data2 = " + Util.bytesToHexString(data));
//			Tools.debug("aInBuffer len = " + aInBuffer.length+"aInLenght" + aInLenght);
			int len = 0;
			int temlen = 0;
			do {
				if((len = Decode(iMsgSCBuffer.array(), iMsgSCBuffer.position())) > 0) {
					Tools.debug("Decode" + aInBuffer.toString());
					DoProcessCmd(iMsgSC, iMsgSC.m_MsgHead.m_cMsgType, iMsgSC.m_MsgHead.m_sMsgID);
//					iMsgSCBuffer.flip();
					temlen = iMsgSCBuffer.position() - len;
					Tools.debug("temlen = " + temlen + " iMsgSCBuffer.position() = " + iMsgSCBuffer.position() + " len = " + len);
					if(temlen > 0) {
						byte[] TempData = new byte[temlen];
						iMsgSCBuffer.clear();
						iMsgSCBuffer.position(len);
						iMsgSCBuffer.get(TempData, 0, TempData.length);
						iMsgSCBuffer = null;
						iMsgSCBuffer = ByteBuffer.allocate(BUFFER_LEN + 2);
						iMsgSCBuffer.put(TempData);
					}
					else {
						iMsgSCBuffer = null;
						iMsgSCBuffer = ByteBuffer.allocate(BUFFER_LEN + 2);
						temlen = 0;
					}
					Tools.debug("iMsgSCBuffer = " + iMsgSCBuffer.toString() + "byte[] = " +  Util.bytesToHexString(iMsgSCBuffer.array()));
				}
				else if(len == -1){
					Tools.debug("OnReceive 解码出错!" + iMsgSC.m_MsgHead.toString());
					startTimer(15, 0);
//				OnReportError(socketEx, SocketErrorType.Error_DecodeError);
				}
				else if(len <= -5){
					iMsgSCBuffer = null;
					iMsgSCBuffer = ByteBuffer.allocate(BUFFER_LEN + 2);
				}
			}while(temlen > 0);
//			Tools.debug("do dataLen = " + dataLen + Temp.toString()+Util.bytesToHexString(Temp.array()));
//			if(Temp.position() < Temp.limit()) {
//				dataLen = Temp.getShort();
//			}
//			else {
//				dataLen = 0;
//			}
//			data = null;
//		}while(dataLen > 0);
	}
	
	protected void DoProcessCmd(Object socket, int aMobileType, int aMsgID) {
		if(issh !=null) {
			issh.OnDlProcessCmd(socket, aMobileType, aMsgID);
		}
	}
	
	public void OnConnect(Object socket) {
		if(issh !=null) issh.OnDlConnect(socket);
	}
	
	
	public void OnClose(Object socket) {
//		synchronized (this) {
//			notifyAll();
//		}
		TimerCtl.startTimer(this,eventId,30*1000,true);
		if(issh !=null) issh.OnDlClose(socket);
		Log.e("", "appsocket onColse!");
	}
	
	public void OnReportError(Object socket, int aErrorCode) {
		if(issh !=null) issh.OnDlReportError(socket, aErrorCode);
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
		pMobileMsg.m_CSHead.iUID = aUID + Util.getPhoneStatus();
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
	 * @param aUserID
	 * @param aDice
	 * @param Count
	 * @return true 发送成功
	 */
	public boolean RequsetShout(int aUserID, int aDice, int Count) {
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
	
	/**
	 * @param aFileName
	 * @param aStartIndex
	 * @return
	 */
	public boolean requestDownloadPic(String aFileName, int aStartIndex) {
		byte[] pData = new byte[MobileMsg.MAX_CACHE_MSG_LEN];
		short len = 0;
		//编码
		MobileMsg pMobileMsg = iMobileMsgCS;
		pMobileMsg.iSessionID = iSessionID;
		pMobileMsg.iFlag = 0;
		//每次都要重新new iCSHead
		pMobileMsg.m_MsgHead.m_cMsgType = IM.IM_REQUEST;
		pMobileMsg.m_MsgHead.m_sMsgID = ReqMsg.MSG_REQ_DOWNLOAD_PIC;
		pMobileMsg.m_pMsgPara = null;
		pMobileMsg.m_pMsgPara = pMobileMsg.CreateMsgPara((short)ReqMsg.MSG_REQ_DOWNLOAD_PIC, (short)IM.IM_REQUEST);
		MBReqDownLoadPic pMbReq = (MBReqDownLoadPic)pMobileMsg.m_pMsgPara;
		if(pMbReq != null) {
			pMbReq.iFileName = aFileName;
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
	 * 判断网络状态是否可用
	 * @return true:网络可用; false:网络不可用
	 */
	
	public boolean isConnectInternet() {
		ConnectivityManager conManager=(ConnectivityManager)MyArStation.mGameMain.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if(networkInfo != null){ 
			return networkInfo.isAvailable();
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.socket.SocketHandler#OnTimeOut(java.lang.Object)
	 */
	@Override
	public void OnTimeOut(Object socket) {
	}
}
