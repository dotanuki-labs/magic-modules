package io.labs.dotanuki.magicmodules.internal.model

sealed class GradleFoundModule(val content: String) {
    class AppliedFrom(content: String) : GradleFoundModule(content)
    class AppliedPlugin(content: String) : GradleFoundModule(content)
}