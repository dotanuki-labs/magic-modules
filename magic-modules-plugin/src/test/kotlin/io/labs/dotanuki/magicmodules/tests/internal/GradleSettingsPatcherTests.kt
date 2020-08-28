package io.labs.dotanuki.magicmodules.tests.internal

import io.labs.dotanuki.magicmodules.MagicModulesExtension
import io.labs.dotanuki.magicmodules.internal.GradleSettingsPatcher
import io.labs.dotanuki.magicmodules.internal.model.CanonicalModuleName
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleInclude
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType
import io.labs.dotanuki.magicmodules.internal.model.ProcessedScriptsResult
import io.labs.dotanuki.magicmodules.tests.fakes.FakeSettings
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class GradleSettingsPatcherTests {

    private val processedScripts = listOf(
        ProcessedScriptsResult(
            moduleType = GradleModuleType.LIBRARY,
            coordinates = mapOf(
                GradleModuleInclude(":core") to listOf(CanonicalModuleName("CORE")),
                GradleModuleInclude(":common") to listOf(CanonicalModuleName("COMMON")),
                GradleModuleInclude(":login") to listOf(CanonicalModuleName("LOGIN")),
                GradleModuleInclude(":profile") to listOf(CanonicalModuleName("PROFILE"))
            )
        ),
        ProcessedScriptsResult(
            moduleType = GradleModuleType.APPLICATION,
            coordinates = mapOf(
                GradleModuleInclude(":app") to listOf(CanonicalModuleName("APP"))
            )
        )
    )

    @Test fun `should add all modules into settings`() {
        val settings = FakeSettings()

        processedScripts.forEach { script ->
            GradleSettingsPatcher.patch(settings, script, MagicModulesExtension.DEFAULT)
        }

        println(settings.included)

        assertThat(settings.included).apply {
            contains(":core")
            contains(":common")
            contains(":login")
            contains(":profile")
            contains(":app")
        }
    }

    @Test fun `should exclude app modules into settings`() {
        val settings = FakeSettings()

        val customExtension = MagicModulesExtension().apply {
            includeApps = false
        }

        processedScripts.forEach { script ->
            GradleSettingsPatcher.patch(settings, script, customExtension)
        }

        println(settings.included)

        assertThat(settings.included).apply {
            contains(":core")
            contains(":common")
            contains(":login")
            contains(":profile")
        }
    }
}