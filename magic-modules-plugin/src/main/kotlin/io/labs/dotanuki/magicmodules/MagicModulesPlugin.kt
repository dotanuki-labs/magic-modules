package io.labs.dotanuki.magicmodules

import io.labs.dotanuki.magicmodules.internal.BuildScriptsProcessor
import io.labs.dotanuki.magicmodules.internal.GradleSettingsPatcher
import io.labs.dotanuki.magicmodules.internal.ModuleNamesWriter
import io.labs.dotanuki.magicmodules.internal.ProjectStructureParser
import io.labs.dotanuki.magicmodules.internal.ResolveOutputFilesDir
import io.labs.dotanuki.magicmodules.internal.util.i
import io.labs.dotanuki.magicmodules.internal.util.logger
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import kotlin.system.measureTimeMillis

class MagicModulesPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        logger().i("Processing :: Started")
        val spent = measureTimeMillis { target.computeModulesAndPatchSettings() }
        logger().i("Processing :: Done. Processing time -> $spent milliseconds")
    }

    private fun Settings.computeModulesAndPatchSettings() {

        val extension = registerPluginExtension()
        val parsedStructure = ProjectStructureParser().parse(settingsDir)
        val processedScripts = BuildScriptsProcessor.process(parsedStructure)

        gradle.settingsEvaluated {
            processedScripts
                .forEach { processed ->
                    GradleSettingsPatcher.patch(this, processed, extension)
                    ModuleNamesWriter.write(
                        folder = ResolveOutputFilesDir.using(settingsDir),
                        filename = processed.moduleType.conventionedFileName(),
                        coordinates = processed.coordinates
                    )
                }
        }
    }

    private fun Settings.registerPluginExtension() =
        extensions.create("magicModules", MagicModulesExtension::class.java).also {
            logger().i("Processing :: Plugin configuration -> Include app modules = ${it.includeApps}")
        }
}