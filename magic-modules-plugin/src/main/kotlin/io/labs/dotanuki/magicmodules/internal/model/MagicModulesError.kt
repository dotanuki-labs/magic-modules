package io.labs.dotanuki.magicmodules.internal.model

internal sealed class MagicModulesError(description: String) : RuntimeException(description) {

    object CantParseProjectStructure : MagicModulesError("Can't parse this project structure")
    object CantEvaluateBuildScriptType : MagicModulesError("Can't evaluate build script type")
}