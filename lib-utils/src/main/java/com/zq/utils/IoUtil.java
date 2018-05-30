package com.zq.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by zhangqiang on 2018/5/30.
 */
public class IoUtil {


    public static void closeSilently(Closeable closeable){

        if(closeable == null){
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
