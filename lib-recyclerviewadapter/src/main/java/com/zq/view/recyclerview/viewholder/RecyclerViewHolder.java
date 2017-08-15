package com.zq.view.recyclerview.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CompoundButton;


/** RecyclerView 万能适配器
 * Created by zhangqiang on 16-10-10.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements SuperViewHolder {

    private int layoutId;
    private SuperViewHolder superViewHolder;

    public static RecyclerViewHolder create(Context context, int layoutId, ViewGroup root) {

        View mConvertView = LayoutInflater.from(context).inflate(layoutId, root, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(mConvertView);
        recyclerViewHolder.setLayoutId(layoutId);
        return recyclerViewHolder;
    }

    private RecyclerViewHolder(View itemView) {
        super(itemView);
        superViewHolder = new SuperViewHolderIMPL(itemView);
    }

    public int getLayoutId() {
        return layoutId;
    }

    private void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public <T extends View> T getView(int viewId) {
        return superViewHolder.getView(viewId);
    }

    @Override
    public View getView() {
        return superViewHolder.getView();
    }

    @Override
    public SuperViewHolder setText(int viewId, CharSequence charSequence) {
        return superViewHolder.setText(viewId, charSequence);
    }

    @Override
    public SuperViewHolder setImageResource(int viewId, int imageResource) {
        return superViewHolder.setImageResource(viewId, imageResource);
    }

    @Override
    public SuperViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        return superViewHolder.setImageBitmap(viewId, bitmap);
    }

    @Override
    public SuperViewHolder setImageDrawable(int viewId, Drawable drawable) {
        return superViewHolder.setImageDrawable(viewId, drawable);
    }

    @Override
    public SuperViewHolder setBackgroundColor(int viewId, int color) {
        return superViewHolder.setBackgroundColor(viewId, color);
    }

    @Override
    public SuperViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        return superViewHolder.setBackgroundRes(viewId, backgroundRes);
    }

    @Override
    public SuperViewHolder setTextColor(int viewId, int textColor) {
        return superViewHolder.setTextColor(viewId, textColor);
    }

    @Override
    public SuperViewHolder setTextColorRes(int viewId, int textColorRes) {
        return superViewHolder.setTextColorRes(viewId, textColorRes);
    }

    @Override
    public SuperViewHolder setAlpha(int viewId, float value) {
        return superViewHolder.setAlpha(viewId, value);
    }

    @Override
    public SuperViewHolder setVisible(int viewId, boolean visible) {
        return superViewHolder.setVisible(viewId, visible);
    }

    @Override
    public SuperViewHolder setVisibility(int viewId, int visible) {
        return superViewHolder.setVisibility(viewId, visible);
    }

    @Override
    public SuperViewHolder linkify(int viewId) {
        return superViewHolder.linkify(viewId);
    }

    @Override
    public SuperViewHolder setTypeface(Typeface typeface, int... viewIds) {
        return superViewHolder.setTypeface(typeface, viewIds);
    }

    @Override
    public SuperViewHolder setProgress(int viewId, int progress) {
        return superViewHolder.setProgress(viewId, progress);
    }

    @Override
    public SuperViewHolder setProgress(int viewId, int progress, int max) {
        return superViewHolder.setProgress(viewId, progress, max);
    }

    @Override
    public SuperViewHolder setMax(int viewId, int max) {
        return superViewHolder.setMax(viewId, max);
    }

    @Override
    public SuperViewHolder setRating(int viewId, float rating) {
        return superViewHolder.setRating(viewId, rating);
    }

    @Override
    public SuperViewHolder setRating(int viewId, float rating, int max) {
        return superViewHolder.setRating(viewId, rating, max);
    }

    @Override
    public SuperViewHolder setTag(int viewId, Object tag) {
        return superViewHolder.setTag(viewId, tag);
    }

    @Override
    public SuperViewHolder setTag(int viewId, int key, Object tag) {
        return superViewHolder.setTag(viewId, key, tag);
    }

    @Override
    public SuperViewHolder setChecked(int viewId, boolean checked) {
        return superViewHolder.setChecked(viewId, checked);
    }

    @Override
    public SuperViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        return superViewHolder.setOnClickListener(viewId, listener);
    }

    @Override
    public SuperViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        return superViewHolder.setOnTouchListener(viewId, listener);
    }

    @Override
    public SuperViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        return superViewHolder.setOnLongClickListener(viewId, listener);
    }

    @Override
    public SuperViewHolder setAdapter(int viewId, Adapter adapter) {
        return superViewHolder.setAdapter(viewId, adapter);
    }

    @Override
    public SuperViewHolder setOnItemClickListener(int viewId, AdapterView.OnItemClickListener itemClickListener) {
        return superViewHolder.setOnItemClickListener(viewId, itemClickListener);
    }

    @Override
    public SuperViewHolder setCompoundDrawablePadding(int viewId, int pad) {
        return superViewHolder.setCompoundDrawablePadding(viewId, pad);
    }

    @Override
    public SuperViewHolder setCompoundDrawablesWithIntrinsicBounds(int viewId, Drawable left, Drawable top, Drawable right, Drawable bottom) {
        return superViewHolder.setCompoundDrawablesWithIntrinsicBounds(viewId, left, top, right, bottom);
    }

    @Override
    public SuperViewHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        return superViewHolder.setOnCheckedChangeListener(viewId, onCheckedChangeListener);
    }

    @Override
    public SuperViewHolder setBackgroundResource(int viewId, int resId) {
        return superViewHolder.setBackgroundResource(viewId, resId);
    }

    @Override
    public SuperViewHolder addTextChangedListener(int viewId, TextWatcher textWatcher) {
        return superViewHolder.addTextChangedListener(viewId, textWatcher);
    }

    @Override
    public SuperViewHolder setMovementMethod(int viewId, MovementMethod movement) {
        return superViewHolder.setMovementMethod(viewId, movement);
    }

    @Override
    public SuperViewHolder setEnable(int viewId, boolean enable) {
        return superViewHolder.setEnable(viewId, enable);
    }

    @Override
    public int getVisibility(int viewId) {
        return superViewHolder.getVisibility(viewId);
    }

    @Override
    public SuperViewHolder setLayouParams(int viewId, ViewGroup.LayoutParams layoutParams) {
        return superViewHolder.setLayouParams(viewId, layoutParams);
    }

    @Override
    public SuperViewHolder setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener onItemLongClickListener) {
        return superViewHolder.setOnItemLongClickListener(viewId, onItemLongClickListener);
    }

    @Override
    public SuperViewHolder removeAllViews(int viewId) {
        return superViewHolder.removeAllViews(viewId);
    }

    @Override
    public SuperViewHolder addView(int viewId, View childView) {
        return superViewHolder.addView(viewId, childView);
    }

    @Override
    public SuperViewHolder setTextSize(int viewId, int unit, float textSize) {
        return superViewHolder.setTextSize(viewId, unit, textSize);
    }

    @Override
    public SuperViewHolder setTextSize(int viewId, float textSize) {
        return superViewHolder.setTextSize(viewId, textSize);
    }
}
