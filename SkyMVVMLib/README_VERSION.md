## [v1.1.0] - 2026-07-01
- 部分功能优化重构
- 库中所有功能依赖SkyMVVMLib，使用此库必须先调用SkyMVVMLib.init()
- 解构XLog依赖项，外部调用方可选传入即可
- 对所有核心功能类增加TAG，方便日志查看。本库所有日志前缀统一为"SkyMVVM"
- 增加SkyDnsParser支持DNS解析
- 日志拆分为okHttp网络请求响应log和常规的log日志(常规日志支持XLog三方库，默认禁用XLog模块)

## [v1.0.5] - 2026-06-29
- 升级构建工具版本：
  - AGP: `8.9.1` → `9.0.0`
  - Gradle: `8.11.1` → `9.6.1`
- 升级其他依赖库版本
- 删除`uploadArchive.gradle`,增加`AndroidMavenPublishConventionPlugin`插件
- 增加`hilt-noop-processor`模块，仅用于声明识别 Hilt 在 KSP 场景下仍注入到 javac 的内部选项，从而消除 javac 的“以下选项未被任何处理程序识别”警告。
- `build-logic`模块优化

## [v1.0.4] - 2026-04-08
- 升级相关依赖版本
- `ActivityMessenger` 类方法中增加 `flags` 可选参数
- 优化 `PageUtils` 类泛型参数 `T`，支持可选类型

## [v1.0.3] - 2025-03-18
- 优化 `SkyFlow` 内部实现
- 升级构建工具版本：
  - AGP: `8.8.1` → `8.9.0`
  - Gradle: `8.10.2` → `8.11.1`
- 新增异常提示文本的自定义支持（通过字符串资源）：

  ```xml
  <resources>
      <string name="sky_mmvmlib_exception_unknown">请求失败，请稍后再试</string>
      <string name="sky_mmvmlib_exception_parse_error">解析错误，请稍后再试</string>
      <string name="sky_mmvmlib_exception_network_error">网络连接错误，请稍后重试</string>
      <string name="sky_mmvmlib_exception_ssl_error">证书出错，请稍后再试</string>
      <string name="sky_mmvmlib_exception_timeout_error">网络连接超时，请稍后重试</string>
      <string name="sky_mmvmlib_download_error">下载错误</string>
      <string name="sky_mmvmlib_loading_message">请求网络中</string>
  </resources>

## [v1.0.2] - 2025-03-07
- 增加SkyFlow,对flow封装

## [v1.0.1] - 2025-03-01
- 增加gsonfactory,gson库
- 增加对应库的混淆规则
- 增加version.md说明文件

## [v1.0.0] - 2025-02-24
- SkyMVVM包重磅首发
