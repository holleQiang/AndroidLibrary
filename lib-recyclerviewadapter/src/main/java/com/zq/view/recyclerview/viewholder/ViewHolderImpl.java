package com.zq.view.recyclerview.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.text.method.MovementMethod;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by zhangqiang on 17-6-30.
 */

public class SuperViewHolderIMPL implements RVViewHolder {

    private SparseArray<View> views = new SparseArray<>();

    private View contentView;

    private Context context;

    public SuperViewHolderIMPL(View contentView) {
        this.contentView = contentView;
        this.context = contentView.getContext();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {

        View view = views.get(viewId);

        if (view == null) {

            view = getView().findViewById(viewId);
            if (view != null) {

                views.put(viewId, view);
            }
        }
        return (T) view;
    }

    @Override
    public View getView() {
        return contentView;
    }

    @Override
    public RVViewHolder setText(int viewId, CharSequence charSequence) {

        TextView textView = getView(viewId);
        textView.setText(charSequence);
        return this;
    }

    @Override
    public RVViewHolder setImageResource(int viewId, int imageResource) {

        ImageView imageView = getView(viewId);
        imageView.setImageResource(imageResource);
        return this;
    }

    @Override
    public RVViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    @Override
    public RVViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    @Override
    public RVViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public RVViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    @Override
    public RVViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    @Override
    public RVViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(context.getResources().getColor(textColorRes));
        return this;
    }

    @Override
    public RVViewHolder setAlpha(int viewId, float value) {

        getView(viewId).setAlpha(value);
        return this;
    }

    @Override
    public RVViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    @Override
    public RVViewHolder setVisibility(int viewId, int visible) {
        View view = getView(viewId);
        view.setVisibility(visible);
        return this;
    }

    @Override
    public RVViewHolder addLinks(int viewId,int mask) {
        TextView view = getView(viewId);
//        Linkify.addLinks(view, Linkify.ALL);
        Linkify.addLinks(view, mask);
        return this;
    }

    @Override
    public RVViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    @Override
    public RVViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    @Override
    public RVViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    @Override
    public RVViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    @Override
    public RVViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    @Override
    public RVViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    @Override
    public RVViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    @Override
    public RVViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    @Override
    public RVViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 关于事件的
     */
    @Override
    public RVViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    @Override
    public RVViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    @Override
    public RVViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    @Override
    public RVViewHolder setAdapter(int viewId, Adapter adapter){

        AdapterView<Adapter> adapterView = getView(viewId);
        adapterView.setAdapter(adapter);
        return this;
    }

    @Override
    public RVViewHolder setOnItemClickListener(int viewId, AdapterView.OnItemClickListener itemClickListener){

        AdapterView<Adapter> adapterView = getView(viewId);
        adapterView.setOnItemClickListener(itemClickListener);
        return this;
    }

    @Override
    public RVViewHolder setCompoundDrawablePadding(int viewId, int pad){

        TextView textView = getView(viewId);
        textView.setCompoundDrawablePadding(pad);
        return this;
    }

    @Override
    public RVViewHolder setCompoundDrawablesWithIntrinsicBounds(int viewId, Drawable left, Drawable top, Drawable right, Drawable bottom) {

        TextView textView = getView(viewId);
        textView.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        return this;
    }

    @Override
    public RVViewHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener onCheckedChangeListener){

        CompoundButton checkBox = getView(viewId);
        checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
        return this;
    }

    @Override
    public RVViewHolder setBackgroundResource(int viewId, int resId) {

        View v = getView(viewId);
        v.setBackgroundResource(resId);
        return this;
    }

    @Override
    public RVViewHolder addTextChangedListener(int viewId, TextWatcher textWatcher) {

        EditText v = getView(viewId);
        v.addTextChangedListener(textWatcher);
        return this;
    }

    @Override
    public RVViewHolder setMovementMethod(int viewId, MovementMethod movement) {

        TextView textView = getView(viewId);
        textView.setMovementMethod(movement);
        return this;
    }

    @Override
    public RVViewHolder setEnable(int viewId, boolean enable) {

        getView(viewId).setEnabled(enable);
        return this;
    }

    @Override
    public int getVisibility(int viewId) {

        return getView(viewId).getVisibility();
    }

    @Override
    public RVViewHolder setLayouParams(int viewId, ViewGroup.LayoutParams layoutParams) {

        getView(viewId).setLayoutParams(layoutParams);
        return this;
    }

    @Override
    public RVViewHolder setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener onItemLongClickListener) {

        AdapterView adapterView = getView(viewId);
        adapterView.setOnItemLongClickListener(onItemLongClickListener);
        return this;
    }

    @Override
    public RVViewHolder removeAllViews(int viewId) {

        ViewGroup viewGroup = getView(viewId);
        viewGroup.removeAllViews();
        return this;
    }

    @Override
    public RVViewHolder addView(int viewId, View childView) {

        ViewGroup viewGroup = getView(viewId);
        viewGroup.addView(childView);
        return this;
    }

    @Override
    public RVViewHolder setTextSize(int viewId, int unit, float textSize) {

        TextView textView = getView(viewId);
        textView.setTextSize(unit,textSize);
        return this;
    }

    @Override
    public RVViewHolder setTextSize(int viewId, float textSize) {

        TextView textView = getView(viewId);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        return this;
    }


}
