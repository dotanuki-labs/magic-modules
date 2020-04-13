buildscript {

    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(Dependencies.kotlinGradlePlugin)
        classpath(Dependencies.testLoggerPlugin)
        classpath(Dependencies.ktLintGradlePlugin)
    }
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = Definitions.groupId
    version = Definitions.libraryVersion
}

tasks {
    create<Delete>("clean") {
        delete(allprojects.map { it.buildDir })
    }
}