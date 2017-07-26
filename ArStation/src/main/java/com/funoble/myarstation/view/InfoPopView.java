/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: InfoView.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2014年3月22日 下午9:38:46
 *******************************************************************************/
package com.funoble.myarstation.view;

import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.funoble.myarstation.game.CPlayer;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.CBuilding;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.resmanager.CPakManager;
import com.funoble.myarstation.screen.BasePopScreen;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.TPoint;


public class InfoPopView extends BasePopScreen {

	private Scene iSceneDetail = null;
	private Vector<SpriteButton> iSpritebuttonListDetail = new Vector<SpriteButton>();//精灵按钮 列表
	
	private CBuilding iDiceSprite = null; 	//骰子动画
	private CBuilding iVipSprite = null;	//VIP动画
	private CBuilding iCardSprite = null;	//酒水卡动画
	private Sprite iSkillZhaiSprite = null;	//斋技能的图标
	private Sprite iSkillLightnSprite = null; //雷劈技能的图标
	private SpriteButton iRenameSpriteButton = null; //修改按钮
	private SpriteButton iAddFriendSpriteButton = null; //修改按钮
	private CBuilding iSexSprite = null;	//性别
	
	private NinePatch iNinePatch = null; //经验条
	private NinePatch iNinePatchShort = null; //经验条
	//玩家信息
	private Paint iExpPaint;
	private RectF iExpRect;
	private CPlayer iPlayer;
	private onClickReameListen listen;
	private boolean ibRename = false;
	private int 	iSceneID =  0;
	
	public InfoPopView() { 
		iSceneID = 0;
	}
	
	public InfoPopView(int aSceneID) {
		if(aSceneID > 1 || aSceneID < 0) {
			aSceneID = 0;
		}
		iSceneID  = aSceneID;
	}
	
	//初始化画笔
	private void initPaint() {
		iExpPaint = new Paint();
		iExpPaint.setColor(Color.MAGENTA);
	}
	
	//设置经验条
	private void setExpRect() {
		if(iPlayer == null) return;
		//float tW = 260*ActivityUtil.ZOOM_X*GameMain.iPlayer.iGameExp/GameMain.iPlayer.iNextExp;
		float tW = 0;
		if(iSceneID == 0) {
			tW = 578 * iPlayer.iGameExp / iPlayer.iNextExp * ActivityUtil.ZOOM_X;//*iPlayer.iGameExp/100;
			
		}
		else {
			tW = 578 * iPlayer.iIntimate / iPlayer.iNextIntimate * ActivityUtil.ZOOM_X;//*iPlayer.iGameExp/100;
		}
		float tLeft = 119 * ActivityUtil.ZOOM_X;
		float tTop = 185 * ActivityUtil.ZOOM_Y;//418
		float tRight = tLeft + tW;
		float tBottom = tTop + 32 * ActivityUtil.ZOOM_Y;
		iExpRect.set(tLeft, tTop, tRight, tBottom);
	}
	
	public void setPlayerInfo(CPlayer aPlayer) {
		if(aPlayer == null) return;
		if(iPlayer != null) {
			iPlayer = null;
		}
		iPlayer = aPlayer;
		if(iVipSprite != null) {//VIP动画
			iVipSprite.setActionID(iPlayer.iVipLevel);
		}
		if(iSexSprite != null) { //性别
			iSexSprite.setActionID(iPlayer.iSex);
//			System.out.println("InfoScreen iPlayeriSex = " + iPlayer.iSex);
		}
		if(iAddFriendSpriteButton != null) {
			iAddFriendSpriteButton.setVision(iPlayer.iIntimate == -1 ? true: false);
		}
//		if(iDiceSprite != null) {//骰子动画
//			//更新自己的骰子类型
//			iDiceSprite.setActionID(iPlayer.iDiceID / 10);
//		}
//		if(iCardSprite != null) {//酒水卡动画
////			iCardSprite.setActionID(rspMainPage.nHasCard);
//		}
		//重设经验条
		setExpRect();
	}
	
	public void setOnClickRename(onClickReameListen l) {
		listen = l;
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#init()
	 */
	@Override
	public void init() {
		super.init();
		initPaint();
		//载入经验图片
		iExpRect = new RectF();
		Bitmap bitmap = BitmapFactory.decodeResource(MyArStation.mGameMain.getResources(), R.drawable.exp4);
		iNinePatch = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
		bitmap = BitmapFactory.decodeResource(MyArStation.mGameMain.getResources(), R.drawable.exp3);
		iNinePatchShort = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
		bitmap = null;
	}
	
	/* 
	 * @see com.funoble.lyingdice.view.GameView#releaseResource()
	 */
	@Override
	public void releaseResource() {
		super.releaseResource();
		iSexSprite = null;
		iSkillLightnSprite = null; //雷劈技能的图标
		iSkillZhaiSprite = null;	//斋技能的图标
		iNinePatch = null;
		iNinePatchShort = null;
		iSceneDetail = null;
		iSceneLoading = null;
		iUIPak = null;
	}

	
	/* 
	 * @see com.funoble.lyingdice.screen.BasePopScreen#show()
	 */
	@Override
	public void show() {
		if(iState == STATE_LOADING || iState == STATE_ENTERING || iPlayer == null) return;
		iState = STATE_ENTERING;
		isVision = true;
	}

	public void dimiss() {
		iState = STATE_EXITING;
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#paintScreen(android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	public void paintScreen(Canvas g, Paint paint) {
		if(!isVision) return;
		if(iState == STATE_LOADING) {
			if(iSceneLoading != null) {
				iSceneLoading.paint(g, 0, 0);
//				g.drawText("正在读取数据", 278*ActivityUtil.ZOOM_X, 288*ActivityUtil.ZOOM_Y, ActivityUtil.mLoading);
			}
		}
		else {
			// 以绘制透明渐变的矩形View
			g.drawRect(rect, iPaint);
			if(iSceneDetail != null && iPlayer != null) {
				iSceneDetail.paint(g, iSceneDetail.iCameraX, iSceneDetail.iCameraY);
				//昵称
				if(iPlayer.iVipLevel > 0) {
					g.drawText(""+iPlayer.stUserNick, 210*ActivityUtil.ZOOM_X + iSoX, 122*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNickVipPaint);
				}
				else {
					g.drawText(""+iPlayer.stUserNick, 210*ActivityUtil.ZOOM_X + iSoX, 122*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNickPaint);
				}
				if(iSceneID == 0) {
					//凤币
					g.drawText(""+MyArStation.iPlayer.iMoney, 178*ActivityUtil.ZOOM_X + iSoX, 166*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				}
				else {
					g.drawText(""+iPlayer.iTitle, 178*ActivityUtil.ZOOM_X + iSoX, 166*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				}
				//金币
				String tInf = ""+iPlayer.iGb;
				if(iPlayer.iGb > 99999999) {
					tInf = "" + (iPlayer.iGb / 10000);
					tInf += "万";
				}
				g.drawText(tInf, 374*ActivityUtil.ZOOM_X + iSoX, 166*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				//支持数
				tInf = ""+iPlayer.iDingCount;
				if(iPlayer.iDingCount > 9999999) {
					tInf = "" + (iPlayer.iDingCount / 10000);
					tInf += "万";
				}
				g.drawText(tInf, 578*ActivityUtil.ZOOM_X + iSoX, 166*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				//称号：
				g.drawText(iPlayer.iHonor, 208*ActivityUtil.ZOOM_X + iSoX, 300*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				//胜场
				if(iPlayer.iWinCount <= 99999) {
					g.drawText(""+iPlayer.iWinCount, 220*ActivityUtil.ZOOM_X + iSoX, 353*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				}
				else {
					g.drawText(""+iPlayer.iWinCount/10000+"万", 220*ActivityUtil.ZOOM_X + iSoX, 353*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				}
				//灌倒
				if(iPlayer.iReward <= 99999) {
					g.drawText(""+iPlayer.iReward, 434*ActivityUtil.ZOOM_X + iSoX, 353*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				}
				else {
					g.drawText(""+iPlayer.iReward/10000+"万", 434*ActivityUtil.ZOOM_X + iSoX, 353*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				}
				//连击
				g.drawText(""+iPlayer.iMaxHit, 634*ActivityUtil.ZOOM_X + iSoX, 353*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				//财富榜
				if (iPlayer.iWealthRank > 0) {
					g.drawText("第"+iPlayer.iWealthRank+"名", 220*ActivityUtil.ZOOM_X + iSoX, 401*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				}
				else {
					g.drawText("?", 234*ActivityUtil.ZOOM_X  + iSoX, 400*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				}
				//等级榜
				if (iPlayer.iLevelRank > 0) {
					g.drawText("第"+iPlayer.iLevelRank+"名", 434*ActivityUtil.ZOOM_X + iSoX, 401*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				}
				else {
					g.drawText("?", 448*ActivityUtil.ZOOM_X + iSoX, 400*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				}
				//魅力榜
				if (iPlayer.iVictoryRank > 0) {
					g.drawText("第"+iPlayer.iVictoryRank+"名", 634*ActivityUtil.ZOOM_X + iSoX, 401*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				}
				else {
					g.drawText("?", 648*ActivityUtil.ZOOM_X + iSoX, 400*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailNumPaint);
				}
				//
				Rect tempRect = new Rect();
				//职称
				if(iSceneID == 0) {
					if(iExpRect.width() > 0 && iExpRect.width() < 52 * ActivityUtil.ZOOM_X) {
						tempRect.set((int)(iExpRect.left + 4 * ActivityUtil.ZOOM_X) + iSoX, (int)(iExpRect.top), (int)(iExpRect.right), (int)(iExpRect.bottom));
						iNinePatchShort.draw(g, tempRect);
					}
					else if(iExpRect.width() >= 52 * ActivityUtil.ZOOM_X) {
						tempRect.set((int)(iExpRect.left) + iSoX, (int)(iExpRect.top), (int)(iExpRect.right), (int)(iExpRect.bottom));
						iNinePatch.draw(g, tempRect);
					}
					//职称文字
					g.drawText(iPlayer.iTitle, 412*ActivityUtil.ZOOM_X + iSoX, 209*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailLevelPaint);
				}
				else {
					if(iExpRect.width() > 0 && iExpRect.width() < 52 * ActivityUtil.ZOOM_X) {
						tempRect.set((int)(iExpRect.left + 4 * ActivityUtil.ZOOM_X) + iSoX, (int)(iExpRect.top), (int)(iExpRect.right), (int)(iExpRect.bottom));
						iNinePatchShort.draw(g, tempRect);
					}
					else if(iExpRect.width() >= 52 * ActivityUtil.ZOOM_X) {
						tempRect.set((int)(iExpRect.left) + iSoX, (int)(iExpRect.top), (int)(iExpRect.right), (int)(iExpRect.bottom));
						iNinePatch.draw(g, tempRect);
					}
					//职称文字
					g.drawText(iPlayer.iFriendTitle, 412*ActivityUtil.ZOOM_X + iSoX, 209*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailLevelPaint);
				}
				//技能
				if(iPlayer.iZhaiSkill >= 2) {
					iSkillZhaiSprite.paintAction(g, 1, (int)(258*ActivityUtil.ZOOM_X) + iSoX, (int)(243*ActivityUtil.ZOOM_Y)); //斋技能的图标
					iSkillLightnSprite.paintAction(g, 1, (int)(448*ActivityUtil.ZOOM_X) + iSoX, (int)(243*ActivityUtil.ZOOM_Y)); //雷技能的图标
				}
				else if(iPlayer.iZhaiSkill == 1) {
					iSkillZhaiSprite.paintAction(g, 1, (int)(258*ActivityUtil.ZOOM_X)+ iSoX, (int)(243*ActivityUtil.ZOOM_Y)); //斋技能的图标
					iSkillLightnSprite.paintAction(g, 0, (int)(448*ActivityUtil.ZOOM_X) + iSoX, (int)(243*ActivityUtil.ZOOM_Y)); //雷技能的图标
				}
				else {
					iSkillZhaiSprite.paintAction(g, 0, (int)(258*ActivityUtil.ZOOM_X) + iSoX, (int)(243*ActivityUtil.ZOOM_Y)); //斋技能的图标
					iSkillLightnSprite.paintAction(g, 0, (int)(448*ActivityUtil.ZOOM_X) + iSoX, (int)(243*ActivityUtil.ZOOM_Y)); //雷技能的图标
				}
				g.drawText("喊斋", 308*ActivityUtil.ZOOM_X + iSoX, 252*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailTextPaint);
				g.drawText("雷劈", 498*ActivityUtil.ZOOM_X + iSoX, 252*ActivityUtil.ZOOM_Y, ActivityUtil.mDetailTextPaint);
				for(int i=0; i<iSpritebuttonListDetail.size(); i++) {
					((SpriteButton)iSpritebuttonListDetail.elementAt(i)).paint(g, iSceneDetail.iCameraX, iSceneDetail.iCameraY);
				}
				//称号
			}
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#performL()
	 */
	@Override
	public void performL() {
		if(!isVision) return;
		switch (iState) {
		case STATE_LOADING:
			if(iSceneLoading != null) {
				iSceneLoading.handle();
			}
			break;
		case STATE_ENTERING:
			iSoX -= iSoSpeed;
			if(iSoX <= 0) {
				iSoX = 0;
				iState = STATE_ENTERED;
			}
			break;
		case STATE_ENTERED:
			iSoX = 0;
			break;
		case STATE_EXITING:
			if(iSoX < iSoffsetX) {
				iSoX += iSoSpeed;
			}
			else {
				iState = STATE_EXITED;
			}
			break;
		case STATE_EXITED:
			iSoX = iSoffsetX;
			iState = STATE_INVALID;
			if(ibRename && listen != null) listen.onClickRename();
			ibRename = false;
			isVision =false;
			break;

		default:
			break;
		}
		spriteButtonPerform();
		
	}

	private void spriteButtonPerform() {
		if(iSceneDetail != null) {
			iSceneDetail.handle();
			iSceneDetail.iCameraX = -iSoX;
			//
			for(int i=0; i<iSpritebuttonListDetail.size(); i++) {
				((SpriteButton)iSpritebuttonListDetail.elementAt(i)).perform();
			}
		}
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!isVision) return false;
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(!isVision) return false;
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			dimiss();
			return true;
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onDown(android.view.MotionEvent)
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		super.onDown(e);
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onLongPress(android.view.MotionEvent)
	 */
	@Override
	public boolean onLongPress(MotionEvent e) {
		super.onLongPress(e);
		if(!isVision) return false;
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onSingleTapUp(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		super.onSingleTapUp(e);
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#pointerPressed(int, int)
	 */
	@Override
	public boolean pointerPressed(int aX, int aY) {
		if(!isVision) return false;
		if(iState != STATE_ENTERED) return true;
		Rect pRect = new Rect();
		SpriteButton pSB;
		if(null != ViewRect && ViewRect.contains(aX, aY)) {
			for(int i=0; i<iSpritebuttonListDetail.size(); i++) {
				pSB = ((SpriteButton)iSpritebuttonListDetail.elementAt(i));
				if(!pSB.isVision()) {
					continue;
				}
				int pX = pSB.getX();
				int pY = pSB.getY();
				Rect pLogicRect = pSB.getRect();
				pRect.set(pX+pLogicRect.left, pY+pLogicRect.top,
						pX+pLogicRect.right, pY+pLogicRect.bottom);
				if (pRect.contains(aX, aY)) {
					MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
					pSB.pressed();
					return true;
				}
			}
		}
		else {
			dimiss();
		}
		return true;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#pointerReleased(int, int)
	 */
	@Override
	public boolean pointerReleased(int aX, int aY) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#OnProcessCmd(java.lang.Object, int, int)
	 */
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
		MobileMsg pMobileMsg = (MobileMsg) socket;
		switch (aMobileType) {
		case IM.IM_RESPONSE:
		{
			switch (aMsgID) {
			case RspMsg.MSG_RSP_ROOM_LIST_C:
				break;
			default:
				break;
			}
		}
		break;
		default:
			break;
		}
	}
	
	protected void loadPak() {
		//详细资料界面
		iUIPak = CPakManager.loadInfoPak();//Project.loadProject(ActivityUtil.PATH_SCREEN+"gameshome_map.pak", false);
		iSceneDetail = iUIPak.getScene(iSceneID);
		if(iSceneDetail != null) {
			iSceneDetail.setViewWindow(0, 0, 800, 480);
			iSceneDetail.initDrawList();
			iSceneDetail.initNinePatchList(null);
			ViewRect = iSceneDetail.getLayoutMap(Scene.RectangleLayout).getNineRect(0);
			iSoffsetX = ActivityUtil.SCREEN_WIDTH - ViewRect.left;
			iSoX = iSoffsetX;
			iSoSpeed = (iSoX / 6);
			iSceneDetail.iCameraX = -iSoX;
//			iSoffsetY = ActivityUtil.SCREEN_HEIGHT - ViewRect.top;
//			iSoY = iSoffsetY;
//			iSoSpeedY = (iSoY / 6);
//			iSceneDetail.iCameraY = -iSoY;
			//
			Vector<?> pSpriteList = iSceneDetail.getLayoutMap(Scene.BuildLayout).getSpriteList();
			if(pSpriteList.size() >= 1) {
				iSexSprite = ((CBuilding)pSpriteList.elementAt(0));//性别动画
				iVipSprite = ((CBuilding)pSpriteList.elementAt(1));//性别动画
			}
			Map pMap = iSceneDetail.getLayoutMap(Scene.SpriteLayout);
			pSpriteList = pMap.getSpriteList();
			Sprite pSprite;
			iSpritebuttonListDetail.removeAllElements();
			for (int i=0; i<pSpriteList.size(); i++) {
				pSprite = (Sprite)pSpriteList.elementAt(i);
				int pX = pSprite.getX();
				int pY = pSprite.getY();
				int pEventID = GameEventID.ESPRITEBUTTON_EVENT_DETAILINFO_OFFSET + pSprite.getIndex();
				TPoint pPoint = new TPoint();
				pPoint.iX = pX;
				pPoint.iY = pY;
				//精灵按钮
				SpriteButton pSpriteButton;
				pSpriteButton = new SpriteButton(pSprite);
				pSpriteButton.setEventID(pEventID);
				pSpriteButton.setPosition(pX, pY);
				pSpriteButton.setHandler(this);
				if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_DETAILINFO_RENAME) {
					iRenameSpriteButton = pSpriteButton;
				}
				if(pEventID == GameEventID.ESPRITEBUTTON_EVENT_DETAILINFO_ADDFRIEND) {
					iAddFriendSpriteButton = pSpriteButton;
					iAddFriendSpriteButton.setVision(false);
				}
				iSpritebuttonListDetail.addElement(pSpriteButton);
			}
		}
		
		Project tSkillIconPak = CPakManager.loadSkillIconPak();
		if(tSkillIconPak != null) {
			iSkillZhaiSprite = tSkillIconPak.getSprite(0);;	//斋技能的图标
			iSkillLightnSprite = tSkillIconPak.getSprite(1);; //雷劈技能的图标
		}
	}

	public void setAmendButton(boolean aVision) {
		if(iRenameSpriteButton != null) iRenameSpriteButton.setVision(aVision);
	}
	
	/* 
	 * @see com.funoble.lyingdice.gamebase.GameButtonHandler#ItemAction(int)
	 */
	@Override
	public boolean ItemAction(int aEventID) {
		if(!isVision || !ibTouch) return false;
		switch (aEventID) {
		case GameEventID.ESPRITEBUTTON_EVENT_DETAILINFO_RENAME:
			ibRename = true;
			dimiss();
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_DETAILINFO_CLOSE:
			dimiss();
			break;
		case GameEventID.ESPRITEBUTTON_EVENT_DETAILINFO_ADDFRIEND:
			MyArStation.iGameProtocol.requestAddFriend(iPlayer.iUserID, 0);
			break;

		default:
			break;
		}
		return false;
	}

	public void loadScene(int aSceneId) {
		if(aSceneId > 1 || aSceneId < 0) {
			aSceneId = 0;
		}
		iSceneID = aSceneId;
		loadPak();
	}
	
	public interface onClickReameListen {
		void onClickRename();
	}
}
