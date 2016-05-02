package com.example.divya.popularmovies.content;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by abarsode on 5/1/16.
 */
public class MovieContract {

    /**
     * The Content Authority is a name for the entire content provider, similar to the relationship
     * between a domain name and its website. A convenient string to use for content authority is
     * the package name for the app, since it is guaranteed to be unique on the device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.divya.popularmovies";

    /**
     * The content authority is used to create the base of all URIs which apps will use to
     * contact this content provider.
     */
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * A list of possible paths that will be appended to the base URI for each of the different
     * tables.
     */
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_GENRE = "genre";


    /**
     * Create one class for each table that handles all information regarding the table schema and
     * the URIs related to it.
     */
    public static final class MovieEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_MOVIE;

        // Define the table schema
        public static final String TABLE_NAME = "movieTable";
        public static final String COLUMN_NAME = "movieName";
        public static final String COLUMN_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_GENRE = "movieGenre";
        public static final String COLUMN_POSTER_URL = "posterUrl";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_OVERVIEW = "overView";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_TRAILERS_URL = "trailerUrl";
        public static final String COLUMN_TRAILERS_TITLES = "trailerTitles";
        public static final String COLUMN_REVIEW_AUTHORS = "movieAuthor";
        public static final String COLUMN_REVIEW_CONTENT = "movieReviewContent";

        // Define a function to build a URI to find a specific movie by it's identifier
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class GenreEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_GENRE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_GENRE;

        public static final String TABLE_NAME = "genreTable";
        public static final String COLUMN_NAME = "genreName";

        public static Uri buildGenreUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
