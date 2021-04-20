package io.labs.dotanuki.magicmodules.tests.internal

import io.labs.dotanuki.magicmodules.internal.BuildScriptsProcessor
import io.labs.dotanuki.magicmodules.internal.MagicModulesError
import io.labs.dotanuki.magicmodules.internal.model.CanonicalModuleName
import io.labs.dotanuki.magicmodules.internal.model.GradleBuildScript
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleInclude
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.APPLICATION
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.INCLUDE_BUILD
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.JAVA_LIBRARY
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.LIBRARY
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.ROOT_LEVEL
import io.labs.dotanuki.magicmodules.internal.model.GradleProjectStructure
import io.labs.dotanuki.magicmodules.internal.model.ProcessedScriptsResult
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.ThrowableAssert.ThrowingCallable
import org.junit.Test
import java.io.File

class BuildScriptsProcessorTests {

    @Test
    fun `should not process when missing buildSrc`() {

        val projectPath = "${File.separator}Dev${File.separator}projects${File.separator}tweetter"

        val projectStructure = GradleProjectStructure(
            "tweetter",
            setOf(
                GradleBuildScript("$projectPath${File.separator}build.gradle", ROOT_LEVEL),
                GradleBuildScript("$projectPath${File.separator}twetter-app${File.separator}build.gradle", APPLICATION),
                GradleBuildScript("$projectPath${File.separator}twetter-core${File.separator}build.gradle", LIBRARY)
            )
        )

        val execution = ThrowingCallable { BuildScriptsProcessor.process(projectStructure) }

        assertThatThrownBy(execution).isEqualTo(MagicModulesError.MissingBuildSrc)
    }

    @Test
    fun `should process library modules`() {

        val projectPath = "${File.separator}home${File.separator}projects${File.separator}awesome-grocery"

        val projectStructure = GradleProjectStructure(
            "awesome-grocery",
            setOf(
                GradleBuildScript("$projectPath${File.separator}build.gradle", ROOT_LEVEL),
                GradleBuildScript("$projectPath${File.separator}buildSrc${File.separator}build.gradle.kts", INCLUDE_BUILD),
                GradleBuildScript("$projectPath${File.separator}core${File.separator}build.gradle", LIBRARY),
                GradleBuildScript("$projectPath${File.separator}login${File.separator}build.gradle", LIBRARY),
                GradleBuildScript("$projectPath${File.separator}app${File.separator}build.gradle", APPLICATION)
            )
        )

        val processed = BuildScriptsProcessor.process(projectStructure)

        val expected = listOf(
            ProcessedScriptsResult(JAVA_LIBRARY, emptyMap()),
            ProcessedScriptsResult(
                moduleType = LIBRARY,
                coordinates = mapOf(
                    GradleModuleInclude(":core") to listOf(CanonicalModuleName("core")),
                    GradleModuleInclude(":login") to listOf(CanonicalModuleName("login"))
                )
            ),
            ProcessedScriptsResult(
                moduleType = APPLICATION,
                coordinates = mapOf(
                    GradleModuleInclude(":app") to listOf(CanonicalModuleName("app"))
                )
            )
        )

        assertThat(processed).isEqualTo(expected)
    }
}