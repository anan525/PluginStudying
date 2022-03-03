package com.pluginstudy;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pluginrule.ApkBean;

import org.w3c.dom.Text;

import java.util.ArrayList;
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
public class TestAdapter extends RecyclerView.Adapter {

    private List<ApkGroups> apkBeanList = new ArrayList<>();

    private Context context;

    public TestAdapter(List<ApkGroups> apkBeanList, Context context) {

        this.apkBeanList = apkBeanList;
        this.context = context;
    }

    public static int getWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new TitleViewHolder(View.inflate(parent.getContext(), R.layout.adapter_title_item, null));
        } else if (viewType == 1) {
            return new ItemViewHodler(View.inflate(parent.getContext(), R.layout.adapter_apk_group_item, null));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        ApkGroups apkGroups = apkBeanList.get(position);
        return apkGroups.getType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ApkGroups apkGroups = apkBeanList.get(position);
        int type = apkGroups.getType();
        if (type == 0) {
            //标题
            TitleViewHolder mHolder = (TitleViewHolder) holder;
            mHolder.tvTitle.setText(apkGroups.getGroupName());
        } else if (type == 1) {
            ItemViewHodler itemViewHodler = (ItemViewHodler) holder;
            List<ApkBean> apkBeanList = apkGroups.getApkBeanList();
            itemViewHodler.rvList.setLayoutManager(new GridLayoutManager(context, 4));
            itemViewHodler.rvList.setAdapter(new ApksAdapter(apkBeanList,getWidth(context)));
        }
    }

    @Override
    public int getItemCount() {
        return apkBeanList.size();
    }


    static class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }

    static class ItemViewHodler extends RecyclerView.ViewHolder {

        private RecyclerView rvList;

        public ItemViewHodler(@NonNull View itemView) {
            super(itemView);
            rvList = itemView.findViewById(R.id.rv_list);
        }
    }
}
