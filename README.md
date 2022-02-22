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
V1.3：插件内占位插件
   1.写法和activity一样


