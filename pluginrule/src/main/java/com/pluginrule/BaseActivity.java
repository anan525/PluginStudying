package com.pluginrule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Copyright (c) 2021-.
 * All Rights Reserved by Software.
 * --
 * You may not use, copy, distribute, modify, transmit in any form this file.
 * except in compliance with szLanyou in writing by applicable law.
 * --
 * brief   brief function description.
 * 主要功能.
 * --
 * date last_modified_date.
 * 时间.
 * --
 * version 1.0.
 * 版本信息。
 * --
 * details detailed function description
 * 功能描述。
 * --
 * DESCRIPTION.
 * Create it.
 * --
 * Edit History.
 * DATE.
 * 2022/2/22.
 * --
 * NAME.
 * anyq.
 * --
 */
public class BaseActivity extends Activity implements ActivityInterfaces {

    public Activity app;

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onPause() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void attachToActivity(Activity app) {
        this.app = app;
    }

    @Override
    public void setContentView(int layoutResID) {
        app.setContentView(layoutResID);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return app.findViewById(id);
    }

    @Override
    public void startActivity(Intent intent) {
        app.startActivity(intent);
    }

    @Override
    public ComponentName startService(Intent service) {
        return app.startService(service);
    }

    @Override
    public boolean stopService(Intent name) {
        return app.stopService(name);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return app.registerReceiver(receiver, filter);
    }
}
