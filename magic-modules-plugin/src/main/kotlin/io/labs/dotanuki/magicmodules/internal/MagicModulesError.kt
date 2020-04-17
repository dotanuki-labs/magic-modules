package io.labs.dotanuki.magicmodules.internal

internal sealed class MagicModulesError(description: String) : RuntimeException(description) {

    object CantParseProjectStructure : MagicModulesError("Can't parse this project structure")
    object CantWriteConstantsFile : MagicModulesError("File should be a directory")
    object CantAcceptModulesNames : MagicModulesError("List of names can not be empty")
    object MissingBuildSrc : MagicModulesError("This plugin requires a Gradle project with buildSrc")
    object CantExtractGradleCoordinates : MagicModulesError("Can't extract Gradle coordinates")
}