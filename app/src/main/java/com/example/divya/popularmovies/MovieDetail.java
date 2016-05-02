package com.example.divya.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divya on 3/5/16.
 */
public class MovieDetail implements Parcelable {

    public String posterUrl, originalTitle, thumbnail, overview, rating, releaseDate, movieId;
    public List<String> mTrailers, mTrailerTitles, mReviewAuthors, mReviews;

    public MovieDetail() {

    }

    public MovieDetail(String url, String originalTitle, String thumbnail, String overview, String rating, String releaseDate, String movieId) {
        this.posterUrl = url;
        this.originalTitle = originalTitle;
        this.thumbnail = thumbnail;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
    }

    public MovieDetail(String url, String originalTitle, String thumbnail, String overview, String rating, String releaseDate, String movieId, List<String> trailerUris) {
        this(url, originalTitle, thumbnail, overview,rating, releaseDate, movieId);
        mTrailers = trailerUris;
    }

    public MovieDetail(Parcel parcel) {
        posterUrl = parcel.readString();
        originalTitle = parcel.readString();
        thumbnail = parcel.readString();
        overview = parcel.readString();
        rating = parcel.readString();
        releaseDate = parcel.readString();
        movieId = parcel.readString();
        mTrailers = new ArrayList<String>();
        parcel.readList(mTrailers, null);
        mTrailerTitles = new ArrayList<>();
        parcel.readList(mTrailerTitles, null);
        mReviewAuthors = new ArrayList<>();
        parcel.readList(mReviewAuthors, null);
        mReviews = new ArrayList<>();
        parcel.readList(mReviews, null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(posterUrl);
        parcel.writeString(originalTitle);
        parcel.writeString(thumbnail);
        parcel.writeString(overview);
        parcel.writeString(rating);
        parcel.writeString(releaseDate);
        parcel.writeString(movieId);
        parcel.writeList(mTrailers);
        parcel.writeList(mTrailerTitles);
        parcel.writeList(mReviewAuthors);
        parcel.writeList(mReviews);
    }

    public final static Parcelable.Creator<MovieDetail> CREATOR = new Parcelable.Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel parcel) {
            return new MovieDetail(parcel);
        }

        @Override
        public MovieDetail[] newArray(int i) {
            return new MovieDetail[i];
        }

    };
}

