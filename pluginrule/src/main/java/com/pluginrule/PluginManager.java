package com.pluginrule;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
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


    public String loadApks(Context context, ApkBean apkBean) {
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
            String path = dir + File.separator + apkBean.getApkName();
            File apkFile = new File(path);
            if (!apkFile.exists()) {
                return null;
            }
            //加载
            addAssetPath.invoke(assetManager, path);

            File file = context.getDir("dex", Context.MODE_PRIVATE);

            DexClassLoader dexClassLoader = new DexClassLoader(path, file.getAbsolutePath(), null, classLoader);

            Resources pluginResource = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());

            PluginBean pluginBean = new PluginBean(path, dexClassLoader, assetManager, pluginResource);

            pluginBeanList.put(apkBean.getPackageName(), pluginBean);


            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public PluginBean getPluginBeanList(String packageName) {

        return pluginBeanList.get(packageName);
    }


    public Object loadPluginActivity(Activity activity, String packageName, String className) {
        PluginBean pluginBean = pluginBeanList.get(packageName);
        if (pluginBean != null) {
            DexClassLoader dexClassLoader = pluginBean.getDexClassLoader();
            PackageManager packageManager = activity.getPackageManager();
            PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(pluginBean.getPath(), PackageManager.GET_ACTIVITIES);
            ActivityInfo[] activities = packageArchiveInfo.activities;
            if (TextUtils.isEmpty(className)) {
                //如果是空的，说明加载启动页
                //xml里面要把启动页放在第一个
                className = activities[0].name;
            }
            try {
                Class aClass = dexClassLoader.loadClass(className);
                return aClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public Object loadPluginService(String packageName, String className) {
        PluginBean pluginBean = pluginBeanList.get(packageName);
        if (pluginBean != null) {
            DexClassLoader dexClassLoader = pluginBean.getDexClassLoader();
            try {
                Class aClass = dexClassLoader.loadClass(className);
                return aClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getPackageNameFromClassName(String className) {
        //这里需要把包名在名字里面切出来
        int i = className.lastIndexOf(".");
        return className.substring(0, i);
    }

}
