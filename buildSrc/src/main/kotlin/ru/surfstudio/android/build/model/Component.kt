package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.EMPTY_INT
import ru.surfstudio.android.build.EMPTY_STRING
import ru.surfstudio.android.build.model.json.JsonComponent

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

        fun create(jsonComponent: JsonComponent) = Component(
                name = jsonComponent.id,
                directory = jsonComponent.dir,
                baseVersion = jsonComponent.version,
                stable = jsonComponent.stable,
                unstableVersion = jsonComponent.unstableVersion,
                hasMirror = jsonComponent.hasMirror,
                mirrorRepo = jsonComponent.mirrorRepo,
                libraries = jsonComponent.libs.map { jsonLib ->
                    Library(
                            name = jsonLib.name,
                            directory = "${jsonComponent.dir}/${jsonLib.dir}",
                            artifactName = jsonLib.artifactName,
                            thirdPartyDependencies = jsonLib.thirdPartyDependencies
                                    .map(Dependency.Companion::create),
                            androidStandardDependencies = jsonLib.androidStandardDependencies
                                    .map(Dependency.Companion::create)
                    )
                },
                samples = jsonComponent.samples.map(Sample.Companion::create)
        )
    }

    /**
     * Get components module
     */
    fun getModules(): List<Module> = libraries + samples
}