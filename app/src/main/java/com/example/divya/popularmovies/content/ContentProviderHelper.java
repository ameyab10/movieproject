package com.example.divya.popularmovies.content;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.divya.popularmovies.MovieDetail;
import com.example.divya.popularmovies.Utils;

import java.util.ArrayList;

public class ContentProviderHelper {

    public static ArrayList<MovieDetail> getMoviesFromContentProvider(Context context) {

        ArrayList<MovieDetail> movieDetails = new ArrayList<>();
        try {
            String[] mProjection =
                    {
                            MovieContract.MovieEntry._ID,
                            MovieContract.MovieEntry.COLUMN_NAME,
                            MovieContract.MovieEntry.COLUMN_OVERVIEW,
                            MovieContract.MovieEntry.COLUMN_RATING,
                            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                            MovieContract.MovieEntry.COLUMN_POSTER_URL,
                            MovieContract.MovieEntry.COLUMN_REVIEW_AUTHORS,
                            MovieContract.MovieEntry.COLUMN_REVIEW_CONTENT,
                            MovieContract.MovieEntry.COLUMN_THUMBNAIL,
                            MovieContract.MovieEntry.COLUMN_TRAILERS_TITLES,
                            MovieContract.MovieEntry.COLUMN_TRAILERS_URL,
                    };

            String mSelectionClause = null;
            String[] mSelectionArgs = null;
            Cursor cursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    mProjection,
                    mSelectionClause,
                    mSelectionArgs,
                    null);

            int movieIndex = cursor.getColumnIndex(MovieContract.MovieEntry._ID);
            int movieNameIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME);
            int posterIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_URL);
            int reviewIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_REVIEW_CONTENT);
            int authorIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_REVIEW_AUTHORS);
            int trailerUrlIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TRAILERS_URL);
            int traierTitleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TRAILERS_TITLES);
            int thumbnailIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_THUMBNAIL);
            int ratingIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
            int releaseDateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
            int overViewIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);

            while (cursor.moveToNext()) {
                MovieDetail movie = new MovieDetail();
                movie.movieId = cursor.getString(movieIndex);
                movie.originalTitle = cursor.getString(movieNameIndex);
                movie.posterUrl = "http://image.tmdb.org/t/p/w185" + cursor.getString(posterIndex);
                movie.mReviews = Utils.stringToList(cursor.getString(reviewIndex));
                movie.mReviewAuthors = Utils.stringToList(cursor.getString(authorIndex));
                movie.mTrailers = Utils.stringToList(cursor.getString(trailerUrlIndex));
                movie.mTrailerTitles = Utils.stringToList(cursor.getString(traierTitleIndex));
                movie.thumbnail = cursor.getString(thumbnailIndex);
                movie.rating = cursor.getString(ratingIndex);
                movie.releaseDate = cursor.getString(releaseDateIndex);
                movie.overview = cursor.getString(overViewIndex);
                movieDetails.add(movie);
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Failed to fetch data from content provider", e);
        }

        return movieDetails;
    }
}
