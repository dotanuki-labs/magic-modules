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
        logger().i("Started processing")
        val spent = measureTimeMillis { target.computeModulesAndPatchSettings() }
        logger().i("Done!!! Processing time -> $spent milliseconds (~${spent / 60000} minutes)")
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
            logger().i("Plugin configuration")
            logger().i("Include app modules -> ${it.includeApps}")
        }
}