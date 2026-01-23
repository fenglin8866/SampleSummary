package com.xxh.compose.nav3

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// Define your navigation keys
@Serializable
data object HomePager : NavKey
@Serializable
data object ProductList : NavKey
@Serializable
data class ProductDetail(val id: String) : NavKey

@Serializable
data object ScreenA : NavKey
@Serializable
data object ScreenB : NavKey
@Serializable
data object ScreenC : NavKey