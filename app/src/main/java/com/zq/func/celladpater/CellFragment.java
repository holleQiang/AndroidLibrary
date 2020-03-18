package com.zq.func.celladpater;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zq.R;
import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.view.recyclerview.adapter.cell.DataBinder;
import com.zq.view.recyclerview.adapter.cell.MultiCell;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CellFragment extends Fragment {

    @BindView(R.id.m_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.bt_open_circle)
    Button btOpenCircle;
    Unbinder unbinder;
    private CellAdapter cellAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_recycler_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        cellAdapter = new CellAdapter(getContext(), makeCellList());
        mRecyclerView.setAdapter(cellAdapter);
        cellAdapter.setLoopEnable(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private List<Cell> makeCellList() {
        ArrayList<Cell> cells = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            cells.add(MultiCell.convert(R.layout.item_text_index, i, new DataBinder<Integer>() {
                @Override
                public void bindData(RVViewHolder viewHolder, Integer data) {
                    viewHolder.setText(R.id.tv_title, data + "");
                }
            }));
        }
        return cells;
    }

    @OnClick(R.id.bt_open_circle)
    public void onViewClicked() {

        cellAdapter.setLoopEnable(!cellAdapter.isLoopEnable());
        btOpenCircle.setText("loopOpen:" + cellAdapter.isLoopEnable());
    }
}
