package io.labs.dotanuki.magicmodules.internal

import io.labs.dotanuki.magicmodules.MagicModulesExtension
import io.labs.dotanuki.magicmodules.internal.model.GradleBuildScript
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType
import io.labs.dotanuki.magicmodules.internal.model.GradleProjectStructure
import io.labs.dotanuki.magicmodules.internal.util.e
import io.labs.dotanuki.magicmodules.internal.util.i
import io.labs.dotanuki.magicmodules.internal.util.logger
import java.io.File

internal class ProjectStructureParser(private val magicModulesExtension: MagicModulesExtension) {

    private val foundedScripts by lazy {
        mutableSetOf<GradleBuildScript>()
    }

    private var projectName = NO_NAME_ASSIGNED

    fun parse(rootFolder: File): GradleProjectStructure =
        when {
            rootFolder.isDirectory -> {
                projectName = rootFolder.name
                deepSearchFirst(rootFolder).also {
                    logger().i("Parser :: Project structure parsed with success!")
                    logger().i("Parser :: Project name -> ${it.rootProjectName}")
                    logger().i("Parser :: Total number of Gradle scripts found -> ${it.scripts.size}")
                }
            }
            else -> {
                logger().e("Error -> Can't parsed this project structure. $rootFolder is not a directory")
                throw MagicModulesError.CantParseProjectStructure
            }
        }

    private fun deepSearchFirst(rootFolder: File): GradleProjectStructure =
        with(rootFolder) {
            innerFiles().forEach { innerFile ->
                when {
                    innerFile.isDirectory -> deepSearchFirst(innerFile)
                    else -> evaluateBuildScript(innerFile)
                }
            }

            GradleProjectStructure(projectName, foundedScripts)
        }

    private fun evaluateBuildScript(target: File) {
        with(target) {
            if (isBuildScript()) {
                foundedScripts += GradleBuildScript(path, evaluateProjectType())
            }
        }
    }

    private fun File.innerFiles(): List<File> = listFiles()?.toList() ?: emptyList()

    private fun File.evaluateProjectType(): GradleModuleType =
        if (matchesBuildSrc()) GradleModuleType.BUILDSRC
        else readLines().asSequence()
            .mapNotNull(::mapPluginLine)
            .mapNotNull(::mapLineToModuleType)
            .firstOrNull() ?: GradleModuleType.ROOT_LEVEL

    private fun File.isBuildScript(): Boolean =
        path.endsWith("build.gradle") || path.endsWith("build.gradle.kts")

    private fun File.matchesBuildSrc(): Boolean = path.contains("buildSrc")

    private fun mapPluginLine(line: String): String? =
        PLUGIN_LINE_REGEX.find(line)?.let { match -> line.substring(match.range.last) }

    private fun mapLineToModuleType(line: String): GradleModuleType? = with(magicModulesExtension) {
        when {
            rawApplicationPlugin.checkPluginLineType(line) -> GradleModuleType.APPLICATION
            rawLibraryPlugins.checkPluginLineType(line) -> GradleModuleType.LIBRARY
            else -> null
        }
    }

    private fun List<String>.checkPluginLineType(line: String) =
        any { plugin -> line.contains(plugin) }

    companion object {
        private const val NO_NAME_ASSIGNED = ""
        private val PLUGIN_LINE_REGEX =
            """^\s*((apply\s*\(?\s*plugin)|(id\s*[('"])|(kotlin\s*\())""".toRegex()
    }
}