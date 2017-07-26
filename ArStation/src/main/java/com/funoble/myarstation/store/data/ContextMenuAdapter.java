/*******************************************************************************
 * Copyright (C) 1998-2009 BBG Inc.All Rights Reserved.
 * FileName:   ImageAdapter.java 
 * Description：
 * History：
 * 版本号   作者       日期      简要介绍相关操作
 * 1.0     sunny    2009-11-26   Create
 *******************************************************************************/
package  com.funoble.myarstation.store.data;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.funoble.myarstation.game.R;

/**
 * <p>
 * </p>
 */
public class ContextMenuAdapter extends BaseAdapter
{
    private Context mContext;
    
    private String[] items;
    public ContextMenuAdapter(Context c, String[] items)
    {
        mContext = c;
        this.items = items;
    }
    
    

    public int getCount()
    {
        return items.length;
    }

    public String getItem(int position)
    {
        return items[position];
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
    	TextView tv = (TextView)convertView;
        if(tv == null)
        {
        	tv = new TextView(mContext);            
        	tv.setGravity(Gravity.CENTER);
        	tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        	tv.setTextColor(0xffffffff);
//        	tv.setHeight(mContext.getResources().getDimensionPixelSize(R.dimen.context_menu_height));
        	int margin_h = mContext.getResources().getDimensionPixelSize(R.dimen.context_menu_margin_h);
        	int margin_v = mContext.getResources().getDimensionPixelSize(R.dimen.context_menu_margin_v);
        	tv.setPadding(margin_h, margin_v, margin_h, margin_v);
        }
        
		tv.setTag(position);
		tv.setText(items[position]);
        return tv;
    }

    

}
