package com.funoble.myarstation.gamebase;



import java.io.DataInputStream;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.Graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Title: 资源包中的文件类
 * Description: 记录图象数据及设置
 * <p>Copyright: 腾讯科技(深圳)有限公司.</p>
 * @author cheerluo
 */
public class ResFile extends BaseAction {

	private byte[] fileData;// 文件数据
	private Bitmap image; // 图形
	private final int fileID;// 文件ID
	private int usedTime;// 统计被使用次数，引用次数为0时，应该自动删除数据
	private short width;
	private short height;
//	private Bitmap iPicBMPZoom = null;//缩放图
	
	/**
	 * 释放自己
	 */
	public void releaseSelf() {
		image = null;
		fileData = null;
		super.releaseSelf();
		//debug
		Tools.println("ResFile释放!");
	}
	
	
	/**
	 * 初始化
	 * @param id
	 */
	public ResFile(int id) {
		fileID = id;
		image = null;
	}

	/**
	 * 获取文件id
	 * @return
	 */
	public int getFileID() {
		return fileID;
	}

	/**
	 * 设置文件数据
	 * @param data
	 */
	public void setFileData(byte[] data) {
		fileData = data;
	}

	/**
	 * 是否已经初始化
	 * @return
	 */
	public boolean isInited() {
		return fileData != null || image != null;
	}

	/**
	 * 增加被使用次数
	 */
	public void addUsedTime() {
		usedTime++;
	}

	/**
	 * 减少被使用次数
	 */
	public void decUsedTime() {
		if (usedTime > 0) {
			usedTime--;
			if (usedTime == 0) {
				releaseSelf();
				//#debug info
				Tools.println("free fileID:" + fileID);
			}
		}
	}

//	/**
//	 * 获取图形
//	 * @return BufferedImage
//	 */
//	public Bitmap getImage() {	
//		
//		if (image == null && fileData != null) {
//			try {// 获取图形
//				image = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
//				fileData = null;// 并把原始文件数据清空
//				setSize((short)image.getWidth(), (short)image.getHeight());
//				addUsedTime();
////				System.out.println("ResFile getImage 获取图片......");
//				//#debug info
////				System.out.println("create Image fileID:" + fileID);
//			} catch (Exception e) {
//				//#debug info
//				e.printStackTrace();
//			}
//		}
//		return image;
//	}
	
	/**
	 * 获取图形
	 * @return BufferedImage
	 */
	public Bitmap getImage() {	
		
		if (image == null && fileData != null) {
			try {// 获取图形
//				image = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
				Bitmap srcImage = null;//原图，临时的，视情况释放内存
				srcImage = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
				if (srcImage == null) {
					fileData = null;
					return null;
				}
				//begin 对原图进行缩放，并保存为image，注意：如果没有任何缩放，image与原图是一样的内存(原图那临时的内存不能释放)，否则把原图的临时内存释放
				float tZoomX = ActivityUtil.PAK_ZOOM_X;
				float tZoomY = ActivityUtil.PAK_ZOOM_Y;
				int tZoomW = (int) (srcImage.getWidth()*tZoomX);
				int tZoomH = (int) (srcImage.getHeight()*tZoomY);
				if (tZoomW<=0) {//图片宽 高不能为0
					tZoomW = 1;
				}
				if (tZoomH<=0) {
					tZoomH = 1;
				}
				image = Graphics.zoomBitmap(srcImage, tZoomW, tZoomH);
				if (image != srcImage) {//如果是不一样内存的，释放srcImage临时的内存
					srcImage.recycle();
					srcImage = null;
					Tools.println("ResFile getImage 释放srcImage临时的内存");
				}
				//end
				fileData = null;// 并把原始文件数据清空
				setSize((short)image.getWidth(), (short)image.getHeight());
				addUsedTime();
//				System.out.println("ResFile getImage 获取图片......");
				//#debug info
//				System.out.println("create Image fileID:" + fileID);
			} catch (Exception e) {
				//#debug info
				e.printStackTrace();
			}
		}
		return image;
	}
	
	/**
	 * 设置尺寸
	 * @param w int
	 * @param h int
	 */
	public void setSize(short w, short h) {
		width = w;
		height = h;
	}

	/**
	 * 获取宽度
	 * @return short
	 */
	public short getWidth() {
		return width;
	}

	/**
	 * 获取高度
	 * @return int
	 */
	public short getHeight() {
		return height;
	}
	
	/**
	 * 导入二进制结构
	 * @param dis DataInputStream
	 * @throws Exception
	 */
	public void inputStream(DataInputStream dis, int format) throws Exception {
		super.inputStream(dis, format);
		// if(false){//导入数据，默认不导入，程序中按动态需要手动设置为导入
//		byte[] data = (byte[]) Project.readArray(dis, 0, Project.TYPE_BYTE);
//		fileData=data;
		// }else
		//{// 不导入数据
//			int nc = dis.readUnsignedShort();
//			if (nc == 0xffff) {//大文件，超过64K
//				nc = dis.readInt();
//			}
//			dis.skip(nc);
		//}
			
//		if(format == 1) { //载入数据
			byte[] data = (byte[]) Project.readArray(dis, 0, Project.TYPE_BYTE);
			fileData=data;
			getImage();
//		}
//		else { //不载入数据
//			int nc = dis.readUnsignedShort();
//			if (nc == 0xffff) {//大文件，超过64K
//				nc = dis.readInt();
//			}
//			dis.skip(nc);
//		}
	}

}
