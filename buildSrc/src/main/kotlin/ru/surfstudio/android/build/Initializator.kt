package ru.surfstudio.android.build

import com.beust.klaxon.Klaxon
import groovy.lang.MissingPropertyException
import org.gradle.api.Project
import org.gradle.api.UnknownProjectException
import ru.surfstudio.android.build.model.Component
import java.io.File
import java.lang.RuntimeException

private const val COMPONENTS_JSON_FILE_PATH = "buildSrc/components.json"

var components: List<Component> = emptyList()

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
 * For project snapshot define projectPostfix and projectVersion in config.gradle
 */
fun getModuleVersionName(project: Project): String {
    val defaultVersionName = project.property("defaultVersionName") as? String ?: ""

    components.find {
        it.libs.map { lib -> lib.name }.contains(project.name)
    }?.let { component ->
        var versionName = component.version

        if (!component.stable) versionName += "-alpha.${component.unstableVersion}"

        val projectPostfix = project.readProperty("projectPostfix", "")
        val projectVersion = project.readProperty("projectVersion", -1)

        if (projectPostfix.isNotEmpty() && projectVersion != -1) {
            versionName += "-$projectPostfix.$projectVersion"
        }
        return versionName
    }

    return defaultVersionName
}

private fun parseComponentJson(): List<Component> {
    return Klaxon().parseArray(File(COMPONENTS_JSON_FILE_PATH))
            ?: throw RuntimeException("Can't parse components.json")
}

/**
 * Check components directories
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
