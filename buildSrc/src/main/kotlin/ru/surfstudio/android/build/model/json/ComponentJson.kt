package ru.surfstudio.android.build.model.json

import com.google.gson.annotations.SerializedName
import ru.surfstudio.android.build.EMPTY_INT
import ru.surfstudio.android.build.EMPTY_STRING
import ru.surfstudio.android.build.model.Component

/**
 * Represent information about component json object
 */
data class ComponentJson(
        val id: String = EMPTY_STRING,
        val version: String = EMPTY_STRING,
        @SerializedName("unstable_version") val unstableVersion: Int = EMPTY_INT,
        val stable: Boolean = false,
        val dir: String = EMPTY_STRING,
        val libs: List<LibJson> = listOf(),
        val samples: List<SampleJson> = listOf(),
        @SerializedName("has_mirror") val hasMirror: Boolean = false,
        @SerializedName("mirror_repo") val mirrorRepo: String = EMPTY_STRING
) {

    companion object {

        fun create(component: Component) = ComponentJson(
                id = component.name,
                dir = component.directory,
                version = component.baseVersion,
                stable = component.stable,
                unstableVersion = component.unstableVersion,
                hasMirror = component.hasMirror,
                mirrorRepo = component.mirrorRepo,
                libs = component.libraries.map { lib ->
                    LibJson(
                            name = lib.name,
                            dir = lib.folder,
                            artifactName = lib.artifactName,
                            thirdPartyDependencies = lib.thirdPartyDependencies
                                    .map(DependencyJson.Companion::create),
                            androidStandardDependencies = lib.androidStandardDependencies
                                    .map(DependencyJson.Companion::create)
                    )
                },
                samples = component.samples.map(SampleJson.Companion::create)
        )
    }
}