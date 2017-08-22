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

public class RVViewHolder extends RecyclerView.ViewHolder implements IViewHolder {

    private int layoutId;
    private IViewHolder viewHolderDelegate;

    public static RVViewHolder create(Context context, int layoutId, ViewGroup root) {

        View mConvertView = LayoutInflater.from(context).inflate(layoutId, root, false);
        RVViewHolder recyclerViewHolder = new RVViewHolder(mConvertView);
        recyclerViewHolder.setLayoutId(layoutId);
        return recyclerViewHolder;
    }

    private RVViewHolder(View itemView) {
        super(itemView);
        setViewHolderDelegate(new ViewHolderImpl(itemView));
    }

    public int getLayoutId() {
        return layoutId;
    }

    private void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public <T extends View> T getView(int viewId) {
        return getViewHolderDelegate().getView(viewId);
    }

    @Override
    public View getView() {
        return getViewHolderDelegate().getView();
    }

    @Override
    public IViewHolder setText(int viewId, CharSequence charSequence) {
        return getViewHolderDelegate().setText(viewId, charSequence);
    }

    @Override
    public IViewHolder setImageResource(int viewId, int imageResource) {
        return getViewHolderDelegate().setImageResource(viewId, imageResource);
    }

    @Override
    public IViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        return getViewHolderDelegate().setImageBitmap(viewId, bitmap);
    }

    @Override
    public IViewHolder setImageDrawable(int viewId, Drawable drawable) {
        return getViewHolderDelegate().setImageDrawable(viewId, drawable);
    }

    @Override
    public IViewHolder setBackgroundColor(int viewId, int color) {
        return getViewHolderDelegate().setBackgroundColor(viewId, color);
    }

    @Override
    public IViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        return getViewHolderDelegate().setBackgroundRes(viewId, backgroundRes);
    }

    @Override
    public IViewHolder setTextColor(int viewId, int textColor) {
        return getViewHolderDelegate().setTextColor(viewId, textColor);
    }

    @Override
    public IViewHolder setTextColorRes(int viewId, int textColorRes) {
        return getViewHolderDelegate().setTextColorRes(viewId, textColorRes);
    }

    @Override
    public IViewHolder setAlpha(int viewId, float value) {
        return getViewHolderDelegate().setAlpha(viewId, value);
    }

    @Override
    public IViewHolder setVisible(int viewId, boolean visible) {
        return getViewHolderDelegate().setVisible(viewId, visible);
    }

    @Override
    public IViewHolder setVisibility(int viewId, int visible) {
        return getViewHolderDelegate().setVisibility(viewId, visible);
    }

    @Override
    public IViewHolder addLinks(int viewId,int mask) {
        return getViewHolderDelegate().addLinks(viewId,mask);
    }

    @Override
    public IViewHolder setTypeface(Typeface typeface, int... viewIds) {
        return getViewHolderDelegate().setTypeface(typeface, viewIds);
    }

    @Override
    public IViewHolder setProgress(int viewId, int progress) {
        return getViewHolderDelegate().setProgress(viewId, progress);
    }

    @Override
    public IViewHolder setProgress(int viewId, int progress, int max) {
        return getViewHolderDelegate().setProgress(viewId, progress, max);
    }

    @Override
    public IViewHolder setMax(int viewId, int max) {
        return getViewHolderDelegate().setMax(viewId, max);
    }

    @Override
    public IViewHolder setRating(int viewId, float rating) {
        return getViewHolderDelegate().setRating(viewId, rating);
    }

    @Override
    public IViewHolder setRating(int viewId, float rating, int max) {
        return getViewHolderDelegate().setRating(viewId, rating, max);
    }

    @Override
    public IViewHolder setTag(int viewId, Object tag) {
        return getViewHolderDelegate().setTag(viewId, tag);
    }

    @Override
    public IViewHolder setTag(int viewId, int key, Object tag) {
        return getViewHolderDelegate().setTag(viewId, key, tag);
    }

    @Override
    public IViewHolder setChecked(int viewId, boolean checked) {
        return getViewHolderDelegate().setChecked(viewId, checked);
    }

    @Override
    public IViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        return getViewHolderDelegate().setOnClickListener(viewId, listener);
    }

    @Override
    public IViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        return getViewHolderDelegate().setOnTouchListener(viewId, listener);
    }

    @Override
    public IViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        return getViewHolderDelegate().setOnLongClickListener(viewId, listener);
    }

    @Override
    public IViewHolder setAdapter(int viewId, Adapter adapter) {
        return getViewHolderDelegate().setAdapter(viewId, adapter);
    }

    @Override
    public IViewHolder setOnItemClickListener(int viewId, AdapterView.OnItemClickListener itemClickListener) {
        return getViewHolderDelegate().setOnItemClickListener(viewId, itemClickListener);
    }

    @Override
    public IViewHolder setCompoundDrawablePadding(int viewId, int pad) {
        return getViewHolderDelegate().setCompoundDrawablePadding(viewId, pad);
    }

    @Override
    public IViewHolder setCompoundDrawablesWithIntrinsicBounds(int viewId, Drawable left, Drawable top, Drawable right, Drawable bottom) {
        return getViewHolderDelegate().setCompoundDrawablesWithIntrinsicBounds(viewId, left, top, right, bottom);
    }

    @Override
    public IViewHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        return getViewHolderDelegate().setOnCheckedChangeListener(viewId, onCheckedChangeListener);
    }

    @Override
    public IViewHolder setBackgroundResource(int viewId, int resId) {
        return getViewHolderDelegate().setBackgroundResource(viewId, resId);
    }

    @Override
    public IViewHolder addTextChangedListener(int viewId, TextWatcher textWatcher) {
        return getViewHolderDelegate().addTextChangedListener(viewId, textWatcher);
    }

    @Override
    public IViewHolder setMovementMethod(int viewId, MovementMethod movement) {
        return getViewHolderDelegate().setMovementMethod(viewId, movement);
    }

    @Override
    public IViewHolder setEnable(int viewId, boolean enable) {
        return getViewHolderDelegate().setEnable(viewId, enable);
    }

    @Override
    public int getVisibility(int viewId) {
        return getViewHolderDelegate().getVisibility(viewId);
    }

    @Override
    public IViewHolder setLayouParams(int viewId, ViewGroup.LayoutParams layoutParams) {
        return getViewHolderDelegate().setLayouParams(viewId, layoutParams);
    }

    @Override
    public IViewHolder setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener onItemLongClickListener) {
        return getViewHolderDelegate().setOnItemLongClickListener(viewId, onItemLongClickListener);
    }

    @Override
    public IViewHolder removeAllViews(int viewId) {
        return getViewHolderDelegate().removeAllViews(viewId);
    }

    @Override
    public IViewHolder addView(int viewId, View childView) {
        return getViewHolderDelegate().addView(viewId, childView);
    }

    @Override
    public IViewHolder setTextSize(int viewId, int unit, float textSize) {
        return getViewHolderDelegate().setTextSize(viewId, unit, textSize);
    }

    @Override
    public IViewHolder setTextSize(int viewId, float textSize) {
        return getViewHolderDelegate().setTextSize(viewId, textSize);
    }

    public IViewHolder getViewHolderDelegate() {
        return viewHolderDelegate;
    }

    public void setViewHolderDelegate(IViewHolder viewHolderDelegate) {
        this.viewHolderDelegate = viewHolderDelegate;
    }
}
