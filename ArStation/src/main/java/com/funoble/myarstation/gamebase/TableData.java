package com.funoble.myarstation.gamebase;

import java.io.*;
import java.util.*;


/**
 * Title: 资源包中的表格
 * Description: 带有列类型的表格
 * <p>Copyright: 腾讯科技(深圳)有限公司.</p>
 * @author cheerluo
 */
public class TableData
    extends BaseAction {

  /**
   * 表格数据
   */
  private Vector dataTable = new Vector();

  /**
   * 表头类型
   * 约定：
   * 第一列为列名 比如姓名，年龄，生命
   * 第二列为列类型 比如String,int,boolean
   */
  private Vector headTable = new Vector();

	/**
	 * 释放自己
	 */
	public void releaseSelf() {
		if(headTable != null) {
			headTable.removeAllElements();
			headTable = null;
		}
		if(dataTable != null) {
			dataTable.removeAllElements();
			dataTable = null;
		}
		super.releaseSelf();
		
	}
  
  
  /**
   * 获取整个数据表格
   * @return Vector
   */
  public Vector getDataTable() {
    return dataTable;
  }
  
  /**
   * 获取表格指定行数据
   * @param row
   * @return
   */
  public Object getRowData(int row) {
	  if(row>=0&&row<dataTable.size()){
		  return dataTable.elementAt(row);
	  }
	  return null;
  }
  
  /**
   * 获取表格指定行指定列数据
   * @param row
   * @param col
   * @return
   */
  public Object getData(int row,int col) {
	  if(row>=0&&row<dataTable.size()){
		  return ((Object[])dataTable.elementAt(row))[col];
	  }
	  return null;
  }

  /**
   * 获取头类型表格
   * @return Vector
   */
  public Vector getHeadTable() {
    return headTable;
  }


  /**
   * 获取类型在类型列表中索引
   * @param typeName String
   * @return int
   */
  public static int getIndexType(String typeName) {
    for (int i = 0; i < Project.DataType_Define.length; i++) {
      if (Project.DataType_Define[i].equals(typeName)) {
        return i;
      }
    }
    return -1;
  }



  /**
   * 导入二进制结构
   * @param dis DataInputStream
   * @throws Exception
   */
  public void inputStream(DataInputStream dis, int format) throws Exception {
    super.inputStream(dis, format);
    headTable.removeAllElements();
    dataTable.removeAllElements();
    int colSize = dis.readShort();
    int rowSize = dis.readShort();
    for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
      Object[] data = new Object[colSize];
      dataTable.addElement(data);
    }
    for (int colIndex = 0; colIndex < colSize; colIndex++) { //表头 操作每一列
      Object[] headRow = new Object[2];
      headRow[0] = dis.readUTF();
      headRow[1] = dis.readUTF();
      headTable.addElement(headRow);
      String typeStr = (String) headRow[1];
      for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) { //操作每一行
        Object[] data = (Object[]) dataTable.elementAt(rowIndex);
        if (Project.dataType_String.equals(typeStr)) {
          data[colIndex] = dis.readUTF();
        }
        else if (Project.dataType_byte.equals(typeStr)) {
          data[colIndex] = new Byte(dis.readByte());
        }
        else if (Project.dataType_short.equals(typeStr)) {
          data[colIndex] = new Short(dis.readShort());//dis.readShort() + "";
        }
        else if (Project.dataType_int.equals(typeStr)) {
          data[colIndex] = new Integer(dis.readInt());//dis.readInt() + "";
        }
        else if (Project.dataType_boolean.equals(typeStr)) {
          data[colIndex] = new Boolean(dis.readBoolean());//dis.readBoolean() + "";
        }
        //System.out.print(data[colIndex]+",");
      }
      //System.out.println();
    }
  }



}
