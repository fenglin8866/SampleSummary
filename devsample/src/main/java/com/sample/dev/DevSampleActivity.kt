package com.sample.dev

import android.content.Intent
import com.sample.core.basic.ui.list.ListBaseActivity
import com.sample.dev.paging.advanced.ui.SearchRepositoriesActivity
import com.sample.dev.paging.basic.ui.ArticleActivity
import com.sample.dev.paging.reddit.SampleWithNetWorkActivity
import com.sample.dev.paging.ui.CheeseActivity

class DevSampleActivity:ListBaseActivity() {
    override fun setData(): Array<String> = arrayOf(
        "Paging3_sample",
        "Paging3_basic",
        "Paging3_advanced",
        "Paging3_reddit",
    )

    override fun setClickIntent(name: String): Intent? {
        val clazz: Class<*>? = when (name) {
            "Paging3_sample" -> CheeseActivity::class.java
            "Paging3_basic" -> ArticleActivity::class.java
            "Paging3_advanced" -> SearchRepositoriesActivity::class.java
            "Paging3_reddit" -> SampleWithNetWorkActivity::class.java
            else -> null
        }
        return Intent(this,clazz)
    }
}