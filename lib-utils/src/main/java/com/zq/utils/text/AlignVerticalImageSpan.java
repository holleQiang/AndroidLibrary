package com.caiyi.fund.trade.widget.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;

import java.lang.ref.WeakReference;

/**
 * 纵向居中的imageSpan
 * Created by zhangqiang on 2017/11/9.
 */

public class AlignVerticalImageSpan extends ImageSpan {

    public static final int ALIGN_CENTER_VETICAL = 2;

    public AlignVerticalImageSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public AlignVerticalImageSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    public AlignVerticalImageSpan(Drawable d) {
        super(d);
    }

    public AlignVerticalImageSpan(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    public AlignVerticalImageSpan(Drawable d, String source) {
        super(d, source);
    }

    public AlignVerticalImageSpan(Drawable d, String source, int verticalAlignment) {
        super(d, source, verticalAlignment);
    }

    public AlignVerticalImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    public AlignVerticalImageSpan(Context context, Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }

    public AlignVerticalImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    public AlignVerticalImageSpan(Context context, int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text,
                     int start, int end, float x,
                     int top, int y, int bottom, Paint paint) {
        Drawable b = getCachedDrawable();
        canvas.save();

        int transY = bottom - b.getBounds().bottom;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        }else if(mVerticalAlignment == ALIGN_CENTER_VETICAL){
            transY = (int) (y + paint.ascent() + (paint.descent() - paint.ascent() - b.getBounds().height())/2);
        }

        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }

    private Drawable getCachedDrawable() {
        WeakReference<Drawable> wr = mDrawableRef;
        Drawable d = null;

        if (wr != null)
            d = wr.get();

        if (d == null) {
            d = getDrawable();
            mDrawableRef = new WeakReference<>(d);
        }

        return d;
    }

    private WeakReference<Drawable> mDrawableRef;
}
