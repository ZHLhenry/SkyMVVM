# SkyMVVMLib

**SkyMVVMLib** 是一个基于 Android Jetpack + MVVM 架构的轻量级组件化框架，封装了日常开发中高频使用的基础能力，帮助开发者快速搭建稳定、规范的 Android 应用。

---

## 技术栈

| 类别 | 技术 |
|------|------|
| 架构模式 | MVVM（Model-View-ViewModel） |
| 依赖注入 | Hilt |
| 异步框架 | Kotlin Coroutines + Flow |
| 网络请求 | Retrofit2 + OkHttp |
| 数据解析 | Gson + GsonFactory |
| 生命周期 | Lifecycle + LiveData |
| 视图绑定 | ViewBinding / DataBinding |
| 事件总线 | SharedFlow（自封装 SkyFlow） |

---

## 模块结构

```
com.sky.mvvm
├── base/                    # 基础组件
│   ├── BaseApplication      # Application 基类，初始化全局配置
│   ├── activity/            # Activity 基类
│   │   ├── BaseVmActivity       # 普通 MVVM Activity
│   │   ├── BaseVmDbActivity     # DataBinding Activity
│   │   └── BaseVmVbActivity     # ViewBinding Activity
│   ├── fragment/            # Fragment 基类
│   │   ├── BaseVmFragment         # 普通 MVVM Fragment（支持懒加载）
│   │   ├── BaseVmDbFragment       # DataBinding Fragment
│   │   └── BaseVmVbFragment       # ViewBinding Fragment
│   └── viewmodel/
│       └── BaseViewModel          # ViewModel 基类，内置 Loading 状态通知
│
├── callback/                # 数据回调封装
│   ├── databind/            # ObservableField 类型封装（Int/String/Boolean...）
│   └── livedata/            # LiveData 类型封装 + UnPeekLiveData（防倒灌）
│       └── event/
│           └── EventLiveData      # 事件型 LiveData（一次性消费）
│
├── ext/                     # 扩展工具
│   ├── BaseViewModelExt     # ViewModel 网络请求扩展（apiRequest / parseState）
│   ├── ViewBindUtil         # 泛型反射自动创建 ViewBinding/DataBinding
│   ├── download/            # 文件下载模块（支持断点续传、暂停、取消）
│   ├── lifecycle/           # 全局生命周期监听（前后台、Activity 数量）
│   ├── util/                # 常用扩展（日志、屏幕尺寸、系统服务）
│   └── view/                # View 扩展（显示隐藏、防重复点击、转 Bitmap）
│
├── flow/                    # SkyFlow 事件总线
│   ├── SkyFlow              # 基于 SharedFlow 的事件总线（普通事件 + 粘性事件）
│   └── SkyFlowEventData     # 事件数据封装
│
├── network/                 # 网络模块
│   ├── BaseNetworkApi       # Retrofit 构建器基类（支持 HTTP/HTTPS 忽略证书）
│   ├── BaseResponse         # 服务器响应基类（需子类实现脱壳逻辑）
│   ├── AppException         # 自定义异常封装
│   ├── ExceptionHandle      # 异常分类处理
│   ├── Error                # 错误码枚举（网络/解析/超时/SSL...）
│   ├── state/ResultState    # 请求结果状态密封类（Loading/Success/Error）
│   ├── interceptor/         # 缓存拦截器
│   ├── log/                 # 网络请求日志拦截器（可配置级别）
│   └── manager/             # 网络状态监听管理
│
└── util/                    # 工具类
    ├── ActivityMessenger    # Activity 跳转工具（泛型参数传递、startActivityForResult）
    ├── NetworkUtil          # 网络状态检测工具
    ├── HttpsCerUtils        # HTTPS 证书工具（信任所有证书模式）
    ├── CharacterHandler     # 字符处理工具
    ├── UrlEncoderUtils      # URL 编码工具
    └── ZipHelper            # 压缩/解压工具
```

---

## 快速开始

### 1. 初始化 Application

```kotlin
class MyApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        // BaseApplication 已自动完成：
        // - 网络状态广播注册
        // - Activity 生命周期回调注册
        // - 应用前后台监听
    }
}
```

### 2. 创建 Activity

**普通布局（layoutId）：**

```kotlin
class MainActivity : BaseVmActivity<MainViewModel>() {
    override fun layoutId() = R.layout.activity_main
    override fun initView(savedInstanceState: Bundle?) { /* ... */ }
    override fun showLoading(message: String) { /* 显示加载框 */ }
    override fun dismissLoading() { /* 隐藏加载框 */ }
    override fun createObserver() { /* 观察 LiveData */ }
}
```

**ViewBinding 布局：**

```kotlin
class MainActivity : BaseVmVbActivity<MainViewModel, ActivityMainBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.tvTitle.text = "Hello"
    }
    override fun showLoading(message: String) { /* ... */ }
    override fun dismissLoading() { /* ... */ }
    override fun createObserver() { /* ... */ }
}
```

**DataBinding 布局：**

```kotlin
class MainActivity : BaseVmDbActivity<MainViewModel, ActivityMainBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
    }
    override fun showLoading(message: String) { /* ... */ }
    override fun dismissLoading() { /* ... */ }
    override fun createObserver() { /* ... */ }
}
```

### 3. 创建 ViewModel 并发起网络请求

```kotlin
@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {

    val articleList = MutableLiveData<ResultState<List<Article>>>()

    fun loadArticles(context: Context) {
        apiRequest(
            context = context,
            block = { apiService.getArticles() },
            resultState = articleList,
            isShowDialog = true,
            loadingMessage = "加载中..."
        )
    }
}
```

### 4. 在 Activity/Fragment 中解析状态

```kotlin
override fun createObserver() {
    mViewModel.articleList.observe(this) { result ->
        parseState(result,
            onSuccess = { data ->
                // 请求成功，data 为解包后的业务数据
                adapter.setList(data)
            },
            onError = { exception ->
                // 请求失败，exception.errorMsg 为可读错误描述
                Toast.makeText(this, exception.errorMsg, Toast.LENGTH_SHORT).show()
            }
        )
    }
}
```

### 5. 使用 SkyFlow 事件总线

**发送事件：**

```kotlin
// 普通事件
SkyFlow.with<String>("event_key").post(scope, "Hello SkyFlow")

// 粘性事件（后注册的订阅者也能收到最新一次事件）
SkyFlow.withStick<Int>("sticky_key").post(scope, 100)
```

**接收事件：**

```kotlin
// 在 Activity/Fragment 中注册（自动绑定生命周期）
SkyFlow.with<String>("event_key").register(
    lifecycleOwner = this,
    filter = { it.isNotEmpty() }
) { event ->
    // 处理事件
}
```

### 6. 文件下载

```kotlin
val downloadManager: DownLoadManager = // Hilt 注入

downloadManager.downLoad(
    tag = "apk_file",
    url = "https://example.com/app.apk",
    savePath = cacheDir.absolutePath,
    saveName = "app.apk",
    loadListener = object : OnDownLoadListener {
        override fun onDownLoadPrepare(key: String) { /* 准备开始 */ }
        override fun onDownLoadProgress(key: String, progress: Int) { /* 进度更新 */ }
        override fun onDownLoadSuccess(key: String, path: String, size: Long) { /* 下载成功 */ }
        override fun onDownLoadPause(key: String) { /* 暂停 */ }
        override fun onDownLoadError(key: String, throwable: Throwable) { /* 失败 */ }
    }
)
```

---

## 自定义异常提示文本

在宿主 App 的 `res/values/strings.xml` 中覆盖以下资源即可自定义提示文案：

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
```

---

## 网络请求说明

### BaseResponse 脱壳

继承 `BaseResponse<T>` 并实现以下方法，框架会自动判断请求是否成功并解包数据：

```kotlin
data class ApiResponse<T>(
    val code: Int,
    val msg: String,
    val data: T?
) : BaseResponse<T>() {
    override fun isSucces() = code == 0
    override fun getResponseData(): T = data!!
    override fun getResponseCode() = code
    override fun getResponseMsg() = msg
}
```

若接口不需要脱壳处理，使用 `apiRequestNoCheck` 方法直接返回原始数据。

### 错误码对照

| 错误码 | 类型 | 说明 |
|--------|------|------|
| 1000 | UNKNOWN | 未知错误 |
| 1001 | PARSE_ERROR | 数据解析错误 |
| 1002 | NETWORK_ERROR | 网络连接错误 |
| 1004 | SSL_ERROR | SSL 证书错误 |
| 1006 | TIMEOUT_ERROR | 连接超时 |

---

## 版本升级说明

详细版本变更记录请查看 [README_VERSION.md](README_VERSION.md)
