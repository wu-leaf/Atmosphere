package com.each.www.atmosphere.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.each.www.atmosphere.R;
import com.each.www.atmosphere.services.DownloadService;
import com.each.www.atmosphere.util.ToastUtil;

public class SettingActivity extends AppCompatActivity {

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
        /*1.检查是否有新版本
        * 2.弹出对话框提示是否有新版本
        * 3.
        */
        ToastUtil.show(SettingActivity.this, "检查更新");
        Intent intent = new Intent(SettingActivity.this, DownloadService.class);
        intent.putExtra("apkUrl", "http://each.ac.cn/apk/app-release.apk");
        startService(intent);

    }
    public void Feedback(View view){
        ToastUtil.show(SettingActivity.this,"反馈");
    }
}
