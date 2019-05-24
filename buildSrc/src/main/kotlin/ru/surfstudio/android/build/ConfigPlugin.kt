package ru.surfstudio.android.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.Dependency
import java.io.File
import java.lang.RuntimeException

const val COMPONENTS_JSON_FILE_PATH = "buildSrc/components.json"

class ConfigPlugin : Plugin<Project> {
    private lateinit var components: List<Component>
    private lateinit var project: Project

    override fun apply(project: Project) {
        this.project = project
        components = parseComponentJson()

        checkComponentsFolders()
        configurateDependancies()
    }

    /**
     * Check components directories
     */
    private fun checkComponentsFolders() {
        components.forEach { component ->
            //check "dir"
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
                            "Component ${component.id} with library ${lib.artifactName} doesn't " +
                                    "have existing directory ${lib.dir}. Please, check components.json" +
                                    " and create folder with 'dir' name."
                    )
                }
            }

            //check samples
            component.samples.forEach { sample ->
                if (!File("${component.dir}/${sample}").exists()) {
                    throw RuntimeException(
                            "Component ${component.id} has sample $sample, but folder doesn't exist."
                    )
                }
            }
        }
    }

    /**
     * Configurate dependancies
     */
    private fun configurateDependancies() {
        components.forEach {
            it.libs.find { it.dir == project.name }?.let {
                configurateThirdPartyDependancies(it.thirdPartyDependencies)
                configurateAndroidStandardDependancies(it.androidStandardDependencies)
            }
        }
    }

    private fun configurateThirdPartyDependancies(deps: List<Dependency>) {
        deps.forEach {
            val depName = "${it.name}:${project.property(it.name)}"
            project.dependencies.add(it.type, depName)
        }
    }

    private fun configurateAndroidStandardDependancies(deps: List<Dependency>) {
        deps.forEach {
            val depName = project.rootProject.project(":${it.name}")
            project.dependencies.add(it.type, depName)
        }
    }
}