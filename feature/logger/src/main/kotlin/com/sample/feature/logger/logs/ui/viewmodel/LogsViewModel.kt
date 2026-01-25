package com.sample.feature.logger.logs.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.sample.feature.logger.basic.BaseViewModel
import com.sample.feature.logger.basic.event.GlobalUiEvent
import com.sample.feature.logger.basic.event.GlobalUiEventDispatcher
import com.sample.feature.logger.logs.domain.ExportResult
import com.sample.feature.logger.logs.domain.LogsUserCase
import com.sample.feature.logger.logs.ui.contract.LogUIEvent
import com.sample.feature.logger.logs.ui.contract.LogUIIntent
import com.sample.feature.logger.logs.ui.contract.LogUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val userCase: LogsUserCase,
    private val eventDispatcher: GlobalUiEventDispatcher
) : BaseViewModel<LogUIState, LogUIIntent, LogUIEvent>(LogUIState.Empty) {

    /*val logs: StateFlow<List<Log>> =
        logsRepository.getAllLogs().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )*/
    init {
        loadLogs()
    }

    override fun handleIntent(intent: LogUIIntent) {
        when (intent) {
            LogUIIntent.OnClearClicked -> {
                clear()
            }

            LogUIIntent.OnBackClicked -> {
                eventDispatcher.emit(GlobalUiEvent.Back)
            }

            LogUIIntent.OnStartClicked -> {
                start()
            }

            LogUIIntent.OnEndClicked -> {
                stop()
            }

            LogUIIntent.OnExportClicked -> {
                export()
            }

            LogUIIntent.OnOpenClicked -> {
                sendEvent(LogUIEvent.OpenLogDir(userCase.getLogsUri()))
            }
        }
    }

    fun loadLogs() {
        viewModelScope.launch {
            userCase.getAllLogs().collect { logs ->
                updateState(LogUIState.LogsData(logs))
            }
        }
    }

    private fun export() {
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.value is LogUIState.LogsData) {
                val result = userCase.exportLogs((uiState.value as LogUIState.LogsData).logs)
                when (result) {
                    is ExportResult.Success -> {
                        eventDispatcher.emit(GlobalUiEvent.Toast("Export Success"))
                    }

                    is ExportResult.Failed -> {
                        eventDispatcher.emit(GlobalUiEvent.Toast("Export Failed"))
                    }
                }
            }
        }
    }

    private fun start() {
        userCase.start()
    }

    private fun stop() {
        viewModelScope.launch {
            userCase.end()
        }
    }

    private fun clear() {
        viewModelScope.launch {
            userCase.clearLogs()
        }
    }
}