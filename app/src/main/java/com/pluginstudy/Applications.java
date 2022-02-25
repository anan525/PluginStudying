package com.pluginstudy;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
 * 2022/2/24.
 * --
 * NAME.
 * anyq.
 * --
 */
public class Applications extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            //使用宿主替换要跳转的activity
            hookStartActivity();
            //跳转完后要指定回来
            hookRebaseActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    private void hookStartActivity() throws Exception {
        Class<?> iActivityManagerClass = Class.forName("android.app.IActivityTaskManager");

        Class<?> aClass = Class.forName("android.app.ActivityTaskManager");
        Field getIAMSField = aClass.getDeclaredField("IActivityTaskManagerSingleton");
        getIAMSField.setAccessible(true);
        Object singtonIAMS = getIAMSField.get(null);//Singleton<IActivityTaskManager>

        Class<?> singletonClass = Class.forName("android.util.Singleton");
        Method get = singletonClass.getDeclaredMethod("get");
        Field mInstance = singletonClass.getDeclaredField("mInstance");
        mInstance.setAccessible(true);
        get.setAccessible(true);
        Object iActivityTaskManager = get.invoke(singtonIAMS);

        Object mIActivityTaskManager = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{iActivityManagerClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("startActivity")) {
                    if (args.length > 3) {
                        Object arg = args[3];
                        if (arg != null && arg instanceof Intent) {
                            String className = ((Intent) arg).getComponent().getClassName();
                            if (className.equals(TestActivity.class.getName())) {
                                //找到了
                                Intent intent = new Intent(Applications.this, ProxyActivity.class);
                                intent.putExtra("acturallIntent", (Intent) arg);
                                args[3] = intent;
                            }
                        }
                    }
                }

                return method.invoke(iActivityTaskManager, args);
            }
        });
        //最重要的一步，需要将iActivityTaskManager替换成我们想要的
        mInstance.set(singtonIAMS, mIActivityTaskManager);
    }


    private void hookRebaseActivity() throws Exception {

        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");


        Field mHField = activityThreadClass.getDeclaredField("mH");
        mHField.setAccessible(true);
        Method currentActivityThread = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThread.setAccessible(true);

        Object activityThread = currentActivityThread.invoke(null);
        Handler mh = (Handler) mHField.get(activityThread);

        Field mCallbackFiled = Handler.class.getDeclaredField("mCallback");
        mCallbackFiled.setAccessible(true);
        mCallbackFiled.set(mh, (Handler.Callback) msg -> {
            //拿到obj
            Object obj = msg.obj;
            Log.d("测试", "" + msg.what);
            try {
                if (obj != null) {
                    Class<?> aClass = Class.forName("android.app.servertransaction.ClientTransaction");
                    Field mLifecycleStateRequest = aClass.getDeclaredField("mActivityCallbacks");
                    mLifecycleStateRequest.setAccessible(true);
                    Object o = mLifecycleStateRequest.get(obj);
                    if (o != null && o instanceof List) {
                        if (((List) o).size() > 0) {
                            //launchActivityItem
                            Object launchActivityItem = ((List) o).get(0);

                            Field mIntentField = launchActivityItem.getClass().getDeclaredField("mIntent");
                            mIntentField.setAccessible(true);
                            Intent intent = (Intent) mIntentField.get(launchActivityItem);

                            if (intent.hasExtra("acturallIntent")) {
                                Parcelable acturallIntent = intent.getParcelableExtra("acturallIntent");
                                mIntentField.set(launchActivityItem, acturallIntent);
                            }

                        }
                    }

                }
            } catch (Exception e) {

            }

            mh.handleMessage(msg);
            return true;
        });
    }
}
