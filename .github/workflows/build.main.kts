#!/usr/bin/env kotlin

@file:DependsOn("io.github.typesafegithub:github-workflows-kt:0.50.0")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV3
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3
import io.github.typesafegithub.workflows.actions.actions.UploadArtifactV3
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.JobBuilder
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile
import java.io.File

workflow(
    name = "Check",
    on = listOf(
        PullRequest(branches = listOf("main")),
    ),
    sourceFile = File("check").toPath(),
) {
    job(
        id = "lint",
        runsOn = RunnerType.UbuntuLatest,
        `if` = expr { "!" + contains(github.eventPush.head_commit.message, "'skip_ci'") },
    ) {
        checkout()
        setupJava()
        setupGradlew()
        loadTwitchSecrets()
        runLint()
        uploadLintReport()
    }
}.writeToFile(addConsistencyCheck = false)

workflow(
    name = "Build",
    on = listOf(
        Push(branches = listOf("main")),
    ),
    sourceFile = File("build").toPath(),
) {
    job(
        id = "build",
        runsOn = RunnerType.UbuntuLatest,
        `if` = expr { "!" + contains(github.eventPush.head_commit.message, "'skip_ci'") },
    ) {
        checkout()
        setupJava()
        setupGradlew()
        loadTwitchSecrets()
        buildApk()
        uploadApk()
    }
}.writeToFile(addConsistencyCheck = false)

fun JobBuilder<*>.checkout() = uses(
    name = "Checkout source",
    action = CheckoutV3(),
)

fun JobBuilder<*>.setupJava() = uses(
    name = "Set up JDK 17",
    action = SetupJavaV3(
        javaVersion = "17",
        distribution = SetupJavaV3.Distribution.Temurin,
        cache = SetupJavaV3.BuildPlatform.Gradle,
    ),
)

fun JobBuilder<*>.setupGradlew() = run(
    name = "Grant execute permission for gradlew",
    command = "chmod +x gradlew",
)

fun JobBuilder<*>.loadTwitchSecrets() = uses(
    name = "Load Twitch secrets",
    action = CustomAction(
        actionOwner = "ozaytsev86",
        actionName = "create-env-file",
        actionVersion = "v1",
        inputs = mapOf(
            "file-name" to "twitch.properties",
            "ENV_client_id" to expr { secrets.getValue("CLIENT_ID") },
            "ENV_client_secret" to expr { secrets.getValue("CLIENT_SECRET") },
        ),
    ),
)

fun JobBuilder<*>.buildApk() = run(
    name = "Build with Gradle",
    command = "./gradlew assembleRelease",
)

fun JobBuilder<*>.runLint() = run(
    name = "Build with Gradle",
    command = "./gradlew lint",
)

fun JobBuilder<*>.createGithubRelease() = uses(
    name = "Create release",
    `if` = expr { github.ref_name + " == " + "'main'" },
    action = CustomAction(
        actionOwner = "ncipollo",
        actionName = "release-action",
        actionVersion = "v1",
        inputs = mapOf(
            "artifacts" to "./app/build/outputs/apk/release/*.apk",
            "allowUpdates" to "true",
            "draft" to "true",
            "tag" to "0.1.0",
        ),
    )
)

fun JobBuilder<*>.uploadApk() = uses(
    name = "Upload a build artifact",
    action = UploadArtifactV3(
        name = "releaseApk",
        path = listOf("./app/build/outputs/apk/release/*.apk"),
    )
)

fun JobBuilder<*>.uploadLintReport() = uses(
    name = "Upload a lint report",
    action = UploadArtifactV3(
        name = "lintReport",
        path = listOf("./app/build/reports/lint-results-*.html"),
    )
)
