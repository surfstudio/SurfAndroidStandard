package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.model.module.Library
import ru.surfstudio.android.build.artifactory.ArtifactoryConfig

/**
 * Represent artifact info
 */
data class ArtifactInfo(val libraryName: String = EMPTY_STRING) {

    private var component = Component()
    private var library = Library()

    private val jarFilePath: String
    private val aarFilePath: String
    private val pomFilePath: String

    init {
        Components.value.forEach top@{ comp ->
            comp.libraries.forEach { lib ->
                if (lib.name == libraryName) {
                    library = lib
                    component = comp
                    return@top
                }
            }
        }

        val prefix = "${ArtifactoryConfig.SOURCE_REPO}/${ArtifactoryConfig.ANDROID_STANDARD_GROUP_ID.replace(".", "/")}" +
                "/${library.artifactName}/${component.projectVersion}/${library.artifactName}-${component.projectVersion}"
        jarFilePath = "$prefix-sources.jar"
        aarFilePath = "$prefix.aar"
        pomFilePath = "$prefix.pom"
    }

    /**
     * Path for artifact files
     */
    fun getPath() = "\"$jarFilePath\", \"$aarFilePath\", \"$pomFilePath\""
}