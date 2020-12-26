package com.test.f5droid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyBaseAdapter extends BaseAdapter {

    private List<String> imagePath;
    private int mResource;
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean isState = false;

    public MyBaseAdapter(Context context, List<String> data, int resource) {
        mInflater = LayoutInflater.from(context);
        imagePath = data;
        mResource = resource;
        mContext = context;
    }

    public void setIsState(boolean isState) {
        this.isState = isState;
        notifyDataSetChanged();
    }

    public void setData(List<String> data) {
        imagePath = data;
    }

    @Override
    public int getCount() {
        return imagePath.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePath.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ArrayList<Boolean> list = ((CheckEmbeddedActivity) mContext).getSelectItems();
        convertView = mInflater.inflate(mResource, null);
        CheckBox checkBox = convertView.findViewById(R.id.check_box);
        checkBox.setVisibility(isState ? View.VISIBLE : View.GONE);
        if (list.size() != 0)
            checkBox.setChecked(list.get(position));

//        if (convertView == null) {
//            imageView = new ImageView(CheckEmbeddedActivity.this);//实例化ImageView的对象
//            //设置推向的宽度和高度
//        } else {

        ImageView imageView = convertView.findViewById(R.id.my_image);
        imageView.setAdjustViewBounds(true);
        imageView.setMaxWidth(512);
        imageView.setMaxHeight(512);
        imageView.setPadding(5, 5, 5, 5);//设置ImageView的内边距

        //为ImageView设置要显示的图片
        Bitmap bm = BitmapFactory.decodeFile(imagePath.get(position));
        imageView.setImageBitmap(bm);

        return convertView;
    }
}
