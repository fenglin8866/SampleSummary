plugins {
    alias(libs.plugins.common.android.library)
    alias(libs.plugins.common.hilt)
    alias(libs.plugins.common.room)
}

android {
    namespace = "com.sample.data.database"
}
