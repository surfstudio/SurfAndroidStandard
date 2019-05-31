package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.Artifactory
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.EMPTY_STRING

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
        Components.value.forEach { comp ->
            comp.libraries.forEach { lib ->
                if (lib.name == libraryName) {
                    library = lib
                    component = comp
                }
            }
        }

        val prefix = "${Artifactory.SOURCE_REPO}/${Artifactory.ANDROID_STANDARD_GROUP_ID.replace(".", "/")}" +
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