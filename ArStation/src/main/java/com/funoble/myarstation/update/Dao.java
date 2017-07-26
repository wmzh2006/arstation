package com.funoble.myarstation.update;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 *
 * 一个业务类
 */
public class Dao {
	private DBHelper dbHelper;
	
	public Dao(Context context) {
		dbHelper = new DBHelper(context);
		
	}
	
	/**
	 * 查看数据库中是否有数据
	 */
	public boolean isHasInfors(Integer version) {
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		String sql = "select count(*)  from download_info where version=?";
		Cursor cursor = database.rawQuery(sql, new String[] {version.toString()});
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		return count == 0;
	}
	
	/**
	 * 保存 下载的具体信息
	 */
	public void saveInfos(List<DownloadInfo> infos) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		for (DownloadInfo info : infos) {
			String sql = "insert into download_info(version,start_pos,end_pos,compelete_size) values (?,?,?,?)";
			Object[] bindArgs = { info.version, info.startPos,
					info.endPos, info.compeleteSize};
			database.execSQL(sql, bindArgs);
		}
	}
	
	/**
	 * 得到下载具体信息
	 */
	public List<DownloadInfo> getInfos(Integer version) {
		List<DownloadInfo> list = new ArrayList<DownloadInfo>();
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		String sql = "select version, start_pos, end_pos,compelete_size,compelete from download_info where version=?";
		Cursor cursor = database.rawQuery(sql, new String[] { version.toString() });
		while (cursor.moveToNext()) {
			DownloadInfo info = new DownloadInfo(cursor.getInt(0),
					cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
			list.add(info);
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 得到下载具体信息
	 */
	public boolean isCompelete(Integer version) {
		int compelete = 0;
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		String sql = "select compelete from download_info where version=?";
		Cursor cursor = database.rawQuery(sql, new String[] { version.toString() });
		while (cursor.moveToNext()) {
			compelete = cursor.getInt(0);
		}
		cursor.close();
		return compelete == 1 ? true : false;
	}
	
	/**
	 * 更新数据库中的下载信息
	 */
	public void updataInfos(int version, int compeleteSize) {
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		String sql = "update download_info set compelete_size=? where version=?";
		Object[] bindArgs = { compeleteSize, version};
		database.execSQL(sql, bindArgs);
	}
	
	/**
	 * 更新完成标志
	 */
	public void updataCompelete(int version, int compelete) {
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		String sql = "update download_info set compelete=? where version=?";
		Object[] bindArgs = { compelete, version};
		database.execSQL(sql, bindArgs);
	}
	
	/**
	 * 关闭数据库
	 */
	public void closeDb() {
		dbHelper.close();
	}
	
	/**
	 * 下载完成后删除数据库中的数据
	 */
	public void delete(Integer version) {
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		database.delete("download_info", "version=?", new String[] { version.toString() });
		database.close();
	}
}
