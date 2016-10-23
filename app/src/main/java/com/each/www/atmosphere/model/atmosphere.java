package com.each.www.atmosphere.model;

/**
 * Created by Veyron on 2016/10/23.
 * Function：气象实体类
 */


/**
 *   Log.e("tag","trans");
 Gson gson = new Gson();
 String json = "[{\"id\":\"1\",\"lng\":\"110.301456\",\"lat\":\"21.151489\"
 ,\"temp\":\"26.5\",\"press\":\"100\",\"uv\":\"64.5\"},{\"id\":\"2\",\"lng\":\
 "110.302566\",\"lat\":\"21.151489\",\"temp\" :\"55.5\",\"press\":\"66.6\",\"uv\"
 :\"77.7\"},{\"id\":\"3\",\"lng\":\"110.301456\",\"lat\":\"21.152489\",\"temp\":\"
 33.3\",\"press\":\"108.8\",\"uv\":\"66.6\"}]";
 atmosphere[] atmosArray = gson.fromJson(json, new TypeToken<atmosphere[]>(){}.getType());
 Log.e("tag",atmosArray[0].toString());
 */

public class atmosphere {

    private String lng;
    private String lat;
    private String temp;
    private String press;
    private String uv;
    private String id;


    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}
