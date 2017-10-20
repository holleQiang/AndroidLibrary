package com.zq.view.recyclerview.sticky;

import android.view.View;

/**
 * Created by zhangqiang on 2017/9/30.
 */

public interface StickyAdapter {

    boolean isSticky(int position);

    View getStickyView(int position);
}
