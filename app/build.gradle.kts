plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version Versions.Ksp
}

android {
    compileSdk = Config.Android.CompileSdk

    defaultConfig {
        applicationId = "com.github.trueddd.truetripletwitch"
        minSdk = Config.Android.MinSdk
        targetSdk = Config.Android.TargetSdk
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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
//    applicationVariants.all { variant ->
//        variant.sourceSets.firstOrNull { it.name == "java" }
//    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(project(":twitch"))
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

    implementation(Dependency.Koin.Android)
    implementation(Dependency.Koin.Annotations)
    ksp(Dependency.Koin.Compiler)
}