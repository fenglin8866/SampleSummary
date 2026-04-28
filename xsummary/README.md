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