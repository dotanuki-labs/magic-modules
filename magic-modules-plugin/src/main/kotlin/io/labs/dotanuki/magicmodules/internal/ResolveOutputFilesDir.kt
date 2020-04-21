package io.labs.dotanuki.magicmodules.internal

import io.labs.dotanuki.magicmodules.MagicModulesExtension
import java.io.File

internal object ResolveOutputFilesDir {

    fun using(target: File, extension: MagicModulesExtension): File =
        evaluateCustomPath(target, extension.customPackageUnderBuildSrc)

    private fun evaluateCustomPath(targetDir: File, extraSegment: String?): File =
        "$targetDir/$OUTPUT_BUILD_SRC"
            .appendSegmentIfNeeded(extraSegment)
            .let { File(it) }

    private fun String.appendSegmentIfNeeded(extraSegment: String?): String =
        when (extraSegment) {
            null -> this
            else -> "$this/$extraSegment"
        }

    private const val OUTPUT_BUILD_SRC = "buildSrc/src/main/kotlin"
}