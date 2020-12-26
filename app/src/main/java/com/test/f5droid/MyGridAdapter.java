package com.test.f5droid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MyGridAdapter extends SimpleAdapter {
    private List<? extends Map<String, ?>> data;
    private int resource;
    private Context context;
    private LayoutInflater inflater;
    private boolean isState = false;


    public MyGridAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        inflater = LayoutInflater.from(context);
        this.resource = resource;
        this.data = data;
        this.context = context;
    }

    public void setData(List<? extends Map<String, ?>> data) {
        this.data = data;
    }

    public void setIsState(boolean isState) {
        this.isState = isState;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArrayList<Boolean> list = ((CheckEmbeddedActivity) context).getSelectItems();
        convertView = inflater.inflate(resource, null);
        CheckBox checkBox = convertView.findViewById(R.id.check_box);
        checkBox.setVisibility(isState ? View.VISIBLE : View.GONE);
        if (list.size() != 0)
            checkBox.setChecked(list.get(position));


        return super.getView(position, convertView, parent);
    }
}
