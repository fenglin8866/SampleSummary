plugins {
    alias(libs.plugins.common.android.library)
//    alias(libs.plugins.kotlin.jvm)
}

android {
    namespace = "com.sample.core.util"
}
dependencies {
    implementation(libs.gson)
}