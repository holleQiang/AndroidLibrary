package com.caiyi.fund.trade.utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Desc : 集合工具类
 * Author : Lauzy
 * Date : 2017/11/23
 * Blog : http://www.jianshu.com/u/e76853f863a9
 * Email : freedompaladin@gmail.com
 */
public class ListUtil {

    /**
     * 判断集合是否为空
     * @param iterable 集合
     * @return 是否为空
     */
    public static <T> boolean isEmpty(Iterable<T> iterable) {
        return iterable == null || !iterable.iterator().hasNext();
    }

    /**
     * 将一个对象转化为array list
     * @param item
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(T item){

        if(item == null){
            return null;
        }
        List<T> list = new ArrayList<>(1);
        list.add(item);
        return list;
    }

    /**
     * 是否是有效的索引
     * @param collection 集合
     * @param index 索引
     * @return true有效 false 无效
     */
    public static boolean isValidIndex(@NonNull Collection<?> collection, int index){

        return index >= 0 && index < collection.size();
    }
}
