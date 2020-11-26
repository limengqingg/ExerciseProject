package com.tti.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tti.myapp.activity.BaseActivity;
import com.tti.myapp.activity.HomeActivity;
import com.tti.myapp.activity.LoginActivity;
import com.tti.myapp.activity.RegisterActivity;
import com.tti.myapp.util.StringUtils;

public class MainActivity extends BaseActivity {
    private Button btnLogin;
    private  Button btnRegister;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        btnLogin = findViewById(R.id.btn_login);
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent in = new Intent(MainActivity.this, LoginActivity.class);
////                startActivity(in);
//                navigateTo(LoginActivity.class);
//            }
//        });
//        btnRegister = findViewById(R.id.btn_register);
//        btnRegister.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
////                Intent in = new Intent(MainActivity.this, RegisterActivity.class);
////                startActivity(in);
//                navigateTo( RegisterActivity.class);
//            }
//        });
//    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
    }

    @Override
    protected void initData() {
        if(!StringUtils.isEmpty(findByKey("token"))){
            navigateTo(HomeActivity.class);
            finish();

        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(in);
                navigateTo(LoginActivity.class);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Intent in = new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(in);
                navigateTo( RegisterActivity.class);
            }
        });
    }
}