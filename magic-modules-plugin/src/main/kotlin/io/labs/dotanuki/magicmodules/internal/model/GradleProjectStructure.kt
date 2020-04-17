package io.labs.dotanuki.magicmodules.internal.model

internal data class GradleProjectStructure(
    val rootProjectName: String,
    val scripts: Set<GradleBuildScript>
)