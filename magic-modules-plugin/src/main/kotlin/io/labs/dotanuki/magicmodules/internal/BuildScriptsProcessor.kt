package io.labs.dotanuki.magicmodules.internal

import io.labs.dotanuki.magicmodules.internal.model.CanonicalModuleName
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleInclude
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.APPLICATION
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.BUILDSRC
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.LIBRARY
import io.labs.dotanuki.magicmodules.internal.model.GradleProjectStructure
import io.labs.dotanuki.magicmodules.internal.model.ProcessedScriptsResult
import io.labs.dotanuki.magicmodules.internal.util.ExtractCoordinates
import io.labs.dotanuki.magicmodules.internal.util.i
import io.labs.dotanuki.magicmodules.internal.util.logger

internal object BuildScriptsProcessor {

    fun process(projectStructure: GradleProjectStructure): List<ProcessedScriptsResult> =
        with(projectStructure) {

            val libraries = mutableMapOf<CanonicalModuleName, GradleModuleInclude>()
            val applications = mutableMapOf<CanonicalModuleName, GradleModuleInclude>()
            var missingBuildSrc = true

            scripts.forEach { script ->

                val moduleName = ExtractCoordinates.moduleName(rootProjectName, script)
                val include = ExtractCoordinates.gradleInclude(rootProjectName, script)

                when (script.moduleType) {
                    BUILDSRC -> missingBuildSrc = false
                    LIBRARY -> libraries[moduleName] = include
                    APPLICATION -> applications[moduleName] = include
                    else -> logger().i("Skipping -> ${script.filePath}")
                }
            }

            if (missingBuildSrc) throw MagicModulesError.MissingBuildSrc

            listOf(
                ProcessedScriptsResult(LIBRARY, libraries),
                ProcessedScriptsResult(APPLICATION, applications)
            )
        }
}