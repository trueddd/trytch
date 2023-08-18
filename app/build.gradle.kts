plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "${Config.PackageName}.${Config.AppName}"
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

    implementation(Dependencies.Core.Ktx)
    implementation(Dependencies.Core.AppCompat)
    implementation(Dependencies.Core.KotlinImmutable)
    implementation(Dependencies.Core.SplashScreen)

    implementation(Dependencies.Compose.Ui)
    implementation(Dependencies.Compose.Runtime)
    implementation(Dependencies.Compose.Material3)
    implementation(Dependencies.Compose.ToolingPreview)
    implementation(Dependencies.Compose.Activity)
    implementation(Dependencies.Compose.FlowLayout)
    debugImplementation(Dependencies.Compose.Tooling)
    lintChecks(Dependencies.Compose.Lint)

    implementation(Dependencies.Lifecycle.RuntimeKtx)
    implementation(Dependencies.Lifecycle.ViewModelKtx)
    implementation(Dependencies.Lifecycle.ViewModelCompose)

    testImplementation(Dependencies.Test.Junit4)
    androidTestImplementation(Dependencies.Test.Android.Junit)
    androidTestImplementation(Dependencies.Test.Android.Espresso)
    androidTestImplementation(Dependencies.Compose.UiTestJunit4)

    implementation(Dependencies.Koin.Android)
    implementation(Dependencies.Koin.Compose)
    implementation(Dependencies.Koin.Annotations)
    ksp(Dependencies.Koin.Compiler)

    implementation(Dependencies.Coil.Compose)
    implementation(Dependencies.Coil.Gif)

    implementation(Dependencies.Navigation.Appyx)

    implementation(Dependencies.VideoPlayer.Core)
    implementation(Dependencies.VideoPlayer.Hls)
}
