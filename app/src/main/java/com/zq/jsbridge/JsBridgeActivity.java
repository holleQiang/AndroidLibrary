package com.zq.jsbridge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.zq.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangqiang on 2017/10/13.
 */

public class JsBridgeActivity extends AppCompatActivity {


    @BindView(R.id.web_view)
    MyWebView webView;
    @BindView(R.id.refresh)
    Button refresh;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.text_view)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_bridge);
        ButterKnife.bind(this);
        webView.registerHandler("AppHandler", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                textView.setText(data);
                function.onCallBack("老子收到了，并给你抛了个异常");
            }
        });

        webView.loadUrl("http://47.91.230.246:8080/dailyfun/jsbridge/jsbridge.html");

        webView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

                Toast.makeText(JsBridgeActivity.this, "js ： " + data, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick({R.id.refresh, R.id.button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.refresh:
                webView.reload();
                break;
            case R.id.button:
                webView.send("你收到了吗？", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Toast.makeText(JsBridgeActivity.this, "==========" + data, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
