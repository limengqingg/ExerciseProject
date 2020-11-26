package com.tti.myapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//import org.apache.commons.lang3.StringUtils;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.tti.myapp.R;
import com.tti.myapp.api.Api;
import com.tti.myapp.api.ApiConfig;
import com.tti.myapp.api.TtitCallback;
import com.tti.myapp.entity.LoginResponse;
import com.tti.myapp.util.AppConfig;
import com.tti.myapp.util.StringUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    private EditText etAccount;
    private EditText etPwd;
    private Button btnLogin;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        etAccount = findViewById(R.id.et_account);
//        etPwd = findViewById(R.id.et_pwd);
//        btnLogin = findViewById(R.id.btn_login);
////        点击登录
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                获取账号密码     trim()去掉空格
//                String account = etAccount.getText().toString().trim();
//                String pwd = etPwd.getText().toString().trim();
//                login(account, pwd);
//
//            }
//
//        });
//    }

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        btnLogin = findViewById(R.id.btn_login);
    }

    @Override
    protected void initData() {
//        点击登录
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                获取账号密码     trim()去掉空格
                String account = etAccount.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                login(account, pwd);

            }

        });

    }

    //第一种写法
    private void login(String account,String pwd){
        if(StringUtils.isEmpty(account)){
            showToast("请输入账号");
            return;
        }
        if(StringUtils.isEmpty(pwd)){
            showToast("请输入密码");
            return;
        }
        //<String,Object> 第一个是对键的限制，第二个是对值的限制
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("mobile",account);
        params.put("password",pwd);
//        Api.config("/app/login",params).postRequest(new TtitCallback() {
        Api.config(ApiConfig.LOGIN,params).postRequest(mContext,new TtitCallback() {
            @Override
            public void onSuccess(final String res) {
                Log.e("onSuccess",res);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //res  请求成功返回的参数
//                        showToast(res);
//                    }
//                });

//                showToastSync(res);
                //转换成实体类再取出token
                //创建gson  gson是谷歌用来解析json
                Gson gson = new Gson();
                //把json字符串转换成LoginResponse.class
                //把res变成了实体对象
                LoginResponse loginResponse = gson.fromJson(res,LoginResponse.class);
                //loginResponse.getCode==0说明登录成功
                if (loginResponse.getCode() == 0) {
                    //得到了token
                      String token = loginResponse.getToken();
                      //token保存到本地   这个方法可以封装一下
                    //创建SharedPreferences
                    // 第一个参数xml的文件名称  第二个参数是模式  MODE_PRIVATE 私有的
                    //
//                    SharedPreferences sp = getSharedPreferences("sp_tti",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp.edit();
//                    //token就会存放在“sp_tti”文件中
//                    editor.putString("token",token);
//                    editor.commit();

                    //封装的写法
                    insertVal("token",token);

                    //登录成功，跳到首页
                    navigateTo(HomeActivity.class);
                    navigateToWithFlag(HomeActivity.class,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);

                    showToastSync("登录成功");
                }else{
                    showToastSync("登录失败");
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }





    //第二种写法
//    private void login(String account, String pwd) {
////        if(account == null || account.equals("")|| account.length()<=0){
////            Toast.makeText(this,"请输入账号",Toast.LENGTH_SHORT).show();
////            return;
////        }
//
//        if (StringUtils.isEmpty(account)) {
////            Toast.makeText(this,"请输入账号",Toast.LENGTH_SHORT).show();
//            showToast("请输入账号");
//            return;
//        }
//
//        if (StringUtils.isEmpty(pwd)) {
////            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
//            showToast("请输入密码");
//            return;
//
//        }
//
//        //第一步创建OKHttpClient
//        OkHttpClient client = new OkHttpClient.Builder()
//                .build();
//        //创建一个map集合
//        Map m = new HashMap();
//        //将账号、密码放到m集合里
//        m.put("mobile", account);
//        m.put("password", pwd);
//        //创建一个json
//        JSONObject jsonObject = new JSONObject(m);
//        //转换成json字符串
//        String jsonStr = jsonObject.toString();
//        // 创建requestBody   把jsonStr引入requestBodyJson
//        RequestBody requestBodyJson =
//                RequestBody.create(MediaType.parse("application/json;charset=utf-8")
//                        , jsonStr);
//        //第三步创建Rquest
//        Request request = new Request.Builder()
//                .url(AppConfig.BASE_URL+"/app/login")
//                .addHeader("contentType", "application/json;charset=UTF-8")
//                .post(requestBodyJson)
//                .build();
//        //第四步创建call回调对象
//        final Call call = client.newCall(request);
//        //第五步发起请求
//        call.enqueue(new Callback() {
//            @Override
//            //请求失败
//            public void onFailure(Call call, IOException e) {
//                Log.e("onFailure", e.getMessage());
//            }
//
//            @Override
//               //请求成功
//            public void onResponse(Call call, Response response) throws IOException {
//                // 请求成功会返回一个response.body 将它转成字符串
//                final String result = response.body().string();
//                //在主线程进行执行
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showToast(result);
//                    }
//                });
//
//            }
//
//        });
//    }
}
