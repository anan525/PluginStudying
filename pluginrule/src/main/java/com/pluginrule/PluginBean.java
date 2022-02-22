package com.pluginrule;

import android.content.res.AssetManager;
import android.content.res.Resources;

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
public class PluginBean {

    private String path;

    private DexClassLoader dexClassLoader;

    private AssetManager assetManager;

    private Resources resources;

    public PluginBean(String path, DexClassLoader dexClassLoader, AssetManager assetManager, Resources resources) {
        this.path = path;
        this.dexClassLoader = dexClassLoader;
        this.assetManager = assetManager;
        this.resources = resources;
    }

    public String getPath() {
        return path;
    }

    public PluginBean setPath(String path) {
        this.path = path;
        return this;
    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    public PluginBean setDexClassLoader(DexClassLoader dexClassLoader) {
        this.dexClassLoader = dexClassLoader;
        return this;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public PluginBean setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
        return this;
    }

    public Resources getResources() {
        return resources;
    }

    public PluginBean setResources(Resources resources) {
        this.resources = resources;
        return this;
    }
}
