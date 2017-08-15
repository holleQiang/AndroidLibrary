package com.zq.view.wheelview;

/**
 * Created by zhangqiang on 2017/8/10.
 */

public interface Adapter {

    int getItemCount();

    String getTextAt(int position);

    String getLeftExtraText(int position);

    String getRightExtraText(int position);
}
