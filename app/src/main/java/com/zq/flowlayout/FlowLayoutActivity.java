package com.zq.flowlayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * ssss
 * Created by zhangqiang on 2017/11/21.
 */

public class FlowLayoutActivity extends AppCompatActivity {

    public static Intent newIntent(Context context){

        Intent intent = new Intent(context,FlowLayoutActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.zq.R.layout.activity_flow_layout);

    }
}
