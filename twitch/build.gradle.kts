import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version Versions.Ksp
}

android {
    compileSdk = Config.Android.CompileSdk

    defaultConfig {
        minSdk = Config.Android.MinSdk
        targetSdk = Config.Android.TargetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val twitchProperties = rootProject.file("twitch.properties")
            .let { Properties().apply { load(FileInputStream(it)) } }
        buildConfigField("String", "twitchClientId", "\"${twitchProperties["client_id"]}\"")
        buildConfigField("String", "twitchClientSecret", "\"${twitchProperties["client_secret"]}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(Dependency.Koin.Core)
    implementation(Dependency.Koin.Annotations)
    ksp(Dependency.Koin.Compiler)

    implementation(Dependency.Ktor.Core)
    implementation(Dependency.Ktor.OkHttp)
    implementation(Dependency.Ktor.OkHttpLogging)
    implementation(Dependency.Ktor.ContentNegotiation)
    implementation(Dependency.Ktor.Gson)
    implementation(Dependency.Ktor.Auth)
    implementation(files(projectDir.resolve("libs").resolve("tmik-jvm-0.1.3.jar")))

    implementation(Dependency.Room.Core)
    implementation(Dependency.Room.Ktx)
    ksp(Dependency.Room.Compiler)
}
