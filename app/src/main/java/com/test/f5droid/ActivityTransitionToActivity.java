package com.test.f5droid;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;


/**
 * Activity that gets transitioned to
 */
public class ActivityTransitionToActivity extends AppCompatActivity {

    private PhotoView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_to);
        ActionBar actionBar = getSupportActionBar();

        Uri imageUri = getIntent().getData();

        imageView = findViewById(R.id.iv_photo);
        imageView.setImageURI(imageUri);

        String picPath = imageUri.getPath();
        String[] temp = picPath.split("\\/");
        String title = temp[temp.length-2].concat("/").concat(temp[temp.length-1]);
        actionBar.setTitle(title);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
