package io.labs.dotanuki.magicmodules.internal.model

internal data class GradleBuildScript(
    val filePath: String,
    val moduleType: GradleModuleType
)