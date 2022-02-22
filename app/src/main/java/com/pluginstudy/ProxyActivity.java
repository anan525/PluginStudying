package com.pluginstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.pluginrule.ActivityInterfaces;
import com.pluginrule.PluginBean;
import com.pluginrule.PluginManager;

public class ProxyActivity extends Activity {

    private ActivityInterfaces targetActivity;

    private PluginBean pluginBean;

    @Override
    public Resources getResources() {
        if (pluginBean != null) {
            return pluginBean.getResources();
        }
        return super.getResources();
    }

    @Override
    public ClassLoader getClassLoader() {
        if (pluginBean != null) {
            return pluginBean.getDexClassLoader();
        }
        return super.getClassLoader();
    }

    @Override
    public AssetManager getAssets() {
        if (pluginBean != null) {
            return pluginBean.getAssetManager();
        }
        return super.getAssets();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String packageName = getIntent().getStringExtra("packageName");
        if (!TextUtils.isEmpty(packageName)) {
            pluginBean = PluginManager.getInstance().getPluginBeanList(packageName);
            String className = getIntent().getStringExtra("className");

            Object activity = PluginManager.getInstance().loadPluginActivity(this, packageName, className);
            if (activity != null && activity instanceof ActivityInterfaces) {
                targetActivity = ((ActivityInterfaces) activity);
                targetActivity.attachToActivity(this);
                targetActivity.onCreate(savedInstanceState);
            }
        }
    }


    @Override
    public void startActivity(Intent intent) {
        String className = intent.getComponent().getClassName();
        String packageName = PluginManager.getInstance().getPackageNameFromClassName(className);
        Intent intentTask = new Intent(this, ProxyActivity.class);
        intentTask.putExtra("className", className);
        intentTask.putExtra("packageName", packageName);
        super.startActivity(intentTask);
    }


    @Override
    public ComponentName startService(Intent service) {
        String className = service.getComponent().getClassName();
        String packageName = PluginManager.getInstance().getPackageNameFromClassName(className);
        Intent intentTask = new Intent(this, ProxyService.class);
        intentTask.putExtra("className", className);
        intentTask.putExtra("packageName", packageName);

        //开启服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //26以上必须用startForegroundService
            return startForegroundService(intentTask);
        } else {
            //26一下直接startService
            return startService(intentTask);
        }
    }
}