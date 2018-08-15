package com.caiyi.fund.trade.widget.html;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.URLSpan;

import com.caiyi.fund.trade.R;
import com.caiyi.fund.trade.utils.ResourceUtil;
import com.caiyi.fund.trade.widget.span.AlignVerticalImageSpan;
import com.caiyi.fund.trade.widget.span.URLSpanWrapper;

import org.xml.sax.XMLReader;

/**
 * html文本辅助类
 * Created by zhangqiang on 2017/11/7.
 */

public class HtmlTextHelper {

    /**
     * 转换
     *
     * @param source          文本内容
     * @param context         上下文
     * @return 转换之后的
     */
    public Spanned fromHtml(String source, Context context){

        return fromHtml(source, context,null);
    }

    /**
     * 转换
     *
     * @param source          文本内容
     * @param context         上下文
     * @param onClickListener urlSpan点击事件拦截监听器，设置了这个，urlspan自身的点击事件将不起作用！
     * @return 转换之后的
     */
    public Spanned fromHtml(String source, Context context, URLSpanWrapper.OnClickListener onClickListener) {

        Spanned spanned = Html.fromHtml(source, new InternalImageGetter(context), new InternalTagHandler());
        if (spanned instanceof SpannableStringBuilder) {

            final int length = spanned.length();

            SpannableStringBuilder spannable = (SpannableStringBuilder) spanned;
            URLSpan[] urlSpans = spannable.getSpans(0, length, URLSpan.class);
            if (urlSpans != null) {

                for (URLSpan urlSpan :
                        urlSpans) {

                    URLSpanWrapper superURLSpan = new URLSpanWrapper(urlSpan.getURL());
                    superURLSpan.setWrapper(urlSpan);
                    superURLSpan.setOnClickListener(onClickListener);
                    int start = spannable.getSpanStart(urlSpan);
                    int end = spannable.getSpanEnd(urlSpan);

                    spannable.setSpan(superURLSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannable.removeSpan(urlSpan);

                    ForegroundColorSpan[] foregroundColorSpans = spannable.getSpans(start, end, ForegroundColorSpan.class);
                    if (foregroundColorSpans != null) {
                        for (ForegroundColorSpan foregroundColorSpan :
                                foregroundColorSpans) {

                            ForegroundColorSpan newForegroundColorSpan = new ForegroundColorSpan(foregroundColorSpan.getForegroundColor());
                            spannable.setSpan(newForegroundColorSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            spannable.removeSpan(foregroundColorSpan);
                        }
                    }
                }
            }
            ImageSpan[] imageSpans = spannable.getSpans(0, length, ImageSpan.class);
            if (imageSpans != null) {

                for (ImageSpan imageSpan :
                        imageSpans) {

                    int start = spannable.getSpanStart(imageSpan);
                    int end = spannable.getSpanEnd(imageSpan);
                    String imageSource = imageSpan.getSource();
                    Uri uri = Uri.parse(imageSource);
                    String align = uri.getQueryParameter("align");
                    if (TextUtils.isEmpty(align)) {
                        align = "bottom";
                    }
                    ImageSpan newImageSpan = null;

                    if (align.equalsIgnoreCase("bottom")) {

                        newImageSpan = imageSpan;
                    } else if (align.equalsIgnoreCase("baseLine")) {

                        newImageSpan = new ImageSpan(imageSpan.getDrawable(), AlignVerticalImageSpan.ALIGN_BASELINE);
                    } else if (align.equalsIgnoreCase("centerVertical")) {

                        newImageSpan = new AlignVerticalImageSpan(imageSpan.getDrawable(), AlignVerticalImageSpan.ALIGN_CENTER_VETICAL);
                    }
                    if (newImageSpan != null) {
                        spannable.setSpan(newImageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        spannable.removeSpan(imageSpan);
                    }
                }
            }
        }
        return spanned;
    }

    /**
     * 获取图片的接口
     */
    class InternalImageGetter implements Html.ImageGetter {

        private Context context;

        InternalImageGetter(Context context) {
            this.context = context;
        }

        @Override
        public Drawable getDrawable(String source) {

            Uri data = Uri.parse(source);
            if ("android_drawable".equals(data.getScheme())) {

                return loadAndroidDrawable(data);
            }else if("android_drawable_res".equals(data.getScheme())){

                return loadAndroidDrawableResource(data);
            } else {
                return ContextCompat.getDrawable(context, R.mipmap.ic_launcher).mutate();
            }
        }

        private Drawable loadAndroidDrawable(Uri data) {

            String drawableName = data.getHost();
            int drawableId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
            if (drawableId == 0) {
               return null;
            }
            final Drawable drawable;
            String colorStr = data.getQueryParameter("color");
            if (!TextUtils.isEmpty(colorStr)) {
                int color = Integer.valueOf(colorStr);
                drawable = ResourceUtil.getDrawableWithFillIntColor(context, drawableId, color);
            } else {
                drawable = ContextCompat.getDrawable(context, drawableId);
            }
            if(drawable == null){
                return null;
            }
            Rect bounds = drawable.getBounds();
            if (bounds.isEmpty()) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
            return drawable;
        }

        private Drawable loadAndroidDrawableResource(Uri data) {

            String drawableResId = data.getHost();
            int drawableId = 0;
            try {
                drawableId = Integer.parseInt(drawableResId);
            }catch (Throwable throwable){
                throwable.printStackTrace();
            }
            if (drawableId == 0) {
                return null;
            }
            final Drawable drawable;
            String colorStr = data.getQueryParameter("color");
            if (!TextUtils.isEmpty(colorStr)) {
                int color = Integer.valueOf(colorStr);
                drawable = ResourceUtil.getDrawableWithFillIntColor(context, drawableId, color);
            } else {
                drawable = ContextCompat.getDrawable(context, drawableId);
            }
            if(drawable == null){
                return null;
            }
            Rect bounds = drawable.getBounds();
            if (bounds.isEmpty()) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
            return drawable;
        }
    }


    class InternalTagHandler implements Html.TagHandler {

        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

            if (opening) {

                if (tag.equalsIgnoreCase("del")) {

                    start(output, new Strikethrough());
                }
            } else {

                if (tag.equalsIgnoreCase("del")) {

                    end(output, Strikethrough.class, new StrikethroughSpan());
                }
            }
        }
    }


    /**
     * 删除线标志对象
     */
    class DeleteLine {
    }

    class Strikethrough {
    }

    class AbsoluteSize{

        int dipSize;

        public AbsoluteSize(int dipSize) {
            this.dipSize = dipSize;
        }
    }

    /**
     * 添加tag起始的标记
     *
     * @param text
     * @param mark 标记对象
     */
    private static void start(Editable text, Object mark) {
        int len = text.length();
        text.setSpan(mark, len, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    /**
     * tag结束时调用，从起始的标记位置，到当前位置添加span
     *
     * @param text
     * @param kind 标记的类
     * @param repl 要添加的span
     */
    private static void end(Editable text, Class kind, Object repl) {
        Object obj = getLast(text, kind);
        if (obj != null) {
            setSpanFromMark(text, obj, repl);
        }
    }

    /**
     * 从后往前，获取指定类的第一个span
     *
     * @param text
     * @param kind span类型
     * @return
     */
    private static <T> T getLast(Spanned text, Class<T> kind) {
        /*
         * This knows that the last returned object from getSpans()
         * will be the most recently added.
         */
        T[] objs = text.getSpans(0, text.length(), kind);

        if (objs.length == 0) {
            return null;
        } else {
            return objs[objs.length - 1];
        }
    }

    /**
     * 根据标志设置span
     *
     * @param text
     * @param mark
     * @param spans
     */
    private static void setSpanFromMark(Spannable text, Object mark, Object... spans) {
        int where = text.getSpanStart(mark);
        text.removeSpan(mark);
        int len = text.length();
        if (where != len) {
            for (Object span : spans) {
                text.setSpan(span, where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
}
