package com.funoble.myarstation.gamebase;

import com.funoble.myarstation.utils.ActivityUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;


/**
 * Title: 图形组件
 * Description: 可以实现显示指定帧及动画
 * <p>Copyright: 腾讯科技(深圳)有限公司.</p>
 * @author cheerluo
 */
public class ImageItem {

	// 组件位置及尺寸
	protected short x;
	protected short y;
//	protected byte width;
//	protected byte height;

	// ////显示属性：基本类型数据定义//////
	private int image_Window_sx; // 图片有效区域的x起始位置
	private int image_Window_sy; // 图片有效区域的y起始位置
	private int image_Window_sw; // 图片有效区域的的宽度
	private int image_Window_sh; // 图片有效区域的的高度
//	private short frame_Index; // 精灵当前所在的帧号
//	private short snPt; // 精灵当前帧序列的指针
//	private short frame_Count; // 精灵的帧总数,1为默认帧数
//	private short frame_Count_H; // 精灵的在水平方向上的帧数
//	private short frame_Count_V; // 精灵的在垂直方向上的帧数
	private int tranFormAnchor; // 当前帧的旋转和翻转
	// ////显示属性：对象类型数据定义//////
	private ResFile image_Obj; // 精灵图片，可能图片上有多个精灵，指定图片上有效区域为本精灵所使用
//	private int[] frameList_Obj; // 精灵帧序列

	// ///图形转换定义,可以实现翻转和旋转显示精灵/////

	public static final int FLIP_HORIZONTAL = Sprite.ANCHOR_FLIP_HORIZONTAL;// 2
	// //水平翻转(镜像)
	public static final int FLIP_VERTICAL = Sprite.ANCHOR_FLIP_VERTICAL;// 1
	// //垂直翻转(镜像180)
	public static final int ROTATE_180 = Sprite.ANCHOR_ROTATE_180;// 3 //逆时针旋转180度
	public static final int ROTATE_270 = Sprite.ANCHOR_ROTATE_270;// 5 //逆时针旋转270度
	public static final int ROTATE_90 = Sprite.ANCHOR_ROTATE_90;// 6 //逆时针旋转90度
	public static final int TRANS_MIRROR_ROT90 = Sprite.ANCHOR_TRANS_MIRROR_ROT90;// 7;//(镜像90度)
	public static final int TRANS_MIRROR_ROT270 = Sprite.ANCHOR_TRANS_MIRROR_ROT270;// 4;//(镜像270度)


	/**
	 * 释放自己
	 */
	public void releaseSelf() {
		image_Obj = null;
//		frameList_Obj = null;
		//debug
//		System.out.println("ImageItem释放!");
	}
	
	/**
	 * 构造
	 */
	public ImageItem() {
	}
	
	/**
	 * 初始化构造
	 * @param image
	 */
	public ImageItem(ResFile image) {
		initImage(image, 1);
	}

	/**
	 * 设置坐标
	 * @param x
	 * @param y
	 */
	public void setPosition(short x, short y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 设置图像资源
	 */
	public void setImage(ResFile aImage) {
		image_Obj = aImage;
	}

	/**
	 * 组件显示(draw)：在所定位置进行组件绘制(paint)
	 */
	public final void draw(Canvas g) {
		int tx = x, ty = y;
		g.translate(tx, ty);
		// 基本界面绘制
		paint(g);// 绘制组件
		g.translate(-tx, -ty);
	}
	public final void draw(Canvas g,int width,int height) {
		int tx = x, ty = y;
		g.translate(tx, ty);
		// 基本界面绘制
		paint(g,width,height);// 绘制组件
		g.translate(-tx, -ty);
	}
	
	public final void drawBmp(Canvas g, Bitmap aBmp) {
		int tx = x, ty = y;
		g.translate(tx, ty);
		// 基本界面绘制
		paintBmp(g, aBmp);// 绘制组件
		g.translate(-tx, -ty);
	}
	
	public void paintBmp(Canvas g, Bitmap aBmp){
		if(aBmp == null) {
			return;
		}
		// 计算出要显示的帧在图片中的位置
//		int draw_org_X = 0;
//		int draw_org_Y = 0;
		int clip_Width = aBmp.getWidth();
		int clip_Height = aBmp.getHeight();
		// 图形转换：翻转与旋转
		g.save();    
		g.clipRect(0, 0, clip_Width, clip_Height);
		if (aBmp != null) {
//			System.out.println("draw_Width = " + draw_Width + "   draw_Height = " + draw_Height);
//			System.out.println("Bmp_Width = " + aBmp.getWidth() + "  Bmp_Height = " + aBmp.getHeight());
			g.drawBitmap(aBmp, 0, 0, null);
		}
		g.restore();
	}
	
	
	/**
	 * 初始化精灵，定义有效的图形区域 指定图片中的一行作为对象要显示时的帧序列
	 * @param image Image 侦图片
	 * @param clipRectx int 有效区域在原图中的x坐标起始位置
	 * @param clipRecty int 有效区域在原图中的y坐标起始位置
	 * @param clipRectwidth int 有效区域在原图中的宽度
	 * @param clipRectheight int 有效区域在原图中的高度
	 * @param count int 总帧数
	 */
	public final void initImage(ResFile image, int clipRectx, int clipRecty, int clipRectwidth, int clipRectheight,
			int count) {
		initImage(image, clipRectx, clipRecty, clipRectwidth, clipRectheight, count, 1);
	}
	
	/**
	 * 初始化精灵
	 * @param image
	 * @param cn
	 * @param rn
	 */
	public final void initImage(ResFile image, int cn, int rn) {
		initImage(image, (byte)0, (byte)0, (byte)(image == null ? 0 : image.getWidth()),
				(byte)(image == null ? 0 : image.getHeight()), cn, rn);
	}
	
	/**
	 * 初始化精灵
	 * @param image
	 * @param sx
	 * @param sy
	 * @param sw
	 * @param sh
	 * @param cn
	 * @param rn
	 */
	public final void initImage(ResFile image, int sx, int sy, int sw, int sh,
			int cn, int rn) {
		image_Obj = image;
		image_Window_sx = sx;
		image_Window_sy = sy;
		image_Window_sw = sw;
		image_Window_sh = sh;
//		frame_Count = (short)(cn * rn);
//		frame_Count_H = (short)cn;
//		frame_Count_V = (short)rn;

//		if (frame_Count != 0) {
//			width = (short)(image_Window_sw / cn);
//			height = (short)(image_Window_sh / rn);
//			setFrameList(null);
//		}
	}

	/**
	 * 初始化精灵，定义有效区域 整张图片作为精灵的有效区域
	 * @param image Image 侦图片
	 * @param count int 总帧数
	 */
	public final void initImage(ResFile image, int count) {
		if (image != image_Obj) // || frame_Count_H != count)
			initImage(image, (byte)0, (byte)0, (byte)(image == null ? 0 : image.getWidth()),
					(byte)(image == null ? 0 : image.getHeight()), count);
	}

	/**
	 * 获取图形文件
	 * @return
	 */
	public ResFile getImageFile() {
		return image_Obj;
	}

	/**
	 * 设置图形文件
	 * @return
	 */
	public void setImageFile(ResFile aImgFile) {
		image_Obj = aImgFile;
	}
	
	/**
	 * 指定显示帧 例如： user.setFrame(5);//显示帧号5
	 * user.setFrame((Drawable.FLIP_HORIZONTAL<<16)|2);//水平翻转显示帧号2
	 * user.setFrame((Drawable.FLIP_VERTICAL<<16)|2);//垂直翻转显示帧号2
	 * user.setFrame((Drawable.ROTATE_180<<16)|4);//逆时针旋转180度显示帧号4
	 * @param i  int 帧参数，高字节表示图片转换模式，低字节表示帧号
	 */
//	public void setFrame(int i) {
//		frame_Index = (short)(i & 0xffff);// 后两字节
//		tranFormAnchor = (short)(i >>> 16);// 前两字节
//	}
	
	/**
	 * 设置转换
	 * @param transform
	 */
	public void setTransform(int transform) {
		tranFormAnchor = transform;
	}

//	/**
//	 * 显示下一帧
//	 * @return int 对象当前所处在的帧号
//	 */
//	public int nextFrame() {
//		int[] frame_SN = getFrameList();
//		if (frame_SN != null) {
//			if (snPt + 1 < frame_SN.length) {
//				snPt++;
//			} else {
//				snPt = 0;
//			}
//			setFrame(frame_SN[snPt]);
//		} else {
//			//#debug info
//			throw new NullPointerException("maybe no initImage!");
//		}
//		return frame_Index;
//	}

//	/**
//	 * 前一帧
//	 * @return int 对象当前所处在的帧号
//	 */
//	public int preFrame() {
//		int[] frame_SN = getFrameList();
//		if (snPt > 0) {
//			snPt--;
//		} else {
//			snPt = (short)(frame_Count - 1);
//		}
//		setFrame(frame_SN[snPt]);
//		return frame_Index;
//	}

	/**
	 * 设置帧序列，当s==null时则默认设置为连续顺序帧s={0,1,2,3,...} 实例： s={0,1,0,1,1,2,3,3,1,2} 或者
	 * s={0,1, (Drawable.FLIP_HORIZONTAL<<16)|2,//显示此帧时需要翻转显示 1,
	 * (Drawable.ROTATE_180<<16)|4, 2}
	 * @param s int[]
	 */
//	public void setFrameList(int[] s) {
//		if (s == null) {
//			s = new int[frame_Count];
//			for (int i = 0; i < s.length; i++) {
//				s[i] = i;
//			}
//		}
//		snPt = 0;
//		frameList_Obj = s;
//		setFrame(s[snPt]);
//	}

	/**
	 * 设置精灵的尺寸
	 * @param x int 坐标x
	 * @param y int 坐标y
	 */
//	public void setSize(byte w, byte h) {
//		switch (tranFormAnchor) {
//		case ROTATE_90:
//		case ROTATE_270: 
//		case TRANS_MIRROR_ROT90:
//		case TRANS_MIRROR_ROT270: {
//			width = h;
//			height = w;
//			break;
//		}
//		default: {
//			width = w;
//			height = h;
//			break;
//		}
//		}
//	}

	/**
	 * 获取精灵宽度
	 * 
	 * @return
	 */
	public int getWidth() {
		switch (tranFormAnchor) {
		case ROTATE_90:
		case ROTATE_270: 
		case TRANS_MIRROR_ROT90:
		case TRANS_MIRROR_ROT270: {
			if(image_Window_sh<0){
				return image_Window_sh+256;
			}
			return image_Window_sh;
		}
		
		default: {
			if(image_Window_sw<0){
				return image_Window_sw+256;
			}
			return image_Window_sw;
		}
		}
	}

	/**
	 * 获取精灵高度
	 * @return
	 */
	public int getHeight() {
		switch (tranFormAnchor) {
		case ROTATE_90:
		case ROTATE_270: 
		case TRANS_MIRROR_ROT90:
		case TRANS_MIRROR_ROT270: {
			if(image_Window_sw<0){
				return image_Window_sw+256;
			}
			return image_Window_sw;
		}
		default: {
			if(image_Window_sh<0){
				return image_Window_sh+256;
			}
			return image_Window_sh;
		}
		}
	}

	/**
	 * 获取精灵当前帧号
	 * @return
	 */
//	public int getFrameIndex() {
//		return frame_Index;
//	}

	/**
	 * 获取精灵当前帧序列
	 * @return
	 */
//	public int[] getFrameList() {
//		return frameList_Obj;
//	}

	/**
	 * 获取精灵当前帧序列指针
	 * @return
	 */
//	public int getFrameListPt() {
//		return snPt;
//	}


//	public int getFrameCount() {
//		return frame_Count;
//	}

	/**
	 * 设置精灵当前帧序列指针
	 * 
	 * @param pt
	 */
//	public void setFrameListPt(int pt) {
//		snPt = (short)pt;
//		setFrame(getFrameList()[snPt]);
//	}

	// /**
	// * 旋转所设置的图片
	// * @param degree
	// * 1，正数表示逆时针旋转，负数表示顺时针旋转
	// * 2，单位度，比如30,60,90.....
	// */
	// public void rotate(int degree){
	// if(degree!=0){
	// if(rotateSrcImg==null)rotateSrcImg=getImage();
	// if(rotateSrcImg!=null){
	// //默认按下中点旋转，如果有指定需要，再做参数扩展
	// Image newImage=MobileUtil.rotate(rotateSrcImg,
	// rotateSrcImg.getWidth()/2,rotateSrcImg.getHeight(),
	// degree);
	// initImage(newImage, 1);
	// }
	// }
	// }
	//	  
	// private Image rotateSrcImg;//旋转前的原始图形
	
	/**
	 * 精灵显示 1）正常、翻转、旋转显示指定帧 2）显示在可见区域内的帧部分
	 * 
	 * @param g
	 */
	public void paint(Canvas g) {		
		paint(g,image_Window_sw,image_Window_sh);
	}
	public void paint(Canvas g,int width,int height){
		if (image_Obj == null) {
			return;
		}
		//载入图片
		Bitmap image = image_Obj.getImage();
		if (image == null) {
			return;
		}
		// 计算出要显示的帧在图片中的位置
		int draw_org_X = image_Window_sx < 0 ? 256 + image_Window_sx : image_Window_sx;
		int draw_org_Y = image_Window_sy < 0 ? 256 + image_Window_sy : image_Window_sy;
		if(draw_org_X >= image.getWidth() || draw_org_Y >= image.getHeight()) {
			return;
		}
		int draw_Width = width < 0 ? 256 + width : width;
		int draw_Height = height < 0 ? 256 + height : height;
		if(draw_org_X + draw_Width > image.getWidth()) {
			draw_Width = image.getWidth() - draw_org_X;
		}
		if(draw_org_Y + draw_Height > image.getHeight()) {
			draw_Height = image.getHeight() - draw_org_Y;
		}
		int clip_Width = draw_Width;
		int clip_Height = draw_Height;
		// 图形转换：翻转与旋转
		int anchor = tranFormAnchor;
		switch (anchor) {
		case FLIP_HORIZONTAL: { // 水平翻转
			break;
		}
		case FLIP_VERTICAL: { // 垂直翻转
			break;
		}
		case ROTATE_90: { // 逆时针旋转90
			clip_Width = draw_Height;
			clip_Height = draw_Width;
			break;
		}
		case ROTATE_180: { // 逆时针旋转180
			break;
		}
		case ROTATE_270: { // 逆时针旋转270
			clip_Width = draw_Height;
			clip_Height = draw_Width;
			break;
		}
		case TRANS_MIRROR_ROT90: { // 镜像逆时针旋转90
			clip_Width = draw_Height;
			clip_Height = draw_Width;
			break;
		}
		case TRANS_MIRROR_ROT270: {  // 镜像逆时针旋转270
			clip_Width = draw_Height;
			clip_Height = draw_Width;
			break;
		}
		}
		g.save();    
		if (anchor == 0) {
			g.clipRect(0, 0, clip_Width, clip_Height);
			g.drawBitmap(image, -draw_org_X, -draw_org_Y, null);
		} else {
			g.clipRect(0, 0, clip_Width, clip_Height);
			drawRegion(g, image, draw_org_X, draw_org_Y, draw_Width, draw_Height, anchor, 0, 0, 0);
		}
		g.restore();
	}
	
	
	//获取镜像、旋转方式
	public int getTranFormAnchor() {
		return tranFormAnchor;
	}
	
	//设置镜像、选择方式
	public void setTranFormAnchor(byte aTranFormAnchor) {
		tranFormAnchor = aTranFormAnchor;
	}
	
	public void drawRegion(Canvas canvas, Bitmap img, int x_src, int y_src, int width,
			   int height, int transform, int x_dest, int y_dest, int anchor) {
		  canvas.save();
	  int ix = 0, iy = 0;
	  switch (transform) {
	  case Sprite.ANCHOR_NONE: // 0
	   break;
	  case Sprite.ANCHOR_ROTATE_270: // 90
	   canvas.rotate(90, x_dest, y_dest);
	   iy = height;
	   break;
	  case Sprite.ANCHOR_ROTATE_180: // 180
	   canvas.rotate(180, x_dest, y_dest);
	   iy = height;
	   ix = width;
	   break;
	  case Sprite.ANCHOR_ROTATE_90: // 270
	   canvas.rotate(270, x_dest, y_dest);
	   ix = width;
	   break;
	  case Sprite.ANCHOR_FLIP_HORIZONTAL: // M
	   canvas.scale(-1, 1, x_dest, y_dest);// 镜像
	   ix = width;
	   break;
	  case Sprite.ANCHOR_TRANS_MIRROR_ROT270: // M90 j2me<-->android 270
	   canvas.scale(-1, 1, x_dest, y_dest);// 镜像
	   canvas.rotate(270, x_dest, y_dest);
	   ix = width;
	   iy = height;
	   break;
	  case Sprite.ANCHOR_FLIP_VERTICAL: // M180
	   canvas.scale(-1, 1, x_dest, y_dest);// 镜像
	   canvas.rotate(180, x_dest, y_dest);
	   iy = height;
	   break;
	  case Sprite.ANCHOR_TRANS_MIRROR_ROT90: // M270 j2me<-->android 90
	   canvas.scale(-1, 1, x_dest, y_dest);// 镜像90
	   canvas.rotate(90, x_dest, y_dest);
	   break;
	  }
	  canvas.clipRect(x_dest - ix, y_dest - iy, x_dest - ix + width, y_dest
	    - iy + height);
	  canvas.drawBitmap(img, x_dest - ix - x_src, y_dest - iy - y_src,
	    null);
	  canvas.restore();
	 }
}
