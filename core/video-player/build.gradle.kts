plugins {
    alias(libs.plugins.qt.app.android.library)
}

android {
    namespace = "com.qt.app.core.video.player"
}

dependencies {
    api(libs.androidx.media3.exoplayer)
    api(libs.androidx.media3.exoplayer.dash)
    api(libs.androidx.media3.exoplayer.hls)
    api(libs.androidx.media3.ui)
    api(libs.coil.kt.compose)
    implementation(project(":core:ui-state"))
}