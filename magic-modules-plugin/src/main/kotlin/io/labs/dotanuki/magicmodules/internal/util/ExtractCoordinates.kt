package io.labs.dotanuki.magicmodules.internal.util

import io.labs.dotanuki.magicmodules.internal.MagicModulesError.CantExtractGradleCoordinates
import io.labs.dotanuki.magicmodules.internal.model.CanonicalModuleName
import io.labs.dotanuki.magicmodules.internal.model.GradleBuildScript
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleInclude
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType
import java.io.File

internal class ExtractCoordinates(
    private val rootProjectName: String,
    private val buildScript: GradleBuildScript
) {

    private val segments: List<String> = with(buildScript) {
        if (moduleType == GradleModuleType.ROOT_LEVEL) {
            return@with emptyList()
        }

        // With this (GITHUB Actions): /home/runner/work/project-name/project-name
        // Results:                    [/home/runner/work/, /, /module/build.gradle]
        val splittedPath = filePath.split(rootProjectName)

        if (splittedPath.size < 2) {
            logger().e("Error -> Can't move with splitted path = $splittedPath")
            throw CantExtractGradleCoordinates
        }

        // Splitted path:   [/home/runner/work/, /, /module/build.gradle]
        // last():          /module/build.gradle
        // split separator: [, module, build.gradle]
        // drop(1):         [module, build.gradle]
        // dropLast(1):     [module]
        val segments = splittedPath
            .last()
            .split(File.separator)
            .drop(1)
            .dropLast(1)

        when (segments.size) {
            0 -> {
                logger().e("Error -> Can't move with path segments = $segments. Splitted path = $splittedPath")
                throw CantExtractGradleCoordinates
            }
            else -> segments
        }
    }

    fun modulePaths(): List<CanonicalModuleName> =
        when (buildScript.moduleType) {
            GradleModuleType.ROOT_LEVEL -> emptyList()
            else -> segments.map { CanonicalModuleName(it) }
        }

    fun gradleInclude(): GradleModuleInclude =
        when (buildScript.moduleType) {
            GradleModuleType.ROOT_LEVEL -> GradleModuleInclude(rootProjectName)
            else -> segments
                .joinToString(separator = ":", prefix = ":")
                .let { GradleModuleInclude(it) }
        }
}