plugins {
    kotlin("jvm") version "2.0.21"
    `maven-publish`
}

group = "off.kys.kli"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])

            groupId = "off.kys.kli"
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