package com.sample.core.basic.event.log

import com.sample.core.basic.event.UIEvent


sealed  class LogUIEvent(override val canDrop: Boolean = true) : UIEvent {
    data class OpenLogDir(val directoryPath: String) : LogUIEvent()
}