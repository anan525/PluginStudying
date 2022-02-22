package com.pluginrule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

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
}