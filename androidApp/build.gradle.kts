import Constants.Deps.Kotlinx.coroutinesCore

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.milesaway.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.milesaway.android"
        minSdk = 29
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui-tooling:1.3.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.0")
    implementation("androidx.compose.foundation:foundation:1.3.0")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation(Constants.Deps.amplifyAuth)
    implementation(Constants.Deps.amplifyCore)
    implementation(coroutinesCore)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.0")

    with (Constants.Compose) {
        implementation(compiler)
        implementation(ui)
        implementation(runtime)
        implementation(activity)
        implementation(uiGraphics)
        implementation(uiTooling)
        implementation(foundationLayout)
        implementation(material)
        implementation(materialIconsExtended)
        implementation(navigation)
        implementation(coilCompose)
    }

    with (Constants.Koin) {
        implementation(core)
        implementation(android)
        implementation(compose)
    }
}