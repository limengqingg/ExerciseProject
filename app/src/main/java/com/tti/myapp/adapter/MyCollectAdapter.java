package com.tti.myapp.adapter;

import android.content.Context;
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
import com.tti.myapp.api.Api;
import com.tti.myapp.api.ApiConfig;
import com.tti.myapp.api.TtitCallback;
import com.tti.myapp.entity.BaseResponse;
import com.tti.myapp.entity.VideoEntity;
import com.tti.myapp.listener.OnItemChildClickListener;
import com.tti.myapp.listener.OnItemClickListener;
import com.tti.myapp.view.CircleTransform;

import java.util.HashMap;
import java.util.List;

//    在复写RecyclerView.Adapter的时候，需要我们复写两个方法：
//    onCreateViewHolder
//    onBindViewHolder
//    这两个方法从字面上看就是创建ViewHolder和绑定ViewHolder的意思
public class MyCollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<VideoEntity> datas;

    public void setDatas(List<VideoEntity> datas) {
        this.datas = datas;
    }

    private OnItemChildClickListener mOnItemChildClickListener;

    private OnItemClickListener mOnItemClickListener;

    public MyCollectAdapter(Context context) {
        this.mContext = context;
    }

    public MyCollectAdapter(Context context, List<VideoEntity> datas) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mycollect_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    //position 下标
    //onBindViewHolder(CrimeHolder holder, int position)
    // 将数据绑定在ViewHolder上。
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder vh = (ViewHolder) holder;
        VideoEntity videoEntity = datas.get(position);
        vh.tvTitle.setText(videoEntity.getVtitle());
        vh.tvAuthor.setText(videoEntity.getAuthor());

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
        private ImageView imgHeader;

        //视频播放的缩略图
        public ImageView mThumb;
        public PrepareView mPrepareView;
        //mPlayerContainer 包裹视频播放器的
        public FrameLayout mPlayerContainer;
        public int mPosition;


        //把view传过来
        public ViewHolder(@NonNull View view) {
            super(view);
            tvTitle = view.findViewById(R.id.title);
            tvAuthor = view.findViewById(R.id.author);
            imgHeader = view.findViewById(R.id.img_header);
            mPlayerContainer = view.findViewById(R.id.player_container);
            mPrepareView = view.findViewById(R.id.prepare_view);
            mThumb = mPrepareView.findViewById(R.id.thumb);
            if (mOnItemChildClickListener != null) {
                mPlayerContainer.setOnClickListener(this);
            }
            if (mOnItemClickListener != null) {
                view.setOnClickListener(this);
            }

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


    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
