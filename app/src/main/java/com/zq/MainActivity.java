package com.zq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zq.histogram.HistogramActivity;
import com.zq.linechart.LineChartActivity;
import com.zq.view.recyclerview.adapter.OnItemClickListener;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.view.recyclerview.adapter.cell.DataBinder;
import com.zq.view.recyclerview.adapter.cell.MultiCell;
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RVUtil.setChangeAnimationEnable(mRecyclerView, false);
        cellAdapter = new CellAdapter(this);
        cellAdapter.addDataAtLast(MultiCell.convert(R.layout.item_text, "图表", dataBinder));
        cellAdapter.addDataAtLast(MultiCell.convert(R.layout.item_text, "柱状图", dataBinder));
        cellAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(cellAdapter);
    }

    private DataBinder<String> dataBinder = new DataBinder<String>() {
        @Override
        public void bindData(RVViewHolder viewHolder, String data) {
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
        }
    }
}
