package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.artifactory.ArtifactoryConfig
import ru.surfstudio.android.build.model.module.Library
import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Represent artifact info
 */
data class ArtifactInfo(val libraryName: String = EMPTY_STRING) {

    private var library = Library()

    private val jarFilePath: String
    private val aarFilePath: String
    private val pomFilePath: String

    init {
        Components.libraries.forEach { lib ->
            if (lib.name == libraryName) {
                library = lib
                return@forEach
            }
        }

        val prefix = "${ArtifactoryConfig.SOURCE_REPO}/${ArtifactoryConfig.ANDROID_STANDARD_GROUP_ID.replace(".", "/")}" +
                "/${library.artifactName}/${library.projectVersion}/${library.artifactName}-${library.projectVersion}"
        jarFilePath = "$prefix-sources.jar"
        aarFilePath = "$prefix.aar"
        pomFilePath = "$prefix.pom"
    }

    /**
     * Path for artifact files
     */
    fun getPath() = "\"$jarFilePath\", \"$aarFilePath\", \"$pomFilePath\""
}