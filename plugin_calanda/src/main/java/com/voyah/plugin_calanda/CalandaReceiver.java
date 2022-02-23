package com.voyah.plugin_calanda;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pluginrule.ReceiverInterfaces;

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
public class CalandaReceiver extends BroadcastReceiver implements ReceiverInterfaces {
    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getStringExtra("data");

        Log.e("测试", "" + data);
    }
}
