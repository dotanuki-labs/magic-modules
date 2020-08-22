package io.labs.dotanuki.magicmodules.internal

import io.labs.dotanuki.magicmodules.internal.model.GradleBuildScript
import io.labs.dotanuki.magicmodules.internal.model.GradleFoundModule
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType
import io.labs.dotanuki.magicmodules.internal.model.GradleProjectStructure
import io.labs.dotanuki.magicmodules.internal.model.ParserRawContent
import io.labs.dotanuki.magicmodules.internal.util.e
import io.labs.dotanuki.magicmodules.internal.util.i
import io.labs.dotanuki.magicmodules.internal.util.logger
import java.io.File

internal class ProjectStructureParser(
    private val parserRawContent: ParserRawContent
) {
    fun parse(rootFolder: File): GradleProjectStructure =
        when {
            rootFolder.isDirectory -> {
                val foundedScripts = rootFolder.lookUpBuildScript()
                GradleProjectStructure(rootFolder.name, foundedScripts).also {
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

    private fun File.lookUpBuildScript() = walkTopDown()
        .maxDepth(parserRawContent.maxDepthToBuildScript)
        .onEnter(::notSkipValidDirectories)
        .filter(::isBuildScript)
        .map(::mapBuildScriptPathAndType)
        .toSet()

    private fun File.evaluateProjectType(): GradleModuleType =
        if (matchesBuildSrc()) GradleModuleType.BUILDSRC
        else readLines().asSequence()
            .mapNotNull(::mapScriptLine)
            .mapNotNull(::mapFoundModule)
            .firstOrNull() ?: GradleModuleType.ROOT_LEVEL

    private fun isBuildScript(file: File): Boolean = with(file) {
        path.endsWith("build.gradle") || path.endsWith("build.gradle.kts")
    }

    private fun File.matchesBuildSrc(): Boolean = path.contains("buildSrc")

    private fun mapScriptLine(line: String): GradleFoundModule? {
        val pluginFound = PLUGIN_LINE_REGEX.find(line)
        return when {
            pluginFound != null ->
                GradleFoundModule.ApplyPlugin(line.substring(pluginFound.range.last))
            parserRawContent.rawLibraryUsingApplyFrom.isEmpty() -> null
            else -> APPLY_FROM_LINE_REGEX.find(line)?.let { match ->
                GradleFoundModule.ApplyFrom(line.substring(match.range.last))
            }
        }
    }

    private fun mapFoundModule(module: GradleFoundModule): GradleModuleType? =
        when (module) {
            is GradleFoundModule.ApplyFrom -> module.toType()
            is GradleFoundModule.ApplyPlugin -> module.toType()
        }

    private fun GradleFoundModule.ApplyFrom.toType(): GradleModuleType? =
        with(parserRawContent) {
            when {
                isAPlugin(rawJavaLibraryUsingApplyFrom) -> GradleModuleType.JAVA_LIBRARY
                isAPlugin(rawLibraryUsingApplyFrom) -> GradleModuleType.LIBRARY
                else -> null
            }
        }

    private fun GradleFoundModule.ApplyPlugin.toType(): GradleModuleType? =
        with(parserRawContent) {
            when {
                isAPlugin(rawApplicationPlugins) -> GradleModuleType.APPLICATION
                isAPlugin(rawJavaLibraryPlugins) -> GradleModuleType.JAVA_LIBRARY
                isAPlugin(rawLibraryPlugins) -> GradleModuleType.LIBRARY
                else -> null
            }
        }

    private fun GradleFoundModule.isAPlugin(plugins: List<String>) =
        plugins.any { plugin -> content.contains(plugin) }

    private fun notSkipValidDirectories(file: File) = with(file) {
        isHidden.not() && name != "build" && name != "src"
    }

    private fun mapBuildScriptPathAndType(file: File) =
        GradleBuildScript(file.path, file.evaluateProjectType())

    companion object {
        private val APPLY_FROM_LINE_REGEX = """^\s*apply\s*\(?from\s*[:=]\s*['"]?""".toRegex()
        private val PLUGIN_LINE_REGEX =
            """^\s*((apply\s*\(?\s*plugin)|(id\s*[('"])|(kotlin\s*\())""".toRegex()
    }
}