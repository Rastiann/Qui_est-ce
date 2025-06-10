pluginManagement {
    repositories {
        maven {
            url = uri("http://nexus.dep-info.iut-nantes.univ-nantes.prive/repository/public/")
            isAllowInsecureProtocol = true
        }
        gradlePluginPortal()
    }
}

/*
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
*/

//rootProject.name = "tp11"