package com.example.ayaelbeltagye.popularmoviesapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Aya Elbeltagye on 9/21/2016.
 */
public class MovieAdapter extends BaseAdapter {
    String[] posters;
    private final Context context;

    public MovieAdapter(Context context, String[]  movies) {
        this.context = context;
        this.posters = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(500, 500));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(context).load(posters[position]).into(imageView);
        return imageView;
    }

    @Override
    public int getCount() {
        return posters.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

}

