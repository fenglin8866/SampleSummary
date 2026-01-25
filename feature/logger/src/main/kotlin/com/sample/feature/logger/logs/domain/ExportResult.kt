package com.sample.feature.logger.logs.domain

sealed class ExportResult {
    object Success : ExportResult()
    data class Failed(val reason: String) : ExportResult()
}