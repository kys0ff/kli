plugins {
    kotlin("jvm") version "2.0.21"
    `maven-publish`
}

val globalVersion = "v0.1.6"

group = "com.github.kys0ff"
version = globalVersion

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(11)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            groupId = "com.github.kys0ff"
            artifactId = "kli"
            version = globalVersion

            pom {
                name.set("kli")
                description.set("Kli is a minimal and expressive Kotlin library for building command-line applications.")
                url.set("https://github.com/kys0ff/kli")
            }
        }
    }
}