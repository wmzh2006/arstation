package com.funoble.myarstation.gamebase;


import java.io.*;

import com.funoble.myarstation.utils.ActivityUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;



/**
 * Title: 图片切割单元，称为子图
 * Description: 在指定图片中切割指定区域作为图片单元
 * <p>Copyright: 腾讯科技(深圳)有限公司.</p>
 * @author cheerluo
 */
public class ImageClip extends BaseAction {

	// 切割后的图形
	ImageItem clipItem;
	short iImageID = 0;
//	private byte alpha;// alpha通道 0-100
//	byte width;
//	byte height;

	/**
	 * 释放自己
	 */
	public void releaseSelf() {
		if(clipItem != null) {
			clipItem.releaseSelf();
			clipItem = null;
		}
		super.releaseSelf();
		//debug
//		System.out.println("ImageClip释放!");
	}
	
	/**
	 * 拷贝动画单元 子类补充拷贝特有数据，即自动实现了对象拷贝
	 * @param b BaseAction
	 */
	public void copyFrom(BaseAction b) {
		super.copyFrom(b);
		if (b != null && b instanceof ImageClip) {
			ImageClip cc = (ImageClip) b;
			clipItem = cc.clipItem;
//			alpha = cc.alpha;
		}
	}
	
	/**
	 * 根据矩阵切割子图
	 * @param rect Rectangle
	 */
	public void setClipRect(ResFile motherImage, int transform, int clipRectx,
			int clipRecty, int clipRectwidth, int clipRectheight) {
//		width = clipRectwidth;
//		height = clipRectheight;
		if (motherImage != null) {
			clipItem = new ImageItem();
			clipItem.initImage(motherImage, clipRectx, clipRecty,
					clipRectwidth, clipRectheight, 1);
//			if(transform == ImageItem.TRANS_MIRROR_ROT90) {
//				transform = ImageItem.TRANS_MIRROR_ROT270;
//			}
//			else if(transform == ImageItem.TRANS_MIRROR_ROT270) {
//				transform = ImageItem.TRANS_MIRROR_ROT90;
//			}
			clipItem.setTransform(transform);
		}
	}

	/**
	 * 再指定点显示本图片单元
	 * @param g  Canvas
	 * @param x int
	 * @param y int
	 */
	public void paint(Canvas g, int x, int y) {
		ImageItem bf = clipItem;
		if (bf == null) {
			return;
		}
		bf.setPosition((short)x, (short)y);
		bf.draw(g);
	}
	public void paint(Canvas g,int x,int y,int width,int height){
		ImageItem bf = clipItem;
		if (bf == null) {
			return;
		}
		bf.setPosition((short)x, (short)y);
		bf.draw(g,width,height);
	}
	public int getWidth(){
		return clipItem.getWidth();
	}
	public int getHeight(){
		return clipItem.getHeight();
	}
	public short getImageID(){
		return this.iImageID;
	}
	
	/**
	 * 水平镜像显示
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintMirror(Canvas g, int x, int y) {
		ImageItem bf = clipItem;
		if (bf == null) {
			return;
		}
		bf.setPosition((short)(x-bf.getWidth()+1), (short)y);
		
		// 图形转换：翻转与旋转
		int anchor = bf.getTranFormAnchor();
		int oldAnchor = anchor;
		switch (anchor) {
		case Sprite.ANCHOR_NONE: { //没有效果
			anchor = ImageItem.FLIP_HORIZONTAL;
			break;
		}
		case ImageItem.FLIP_HORIZONTAL: { // 水平翻转
			anchor = Sprite.ANCHOR_NONE;
			break;
		}
		case ImageItem.FLIP_VERTICAL: { // 垂直翻转
			anchor = ImageItem.ROTATE_180;
			break;
		}
		case ImageItem.ROTATE_90: { // 逆时针旋转90
			anchor = ImageItem.TRANS_MIRROR_ROT90;
			break;
		}
		case ImageItem.ROTATE_180: { // 逆时针旋转180
			anchor = ImageItem.FLIP_VERTICAL;
			break;
		}
		case ImageItem.ROTATE_270: { // 逆时针旋转270
			anchor = ImageItem.TRANS_MIRROR_ROT270;
			break;
		}
		case ImageItem.TRANS_MIRROR_ROT90: { // 镜像逆时针旋转90
			anchor = ImageItem.ROTATE_90;
			break;
		}
		case ImageItem.TRANS_MIRROR_ROT270: { // 镜像逆时针旋转270
			anchor = ImageItem.ROTATE_270;
			break;
		}
		}
		bf.setTranFormAnchor((byte)anchor);
		bf.draw(g);
		bf.setTranFormAnchor((byte)oldAnchor);
	}

	/**
	 * 再指定点显示本图片单元
	 * @param g  Canvas
	 * @param x int
	 * @param y int
	 */
	public void paintByPak(Canvas g, int x, int y, Project aImgPak) {
		ImageItem bf = clipItem;
		if (bf == null) {
			return;
		}
		if (aImgPak == null) {
			return;
		}
		bf.setPosition((short)x, (short)y);
		ResFile oldImage = bf.getImageFile();
		ResFile image = (ResFile) aImgPak.getObject(iImageID, Project.FILE);
		bf.setImage(image);
		bf.draw(g);
		bf.setImage(oldImage);
	}
	
	/**
	 * 水平镜像显示
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintMirrorByPak(Canvas g, int x, int y, Project aImgPak) {
		ImageItem bf = clipItem;
		if (bf == null) {
			return;
		}
		if (aImgPak == null) {
			return;
		}
		bf.setPosition((short)(x-bf.getWidth()+1), (short)y);
		
		// 图形转换：翻转与旋转
		int anchor = bf.getTranFormAnchor();
		int oldAnchor = anchor;
		switch (anchor) {
		case Sprite.ANCHOR_NONE: { //没有效果
			anchor = ImageItem.FLIP_HORIZONTAL;
			break;
		}
		case ImageItem.FLIP_HORIZONTAL: { // 水平翻转
			anchor = Sprite.ANCHOR_NONE;
			break;
		}
		case ImageItem.FLIP_VERTICAL: { // 垂直翻转
			anchor = ImageItem.ROTATE_180;
			break;
		}
		case ImageItem.ROTATE_90: { // 逆时针旋转90
			anchor = ImageItem.TRANS_MIRROR_ROT90;
			break;
		}
		case ImageItem.ROTATE_180: { // 逆时针旋转180
			anchor = ImageItem.FLIP_VERTICAL;
			break;
		}
		case ImageItem.ROTATE_270: { // 逆时针旋转270
			anchor = ImageItem.TRANS_MIRROR_ROT270;
			break;
		}
		case ImageItem.TRANS_MIRROR_ROT90: { // 镜像逆时针旋转90
			anchor = ImageItem.ROTATE_90;
			break;
		}
		case ImageItem.TRANS_MIRROR_ROT270: { // 镜像逆时针旋转270
			anchor = ImageItem.ROTATE_270;
			break;
		}
		}
		ResFile oldImgFile = bf.getImageFile();
		ResFile newImgFile = (ResFile) aImgPak.getObject(iImageID, Project.FILE);
		bf.setImage(newImgFile);
		bf.setTranFormAnchor((byte)anchor);
		bf.draw(g);
		bf.setTranFormAnchor((byte)oldAnchor);
		bf.setImage(oldImgFile);
	}
	
	/**
	 * 再指定点显示本图片单元
	 * @param g  Canvas
	 * @param x int
	 * @param y int
	 */
	public void paintByBmp(Canvas g, int x, int y, Bitmap aBmp) {
		ImageItem bf = clipItem;
		if (bf == null) {
			return;
		}
		if (aBmp == null) {
			return;
		}
		bf.setPosition((short)x, (short)y);
		bf.drawBmp(g, aBmp);
	}
	
	
	/**
	 * 导入二进制数据构造
	 * @param dis DataInputStream
	 */
	public void inputStream(DataInputStream dis, int format) throws Exception {
		super.inputStream(dis, format);
		int[] data = (int[]) Project.readArray(dis, 2, Project.TYPE_INT);
		if (data != null && data.length == 2
				&& (data[0] != -1 || data[1] != -1)) {

			iImageID = (short)((data[0] >> 24) & 0xff);// 8位
			int clipRectx = (data[0] >> 12) & 0xfff;// 12位
			int clipRecty = (data[0]) & 0xfff;// 12位

			int alphaPercent = (data[1] >> 25) & 0x7f;// 7位
			int alpha = alphaPercent * 255 / 100;// 由百分比转变为0-256的绝对数值

			int transform = (data[1] >> 22) & 0x7;// 3位
			int clipRectwidth = (data[1] >> 11) & 0x7ff;// 11位
			int clipRectheight = (data[1]) & 0x7ff;// 11位
			ResFile image = (ResFile) Project.getLoadingProject().getObject(
					iImageID, Project.FILE);
			float tZoomX = ActivityUtil.PAK_ZOOM_X;
			float tZoomY = ActivityUtil.PAK_ZOOM_Y;
			clipRectx = (int) (clipRectx*tZoomX);
			clipRecty = (int) (clipRecty*tZoomY);
			clipRectwidth = (int) (clipRectwidth*tZoomX);
			clipRectheight = (int) (clipRectheight*tZoomY);
			setClipRect(image, transform, clipRectx, clipRecty, clipRectwidth,
					clipRectheight);
		}
		data = null;
	}

}
