package com.example.ayaelbeltagye.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aya Elbeltagye on 8/23/2016.
 */
public class ReviewsAdapter extends BaseAdapter {
    Context myContext;
    ArrayList<Review> reviewsArray;
    public ReviewsAdapter(Context c,ArrayList<Review> arrayList) {
        myContext=c;
        reviewsArray=arrayList;
    }

    @Override
    public int getCount() {
        return reviewsArray.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewsArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.reviews, parent, false);
        TextView content = (TextView) view.findViewById(R.id.textView6);
        content.setText("content" + reviewsArray.get(position).getContent());
        return view;
    }

}

