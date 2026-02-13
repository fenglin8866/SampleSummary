package com.sample.feature.logger.logs.ui.contract

import android.net.Uri
import com.sample.core.basic2.event.UIEvent

sealed interface LogUIEvent : UIEvent {

    override val canDrop: Boolean get() = false

    data class OpenLogDir(val dirUri: Uri) : LogUIEvent
}