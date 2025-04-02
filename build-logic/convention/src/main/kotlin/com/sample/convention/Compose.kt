package com.sample.convention

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure Compose-specific options
 */
internal fun Project.configureCompose(baseExtension: BaseExtension) {
    baseExtension.apply {

        buildFeatures.apply {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            "implementation"(platform(bom))
            "implementation"(libs.findLibrary("androidx-compose-ui").get())
            "implementation"(libs.findLibrary("androidx-compose-ui-util").get())
            "implementation"(libs.findLibrary("androidx-compose-ui-graphics").get())
            "implementation"(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            "implementation"(libs.findLibrary("androidx-compose-material").get())
            "implementation"(libs.findLibrary("androidx-compose-material-icon").get())
            "implementation"(libs.findLibrary("androidx-compose-material-icon-extended").get())
            "implementation"(libs.findLibrary("androidx-compose-material3").get())
            "implementation"(libs.findLibrary("androidx-compose-material3-adaptive").get())
            "implementation"(libs.findLibrary("androidx-compose-material3-adaptive-layout").get())
            "implementation"(libs.findLibrary("androidx-compose-material3-adaptive-navigation").get())
            "implementation"(libs.findLibrary("androidx-compose-material3-adaptive-navigation-suite").get())
            "implementation"(libs.findLibrary("androidx-compose-material3-window-size").get())
            "androidTestImplementation"(platform(bom))
            "androidTestImplementation"(libs.findLibrary("androidx-compose-ui-test-junit4").get())
            "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
            "debugImplementation"(libs.findLibrary("androidx-compose-ui-test-manifest").get())


            "implementation"(libs.findLibrary("androidx.lifecycle.runtime.compose").get())
            "implementation"(libs.findLibrary("androidx.lifecycle.viewmodel.compose").get())
            "implementation"(libs.findLibrary("androidx.navigation.compose").get())
            "implementation"(libs.findLibrary("hilt.navigation.compose").get())
        }
    }
}