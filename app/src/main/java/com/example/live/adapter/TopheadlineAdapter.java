package com.example.live.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.live.R;
import com.example.live.models.topheadline.ArticlesItem;
import com.example.live.utils.Utils;

import java.util.List;

public class TopheadlineAdapter extends RecyclerView.Adapter<TopheadlineAdapter.MyViewHolder>
{
    private OnItemClickListener onItemClickListener;
    private List<ArticlesItem> topheadlinesarticlesItems;
    private int lastPosition = -1;
    private Context context;
    private View view;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    public TopheadlineAdapter(List<ArticlesItem> topheadlinesarticlesItems, Context context, View view)
    {
        this.topheadlinesarticlesItems = topheadlinesarticlesItems;
        this.context = context;
        this.view = view;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hotnews_layout, viewGroup, false);
        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position)
    {
        final MyViewHolder holder = myViewHolder;
        ArticlesItem model = topheadlinesarticlesItems.get(position);

        RequestOptions requestOptions = new RequestOptions();

        requestOptions.placeholder(Utils.getRandomDrawableColor());
        requestOptions.error(Utils.getRandomDrawableColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(myViewHolder.imageView.getContext())
                .load(model.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);

        holder.title.setText(model.getTitle());
        holder.author.setText(model.getSource().getName());
        holder.time.setText(Utils.DateToTimeFormat(model.getPublishedAt()));

    }

    @Override
    public int getItemCount() {
        return topheadlinesarticlesItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView title, author, time;
        ImageView imageView;
        OnItemClickListener onItemClickListener;
        ProgressBar progressBar;
        CardView container;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener)
        {
            super(itemView);
            itemView.setOnClickListener(this);

            container = itemView.findViewById(R.id.container);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            time = itemView.findViewById(R.id.time);
            imageView = itemView.findViewById(R.id.img);
            progressBar = itemView.findViewById(R.id.progress_load_photo);

            this.onItemClickListener = onItemClickListener;

        }

        @Override
        public void onClick(View v)
        {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
