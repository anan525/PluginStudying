package com.pluginstudy;

import com.pluginrule.ApkBean;

import java.util.List;

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
 * 2022/2/28.
 * --
 * NAME.
 * anyq.
 * --
 */
public class ApkGroups {

    private int type;

    private String groupName;

    private List<ApkBean> apkBeanList;

    public ApkGroups(int type, String groupName, List<ApkBean> apkBeanList) {
        this.type = type;
        this.groupName = groupName;
        this.apkBeanList = apkBeanList;
    }

    public int getType() {
        return type;
    }

    public ApkGroups setType(int type) {
        this.type = type;
        return this;
    }

    public String getGroupName() {
        return groupName;
    }

    public ApkGroups setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public List<ApkBean> getApkBeanList() {
        return apkBeanList;
    }

    public ApkGroups setApkBeanList(List<ApkBean> apkBeanList) {
        this.apkBeanList = apkBeanList;
        return this;
    }
}
