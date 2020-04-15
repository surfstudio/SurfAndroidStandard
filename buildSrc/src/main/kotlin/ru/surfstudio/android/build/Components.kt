package ru.surfstudio.android.build

import org.gradle.api.GradleException
import ru.surfstudio.android.build.exceptions.component.ComponentNotFoundForStandardDependencyException
import ru.surfstudio.android.build.exceptions.library.LibraryNotFoundException
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.dependency.Dependency
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
     * Function for parsing a single component from list
     */
    fun parseComponent(componentJsons: List<ComponentJson>, componentName: String): Component? =
            componentJsons.firstOrNull { it.id == componentName }?.transform()

    /**
     * Get project's module
     */
    @JvmStatic
    fun getModules(): List<Module> {
        val mirrorComponentName = GradlePropertiesManager.componentMirrorName
        val skipSamplesBuilding = GradlePropertiesManager.skipSamplesBuilding

        return if (!GradlePropertiesManager.isCurrentComponentAMirror()) {
            value.flatMap { component ->
                component.getModules(skipSamplesBuilding)
            }
        } else {
            val mirrorComponent = getComponentByName(mirrorComponentName)
            val result = mirrorComponent.libraries + mirrorComponent.samples
            if (GradlePropertiesManager.hasCommonComponent()) {
                val commonComponent = getComponentByName(GradlePropertiesManager.commonComponentNameForMirror)
                result + commonComponent.libraries + commonComponent.samples
            } else {
                result
            }
        }
    }

    /**
     * Get moduleVersionName
     *
     * There are 4 types of version:
     * 1. X.Y.Z - component is stable, projectPostfix is empty
     * 2. X.Y.Z-alpha.unstable_version - component is unstable, projectPostfix is empty
     * 3. X.Y.Z-projectPostfix.projectVersion - component is stable, projectPostfix isn't empty
     * 4. X.Y.Z-alpha.unstable_version-projectPostfix.projectVersion - component is unstable, projectPostfix isn't empty
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
        val library = value.flatMap { it.libraries }.find { it.name == libraryName }
        return library?.artifactName ?: EMPTY_STRING
    }

    /**
     * Get standard artifact names by library name
     */
    @JvmStatic
    fun getAndroidStandardDependencies(libraryName: String): List<Library> {
        val libs = value.flatMap { it.libraries }
        val standardDepNames = libs.find { it.name == libraryName }
                ?.androidStandardDependencies
                ?.map(Dependency::name) ?: return emptyList()
        return libs.filter { standardDepNames.contains(it.name) }
    }

    /**
     * Get component stability by module name
     */
    @JvmStatic
    fun getComponentStability(libraryName: String): Boolean {
        value.forEach { component ->
            component.libraries
                    .find { it.name == libraryName }
                    ?.let { return component.stable }
        }

        throw LibraryNotFoundException(libraryName)
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
        val libs = value.flatMap { it.libraries }
        val libNameCompMap: Map<String, Component?> = libs.map { lib ->
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
                    component.stable,
                    component.unstableVersion,
                    configInfo.projectSnapshotName,
                    configInfo.projectSnapshotVersion
            )
            component.projectVersion = componentVersion
            component.libraries.forEach { it.projectVersion = componentVersion }
        }
    }

    private fun getComponentByName(componentName: String): Component {
        return value.firstOrNull { it.name == componentName }
                ?: throw GradleException("Component name $componentName not found")
    }
}