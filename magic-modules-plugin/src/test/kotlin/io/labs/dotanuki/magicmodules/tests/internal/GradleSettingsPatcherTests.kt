package io.labs.dotanuki.magicmodules.tests.internal

import io.labs.dotanuki.magicmodules.internal.GradleSettingsPatcher
import io.labs.dotanuki.magicmodules.internal.model.CanonicalModuleName
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleInclude
import io.labs.dotanuki.magicmodules.tests.fakes.FakeSettings
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class GradleSettingsPatcherTests {

    @Test fun `should add modules into settings`() {
        val settings = FakeSettings()

        val coordinates = mapOf(
            CanonicalModuleName("LIBRARY") to GradleModuleInclude(":library"),
            CanonicalModuleName("APP") to GradleModuleInclude(":app")
        )

        GradleSettingsPatcher.patch(settings, coordinates)

        assertThat(settings.included).apply {
            contains(":library")
            contains(":app")
        }
    }
}