一、项目简介
总结出成熟的模版代码，可以直接用于项目开发[README.md](README.md)



让我组织一个清晰的对比：

1. **Flow** - 冷流，每次收集都会重新执行代码块
2. **StateFlow** - 热流，有初始值，会重放最新值给新订阅者，需要初始值
3. **SharedFlow** - 热流，可配置 replay 缓存，不需要初始值

使用场景：
- Flow：一次性数据流、耗时操作（网络请求、数据库查询）
- StateFlow：UI 状态管理（MVVM 中的状态）
- SharedFlow：事件/通知（Toast、导航、副作用）

让我给出详细的对比表格和代码示例。
```
## Flow、StateFlow、SharedFlow 对比

### 核心区别

| 特性 | `Flow` | `StateFlow` | `SharedFlow` |
|------|--------|-------------|--------------|
| **流类型** | 冷流 (Cold) | 热流 (Hot) | 热流 (Hot) |
| **有无状态** | 无 | 有（始终持有一个值） | 无（可配置 replay 缓存） |
| **是否需要初始值** | 不需要 | **必须** | 不需要 |
| **新订阅者行为** | 重新执行上游代码 | 立即收到**最新值** | 收到配置的 replay 缓存 |
| **适用场景** | 数据流、耗时操作 | UI 状态绑定 | 一次性事件、副作用 |

---

### 1. Flow（冷流）

每次有新的收集者（`collect`），上游代码都会**重新执行**。

```kotlin
// 每次 collect，getData() 都会重新调用
val dataFlow: Flow<List<Item>> = flow {
    emit(repository.getData()) 
}

// 使用
viewModelScope.launch { dataFlow.collect { ... } } // 触发一次网络请求
viewModelScope.launch { dataFlow.collect { ... } } // 又触发一次网络请求
```


**使用场景**：网络请求、数据库查询、文件读写等**一次性数据获取**。

---

### 2. StateFlow（状态流）

- 必须有初始值
- 始终缓存**最新值**
- 新订阅者立即收到当前值
- 通常配合 `asStateFlow()` 使用，对外暴露只读接口

```kotlin
private val _uiState = MutableStateFlow(UiState.Loading) // 必须有初始值
val uiState: StateFlow<UiState> = _uiState.asStateFlow()

// 更新状态
_uiState.value = UiState.Success(data)

// UI 收集（Activity/Fragment）
lifecycleScope.launch {
    viewModel.uiState.collect { state ->
        when (state) { ... }
    }
}
```


**使用场景**：MVVM 中的 **UI 状态管理**（Loading/Success/Error）、进度条、开关状态等**需要持续反映当前状态**的场景。

---

### 3. SharedFlow（共享流）

- 无初始值
- 通过 `replay` 参数控制给新订阅者重放几条历史数据（默认 0）
- 不会自动去重（发射相同值也会通知订阅者）

```kotlin
// 配置：缓存容量 0，超出时丢弃最老的事件
private val _events = MutableSharedFlow<UiEvent>(replay = 0, extraBufferCapacity = 1)
val events: SharedFlow<UiEvent> = _events.asSharedFlow()

// 发送事件
_events.tryEmit(UiEvent.ShowToast("保存成功"))

// UI 收集
lifecycleScope.launch {
    viewModel.events.collect { event ->
        when (event) { ... }
    }
}
```


**使用场景**：Toast、Snackbar、导航跳转、页面刷新指令等**一次性事件/副作用**。  
**注意**：不要用 `StateFlow` 做事件通知，因为屏幕旋转后配置变更会导致事件被重放，造成 Toast 重复弹出。

---

### 选型速查

| 需求 | 选择 |
|------|------|
| 请求网络/数据库，获取数据 | `Flow` |
| 页面状态（Loading/Content/Error） | `StateFlow` |
| 点击事件、Toast、导航 | `SharedFlow` |
| 下载进度、播放进度 | `StateFlow` |
| 需要多个订阅者共享同一数据源 | `StateFlow` / `SharedFlow` |

---

### 项目中的典型搭配

```kotlin
class MyViewModel : ViewModel() {
    // 页面状态
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // 一次性事件
    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    // 数据流
    fun loadData(): Flow<Result<List<Item>>> = repository.getItems()
}
```


**原则**：**状态用 `StateFlow`，事件用 `SharedFlow`，数据获取用 `Flow`**。




### 模块优化建议
深度思考
```用户要求重新读取 download 模块的文件，并给出优化建议。模块结构已经有了变化，新增了 `di` 目录，包含 `AppSdkModule.kt`、`DataModule.kt`、`DatabaseModule.kt`。

我需要读取这些新增/修改的文件，以及之前可能修改过的关键文件，然后给出针对性的优化建议。

让我读取：
1. `di/AppSdkModule.kt`
2. `di/DataModule.kt`
3. `di/DatabaseModule.kt`
4. `data/repository/DownloadRepository.kt`（可能已修改）
5. `data/repository/AppRepository.kt`（可能已修改）
6. `ui/AppViewModel.kt`（可能已修改）
7. `domain/DownloadAppUserCase.kt`（可能已修改）
8. `ui/AppFragment.kt`
9. `data/db/AppDatabase.kt`
10. `data/sdk/RealAppCenterSdk.kt`
```
**已查看文件** *AppSdkModule.kt*

**已查看文件** *DataModule.kt*

**已查看文件** *DatabaseModule.kt*

**已查看文件** *DownloadRepository.kt*

**已查看文件** *AppRepository.kt*

**已查看文件** *AppViewModel.kt*

**已查看文件** *DownloadAppUserCase.kt*

**已查看文件** *AppFragment.kt*
深度思考
```用户要求对修改后的 download 模块给出优化建议。让我仔细阅读所有文件，分析可以改进的地方。

当前架构：
- `AppFragment` (`@AndroidEntryPoint`) → `AppViewModel` (`@HiltViewModel` + `@Inject`) → `DownloadAppUserCase` (`@Inject`) → `AppRepository` / `DownloadRepository`
- DI: `DataModule` (`class` 提供 `AppApi`), `DatabaseModule` (`class` 提供 `AppDatabase`, `DownloadDao`), `AppSdkModule` (`interface` + `@Binds` 提供 `AppCenterSdk`)

发现的问题和优化点：

### 1. Hilt Module 声明方式不统一
- `DataModule` 是 `class`，应该用 `object`
- `DatabaseModule` 是 `class`，应该用 `object`
- `AppSdkModule` 是 `interface`（用 `@Binds`），这是正确的

在 Kotlin 中，如果 Module 只有 `@Provides` 方法且不需要抽象方法，应该用 `object` 而不是 `class`。这样 Hilt 不需要实例化该类。

### 2. `AppSdkModule` 中方法命名问题
```kotlin
@Binds
fun bindsCarRepository(repository: RealAppCenterSdk): AppCenterSdk
```
方法名 `bindsCarRepository` 应该改为 `bindsAppCenterSdk` 或 `bindAppCenterSdk`，因为绑定的是 AppCenterSdk 而不是 CarRepository。

### 3. `DatabaseModule` 中方法命名问题
```kotlin
fun provideCarDao(appDatabase: AppDatabase): DownloadDao
```
方法名 `provideCarDao` 应该改为 `provideDownloadDao`。

### 4. `DownloadRepository` 中的协程作用域
```kotlin
private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
```
`DownloadRepository` 是 `@Singleton`，但它的 `scope` 是内部创建的。如果 Hilt 提供了 `ApplicationScope` 或 `CoroutineScope`，应该注入进来。不过当前这样做也还行。

更严重的问题是：`DownloadRepository` 是一个单例，内部自己创建 `CoroutineScope`。如果应用被杀死后进程重启，`DownloadRepository` 会重新创建，但之前注册的 SDK 回调会丢失。不过这不是当前要优化的重点。

### 5. `DownloadRepository.restore()` 中的问题
```kotlin
map.forEach { (packageName, state) ->
    sdk.queryDownloadStatus(packageName) { remoteState ->
        if (remoteState != null) {
            restoreState = remoteState
            map[packageName] = restoreState
            updateToDB(restoreState)
        }
    }
    sdk.registerCallback(...)
}
```

这里 `sdk.queryDownloadStatus` 是异步回调，但 `map.forEach` 会同步遍历完所有项后才执行 `_stateFlow.emit(map)`。这意味着：
- `queryDownloadStatus` 的回调可能在 `_stateFlow.emit(map)` 之后才执行
- 所以 `map[packageName] = restoreState` 可能发生在 `emit` 之后
- 这是一个并发/时序 bug

应该使用 `suspend` 版本的 `queryDownloadStatus` 或者使用 `async/await` 等待所有查询完成后再 `emit`。

### 6. `DownloadRepository` 的 `downloadCallback` 方法
```kotlin
fun downloadCallback(downloadApps: ((Map<String, DownloadState>) -> Unit)?) {
    callbackJob?.cancel()
    callbackJob = scope.launch {
        stateFlow.collect {
            downloadApps?.invoke(it)
        }
    }
}
```
这里存在一个问题：如果 `downloadApps` 为 null，仍然会启动一个协程不断收集 `stateFlow`。而且外部调用 `downloadCallback(null)` 时，旧的 callback 会被取消，但新的协程还是会启动。

优化：
```kotlin
fun setDownloadListener(listener: ((Map<String, DownloadState>) -> Unit)?) {
    callbackJob?.cancel()
    listener ?: return
    callbackJob = scope.launch {
        stateFlow.collect { listener(it) }
    }
}
```

### 7. `DownloadRepository` 的 `saveToDB` 节流逻辑
用户已经改成了 `lastSaveTimeMap`，这是好的。

但 `updateToDB` 方法调用了 `scope.launch { saveToDB(state) }`，而 `update()` 方法已经在 `scope.launch` 中了，所以 `saveToDB` 会被调度到另一个协程。这没问题，但 `updateToDB` 在 `restore()` 中被调用时，`restore()` 本身也在 `scope.launch` 中。所以 `updateToDB` 会启动一个新的协程去保存，而父协程不会等待它完成。这是可以接受的。

### 8. `DownloadAppUserCase` 的职责边界
`DownloadAppUserCase` 中有很多方法直接暴露 `DownloadRepository` 的能力：
- `downloadCallback`
- `getDownloadStateLiveData`
- `registerDetailDownloadListener`
- `isSupportDownload`

这些方法看起来更像是给 UI 直接调用的，但 `DownloadAppUserCase` 的定位是 UseCase。如果 `AppViewModel` 需要这些能力，可以直接通过 `DownloadRepository` 暴露，或者 UseCase 提供更高级别的封装。

不过这是一个设计选择，不一定算问题。

### 9. `DownloadAppUserCase.getShowAppsFlow` 中 `combine` 的问题
```kotlin
return combine(
    appRepository.loadTopApps(),
    downloadRepository.stateFlow
) { result, downloadMap -> ... }
    .stateIn(scope, SharingStarted.WhileSubscribed(5000), AppUiState.Loading)
```

`appRepository.loadTopApps()` 是一个冷流，每次有新的订阅者都会重新触发网络请求。但这里通过 `combine` 和 `stateIn` 转成了热流 `StateFlow`。

问题是：`combine` 会同时订阅两个 Flow。如果 `loadTopApps()` 在 `combine` 订阅时发射了数据，会触发一次合并。但 `downloadRepository.stateFlow` 会持续变化。

这个逻辑看起来是正确的。

### 10. `AppViewModel.state` 的初始化
```kotlin
val state = userCase.getShowAppsFlow(viewModelScope)
```

`getShowAppsFlow` 内部调用了 `stateIn(viewModelScope, ...)`。这意味着 `stateIn` 使用 `viewModelScope`。当 ViewModel 被清除时，这个 StateFlow 会停止。这是正确的。

但有一个潜在问题：`loadTopApps()` 的 Flow 在 `combine` 中会每次订阅时重新执行。如果 `AppFragment` 经历配置变更（如旋转屏幕），`viewModelScope` 不会销毁，`StateFlow` 会继续存活。但如果用户离开页面后 `WhileSubscribed(5000)` 超时，上游 `loadTopApps()` 的订阅会取消。当用户再次返回时，会重新订阅 `loadTopApps()`，再次触发网络请求。

这取决于业务需求——如果希望避免不必要的重复请求，可以单独把 API 结果缓存为 `StateFlow`。

### 11. `DataModule` 是 `class` 不是 `object`
```kotlin
class DataModule { ... }
```
应该改为 `object DataModule`，这样 Hilt 不需要为 Module 创建实例。

### 12. `DatabaseModule` 是 `class` 不是 `object`
同样的问题。

### 13. `AppRepository` 的 `loadTopApps()` 返回的 Flow
`BaseRepository` 的 `request` 方法是什么？让我看看。

### 14. `RealAppCenterSdk` 没有 `@Inject` 构造函数
`AppSdkModule` 中：
```kotlin
@Binds
fun bindsCarRepository(repository: RealAppCenterSdk): AppCenterSdk
```
`RealAppCenterSdk` 需要能被 Hilt 实例化。它当前是 `class RealAppCenterSdk : AppCenterSdk`，没有 `@Inject constructor()`。

让我检查一下 `RealAppCenterSdk` 是否有 `@Inject`。
```
**已查看文件** *RealAppCenterSdk.kt 1-30*

**已查看文件** *BaseRepository.kt*
深度思考
```用户要求对修改后的 download 模块给出优化建议。我已经读取了所有关键文件。让我系统性地整理优化建议。

当前模块状态：
- Hilt DI 已接入
- `DownloadRepository` 从 object 改为 `@Singleton` class，带 `@Inject constructor`
- `AppViewModel` 已改为 `@HiltViewModel`
- `DownloadAppUserCase` 已改为 `@Inject constructor`
- `AppRepository` 已改为 `@Inject constructor`
- DI 模块：`AppSdkModule` (interface + @Binds), `DataModule` (class + @Provides), `DatabaseModule` (class + @Provides)

发现的问题：

### 1. Hilt Module 应该用 `object` 而非 `class`
`DataModule` 和 `DatabaseModule` 使用 `class` 声明。在 Kotlin 中，如果 Module 全是 `@Provides` 方法，应该用 `object`，这样 Hilt 不需要创建实例。

### 2. `AppSdkModule` 方法命名不当
`bindsCarRepository(repository: RealAppCenterSdk): AppCenterSdk` —— 方法名 `bindsCarRepository` 与实际的 `AppCenterSdk` 无关，应该改为 `bindsAppCenterSdk`。

### 3. `DatabaseModule` 方法命名不当
`provideCarDao(appDatabase: AppDatabase): DownloadDao` —— 方法名 `provideCarDao` 与实际返回的 `DownloadDao` 不符。

### 4. `DownloadRepository.restore()` 存在时序 bug
```kotlin
map.forEach { (packageName, state) ->
    sdk.queryDownloadStatus(packageName) { remoteState ->  // 异步回调
        if (remoteState != null) {
            map[packageName] = restoreState
            updateToDB(restoreState)
        }
    }
    sdk.registerCallback(...)
}
_stateFlow.emit(map)  // 可能早于所有 queryDownloadStatus 回调执行
```

`queryDownloadStatus` 是异步回调，`_stateFlow.emit(map)` 可能在回调完成前就执行了，导致恢复的远程状态没有同步到 StateFlow。

### 5. `DownloadRepository` 的 `downloadCallback` 在传入 null 时仍启动协程
```kotlin
fun downloadCallback(downloadApps: ((Map<String, DownloadState>) -> Unit)?) {
    callbackJob?.cancel()
    callbackJob = scope.launch {
        stateFlow.collect {
            downloadApps?.invoke(it)
        }
    }
}
```
如果 `downloadApps` 为 null，仍会启动一个无用的收集协程。

### 6. `DownloadRepository` 的 `init` 块中调用 `sdk.initSdk`
`DownloadRepository` 是 `@Singleton`，在构造函数中调用 `sdk.initSdk(context)`，这会触发 SDK 初始化。但 `initSdk` 的回调中调用了 `restore()`。`restore()` 中又会注册回调并 emit StateFlow。

这里有个问题：`DownloadRepository` 被注入后，`init` 块立即执行，但此时可能 UI 还没准备好监听 StateFlow。不过这不是严重问题。

### 7. `DownloadAppUserCase` 中大量方法直接透传 `DownloadRepository`
`downloadCallback`, `getDownloadStateLiveData`, `registerDetailDownloadListener`, `isSupportDownload` 都是直接透传。UseCase 的封装意义不大。

### 8. `AppViewModel.state` 的 `combine` 问题
`getShowAppsFlow` 内部 combine 了 `appRepository.loadTopApps()` 和 `downloadRepository.stateFlow`。`loadTopApps()` 是冷流，每次 StateFlow 有新的订阅者（在 `WhileSubscribed` 超时后重新订阅）都会重新触发网络请求。这可能导致用户离开页面5秒后回来，自动重新请求列表。

### 9. `DownloadRepository` 内部协程作用域
```kotlin
private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
```
`@Singleton` 的 Repository 自己创建协程作用域。应用进程被杀后不会自动清理。更好的做法是注入 `@ApplicationScope` 或 `SupervisorJob()`。不过项目可能没有配置这些。

### 10. `DataModule` 中 `HostManager.getAppBaseUrl()` 的调用
每次获取 `AppApi` 实例时都会调用 `HostManager.getAppBaseUrl()`。如果 `HostManager` 中的 `EnvManager.getEnv()` 可能变化，这没问题。但如果 `AppApi` 是单例且 baseUrl 在运行时切换，当前 `NetworkManagerMultiBaseUrl.create` 创建的 Retrofit 实例是缓存的，不会自动更新 baseUrl。

实际上 `NetworkManagerMultiBaseUrl` 内部有缓存：
```kotlin
private val retrofitCache = mutableMapOf<String, Retrofit>()
```

所以如果 baseUrl 变了，会创建新的 Retrofit 实例，旧的 Service 实例就过期了。这意味着如果用户在运行中切换环境，`AppApi` 单例不会自动更新。

但这属于业务逻辑问题，不一定需要优化。

### 11. `RealAppCenterSdk` 的 `@Inject constructor()`
已经加了 `@Inject constructor()`，没问题。

### 12. `DownloadRepository` 的 `updateToDB` 和 `saveToDB` 冗余
`updateToDB` 就是 `scope.launch { saveToDB(state) }`，而 `update()` 已经在 `scope.launch` 中了。`update()` 调用 `saveToDB` 是同步的（在同一个协程中），但 `restore()` 中调用 `updateToDB` 会启动新协程。这没问题。

### 13. `DownloadRepository` 中 `isSupportDownload` 的默认值
`RealAppCenterSdk` 中：
```kotlin
private var isSupportDownload: Boolean = true
```

初始值是 `true`，但 `initSdk` 的回调中可能将其设为 `false`。如果 `isDownloadEnabled()` 在 `initSdk` 完成前被调用，会返回 `true`（因为 `ctaStatus == null` 时 `checkCTAStatus` 被异步调用，返回的是旧值）。

这不是当前模块的问题。

让我给出有针对性的优化建议。我会按优先级排序。
```
已经仔细看了修改后的代码，整体架构从手动 Factory 成功迁移到了 Hilt，DI 链路已经打通。以下是针对当前实现的优化建议：

---

## 1. Hilt Module 声明方式：`class` → `object`

`DataModule` 和 `DatabaseModule` 目前用 `class` 声明。如果 Module 里全是 `@Provides` 方法（没有抽象方法/ `@Binds`），Kotlin 中应该用 `object`，避免 Hilt 额外实例化。

```kotlin
package com.xxh.summary.download.di

import com.sample.lib.network.core.NetworkManagerMultiBaseUrl
import com.xxh.summary.download.data.remote.api.AppApi
import com.xxh.summary.download.data.remote.config.HostManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideAppApi(): AppApi {
        return NetworkManagerMultiBaseUrl.create(HostManager.getAppBaseUrl(), AppApi::class.java)
    }

}
```
```kotlin
package com.xxh.summary.download.di

import android.content.Context
import com.xxh.summary.download.data.db.AppDatabase
import com.xxh.summary.download.data.db.DownloadDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideDownloadDao(appDatabase: AppDatabase): DownloadDao {
        return appDatabase.downloadDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

}
```


---

## 2. `AppSdkModule` 方法命名与参数名修正

当前方法名 `bindsCarRepository` 和参数名 `repository` 与实际类型不匹配，应修正为语义化命名：

```kotlin
package com.xxh.summary.download.di

import com.xxh.summary.download.data.sdk.AppCenterSdk
import com.xxh.summary.download.data.sdk.RealAppCenterSdk
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppSdkModule {
    @Singleton
    @Binds
    fun bindAppCenterSdk(sdk: RealAppCenterSdk): AppCenterSdk
}
```


---

## 3. `DownloadRepository.restore()` 存在时序 Bug

`queryDownloadStatus` 是异步回调，但 `_stateFlow.emit(map)` 在 `forEach` 结束后**同步执行**，可能导致远程状态还没回来就 emit 了旧数据。

```kotlin
// 当前问题代码
map.forEach { (packageName, state) ->
    sdk.queryDownloadStatus(packageName) { remoteState ->  // 异步
        map[packageName] = remoteState ?: state
    }
    sdk.registerCallback(...)
}
_stateFlow.emit(map)  // 可能早于所有回调执行
```


**优化**：使用 `suspendCancellableCoroutine` 把 `queryDownloadStatus` 包装为挂起函数，确保所有查询完成后再 emit。

```kotlin
// ... existing code ...

    fun restore() {
        scope.launch {
            val dbTasks = dao.getAll()
            val map = dbTasks.associate {
                it.packageName to DownloadState(
                    packageName = it.packageName,
                    progress = it.progress,
                    status = Status.entries[it.status]
                )
            }.toMutableMap()

            map.forEach { (packageName, state) ->
                val remoteState = queryDownloadStatusSync(packageName)
                if (remoteState != null) {
                    map[packageName] = remoteState
                    saveToDB(remoteState)
                }
                sdk.registerCallback(
                    remoteState?.packageName ?: packageName,
                    remoteState?.progress ?: state.progress,
                    callback(packageName)
                )
            }

            _stateFlow.emit(map)
        }
    }

    private suspend fun queryDownloadStatusSync(packageName: String): DownloadState? =
        suspendCancellableCoroutine { continuation ->
            sdk.queryDownloadStatus(packageName) { result ->
                continuation.resume(result)
            }
        }

// ... existing code ...
```


> 注意：需要在文件顶部 `import kotlinx.coroutines.suspendCancellableCoroutine`。

---

## 4. `downloadCallback` 传入 null 时仍启动无用协程

当前逻辑：即使 `downloadApps` 为 `null`，也会启动一个持续 `collect` 的协程。

```kotlin
// ... existing code ...

    fun setDownloadListener(listener: ((Map<String, DownloadState>) -> Unit)?) {
        callbackJob?.cancel()
        listener ?: return
        callbackJob = scope.launch {
            stateFlow.collect { listener(it) }
        }
    }

// ... existing code ...
```


> 建议把方法名也改为 `setDownloadListener`，语义更清晰。

---

## 5. `DownloadAppUserCase` 方法职责边界

UseCase 中大量方法是对 `DownloadRepository` 的透传，封装价值较低：

```kotlin
fun downloadCallback(...) = downloadRepository.downloadCallback(...)
fun getDownloadStateLiveData() = downloadRepository.stateFlow.asLiveData()
fun isSupportDownload() = downloadRepository.isDownloadEnabled()
```


**优化方向**：如果 `AppViewModel` / UI 需要这些能力，可以直接注入 `DownloadRepository`，不需要经过 UseCase 透传。UseCase 应只保留**业务编排逻辑**（如 `getShowAppsFlow`、`handleAction`）。

---

## 6. `AppViewModel.state` 的重复请求风险

`getShowAppsFlow` 内部 `combine(appRepository.loadTopApps(), downloadRepository.stateFlow)`，`loadTopApps()` 是冷流。当 `WhileSubscribed(5000)` 超时后订阅取消，用户返回页面会重新订阅，触发新的网络请求。

如果列表数据不需要每次回到页面都刷新，可以预加载并缓存：

```kotlin
class AppViewModel @Inject constructor(
    private val userCase: DownloadAppUserCase,
    private val appRepository: AppRepository
) : ViewModel() {

    // 缓存 API 结果，避免重复请求
    private val apiResult = appRepository.loadTopApps()
        .stateIn(viewModelScope, SharingStarted.Eagerly, ResultState.Loading)

    val state = userCase.getShowAppsFlow(apiResult, viewModelScope)
}
```


---

## 7. `DownloadRepository` 内部协程作用域建议

当前自己创建 `CoroutineScope`：
```kotlin
private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
```


建议改为注入 `SupervisorJob()` + `Dispatchers.IO`，或使用 `@ApplicationScope`。如果项目没有自定义 Qualifier，可以暂时保持现状。

---


## 8. SDK 封装：回调转协程，消除样板 try-catch
问题：RealAppCenterSdk 中每个方法都包了一层 try-catch，且全是回调式 API，调用方很繁琐。
   优化：提供 suspend 函数版本，内部用 suspendCancellableCoroutine 包装：
// 在 SDK 层提供 suspend 函数
suspend fun queryDownloadStatus(packageName: String): DownloadState? =
    suspendCancellableCoroutine { continuation ->
        downloadManager?.getDownloadStatus(packageName) { result ->
            if (NeoDownloadManager.isSuccess(result)) {
                val statusCode = result.getInt(...)
                ...
                continuation.resume(DownloadState(...))
            } else {
                continuation.resume(null)
            }
        }
    }
   这样 DownloadRepository 里可以写顺序代码，不再需要回调地狱。

## 总结优先级

| 优先级 | 优化点 | 影响 |
|--------|--------|------|
| P0 | `restore()` 时序 Bug | 恢复时状态不准确 |
| P1 | Module 改用 `object` | 符合 Hilt/Kotlin 规范 |
| P1 | `downloadCallback(null)` 启动空协程 | 内存泄漏风险 |
| P2 | 方法命名修正 | 代码可读性 |
| P2 | UseCase 职责拆分 | 架构清晰度 |
| P3 | 网络请求缓存 | 避免不必要的重复请求 |

需要我针对某一项给出完整的重构代码吗？