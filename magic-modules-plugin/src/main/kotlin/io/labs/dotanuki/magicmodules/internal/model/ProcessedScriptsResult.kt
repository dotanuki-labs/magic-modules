package io.labs.dotanuki.magicmodules.internal.model

internal data class ProcessedScriptsResult(
    val moduleType: GradleModuleType,
    val processedCoordinates: Map<CanonicalModuleName, GradleModuleInclude>
)