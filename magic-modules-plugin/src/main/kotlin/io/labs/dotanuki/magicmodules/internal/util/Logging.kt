package io.labs.dotanuki.magicmodules.internal.util

import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

internal inline fun <reified T> T.logger(): Logger = Logging.getLogger(T::class.java)

internal fun Logger.i(message: String) = log(LogLevel.INFO, tagged(message))
internal fun Logger.e(message: String) = log(LogLevel.ERROR, tagged(message))

private fun tagged(message: String) = "[MagicModulesPlugin] $message"