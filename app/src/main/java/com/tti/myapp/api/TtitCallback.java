package com.tti.myapp.api;

public interface TtitCallback {
    //请求成功的回调
    void  onSuccess(String res);

    //请求失败的回调
    void onFailure(Exception e);

}
