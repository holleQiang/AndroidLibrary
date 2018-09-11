package com.zq.utils;

import android.content.Context;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.widget.TextView;

import com.zq.utils.text.FixedLinkMovementMethod;
import com.zq.utils.text.HtmlTextHelper;

/**
 * Created by zhangqiang on 2018/7/19.
 */
public class ViewUtil {

    /**
     * dp转化为px
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp){

        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5f);
    }

    /**
     * 为textView设置html文本
     */
    public static void setHtmlText(TextView textView, String source) {

        setHtmlText(textView, source, null);
    }

    /**
     * 为textView设置html文本
     */
    public static void setHtmlText(TextView textView, String source, com.caiyi.fund.trade.widget.span.URLSpanWrapper.OnClickListener onClickListener) {

        if (TextUtils.isEmpty(source)) {
            textView.setText(source);
            return;
        }
        Spanned spanned = new HtmlTextHelper().fromHtml(source, textView.getContext(), onClickListener);
        if (spanned != null) {
            Object[] spans = spanned.getSpans(0, spanned.length(), ClickableSpan.class);
            if (spans != null && spans.length > 0) {
                textView.setMovementMethod(FixedLinkMovementMethod.getInstance());
            }
        }
        textView.setText(spanned);
    }

}
