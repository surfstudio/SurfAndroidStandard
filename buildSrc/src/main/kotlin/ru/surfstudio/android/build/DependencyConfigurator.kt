package ru.surfstudio.android.build

import org.gradle.api.Project
import ru.surfstudio.android.build.artifactory.ArtifactoryConfig
import ru.surfstudio.android.build.model.dependency.Dependency

private const val LIBRARY_VERSIONS_KEY = "libraryVersions"
private const val IMPLEMENTATION_DEP_TYPE = "implementation"
private const val API_DEP_TYPE = "api"
private const val KAPT_DEP_TYPE = "kapt"
private const val TEST_IMPLEMENTATION_DEP_TYPE = "testImplementation"
private const val ANDROID_TEST_IMPLEMENTATION_DEP_TYPE = "androidTestImplementation"
private const val KAPT_TEST_DEP_TYPE = "kaptTest"
private const val KAPT_ANDROID_TEST_DEP_TYPE = "kaptAndroidTest"
private const val CLASSPATH_DEP_TYPE = "classpath"

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
     * If current project is not mirror and dependency is not contained in current project then
     * add in with artifactory name else add locally
     *
     * @param project - project
     * @param dependencyName - dependency name
     */
    @JvmStatic
    fun projectImplementation(project: Project, dependencyName: String) {
        if (!isProjectIncluded(project, dependencyName)) {
            addDependency(project, IMPLEMENTATION_DEP_TYPE, getDependencyArtifactoryName(dependencyName))
        } else {
            addDependency(project, IMPLEMENTATION_DEP_TYPE, project.rootProject.project(dependencyName))
        }
    }

    /**
     * Add dependency to project with "testImplementation"
     * If current project is not mirror and dependency is not contained in current project then
     * add in with artifactory name else add locally
     *
     * @param project - project
     * @param dependencyName - dependency name
     */
    @JvmStatic
    fun projectTestImplementation(project: Project, dependencyName: String) {
        if (!isProjectIncluded(project, dependencyName)) {
            addDependency(project, TEST_IMPLEMENTATION_DEP_TYPE, getDependencyArtifactoryName(dependencyName))
        } else {
            addDependency(project, TEST_IMPLEMENTATION_DEP_TYPE, project.rootProject.project(dependencyName))
        }
    }

    /**
     * Add dependency to project with "androidTestImplementation"
     * If current project is not mirror and dependency is not contained in current project then
     * add in with artifactory name else add locally
     *
     * @param project - project
     * @param dependencyName - dependency name
     */
    @JvmStatic
    fun projectAndroidTestImplementation(project: Project, dependencyName: String) {
        if (!isProjectIncluded(project, dependencyName)) {
            addDependency(project, ANDROID_TEST_IMPLEMENTATION_DEP_TYPE, getDependencyArtifactoryName(dependencyName))
        } else {
            addDependency(project, ANDROID_TEST_IMPLEMENTATION_DEP_TYPE, project.rootProject.project(dependencyName))
        }
    }

    /**
     * Add dependency to project with "implementation"
     *
     * @param project - project
     * @param dep - dependency name
     */
    @JvmStatic
    fun implementation(project: Project, dep: String) {
        addDependency(project, IMPLEMENTATION_DEP_TYPE, getDependencyNameWithVersion(dep))
    }

    /**
     * Add dependency to project with "api"
     *
     * @param project - project
     * @param dep - dependency name
     */
    @JvmStatic
    fun api(project: Project, dep: String) {
        addDependency(project, API_DEP_TYPE, getDependencyNameWithVersion(dep))
    }

    /**
     * Add dependency to project with "kapt"
     *
     * @param project - project
     * @param dep - dependency name
     */
    @JvmStatic
    fun kapt(project: Project, dep: String) {
        addDependency(project, KAPT_DEP_TYPE, getDependencyNameWithVersion(dep))
    }

    /**
     * Add dependency to project with "testImplementation"
     *
     * @param project - project
     * @param dep - dependency name
     */
    @JvmStatic
    fun testImplementation(project: Project, dep: String) {
        addDependency(project, TEST_IMPLEMENTATION_DEP_TYPE, getDependencyNameWithVersion(dep))
    }

    /**
     * Add dependency to project with "kaptTest"
     *
     * @param project - project
     * @param dep - dependency name
     */
    @JvmStatic
    fun kaptTest(project: Project, dep: String) {
        addDependency(project, KAPT_TEST_DEP_TYPE, getDependencyNameWithVersion(dep))
    }

    /**
     * Add dependency to project with "kaptAndroidTest"
     *
     * @param project - project
     * @param dep - dependency name
     */
    @JvmStatic
    fun kaptAndroidTest(project: Project, dep: String) {
        addDependency(project, KAPT_ANDROID_TEST_DEP_TYPE, getDependencyNameWithVersion(dep))
    }

    /**
     * Add dependency to project with "androidTestImplementation"
     *
     * @param project - project
     * @param dep - dependency name
     */
    @JvmStatic
    fun androidTestImplementation(project: Project, dep: String) {
        addDependency(project, ANDROID_TEST_IMPLEMENTATION_DEP_TYPE, getDependencyNameWithVersion(dep))
    }

    /**
     * Add dependency to project with "classpath"
     *
     * @param project - project
     * @param dep - dependency name
     */
    @JvmStatic
    fun classpath(project: Project, dep: String) {
        project.buildscript.dependencies.add(CLASSPATH_DEP_TYPE, getDependencyNameWithVersion(dep))
    }

    private fun addThirdPartyDependencies(project: Project, dependencies: List<Dependency>) {
        dependencies.forEach {
            addDependency(project, it.type.gradleType, "${it.name}:${libraryVersions[it.name]}")
        }
    }

    private fun addAndroidStandardDependencies(project: Project, dependencies: List<Dependency>) {
        dependencies.forEach {
            if (!isProjectIncluded(project, it.name)) {
                addDependency(project, it.type.gradleType, getDependencyArtifactoryName(it.name))
            } else {
                addDependency(project, it.type.gradleType, project.rootProject.project(":${it.name}"))
            }
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

    /**
     * @param dependencyName dependency name for creating artifactory name
     *
     * @return artifactory name for dependency
     */
    private fun getDependencyArtifactoryName(dependencyName: String): String {
        val group = ArtifactoryConfig.ANDROID_STANDARD_GROUP_ID
        val name = Components.getArtifactName(dependencyName)
        val version = Components.getModuleVersion(dependencyName)
        return "$group:$name:$version"
    }

    /**
     * checks if project with [gradleProjectName] included in current project [project]
     *
     * @return true if included
     */
    private fun isProjectIncluded(project: Project, gradleProjectName: String): Boolean {
        val allProjects = project.rootProject.allprojects
        return allProjects.map { it.name }.contains(gradleProjectName)
    }
}