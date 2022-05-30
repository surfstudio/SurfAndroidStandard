package ru.surfstudio.android.build

import ru.surfstudio.android.build.exceptions.component.ComponentNotFoundForStandardDependencyException
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.json.ComponentJson
import ru.surfstudio.android.build.model.module.Library
import ru.surfstudio.android.build.model.module.Module
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.createCompositeVersion

/**
 * Project value
 */
object Components {

    var value: List<Component> = emptyList()
        set(value) {
            field = value
            libraries = value.flatMap { it.libraries }
            setComponentsForAndroidStandardDependencies()
        }

    var libraries: List<Library> = emptyList()

    /**
     * Create value from json value
     */
    fun init(componentJsons: List<ComponentJson>) {
        value = componentJsons.map(ComponentJson::transform)
    }

    /**
     * Get moduleVersionName
     *
     * There are 2 types of version:
     * X.Y.Z-unstableVersion - projectSnapshotName is empty
     * X.Y.Z-projectSnapshotName.projectSnapshotVersion - projectSnapshotName isn't empty
     */
    @JvmStatic
    fun getModuleVersion(moduleName: String): String {
        if (value.isEmpty()) return EMPTY_STRING

        if (value.any { it.projectVersion.isEmpty() }) configModuleVersions()

        val component = value.find { it.getModules().map(Module::name).contains(moduleName) }

        return component?.projectVersion ?: EMPTY_STRING
    }

    /**
     * Get artifact name for library
     *
     * @param libraryName - library name
     */
    @JvmStatic
    fun getArtifactName(libraryName: String): String {
        return libraries.find { it.name == libraryName }?.artifactName.orEmpty()
    }

    /**
     * Get artifact description for library
     *
     * @param libraryName - library name
     */
    @JvmStatic
    fun getArtifactDescription(libraryName: String): String {
        return libraries.find { it.name == libraryName }?.description.orEmpty()
    }

    /**
     * Get artifact url for library
     *
     * @param libraryName - library name
     */
    @JvmStatic
    fun getArtifactUrl(libraryName: String): String {
        return libraries.find { it.name == libraryName }?.url.orEmpty()
    }

    /**
     * Is library from component
     */
    @JvmStatic
    fun isLibraryFromComponent(libraryName: String, componentName: String): Boolean {
        return value.find { it.name == componentName }
            ?.libraries
            ?.any { it.name == libraryName }
            ?: false
    }

    /**
     * Set components for android standard dependencies
     */
    private fun setComponentsForAndroidStandardDependencies() {
        val libNameCompMap: Map<String, Component?> = libraries.map { lib ->
            lib.name to value.find { it.libraries.contains(lib) }
        }.toMap()

        value.forEach { component ->
            component.libraries.forEach { library ->
                library.androidStandardDependencies.forEach { dependency ->
                    dependency.component = libNameCompMap[dependency.name]
                        ?: throw ComponentNotFoundForStandardDependencyException(dependency.name)
                }
            }
        }
    }

    /**
     * Create project versions for components
     */
    private fun configModuleVersions() {
        val configInfo = ConfigInfoProvider.globalConfigInfo

        value.forEach { component ->
            val componentVersion = createCompositeVersion(
                component.baseVersion,
                configInfo.unstableVersion,
                configInfo.projectSnapshotName,
                configInfo.projectSnapshotVersion
            )
            component.projectVersion = componentVersion
            component.libraries.forEach { it.projectVersion = componentVersion }
        }
    }
}