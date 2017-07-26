package com.funoble.myarstation.store.data;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (C) 2005-2010 ddl Inc.All Rights Reserved.
 * FileName：FeeTypes.java
 * Description：
 * History：
 * 1.0 sunny 2011-5-27 Create
 */

/**
 * <p>
 * 
 * </p>
 */
public class ParValue
{
	public final static List<ParValue>	mList	= new ArrayList<ParValue>();
	public final int						mId;

	private final String					mName;

	static
	{
		mList.add(new ParValue(10, "10.00元"));
		mList.add(new ParValue(30, "30.00元"));
		mList.add(new ParValue(50, "50.00元"));
		mList.add(new ParValue(100, "100.00元"));
		
	}

	public ParValue(int id, String name)
	{
		this.mId = id;
		this.mName = name;
	}

	public List<ParValue> getFeeTypes()
	{
		return mList;
	}

	/**
	 * 获取id列表
	 * 
	 * @return
	 */
	public static int[] getFeeIdList()
	{
		int[] ids = new int[mList.size()];
		for (int i = 0, len = mList.size(); i < len; i++)
		{
			ids[i] = mList.get(i).mId;
		}

		return ids;
	}

	/**
	 * 获取名称列表
	 * 
	 * @return
	 */
	public static String[] getFeeNameList()
	{
		String[] names = new String[mList.size()];
		for (int i = 0, len = mList.size(); i < len; i++)
		{
			names[i] = mList.get(i).mName;
		}

		return names;
	}
	
	/**
	 * 根据名称id
	 * @param id
	 * @return
	 */
	public static int getFeeIdByName(String name)
	{
		if(name == null)
		{
			return -1;
		}
		for (int i = 0, len = mList.size(); i < len; i++)
		{
			if(mList.get(i).mName.equalsIgnoreCase(name))
			{
				return mList.get(i).mId;
			}
		}
		return -1;
	}
	

}
