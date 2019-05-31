package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.EMPTY_INT
import ru.surfstudio.android.build.EMPTY_STRING
import ru.surfstudio.android.build.model.dependency.AndroidStandardDependency
import ru.surfstudio.android.build.model.dependency.ThirdPartyDependency
import ru.surfstudio.android.build.model.json.ComponentJson
import ru.surfstudio.android.build.model.module.Library
import ru.surfstudio.android.build.model.module.Module
import ru.surfstudio.android.build.model.module.Sample

/**
 * Represent information about component
 */
data class Component(
        val name: String = EMPTY_STRING,
        val directory: String = EMPTY_STRING,
        val baseVersion: String = EMPTY_STRING, // Version from components.json
        var projectVersion: String = EMPTY_STRING, // Project version
        val stable: Boolean = false,
        val unstableVersion: Int = EMPTY_INT,
        val libraries: List<Library> = listOf(),
        val samples: List<Sample> = listOf(),
        val hasMirror: Boolean = false,
        val mirrorRepo: String = EMPTY_STRING
) {

    companion object {

        fun create(componentJson: ComponentJson) = Component(
                name = componentJson.id,
                directory = componentJson.dir,
                baseVersion = componentJson.version,
                stable = componentJson.stable,
                unstableVersion = componentJson.unstableVersion,
                hasMirror = componentJson.hasMirror,
                mirrorRepo = componentJson.mirrorRepo,
                libraries = componentJson.libs.map { jsonLib ->
                    Library(
                            name = jsonLib.name,
                            directory = "${componentJson.dir}/${jsonLib.dir}",
                            artifactName = jsonLib.artifactName,
                            thirdPartyDependencies = jsonLib.thirdPartyDependencies
                                    .map { ThirdPartyDependency.create(it) },
                            androidStandardDependencies = jsonLib.androidStandardDependencies
                                    .map { AndroidStandardDependency.create(it) }
                    )
                },
                samples = componentJson.samples.map(Sample.Companion::create)
        )
    }

    /**
     * Get components module
     */
    fun getModules(): List<Module> = libraries + samples
}