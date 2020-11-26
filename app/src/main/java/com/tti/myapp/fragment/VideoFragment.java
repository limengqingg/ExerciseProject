package com.tti.myapp.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.player.VideoView;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tti.myapp.R;
import com.tti.myapp.activity.LoginActivity;
import com.tti.myapp.adapter.VideoAdapter;
import com.tti.myapp.api.Api;
import com.tti.myapp.api.ApiConfig;
import com.tti.myapp.api.TtitCallback;
import com.tti.myapp.entity.VideoEntity;
import com.tti.myapp.entity.VideoListResponse;

import com.tti.myapp.util.StringUtils;
import com.tti.myapp.listener.OnItemChildClickListener;
import com.tti.myapp.util.Tag;
import com.tti.myapp.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VideoFragment extends BaseFragment implements OnItemChildClickListener {
    private int categoryId;
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private int pageNum = 1;//当前是哪一页
    private VideoAdapter videoAdapter;
    private List<VideoEntity> datas = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    //视频播放器
    protected VideoView mVideoView;
    protected StandardVideoController mController;
    protected ErrorView mErrorView;
    protected CompleteView mCompleteView;
    protected TitleView mTitleView;

    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;

    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    protected int mLastPos = mCurPos;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    videoAdapter.setDatas(datas);
                    videoAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    //自己改

    public VideoFragment(){

    }


    public static VideoFragment newInstance(int categoryId){
        VideoFragment fragment = new VideoFragment();
        fragment.categoryId = categoryId;
        return fragment;
    }


    @Override
    protected int initLayout() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        //初始化initVideoView
        initVideoView();
        recyclerView = mRootView.findViewById(R.id.recyclerView);
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        //线性布局管理器
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        //排列方式  线性的
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        一定要用布局管理器，不然recyclerView无法使用
//        布局管理器:线性布局管理器，网格布局管理器，瀑布流布局管理器
//        recyclerView.setLayoutManager(linearLayoutManager);
//        List<VideoEntity> datas = new ArrayList<>();
//        for(int i = 0;i<8;i++){
//            VideoEntity ve = new VideoEntity();
//            ve.setVtitle("韭菜盒子新做法，不发面不烫面");
//            ve.setAuthor("大胃王");
//            ve.setLikeNum(1*2);
//            ve.setCollectNum(i * 4);
//            ve.setCommentNum(i * 6);
//            //自己写死的图片
//            ve.setHead(R.mipmap.header);
////            ve.setCon(R.mipmap.default_bg);
//            ve.setCon(R.mipmap.default_bg);
//            datas.add(ve);
//        }

    }


    @Override
    protected void initData() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        videoAdapter = new VideoAdapter(getActivity());
        videoAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(videoAdapter);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                FrameLayout playerContainer = view.findViewById(R.id.player_container);
                View v = playerContainer.getChildAt(0);
                if (v != null && v == mVideoView && !mVideoView.isFullScreen()) {
                    releaseVideoView();
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getVideoList(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                pageNum++;
                getVideoList(false);
            }
        });
        getVideoList(true);
    }


    //初始化播放器
    protected void initVideoView(){
        mVideoView = new VideoView(getActivity());
        mVideoView.setOnStateChangeListener(new com.dueeeke.videoplayer.player.VideoView.SimpleOnStateChangeListener(){
            @Override
            public void onPlayerStateChanged(int playState) {
               //监听VideoViewManager释放，重置状态
                if(playState == com.dueeeke.videoplayer.player.VideoView.STATE_IDLE){
                    Utils.removeViewFormParent(mVideoView);
                    mLastPos = mCurPos;
                    mCurPos = -1;
                }
            }
        });
        mController = new StandardVideoController(getActivity());
        mErrorView = new ErrorView(getActivity());
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(getActivity());
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(getActivity());
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(getActivity()));
        mController.addControlComponent(new GestureView(getActivity()));
        mController.setEnableOrientation(true);
        mVideoView.setVideoController(mController);
    }

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    /**
     * 由于onPause必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onPause的逻辑
     */
    protected void pause() {
        releaseVideoView();
    }

    @Override
    public void onResume() {
        super.onResume();
        resume();
    }

    /**
     * 由于onResume必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onResume的逻辑
     */
    protected void resume() {
        if (mLastPos == -1)
            return;
        //恢复上次播放的位置
        startPlay(mLastPos);
    }

    /**
     * PrepareView被点击
     */
    @Override
    public void onItemChildClick(int position) {
        startPlay(position);
    }

    /**
     * 开始播放
     *
     * @param position 列表位置
     */
    protected void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        VideoEntity videoEntity = datas.get(position);
        //边播边存
//        String proxyUrl = ProxyVideoCacheManager.getProxy(getActivity()).getProxyUrl(videoBean.getUrl());
//        mVideoView.setUrl(proxyUrl);

        mVideoView.setUrl(videoEntity.getPlayurl());
        mTitleView.setTitle(videoEntity.getVtitle());
        View itemView = linearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoAdapter.ViewHolder viewHolder = (VideoAdapter.ViewHolder) itemView.getTag();
        //把列表中预置的PrepareView添加到控制器中，注意isPrivate此处只能为true。
        mController.addControlComponent(viewHolder.mPrepareView, true);
        Utils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        getVideoViewManager().add(mVideoView, Tag.LIST);
        mVideoView.start();
        mCurPos = position;

    }

    //释放视频，关闭视频
    private void releaseVideoView() {
        mVideoView.release();
        if (mVideoView.isFullScreen()) {
            mVideoView.stopFullScreen();
        }
        if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }




//    @Nullable
//    @Override
//    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
////        View v = inflater.inflate(R.layout.fragment_video, container, false);
////        recyclerView = v.findViewById(R.id.recyclerView);
////        refreshLayout = v.findViewById(R.id.refreshLayout);
//        //写死的数据
//        //线性布局管理器
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        //排列方式  线性的
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
////        一定要用布局管理器，不然recyclerView无法使用
////        布局管理器:线性布局管理器，网格布局管理器，瀑布流布局管理器
//        recyclerView.setLayoutManager(linearLayoutManager);
////        List<VideoEntity> datas = new ArrayList<>();
//        for(int i = 0;i<8;i++){
//            VideoEntity ve = new VideoEntity();
//            ve.setVtitle("韭菜盒子新做法，不发面不烫面");
//            ve.setAuthor("大胃王");
//            ve.setLikeNum(1*2);
//            ve.setCollectNum(i * 4);
//            ve.setCommentNum(i * 6);
//            //自己写死的图片
//            ve.setHead(R.mipmap.header);
////            ve.setCon(R.mipmap.default_bg);
//            ve.setCon(R.mipmap.default_bg);
//            datas.add(ve);
//        }
//
//
//        //return  v;
//    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
//         videoAdapter = new VideoAdapter(getActivity());
////                    //把videoAdapter设置到recyclerView里
//        recyclerView.setAdapter(videoAdapter);
//
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh( RefreshLayout refreshLayout) {
//               // refreshLayout.finishRefresh(2000/*,false*/); //传入false表示刷新失败
//               pageNum = 1;
//                getVideoList(true);
//            }
//        });
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            //下拉加载
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
////                refreshLayout.finishLoadMore(2000/*,false*/); //传入false表示加载失败
//                pageNum++;
//                // true false 判断是否是刷新
//                getVideoList(false);
//            }
//        });
//
//
//        getVideoList(true);
  //  }



    //获取视频列表
    private void getVideoList(boolean isRefresh){
        //获取token
       // String token = getStringFromSp("token");
       // if(!StringUtils.isEmpty(token)){
            HashMap<String,Object> params = new HashMap<>();
            //添加token
//            params.put("token",token);
            params.put("page",pageNum);
            params.put("limit",ApiConfig.PAGE_SIZE);
            params.put("categoryId",categoryId);
            //参数params  发送get请求
           Api.config(ApiConfig.VIDEO_LIST_BY_CATEGORY,params).getRequest(getActivity(), new TtitCallback() {
                @Override
                public void onSuccess(String res) {
                    if(isRefresh){
                        //如果是下拉刷新
                        refreshLayout.finishRefresh(true);
                    }else{
                        refreshLayout.finishLoadMore(true);
                    }

                    VideoListResponse response = new Gson().fromJson(res,VideoListResponse.class);

                    if(response != null && response.getCode() == 0){

                        List<VideoEntity> list = response.getPage().getList();
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
//            navigateTo(LoginActivity.class);
//        }
    }
}
