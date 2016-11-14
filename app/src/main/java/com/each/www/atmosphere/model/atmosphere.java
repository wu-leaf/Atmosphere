package com.each.www.atmosphere.model;

/**
 * Created by Veyron on 2016/10/23.
 * Function：气象实体类
 */
/**
 * [
 {
 "id": "28800",
 "devId": "1",
 "lng": "110.301691",
 "lat": "21.151896",
 "t": "26.3",
 "h": "95",
 "p": "11",
 "uv": "100",
 "cd": "100",
 "wd": "4",
 "wr": "100",
 "pm": "100",
 "dust": "100",
 "water": "109.5",
 "create_at": "2016-11-20 23:59:00"
 }
 ]
 */

public class atmosphere {
    private String id;
    private String devId; //设备id
    private String lng;
    private String lat;
    private String t;     //温度
    private String h;     //湿度
    private String p;     //大气压
    private String uv;    //紫外线
    private String cd;    //光照
    private String wd;    //风向
    private String wr;    //风级
    private String pm;    //PM 2.5
    private String dust;  //空气质量
    private String water; //降水
    private String create_at; //时间


    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getWr() {
        return wr;
    }

    public void setWr(String wr) {
        this.wr = wr;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getDust() {
        return dust;
    }

    public void setDust(String dust) {
        this.dust = dust;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }



    @Override
    public String toString() {
        return "atmosphere{" +
                "id='" + id + '\'' +
                ", devId='" + devId + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", t='" + t + '\'' +
                ", h='" + h + '\'' +
                ", p='" + p + '\'' +
                ", uv='" + uv + '\'' +
                ", cd='" + cd + '\'' +
                ", wd='" + wd + '\'' +
                ", wr='" + wr + '\'' +
                ", pm='" + pm + '\'' +
                ", dust='" + dust + '\'' +
                ", water='" + water + '\'' +
                ", create_at='" + create_at + '\'' +
                '}';
    }

}
