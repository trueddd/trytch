plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.github.trueddd.truetripletwitch"
    compileSdk = Config.Android.CompileSdk

    defaultConfig {
        applicationId = "${Config.PackageName}.${Config.AppName}"
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
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = Versions.Java
        targetCompatibility = Versions.Java
    }
    kotlinOptions {
        freeCompilerArgs = listOf(
            "-Xcontext-receivers"
        )
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.ComposeCompiler
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(project(":twitch"))
    implementation(Dependency.Core.Ktx)
    implementation(Dependency.Core.AppCompat)
    implementation(Dependency.Compose.Ui)
    implementation(Dependency.Compose.Runtime)
    implementation(Dependency.Compose.Material3)
    implementation(Dependency.Compose.ToolingPreview)
    implementation(Dependency.Lifecycle.RuntimeKtx)
    implementation(Dependency.Compose.Activity)
    implementation(Dependency.Compose.FlowLayout)
    testImplementation(Dependency.Junit4)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(Dependency.Compose.UiTestJunit4)
    debugImplementation(Dependency.Compose.Tooling)

    implementation(Dependency.Koin.Android)
    implementation(Dependency.Koin.Annotations)
    ksp(Dependency.Koin.Compiler)

    implementation(Dependency.Coil.Compose)
    implementation(Dependency.Coil.Gif)
    implementation(Dependency.Navigation.Appyx)

    implementation(Dependency.VideoPlayer.Core)
    implementation(Dependency.VideoPlayer.Hls)
}
