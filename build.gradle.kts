@file:Suppress("SpellCheckingInspection")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar


plugins {
    kotlin("jvm") version "2.0.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    `maven-publish`
}

group = "off.kys.kli"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    withType<ShadowJar> {
        archiveFileName.set("kli-1.0.0.jar")

        manifest { attributes("Main-Class" to "off.kys.MainKt") }
    }
}

publishing {
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])

            groupId = "off.kys"
            artifactId = "kli"
            version = "1.0.0"
        }
    }

    repositories {
        maven {
            name = "kli"
            url = uri("https://maven.pkg.github.com/kys0ff/kli")

            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}