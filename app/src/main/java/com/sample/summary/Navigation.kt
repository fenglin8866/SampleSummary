/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sample.summary

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sample.feature.book.details.DetailsRoute
import com.sample.feature.book.list.ListRoute
import com.sample.feature.car.CarScreen
import com.sample.feature.logger.logs.DevModeActivity
import com.sample.feature.login.LoginActivity
import com.sample.feature.upgrade.UpgradeActivity

@Composable
fun MainNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "list") {

        composable("main") {
            CarScreen(modifier = Modifier.padding(16.dp))
        }

        composable("list") {
            val content = LocalContext.current
            ListRoute(
                onGoToItem = { id ->
                    when (id) {
                        1L -> content.startActivity(Intent(content, UpgradeActivity::class.java))
                        2L -> content.startActivity(Intent(content, LoginActivity::class.java))
                        3L -> content.startActivity(Intent(content, DevModeActivity::class.java))
                        4L -> navController.navigate("main")
                        else -> navController.navigate("details/$id")
                    }

                }
            )
        }

        composable(
            "details/{id}",
            listOf(navArgument("id") { type = NavType.LongType })
        ) {
            DetailsRoute(
                onGoBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
