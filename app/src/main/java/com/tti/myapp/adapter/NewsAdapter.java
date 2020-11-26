package com.tti.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.component.PrepareView;
import com.squareup.picasso.Picasso;
import com.tti.myapp.R;
import com.tti.myapp.entity.NewsEntity;
import com.tti.myapp.listener.OnItemChildClickListener;
import com.tti.myapp.listener.OnItemClickListener;
import com.tti.myapp.view.CircleTransform;

import java.io.Serializable;
import java.util.List;

//    在复写RecyclerView.Adapter的时候，需要我们复写两个方法：
//    onCreateViewHolder
//    onBindViewHolder
//    这两个方法从字面上看就是创建ViewHolder和绑定ViewHolder的意思
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<NewsEntity> datas;

    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setDatas(List<NewsEntity> datas) {
        this.datas = datas;
    }

    public NewsAdapter(Context context) {
        this.mContext = context;
    }

    public NewsAdapter(Context context, List<NewsEntity> datas) {
        this.mContext = context;
        this.datas = datas;
    }

    @Override
    //getItemViewType  当前布局的类型
    public int getItemViewType(int position) {
        int type = datas.get(position).getType();
        return type;
    }

    @NonNull
    @Override
//     用户滑动屏幕切换视图时，上一个视图会回收利用，RecyclerView所做的就是回收再利用，循环往复。
// ViewHolder的主要任务：容纳View视图。
// Adapter从模型层获取数据，然后提供给RecyclerView显示，是沟通的桥梁。Adapter主要的任务是：创建ViewHolder和将模型层的数据绑定到ViewHolder上

    //调用Adapter.onCreateViewHolder(ViewGroup, int)创建ViewHolder
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.new_item_one, parent, false);
            return new ViewHolderOne(view);
        } else if (viewType == 2) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_item_two, parent, false);
            return new ViewHolderTwo(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_item_three, parent, false);
            return new ViewHolderThree(view);
        }
    }

    @Override
    //position 下标
    //onBindViewHolder(CrimeHolder holder, int position)
    // 将数据绑定在ViewHolder上。渲染数据
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      int type = getItemViewType(position);
      NewsEntity newsEntity = datas.get(position);
      if(type == 1){
          ViewHolderOne vh = (ViewHolderOne) holder;
          vh.title.setText(newsEntity.getNewsTitle());
          vh.author.setText(newsEntity.getAuthorName());
          vh.comment.setText(newsEntity.getCommentCount()+"评论 .");
          vh.time.setText(newsEntity.getReleaseDate());
          vh.newsEntity = newsEntity;

          Picasso.with(mContext)
                  .load(newsEntity.getHeaderUrl())
                  .transform(new CircleTransform())
                  .into(vh.header);
          //找到缩略图
          Picasso.with(mContext)
                  .load(newsEntity.getThumbEntities().get(0).getThumbUrl())
                  .into(vh.thumb);

      }else if (type == 2){
            ViewHolderTwo vh = (ViewHolderTwo) holder;
          vh.title.setText(newsEntity.getNewsTitle());
          vh.author.setText(newsEntity.getAuthorName());
          vh.comment.setText(newsEntity.getCommentCount()+"评论 .");
          vh.time.setText(newsEntity.getReleaseDate());
          vh.newsEntity = newsEntity;

          Picasso.with(mContext)
                  .load(newsEntity.getHeaderUrl())
                  .transform(new CircleTransform())
                  .into(vh.header);
          //找到缩略图
          Picasso.with(mContext)
                  .load(newsEntity.getThumbEntities().get(0).getThumbUrl())
                  .into(vh.pic1);
          Picasso.with(mContext)
                  .load(newsEntity.getThumbEntities().get(1).getThumbUrl())
                  .into(vh.pic2);
          Picasso.with(mContext)
                  .load(newsEntity.getThumbEntities().get(1).getThumbUrl())
                  .into(vh.pic3);
        }else{
          ViewHolderThree vh = (ViewHolderThree) holder;
          vh.title.setText(newsEntity.getNewsTitle());
          vh.author.setText(newsEntity.getAuthorName());
          vh.comment.setText(newsEntity.getCommentCount()+"评论 .");
          vh.time.setText(newsEntity.getReleaseDate());
          vh.newsEntity = newsEntity;
          Picasso.with(mContext)
                  .load(newsEntity.getHeaderUrl())
                  .transform(new CircleTransform())
                  .into(vh.header);
          //找到缩略图
          Picasso.with(mContext)
                  .load(newsEntity.getThumbEntities().get(0).getThumbUrl())
                  .into(vh.thumb);
      }
    }

    @Override
    // getItemCount() 返回总共要显示的列表的数量(创建的ViewHolder数量比前者要小得多)
    public int getItemCount() {
        if(datas != null && datas.size()>0){
            return datas.size();
        }
        return 0;
    }

    public  class ViewHolderOne extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header;
        private  ImageView thumb;
        private NewsEntity newsEntity;
        //把view传过来
        public ViewHolderOne(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            comment = view.findViewById(R.id.comment);
            time = view.findViewById(R.id.time);
            header = view.findViewById(R.id.header);
            thumb = view.findViewById(R.id.thumb);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(newsEntity);
                }
            });
        }
    }
    public  class ViewHolderTwo extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header;
        private  ImageView pic1,pic2,pic3;
        private NewsEntity newsEntity;
        //把view传过来
        public ViewHolderTwo(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            comment = view.findViewById(R.id.comment);
            time = view.findViewById(R.id.time);
            header = view.findViewById(R.id.header);
            pic1 = view.findViewById(R.id.pic1);
            pic2 = view.findViewById(R.id.pic2);
            pic3 = view.findViewById(R.id.pic3);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(newsEntity);
                }
            });

        }
    }
    public  class ViewHolderThree extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header;
        private  ImageView thumb;
        private NewsEntity newsEntity;
        //把view传过来
        public ViewHolderThree(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            comment = view.findViewById(R.id.comment);
            time = view.findViewById(R.id.time);
            header = view.findViewById(R.id.header);
            thumb = view.findViewById(R.id.thumb);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(newsEntity);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Serializable obj);
    }
}
