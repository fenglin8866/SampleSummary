package com.xxh.compose.nav3

import android.util.Log
import androidx.navigation3.runtime.NavEntryDecorator

class CustomNavEntryDecorator<T : Any> : NavEntryDecorator<T>(
    decorate = { entry ->
        Log.d("CustomNavEntryDecorator", "entry with ${entry.contentKey} entered composition and was decorated")
        entry.Content()
    },
    onPop = { contentKey -> Log.d("CustomNavEntryDecorator", "entry with $contentKey was popped") }
)