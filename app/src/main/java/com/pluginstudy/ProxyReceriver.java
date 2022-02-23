package com.pluginstudy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.pluginrule.PluginBean;
import com.pluginrule.PluginManager;
import com.pluginrule.ReceiverInterfaces;
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
public class ProxyReceriver extends BroadcastReceiver {

    private ReceiverInterfaces broadcastReceiver;

    public void loadclassName(String className) {
        String packageNameFromClassName = PluginManager.getInstance().getPackageNameFromClassName(className);
        Object o = PluginManager.getInstance().loadPluginClass(packageNameFromClassName, className);
        if (o != null && o instanceof ReceiverInterfaces) {
            broadcastReceiver = (ReceiverInterfaces) o;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (broadcastReceiver != null) {
            broadcastReceiver.onReceive(context, intent);
        }
    }
}
