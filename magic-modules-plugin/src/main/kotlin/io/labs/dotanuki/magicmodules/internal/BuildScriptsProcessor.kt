package io.labs.dotanuki.magicmodules.internal

import io.labs.dotanuki.magicmodules.internal.model.CanonicalModuleName
import io.labs.dotanuki.magicmodules.internal.model.GradleBuildScript
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleInclude
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.APPLICATION
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.INCLUDE_BUILD
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.JAVA_LIBRARY
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

            val javaLibraries = mutableMapOf<GradleModuleInclude, List<CanonicalModuleName>>()
            val libraries = mutableMapOf<GradleModuleInclude, List<CanonicalModuleName>>()
            val applications = mutableMapOf<GradleModuleInclude, List<CanonicalModuleName>>()
            var missingBuildSrc = true

            for (script in scripts) {
                if (script.moduleType == INCLUDE_BUILD) {
                    reportSkipped(script)
                    missingBuildSrc = false
                    continue
                }

                val extractor = ExtractCoordinates(rootProjectName, script)
                when (script.moduleType) {
                    JAVA_LIBRARY -> javaLibraries[extractor.gradleInclude()] = extractor.modulePaths()
                    LIBRARY -> libraries[extractor.gradleInclude()] = extractor.modulePaths()
                    APPLICATION -> applications[extractor.gradleInclude()] = extractor.modulePaths()
                    else -> reportSkipped(script)
                }
            }

            if (missingBuildSrc) {
                logger().e("Error -> $rootProjectName has no buildSrc build folder defined")
                throw MagicModulesError.MissingBuildSrc
            }

            logger().i("ScriptsProcessor :: Gradle build scripts processed with success!")
            logger().i("ScriptsProcessor :: Application modules counted -> ${applications.size}")
            logger().i("ScriptsProcessor :: Java modules counted -> ${javaLibraries.size}")
            logger().i("ScriptsProcessor :: Library modules counted -> ${libraries.size}")

            listOf(
                ProcessedScriptsResult(JAVA_LIBRARY, javaLibraries),
                ProcessedScriptsResult(LIBRARY, libraries),
                ProcessedScriptsResult(APPLICATION, applications)
            )
        }

    private fun reportSkipped(script: GradleBuildScript) {
        logger().i("ScriptsProcessor :: Skipping script -> ${script.filePath}")
    }
}