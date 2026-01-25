package com.sample.feature.logger.logs.ui.contract

import com.sample.feature.logger.logs.repository.Log


sealed interface LogUIState {
    data object Empty: LogUIState
    data class LogsData(val logs:List<Log>): LogUIState
}