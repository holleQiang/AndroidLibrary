package com.zq.utils;

import android.content.Context;

/**
 * Created by zhangqiang on 2018/7/19.
 */
public class ViewUtil {

    /**
     * dp转化为px
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp){

        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5f);
    }
}
