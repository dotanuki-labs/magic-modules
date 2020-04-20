package io.labs.dotanuki.magicmodules.internal.model

internal data class ProcessedScriptsResult(
    val moduleType: GradleModuleType,
    val coordinates: Map<CanonicalModuleName, GradleModuleInclude>
)