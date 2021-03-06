package com.pluginstudy;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.pluginrule.ActivityInterfaces;
import com.pluginrule.PluginBean;
import com.pluginrule.PluginManager;

public class ProxyActivity extends AppCompatActivity {

    private ActivityInterfaces targetActivity;
    private ProxyReceriver proxyReceriver = new ProxyReceriver();
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

        //????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //26???????????????startForegroundService
            return startForegroundService(intentTask);
        } else {
            //26????????????startService
            return startService(intentTask);
        }
    }


    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        String name = receiver.getClass().getName();
        proxyReceriver.loadclassName(name);
        return super.registerReceiver(proxyReceriver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}