package io.labs.dotanuki.magicmodules.internal

import java.io.File

internal object ResolveOutputFilesDir {

    fun using(target: File): File = File("$target/$OUTPUT_BUILD_SRC")

    private const val OUTPUT_BUILD_SRC = "buildSrc/src/main/kotlin"
}