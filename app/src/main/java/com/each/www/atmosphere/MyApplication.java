package com.each.www.atmosphere;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Veyron on 2016/10/23.
 * Function：
 */
public class MyApplication extends Application{
    private static RequestQueue queues ;
    @Override
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getHttpQueues() {
        return queues;
    }
}
