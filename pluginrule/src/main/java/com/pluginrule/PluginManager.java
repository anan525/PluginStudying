package com.pluginrule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

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
public class PluginManager {

    private HashMap<String, PluginBean> pluginBeanList = new HashMap<>();


    private volatile static PluginManager pluginManager;

    public static PluginManager getInstance() {
        if (pluginManager == null) {
            synchronized (PluginManager.class) {
                if (pluginManager == null) {
                    pluginManager = new PluginManager();
                }
            }
        }
        return pluginManager;
    }

    public PluginManager() {
        pluginBeanList.clear();
    }


    public String loadApks(Context context, String name) {
        pluginBeanList.clear();
        //loadapk
        try {
            AssetManager assetManager = AssetManager.class.newInstance();

            Resources resources = context.getResources();

            ClassLoader classLoader = context.getClassLoader();


            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);

            File dataDir = context.getDataDir();

            String dir = dataDir.getPath() + File.separator + "Plugins";

            File apkDir = new File(dir);
            if (!apkDir.exists()) {
                boolean mkdirs = apkDir.mkdirs();
                Log.e("ces", "" + mkdirs);
            }
            String path = dir + File.separator + name;
            File apkFile = new File(path);
            if (!apkDir.exists()) {
                return null;
            }
            //加载
            addAssetPath.invoke(assetManager, path);

            File file = context.getDir("dex", Context.MODE_PRIVATE);

            DexClassLoader dexClassLoader = new DexClassLoader(path, file.getAbsolutePath(), null, classLoader);

            Resources pluginResource = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());

            PluginBean pluginBean = new PluginBean(path, dexClassLoader, assetManager, pluginResource);

            pluginBeanList.put(name, pluginBean);


            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public PluginBean getPluginBeanList(String name) {

        return pluginBeanList.get(name);
    }


    public Object loadPluginActivity(Activity activity, String name) {
        PluginBean pluginBean = pluginBeanList.get(name);
        if (pluginBean != null) {
            DexClassLoader dexClassLoader = pluginBean.getDexClassLoader();

            PackageManager packageManager = activity.getPackageManager();
            PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(pluginBean.getPath(), PackageManager.GET_ACTIVITIES);
            ActivityInfo activityInfo = packageArchiveInfo.activities[0];
            try {
                Class aClass = dexClassLoader.loadClass(activityInfo.name);
                return aClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
