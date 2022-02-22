package com.pluginstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
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
        //这里需要把包名在名字里面切出来
        int i = className.lastIndexOf(".");
        String packageName = className.substring(0, i);

        Intent intentTask = new Intent(this, ProxyActivity.class);
        intentTask.putExtra("className", className);
        intentTask.putExtra("packageName", packageName);
        super.startActivity(intentTask);
    }
}