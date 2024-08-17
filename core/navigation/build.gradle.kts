plugins {
    alias(libs.plugins.qt.app.android.library)
}

android {
    namespace = "com.qt.app.core.navigation"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
}