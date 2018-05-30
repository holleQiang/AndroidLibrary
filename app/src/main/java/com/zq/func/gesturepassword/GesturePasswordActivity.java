package com.zq.func.gesturepassword;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zq.R;
import com.zq.widget.gesturepassword.GesturePasswordView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 2017/10/18.
 */

public class GesturePasswordActivity extends AppCompatActivity {


    @BindView(R.id.gesture_password_view)
    GesturePasswordView gesturePasswordView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_password);
        ButterKnife.bind(this);
        gesturePasswordView.setOnGetPasswordListener(new GesturePasswordView.OnGetPasswordListener() {
            @Override
            public void onGetPassword(int selectCount, String password) {
                Toast.makeText(GesturePasswordActivity.this, password, Toast.LENGTH_SHORT).show();

                ViewCompat.postOnAnimationDelayed(gesturePasswordView, new Runnable() {
                    @Override
                    public void run() {

                        gesturePasswordView.clearSelections();
                    }
                }, 2000);
            }

        });
    }
}
