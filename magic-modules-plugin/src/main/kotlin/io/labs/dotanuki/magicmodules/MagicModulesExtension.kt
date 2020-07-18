package io.labs.dotanuki.magicmodules

open class MagicModulesExtension {
    var includeApps: Boolean = true
    var rawApplicationPlugin: String = "com.android.application"
    var rawLibraryPlugins: List<String> = listOf(
        "com.android.library", "kotlin", "com.android.dynamic-feature", "jvm"
    )

    companion object {
        val DEFAULT = MagicModulesExtension()
    }
}