package com.zq.linechart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zq.R;
import com.zq.widget.AxisFrameView;
import com.zq.widget.linechart.line.Line;
import com.zq.widget.linechart.LineChartView;
import com.zq.widget.linechart.point.Point;
import com.zq.widget.linechart.point.PointImpl;
import com.zq.widget.axis.XAxis;
import com.zq.widget.axis.YAxis;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhangqiang on 2017/9/25.
 */

public class LineChartActivity extends AppCompatActivity {

    @InjectView(R.id.axis_frame_view)
    AxisFrameView axisFrameView;
    @InjectView(R.id.line_chart_view2)
    LineChartView lineChartView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        ButterKnife.inject(this);


        List<Line> lineList = new ArrayList<>();
//        lineList.add(makeLine(Color.RED,10,false,0,0));
        lineList.add(makeLine(Color.MAGENTA,1,true,Color.parseColor("#2031b14d"),Color.parseColor("#9032b14d")));
        lineList.add(makeLine(Color.GREEN,5,true,Color.parseColor("#201b1b1b"),Color.parseColor("#901b1b1b")));
//        lineList.add(makeLine());
//        lineList.add(makeLine());
//        lineList.add(makeLine());

        axisFrameView.setXAxis(makeXAxis());
        axisFrameView.setYAxis(makeYAxis2());

        lineChartView2.setXAxis(makeXAxis());
        lineChartView2.setYAxis(makeYAxis());
        lineChartView2.setLineList(lineList);
    }

    private XAxis makeXAxis() {

        XAxis xAxis = new XAxis();
        xAxis.setMinValue(0);
        xAxis.setMaxValue(1000);
        List<XAxis.Item> xItemList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            XAxis.Item xItem = new XAxis.Item();
            xItem.setValue((int) (1000 / 10 * 0.5 + 1000 / 10 * i));
            xItem.setDrawText("X" + i);
            xItemList.add(xItem);
        }
        xAxis.setItems(xItemList);
        return xAxis;
    }


    private XAxis makeXAxis2() {

        XAxis xAxis = new XAxis();
        xAxis.setMinValue(0);
        xAxis.setMaxValue(1000);
        List<XAxis.Item> xItemList = new ArrayList<>();
        XAxis.Item xItem = new XAxis.Item();
        xItem.setValue(0);
        xItem.setDrawText("5月");
        xItemList.add(xItem);

        XAxis.Item xItem2 = new XAxis.Item();
        xItem2.setValue(500);
        xItem2.setDrawText("6月");
        xItemList.add(xItem2);

        XAxis.Item xItem3 = new XAxis.Item();
        xItem3.setValue(1000);
        xItem3.setDrawText("7月");
        xItemList.add(xItem3);

        xAxis.setItems(xItemList);
        return xAxis;
    }

    private YAxis makeYAxis() {

        YAxis yAxis = new YAxis();
        yAxis.setMinValue(0);
        yAxis.setMaxValue(1000);
        List<YAxis.Item> yItemList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            YAxis.Item yItem = new YAxis.Item();
            yItem.setValue((int) (1000 / 10 * 0.5 + 1000 / 10 * i));
            yItem.setDrawText("Y" + i);
            yItemList.add(yItem);
        }
        yAxis.setItems(yItemList);
        return yAxis;
    }

    private Line makeLine(int lineColor,int lineWidth,boolean fillAreaVisible,int fillStartColor,int fillEndColor) {

        Line line = new Line();
        line.setLineColor(lineColor);
        line.setLineWidth(lineWidth);
        line.setFillAreaVisible(fillAreaVisible);
        line.setFillStartColor(fillStartColor);
        line.setFillEndColor(fillEndColor);
        List<Point> pointList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            PointImpl point = new PointImpl();
            point.setxValue((int) (100 * i + 100 * Math.random()));
            point.setyValue((int) (500 + 400 * Math.random() - 200));
            pointList.add(point);
        }
        line.setPointList(pointList);
        return line;
    }


    private YAxis makeYAxis2() {

        YAxis yAxis = new YAxis();
        yAxis.setMinValue(0);
        yAxis.setMaxValue(1000);
        List<YAxis.Item> yItemList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            YAxis.Item yItem = new YAxis.Item();
            yItem.setValue((int) (1000 / 10 * 0.5 + 1000 / 10 * i));
            yItem.setDrawText(i + "%");
            yItemList.add(yItem);
        }
        yAxis.setItems(yItemList);
        return yAxis;
    }
}
