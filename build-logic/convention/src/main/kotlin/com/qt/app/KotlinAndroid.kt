package com.qt.app

import VersionConfig
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion


internal fun configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = VersionConfig.targetSdk

        defaultConfig {
            minSdk = VersionConfig.midSdk
        }

        buildFeatures {
            compose = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        lint {
            targetSdk = VersionConfig.targetSdk
        }
    }
}