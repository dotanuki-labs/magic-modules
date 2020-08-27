package io.labs.dotanuki.magicmodules.tests.internal

import io.labs.dotanuki.magicmodules.internal.MagicModulesError
import io.labs.dotanuki.magicmodules.internal.model.CanonicalModuleName
import io.labs.dotanuki.magicmodules.internal.model.GradleBuildScript
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleInclude
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType
import io.labs.dotanuki.magicmodules.internal.util.ExtractCoordinates
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.ThrowableAssert.ThrowingCallable
import org.junit.Test
import java.io.File

class ExtractCoordinatesTests {

    @Test
    fun `should throw when coordinates extraction not possible`() {

        val projectName = "friendsfinder"
        val scriptFilePath =
            "${File.separator}home${File.separator}Dev${File.separator}blogger${File.separator}app${File.separator}build.gradle"
        val buildScript = GradleBuildScript(scriptFilePath, GradleModuleType.APPLICATION)

        val execution = ThrowingCallable { ExtractCoordinates(projectName, buildScript).gradleInclude() }

        assertThatThrownBy(execution).isEqualTo(MagicModulesError.CantExtractGradleCoordinates)
    }

    @Test
    fun `should extract coordinates with one level`() {
        val projectName = "awesome"
        val scriptFilePath =
            "${File.separator}home${File.separator}AndroidStudioProjects${File.separator}awesome${File.separator}app${File.separator}build.gradle"
        val buildScript = GradleBuildScript(scriptFilePath, GradleModuleType.APPLICATION)

        val extractor = ExtractCoordinates(projectName, buildScript)
        val extractedInclude = extractor.gradleInclude()
        val extractedName = extractor.modulePaths()

        val expectedInclude = GradleModuleInclude(":app")
        val expectedName = listOf(CanonicalModuleName("app"))

        assertThat(extractedInclude).isEqualTo(expectedInclude)
        assertThat(extractedName).isEqualTo(expectedName)
    }

    @Test
    fun `should extract gradle module with two levels`() {

        val projectName = "twitter-clone"
        val scriptFilePath =
            "${File.separator}Desktop${File.separator}twitter-clone${File.separator}features${File.separator}login${File.separator}build.gradle.kts"
        val buildScript = GradleBuildScript(scriptFilePath, GradleModuleType.LIBRARY)

        val extractor = ExtractCoordinates(projectName, buildScript)
        val extractedInclude = extractor.gradleInclude()
        val extractedName = extractor.modulePaths()

        val expectedInclude = GradleModuleInclude(":features:login")
        val expectedName = listOf(
            CanonicalModuleName("features"),
            CanonicalModuleName("login")
        )

        assertThat(extractedInclude).isEqualTo(expectedInclude)
        assertThat(extractedName).isEqualTo(expectedName)
    }

    @Test
    fun `should extract gradle module with three levels`() {

        val projectName = "coronatracker"
        val scriptFilePath =
            "${File.separator}Desktop${File.separator}coronatracker${File.separator}features${File.separator}login${File.separator}recover${File.separator}build.gradle.kts"
        val buildScript = GradleBuildScript(scriptFilePath, GradleModuleType.LIBRARY)

        val extractor = ExtractCoordinates(projectName, buildScript)
        val extractedInclude = extractor.gradleInclude()
        val extractedName = extractor.modulePaths()

        val expectedInclude = GradleModuleInclude(":features:login:recover")
        val expectedName = listOf(
            CanonicalModuleName("features"),
            CanonicalModuleName("login"),
            CanonicalModuleName("recover")
        )

        assertThat(extractedInclude).isEqualTo(expectedInclude)
        assertThat(extractedName).isEqualTo(expectedName)
    }
}