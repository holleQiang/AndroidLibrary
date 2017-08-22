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

public interface RVViewHolder {

    <T extends View> T getView(int viewId);

    View getView();

    RVViewHolder setText(int viewId, CharSequence charSequence) ;

    RVViewHolder setImageResource(int viewId, int imageResource);

    RVViewHolder setImageBitmap(int viewId, Bitmap bitmap);

    RVViewHolder setImageDrawable(int viewId, Drawable drawable);

    RVViewHolder setBackgroundColor(int viewId, int color);

    RVViewHolder setBackgroundRes(int viewId, int backgroundRes);

    RVViewHolder setTextColor(int viewId, int textColor);

    RVViewHolder setTextColorRes(int viewId, int textColorRes);

    RVViewHolder setAlpha(int viewId, float value);

    RVViewHolder setVisible(int viewId, boolean visible);

    RVViewHolder setVisibility(int viewId, int visible);

    RVViewHolder addLinks(int viewId,int mask);

    RVViewHolder setTypeface(Typeface typeface, int... viewIds);

    RVViewHolder setProgress(int viewId, int progress);

    RVViewHolder setProgress(int viewId, int progress, int max);

    RVViewHolder setMax(int viewId, int max);

    RVViewHolder setRating(int viewId, float rating);

    RVViewHolder setRating(int viewId, float rating, int max);

    RVViewHolder setTag(int viewId, Object tag) ;

    RVViewHolder setTag(int viewId, int key, Object tag) ;

    RVViewHolder setChecked(int viewId, boolean checked);

    /**
     * 关于事件的
     */
    RVViewHolder setOnClickListener(int viewId, View.OnClickListener listener);

    RVViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener);

    RVViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener);

    RVViewHolder setAdapter(int viewId, Adapter adapter);

    RVViewHolder setOnItemClickListener(int viewId, AdapterView.OnItemClickListener itemClickListener);

    RVViewHolder setCompoundDrawablePadding(int viewId, int pad);

    RVViewHolder setCompoundDrawablesWithIntrinsicBounds(int viewId, Drawable left, Drawable top, Drawable right, Drawable bottom);

    RVViewHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener onCheckedChangeListener);

    RVViewHolder setBackgroundResource(int viewId, int resId);

    RVViewHolder addTextChangedListener(int viewId, TextWatcher textWatcher);

    RVViewHolder setMovementMethod(int viewId, MovementMethod movement);

    RVViewHolder setEnable(int viewId, boolean enable);

     int getVisibility(int viewId);

    RVViewHolder setLayouParams(int viewId, ViewGroup.LayoutParams layoutParams) ;

    RVViewHolder setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener onItemLongClickListener);

    RVViewHolder removeAllViews(int viewId);

    RVViewHolder addView(int viewId, View childView);

    RVViewHolder setTextSize(int viewId, int unit, float textSize);

    RVViewHolder setTextSize(int viewId, float textSize);
}
