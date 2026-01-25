package com.sample.feature.logger.logs.ui.contract

import android.net.Uri

sealed interface LogUIEvent{
    data class OpenLogDir(val dirUri: Uri) : LogUIEvent
}