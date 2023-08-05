import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.implementation(dependency: Dependency) {
    add("implementation", create(dependency.notation))
}

fun DependencyHandlerScope.api(dependency: Dependency) {
    add("api", create(dependency.notation))
}

fun DependencyHandlerScope.debugImplementation(dependency: Dependency) {
    add("debugImplementation", create(dependency.notation))
}

fun DependencyHandlerScope.testImplementation(dependency: Dependency) {
    add("testImplementation", create(dependency.notation))
}

fun DependencyHandlerScope.androidTestImplementation(dependency: Dependency) {
    add("androidTestImplementation", create(dependency.notation))
}

fun DependencyHandlerScope.lintChecks(dependency: Dependency) {
    add("lintChecks", create(dependency.notation))
}

fun DependencyHandlerScope.ksp(dependency: Dependency) {
    add("ksp", create(dependency.notation))
}

data class Dependency(val notation: String)

object Dependencies {
    object Core {
        val Ktx = Dependency("androidx.core:core-ktx:1.10.1")
        val SplashScreen = Dependency("androidx.core:core-splashscreen:1.0.1")
        val AppCompat = Dependency("androidx.appcompat:appcompat:1.6.1")
        val KotlinImmutable = Dependency("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
    }
    object Compose {
        val Ui = Dependency("androidx.compose.ui:ui:${Versions.Compose}")
        val Runtime = Dependency("androidx.compose.runtime:runtime:${Versions.Compose}")
        val UiTestJunit4 = Dependency("androidx.compose.ui:ui-test-junit4:${Versions.Compose}")
        val Tooling = Dependency("androidx.compose.ui:ui-tooling:${Versions.Compose}")
        val ToolingPreview = Dependency("androidx.compose.ui:ui-tooling-preview:${Versions.Compose}")
        val Activity = Dependency("androidx.activity:activity-compose:${Versions.Activity}")
        val Material3 = Dependency("androidx.compose.material3:material3:${Versions.ComposeMaterial3}")
        val FlowLayout = Dependency("com.google.accompanist:accompanist-flowlayout:${Versions.Accompanist}")
        val Lint = Dependency("com.slack.lint.compose:compose-lint-checks:1.2.0")
    }
    object Lifecycle {
        val RuntimeKtx = Dependency("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.Lifecycle}")
        val ViewModelKtx = Dependency("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.Lifecycle}")
        val ViewModelCompose = Dependency("androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.Lifecycle}")
    }
    object Test {
        val Junit4 = Dependency("junit:junit:4.13.2")
        object Android {
            val Junit = Dependency("androidx.test.ext:junit:1.1.5")
            val Espresso = Dependency("androidx.test.espresso:espresso-core:3.5.1")
        }
    }
    object Koin {
        val Core = Dependency("io.insert-koin:koin-core:${Versions.Koin}")
        val Android = Dependency("io.insert-koin:koin-android:${Versions.Koin}")
        val Compose = Dependency("io.insert-koin:koin-androidx-compose:3.4.5")
        val Annotations = Dependency("io.insert-koin:koin-annotations:${Versions.KoinAnnotations}")
        val Compiler = Dependency("io.insert-koin:koin-ksp-compiler:${Versions.KoinAnnotations}")
    }
    object Ktor {
        val Core = Dependency("io.ktor:ktor-client-core:${Versions.Ktor}")
        val OkHttp = Dependency("io.ktor:ktor-client-okhttp:${Versions.Ktor}")
        val OkHttpLogging = Dependency("com.squareup.okhttp3:logging-interceptor:4.10.0")
        val ContentNegotiation = Dependency("io.ktor:ktor-client-content-negotiation:${Versions.Ktor}")
        val Gson = Dependency("io.ktor:ktor-serialization-gson:${Versions.Ktor}")
        val KotlinX = Dependency("io.ktor:ktor-serialization-kotlinx-json:${Versions.Ktor}")
        val Auth = Dependency("io.ktor:ktor-client-auth:${Versions.Ktor}")
    }
    object Room {
        val Core = Dependency("androidx.room:room-runtime:${Versions.Room}")
        val Ktx = Dependency("androidx.room:room-ktx:${Versions.Room}")
        val Compiler = Dependency("androidx.room:room-compiler:${Versions.Room}")
    }
    object Coil {
        val Compose = Dependency("io.coil-kt:coil-compose:${Versions.Coil}")
        val Gif = Dependency("io.coil-kt:coil-gif:${Versions.Coil}")
    }
    object Navigation {
        val Appyx = Dependency("com.bumble.appyx:core:1.3.0")
    }
    object VideoPlayer {
        val Core = Dependency("com.google.android.exoplayer:exoplayer:${Versions.ExoPlayer}")
        val Hls = Dependency("com.google.android.exoplayer:exoplayer-hls:${Versions.ExoPlayer}")
    }
    val DataStore = Dependency("androidx.datastore:datastore-preferences:${Versions.DataStore}")
}
