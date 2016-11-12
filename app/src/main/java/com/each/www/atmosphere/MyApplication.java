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
    private int value;
    @Override
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
        setValue(0);

    }
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static RequestQueue getHttpQueues() {
        return queues;
    }
}
