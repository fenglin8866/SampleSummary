package com.sample.feature.logger.logs

import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.fragment.fragment
import com.sample.feature.logger.logs.ui.DevFragment
import com.sample.feature.logger.logs.ui.LogsFragment
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
object Dev

@Serializable
object Logs

class NavigationGraph @Inject constructor() {
    fun createGraph(navController: NavController) {
        navController.graph = navController.createGraph(
            startDestination = Logs
        ) {
            fragment<DevFragment, Dev>()
            fragment<LogsFragment, Logs>()
        }
    }
}