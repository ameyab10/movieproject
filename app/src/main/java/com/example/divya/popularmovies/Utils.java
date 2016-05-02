package com.example.divya.popularmovies;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static String getYoutubeUri(String movieId) {
        return "https://www.youtube.com/watch?v=" + movieId;
    }

    public static void watchYoutubeVideo(String id, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(id));
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e("Utils", "Error launching youtube", e);
        }
    }

    public static String listToString(List<String> strings) {
        StringBuilder rString = new StringBuilder();
        for (String each : strings) {
            rString.append("^").append(each);
        }
        return rString.toString();
    }

    public static List<String> stringToList(String string) {
        return Arrays.asList(string.split("\\^"));
    }

}
