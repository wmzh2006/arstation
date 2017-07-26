package com.funoble.myarstation.gamebase;


import java.io.*;
import java.util.*;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.utils.ActivityUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;



/**
 * Title: 地图
 * Description: 场景地图
 * <p>Copyright: 腾讯科技(深圳)有限公司.</p>
 * @author cheerluo
 */
public class Map extends BaseAction {

	// 本地图用到的chip集合
	private Vector chipSet = new Vector();

	// 元素通过状态 把元素分为4块 0表示通过,1表示不能通过 用四位表示
	private int[] chipSet_PASSSTATE;

	// 地图元素类型
	protected int layoutIndex;// 地图层次索引
	private boolean isGrid=true;// 是否是网格形式地图

	// 地图网格尺寸
	private int cellWidth, cellHeight;
	private int zoomCellWidth, zoomCellHeight;//缩放后的，用于计算画地板个数
	private int row,col;//网格行数列数

	// 地图数据------利用二维数组保存
	private byte[][] mapData = new byte[0][0];

	// 地图数据------利用向量列表保存
	private Vector mapSprteList = new Vector();
	
	//元素属性数据------利用向量列表保存
	private Vector mapResList = new Vector();

	NinePatch[] iNinePatch = null;
	private Vector<Rect> iNinePatchRects = null;
	
	/**
	 * 释放自己
	 */
	public void releaseSelf() {
		if(mapResList != null) {
			for(int i=0; i<mapResList.size(); i++) {
				((BaseRes)mapResList.elementAt(i)).releaseSelf();
				iNinePatch[i] = null;
			}
			mapResList.removeAllElements();
			iNinePatchRects.removeAllElements();
		}
		iNinePatch = null;
		iNinePatchRects = null;
		if(mapSprteList != null) {
			for(int i=0; i<mapSprteList.size(); i++) {
				((BaseAction)mapSprteList.elementAt(i)).releaseSelf();
			}
			mapSprteList.removeAllElements();
		}
		if(mapData != null) {
			for(int i=0; i<mapData.length; i++) {
				mapData[i] = null;
			}
			mapData = null;
		}
		chipSet_PASSSTATE = null;
		if(chipSet != null) {
			for(int i=0; i<chipSet.size(); i++) {
				((BaseAction)chipSet.elementAt(i)).releaseSelf();
			}
			chipSet.removeAllElements();
		}
		
		//debug
		Tools.println("Map释放！");
	}
	
	/**
	 * 构造
	 */
	public Map(int layoutIndex) {
		this.layoutIndex = layoutIndex;
	}

	/**
	 * 是否是网格形式地图
	 * @return
	 */
	public boolean isGrid() {
		return isGrid;
	}

	public void setNinePatch(int[] aID) {
		if(aID == null) return;
		for(int i=0; i<aID.length; i++) {
			if(iNinePatch[i] != null) {
				continue;
			}
			Bitmap bitmap = BitmapFactory.decodeResource(MyArStation.mGameMain.getResources(), aID[i]);
			iNinePatch[i] = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
			bitmap = null;
		}
	}
	
	
	public Rect getNineRect(int index) {
		if(iNinePatchRects != null && iNinePatchRects.size() > index) {
			return iNinePatchRects.get(index);
		}
		return null;
	}
	/**
	 * 显示指定像素区域的地图
	 * @param g
	 * @param gx
	 * @param gy
	 * @param gw
	 * @param gh
	 */
	public void draw(Canvas g, int sx, int sy, int sw, int sh) {
		if (isGrid) {
			byte[][] mapData = this.mapData;
			if (mapData == null || mapData.length == 0 || mapData[0] == null
					|| layoutIndex == Scene.RectangleLayout) {
				return;
			}
			Rect saveClipRect = g.getClipBounds();
			g.clipRect(0, 0, sw, sh);
			int gx = sx / zoomCellWidth;
			int gy = sy / zoomCellHeight;
			int gw = (sx + sw + zoomCellWidth - 1) / zoomCellWidth - gx;
			int gh = (sy + sh + zoomCellHeight - 1) / zoomCellHeight - gy;
			int ox = 0, oy = 0;
			int ex = gx + gw;
			int ey = gy + gh;
			int chipNum = chipSet.size();
			for (int i = gy; i < ey; i++) {
				for (int j = gx; j < ex; j++) {
					if (i >= 0 && i < mapData.length && j >= 0
							&& j < mapData[i].length && mapData[i][j] != -1) { // 合法性检查
						int id = mapData[i][j] & 0x00ff;
						if (id < chipNum) {
							BaseAction item = (BaseAction) chipSet
									.elementAt(id);
							item.setPosition((short)((j * cellWidth - sx + ox)*ActivityUtil.PAK_ZOOM_X), 
									(short)((i * cellHeight - sy + oy)*ActivityUtil.PAK_ZOOM_Y));
							item.draw(g);
						}
					}
				}
			}
			g.clipRect(saveClipRect);
		} 
		else {
			for (int i = 0; i < mapSprteList.size(); i++) {
				BaseAction ba = (BaseAction) mapSprteList.elementAt(i);
				int tempX = ba.getX()-sx;
				int tempY = ba.getY()-sy;
				if(tempX+64 > 0 && tempY+64 > 0
						&& tempX < sw && tempY < sh) {
					ba.paint(g,tempX,tempY);
				}
			}
		}
		
//		//画碰撞矩形
		if(layoutIndex == Scene.RectangleLayout) {
			MapRect tempRect = null;
			Rect drawRect = new Rect();
			for(int k=0; k<mapSprteList.size(); k++) {
				tempRect = (MapRect) mapSprteList.elementAt(k);
				int tempX = tempRect.getX()-sx;
				int tempY = tempRect.getY()-sy;
				drawRect.left = tempX;
				drawRect.top = tempY;
				drawRect.right = tempX+tempRect.width;
				drawRect.bottom = tempY+tempRect.height;
				if(iNinePatch[k] != null) {
					iNinePatch[k].draw(g, drawRect);
				}
			}
		}
//		if(layoutIndex == Scene.RectangleLayout) {
//			MapRect tempRect = null;
//			Paint paint = new Paint();
//			paint.setColor(0xff0000);
//			paint.setStrokeWidth(2);
//			paint.setStyle(Style.FILL);
//			if(mapSprteList.size() <= 0) {
//				return;
//			}
//			System.out.println("asdfasdfsadfsdf mapSprteList.size()=" + mapSprteList.size());
//			for(int k=0; k<mapSprteList.size(); k++) {
//				tempRect = (MapRect) mapSprteList.elementAt(k);
//				int tempX = tempRect.getX()-sx;
//				int tempY = tempRect.getY()-sy;
//				g.drawRect(tempX, tempY, tempX+tempRect.width, tempY+tempRect.height, ActivityUtil.mRectPaint);
//				g.drawText("asdf", tempX, tempY, ActivityUtil.mMoneyPaint);
//				System.out.println("RectangleLayout tempX =" + tempX + "  tempY=" + tempY
//						+ "  w=" + tempRect.width + "  h=" + tempRect.height);
//			}
//		}
	}

	/**
	 * 获取地区元素表
	 * @return Vector
	 */
	public Vector getChipList() {
		return chipSet;
	}

	/**
	 * 获取地区元素表
	 * @return Vector
	 */
	public Vector getSpriteList() {
		return mapSprteList;
	}
	
	/**
	 * 获取地图单元格宽度
	 * @return int
	 */
	public int getCellWidth() {
		return cellWidth;
	}

	/**
	 * 获取地图单元格高度
	 * @return int
	 */
	public int getCellHeight() {
		return cellHeight;
	}

	/**
	 * 获取地图宽度
	 * @return int
	 */
	public int getMaxWidth() {
		return cellWidth * col;
	}

	/**
	 * 获取地图高度
	 * @return int
	 */
	public int getMaxHeight() {
		return cellHeight * row;
	}

	/**
	 * 导入二进制结构
	 * @param dis DataInputStream
	 * @throws Exception
	 */
	public void inputStream(DataInputStream dis, int format) throws Exception {
	    int tempchipType = dis.readByte();
	    isGrid=(tempchipType>>>7)==0;//最高位表示是否网格
		int size = dis.readShort();
		chipSet.removeAllElements();
		if (layoutIndex == Scene.MapLayout && chipSet_PASSSTATE == null) {
			chipSet_PASSSTATE = new int[size];
		}
		Project project = Project.getLoadingProject();
		for (int i = 0; i < size; i++) {
			Object chip = inputIdentify(project, dis, format,
					chipSet_PASSSTATE, i);
			chipSet.addElement(chip);
		}
		cellWidth = dis.readShort();
		cellHeight = dis.readShort();
		zoomCellWidth = (int) (cellWidth*ActivityUtil.PAK_ZOOM_X);
		zoomCellHeight = (int) (cellHeight*ActivityUtil.PAK_ZOOM_Y);
		row = dis.readShort();
		col = dis.readShort();
		if (isGrid) {
			mapData = new byte[row][col];
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					mapData[i][j] = (byte) dis.read();
				}
			}
		} else {
			mapSprteList.removeAllElements();
			int num = dis.readUnsignedShort();
			if(layoutIndex == Scene.RectangleLayout) {
				num = dis.readUnsignedShort();
				BaseAction ac = null;
				//创建NinePatch数组
				iNinePatch = new NinePatch[num];
				iNinePatchRects = new Vector<Rect>();
				//
				for(int k=0; k<num; k++) {
					ac = new MapRect();
					MapRect tempRect = (MapRect) ac;
					tempRect.iID = (short)k;
					ac.inputStream(dis, format);
					//根据缩放比例，重设数值
					tempRect.x = (short) (tempRect.x*ActivityUtil.PAK_ZOOM_X);
					tempRect.y = (short) (tempRect.y*ActivityUtil.PAK_ZOOM_Y);
					tempRect.width = (short) (tempRect.width*ActivityUtil.PAK_ZOOM_X);
					tempRect.height = (short) (tempRect.height*ActivityUtil.PAK_ZOOM_Y);
					//
					mapSprteList.addElement(ac);
					iNinePatchRects.add(new Rect(tempRect.x, tempRect.y, tempRect.x+tempRect.width, tempRect.y+tempRect.height));
				}
				return;
			}
			
//			System.out.println("Map layoutIndex=" + layoutIndex + "  num=" + num);
			for (int i = 0; i < num; i++) {
				int id = dis.read();
				int x = dis.readShort();//dis.readUnsignedShort();
				int y = dis.readShort();//dis.readUnsignedShort();
				if (id != 255 && id < size) {
					BaseAction item = (BaseAction) chipSet.elementAt(id & 0x00ff);
					BaseAction ac = null;
					switch(layoutIndex) {
					case Scene.MapLayout: // 第一层 地面层
					case Scene.FloorLayOut: //第二层 杂物层
						ac = new ImageClip();
						ac.copyFrom(item);
						break;
						
					case Scene.BuildLayout: // 第三层 建筑层
						Sprite tempSprite = (Sprite)item;
						//载入建筑数据
						BuildRes res = new BuildRes();
						res.intputStream(dis);
						ac = new CBuilding(tempSprite, res);
						break;
						
					case Scene.SpriteLayout: // 第四层 NPC层
						ac = new Sprite();
						ac.copyFrom(item);
						//载入精灵数据
						UnitRes unitRes = new UnitRes();
						unitRes.intputStream(dis);
						mapResList.addElement(unitRes);
						break;
						
					case Scene.RectangleLayout: // 第五层 逻辑层
						//载入逻辑矩形
						ac.copyFrom(item);
						break;
					}
					x = (int) (x*ActivityUtil.PAK_ZOOM_X);
					y = (int) (y*ActivityUtil.PAK_ZOOM_Y);
					ac.setPosition((short)x, (short)y);
					mapSprteList.addElement(ac);
				}
			}
		}

	}

	/**
	 * 根据身份导入对象
	 * @param project Project
	 * @param chipType int
	 * @param dis DataInputStream
	 * @throws Exception
	 * @return BaseAction
	 */
	public Object inputIdentify(Project project, DataInputStream dis,
			int format, int[] chipSet_PASSSTATE, int index) throws Exception {
		switch (layoutIndex) {
//		case Scene.EventLayout: {
//			byte[] newTrigger = (byte[]) Project.readArray(dis, -1,
//					Project.TYPE_BYTE);
//			return newTrigger;
//		}
		case Scene.RectangleLayout: {
			return null;
		}
		case Scene.BuildLayout:
		case Scene.SpriteLayout: {
			short id = dis.readShort();
			BaseAction b = (BaseAction) project.getObject(id, Project.SPRITE);
			return b;// .copyToImp(new Sprite());
		}
		
		case Scene.FloorLayOut:
		case Scene.MapLayout: {
			short flag = dis.readShort();
			int id = flag & 0x0fff;
			int passState = flag >>> 12;
			BaseAction b = (BaseAction) project.getObject(id, Project.CLIP);

			if (chipSet_PASSSTATE != null)
				chipSet_PASSSTATE[index] = passState;
			return b;
		}
		}
		return null;
	}

}
