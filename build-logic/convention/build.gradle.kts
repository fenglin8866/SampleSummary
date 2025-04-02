import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.example.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.common.android.application.get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            //添加asProvider()是为了延时加载
            id = libs.plugins.common.android.library.get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("compose") {
            id = libs.plugins.common.compose.get().pluginId
            implementationClass = "ComposeConventionPlugin"
        }
        register("hilt") {
            id = libs.plugins.common.hilt.get().pluginId
            implementationClass = "HiltConventionPlugin"
        }
        register("room") {
            id = libs.plugins.common.room.get().pluginId
            implementationClass = "RoomConventionPlugin"
        }
        register("navigation") {
            id = libs.plugins.common.navigation.get().pluginId
            implementationClass = "NavigationConventionPlugin"
        }
        register("lifecycle") {
            id = libs.plugins.common.lifecycle.get().pluginId
            implementationClass = "LifecycleConventionPlugin"
        }
        register("paging") {
            id = libs.plugins.common.paging.get().pluginId
            implementationClass = "PagingConventionPlugin"
        }
    }
}


