plugins {
    alias(libs.plugins.qt.app.android.library)
    alias(libs.plugins.qt.app.android.hilt)
}

android {
    namespace = "com.qt.app.feature.article"
}

dependencies {
    api(project(":core:ui-state"))
    api(project(":core:navigation"))
    implementation(libs.jsoup)
}