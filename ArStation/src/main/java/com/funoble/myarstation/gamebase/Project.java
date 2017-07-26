package com.funoble.myarstation.gamebase;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.funoble.myarstation.common.Tools;

/**
 * Title: 编辑器的项目类 Description: 保存项目数据及设置
 * <p>
 * Copyright: 腾讯科技(深圳)有限公司.
 * </p>
 * 
 * @author cheerluo
 */
/**
 * description 
 * version 1.0
 * author 
 * update 2014年3月20日 下午7:04:02 
 */
public class Project extends BaseAction {

	// Version
	private byte Core_Version = 1; // Core Version
	private byte Function_Version = 0; // Function Version
	// 类型
	private byte iProjectType = 0; // 0 --- 全部 1 --- 地图 2 --- 武器 3 --- 光效
	// 属性
	private byte iProperty = 0; // 如果类型是武器，则表示0 --- 轻武器 1 --- 双手武器 2 --- 重型武器
	// 如果类型是坐骑，则表示0 --- 低位 1 --- 中位 2 --- 高位
	// ID
	private int iID = 0; // 因为每个Project只能表示一件装备，所以得用一个ID来表示是哪件装备
	// 描述信息
	private String description = "";
	private String path;// 路径

	// //模型
	// public Project iModelProject = null;
	// //图像
	// public Project iImgProject = null;

	// 定义数据类型
	public static final byte TYPE_STRING = 0, TYPE_INT = 1, TYPE_SHORT = 2,
			TYPE_BYTE = 3, TYPE_BOOLEAN = 4;
	public static final String dataType_String = "String";
	public static final String dataType_int = "int";
	public static final String dataType_short = "short";
	public static final String dataType_byte = "byte";
	public static final String dataType_boolean = "boolean";
	public static final String dataType_default = dataType_String;
	public static final String[] DataType_Define = new String[] { // 类型定义
	dataType_String, dataType_int, dataType_short, dataType_byte,
			dataType_boolean };
	private Hashtable iClipTable;
	// ///////////////数据区域/////////////////
	public static final byte FILE = 0, CLIP = 1, FRAME = 2, ACTION = 3,
			SPRITE = 4, SCENE = 5, TABLEDATA = 6; // 库表类型

	private final Vector[] table = new Vector[7];
	{ // 初始化库表
		for (int i = 0; i < table.length; i++) {
			table[i] = new Vector();
		}
	}

	/**
	 * 将图片切片放入一个hashtable中，便于查找
	 */
	public void setClips() {
		setClips(false);
//		if (table[1] == null) {
//			return;
//		}
//		iClipTable = new Hashtable();
//		for (int i = 0; i < table[1].size(); i++) {
//			ImageClip ic = (ImageClip) table[1].elementAt(i);
//			int id = ic.iImageID;
//			if (iClipTable.get(id) == null) {
//				Vector tVect = new Vector();
//				iClipTable.put(id, tVect);
//			}
//			Vector tVect = (Vector) iClipTable.get(id);
//			tVect.addElement(ic);
//
//		}
//		for (int i = 0; i < table.length; i++) {
//			// if(table[i]==null){
//			// continue;
//			// }
//			// for (int k = 0; k < table[i].size(); k++) {
//			// ((BaseAction) table[i].elementAt(k)).releaseSelf();
//			// }
//			table[i] = null;
//		}

	}
	public void setClips(boolean isClearTable) {
		if (table[1] == null) {
			return;
		}
		iClipTable = new Hashtable();
		for (int i = 0; i < table[1].size(); i++) {
			ImageClip ic = (ImageClip) table[1].elementAt(i);
			int id = ic.iImageID;
			if (iClipTable.get(id) == null) {
				Vector tVect = new Vector();
				iClipTable.put(id, tVect);
			}
			Vector tVect = (Vector) iClipTable.get(id);
			tVect.addElement(ic);

		}
		if(!isClearTable){
			return;
		}
		for (int i = 0; i < table.length; i++) {
			// if(table[i]==null){
			// continue;
			// }
			// for (int k = 0; k < table[i].size(); k++) {
			// ((BaseAction) table[i].elementAt(k)).releaseSelf();
			// }
			table[i] = null;
		}

	}

	/**
	 * 找到切片对象
	 * 
	 * @param aMotherId
	 *            父图ID
	 * @param id
	 *            切片ID
	 * @return
	 */
	public ImageClip getImageClip(int aMotherId, int id) {
		if (iClipTable.get(aMotherId) == null) {
			return null;
		}
		Object ojb = iClipTable.get(aMotherId);
		if (ojb instanceof Vector) {
			Vector v = (Vector) ojb;
			if (v.size() > id) {
				return (ImageClip) v.elementAt(id);
			}
		}
		return null;
	}

	/**
	 * 释放自己
	 */
	public void releaseSelf() {
		Tools.println("Project释放！"+"文件是："+path);
		if (table != null) {
			for (int i = 0; i < table.length; i++) {
				if (table[i] == null) {
					continue;
				}
				for (int k = 0; k < table[i].size(); k++) {
					if(table[i].elementAt(k)!=null){
						((BaseAction) table[i].elementAt(k)).releaseSelf();
					}else{
						Tools.println("******** table[i].elementAt(k)==null *******");
					}
				}
				table[i].removeAllElements();
				table[i] = null;
			}
		}
		if (iClipTable != null) {
			Enumeration e = iClipTable.keys();
			while (e.hasMoreElements()) {
				Object obj = e.nextElement();
				 ResFile tRes=null;
				Vector tVect = (Vector) iClipTable.get(obj);
				for (int i = 0; i < tVect.size(); i++) {
					ImageClip tImgClip = (ImageClip) tVect.elementAt(i);
					if(tImgClip!=null){
						if(tImgClip.clipItem!=null){
							tRes=tImgClip.clipItem.getImageFile();
						}
						tImgClip.releaseSelf();
						tImgClip = null;
					}
				}
				if(tRes!=null){
					tRes.releaseSelf();
				}
				tVect.removeAllElements();
				tVect = null;
//				iClipTable.put(obj, null);
			}
			iClipTable.clear();
		}

		super.releaseSelf();
		// debug

	}

	/**
	 * 获取项目描叙
	 * 
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	// /**
	// * 获取指定对象在指定库中的位置索引
	// * @param clip ClipImage
	// * @return int
	// */
	// public int searchID(int nClass, Object o) {
	// int index = table[nClass].indexOf(o);
	// if (index == -1) {
	// // 库中不存在,添加到库中
	// table[nClass].addElement(o);
	// return table[nClass].size();
	// }
	// return index;
	// }

	/**
	 * 获取指定库的指定对象
	 * 
	 * @param id
	 *            int
	 * @return ClipImage
	 */
	public Object getObject(int id, int aClass) {
		if (id < 0 || id >= table[aClass].size()) {
			return null;
		}
		return table[aClass].elementAt(id);
	}

	/**
	 * 获取指定库
	 * 
	 * @param nClass
	 *            int
	 * @return Vector
	 */
	public Vector getObjectList(int aClass) {
		return table[aClass];
	}

	/**
	 * 获取指定库的大小
	 * 
	 * @param nClass
	 *            int
	 * @return int
	 */
	public int getSize(int aClass) {
		return table[aClass].size();
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
			nc = din.readUnsignedShort();
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
			if (nc == 0xffff) {// 大文件，超过64K
				nc = din.readInt();
			}
			byte data[] = new byte[nc];
			din.read(data);
			return data;
		}
		}
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
		// 版本
		Core_Version = dis.readByte();
		Function_Version = dis.readByte();
		// #debug info
		Tools.println("Project Core_Version:" + Core_Version
				+ ",Function_Version:" + Function_Version);

		// 描述
		description = dis.readUTF();

		// 资源表
		int freshFrequency = dis.readByte();// 刷新频率
		int resNum = dis.readByte();// 资源数量

		// #debug info
		Tools.println("freshFrequency:" + freshFrequency + ",resNum:"
				+ resNum + ",description:" + description);

		for (int n = 0; n < resNum; n++) {
			int size = dis.readShort();
			Vector v = table[n];
			v.removeAllElements();
			for (int i = 0; i < size; i++) {
				BaseAction stream = null;
				switch (n) {
				case FILE: {
					stream = new ResFile(i);
					break;
				}
				case CLIP: {
					stream = new ImageClip();
					break;
				}
				case FRAME: {
					stream = new ActionFrame();
					break;
				}
				case ACTION: {
					stream = new SpriteAction();
					break;
				}
				case SPRITE: {
					stream = new Sprite();
					break;
				}
				case SCENE: {
					stream = new Scene();
					break;
				}
				case TABLEDATA: {
					stream = new TableData();
					break;
				}
				}
				// //#debug info
				// System.out.println(i+" n:"+n+"
				// item:"+(stream==null?null:stream.getClass()));
				if (stream != null) {
					stream.inputStream(dis, format);
					stream.setIndex(i);
					v.addElement(stream);
				}
			}
		}
	}

	// 导入项目时临时静态变量，用于导入过程之中获取正在导入的项目
	private static Project loading_project;

	/**
	 * 获取正在导入的项目
	 * 
	 * @return
	 */
	public static Project getLoadingProject() {
		return loading_project;
	}

	/**
	 * 导入文件项目
	 * 
	 * @param dis
	 *            DataInputStream
	 */
	public static Project loadProject(String path, boolean aIsLoadImage) {
//		System.out.println("Project loadProject 开始加载资源-->"+path);
		long startTime = System.currentTimeMillis();
		InputStream is = null;
		is = ResManager.getDataInputStream(path);
//		try {
//			is = ActivityUtil.resources.getAssets().open(path);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		if (is==null) {
			Tools.println(" Project loadProject 失败于 "+path);
			return null;
		}
		// Object().getClass().getResourceAsStream(path);
		DataInputStream dis = new DataInputStream(is);
		int resSize = 0;
		try {
			// 尺寸
			resSize = dis.readInt();

			// 编辑器版本号版本号
			byte core_Version = dis.readByte();
			byte function_Version = dis.readByte();
			Tools.println("Editor core_Version:" + core_Version
					+ "Editor function_Version:" + function_Version);

			// 类型
			int projectType = dis.readByte();
			// #debug info
			Tools.println("Project Type = " + projectType);

			// 属性
			byte projectProperty = dis.readByte();

			// #debug info
			Tools.println("Project Property = " + projectProperty);

			// 工作项目
			loading_project = new Project();
			loading_project.setProperty(projectProperty);
			loading_project.path = path;
			if (aIsLoadImage) {
				loading_project.inputStream(dis, 1);
			} else {
				loading_project.inputStream(dis, 0);
			}
		} catch (Exception e) {
			// #debug info
			e.printStackTrace();
		} finally {
			try {
				is.close();
				dis.close();
			} catch (Exception e) {
			}
		}
		Project retproject = loading_project;
		loading_project = null;
		// #debug info
		Tools.println("Time" + (System.currentTimeMillis() - startTime) / 1000.0 + "秒  Project loadProject:" + path +"  "+ retproject  + "fileSize:" + (resSize / 1024)+"k  加载成功 =====================================================");
		return retproject;
	}
	
	/**
	 * 导入文件项目
	 * @param path
	 * @param aIsLoadImage
	 * @param asset true从asset载入数据，false从sdcard
	 * @return
	 */
	public static Project loadProject(String path, boolean aIsLoadImage, boolean asset) {
//		System.out.println("Project loadProject 开始加载资源-->"+path);
		long startTime = System.currentTimeMillis();
		InputStream is = null;
		is = ResManager.getDataInputStream(path, asset);
//		try {
//			is = ActivityUtil.resources.getAssets().open(path);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		if (is==null) {
			Tools.println(" Project loadProject 失败于 "+path);
			return null;
		}
		// Object().getClass().getResourceAsStream(path);
		DataInputStream dis = new DataInputStream(is);
		try {
			// 尺寸
			int resSize = dis.readInt();
			
			// 编辑器版本号版本号
			byte core_Version = dis.readByte();
			byte function_Version = dis.readByte();
			Tools.println("Editor core_Version:" + core_Version
					+ "Editor function_Version:" + function_Version);
			
			// 类型
			int projectType = dis.readByte();
			// #debug info
			Tools.println("Project Type = " + projectType);
			
			// 属性
			byte projectProperty = dis.readByte();
			
			// #debug info
			Tools.println("Project Property = " + projectProperty);
			
			// 工作项目
			loading_project = new Project();
			loading_project.setProperty(projectProperty);
			loading_project.path = path;
			if (aIsLoadImage) {
				loading_project.inputStream(dis, 1);
			} else {
				loading_project.inputStream(dis, 0);
			}
		} catch (Exception e) {
			// #debug info
			e.printStackTrace();
		} finally {
			try {
				is.close();
				dis.close();
			} catch (Exception e) {
			}
		}
		Project retproject = loading_project;
		loading_project = null;
		// #debug info
		Tools.println("Time" + (System.currentTimeMillis() - startTime) + " Project loadProject:" + path +"  "+ retproject + "  加载成功 =====================================================");
		return retproject;
	}

	/**
	 * 初始化对象所需文件
	 * 
	 * @param objList
	 * @param libName
	 */
//	public void initFileRes(BaseAction obj) {
//		initFileRes(new BaseAction[] { obj });
//	}

	/**
	 * 初始化对象所需文件
	 * 
	 * @param objList
	 * @param libName
	 */
//	public void initFileRes(BaseAction[] objList) {
//		Vector fileIDlist = new Vector();
//		for (int i = 0; i < objList.length; i++) {
//			handleFileRes(objList[i], fileIDlist, false);
//		}
//		int[] fileID = new int[fileIDlist.size()];
//		for (int i = 0; i < fileID.length; i++) {
//			int id = Integer.parseInt((String) fileIDlist.elementAt(i));
//			fileID[i] = id;
//		}
//		if (fileID != null && fileID.length > 0) {
//			byte[][] fileData = ResManager.getDataFromLib(fileID, path, FILE);
//			for (int i = 0; i < fileID.length; i++) {
//				ResFile resFile = (ResFile) table[FILE].elementAt(fileID[i]);
//				resFile.setFileData(fileData[i]);
//			}
//		}
//	}

	/**
	 * 释放对象所需文件
	 * 
	 * @param obj
	 */
	public void freeFileRes(BaseAction obj) {
		freeFileRes(new BaseAction[] { obj });
	}

	/**
	 * 释放对象所需文件
	 * 
	 * @param obj
	 */
	public void freeFileRes(BaseAction[] objList) {
		for (int i = 0; i < objList.length; i++) {
			handleFileRes(objList[i], null, true);
		}
	}

	/**
	 * 初始化对象所需文件(释放或者初始化)
	 * 
	 * @param obj
	 * @param initList
	 * @param freeFile
	 *            是否释放所需文件
	 */
	private void handleFileRes(BaseAction obj, Vector initList, boolean freeFile) {
		// //#debug info
		// System.out.println("handleFileRes obj:"+obj+" initList:"+initList.size());
		if (obj instanceof ImageClip) {
			ImageClip clip = (ImageClip) obj;
			ImageItem clipItem = clip.clipItem;
			if (clipItem != null) {
				ResFile resFile = clipItem.getImageFile();
				if (resFile != null) {
					if (freeFile) {// 释放所需文件
						resFile.decUsedTime();
					} else {// 使用所需文件
						resFile.addUsedTime();
						if (!resFile.isInited()) {
							int fileID = resFile.getFileID();
							String key = fileID + "";
							if (initList.indexOf(key) == -1) {
								initList.addElement(key);
							}
						}
					}
				}
			}
		} else if (obj instanceof Scene) {
			Scene scene = (Scene) obj;
			Vector mapList = scene.getLayoutManager();
			for (int i = 0; i < mapList.size(); i++) {
				handleFileRes((BaseAction) mapList.elementAt(i), initList,
						freeFile);
			}
		} else if (obj instanceof Map) {
			Map map = (Map) obj;
			Vector chipList = map.getChipList();
			for (int i = 0; i < chipList.size(); i++) {
				Object chip = chipList.elementAt(i);
				if (chip instanceof BaseAction) {// 事件不需要初始化资源
					handleFileRes((BaseAction) chip, initList, freeFile);
				}
			}
		} else if (obj.actionList != null) {
			int num = obj.actionList.length;
			for (int j = 0; j < num; j++) {
				handleFileRes(obj.getAction(j), initList, freeFile);
			}
		}
	}

	/**
	 * 在数组中查找指定值的位置
	 * 
	 * @param value
	 * @param array
	 * @return
	 */
	public static int findIndexInArray(int value, int array[]) {
		if (array == null) {
			return -1;
		}
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * 设置Project的类型和属性
	 */
	public void setType(byte aType) {
		iProjectType = aType;
	}

	public void setProperty(byte aProperty) {
		iProperty = aProperty;
	}

	/*
	 * 获取Project的类型和属性
	 */
	public byte getType() {
		return iProjectType;
	}

	public byte getProperty() {
		return iProperty;
	}

	/*
	 * 获取ID
	 */
	public int getID() {
		return iID;
	}

	public void setID(int aID) {
		iID = aID;
	}

	public Vector[] getTable() {
		return table;
	}

	// public void setImgProject(Project aImgProject) {
	// iImgProject = aImgProject;
	// }
	//	
	// public void setModelProject(Project aModelProject) {
	// iModelProject = aModelProject;
	// }
	
	public Scene getScene(int aIndex) {
		Scene pScene = (Scene)getObject(aIndex, SCENE);
		return pScene;
	}
	
	public Sprite getSprite(int aIndex) {
		Sprite pSprite = (Sprite)getObject(aIndex, SPRITE);
		return pSprite;
	}
	
}
