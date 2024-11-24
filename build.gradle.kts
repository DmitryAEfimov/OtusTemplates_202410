plugins {
    id("java-library")
    id("checkstyle")
    id("jacoco")
}

group = "ru.otus.templates"

checkstyle {
    toolVersion = rootProject.libs.versions.checkstyleToolVersion.get()
    configFile = file("config/checkstyle/checkstyle.xml")
    maxWarnings = 0
    maxErrors = 0
}

jacoco {
    toolVersion = rootProject.libs.versions.jacocoToolVersion.get()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "checkstyle")
    apply(plugin = "jacoco")

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(rootProject.libs.bundles.testing)
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        checkstyleMain {
            source("${projectDir}/src/main/java")
        }

        checkstyleTest {
            source("${projectDir}/src/test/java")
        }

        jacocoTestReport {
            reports {
                xml.required.set(true)
                html.required.set(true)
                csv.required.set(true)
            }

            classDirectories.setFrom(files(classDirectories.files.map {
                fileTree(it) {
                    exclude(file("$rootDir/config/coverage/excludeCoverage").readLines())
                }
            }))

            dependsOn(test)
        }

        test {
            useTestNG()
            dependsOn(checkstyleMain, checkstyleTest)
            finalizedBy(jacocoTestReport)
        }
    }
}