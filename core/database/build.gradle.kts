plugins {
    alias(libs.plugins.qt.app.android.library)
    alias(libs.plugins.qt.app.android.hilt)
}

android {
    namespace = "com.qt.app.core.database"
}

dependencies {
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)
}