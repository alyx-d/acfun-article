plugins {
    alias(libs.plugins.qt.app.android.library)
}

android {
    namespace = "com.qt.app.core.ui.state"
}

dependencies {
    api(libs.coil.kt.gif)
    api(libs.coil.kt.compose)
    api(libs.androidx.ui)
    api(libs.androidx.core.ktx)
    api(libs.androidx.material3)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.activity.compose)
    api(libs.androidx.ui.tooling.preview)
    api(libs.jetbrains.kotlin.reflect)
    api(platform(libs.androidx.compose.bom))
    api(project(":core:data"))
}