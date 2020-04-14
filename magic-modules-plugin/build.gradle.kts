import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    with(BuildPlugins) {
        id(kotlinJVM)
        id(ktlint)
        id(testLogger)
        id(gradlePluginDevelopment)
        id(mavenPublisher)
        id(gradlePluginPublisher) version Versions.gradlePublishPlugin
    }
}

dependencies {
    implementation(Dependencies.kotlinStdLib)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.assertj)
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = Definitions.targetJDK
    }

    withType<Test>().configureEach {
        dependsOn("publishToMavenLocal")
    }
}

val sourcesJar = tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    val localRepoPath = "${rootProject.rootDir}/repo"
    repositories {
        maven(url = uri(localRepoPath))
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}

gradlePlugin {
    plugins {
        create("magic-modules-plugin") {
            id = "io.labs.dotanuki.magicmodules"
            displayName = "Magic Modules Gradle Plugin"
            description = "The easiest way to reference modules names in massive multi-module Gradle projects"
            implementationClass = "io.labs.dotanuki.magicmodules.MagicModulesPlugin"
        }
    }
}