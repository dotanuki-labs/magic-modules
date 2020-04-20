package io.labs.dotanuki.magicmodules.internal.model

import io.labs.dotanuki.magicmodules.internal.MagicModulesError

internal enum class GradleModuleType {
    BUILDSRC,
    ROOT_LEVEL,
    APPLICATION,
    LIBRARY;

    fun conventionedFileName() = when (this) {
        APPLICATION -> "Applications"
        LIBRARY -> "Libraries"
        else -> throw MagicModulesError.ImpossibleOutputAssociation
    }
}