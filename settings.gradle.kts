rootProject.name = "otus-template-patterns"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.version.toml"))
        }
    }
}

pluginManagement {
    repositories {
        mavenCentral()
    }
}


include("sqrt")