package io.labs.dotanuki.magicmodules.internal.model

internal data class ParserRawContent(
    val rawApplicationPlugin: List<String>,
    val rawLibraryPlugins: List<String>,
    val rawLibraryUsingApplyFrom: List<String>
)