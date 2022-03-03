package com.pluginstudy;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.pluginrule.PluginManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

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

    public static Applications instance;
    private BaseDexClassLoader dexClassLoader;
    private AssetManager assetManager;
    private Resources resources;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        try {
            //使用宿主替换要跳转的activity
            hookStartActivity();
            //跳转完后要指定回来
            hookRebaseActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void hookLoadApk(String name, String packageName) throws Exception {
        String path = getDataDir().getPath() + File.separator + "Plugins" + File.separator + name;
        //加载资源
        doLoadPluginLayout(path);

        Class<?> mActivityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = mActivityThreadClass.
                getDeclaredMethod("currentActivityThread");
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);

        Field mPackagesField = mActivityThreadClass.getDeclaredField("mPackages");
        mPackagesField.setAccessible(true);
        //activityThread 的mPackages插入 插件的自定义loadedapk
        ArrayMap mpackages = (ArrayMap) mPackagesField.get(currentActivityThread);

        Object loadApk = getLoadApk(currentActivityThread, name, packageName);
        //设置里面的classLoader

        //设置loadedAPk
        WeakReference weakReference = new WeakReference(loadApk);
        mpackages.put(packageName, weakReference);

        //************************ PackageManager.getPackageInfoAsUserCached(
        //                        mPackageName,
        //                        PackageManager.MATCH_DEBUG_TRIAGED_MISSING,
        //                        UserHandle.myUserId());******************************//
        //需要绕过getPackageInfoAsUserCached返回的packageInfo 插件会返回null
        Method getPackageManagerMethod = mActivityThreadClass.getDeclaredMethod("getPackageManager");
        getPackageManagerMethod.setAccessible(true);
        Object iPackageManager = getPackageManagerMethod.invoke(null);

        Class<?> iPackageManagerClass = Class.forName("android.content.pm.IPackageManager");
        Object iPackageManagerInterface = Proxy.newProxyInstance(getClassLoader(), new Class[]{iPackageManagerClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("getPackageInfo")) {
                    if (args != null && args.length == 3 && args[0] instanceof String) {
                        String mPackageName = (String) args[0];
                        if (mPackageName.equals(packageName)) {
                            return new PackageInfo();
                        }
                    }
                }
                return method.invoke(iPackageManager, args);
            }
        });
        Field sPackageManager = mActivityThreadClass.getDeclaredField("sPackageManager");
        sPackageManager.setAccessible(true);
        sPackageManager.set(currentActivityThread, iPackageManagerInterface);
    }

    /**
     * 自定义loadedApk
     *
     * @param currentActivityThread
     * @return
     */
    private Object getLoadApk(Object currentActivityThread, String name, String packageName) throws Exception {
        Class<?> compatibilityInfoClass = Class.forName("android.content.res.CompatibilityInfo");
        Method getPackageInfoNoCheckMethod = currentActivityThread.getClass().getDeclaredMethod("getPackageInfoNoCheck",
                ApplicationInfo.class, compatibilityInfoClass);
        //1.获取applicationinfor
        Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");
        Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage", File.class, int.class);
        parsePackageMethod.setAccessible(true);

        String path = getDataDir().getPath() + File.separator + "Plugins" + File.separator + name;
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("没找到file");
        }
        Class<?> packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
        Object mPackage = parsePackageMethod.invoke(packageParserClass.newInstance(), file, PackageManager.GET_ACTIVITIES);
        Method generateApplicationInfoMethod = packageParserClass.
                getDeclaredMethod("generateApplicationInfo", mPackage.getClass(), int.class, packageUserStateClass);
        generateApplicationInfoMethod.setAccessible(true);
        Object mApplicationInfor = generateApplicationInfoMethod.invoke(null, mPackage,
                PackageManager.GET_ACTIVITIES, packageUserStateClass.newInstance());
        //2.获取compatibilityInfoClass
        Object compatibilityInfo = compatibilityInfoClass.getDeclaredField("DEFAULT_COMPATIBILITY_INFO").get(null);

        Object loadedApk = getPackageInfoNoCheckMethod.invoke(currentActivityThread, mApplicationInfor, compatibilityInfo);

        File dexFile = getDir("dex", Context.MODE_PRIVATE);
        //设置classLoader
        DexClassLoader dexClassLoader = new DexClassLoader(path,
                /*缓存目录*/dexFile.getAbsolutePath(), null, getClassLoader());
        Field mClassLoaderFiled = loadedApk.getClass().getDeclaredField("mClassLoader");
        mClassLoaderFiled.setAccessible(true);
        mClassLoaderFiled.set(loadedApk, dexClassLoader);
        return loadedApk;
    }

    public String hookPlugin(String apkName) throws Exception {
        //pathList  //2.dexElements
        ClassLoader classLoader = getClassLoader();
        Class<?> mBaseDexClassLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
        Field pathListField = mBaseDexClassLoaderClass.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object mPathList = pathListField.get(classLoader);//mPathList对象
        //获取dexElements[]
        Field dexElementsField = mPathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        Object dexElements = dexElementsField.get(mPathList);


        File dataDir = getDataDir();

        String dir = dataDir.getPath() + File.separator + "Plugins";

        File apkDir = new File(dir);
        if (!apkDir.exists()) {
            boolean mkdirs = apkDir.mkdirs();
            Log.e("ces", "" + mkdirs);
        }
        String path = dir + File.separator + apkName;
        File apkFile = new File(path);
        if (!apkFile.exists()) {
            throw new FileNotFoundException("没找到apk");
        }


        dexClassLoader = new BaseDexClassLoader(path, apkDir, null, classLoader);
        Field pathListPluginField = dexClassLoader.getClass().getDeclaredField("pathList");
        pathListPluginField.setAccessible(true);
        Object mPluginPathList = pathListPluginField.get(dexClassLoader);//mPathList对象
        //获取dexElements[]
        Field mPluginDexElements = mPluginPathList.getClass().getDeclaredField("dexElements");
        mPluginDexElements.setAccessible(true);
        Object mPluginDexElement = mPluginDexElements.get(mPluginPathList);

        int length = Array.getLength(dexElements);
        int pluginLength = Array.getLength(mPluginDexElement);
        //合并
        Object newDexElement = Array.newInstance(dexElements.getClass().getComponentType(), length + pluginLength);
        for (int i = 0; i < length + pluginLength; i++) {
            if (i < length) {
                Array.set(newDexElement, i, Array.get(dexElements, i));
            } else {
                Array.set(newDexElement, i, Array.get(mPluginDexElement, i - length));
            }
        }
        //设置进去
        dexElementsField.set(mPathList, newDexElement);

        //资源文件
        doLoadPluginLayout(path);

        return path;
    }

    private void doLoadPluginLayout(String path) throws Exception {
        assetManager = AssetManager.class.newInstance();
        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
        addAssetPath.setAccessible(true);
        int invoke = (int) addAssetPath.invoke(assetManager, path);//apks

        resources = new Resources(assetManager, getResources().getDisplayMetrics(), getResources().getConfiguration());
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
                            //找到了
                            String packageName = ((Intent) arg).getPackage();
                            ComponentName component = ((Intent) arg).getComponent();
                            String packageName1 = "";
                            if (component != null) {
                                packageName1 = component.getPackageName();
                            }
                            if (("" + packageName).equals("com.voyah.plugin_caculate") || packageName1.equals("com.voyah.plugin_caculate")) {
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
                                //需要修改launchActivityItem的activityInfor
                                Field mInfoFiled = launchActivityItem.getClass().getDeclaredField("mInfo");
                                mInfoFiled.setAccessible(true);
                                ActivityInfo activityInfo = (ActivityInfo) mInfoFiled.get(launchActivityItem);

                                Intent acturallIntent = intent.getParcelableExtra("acturallIntent");
                                mIntentField.set(launchActivityItem, acturallIntent);

                                String className = acturallIntent.getComponent().getClassName();
                                String packageName = acturallIntent.getComponent().getPackageName();
                                activityInfo.packageName = packageName;
                                activityInfo.applicationInfo.packageName = packageName;


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

    public AssetManager getAssetManager() {
        if (assetManager != null) {
            return assetManager;
        }
        return super.getAssets();
    }

    @Override
    public Resources getResources() {
        if (resources != null) {
            return resources;
        }
        return super.getResources();
    }
}
