package com.zq.func.celladpater;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zq.R;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CustomeAdapter extends RecyclerView.Adapter<RVViewHolder> {

    private List<Integer> dataList = new ArrayList<>();

    public CustomeAdapter() {
        for (int i = 0; i < 5; i++) {
            dataList.add(i);
        }
    }

    @NonNull
    @Override
    public RVViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return RVViewHolder.create(viewGroup.getContext(), R.layout.item_text_index, viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolder rvViewHolder, int i) {
        Integer integer = dataList.get(i % dataList.size());
        rvViewHolder.setText(R.id.tv_title,integer+"");
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}
