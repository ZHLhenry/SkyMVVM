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
