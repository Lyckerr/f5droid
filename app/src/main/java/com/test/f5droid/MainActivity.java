package com.test.f5droid;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

import f5android.plugins.PluginNotificationListener;
import f5android.plugins.f5.*;

public class MainActivity extends AppCompatActivity implements PluginNotificationListener, Embed.EmbedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String picPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/1.jpg";
        final String password = "123123123";


        final EditText editText = findViewById(R.id.edittext_message);
        final String message;

        Button button_embed = findViewById(R.id.button_embed);
        button_embed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Embed embed = new Embed(MainActivity.this,
                        picPath,
                        editText.getText().toString().trim(),
                        password.getBytes());
                embed.start();
            }
        });

    }

    @Override
    public void onUpdate(String with_message) {
        if (with_message!=null)
            Log.d("MainActivity", with_message);
        else
            Log.d("MainActivity", "onUpdate: Nothing to show");
    }
    @Override
    public void onFailure() {}


    @Override
    public void onEmbedded(File outFile) {

    }
}
