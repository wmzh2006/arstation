package com.funoble.myarstation.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ResourceLoader {

	private static final Resources res = ActivityUtil.resources;

	public static Bitmap createImage(int id){
		return BitmapFactory.decodeResource(res, id);
	}
}
