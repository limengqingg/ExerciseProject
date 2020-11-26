package com.tti.myapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.component.PrepareView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tti.myapp.R;
import com.tti.myapp.activity.HomeActivity;
import com.tti.myapp.api.Api;
import com.tti.myapp.api.ApiConfig;
import com.tti.myapp.api.TtitCallback;
import com.tti.myapp.entity.BaseResponse;
import com.tti.myapp.entity.LoginResponse;
import com.tti.myapp.entity.VideoEntity;
import com.tti.myapp.listener.OnItemChildClickListener;
import com.tti.myapp.listener.OnItemClickListener;
import com.tti.myapp.view.CircleTransform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//    在复写RecyclerView.Adapter的时候，需要我们复写两个方法：
//    onCreateViewHolder
//    onBindViewHolder
//    这两个方法从字面上看就是创建ViewHolder和绑定ViewHolder的意思
public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<VideoEntity> datas;

    public void setDatas(List<VideoEntity> datas) {
        this.datas = datas;
    }

    private OnItemChildClickListener mOnItemChildClickListener;

    private OnItemClickListener mOnItemClickListener;

    public VideoAdapter(Context context) {
        this.mContext = context;
    }

    public VideoAdapter(Context context, List<VideoEntity> datas) {
        this.mContext = context;
        this.datas = datas;
    }


    @NonNull
    @Override
//     用户滑动屏幕切换视图时，上一个视图会回收利用，RecyclerView所做的就是回收再利用，循环往复。
// ViewHolder的主要任务：容纳View视图。
// Adapter从模型层获取数据，然后提供给RecyclerView显示，是沟通的桥梁。Adapter主要的任务是：创建ViewHolder和将模型层的数据绑定到ViewHolder上

    //调用Adapter.onCreateViewHolder(ViewGroup, int)创建ViewHolder
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    //position 下标
    //onBindViewHolder(CrimeHolder holder, int position)
    // 将数据绑定在ViewHolder上。
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //给控件对象赋值
//        ViewHolder vh = (ViewHolder) holder;
//        VideoEntity videoEntity = datas.get(position);
//        vh.tvTitle.setText(videoEntity.getVtitle());
//        vh.tvAuthor.setText(videoEntity.getAuthor());
//        //把int转换成字符串,点赞数量是int
//        vh.tvDz.setText(String.valueOf(videoEntity.getLikeNum()));
//        vh.tvComment.setText(String.valueOf(videoEntity.getCommentNum()));
//        vh.tvDz.setText(String.valueOf(videoEntity.getCollectNum()));
//
//        //加载异步图片
//        Picasso.with(mContext).load(videoEntity.getHeadurl()).into(vh.imgHeader);
//        Picasso.with(mContext).load(videoEntity.getCoverurl()).into(vh.imgCover);

        ViewHolder vh = (ViewHolder) holder;
        VideoEntity videoEntity = datas.get(position);
        vh.tvTitle.setText(videoEntity.getVtitle());
        vh.tvAuthor.setText(videoEntity.getAuthor());
//        vh.tvDz.setText(String.valueOf(videoEntity.getLikeNum()));
//        vh.tvComment.setText(String.valueOf(videoEntity.getCommentNum()));
//        vh.tvCollect.setText(String.valueOf(videoEntity.getCollectNum()));
        if(videoEntity.getVideoSocialEntity() != null){
            int likenum = videoEntity.getVideoSocialEntity().getLikenum();
            int commentnum = videoEntity.getVideoSocialEntity().getCommentnum();
            int collectnum = videoEntity.getVideoSocialEntity().getCollectnum();
            boolean flagLike = videoEntity.getVideoSocialEntity().isFlagLike();
            boolean flagCollect = videoEntity.getVideoSocialEntity().isFlagCollect();

            if(flagLike){
                vh.tvDz.setTextColor(Color.parseColor("#E21918"));
                vh.imgDizan.setImageResource(R.mipmap.dianzan_select);
            }
            if(flagCollect){
                vh.tvCollect.setTextColor(Color.parseColor("#E21918"));
                vh.imgCollect.setImageResource(R.mipmap.collect_select);
            }
            vh.tvDz.setText(String.valueOf(likenum));
            vh.tvComment.setText(String.valueOf(commentnum));
            vh.tvCollect.setText(String.valueOf(collectnum));
            vh.flagCollect = flagCollect;
            vh.flagLike = flagLike;
        }



        Picasso.with(mContext).load(videoEntity.getHeadurl())
                .transform(new CircleTransform())
                .into(vh.imgHeader);

        //找到缩略图
        Picasso.with(mContext).load(videoEntity.getCoverurl()).into(vh.mThumb);
        vh.mPosition = position;
    }

    @Override
    // getItemCount() 返回总共要显示的列表的数量(创建的ViewHolder数量比前者要小得多)
    public int getItemCount() {
        if(datas != null && datas.size()>0){
            return datas.size();
        }
        return 0;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvDz;
        private TextView tvComment;
        private TextView tvCollect;
        private ImageView imgHeader;
       // private ImageView imgCover;

        private ImageView imgCollect;
        private ImageView imgDizan;

        //视频播放的缩略图
        public ImageView mThumb;
        public PrepareView mPrepareView;
        //mPlayerContainer 包裹视频播放器的
        public FrameLayout mPlayerContainer;
        //public int mPosition;
        public int mPosition;

        private boolean flagCollect;
        private  boolean flagLike;

        //定义的写死的头部图片
//        private  ImageView imgHead;
//        private  ImageView imgCon;


        //把view传过来
        public ViewHolder(@NonNull View view) {
            super(view);
            tvTitle = view.findViewById(R.id.title);
            tvAuthor = view.findViewById(R.id.author);
            tvDz = view.findViewById(R.id.dz);
            tvComment = view.findViewById(R.id.comment);
            tvCollect = view.findViewById(R.id.collect);
            imgHeader = view.findViewById(R.id.img_header);
            imgCollect = view.findViewById(R.id.img_collect);
            imgDizan = view.findViewById(R.id.img_like);
          //  imgCover = view.findViewById(R.id.img_cover);

//            imgHead = view.findViewById(R.id.img_header);
//            imgCon = view.findViewById(R.id.img_cover);

            mPlayerContainer = view.findViewById(R.id.player_container);
            mPrepareView = view.findViewById(R.id.prepare_view);
            mThumb = mPrepareView.findViewById(R.id.thumb);
            if (mOnItemChildClickListener != null) {
                mPlayerContainer.setOnClickListener(this);
            }
            if (mOnItemClickListener != null) {
                view.setOnClickListener(this);
            }
            //收藏
            imgCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int collectNum = Integer.parseInt(tvCollect.getText().toString());
                    if(flagCollect){   //已收藏
                        if(collectNum > 0){
                            tvCollect.setText(String.valueOf(--collectNum));
                            tvCollect.setTextColor(Color.parseColor("#161616"));
                            imgCollect.setImageResource(R.mipmap.collect);
                            //type 1 是收藏  2 是点赞
                            updateCount(datas.get(mPosition).getVid(),1,!flagCollect);
                        }
                    }else{    //未收藏
                            tvCollect.setText(String.valueOf(++collectNum));
                            tvCollect.setTextColor(Color.parseColor("#E21918"));
                            imgCollect.setImageResource(R.mipmap.collect_select);
                            updateCount(datas.get(mPosition).getVid(),1,!flagCollect);
                    }
                    //这个地方是给UI做显示用的
                    flagCollect = !flagCollect;
                }
            });

            //点赞
            imgDizan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int likeNum = Integer.parseInt(tvDz.getText().toString());
                    if(flagLike){   //已点赞
                        if(likeNum > 0){
                            tvDz.setText(String.valueOf(--likeNum));
                            tvDz.setTextColor(Color.parseColor("#161616"));
                            imgDizan.setImageResource(R.mipmap.dianzan);
                            //type 2 是点赞
                            updateCount(datas.get(mPosition).getVid(),2,!flagLike);
                        }
                    }else{    //未点赞
                        tvDz.setText(String.valueOf(++likeNum));
                        tvDz.setTextColor(Color.parseColor("#E21918"));
                        imgDizan.setImageResource(R.mipmap.dianzan_select);
                        updateCount(datas.get(mPosition).getVid(),2,!flagLike);
                    }
                    flagLike = !flagLike;
                }
            });
            //通过tag将ViewHolder和itemView绑定
            view.setTag(this);
        }
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.player_container) {
                if (mOnItemChildClickListener != null) {
                    mOnItemChildClickListener.onItemChildClick(mPosition);
                }
            } else {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mPosition);
                }
            }

        }
    }

    //点赞收藏的接口
    private void updateCount(int vid,int type,boolean flag){
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("vid",vid);
        params.put("type",type);
        params.put("flag",flag);
//        Api.config("/app/login",params).postRequest(new TtitCallback() {
        Api.config(ApiConfig.VIDEO_UPDATE_COUNT,params).postRequest(mContext,new TtitCallback() {
            @Override
            public void onSuccess(final String res) {
                Log.e("onSuccess",res);
//                showToastSync(res);
                //转换成实体类再取出token
                //创建gson  gson是谷歌用来解析json
                Gson gson = new Gson();
                //把json字符串转换成LoginResponse.class
                //把res变成了实体对象
                BaseResponse baseResponse = gson.fromJson(res,BaseResponse.class);
                //loginResponse.getCode==0说明登录成功
               if(baseResponse.getCode() == 0){

               }


            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }


    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
