package com.zq.func.pageradapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zq.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 2017/10/30.
 */

public class FragmentPagerAdapterTestActivity extends AppCompatActivity {

    @BindView(R.id.m_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.delete_pre)
    Button deletePre;
    @BindView(R.id.delete_curr)
    Button deleteCurr;
    @BindView(R.id.delete_suf)
    Button deleteSuf;

    private BasePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager_adapter);
        ButterKnife.bind(this);
        pagerAdapter = new BasePagerAdapter();
        mViewPager.setAdapter(pagerAdapter);

        mViewPager.setPageTransformer(false,new PageTransformer());
        mViewPager.setPageMargin(50);
    }

//    @OnClick({R.id.delete_pre, R.id.delete_curr, R.id.delete_suf})
    public void onViewClicked(View view) {

        int position = mViewPager.getCurrentItem();

        switch (view.getId()) {
            case R.id.delete_pre:

                if(position > 0){

                    pagerAdapter.getDataList().remove(position - 1);
                    pagerAdapter.notifyDataSetChanged();
                    mViewPager.setCurrentItem(-- position,false);
                }
                break;
            case R.id.delete_curr:

                if(position >= 0){

                    pagerAdapter.getDataList().remove(position );
                    pagerAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.delete_suf:

                if(position < pagerAdapter.getCount() - 1){

                    pagerAdapter.getDataList().remove(position + 1);
                    pagerAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    class PageTransformer implements ViewPager.PageTransformer{


        private float MIN_SCALE = 0.5f;

        @Override
        public void transformPage(View page, float position) {


            float alpha;
            if(-1 <= position && position <= 0){

                alpha = position + 1;
            }else if(0 < position && position <= 1){

                alpha = 1- position;
            }else{
                alpha = 0;
            }
            page.setAlpha(alpha);
            Log.i("Test","======position=====" + position);
//            page.setScaleX(0.8f * (1 - position));
//            page.setScaleY(0.8f * (1 - position));
        }
    }
}
