/*******************************************************************************
 * Copyright (C) 1998-2009 BBG Inc.All Rights Reserved.
 * FileName:   ImageAdapter.java 
 * Description：
 * History：
 * 版本号   作者       日期      简要介绍相关操作
 * 1.0     sunny    2009-11-26   Create
 *******************************************************************************/
package  com.funoble.myarstation.store.data;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

import com.funoble.myarstation.game.R;

/**
 * <p>
 * </p>
 */
public class ImageAdapter extends BaseAdapter
{
    private Context mContext;
    
    private List<Bitmap> items;
    public ImageAdapter(Context c, List<Bitmap> items)
    {
        mContext = c;
        this.items = items;
    }
    
    

    public int getCount()
    {
        return items.size();
    }

    public Bitmap getItem(int position)
    {
        return items.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView iv = (ImageView)convertView;
        if(iv == null)
        {
            iv = new ImageView(mContext);            
        }
        
//        Bitmap bm = CacheUtils.getImage(items[position].picName);
        if (position >= items.size()){
        	return iv;
        }
        Bitmap bm = items.get(position);
        if(bm != null)
        {
            iv.setImageBitmap(bm);
        }
        else
        {
//            iv.setImageResource(R.drawable.defaultitem);
        }
        iv.setAdjustViewBounds(true);
        iv.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        iv.setBackgroundResource(R.drawable.picture_frame);
        return iv;
    }


    

}
