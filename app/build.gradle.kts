plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.github.trueddd.truetripletwitch"
        minSdk = 21
        targetSdk = 33
        versionCode = Config.VersionCode
        versionName = Config.Version

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
//            proguardFiles.add(getDefaultProguardFile("proguard-android-optimize.txt"))
//            proguardFiles.add("proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(Dependency.Core.Ktx)
    implementation(Dependency.Compose.Ui)
    implementation(Dependency.Compose.Material3)
    implementation(Dependency.Compose.ToolingPreview)
    implementation(Dependency.Lifecycle.RuntimeKtx)
    implementation(Dependency.Compose.Activity)
    testImplementation(Dependency.Junit4)
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation(Dependency.Compose.UiTestJunit4)
    debugImplementation(Dependency.Compose.Tooling)
//    debugImplementation(Dependency.Compose.TestManifest)
}