package com.funoble.myarstation.gamebase;

import java.io.DataInputStream;

import com.funoble.myarstation.utils.ActivityUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;


/**
 * Title: 动作单帧
 * Description: 定义一系列的附有坐标的图片单元
 * <p>Copyright: 腾讯科技(深圳)有限公司.</p>
 * @author mark
 */
public class ActionFrame extends BaseAction {

	public byte iShowPriority = 0;	//显示优先级
	private short[][] clipPosition = null;// 切图的坐标
	private int[][] logicRect = null;//逻辑矩形
	
	/**
	 * 释放自己
	 */
	public void releaseSelf() {
		if(logicRect != null) {
			for(int i=0; i<logicRect.length; i++) {
				logicRect[i] = null;
			}
			logicRect = null;
		}
		if(clipPosition != null) {
			for(int i=0; i<clipPosition.length; i++) {
				clipPosition[i] = null;
			}
			clipPosition = null;
		}
		super.releaseSelf();
		//debug
//		System.out.println("ActionFrame释放!");
	}
	
	public ActionFrame() {
	}

	/**
	 * 放大或者缩小显示
	 * @param g Canvas
	 * @param x int
	 * @param y int
	 */
	public void paint(Canvas g, int x, int y) {
		BaseAction[] list = actionList;
		if (list != null) {
			g.translate(x, y);
			int size = list.length;
			for (int i = 0; i < size; i++) { // 显示全部序列
				list[i].paint(g, clipPosition[i][0], clipPosition[i][1]);
			}
			g.translate(-x, -y);
		}
	}
	
	/**
	 * 放大或者缩小显示
	 * @param g Canvas
	 * @param x int
	 * @param y int
	 */
	public void paint(Canvas g, int x, int y, Project aImgPak) {
		BaseAction[] list = actionList;
		if (list != null) {
			g.translate(x, y);
			int size = list.length;
			for (int i = 0; i < size; i++) { // 显示全部序列
				list[i].paintByPak(g, clipPosition[i][0], clipPosition[i][1], aImgPak);
			}
			g.translate(-x, -y);
		}
	}
	
	/**
	 * 水平镜像显示
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintMirror(Canvas g, int x, int y, Project aImgPak) {
		BaseAction[] list = actionList;
		if (list != null) {
			g.translate(x, y);
			int size = list.length;
			for (int i = 0; i < size; i++) { // 显示全部序列
				list[i].paintMirrorByPak(g, -clipPosition[i][0], clipPosition[i][1], aImgPak);
			}
			g.translate(-x, -y);
		}
	}
	
	/**
	 * 放大或者缩小显示
	 * @param g Canvas
	 * @param x int
	 * @param y int
	 */
	public void paintBmp(Canvas g, int x, int y, Bitmap aBmp) {
		BaseAction[] list = actionList;
		if (list != null) {
			g.translate(x, y);
			int size = list.length;
			if(size > 1) {
				size = 1;
			}
			for (int i = 0; i < size; i++) { // 显示全部序列
				ImageClip ic = (ImageClip)list[0];
				int tempX = clipPosition[i][0] + (ic.clipItem.getWidth() - (aBmp.getWidth()) >> 1);
				//int tempX = - (aBmp.getWidth() >> 1);
				list[i].paintByBmp(g, tempX, clipPosition[i][1], aBmp);
			}
			g.translate(-x, -y);
		}
	}	

	/**
	 * 水平镜像显示
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintMirror(Canvas g, int x, int y) {
		BaseAction[] list = actionList;
		if (list != null) {
			g.translate(x, y);
			int size = list.length;
			for (int i = 0; i < size; i++) { // 显示全部序列
				list[i].paintMirror(g, -clipPosition[i][0], clipPosition[i][1]);
			}
			g.translate(-x, -y);
		}
	}
	
//	/**
//	 * 获取实际宽度
//	 * @return
//	 */
//	public short getActWidth() {
//		return width;
//	}

//	/**
//	 * 获取实际高度
//	 * 
//	 * @return
//	 */
//	public short getActHeight() {
//		return height;
//	}

	/**
	 * 导入二进制结构
	 * @param dis DataInputStream
	 * @throws Exception
	 */
	public void inputStream(DataInputStream dis, int format) throws Exception {
		super.inputStream(dis, format);
		iShowPriority = dis.readByte();
		int count = dis.readShort();
		clipPosition = new short[count][2];
		initBaseActionSize(count);
//		int minx = 0;
//		int miny = 0;
//		int maxx = 0;
//		int maxy = 0;
		int i;
		for (i = 0; i < count; i++) {
			int d = dis.readInt();
			clipPosition[i][0] = (short)((d >>> 21) - 1024);// x
			clipPosition[i][1] = (short)(((d << 11) >>> 21) - 1024);// y
			clipPosition[i][0] = (short) (clipPosition[i][0]*ActivityUtil.PAK_ZOOM_X);
			clipPosition[i][1] = (short) (clipPosition[i][1]*ActivityUtil.PAK_ZOOM_Y);
			int index = ((d << 22) >>> 22);
//			System.out.println("******* ActionFrame inputStream clip index *****"+index);
			BaseAction baseAction = (BaseAction) Project.getLoadingProject()
					.getObject(index, Project.CLIP);
			setAction(baseAction, i);

			// 计算帧的尺寸
//			minx = Math.min(minx, clipPosition[i][0]);
//			miny = Math.min(miny, clipPosition[i][1]);
//			maxx = Math.max(maxx, clipPosition[i][0] + baseAction.getWidth());
//			maxy = Math.max(maxy, clipPosition[i][1] + baseAction.getHeight());
		}
//		setSize((short)(maxx - minx), (short)(maxy - miny));
		//各种矩形
	    int rectCount = dis.readByte(); 
	    if(rectCount > 0) {
	    	logicRect = new int[rectCount][5];
	    }
	    for(i=0;i<rectCount;++i)
	    {
	    	//对应2.7.1文件
//	    	int d = dis.readInt(); 	
//	    	int x = (d >>> 24) - 128;
//	        int y = ( (d << 8) >>> 24) - 128;
//	        int width = ( (d << 16) >>> 25);
//	    	int height = ( (d << 23) >>> 25);
//	        int drawRectStyle = d & 0x03;    	
	    	//对应2.8.1以上文件
	    	int x = dis.readInt();
	        int y = dis.readInt();
	        int width = dis.readInt();
	    	int height = dis.readInt();
	        int drawRectStyle = dis.readInt();
	        
	        float tZoomX = ActivityUtil.PAK_ZOOM_X;
			float tZoomY = ActivityUtil.PAK_ZOOM_Y;
			x = (int) (x*tZoomX);
			y = (int) (y*tZoomY);
			width = (int) (width*tZoomX);
			height = (int) (height*tZoomY);
	        
	    	logicRect[i][0] = x;
	    	logicRect[i][1] = y;
	    	logicRect[i][2] = width;
	    	logicRect[i][3] = height;
	    	logicRect[i][4] = drawRectStyle;
	    } 
	}

	public short[][] getClipPosition() {
		return clipPosition;
	}

	public void setClipPosition(short[][] clipPosition) {
		this.clipPosition = clipPosition;
	}
	
	/**
	 * 返回一个动作最小的母图id
	 * @return
	 */
	public int getMinClipID(){
		ImageClip ti = null;
		short id = 0;
		int j = 0;
		if(this.actionList!=null&&actionList.length>0){
			for(int i=0;i<actionList.length;i++){
				ti = (ImageClip)actionList[i];
				if(i==0){
					id = ti.getImageID();
				}
				if(id>ti.getImageID()){
					id = ti.getImageID();
					j = i;
				}
			}
		}
		if(ti!=null){
			ti=null;
		}
		return id;
	}

//	public void paintByPak(Canvas g, int x, int y, Project imgPak) {
//		BaseAction[] list = actionList;
//		if (list != null) {
//			g.translate(x, y);
//			int size = list.length;
//			for (int i = 0; i < size; i++) { // 显示全部序列
//				list[i].paintByPak(g, clipPosition[i][0], clipPosition[i][1],imgPak);
//			}
//			g.translate(-x, -y);
//		}
//	}
//
//	public void paintMirrorByPak(Canvas g, int x, int y, Project imgPak) {
//		BaseAction[] list = actionList;
//		if (list != null) {
//			g.translate(x, y);
//			int size = list.length;
//			for (int i = 0; i < size; i++) { // 显示全部序列
//				list[i].paintMirror(g, -clipPosition[i][0], clipPosition[i][1]);
//			}
//			g.translate(-x, -y);
//		}
//	}
	
	public int[][] getLogicRect() {
		return logicRect;
	}
	
	public int getRectCount() {
		int pRectCount = 0;
		if (logicRect != null) {
			pRectCount = logicRect.length;
		}
		return pRectCount;
	}

}
