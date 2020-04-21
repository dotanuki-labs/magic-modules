package io.labs.dotanuki.magicmodules.internal

import io.labs.dotanuki.magicmodules.internal.model.CanonicalModuleName
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleInclude
import io.labs.dotanuki.magicmodules.internal.util.i
import io.labs.dotanuki.magicmodules.internal.util.logger
import org.gradle.api.initialization.Settings

internal object GradleSettingsPatcher {

    fun patch(target: Settings, coordinates: Map<CanonicalModuleName, GradleModuleInclude>) {
        coordinates.values
            .map { it.value }
            .forEach { gradleInclude ->
                target.include(gradleInclude)
                logger().i("Added to Settings -> $gradleInclude")
            }
    }
}