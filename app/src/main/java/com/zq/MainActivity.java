package com.zq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zq.behavior.BehaviorActivity;
import com.zq.gesturepassword.GesturePasswordActivity;
import com.zq.histogram.HistogramActivity;
import com.zq.hscrollview.HorizontalScrollViewInRVActivity;
import com.zq.htmltext.HtmlTextActivity;
import com.zq.jsbridge.JsBridgeActivity;
import com.zq.linechart.LineChartActivity;
import com.zq.lrc.LrcActivity;
import com.zq.pageradapter.FragmentPagerAdapterTestActivity;
import com.zq.redpoint.RedPointActivity;
import com.zq.ringchart.RingChartActivity;
import com.zq.rotateanim.RotateViewActivity;
import com.zq.utils.ViewUtil;
import com.zq.view.recyclerview.adapter.OnItemClickListener;
import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.view.recyclerview.adapter.cell.DataBinder;
import com.zq.view.recyclerview.adapter.cell.MultiCell;
import com.zq.view.recyclerview.divider.RVItemDivider;
import com.zq.view.recyclerview.item.HorizontalSlidLayout;
import com.zq.view.recyclerview.utils.RVUtil;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {


    CellAdapter cellAdapter;
    boolean change;
    @BindView(R.id.m_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RVUtil.setChangeAnimationEnable(mRecyclerView, false);
        cellAdapter = new CellAdapter(this);
        cellAdapter.addDataAtLast(MultiCell.convert(R.layout.item_text, "图表", dataBinder));
        cellAdapter.addDataAtLast(MultiCell.convert(R.layout.item_text, "柱状图", dataBinder));
        cellAdapter.addDataAtLast(MultiCell.convert(R.layout.item_text, "歌词", dataBinder));
        cellAdapter.addDataAtLast(MultiCell.convert(R.layout.item_text, "RecyclerView嵌套RecyclerView", dataBinder));
        cellAdapter.addDataAtLast(MultiCell.convert(R.layout.item_text, "JsBridge测试", dataBinder));
        cellAdapter.addDataAtLast(MultiCell.convert(R.layout.item_text, "小红点", dataBinder));
        cellAdapter.addDataAtLast(MultiCell.convert(R.layout.item_text, "Behavior", dataBinder));
        cellAdapter.addDataAtLast(MultiCell.convert(R.layout.item_text, "手势密码", dataBinder));
        cellAdapter.addDataAtLast(MultiCell.convert(R.layout.item_text, "环形图", dataBinder));
        cellAdapter.addDataAtLast(MultiCell.convert(R.layout.item_text, "View翻转", dataBinder));
        cellAdapter.addDataAtLast(MultiCell.convert(R.layout.item_text, "ViewPager测试", dataBinder));
        cellAdapter.addDataAtLast(MultiCell.convert(R.layout.item_text, "htmlText", dataBinder));
        cellAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(cellAdapter);
        mRecyclerView.addItemDecoration(new RVItemDivider(getResources().getColor(R.color.colorPrimary), ViewUtil.dp2px(this, 5)));
    }

    private DataBinder<String> dataBinder = new DataBinder<String>() {
        @Override
        public void bindData(RVViewHolder viewHolder, String data) {
//            viewHolder.setText(R.id.textView, viewHolder.getAdapterPosition() + "、" + data);
            viewHolder.setText(R.id.textView, data);
            HorizontalSlidLayout slidLayout = viewHolder.getView(R.id.m_horizontal_slid);
            slidLayout.reset();
            slidLayout.setOnSlidListener(new HorizontalSlidLayout.OnSlidListener() {
                @Override
                public void onStateChange(int state) {

                }

                @Override
                public void onViewSelect(int location) {

                    Log.i("Test","===============" + location);
                }

                @Override
                public void onViewTransfer(View view, float translate) {

                    float alpha = 0f;
                    if(0 <= translate && translate <= 1){

                        alpha = 1 - translate;
                    }else if(-1 <= translate && translate <= 0){

                        alpha = translate + 1;
                    }
                    view.setAlpha(alpha);
                }
            });
            viewHolder.setOnClickListener(R.id.left_view, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.setOnClickListener(R.id.right_view, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.setOnClickListener(R.id.view_center, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "view_center", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, int position) {

        MultiCell multiCell = (MultiCell) cellAdapter.getDataAt(position);
        String fun = (String) multiCell.getData();
        if (fun.equals("图表")) {

            startActivity(new Intent(MainActivity.this, LineChartActivity.class));
        } else if ("柱状图".equals(fun)) {

            startActivity(new Intent(MainActivity.this, HistogramActivity.class));
        } else if ("歌词".equals(fun)) {

            startActivity(new Intent(MainActivity.this, LrcActivity.class));
        } else if ("RecyclerView嵌套RecyclerView".equals(fun)) {

            startActivity(new Intent(MainActivity.this, HorizontalScrollViewInRVActivity.class));
        } else if ("JsBridge测试".equals(fun)) {

            startActivity(new Intent(MainActivity.this, JsBridgeActivity.class));
        } else if ("小红点".equals(fun)) {

            startActivity(new Intent(MainActivity.this, RedPointActivity.class));
        } else if ("Behavior".equals(fun)) {

            startActivity(new Intent(MainActivity.this, BehaviorActivity.class));
        } else if ("手势密码".equals(fun)) {

            startActivity(new Intent(MainActivity.this, GesturePasswordActivity.class));
        } else if ("环形图".equals(fun)) {
            startActivity(new Intent(MainActivity.this, RingChartActivity.class));
        } else if ("View翻转".equals(fun)) {
            startActivity(new Intent(MainActivity.this, RotateViewActivity.class));
        }else if ("ViewPager测试".equals(fun)) {
            startActivity(new Intent(MainActivity.this, FragmentPagerAdapterTestActivity.class));
        }else if ("htmlText".equals(fun)) {
            startActivity(new Intent(MainActivity.this, HtmlTextActivity.class));
        }
    }
}
