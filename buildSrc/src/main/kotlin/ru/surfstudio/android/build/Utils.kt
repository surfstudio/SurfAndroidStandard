package ru.surfstudio.android.build

import groovy.lang.MissingPropertyException
import org.gradle.api.Project
import org.gradle.api.UnknownProjectException
import ru.surfstudio.android.build.model.json.JsonProjectSnapshot

const val EMPTY_STRING = ""
const val EMPTY_INT = -1
const val LIBRARY_VERSIONS_KEY = "libraryVersions"

private const val PROJECT_POSTFIX_KEY = "projectPostfix"
private const val PROJECT_VERSION_KEY = "projectVersion"

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

/**
 * Get project snapshot information
 */
fun Project.getProjectSnapshot() = JsonProjectSnapshot(
        readProperty(PROJECT_POSTFIX_KEY, EMPTY_STRING),
        readProperty(PROJECT_VERSION_KEY, EMPTY_INT)
)

/**
 * Read property from Project without Exception
 */
private fun <T> Project.readProperty(name: String, defValue: T): T {
    try {
        return property(name) as? T ?: defValue
    } catch (e: MissingPropertyException) {
        //Missing property
    } catch (e: UnknownProjectException) {
        //Missing property
    }
    return defValue
}