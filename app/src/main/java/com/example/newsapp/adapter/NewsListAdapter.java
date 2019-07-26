package com.example.newsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newsapp.R;
import com.example.newsapp.activity.NewsActivity;
import com.example.newsapp.model.NewsDetail;
import com.example.newsapp.others.DownloadImageTask;
import com.example.newsapp.others.ImagesCache;
import com.squareup.picasso.Picasso;

import java.util.List;


public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsListViewHolder>  {
    private List<NewsDetail> newsDetailsListModel;
    private int rowLayout;
    private Context context;



    public NewsListAdapter(List<NewsDetail> newsDetailsListModel, int rowLayout, Context context) {
        this.newsDetailsListModel = newsDetailsListModel;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout,viewGroup,false);
        return new NewsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsListViewHolder holder, final int position) {
        holder.title.setText(newsDetailsListModel.get(position).getTitle());
        final String imageUrl = newsDetailsListModel.get(position).getImage();
        setImagePoster(holder,position);
        Picasso.get().load(imageUrl).into(holder.poster);
        final String news = newsDetailsListModel.get(position).getNews();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("url", news);
                context.startActivity(intent);
            }
        });


    }

    private void setImagePoster(NewsListViewHolder holder, int position){
        ImagesCache cache = ImagesCache.getInstance();
        cache.initializeCache();

        String img = newsDetailsListModel.get(position).getImage();

        Bitmap bm = cache.getImageFromWarehouse(img);

        if(bm != null)
        {
            holder.poster.setImageBitmap(bm);
        }
        else
        {
            holder.poster.setImageBitmap(null);

            DownloadImageTask imgTask = new DownloadImageTask(cache, holder.poster, holder.poster.getWidth(), holder.poster.getHeight());//Since you are using it from `Activity` call second Constructor.

            imgTask.execute(img);
        }
    }

    @Override
    public int getItemCount() {
        return newsDetailsListModel.size();
    }


    public static class NewsListViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout newsList;
        TextView title;
        ImageView poster;

        public NewsListViewHolder(@NonNull View v) {
            super(v);
            newsList = v.findViewById(R.id.news_item_layout);
            title = v.findViewById(R.id.title);
            poster = v.findViewById(R.id.newsImage);
        }
    }
}
