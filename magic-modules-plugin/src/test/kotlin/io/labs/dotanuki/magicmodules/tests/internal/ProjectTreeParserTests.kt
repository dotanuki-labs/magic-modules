package io.labs.dotanuki.magicmodules.tests.internal

import io.labs.dotanuki.magicmodules.internal.ProjectTreeParser
import io.labs.dotanuki.magicmodules.internal.model.GradleModule
import io.labs.dotanuki.magicmodules.internal.model.ProjectTree
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.io.File

internal class ProjectTreeParserTests {

    lateinit var parser: ProjectTreeParser

    @Before fun `before each test`() {
        parser = ProjectTreeParser()
    }

    @Test fun `should throw if input is not a directory`() {
        val target = fixture("not-a-project.txt")

        val parsedProject = parser.parse(target)

        assertThat(parsedProject).isEqualTo(null)
    }

    @Test fun `should parse into empty tree if no build files found`() {
        val target = fixture("no_gradle_projects")

        val parsedProject = parser.parse(target)

        assertThat(parsedProject).isEqualTo(null)
    }

    @Test fun `should parse single module project at root level`() {
        val target = fixture("single_module_on_root")

        val parsedProject = parser.parse(target)

        val expected = ProjectTree(
            GradleModule(
                target.resolve("build.gradle").path
            )
        )

        assertThat(parsedProject).isEqualTo(expected)
    }

    @Test fun `should parse multiple modules project at one level`() {
        val target = fixture("multiple_modules_one_level")

        val parsedProject = parser.parse(target)

        val noSubprojects = emptyList<ProjectTree>()

        val projectsAtRootLevel =
            listOf("app", "common", "feature")
                .map { "$it/build.gradle" }
                .map { GradleModule(target.resolve(it).path) }
                .map { ProjectTree(it, noSubprojects) }

        val expected = ProjectTree(
            children = projectsAtRootLevel,
            module = GradleModule(target.resolve("build.gradle").path)
        )

        assertThat(parsedProject).isEqualTo(expected)
    }

    private fun fixture(name: String): File = File("$FIXTURES_FOLDER/$name")

    companion object {
        const val FIXTURES_FOLDER = "src/test/fixtures/parser"
    }
}