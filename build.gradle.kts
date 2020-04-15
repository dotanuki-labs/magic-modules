buildscript {

    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(Libraries.kotlinGradlePlugin)
        classpath(Libraries.testLoggerPlugin)
        classpath(Libraries.ktLintGradlePlugin)
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