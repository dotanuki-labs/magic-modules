package io.labs.dotanuki.magicmodules.internal

import org.gradle.api.initialization.Settings
import java.io.File

internal object ResolveOutputFilesDir {

    fun using(target: Settings): File = File("${target.settingsDir}/$OUTPUT_BUILD_SRC")

    private const val OUTPUT_BUILD_SRC = "buildSrc/main/kotlin"
}