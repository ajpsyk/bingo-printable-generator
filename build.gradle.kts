/*
 * Printable PDF Generator
 * Build: Gradle + JavaFX + iText + Jackson
 * Copyright (C) 2025 A.J. Psyk
 * Licensed under the GNU AGPL v3 or later (see LICENSE).
 */
plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.runtime") version "1.13.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.itextKernel)
    implementation(libs.itextIO)
    implementation(libs.itextSVG)
    implementation(libs.jackson)
    implementation(libs.jacksonDatabind)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

javafx {
    version = "24.0.2"
    modules = listOf("javafx.controls")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.12.1")
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

application {
    mainClass = "org.bingo.ui.Main"
}
