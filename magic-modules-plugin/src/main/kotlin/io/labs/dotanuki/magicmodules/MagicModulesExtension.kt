package io.labs.dotanuki.magicmodules

open class MagicModulesExtension {
    var includeApps: Boolean = true
    var includeBuildDir: String = DEFAULT_INCLUDE_BUILD_PROJECT
    var maxDepthToBuildScript: Int = Int.MAX_VALUE
    var modulesToSkip: List<String> = emptyList()
    var rawApplicationPlugins: List<String> = listOf("com.android.application")
    var rawJavaLibraryPlugins: List<String> = listOf("jvm", "kotlin")
    var rawJavaLibraryUsingApplyFrom: List<String> = emptyList()
    var rawLibraryPlugins: List<String> = listOf(
        "com.android.library", "com.android.dynamic-feature"
    )
    var rawLibraryUsingApplyFrom: List<String> = emptyList()

    companion object {
        private const val DEFAULT_INCLUDE_BUILD_PROJECT = "buildSrc"
        val DEFAULT = MagicModulesExtension()
    }
}