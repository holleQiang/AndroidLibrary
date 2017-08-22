package com.zq;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.view.recyclerview.adapter.cell.DataBinder;
import com.zq.view.recyclerview.adapter.cell.MultiCell;
import com.zq.view.recyclerview.adapter.cell.collapse.CollapsibleCell;
import com.zq.view.recyclerview.utils.RVUtil;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.m_recycler_view)
    RecyclerView mRecyclerView;

    CellAdapter cellAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        RVUtil.setChangeAnimationEnable(mRecyclerView,false);
        cellAdapter = new CellAdapter(this);

        for (int j = 0; j < 10; j++) {

            cellAdapter.addDataAtFirst(new CollapsibleCell<String>(R.layout.item_parent, Cell.FULL_SPAN,"", collapsibleCells){


                @Override
                public List<Cell> initCollapsibleCells() {

                    List<Cell> list = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        list.add(new CollapsibleCell<String>(R.layout.item_parent2, Cell.FULL_SPAN,"", collapsibleCells){


                            @Override
                            public List<Cell> initCollapsibleCells() {

                                List<Cell> list = new ArrayList<>();
                                for (int i = 0; i < 5; i++) {
                                    list.add(new CollapsibleCell<String>(R.layout.item_parent3, Cell.FULL_SPAN,"", collapsibleCells){


                                        @Override
                                        public List<Cell> initCollapsibleCells() {

                                            List<String> list = new ArrayList<>();
                                            for (int i = 0; i < 5; i++) {
                                                list.add("Item :" + i);
                                            }

                                            return MultiCell.convert2(R.layout.item_child, list, new DataBinder<String>() {
                                                @Override
                                                public void bindData(RVViewHolder viewHolder, String data) {
                                                }
                                            });
                                        }

                                        @Override
                                        public void bindData(RVViewHolder viewHolder, String data) {

                                            viewHolder.getView().setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    if(isCollapsible()){

                                                        expand(cellAdapter);
                                                    }else{

                                                        collapse(cellAdapter);
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                                return list;
                            }

                            @Override
                            public void bindData(RVViewHolder viewHolder, String data) {

                                viewHolder.getView().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(isCollapsible()){

                                            expand(cellAdapter);
                                        }else{

                                            collapse(cellAdapter);
                                        }
                                    }
                                });
                            }
                        });
                    }
                    return list;
                }

                @Override
                public void bindData(RVViewHolder viewHolder, String data) {

                    viewHolder.getView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(isCollapsible()){

                                expand(cellAdapter);
                            }else{

                                collapse(cellAdapter);
                            }
                        }
                    });
                }
            });
        }


        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        mRecyclerView.setAdapter(cellAdapter);
    }
}
