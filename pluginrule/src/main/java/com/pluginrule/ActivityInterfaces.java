package com.pluginrule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
 * 2022/2/21.
 * --
 * NAME.
 * anyq.
 * --
 */
public interface ActivityInterfaces {

    public void onCreate(@Nullable Bundle savedInstanceState);

    public void onResume();

    public void onPause();

    public void onDestroy();

    void onSaveInstanceState(@NonNull Bundle outState);

    void attachToActivity(Activity app);

}
