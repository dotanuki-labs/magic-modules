package io.labs.dotanuki.magicmodules.internal.model

internal data class ParserRawContent(
    val maxDepthToBuildScript: Int,
    val rawApplicationPlugins: List<String>,
    val rawJavaLibraryPlugins: List<String>,
    val rawJavaLibraryUsingApplyFrom: List<String>,
    val rawLibraryPlugins: List<String>,
    val rawLibraryUsingApplyFrom: List<String>
)