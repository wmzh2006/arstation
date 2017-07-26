package com.funoble.myarstation.gamebase;

//import gui.EditorStudio;


import java.io.DataInputStream;

//import struct.Frame;
//import struct.Project;

/**
 * Title: 精灵动作
 * Description: 一个精灵的行走、魔法等动作
 * <p>Copyright: 腾讯科技(深圳)有限公司.</p>
 * @author cheerluo
 */
public class SpriteAction extends BaseAction {

	public byte[] timeDelay;//时间间隔
	
	/**
	 * 释放自己
	 */
	public void releaseSelf() {
		if(timeDelay != null) {
			timeDelay = null;
		}
		super.releaseSelf();
		//debug
//		System.out.println("SpriteAction释放!");
	}
	
	public SpriteAction() {
	}
	  
	public void setFrameTime(int frameIndex, int time){
		if(timeDelay == null || frameIndex >= timeDelay.length || frameIndex < 0){
			return;
		}
		timeDelay[frameIndex]=(byte)time;
	}
  
	/**
	 * 导入二进制结构
	 * @param dis DataInputStream
	 * @throws Exception
	 */
	public void inputStream(DataInputStream dis, int format) throws Exception {
		super.inputStream(dis, format);
		int count = dis.readShort();
		timeDelay=new byte[count];
		if(count <= 0) {
//			setSize(getActWidth(), getActHeight());
			return;
		}
		initBaseActionSize(count);
//		System.out.println("SpriteAction count = " + count);
	    for (int i = 0; i < count; i++) {
	        short tmp = dis.readShort();
	        byte delay=0;
	        int tempindex=0;
	        if(tmp==-1){//扩展，用更多字节表示
	      	  delay=dis.readByte();
	      	tempindex=dis.readShort();
	        }
	        else{
	      	  delay=(byte)(tmp>>>10);
	      	tempindex=(tmp&0x3FF);
	        }
	        //System.out.println("in delay:"+this.getFrameTime(index)+",index:"+index+" "+this);
	        ActionFrame frame = (ActionFrame) Project.getLoadingProject()
	        	.getObject(tempindex, Project.FRAME);
			setAction(frame, i);
	        setFrameTime(i, delay);
	    }
//		setSize(getActWidth(), getActHeight());
	}
	
	
}
