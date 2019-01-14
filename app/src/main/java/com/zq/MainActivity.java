package com.zq;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.didi.virtualapk.PluginManager;
import com.didi.virtualapk.internal.LoadedPlugin;
import com.zq.func.behavior.BehaviorActivity;
import com.zq.func.flowlayout.FlowLayoutActivity;
import com.zq.func.gesturepassword.GesturePasswordActivity;
import com.zq.func.histogram.HistogramActivity;
import com.zq.func.hscrollview.HorizontalScrollViewInRVActivity;
import com.zq.func.htmltext.HtmlTextActivity;
import com.zq.func.jsbridge.JsBridgeActivity;
import com.zq.func.linechart.LineChartActivity;
import com.zq.func.lrc.LrcActivity;
import com.zq.func.pageradapter.FragmentPagerAdapterTestActivity;
import com.zq.func.redpoint.RedPointActivity;
import com.zq.func.ringchart.RingChartActivity;
import com.zq.func.rotateanim.RotateViewActivity;
import com.zq.func.shadow.ShadowTestActivity;
import com.zq.func.snaphelper.SnapHelperSampleActivity;
import com.zq.utils.FileUtil;
import com.zq.utils.ViewUtil;
import com.zq.view.recyclerview.adapter.OnItemClickListener;
import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.DataBinder;
import com.zq.view.recyclerview.adapter.cell.MultiCell;
import com.zq.view.recyclerview.divider.RVItemDivider;
import com.zq.view.recyclerview.utils.RVUtil;
import com.zq.view.recyclerview.viewholder.RVViewHolder;
import com.zq.func.rulerview.RulerViewDemo;
import com.zq.widget.ptr.CellConverter;
import com.zq.widget.ptr.SimplePullToRefreshHelper;
import com.zq.widget.ptr.data.Callback;
import com.zq.widget.ptr.data.DataSource;
import com.zq.widget.ptr.data.RxDataSource;
import com.zq.widget.ptr.loadmore.LoadMoreWidget;
import com.zq.widget.ptr.refresh.RefreshWidget;
import com.zq.widget.ptr.view.SamplePullToRefreshView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

public class MainActivity extends AppCompatActivity implements OnItemClickListener, CellConverter<List<String>> {


    boolean change;
    @BindView(R.id.m_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.m_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    SimplePullToRefreshHelper<List<String>> pullToRefreshHelper;
    private SamplePullToRefreshView<List<String>> refreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshView = new SamplePullToRefreshView<>(mRecyclerView, mSwipeRefreshLayout, this);
        refreshView.getRefreshWidget().setOnRefreshListener(new RefreshWidget.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefreshHelper.refresh();
            }
        });
        refreshView.getLoadMoreWidget().setOnLoadMoreListener(new LoadMoreWidget.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pullToRefreshHelper.loadMore();
            }
        });
        refreshView.getLoadMoreWidget().setLoadMoreEnable(true);
        refreshView.getAdapter().setOnItemClickListener(this);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                pullToRefreshHelper.stopRefresh();
//            }
//        },500);
        pullToRefreshHelper = new SimplePullToRefreshHelper<>(refreshView,
                new RxDataSource<List<String>>() {
                    @Override
                    public Observable<List<String>> getDataSource(int pageIndex, int pageSize, int startIndex, int endIndex) {
                        List<String> strings = new ArrayList<>();
                        strings.add("图表");
                        strings.add("柱状图");
                        strings.add("RecyclerView嵌套RecyclerView");
                        strings.add("JsBridge测试");
                        strings.add("小红点");
                        strings.add("Behavior");
                        strings.add("手势密码");
                        strings.add("环形图");
                        strings.add("View翻转");
                        strings.add("ViewPager测试");
                        strings.add("htmlText");
                        strings.add("FlowLayout");
                        strings.add("SnapHelper");
                        strings.add("RulerView");
                        strings.add("ShadowTest");
                        strings.add("插件测试");
                        return Observable.just(strings).delay(1000,TimeUnit.MILLISECONDS).observeOn(new Scheduler() {
                            @Override
                            public Worker createWorker() {
                                return new Worker() {
                                    @Override
                                    public Disposable schedule(final Runnable run, long delay, TimeUnit unit) {
                                        final Handler handler = new Handler(Looper.getMainLooper());
                                        handler.postDelayed(run,unit.toMillis(delay));
                                        return new Disposable() {
                                            boolean isDisposed;
                                            @Override
                                            public void dispose() {
                                                handler.removeCallbacks(run);
                                                isDisposed = true;
                                            }

                                            @Override
                                            public boolean isDisposed() {
                                                return isDisposed;
                                            }
                                        };
                                    }

                                    @Override
                                    public void dispose() {

                                    }

                                    @Override
                                    public boolean isDisposed() {
                                        return false;
                                    }
                                };
                            }
                        });
                    }
                });

//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RVUtil.setChangeAnimationEnable(mRecyclerView, false);
        mRecyclerView.addItemDecoration(new RVItemDivider(getResources().getColor(R.color.colorPrimary), ViewUtil.dp2px(this, 5)));
        pullToRefreshHelper.initRefresh();
    }

    private DataBinder<String> dataBinder = new DataBinder<String>() {
        @Override
        public void bindData(RVViewHolder viewHolder, String data) {
//            viewHolder.setText(R.id.textView, viewHolder.getAdapterPosition() + "、" + data);
            viewHolder.setText(R.id.textView, data);
            /*HorizontalSlidLayout slidLayout = viewHolder.getView(R.id.m_horizontal_slid);
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
            });*/
        }
    };


    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, int position) {

        MultiCell multiCell = (MultiCell) refreshView.getAdapter().getDataAt(position);
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
        } else if ("ViewPager测试".equals(fun)) {
            startActivity(new Intent(MainActivity.this, FragmentPagerAdapterTestActivity.class));
        } else if ("htmlText".equals(fun)) {
            startActivity(new Intent(MainActivity.this, HtmlTextActivity.class));
        } else if ("FlowLayout".equals(fun)) {
            startActivity(FlowLayoutActivity.newIntent(MainActivity.this));
        } else if ("SnapHelper".equals(fun)) {
            startActivity(new Intent(MainActivity.this, SnapHelperSampleActivity.class));
        } else if ("RulerView".equals(fun)) {
            startActivity(new Intent(MainActivity.this, RulerViewDemo.class));
        } else if ("ShadowTest".equals(fun)) {
            startActivity(new Intent(MainActivity.this, ShadowTestActivity.class));
        } else if ("插件测试".equals(fun)) {

            try {

                String pkg = "com.zq.cc";
                LoadedPlugin loadedPlugin = PluginManager.getInstance(getApplicationContext()).getLoadedPlugin(pkg);
                if (loadedPlugin != null) {

                    FileUtil.readStream(getAssets().open("app-release-unsigned.apk"), new FileOutputStream(new File(getFilesDir(), "plugin.apk")));
                    PluginManager.getInstance(getApplicationContext()).loadPlugin(new File(getFilesDir(), "plugin.apk"));
                }

//                PluginUtil.hookActivityResources(MainActivity.this, pkg);

                Intent intent = new Intent();
                intent.setClassName(pkg, "com.zq.cc.MainActivity");
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public List<Cell> convert(List<String> s) {
        
        List<Cell> cellList = new ArrayList<>();
        cellList.add(MultiCell.convert(R.layout.item_text, "图表", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "柱状图", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "歌词", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "RecyclerView嵌套RecyclerView", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "JsBridge测试", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "小红点", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "Behavior", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "手势密码", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "环形图", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "View翻转", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "ViewPager测试", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "htmlText", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "FlowLayout", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "SnapHelper", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "RulerView", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "ShadowTest", dataBinder));
        cellList.add(MultiCell.convert(R.layout.item_text, "插件测试", dataBinder));
        return cellList;
    }
}
