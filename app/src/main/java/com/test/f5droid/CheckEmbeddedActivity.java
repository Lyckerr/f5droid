package com.test.f5droid;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

public class CheckEmbeddedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embedded_gallery);

        initView();
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(R.string.title_embedded);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new GridLayoutManager(this, 3));
        ImageAdapter imageAdapter = new ImageAdapter(new ImageAdapter.Listener() {
            @Override
            public void onImageClicked(View view) {
                transition(view);
            }

            @Override
            public void onImageLongClicked(View view) {

            }
        });
        list.setAdapter(imageAdapter);
    }


    private void transition(View view) {
        if (Build.VERSION.SDK_INT < 21) {
            Toast.makeText(CheckEmbeddedActivity.this, "21+ only, keep out", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(CheckEmbeddedActivity.this, ActivityTransitionToActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(CheckEmbeddedActivity.this, view, getString(R.string.app_name));
            startActivity(intent, options.toBundle());
        }
    }
}
