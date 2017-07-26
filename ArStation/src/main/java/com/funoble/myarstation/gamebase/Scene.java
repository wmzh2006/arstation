package com.funoble.myarstation.gamebase;


import java.io.*;
import java.util.*;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.utils.ActivityUtil;

import android.graphics.Canvas;

/**
 * Title: 场景
 * Description: 地图及地图相关的人物、动画等
 * <p>Copyright: 腾讯科技(深圳)有限公司.</p>
 * @author cheerluo
 */
public class Scene extends BaseAction {

	private int layoutNum = 5; // 场景层数
	private Vector mapLayout = new Vector();
	public static final int MapLayout = 0; // 第一层 地面层
	public static final int FloorLayOut = 1; //第二层 杂物层
	public static final int BuildLayout = 2; // 第三层 建筑层
	public static final int SpriteLayout = 3; // 第四层 NPC层
	public static final int RectangleLayout = 4; // 第五层 逻辑层
	//光效管理器
//	public CLightEffectManager sceneLightEffectManager = new CLightEffectManager();
	public static final int KEY_MapType = 1; // 地图类型
	public static final int KEY_BackColor = 2; // 地图背景

	public int viewWindowX, viewWindowY, viewWindowWidth, viewWindowHeight;// 视图窗口
	
	//精灵列表,绘图用
	private Vector iSpriteList = new Vector();
	
	//场景的宽高
	public int iWidth;
	public int iHeight;
	
	//摄像机坐标
	public int iCameraX = 0;
	public int iCameraY = 0;

	
	
	/**
	 * 释放自己
	 */
	public void releaseSelf() {
		if(iSpriteList != null) {
			iSpriteList.removeAllElements();
		}
		if(mapLayout != null) {
			for(int i=0; i<mapLayout.size(); i++) {
				((Map)mapLayout.elementAt(i)).releaseSelf();
			}
			mapLayout.removeAllElements();
		}
		super.releaseSelf();
		//debug
		Tools.println("Scene释放！");
	}
	
	/**
	 * 构造
	 */
	public Scene() {
	}

	/**
	 * 设置视图窗口
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void setViewWindow(int x, int y, int w, int h) {
		viewWindowX = x;
		viewWindowY = y;
		viewWindowWidth = ActivityUtil.SCREEN_WIDTH;
		viewWindowHeight = ActivityUtil.SCREEN_HEIGHT;
	}

	/**
	 * 获取层列表
	 * @return Vector
	 */
	public Vector getLayoutManager() {
		return mapLayout;
	}

	/**
	 * 设置层列表
	 * @param v Vector
	 */
	public void setLayoutManager(Vector v) {
		mapLayout = v;
	}

	/**
	 * 获取指定层地图
	 * @param layout int
	 * @return Map
	 */
	public Map getLayoutMap(int layout) {
		return (Map) mapLayout.elementAt(layout);
	}

	/**
	 * 设置指定层地图
	 * @param layout int
	 * @param map Map
	 */
	public void setLayoutMap(int layout, Map map) {
		mapLayout.setElementAt(map, layout);
	}
	
	/*
	 * 执行逻辑
	 */
	public void handle() {
		Vector spriteList = getLayoutMap(BuildLayout).getSpriteList();
		if(spriteList != null) {
			for(int i=0; i<spriteList.size(); i++) {
				((CBuilding)spriteList.elementAt(i)).handle();
			}
		}
//		sceneLightEffectManager.handle();
	}
	
	/**
	 * 初始化地图的精灵绘画列表
	 */
	public void initDrawList() {
		iSpriteList.removeAllElements();
		if (getLayoutMap(BuildLayout) != null) {// 刷新精灵层动画
			Vector spriteList = getLayoutMap(BuildLayout).getSpriteList();
			if(spriteList != null) {
				for(int i=0; i<spriteList.size(); i++) {
					iSpriteList.addElement(spriteList.elementAt(i));
				}
			}
		}
	}
	
	/**
	 * 初始化地图的精灵绘画列表
	 */
	public void initNinePatchList(int[] aID) {
		if(aID == null) return;
		//碰撞层
		Map map = ((Map) mapLayout.elementAt(RectangleLayout));
		if(map != null) {
			map.setNinePatch(aID);
		}
	}
	
	/**
	 * 插入精灵到绘画列表
	 * @param aNpcList Vector 
	 */
	public void insertDrawList(Vector aNpcList) {
		//插入Npc
		for(int k=0; k<aNpcList.size(); k++) {
			boolean bInsert = false;
			BaseAction ba = (BaseAction) aNpcList.elementAt(k);
			for(int m=0; m<iSpriteList.size(); m++) {
				BaseAction building = (BaseAction) iSpriteList.elementAt(m);
				if(ba.getY() < building.getY()) {
					iSpriteList.insertElementAt(ba, m);
					bInsert = true;
					break;
				}
			}
			if(!bInsert) {
				iSpriteList.addElement(aNpcList.elementAt(k));
			}
		}
	}
	
	
	/**
	 * 快速排序
	 * @param aSpriteList
	 * @param aLeft
	 * @param aRight
	 */
    public static Vector quickSort(Vector aSpriteList, int aLeft, int aRight) {   
        int i= aLeft, j= aRight;   
        BaseAction middle, strTemp;   
  
        middle = (BaseAction)aSpriteList.elementAt( (aLeft + aRight) / 2 );   
        do {   
            while ( (((BaseAction)aSpriteList.elementAt(i)).getY() < middle.getY() ) && (i < aRight))   
                i++;   
            while ( (((BaseAction)aSpriteList.elementAt(j)).getY() > middle.getY()) && (j > aLeft))   
                j--;   
            if (i <= j) {
                strTemp = (BaseAction)aSpriteList.elementAt(i);  
                aSpriteList.setElementAt(aSpriteList.elementAt(j), i);
                aSpriteList.setElementAt(strTemp, j);  
                i++;   
                j--;   
            }   
        } while (i <= j);   
//        for (int t = 0; t < aSpriteList.size(); t++)   
//            System.out.print(((BaseAction)aSpriteList.elementAt(t)).getY() + " ");   
//        System.out.println("");   
        if (aLeft < j) {   
            quickSort(aSpriteList, aLeft, j);   
        }   
  
        if (aRight > i) {
            quickSort(aSpriteList, i, aRight);
        }
        return aSpriteList;   
    }   
	
	/**
	 * 显示
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paint(Canvas g, int x, int y) {
//		g.translate(x, y);
		draw(g, viewWindowX, viewWindowY, viewWindowWidth, viewWindowHeight);
//		g.translate(-x, -y);
	}

	/**
	 * 只显示地板
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintGround(Canvas g, int x, int y) {
		drawGround(g, viewWindowX, viewWindowY, viewWindowWidth, viewWindowHeight);
	}
	
	/**
	 * 指定显示某层
	 * @param g
	 * @param x
	 * @param y
	 * @param aLayer
	 */
	public void paintLayer(Canvas g, int x, int y, int aLayer) {
		drawLayer(g, viewWindowX, viewWindowY, viewWindowWidth, viewWindowHeight, aLayer);
	}
	
	/**
	 * 显示指定像素区域的地图
	 * @param g
	 * @param sx
	 * @param sy
	 * @param sw
	 * @param sh
	 */
	public void draw(Canvas g, int sx, int sy, int sw, int sh) {

		//地板层
		Map map = ((Map) mapLayout.elementAt(MapLayout));
		if(map != null) {
			map.draw(g, sx+iCameraX, sy+iCameraY, sw, sh);
		}
//		//碰撞层
		map = ((Map) mapLayout.elementAt(RectangleLayout));
		if(map != null) {
			map.draw(g, sx+iCameraX, sy+iCameraY, sw, sh);
		}	
		//杂物层
		map = ((Map) mapLayout.elementAt(FloorLayOut));
		if(map != null) {
			map.draw(g, sx+iCameraX, sy+iCameraY, sw, sh);
		}
		//
		int tempX = 0;
		int tempY = 0;
		for(int i=0; i<iSpriteList.size(); i++) {
			BaseAction ba = (BaseAction) iSpriteList.elementAt(i);
			tempX = ba.getX()-sx-iCameraX;
			tempY = ba.getY()-sy-iCameraY;
//			if(tempX + 64 > 0 && tempY + 44 > 0 
//					&& tempX - 64 < sw && tempY - 84 < sh) {
				ba.paint(g, tempX, tempY);
//			}
		}
	}
	
	/**
	 * 显示指定像素区域的地图
	 * @param g
	 * @param sx
	 * @param sy
	 * @param sw
	 * @param sh
	 */
	public void drawGround(Canvas g, int sx, int sy, int sw, int sh) {

		//地板层
		Map map = ((Map) mapLayout.elementAt(MapLayout));
		if(map != null) {
			map.draw(g, sx+iCameraX, sy+iCameraY, sw, sh);
		}
//		//碰撞层
//		map = ((Map) mapLayout.elementAt(RectangleLayout));
//		if(map != null) {
//			map.draw(g, sx+iCameraX, sy+iCameraY, sw, sh);
//		}	
		//杂物层
		map = ((Map) mapLayout.elementAt(FloorLayOut));
		if(map != null) {
			map.draw(g, sx+iCameraX, sy+iCameraY, sw, sh);
		}
		//
//		int tempX = 0;
//		int tempY = 0;
//		for(int i=0; i<iSpriteList.size(); i++) {
//			BaseAction ba = (BaseAction) iSpriteList.elementAt(i);
//			tempX = ba.getX()-sx-iCameraX;
//			tempY = ba.getY()-sy-iCameraY;
//			ba.paint(g, tempX, tempY);
//		}
	}

	/**
	 * 显示指定像素区域的地图
	 * @param g
	 * @param sx
	 * @param sy
	 * @param sw
	 * @param sh
	 * @param aLayer
	 */
	public void drawLayer(Canvas g, int sx, int sy, int sw, int sh, int aLayer) {
		if(aLayer == MapLayout) {
			//地板层
			Map map = ((Map) mapLayout.elementAt(MapLayout));
			if(map != null) {
				map.draw(g, sx+iCameraX, sy+iCameraY, sw, sh);
			}
		}
		else if(aLayer == RectangleLayout) {
//			//碰撞层
			Map map = ((Map) mapLayout.elementAt(RectangleLayout));
			if(map != null) {
				map.draw(g, sx+iCameraX, sy+iCameraY, sw, sh);
			}
		}
		else if(aLayer == FloorLayOut) {
			//杂物层
			Map map = ((Map) mapLayout.elementAt(FloorLayOut));
			if(map != null) {
				map.draw(g, sx+iCameraX, sy+iCameraY, sw, sh);
			}
		}
		else {
			//
			int tempX = 0;
			int tempY = 0;
			for(int i=0; i<iSpriteList.size(); i++) {
				BaseAction ba = (BaseAction) iSpriteList.elementAt(i);
				tempX = ba.getX()-sx-iCameraX;
				tempY = ba.getY()-sy-iCameraY;
	//			if(tempX + 64 > 0 && tempY + 44 > 0 
	//					&& tempX - 64 < sw && tempY - 84 < sh) {
					ba.paint(g, tempX, tempY);
	//			}
			}
		}
	}
	
	/**
	 * 响应触发事件
	 * @param event byte 事件触发源
	 * @param triggerID 触发器id，-1表示扫描所有的触发器
	 */
	public void handleEvent(byte event, int triggerID) {
		// //#debug info
		// System.out.println("handleEvent
		// event:"+event+",triggerID:"+triggerID);
		// Map eventMap=getLayoutMap(EventLayout);
		// if(triggerID==-1){//表示扫描所有的触发器
		// for(int i=0;i<eventMap.getChipList().size();i++){
		// TriggerExecuter.handleEvent(event,(byte[])
		// (eventMap.getChipList().elementAt(i)));
		// }
		// }
		// else
		// if(triggerID>=0&&triggerID<eventMap.getChipList().size()){//表示扫描指定的触发器
		// TriggerExecuter.handleEvent(event,(byte[])
		// (eventMap.getChipList().elementAt(triggerID)));
		// }
	}

	/**
	 * 导入二进制结构
	 * @param dis DataInputStream
	 * @throws Exception
	 */
	public void inputStream(DataInputStream dis, int format) throws Exception {
		super.inputStream(dis, format);
		layoutNum = dis.readShort();
		mapLayout.removeAllElements();
		for (int i = 0; i < layoutNum; i++) {// 导入地图各层
			Map map = new Map(i);
			map.inputStream(dis, format);
//			if(i == BuildLayout && map.getSpriteList().size() > 1) {
//				quickSort(map.getSpriteList(), 0, map.getSpriteList().size()-1);
//			}
			if(i == 0) {
				iWidth = map.getMaxWidth();
				iHeight = map.getMaxHeight();
			}
			mapLayout.addElement(map);
		}
	}
	
	/*
	 * 通过aValue获取传送点的X坐标
	 */
	public int getPortalX(int aValue) {
		int tempX = 0;
//		Map tempMap = getLayoutMap(Scene.RectangleLayout);
//		if (tempMap == null) {
//			return 0;
//		}
//		Vector spriteList = tempMap.getSpriteList();
//		if(spriteList == null) {
//			return 0;
//		}
//		for(int i=0; i<spriteList.size(); i++) {
//			MapRect mapRect = (MapRect)spriteList.elementAt(i);
//			if(mapRect.iType == MapRect.PORTAL_RECT && mapRect.iValue == aValue) {
//				tempX = mapRect.x + mapRect.width/2;
//				break;
//			}
//		}
		return tempX;
	}

	/*
	 * 通过aValue获取传送点的Y坐标
	 */
	public int getPortalY(int aValue) {
		int tempY = 0;
//		Map tempMap = getLayoutMap(Scene.RectangleLayout);
//		if (tempMap == null) {
//			return 0;
//		}
//		Vector spriteList = tempMap.getSpriteList();
//		if(spriteList == null) {
//			return 0;
//		}
//		for(int i=0; i<spriteList.size(); i++) {
//			MapRect mapRect = (MapRect)spriteList.elementAt(i);
//			if(mapRect.iType == MapRect.PORTAL_RECT && mapRect.iValue == aValue) {
//				tempY = mapRect.y + mapRect.height/2;
//				break;
//			}
//		}
		return tempY;
	}
}
