package ru.surfstudio.android.build

import org.gradle.api.Project
import ru.surfstudio.android.build.model.Component
import java.io.File
import java.lang.RuntimeException

//const val COMPONENTS_JSON_FILE_PATH = "buildSrc/components.json"

//class Configurator(private val project: Project) {
//    val components: List<Component>
//
//    init {
//        components = parseComponentJson()
//    }
//
//    /**
//     * Configurate components
//     */
//    fun configurateComponents() {
//        checkComponentsFolders()
//        configurateAndroidStandardDependancies()
//    }
//
//
//    /**
//     * Check components directories
//     */
//    private fun checkComponentsFolders() {
//        components.forEach { component ->
//            //check "dir"
//            if (!File(component.dir).exists()) {
//                throw RuntimeException(
//                        "Component ${component.id} doesn't have existing directory. " +
//                                "Please, check components.json and create folder with 'dir' name."
//                )
//            }
//
//            //check libs
//            component.libs.forEach { lib ->
//                if (!File("${component.dir}/${lib.dir}").exists()) {
//                    throw RuntimeException(
//                            "Component ${component.id} with library ${lib.artifactName} doesn't " +
//                                    "have existing directory ${lib.dir}. Please, check components.json" +
//                                    " and create folder with 'dir' name."
//                    )
//                }
//            }
//
//            //check samples
//            component.samples.forEach { sample ->
//                if (!File("${component.dir}/${sample}").exists()) {
//                    throw RuntimeException(
//                            "Component ${component.id} has sample $sample, but folder doesn't exist."
//                    )
//                }
//            }
//        }
//    }
//
//    private fun configurateAndroidStandardDependancies(){
//        components.forEach {component ->
//            component.libs.forEach {lib ->
//                lib.androidStandardDependencies.forEach {dependency ->
//
//                }
//            }
//        }
//    }
//
//    private fun checkDependanciesVersion() {
//        val projectProperties = java.util.Properties()
//        println("123123 androidx.annotation:annotation=${project?.property("androidx.annotation:annotation")}")
//    }
//}