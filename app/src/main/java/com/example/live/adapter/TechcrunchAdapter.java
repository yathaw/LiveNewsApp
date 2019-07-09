package com.example.live.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.live.models.techcrunch.ArticlesItem;
import com.example.live.utils.Utils;

import java.util.List;

public class TechcrunchAdapter extends RecyclerView.Adapter<TechcrunchAdapter.MyViewHolder>
{

    private OnItemClickListener onItemClickListener;
    private List<ArticlesItem> techarticlesItems;
    private Context context;

    public TechcrunchAdapter(List<ArticlesItem> techarticlesItems)
    {
        this.techarticlesItems = techarticlesItems;
    }

    public interface  OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.techcrunch_layout, viewGroup, false);
        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position)
    {
        final MyViewHolder holder = myViewHolder;
        ArticlesItem model = techarticlesItems.get(position);

        RequestOptions requestOptions = new RequestOptions();

        requestOptions.placeholder(Utils.getRandomDrawableColor());
        requestOptions.error(Utils.getRandomDrawableColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(myViewHolder.imageView.getContext())
                .load(model.getUrlToImage())
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);

        Log.d("Image", String.valueOf(model.getUrlToImage()));


        holder.title.setText(model.getTitle());
    }

    @Override
    public int getItemCount()
    {
        return techarticlesItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView title;
        ImageView imageView;
        OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener)
        {
            super(itemView);
            itemView.setOnClickListener(this);

            title = itemView.findViewById(R.id.techcrunch_title);
            imageView = itemView.findViewById(R.id.techcrunch_img);

            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v)
        {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
