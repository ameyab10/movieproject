package com.example.divya.popularmovies.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movieList.db";

    public MovieDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        addMovieTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Inserts the movie table into the database.
     * @param db The SQLiteDatabase the table is being inserted into.
     */
    private void addMovieTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                        MovieContract.MovieEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_GENRE + " INTEGER NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_POSTER_URL + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_REVIEW_AUTHORS + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_REVIEW_CONTENT + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_THUMBNAIL + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_TRAILERS_TITLES + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_TRAILERS_URL + " TEXT NOT NULL, " +
                        "FOREIGN KEY (" + MovieContract.MovieEntry.COLUMN_GENRE + ") " +
                        "REFERENCES " + MovieContract.GenreEntry.TABLE_NAME + " (" + MovieContract.GenreEntry._ID + "));"
        );
    }
}