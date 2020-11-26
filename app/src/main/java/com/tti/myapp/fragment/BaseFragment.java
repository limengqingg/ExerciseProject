package com.tti.myapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dueeeke.videoplayer.player.VideoViewManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRootView == null){
            mRootView = inflater.inflate(initLayout(),container,false);
            initView();
        }
        unbinder = ButterKnife.bind(this,mRootView);
        return  mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解绑
        unbinder.unbind();
    }

    protected abstract int initLayout();

    protected abstract void initView();

    protected abstract void initData();

    //getActivity() 得到父组件
    public void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    public void showToastSync(String msg){
        Looper.prepare();
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void navigateTo(Class cls){
        Intent in = new Intent(getActivity(),cls);
        startActivity(in);
    }

    public void navigateToWithFlag(Class cls,int flags){
        Intent in = new Intent(getActivity(),cls);
        //以回退栈的形式添加activity
        in.setFlags(flags);
        startActivity(in);
    }


    public void navigateToWithBundle(Class cls, Bundle bundle) {
        Intent in = new Intent(getActivity(), cls);
        in.putExtras(bundle);
        startActivity(in);
    }

    //保存数据到xml中  saveString
    public void insertVal(String key,String val){
        SharedPreferences sp =getActivity().getSharedPreferences("sp_tti",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,val);
        editor.commit();
    }

    //取出xml中的数据,获取token值  getStringFromSp
    protected String findByKey(String key){
        SharedPreferences sp =getActivity().getSharedPreferences("sp_tti",MODE_PRIVATE);
        return sp.getString(key,"");
    }

    //
    //删除token值
    protected void removeByKey(String key){
        SharedPreferences sp =getActivity().getSharedPreferences("sp_tti",MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.commit();
    }

    protected VideoViewManager getVideoViewManager(){
        return VideoViewManager.instance();
    }
}
