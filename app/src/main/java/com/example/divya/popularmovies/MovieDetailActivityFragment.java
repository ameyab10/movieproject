package com.example.divya.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.divya.popularmovies.content.MovieContract;
import com.example.divya.popularmovies.network.MyVolley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {
    MovieDetail detail;
    private static final String TRAILER_URI = "http://api.themoviedb.org/3/movie/%s/videos";
    private static final String REVIEWS_URI = "http://api.themoviedb.org/3/movie/%s/reviews";

    private LinearLayout trailerContainer;
    private LinearLayout reviewContainer;
    private Button addToFavButton;
    TextView titleView;
    TextView overviewView;
    TextView ratingView;
    TextView releaseView;
    ImageView thumbnailView;

    public MovieDetailActivityFragment() {
    }

    public void setMovieDetail(MovieDetail movieDetail) {
        detail = movieDetail;
        titleView.setText(detail.originalTitle);
        overviewView.setText(detail.overview);
        ratingView.setText(detail.rating + "/10");
        releaseView.setText(detail.releaseDate);
        Picasso.with(getActivity()).load(detail.posterUrl).into(thumbnailView);
        fetchTrailerLink(detail.movieId);
        fetchReviews(detail.movieId);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        titleView = (TextView) rootView.findViewById(R.id.movieTitle);
         overviewView = (TextView) rootView.findViewById(R.id.movieOverview);


         ratingView = (TextView) rootView.findViewById(R.id.movieRating);


         releaseView = (TextView) rootView.findViewById(R.id.movieRelease);


         thumbnailView = (ImageView) rootView.findViewById(R.id.thumbnail);

        trailerContainer = (LinearLayout) rootView.findViewById(R.id.trailer_container);
        reviewContainer = (LinearLayout) rootView.findViewById(R.id.reviews_container);
        addToFavButton = (Button) rootView.findViewById(R.id.add_to_favorites);
        addToFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert(detail);
            }
        });

        return rootView;
    }

    private void fetchTrailerLink(String movieId) {

        if (detail != null && detail.mTrailerTitles != null && detail.mTrailerTitles.size() > 0) {
            addTrailerViews(detail);
            return;
        }

        final String apiKey = "83e702f55739e6bb647e862d983d1505";
        String url = Uri.parse(String.format(TRAILER_URI, movieId)).buildUpon().appendQueryParameter("api_key", apiKey).toString();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            List<String> trailerList = new ArrayList<>();
                            List<String> trailerTitles = new ArrayList<>();
                            if (response != null && response.has("results")) {

                                JSONArray array = response.getJSONArray("results");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    final String trailerUri = Utils.getYoutubeUri(object.optString("key", ""));
                                    trailerList.add(trailerUri);
                                    trailerTitles.add(object.optString("name", ""));
                                }
                                detail.mTrailers = trailerList;
                                detail.mTrailerTitles = trailerTitles;
                                addTrailerViews(detail);
                            }

                        } catch (Exception e) {
                            Log.e("MovieDetailActivity", "Trailer request failed", e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("MovieDetailActivity", "Trailer request failed");
                    }
                });

        MyVolley.getInstance(getContext()).getRequestQueue().add(jsObjRequest);
    }

    private void addTrailerViews(final MovieDetail detail) {

        trailerContainer.removeAllViews();
        if (detail == null || detail.mTrailers == null) {
            return;
        }
        for (int i = 0; i < detail.mTrailers.size(); i++) {
            ViewGroup vg = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.movie_trailer, null);
            TextView tv = (TextView) vg.findViewById(R.id.trailer_title);
            if (detail.mTrailerTitles.size() > i) {
                tv.setText(detail.mTrailerTitles.get(i));
            }
            trailerContainer.addView(vg);
            final int finalI = i;
            vg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.watchYoutubeVideo(detail.mTrailers.get(finalI), getActivity());
                }
            });
        }
    }

    private void fetchReviews(String movieId) {

        if (detail != null && detail.mReviewAuthors != null && detail.mReviewAuthors.size() > 0) {
            addReviewViews(detail);
            return;
        }

        final String apiKey = "83e702f55739e6bb647e862d983d1505";
        String url = Uri.parse(String.format(REVIEWS_URI, movieId)).buildUpon().appendQueryParameter("api_key", apiKey).toString();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            List<String> authorList = new ArrayList<>();
                            List<String> contentList = new ArrayList<>();
                            if (response != null && response.has("results")) {

                                JSONArray array = response.getJSONArray("results");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    authorList.add(object.optString("author", ""));
                                    contentList.add(object.optString("content", ""));
                                }
                                detail.mReviewAuthors = authorList;
                                detail.mReviews = contentList;
                                addReviewViews(detail);
                            }

                        } catch (Exception e) {
                            Log.e("MovieDetailActivity", "Trailer request failed", e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("MovieDetailActivity", "Trailer request failed");
                    }
                });

        MyVolley.getInstance(getContext()).getRequestQueue().add(jsObjRequest);
    }

    private void addReviewViews(final MovieDetail detail) {

        reviewContainer.removeAllViews();
        if (detail == null || detail.mReviewAuthors == null) {
            return;
        }
        for (int i = 0; i < detail.mReviewAuthors.size(); i++) {
            ViewGroup vg = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.movie_reviews, null);
            TextView author = (TextView) vg.findViewById(R.id.movie_author);
            TextView review = (TextView) vg.findViewById(R.id.movie_review);
            if (detail.mReviewAuthors.size() > i) {
                author.setText(detail.mReviewAuthors.get(i));
            }
            if (detail.mReviews.size() > i) {
                review.setText(detail.mReviews.get(i));
            }
            reviewContainer.addView(vg);
        }
    }


    private void insert(MovieDetail movieDetail) {

        try {
            // Defines a new Uri object that receives the result of the insertion
            Uri mNewUri;

            ContentValues mNewValues = new ContentValues();

            mNewValues.put(MovieContract.MovieEntry._ID, movieDetail.movieId);
            mNewValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieDetail.movieId);
            mNewValues.put(MovieContract.MovieEntry.COLUMN_NAME, movieDetail.originalTitle);
            mNewValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movieDetail.overview);
            mNewValues.put(MovieContract.MovieEntry.COLUMN_RATING, movieDetail.rating);
            mNewValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieDetail.releaseDate);
            mNewValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, movieDetail.posterUrl);
            mNewValues.put(MovieContract.MovieEntry.COLUMN_REVIEW_AUTHORS, Utils.listToString(movieDetail.mReviewAuthors));
            mNewValues.put(MovieContract.MovieEntry.COLUMN_REVIEW_CONTENT, Utils.listToString(movieDetail.mReviews));
            mNewValues.put(MovieContract.MovieEntry.COLUMN_THUMBNAIL, movieDetail.thumbnail);
            mNewValues.put(MovieContract.MovieEntry.COLUMN_TRAILERS_TITLES, Utils.listToString(movieDetail.mTrailerTitles));
            mNewValues.put(MovieContract.MovieEntry.COLUMN_GENRE, "genre");
            mNewValues.put(MovieContract.MovieEntry.COLUMN_TRAILERS_URL, Utils.listToString(movieDetail.mTrailers));


            Cursor c = getActivity().getContentResolver().query(MovieContract.MovieEntry.buildMovieUri(Integer.parseInt(movieDetail.movieId)),
                    null, MovieContract.MovieEntry._ID + " = " + DatabaseUtils.sqlEscapeString(movieDetail.movieId), null, null);
            if(c.getCount() == 0) {
                // not found in database
                mNewUri = getActivity().getContentResolver().insert(
                        MovieContract.MovieEntry.CONTENT_URI,
                        mNewValues                          // the values to insert
                );

                Log.e("Awesome", "Successfully inserted for Uri : " + mNewUri);
            }


            //print the date bt requerying
        } catch (Exception e) {
            Log.e("DetailFragment", "Error while inserting in content provider", e);
        }

    }

}
