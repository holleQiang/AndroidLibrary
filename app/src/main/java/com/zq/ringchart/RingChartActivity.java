package com.zq.ringchart;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zq.R;
import com.zq.widget.ringchart.ArcItem;
import com.zq.widget.ringchart.RingChartView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 2017/10/18.
 */

public class RingChartActivity extends AppCompatActivity {


    @BindView(R.id.ring_chart_view)
    RingChartView ringChartView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_chart);
        ButterKnife.bind(this);

        new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                List<ArcItem> arcItemList = new ArrayList<>();
                for (int i = 0; i < 5; i++) {

                    ArcItem arcItem = new ArcItem();
                    arcItem.setWeight((int) (Math.random() * 100) + 1);
                    arcItem.setText("index:" + i);
                    arcItem.setTextColor(Color.WHITE);
                    arcItem.setColor(Color.argb(255, (int) (255 * Math.random()), (int) (255 * Math.random()), (int) (255 * Math.random())));
                    arcItemList.add(arcItem);
                }
                ringChartView.setArcItemList(arcItemList);

                if (!isFinishing()) {
                    sendEmptyMessageDelayed(0, 2000);
                }
            }
        }.sendEmptyMessage(0);


    }
}
