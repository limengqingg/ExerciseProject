package com.tti.myapp.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.tti.myapp.activity.LoginActivity;
import com.tti.myapp.util.AppConfig;
import com.tti.myapp.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class Api {
    private static  OkHttpClient client;
    private static String requestUrl;
    private static HashMap<String,Object> mParams;
    //请求的逻辑
    public static  Api api = new Api();

    public Api(){

    }
    //传入请求的url  传入的参数是一个map
    //z这个url是/app/login   params 是传递过过来的账号，密码
    public static Api config(String url, HashMap<String,Object> params){
       client = new OkHttpClient.Builder()
                .build();

         requestUrl = ApiConfig.BASE_URL+ url;
         mParams = params;
         return api;
    }

    //post请求
    public void postRequest(Context context, final TtitCallback callback){
        SharedPreferences sp = context.getSharedPreferences("sp_ttit",MODE_PRIVATE);
        String token = sp.getString("token","");
        JSONObject jsonObject = new JSONObject(mParams);
        //转换成json字符串
        String jsonStr = jsonObject.toString();
        // 创建requestBody   把jsonStr引入requestBodyJson
        RequestBody requestBodyJson =
                RequestBody.create(MediaType.parse("application/json;charset=utf-8")
                        , jsonStr);
        //第三步创建Rquest
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("contentType", "application/json;charset=UTF-8")
                .addHeader("token",token)
                .post(requestBodyJson)
                .build();
        //第四步创建call回调对象
        final Call call = client.newCall(request);
        //第五步发起请求
        call.enqueue(new Callback() {
            @Override
            //请求失败
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.onFailure(e);
            }

            @Override
            //请求成功
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功会返回一个response.body 将它转成字符串
                final String result = response.body().string();
                callback.onSuccess(result);

            }

        });
    }

    //context 就是传递过来的getActivity()
    public void getRequest(Context context, final TtitCallback callback){
        SharedPreferences sp =context.getSharedPreferences("sp_tti",MODE_PRIVATE);
        String token =  sp.getString("token","");
        String url = getAppendUrl(requestUrl,mParams);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("token",token)   //把token放到请求头中
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure",e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("401")) {
                        Intent in = new Intent(context, LoginActivity.class);
                        context.startActivity(in);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onSuccess(result);
            }
        });
    }

    private String getAppendUrl(String url, Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
//            当对字符串进行修改的时候，需要使用 StringBuffer 和 StringBuilder 类。
//            和 String 类不同的是，StringBuffer 和 StringBuilder 类的对象能够被多次的修改，并且不产生新的未使用对象。
            StringBuffer buffer = new StringBuffer();
            //获得map的迭代器，用作遍历map中的每一个键值对
//            Iterator是迭代器，map之前应该定义过，姑且认为是HashMap。
//<Entry<String,String>>表示map中的键值对都是String类型的。
//            map.entrySet()是把HashMap类型的数据转换成集合类型
//            map.entrySet().iterator()是去获得这个集合的迭代器，保存在iterator里面。。

//            keySet获得的只是key值的集合，
//            values获得的是value集合，
//            entryset获得的是键值对的集合
//

            Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();  ////就能获得map中的每一个键值对了
                if (StringUtils.isEmpty(buffer.toString())) {
                    buffer.append("?");
                } else {
                    buffer.append("&");
                }
                buffer.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url += buffer.toString();
        }
        return url;
    }
}
