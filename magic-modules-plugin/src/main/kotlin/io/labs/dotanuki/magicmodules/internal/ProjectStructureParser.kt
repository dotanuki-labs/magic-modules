package io.labs.dotanuki.magicmodules.internal

import io.labs.dotanuki.magicmodules.internal.model.GradleBuildScript
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType
import io.labs.dotanuki.magicmodules.internal.model.GradleProjectStructure
import java.io.File

internal class ProjectStructureParser {

    private val foundedScripts by lazy {
        mutableSetOf<GradleBuildScript>()
    }

    private var projectName = NO_NAME_ASSIGNED

    fun parse(rootFolder: File): GradleProjectStructure =
        when {
            rootFolder.isDirectory -> {
                projectName = rootFolder.name
                deepSearchFirst(rootFolder)
            }
            else -> throw MagicModulesError.CantParseProjectStructure
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
        else readText().let {
            when {
                it.matchesApplicationProject() -> GradleModuleType.APPLICATION
                it.matchesLibraryProject() -> GradleModuleType.LIBRARY
                else -> GradleModuleType.ROOT_LEVEL
            }
        }

    private fun File.isBuildScript(): Boolean =
        path.endsWith("build.gradle") || path.endsWith("build.gradle.kts")

    private fun File.matchesBuildSrc(): Boolean = path.contains("buildSrc")

    private fun String.matchesLibraryProject(): Boolean =
        contains("apply plugin: \"com.android.library\"") ||
            contains("apply plugin: \"kotlin\"") ||
            contains("apply(plugin = \"com.android.library\")") ||
            contains("apply(plugin = \"kotlin\")")

    private fun String.matchesApplicationProject(): Boolean =
        contains("apply plugin: \"com.android.application\"") ||
            contains("apply(plugin = \"com.android.application\")")

    companion object {
        const val NO_NAME_ASSIGNED = ""
    }
}