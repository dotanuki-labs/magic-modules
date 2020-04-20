package io.labs.dotanuki.magicmodules.internal

import io.labs.dotanuki.magicmodules.internal.model.CanonicalModuleName
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleInclude
import org.gradle.api.initialization.Settings

internal object GradleSettingsPatcher {

    fun patch(target: Settings, coordinates: Map<CanonicalModuleName, GradleModuleInclude>) {
        coordinates.values.forEach { gradleInclude ->
            target.include(gradleInclude.value)
        }
    }
}