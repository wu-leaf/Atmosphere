package com.each.www.atmosphere.view;



import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
import com.each.www.atmosphere.MyApplication;
import com.each.www.atmosphere.R;
import com.each.www.atmosphere.model.atmosphere;
import com.each.www.atmosphere.model.version;
import com.each.www.atmosphere.util.ToastUtil;
import com.each.www.atmosphere.util.VersionCheck;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AMap.OnMarkerClickListener,
         AMap.OnMarkerDragListener, AMap.OnMapLoadedListener,
        View.OnClickListener ,AMap.OnInfoWindowClickListener, SearchView.OnQueryTextListener,
        LocationSource,AMapLocationListener{

        private DrawerLayout mDrawerLayout;


        private AMap aMap;
        private MapView mapView;

        private OnLocationChangedListener mListener;
        private AMapLocationClient mlocationClient;
        private AMapLocationClientOption mLocationOption;
        private TextView mLocationErrText;
        //Color.argb(180, 3, 145, 255);
        private static final int STROKE_COLOR = Color.argb(180, 255, 0, 0);
        private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

        private SearchView searchView;

        version mVersion;

        private MyApplication app;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                //setContentView(R.layout.custommarker_activity);
                setContentView(R.layout.activity_main);

                Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                final ActionBar ab = getSupportActionBar();
                assert ab != null;
                ab.setHomeAsUpIndicator(R.drawable.ic_menu);
                ab.setDisplayHomeAsUpEnabled(true);
                mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

                //NavigationView是左侧侧滑菜单
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                if (navigationView != null) {
                        setupDrawerContent(navigationView);//设置左侧导航抽屉
                }

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(this);


                mapView = (MapView) findViewById(R.id.map);
                mapView.onCreate(savedInstanceState); // 此方法必须重写

                init();

                checkIfNewVersion();
        }

    private void checkIfNewVersion() {
        int newVersionCode;
        goToCheckNewVersion();
        int now_VersionCode = VersionCheck.getNowVerCode(MainActivity.this);

        Log.e("TAG","now_VersionCode "+ now_VersionCode );
        app = (MyApplication)getApplication();
        newVersionCode = app.getValue();
        Log.e("TAG","new_VersionCode "+ newVersionCode);

        if (VersionCheck.isNewVersion(newVersionCode,now_VersionCode)){
            Log.e("TAG", "可以更新");
            //弹出对话框
            Dialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("软件更新")
                    .setMessage("有新版本")
                            // 设置内容
                    .setPositiveButton("更新",// 设置确定按钮
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    VersionCheck.goUpdate(MainActivity.this);
                                    ToastUtil.show(MainActivity.this, "正在更新");
                                }
                            })
                    .setNegativeButton("暂不更新",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    // 点击"取消"按钮之后退出程序
                                    //finish();
                                }
                            }).create();// 创建
            // 显示对话框
            dialog.show();
        }
    }

    private void goToCheckNewVersion() {
        //检查服务器的app版本号,解析这个:http://each.ac.cn/atmosphere.json，获得versionCode
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://each.ac.cn/atmosphere.json";
                StringRequest request = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e("TAG", s);
                        List<version> versionList = parseJSONWithGsonForVersion(s);
                        for (version ver : versionList){
                            if (ver != null){
                                Log.e("TAG", ver.getVersionCode()+";"+ver.getApkUrl());
                                versionModel(ver);
                            }
                        }
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
    private void versionModel(version ver) {
        mVersion = ver;
        Log.e("TAG", mVersion.getVersionCode()+"  versionModel");
        int newVersionCode = mVersion.getVersionCode();
        Log.e("TAG", "newVersionCode  " + newVersionCode);
        app = (MyApplication)getApplication();
        app.setValue(newVersionCode);
    }
    private  List<version> parseJSONWithGsonForVersion(String jsonData) {
        Gson gson = new Gson();
        List<version> versList = gson.fromJson(jsonData,
                new TypeToken<List<version>>() {}.getType());
        return versList;
    }
    private void setupDrawerContent(NavigationView navigationView) {
                navigationView.setNavigationItemSelectedListener(
                        new NavigationView.OnNavigationItemSelectedListener() {
                                @Override
                                public boolean onNavigationItemSelected(MenuItem menuItem) {
                                        switch (menuItem.getItemId()){
                                                case R.id.nav_home:
                                                        mDrawerLayout.closeDrawers();
                                                        return true;
                                            case R.id.action_settings:
                                                        Intent intent_setting = new Intent(MainActivity.this,SettingActivity.class);
                                                        startActivity(intent_setting);
                                                        return true;
                                                case R.id.action_about:
                                                        Intent intent_about = new Intent(MainActivity.this,AboutActivity.class);
                                                        startActivity(intent_about);
                                                        return true;
                                                case R.id.action_guide:
                                                        Intent intent_guide = new Intent(MainActivity.this,GuideActivity.class);
                                                        startActivity(intent_guide);
                                                        return true;
                                        }
                                        menuItem.setChecked(true);
                                        mDrawerLayout.closeDrawers();
                                        return true;
                                }
                        });
        }

        /**
         * 初始化AMap对象
         */
        private void init() {
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
                aMap.setOnInfoWindowClickListener(this);//设置infowindows时间监听器

                aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                initMarkersInfoFormNet();//从网络获得  气象站 对象
                //addMarkersToMap();// 往地图上添加marker
        }

        private void initMarkersInfoFormNet() {
                //String url = "http://11101001.com/service.php?id=-1";
                new Thread(new Runnable() {
                        @Override
                        public void run() {
                                String url = "http://112.74.187.80/dataService.php?devId=-1";
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
                // 自定义精度范围的圆形边框颜色  STROKE_COLOR
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
                        .position(latLng).title(" 气象站: "+atmos.getDevId())
                        .snippet(
                                        "实时温度: " + atmos.getT() +"℃" + "\n" +
                                        "实时湿度: " + atmos.getH() +"%"+ "\n" +
                                        "大气压强: " + atmos.getP() +"p"+ "\n" +
                                        " P M 2.5 : " + atmos.getPm() + "μg/m3"+"\n" +
                                        "光照强度: " + atmos.getCd() + "LX"+"\n" +
                                        "风速级别: " + atmos.getWr() + "级"+"\n" +
                                        "雨势级别: " + atmos.getWater() + "级"+"\n" +
                                        " 紫外线  : " + atmos.getUv() +"uw/cm2"+ "\n" +
                                        "当前风向: " + atmos.getWd() + "\n" +
                                        "空气质量指数: " + atmos.getDust() + "\n" +
                                        atmos.getCreate_at()).draggable(true)).setObject(atmos.getDevId());
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
         * 监听点击infowindow窗口事件回调
         * @param marker
         */
        @Override
        public void onInfoWindowClick(Marker marker) {
            /**
             * 1.获得气象站 站名
             * 2.获得气象站 所有图表  URL
             */
                String devId = (String)marker.getObject();
                Intent intent = new Intent(this,DetailsActivity.class);
                intent.putExtra("devId",devId);
                startActivity(intent);
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
            ToastMarkers();
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
        public void ToastMarkers(){
            if (aMap != null) {
                List<Marker> markers = aMap.getMapScreenMarkers();
                if (markers == null || markers.size() == 0) {
                    ToastUtil.show(this, "当前可视范围内没有气象站");
                    return;
                }
                String tile = "屏幕内有：";
                for (Marker marker : markers) {
                    tile = tile + " " + marker.getTitle();

                }
                ToastUtil.show(this, tile);
            }
        }
        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                        //刷新所有marker
                        case R.id.fab:
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
                               Log.e("TAG",atmos.toString());
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
               // ToastMarkers();
                if (mListener != null && amapLocation != null) {
                        if (amapLocation != null
                                && amapLocation.getErrorCode() == 0) {
                                mLocationErrText.setVisibility(View.GONE);
                                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                              //  aMap.moveCamera(CameraUpdateFactory.zoomTo(18));


                        } else {
                                String errText = "定位失败," + amapLocation.getErrorCode()+
                                        ": " + amapLocation.getErrorInfo();
                                Log.e("AmapErr", errText);
                               // mLocationErrText.setVisibility(View.VISIBLE);
                               // mLocationErrText.setText(errText);
                                DrawerLayout view = (DrawerLayout)findViewById(R.id.drawer_layout);
                                Snackbar.make(view,errText,Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                        }
                }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
               getMenuInflater().inflate(R.menu.menu_main,menu);

                SearchManager searchManager =
                        (SearchManager)getSystemService(Context.SEARCH_SERVICE);
                searchView =
                        (SearchView)menu.findItem(R.id.action_search).getActionView();
                searchView.setSearchableInfo(searchManager
                        .getSearchableInfo(getComponentName()));
                searchView.setOnQueryTextListener(this);
                return true;
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                       /* case R.id.:
                                Intent intent_about = new Intent(MainActivity.this,SearchActivity.class);
                                startActivity(intent_about);
                                return true;*/

                }
                return super.onOptionsItemSelected(item);
        }


    //监听  搜索框
    @Override
    public boolean onQueryTextSubmit(String query) {
        ToastUtil.show(MainActivity.this,"你想搜索"+query+"吗？");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e("TAG",newText);
        return false;
    }
}
