/**
 * Common libraries used in this Gradle build
 */

object Dependencies {
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val testLoggerPlugin = "com.adarshr:gradle-test-logger-plugin:${Versions.testLoggerPlugin}"
    const val ktLintGradlePlugin = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.ktlintGradlePlugin}"

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val junit = "junit:junit:${Versions.junit}"
    const val assertj = "org.assertj:assertj-core:${Versions.assertj}"
}