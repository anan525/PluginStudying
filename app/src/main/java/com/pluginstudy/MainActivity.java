package com.pluginstudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.pluginrule.ApkBean;
import com.pluginrule.PluginManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ApkBean> pluginList = new ArrayList<>();
    private RecyclerView rvList;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new GridLayoutManager(this, 4));
        ApksAdapter apksAdapter = new ApksAdapter(pluginList);
        rvList.setAdapter(apksAdapter);
        apksAdapter.setOnItemClickListener((posiont, apkBean) -> {
            Intent intent = new Intent(MainActivity.this, ProxyActivity.class);
            intent.putExtra("packageName", apkBean.getPackageName());
            startActivity(intent);
        });


        ApkBean apkBean = new ApkBean(R.mipmap.icon_jisuanqi, "计算器"
                , "calander.apk", "com.voyah.plugin_calanda");

        RxPermissions rxPermissions = new RxPermissions(this);

        findViewById(R.id.bt_loadApk).setOnClickListener(l -> {
            //加载apk信息
            rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(permission -> {
                        if (permission.granted) {
                            pluginList.clear();

                            String apkName = apkBean.getApkName();
                            String s = PluginManager.getInstance().loadApks(MainActivity.this
                                    , apkBean);
                            if (!TextUtils.isEmpty(s) && !pluginList.contains(apkBean)) {
                                //更新view
                                pluginList.add(apkBean);
                                apksAdapter.notifyDataSetChanged();
                            }
                        }
                    });

        });
    }
}