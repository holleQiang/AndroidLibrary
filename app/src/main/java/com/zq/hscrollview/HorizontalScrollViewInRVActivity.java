package com.zq.hscrollview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.zq.R;
import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.view.recyclerview.adapter.cell.DataBinder;
import com.zq.view.recyclerview.adapter.cell.MultiCell;
import com.zq.view.recyclerview.divider.RVItemDivider;
import com.zq.view.recyclerview.hscroll.ScrollableRecyclerViewGetter;
import com.zq.view.recyclerview.hscroll.ScrollableRecyclerViewHelper;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhangqiang on 2017/10/9.
 */

public class HorizontalScrollViewInRVActivity extends AppCompatActivity {

    @InjectView(R.id.recycler_view)
    RecyclerView totalRecyclerView;

    CellAdapter cellAdapter = new CellAdapter(this);
    RecyclerView.RecycledViewPool viewPool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hscrollview_in_rv);
        ButterKnife.inject(this);


        for (int i = 0; i < 150; i++) {

            Data data = new Data();
            data.setColumnIndex(i);
            horizontalAdapter.addDataAtLast(MultiCell.convert(R.layout.item_text2, data, new DataBinder<Data>() {
                @Override
                public void bindData(final RVViewHolder viewHolder, Data data) {
                    if (data.getColumnIndex() % 2 == 0) {
                        viewHolder.setBackgroundResource(R.id.textView, R.drawable.click_selector);
                    } else {
                        viewHolder.setBackgroundResource(R.id.textView, R.color.colorPrimary);
                    }
                    viewHolder.setText(R.id.textView, "" + data.getColumnIndex());
                    viewHolder.setOnClickListener(R.id.textView, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(HorizontalScrollViewInRVActivity.this, "click : " + viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }));
        }


        viewPool = new RecyclerView.RecycledViewPool();

        totalRecyclerView.addItemDecoration(new RVItemDivider(Color.WHITE, 2));
        totalRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Cell> cellList = new ArrayList<>();
        for (int i = 0; i < 150; i++) {

            cellList.add(new MultiCell<>(R.layout.item_horizontal_scroo_view, "", new DataBinder<String>() {

                @Override
                public void bindData(RVViewHolder viewHolder, String data) {

                    RecyclerView recyclerView = viewHolder.getView(R.id.recycler_view);
                    recyclerView.setRecycledViewPool(viewPool);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(HorizontalScrollViewInRVActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(horizontalAdapter);
                }
            }));
        }
        cellAdapter.setDataList(cellList);
        totalRecyclerView.setAdapter(cellAdapter);
        new ScrollableRecyclerViewHelper(totalRecyclerView, new ScrollableRecyclerViewGetter<RVViewHolder>() {
            @Nullable
            @Override
            public RecyclerView getScrollableRecyclerView(RVViewHolder viewHolder) {
                if(viewHolder.getAdapterPosition() % 2==0){
                    return null;
                }
                return viewHolder.getView(R.id.recycler_view);
            }
        }).attachToTarget();
    }


    CellAdapter horizontalAdapter = new CellAdapter(this);



    private static class Data {

        private int rowIndex, columnIndex;

        public void setRowIndex(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        public void setColumnIndex(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public int getColumnIndex() {
            return columnIndex;
        }
    }
}
