package com.sample.feature.set.ui

sealed interface BrowserUiEvent {

    /** 展示 App 内引导弹窗 */
    object ShowDefaultBrowserGuide : BrowserUiEvent

    /** 调起系统默认浏览器弹窗 */
    object LaunchRoleManager : BrowserUiEvent

    /** 跳转系统设置页 */
    object OpenDefaultBrowserSettings : BrowserUiEvent
}
