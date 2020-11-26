package com.tti.myapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;

import com.dueeeke.videoplayer.player.VideoViewManager;
import com.tti.myapp.MainActivity;

public abstract class BaseActivity extends AppCompatActivity {
    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        mContext = this;
        //layout布局
        setContentView(initLayout());
        //初始化一些控件
        initView();
        //对数据的封装，监听事件  调用一些接口 方法
        initData();
    }

    protected abstract int initLayout();

    protected abstract void initView();

    protected abstract void initData();

    public  void showToast(String msg){

        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }

    //异步的消息
    public  void showToastSync(String msg){
        Looper.prepare();
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void navigateToWithFlag(Class cls,int flags){
        Intent in = new Intent(mContext,cls);
        //以回退栈的形式添加activity
        in.setFlags(flags);
        startActivity(in);
    }

//    页面跳转，传入跳转的页面 mContext当前页面
    public void navigateTo(Class cls){
        Intent in = new Intent(mContext,cls);
        startActivity(in);
    }

    //把token保存到本地  saveStringToSp
    protected void insertVal(String key,String val){
        SharedPreferences sp = getSharedPreferences("sp_tti",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,val);
        editor.commit();
    }

    //取出xml中的数据,获取token值  getStringFromSp
    protected String findByKey(String key){
        SharedPreferences sp =getSharedPreferences("sp_tti",MODE_PRIVATE);
        return sp.getString(key,"");
    }


    protected VideoViewManager getVideoViewManager(){
        return VideoViewManager.instance();
    }

    //换肤功能   如果项目中使用的Activity继承自AppCompatActivity，需要重载getDelegate()方法
    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        return SkinAppCompatDelegateImpl.get(this, this);
    }
}
