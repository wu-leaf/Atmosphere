package com.each.www.atmosphere.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.each.www.atmosphere.MyApplication;
import com.each.www.atmosphere.R;
import com.each.www.atmosphere.model.version;
import com.each.www.atmosphere.services.DownloadService;
import com.each.www.atmosphere.util.Constants;
import com.each.www.atmosphere.util.ToastUtil;
import com.each.www.atmosphere.util.VersionCheck;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SettingActivity extends AppCompatActivity {
    version mVersion;
    int newVersionCode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Check(View view){
        /*
        * 1.检查是否有新版本
        * 2.弹出对话框提示是否有新版本
        * 3.点击确认执行下面
        */
            ToastUtil.show(SettingActivity.this, "检查更新");
            goToCheckNewVersion();
            Log.e("TAG","Check");
            int now_VersionCode = VersionCheck.getNowVerCode(SettingActivity.this);
            Log.e("TAG", now_VersionCode + ";"+newVersionCode);
            if (VersionCheck.isNewVersion(newVersionCode,now_VersionCode)){
                Log.e("TAG", "可以更新");
                 ToastUtil.show(SettingActivity.this,"正在更新");
                 Intent intent = new Intent(SettingActivity.this, DownloadService.class);
                 intent.putExtra("apkUrl", "http://each.ac.cn/new_apk/app-release.apk");
                 startService(intent);
            }else{
                ToastUtil.show(SettingActivity.this,"已是最新版本");
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
                        List<version> versionList = parseJSONWithGson(s);
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
        Log.e("TAG", mVersion.getVersionCode()+"");
        newVersionCode = mVersion.getVersionCode();
    }

    private  List<version> parseJSONWithGson(String jsonData) {
        Gson gson = new Gson();
        List<version> versList = gson.fromJson(jsonData,
                new TypeToken<List<version>>() {}.getType());
        return versList;
    }


    public void Feedback(View view){
        ToastUtil.show(SettingActivity.this,"反馈");
    }
}
