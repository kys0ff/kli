@file:Suppress("SpellCheckingInspection")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.0.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    `maven-publish`
}

group = "com.github.kys0ff"
version = "v0.1.3"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")
    manifest {
        attributes["Main-Class"] = "off.kys.MainKt"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            groupId = "com.github.kys0ff"
            artifactId = "kli"
            version = "v0.1.3"

            pom {
                name.set("kli")
                description.set("Kli is a minimal and expressive Kotlin library for building command-line applications.")
                url.set("https://github.com/kys0ff/kli")
            }
        }
    }
}