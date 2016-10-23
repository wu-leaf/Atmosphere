package com.each.www.atmosphere;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.each.www.atmosphere.model.atmosphere;
import com.each.www.atmosphere.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;


public class MainActivity extends Activity implements AMap.OnMarkerClickListener,
         AMap.OnMarkerDragListener, AMap.OnMapLoadedListener,
        View.OnClickListener ,LocationSource,AMapLocationListener{

        private AMap aMap;
        private MapView mapView;

        private Button trans;  //刷新marker

        private OnLocationChangedListener mListener;
        private AMapLocationClient mlocationClient;
        private AMapLocationClientOption mLocationOption;
        private TextView mLocationErrText;
        private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
        private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.custommarker_activity);
                mapView = (MapView) findViewById(R.id.map);
                mapView.onCreate(savedInstanceState); // 此方法必须重写
                init();
        }
        /**
         * 初始化AMap对象
         */
        private void init() {
                trans = (Button)findViewById(R.id.test_trans);
                trans.setOnClickListener(this);
                if (aMap == null) {
                        aMap = mapView.getMap();
                        setUpMap();
                }
                mLocationErrText = (TextView)findViewById(R.id.location_errInfo_text);
                mLocationErrText.setVisibility(View.GONE);
        }

        private void setUpMap() {
                aMap.setLocationSource(this);//设置定位监听
                aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮
                aMap.setMyLocationEnabled(true);//显示定位层可触发
                setupLocationStyle();

                aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
                aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
                aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
                initMarkersInfoFormNet();//从网络获得  气象站 对象
                //addMarkersToMap();// 往地图上添加marker
        }

        private void initMarkersInfoFormNet() {
                //String url = "http://11101001.com/service.php?id=-1";
                new Thread(new Runnable() {
                        @Override
                        public void run() {
                                String url = "http://each.ac.cn/json.json";
                                StringRequest request = new StringRequest(Request.Method.GET,
                                        url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String s) {
                                                Log.e("TAG",s);
                                                parseJSONWithGson(s);
                                        }
                                }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                                Log.e("TAG",error.toString());
                                        }
                                });
                                request.setTag("testGet");
                                MyApplication.getHttpQueues().add(request);
                        }
                }).start();
        }

        private void setupLocationStyle(){
                // 自定义系统定位蓝点
                MyLocationStyle myLocationStyle = new MyLocationStyle();
                // 自定义定位蓝点图标
                myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                        fromResource(R.drawable.gps_point));
                // 自定义精度范围的圆形边框颜色
                myLocationStyle.strokeColor(STROKE_COLOR);
                //自定义精度范围的圆形边框宽度
                myLocationStyle.strokeWidth(5);
                // 设置圆形的填充颜色
                myLocationStyle.radiusFillColor(FILL_COLOR);
                // 将自定义的 myLocationStyle 对象添加到地图上
                aMap.setMyLocationStyle(myLocationStyle);
        }
        /**
         * 方法必须重写
         */
        @Override
        protected void onResume() {
                super.onResume();
                mapView.onResume();
        }
        /**
         * 方法必须重写
         */
        @Override
        protected void onPause() {
                super.onPause();
                mapView.onPause();
        }
        /**
         * 方法必须重写
         */
        @Override
        protected void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
                mapView.onSaveInstanceState(outState);
        }
        /**
         * 方法必须重写
         */
        @Override
        protected void onDestroy() {
                super.onDestroy();
                mapView.onDestroy();
                if (null != mlocationClient){
                        mlocationClient.onDestroy();
                }
        }
        /**
         * 在地图上添加marker
         */
        private void addMarkersToMap(atmosphere atmos) {
                 final LatLng latLng = new LatLng(Double
                         .parseDouble(atmos.getLat()), Double.parseDouble(atmos.getLng()));

                aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(latLng).title(atmos.getId())
                        .snippet(atmos.getPress() + "\n" +
                                atmos.getTemp() + "\n" +
                                atmos.getUv()).draggable(true));
               /* // 动画效果
                ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
                giflist.add(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                giflist.add(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED));
                giflist.add(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(Constants.xxx).title("xxx").icons(giflist)
                        .draggable(true).period(10));*/
               // drawMarkers();// 添加10个带有系统默认icon的marker
        }
        /**
         * 对marker标注点点击响应事件
         */
        @Override
        public boolean onMarkerClick(final Marker marker) {
                ToastUtil.show(MainActivity.this,marker.getTitle()+"");
                return false;
        }
        /**
         * 监听拖动marker时事件回调
         */
        @Override
        public void onMarkerDrag(Marker marker) {
                String curDes = marker.getTitle() + "拖动时当前位置:(lat,lng)\n("
                        + marker.getPosition().latitude + ","
                        + marker.getPosition().longitude + ")";
                ToastUtil.show(MainActivity.this,curDes);
        }
        /**
         * 监听拖动marker结束事件回调
         */
        @Override
        public void onMarkerDragEnd(Marker marker) {
        }
        /**
         * 监听开始拖动marker事件回调
         */
        @Override
        public void onMarkerDragStart(Marker marker) {
        }
        /**
         * 监听amap地图加载成功事件回调
         */
        @Override
        public void onMapLoaded() {
               /* // 设置所有maker显示在当前可视区域地图中
                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(Constants.XIAN).include(Constants.CHENGDU)
                        .include(latlng).include(Constants.ZHENGZHOU).include(Constants.BEIJING).build();
                aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));*/
        }
        /**
         * 监听自定义infowindow窗口的infocontents事件回调
         */
      /*  @Override
        public View getInfoContents(Marker marker) {
                if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_contents) {
                        return null;
                }
                View infoContent = getLayoutInflater().inflate(
                        R.layout.custom_info_contents, null);
                //render(marker, infoContent);
                return infoContent;
        }*/
        /**
         * 监听自定义infowindow窗口的infowindow事件回调
         */
        // @Override
       /* public View getInfoWindow(Marker marker) {
                if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_window) {
                        return null;
                }
                View infoWindow = getLayoutInflater().inflate(
                        R.layout.custom_info_window, null);
               // render(marker, infoWindow);
                return infoWindow;
        }*/
        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                        //刷新所以marker
                        case R.id.test_trans:
                                aMap.clear();
                                initMarkersInfoFormNet();
                                break;
                         default:
                                 break;
                }
        }
        private void parseJSONWithGson(String jsonData) {
                Gson gson = new Gson();
                List<atmosphere> atmosList = gson.fromJson(jsonData,
                        new TypeToken<List<atmosphere>>() {}.getType());
                   for (atmosphere atmos : atmosList){
                           if (atmos != null){
                                 //  Log.e("gson",atmos.toString());
                                   Log.e("gson",atmos.getId()+"");
                                   Log.e("gson",atmos.getLat()+"");
                                   Log.e("gson",atmos.getLng()+"");
                                   Log.e("gson",atmos.getPress()+"");
                                   Log.e("gson",atmos.getTemp()+"");
                                   Log.e("gson",atmos.getUv()+"");
                                   addMarkersToMap(atmos);
                           }
                   }
        }
        @Override
        public void activate(OnLocationChangedListener listener) {
                mListener = listener;
                if (mlocationClient == null) {
                        mlocationClient = new AMapLocationClient(this);
                        mLocationOption = new AMapLocationClientOption();
                        //设置定位监听
                        mlocationClient.setLocationListener(this);
                        //设置为高精度定位模式
                        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                        //设置定位参数
                        mlocationClient.setLocationOption(mLocationOption);
                        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                        // 在定位结束后，在合适的生命周期调用onDestroy()方法
                        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                        mlocationClient.startLocation();
                }
        }
        @Override
        public void deactivate() {
                mListener = null;
                if (mlocationClient != null) {
                        mlocationClient.stopLocation();
                        mlocationClient.onDestroy();
                }
                mlocationClient = null;
        }
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
                if (mListener != null && amapLocation != null) {
                        if (amapLocation != null
                                && amapLocation.getErrorCode() == 0) {
                                mLocationErrText.setVisibility(View.GONE);
                                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                                aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                        } else {
                                String errText = "定位失败," + amapLocation.getErrorCode()+
                                        ": " + amapLocation.getErrorInfo();
                                Log.e("AmapErr", errText);
                                mLocationErrText.setVisibility(View.VISIBLE);
                                mLocationErrText.setText(errText);
                        }
                }
        }
}
