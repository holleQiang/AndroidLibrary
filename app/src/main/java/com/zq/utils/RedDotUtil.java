package com.zq.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zq.R;

/**
 * 小红点工具类
 * Created by zhangqiang on 2017/10/18.
 */

public class RedDotUtil {

    public static void attachToView(@NonNull View targetView, int number, int gravity) {

        Context context = targetView.getContext();
        attachToView(targetView, number, gravity,new TextNumberViewAdapter(context));
    }

    public static void attachToView(View targetView,int number) {

        attachToView(targetView, number, Gravity.END | Gravity.TOP);
    }

    public static <V extends View> void attachToView(View targetView,int number,NumberViewAdapter<V> numberViewAdapter) {

        attachToView(targetView, number, Gravity.END | Gravity.TOP,numberViewAdapter);
    }


    /**
     * 清除目标view上的红点
     *
     * @param targetView 目标view
     */
    public static void clear(View targetView) {

        ViewParent viewParent = targetView.getParent();
        if (viewParent == null || !(viewParent instanceof FrameLayout)) {
            return;
        }

        FrameLayout frameParent = (FrameLayout) viewParent;

        final int childCount = frameParent.getChildCount();
        if (childCount <= 1) {
            return;
        }

        for (int i = 0; i < childCount; i++) {

            View childView = frameParent.getChildAt(i);
            if (childView.getId() == R.id.red_point_text_id) {

                ((FrameLayout) viewParent).removeView(childView);
                return;
            }
        }
    }

    public interface NumberViewAdapter<V extends View> {

        V getView();

        void onNumberChange(V view, int number);
    }

    public static <V extends View> void attachToView(@NonNull View targetView, int number, int gravity, NumberViewAdapter<V> adapter) {

        Context context = targetView.getContext();
        ViewParent viewParent = targetView.getParent();
        if (viewParent == null) {
            throw new NullPointerException("targetView dose not have parent");
        }

        if (viewParent instanceof FrameLayout) {

            FrameLayout frameParent = (FrameLayout) viewParent;

            final int childCount = frameParent.getChildCount();
            if (childCount > 1) {

                for (int i = 0; i < childCount; i++) {

                    View childView = frameParent.getChildAt(i);
                    if (childView.getId() == R.id.red_point_text_id) {

                        adapter.onNumberChange((V) childView, number);
                        return;
                    }
                }
            }
            V view = adapter.getView();
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = gravity;
            view.setLayoutParams(layoutParams);
            view.setId(R.id.red_point_text_id);
            adapter.onNumberChange(view, number);
            frameParent.addView(view);
        } else {


            ViewGroup viewGroup = (ViewGroup) viewParent;
            int index = viewGroup.indexOfChild(targetView);
            viewGroup.removeViewAt(index);

            FrameLayout wrapperFrameLayout = new FrameLayout(context);
            wrapperFrameLayout.setLayoutParams(targetView.getLayoutParams());
            wrapperFrameLayout.addView(targetView);

            V view = adapter.getView();
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = gravity;
            view.setLayoutParams(layoutParams);
            view.setId(R.id.red_point_text_id);
            adapter.onNumberChange(view, number);
            wrapperFrameLayout.addView(view);

            viewGroup.addView(wrapperFrameLayout, index);
        }
    }

    private static class TextNumberViewAdapter implements NumberViewAdapter<TextView> {

        private Context context;

        TextNumberViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public TextView getView() {

            TextView textView = new TextView(context);
            float density = context.getResources().getDisplayMetrics().density;

            int horizontalPadding = (int) (density * 6 + 0.5f);
            int verticalPadding = (int) (density * 0 + 0.5f);
            textView.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
            textView.setBackgroundResource(R.drawable.bg_red_point);
            textView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                    int viewHeight = v.getHeight();
                    if (viewHeight > 0) {

                        float corner = viewHeight / 2f;
                        GradientDrawable drawable = (GradientDrawable) v.getBackground();
                        drawable.setCornerRadii(new float[]{corner, corner, corner, corner, corner, corner, corner, corner});
                    }
                }
            });
            return textView;
        }

        @Override
        public void onNumberChange(TextView view, int number) {
            view.setText(number + "");
        }
    }
}
