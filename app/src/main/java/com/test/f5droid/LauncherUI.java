package com.test.f5droid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LauncherUI extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launcher_ui);

        Button button_single = findViewById(R.id.button_singlePic);
        Button button_check = findViewById(R.id.button_checkEmbeddedPic);
        button_single.setOnClickListener(this);
        button_check.setOnClickListener(this);


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
}
