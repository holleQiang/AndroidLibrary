package com.zq.view.recyclerview.viewholder;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
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

    <T extends View> T getView(@IdRes int viewId);

    View getView();

    IViewHolder setText(@IdRes int viewId, CharSequence charSequence) ;

    IViewHolder setImageResource(@IdRes int viewId, int imageResource);

    IViewHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap);

    IViewHolder setImageDrawable(@IdRes int viewId, Drawable drawable);

    IViewHolder setBackgroundColor(@IdRes int viewId, int color);

    IViewHolder setBackgroundRes(@IdRes int viewId, int backgroundRes);

    IViewHolder setTextColor(@IdRes int viewId, int textColor);

    IViewHolder setTextColorRes(@IdRes int viewId, int textColorRes);

    IViewHolder setAlpha(@IdRes int viewId, float value);

    IViewHolder setVisible(@IdRes int viewId, boolean visible);

    IViewHolder setVisibility(@IdRes int viewId, int visible);

    IViewHolder addLinks(@IdRes int viewId, int mask);

    IViewHolder setTypeface(Typeface typeface, int... viewIds);

    IViewHolder setProgress(@IdRes int viewId, int progress);

    IViewHolder setProgress(@IdRes int viewId, int progress, int max);

    IViewHolder setMax(@IdRes int viewId, int max);

    IViewHolder setRating(@IdRes int viewId, float rating);

    IViewHolder setRating(@IdRes int viewId, float rating, int max);

    IViewHolder setTag(@IdRes int viewId, Object tag) ;

    IViewHolder setTag(@IdRes int viewId, int key, Object tag) ;

    IViewHolder setChecked(@IdRes int viewId, boolean checked);

    /**
     * 关于事件的
     */
    IViewHolder setOnClickListener(@IdRes int viewId, View.OnClickListener listener);

    IViewHolder setOnTouchListener(@IdRes int viewId, View.OnTouchListener listener);

    IViewHolder setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener listener);

    IViewHolder setAdapter(@IdRes int viewId, Adapter adapter);

    IViewHolder setOnItemClickListener(@IdRes int viewId, AdapterView.OnItemClickListener itemClickListener);

    IViewHolder setCompoundDrawablePadding(@IdRes int viewId, int pad);

    IViewHolder setCompoundDrawablesWithIntrinsicBounds(@IdRes int viewId, Drawable left, Drawable top, Drawable right, Drawable bottom);

    IViewHolder setOnCheckedChangeListener(@IdRes int viewId, CompoundButton.OnCheckedChangeListener onCheckedChangeListener);

    IViewHolder setBackgroundResource(@IdRes int viewId, int resId);

    IViewHolder addTextChangedListener(@IdRes int viewId, TextWatcher textWatcher);

    IViewHolder setMovementMethod(@IdRes int viewId, MovementMethod movement);

    IViewHolder setEnable(@IdRes int viewId, boolean enable);

     int getVisibility(@IdRes int viewId);

    IViewHolder setLayoutParams(@IdRes int viewId, ViewGroup.LayoutParams layoutParams) ;

    IViewHolder setOnItemLongClickListener(@IdRes int viewId, AdapterView.OnItemLongClickListener onItemLongClickListener);

    IViewHolder removeAllViews(@IdRes int viewId);

    IViewHolder addView(@IdRes int viewId, View childView);

    IViewHolder setTextSize(@IdRes int viewId, int unit, float textSize);

    IViewHolder setTextSize(@IdRes int viewId, float textSize);

    IViewHolder setText(@IdRes int viewId, @StringRes int textRes);
}
