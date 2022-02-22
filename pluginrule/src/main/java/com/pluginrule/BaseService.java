package com.pluginrule;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
public class BaseService extends Service implements ServiceInterface {

    public Service service;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return service.onBind(intent);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return service.onUnbind(intent);
    }

    @Override
    public void attachToService(Service service) {
        this.service = service;
    }
}
