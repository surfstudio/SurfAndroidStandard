package ru.surfstudio.android.build.model.json

import com.google.gson.annotations.SerializedName
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.module.Library
import ru.surfstudio.android.build.model.module.Sample
import ru.surfstudio.android.build.publish.PublishConfig
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.Transformable

/**
 * Represent information about component json object
 */
data class ComponentJson(
        val id: String = EMPTY_STRING,
        val version: String = EMPTY_STRING,
        val dir: String = EMPTY_STRING,
        val libs: List<LibJson> = listOf(),
        val samples: List<SampleJson> = listOf(),
        val disabled: Boolean = false,
        @SerializedName("enabled_samples") val enabledSamples: Boolean = false,
        @SerializedName("has_mirror") val hasMirror: Boolean = false,
        @SerializedName("mirror_repo") val mirrorRepo: String = EMPTY_STRING
) : Transformable<Component> {

    override fun transform() = Component(
            name = id,
            directory = dir,
            baseVersion = version,
            disabled = disabled,
            enabledSamples = enabledSamples,
            hasMirror = hasMirror,
            mirrorRepo = mirrorRepo,
            libraries = libs.map { jsonLib ->
                Library(
                        name = jsonLib.name,
                        directoryPath = "$dir/${jsonLib.dir}",
                        artifactName = jsonLib.artifactName,
                        directory = jsonLib.dir,
                        thirdPartyDependencies = jsonLib.thirdPartyDependencies
                                .map(DependencyJson::transformToThirdPartyDependency),
                        androidStandardDependencies = jsonLib.androidStandardDependencies
                                .map(DependencyJson::transformToAndroidStandardDependency),
                        description = "${jsonLib.name} library from Surf Android Standard",
                        url = "${PublishConfig.SCM_URL}/$id/${jsonLib.dir}"
                )
            },
            samples = samples.map {
                Sample(
                        name = it.name,
                        directoryPath = "$dir/${it.dir}",
                        directory = it.dir
                )
            }
    )

    constructor(component: Component) : this(
            id = component.name,
            dir = component.directory,
            version = component.baseVersion,
            hasMirror = component.hasMirror,
            mirrorRepo = component.mirrorRepo,
            libs = component.libraries.map { LibJson(it) },
            samples = component.samples.map { SampleJson(it) }
    )
}