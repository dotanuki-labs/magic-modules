package io.labs.dotanuki.magicmodules.tests.internal

import io.labs.dotanuki.magicmodules.MagicModulesExtension
import io.labs.dotanuki.magicmodules.internal.ResolveOutputFilesDir
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class ResolveOutputFilesDirTests {

    @get:Rule val tempFolder = TemporaryFolder()

    @Test fun `resolve output with default package under buildSrc`() {
        val targetFolder = tempFolder.newFolder()
        val resolved = ResolveOutputFilesDir.using(targetFolder, MagicModulesExtension())

        val expected = "$targetFolder/buildSrc/src/main/kotlin"
        assertThat(resolved.path).isEqualTo(expected)
    }

    @Test fun `resolve output with custom package under buildSrc`() {

        val customExtension = MagicModulesExtension().apply {
            customPackageUnderBuildSrc = "modules"
        }

        val targetFolder = tempFolder.newFolder()
        val resolved = ResolveOutputFilesDir.using(targetFolder, customExtension)

        val expected = "$targetFolder/buildSrc/src/main/kotlin/modules"
        assertThat(resolved.path).isEqualTo(expected)
    }
}