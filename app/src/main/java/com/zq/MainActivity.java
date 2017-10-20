package com.zq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zq.behavior.BehaviorActivity;
import com.zq.gesturepassword.GesturePasswordActivity;
import com.zq.histogram.HistogramActivity;
import com.zq.hscrollview.HorizontalScrollViewInRVActivity;
import com.zq.jsbridge.JsBridgeActivity;
import com.zq.linechart.LineChartActivity;
import com.zq.lrc.LrcActivity;
import com.zq.redpoint.RedPointActivity;
import com.zq.ringchart.RingChartActivity;
import com.zq.utils.ViewUtil;
import com.zq.view.recyclerview.adapter.OnItemClickListener;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.view.recyclerview.adapter.cell.DataBinder;
import com.zq.view.recyclerview.adapter.cell.MultiCell;
import com.zq.view.recyclerview.divider.RVItemDivider;
import com.zq.view.recyclerview.utils.RVUtil;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements OnItemClickListener{

    @InjectView(R.id.m_recycler_view)
    RecyclerView mRecyclerView;

    CellAdapter cellAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false));
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
        cellAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(cellAdapter);
        mRecyclerView.addItemDecoration(new RVItemDivider(getResources().getColor(R.color.colorPrimary), ViewUtil.dp2px(this,5)));
    }

    private DataBinder<String> dataBinder = new DataBinder<String>() {
        @Override
        public void bindData(RVViewHolder viewHolder, String data) {
//            viewHolder.setText(R.id.textView, viewHolder.getAdapterPosition() + "、" + data);
            viewHolder.setText(R.id.textView, data);
        }
    };

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, int position) {

        MultiCell multiCell = (MultiCell) cellAdapter.getDataAt(position);
        String fun = (String) multiCell.getData();
        if(fun.equals("图表")){

            startActivity(new Intent(MainActivity.this, LineChartActivity.class));
        }else if("柱状图".equals(fun)){

            startActivity(new Intent(MainActivity.this, HistogramActivity.class));
        }else if("歌词".equals(fun)){

            startActivity(new Intent(MainActivity.this, LrcActivity.class));
        }else if("RecyclerView嵌套RecyclerView".equals(fun)){

            startActivity(new Intent(MainActivity.this, HorizontalScrollViewInRVActivity.class));
        }else if("JsBridge测试".equals(fun)){

            startActivity(new Intent(MainActivity.this, JsBridgeActivity.class));
        }else if("小红点".equals(fun)){

            startActivity(new Intent(MainActivity.this, RedPointActivity.class));
        }else if("Behavior".equals(fun)){

            startActivity(new Intent(MainActivity.this, BehaviorActivity.class));
        }else if("手势密码".equals(fun)){

            startActivity(new Intent(MainActivity.this, GesturePasswordActivity.class));
        }else if("环形图".equals(fun)){

            startActivity(new Intent(MainActivity.this, RingChartActivity.class));
        }
    }
}
