package io.labs.dotanuki.magicmodules.tests.internal

import io.labs.dotanuki.magicmodules.internal.ProjectStructureParser
import io.labs.dotanuki.magicmodules.internal.model.GradleBuildScript
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.APPLICATION
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.BUILDSRC
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.LIBRARY
import io.labs.dotanuki.magicmodules.internal.model.GradleModuleType.ROOT_LEVEL
import io.labs.dotanuki.magicmodules.internal.model.GradleProjectStructure
import io.labs.dotanuki.magicmodules.internal.MagicModulesError
import java.io.File
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test

internal class ProjectStructureParserTests {

    private lateinit var parser: ProjectStructureParser

    @Before fun `before each test`() {
        parser = ProjectStructureParser()
    }

    @Test fun `should throw if input is not a directory`() {
        val target = fixture("not-a-project.txt")

        val execution = { Unit.also { parser.parse(target) } }

        val expectedError = MagicModulesError.CantParseProjectStructure
        assertThatThrownBy(execution).isEqualTo(expectedError)
    }

    @Test fun `should parse with no gradle projects`() {
        val target = fixture("no_gradle_projects")

        val parsed = parser.parse(target)

        val expected = GradleProjectStructure("no_gradle_projects", emptySet())
        assertThat(parsed).isEqualTo(expected)
    }

    @Test fun `should parse single module project at root level`() {
        val target = fixture("single_module_on_root")

        val parsedProject = parser.parse(target)

        val expected =
            GradleProjectStructure(
                "single_module_on_root",
                setOf(GradleBuildScript(target.resolvePath("build.gradle"), ROOT_LEVEL))
            )

        assertThat(parsedProject).isEqualTo(expected)
    }

    @Test fun `should parse multiple modules project at one level`() {
        val target = fixture("multiple_modules_one_level")

        val parsed = parser.parse(target)

        val expected = with(target) {

            GradleProjectStructure(
                "multiple_modules_one_level",
                setOf(
                    GradleBuildScript(resolvePath("build.gradle"), ROOT_LEVEL),
                    GradleBuildScript(resolvePath("buildSrc/build.gradle.kts"), BUILDSRC),
                    GradleBuildScript(resolvePath("app/build.gradle"), APPLICATION),
                    GradleBuildScript(resolvePath("common/build.gradle"), LIBRARY),
                    GradleBuildScript(resolvePath("feature/build.gradle"), LIBRARY)
                )
            )
        }

        assertThat(parsed).isEqualTo(expected)
    }

    @Test fun `should parse multiple modules project with two levels`() {
        val target = fixture("multiple_modules_two_levels")

        val parsed = parser.parse(target)

        val expected = with(target) {
            GradleProjectStructure(
                "multiple_modules_two_levels",
                setOf(
                    GradleBuildScript(resolvePath("build.gradle.kts"), ROOT_LEVEL),
                    GradleBuildScript(resolvePath("buildSrc/build.gradle.kts"), BUILDSRC),
                    GradleBuildScript(resolvePath("app/build.gradle.kts"), APPLICATION),
                    GradleBuildScript(resolvePath("common/core/build.gradle.kts"), LIBRARY),
                    GradleBuildScript(resolvePath("common/utils/build.gradle"), LIBRARY),
                    GradleBuildScript(resolvePath("features/profile/build.gradle.kts"), LIBRARY),
                    GradleBuildScript(resolvePath("features/signup/build.gradle.kts"), LIBRARY)
                )
            )
        }

        assertThat(parsed).isEqualTo(expected)
    }

    private fun File.resolvePath(relativePath: String): String = resolve(relativePath).path

    private fun fixture(name: String): File = File("$FIXTURES_FOLDER/$name")

    companion object {
        const val FIXTURES_FOLDER = "src/test/fixtures/parser"
    }
}