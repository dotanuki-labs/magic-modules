package io.labs.dotanuki.magicmodules.internal.model

internal data class ProjectTree(
    var module: GradleModule,
    var children: List<ProjectTree> = emptyList()
)