package com.each.www.atmosphere.util;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.each.www.atmosphere.services.DownloadService;


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
    public static void goUpdate(Context context){
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra("apkUrl", "http://each.ac.cn/new_apk/app-release.apk");
        context.startService(intent);
    }

}
