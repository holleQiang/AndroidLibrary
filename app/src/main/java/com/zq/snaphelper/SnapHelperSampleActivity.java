package com.zq.snaphelper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zq.R;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.view.recyclerview.adapter.cell.DataBinder;
import com.zq.view.recyclerview.adapter.cell.MultiCell;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 2017/12/9.
 */

public class SnapHelperSampleActivity extends AppCompatActivity {

    @BindView(R.id.m_recycler_view)
    RecyclerView mRecyclerView;

    private CellAdapter cellAdapter = new CellAdapter(this);

    int mItemWidth;

    OrientationHelper orientationHelper;
    private int screenWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_helper);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(cellAdapter);
        orientationHelper = OrientationHelper.createHorizontalHelper(mRecyclerView.getLayoutManager());
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        mItemWidth = (int) (screenWidth * 0.8f);
        mRecyclerView.addOnScrollListener(new ScaledScrollListener(mItemWidth,0.9f));
        cellAdapter.setLoopEnable(true);




        List<String> urls = makeUrls();

        cellAdapter.setDataList(MultiCell.convert2(R.layout.item_image, urls, new DataBinder<String>() {
            @Override
            public void bindData(RVViewHolder viewHolder, String data) {

                int itemCount = cellAdapter.getItemCount();
                int position = viewHolder.getAdapterPosition();

                if(position % 2  == 0){
                    viewHolder.getView().setBackgroundColor(Color.RED);
                }else{
                    viewHolder.getView().setBackgroundColor(Color.BLUE);
                }

//                int leftMarin = margin / 2;
//                int rightMarin = margin / 2;
                int leftMarin = 0;
                int rightMarin = 0;
                int topMarin = 0;
                int bottomMarin = 0;
                if(position == 0){
                    leftMarin = (screenWidth - mItemWidth)/2;
                }else if(position == (itemCount-1)){
                    rightMarin =  (screenWidth - mItemWidth)/2;
                }
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewHolder.getView().getLayoutParams();

                layoutParams.width = mItemWidth;
                layoutParams.setMargins(leftMarin, topMarin, rightMarin, bottomMarin);
//                viewHolder.getView().setPadding(leftMarin, topMarin, rightMarin, bottomMarin);

                Glide.with(SnapHelperSampleActivity.this).load(data).into((ImageView) viewHolder.getView(R.id.image));
            }
        }));

       ViewCompat.postOnAnimation(mRecyclerView,new Runnable() {
            @Override
            public void run() {
                PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
                pagerSnapHelper.attachToRecyclerView(mRecyclerView);
            }
        });
    }

    private List<String> makeUrls() {

        List<String> urls = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            urls.add("http://f.hiphotos.baidu.com/image/pic/item/f9198618367adab477cee95081d4b31c8701e483.jpg");
        }
//        urls.add("http://f.hiphotos.baidu.com/image/pic/item/f9198618367adab477cee95081d4b31c8701e483.jpg");
//        urls.add("http://e.hiphotos.baidu.com/image/pic/item/f3d3572c11dfa9eca6f7900468d0f703908fc1cc.jpg");
//        urls.add("http://f.hiphotos.baidu.com/image/pic/item/c2cec3fdfc0392452b0c121c8d94a4c27d1e256c.jpg");
//        urls.add("http://g.hiphotos.baidu.com/image/pic/item/30adcbef76094b36da26dbfea9cc7cd98d109d69.jpg");
//        urls.add("http://e.hiphotos.baidu.com/image/pic/item/b3119313b07eca80f9e7cdf49b2397dda04483c7.jpg");
//        urls.add("http://b.hiphotos.baidu.com/image/pic/item/c9fcc3cec3fdfc03434927abde3f8794a4c226f4.jpg");
//        urls.add("http://h.hiphotos.baidu.com/image/pic/item/3801213fb80e7bec2b83c0f2252eb9389b506b4c.jpg");
//        urls.add("http://d.hiphotos.baidu.com/image/pic/item/0df3d7ca7bcb0a468129782a6163f6246b60af9e.jpg");
//        urls.add("http://h.hiphotos.baidu.com/image/pic/item/279759ee3d6d55fbd5e3873567224f4a20a4ddf8.jpg");
//        urls.add("http://a.hiphotos.baidu.com/image/pic/item/9e3df8dcd100baa1053680a84d10b912c8fc2e8f.jpg");
        return urls;
    }
}
