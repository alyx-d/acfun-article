plugins {
    `kotlin-dsl`
}

group = "com.qt.app.build-logic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("appVersion") {
            id = "com.qt.app.version"
            implementationClass = "VersionPlugin"
        }
    }
}