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
    api(libs.coil.kt.compose)
    api(libs.androidx.media3.ui)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(platform(libs.androidx.compose.bom))
}