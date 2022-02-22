package com.voyah.plugin_calanda;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.pluginrule.ActivityInterfaces;
import com.pluginrule.BaseActivity;

public class TopActivity extends BaseActivity {

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_top);

        findViewById(R.id.tv_top).setOnClickListener(l -> {
            Intent intent = new Intent(app, SecondActivity.class);
            startActivity(intent);
        });

    }
}