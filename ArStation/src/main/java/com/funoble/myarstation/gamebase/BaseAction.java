package com.funoble.myarstation.gamebase;


import java.io.DataInputStream;

import android.graphics.Bitmap;
import android.graphics.Canvas;


/**
 * 
 * Title: 动画基本单元
 * Description: 动画单元操作：数据属性、显示属性、及保存到流中或者从流中读取动画单元
 * <p>Copyright: 腾讯科技(深圳)有限公司.</p>
 * @author cheerluo
 */
public class BaseAction {

	// ////////动画单元的集合属性//////////
	// 动画单元集合序列,默认为null
	public BaseAction[] actionList;
	//显示时自动播放到下一帧
//	public boolean autoNextFrm = false;
	//循环播放
//	public boolean circlePlay = true;
	// 集合序列中当前显示序号
//	private byte index = 0;

	// 位置及尺寸
	protected short x;
	protected short y;
//	protected short width;
//	protected short height;
	protected int iIndex;//该单元在ResProject中的Index
	/**
	 * 释放自己
	 */
	public void releaseSelf() {
		if(actionList != null) {
			for(int i=0; i<actionList.length; i++) {
				if(actionList[i] != null) {
					actionList[i].releaseSelf();
					actionList[i] = null;
				}
			}
			actionList = null;
		}
		//debug
//		System.out.println("BaseAction释放！");
	}
	
	/**
	 * 构造
	 */
	public BaseAction() {
	}

	/**
	 * 拷贝动画单元 子类补充拷贝特有数据，即自动实现了对象拷贝
	 * @param b BaseAction
	 */
	public void copyFrom(BaseAction b) {
		if (b != null) {
			actionList = b.actionList;
//			index = b.index;
			x = b.x;
			y = b.y;
//			width = b.width;
//			height = b.height;
			iIndex = b.iIndex;
		}
	}

	/**
	 * 设置坐标
	 * @param x int
	 * @param y int
	 */
	public void setPosition(short x, short y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 获取坐标X
	 * @return int
	 */
	public short getX() {
		return x;
	}

	/**
	 * 获取坐标Y
	 * @return int
	 */
	public short getY() {
		return y;
	}

	/**
	 * 设置尺寸
	 * @param w int
	 * @param h int
	 */
//	public void setSize(short w, short h) {
//		width = w;
//		height = h;
//	}

	/**
	 * 获取宽度
	 * @return int
	 */
//	public int getWidth() {
//		return width;
//	}

	/**
	 * 获取高度
	 * @return int
	 */
//	public int getHeight() {
//		return height;
//	}

	/**
	 * 获取实际宽度
	 * @return
	 */
//	public short getActWidth() {
//		if (actionList != null) {
//			System.out.println("BaseAction getActWidth index = " + index);
//			return actionList[index].getActWidth();
//		} else {
//			return width;
//		}
//	}

	/**
	 * 获取实际高度
	 * @return
	 */
//	public short getActHeight() {
//		if (actionList != null) {
//			return actionList[index].getActHeight();
//		} else {
//			return height;
//		}
//	}

//	/**
//	 * 获取当前动画单元序号
//	 * @return
//	 */
//	public final int getCurrentActionIndex() {
//		return index;
//	}

//	/**
//	 * 显示下一动画单元
//	 */
//	public final void nextAction() {
//		if (actionList == null) {
//			return;
//		}
//		if (index + 1 < actionList.length) {
//			index++;
//		} 
//		else {//if(circlePlay) {//循环播放
//			index = 0;
//		}
//	}
	
	
//	/**
//	 * 设置动画单元序号
//	 * @param i
//	 */
//	public void setFrame(byte i){
//		index=i;
//	}

	/**
	 * 初始化元素尺寸
	 * @param n
	 */
	public void initBaseActionSize(int n) {
		actionList = new BaseAction[n];
	}

	/**
	 * 指定位置插入动画单元
	 * 
	 * @param frame Object
	 * @param i int
	 */
	public final void setAction(BaseAction d, int i) {
		actionList[i] = d;
	}

	/**
	 * 获取指定位置动作单元
	 * @param i  int
	 * @return BaseAction
	 */
	public final BaseAction getAction(int i) {
		if (actionList == null)
			return null;
		return actionList[i];
	}

	/**
	 * 显示
	 * @param g Canvas
	 */
	public final void draw(Canvas g) {
		paint(g, x, y);
	}

	/**
	 * 显示
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paint(Canvas g, int x, int y) {
//		BaseAction[] list = this.actionList;
//		if (list != null && list.length > 0) {
//			list[0].paint(g, x, y);
//		}
	}
	
	/**
	 * 显示
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintByPak(Canvas g, int x, int y, Project aImgPak) {
//		BaseAction[] list = this.actionList;
//		if (list != null && list.length > 0) {
//			list[0].paint(g, x, y);
//		}
	}
	
	/**
	 * 显示
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintByBmp(Canvas g, int x, int y, Bitmap aBmp) {
//		BaseAction[] list = this.actionList;
//		if (list != null && list.length > 0) {
//			list[0].paint(g, x, y);
//		}
	}
	
	/**
	 * 水平镜像显示
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintMirror(Canvas g, int x, int y) {
//		BaseAction[] list = this.actionList;
//		if (list != null && list.length > 0) {
//			list[0].paintMirror(g, x, y);
//		}
	}

	/**
	 * 水平镜像显示
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintMirrorByPak(Canvas g, int x, int y, Project aImgPak) {
//		BaseAction[] list = this.actionList;
//		if (list != null && list.length > 0) {
//			list[0].paintMirror(g, x, y);
//		}
	}
	
	/**
	 * 从二进制数据中导入本对象
	 * @param dis DataInputStream
	 * @throws Exception
	 */
	public void inputStream(DataInputStream dis, int format) throws Exception {
	}
	
	public int getIndex() {
		return iIndex;
	}
	
	public void setIndex(int aIndex) {
		iIndex = aIndex;
	}
}
