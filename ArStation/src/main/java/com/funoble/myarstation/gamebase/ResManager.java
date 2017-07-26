package com.funoble.myarstation.gamebase;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Dialog;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.utils.ActivityUtil;


/**
 * Title: 资源管理器 下载与维护资源
 * Description: 
 * <p>Copyright: 腾讯科技(深圳)有限公司.</p>
 * @author cheerluo
 */
public class ResManager {
	
	//资源根目录标记
	public final static String rootPath = "/";
	
	//UI包
	public final static String uiPak = "/ui.pak";
	
	//本地包
	public final static String localPak = "/local.pak";
	
	//公共包
	public final static String commPak = "/comm.pak";
	
	//游戏公共包
	public final static String gameCommPak = "gameComm.pak";

	// 数据类型
	public static final byte TYPE_STRING = 0, TYPE_INT = 1, TYPE_SHORT = 2,
			TYPE_BYTE = 3, TYPE_BOOLEAN = 4;

	// 库表类型
	public static final byte FILE = 0, CLIP = 1, FRAME = 2, ACTION = 3,SPRITE = 4, SCENE = 5, TABLEDATA = 6;
	
	/**
	 * 从资源库中创建指定id序列的图片
	 * @param imageID
	 * @param libName
	 * @return
	 */
//	public static Bitmap[] createImageFromLib(int[] imageID, String libName) {
//		//#mdebug info
//		String str = "createImageFromLib libName:" + libName + ", mum:" + imageID.length + " <";
//		for (int i = 0; i < imageID.length; i++) {
//			str = str + imageID[i] + ",";
//		}
//		str = str + ">";
//		System.out.println(str);
//		//#enddebug
//		Image[] images = new Image[imageID.length];
//		byte[][] data = (byte[][]) getDataFromLib(imageID, libName, FILE);
//		if (data != null) {
//			for (int i = 0; i < images.length; i++) {
//				//#if polish.showUserMemory
//				try {
//				//#endif
//					images[i] = Image.createImage(data[i], 0, data[i].length);
//					data[i]=null;
//				//#if polish.showUserMemory
//				} catch (Exception e) {
//					e.printStackTrace();
//					Dialog.showSimpleDialog("createImage异常[ id="+imageID[i]+" "+libName+" "+e+" ] at "+QQGameSystem.getRoot()).show(QQGameSystem.getInstance());
//				}
//				//#endif
//				
//			}
//		}
//		return images;
//	}
	
	/**
	 * 从资源库中创建指定id序列的图片数组
	 * @param imageID
	 * @param libName
	 * @return
	 */
//	public static ImageItem[] createImageItemFromLib(int[] imageID, String libName) {
//		Image[] list = createImageFromLib(imageID, libName);
//		ImageItem[] ret = null;
//		if (list != null) {
//			ret = new ImageItem[list.length];
//			for (int i = 0; i < list.length; i++) {
//				ret[i] = new ImageItem();
//				ret[i].initImage(list[i], 1);
//			}
//		}
//		return ret;
//	}
	

//	private static Hashtable bufferDataTable=new Hashtable();//资源数据缓冲
	//读取文件,并返回byte数组
	public static byte[] getDatafromFile(String libName) {
		//内存紧缺，所以在载入文件时，先叫系统回收内存
		System.gc();
		byte[] bufferData = null;
		if(bufferData==null) {
			InputStream tempis =(new Object()).getClass().getResourceAsStream(libName);
			try {
				int presize=tempis.available();
				if(presize<=0){
					presize=40*1024;
				}
				bufferData=new byte[presize];
				int ch=0;
				int i=0;
				while((ch=tempis.read())!=-1){
					if(i>=bufferData.length){						
						byte[] data=new byte[bufferData.length+10*1024];
						System.arraycopy(bufferData, 0, data, 0, bufferData.length);
						bufferData=data;
						data = null;
					}
					bufferData[i]=(byte)ch;
					i++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//#debug info
				e.printStackTrace();
			}
			finally{
				try {
					tempis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//#debug info
					e.printStackTrace();
				}
			}
		}
		return bufferData;
	}
	
	//读取文件，并返回InputStream
	public static InputStream getDataInputStream(String libName){
		//内存紧缺，所以在载入文件时，先叫系统回收内存
		System.gc();
		InputStream iStream =null;	
			try {
				iStream = ActivityUtil.resources.getAssets().open(libName);
			} catch (IOException e1) {
				e1.printStackTrace();
				try {
					if(iStream != null)iStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		return iStream;
	}
	
	//读取文件，并返回InputStream
	/**
	 * @param libName
	 * @param asset 是否重assets载入数据
	 * @return
	 */
	@SuppressWarnings("unused")
	public static InputStream getDataInputStream(String libName, boolean asset){
		//内存紧缺，所以在载入文件时，先叫系统回收内存
		System.gc();
		InputStream iStream =null;	
		try {
			if(asset) {
				iStream = ActivityUtil.resources.getAssets().open(libName);
			}
			else {
				iStream = new FileInputStream(libName);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			try {
				if(iStream != null)iStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return iStream;
	}
//	//读取文件，并返回InputStream
//	/**
//	 * @param libName
//	 * @param asset 是否重assets载入数据
//	 * @return
//	 */
//	public static InputStream getDataInputStream(String libName, boolean asset){
//		//内存紧缺，所以在载入文件时，先叫系统回收内存
//		System.gc();
//		InputStream iStream =null;	
//		byte[] bufferData = null;
//		if(bufferData==null) {
//			InputStream tempis = null;
//			try {
//				if(asset) {
//					tempis = ActivityUtil.resources.getAssets().open(libName);
//				}
//				else {
//					tempis = new FileInputStream(libName);
//				}
//				if (tempis == null) {
//					return null;
//				}
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			try {
//				if (tempis == null) {
//					return null;
//				}
//				int presize=tempis.available();
//				if(presize<=0){
//					presize=40*1024;
//				}
//				bufferData=new byte[presize];
//				int ch=0;
//				int i=0;
//				while((ch=tempis.read())!=-1){
//					if(i>=bufferData.length){						
//						byte[] data=new byte[bufferData.length+10*1024];
//						System.arraycopy(bufferData, 0, data, 0, bufferData.length);
//						bufferData=data;
//						data = null;
//					}
//					bufferData[i]=(byte)ch;
//					i++;
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			finally{
//				try {
//					if (tempis == null) {
//						return null;
//					}
//					tempis.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					//#debug info
//					e.printStackTrace();
//				}
//			}
//			//#debug info
//			Tools.println("ResManager （jar包有该资源）getInputStream from bufferData 大小= " + bufferData.length);
//			//内存缺少，暂时不存入哈希表
////			bufferDataTable.put(libName, bufferData);
//		}
//		if(bufferData!=null) {
//			iStream = new ByteArrayInputStream(bufferData);
//			Tools.println("ResManager getInputStream from File = " + libName);
//		}
//		return iStream;
//	}
//	
	/**
	 * 获取指定索引的资源数据
	 * @param index
	 * @param libName
	 * @return
	 */
//	public static byte[][] getDataFromLib(int[] fileIDList, String libName,int type) {
//		if(fileIDList==null||fileIDList.length==0){
//			//#debug info
//			System.out.println("no need to getDataFromLib libName:"+libName+",fileIDList:"+(fileIDList==null?(fileIDList+""):(fileIDList.length+"")));
//			return null;
//		}
//		
//		InputStream is=null;
//		is=getDataInputStream(libName);
//		
////		if (isDataExistInJar(libName)) {
////			is=getDataInputStream(libName);
////		}
//////		if(libName.startsWith("/")){//以斜杆开头的资源存在本地jar中
//////			is=getDataInputStream(libName);
//////		}
////		//#if polish.DownloadRes_Model
////		else{//其他资源存在rms中
////			final int rmsIndexFinal=0;
////			final String libNameFinal=libName;
////			is=getDataInputStream(libName);
//////			byte[][] data=loadDataFromRms(getSubRmsFlag(libNameFinal,rmsIndexFinal));
//////			if(data!=null){
//////				is=new ByteArrayInputStream(data[0]){
//////					int rmsIndex=rmsIndexFinal;
//////					public int read(){
//////						if(pos < count){
//////							return super.read();
//////						}
//////						else{
//////							this.buf=null;
//////							this.buf=loadDataFromRms(getSubRmsFlag(libNameFinal,++rmsIndex))[0];
//////							if(this.buf==null)return -1;
//////					        this.pos = 0;
//////					        this.count = buf.length;
//////							return super.read();
//////						}
//////					}
//////				    
//////				    public long skip(long n) {
//////				    	int avail=this.available();
//////				    	if(avail>=n){
//////				    		super.skip(n);
//////				    	}
//////				    	else{
//////				    		super.skip(avail);
//////				    		read();
//////				    		skip(n-avail-1);
//////				    	}
//////				        return n;
//////				    }
//////				    public int read(byte b[], int off, int len) {
//////				    	int avail=this.available();
//////				    	if(avail>=len){
//////				    		super.read(b,  off,  len);
//////				    	}
//////				    	else{
//////				    		super.read(b,  off,  avail);
//////				    		b[off+avail]=(byte)read();
//////				    		read(b,  off+avail+1,  len-avail-1);
//////				    	}
//////				        return len;
//////				    }
//////				};
//////			}
//////			else{
//////				return null;
//////			}
////		}
////		//#endif
//
//		DataInputStream dis = new DataInputStream(is);
//		try {
//			byte[][] ret = null;
//			if(fileIDList!=null)ret=new byte[fileIDList.length][];
//			// 尺寸
//			int resSize = dis.readInt();
//			
//			// 编辑器版本号
//			int Edition_Core_Version = dis.readUnsignedByte();
//			int Edition_Function_Version = dis.readUnsignedByte();
//			
//			// Project类型
//			int Project_Type = 0;
//			if(Edition_Core_Version == 2 && Edition_Function_Version >= 5) {
//				Project_Type = dis.readByte();
//			}
//			
//			// Project属性
//			int project_Property = 0;
//			if(Edition_Core_Version == 2 && Edition_Function_Version >= 6) {
//				project_Property = dis.readByte();
//			}
//			
//			// Project 版本号
//			int Project_Core_Version=dis.readUnsignedByte();
//			int Project_Function_Version=dis.readUnsignedByte();
//		
//		    String Project_Description=dis.readUTF();
//		    
//			// 资源表
//		    int freshFrequency=dis.readByte();//刷新频率
//			int resNum = dis.readByte();// 资源数量
//			
//			//#debug info
//			System.out.println("libName:"+libName+", resSize:"+resSize+",Edition_Core_Version:"+Edition_Core_Version+",Edition_Function_Version"+Edition_Function_Version+
//					",Project_Core_Version:"+Project_Core_Version+",Project_Function_Version:"+Project_Function_Version+",Project_Description:"+Project_Description+
//					",resNum:"+resNum
//			);
//			
//			int readCount = 0;
//			for (int n = 0; n < resNum; n++) {
//				int size = dis.readShort();		
//				////#debug info
//				//System.out.println("img num:"+size+" , in:"+libName);
//				for (int i = 0; i < size; i++) {
//					switch (n) {
//					case FILE: {// 文件资源
//						int idPosition = UtilTools.findIndexInArray(i, fileIDList);
//						if (idPosition >= 0&&type==n) {// 是需要的资源
//							byte[] data = (byte[]) readArray(dis, 0, TYPE_BYTE);	
//							ret[idPosition] = data;
//							readCount++;
//							////#debug info
//							//System.out.println(i+" img idPosition:"+idPosition+" , size:"+data.length);
//							if (readCount == fileIDList.length) {
//								return ret;
//							}
//						}
//						else{
//							int nc = dis.readShort()&0x00ffff;
//							if(nc==0xffff){//大文件，超过64K
//								nc=dis.readInt();
//							}
//							////#debug info
//							//System.out.println(i+" img idPosition:"+idPosition+" , skip:"+nc);
//							dis.skip(nc);
//						}
//						break;
//					}
//					}
//				}
//			}
//		} catch (Exception e) {
//			//#debug info
//			e.printStackTrace();
//		} finally {
//			try {
//				dis.close();
//				dis = null;
//				is.close();
//				is = null;
//			} catch (Exception e) {
//			}
//		}
//		return null;
//	}
	
	
//	/**
//	 * 获取指定索引的资源数据
//	 * @param index
//	 * @param libName
//	 * @return
//	 */
//	public static byte[][] getDataFromLib(int[] fileIDList, String libName,int type) {
//		if(fileIDList==null||fileIDList.length==0){
//			//#debug info
//			System.out.println("no need to getDataFromLib libName:"+libName+",fileIDList:"+(fileIDList==null?(fileIDList+""):(fileIDList.length+"")));
//			return null;
//		}
//		//#if !polish.DownloadRes_Model
////		if(!libName.startsWith("/")){
////			libName="/"+libName;
////		}
//		//#endif
//		
//		InputStream is=null;
//		if(libName.startsWith("/")){//以斜杆开头的资源存在本地jar中
//			is=getDataInputStream(libName);
//		}
//		//#if polish.DownloadRes_Model
//		else{//其他资源存在rms中
//			final int rmsIndexFinal=0;
//			final String libNameFinal=libName;
//			is=getDataInputStream(libName);
//			MsgHandle.pak = null;
////			byte[][] data=loadDataFromRms(getSubRmsFlag(libNameFinal,rmsIndexFinal));
////			if(data!=null){
////				is=new ByteArrayInputStream(data[0]){
////					int rmsIndex=rmsIndexFinal;
////					public int read(){
////						if(pos < count){
////							return super.read();
////						}
////						else{
////							this.buf=null;
////							this.buf=loadDataFromRms(getSubRmsFlag(libNameFinal,++rmsIndex))[0];
////							if(this.buf==null)return -1;
////					        this.pos = 0;
////					        this.count = buf.length;
////							return super.read();
////						}
////					}
////				    
////				    public long skip(long n) {
////				    	int avail=this.available();
////				    	if(avail>=n){
////				    		super.skip(n);
////				    	}
////				    	else{
////				    		super.skip(avail);
////				    		read();
////				    		skip(n-avail-1);
////				    	}
////				        return n;
////				    }
////				    public int read(byte b[], int off, int len) {
////				    	int avail=this.available();
////				    	if(avail>=len){
////				    		super.read(b,  off,  len);
////				    	}
////				    	else{
////				    		super.read(b,  off,  avail);
////				    		b[off+avail]=(byte)read();
////				    		read(b,  off+avail+1,  len-avail-1);
////				    	}
////				        return len;
////				    }
////				};
////			}
////			else{
////				return null;
////			}
//		}
//		//#endif
//
//		DataInputStream dis = new DataInputStream(is);
//		try {
//			byte[][] ret = null;
//			if(fileIDList!=null)ret=new byte[fileIDList.length][];
//			// 尺寸
//			int resSize = dis.readInt();
//			
//			// 编辑器版本号
//			int Edition_Core_Version = dis.readUnsignedByte();
//			int Edition_Function_Version = dis.readUnsignedByte();
//			
//			// Project类型
//			int Project_Type = 0;
//			if(Edition_Core_Version == 2 && Edition_Function_Version >= 5) {
//				Project_Type = dis.readByte();
//			}
//			
//			// Project属性
//			int project_Property = 0;
//			if(Edition_Core_Version == 2 && Edition_Function_Version >= 6) {
//				project_Property = dis.readByte();
//			}
//			
//			// Project 版本号
//			int Project_Core_Version=dis.readUnsignedByte();
//			int Project_Function_Version=dis.readUnsignedByte();
//		
//		    String Project_Description=dis.readUTF();
//		    
//			// 资源表
//		    int freshFrequency=dis.readByte();//刷新频率
//			int resNum = dis.readByte();// 资源数量
//			
//			//#debug info
//			System.out.println("libName:"+libName+", resSize:"+resSize+",Edition_Core_Version:"+Edition_Core_Version+",Edition_Function_Version"+Edition_Function_Version+
//					",Project_Core_Version:"+Project_Core_Version+",Project_Function_Version:"+Project_Function_Version+",Project_Description:"+Project_Description+
//					",resNum:"+resNum
//			);
//			
//			int readCount = 0;
//			for (int n = 0; n < resNum; n++) {
//				int size = dis.readShort();		
//				////#debug info
//				//System.out.println("img num:"+size+" , in:"+libName);
//				for (int i = 0; i < size; i++) {
//					switch (n) {
//					case FILE: {// 文件资源
//						int idPosition = UtilTools.findIndexInArray(i, fileIDList);
//						if (idPosition >= 0&&type==n) {// 是需要的资源
//							byte[] data = (byte[]) readArray(dis, 0, TYPE_BYTE);	
//							ret[idPosition] = data;
//							readCount++;
//							////#debug info
//							//System.out.println(i+" img idPosition:"+idPosition+" , size:"+data.length);
//							if (readCount == fileIDList.length) {
//								return ret;
//							}
//						}
//						else{
//							int nc = dis.readShort()&0x00ffff;
//							if(nc==0xffff){//大文件，超过64K
//								nc=dis.readInt();
//							}
//							////#debug info
//							//System.out.println(i+" img idPosition:"+idPosition+" , skip:"+nc);
//							dis.skip(nc);
//						}
//						break;
//					}
//					}
//				}
//			}
//		} catch (Exception e) {
//			//#debug info
//			e.printStackTrace();
//		} finally {
//			try {
//				dis.close();
//				dis = null;
//				is.close();
//				is = null;
//			} catch (Exception e) {
//			}
//		}
//		return null;
//	}
	
	
//	/**
//	 * 获取指定索引的资源数据
//	 * @param index
//	 * @param libName
//	 * @return
//	 */
//	public static byte[][] getDataFromLib(int[] fileIDList, String libName,int type) {
//		if(fileIDList==null||fileIDList.length==0){
//			//#debug info
//			System.out.println("no need to getDataFromLib libName:"+libName+",fileIDList:"+(fileIDList==null?(fileIDList+""):(fileIDList.length+"")));
//			return null;
//		}
//		//#if !polish.DownloadRes_Model
//		if(!libName.startsWith("/")){
//			libName="/"+libName;
//		}
//		//#endif
//		
//		InputStream is=null;
//		if(libName.startsWith("/")){//以斜杆开头的资源存在本地jar中
//			is=getDataInputStream(libName);
//		}
//		//#if polish.DownloadRes_Model
//		else{//其他资源存在rms中
//			final int rmsIndexFinal=0;
//			final String libNameFinal=libName;
//			byte[][] data=loadDataFromRms(getSubRmsFlag(libNameFinal,rmsIndexFinal));
//			if(data!=null){
//				is=new ByteArrayInputStream(data[0]){
//					int rmsIndex=rmsIndexFinal;
//					public int read(){
//						if(pos < count){
//							return super.read();
//						}
//						else{
//							this.buf=null;
//							this.buf=loadDataFromRms(getSubRmsFlag(libNameFinal,++rmsIndex))[0];
//							if(this.buf==null)return -1;
//					        this.pos = 0;
//					        this.count = buf.length;
//							return super.read();
//						}
//					}
//				    
//				    public long skip(long n) {
//				    	int avail=this.available();
//				    	if(avail>=n){
//				    		super.skip(n);
//				    	}
//				    	else{
//				    		super.skip(avail);
//				    		read();
//				    		skip(n-avail-1);
//				    	}
//				        return n;
//				    }
//				    public int read(byte b[], int off, int len) {
//				    	int avail=this.available();
//				    	if(avail>=len){
//				    		super.read(b,  off,  len);
//				    	}
//				    	else{
//				    		super.read(b,  off,  avail);
//				    		b[off+avail]=(byte)read();
//				    		read(b,  off+avail+1,  len-avail-1);
//				    	}
//				        return len;
//				    }
//				};
//			}
//			else{
//				return null;
//			}
//		}
//		//#endif
//
//		DataInputStream dis = new DataInputStream(is);
//		try {
//			byte[][] ret = null;
//			if(fileIDList!=null)ret=new byte[fileIDList.length][];
//			// 尺寸
//			int resSize = dis.readInt();
//			
//			// 编辑器版本号
//			int Edition_Core_Version = dis.readUnsignedByte();
//			int Edition_Function_Version = dis.readUnsignedByte();
//			
//			// Project类型
//			int Project_Type = 0;
//			if(Edition_Core_Version == 2 && Edition_Function_Version >= 5) {
//				Project_Type = dis.readByte();
//			}
//			
//			// Project属性
//			int project_Property = 0;
//			if(Edition_Core_Version == 2 && Edition_Function_Version >= 6) {
//				project_Property = dis.readByte();
//			}
//			
//			// Project 版本号
//			int Project_Core_Version=dis.readUnsignedByte();
//			int Project_Function_Version=dis.readUnsignedByte();
//		
//		    String Project_Description=dis.readUTF();
//		    
//			// 资源表
//		    int freshFrequency=dis.readByte();//刷新频率
//			int resNum = dis.readByte();// 资源数量
//			
//			//#debug info
//			System.out.println("libName:"+libName+", resSize:"+resSize+",Edition_Core_Version:"+Edition_Core_Version+",Edition_Function_Version"+Edition_Function_Version+
//					",Project_Core_Version:"+Project_Core_Version+",Project_Function_Version:"+Project_Function_Version+",Project_Description:"+Project_Description+
//					",resNum:"+resNum
//			);
//			
//			int readCount = 0;
//			for (int n = 0; n < resNum; n++) {
//				int size = dis.readShort();		
//				////#debug info
//				//System.out.println("img num:"+size+" , in:"+libName);
//				for (int i = 0; i < size; i++) {
//					switch (n) {
//					case FILE: {// 文件资源
//						int idPosition = UtilTools.findIndexInArray(i, fileIDList);
//						if (idPosition >= 0&&type==n) {// 是需要的资源
//							byte[] data = (byte[]) readArray(dis, 0, TYPE_BYTE);	
//							ret[idPosition] = data;
//							readCount++;
//							////#debug info
//							//System.out.println(i+" img idPosition:"+idPosition+" , size:"+data.length);
//							if (readCount == fileIDList.length) {
//								return ret;
//							}
//						}
//						else{
//							int nc = dis.readShort()&0x00ffff;
//							if(nc==0xffff){//大文件，超过64K
//								nc=dis.readInt();
//							}
//							////#debug info
//							//System.out.println(i+" img idPosition:"+idPosition+" , skip:"+nc);
//							dis.skip(nc);
//						}
//						break;
//					}
//					}
//				}
//			}
//		} catch (Exception e) {
//			//#debug info
//			e.printStackTrace();
//		} finally {
//			try {
//				dis.close();
//				dis = null;
//				is.close();
//				is = null;
//			} catch (Exception e) {
//			}
//		}
//		return null;
//	}
	
	
	
	
	
	/**
	 * 资源包保存到rms中的索引名字
	 * @param resName
	 * @param index
	 * @return
	 */
	public static String getSubRmsFlag(String resName,int index){
		return resName+"V1_"+index;
	}
	

	
	/**
	 * 从流中创建一个数组
	 * 
	 * @param in
	 *            DataInputStream 输入流
	 * @param nc
	 *            int 要载入的数据个数，nc<=0表示从流中读入数据个数
	 * @param nClass
	 *            int 数据类型 DT_STRING = 0, DT_INT = 1, DT_SHORT = 2, DT_BYTE = 3
	 * @throws Exception
	 * @return Object 返回值为载入的数据，根据nClass强制转换成相应的数组
	 */
	public static Object readArray(DataInputStream din, int nc, int nClass)
			throws Exception {
		if (nc <= 0) {
			nc = din.readShort()&0x00ffff;
		}
		if (nc < 0) {
			return null;
		}
		switch (nClass) {
		default:
			return null;
		case TYPE_STRING: { // 读入字符串
			String str[] = new String[nc];
			for (int i = 0; i < nc; i++) {
				str[i] = din.readUTF();
			}
			return str;
		}
		case TYPE_INT: { // 读入整数数组
			int data[] = new int[nc];
			for (int i = 0; i < nc; i++) {
				data[i] = din.readInt();
			}
			return data;
		}
		case TYPE_SHORT: { // 读入短整型数组
			short data[] = new short[nc];
			for (int i = 0; i < nc; i++) {
				data[i] = din.readShort();
			}
			return data;
		}
		case TYPE_BYTE: { // 读入字节数组
			byte data[] = new byte[nc];
			int count=0;
			while(count<nc){
				count+=din.read(data,count,nc-count);
			}			
			return data;
		}
		}
	}
	
	
	  /**
	   * 写入一个数组到流中
	   * @param out DataOutputStream
	   * @param obj Object 数据表
	   * @param nc int     数据个数，当n<=0表示数据表中全部数据
	   * @param nClass int 数据类型 DT_STRING = 0, DT_INT = 1, DT_SHORT = 2, DT_BYTE = 3
	   * @throws Exception
	   */
	  public static void writeArray(DataOutputStream out, Object obj, int nc,
	                                int nClass) throws Exception {
	    if (obj == null) { //数据空
	      out.writeShort(0);
	      return;
	    }
	    switch (nClass) {
	      case TYPE_STRING: { //写入字符表
	        String str[] = (String[]) obj;
	        if (nc <= 0 || nc > str.length) {
	          nc = str.length;
	          out.writeShort(nc);
	        }
	        for (int i = 0; i < nc; i++) {
	          out.writeUTF(str[i]);
	        }
	        break;
	      }
	      case TYPE_INT: { //写入一个整数数组
	        int data[] = (int[]) obj;
	        if (nc <= 0 || nc > data.length) {
	          nc = data.length;
	          out.writeShort(nc);
	        }
	        for (int i = 0; i < nc; i++) {
	          out.writeInt(data[i]);
	        }
	        break;
	      }
	      case TYPE_SHORT: { //写入一个短整型数组
	        short data[] = (short[]) obj;
	        if (nc <= 0 || nc > data.length) {
	          nc = data.length;
	          out.writeShort(nc);
	        }
	        for (int i = 0; i < nc; i++) {
	          out.writeShort(data[i]);
	        }
	        break;
	      }
	      case TYPE_BYTE: { //写入一个字节数组
	        byte data[] = (byte[]) obj;
	        if (nc <= 0 || nc > data.length) {
	          nc = data.length;
	          out.writeShort(nc);
	        }
	        out.write(data, 0, nc);
	        break;
	      }
	    }
	  }
	
	
	
	/**
	 * 从指定RMS中导出数据
	 * @param rmsName
	 */
//	public static byte[][] loadDataFromRms(String rmsName) {
//		RecordStore store = null;
//		byte[][] data = null;
//		try {
//			store = RecordStore.openRecordStore(rmsName, false);
//			if(store!=null){
//				int num = store.getNumRecords();
//				//#debug info
//				System.out.println("loadDataFromRms rmsName:" + rmsName+" NumRecords:"+num);
//				data = new byte[num][];
//				for (int i = 0; i < data.length; i++) {
//					data[i] = store.getRecord(i + 1);
//				}
//			}
//		} catch (Exception e) {
//			//#debug info
//			System.out.println("loadDataFromRms "+e.getClass().getName()+" at:"+rmsName);
//		} finally {
//			try {
//				if(store!=null)store.closeRecordStore();
//			} catch (Throwable ee) {
//			}
//		}
//		return data;
//	}
	
	/**
	 * 把二维数组保存到指定RMS中
	 * @param rmsName
	 * @param data
	 * @return
	 */
//	public static boolean saveDataToRms(String rmsName, byte[][] data) {
//		RecordStore store = null;
//		try {
//			store = RecordStore.openRecordStore(rmsName, true);
//			//#debug info
//			System.out.println("saveDataToRms(String rmsName,byte[][] data) rmsName:" + rmsName+" data:"+data.length+" NumRecords:"+store.getNumRecords());
//			if (store.getNumRecords() > data.length) {// 数据库中个数多余需要保存的个数，删除数据库，避免数据库中存在多余数据项
//				try {
//					store.closeRecordStore();
//				} catch (Throwable ee) {}
//				try {
//					RecordStore.deleteRecordStore(rmsName);
//				} catch (Throwable e) {
//				}
//				store = RecordStore.openRecordStore(rmsName, true);
//				//#debug info
//				System.out.println("deleteRecordStore and  openRecordStore rmsName:" + rmsName);
//			}
//			for (int i = 0; i < data.length; i++) {
//				if (i < store.getNumRecords()) {
//					store.setRecord(i + 1, data[i], 0, data[i].length);
//				} else {
//					store.addRecord(data[i], 0, data[i].length);
//				}
//			}
//			System.out.println("**************** saveDataToRms  *****************"+rmsName);
//		} catch (Exception e) {
//			//#debug info
//			e.printStackTrace();
//			return false;
//		} finally {
//			try {
//				if(store!=null)store.closeRecordStore();
//			} catch (Throwable ee) {
//			}
//		}
//		return true;
//	}
	
	
	
	/**
	 * 把一维数组保存到指定RMS中
	 * @param rmsName
	 * @param data
	 * @param dataOffset
	 * @param dataLength
	 * @return
	 */
//	public static boolean saveDataToRms(String rmsName, byte[] data, int dataOffset, int dataLength) {
//		//#debug info
//		System.out.println("saveDataToRms rmsName:" + rmsName + " data.len:" + data.length + " dataOffset:"+ dataOffset + " saveLength:" + dataLength);
//		RecordStore store = null;
//		try {
//			store = RecordStore.openRecordStore(rmsName, true);
//			if (store.getNumRecords() > 0) {
//				store.setRecord(1, data, dataOffset, dataLength);
//			} else {
//				store.addRecord(data, dataOffset, dataLength);
//			}
//		} catch (Exception e) {
//			return false;
//		} finally {
//			try {
//				if(store!=null)store.closeRecordStore();
//			} catch (Throwable ee) {
//			}
//		}
//		return true;
//	}
	
	

	/** **************资源包下载********************** */
	//#if polish.DownloadRes_Model
	private static final String RMSName_resLibList = "IntomsResList";
	private static String[][] resLibListInfo;

	/**
	 * 列举资源库信息(名称及版本)
	 * 
	 * @return
	 */
//	public static synchronized String[][] listResLibsInfo() {
//		if (resLibListInfo == null) {
//			byte[][] resLibListData = loadDataFromRms(RMSName_resLibList);
//			if (resLibListData != null) {
//				//#debug info
//				System.out.println("listResLibsInfo resLibListData:"+resLibListData.length);
//				String[][] listRecord = new String[resLibListData.length][];
//				for (int i = 0; i < listRecord.length; i++) {
//					String info = new String(resLibListData[i]);
//					//#debug info
//					System.out.println("listResLibsInfo:" + info);
//					int index = info.indexOf("\n");
//					listRecord[i] = new String[2];
//					listRecord[i][0] = info.substring(0, index);
//					listRecord[i][1] = info.substring(index + 1);
//				}
//				resLibListInfo = listRecord;
//			} else {
//				resLibListInfo = new String[0][];
//			}
//		}
//		return resLibListInfo;
//	}

	/**
	 * 保存源库信息(名称及版本)
	 * 
	 * @param info
	 */
//	private static synchronized void saveResLibsInfo(String[][] info) {
//		byte[][] data = new byte[info.length][];
//		for (int i = 0; i < data.length; i++) {
//			data[i] = (info[i][0] + "\n" + info[i][1]).getBytes();
//			//#debug info
//			System.out.println("saveResLibsInfo:" + new String(data[i]));
//		}
//		saveDataToRms(RMSName_resLibList, data);
//		resLibListInfo=null;
//	}
//	
	
	//资源的名称，下载地址，最小版本号，最大版本号
	private static String[] resfileNameList, resfileURLeList;
	private static int[] resfileMinVersionList,resfileMaxVersionList;
	
	/**
	 * 获取指定资源的路径
	 * @param resName
	 * @return
	 */
//	public static String getResUrl(String resName){
//		if(resfileNameList!=null){
//			int index = UtilTools.findStringInArray(resName,resfileNameList);
//			if(index>=0){
//				return resfileURLeList[index];
//			}
//		}
//		return null;
//	}
	
	/**
	 * 获取指定资源的最新下载版本
	 * @param resname
	 * @return
	 */
//	public static int getResDownVersion(String resName){
//		if(resfileNameList!=null){
//			int index = UtilTools.findStringInArray(resName,resfileNameList);
//			if(index>=0){
//				return resfileMaxVersionList[index];
//			}
//		}
//		return -1;
//	}
	
	/**
	 * 清楚各个内存中各个版本信息
	 */
	public static void clearVersions(){
		resfileNameList=null;
		resfileURLeList=null;
		resfileMinVersionList=null;
		resfileMaxVersionList=null;
	}
	
	
	/**
	 * 是否存在版本信息
	 * @return
	 */
	public static boolean existVersions(){
		return resfileMaxVersionList!=null;
	}
	
	public static Dialog gradeDialog;
	/**
	 * 检查RMS中资源的版本号
	 * @param fileNameList
	 * @param fileURLeList
	 * @param minVersionList
	 * @param maxVersionList
	 * @param needShowOptionUpgrate = true 有可选升级时是否提示可选升级
	 * @param upgradeWhenOption 可选升级时用户选择了升级
	 */
//	public static void saveAndCheckResPackVersionInRMS(final String[] fileNameList, final String[] fileURLeList,final int[] minVersionList,final int[] maxVersionList,boolean needShowOptionUpgrate,boolean upgradeWhenOption) {
//		String[][] infoList = listResLibsInfo();
//		boolean needDelete = false;
//		if (infoList != null) {
//			for (int i = 0; i < infoList.length; i++) {
//				String localResFileName = infoList[i][0];
//				int localResFileVersion = -1;
//				try{
//					localResFileVersion = Integer.parseInt(infoList[i][1]);
//				}catch(Exception e){}
//				
//				//#debug info
//				System.out.println("localResFileName:" + localResFileName
//						+ ",localResFileVersion:" + localResFileVersion);
//				int index = UtilTools.findStringInArray(localResFileName,fileNameList);
//				
//				boolean gradeForce=index <= -1||localResFileVersion<minVersionList[index]||localResFileVersion>maxVersionList[index];//强制更新
//				boolean gradeOptian=(index != -1&&localResFileVersion>=minVersionList[index]&&localResFileVersion<maxVersionList[index]);//可选更新
//				
//				if (gradeForce||gradeOptian) {// 有版本可以更新
//					if (needShowOptionUpgrate&&!gradeForce&&gradeOptian) {//可选升级资源包
//						needShowOptionUpgrate=false;
//						final StringItem okItem=new StringItem("是");
//						final StringItem noItem=new StringItem("否");
//						gradeDialog=new Dialog(null,
//								new Component(){
//									public boolean itemAction(Object item,Component container) {
//										if(super.itemAction(item, container)){
//											return true;
//										}
//										if(okItem==item){//下载
//											saveAndCheckResPackVersionInRMS(fileNameList,fileURLeList,minVersionList,maxVersionList,false,true);
//											gradeDialog.focusable=false;//用于标识提示升级执行完毕
//											return true;
//										}
//										else if(noItem==item){//不下载
//											saveAndCheckResPackVersionInRMS(fileNameList,fileURLeList,minVersionList,maxVersionList,false,false);
//											gradeDialog.focusable=false;//用于标识提示升级执行完毕
//											return true;
//										}
//										return false;
//									}
//								}
//								,okItem);
//						gradeDialog.setSoftCMD(okItem, noItem);
//						gradeDialog.setComfirmCMD_Listener(okItem);
//						gradeDialog.setBackCMD_Listener(noItem);
//						gradeDialog.setTextDialog("发现资源有可选更新\n是否下载？");
//						return;
//						//////wait
//					}
//					
//					//是否需要升级
//					boolean shouldGrade=gradeForce||(gradeOptian&&upgradeWhenOption);
//					if(shouldGrade){//资源需要升级，删除本地对应资源
//						try {
//							//#debug info
//							System.out.println("deleteRecordStore localResFileName:"
//											+ localResFileName
//											+ " localResFileVersion:"
//											+ localResFileVersion
//											+ " resFileVersion[index]:"
//											+ minVersionList[index]+"->"+maxVersionList[index]);
//							needDelete = true;
//							infoList[i][0] = null;
//							infoList[i][1] = null;
//							try {
//								for (int k = 0; k < 20; k++) {// 当不存在更多数据库时将跳出删除循环
//									RecordStore.deleteRecordStore(getSubRmsFlag(localResFileName, k));
//								}
//							} catch (Exception e) {}
//						} catch (Exception e) {}
//					}
//				}
//			}
//		}
//		if (needDelete) {
//			//#debug info
//			System.out.println("needDelete:"+needDelete+" infoList:"+infoList.length);
//			Vector v = new Vector();
//			for (int i = 0; i < infoList.length; i++) {
//				if (infoList[i] != null && infoList[i].length > 0
//						&& infoList[i][0] != null) {
//					v.addElement(infoList[i]);
//					//#debug info
//					System.out.println("addElement newInfoList[i]:"+infoList[i]);
//				}
//			}
//			//#debug info
//			System.out.println("needDelete:"+needDelete+" v:"+v.size());
//			String[][] newInfoList = new String[v.size()][];
//			for (int i = 0; i < newInfoList.length; i++) {
//				newInfoList[i] = (String[]) v.elementAt(i);
//				//#debug info
//				System.out.println("newInfoList[i]:"+newInfoList[i]);
//			}
//			//#debug info
//			System.out.println("needDelete:"+needDelete+" newInfoList:"+newInfoList.length);
//			saveResLibsInfo(newInfoList);
//		}
//		resfileNameList=fileNameList;
//		resfileURLeList=fileURLeList;
//		resfileMinVersionList=minVersionList;
//		resfileMaxVersionList=maxVersionList;
//	}

	/**
	 * 检查RMS中是否存在指定资源
	 * 
	 * @return
	 */
//	public static boolean checkResLibExistInRMS(String resName) {
//		if (resName.startsWith("/"))
//			return true;
//		String[][] infoList = listResLibsInfo();
//		if (infoList != null)
//			for (int i = 0; i < infoList.length; i++) {
//				if (resName.equals(infoList[i][0])) {
//					//#debug info
//					System.out.println("checkResLibExist resName:" + resName+ " Exist");
//					return true;
//			}
//		}
//		return false;
//	}

	/**
	 * 保存资源到RMS
	 * 
	 * @param resName
	 */
//	public static synchronized void saveResLibToRMS(String resName,
//			String version, byte[] data, int dataOffset, final int dataLength) {
//
//		// 将资源包数据保存到多个rms文件中
//		int pt = dataOffset;
//		int rmsIndex = 0;
//		while (pt < dataOffset + dataLength) {
//			int size = 55 * 1024;// RMS最大数据尺寸
//			if (pt + size > dataOffset + dataLength) {
//				size = dataOffset + dataLength - pt;
//			}
//			if (!saveDataToRms(getSubRmsFlag(resName,
//					rmsIndex++), data, pt, size)) {
//				return;
//			}
//			pt += size;
//		}
//
//		String[][] infoList = listResLibsInfo();
//		boolean exist = false;
//		if (infoList != null && infoList.length > 0)
//			for (int i = 0; i < infoList.length; i++) {
//				if (resName.equals(infoList[i][0])) {
//					infoList[i][1] = version;
//					exist = true;
//					break;
//				}
//			}
//		if (!exist) {
//			int pos = infoList != null ? infoList.length : 0;
//			String[][] newlist = new String[pos + 1][2];
//			if (pos > 0)
//				System.arraycopy(infoList, 0, newlist, 0, pos);
//			newlist[pos][0] = resName;
//			newlist[pos][1] = version;
//			infoList = newlist;
//		}
//		saveResLibsInfo(infoList);
//	}


	//#endif
	/***********************************************/
	
	
	
	/**
	 * 从资源文件文件中获取指定索引的文本
	 * @param index
	 * @return
	 */
//	public static String getString(int index){
//		return getString(new int[] {index})[0];
//	}
	
	/**
	 * 从资源文件文件中获取指定索引序列的文本
	 * @param indexList
	 * @return
	 */
//	public static String[] getString(int[] indexList){
//		InputStream is=getDataInputStream("/txt.pak");//(new Object().getClass()).getResourceAsStream("/txt.pak");
//		DataInputStream dis=new DataInputStream(is);
//		String[] ret=new String[indexList.length];
//		try {
//			int count=0;
//			int num=dis.readShort();
//			for(int i=0;i<num;i++){
//				int p=UtilTools.findIndexInArray(i, indexList);
//				if(p>=0){
//					ret[p]=dis.readUTF();
//					count++;
//					if(count==indexList.length){
//						break;
//					}
//				}
//				else{
//					dis.skip(dis.readShort());
//				}
//			}
//		} catch (Exception e) {
//			//#debug info
//			e.printStackTrace();
//		}
//		finally{
//		       try {
//		              is.close();
//		              dis.close();
//		       } catch (Exception e) {}
//		}
//		return ret;
//	}
	
	/**
	 * 检测资源数据是否存在于jar包
	 * @param libName 文件路径
	 * @return
	 */
	public static boolean isDataExistInJar(String libName) {
		return ((new Object()).getClass().getResourceAsStream(libName) != null);
	}
	
	/**
	 * 检测资源数据是否存在于RMS
	 * @param rmsName
	 * @return
	 */
//	private static boolean checkDataExistInRMS(String rmsName) {
//		RecordStore store = null;
//		boolean exist = false;
//		try {
//			store = RecordStore.openRecordStore(rmsName, false);
//			if(store!=null){
//				exist = true;
//			}
//		} catch (Exception e) {
////			e.printStackTrace();
//		}finally {
//			try {
//				if(store!=null)store.closeRecordStore();
//			} catch (Throwable ee) {
//			}
//		}
//		return exist;
//	}
}
