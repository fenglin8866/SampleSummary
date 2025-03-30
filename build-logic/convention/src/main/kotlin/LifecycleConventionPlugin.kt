/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.sample.convention.libs

class LifecycleConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            dependencies {
                // Lifecycles only (without ViewModel or LiveData)
                "implementation"(libs.findLibrary("androidx.lifecycle.runtime.ktx").get())
                // Lifecycle utilities for Compose
                "implementation"(libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                // ViewModel
                "implementation"(libs.findLibrary("androidx.lifecycle.viewmodel.ktx").get())
                // ViewModel utilities for Compose
                "implementation"(libs.findLibrary("androidx.lifecycle.viewmodel.compose").get())
                // LiveData
                "implementation"(libs.findLibrary("androidx.lifecycle.livedata.ktx").get())
                // Saved state module for ViewModel
                "implementation"(libs.findLibrary("androidx.lifecycle.viewmodel.savedstate").get())
                // alternately - if using Java8, use the following instead of lifecycle-compiler
                "implementation"(libs.findLibrary("androidx.lifecycle.common.java8").get())
            }
        }
    }
}
