package com.caiyi.fund.trade.widget.span;

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

/**
 * 超文本链接span
 * Created by zhangqiang on 2017/11/7.
 */

public class URLSpanWrapper extends URLSpan {

    private URLSpan wrapper;

    private OnClickListener onClickListener;

    public URLSpanWrapper(String url) {
        super(url);
    }

    public URLSpanWrapper(Parcel src) {
        super(src);
    }

    @Override
    public void onClick(View widget) {

        if(onClickListener != null){
            onClickListener.onClick(widget,getURL());
            return;
        }
        if(wrapper != null){
            wrapper.onClick(widget);
        }else{
            super.onClick(widget);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if(wrapper != null){
            wrapper.updateDrawState(ds);
        }else{
            super.updateDrawState(ds);
        }
        ds.setUnderlineText(false);
    }

    @Override
    public int getSpanTypeId() {
        if(wrapper != null){
            return wrapper.getSpanTypeId();
        }
        return super.getSpanTypeId();
    }

    @Override
    public String getURL() {
        if(wrapper != null){
            return wrapper.getURL();
        }
        return super.getURL();
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{

        void onClick(View widget,String url);
    }

    public URLSpan getWrapper() {
        return wrapper;
    }

    public void setWrapper(URLSpan wrapper) {
        this.wrapper = wrapper;
    }
}
