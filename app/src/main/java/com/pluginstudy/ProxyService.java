package com.pluginstudy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.pluginrule.PluginBean;
import com.pluginrule.PluginManager;
import com.pluginrule.ServiceInterface;

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
public class ProxyService extends Service {

    private PluginBean pluginBean;
    private ServiceInterface targetService;

    @Override
    public void onCreate() {
        super.onCreate();

        createNeededNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String packageName = intent.getStringExtra("packageName");
        if (!TextUtils.isEmpty(packageName)) {
            pluginBean = PluginManager.getInstance().getPluginBeanList(packageName);
            String className = intent.getStringExtra("className");
            if (pluginBean != null) {
                Object o = PluginManager.getInstance().loadPluginService(packageName, className);
                if (o != null && o instanceof ServiceInterface) {
                    targetService = (ServiceInterface) o;
                    targetService.attachToService(this);
                    return targetService.onStartCommand(intent, flags, startId);
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void createNeededNotification() {
        //channelid
        String channelID = "CHANNEL_ID";
        String channelName = "CHANNEL_NAME";
        //Builder
        Notification.Builder builder = null;
        //大于26需要channelID
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelID,
                    channelName, NotificationManager.IMPORTANCE_HIGH);
            //设置Lights
            notificationChannel.enableLights(true);
            //设置LightColor
            notificationChannel.setLightColor(Color.RED);
            //ShowBadge
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //createNotificationChannel
            manager.createNotificationChannel(notificationChannel);
            //初始化Notification
            builder = new Notification.Builder(getApplicationContext(), channelID);
        } else {
            //<26创建Notification
            builder = new Notification.Builder(this)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setLights(Color.RED, 1000, 0)
                    .setSound(null, null);
        }
        //打开前台notify
        startForeground(520, builder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (targetService != null) {
            return targetService.onBind(intent);
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (targetService != null) {
            targetService.onDestroy();
        }
    }
}
