package io.labs.dotanuki.magicmodules.internal.util

import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

internal inline fun <reified T> T.logger(): Logger = Logging.getLogger(T::class.java)

internal fun Logger.i(message: String) = log(LogLevel.INFO, tag(message))
internal fun Logger.e(message: String) = log(LogLevel.ERROR, tag(message))
internal fun Logger.l(message: String) = log(LogLevel.LIFECYCLE, tag(message))

private fun tag(message: String) = "[MagicModulesPlugin] $message"