package io.labs.dotanuki.magicmodules.internal.model

internal enum class BuildScriptType(val fileName: String) {
    GROOVY("build.gradle"),
    KOTLIN("build.gradle.kts"),
}