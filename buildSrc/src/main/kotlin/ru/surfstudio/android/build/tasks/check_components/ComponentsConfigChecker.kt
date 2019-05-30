package ru.surfstudio.android.build.tasks.check_components

import com.beust.klaxon.Klaxon
import groovy.util.ConfigObject
import groovy.util.ConfigSlurper
import org.gradle.api.GradleException
import ru.surfstudio.android.build.model.*
import java.io.File

/**
 * Class for checking if components configuration has changed
 * Configuration includes: components.json info and values map versions from config.gradle
 * Also values from config.gradle: libMinSdkVersion, targetSdkVersion,moduleVersionCode, compileSdkVersion
 *
 * @param currentRevision current revision of project to check
 * @param revisionToCompare revision of this project to compare with current
 */
class ComponentsConfigChecker(val currentRevision: String,
                              val revisionToCompare: String
) {

    private val tempFolderName = "temp"
    private val outputFolderName = "outputs"
    private val buildFolderName = "build"
    private val currentTaskFolderName = "check-stable-components-changed-task"
    private val tempDir = "$currentDirectory/$tempFolderName"
    private val outputJsonDir = "$currentDirectory/$buildFolderName/$outputFolderName/$currentTaskFolderName/"

    private val componentsJsonFilePath = "/buildSrc/components.json"
    private val configFilePath = "/buildSrc/config.gradle"

    /**
     * compares component configurations
     *
     * @return result of configuration comparision
     */
    fun checkComponentsConfigChanged(): CheckComponentsConfigurationResult {
        createProjectWithRevToCompare()
        createFilesToCompare()
        return compareFiles()
    }

    /**
     * create json files with information about revisions
     */
    private fun createFilesToCompare() {
        createProjectInfoFile(currentDirectory, currentRevision)
        createProjectInfoFile(tempDir, revisionToCompare)
    }

    /**
     * creates temp folder, copies current project to it
     * then in that folder checks out from git revisionToCompare
     */
    private fun createProjectWithRevToCompare() {
        createTempFolder()
        copyProjectToTempFolder()
        checkoutGitRevision(revisionToCompare, tempDir)
    }

    private fun copyProjectToTempFolder() {
        val fileFrom = File(currentDirectory)
        fileFrom.listFiles().forEach { file ->
            if (file.name != tempFolderName) {
                file.copyRecursively(File("$tempDir/${file.name}"), true)
                { _, ioException ->
                    throw GradleException(ioException.message)
                }
            }
        }
    }

    private fun createTempFolder() {
        val tempDirFile = File(tempDir)
        if (tempDirFile.exists()) {
            tempDirFile.deleteRecursively()
        }
        tempDirFile.mkdir()
    }

    private fun checkoutGitRevision(revision: String, directory: String) {
        GitCommandRunner(directory).checkoutRevision(revision)
    }

    private fun createProjectInfoFile(sourceDir: String, revision: String) {
        val projectInfo = createProjectInfo(sourceDir)
        serializeFile(projectInfo, revision)
    }

    private fun createProjectInfo(sourceDir: String): ProjectConfigurationInfo {
        val config = parseConfigFile("$sourceDir$configFilePath")
        val configList = config.getValue("ext") as ConfigObject
        val versions = configList["libraryVersions"] as LinkedHashMap<String, String>
        val libMinSdkVersion = configList["libMinSdkVersion"] as Int
        val targetSdkVersion = configList["targetSdkVersion"] as Int
        val moduleVersionCode = configList["moduleVersionCode"] as Int
        val compileSdkVersion = configList["compileSdkVersion"] as Int


        val compsWithVersions = createComponentsWithVersions(sourceDir, versions)
        return ProjectConfigurationInfo(
                currentRevision,
                compsWithVersions,
                libMinSdkVersion,
                targetSdkVersion,
                moduleVersionCode,
                compileSdkVersion
        )
    }

    private fun createComponentsWithVersions(sourceDir: String, versions: LinkedHashMap<String, String>): List<ComponentWithVersion> {
        val components = parseComponentJson(sourceDir + componentsJsonFilePath)
        val compsWithVersions = components.map { component ->
            val libs = component.libs.map { lib ->
                val standartDeps = lib.androidStandardDependencies.map { dep ->
                    DepWithVersion(dep.name, versions[dep.name] ?: "")
                }
                val thirdPartDeps = lib.thirdPartyDependencies.map { dep ->
                    DepWithVersion(dep.name, versions[dep.name] ?: "")
                }
                LibWithVersion(lib.name, thirdPartDeps, standartDeps)
            }

            ComponentWithVersion(component.id, component.version, component.stable, libs)
        }
        return compsWithVersions
    }

    private fun serializeFile(projectConfigurationInfo: ProjectConfigurationInfo, revision: String) {
        val fileOutputJson = createFolderAndFilesForJson(revision)
        val json = Klaxon().toJsonString(projectConfigurationInfo)

        fileOutputJson.writeText(json)
    }

    private fun createFolderAndFilesForJson(revision: String): File {

        val folderOutputs = File("$currentDirectory/$buildFolderName/$outputFolderName")
        if (!folderOutputs.exists()) folderOutputs.mkdir()
        val folderCurrentTask = File("$outputJsonDir")
        if (!folderCurrentTask.exists()) folderCurrentTask.mkdir()

        val fileOutputJson = File("$outputJsonDir$revision.json")
        if (fileOutputJson.exists()) {
            fileOutputJson.delete()
        }
        fileOutputJson.createNewFile()
        return fileOutputJson
    }

    private fun parseComponentJson(path: String): List<Component> {
        return Klaxon().parseArray(File(path))
                ?: throw RuntimeException("Can't parse components.json")
    }

    private fun parseConfigFile(path: String): ConfigObject {
        return ConfigSlurper().parse(File(path).readText())
    }

    private fun compareFiles(): CheckComponentsConfigurationResult {
        val firstFile = File("$outputJsonDir$currentRevision.json")
        val secondFile = File("$outputJsonDir$revisionToCompare.json")

        val firstProjectInfo = Klaxon().parse<ProjectConfigurationInfo>(firstFile.readText())
        val secondProjectInfo = Klaxon().parse<ProjectConfigurationInfo>(secondFile.readText())

        return ProjectInfosComparator().compareProjectInfos(firstProjectInfo!!, secondProjectInfo!!)
    }
}