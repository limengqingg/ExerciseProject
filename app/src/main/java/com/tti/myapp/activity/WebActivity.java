package com.tti.myapp.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.widget.TextView;

import com.tti.myapp.R;
import com.tti.myapp.jsbridge.BridgeHandler;
import com.tti.myapp.jsbridge.BridgeWebView;
import com.tti.myapp.jsbridge.CallBackFunction;

public class WebActivity extends BaseActivity {
    private BridgeWebView bridgeWebView;
    private String url;
    //自己定义的
    private TextView title_zj;
    @Override
    protected int initLayout() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        bridgeWebView = findViewById(R.id.bridgeWebView);
        title_zj = findViewById(R.id.title_zj);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            url = bundle.getString("url");
        }
        title_zj.setText(url);
        registJavaHandler();
        initWebView();
    }

    private void initWebView(){
        WebSettings settings = bridgeWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //加载url
        //bridgeWebView.loadUrl(url);
    }

    private void registJavaHandler(){
        //注册goback 点击返回键会触发handler
        bridgeWebView.registerHandler("goback", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                finish();
            }
        });
    }
}
