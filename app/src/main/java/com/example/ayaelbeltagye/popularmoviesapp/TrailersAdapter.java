package com.example.ayaelbeltagye.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Aya Elbeltagye on 8/23/2016.
 */
public class TrailersAdapter extends BaseAdapter {
    Context myContext;
    ArrayList<Trailer> trailersArray;
    String key;
    public TrailersAdapter(Context c,ArrayList<Trailer> list){
        myContext=c;
        trailersArray=list;
    }
    @Override
    public int getCount() {
        return trailersArray.size();
    }

    @Override
    public Object getItem(int position) {
        return trailersArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.trailers, parent, false);
        TextView trailersName = (TextView) view.findViewById(R.id.textView7);
        String base="http://img.youtube.com/vi/";
        key=trailersArray.get(position).getKey();
        String extension="/default.jpg";
        trailersName.setText(trailersArray.get(position).getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="https://www.youtube.com/watch?v="+key;
                myContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        return view;

    }
}
