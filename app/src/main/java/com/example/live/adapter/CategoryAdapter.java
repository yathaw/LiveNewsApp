package com.example.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.live.R;
import com.example.live.models.Category;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<Category> implements View.OnClickListener
{
    private ArrayList<Category> dataSet;
    Context mContext;

    private static class ViewHolder
    {
        TextView txtName;
        ImageView logo;
    }

    public CategoryAdapter(ArrayList<Category> data, Context context)
    {
        super(context, R.layout.categories_layout, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v)
    {

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Category category = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.categories_layout, parent, false);

            viewHolder.txtName = convertView.findViewById(R.id.txt_status);
            viewHolder.logo = convertView.findViewById(R.id.category_logo);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(category.getName());
        viewHolder.logo.setImageResource(category.getImageUrl());
        viewHolder.logo.setOnClickListener(this);
        viewHolder.logo.setTag(position);

        return convertView;
    }
}
