package com.pluginstudy;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pluginrule.ApkBean;
import com.pluginrule.PluginBean;
import com.pluginrule.PluginManager;

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
 * 2022/2/21.
 * --
 * NAME.
 * anyq.
 * --
 */
public class ApksAdapter extends RecyclerView.Adapter {

    private OnItemClickListener onItemClickListener;

    public ApksAdapter setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    private List<ApkBean> apkBeans = new ArrayList<>();
    private int width;

    public ApksAdapter(List<ApkBean> apkBeans, int width) {
        this.width = width;
        this.apkBeans = apkBeans;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(parent.getContext(), R.layout.adapter_apkitem, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ApkBean apkBean = apkBeans.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.ivIcon.setImageResource(apkBean.getIconResource());
        viewHolder.tvTitle.setText(apkBean.getName());

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewHolder.clRoot.setLayoutParams(layoutParams);

        viewHolder.clRoot.setOnClickListener(l -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, apkBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apkBeans.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout clRoot;
        private ImageView ivIcon;
        private TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clRoot = itemView.findViewById(R.id.cl_root);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, ApkBean apkBean);
    }
}
