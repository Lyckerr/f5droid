package com.test.f5droid;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import java.io.File;

import info.guardianproject.f5android.plugins.PluginNotificationListener;
import info.guardianproject.f5android.plugins.f5.*;

public class SingleEmbedActivity extends AppCompatActivity implements
        PluginNotificationListener, Embed.EmbedListener {

    private static final String TAG = "SingleEmbedActivity";

    private int FLAG_IS_PHOTO_SET = 0;
    private int FLAG_IS_MESSAGE_SET = 0;
    private int FLAG_IS_PASSWORD_SET = 0;

    private String mSelectedImagePath;
    private String mPassword;
    private String mMessage;
    private EditText mEditMessage;
    private Embed embed;
    private Button mButtonSelect;
    private ImageView mImageViewSelectPicture;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(R.string.title_single);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.activity_single_embed);

//        mSelectedImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/1.jpg";
        mPassword = "123123123";


        mEditMessage = findViewById(R.id.edittext_message);
        mButtonSelect = findViewById(R.id.button_select);
        mButtonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector
                        .create(SingleEmbedActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture(false);
            }
        });
        mImageViewSelectPicture = findViewById(R.id.imageview_select);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.button_single_confirm:
                if ( FLAG_IS_PHOTO_SET != 1 )
                {
                    Toast.makeText(this, "请选择载体图片！", Toast.LENGTH_SHORT).show();
                    break;
                }
                if ( mEditMessage.getText().toString().isEmpty() )
                {
                    Toast.makeText(this, "请输入嵌入信息！", Toast.LENGTH_SHORT).show();
                    break;
                }
                if ( FLAG_IS_PASSWORD_SET != 1 )
                {
                    Toast.makeText(this, "请设置加密短语！", Toast.LENGTH_SHORT).show();
                    break;
                }

                embed = new Embed(SingleEmbedActivity.this,
                        mSelectedImagePath,
                        mEditMessage.getText().toString().trim(),
                        mPassword.getBytes());
                embed.start();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                FLAG_IS_PHOTO_SET = 1;
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                if (pictureBean.isCut()) {
                    mImageViewSelectPicture.setImageBitmap(BitmapFactory.decodeFile(pictureBean.getPath()));
                } else {
                    mImageViewSelectPicture.setImageURI(pictureBean.getUri());
                }
                mSelectedImagePath = pictureBean.getPath();
                if( mSelectedImagePath != null )
                {
                    mButtonSelect.setText(R.string.button_reselect);
                }

                //使用 Glide 加载图片
                /*Glide.with(this)
                        .load(pictureBean.isCut() ? pictureBean.getPath() : pictureBean.getUri())
                        .apply(RequestOptions.centerCropTransform()).into(mIvImage);*/
            }
        }
    }

    @Override
    public void onUpdate(String with_message) {
        if (with_message!=null)
            Log.d("SingleEmbedActivity", with_message);
        else
            Log.d("SingleEmbedActivity", "onUpdate: Nothing to show");
    }
    @Override
    public void onFailure() {}


    @Override
    public void onEmbedded(File outFile) {
    }

    @Override
    public void onPause() {

        super.onPause();
    }
}
