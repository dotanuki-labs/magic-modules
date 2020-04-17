package io.labs.dotanuki.magicmodules.internal.model

internal sealed class MagicModulesError(description: String) : RuntimeException(description) {

    object CantParseProjectStructure : MagicModulesError("Can't parse this project structure")
    object CantWriteConstantsFile : MagicModulesError("File should be a directory")
    object CantAcceptModulesNames : MagicModulesError("List of names can not be empty")
}