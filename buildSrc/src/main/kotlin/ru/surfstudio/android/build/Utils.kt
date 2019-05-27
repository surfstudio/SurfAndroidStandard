package ru.surfstudio.android.build

import org.gradle.api.Project

const val LIBRARY_VERSIONS_KEY = "libraryVersions"

private const val IMPLEMENTATION_DEP_TYPE = "implementation"
private const val API_DEP_TYPE = "api"

/**
 * Add dependency to project
 *
 * @param project - project
 * @param dep - dependency name
 * @param type - dependency type e.x. implementation, api...
 */
fun addDependency(project: Project, dep: String, type: String) {
    val libraryVersions = project.property(LIBRARY_VERSIONS_KEY) as Map<String, String>
    val depName = "$dep:${libraryVersions[dep]}"
    project.dependencies.add(type, depName)
}

/**
 * Add dependency to project with "implementation"
 *
 * @param project - project
 * @param dep - dependency name
 */
fun implementation(project: Project, dep: String) = addDependency(project, dep, IMPLEMENTATION_DEP_TYPE)

/**
 * Add dependency to project with "api"
 *
 * @param project - project
 * @param dep - dependency name
 */
fun api(project: Project, dep: String) = addDependency(project, dep, API_DEP_TYPE)