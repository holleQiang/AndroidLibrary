package com.zq.lrc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zq.R;
import com.zq.widget.lrc.Lrc;
import com.zq.widget.lrc.LrcHelper;
import com.zq.widget.lrc.LrcView;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhangqiang on 2017/9/30.
 */

public class LrcActivity extends AppCompatActivity {


    @InjectView(R.id.lrc_view)
    LrcView lrcView;

    private boolean isStop;
    int i = 3 * 60*1000;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(isStop){
                return;
            }
            lrcView.setPlayingTime(i);
            sendEmptyMessageDelayed(0,500);
            i += 1000 ;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lrc);
        ButterKnife.inject(this);

        try {
            Lrc lrc = LrcHelper.parseLrc(getAssets().open("2744281.lrc"),"utf-8");
            lrcView.setLrc(lrc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        isStop = true;
    }
}
