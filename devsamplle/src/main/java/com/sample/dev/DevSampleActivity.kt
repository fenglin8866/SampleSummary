package com.sample.dev

import android.content.Intent
import com.sample.core.basic.ui.list.ListBaseActivity
import com.sample.dev.paging.advanced.ui.SearchRepositoriesActivity
import com.sample.dev.paging.basic.ui.ArticleActivity
import com.sample.dev.paging.ui.CheeseActivity

class DevSampleActivity:ListBaseActivity() {
    override fun setData(): Array<String> = arrayOf(
        "Paging3_1",
        "Paging3_2",
        "Paging3_3",
    )

    override fun setClickIntent(name: String): Intent? {
        val clazz: Class<*>? = when (name) {
            "Paging3_1" -> CheeseActivity::class.java
            "Paging3_2" -> ArticleActivity::class.java
            "Paging3_3" -> SearchRepositoriesActivity::class.java
            else -> null
        }
        return Intent(this,clazz)
    }
}