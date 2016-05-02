package com.example.divya.popularmovies.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyVolley {

    private static MyVolley sInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private MyVolley(Context context) {
        mCtx = context.getApplicationContext();
        mRequestQueue = getRequestQueue();
    }

    public static MyVolley getInstance(Context context) {
        synchronized (MyVolley.class) {
            if (sInstance == null) {
                sInstance = new MyVolley(context);
            }
            return sInstance;
        }
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

}
