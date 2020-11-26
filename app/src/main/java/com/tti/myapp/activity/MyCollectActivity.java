package com.tti.myapp.activity;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tti.myapp.R;
import com.tti.myapp.adapter.MyCollectAdapter;
import com.tti.myapp.api.Api;
import com.tti.myapp.api.ApiConfig;
import com.tti.myapp.api.TtitCallback;
import com.tti.myapp.entity.MyCollectResponse;
import com.tti.myapp.entity.VideoEntity;
import com.tti.myapp.entity.VideoListResponse;
import com.tti.myapp.fragment.VideoFragment;
import com.tti.myapp.listener.OnItemChildClickListener;
import com.tti.myapp.util.Tag;
import com.tti.myapp.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyCollectActivity extends  BaseActivity implements OnItemChildClickListener {
    private RecyclerView recyclerView;
    private int pageNum = 1;//当前是哪一页
    private MyCollectAdapter myCollectAdapter;
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
                    myCollectAdapter.setDatas(datas);
                    myCollectAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_mycollect;
    }

    @Override
    protected void initView() {
        //初始化initVideoView
        initVideoView();
        recyclerView = findViewById(R.id.recyclerView);

    }


    @Override
    protected void initData() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        myCollectAdapter = new MyCollectAdapter(this);
        myCollectAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(myCollectAdapter);
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

        //getVideoList(true);
        getVideoList();
    }
    //初始化播放器
    protected void initVideoView(){
        mVideoView = new VideoView(this);
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
        mController = new StandardVideoController(this);
        mErrorView = new ErrorView(this);
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(this);
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(this);
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(this));
        mController.addControlComponent(new GestureView(this));
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
        MyCollectAdapter.ViewHolder viewHolder = (MyCollectAdapter.ViewHolder) itemView.getTag();
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
        if (this.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }

    //获取视频列表
    private void getVideoList(){
        //获取token

        HashMap<String,Object> params = new HashMap<>();


        //参数params  发送get请求
        Api.config(ApiConfig.VIDEO_MYCOLLECT,params).getRequest(this, new TtitCallback() {
            @Override
            public void onSuccess(String res) {

                MyCollectResponse response = new Gson().fromJson(res,MyCollectResponse.class);

                if(response != null && response.getCode() == 0){

                    List<VideoEntity> list = response.getList();
                    if(list != null && list.size()>0){
                            datas = list;
                        mHandler.sendEmptyMessage(0);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
    protected VideoViewManager getVideoViewManager(){
        return VideoViewManager.instance();
    }
}
