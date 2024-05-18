plugins {
    java
    id("com.github.johnrengelman.shadow")
}

tasks {
    shadowJar {
        archiveFileName.set("packetevents-${project.name}-${project.version}.jar")
        archiveClassifier = null
    }

    build {
        dependsOn(shadowJar)
    }
}