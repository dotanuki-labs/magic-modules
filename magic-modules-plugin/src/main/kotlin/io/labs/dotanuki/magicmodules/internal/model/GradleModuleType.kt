package io.labs.dotanuki.magicmodules.internal.model

import io.labs.dotanuki.magicmodules.internal.MagicModulesError

internal enum class GradleModuleType {
    INCLUDE_BUILD,
    ROOT_LEVEL,
    APPLICATION,
    LIBRARY,
    JAVA_LIBRARY;

    fun conventionFileName() = when (this) {
        APPLICATION -> "ApplicationModules"
        JAVA_LIBRARY -> "JavaModules"
        LIBRARY -> "LibraryModules"
        else -> throw MagicModulesError.ImpossibleOutputAssociation
    }
}