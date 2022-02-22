# PluginStudying
学习插件
1.ProxyActivity裏面一定要复写
   getResources();
   getClassLoader();
   getAssets();
  否则插件里面setcontentView()会空白