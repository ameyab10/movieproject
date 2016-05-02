package com.example.divya.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by divya on 3/5/16.
 */
public class MovieGridAdapter extends ArrayAdapter<MovieDetail> {
    private Activity context;
    public  MovieGridAdapter(Activity context,List<MovieDetail> movieDetails){

        super(context,0,movieDetails);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieDetail movie=getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_view_layout, parent, false);
        }
        ImageView iconView = (ImageView) convertView.findViewById(R.id.poster_image);
        Picasso.with(context).load(movie.posterUrl).into(iconView);

        return  convertView;
    }
}
