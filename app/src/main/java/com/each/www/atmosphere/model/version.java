package com.each.www.atmosphere.model;

/**
 * Created by Veyron on 2016/11/9.
 * Function：版本实体类
 */
public class version {
    private int versionCode;
    private String apkUrl;

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }


}
