package io.labs.dotanuki.magicmodules.tests.internal

import io.labs.dotanuki.magicmodules.internal.BuildScriptsProcessor
import io.labs.dotanuki.magicmodules.internal.model.CanonicalModuleName
import io.labs.dotanuki.magicmodules.internal.model.GradleBuildScript
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleInclude
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.APPLICATION
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.BUILDSRC
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.LIBRARY
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.ROOT_LEVEL
import io.labs.dotanuki.magicmodules.internal.model.GradleProjectStructure
import io.labs.dotanuki.magicmodules.internal.model.MagicModulesError
import io.labs.dotanuki.magicmodules.internal.model.ProcessedScriptsResult
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.ThrowableAssert.ThrowingCallable
import org.junit.Test

class BuildScriptsProcessorTests {

    @Test fun `should not process when missing buildSrc`() {

        val projectPath = "/Dev/projects/tweetter"

        val projectStructure = GradleProjectStructure(
            "tweetter",
            setOf(
                GradleBuildScript("$projectPath/build.gradle", ROOT_LEVEL),
                GradleBuildScript("$projectPath/twetter-app/build.gradle", APPLICATION),
                GradleBuildScript("$projectPath/twetter-core/build.gradle", LIBRARY)
            )
        )

        val execution = ThrowingCallable { BuildScriptsProcessor.process(projectStructure) }

        assertThatThrownBy(execution).isEqualTo(MagicModulesError.MissingBuildSrc)
    }

    @Test fun `should process library modules`() {

        val projectPath = "/home/projects/awesome-grocery"

        val projectStructure = GradleProjectStructure(
            "awesome-grocery",
            setOf(
                GradleBuildScript("$projectPath/build.gradle", ROOT_LEVEL),
                GradleBuildScript("$projectPath/buildSrc/build.gradle.kts", BUILDSRC),
                GradleBuildScript("$projectPath/core/build.gradle", LIBRARY),
                GradleBuildScript("$projectPath/login/build.gradle", LIBRARY),
                GradleBuildScript("$projectPath/app/build.gradle", APPLICATION)
            )
        )

        val processed = BuildScriptsProcessor.process(projectStructure)

        val expected = listOf(
            ProcessedScriptsResult(
                moduleType = LIBRARY,
                processedCoordinates = mapOf(
                    CanonicalModuleName("CORE") to GradleModuleInclude(":core"),
                    CanonicalModuleName("LOGIN") to GradleModuleInclude(":login")
                )
            ),
            ProcessedScriptsResult(
                moduleType = APPLICATION,
                processedCoordinates = mapOf(
                    CanonicalModuleName("APP") to GradleModuleInclude(":app")
                )
            )
        )

        assertThat(processed).isEqualTo(expected)
    }
}