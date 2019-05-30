package ru.surfstudio.android.build

import org.gradle.api.Project
import ru.surfstudio.android.build.model.Dependency

private const val LIBRARY_VERSIONS_KEY = "libraryVersions"
private const val IMPLEMENTATION_DEP_TYPE = "implementation"
private const val API_DEP_TYPE = "api"

/**
 * Encapsulate working with gradle dependencies
 */
object DependencyConfigurator {

    private var libraryVersions: Map<String, String> = emptyMap()

    /**
     * Configure dependencies for project
     *
     * It uses components.json to get dependencies
     */
    @JvmStatic
    fun configure(project: Project) {
        if (libraryVersions.isEmpty()) {
            libraryVersions = project.property(LIBRARY_VERSIONS_KEY) as Map<String, String>
        }

        Components.value.forEach { component ->
            component.libraries
                    .find { it.name == project.name }
                    ?.let {
                        addThirdPartyDependencies(project, it.thirdPartyDependencies)
                        addAndroidStandardDependencies(project, it.androidStandardDependencies)
                    }
        }
    }

    /**
     * Add dependency to project with "implementation"
     *
     * @param project - project
     * @param dep - dependency name
     */
    fun projectImplementation(project: Project, gradleProjectName: String) {
        addDependency(project, IMPLEMENTATION_DEP_TYPE, project.rootProject.project(gradleProjectName))
    }

    /**
     * Add dependency to project with "implementation"
     *
     * @param project - project
     * @param dep - dependency name
     */
    fun implementation(project: Project, dep: String) {
        addDependency(project, IMPLEMENTATION_DEP_TYPE, getDependencyNameWithVersion(dep))
    }

    /**
     * Add dependency to project with "api"
     *
     * @param project - project
     * @param dep - dependency name
     */
    fun api(project: Project, dep: String) {
        addDependency(project, API_DEP_TYPE, getDependencyNameWithVersion(dep))
    }

    private fun addThirdPartyDependencies(project: Project, dependencies: List<Dependency>) {
        dependencies.forEach {
            addDependency(project, it.type, "${it.name}:${libraryVersions[it.name]}")
        }
    }

    private fun addAndroidStandardDependencies(project: Project, dependencies: List<Dependency>) {
        dependencies.forEach {
            addDependency(project, it.type, project.rootProject.project(":${it.name}"))
        }
    }

    /**
     * Add dependency to project
     *
     * @param project - project
     * @param type - dependency type e.x. implementation, api...
     * @param dependency - dependency name
     */
    private fun addDependency(project: Project, type: String, dependency: Any) {
        project.dependencies.add(type, dependency)
    }

    /**
     * Return dependency name with dependency version
     */
    private fun getDependencyNameWithVersion(shortDependencyName: String): String {
        return "$shortDependencyName:${libraryVersions[shortDependencyName]}"
    }
}