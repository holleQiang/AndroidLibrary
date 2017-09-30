package com.zq.histogram;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zq.R;
import com.zq.widget.axis.XAxis;
import com.zq.widget.axis.YAxis;
import com.zq.widget.histogram.HistogramView;
import com.zq.widget.histogram.rect.Rect;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhangqiang on 2017/9/30.
 */

public class HistogramActivity extends AppCompatActivity {

    @InjectView(R.id.histogram_view)
    HistogramView histogramView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram);
        ButterKnife.inject(this);

        histogramView.setXAxis(makeXAxis());
        histogramView.setYAxis(makeYAxis());
        histogramView.setRectList(makeRectList());
    }


    private List<Rect> makeRectList() {

        List<Rect> rectList = new ArrayList<>();

        for (int i = 1; i < 10; i++) {

            rectList.add(makeRect(i * 2));
        }

        return rectList;
    }

    private Rect makeRect(int x) {

        Rect rect = new Rect();
        rect.setxValue(x);
        rect.setyValue(getRandomY());
        return rect;
    }

    private int getRandomY() {

        int value = (int) (-Math.random() * 1000 + 500);
        if (Math.abs(value) < 300) {
            return getRandomY();
        }
        return value;
    }

    private XAxis makeXAxis() {

        XAxis xAxis = new XAxis();
        xAxis.setMinValue(0);
        xAxis.setMaxValue(20);
        List<XAxis.Item> xItemList = new ArrayList<>();
        for (int i = 1; i < 4; i++) {

            XAxis.Item xItem = new XAxis.Item();
            xItem.setValue(6 * i);
            xItem.setDrawText("X" + i);
            xItemList.add(xItem);
        }
        xAxis.setItems(xItemList);
        return xAxis;
    }


    private YAxis makeYAxis() {

        YAxis yAxis = new YAxis();
        yAxis.setMinValue(-500);
        yAxis.setMaxValue(500);
        List<YAxis.Item> yItemList = new ArrayList<>();
        for (int i = -4; i < 5; i++) {

            YAxis.Item yItem = new YAxis.Item();
            yItem.setValue(i * 100);
            yItem.setDrawText("Y" + i);
            yItemList.add(yItem);
        }
        yAxis.setItems(yItemList);
        return yAxis;
    }
}
