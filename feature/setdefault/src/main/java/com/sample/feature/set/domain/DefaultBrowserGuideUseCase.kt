package com.sample.feature.set.domain

import android.content.Context
import com.sample.feature.set.infra.DefaultBrowserChecker
import com.sample.feature.set.repository.DefaultBrowserGuideStore
import kotlinx.coroutines.flow.first

class DefaultBrowserGuideUseCase(
    private val checker: DefaultBrowserChecker,
    //private val store: DefaultBrowserGuideStore
) {

    /**
     * 是否展示引导弹窗。最多提示两次。
     * 第一次加载网页完成，且当前不是默认浏览器；
     * 第二次七天后再检查是否需要提示。
     */
    suspend fun shouldShowGuide(context: Context): Boolean {
        //if (checker.isDefaultBrowser(context)) return false

       /* val lastTime = store.lastGuideTimeFlow.first()
        val now = System.currentTimeMillis()

        return now - lastTime > store.cooldownMillis()*/
        return true
    }

    /**
     * 标记已展示引导弹窗,用于第二次提示做检查。
     */
    suspend fun markGuideShown() {
       // store.updateGuideTime()
    }
}


