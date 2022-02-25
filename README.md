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
   将intent修改成Testactivity




为什么onresume后才会看到并能点击activity?

view的绘制过程是在类ViewRootImpl中完成

   Windmanagerimpl的addview()-->-WindowManagerGlobal.addview()
    --->new ViewRootImpl.setview()---> doTarversals()

  而windmanagerimpl在handleresumeActivity()触发


