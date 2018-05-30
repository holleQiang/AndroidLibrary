package com.zq.func.htmltext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import com.zq.R;

import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 2017/11/2.
 */

public class HtmlTextActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_text);
        ButterKnife.bind(this);

        textView.setMovementMethod(LinkMovementMethod.getInstance());

        String source = "变更后：<font color='#3a93d8'><a href='youyufund://webLink/openFundManager?managerId=101000971 '>张胜记</a></font><font color='#3a93d8'><a href='youyufund://webLink/openFundManager?managerId=101004754 '>范冰</a></font><br>变更前：<font color='#3a93d8'><a href='youyufund://webLink/openFundManager?managerId=101000971 '>张胜记</a></font>";

        textView.setText(Html.fromHtml(source, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(final String source) {

                new AsyncTask<String,Integer,Drawable>(){
                    @Override
                    protected Drawable doInBackground(String... strings) {

                        try {
                            HttpURLConnection urlConnection = (HttpURLConnection) new URL(strings[0]).openConnection();
                            InputStream is = urlConnection.getInputStream();

                            Bitmap bitmap = BitmapFactory.decodeStream(is);

                            is.close();

                            return new BitmapDrawable(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Drawable drawable) {
                        drawable.setBounds(0,0, (int) (drawable.getIntrinsicWidth() * getResources().getDisplayMetrics().density), (int) (drawable.getIntrinsicHeight()* getResources().getDisplayMetrics().density));
                        Spannable spannable = (Spannable) textView.getText();
                        ImageSpan[] imageSpans = spannable.getSpans(0,spannable.length(), ImageSpan.class);
                        for (ImageSpan imageSpan:
                                imageSpans) {

                            if(source.equals(imageSpan.getSource())){

                                int start = spannable.getSpanStart(imageSpan);
                                int end = spannable.getSpanEnd(imageSpan);
                                spannable.removeSpan(imageSpan);
                                spannable.setSpan(new ImageSpan(drawable),start,end,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            }
                        }
                    }
                }.execute(source);
                Drawable drawable = getResources().getDrawable(R.drawable.bg_test);
                drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                return drawable;
            }
        }, new Html.TagHandler() {
            @Override
            public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

                Log.i("Test","==========" + opening + "==tag==" + tag);
            }
        }));
    }
}
