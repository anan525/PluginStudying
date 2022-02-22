package com.voyah.plugin_calanda;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import com.pluginrule.ActivityInterfaces;
import com.pluginrule.BaseActivity;

public class TopActivity extends BaseActivity {

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_top);

        Toast.makeText(app, "測試", Toast.LENGTH_SHORT).show();
    }
}