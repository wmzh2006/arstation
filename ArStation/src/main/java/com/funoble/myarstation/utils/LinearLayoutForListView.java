/*******************************************************************************
 * Copyright (C)   Inc.All Rights Reserved.
 * FileName: LinearLayoutForListView.java 
 * Description:
 * History:  
 * 版本号 作者 日期 简要介绍相关操作 
 * 1.0 cole  2012-9-9 下午09:37:50
 *******************************************************************************/
package com.funoble.myarstation.utils;

import com.funoble.myarstation.adapter.DetailAdapter;
import com.funoble.myarstation.game.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


public class LinearLayoutForListView extends LinearLayout {

    private DetailAdapter adapter;
    private OnClickListener onClickListener = null;
    private Context context;
    /**
     * 绑定布局
     */
    public void bindLinearLayout() {
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View v = adapter.getView(i, null, null);

//            v.setOnClickListener(this.onClickListener);
//            if (i == count - 1) {
//                LinearLayout ly = (LinearLayout) v;
//                ly.removeViewAt(2);
//            }
            addView(v, i);
        }
        Log.v("countTAG", "" + count);
    }

    public LinearLayoutForListView(Context context) {
        super(context);
        this.context = context;
    }

    public LinearLayoutForListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * 获取Adapter
     * 
     * @return adapter
     */
    public DetailAdapter getAdpater() {
        return adapter;
    }

    /**
     * 设置数据
     * 
     * @param adpater
     */
    public void setAdapter(DetailAdapter adpater) {
        this.adapter = adpater;
        removeViews(0, getChildCount()-1);
        bindLinearLayout();
    }

    /**
     * 获取点击事件
     * 
     * @return
     */
    public OnClickListener getOnclickListner() {
        return onClickListener;
    }

    /**
     * 设置点击事件
     * 
     * @param onClickListener
     */
    public void setOnclickLinstener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
