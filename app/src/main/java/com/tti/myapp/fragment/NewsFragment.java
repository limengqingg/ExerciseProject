package com.tti.myapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tti.myapp.R;
import com.tti.myapp.activity.LoginActivity;
import com.tti.myapp.activity.WebActivity;
import com.tti.myapp.adapter.NewsAdapter;
import com.tti.myapp.adapter.VideoAdapter;
import com.tti.myapp.api.Api;
import com.tti.myapp.api.ApiConfig;
import com.tti.myapp.api.TtitCallback;
import com.tti.myapp.entity.NewsEntity;
import com.tti.myapp.entity.NewsListResponse;
import com.tti.myapp.entity.VideoEntity;
import com.tti.myapp.entity.VideoListResponse;
import com.tti.myapp.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NewsFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;

    private int pageNum = 1;//当前是哪一页
    private NewsAdapter newsAdapter;
    private List<NewsEntity> datas = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    newsAdapter.setDatas(datas);
                    newsAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public NewsFragment() {

    }


    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        recyclerView = mRootView.findViewById(R.id.recyclerView);
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);

    }

    @Override
    protected void initData() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        newsAdapter = new NewsAdapter(getActivity());
        recyclerView.setAdapter(newsAdapter);
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Serializable obj) {
               // showToast("点击");
                NewsEntity newsEntity = (NewsEntity) obj;
                //http://192.168.31.31:8089/newsDetail
                //http://10.16.6.180:8089/newsDetail
                //String url = "http://10.16.6.180:8089/newsDetail?title=" + newsEntity.getAuthorName();

//                Bundle bundle = new Bundle();
//                bundle.putString("url",url);
                //navigateToWithBundle(WebActivity.class,bundle);

                String url =  newsEntity.getAuthorName();

                Bundle bundle = new Bundle();
                bundle.putString("url",url);
                navigateToWithBundle(WebActivity.class,bundle);
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getNewsList(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                pageNum++;
                getNewsList(false);
            }
        });
        getNewsList(true);
    }

    private void getNewsList(boolean isRefresh){
        //获取token
        String token = findByKey("token");
       // if(!StringUtils.isEmpty(token)){
            HashMap<String,Object> params = new HashMap<>();
            //添加token
          //  params.put("token",token);
            params.put("page",pageNum);
            params.put("limit", ApiConfig.PAGE_SIZE);

            //参数params  发送get请求
            Api.config(ApiConfig.NEWS_LIST,params).getRequest(getActivity(),new TtitCallback() {
                @Override
                public void onSuccess(String res) {
                    if(isRefresh){
                        //如果是下拉刷新
                        refreshLayout.finishRefresh(true);
                    }else{
                        refreshLayout.finishLoadMore(true);
                    }

                    NewsListResponse response = new Gson().fromJson(res,NewsListResponse.class);

                    if(response != null && response.getCode() == 0){

                        List<NewsEntity> list = response.getPage().getList();
                        if(list != null && list.size()>0){
                            if(isRefresh){
                                datas = list;
                            }else{
                                datas.addAll(list);
                            }
                            mHandler.sendEmptyMessage(0);
//                                    videoAdapter.setDatas(datas);
//                                    videoAdapter.notifyDataSetChanged();
                        }else{
                            if(isRefresh){
                                showToastSync("暂时加载无数据");
                            }else{
                                showToastSync("没有更多数据");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    if (isRefresh) {
                        refreshLayout.finishLoadMore(true);
                    } else {
                        //如果是下拉刷新
                        refreshLayout.finishRefresh(true);
                    }
                }
            });
//        }else{
//            //如果token为空，就跳到登录页面
////            navigateTo(LoginActivity.class);
//        }
    }
}