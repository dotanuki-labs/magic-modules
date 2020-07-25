package io.labs.dotanuki.magicmodules.internal.model

sealed class GradleFoundModule(val content: String) {
    class ApplyFrom(content: String) : GradleFoundModule(content)
    class ApplyPlugin(content: String) : GradleFoundModule(content)
}