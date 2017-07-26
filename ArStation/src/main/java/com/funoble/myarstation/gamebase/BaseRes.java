package com.funoble.myarstation.gamebase;

import java.io.*;

/**
 * <p>
 * Title: 动画基本单元
 * </p>
 * <p>
 * Description: 动画单元操作：数据属性、显示属性、及保存到流中或者从流中读取动画单元
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Cheer
 * @version 1.0
 */
public abstract class BaseRes {

	// ////////动画单元的数据属性//////////
	public final static int RES_CLIP = 0;
	// 动画单元基本数据
	public final static int RES_BUILD = 1;
	// 元素集合的具体标志
	public static final int RES_UNIT = 2;

	public int iType;
	public int iID = 0;

	/**
	 * 释放自己
	 */
	public void releaseSelf() {
	}

	/**
	 * 导入二进制结构
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws Exception
	 */
	public void inputStream(DataInputStream dis) throws Exception {
	}

}
