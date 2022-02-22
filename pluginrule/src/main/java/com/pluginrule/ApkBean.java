package com.pluginrule;

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
public class ApkBean {

    private int iconResource;

    private String name;

    private String apkName;

    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public ApkBean setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public ApkBean(int iconResource, String name, String apkName, String packageName) {
        this.iconResource = iconResource;
        this.name = name;
        this.apkName = apkName;
        this.packageName = packageName;
    }

    public String getApkName() {
        return apkName;
    }

    public ApkBean setApkName(String apkName) {
        this.apkName = apkName;
        return this;
    }

    public int getIconResource() {
        return iconResource;
    }

    public ApkBean setIconResource(int iconResource) {
        this.iconResource = iconResource;
        return this;
    }

    public String getName() {
        return name;
    }

    public ApkBean setName(String name) {
        this.name = name;
        return this;
    }
}
