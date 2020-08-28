package io.labs.dotanuki.magicmodules.internal

import io.labs.dotanuki.magicmodules.MagicModulesExtension
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.APPLICATION
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.JAVA_LIBRARY
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.LIBRARY
import io.labs.dotanuki.magicmodules.internal.model.ProcessedScriptsResult
import io.labs.dotanuki.magicmodules.internal.util.i
import io.labs.dotanuki.magicmodules.internal.util.logger
import org.gradle.api.initialization.Settings

internal object GradleSettingsPatcher {

    fun patch(
        target: Settings,
        processedScript: ProcessedScriptsResult,
        extension: MagicModulesExtension
    ) {
        with(processedScript) {
            coordinates.keys
                .map { it.value }
                .forEach { gradleInclude ->
                    when (moduleType) {
                        JAVA_LIBRARY, LIBRARY -> include(target, gradleInclude)
                        APPLICATION -> if (extension.includeApps) include(target, gradleInclude)
                        else -> logger().i("Patcher :: Won't include -> $gradleInclude")
                    }
                }
        }
    }

    private fun include(target: Settings, gradleInclude: String) {
        target.include(gradleInclude)
        logger().i("Patcher :: Included on settings.gradle -> $gradleInclude")
    }
}