package ru.surfstudio.android.build.model.json

import com.beust.klaxon.Json
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.module.Library
import ru.surfstudio.android.build.model.module.Sample
import ru.surfstudio.android.build.utils.EMPTY_INT
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.Transformable

/**
 * Represent information about component json object
 */
data class ComponentJson(
        val id: String = EMPTY_STRING,
        val version: String = EMPTY_STRING,
        val stable: Boolean = false,
        @Json(name = "unstable_version") val unstableVersion: Int = EMPTY_INT,
        val dir: String = EMPTY_STRING,
        val libs: List<LibJson> = listOf(),
        val samples: List<SampleJson> = listOf(),
        @Json(name = "has_mirror") val hasMirror: Boolean = false,
        @Json(name = "mirror_repo") val mirrorRepo: String = EMPTY_STRING
) : Transformable<Component> {

    override fun transform() = Component(
            name = id,
            directory = dir,
            baseVersion = version,
            stable = stable,
            unstableVersion = unstableVersion,
            hasMirror = hasMirror,
            mirrorRepo = mirrorRepo,
            libraries = libs.map { jsonLib ->
                Library(
                        name = jsonLib.name,
                        directory = "$dir/${jsonLib.dir}",
                        artifactName = jsonLib.artifactName,
                        thirdPartyDependencies = jsonLib.thirdPartyDependencies
                                .map(DependencyJson::transformToThirdPartyDependency),
                        androidStandardDependencies = jsonLib.androidStandardDependencies
                                .map(DependencyJson::transformToAndroidStandardDependency)
                )
            },
            samples = samples.map {
                Sample(
                        name = it.name,
                        directory = "$dir/${it.dir}"
                )
            }
    )
}