# SkyMVVMLib

**SkyMVVMLib** 是一个基于 Android Jetpack + MVVM 架构的轻量级组件化框架，封装了日常开发中高频使用的基础能力，帮助开发者快速搭建稳定、规范的 Android 应用。

---

## 目录

- [版本要求](#版本要求)
- [技术栈](#技术栈)
- [模块结构](#模块结构)
- [引入依赖](#引入依赖)
- [初始化配置](#初始化配置)
- [Base 基础组件](#base-基础组件)
  - [BaseApplication](#1-baseapplication)
  - [Activity 基类](#2-activity-基类)
  - [Fragment 基类](#3-fragment-基类)
  - [BaseViewModel](#4-baseviewmodel)
- [网络请求模块](#网络请求模块)
  - [BaseNetworkApi](#1-basenetworkapi--retrofit-构建器)
  - [BaseResponse 脱壳](#2-baseresponse-响应脱壳)
  - [apiRequest / apiRequestNoCheck](#3-apirequest--apirequestnocheck-请求扩展)
  - [ResultState 状态封装](#4-resultstate-请求状态密封类)
  - [parseState 状态解析](#5-parsestate-页面状态解析)
  - [异常处理](#6-异常处理)
  - [网络状态监听](#7-网络状态监听)
  - [DNS 回退解析](#8-skydnsparser--dns-回退解析)
  - [缓存拦截器](#9-cacheinterceptor--缓存拦截器)
  - [网络日志拦截器](#10-logginginterceptor--网络日志拦截器)
- [SkyFlow 事件总线](#skyflow-事件总线)
- [文件下载模块](#文件下载模块)
- [Callback 数据回调](#callback-数据回调)
  - [LiveData 类型封装](#1-livedata-类型封装)
  - [ObservableField 类型封装](#2-observablefield-类型封装)
- [扩展工具](#扩展工具)
  - [View 扩展](#1-view-扩展)
  - [通用扩展](#2-通用扩展)
  - [系统服务扩展](#3-系统服务扩展)
  - [日志扩展](#4-日志扩展)
  - [生命周期管理](#5-生命周期管理)
  - [ActivityMessenger 跳转工具](#6-activitymessenger-跳转工具)
- [自定义提示文案](#自定义提示文案)
- [错误码对照表](#错误码对照表)
- [版本升级说明](#版本升级说明)

---

## 版本要求

| 项目 | 最低版本 |
|------|----------|
| **minSdk** | 24（Android 7.0） |
| **compileSdk** | 36 |
| **targetSdk** | 35 |
| **Kotlin** | 2.2.10 |
| **AGP（Android Gradle Plugin）** | 9.2.1 |
| **Gradle** | 9.6.1 |
| **JDK** | 17+ |

> **注意**：宿主 App 的 `compileSdk` 必须 **≥ 36**，否则 AAR 依赖会出现元数据版本冲突。

---

## 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 架构模式 | MVVM（Model-View-ViewModel） | — |
| 依赖注入 | Hilt | 2.60 |
| 异步框架 | Kotlin Coroutines + Flow | — |
| 网络请求 | Retrofit2 + OkHttp | 3.0.0 |
| 数据解析 | Gson + GsonFactory | 2.14.0 / 10.5 |
| 生命周期 | Lifecycle + LiveData | 2.10.0 |
| 视图绑定 | ViewBinding / DataBinding | — |
| 事件总线 | SharedFlow（自封装 SkyFlow） | — |
| 日志框架 | XLog（可选，compileOnly） | 1.11.1 |

---

## 模块结构

```
com.sky.mvvm
├── base/                        # 基础组件
│   ├── BaseApplication          # Application 基类，初始化全局配置
│   ├── activity/                # Activity 基类
│   │   ├── BaseVmActivity       # 普通 MVVM Activity（layoutId 模式）
│   │   ├── BaseVmDbActivity     # DataBinding Activity
│   │   └── BaseVmVbActivity     # ViewBinding Activity
│   ├── fragment/                # Fragment 基类
│   │   ├── BaseVmFragment       # 普通 MVVM Fragment（支持懒加载）
│   │   ├── BaseVmDbFragment     # DataBinding Fragment
│   │   └── BaseVmVbFragment     # ViewBinding Fragment
│   └── viewmodel/
│       └── BaseViewModel        # ViewModel 基类，内置 Loading 状态通知
│
├── callback/                    # 数据回调封装
│   ├── databind/                # ObservableField 类型封装
│   │   ├── BooleanObservableField
│   │   ├── IntObservableField
│   │   ├── StringObservableField
│   │   ├── ByteObservableField
│   │   ├── ShortObservableField
│   │   ├── FloatObservableField
│   │   └── DoubleObservableField
│   ├── livedata/                # LiveData 类型封装
│   │   ├── BooleanLiveData
│   │   ├── IntLiveData
│   │   ├── StringLiveData
│   │   ├── ByteLiveData
│   │   ├── ShortLiveData
│   │   ├── FloatLiveData
│   │   ├── DoubleLiveData
│   │   ├── UnPeekLiveData       # 防倒灌 LiveData
│   │   └── event/
│   │       └── EventLiveData    # 事件型 LiveData（一次性消费）
│   ├── ProtectedUnPeekLiveData  # 受保护的非倒灌 LiveData（Java）
│   └── UnPeekLiveData           # 非倒灌 LiveData（Java）
│
├── ext/                         # 扩展工具
│   ├── BaseViewModelExt         # ViewModel 网络请求扩展函数
│   ├── ViewBindUtil             # 泛型反射自动创建 ViewBinding/DataBinding
│   ├── download/                # 文件下载模块
│   │   ├── DownLoadManager      # 下载管理器（支持断点续传）
│   │   ├── DownLoadManagerModule # Hilt 注入模块
│   │   ├── DownLoadPool         # 下载任务池
│   │   ├── DownLoadProgressListener # 下载进度监听接口
│   │   ├── DownLoadService      # Retrofit 下载服务
│   │   ├── DownloadResultState  # 下载状态密封类
│   │   ├── FileDownloaderExt    # 下载扩展函数
│   │   ├── FileTool             # 文件写入工具
│   │   └── ShareDownLoadUtil    # 下载进度持久化工具
│   ├── lifecycle/               # 全局生命周期监听
│   │   ├── KtxActivityManger    # Activity 栈管理
│   │   ├── KtxAppLifeObserver   # 应用前后台监听
│   │   ├── KtxHandler           # 生命周期感知 Handler
│   │   └── KtxLifeCycleCallBack # Activity 生命周期回调
│   ├── util/                    # 常用扩展
│   │   ├── CommonExt            # 通用扩展（屏幕尺寸、防重复点击等）
│   │   ├── LogExt               # 日志扩展（自动切换 XLog/Android Log）
│   │   └── SystemServiceExt     # 系统服务快捷访问扩展
│   └── view/                    # View 扩展
│       └── ViewExt              # 显示隐藏、防重复点击、转 Bitmap
│
├── flow/                        # SkyFlow 事件总线
│   ├── SkyFlow                  # 基于 SharedFlow 的事件总线
│   └── SkyFlowEventData         # 事件数据封装
│
├── network/                     # 网络模块
│   ├── BaseNetworkApi           # Retrofit 构建器基类
│   ├── BaseResponse             # 服务器响应基类（需子类实现脱壳逻辑）
│   ├── AppException             # 自定义异常封装
│   ├── ExceptionHandle          # 异常分类处理
│   ├── Error                    # 错误码枚举
│   ├── CoroutineCallAdapterFactory # Deferred 适配器
│   ├── state/
│   │   └── ResultState          # 请求结果状态密封类
│   ├── interceptor/
│   │   └── CacheInterceptor     # 缓存拦截器
│   ├── log/                     # 网络请求日志模块
│   │   ├── LoggingInterceptor   # OkHttp 日志拦截器
│   │   ├── AndroidLoggingInterceptor # Android 日志拦截器
│   │   ├── Logger               # 日志打印器
│   │   ├── LogManager           # 日志管理
│   │   ├── LogProxy             # 日志代理
│   │   └── Config               # 日志配置
│   ├── dns/
│   │   └── SkyDnsParser         # DNS 回退解析器
│   └── manager/                 # 网络状态管理
│       ├── NetState             # 网络状态数据类
│       ├── NetworkStateManager  # 网络状态管理器（单例）
│       └── NetworkStateReceive  # 网络状态广播接收器
│
├── util/                        # 工具类
│   ├── ActivityMessenger        # Activity 跳转工具（泛型参数传递）
│   ├── NetworkUtil              # 网络状态检测工具
│   ├── HttpsCerUtils            # HTTPS 证书工具（信任所有证书模式）
│   ├── CharacterHandler         # 字符处理工具
│   ├── UrlEncoderUtils          # URL 编码工具
│   └── ZipHelper                # 压缩/解压工具
│
├── SkyMVVMLib                   # 库初始化入口（单例）
├── SkyMVVMLibConfig             # 库配置类（Builder 模式）
└── res/values/strings.xml       # 可覆盖的字符串资源
```

---

## 引入依赖

在项目根目录的 `build.gradle.kts` 或 `libs.versions.toml` 中配置 Maven 仓库，然后在模块中添加依赖：

```kotlin
// build.gradle.kts
dependencies {
    implementation("com.sky.lib:SkyMVVM:最新版本号")
}
```

或通过 `libs.versions.toml`：

```toml
[versions]
skymvvm = "最新版本号"

[libraries]
skymvvm = { module = "com.sky.lib:SkyMVVM", version.ref = "skymvvm" }
```

```kotlin
// 模块 build.gradle.kts
dependencies {
    implementation(libs.skymvvm)
}
```

> **注意**：如果启用了 XLog 日志模块，需要在业务模块中单独添加 XLog 依赖：
> ```kotlin
> implementation("com.elvishew:xlog:1.11.1")
> ```

---

## 初始化配置

### SkyMVVMLib 初始化

在 `Application` 的 `onCreate()` 中调用 `SkyMVVMLib.init()` 完成初始化。**所有使用 SkyMVVMLib 的页面都会检查是否已初始化**，未初始化将抛出 `UninitializedException`。

```kotlin
class MyApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        SkyMVVMLib.init(
            SkyMVVMLibConfig.Builder(this)
                // 启用 XLog 日志模块（可选）
                .enableXLog(
                    enableXLogLib = true,
                    // xLogConfig = null,     // 传 null 使用内置默认配置（LogLevel.ALL）
                    // vararg printers        // 自定义 Printer，不传默认 AndroidPrinter
                )
                // 启用 SkyFlow 事件总线（可选）
                .enableSkyFlow(enableSkyFlowLib = true)
                // 启用 OkHttp 日志拦截器（可选）
                .enableOkHttpLogLib(
                    enableOkHttpLogLib = true,
                    // okHttpLogConfig = null  // 传 null 使用内置默认配置
                )
                .build()
        )
    }
}
```

### SkyMVVMLibConfig 配置项说明

| 方法 | 参数 | 说明 |
|------|------|------|
| `enableXLog()` | `enableXLogLib: Boolean`<br>`xLogConfig: LogConfiguration?`<br>`vararg printers: Printer` | 启用 XLog 日志模块。启用后库内日志将自动通过 XLog 输出；未启用则回退到 Android Log |
| `enableSkyFlow()` | `enableSkyFlowLib: Boolean` | 启用 SkyFlow 事件总线模块。未启用时调用 `SkyFlow.with()` 会抛出异常 |
| `enableOkHttpLogLib()` | `enableOkHttpLogLib: Boolean`<br>`okHttpLogConfig: Interceptor?` | 启用 OkHttp 日志拦截器。传 `null` 则使用内置默认配置 |

---

## Base 基础组件

### 1. BaseApplication

`BaseApplication` 是所有 Application 的基类，继承自 `Application`。它在 `onCreate()` 中自动完成以下初始化：

- **网络状态广播注册**：监听网络变化，通过 `NetworkStateManager` 分发
- **Activity 生命周期回调注册**：通过 `KtxLifeCycleCallBack` 管理 Activity 栈
- **应用前后台监听**：通过 `ProcessLifecycleOwner` + `KtxAppLifeObserver` 监听

```kotlin
// 你的 Application 只需继承 BaseApplication
class MyApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()  // 必须调用 super
        // 在这里进行 SkyMVVMLib.init() 和其他初始化
    }
}
```

**全局 Application 实例**：通过 `BaseApplication.app` 可获取全局 Application 引用。

### 2. Activity 基类

库提供三种 Activity 基类，根据视图绑定方式选择：

#### BaseVmActivity（普通布局）

使用 `layoutId()` 返回布局资源 ID：

```kotlin
class MainActivity : BaseVmActivity<MainViewModel>() {
    override fun layoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        // 初始化视图，mViewModel 已自动创建
    }

    override fun showLoading(message: String) {
        // 显示加载框（需自行实现）
    }

    override fun dismissLoading() {
        // 隐藏加载框（需自行实现）
    }

    override fun createObserver() {
        // 观察 LiveData 数据变化
    }

    // 可选：监听网络状态变化
    override fun onNetworkStateChanged(netState: NetState) {
        // netState.isSuccess 表示当前是否有网络
    }
}
```

#### BaseVmVbActivity（ViewBinding）

自动通过泛型反射创建 ViewBinding 实例，通过 `mViewBind` 访问：

```kotlin
class MainActivity : BaseVmVbActivity<MainViewModel, ActivityMainBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.tvTitle.text = "Hello SkyMVVM"
    }

    override fun showLoading(message: String) { /* ... */ }
    override fun dismissLoading() { /* ... */ }
    override fun createObserver() { /* ... */ }
}
```

#### BaseVmDbActivity（DataBinding）

自动通过泛型反射创建 ViewDataBinding 实例，通过 `mDatabind` 访问：

```kotlin
class MainActivity : BaseVmDbActivity<MainViewModel, ActivityMainBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.lifecycleOwner = this
    }

    override fun showLoading(message: String) { /* ... */ }
    override fun dismissLoading() { /* ... */ }
    override fun createObserver() { /* ... */ }
}
```

**Activity 基类通用特性**：

- 自动通过泛型创建 `mViewModel` 实例
- 自动注册 `loadingChange` 监听，实现 Loading 弹窗联动
- 自动监听网络状态变化
- 提供 `addLoadingObserve()` 方法为额外的 ViewModel 注册 Loading 回调

### 3. Fragment 基类

同样提供三种 Fragment 基类：

#### BaseVmFragment（普通布局）

```kotlin
class HomeFragment : BaseVmFragment<HomeViewModel>() {
    override fun layoutId() = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
        // 初始化视图
    }

    override fun lazyLoadData() {
        // 首次可见时懒加载数据
    }

    override fun createObserver() {
        // 观察数据变化
    }

    override fun showLoading(message: String) { /* ... */ }
    override fun dismissLoading() { /* ... */ }

    // 可选：自定义懒加载延迟时间（默认 300ms）
    override fun lazyLoadTime(): Long = 300

    // 可选：onCreateView 后执行（适合不依赖视图的初始化）
    override fun initData() { }
}
```

#### BaseVmDbFragment（DataBinding）

```kotlin
class HomeFragment : BaseVmDbFragment<HomeViewModel, FragmentHomeBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
    }
    override fun lazyLoadData() { /* ... */ }
    override fun createObserver() { /* ... */ }
    override fun showLoading(message: String) { /* ... */ }
    override fun dismissLoading() { /* ... */ }
}
```

#### BaseVmVbFragment（ViewBinding）

```kotlin
class HomeFragment : BaseVmVbFragment<HomeViewModel, FragmentHomeBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.tvTitle.text = "Hello"
    }
    override fun lazyLoadData() { /* ... */ }
    override fun createObserver() { /* ... */ }
    override fun showLoading(message: String) { /* ... */ }
    override fun dismissLoading() { /* ... */ }
}
```

**Fragment 基类特性**：

- **懒加载**：内置首次可见懒加载机制，延迟默认 300ms（可通过 `lazyLoadTime()` 自定义）
- **Activity ViewModel 共享**：通过 `obtainActivityViewModel()` 获取宿主 Activity 的 ViewModel
- **网络状态监听**：懒加载后才开始监听网络变化，避免首次订阅数据错乱
- **ViewBinding/DataBinding 生命周期安全**：`onDestroyView` 时自动置空 binding 引用

### 4. BaseViewModel

所有 ViewModel 的基类，继承自 `androidx.lifecycle.ViewModel`。

```kotlin
class MainViewModel @Inject constructor() : BaseViewModel() {
    // loadingChange 已内置，用于通知 UI 显示/隐藏加载框
    // loadingChange.showDialog  → 显示加载框
    // loadingChange.dismissDialog → 隐藏加载框
}
```

**内置功能**：

- `loadingChange.showDialog`：`EventLiveData<String>`，通知 UI 显示加载框，值为提示消息
- `loadingChange.dismissDialog`：`EventLiveData<Boolean>`，通知 UI 隐藏加载框
- 初始化时自动检查 `SkyMVVMLib` 是否已初始化

---

## 网络请求模块

### 1. BaseNetworkApi — Retrofit 构建器

继承 `BaseNetworkApi`，实现 `setHttpClientBuilder()` 和 `setRetrofitBuilder()` 来构建 API 服务：

```kotlin
@Singleton
class ApiService @Inject constructor() : BaseNetworkApi() {

    private val api: ApiInterface by lazy {
        getApi(ApiInterface::class.java, "https://api.example.com/", false)
    }

    // 第一个参数：true 使用忽略证书的 HTTPS 模式，false 使用普通 HTTP
    fun getArticles(): BaseResponse<ArticleList> = api.getArticles()

    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        // 添加拦截器、Cookie 等
        return builder
    }

    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        // 添加 GsonConverterFactory 等
        return builder.addConverterFactory(GsonConverterFactory.create())
    }
}
```

**`getApi()` 方法参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| `serviceClass` | `Class<T>` | API 接口类 |
| `baseUrl` | `String` | 基础 URL |
| `type` | `Boolean` | `true` = HTTPS 忽略证书模式，`false` = 普通 HTTP 模式 |

### 2. BaseResponse — 响应脱壳

继承 `BaseResponse<T>` 并实现抽象方法，框架会自动判断请求是否成功并解包数据：

```kotlin
data class ApiResponse<T>(
    val code: Int,
    val msg: String,
    val data: T?
) : BaseResponse<T>() {
    override fun isSucces(): Boolean = code == 0
    override fun getResponseData(): T = data!!
    override fun getResponseCode(): Int = code
    override fun getResponseMsg(): String = msg
}
```

### 3. apiRequest / apiRequestNoCheck — 请求扩展

库为 `BaseViewModel` 提供了多种网络请求扩展函数：

#### apiRequest（带 BaseResponse 脱壳）

```kotlin
// 方式一：通过 ResultState 回调
fun loadData(context: Context) {
    apiRequest(
        context = context,
        block = { apiService.getArticles() },     // suspend 请求体
        resultState = articleListResult,           // MutableLiveData<ResultState<T>>
        isShowDialog = true,                       // 是否显示加载框
        loadingMessage = "加载中..."                // 加载框提示文字
    )
}

// 方式二：通过 success/error 回调
fun loadData(context: Context) {
    apiRequest(
        context = context,
        block = { apiService.getArticles() },
        success = { data -> /* 成功，data 为解包后的业务数据 */ },
        error = { exception -> /* 失败，exception 为 AppException */ },
        isShowDialog = true,
        loadingMessage = "加载中..."
    )
}
```

#### apiRequestNoCheck（不脱壳，直接返回原始数据）

```kotlin
fun loadData(context: Context) {
    apiRequestNoCheck(
        context = context,
        block = { apiService.getRawData() },       // 返回值不是 BaseResponse
        resultState = resultLiveData,
        isShowDialog = false
    )
}
```

#### launch（IO 线程协程）

```kotlin
fun doHeavyWork(context: Context) {
    launch(
        block = { /* IO 线程耗时操作 */ "result" },
        success = { result -> /* 主线程处理结果 */ },
        error = { throwable -> /* 错误处理 */ }
    )
}
```

### 4. ResultState — 请求状态密封类

```kotlin
sealed class ResultState<out T> {
    data class Loading(val loadingMessage: String) : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val error: AppException) : ResultState<Nothing>()
}
```

### 5. parseState — 页面状态解析

在 Activity 或 Fragment 中观察 `ResultState`，使用 `parseState` 统一处理：

```kotlin
// 在 Activity 中
mViewModel.articleList.observe(this) { resultState ->
    parseState(resultState,
        onSuccess = { data ->
            // 请求成功，data 为解包后的业务数据
            adapter.setList(data)
        },
        onError = { exception ->
            // 请求失败
            Toast.makeText(this, exception.errorMsg, Toast.LENGTH_SHORT).show()
        },
        onLoading = {
            // 可选：加载中回调
        }
    )
}

// 在 Fragment 中用法相同
mViewModel.articleList.observe(viewLifecycleOwner) { resultState ->
    parseState(resultState,
        onSuccess = { data -> /* ... */ },
        onError = { exception -> /* ... */ }
    )
}
```

### 6. 异常处理

`ExceptionHandle` 会自动将各类异常转换为 `AppException`：

| 异常类型 | 错误码 | 说明 |
|---------|--------|------|
| `HttpException` | 1002 | HTTP 错误 |
| `JsonParseException` / `JSONException` | 1001 | 数据解析错误 |
| `ConnectException` | 1002 | 网络连接错误 |
| `SSLException` | 1004 | SSL 证书错误 |
| `ConnectTimeoutException` / `SocketTimeoutException` | 1006 | 连接超时 |
| `UnknownHostException` | 1006 | 未知主机 |
| 其他 | 1000 | 未知错误 |

`AppException` 属性：

```kotlin
exception.errCode    // Int - 错误码
exception.errorMsg   // String - 用户可读的错误描述
exception.errorLog   // String? - 错误日志
exception.throwable  // Throwable? - 原始异常
```

### 7. 网络状态监听

`BaseApplication` 自动注册 `NetworkStateReceive` 广播接收器，通过 `NetworkStateManager` 单例分发网络状态变化。

**在 Activity/Fragment 中监听**（基类已自动注册，只需重写回调）：

```kotlin
override fun onNetworkStateChanged(netState: NetState) {
    if (netState.isSuccess) {
        // 网络恢复
    } else {
        // 网络断开
    }
}
```

**手动获取网络信息**：

```kotlin
NetworkUtil.isNetworkAvailable(context)  // 网络是否可用
NetworkUtil.isWifi(context)              // 是否 WiFi
NetworkUtil.is3G(context)                // 是否 3G/4G
NetworkUtil.localIpAddress               // 本地 IP 地址
NetworkUtil.getNetState(context)          // 详细网络状态码
```

### 8. SkyDnsParser — DNS 回退解析

在 VPN 环境下 DNS 解析可能失败，`SkyDnsParser` 实现了 `okhttp3.Dns` 接口，优先使用系统 DNS，失败后通过 UDP 直连公共 DNS 服务器（Google DNS 8.8.8.8 / 114.114.114.114）解析：

```kotlin
// 在 OkHttpClient 中使用
val okHttpClient = OkHttpClient.Builder()
    .dns(SkyDnsParser())
    .build()
```

也可自定义备用 DNS 服务器：

```kotlin
SkyDnsParser(
    fallbackServers = listOf(
        InetAddress.getByName("8.8.8.8"),
        InetAddress.getByName("1.1.1.1")
    )
)
```

### 9. CacheInterceptor — 缓存拦截器

根据网络状态自动切换缓存策略：无网络时强制读缓存，有网络时设置缓存有效期：

```kotlin
// 默认缓存 7 天
val cacheInterceptor = CacheInterceptor(day = 7)

// 添加到 OkHttpClient
OkHttpClient.Builder()
    .addInterceptor(cacheInterceptor)
    .build()
```

### 10. LoggingInterceptor — 网络日志拦截器

可配置的 OkHttp 日志拦截器，支持请求/响应日志分离打印：

```kotlin
val loggingInterceptor = LoggingInterceptor.Builder()
    .loggable(true)               // 开启日志
    .androidPlatform()            // 使用 Android 平台日志
    .request()                    // 打印请求日志
    .requestTag("Request")        // 请求日志 Tag
    .response()                   // 打印响应日志
    .responseTag("Response")      // 响应日志 Tag
    .hideVerticalLine()           // 隐藏竖线边框，便于拷贝
    .tag("MyApp")                 // 统一 Tag
    .logLevel(LoggingInterceptor.LogLevel.INFO) // 日志级别
    .excludePath("/upload")       // 排除指定路径不打印
    .build()
```

> 初始化时如果启用了 `enableOkHttpLogLib(true)` 且未传自定义配置，库会自动创建默认配置的拦截器，可通过 `SkyMVVMLib.getConfig()?.okHttpLogConfig` 获取。

---

## SkyFlow 事件总线

基于 Kotlin `SharedFlow` 实现的事件总线，替代传统的 EventBus，支持**普通事件**和**粘性事件**，自动绑定生命周期。

> **前提**：必须在初始化时启用 `enableSkyFlow(true)`，否则调用时会抛出 `UninitializedException`。

### 发送事件

```kotlin
// 普通事件（挂起函数）
SkyFlow.with<String>("event_key").post(scope, "Hello SkyFlow")

// 粘性事件（后注册的订阅者也能收到最新一次事件）
SkyFlow.withStick<Int>("sticky_key").post(scope, 100)

// 使用 SkyFlowEventData 传递键值对
SkyFlow.with<SkyFlowEventData>("app_event").post(scope,
    SkyFlowEventData(eventKey = "login_expired", eventValue = "token")
)
```

### 接收事件

```kotlin
// 在 Activity 中注册（自动绑定生命周期）
SkyFlow.with<String>("event_key").register(
    lifecycleOwner = this,            // 自动在 onDestroy 时取消订阅
    filter = { it.isNotEmpty() },     // 可选：事件过滤条件
    action = { event ->
        // 处理事件
    }
)

// 在 Fragment 中注册
SkyFlow.with<String>("event_key").register(
    lifecycleOwner = viewLifecycleOwner,
    action = { event -> /* ... */ }
)

// 使用自定义 CoroutineScope（无生命周期绑定时）
SkyFlow.with<String>("event_key").register(
    scope = myCoroutineScope,
    action = { event -> /* ... */ }
)
```

### 清理资源

```kotlin
// 清理无订阅者的 Flow 实例（建议在进入后台时调用）
SkyFlow.clearUnusedFlow()

// 手动销毁指定 Flow
SkyFlow.with<String>("event_key").destroy()
```

### SkyFlowEventData

内置的事件数据封装类，用于传递简单的键值对事件：

```kotlin
data class SkyFlowEventData(
    var eventKey: String,
    var eventValue: String
)
```

---

## 文件下载模块

内置文件下载管理器，支持**断点续传**、**暂停**、**取消**，通过 Hilt 注入使用。

### 注入 DownLoadManager

```kotlin
// DownLoadManagerModule 已自动通过 Hilt 提供单例
@Inject lateinit var downloadManager: DownLoadManager
```

### 开始下载

```kotlin
// 在 ViewModel 中
viewModelScope.launch {
    downloadManager.downLoad(
        tag = "apk_update",              // 下载标识（唯一）
        url = "https://example.com/app.apk",
        savePath = context.cacheDir.absolutePath,
        saveName = "app.apk",
        reDownload = false,              // 文件已存在时是否重新下载
        whetherHttps = false,            // 是否使用忽略证书的 HTTPS 模式
        loadListener = downLoadExt(context, downloadState)
    )
}
```

### 监听下载状态

```kotlin
val downloadState = MutableLiveData<DownloadResultState>()

downloadState.observe(this) { state ->
    when (state) {
        is DownloadResultState.Pending -> { /* 等待下载 */ }
        is DownloadResultState.Progress -> {
            // state.progress  进度百分比 (0-100)
            // state.soFarBytes 已下载字节
            // state.totalBytes 总字节数
        }
        is DownloadResultState.Success -> {
            // state.filePath 文件路径
            // state.totalBytes 文件大小
        }
        is DownloadResultState.Pause -> { /* 下载暂停 */ }
        is DownloadResultState.Error -> {
            // state.errorMsg 错误信息
        }
    }
}
```

### 暂停/取消下载

```kotlin
downloadManager.pause("apk_update")      // 暂停
downloadManager.cancel("apk_update")     // 取消（删除已下载文件）
downloadManager.doDownLoadCancelAll()    // 取消所有下载
downloadManager.doDownLoadPauseAll()     // 暂停所有下载
```

### 自定义下载监听

```kotlin
val listener = object : OnDownLoadListener {
    override fun onDownLoadPrepare(key: String) { /* 准备开始 */ }
    override fun onDownLoadError(key: String, throwable: Throwable) { /* 失败 */ }
    override fun onDownLoadSuccess(key: String, path: String, size: Long) { /* 成功 */ }
    override fun onDownLoadPause(key: String) { /* 暂停 */ }
    override fun onUpdate(key: String, progress: Int, read: Long, count: Long, done: Boolean) {
        // 进度更新
    }
}
```

或使用 `downLoadExt()` 扩展函数将下载状态自动转为 `DownloadResultState`：

```kotlin
val listener = downLoadExt(context, downloadState)
```

---

## Callback 数据回调

### 1. LiveData 类型封装

提供带默认值的 `MutableLiveData` 子类，避免取值时判空：

| 类名 | 默认值 | 说明 |
|------|--------|------|
| `BooleanLiveData` | `false` | Boolean 类型 |
| `IntLiveData` | `0` | Int 类型 |
| `StringLiveData` | `""` | String 类型 |
| `ByteLiveData` | `0` | Byte 类型 |
| `ShortLiveData` | `0` | Short 类型 |
| `FloatLiveData` | `0f` | Float 类型 |
| `DoubleLiveData` | `0.0` | Double 类型 |

```kotlin
// 使用示例
val isVisible = BooleanLiveData()  // 默认 false，getValue() 不会返回 null
val count = IntLiveData()          // 默认 0
val name = StringLiveData()        // 默认 ""
```

### 2. ObservableField 类型封装

提供带默认值的 `ObservableField` 子类，用于 DataBinding：

| 类名 | 默认值 |
|------|--------|
| `BooleanObservableField` | `false` |
| `IntObservableField` | `0` |
| `StringObservableField` | `""` |
| `ByteObservableField` | `0` |
| `ShortObservableField` | `0` |
| `FloatObservableField` | `0f` |
| `DoubleObservableField` | `0.0` |

```kotlin
// 在 ViewModel 中使用（DataBinding 场景）
val userName = StringObservableField("Henry")
val age = IntObservableField(25)
val isVip = BooleanObservableField(false)
```

### 3. UnPeekLiveData / EventLiveData

**UnPeekLiveData**：防止数据倒灌的 LiveData，新订阅者不会收到订阅前的旧数据，适用于共享 ViewModel 场景。

**EventLiveData**：继承自 `UnPeekLiveData`，用于一次性事件消费（如弹窗、Toast、导航等），避免配置变更时重复触发。

```kotlin
// 在 ViewModel 中
val toastEvent = EventLiveData<String>()

// 在 Activity/Fragment 中观察
mViewModel.toastEvent.observe(this) { message ->
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
```

---

## 扩展工具

### 1. View 扩展

```kotlin
// 显示/隐藏
view.visible()                    // View.VISIBLE
view.gone()                       // View.GONE
view.invisible()                  // View.INVISIBLE
view.visibleOrGone(true)          // 条件显示
view.visibleOrInvisible(false)    // 条件隐藏

// 防重复点击（默认 500ms 间隔）
view.clickNoRepeat(interval = 500) { view ->
    // 点击事件
}

// 批量设置点击事件
setOnclick(btn1, btn2, btn3) { view ->
    // 点击事件
}

// 批量设置防重复点击
setDebouncedClickListener(btn1, btn2, interval = 500) { view ->
    // 点击事件
}
```

### 2. 通用扩展

```kotlin
// 屏幕尺寸
context.screenWidth    // 屏幕宽度（px）
context.screenHeight   // 屏幕高度（px）

// dp/px 转换
context.dp2px(16)      // dp → px
context.px2dp(108)     // px → dp
view.dp2px(16)         // View 扩展也可以

// 判空处理
value.notNull(
    notNullAction = { /* 不为空时执行 */ },
    nullAction = { /* 为空时执行 */ }
)

// 复制到剪贴板
context.copyToClipboard("文本内容", "标签")

// 检查无障碍服务
context.checkAccessibilityServiceEnabled("com.example.MyService")
```

### 3. 系统服务扩展

通过属性访问快捷获取系统服务，无需手动 `getSystemService()`：

```kotlin
context.windowManager          // WindowManager
context.clipboardManager       // ClipboardManager
context.layoutInflater         // LayoutInflater
context.activityManager        // ActivityManager
context.powerManager           // PowerManager
context.alarmManager           // AlarmManager
context.notificationManager    // NotificationManager
context.keyguardManager        // KeyguardManager
context.locationManager        // LocationManager
context.connectivityManager    // ConnectivityManager
context.wifiManager            // WifiManager
context.audioManager           // AudioManager
context.telephonyManager       // TelephonyManager
context.sensorManager          // SensorManager
context.inputMethodManager     // InputMethodManager
context.downloadManager        // DownloadManager
context.batteryManager         // BatteryManager
context.vibrator               // Vibrator
// ... 等等
```

### 4. 日志扩展

库内置日志扩展函数，会根据 `SkyMVVMLibConfig` 配置自动切换 XLog 或 Android Log 输出：

```kotlin
"这是一条调试日志".logD()              // Debug 级别，默认 Tag "SkyMVVM"
"这是一条信息日志".logI(tag = "MyTag")  // Info 级别，自定义 Tag
"这是一条警告日志".logW()              // Warn 级别
"这是一条错误日志".logE()              // Error 级别
"这是一条详细日志".logV()              // Verbose 级别
```

- 当 `xLogLibEnabled = true` 时，通过 XLog 输出
- 当 `xLogLibEnabled = false` 时，回退到 `android.util.Log` 输出
- 可通过全局变量 `enableSkyMVVMLog = false` 关闭所有库日志

### 5. 生命周期管理

#### KtxActivityManger — Activity 栈管理

```kotlin
KtxActivityManger.currentActivity         // 获取当前栈顶 Activity
KtxActivityManger.finishCurrentActivity() // 关闭当前 Activity
KtxActivityManger.finishActivity(activity)// 关闭指定 Activity
KtxActivityManger.finishActivity(clazz)   // 按类名关闭 Activity
KtxActivityManger.finishAllActivity()     // 关闭所有 Activity
```

#### KtxAppLifeObserver — 应用前后台监听

```kotlin
// 监听应用是否在前台
KtxAppLifeObserver.isForeground.observe(owner) { isForeground ->
    if (isForeground) {
        // 应用进入前台
    } else {
        // 应用进入后台
    }
}
```

#### KtxHandler — 生命周期感知 Handler

自动在 `LifecycleOwner` 销毁时清理消息队列：

```kotlin
val handler = KtxHandler(lifecycleOwner, callback)
```

### 6. ActivityMessenger 跳转工具

提供泛型参数传递的 Activity 跳转工具，支持 Activity、Fragment、Context 发起：

#### 启动 Activity

```kotlin
// 不携带参数
ActivityMessenger.startActivity<TargetActivity>(this)

// 携带参数
ActivityMessenger.startActivity<TargetActivity>(this,
    "name" to "Henry",
    "age" to 25,
    "isVip" to true
)

// 携带 flags
ActivityMessenger.startActivity<TargetActivity>(this,
    flags = Intent.FLAG_ACTIVITY_NEW_TASK
)

// 从 Fragment 启动
ActivityMessenger.startActivity<TargetActivity>(this, "key" to "value")

// 从 Context 启动（如 Adapter 中）
ActivityMessenger.startActivity<TargetActivity>(context, "key" to "value")
```

#### startActivityForResult

```kotlin
ActivityMessenger.startActivityForResult<TargetActivity>(this) { result ->
    if (result != null) {
        val name: String? = result.get("name")
    }
}
```

#### 返回数据并关闭页面

```kotlin
ActivityMessenger.finish(this, "result_key" to "result_value")
```

#### 获取 Intent 参数

```kotlin
// Activity 中
private var name: String? by extraAct("name")
private var age: Int by extraAct("age", 0)  // 带默认值

// Fragment 中
private var name: String? by extraFrag("name")
private var age: Int by extraFrag("age", 0)
```

#### Intent 扩展

```kotlin
// 批量 put 参数
intent.putExtras("key1" to "value1", "key2" to 123)

// 泛型获取参数（无需区分类型）
val name: String? = intent.get("name")
val age: Int? = intent.get("age")
```

---

## 自定义提示文案

在宿主 App 的 `res/values/strings.xml` 中覆盖以下资源即可自定义提示文案：

```xml
<resources>
    <!-- 异常提示 -->
    <string name="sky_mvvmlib_exception_unknown">请求失败，请稍后再试</string>
    <string name="sky_mvvmlib_exception_parse_error">解析错误，请稍后再试</string>
    <string name="sky_mvvmlib_exception_network_error">网络连接错误，请稍后重试</string>
    <string name="sky_mvvmlib_exception_ssl_error">证书出错，请稍后再试</string>
    <string name="sky_mvvmlib_exception_timeout_error">网络连接超时，请稍后重试</string>

    <!-- 下载提示 -->
    <string name="sky_mvvmlib_download_error">下载错误</string>

    <!-- 加载提示 -->
    <string name="sky_mvvmlib_loading_message">请求网络中</string>
</resources>
```

---

## 错误码对照表

| 错误码 | 枚举 | 说明 |
|--------|------|------|
| 1000 | `Error.UNKNOWN` | 未知错误 |
| 1001 | `Error.PARSE_ERROR` | 数据解析错误 |
| 1002 | `Error.NETWORK_ERROR` | 网络连接错误 |
| 1004 | `Error.SSL_ERROR` | SSL 证书错误 |
| 1006 | `Error.TIMEOUT_ERROR` | 连接超时 |

---

## 版本升级说明

详细版本变更记录请查看 [README_VERSION.md](README_VERSION.md)
