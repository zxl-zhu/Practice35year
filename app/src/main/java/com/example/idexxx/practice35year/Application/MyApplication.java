package com.example.idexxx.practice35year.Application;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by idexxx on 2020/3/14.
 */

public class MyApplication extends Application {

    public static RequestQueue mQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mQueue = Volley.newRequestQueue( this );
    }

    public static RequestQueue getRequestQueue() {
        return mQueue;
    }

}
