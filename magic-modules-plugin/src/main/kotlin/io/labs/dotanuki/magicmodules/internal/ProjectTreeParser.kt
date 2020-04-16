package io.labs.dotanuki.magicmodules.internal

import io.labs.dotanuki.magicmodules.internal.model.BuildScriptType.GROOVY
import io.labs.dotanuki.magicmodules.internal.model.BuildScriptType.KOTLIN
import io.labs.dotanuki.magicmodules.internal.model.GradleModule
import io.labs.dotanuki.magicmodules.internal.model.ProjectTree
import java.io.File

internal class ProjectTreeParser {

    fun parse(rootFolder: File): ProjectTree? =
        when {
            rootFolder.isDirectory -> processNode(rootFolder)
            else -> null
        }

    private fun processNode(rootFolder: File): ProjectTree? {
        val contents = rootFolder.listFiles()?.toList() ?: emptyList()

        return when {
            contents.isEmpty() -> null
            else -> evaluateGradleModule(contents)
        }
    }

    private fun evaluateGradleModule(contents: List<File>): ProjectTree? =
        contents
            .find { hasBuildScript(it.path) }
            ?.let { script ->
                ProjectTree(
                    module = GradleModule(script.path),
                    children = contents.mapNotNull { parse(it) }
                )
            }
            ?: null

    private fun hasBuildScript(filePath: String): Boolean =
        filePath.let { it.contains(GROOVY.fileName) || it.contains(KOTLIN.fileName) }
}