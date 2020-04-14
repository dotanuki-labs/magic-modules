package io.labs.dotanuki.magicmodules

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class MagicModulesPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        println("Hello from MagicModules Gradle Plugin")
    }
}