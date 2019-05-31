package ru.surfstudio.android.build.tasks.changed_components

import groovy.util.ConfigObject
import ru.surfstudio.android.build.model.*

/**
 * Class for creating  Project Configuration Info files and getting [ProjectConfiguration] from files
 */
class ProjectConfigurationCreator(
        private val revision: String,
        directory: String
) {

    private val componentsJsonFilePath = "$directory/buildSrc/components.json"
    private val configFilePath = "$directory/buildSrc/config.gradle"

    fun getProjectConfiguration(): ProjectConfiguration {
        return createProjectInfo()
    }

    private fun createProjectInfo(): ProjectConfiguration {
        val config = ConfigParser.parseConfigFile(configFilePath)
        val configList = config.getValue("ext") as ConfigObject
        val versions = configList["libraryVersions"] as LinkedHashMap<String, String>
        val libMinSdkVersion = configList["libMinSdkVersion"] as Int
        val targetSdkVersion = configList["targetSdkVersion"] as Int
        val moduleVersionCode = configList["moduleVersionCode"] as Int
        val compileSdkVersion = configList["compileSdkVersion"] as Int


        val compsWithVersions = createComponentsWithVersions(versions)

        return ProjectConfiguration(
                revision,
                compsWithVersions,
                libMinSdkVersion,
                targetSdkVersion,
                moduleVersionCode,
                compileSdkVersion
        )
    }

    private fun createComponentsWithVersions(versions: LinkedHashMap<String, String>): List<ComponentForCheck> {
        val components = JsonHelper.parseComponentJson(componentsJsonFilePath).map(Component.Companion::create)
        val compsWithVersions = components.map { component ->
            val libs = component.libraries.map { lib ->
                val standartDeps = lib.androidStandardDependencies.map { dep ->
                    DependencyForCheck(dep.name, versions[dep.name] ?: "")
                }
                val thirdPartDeps = lib.thirdPartyDependencies.map { dep ->
                    DependencyForCheck(dep.name, versions[dep.name] ?: "")
                }
                LibraryForCheck(lib.name, lib.directory, thirdPartDeps, standartDeps)
            }

            ComponentForCheck(component.name, component.directory, component.baseVersion, component.stable, libs)
        }
        return compsWithVersions
    }
}