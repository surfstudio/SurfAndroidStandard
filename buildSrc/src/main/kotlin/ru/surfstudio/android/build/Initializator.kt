package ru.surfstudio.android.build

import com.beust.klaxon.Klaxon
import org.gradle.api.Project
import ru.surfstudio.android.build.model.Component
import java.io.File
import java.lang.RuntimeException

private const val COMPONENTS_JSON_FILE_PATH = "buildSrc/components.json"
private const val DEFAULT_VERSION_NAME_KEY = "defaultVersionName"

/**
 * Components define all information for android-standard structure
 *
 * This value exist for all build time
 */
var components: List<Component> = emptyList()

/**
 * Represent information about module and module's directory
 *
 * @return list of pair module's name to module's directory
 */
fun getModulesInfo(): List<Pair<String, String>> {
    if (components.isEmpty()) {
        components = parseComponentJson()
        checkComponentsFolders()
    }
    val names = ArrayList<Pair<String, String>>()
    components.forEach { component ->
        component.libs.forEach { lib ->
            names.add(":${lib.name}" to "${component.dir}/${lib.dir}")
        }
        component.samples.forEach { sample ->
            names.add(":${sample.name}" to "${component.dir}/${sample.dir}")
        }
    }
    return names
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
fun getModuleVersionName(gradleProject: Project): String {
    val defaultVersionName = gradleProject.property(DEFAULT_VERSION_NAME_KEY) as? String
            ?: EMPTY_STRING

    components.find {
        it.libs.map { lib -> lib.name }.contains(gradleProject.name)
    }?.let { component ->
        var versionName = component.version
        val projectInformation = gradleProject.getProjectInformation()

        if (!component.stable) versionName += "-alpha.${component.unstableVersion}"
        if (!projectInformation.isEmpty) versionName += "-${projectInformation.name}.${projectInformation.version}"

        return versionName
    }

    return defaultVersionName
}

/**
 * Return artifactName from component.json by libName
 */
fun getArtifactNameByLibName(libName: String): String = components.flatMap { it.libs }
        .find { it.name == libName }
        ?.artifactName ?: EMPTY_STRING

/**
 * Return artifactNames from component.json by libName for all standard dependencies
 */
fun getDependsArtifactNamesByLibName(libName: String): List<String> {
    val libs = components.flatMap { it.libs }
    val depNames = libs.find { it.name == libName }
            ?.androidStandardDependencies
            ?.map { it.name } ?: return emptyList()

    return libs.filter { depNames.contains(it.name) }.map { it.artifactName }
}

/**
 * Parsing components.json file
 * @return list of components
 */
private fun parseComponentJson(): List<Component> {
    return Klaxon().parseArray(File(COMPONENTS_JSON_FILE_PATH))
            ?: throw RuntimeException("Can't parse components.json")
}

/**
 * Check components directories for exist
 */
private fun checkComponentsFolders() {
    components.forEach { component ->

        //check component "dir"
        if (!File(component.dir).exists()) {
            throw RuntimeException(
                    "Component ${component.id} doesn't have existing directory. " +
                            "Please, check components.json and create folder with 'dir' name."
            )
        }

        //check libs
        component.libs.forEach { lib ->
            if (!File("${component.dir}/${lib.dir}").exists()) {
                throw RuntimeException(
                        "Component ${component.id} with library ${lib.name} doesn't " +
                                "have existing directory ${lib.dir}. Please, check components.json" +
                                " and create folder with 'dir' name."
                )
            }
        }

        //check samples
        component.samples.forEach { sample ->
            if (!File("${component.dir}/${sample.dir}").exists()) {
                throw RuntimeException(
                        "Component ${component.id} has sample $${sample.name}, but folder doesn't exist."
                )
            }
        }
    }
}