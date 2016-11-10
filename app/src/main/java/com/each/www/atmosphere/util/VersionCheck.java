package com.each.www.atmosphere.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.each.www.atmosphere.MyApplication;
import com.each.www.atmosphere.model.version;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.logging.Handler;

/**
 * Created by Veyron on 2016/11/8.
 * Function：检查服务器版本号，一个工具类
 */
public class VersionCheck {

    public static Boolean isNewVersion(int newVersionCode,int nowVersionCode){
        //判断是否新版本
        if (newVersionCode > nowVersionCode){
            //可以更新
            return true;
        }
        return false;
    }

    /*
     * 取得程序的当前版本
     */
    public static int getNowVerCode(Context context) {
        int verCode = 0;
        try {
            verCode = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return verCode;
    }
    public static String getNowVerName(Context context){
        String VerName = "";
        try{
            VerName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        }catch (PackageManager.NameNotFoundException e){

        }
        return VerName;
    }

}
