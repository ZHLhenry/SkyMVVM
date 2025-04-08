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
