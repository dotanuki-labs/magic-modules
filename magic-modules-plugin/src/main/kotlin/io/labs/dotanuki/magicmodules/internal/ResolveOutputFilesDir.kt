package io.labs.dotanuki.magicmodules.internal

import java.io.File

internal object ResolveOutputFilesDir {
    private val outputDirs = arrayOf("src", "main", "kotlin")

    fun using(target: File, includeBuildDir: String): File =
        (arrayOf("$target", includeBuildDir) + outputDirs)
            .joinToString(separator = File.separator)
            .let(::File)
}