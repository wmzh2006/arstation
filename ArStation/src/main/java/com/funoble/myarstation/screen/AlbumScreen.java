/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: Album.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-7-15 下午05:33:31
 *******************************************************************************/
package com.funoble.myarstation.screen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.common.Util;
import com.funoble.myarstation.data.cach.ImageDataManager;
import com.funoble.myarstation.game.CPlayer;
import com.funoble.myarstation.game.GameCanvas;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.game.R;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.imagemanager.CropImage;
import com.funoble.myarstation.imagemanager.CropImageView;
import com.funoble.myarstation.socket.GameProtocol;
import com.funoble.myarstation.socket.protocol.BufferSize;
import com.funoble.myarstation.socket.protocol.MBRspDelPic;
import com.funoble.myarstation.socket.protocol.MBRspLogin;
import com.funoble.myarstation.socket.protocol.MBRspPlayerInfoTwo;
import com.funoble.myarstation.socket.protocol.MBRspUpLoadPic;
import com.funoble.myarstation.socket.protocol.MBRspUpLoadShareText;
import com.funoble.myarstation.socket.protocol.MBRspVisitorLogin;
import com.funoble.myarstation.socket.protocol.MobileMsg;
import com.funoble.myarstation.socket.protocol.ProtocolType.IM;
import com.funoble.myarstation.socket.protocol.ProtocolType.ResultCode;
import com.funoble.myarstation.socket.protocol.ProtocolType.RspMsg;
import com.funoble.myarstation.sound.MediaManager;
import com.funoble.myarstation.sound.SoundsResID;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.view.GameView;
import com.funoble.myarstation.view.MessageEventID;


public class AlbumScreen extends GameView implements OnClickListener{


	private int	iGameState = 0; //pak在数据动画
	private Scene iSceneLoading = null;
	//资源
	private Project iUIPak = null;
	
	private Uri				originalUri;
	private boolean			isMyPhoto;
	private CPlayer			iPlayerInfo = null;
	
	private ByteBuffer 		iPicBuf;
	private int 			fileSize;
	private int				currentSize;
	private boolean  		bReturn = false;
	
	private String iFileName = null;
	
	//相册界面 
	private RelativeLayout rlAlbum;
	private RelativeLayout rlPhoto;
	private LinearLayout llMemu;
	
	private ImageView	imPhoto;
	private Button		btnReturn;
	private Button      btnFolder;
	private Button		btnCameral;
	//图片预览界面
	private RelativeLayout rlPreAlbum;
	private CropImageView cropImageView;
	private Button		btnCancel;
	private Button      btnUpDate;
	private CropImage 	mCrop;
	private Bitmap		resBitmap = null;
	private Bitmap		dstBitmap = null;
	private Context	 context = MyArStation.mGameMain.getApplicationContext();
	private RelativeLayout mainRelativeLayout;
	//动画
	AnimationSet set = new AnimationSet(true);
	Animation putup_in_animation;
	LayoutAnimationController controller;
	Animation putinAnimation;
	Animation putoutAnimation;
	Animation putupOutAnimation;
	private int iReturnType = 0;//0 主页
	//1个人信息
	//2好友信息
	private int iListPos = 0;
	private int iUserID = 0;
	
	private void initAnim() {
		putinAnimation = AnimationUtils.loadAnimation(context, R.anim.push_right_in);
		putoutAnimation = AnimationUtils.loadAnimation(context, R.anim.push_left_out);
		putupOutAnimation = AnimationUtils.loadAnimation(context, R.anim.push_up_out);
		putup_in_animation = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
		controller = AnimationUtils.loadLayoutAnimation(context, R.anim.push_up_in_layoutanim);
	}
	/**
	 * construct
	 */
	public AlbumScreen() {
		iPlayerInfo = MyArStation.mGameMain.iPlayer;
		iFileName = iPlayerInfo.szBigPicName;
	}
	
	/**
	 * construct
	 */
	public AlbumScreen(int aReturnType, CPlayer aPlayer , int aUserID, int pos) {
		if(aReturnType > 2 || aReturnType < 0) {
			aReturnType = 0;
		}
		iReturnType = aReturnType;
		iPlayerInfo = aPlayer;
		iFileName = iPlayerInfo.szBigPicName;
		iListPos = pos;
		iUserID = aUserID;
	}

	private void initAlbum() {
		rlAlbum = (RelativeLayout) mainRelativeLayout.findViewById(R.id.rlMyPhoto);
		rlPhoto = (RelativeLayout) mainRelativeLayout.findViewById(R.id.rlPhoto);
		llMemu = (LinearLayout) mainRelativeLayout.findViewById(R.id.llMenu);
		imPhoto = (ImageView) mainRelativeLayout.findViewById(R.id.imPhoto);
		btnReturn = (Button) mainRelativeLayout.findViewById(R.id.btn_return);
		btnReturn.setOnClickListener(this);
		btnFolder = (Button) mainRelativeLayout.findViewById(R.id.btn_file);
		btnFolder.setOnClickListener(this);
		btnCameral = (Button) mainRelativeLayout.findViewById(R.id.btn_cameral);
		btnCameral.setOnClickListener(this);
		btnCameral.setVisibility(View.INVISIBLE);
		btnFolder.setVisibility(View.INVISIBLE);
		btnReturn.setVisibility(View.INVISIBLE);
		llMemu.setVisibility(View.INVISIBLE);
		rlPhoto.setVisibility(View.INVISIBLE);
	}
	
	private void initPreAlbum() {
		rlPreAlbum = (RelativeLayout) mainRelativeLayout.findViewById(R.id.rlcropPhoto);
		cropImageView = (CropImageView)rlPreAlbum.findViewById(R.id.image);
		btnCancel = (Button) mainRelativeLayout.findViewById(R.id.cancel);
		btnCancel.setOnClickListener(this);
		btnUpDate = (Button) mainRelativeLayout.findViewById(R.id.crop);
		btnUpDate.setOnClickListener(this);
		mCrop = new CropImage(MyArStation.getInstance(), cropImageView);
	}
	
	private void AnimationOut() {
//		llMemu.startAnimation(putupOutAnimation);
//		rlPhoto.setVisibility(View.INVISIBLE);
		mainRelativeLayout.startAnimation(putoutAnimation);
//		llMemu.setVisibility(View.INVISIBLE);
		putoutAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				if(iReturnType == 2) {
					GameCanvas.getInstance().changeView(new FriendDetailScreen(iUserID, iListPos));
				}
				else if(iReturnType == 1) {
					GameCanvas.getInstance().changeView(new InfoScreen());
				}
				else {
					messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
				}
//				GameCanvas.getInstance().changeView(new HomeScreen());
			}
		});
	}
	/* 
	 * @see com.funoble.lyingdice.view.GameView#init()
	 */
	@Override
	public void init() {
		iUIPak = MyArStation.iPakManager.loadUIPak();//Project.loadProject(ActivityUtil.PATH_SCREEN+"gameshome_map.pak", false);
		if (iUIPak != null) {
			iSceneLoading = iUIPak.getScene(12);
			if(iSceneLoading != null) {
				iSceneLoading.setViewWindow(0, 0, 800, 480);
				iSceneLoading.initDrawList();
			}
		}
		messageEvent(MessageEventID.EMESSAGE_EVENT_TO_ALBUM);
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#releaseResource()
	 */
	@Override
	public void releaseResource() {
		iSceneLoading = null;
		iUIPak = null;
		messageEvent(MessageEventID.EMESSAGE_EVENT_REQ_DELETE_SYSCOM);
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#paintScreen(android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	public void paintScreen(Canvas g, Paint paint) {
		//载入数据
		if(iGameState == 0) {
			g.drawBitmap(MyArStation.iImageManager.loadLoginBack(),
					0,
					0,
					null);
			if(iSceneLoading != null) {
				iSceneLoading.paint(g, 0, 0);
//				g.drawText("正在读取数据", 278*ActivityUtil.ZOOM_X, 288*ActivityUtil.ZOOM_Y, ActivityUtil.mLoading);
			}
		}
		else {
			g.drawBitmap(MyArStation.iImageManager.iBackLoginBmp,
					0,
					0,
					null);
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#performL()
	 */
	@Override
	public void performL() {
		//载入数据
		if(iGameState == 0) {
			if(iSceneLoading != null) {
				iSceneLoading.handle();
			}
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(rlPreAlbum.getVisibility() == View.VISIBLE) {
				cropImageView.clear();
				mCrop.cropCancel();
				switchTab(true);
				setImage();
				return true;
			}
			else {
//				messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
				if(!bReturn) {
					bReturn = !bReturn;
					AnimationOut();
				}
					
				return true;
			}
		}
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onDown(android.view.MotionEvent)
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onLongPress(android.view.MotionEvent)
	 */
	@Override
	public boolean onLongPress(MotionEvent e) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#onSingleTapUp(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#pointerPressed(int, int)
	 */
	@Override
	public boolean pointerPressed(int aX, int aY) {
		return false;
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
		switch (msg.what) {
		case ResultCode.ERR_UPLOAD_PIC_HOLD:
			removeProgressDialog();
			Tools.showSimpleToast(MyArStation.mGameMain, Gravity.CENTER, (String) msg.obj, Toast.LENGTH_LONG);
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RspUpLoadPic:
			MBRspUpLoadPic rspUpLoadPic = (MBRspUpLoadPic) msg.obj;
			int upsize = rspUpLoadPic.iUploadSize;
//			upsize += currentSize;
			Tools.debug("fileSize" + fileSize + " upsize = " + upsize + " currentSize " + currentSize);
			if(fileSize > iPicBuf.position()) {
				byte[] data = getData(upsize);
				MyArStation.iGameProtocol.requestUploadPic(fileSize, currentSize, data);
//				showProgressDialog(R.string.Loading_String);
			}
			else {
				iFileName = rspUpLoadPic.iFileName;
//				deleteImage(Util.int2Long(iPlayerInfo.iUserId) + "");
				savePhoto();
				switchTab(true);
				setAlbumInfo();
				removeProgressDialog();
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_DELPIC:
			removeProgressDialog();
			MBRspDelPic rspDelPic = (MBRspDelPic) msg.obj;
			if(deleteImage(iPlayerInfo.szBigPicName)) {
				imPhoto.setImageBitmap(null);
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_PlayerInfoTwo:
			MBRspPlayerInfoTwo ipInfoTwo = (MBRspPlayerInfoTwo)msg.obj;
			iFileName = ipInfoTwo.iBigPicName;
			setImage();
			break;
		case MessageEventID.EMESSAGE_EVENT_SOCKET_DOWNLOADP_COMPLETE_PIC:
			String picNameString  = (String) msg.obj;
			if(picNameString != null && picNameString.equals(iPlayerInfo.szBigPicName)) {
				setAlbumInfo();
			}
			break;
		case MessageEventID.EMESSAGE_EVENT_TO_ALBUM:
			MyArStation.mGameMain.mainLayout.removeAllViews();
			mainRelativeLayout = (RelativeLayout) LayoutInflater.from(MyArStation.mGameMain.getApplicationContext()).inflate(R.layout.new_album, null);
			MyArStation.mGameMain.mainLayout.addView(mainRelativeLayout);
			initAnim();
			initAlbum();
			initPreAlbum();
			if(iReturnType == 2) {
				setImageOther();
			}
			else {
				setImage();
			}
			mainRelativeLayout.startAnimation(putinAnimation);
			rlPhoto.setVisibility(View.VISIBLE);
			llMemu.setVisibility(View.VISIBLE);
//			putinAnimation.setAnimationListener(new AnimationListener() {
//				
//				@Override
//				public void onAnimationStart(Animation animation) {
//				}
//				
//				@Override
//				public void onAnimationRepeat(Animation animation) {
//				}
//				
//				@Override
//				public void onAnimationEnd(Animation animation) {
//					llMemu.startAnimation(putup_in_animation);
//					llMemu.setVisibility(View.VISIBLE);
//					putup_in_animation.setAnimationListener(new AnimationListener() {
//						
//						@Override
//						public void onAnimationStart(Animation animation) {
//						}
//						
//						@Override
//						public void onAnimationRepeat(Animation animation) {
//						}
//						
//						@Override
//						public void onAnimationEnd(Animation animation) {
//							llMemu.setLayoutAnimation(controller);
//							llMemu.startLayoutAnimation();
							if(iReturnType == 2) {
								btnFolder.setVisibility(View.GONE);
								btnCameral.setVisibility(View.GONE);
							}
							else {
								btnFolder.setVisibility(View.VISIBLE);
								btnCameral.setVisibility(View.VISIBLE);
							}
							btnReturn.setVisibility(View.VISIBLE);
//						}
//					});
//				}
//			});
			mainRelativeLayout.setVisibility(View.VISIBLE);
			iGameState = -1;
			break;
		default:
			break;
		}
	}

	/* 
	 * @see com.funoble.lyingdice.view.GameView#OnProcessCmd(java.lang.Object, int, int)
	 */
	@Override
	public void OnProcessCmd(Object socket, int aMobileType, int aMsgID) {
		GameProtocol gameProtocol = MyArStation.iGameProtocol;
		MobileMsg pMobileMsg = (MobileMsg) socket;
		switch (aMobileType) {
		case IM.IM_NOTIFY:
		{
			switch (aMsgID) {
//				case ReqMsg.MSG_NOTIFY_EVENT:
//					
//					break;
			
			default:
				break;
			}
		}
		break;
		case IM.IM_RESPONSE:
		{
			switch (aMsgID) {
			case RspMsg.MSG_RSP_UPLOAD_PIC:
				MBRspUpLoadPic rspUpdUpLoadPic = (MBRspUpLoadPic)pMobileMsg.m_pMsgPara;
				if(rspUpdUpLoadPic == null) {
					return;
				}
				Tools.debug(rspUpdUpLoadPic.toString());
				if(rspUpdUpLoadPic.nResult == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspUpLoadPic, rspUpdUpLoadPic);
				}
				else {
					messageEvent(rspUpdUpLoadPic.nResult, rspUpdUpLoadPic.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_UPLOAD_SHARETEXT:
				MBRspUpLoadShareText rspUpLoadShareText= (MBRspUpLoadShareText)pMobileMsg.m_pMsgPara;
				if(rspUpLoadShareText == null) {
					return;
				}
				Tools.debug(rspUpLoadShareText.toString());
				if(rspUpLoadShareText.nResult == ResultCode.SUCCESS) {
//					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RspUpLoadPic, rspUpdUpLoadPic);
					upLoadImage();
				}
				else {
					messageEvent(rspUpLoadShareText.nResult, rspUpLoadShareText.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_DEL_PIC:
				MBRspDelPic rspDelPic = (MBRspDelPic)pMobileMsg.m_pMsgPara;
				if(rspDelPic == null) {
					return;
				}
				Tools.debug(rspDelPic.toString());
				if(rspDelPic.nResult == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_RSP_DELPIC, rspDelPic);
					upLoadImage();
				}
				else {
					messageEvent(rspDelPic.nResult, rspDelPic.iMsg);
				}
				break;
			case RspMsg.MSG_RSP_PLAYINFO_TWO:
				removeProgressDialog();
				MBRspPlayerInfoTwo rspMainPage = (MBRspPlayerInfoTwo)pMobileMsg.m_pMsgPara;
				if(rspMainPage == null) {
					break;
				}
				Tools.debug(rspMainPage.toString());
				int type = rspMainPage.iResult;
				if(type == ResultCode.SUCCESS) {
					messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_PlayerInfoTwo, rspMainPage);
				}
				else {
					messageEvent(type, rspMainPage.iMsg);
				}
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

	private void loadImageFromCamera() {
		
		if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			Toast.makeText(MyArStation.mGameMain, R.string.create_no_sdcard,
					Toast.LENGTH_LONG).show();
			return; 
		}
		try {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			File tmpFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
					System.currentTimeMillis() + ".jpg");
			// 获取这个图片的URI
			originalUri = Uri.fromFile(tmpFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);
			MyArStation.getInstance().startActivityForResult(intent, MessageEventID.EMESSAGE_EVENT_REQUEST_CAMERA_IMAGE);
		}
		catch (Exception e) {
		}
	}
	
	public void onGetImage(Uri data, int from, byte[] picData) {
		Tools.debug("onGetImage, data:" + data + ",from:" + from);
		ContentResolver resolver = MyArStation.mGameMain.getContentResolver();
		Bitmap bm = null;
		try {
			final int MAX_SIZE = 400; // 超过了400，要按比例压缩
			System.gc();
			InputStream is = null;
			if (from ==  MessageEventID.EMESSAGE_EVENT_REQ_LOADPHOTOFROMALBUM) {
				if (data != null) {
					is = (FileInputStream) resolver.openInputStream(Uri.parse(data.toString()));
				}
			}
			if (originalUri != null) {
				Tools.debug("originalUri:" + originalUri.toString());
				is = (FileInputStream) resolver.openInputStream(originalUri);
			}
			else if (picData != null && picData.length > 0) {
				Tools.debug("from picData:" + picData.length);
				is = new ByteArrayInputStream(picData);
			}
			if (is != null) {
				Options options = new Options();
				options.inJustDecodeBounds = true;
				// 获取这个图片的宽和高
				BitmapFactory.decodeStream(is, null, options); // 此时返回bm为空
				options.inJustDecodeBounds = false;
				// 计算缩放比
				int max = Math.max(options.outWidth, options.outHeight);
				int be = (int) ((float) max / (float) MAX_SIZE);
				if (be <= 0) {
					be = 1;
				}
				if (be > 1) {
//					Toast.makeText(getContext(), R.string.compress_tip, Toast.LENGTH_LONG).show();
				}
				options.inSampleSize = be;
				// 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
				if (from == MessageEventID.EMESSAGE_EVENT_REQ_LOADPHOTOFROMALBUM) {
					if (data != null) {
						is = (FileInputStream) resolver.openInputStream(Uri.parse(data.toString()));
					}
				}
				if (originalUri != null) {
					is = (FileInputStream) resolver.openInputStream(originalUri);
				}
				else if (picData != null && picData.length > 0) {
					Tools.debug("from picData:" + picData.length);
					is = new ByteArrayInputStream(picData);
				}
				bm = BitmapFactory.decodeStream(is, new Rect(), options);
			}
			
			if (bm != null) {
				// 压缩图片
				int max = Math.max(bm.getWidth(), bm.getHeight());
				if (max > MAX_SIZE) {
					boolean isBasedWidth = false;
					if (bm.getWidth() > bm.getHeight()) {
						isBasedWidth = true;
					}
					int detaWidth = 0;
					int detaHeight = 0;
					if (isBasedWidth) {
						detaWidth = MAX_SIZE;
						float rate = MAX_SIZE / (float) bm.getWidth();
						detaHeight = (int) (bm.getHeight() * rate);
					}
					else {
						detaHeight = MAX_SIZE;
						float rate = (float) MAX_SIZE / (float) bm.getHeight();
						detaWidth = (int) (bm.getWidth() * rate);
					}
					bm = Bitmap.createScaledBitmap(bm, detaWidth, detaHeight, true);
				}
				
				if (bm != null) {
					resBitmap = bm;
					cropImageView.setImageBitmap(bm);
					cropImageView.setImageBitmapResetBase(bm, true);
					cropImageView.setVisibility(View.VISIBLE);
					mCrop.crop(resBitmap);
				}
			}
		}
		catch (Exception e) {
		}
		finally {
			if (bm == null) {
				Tools.showSimpleToast(context, Gravity.CENTER, "读取图片失败！", Toast.LENGTH_LONG);
				switchTab(true);
			}
		}
	}

	/* 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		MediaManager.getMediaManagerInstance(MediaManager.OTHER_SOUND_ID).playerSound(SoundsResID.bn_pressed, 0);
		int id = v.getId();
		if (id == R.id.btn_cameral) {
			cropImageView.clear();
			switchTab(false);
			loadImageFromCamera();
		} else if (id == R.id.btn_file) {
			cropImageView.clear();
			switchTab(false);
			loadImageFromAlbums();
		} else if (id == R.id.crop) {
			upLoadImage();
		} else if (id == R.id.cancel) {
			cropImageView.clear();
			mCrop.cropCancel();
			switchTab(true);
		} else if (id == R.id.btn_return) {
			//			messageEvent(MessageEventID.EMESSAGE_EVENT_TO_HOME);
			if(!bReturn) {
				bReturn = !bReturn;
				AnimationOut();
			}
		} else {
		}
	}
	
	private void switchTab(boolean aMyPhoto) {
		this.isMyPhoto = aMyPhoto;
		rlAlbum.setVisibility(isMyPhoto ? View.VISIBLE : View.GONE);
		rlPreAlbum.setVisibility(isMyPhoto ? View.GONE : View.VISIBLE);
	}
	
	private void savePhoto() {
//		Drawable drawable = ivPhotoPreview.getDrawable();
		if (dstBitmap != null) {
//			try {
//				Bitmap upfile = dstBitmap;
			MyArStation.iHttpDownloadManager.saveImage(Bitmap2Bytes(dstBitmap), Util.getFileNameNotExt(iFileName));
//				ImageDataManager.saveImage2SDCard(Bitmap2Bytes(upfile), iFileName);
//			}
//			catch (OutOfMemoryError e)
//			{
//				Tools.debug("get pic. OutOfMemoryError");
//				// 再试一次
//				try
//				{
//					Bitmap upfile = dstBitmap;
//					ImageDataManager.saveImage2SDCard(Bitmap2Bytes(upfile),iFileName);
//				}
//				catch (Exception e2)
//				{
//				}
//			}
//			catch (Exception e)
//			{
//			}
		}
	}
	
	private byte[] Bitmap2Bytes(Bitmap bm) {
		byte[] data = null;
		if (bm != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try
			{
				bm.compress(Bitmap.CompressFormat.JPEG, 95, out);
				out.flush();
				data = out.toByteArray();
//				String strBM = new String(chars);
//				value.put("Picture", strBM);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					out.close();
				}
				catch (Exception e)
				{
				}
			}
		}
		return data;
	}
	
	private boolean deleteImage(String fileName) {
		return ImageDataManager.deleteImage(fileName);
	}
	
	private boolean setImage() {
		boolean result = false;
//		long userid = Util.int2Long(iPlayerInfo.iUserId);
		Bitmap bitmap = MyArStation.iHttpDownloadManager.getImage(iFileName);
		MyArStation.iPlayer.szBigPicName = iFileName;
		MyArStation.iPlayer.szLittlePicName = Util.replace(".jpg", "_h.jpg", iFileName);
//		System.out.println("GameMain.iPlayer.szLittlePicName = " + GameMain.iPlayer.szLittlePicName + " GameMain.iPlayer.szBigPicName = " + GameMain.iPlayer.szBigPicName);
		if(bitmap != null) {
			imPhoto.setImageBitmap(bitmap);
			result = false;
		}
		return result;
	}
	
	private boolean setImageOther() {
		boolean result = false;
//		long userid = Util.int2Long(iPlayerInfo.iUserId);
		Bitmap bitmap = MyArStation.iHttpDownloadManager.getImage(iPlayerInfo != null ? iPlayerInfo.szBigPicName : "");
//		System.out.println("GameMain.iPlayer.szLittlePicName = " + GameMain.iPlayer.szLittlePicName + " GameMain.iPlayer.szBigPicName = " + GameMain.iPlayer.szBigPicName);
		if(bitmap != null) {
			imPhoto.setImageBitmap(bitmap);
			result = false;
		}
		return result;
	}
	
	private Bitmap getBitmapFromImageView(ImageView aImageView) {
		Bitmap bitmap = null;
		BitmapDrawable bd = (BitmapDrawable) aImageView.getDrawable();
		if(bd != null) {
			bitmap = bd.getBitmap();
		}
		return bitmap;
	}
	
	private void setAlbumInfo() {
		if(iReturnType == 2) {
			setImageOther();
		}
		else {
			setImage();
		}
	}
	
	private void upLoadImage() {
		dstBitmap = mCrop.cropAndSave(resBitmap);
		byte[] buffer = Bitmap2Bytes(dstBitmap);
		if(buffer != null) {
			fileSize = buffer.length;
			iPicBuf = ByteBuffer.allocate(fileSize + 2);
			iPicBuf.put(buffer);
//			GameMain.iGameProtocol.create(Url.ip, Url.port);
			iPicBuf.position(0);
			byte[] data = getData(0);
			MyArStation.iGameProtocol.requestUploadPic(fileSize, currentSize, data);
			showProgressDialog(R.string.Loading_String);
		}
	}
	

	public byte[] getData(int aUploadSize) {
		byte[] data;
		if((fileSize - iPicBuf.position()) >= BufferSize.MAX_UPLOAD_BLOCK_LEN) {
			currentSize = BufferSize.MAX_UPLOAD_BLOCK_LEN;
			data = new byte[currentSize];
		}
		else {
			currentSize = fileSize - iPicBuf.position();
			data = new byte[currentSize];
		}
//		iPicBuf.position(aUploadSize);
//		Tools.debug("////currentSize " + currentSize);
//		Tools.debug("iPicBuf.position.... " + iPicBuf.position());
		iPicBuf.get(data, 0, currentSize);
//		Tools.debug("iPicBuf.position " + iPicBuf.position());
		return data;
	}
	
	private void requestDelPic() {
		MyArStation.iGameProtocol.requestDelPic(0);
		showProgressDialog(R.string.Loading_String);
	}
	
	private void requestPlayerInfo() {
		MBRspLogin login = (MBRspLogin)MyArStation.iGameProtocol.getMobileMsgCS(RspMsg.MSG_RSP_LOGIN);
		MBRspVisitorLogin visitorlogin = (MBRspVisitorLogin)MyArStation.iGameProtocol.getMobileMsgCS(RspMsg.MSG_RSP_VISITORLOGIN);
		int userid = login != null ? login.iUserId : (visitorlogin != null ? visitorlogin.iUserId : 0);
//		if(login != null || visitorlogin != null) {
			if(MyArStation.iGameProtocol.requestPlayerInfoTwo(userid)) {
//				messageEvent(MessageEventID.EMESSAGE_EVENT_SOCKET_ShowMainPageLoading);
				showProgressDialog(R.string.Loading_String);
			}
//		}
	}
	
	private void loadImageFromAlbums() {
		try {
			Intent mIntent = new Intent(Intent.ACTION_GET_CONTENT);
			mIntent.addCategory(Intent.CATEGORY_OPENABLE);
			mIntent.setType("image/*");
			MyArStation.getInstance().startActivityForResult(mIntent, MessageEventID.EMESSAGE_EVENT_REQ_LOADPHOTOFROMALBUM);
		}
		catch (Exception e) {
//			Toast.makeText(getContext(), R.string.store_load_albums_fault, Toast.LENGTH_LONG).show();
		}
	}
}
