v1.0.0
1:SkyMVVM包重磅发布

v1.0.1
1:增加gsonfactory,gson库
2:增加对应库的混淆规则
3:增加version.md说明文件

v1.0.2
1:增加SkyFlow,对flow封装

v1.0.3
1:SkyFlow内部优化
2:agp版本升级8.8.1->8.9.0，gradle版本升级8.10.2->8.11.1
3:增加描叙文本自定义,如下:
<resources>
    <string name="sky_mmvmlib_exception_unknown">请求失败,请稍后再试</string>
    <string name="sky_mmvmlib_exception_parse_error">解析错误,请稍后再试</string>
    <string name="sky_mmvmlib_exception_network_error">网络连接错误,请稍后重试</string>
    <string name="sky_mmvmlib_exception_ssl_error">证书出错,请稍后再试</string>
    <string name="sky_mmvmlib_exception_timeout_error">网络连接超时,请稍后重试</string>
    <string name="sky_mmvmlib_download_error">下载错误</string>
    <string name="sky_mmvmlib_loading_message">请求网络中</string>
</resources>

v1.0.4
1:相关版本依赖升级
2:ActivityMessenger类方法中增加flags可选参数
3:PageUtils类优化泛型参数T，可选

v1.0.5
1:agp版本升级8.9.1->9.0.0，kotlin版本升级2.1.20->2.4.0，gradle版本升级8.11.1->9.6.1，相关其他的依赖版本对应升级
2:删除uploadArchive.gradle,增加AndroidMavenPublishConventionPlugin插件
3:增加hilt-noop-processor模块，仅用于声明识别 Hilt 在 KSP 场景下仍注入到 javac 的内部选项，从而消除 javac 的“以下选项未被任何处理程序识别”警告。
4:build-logic模块优化
