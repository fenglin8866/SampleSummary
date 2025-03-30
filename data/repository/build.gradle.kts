import org.gradle.kotlin.dsl.project

plugins {
    alias(libs.plugins.common.android.library)
    alias(libs.plugins.common.hilt)
}

android {
    namespace = "com.sample.data.repository"

}
dependencies {
    implementation(project(":data:database"))
}