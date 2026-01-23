package com.xxh.compose.nav3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable


@Serializable
data object BookList : NavKey

@Serializable
data class BookDetail(val name:String): NavKey

@Composable
fun BookContent() {
    val backStack = remember {
        mutableStateListOf<Any>(BookList)
    }
    /*NavDisplay(backStack = backStack, onBack = {
        backStack.removeLastOrNull()
    }, entryProvider = entryProvider {
        entry<BookList> {
            // BookList页面内容
            ContentRed("列表页", onNext = {
                backStack.add(BookDetail("详情页"))
            })
        }
        entry<BookDetail> { detail ->
            // BookDetail页面内容，可以使用detail.name
            ContentYellow(detail.name)
        }
    })*/

    /*NavDisplay(backStack = backStack, onBack = {
        backStack.removeLastOrNull()
    }) { key ->
        when (key) {
            is BookList -> NavEntry(key) {
                ContentRed("列表页", onNext = {
                    backStack.add(BookDetail("详情页"))
                })
            }

            is BookDetail -> NavEntry(key) {
                ContentYellow(key.name)
            }

            else -> NavEntry(BookList) {
                ContentRed("列表页", onNext = {
                    backStack.add(BookDetail("详情页"))
                })
            }
        }
    }*/
    NavDisplay(backStack=backStack, onBack = {
        backStack.removeLastOrNull()
    }, entryProvider = entryProvider {
        entry<BookList>{
            ContentRed("x")
        }
        entry<BookDetail>{ key->
            ContentYellow(key.name)
        }
    })


    NavDisplay(backStack = backStack, onBack = {
        backStack.removeLastOrNull()
    }) { key ->
        when (key) {
            is BookList -> NavEntry(key) {
                ContentRed("")
            }

            is BookDetail -> NavEntry(key) {
                ContentYellow(key.name)
            }

            else -> NavEntry(BookList) {
                ContentRed("")
            }
        }
    }

}