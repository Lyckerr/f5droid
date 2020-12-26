package com.test.f5droid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckEmbeddedActivity
        extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    private ActionBar actionBar;
    private Button btnCancel, btnDel, btnSelectAll, btnClear;
    private RelativeLayout headerLayout;
    private TextView numText;
    private GridView gridView;
    private ArrayList<String> items; //每一个item
    private ArrayList<Boolean> selectItems; //用于存储已选中项目的位置
    private MyBaseAdapter adapter;
    private boolean isState;

    private String F5AndroidPath = Environment.getExternalStorageDirectory() + "/F5Android";
    private List<String> imagePath=new ArrayList<String>();
    private static String[] imageFormatSet=new String[]{"jpg", "png", "gif", "bmp"};
    /**
     * @方法:判断是否为图片文件
     * @param path 图片路径
     * @return boolean 是否是图片文件，是true，否false
     * */
    private static boolean isImageFile(String path){
        for(String format:imageFormatSet){//遍历数组
            if(path.contains(format)){//判断是否为合法的图片文件
                return true;
            }
        }
        return false;
    }
    /**
     * 方法:用于遍历指定路径
     * 参数:String url遍历路径
     * 无返回值
     * */
    private void getFiles(String url){
        File files=new File(url);
        File[] file=files.listFiles();
        try {
            for(File f:file){//通过for循环遍历获取到的文件数组
                if(f.isDirectory()){//如果是目录
                    getFiles(f.getAbsolutePath());//递归调用
                }else{
                    if(isImageFile(f.getPath())){//如果是图片文件
                        imagePath.add(f.getPath());//将文件的路径添加到List集合中
//                        Log.d("getFiles: ", f.getPath());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();//输出异常信息
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_embedded);

        initView();

    }

    private void initView() {
        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(R.string.title_embedded);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        numText = findViewById(R.id.number_check_embedded);
        btnCancel = findViewById(R.id.btn_cancel_select);
        btnDel = findViewById(R.id.btn_del);
        btnSelectAll = findViewById(R.id.btn_select_all);
        btnClear = findViewById(R.id.btn_clear);
        btnCancel.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnSelectAll.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        headerLayout = findViewById(R.id.header_check_embedded);
        gridView = findViewById(R.id.gridView);

        items = new ArrayList<>();
        selectItems = new ArrayList<>();

        //加载目录内的图片
        getFiles(F5AndroidPath);
        items.addAll(imagePath);
        String[] imageArr = imagePath.toArray(new String[100]);

        adapter = new MyBaseAdapter(this, imagePath, R.layout.item_image);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);


    }

    public ArrayList<Boolean> getSelectItems() {
        return selectItems;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            this.finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel_select :
                selectItems.clear();
                numText.setText("已选择1项");
                adapter.setIsState(false);
                setState(false);
                break;
            case R.id.btn_clear :
                setSelectAll(false);
                break;
            case R.id.btn_select_all:
                setSelectAll(true);
                break;
            case R.id.btn_del:
                delSelections();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isState) {
            CheckBox checkBox = view.findViewById(R.id.check_box);
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                selectItems.set(position, false);
            } else {
                checkBox.setChecked(true);
                selectItems.set(position, true);
            }
            adapter.notifyDataSetChanged();
            setSelectNum();
        }
        else {
            transition(view, position);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isState) {
            selectItems = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                selectItems.add(false);
            }
            CheckBox box = view.findViewById(R.id.check_box);
            box.setChecked(true);
            selectItems.set(position, true);
            setState(true);
            adapter.setIsState(true);
            setSelectNum();
        }
        return false;
    }

    //设置当前状态 是否在多选模式
    private void setState(boolean b) {
        isState = b;
        if (b) {
            headerLayout.setVisibility(View.VISIBLE);
            btnDel.setVisibility(View.VISIBLE);
            actionBar.hide();
        } else {
            headerLayout.setVisibility(View.GONE);
            btnDel.setVisibility(View.GONE);
            actionBar.show();
        }
    }

    //显示已选项数目
    private void setSelectNum() {
        int num = 0;
        for (Boolean b : selectItems) {
            if (b)
                num ++;
        }
        numText.setText(String.format("已选择%d项", num));
    }

    //全选
    private void setSelectAll(boolean b) {
        for (int i = 0; i < selectItems.size(); i++) {
            selectItems.set(i, b);
            adapter.notifyDataSetChanged();
            setSelectNum();
        }
        btnSelectAll.setVisibility(b? View.GONE : View.VISIBLE);
        btnClear.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    //删除
    private void delSelections() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (!selectItems.contains(true)) {
            builder.setTitle("提示").setMessage("当前未选中项目").setPositiveButton("确认", null).create().show();
        } else {
            builder.setTitle("提示");
            builder.setMessage("确认删除所选项目？");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < items.size(); i++) {
                        if (selectItems.get(i)) {

                            String delPath = items.get(i);
                            File delFile = new File(delPath);
                            final boolean delete = delFile.delete();

                            items.set(i, null);
                        }
                    }
                    while (items.contains(null)) {
                        items.remove(null);

                    }
                    selectItems = new ArrayList<>();
                    for (int i = 0; i < items.size(); i++) {
                        selectItems.add(false);
                    }

                    adapter.setData(items);
                    adapter.notifyDataSetChanged();
                    if (items.size() == 0) {
                        adapter.setIsState(false);
                        setState(false);
                    }
                }
            });
            builder.setNegativeButton("取消", null);
            builder.create().show();
        }

    }

//
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isState) {
                selectItems.clear();
                numText.setText("已选择1项");
                adapter.setIsState(false);
                setState(false);
                return true;
//            } else {
//                if ((System.currentTimeMillis() - mExitTime) > 2000) {
//                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                    mExitTime = System.currentTimeMillis();
//                }
//                else finish();
//                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void transition(View view, int position) {
        if (Build.VERSION.SDK_INT < 21) {
            Toast.makeText(CheckEmbeddedActivity.this, "21+ only, keep out", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(CheckEmbeddedActivity.this, ActivityTransitionToActivity.class);
            Uri imageUri = Uri.fromFile(new File(imagePath.get(position)));
            intent.setData(imageUri);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(CheckEmbeddedActivity.this, view, getString(R.string.app_name));
            startActivity(intent, options.toBundle());
        }
    }
}
