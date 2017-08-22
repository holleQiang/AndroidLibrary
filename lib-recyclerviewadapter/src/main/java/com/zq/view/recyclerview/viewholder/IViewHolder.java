package com.zq.view.recyclerview.viewholder;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.text.method.MovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CompoundButton;

/**
 * Created by zhangqiang on 17-6-30.
 */

public interface IViewHolder {

    <T extends View> T getView(int viewId);

    View getView();

    IViewHolder setText(int viewId, CharSequence charSequence) ;

    IViewHolder setImageResource(int viewId, int imageResource);

    IViewHolder setImageBitmap(int viewId, Bitmap bitmap);

    IViewHolder setImageDrawable(int viewId, Drawable drawable);

    IViewHolder setBackgroundColor(int viewId, int color);

    IViewHolder setBackgroundRes(int viewId, int backgroundRes);

    IViewHolder setTextColor(int viewId, int textColor);

    IViewHolder setTextColorRes(int viewId, int textColorRes);

    IViewHolder setAlpha(int viewId, float value);

    IViewHolder setVisible(int viewId, boolean visible);

    IViewHolder setVisibility(int viewId, int visible);

    IViewHolder addLinks(int viewId, int mask);

    IViewHolder setTypeface(Typeface typeface, int... viewIds);

    IViewHolder setProgress(int viewId, int progress);

    IViewHolder setProgress(int viewId, int progress, int max);

    IViewHolder setMax(int viewId, int max);

    IViewHolder setRating(int viewId, float rating);

    IViewHolder setRating(int viewId, float rating, int max);

    IViewHolder setTag(int viewId, Object tag) ;

    IViewHolder setTag(int viewId, int key, Object tag) ;

    IViewHolder setChecked(int viewId, boolean checked);

    /**
     * 关于事件的
     */
    IViewHolder setOnClickListener(int viewId, View.OnClickListener listener);

    IViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener);

    IViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener);

    IViewHolder setAdapter(int viewId, Adapter adapter);

    IViewHolder setOnItemClickListener(int viewId, AdapterView.OnItemClickListener itemClickListener);

    IViewHolder setCompoundDrawablePadding(int viewId, int pad);

    IViewHolder setCompoundDrawablesWithIntrinsicBounds(int viewId, Drawable left, Drawable top, Drawable right, Drawable bottom);

    IViewHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener onCheckedChangeListener);

    IViewHolder setBackgroundResource(int viewId, int resId);

    IViewHolder addTextChangedListener(int viewId, TextWatcher textWatcher);

    IViewHolder setMovementMethod(int viewId, MovementMethod movement);

    IViewHolder setEnable(int viewId, boolean enable);

     int getVisibility(int viewId);

    IViewHolder setLayouParams(int viewId, ViewGroup.LayoutParams layoutParams) ;

    IViewHolder setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener onItemLongClickListener);

    IViewHolder removeAllViews(int viewId);

    IViewHolder addView(int viewId, View childView);

    IViewHolder setTextSize(int viewId, int unit, float textSize);

    IViewHolder setTextSize(int viewId, float textSize);
}
