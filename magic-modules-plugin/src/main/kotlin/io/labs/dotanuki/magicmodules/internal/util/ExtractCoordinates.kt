package io.labs.dotanuki.magicmodules.internal.util

import io.labs.dotanuki.magicmodules.internal.MagicModulesError.CantExtractGradleCoordinates
import io.labs.dotanuki.magicmodules.internal.model.CanonicalModuleName
import io.labs.dotanuki.magicmodules.internal.model.GradleBuildScript
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleInclude
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType

internal object ExtractCoordinates {

    fun moduleName(rootProjectName: String, buildScript: GradleBuildScript): CanonicalModuleName =
        when (buildScript.moduleType) {
            GradleModuleType.ROOT_LEVEL -> CanonicalModuleName(rootProjectName)
            else -> extractSegments(rootProjectName, buildScript)
                .joinToString(separator = "_") { it.remove("_").remove("-").toUpperCase() }
                .let { CanonicalModuleName(it) }
        }

    fun gradleInclude(rootProjectName: String, buildScript: GradleBuildScript): GradleModuleInclude =
        when (buildScript.moduleType) {
            GradleModuleType.ROOT_LEVEL -> GradleModuleInclude(rootProjectName)
            else -> extractSegments(rootProjectName, buildScript)
                .joinToString(separator = ":", prefix = ":")
                .let { GradleModuleInclude(it) }
        }

    private fun extractSegments(rootProjectName: String, buildScript: GradleBuildScript): List<String> =
        with(buildScript) {
            val splittedPath = filePath.split(rootProjectName)

            if (splittedPath.size != 2) {
                logger().e("Error -> Can move with splitted path = $splittedPath")
                throw CantExtractGradleCoordinates
            }

            val segments = splittedPath[1].split("/").drop(1).dropLast(1)

            when (segments.size) {
                0 -> {
                    logger().e("Error -> Can move with path segments = $segments. Splitted path = $splittedPath")
                    throw CantExtractGradleCoordinates
                }
                else -> segments
            }
        }

    private fun String.remove(targetPattern: String) = replace(targetPattern, "")
}