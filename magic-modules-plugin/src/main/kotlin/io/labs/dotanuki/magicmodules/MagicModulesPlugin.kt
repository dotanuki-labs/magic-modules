package io.labs.dotanuki.magicmodules

import io.labs.dotanuki.magicmodules.internal.BuildScriptsProcessor
import io.labs.dotanuki.magicmodules.internal.GradleSettingsPatcher
import io.labs.dotanuki.magicmodules.internal.ModuleNamesWriter
import io.labs.dotanuki.magicmodules.internal.ProjectStructureParser
import io.labs.dotanuki.magicmodules.internal.ResolveOutputFilesDir
import io.labs.dotanuki.magicmodules.internal.model.ParserRawContent
import io.labs.dotanuki.magicmodules.internal.util.i
import io.labs.dotanuki.magicmodules.internal.util.logger
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import kotlin.system.measureTimeMillis

class MagicModulesPlugin : Plugin<Settings> {

    override fun apply(target: Settings) =
        target.computeModulesAndPatchSettings()

    private fun Settings.computeModulesAndPatchSettings() {
        val extension = registerPluginExtension()
        gradle.settingsEvaluated {
            logger().i("Processing :: Started")
            val structureParser = ProjectStructureParser(
                ParserRawContent(
                    includeBuildDir = extension.includeBuildDir,
                    maxDepthToBuildScript = extension.maxDepthToBuildScript,
                    rawApplicationPlugins = extension.rawApplicationPlugins,
                    rawJavaLibraryPlugins = extension.rawJavaLibraryPlugins,
                    rawJavaLibraryUsingApplyFrom = extension.rawJavaLibraryUsingApplyFrom,
                    rawLibraryPlugins = extension.rawLibraryPlugins,
                    rawLibraryUsingApplyFrom = extension.rawLibraryUsingApplyFrom
                )
            )
            val spent = measureTimeMillis { parseAndProcessScripts(structureParser, extension) }
            logger().i("Processing :: Done. Processing time -> $spent milliseconds")
        }
    }

    private fun Settings.parseAndProcessScripts(
        structureParser: ProjectStructureParser,
        extension: MagicModulesExtension
    ) {
        val parsedStructure = structureParser.parse(settingsDir)
        val processedScripts = BuildScriptsProcessor.process(parsedStructure)
        processedScripts.forEach { processed ->
            GradleSettingsPatcher.patch(this, processed, extension)
            ModuleNamesWriter.write(
                folder = ResolveOutputFilesDir.using(settingsDir, extension.includeBuildDir),
                moduleType = processed.moduleType,
                coordinates = processed.coordinates
            )
        }
    }

    private fun Settings.registerPluginExtension() =
        extensions.create("magicModules", MagicModulesExtension::class.java).also {
            logger().i("Processing :: Plugin configuration -> Include app modules = ${it.includeApps}")
        }
}