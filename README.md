# PluginStudying
学习插件
V1.1:
    1.ProxyActivity裏面一定要复写
       getResources();
       getClassLoader();
       getAssets();
      否则插件里面setcontentView()会空白
    2.重点
      使用assertmanage的addAssetPath 加载apk
      通过packagemanager的getPackageArchiveInfo找到目标apk的activity[0]
      使用classloader.loadclass加载目标activity
      Class.newinstance()获取目标示例，然后调用指定的oncreate()等方法
      setcontentview都需要使用proxyactivity的setcontentview
V1.2: 插件内Activity的跳转
   1.修改成用包名存储classloader
   需要注意的是：1.点击事件findviewbyID需要使用ProxyActivity
                2.startactivity也需要使用ProxyActivity
                3.不知道怎么获取启动页，这里需要将启动页放在第一个定义
                4.需要穿包名，因为用的是ProxyActivity，跳转的intent包名是ProxyActivity的,因此这里用string剪切最后一个“.”
                之前的字符为包名
V1.3：插件内占位service
   1.写法和activity一样
V1.4: 插件内占位recerver和静态broadcast
   静态：主要利用反射，拿到PackageParser，调用其方法parsePackage 获取Package对象
        package对象里面包含了manifest里面注册的所有信息
        反射拿到Package的 recervices 遍历其intents然后注册


无注册activity启动（api30）
   1.hook IActivityTaskManager的startactivity方法 ，在其调用替换proxyactivity骗过ams
  activity: startactivity()->startactivityforresult()------
  Instrumentation: execStartActivity()-----------ActivityTaskManager.getservice().startactivity()
  ActivityTaskManager:getservice()返回的是IActivityTaskManager
hook重点在ActivityTaskManager的静态变量IActivityTaskManagerSingleton 反射获取值得到IActivityTaskManager，然后修改
   2.hook 在handleLaunchActivity前将intent改回来
   ams检测完后，binder通信给app,app内部是通过handler-message传递.
   这里反射获取ActivityThread的 mh(handler对象)，handler的dispatchMessage中callback!=null直接执行mCallback.handleMessage(msg)
   因此这里给mh设置一个callback ,就可以让handleLaunchActivity信号时候执行自己的代码了

   api30时候: handleLaunchActivity()调用是在launchActivityItem
        ams事务-->app（159）msg.obj是ClientTransaction对象，  ClientTransaction的mActivityCallbacks里面就是一个包含
   launchActivityItem的arraylist ,反射拿到mActivityCallbacks,然后获取launchActivityItem ,反射aunchActivityItem的intent
   将intent修改成Testactivity，然后还要将launchActivityItem里面的包名修改成指定的包名


class加载过程：
   start新activity时候---ams---------handlerlauchActivity-----Instrumentation.newActivity()----dexclassLoader.loadClass
  newActivity时候，最终是PathClassLoader的findclass-->basedexclassloader的findclass---dexpathlist.findclass
  在dexpathlist里面就有遍历其属性,dexelements(classdexes)
android的classloader有 1.bootclassloader 2.dexpathclassloader 3.pathclassloader
1:系统预加载使用的 2：运行程序（系统，应用）加载class 3:加载apk,zip等文件
系统内核启动第一个进程init(),init()进程孵化出其子类zygote进程，zygote进程启动systemserver，由systemserver启动服务ams,pms
zygote进程里面  zygoteinit()就会启动bootclassloader 和 pathclassloader

使用hook形式加载插件---融合DexElement
      mInstrumentation的newActivity里面是调用的BaseDexClassLoader的loadClass
   loadclass最终从dexElements里面查找到对应的class然后newintance()
        BaseDexClassLoader里面的属性pathList（pathlist对象）
        pathlist对象里面的dexElements 获取elements数组
    同时获取宿主和插件的，然后将其融合，最后设置给dexElements
 缺點：插件越多dexelement内存越大

使用loadApk的方式:
      handlerLaunchActivity时候,会getPackageInfo获取到LoadedApk对象（mPackager），然后用loadedApk对象里面的classLoader给
 mInstrumentaton去newActivity，这里自定义插件的loadApk，设置插件classLoader,然后将loadApk加入到缓存中
       跳过pms检测，hook  IPackageManager的 getPackageInfo 返回一个不为空的packageInfor
       (虽然绕过了，但是测试发现总是报resourceid找不到啥的)




为什么onresume后才会看到并能点击activity?

view的绘制过程是在类ViewRootImpl中完成

   Windmanagerimpl的addview()-->-WindowManagerGlobal.addview()
    --->new ViewRootImpl.setview()---> doTarversals()

  而windmanagerimpl在handleresumeActivity()触发





