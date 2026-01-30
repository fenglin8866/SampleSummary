package com.sample.feature.logger.logs

import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.fragment.fragment
import com.sample.feature.logger.logs.ui.DevFragment
import com.sample.feature.logger.logs.ui.LogFragment
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
object Dev

@Serializable
object Log

class NavigationGraph @Inject constructor() {
    fun createGraph(navController: NavController) {
        navController.graph = navController.createGraph(
            startDestination = Log
        ) {
            fragment<DevFragment, Dev>()
            fragment<LogFragment, Log>()
        }
    }
}