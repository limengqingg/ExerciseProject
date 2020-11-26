package com.tti.myapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.tti.myapp.R;
import com.tti.myapp.activity.LoginActivity;
import com.tti.myapp.adapter.HomeAdapter;
import com.tti.myapp.api.Api;
import com.tti.myapp.api.ApiConfig;
import com.tti.myapp.api.TtitCallback;
import com.tti.myapp.entity.CategoryEntity;
import com.tti.myapp.entity.VideoCategoryResponse;
import com.tti.myapp.entity.VideoEntity;
import com.tti.myapp.entity.VideoListResponse;
import com.tti.myapp.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment {
    //Fragment集合
    private ArrayList<Fragment> mFragments = new ArrayList<>();
//    private final String[] mTitles = {
//         "热门","iOS","Android","前端","后端","设计","工具资源"
//    };
    private  String[] mTitles;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    //newInstance( )是一个方法，而new是一个关键字，其次，Class下的newInstance()的使用有局限，因为它生成对象只能调用无参的构造函数
    public static HomeFragment newInstance() {
        //fragment 空的对象
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
////            mParam1 = getArguments().getString(ARG_PARAM1);
////            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
////    LayoutInflater inflater：作用类似于findViewById（），findViewById（）用来寻找xml布局下的具体的控件（Button、TextView等），LayoutInflater inflater（）用来找res/layout/下的xml布局文件
////    ViewGroup container：表示容器，View放在里面（还不理解）
////    Bundle savedInstanceState：保存当前的状态，在活动的生命周期中，只要离开了可见阶段，活动很可能就会被进程终止，这种机制能保存当时的状态
//    //container实际上添加fragment时，包裹fragment的view
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_home,container,false);
//        viewPager = v.findViewById(R.id.fixedViewPager);
//        slidingTabLayout = v.findViewById(R.id.slidingTabLayout);
//        return v;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        //uper.onCreate()方法的作用，就是用父类 的onCreate方法记住当前活动的镜像，当下次再执行到这个活动的时候还是从这个镜像开始执行
//        super.onViewCreated(view, savedInstanceState);
//
//        for(String title:mTitles){
//            mFragments.add(VideoFragment.newInstance(title));
//        }
//        //预加载
//        //setOffscreenPageLimit（） 方法设置的默认值是1.这个设置的值有两层含义: 一是 ViewPager 会预加载几页; 二是 ViewPager 会缓存 2*n+1 页(n为设置的值)。如设置为n=1，当前在第一页，会预加载第二页，滑倒第二页，会预加载第三页，当滑倒第三页，第一页会销毁，第四页会加载。
//        viewPager.setOffscreenPageLimit(mFragments.size());
//        //Adapter是用来帮助填出数据的中间桥梁，简单点说吧：将各种数据以合适的形式显示在View中给用户看
//        viewPager.setAdapter(new HomeAdapter(getFragmentManager(),mTitles,mFragments));
//        slidingTabLayout.setViewPager(viewPager);
//    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        viewPager = mRootView.findViewById(R.id.fixedViewPager);
        slidingTabLayout = mRootView.findViewById(R.id.slidingTabLayout);
    }

    @Override
    protected void initData() {
//        for(String title:mTitles){
//            mFragments.add(VideoFragment.newInstance(title));
//        }
//        //预加载
//        //setOffscreenPageLimit（） 方法设置的默认值是1.这个设置的值有两层含义: 一是 ViewPager 会预加载几页; 二是 ViewPager 会缓存 2*n+1 页(n为设置的值)。如设置为n=1，当前在第一页，会预加载第二页，滑倒第二页，会预加载第三页，当滑倒第三页，第一页会销毁，第四页会加载。
//        viewPager.setOffscreenPageLimit(mFragments.size());
//        //Adapter是用来帮助填出数据的中间桥梁，简单点说吧：将各种数据以合适的形式显示在View中给用户看
//        viewPager.setAdapter(new HomeAdapter(getFragmentManager(),mTitles,mFragments));
//        slidingTabLayout.setViewPager(viewPager);
        getVideoCategoryList();
    }
    private void getVideoCategoryList(){
        //获取token
       // String token = getStringFromSp("token");
       //if(!StringUtils.isEmpty(token)){
            HashMap<String,Object> params = new HashMap<>();
            //添加token
          //  params.put("token",token);
            //参数params  发送get请求
            Api.config(ApiConfig.VIDEO_CATEGORY_LIST,params).getRequest(getActivity(),new TtitCallback() {
                @Override
                public void onSuccess(String res) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            VideoCategoryResponse response = new Gson().fromJson(res,VideoCategoryResponse.class);
                            if(response != null && response.getCode() == 0){
                                List<CategoryEntity> list = response.getPage().getList();
                                 if(list != null && list.size()>0){
                                     mTitles = new String[list.size()];
                                     for (int i = 0;i<list.size();i++){
                                         mTitles[i] = list.get(i).getCategoryName();
                                         mFragments.add(VideoFragment.newInstance(list.get(i).getCategoryId()));
                                     }

                                     //预加载
                                     //setOffscreenPageLimit（） 方法设置的默认值是1.这个设置的值有两层含义: 一是 ViewPager 会预加载几页; 二是 ViewPager 会缓存 2*n+1 页(n为设置的值)。如设置为n=1，当前在第一页，会预加载第二页，滑倒第二页，会预加载第三页，当滑倒第三页，第一页会销毁，第四页会加载。
                                     viewPager.setOffscreenPageLimit(mFragments.size());
                                     //Adapter是用来帮助填出数据的中间桥梁，简单点说吧：将各种数据以合适的形式显示在View中给用户看
                                     viewPager.setAdapter(new HomeAdapter(getFragmentManager(),mTitles,mFragments));
                                     slidingTabLayout.setViewPager(viewPager);
                                }
                        }
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
//        }else{
//            //如果token为空，就跳到登录页面
//            navigateTo(LoginActivity.class);
//        }
    }

}