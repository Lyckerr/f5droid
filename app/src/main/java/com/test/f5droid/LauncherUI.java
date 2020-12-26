package com.test.f5droid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mingyuers.permission.PermissionAnywhere;
import com.mingyuers.permission.PermissionCallback;

import java.util.List;

public class LauncherUI extends AppCompatActivity implements View.OnClickListener {

    private final String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launcher_ui);

        Button button_single = findViewById(R.id.button_singlePic);
        Button button_check = findViewById(R.id.button_checkEmbeddedPic);
        button_single.setOnClickListener(this);
        button_check.setOnClickListener(this);

//        if(this.checkSelfPermission(permissions) != PackageManager.PERMISSION_GRANTED)

        try {
            PermissionAnywhere.requestPermission(permissions, new PermissionCallback() {
                @Override
                public void onComplete(List<String> grantedPermissions, List<String> deniedPermissions, List<String> alwaysDeniedPermissions) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_singlePic:
                Intent intentSingle = new Intent();
                intentSingle.setClass(LauncherUI.this, SingleEmbedActivity.class);
                intentSingle.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentSingle);
                break;
            case R.id.button_checkEmbeddedPic:
                Intent intentCheck = new Intent();
                intentCheck.setClass(LauncherUI.this, CheckEmbeddedActivity.class);
                intentCheck.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheck);
                break;
        }
    }
    private long mExitTime;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }
            else finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
