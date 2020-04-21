package io.labs.dotanuki.magicmodules.internal

import io.labs.dotanuki.magicmodules.internal.model.CanonicalModuleName
import io.labs.dotanuki.magicmodules.internal.model.GradleBuildScript
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleInclude
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.APPLICATION
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.BUILDSRC
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.LIBRARY
import io.labs.dotanuki.magicmodules.internal.model.GradleProjectStructure
import io.labs.dotanuki.magicmodules.internal.model.ProcessedScriptsResult
import io.labs.dotanuki.magicmodules.internal.util.ExtractCoordinates
import io.labs.dotanuki.magicmodules.internal.util.e
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
                    BUILDSRC -> {
                        reportSkipped(script)
                        missingBuildSrc = false
                    }
                    LIBRARY -> libraries[moduleName] = include
                    APPLICATION -> applications[moduleName] = include
                    else -> reportSkipped(script)
                }
            }

            if (missingBuildSrc) {
                logger().e("Error -> $rootProjectName has no buildSrc build folder defined")
                throw MagicModulesError.MissingBuildSrc
            }

            logger().i("ScriptsProcessor :: Gradle build scripts processed with success!")
            logger().i("ScriptsProcessor :: Library modules counted -> ${libraries.size}")
            logger().i("ScriptsProcessor :: Application modules counted -> ${applications.size}")

            listOf(
                ProcessedScriptsResult(LIBRARY, libraries),
                ProcessedScriptsResult(APPLICATION, applications)
            )
        }

    private fun reportSkipped(script: GradleBuildScript) {
        logger().i("ScriptsProcessor :: Skipping script -> ${script.filePath}")
    }
}