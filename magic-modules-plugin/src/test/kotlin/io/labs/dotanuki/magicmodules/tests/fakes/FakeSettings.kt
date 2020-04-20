package io.labs.dotanuki.magicmodules.tests.fakes

import groovy.lang.Closure
import org.gradle.StartParameter
import org.gradle.api.Action
import org.gradle.api.initialization.ConfigurableIncludedBuild
import org.gradle.api.initialization.ProjectDescriptor
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.dsl.ScriptHandler
import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.ObjectConfigurationAction
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.plugins.PluginManager
import org.gradle.caching.configuration.BuildCacheConfiguration
import org.gradle.plugin.management.PluginManagementSpec
import org.gradle.vcs.SourceControl
import java.io.File

class FakeSettings : Settings {

    val included = mutableListOf<String>()

    override fun getSettings(): Settings = notUsed()

    override fun enableFeaturePreview(feature: String) = notUsed()

    override fun getSettingsDir(): File = notUsed()

    override fun includeBuild(config: Any) = notUsed()

    override fun includeBuild(config: Any, target: Action<ConfigurableIncludedBuild>) = notUsed()

    override fun getExtensions(): ExtensionContainer = notUsed()

    override fun sourceControl(action: Action<in SourceControl>) = notUsed()

    override fun findProject(name: String): ProjectDescriptor? = notUsed()

    override fun findProject(file: File): ProjectDescriptor? = notUsed()

    override fun getPluginManagement(): PluginManagementSpec = notUsed()

    override fun getPlugins(): PluginContainer = notUsed()

    override fun pluginManagement(action: Action<in PluginManagementSpec>) = notUsed()

    override fun getRootProject(): ProjectDescriptor = notUsed()

    override fun getBuildCache(): BuildCacheConfiguration = notUsed()

    override fun getBuildscript(): ScriptHandler = notUsed()

    override fun getGradle(): Gradle = notUsed()

    override fun getPluginManager(): PluginManager = notUsed()

    override fun getRootDir(): File = notUsed()

    override fun getSourceControl(): SourceControl = notUsed()

    override fun apply(closure: Closure<Any>) = notUsed()

    override fun apply(action: Action<in ObjectConfigurationAction>) = notUsed()

    override fun apply(map: MutableMap<String, *>) = notUsed()

    override fun includeFlat(vararg module: String?) = notUsed()

    override fun buildCache(action: Action<in BuildCacheConfiguration>) = notUsed()

    override fun project(project: String): ProjectDescriptor = notUsed()

    override fun project(project: File): ProjectDescriptor = notUsed()

    override fun getStartParameter(): StartParameter = notUsed()

    override fun include(vararg modules: String) {
        modules.forEach { included += it }
    }

    private fun notUsed(): Nothing = throw IllegalAccessException("Should not be called")
}