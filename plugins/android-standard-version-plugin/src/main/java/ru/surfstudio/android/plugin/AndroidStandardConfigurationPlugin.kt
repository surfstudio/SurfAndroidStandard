package ru.surfstudio.android.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.BiPredicate
import java.util.stream.Collectors

private const val IMPLEMENTATION = "implementation"
private const val STANDARD_EXTENSION_NAME = "androidStandard"
private const val API_STANDARD_CONFIGURATION_NAME = "apiStandard"
private const val IMPLEMENTATION_STANDARD_CONFIGURATION_NAME = "implementationStandard"

/**
 * Plugin provide ability to connect local android-standard in project
 */
class AndroidStandardConfigurationPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create(STANDARD_EXTENSION_NAME, AndroidStandardPluginExtension::class.java)

        project.configurations.create(IMPLEMENTATION_STANDARD_CONFIGURATION_NAME)
        project.configurations.create(API_STANDARD_CONFIGURATION_NAME)

        project.afterEvaluate {
            configureDependencies(it, IMPLEMENTATION_STANDARD_CONFIGURATION_NAME)
            configureDependencies(it, API_STANDARD_CONFIGURATION_NAME)
        }
    }

    private fun configureDependencies(project: Project, configurationName: String) {
        with(project) {
            val extension = extensions.getByName(STANDARD_EXTENSION_NAME) as AndroidStandardPluginExtension

            configurations.findByName(configurationName)
                    ?.dependencies
                    ?.forEach { dependency ->
                        if (extension.useLocal) {
                            val libFile = Files
                                    .find(Paths.get(extension.localPath),
                                            6,
                                            BiPredicate { path, _ ->
                                                path.toString().endsWith("/${dependency.name}.aar")
                                            }
                                    )
                                    .map(Path::toFile)
                                    .collect(Collectors.toList())[0]
                            dependencies.add(IMPLEMENTATION, files(libFile.absolutePath))
                        } else {
                            dependencies.add(IMPLEMENTATION, dependency)
                        }
                    }
        }
    }
}

open class AndroidStandardPluginExtension(
        var useLocal: Boolean = false,
        var localPath: String = ""
)