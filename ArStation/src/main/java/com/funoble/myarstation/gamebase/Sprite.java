package com.funoble.myarstation.gamebase;


import java.io.DataInputStream;

import com.funoble.myarstation.common.Tools;

import android.graphics.Bitmap;
import android.graphics.Canvas;


/**
 * Title: 精灵：人物,NPC等 Description: 具有不同的状态，每一状态具有不同的帧，每一帧具有不同的子图
 * <p>
 * Copyright: 腾讯科技(深圳)有限公司.
 * </p>
 * 
 * @author cheerluo
 */
public class Sprite extends BaseAction {

	public final static int	 	ANCHOR_NONE = 0;
	public final static int		ANCHOR_FLIP_HORIZONTAL = 2;//水平翻转(镜像)
	public final static int 	ANCHOR_FLIP_VERTICAL = 1;//垂直翻转(镜像180)
	public final static int 	ANCHOR_ROTATE_90 = 6;//逆时针旋转90度
	public final static int		ANCHOR_ROTATE_180 = 3;//逆时针旋转180度
	public final static int		ANCHOR_ROTATE_270  = 5;//逆时针旋转270度
	public final static int 	ANCHOR_TRANS_MIRROR_ROT90 = 7;//(镜像90度)
	public final static int		ANCHOR_TRANS_MIRROR_ROT270 = 4;//(镜像270度)
	// 帧ID
	private int iFrameID = 0;
	// 帧停留时间
	private byte iFrameDelay = 0;
	// 所属身体的部件,与游戏逻辑有关，暂时没有想到更好的办法
	public byte iPart = 0;
	private short spriteLeft = 0;
	private short spriteTop = 0;
	private short spriteWidth = 0;
	private short spriteHeight = 0;
	private int headImageID = 0;
	private short firstFrameWidth = 0; //第一幀的宽
	private short firstFrameHeight = 0;//第一幀的高
	/**
	 * 拷贝精灵,复制所有动作
	 * 
	 * @param b
	 *            BaseAction
	 */
	public void copyFrom(BaseAction c) {
		if(c.equals(this)){
			return;
		}
		super.copyFrom(c);
		// 深层拷贝动作
//		try {
			if (c != null && c instanceof Sprite) {
				Sprite cc = (Sprite) c;
				initBaseActionSize(cc.actionList.length);
				for (int i = 0; i < cc.actionList.length; i++) {
					SpriteAction sa = (SpriteAction) cc.actionList[i];
					SpriteAction newsa = new SpriteAction();
					newsa.copyFrom(sa);
					newsa.timeDelay = sa.timeDelay;
					setAction(newsa, i);
				}
			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Sprite cc = (Sprite)c;
		iPart = cc.iPart;
		spriteLeft = cc.spriteLeft;
		spriteTop = cc.spriteTop;
		spriteWidth = cc.spriteWidth;
		spriteHeight = cc.spriteHeight;
		headImageID = cc.headImageID;
		firstFrameWidth = cc.firstFrameWidth; //第一幀的宽
		firstFrameHeight = cc.firstFrameHeight;//第一幀的高
	}

	/**
	 * 导入二进制结构
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws Exception
	 */
	public void inputStream(DataInputStream dis, int format) throws Exception {
		super.inputStream(dis, format);
		int count = dis.readShort();
		initBaseActionSize(count);
		SpriteAction sa = null;
		for (int i = 0; i < count; i++) {
			short index = dis.readShort();
			sa = (SpriteAction) Project.getLoadingProject().getObject(
					index, Project.ACTION);
			setAction(sa, i);
			if (sa != null) {
				setSize(sa.actionList);
			}
		}
		setHeadImageID();
		// setSize(getActWidth(), getActHeight());
	}

	/**
	 * 设置精灵的大小
	 * 
	 * @param frameList
	 */
	private void setSize(Object[] frameList) {
		Object[] clipList = null;
		short[][] clipPosition = null;
		int[] x = null;
		int[] y = null;
		if (frameList == null) {
			return;
		}
		if (frameList.length > 0) {
			for (int i = 0; i < frameList.length; i++) {
				clipList = ((ActionFrame) frameList[i]).actionList;// 一个幁的切片列表
				clipPosition = ((ActionFrame) frameList[i]).getClipPosition();// 切片坐标
				int maxX = 0;
				int minX = 0;
				int maxY = 0;
				int minY = 0;
				if (clipPosition == null || clipPosition.length == 0
						|| clipList == null || clipList.length == 0) {
					return;
				}
				ImageClip tempClip = new ImageClip();
				x = new int[clipList.length << 1];// x坐标
				y = new int[clipList.length << 1];// y坐标
				for (int j = 0; j < clipPosition.length; j++) {
					tempClip = (ImageClip) clipList[j];
//					if(j == 0) {
//						firstFrameWidth = (short)tempClip.getWidth();
//						firstFrameHeight = (short)tempClip.getHeight();
//						System.out.println("firstFrameWidth = " + firstFrameWidth);
//						System.out.println("firstFrameHeight = " + firstFrameHeight);
//					}
					x[j * 2] = clipPosition[j][0];
					y[j * 2] = clipPosition[j][1];
					x[j * 2 + 1] = clipPosition[j][0] + tempClip.getWidth();
					y[j * 2 + 1] = clipPosition[j][1] + tempClip.getHeight();
				}
				for (int j = 0; j < x.length; j++) {
					if (maxX < x[j]) {
						maxX = x[j];
					}
					if (minX > x[j]) {
						minX = x[j];
					}
					if (maxY < y[j]) {
						maxY = y[j];
					}
					if (minY > y[j]) {
						minY = y[j];
					}
				}
				this.spriteTop = (short)minY;
				this.spriteLeft = (short)minX;
				if (this.spriteHeight < (short) (maxY - minY)) {
					this.spriteHeight = (short) (maxY - minY);
				}
				if (this.spriteWidth < (short) (maxX - minX)) {
					this.spriteWidth = (short) (maxX - minX);
				}
			}

		}
		if (clipList != null) {
			clipList = null;
		}
		if (clipPosition != null) {
			clipPosition = null;
		}
		if (x != null) {
			x = null;
		}
		if (y != null) {
			y = null;
		}
	}

	/**
	 * 获取头像母图id
	 * 
	 * @return
	 */
	public void setHeadImageID() {
		BaseAction[] tempArray = null;
		SpriteAction sa = null;
		ActionFrame af = null;
		int id = 0;//所有动作，所有幁中最小母图id
		tempArray = this.actionList;//动作列表
		if(tempArray!=null&&tempArray.length>0){
			for(int i=0;i<tempArray.length;i++){
				sa = (SpriteAction)tempArray[i];
				if(sa!=null){
					if(sa.actionList!=null&&sa.actionList.length>0){
						af = (ActionFrame)sa.actionList[0];//幁
						if(i==0){
							id = af.getMinClipID();
						}
//						System.out.println("********  min id *****"+af.getMinClipID());
						if(id>=af.getMinClipID()){
							id = af.getMinClipID();
						}
					}
				}
			}
		}
		if(tempArray!=null){
			tempArray=null;
		}
		if(sa!=null){
			sa=null;
		}
		if(af!=null){
			af=null;
		}
		this.headImageID = id;
	}
	
	//执行动作，如果动作执行完成返回 true，否则返回 false
	public boolean performAction(int aActionID) {
		if (aActionID < 0 || aActionID >= actionList.length) {
			return true;
		}
		SpriteAction tempAction = (SpriteAction) this.actionList[aActionID];
		if (iFrameID >= tempAction.actionList.length) {
			iFrameID = tempAction.actionList.length - 1;
			return true;
		}
		iFrameDelay++;
		if (iFrameDelay > tempAction.timeDelay[iFrameID]) {
			iFrameDelay = 0;
			iFrameID++;
			if (iFrameID >= tempAction.actionList.length) {
				iFrameID = tempAction.actionList.length - 1;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 显示
	 * 
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintStatic(Canvas g, int aActionID, int x, int y) {
		if (aActionID < 0 || aActionID >= actionList.length) {
			return;
		}
		SpriteAction tempAction = (SpriteAction) this.actionList[aActionID];
		if (iFrameID < 0 || iFrameID >= tempAction.actionList.length) {
			return;
		}
		ActionFrame tempFrame = (ActionFrame) tempAction.actionList[iFrameID];
		tempFrame.paint(g, x, y);
	}
	
	/**
	 * 显示
	 * 
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paint(Canvas g, int x, int y) {
		if (actionList.length <= 0) {
			return;
		}
		SpriteAction tempAction = (SpriteAction) this.actionList[0];
		if (iFrameID >= tempAction.actionList.length) {
			iFrameID = 0;
		}
		iFrameDelay++;
		if (iFrameDelay > tempAction.timeDelay[iFrameID]) {
			iFrameDelay = 0;
			iFrameID++;
			if (iFrameID >= tempAction.actionList.length) {
				iFrameID = 0;
			}
		}
		if (iFrameID < tempAction.actionList.length) {
			ActionFrame tempFrame = (ActionFrame) tempAction.actionList[iFrameID];
			tempFrame.paint(g, x, y);
		}
	}

	/**
	 * 画指定Index的动画
	 * 
	 * @param g
	 * @param aActionIndex 指定的动画Index
	 * @param x
	 * @param y
	 */
	public void paintAction(Canvas g, int aActionIndex, int x, int y) {
		if(actionList==null){
			return;
		}
		if (actionList.length <= 0 || aActionIndex >= actionList.length) {
			return;
		}
		SpriteAction tempAction = (SpriteAction) this.actionList[aActionIndex];
		if(tempAction==null){
			return;
		}
		if(tempAction.actionList==null){
			return;
		}
		if (iFrameID >= tempAction.actionList.length) {
			iFrameID = 0;
		}
		iFrameDelay++;
		if (iFrameDelay > tempAction.timeDelay[iFrameID]) {
			iFrameDelay = 0;
			iFrameID++;
			if (iFrameID >= tempAction.actionList.length) {
				iFrameID = 0;
			}
		}
		if (iFrameID < tempAction.actionList.length) {
			ActionFrame tempFrame = (ActionFrame) tempAction.actionList[iFrameID];
			if(tempFrame==null){
				Tools.println("ActionFrame == null");
				return;
			}
			tempFrame.paint(g, x, y);
		}
	}

	/**
	 * 显示
	 * 
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintAction(Canvas g, int x, int y, int aActionIndex,
			int aFrameIndex) {
		if (aActionIndex >= 0 && aActionIndex < this.actionList.length) {
			SpriteAction tempAction = (SpriteAction) this.actionList[aActionIndex];
			if (tempAction.actionList != null && aFrameIndex >= 0 && aFrameIndex < tempAction.actionList.length) {
				ActionFrame tempFrame = (ActionFrame) tempAction.actionList[aFrameIndex];
				if (tempFrame != null) {
					tempFrame.paint(g, x, y);
				}
			}
		}
	}

	/**
	 * 水平镜像显示动作
	 * 
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintActionMirror(Canvas g, int x, int y, int aActionIndex,
			int aFrameIndex) {
		if (aActionIndex >= 0 && aActionIndex < this.actionList.length) {
			SpriteAction tempAction = (SpriteAction) this.actionList[aActionIndex];
			if (tempAction.actionList != null && aFrameIndex >= 0 && aFrameIndex < tempAction.actionList.length) {
				ActionFrame tempFrame = (ActionFrame) tempAction.actionList[aFrameIndex];
				tempFrame.paintMirror(g, x, y);
			}
		}
	}

	/**
	 * 显示
	 * 
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintAction(Canvas g, int x, int y, int aActionIndex,
			int aFrameIndex, Project aImgPak) {
		if (aActionIndex >= 0 && aActionIndex < this.actionList.length) {
			SpriteAction tempAction = (SpriteAction) this.actionList[aActionIndex];
			if (aFrameIndex >= 0 && aFrameIndex < tempAction.actionList.length) {
				ActionFrame tempFrame = (ActionFrame) tempAction.actionList[aFrameIndex];
				tempFrame.paint(g, x, y, aImgPak);
			}
		}
	}

	/**
	 * 水平镜像显示动作
	 * 
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintActionMirror(Canvas g, int x, int y, int aActionIndex,
			int aFrameIndex, Project aImgPak) {
		if (aActionIndex >= 0 && aActionIndex < this.actionList.length) {
			SpriteAction tempAction = (SpriteAction) this.actionList[aActionIndex];
			if (aFrameIndex >= 0 && aFrameIndex < tempAction.actionList.length) {
				ActionFrame tempFrame = (ActionFrame) tempAction.actionList[aFrameIndex];
				tempFrame.paintMirror(g, x, y, aImgPak);
			}
		}
	}
	
	/**
	 * 显示
	 * 
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintActionBmp(Canvas g, int x, int y, int aActionIndex, Bitmap aBmp) {
//		if(actionList==null){
//			return;
//		}
//		if (actionList.length <= 0) {
//			return;
//		}
//		SpriteAction tempAction = (SpriteAction) this.actionList[aActionIndex];
//		if(tempAction==null){
//			return;
//		}
//		if(tempAction.actionList==null){
//			return;
//		}
//		if (iFrameID >= tempAction.actionList.length) {
//			iFrameID = 0;
//		}
//		iFrameDelay++;
//		if (iFrameDelay > tempAction.timeDelay[iFrameID]) {
//			iFrameDelay = 0;
//			iFrameID++;
//			if (iFrameID >= tempAction.actionList.length) {
//				iFrameID = 0;
//			}
//		}
//		if (iFrameID < tempAction.actionList.length) {
//			ActionFrame tempFrame = (ActionFrame) tempAction.actionList[iFrameID];
//			if(tempFrame==null){
//				Tools.println("ActionFrame == null");
//				return;
//			}
//			tempFrame.paintBmp(g, x, y, aBmp);
//		}
		if (aActionIndex < 0 || aActionIndex >= actionList.length) {
			return;
		}
		SpriteAction tempAction = (SpriteAction) this.actionList[aActionIndex];
		if (iFrameID < 0 || iFrameID >= tempAction.actionList.length) {
			return;
		}
		ActionFrame tempFrame = (ActionFrame) tempAction.actionList[iFrameID];
		tempFrame.paintBmp(g, x, y, aBmp);
	}

	// 获取指定动作的帧
	public ActionFrame getFrame(int aActionIndex, int aFrameIndex) {
		ActionFrame tempFrame = null;
		if (aActionIndex >= 0 && aActionIndex < this.actionList.length) {
			SpriteAction tempAction = (SpriteAction) this.actionList[aActionIndex];
			if (aFrameIndex >= 0 && aFrameIndex < tempAction.actionList.length) {
				tempFrame = (ActionFrame) tempAction.actionList[aFrameIndex];
			}
		}
		return tempFrame;
	}

	public short getFirstFrameWidth() {
		if(firstFrameWidth > 0) {
			return firstFrameWidth;
		}
		if(actionList.length <= 0) {
			return 0;
		}
		SpriteAction sa = (SpriteAction)actionList[0];
		Object[] frameList = sa.actionList;
		if(frameList.length <= 0) {
			return 0;
		}
		ActionFrame frame = (ActionFrame)frameList[0];
		if(frame.actionList.length <= 0) {
			return 0;
		}
		ImageClip tempClip = (ImageClip)frame.actionList[0];
		firstFrameWidth = (short)tempClip.getWidth();
		return firstFrameWidth;
	}

	public short getFirstFrameHeight() {
		if(firstFrameHeight > 0) {
			return firstFrameHeight;
		}
		if(actionList.length <= 0) {
			return 0;
		}
		SpriteAction sa = (SpriteAction)actionList[0];
		Object[] frameList = sa.actionList;
		if(frameList.length <= 0) {
			return 0;
		}
		ActionFrame frame = (ActionFrame)frameList[0];
		if(frame.actionList.length <= 0) {
			return 0;
		}
		ImageClip tempClip = (ImageClip)frame.actionList[0];
		firstFrameHeight = (short)tempClip.getHeight();
		return firstFrameHeight;
	}
	
	public short getSpriteWidth() {
		return spriteWidth;
	}

	public void setSpriteWidth(short spriteWidth) {
		this.spriteWidth = spriteWidth;
	}

	public short getSpriteLeft() {
		return spriteLeft;
	}
	
	public short getSpriteTop() {
		return spriteTop;
	}
	
	public short getSpriteHeight() {
		return spriteHeight;
	}

	public void setSpriteHeight(short spriteHeight) {
		this.spriteHeight = spriteHeight;
	}

	public int getHeadImageID() {
		return headImageID;
	}

	public void resetFrameID() {
		iFrameID = 0;
	}
	
	public int getFrameID() {
		return iFrameID;
	}
}
