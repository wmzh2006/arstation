

package com.funoble.myarstation.gamebase;

import java.io.DataInputStream;

/**
 * Title: 精灵：人物,NPC等
 * Description: 具有不同的状态，每一状态具有不同的帧，每一帧具有不同的子图
 * <p>Copyright: 盈动天下科技有限公司.</p>
 * @author mark
 */
public class MapRect extends BaseAction {

	public static final int UNABLE_WALK_RECT = 0;
	public static final int PORTAL_RECT = 1;
	
	public short iType = 0;//类型
	public short iValue = 0;//值
	public short width;
	public short height;
	
	public short iID = 0;
	private boolean ibCollision = false; //是否已经相碰	
	
	
	//游戏窗口
//	private CMmoWindow iMmoWindow = CMmoWindow.getInstance();
	
	/**
	 * 拷贝精灵,复制所有动作
	 * @param b BaseAction
	 */
//	public void copyFrom(BaseAction c) {
//		super.copyFrom(c);
//		// 深层拷贝动作
//		if (c != null && c instanceof Sprite) {
//			Sprite cc = (Sprite) c;
//			initBaseActionSize(cc.actionList.length);
//			for (int i = 0; i < cc.actionList.length; i++) {
//				SpriteAction sa = (SpriteAction) cc.actionList[i];
//				SpriteAction newsa = new SpriteAction();
//				newsa.copyFrom(sa);
//				setAction(newsa, i);
//			}
//		}
//	}

	/**
	 * 导入二进制结构
	 * @param dis DataInputStream
	 * @throws Exception
	 */
	public void inputStream(DataInputStream dis, int format) throws Exception {
		super.inputStream(dis, format);
		iType = (short)dis.readInt();
		iValue = (short)dis.readInt();
		x = (short)dis.readInt();
		y = (short)dis.readInt();
		width = (short)dis.readInt();
		height = (short)dis.readInt();
	}
	
	/**
	 * 显示
	 * @param g
	 * @param x
	 * @param y
	 */
//	public void paintAction(Graphics g, int x, int y, int aActionIndex, int aFrameIndex) {
//		if(aActionIndex >= 0 && aActionIndex < this.actionList.length) {
//			SpriteAction tempAction = (SpriteAction)this.actionList[aActionIndex];
//			if(aFrameIndex >=0 && aFrameIndex < tempAction.actionList.length) {
//				ActionFrame tempFrame = (ActionFrame)tempAction.actionList[aFrameIndex];
//				tempFrame.paint(g, x, y);
//			}
//		}
//	}
	
	/**
	 * 水平镜像显示动作
	 * @param g
	 * @param x
	 * @param y
	 */
//	public void paintActionMirror(Graphics g, int x, int y, int aActionIndex, int aFrameIndex) {
//		if(aActionIndex >= 0 && aActionIndex < this.actionList.length) {
//			SpriteAction tempAction = (SpriteAction)this.actionList[aActionIndex];
//			if(aFrameIndex >=0 && aFrameIndex < tempAction.actionList.length) {
//				ActionFrame tempFrame = (ActionFrame)tempAction.actionList[aFrameIndex];
//				tempFrame.paintMirror(g, x, y);
//			}
//		}
//	}

	
//	public boolean collisionToNpc(CPlayer aNpc, int tempX, int tempY, int tempW, int tempH) {
//				
//		//判断矩形是否碰撞
//		if( !UtilTools.rectIntersects(tempX,tempY,tempW,tempH,x,y,width,height) ) {
//			ibCollision = false;
//			return false;
//		}
//		
//		switch(iType) {
//		case UNABLE_WALK_RECT:	//不可行走区域
//			{
//				if(aNpc.isPathing()){//自动寻路不碰撞
//					return false;
//				}
//				ibCollision = true;
//				//如果Npc第一次与区域碰撞
//				if(aNpc.getCollisionMapRectID() == -1) {
//					aNpc.setMoveX(0);
//					aNpc.setMoveY(0);
//					aNpc.setCollisionMapRectID(iID);
//					int direct = getRunDirect(aNpc,tempX,tempY,tempW,tempH);
//					aNpc.setNextDirectID(direct);
////					System.out.println("---------------------------setCollisionMapRectID = " + iID);
//				}
//				//再次与其它区域碰撞
//				else {
//					if(aNpc.getCollisionMapRectID() != iID) {
//						aNpc.setMoveX(0);
//						aNpc.setMoveY(0);
//					}
//				}
//			}
//			break;
//			
//		case PORTAL_RECT:		//传送门
//			{	
//				if(!ibCollision) {
//					ibCollision = true;
//					//发送切换地图消息
//					if(!iMmoWindow.isChangingScene()) {
//						iMmoWindow.notifyChangeScene(iValue);
//						ibCollision = false;
//					}
//					else {
//						iMmoWindow.notifyReachNewScene();
//					}
//				}
//			}
//			break;
//		}
//		return true;
//	}
	
	//获取预行走方向
//	private int getRunDirect(CPlayer aNpc, int tempX, int tempY, int tempW, int tempH) {
//		int direct = aNpc.getDirectID();
//		switch(aNpc.getDirectID()) {
//		case CPlayer.NPC_DIRECT_R://方向右
//		case CPlayer.NPC_DIRECT_L: //方向左
//			{
//				if(tempY+tempY+tempH > y+y+height) {
////					aNpc.setMoveY(4);
//					direct = CPlayer.NPC_DIRECT_D;
//				}
//				else {
////					aNpc.setMoveY(-4);
//					direct = CPlayer.NPC_DIRECT_U;
//				}
//			}
//			break;
//			
//		case CPlayer.NPC_DIRECT_D: //方向下
//		case CPlayer.NPC_DIRECT_U: //方向上
//			{
//				if(tempX+tempX+tempW > x+x+width) {
////					aNpc.setMoveX(4);
//					direct = CPlayer.NPC_DIRECT_R;
//				}
//				else {
////					aNpc.setMoveX(-4);
//					direct = CPlayer.NPC_DIRECT_L;
//				}
//			}
//			break;
//		}
//		return direct;
//	}
	
	
}//END CLASS MapRect
				
//					if(aOffsetX != 0) {
//						if(tempY+tempY+tempH > mapRect.y+mapRect.y+mapRect.height) {
//							direction = NPC_DIRECT_D;
//						}
//						else {
//							direction = NPC_DIRECT_U;
//						}
//					}
//					else if(aOffsetY != 0) {
//						if(tempX+tempX+tempW > mapRect.x+mapRect.x+mapRect.width) {
//							direction = NPC_DIRECT_R;
//						}
//						else {
//							direction = NPC_DIRECT_L;
//						}
//					}
//					return direction;
//				}
//			}
//		}
//	}
//}
