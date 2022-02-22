package com.pluginstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
        String className = getIntent().getStringExtra("className");

        if (!TextUtils.isEmpty(className)) {
            pluginBean = PluginManager.getInstance().getPluginBeanList(className);

            Object activity = PluginManager.getInstance().loadPluginActivity(this, className);
            if (activity != null && activity instanceof ActivityInterfaces) {
                targetActivity = ((ActivityInterfaces) activity);
                targetActivity.attachToActivity(this);
                targetActivity.onCreate(savedInstanceState);
            }
        }
    }

}