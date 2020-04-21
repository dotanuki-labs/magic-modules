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

        val spent = measureTimeMillis { computeModulesAndPatchSettings(target) }

        logger().i("Done !!! Processing time -> $spent milliseconds (~ ${spent / 60000} minutes)")
    }

    private fun computeModulesAndPatchSettings(target: Settings) {
        with(target) {

            val extension = extensions.create("magicModules", MagicModulesExtension::class.java)
            val parsedStructure = ProjectStructureParser().parse(settingsDir)
            val processedScripts = BuildScriptsProcessor.process(parsedStructure)

            processedScripts
                .forEach { processed ->
                    GradleSettingsPatcher.patch(target, processed, extension)
                    ModuleNamesWriter.write(
                        folder = ResolveOutputFilesDir.using(settingsDir, extension),
                        filename = processed.moduleType.conventionedFileName(),
                        coordinates = processed.coordinates
                    )
                }
        }
    }
}