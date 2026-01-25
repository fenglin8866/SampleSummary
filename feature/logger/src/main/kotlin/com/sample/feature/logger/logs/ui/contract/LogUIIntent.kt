package com.sample.feature.logger.logs.ui.contract

sealed interface LogUIIntent {
    data object OnClearClicked : LogUIIntent
    data object OnOpenClicked : LogUIIntent
    data object OnExportClicked : LogUIIntent
    data object OnBackClicked : LogUIIntent
    data object OnStartClicked : LogUIIntent
    data object OnEndClicked : LogUIIntent
}