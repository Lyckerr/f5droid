package com.test.f5droid;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyDialog extends Dialog {
    private TextView tv;

    MyDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);

        tv = findViewById(R.id.tv);
        tv.setText("正在提取.....");
        LinearLayout linearLayout = findViewById(R.id.LinearLayout_dialog);
        linearLayout.getBackground().setAlpha(210);
    }

    public TextView getTv() {
        return tv;
    }
}
