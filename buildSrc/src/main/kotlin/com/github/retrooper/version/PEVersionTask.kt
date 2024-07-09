package com.github.retrooper.version

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class PEVersionTask : DefaultTask() {

    companion object {
        const val TASK_NAME = "generateVersionsFile"
    }

    @get:Input
    abstract var packageName: String

    @get:Input
    abstract var version: String

    @get:OutputDirectory
    abstract var outputDir: Provider<Directory>

    @TaskAction
    fun generate() {
        val dir = outputDir.get().dir(packageName.replace('.', '/'))
        dir.asFile.mkdirs()

        val file = dir.file("PEVersions.java").asFile
        if (!file.exists()) {
            file.createNewFile()
        }

        val ver = Version.fromString(version)
        logger.info("Generating PEVersions.java with version $ver")

        file.writeText("""
            /**
             * This file is generated by the auto-version task. Modifying it will have no effect.
             */
            package $packageName;
            
            public final class PEVersions {
            
                public static final String RAW = "$version";
                public static final PEVersion CURRENT = new PEVersion(${ver.major}, ${ver.minor}, ${ver.patch}, ${ver.quotedSnapshotCommit()});
                public static final PEVersion UNKNOWN = new PEVersion(0, 0, 0);
                
                private PEVersions() {
                    throw new IllegalStateException();
                }
            }
        """.trimIndent())
    }

    private data class Version(
        val major: Int,
        val minor: Int,
        val patch: Int,
        val snapshotCommit: String?
    ) {
        companion object {
            private val REGEX = Regex("""(\d+)\.(\d+)\.(\d+)(?:\+([0-9a-f]+)-SNAPSHOT)?""")

            fun fromString(version: String): Version {
                val match = REGEX.matchEntire(version) ?: throw IllegalArgumentException("Invalid version: $version")
                return Version(
                    match.groupValues[1].toInt(),
                    match.groupValues[2].toInt(),
                    match.groupValues[3].toInt(),
                    match.groupValues[4].ifEmpty { null }
                )
            }
        }

        fun quotedSnapshotCommit(): String {
            if (snapshotCommit == null) {
                return "null"
            }
            return "\"$snapshotCommit\"";
        }
    }

}
