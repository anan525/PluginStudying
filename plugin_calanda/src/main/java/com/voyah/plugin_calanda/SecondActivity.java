package com.voyah.plugin_calanda;

import android.content.IntentFilter;
import android.os.Bundle;

import com.pluginrule.BaseActivity;

public class SecondActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        registerReceiver(new CalandaReceiver(), new IntentFilter("com.voyah.plugin_calanda.CalandaReceiver"));
    }
}