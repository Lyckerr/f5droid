package com.test.f5droid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wildma.pictureselector.FileUtils;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import java.io.ByteArrayOutputStream;
import java.io.File;

import info.guardianproject.f5android.plugins.PluginNotificationListener;
import info.guardianproject.f5android.plugins.f5.Embed;
import info.guardianproject.f5android.plugins.f5.Extract;

public class SingleExtractActivity extends AppCompatActivity implements
        PluginNotificationListener, Extract.ExtractionListener, Runnable {

    private static final String TAG = "SingleExtractActivity";

    private int FLAG_IS_PHOTO_SET = 0;

    private EditText mEditPassword;
    private TextView mTextMessage;
    private String mMessage;
    private String mPassword;
    private File mOutFile;
    private Extract extract;
    private Button mButtonSelect;
    private String mSelectedImagePath;
    private ImageView mImageViewSelectPicture;
    private MyDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(R.string.title_single_extract);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.activity_single_extract);
        mEditPassword = findViewById(R.id.edittext_extract_password);
        mTextMessage = findViewById(R.id.textview_extract_message);

        mButtonSelect = findViewById(R.id.button_extract_select);
        mButtonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector
                        .create(SingleExtractActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture(false);
            }
        });
        mImageViewSelectPicture = findViewById(R.id.imageview_extract_select);

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
                    Toast.makeText(this, "请选择载密图片！", Toast.LENGTH_SHORT).show();
                    break;
                }
                if ( mEditPassword.getText().toString().isEmpty() )
                {
                    Toast.makeText(this, "请输入加密短语！", Toast.LENGTH_SHORT).show();
                    break;
                }
//                mMessage = mEditMessage.getText().toString().trim();
                mPassword = mEditPassword.getText().toString();

                dialog = new MyDialog(this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                new Thread(this).start();
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
            Log.d("SingleExtractActivity", with_message);
        else
            Log.d("SingleExtractActivity", "onUpdate: Nothing to show");
    }
    @Override
    public void onFailure() {
        Log.d(TAG, "onExtractionResult: 秘密信息提取失败");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextMessage.setText("提取失败，请检查：\n1.载密图片是否选取正确；\n2.加密短语是否输入正确。");
                dialog.dismiss();
            }
        });
    }


//    @Override
//    public void onEmbedded(File outFile) {
//        mOutFile = outFile;
//        Log.d(TAG, "onEmbedded: 图片已保存。");
//        Log.d(TAG, "onEmbedded: 存储位置为：" + mOutFile.getAbsolutePath());
//
//        runOnUiThread(new Runnable() {
//            public void run() {
//                Toast.makeText(SingleExtractActivity.this, "载密图片已生成！\n保存在 " + mOutFile.getAbsolutePath(), Toast.LENGTH_LONG).
//                        show();
//
//            }
//        });
//    }


    @Override
    public void onExtractionResult(ByteArrayOutputStream baos) {
        final String outMessage = baos.toString();

            Log.d(TAG, "onExtractionResult: 秘密信息提取完成。");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextMessage.setText(outMessage);
                    dialog.dismiss();
                }
            });
    }

    @Override
    public void run() {
        Extract extract = new Extract(SingleExtractActivity.this,
                new File(mSelectedImagePath),
                mPassword.getBytes()
        );
        extract.run();
    }

    @Override
    protected void onDestroy() {
        FileUtils.deleteAllCacheImage(this);
        super.onDestroy();
    }
}
