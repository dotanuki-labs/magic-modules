package io.labs.dotanuki.magicmodules

import org.gradle.api.plugins.ExtensionAware

open class MagicModulesExtension {
    var customPackageUnderBuildSrc: String? = null
    var includeApps: Boolean = true

    companion object {
        val DEFAULT = MagicModulesExtension()
    }
}

fun ExtensionAware.magicModules(block: MagicModulesExtension.() -> Unit) {
    extensions.configure(MagicModulesExtension::class.java, block)
}