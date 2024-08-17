plugins {
    alias(libs.plugins.qt.app.android.library)
    alias(libs.plugins.qt.app.android.hilt)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
}

android {
    namespace = "com.qt.app.feature.article"
}

dependencies {
    api(project(":core:network"))
    api(project(":core:ui-state"))
    api(project(":core:navigation"))
    implementation(libs.jsoup)
    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.gif)
    implementation(libs.androidx.paging.rumtime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.jetbrains.kotlin.reflect)
}