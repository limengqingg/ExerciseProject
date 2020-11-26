package com.tti.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tti.myapp.R;
import com.tti.myapp.api.Api;
import com.tti.myapp.api.ApiConfig;
import com.tti.myapp.api.TtitCallback;
import com.tti.myapp.util.StringUtils;

import java.util.HashMap;

public class RegisterActivity extends BaseActivity {
    private EditText etAccount;
    private EditText etPwd;
    private Button btnResiter;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        etAccount = findViewById(R.id.et_account);
//        etPwd = findViewById(R.id.et_pwd);
//        btnResiter = findViewById(R.id.btn_register);
//        btnResiter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String account = etAccount.getText().toString().trim();
//                String pwd = etPwd.getText().toString().trim();
//                register(account,pwd);
//            }
//        });
//    }

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        btnResiter = findViewById(R.id.btn_register);
    }

    @Override
    protected void initData() {
        btnResiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                register(account,pwd);
            }
        });
    }

    private void register(String account,String pwd){
        if(StringUtils.isEmpty(account)){
            showToast("请输入账号");
            return;
        }
        if(StringUtils.isEmpty(pwd)){
            showToast("请输入密码");
            return;
        }
        HashMap<String, Object> params = new HashMap<String,Object>();
        params.put("mobile",account);
        params.put("password",pwd);
        Api.config(ApiConfig.REGISTER,params).postRequest(mContext,new TtitCallback() {
            @Override
            public void onSuccess(final String res) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //res  请求成功返回的参数
                        showToast(res);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("onFailure",e.toString());

            }
        });
    }
}