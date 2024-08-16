plugins {
    alias(libs.plugins.qt.app.android.library)
}

android {
    namespace = "com.qt.app.ui.state"
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime.compose)
}