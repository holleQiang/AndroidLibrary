package com.zq.utils;

import android.content.Context;
import android.support.annotation.StringRes;

import java.util.Locale;

/**
 * Created by zhangqiang on 2018/7/19.
 */
public class StringUtil {

    /**
     * 格式化
     *
     * @param context
     * @param stringResId   字符串资源id
     * @param replaceValues 替换的值
     * @return 格式化后的字符串
     */
    public static String format(Context context, @StringRes int stringResId, Object... replaceValues) {

        return format(context.getResources().getString(stringResId), replaceValues);
    }

    /**
     * 格式化
     *
     * @param text   字符串资源id
     * @param replaceValues 替换的值
     * @return 格式化后的字符串
     */
    public static String format(String text, Object... replaceValues) {

        return String.format(Locale.CHINA, text, replaceValues);
    }
}
