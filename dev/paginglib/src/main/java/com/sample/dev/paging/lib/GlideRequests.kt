package com.sample.dev.paging.lib

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

class GlideRequests(context: Context) {

    private val requestManager = Glide.with(context)

    fun load(string: String): RequestBuilder<Drawable> {
        return requestManager.load(string)
    }

    fun clear(view: View) {
        requestManager.clear(view)
    }
}