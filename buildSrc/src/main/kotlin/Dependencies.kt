import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.implementation(dependency: Dependency) {
    add("implementation", create(dependency.notation))
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

sealed class Dependency(val notation: String) {
    sealed class Core(notation: String) : Dependency(notation) {
        object Ktx : Core("androidx.core:core-ktx:1.8.0")
    }
    sealed class Compose(notation: String) : Dependency(notation) {
        object Ui : Compose("androidx.compose.ui:ui:${Versions.Compose}")
        object UiTestJunit4 : Compose("androidx.compose.ui:ui-test-junit4:${Versions.Compose}")
        object Tooling : Compose("androidx.compose.ui:ui-tooling:${Versions.Compose}")
        object ToolingPreview : Compose("androidx.compose.ui:ui-tooling-preview:${Versions.Compose}")
        object Activity : Compose("androidx.activity:activity-compose:${Versions.Activity}")
        object TestManifest : Compose("androidx.compose.ui:ui-test-manifest:${Versions.Compose}")
        object Material3 : Compose("androidx.compose.material3:material3:1.0.0-alpha16")
    }
    sealed class Lifecycle(notation: String) : Dependency(notation) {
        object RuntimeKtx : Lifecycle("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.Lifecycle}")
    }
    object Junit4 : Dependency("junit:junit:4.13.2")
}
