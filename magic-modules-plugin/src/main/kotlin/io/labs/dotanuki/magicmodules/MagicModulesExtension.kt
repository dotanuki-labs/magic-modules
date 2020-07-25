package io.labs.dotanuki.magicmodules

open class MagicModulesExtension {
    var includeApps: Boolean = true
    var rawApplicationPlugin: List<String> = listOf("com.android.application")
    var rawLibraryPlugins: List<String> = listOf(
        "com.android.library", "kotlin", "com.android.dynamic-feature", "jvm"
    )
    var rawLibraryUsingApplyFrom: List<String> = emptyList()

    companion object {
        val DEFAULT = MagicModulesExtension()
    }
}