package com.pluginstudy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pluginrule.ApkBean;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ApkBean> pluginList = new ArrayList<>();
    private RecyclerView rvList;
    private View viewById;

    private float MaxDy = 100;
    private float scrollDy = 0;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvList = findViewById(R.id.rv_list);
//        List<ApkGroups> data = getData();
//        rvList.setLayoutManager(new LinearLayoutManager(this));
//        rvList.setAdapter(new TestAdapter(data, this));
        rvList.setLayoutManager(new GridLayoutManager(this, 4));
        ApksAdapter apksAdapter = new ApksAdapter(pluginList, TestAdapter.getWidth(this));
        rvList.setAdapter(apksAdapter);
        apksAdapter.setOnItemClickListener((posiont, apkBean) -> {
//            Intent intent = new Intent(MainActivity.this, ProxyActivity.class);
//            intent.putExtra("packageName", apkBean.getPackageName());
//            startActivity(intent);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.voyah.plugin_caculate", "com.voyah.plugin_caculate.PluginActivity"));
            startActivity(intent);
        });


        ApkBean apkBean = new ApkBean(R.mipmap.icon_jisuanqi, "?????????"
                , "plugin_caculate-debug.apk", "com.voyah.plugin_caculate");

        RxPermissions rxPermissions = new RxPermissions(this);

        this.viewById = findViewById(R.id.bt_loadApk);
        viewById.setOnClickListener(l -> {
            //??????apk??????
            rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(permission -> {
                        if (permission.granted) {
                            pluginList.clear();

                            String apkName = apkBean.getApkName();
                            Applications.instance.hookLoadApk(apkName, apkBean.getPackageName());
//                            if (!TextUtils.isEmpty(s) && !pluginList.contains(apkBean)) {
                            //??????view
                            pluginList.add(apkBean);
                            apksAdapter.notifyDataSetChanged();
//                            }
                        }
                    });

        });

//        hook();

    }

    private List<ApkGroups> getData() {
        List<ApkGroups> apkGroupsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int type = i % 2;
            String name = "????????????" + i;
            List<ApkBean> apkBeans = new ArrayList<>();
            if (type == 1) {
                for (int j = 0; j < 10; j++) {
                    ApkBean apkBean = new ApkBean(R.mipmap.icon_jisuanqi, "?????????" + j
                            , "calander.apk", "com.voyah.plugin_calanda");
                    apkBeans.add(apkBean);
                }
            }

            apkGroupsList.add(new ApkGroups(type, name, apkBeans));
        }
        return apkGroupsList;
    }

    private void hook() {

        try {
            //????????????
            Class<View> viewClass = View.class;
            Method getListenerInfo = viewClass.getDeclaredMethod("getListenerInfo");
            //?????????hook???ListenerInfo
            getListenerInfo.setAccessible(true);
            Object invoke = getListenerInfo.invoke(viewById);///ListenerInfo
            Field mOnClickListener = invoke.getClass().getDeclaredField("mOnClickListener");
            Object o = mOnClickListener.get(invoke);//OnClickListener

            View.OnClickListener onClickListener = (View.OnClickListener) Proxy.newProxyInstance(getClassLoader(), new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Toast.makeText(MainActivity.this, "??????hook???,????????????????????????", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, TestActivity.class);
                    startActivity(intent);

                    return null;
                }
            });
            mOnClickListener.set(invoke, onClickListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}