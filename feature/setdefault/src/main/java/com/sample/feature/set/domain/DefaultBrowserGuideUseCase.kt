package com.sample.feature.set.domain

import android.content.Context
import com.sample.feature.set.infra.DefaultBrowserChecker
import com.sample.feature.set.repository.DefaultBrowserGuideStore
import kotlinx.coroutines.flow.first

class DefaultBrowserGuideUseCase(
    private val checker: DefaultBrowserChecker,
    private val store: DefaultBrowserGuideStore
) {

    /**
     * 是否展示引导弹窗
     */
    suspend fun shouldShowGuide(context: Context): Boolean {
        if (checker.isDefaultBrowser(context)) return false

        val lastTime = store.lastGuideTimeFlow.first()
        val now = System.currentTimeMillis()

        return now - lastTime > store.cooldownMillis()
    }

    suspend fun markGuideShown() {
        store.updateGuideTime()
    }
}


