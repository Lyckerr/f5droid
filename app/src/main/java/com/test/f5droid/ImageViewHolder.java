package com.test.f5droid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ImageViewHolder extends RecyclerView.ViewHolder {

    private TextView mTextTitle;

    public static ImageViewHolder inflate(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    private ImageViewHolder(View view) {
        super(view);
        mTextTitle = view.findViewById(R.id.title);
    }

    private void setTitle(String title) {
        mTextTitle.setText(title);
    }
}
