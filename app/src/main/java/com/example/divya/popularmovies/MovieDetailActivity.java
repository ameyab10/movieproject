package com.example.divya.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        MovieDetail detail = intent.getParcelableExtra("movieDetail");
        MovieDetailActivityFragment f  = (MovieDetailActivityFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
        f.setMovieDetail(detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
