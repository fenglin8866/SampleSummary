package com.sample.data.datastore.ui

import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.fragment.fragment
import com.sample.data.datastore.ui.task.TasksFragment
import com.sample.data.datastore.ui.user.UserFragment
import kotlinx.serialization.Serializable
import javax.inject.Inject


@Serializable
object Main

@Serializable
object Task

@Serializable
object User

class NavigationGraph @Inject constructor() {
    fun createGraph(navController: NavController) {
        navController.graph = navController.createGraph(
            startDestination = Main
        ) {
            fragment<MainFragment, Main>()
            fragment<UserFragment, User>()
            fragment<TasksFragment, Task>()
        }
    }
}