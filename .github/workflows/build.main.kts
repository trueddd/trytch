#!/usr/bin/env kotlin

@file:DependsOn("io.github.typesafegithub:github-workflows-kt:0.49.0")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV3
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile
import java.io.File

workflow(
    name = "Android CI (Kotlin script)",
    on = listOf(
        Push(), // todo: add `branches = listOf("main")`
        PullRequest(branches = listOf("main")),
    ),
    sourceFile = File("build").toPath(),
) {
    job(
        id = "build",
        runsOn = RunnerType.UbuntuLatest,
        `if` = expr { "!" + contains(github.eventPush.head_commit.message, "skip_ci") },
    ) {
        uses(
            name = "Checkout source",
            action = CheckoutV3(),
        )
        uses(
            name = "Set up JDK 17",
            action = SetupJavaV3(
                javaVersion = "17",
                distribution = SetupJavaV3.Distribution.Temurin,
                cache = SetupJavaV3.BuildPlatform.Gradle,
            ),
        )
        run(
            name = "Grant execute permission for gradlew",
            command = "chmod +x gradlew",
        )
        uses(
            name = "Load Twitch secrets",
            action = CustomAction(
                actionOwner = "ozaytsev86",
                actionName = "create-env-file",
                actionVersion = "v1",
                inputs = mapOf(
                    "file-name" to "twitch.properties",
                    "ENV_client_id" to "\${{ secrets.CLIENT_ID }}",
                    "ENV_client_secret" to "\${{ secrets.CLIENT_SECRET }}",
                ),
            ),
        )
        run(
            name = "Build with Gradle",
            command = "./gradlew lint assembleRelease",
        )
    }
}.writeToFile(addConsistencyCheck = false)
