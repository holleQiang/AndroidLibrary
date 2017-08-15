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

public interface SuperViewHolder {

    <T extends View> T getView(int viewId);

    View getView();

    SuperViewHolder setText(int viewId, CharSequence charSequence) ;

    SuperViewHolder setImageResource(int viewId, int imageResource);

    SuperViewHolder setImageBitmap(int viewId, Bitmap bitmap);

    SuperViewHolder setImageDrawable(int viewId, Drawable drawable);

    SuperViewHolder setBackgroundColor(int viewId, int color);

    SuperViewHolder setBackgroundRes(int viewId, int backgroundRes);

    SuperViewHolder setTextColor(int viewId, int textColor);

    SuperViewHolder setTextColorRes(int viewId, int textColorRes);

    SuperViewHolder setAlpha(int viewId, float value);

    SuperViewHolder setVisible(int viewId, boolean visible);

    SuperViewHolder setVisibility(int viewId, int visible);

    SuperViewHolder linkify(int viewId);

    SuperViewHolder setTypeface(Typeface typeface, int... viewIds);

    SuperViewHolder setProgress(int viewId, int progress);

    SuperViewHolder setProgress(int viewId, int progress, int max);

    SuperViewHolder setMax(int viewId, int max);

    SuperViewHolder setRating(int viewId, float rating);

    SuperViewHolder setRating(int viewId, float rating, int max);

    SuperViewHolder setTag(int viewId, Object tag) ;

    SuperViewHolder setTag(int viewId, int key, Object tag) ;

    SuperViewHolder setChecked(int viewId, boolean checked);

    /**
     * 关于事件的
     */
    SuperViewHolder setOnClickListener(int viewId, View.OnClickListener listener);

    SuperViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener);

    SuperViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener);

    SuperViewHolder setAdapter(int viewId, Adapter adapter);

    SuperViewHolder setOnItemClickListener(int viewId, AdapterView.OnItemClickListener itemClickListener);

    SuperViewHolder setCompoundDrawablePadding(int viewId, int pad);

    SuperViewHolder setCompoundDrawablesWithIntrinsicBounds(int viewId, Drawable left, Drawable top, Drawable right, Drawable bottom);

    SuperViewHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener onCheckedChangeListener);

    SuperViewHolder setBackgroundResource(int viewId, int resId);

    SuperViewHolder addTextChangedListener(int viewId, TextWatcher textWatcher);

    SuperViewHolder setMovementMethod(int viewId, MovementMethod movement);

    SuperViewHolder setEnable(int viewId, boolean enable);

     int getVisibility(int viewId);

    SuperViewHolder setLayouParams(int viewId, ViewGroup.LayoutParams layoutParams) ;

    SuperViewHolder setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener onItemLongClickListener);

    SuperViewHolder removeAllViews(int viewId);

    SuperViewHolder addView(int viewId, View childView);

    SuperViewHolder setTextSize(int viewId, int unit, float textSize);

    SuperViewHolder setTextSize(int viewId, float textSize);
}
